package cj.netos.x.dealmaking.plugin.XEngine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cj.netos.x.dealmaking.bs.IQueueEvent;
import cj.studio.ecm.IServiceSite;

public class CBankEngine implements ICBankEngine {
	Map<String, Future<?>> futures;
	IQueueEvent buyOrderQueueEvent;
	IQueueEvent sellOrderQueueEvent;
	ExecutorService bankWorks;// 每个线程负责处理一个市场，如果处理完交易则释放线程
	IServiceSite site;

	public CBankEngine(IServiceSite site) {
		futures = new ConcurrentHashMap<String, Future<?>>();
		this.site = site;
		this.bankWorks = Executors.newCachedThreadPool();
		this.buyOrderQueueEvent = createBuyOrderQueueEvent();
		this.sellOrderQueueEvent = createSellOrderQueueEvent();
	}

	@Override
	public IQueueEvent buyOrderQueueEvent() {
		return buyOrderQueueEvent;
	}

	@Override
	public IQueueEvent sellOrderQueueEvent() {
		return sellOrderQueueEvent;
	}

	@Override
	public synchronized void runCBank(String bank) {
		Future<?> f = futures.get(bank);
		if (f == null || f.isCancelled() || f.isDone()) {
			IDealmakingQueueTask task = new DealmakingQueueTask(bank, site);
			f = bankWorks.submit(task);
			futures.put(bank, f);
		}
	}

	@Override
	public void stop() {
		for (String bank : this.futures.keySet()) {
			Future<?> f = this.futures.get(bank);
			if (!f.isDone()) {
				f.cancel(true);
			}
		}
		this.futures.clear();
		this.bankWorks.shutdown();
		this.site = null;
	}

	protected IQueueEvent createSellOrderQueueEvent() {
		return new IQueueEvent() {

			@Override
			public void onevent(String action, Object... args) {
				if("offer".equals(action)) {
					runCBank((String)args[0]);
				}
			}
		};
	}

	protected IQueueEvent createBuyOrderQueueEvent() {
		return new IQueueEvent() {

			@Override
			public void onevent(String action, Object... args) {
				if("offer".equals(action)) {
					runCBank((String)args[0]);
				}
			}
		};
	}
}
