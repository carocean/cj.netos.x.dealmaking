package cj.netos.x.dealmaking.plugin.XEngine;

import java.math.BigDecimal;

import cj.netos.x.dealmaking.args.DealType;
import cj.netos.x.dealmaking.args.BidOrderStock;
import cj.netos.x.dealmaking.args.CBankPolicy;
import cj.netos.x.dealmaking.args.DeliverContract;
import cj.netos.x.dealmaking.args.PutonOrderStock;
import cj.netos.x.dealmaking.bs.ICBankBidOrderQueueBS;
import cj.netos.x.dealmaking.bs.ICBankInitializer;
import cj.netos.x.dealmaking.bs.ICBankPutonOrderQueueBS;
import cj.netos.x.dealmaking.bs.IDeliveryContractBS;
import cj.studio.ecm.CJSystem;
import cj.studio.ecm.IServiceSite;

//撮合一个银行的任务，撮合完则任务退出
public class DealmakingQueueTask implements IDealmakingQueueTask {
	String cbank;
	IServiceSite site;
	private ICBankBidOrderQueueBS cbankBidOrderQueueBS;
	private ICBankPutonOrderQueueBS cbankPutonOrderQueueBS;
	IDeliveryContractBS deliveryContractBS;
	ICBankInitializer cbankInitializer;

	public DealmakingQueueTask(String bank, IServiceSite site) {
		this.cbank = bank;
		this.site = site;
		this.cbankBidOrderQueueBS = (ICBankBidOrderQueueBS) site.getService("cbankBidOrderQueueBS");
		this.cbankPutonOrderQueueBS = (ICBankPutonOrderQueueBS) site.getService("cbankPutonOrderQueueBS");
		this.deliveryContractBS = (IDeliveryContractBS) site.getService("deliveryContractBS");
		cbankInitializer = (ICBankInitializer) site.getService("cbankInitializer");
	}

	@Override
	public void run() {
		/*
		 * 投放价（原价在变）、成交价（含奖金） 放弃期货算法，因为太复杂以至于一般的交易者不会玩。 本实现支持供应与采购的买卖
		 */
		CJSystem.logging().info(getClass(), "发现银行：" + cbank);
		while (!Thread.interrupted()) {
			PutonOrderStock puton = this.cbankPutonOrderQueueBS.peek(cbank);
			if (puton == null) {
				CJSystem.logging().info(getClass(), "\t\t投放队列为空");
				break;
			}
			BidOrderStock bid = this.cbankBidOrderQueueBS.peek(cbank);
			if (bid == null) {
				CJSystem.logging().info(getClass(), "\t\t买入队列为空");
				break;
			}
			// 具有两种价格：投放价与现时卖价
			// 供应算法：现卖价=投放价-奖金；采购算法：现卖价=投放价+奖金；
			// TODO 下一步取出奖金
			BigDecimal rewardAmount = new BigDecimal("0.00");// 现时奖金,
			BigDecimal nowSellingPrice = null;
			if (DealType.goods == puton.getDealType()) {
				nowSellingPrice = puton.getPuttingPrice().subtract(rewardAmount);
				if (bid.getBiddingPrice().compareTo(nowSellingPrice) < 0) {
					CJSystem.logging().info(getClass(), "\t\t未达到交易条件");
					break;
				}
				// 该市场有撮合交易，则进入撮合线程执行
				CJSystem.logging().info(getClass(), "\t\t进入撮合程序");
				try {
					deliverySellerContract(bid, puton, nowSellingPrice);
				} catch (Exception e) {
					CJSystem.logging().error(getClass(), e);
				}
			} else if (DealType.services == puton.getDealType()) {
				nowSellingPrice = puton.getPuttingPrice().add(rewardAmount);
				if (bid.getBiddingPrice().compareTo(nowSellingPrice) > 0) {
					CJSystem.logging().info(getClass(), "\t\t未达到交易条件");
					break;
				}
				// 该市场有撮合交易，则进入撮合线程执行
				CJSystem.logging().info(getClass(), "\t\t进入撮合程序");
				try {
					deliveryPurchaserContract(bid, puton, nowSellingPrice);
				} catch (Exception e) {
					CJSystem.logging().error(getClass(), e);
				}
			} else {
				CJSystem.logging().error(getClass(), "\t\t仅支持商品和服务两类交易，该投单是：" + puton.getDealType());
			}

		}
	}

	/**
	 * 交割供应合约（卖场+）
	 */
	protected void deliverySellerContract(BidOrderStock bid, PutonOrderStock put, BigDecimal nowSellingPrice) {
		DeliverContract contract = new DeliverContract();
		contract.setBidder(bid.getBidder());
		contract.setBidorderno(bid.getNo());
		contract.setCtime(System.currentTimeMillis());
		contract.setDealPrice(nowSellingPrice);
		contract.setPutonorderno(put.getNo());
		contract.setPutter(put.getPutter());
		// 每次成交一个商品，因此：投单扣1个，竞单扣1个。原因是：奖金每次用于1个商品降价是最快的
		long subtractQuantities = 1;
		contract.setThingsQuantities(subtractQuantities);// 购买的数量
		// =现价-竞拍申购价*保证金率
		BigDecimal demandAmount = nowSellingPrice.subtract(bid.getBiddingPrice().multiply(bid.getCashDepositRate()));
		contract.setDemandAmount(demandAmount);// 要补交的款

		CBankPolicy policy = this.cbankInitializer.getPolicy(cbank);
		if (policy != null) {
			contract.setBreakRate(policy.getBreakRate());// 违约金率
			contract.setExpiredTimeWin(policy.getExpiredTimeWin());
		}
		String code = this.deliveryContractBS.save(this.cbank, contract);
		contract.setCode(code);

		bid.setBiddingQuantities(bid.getBiddingQuantities() - subtractQuantities);
		put.setPuttingQuantities(put.getPuttingQuantities() - subtractQuantities);
		if (bid.getBiddingQuantities() <= 0) {
			this.cbankBidOrderQueueBS.remove(cbank, bid.getNo());
		} else {
			this.cbankBidOrderQueueBS.updateQuantities(cbank, bid.getNo(), bid.getBiddingQuantities());
		}
		if (put.getPuttingQuantities() <= 0) {
			cbankPutonOrderQueueBS.remove(cbank, put.getNo());
		} else {
			this.cbankPutonOrderQueueBS.updateQuantities(cbank, put.getNo(), put.getPuttingQuantities());
		}

	}

	/**
	 * 交割采购合约（服务厅+）
	 */
	protected void deliveryPurchaserContract(BidOrderStock bid, PutonOrderStock sell, BigDecimal nowSellingPrice) {
		// TODO Auto-generated method stub

	}
}
