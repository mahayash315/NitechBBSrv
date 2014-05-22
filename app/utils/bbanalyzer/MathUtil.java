package utils.bbanalyzer;

import java.math.BigInteger;

public class MathUtil {

	long N;
	
	public static MathUtil use() {
		return new MathUtil();
	}

	public BigInteger factorial(int n) {
		if (n < 2) {
			return BigInteger.ONE;
		}
		BigInteger p = BigInteger.ONE, r = BigInteger.ONE;
		N = 1;
		int log2n = 31 - Integer.numberOfLeadingZeros(n);
		int h = 0, shift = 0, high = 1;
		while (h != n) {
			shift += h;
			h = n >>> log2n--;
			int len = high;
			high = (h & 1) == 1 ? h : h - 1;
			len = (high - len) >> 1;
			if (len > 0) {
				r = r.multiply((p = p.multiply(bp(len))));
			}
		}
		return r.shiftLeft(shift);
	}

	private BigInteger bp(int n) {
		int m = n >> 1;
		if (m == 0) {
			return BigInteger.valueOf(N += 2);
		} else if (n == 2) {
			return BigInteger.valueOf(N += 2).multiply(BigInteger.valueOf(N += 2));
		}
		return bp(n - m).multiply(bp(m));
	}
}
