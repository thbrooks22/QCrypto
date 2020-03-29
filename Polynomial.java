import java.util.ArrayList;
import java.util.Collections;
import java.math.BigInteger;
import java.lang.Math;


public class Polynomial
{
  private int degree;
  private ArrayList<BigInteger> coeffs;


  public Polynomial(ArrayList<BigInteger> coeffs) {
    if (coeffs.isEmpty())
      throw new IllegalArgumentException("Coefficients may not be empty.");
    this.coeffs = coeffs;
    this.degree = coeffs.size() - 1;
  }


  public int getDegree() {
    return this.degree;
  }


  public ArrayList<BigInteger> getCoeffs() {
    return this.coeffs;
  }


  // Get coefficient of x^k, return 0 if k>0 and coefficient not defined as of yet
  public BigInteger getCoeffOf(int k) {
    if (k < 0)
      throw new IndexOutOfBoundsException("No such coefficient.");
    else if (k > this.degree) return BigInteger.ZERO;
    return this.coeffs.get(k);
  }


  // Set coefficient of x^k to z
  public void setCoeffOf(int k, BigInteger z) {
    if (k < 0)
      throw new IndexOutOfBoundsException("No such coefficient.");
    else if (k > this.degree) {
      for (int i = this.degree + 1; i < k; i++) {
        this.coeffs.add(BigInteger.ZERO);
      }
      this.coeffs.add(z);
      this.degree = k;
    }
    else this.coeffs.set(k, z);
  }


  // Zero Polynomial of degree deg
  public static Polynomial zero(int deg) {
    ArrayList<BigInteger> zeros =
      new ArrayList<BigInteger>(Collections.nCopies(deg + 1, BigInteger.ZERO));
    return new Polynomial(zeros);
  }


  // Polynomial with coefficients chosen at random according to some specified
  //  distribution over the BigIntegers
  public static Polynomial randPolynomial(int deg, ArrayList<BigInteger> params,
    int distro) {
    ArrayList<BigInteger> coeffs= new ArrayList<BigInteger>(deg + 1);
    for (int i = 0; i < deg + 1; i++) {
      coeffs.add(Mathematics.distros.get(distro).apply(params));
    }
    return new Polynomial(coeffs);
  }


  /*
    Polynomial arithmetic:

      modZ(BigInteger z): returns this mod z.

      modP(Polynomial p): returns this mod p.

      plus(Polynomial p): returns this + p.

      plusModZ(Polynomial p, BigInteger z): returns (this + p) mod z.

      plusModP(Polynomial p, Polynomial q): returns (this + p) mod q.

      times(Polynomial p): returns this * p.

      timesModZ(Polynomial p, BigInteger z): returns (this * p) mod z.

      timesModP(Polynomial p, Polynomial q): returns (this * p) mod q.

      scalarTimes(BigInteger k): returns k * this.
  */

  public Polynomial modZ(BigInteger z) {
    ArrayList<BigInteger> coeffs = this.coeffs;
    for (int i = 0; i < coeffs.size(); i++) {
      coeffs.set(i, coeffs.get(i).mod(z));
    }
    return new Polynomial(coeffs);
  }


  public Polynomial modP(Polynomial p) {
    ArrayList<BigInteger> coeffs = this.coeffs;
    for (int i = 0; i < coeffs.size(); i++) {
      coeffs.set(i, coeffs.get(i).mod(p.getCoeffOf(i)));
    }
    return new Polynomial(coeffs);
  }


  public Polynomial plus(Polynomial p) {
    int maxDegree = Math.max(this.degree, p.getDegree());
    ArrayList<BigInteger> resCoeffs =
      new ArrayList<BigInteger>(maxDegree + 1);

    for (int i = 0; i < maxDegree + 1; i++) {
      resCoeffs.add(p.getCoeffOf(i).add(this.getCoeffOf(i)));
    }

    return new Polynomial(resCoeffs);
  }


  // Sum of Polynomials mod a BigInteger
  public Polynomial plusModZ(Polynomial p, BigInteger z) {
    return this.plus(p).modZ(z);
  }


  // Sum of Polynomials mod a Polynomial
  public Polynomial plusModP(Polynomial p, Polynomial q) {
    return this.plus(p).modP(q);
  }


  public Polynomial times(Polynomial p) {
    ArrayList<BigInteger> prodCoeffs =
      new ArrayList<BigInteger>(
        Collections.nCopies(this.degree + p.getDegree(), BigInteger.ZERO)
      );

    for (int i = 0; i < this.degree + 1; i++) {
      for (int j = 0; j < p.getDegree() + 1; j++) {
        prodCoeffs.set(i + j,
          prodCoeffs.get(i + j).add(
            this.getCoeffOf(i).multiply(p.getCoeffOf(j))
          ));
      }
    }

    return new Polynomial(prodCoeffs);
  }


  public Polynomial timesModZ(Polynomial p, BigInteger z) {
    return this.times(p).modZ(z);
  }


  public Polynomial timesModP(Polynomial p, Polynomial q) {
    return this.times(p).modP(q);
  }


  public Polynomial scalarTimes(BigInteger k) {
    ArrayList<BigInteger> prodCoeffs = new ArrayList<BigInteger>(this.degree + 1);
    for (int i = 0; i < this.degree + 1; i++) {
      prodCoeffs.add(this.getCoeffOf(i).multiply(k));
    }
    return new Polynomial(prodCoeffs);
  }


  public static void main(String[] args) {
    int deg = Integer.parseInt(args[0]);
    ArrayList<BigInteger> gaussParams = new ArrayList<BigInteger>() {
      {
        add(new BigInteger("13"));
        add(new BigInteger("1"));
      }
    };
    Polynomial p = randPolynomial(deg, gaussParams, 2);
    System.out.println(p.getCoeffs());
    p.setCoeffOf(deg, BigInteger.TWO);
    System.out.println(p.getCoeffs());
  }
}
