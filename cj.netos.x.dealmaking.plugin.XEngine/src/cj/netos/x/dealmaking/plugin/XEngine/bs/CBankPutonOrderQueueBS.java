package cj.netos.x.dealmaking.plugin.XEngine.bs;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.model.UpdateOptions;

import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.netos.x.dealmaking.args.PutonOrderStock;
import cj.netos.x.dealmaking.bs.ICBankInitializer;
import cj.netos.x.dealmaking.bs.ICBankPutonOrderQueueBS;
import cj.netos.x.dealmaking.bs.IQueueEvent;
import cj.netos.x.dealmaking.plugin.XEngine.db.ICBankStore;
import cj.studio.ecm.CJSystem;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.ultimate.gson2.com.google.gson.Gson;

@CjService(name = "cbankPutonOrderQueueBS")
public class CBankPutonOrderQueueBS implements ICBankPutonOrderQueueBS {
	@CjServiceRef
	ICBankStore cbankStore;
	@CjServiceRef
	ICBankInitializer cbankInitializer;
	IQueueEvent event;

	@Override
	public void onevent(IQueueEvent e) {
		event = e;
	}

	@Override
	public void offer(String bank, PutonOrderStock stock) {
		if (!cbankInitializer.isCbankInitialized(bank)) {
			String result = cbankStore.bank(bank).createIndex(TABLE_queue_putonOrder,
					Document.parse("{'tuple.puttingPrice':1,'tuple.otime':1}"));// 价格升序、委托时间升序，意为：最低价且越早的越优先成交，并且价格优先于时间
			cbankInitializer.setCbankInitialized(bank);
			CJSystem.logging().debug(getClass(), String.format("银行：%s 已建立索引：%s", bank, result));
			if (event != null) {
				event.onevent("bankInitialized", bank);
			}
		}
		String id = cbankStore.bank(bank).saveDoc(TABLE_queue_putonOrder, new TupleDocument<>(stock));
		stock.setNo(id);
		if (event != null) {
			event.onevent("offer", bank, id);
		}

	}

	@Override
	public PutonOrderStock peek(String bank) {
		String cjql = String.format(
				"select {'tuple':'*'}.sort({'tuple.puttingPrice':1,'tuple.otime':1}).skip(0).limit(1) from tuple %s %s where {}",
				TABLE_queue_putonOrder, PutonOrderStock.class.getName(), bank);
		IQuery<PutonOrderStock> q = cbankStore.bank(bank).createQuery(cjql);
		IDocument<PutonOrderStock> doc = q.getSingleResult();
		if (doc == null)
			return null;
		doc.tuple().setNo(doc.docid());
		return doc.tuple();
	}

	@Override
	public void remove(String bank) {
		PutonOrderStock stock = peek(bank);
		if (stock == null) {
			return;
		}
		remove(bank, stock.getNo());
	}

	@Override
	public void remove(String bank, String no) {
		cbankStore.bank(bank).deleteDocOne(TABLE_queue_putonOrder, String.format("{'_id':ObjectId('%s')}", no));
	}

	@Override
	public void updateQuantities(String bank, String stockno, long quantities) {
		Bson filter = Document.parse(String.format("{'_id':ObjectId('%s')}", stockno));
		Bson update = Document.parse(String.format("{'$set':{'tuple.puttingQuantities':%s}}", new Gson().toJson(quantities)));
		UpdateOptions op = new UpdateOptions();
		op.upsert(false);
		cbankStore.bank(bank).updateDocOne(TABLE_queue_putonOrder, filter, update, op);
	}

	@Override
	public List<PutonOrderStock> listFivePuttingWindow(String bank) {
		String cjql = String.format(
				"select {'tuple':'*'}.sort({'tuple.puttingPrice':1,'tuple.otime':1}).skip(0).limit(5) from tuple %s %s where {}",
				TABLE_queue_putonOrder, PutonOrderStock.class.getName(), bank);
		IQuery<PutonOrderStock> q = cbankStore.bank(bank).createQuery(cjql);
		List<IDocument<PutonOrderStock>> docs = q.getResultList();
		List<PutonOrderStock> list = new ArrayList<>();
		for (IDocument<PutonOrderStock> doc : docs) {
			PutonOrderStock stock = doc.tuple();
			stock.setNo(doc.docid());
			list.add(stock);
		}
		return list;
	}

}
