package cj.netos.x.dealmaking.args;
/**
 * 银行政策，从合约银行获取
 * @author caroceanjofers
 *
 */

import java.math.BigDecimal;

public class CBankPolicy {
	BigDecimal breakRate;//交割期间违约的处罚比率
	long expiredTimeWin;//交割时间窗
	public BigDecimal getBreakRate() {
		return breakRate;
	}
	public void setBreakRate(BigDecimal breakRate) {
		this.breakRate = breakRate;
	}
	public long getExpiredTimeWin() {
		return expiredTimeWin;
	}
	public void setExpiredTimeWin(long expiredTimeWin) {
		this.expiredTimeWin = expiredTimeWin;
	}
	
}
