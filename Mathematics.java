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
        add(params -> unifRandBigInt(params));
        add(params -> dNormRandBigInt(params));
        add(params -> finiteDNormRandBigInt(params));
      }
    };


  /*
    Probability distributions:

      unifRandBigInt(ArrayList<BigInteger> params): generates a uniform random
        BigInteger between params(0), inclusive, and params(1), exclusive.

      dNormRandBigInt(ArrayList<BigInteger> params): generates a random variable
        distributed according to the discrete Gaussian distribution (round-down
        Gaussian) with mean params(0) and standard deviation params(1).

      finiteDNormRandBigInt(ArrayList<BigInteger> params): generates a random
        variable distributed according to the finite discrete Gaussian distribution
        (truncated round-down Gaussian) with mean 0, limit parameter params(0),
        and standard deviation params(1).

  */

  public static BigInteger unifRandBigInt(ArrayList<BigInteger> params) {
    BigInteger minLimit;
    BigInteger maxLimit;

    // Throw IllegalArgumentException if parameters are of improper length or value
    try {
      minLimit = params.get(0);
      maxLimit = params.get(1);
      if (minLimit.compareTo(maxLimit) >= 0) throw new Exception();
    }
    catch (Exception e) {
      throw new
        IllegalArgumentException("Invalid parameters for uniform distribution.");
    }

    BigInteger bigInt = maxLimit.subtract(minLimit);
    Random rnd = new Random(System.nanoTime());
    int len = maxLimit.bitLength();
    BigInteger res = new BigInteger(len, rnd);
    if (res.compareTo(minLimit) < 0) res = res.add(minLimit);
    if (res.compareTo(bigInt) >= 0) res = res.mod(bigInt).add(minLimit);
    return res;
  }


  public static BigInteger dNormRandBigInt(ArrayList<BigInteger> params) {
    BigInteger mu;
    BigInteger sigma;

    // Throw IllegalArgumentException if parameters are of improper length
    try {
      mu = params.get(0);
      sigma = params.get(1);
    }
    catch (Exception e)
    {
      throw new IllegalArgumentException(
        "Invalid parameters for discrete Gaussian distribution."
        );
    }

    Random rnd = new Random(System.nanoTime());
    double gaussian = ((double) mu.intValue()) +  ((double) sigma.intValue()) *
      rnd.nextGaussian();
    return new BigInteger(Integer.toString((int) Math.floor(gaussian)));
  }


  public static BigInteger finiteDNormRandBigInt(ArrayList<BigInteger> params) {
    BigInteger mu = BigInteger.ZERO;
    BigInteger q;
    BigInteger sigma;

    // Throw IllegalArgumentException if parameters are of improper length
    try {
      q = params.get(0);
      sigma = params.get(1);
    }
    catch (Exception e)
    {
      throw new IllegalArgumentException(
        "Invalid parameters for finite discrete Gaussian distribution."
        );
    }

    BigInteger minLimit = BigInteger.ONE.subtract(q).divide(BigInteger.TWO);
    BigInteger maxLimit = q.subtract(BigInteger.ONE).divide(BigInteger.TWO);
    BigInteger res = dNormRandBigInt(
      new ArrayList<BigInteger>() {
        {
          add(mu);
          add(sigma);
        }
      });
    if (res.compareTo(minLimit) < 0) return minLimit;
    if (res.compareTo(maxLimit) > 0) return maxLimit;
    return res;
  }


  /*
    Number-theoretic algorithms:

      isPrime(BigInteger n, int k): probabilistically checks the primality of
        n according to the Rabin-Miller test using precision parameter k.
  */

  public static boolean isPrime(BigInteger n, int k) {
    BigInteger one = BigInteger.ONE;
    BigInteger two = BigInteger.TWO;
    BigInteger r = BigInteger.ZERO;
    BigInteger d = n.subtract(one);

    while (!one.equals(d.mod(two))) {
      d = d.divide(two);
      r = r.add(one);
    }

    for (int i = k; i > 0; i--) {
      boolean ct = false;
      ArrayList<BigInteger> params = new ArrayList<BigInteger>(2);
      params.add(two);
      params.add(n.subtract(one));
      BigInteger a = unifRandBigInt(params);
      BigInteger x = a.pow(d.intValue()).mod(n);
      if (x.equals(one) || x.equals(n.subtract(one))) continue;
      for (int j = r.intValue() - 1; j > 0; j--) {
        x = x.pow(2).mod(n);
        if (x.equals(n.subtract(one))) {
          ct = true;
          break;
        }
      }
      if (ct) continue;
      return false;
    }

    return true;
  }
}
