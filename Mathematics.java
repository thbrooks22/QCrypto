import java.math.BigInteger;
import java.lang.Math;
import java.util.Random;

public class Mathematics
{
  // Generate random BigInteger between minLimit, inclusive, and maxLimit,
  //  exclusive
  public static BigInteger randBigInt(BigInteger minLimit, BigInteger maxLimit) {
    BigInteger bigInt = maxLimit.subtract(minLimit);
    Random rnd = new Random();
    int len = maxLimit.bitLength();
    BigInteger res = new BigInteger(len, randNum);
    if (res.compareTo(minLimit) < 0) res = res.add(minLimit);
    if (res.compareTo(bigInt) >= 0) res = res.mod(bigInt).add(minLimit);
    return res;
  }


  // Rabin-Miller primality test on n with k rounds of testing
  public static boolean isPrime(BigInteger n, int k) {
    BigInteger one = new BigInteger("1");
    BigInteger two = new BigInteger("2");
    BigInteger r = new BigInteger("0");
    BigInteger d = n.subtract(one);

    while (!one.equals(d.mod(two))) {
      d = d.divide(two);
      r = r.add(one);
    }

    for (int i = k; i > 0; i--) {
      BigInteger a = randBigInt(two, n.subtract(two));
      BigInteger x = a.pow(d.intValue()).mod(n);
      if (x.equals(one) || x.equals(n.subtract(one))) continue;
      for (int j = r.intValue() - 1; j > 0; j--) {
        x = x.pow(2).mod(n);
        if (x.equals(n.subtract(one))) continue;
      }
      return false;
    }

    return true;
  }
}
