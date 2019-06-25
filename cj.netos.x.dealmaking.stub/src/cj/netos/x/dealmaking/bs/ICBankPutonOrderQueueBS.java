package cj.netos.x.dealmaking.bs;

import java.math.BigDecimal;
import java.util.List;

import cj.netos.x.dealmaking.args.PutonOrderStock;

public interface ICBankPutonOrderQueueBS {
	static String TABLE_queue_putonOrder = "queue.putons";

	void offer(String bank, PutonOrderStock buyOrderStock);
	void onevent(IQueueEvent e);
	PutonOrderStock peek(String bank);

	void remove(String bank);

	void remove(String bank, String stockno);

	void updateAmount(String bank, String stockno, BigDecimal amount);

	List<PutonOrderStock> listFiveBuyingWindow(String bank);
}
