package cj.netos.x.dealmaking.plugin.XEngine.db;

import java.util.HashMap;
import java.util.Map;

import cj.lns.chip.sos.cube.framework.ICube;
import cj.studio.ecm.IServiceSite;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.annotation.CjServiceSite;

@CjService(name = "cbankStore")
public class CBankStore implements ICBankStore {
	@CjServiceSite
	IServiceSite site;
	@CjServiceRef(refByName = "mongodb.cbank.home")
	ICube home;
	Map<String, ICube> banks;

	public CBankStore() {
		banks = new HashMap<String, ICube>();
	}

	@Override
	public synchronized ICube bank(String market) {
		if (banks.containsKey(market)) {
			return banks.get(market);
		}
		return (ICube) site.getService("mongodb.cbank." + market + ":autocreate");
	}

	@Override
	public synchronized ICube home() {
		return home;
	}
}
