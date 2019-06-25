package cj.netos.x.dealmaking.bs;

import java.math.BigDecimal;
import java.util.List;

import cj.netos.x.dealmaking.args.BuyOrderStock;

public interface ICBankBuyOrderQueueBS {
	static String TABLE_queue_buyOrder = "queue.buyorders";

	void offer(String bank, BuyOrderStock buyOrderStock);
	void onevent(IQueueEvent e);
	BuyOrderStock peek(String bank);

	void remove(String bank);

	void remove(String bank, String stockno);

	void updateAmount(String bank, String stockno, BigDecimal amount);

	List<BuyOrderStock> listFiveBuyingWindow(String bank);
}
