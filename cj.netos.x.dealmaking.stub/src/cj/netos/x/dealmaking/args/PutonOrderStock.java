package cj.netos.x.dealmaking.args;

import java.math.BigDecimal;

public class PutonOrderStock {
	String no;//存量号
	String orderno;//投放单号
	String putter;
	DealType dealType;//卖方或采购方
	long puttingQuantities;//商品个数
	BigDecimal puttingPrice;//想卖的价格
	BigDecimal cashDepositRate;// 保证金率
	BigDecimal bondQuantities;//投放者通过保证金换取的债量
	long otime;//委托时间
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public BigDecimal getCashDepositRate() {
		return cashDepositRate;
	}
	public void setCashDepositRate(BigDecimal cashDepositRate) {
		this.cashDepositRate = cashDepositRate;
	}
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	
	public String getPutter() {
		return putter;
	}
	public void setPutter(String putter) {
		this.putter = putter;
	}
	public DealType getDealType() {
		return dealType;
	}
	public void setDealType(DealType dealType) {
		this.dealType = dealType;
	}
	public long getPuttingQuantities() {
		return puttingQuantities;
	}
	public void setPuttingQuantities(long puttingQuantities) {
		this.puttingQuantities = puttingQuantities;
	}
	public BigDecimal getPuttingPrice() {
		return puttingPrice;
	}
	public void setPuttingPrice(BigDecimal puttingPrice) {
		this.puttingPrice = puttingPrice;
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
