package cj.netos.x.dealmaking.plugin.XEngine.bs;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.model.UpdateOptions;

import cj.lns.chip.sos.cube.framework.IDocument;
import cj.lns.chip.sos.cube.framework.IQuery;
import cj.lns.chip.sos.cube.framework.TupleDocument;
import cj.netos.x.dealmaking.args.BidOrderStock;
import cj.netos.x.dealmaking.bs.ICBankBidOrderQueueBS;
import cj.netos.x.dealmaking.bs.ICBankInitializer;
import cj.netos.x.dealmaking.bs.IQueueEvent;
import cj.netos.x.dealmaking.plugin.XEngine.db.ICBankStore;
import cj.studio.ecm.CJSystem;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.ultimate.gson2.com.google.gson.Gson;

@CjService(name = "cbankBidOrderQueueBS")
public class CBankBidOrderQueueBS implements ICBankBidOrderQueueBS {
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
	public void offer(String bank, BidOrderStock bidOrderStock) {
		if (!cbankInitializer.isCbankInitialized(bank)) {
			String result = cbankStore.bank(bank).createIndex(TABLE_queue_bidOrder,
					Document.parse("{'tuple.biddingPrice':-1,'tuple.otime':1}"));// 价格降、委托时间降，意为：最高价且越早的越优先成交，并且价格优先于时间
			cbankInitializer.setCbankInitialized(bank);
			CJSystem.logging().debug(getClass(), String.format("银行：%s 已建立索引：%s", bank, result));
			if (event != null) {
				event.onevent("bankInitialized", bank);
			}
		}
		String id = cbankStore.bank(bank).saveDoc(TABLE_queue_bidOrder, new TupleDocument<>(bidOrderStock));
		bidOrderStock.setNo(id);
		if (event != null) {
			event.onevent("offer", bank, id);
		}
	}

	@Override
	public BidOrderStock peek(String bank) {
		String cjql = String.format(
				"select {'tuple':'*'}.sort({'tuple.biddingPrice':-1,'tuple.otime':1}).skip(0).limit(1) from tuple %s %s where {}",
				TABLE_queue_bidOrder, BidOrderStock.class.getName(), bank);
		IQuery<BidOrderStock> q = cbankStore.bank(bank).createQuery(cjql);
		IDocument<BidOrderStock> doc = q.getSingleResult();
		if (doc == null)
			return null;
		doc.tuple().setNo(doc.docid());
		return doc.tuple();
	}

	@Override
	public void remove(String bank) {
		BidOrderStock stock = peek(bank);
		if (stock == null) {
			return;
		}
		remove(bank, stock.getNo());
	}

	@Override
	public void remove(String bank, String stockno) {
		cbankStore.bank(bank).deleteDocOne(TABLE_queue_bidOrder, String.format("{'_id':ObjectId('%s')}", stockno));
	}

	@Override
	public void updateQuantities(String bank, String stockno, long quantities) {
		Bson filter = Document.parse(String.format("{'_id':ObjectId('%s')}", stockno));
		Bson update = Document.parse(String.format("{'$set':{'tuple.biddingQuantities':%s}}", new Gson().toJson(quantities)));
		UpdateOptions op = new UpdateOptions();
		op.upsert(false);
		cbankStore.bank(bank).updateDocOne(TABLE_queue_bidOrder, filter, update, op);
	}

	@Override
	public List<BidOrderStock> listFiveBiddingWindow(String bank) {
		String cjql = String.format(
				"select {'tuple':'*'}.sort({'tuple.biddingPrice':-1,'tuple.otime':1}).skip(0).limit(5) from tuple %s %s where {}",
				TABLE_queue_bidOrder, BidOrderStock.class.getName(), bank);
		IQuery<BidOrderStock> q = cbankStore.bank(bank).createQuery(cjql);
		List<IDocument<BidOrderStock>> docs = q.getResultList();
		List<BidOrderStock> list = new ArrayList<>();
		for (IDocument<BidOrderStock> doc : docs) {
			BidOrderStock stock = doc.tuple();
			stock.setNo(doc.docid());
			list.add(stock);
		}
		return list;
	}

}
