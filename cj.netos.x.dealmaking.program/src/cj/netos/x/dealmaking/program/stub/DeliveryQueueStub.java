package cj.netos.x.dealmaking.program.stub;

import cj.netos.x.dealmaking.args.BidOrderStock;
import cj.netos.x.dealmaking.args.PutonOrderStock;
import cj.netos.x.dealmaking.bs.ICBankBidOrderQueueBS;
import cj.netos.x.dealmaking.bs.ICBankPutonOrderQueueBS;
import cj.netos.x.dealmaking.stub.IDeliveryQueueStub;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.gateway.stub.GatewayAppSiteRestStub;

@CjService(name = "/queue.service")
public class DeliveryQueueStub extends GatewayAppSiteRestStub implements IDeliveryQueueStub {
	@CjServiceRef(refByName = "XEngine.cbankPutonOrderQueueBS")
	ICBankPutonOrderQueueBS cbankPutonOrderQueueBS;
	@CjServiceRef(refByName = "XEngine.cbankBidOrderQueueBS")
	ICBankBidOrderQueueBS cbankBidOrderQueueBS;

	@Override
	public void biddingQueue(String bank, BidOrderStock stock) throws CircuitException {
		cbankBidOrderQueueBS.offer(bank, stock);
	}

	@Override
	public void putonQueue(String bank, PutonOrderStock stock) throws CircuitException {
		cbankPutonOrderQueueBS.offer(bank, stock);
	}

}
