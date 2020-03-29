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


  public Polynomial initiate() {
    this.si = this.chiAlpha();
    this.ei = this.chiAlpha();
    Polynomial pi =
      this.a.times(this.si).plus(
        this.ei.scalarTimes(BigInteger.TWO)
        ).modZ(this.q).modP(this.phi);
    return pi;
  }


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


  public Polynomial chiAlpha() {
    return Polynomial.randPolynomial(
      this.deg, this.params, Mathematics.FINITE_DGAUSSIAN
      ).modZ(this.q).modP(this.phi);
  }


  public static void main(String args[]) {
    RLWE r = new RLWE();
    System.out.println(r.getPhi());
  }
}
