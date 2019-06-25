package cj.netos.x.dealmaking.plugin.XEngine;

import java.util.List;

import cj.netos.x.dealmaking.bs.ICBankBuyOrderQueueBS;
import cj.netos.x.dealmaking.bs.ICBankInitializer;
import cj.netos.x.dealmaking.bs.ICBankSellOrderQueueBS;
import cj.studio.ecm.IChip;
import cj.studio.ecm.IEntryPointActivator;
import cj.studio.ecm.IServiceSite;
import cj.studio.ecm.context.IElement;

public class DealmakingEngineEntrypointActivitor implements IEntryPointActivator {
	ICBankEngine engine;
	
	@Override
	public void activate(IServiceSite site, IElement args) {
		engine=new CBankEngine(site);
		ICBankBuyOrderQueueBS marketBuyOrderQueueBS = (ICBankBuyOrderQueueBS) site.getService("cbankBuyOrderQueueBS");
		ICBankSellOrderQueueBS marketSellOrderQueueBS = (ICBankSellOrderQueueBS) site.getService("cbankSellOrderQueueBS");
		marketBuyOrderQueueBS.onevent(engine.buyOrderQueueEvent());
		marketSellOrderQueueBS.onevent(engine.sellOrderQueueEvent());
		
		IChip chip = (IChip) site.getService(IChip.class.getName());
		String dealmakingEngineID = chip.info().getId();
		ICBankInitializer bankInitializer = (ICBankInitializer) site.getService("cbankInitializer");
		List<String> cbanksInDB = bankInitializer.pageCBank(dealmakingEngineID);
		for (String cbank : cbanksInDB) {
			engine.runCBank(cbank);
		}
	}

	@Override
	public void inactivate(IServiceSite site) {
		engine.stop();
	}

}
