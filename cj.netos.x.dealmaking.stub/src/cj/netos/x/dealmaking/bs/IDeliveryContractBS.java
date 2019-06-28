package cj.netos.x.dealmaking.bs;

import cj.netos.x.dealmaking.args.DeliverContract;

public interface IDeliveryContractBS {
	static String TABLE_Contract="contracts";
	String save(String bank,DeliverContract contract);

}
