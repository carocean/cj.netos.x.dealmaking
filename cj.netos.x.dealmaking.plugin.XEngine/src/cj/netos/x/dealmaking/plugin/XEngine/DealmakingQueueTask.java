package cj.netos.x.dealmaking.plugin.XEngine;

import cj.netos.x.dealmaking.bs.ICBankBuyOrderQueueBS;
import cj.netos.x.dealmaking.bs.ICBankPutonOrderQueueBS;
import cj.netos.x.dealmaking.bs.ICBankSellOrderQueueBS;
import cj.studio.ecm.IServiceSite;

//撮合一个银行的任务，撮合完则任务退出
public class DealmakingQueueTask implements IDealmakingQueueTask {
	String cbank;
	IServiceSite site;
	private ICBankBuyOrderQueueBS cbankBuyOrderQueueBS;
	private ICBankSellOrderQueueBS cbankSellOrderQueueBS;
	private ICBankPutonOrderQueueBS cbankPutonOrderQueueBS;

	public DealmakingQueueTask(String bank, IServiceSite site) {
		this.cbank = bank;
		this.site = site;
		this.cbankBuyOrderQueueBS = (ICBankBuyOrderQueueBS) site.getService("cbankBuyOrderQueueBS");
		this.cbankSellOrderQueueBS = (ICBankSellOrderQueueBS) site.getService("cbankSellOrderQueueBS");
		this.cbankPutonOrderQueueBS = (ICBankPutonOrderQueueBS) site.getService("cbankPutonOrderQueueBS");
	}

	@Override
	public void run() {

	}

}
