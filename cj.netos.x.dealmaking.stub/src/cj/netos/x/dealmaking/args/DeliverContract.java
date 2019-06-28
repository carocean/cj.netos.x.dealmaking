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
	BigDecimal demandAmount;//需要再交的资金
	long expiredTimeWin;//交割时间窗
	BigDecimal breakRate;//违约处罚率
	BigDecimal breakAmount;//违约罚金
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
	public BigDecimal getDemandAmount() {
		return demandAmount;
	}
	public void setDemandAmount(BigDecimal demandAmount) {
		this.demandAmount = demandAmount;
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
