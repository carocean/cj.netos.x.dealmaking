package cj.netos.x.dealmaking.bs;

import java.util.List;

import cj.netos.x.dealmaking.args.SellOrderStock;

public interface ICBankSellOrderQueueBS {
	static String TABLE_queue_sellOrder = "queue.sellorders";

	void onevent(IQueueEvent e);
	void offer(String bank, SellOrderStock sellOrderStock);

	SellOrderStock peek(String bank);

	void remove(String bank);

	void remove(String bank, String stockno);


	List<SellOrderStock> listFiveSellingWindow(String bank);

	
}
