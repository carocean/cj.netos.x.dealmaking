package cj.netos.x.dealmaking.plugin.XEngine;

import cj.netos.x.dealmaking.bs.IQueueEvent;

public interface ICBankEngine {

	IQueueEvent bidOrderQueueEvent();


	IQueueEvent putonOrderQueueEvent();

	void runCBank(String bank);

	void stop();

}
