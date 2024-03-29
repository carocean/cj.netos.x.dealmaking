package cj.netos.x.dealmaking.bs;

import java.util.List;

import cj.netos.x.dealmaking.args.CBankPolicy;

public interface ICBankInitializer {
	static String TABLE_engine_info="engine.info";
	boolean isCbankInitialized(String bank);
	void setCbankInitialized(String bank);
	List<String> pageCBank(String dealmakingEngineID);
	CBankPolicy getPolicy(String bank);
}
