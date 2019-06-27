package cj.netos.x.dealmaking.stub;

import cj.netos.x.dealmaking.args.BidOrderStock;
import cj.netos.x.dealmaking.args.PutonOrderStock;
import cj.studio.ecm.net.CircuitException;
import cj.studio.gateway.stub.annotation.CjStubCircuitStatusMatches;
import cj.studio.gateway.stub.annotation.CjStubInContentKey;
import cj.studio.gateway.stub.annotation.CjStubInParameter;
import cj.studio.gateway.stub.annotation.CjStubMethod;
import cj.studio.gateway.stub.annotation.CjStubService;

@CjStubService(bindService = "/queue.service",usage = "交割队列")
public interface IDeliveryQueueStub {
	@CjStubMethod(command = "post", usage = "向买方队列添加")
	@CjStubCircuitStatusMatches(status = { "503 error.", "500 error.", "404 not found." })
	void biddingQueue(@CjStubInParameter(key = "bank", usage = "银行编号") String bank,
			@CjStubInContentKey(key = "stock", usage = "委托买单存量") BidOrderStock stock)
			throws CircuitException;

	@CjStubMethod(command = "post", usage = "向卖方队列添加")
	@CjStubCircuitStatusMatches(status = { "503 error.", "500 error.", "404 not found." })
	void putonQueue(@CjStubInParameter(key = "bank", usage = "银行编号") String bank,
			@CjStubInContentKey(key = "stock", usage = "委托投单存量") PutonOrderStock stock)
			throws CircuitException;
}
