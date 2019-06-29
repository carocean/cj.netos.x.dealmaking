package cj.netos.x.dealmaking.args;

import java.math.BigDecimal;

public class DeliverContract {
	String code;
	String putter;
	String bidder;
	long ctime;
	String putonorderno;
	String bidorderno;
	long thingsQuantities;
	BigDecimal dealPrice;//成交价
	BigDecimal payableAmount;//除了保证金还要应付给对方的资金,当dealType是商品模式时，是竞拍者付款给投放者；当为服务模式时，是投放者付款给竞拍者
	long expiredTimeWin;//交割时间窗
	BigDecimal breakRate;//违约处罚率
	BigDecimal breakAmount;//违约罚金
	DealType dealType;
	public DealType getDealType() {
		return dealType;
	}
	public void setDealType(DealType dealType) {
		this.dealType = dealType;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPutter() {
		return putter;
	}
	public void setPutter(String putter) {
		this.putter = putter;
	}
	public String getBidder() {
		return bidder;
	}
	public void setBidder(String bidder) {
		this.bidder = bidder;
	}
	public long getCtime() {
		return ctime;
	}
	public void setCtime(long ctime) {
		this.ctime = ctime;
	}
	public String getPutonorderno() {
		return putonorderno;
	}
	public void setPutonorderno(String putonorderno) {
		this.putonorderno = putonorderno;
	}
	public String getBidorderno() {
		return bidorderno;
	}
	public void setBidorderno(String bidorderno) {
		this.bidorderno = bidorderno;
	}
	public long getThingsQuantities() {
		return thingsQuantities;
	}
	public void setThingsQuantities(long thingsQuantities) {
		this.thingsQuantities = thingsQuantities;
	}
	public BigDecimal getDealPrice() {
		return dealPrice;
	}
	public void setDealPrice(BigDecimal dealPrice) {
		this.dealPrice = dealPrice;
	}
	public BigDecimal getPayableAmount() {
		return payableAmount;
	}
	public void setPayableAmount(BigDecimal payableAmount) {
		this.payableAmount = payableAmount;
	}
	public long getExpiredTimeWin() {
		return expiredTimeWin;
	}
	public void setExpiredTimeWin(long expiredTimeWin) {
		this.expiredTimeWin = expiredTimeWin;
	}
	public BigDecimal getBreakRate() {
		return breakRate;
	}
	public void setBreakRate(BigDecimal breakRate) {
		this.breakRate = breakRate;
	}
	public BigDecimal getBreakAmount() {
		return breakAmount;
	}
	public void setBreakAmount(BigDecimal breakAmount) {
		this.breakAmount = breakAmount;
	}
	
}
