package cj.netos.x.dealmaking.bs;

import java.util.List;

import cj.netos.x.dealmaking.args.PutonOrderStock;

public interface ICBankPutonOrderQueueBS {
	static String TABLE_queue_putonOrder = "queue.puts";

	void offer(String bank, PutonOrderStock stock);
	void onevent(IQueueEvent e);
	PutonOrderStock peek(String bank);

	void remove(String bank);

	void remove(String bank, String stockno);

	void updateQuantities(String bank, String stockno, long quantities);

	List<PutonOrderStock> listFivePuttingWindow(String bank);
}
