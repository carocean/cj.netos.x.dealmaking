package cj.netos.x.dealmaking.args;

import java.math.BigDecimal;

public class BidOrderStock {
	String no;
	String orderno;
	String bidder;
	long biddingQuantities;//商品个数
	BigDecimal biddingPrice;
	BigDecimal bondQuantities;//买方通过保证金换取的债量
	long otime;//委托买入时间
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public String getBidder() {
		return bidder;
	}
	public void setBidder(String bidder) {
		this.bidder = bidder;
	}
	public long getBiddingQuantities() {
		return biddingQuantities;
	}
	public void setBiddingQuantities(long biddingQuantities) {
		this.biddingQuantities = biddingQuantities;
	}
	public BigDecimal getBiddingPrice() {
		return biddingPrice;
	}
	public void setBiddingPrice(BigDecimal biddingPrice) {
		this.biddingPrice = biddingPrice;
	}
	public BigDecimal getBondQuantities() {
		return bondQuantities;
	}
	public void setBondQuantities(BigDecimal bondQuantities) {
		this.bondQuantities = bondQuantities;
	}
	public long getOtime() {
		return otime;
	}
	public void setOtime(long otime) {
		this.otime = otime;
	}
	
	
}
