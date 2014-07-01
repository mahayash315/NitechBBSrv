package utils.bbanalyzer;

import java.math.BigInteger;

public class MathUtil {

	static MathUtil mathUtil;
	long N;
	
	public static MathUtil use() {
		if (mathUtil == null) {
			mathUtil = new MathUtil();
		}
		return mathUtil;
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
	
	
	// --------------------------------------------------------------------------------------
	// vector
	// --------------------------------------------------------------------------------------
	
	public static void vectorAdd(double[] dst, double[] v) throws IllegalArgumentException {
		if (dst.length != v.length) {
			throw new IllegalArgumentException("v1.length != v2.length");
		}
		for(int i = 0; i < dst.length; ++i) {
			dst[i] = dst[i] + v[i];
		}
	}
	
	public static double vectorMultiply(double[] v1, double[] v2) throws IllegalArgumentException {
		if (v1.length != v2.length) {
			throw new IllegalArgumentException("v1.length != v2.length");
		}
		double res = 0;
		for(int i = 0; i < v1.length; ++i) {
			res = res + (v1[i] * v2[i]);
		}
		return res;
	}
	
	public static void vectorDivide(double[] dst, double div) {
		for(int i = 0; i < dst.length; ++i) {
			dst[i] = dst[i] / div;
		}
	}
	
	public static void vectorMultiplyAndAdd(double[] dst, double[] v, double factor) throws IllegalArgumentException {
		if (dst.length != v.length) {
			throw new IllegalArgumentException("v1.length != v2.length");
		}
		for(int i = 0; i < dst.length; ++i) {
			dst[i] = dst[i] + factor*v[i];
		}
	}
	
	public static double vectorSize(double[] v) {
		double sum = 0;
		for(int i = 0; i < v.length; ++i) {
			sum = sum + (v[i] * v[i]);
		}
		return Math.sqrt(sum);
	}
	
	public static String printVector(double[] vector) {
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		for(double d : vector) {
			sb.append(d);
			sb.append(" ");
		}
		sb.append("]");
		return sb.toString();
	}
}
