package cj.netos.x.dealmaking.bs;

import java.util.List;

import cj.netos.x.dealmaking.args.BidOrderStock;

public interface ICBankBidOrderQueueBS {
	static String TABLE_queue_bidOrder = "queue.bids";

	void offer(String bank, BidOrderStock stock);
	void onevent(IQueueEvent e);
	BidOrderStock peek(String bank);

	void remove(String bank);

	void remove(String bank, String stockno);

	void updateQuantities(String bank, String stockno, long quantities);

	List<BidOrderStock> listFiveBiddingWindow(String bank);
}
