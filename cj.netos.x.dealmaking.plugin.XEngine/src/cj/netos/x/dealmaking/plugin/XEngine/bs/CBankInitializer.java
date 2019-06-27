package cj.netos.x.dealmaking.plugin.XEngine.bs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.netos.x.dealmaking.args.EngineInfo;
import cj.netos.x.dealmaking.bs.ICBankInitializer;
import cj.netos.x.dealmaking.plugin.XEngine.db.ICBankStore;
import cj.studio.ecm.IChip;
import cj.studio.ecm.IServiceSite;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.annotation.CjServiceSite;
@CjService(name="cbankInitializer")
public class CBankInitializer implements ICBankInitializer {
	@CjServiceRef
	ICBankStore cbankStore;
	@CjServiceSite
	IServiceSite site;
	@Override
	public boolean isCbankInitialized(String bank) {
		String cjql = String.format("select {'tuple.isInitialized':1} from tuple %s %s where {'tuple.bank':'%s'}",
				TABLE_engine_info, HashMap.class.getName(), bank);
		IQuery<HashMap<String, Object>> q = cbankStore.home().createQuery(cjql);
		IDocument<HashMap<String, Object>> doc = q.getSingleResult();
		if (doc == null || doc.tuple() == null) {
			return false;
		}
		Object v = doc.tuple().get("isInitialized");
		if (v == null) {
			return false;
		}
		return (boolean) v;
	}

	@Override
	public void setCbankInitialized(String bank) {
		IChip chip = (IChip) site.getService(IChip.class.getName());
		String dealmakingEngineID = chip.info().getId();
		EngineInfo info = new EngineInfo();
		info.setDealmakingEngineID(dealmakingEngineID);
		info.setInitialized(true);
		info.setBank(bank);

		cbankStore.home().saveDoc(TABLE_engine_info, new TupleDocument<>(info));

	}

	@Override
	public List<String> pageCBank(String dealmakingEngineID) {
		String cjql = String.format("select {'tuple.bank':1} from tuple %s %s where {'tuple.dealmakingEngineID':'%s'}",
				TABLE_engine_info, HashMap.class.getName(), dealmakingEngineID);
		IQuery<HashMap<String, String>> q = cbankStore.home().createQuery(cjql);
		List<IDocument<HashMap<String, String>>> docs = q.getResultList();
		List<String> list=new ArrayList<>();
		for(IDocument<HashMap<String, String>> doc:docs) {
			HashMap<String, String> obj=doc.tuple();
			list.add(obj.get("bank"));
		}
		return list;
	}

}
