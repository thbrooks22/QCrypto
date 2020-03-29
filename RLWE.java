/*
  Ring learning with errors key exchange protocol originally conceived by Regev.

    Some proofs of security and correctness: https://eprint.iacr.org/2012/688.pdf
    SINGH-prefixed parameters: https://eprint.iacr.org/2015/138
*/

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

public class RLWE
{
  private static int SINGH_DEG = 1024;
  private static BigInteger SINGH_Q = new BigInteger("40961");
  private static BigInteger SINGH_SIGMA = new BigInteger("4");
  private static Polynomial SINGH_PHI = new Polynomial (
    new ArrayList<BigInteger>(Collections.nCopies(SINGH_DEG, BigInteger.ZERO)) {
      {
        set(0, BigInteger.ONE);
        add(BigInteger.ONE);
      }
    }
  );
  public static ArrayList<BigInteger> SINGH_PARAMS = new ArrayList<BigInteger>() {
    {
      add(SINGH_Q);
      add(SINGH_SIGMA);
    }
  };

  /*
    Initial parameters
  */
  private Polynomial a;
  private BigInteger q;
  private int deg;
  private ArrayList<BigInteger> params;
  private Polynomial phi;

  /*
    Secret parameters
  */
  private Polynomial si;
  private Polynomial ei;
  private Polynomial sr;
  private Polynomial er;

  public RLWE(Polynomial a, BigInteger q, int deg, ArrayList<BigInteger> params,
    Polynomial phi) {
    this.a = a;
    this.q = q;
    this.deg = deg;
    this.params = params;
    this.phi = phi;
  }


  // Initialize RLWE-KX protocol with parameters specified in Singh (link above)
  public RLWE() {
    this.params = SINGH_PARAMS;
    this.deg = SINGH_DEG;
    this.q = SINGH_Q;
    this.phi = SINGH_PHI;
    this.a = this.chiAlpha();
  }


  public Polynomial getPhi() {
    return this.phi;
  }


  // RLWE-KX initiator
  public Polynomial initiate() {
    this.si = this.chiAlpha();
    this.ei = this.chiAlpha();
    Polynomial pi =
      this.a.times(this.si).plus(
        this.ei.scalarTimes(BigInteger.TWO)
        ).modZ(this.q).modP(this.phi);
    return pi;
  }


  // RLWE-KX responder
  public Polynomial respond(Polynomial pi) {
    this.sr = this.chiAlpha();
    this.er = this.chiAlpha();
    Polynomial epr = this.chiAlpha();
    Polynomial pr =
      this.a.times(this.sr).plus(
        this.er.scalarTimes(BigInteger.TWO)
        ).modZ(this.q).modP(this.phi);
    Polynomial kr =
      pi.times(sr).plus(
        epr.scalarTimes(BigInteger.TWO)
        ).modZ(this.q).modP(this.phi);
    // finish protocol
  }


  /*
    Return random polynomial with this.degree coefficients distributed according
      to the finite discrete Gaussian distribution with limit parameter this.q and
      some specified standard deviation.
  */
  public Polynomial chiAlpha() {
    return Polynomial.randPolynomial(
      this.deg, this.params, Mathematics.FINITE_DGAUSSIAN
      ).modZ(this.q).modP(this.phi);
  }


  /*
    RLWE helpers:

      sig(BigInteger z): computes the signal function on z, deciding membership
        of z in the set E = {-[q/4], ... , [[q/4]]}, where [] is the floor
        function and [[]] is the round function. If z is a member, return ZERO.
        Otherwise, return ONE.
  */

  public BigInteger sig(BigInteger z) {
    // Compute minimum bound of set E described above
    BigInteger eMin =
      BigInteger.ZERO.subtract(
        this.q.divide(new BigInteger("4"))
      ).mod(this.q);
    BigInteger eMax;

    // Get quotient and remainder, check which way to round
    BigInteger[] proveEMax =
      this.q.divideAndRemainder(new BigInteger("4"));
    if (proveEMax[1].compareTo(BigInteger.TWO) < 0)
      eMax = proveEMax[0];
    else eMax = proveEMax[0].add(BigInteger.ONE);

    // Check z for membership in set E, return appropriate signal
    if (z.compareTo(eMin) >= 0 || z.compareTo(eMax) <= 0) return BigInteger.ZERO;
    return BigInteger.ONE;
  }


  public static void main(String args[]) {
    RLWE r = new RLWE();
    System.out.println(r.getPhi());
  }
}
