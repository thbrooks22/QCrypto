import java.math.BigInteger;
import java.lang.Math;
import java.util.Random;
import java.util.ArrayList;
import java.util.function.Function;

public class Mathematics
{
  public static ArrayList<Function<ArrayList<BigInteger>, BigInteger>> distros =
    new ArrayList<Function<ArrayList<BigInteger>, BigInteger>>() {
      {
        add(p -> Mathematics.unifRandBigInt(p));
        add(p -> Mathematics.dNormRandBigInt(p));
      }
    };


  /*
    Probability distributions:

      unifRandBigInt(ArrayList<BigInteger> params): generates a uniform random
        BigInteger between params(0), inclusive, and params(1), exclusive.

      dNormRandBigInt(ArrayList<BigInteger> params): generates a random variable
        distributed according to the discrete Gaussian distribution with mean
        params(0) and standard deviation params(1).

  */

  public static BigInteger unifRandBigInt(ArrayList<BigInteger> params) {
    BigInteger minLimit = params.get(0);
    BigInteger maxLimit = params.get(1);
    BigInteger bigInt = maxLimit.subtract(minLimit);
    Random rnd = new Random();
    int len = maxLimit.bitLength();
    BigInteger res = new BigInteger(len, rnd);
    if (res.compareTo(minLimit) < 0) res = res.add(minLimit);
    if (res.compareTo(bigInt) >= 0) res = res.mod(bigInt).add(minLimit);
    return res;
  }


  public static BigInteger dNormRandBigInt(ArrayList<BigInteger> params) {
    BigInteger mu = params.get(0);
    BigInteger sigma = params.get(1);
    Random rnd = new Random();
    double gaussian = ((double) mu.intValue()) +  ((double) sigma.intValue()) *
      rnd.nextGaussian();
    return new BigInteger(Integer.toString((int) Math.floor(gaussian)));
  }


  /*
    Number-theoretic algorithms:

      isPrime(BigInteger n, int k): probabilistically checks the primality of
        n according to the Rabin-Miller test using precision parameter k.
  */

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
      ArrayList<BigInteger> params = new ArrayList<BigInteger>(2);
      params.add(two);
      params.add(n.subtract(two));
      BigInteger a = unifRandBigInt(params);
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
