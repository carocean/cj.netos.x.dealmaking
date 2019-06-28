package cj.netos.x.dealmaking.plugin.XEngine.bs;

import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.netos.x.dealmaking.args.DeliverContract;
import cj.netos.x.dealmaking.bs.IDeliveryContractBS;
import cj.netos.x.dealmaking.plugin.XEngine.db.ICBankStore;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
@CjService(name="deliveryContractBS")
public class DeliveryContractBS implements IDeliveryContractBS {
	@CjServiceRef
	ICBankStore cbankStore;
	@Override
	public String save(String bank,DeliverContract contract) {
		cbankStore.bank(bank).saveDoc(TABLE_Contract, new TupleDocument<>(contract));
		return null;
	}

}
