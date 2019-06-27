package cj.netos.x.dealmaking.plugin.XEngine;

import java.math.BigDecimal;

import cj.netos.x.dealmaking.args.Actor;
import cj.netos.x.dealmaking.args.BidOrderStock;
import cj.netos.x.dealmaking.args.PutonOrderStock;
import cj.netos.x.dealmaking.bs.ICBankBidOrderQueueBS;
import cj.netos.x.dealmaking.bs.ICBankPutonOrderQueueBS;
import cj.netos.x.dealmaking.util.BigDecimalConstants;
import cj.studio.ecm.CJSystem;
import cj.studio.ecm.IServiceSite;

//撮合一个银行的任务，撮合完则任务退出
public class DealmakingQueueTask implements IDealmakingQueueTask {
	String cbank;
	IServiceSite site;
	private ICBankBidOrderQueueBS cbankBidOrderQueueBS;
	private ICBankPutonOrderQueueBS cbankPutonOrderQueueBS;

	public DealmakingQueueTask(String bank, IServiceSite site) {
		this.cbank = bank;
		this.site = site;
		this.cbankBidOrderQueueBS = (ICBankBidOrderQueueBS) site.getService("cbankBidOrderQueueBS");
		this.cbankPutonOrderQueueBS = (ICBankPutonOrderQueueBS) site.getService("cbankPutonOrderQueueBS");
	}

	@Override
	public void run() {
		/*
		 * 投放价（原价在变）、成交价（含奖金） 放弃期货算法，因为太复杂以至于一般的交易者不会玩。 本实现支持供应与采购的买卖
		 */
		CJSystem.logging().info(getClass(), "发现银行：" + cbank);
		while (!Thread.interrupted()) {
			PutonOrderStock sell = this.cbankPutonOrderQueueBS.peek(cbank);
			if (sell == null) {
				CJSystem.logging().info(getClass(), "\t\t投放队列为空");
				break;
			}
			BidOrderStock bid = this.cbankBidOrderQueueBS.peek(cbank);
			if (bid == null) {
				CJSystem.logging().info(getClass(), "\t\t买入队列为空");
				break;
			}
			// 具有两种价格：投放价与现时卖价
			// 供应算法：现卖价=投放价-奖金/数量；采购算法：现卖价=投放价+奖金/数量；
			// TODO 下一步取出奖金
			BigDecimal rewardAmount = new BigDecimal("0.00");// 现时奖金,
			BigDecimal nowSellingPrice = null;
			if (Actor.seller == sell.getActor()) {
				nowSellingPrice = sell.getPuttingPrice()
						.subtract(rewardAmount.divide(new BigDecimal(sell.getPuttingQuantities() + ""),
								BigDecimalConstants.scale, BigDecimalConstants.roundingMode));
				if (bid.getBiddingPrice().compareTo(nowSellingPrice) < 0) {
					CJSystem.logging().info(getClass(), "\t\t未达到交易条件");
					break;
				}
			} else {
				nowSellingPrice = sell.getPuttingPrice()
						.add(rewardAmount.divide(new BigDecimal(sell.getPuttingQuantities() + ""),
								BigDecimalConstants.scale, BigDecimalConstants.roundingMode));
				if (bid.getBiddingPrice().compareTo(nowSellingPrice) > 0) {
					CJSystem.logging().info(getClass(), "\t\t未达到交易条件");
					break;
				}
			}
			
			// 该市场有撮合交易，则进入撮合线程执行
			CJSystem.logging().info(getClass(), "\t\t进入撮合程序");
			try {
				deliveryContract(bid, sell, nowSellingPrice);
			} catch (Exception e) {
				CJSystem.logging().error(getClass(), e);
			}
		}
	}

	/*
	 * 交割合约
	 */
	protected void deliveryContract(BidOrderStock bid, PutonOrderStock sell, BigDecimal nowSellingPrice) {
		System.out.println("......");
	}

}
