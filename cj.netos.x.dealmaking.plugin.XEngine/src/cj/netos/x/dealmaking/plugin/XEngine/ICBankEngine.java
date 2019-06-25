package cj.netos.x.dealmaking.plugin.XEngine;

import cj.netos.x.dealmaking.bs.IQueueEvent;

public interface ICBankEngine {

	IQueueEvent buyOrderQueueEvent();

	IQueueEvent sellOrderQueueEvent();

	void runCBank(String bank);
	void stop();
	
}
