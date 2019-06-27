package cj.netos.x.dealmaking.plugin.XEngine;

import java.util.List;

import cj.netos.x.dealmaking.bs.ICBankBidOrderQueueBS;
import cj.netos.x.dealmaking.bs.ICBankInitializer;
import cj.netos.x.dealmaking.bs.ICBankPutonOrderQueueBS;
import cj.studio.ecm.IChip;
import cj.studio.ecm.IEntryPointActivator;
import cj.studio.ecm.IServiceSite;
import cj.studio.ecm.context.IElement;

public class DealmakingEngineEntrypointActivitor implements IEntryPointActivator {
	ICBankEngine engine;
	
	@Override
	public void activate(IServiceSite site, IElement args) {
		engine=new CBankEngine(site);
		ICBankBidOrderQueueBS cbankBidOrderQueueBS = (ICBankBidOrderQueueBS) site.getService("cbankBidOrderQueueBS");
		ICBankPutonOrderQueueBS cbankPutonOrderQueueBS = (ICBankPutonOrderQueueBS) site.getService("cbankPutonOrderQueueBS");
		cbankBidOrderQueueBS.onevent(engine.bidOrderQueueEvent());
		cbankPutonOrderQueueBS.onevent(engine.putonOrderQueueEvent());
		
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
