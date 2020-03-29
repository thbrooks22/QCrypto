import java.util.ArrayList;
import java.math.BigInteger;
import java.lang.Math;
import java.util.function.Function;

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
    else if (k > this.degree) return new BigInteger("0");
    return this.coeffs.get(k);
  }


  // Set coefficient of x^k to z
  public void setCoeffOf(int k, BigInteger z) {
    if (k < 0)
      throw new IndexOutOfBoundsException("No such coefficient.");
    else if (k > this.degree) {
      for (int i = this.degree; i < k; i++) {
        this.coeffs.add(new BigInteger("0"));
      }
      this.coeffs.add(z);
    }
    else this.coeffs.set(k, z);
  }


  // Zero Polynomial of degree deg
  public static Polynomial zero(int deg) {
    ArrayList<BigInteger> zeros= new ArrayList<BigInteger>(deg + 1);
    for (int i = 0; i < deg + 1; i++) {
      zeros.add(new BigInteger("0"));
    }
    return new Polynomial(zeros);
  }


  // Polynomial with coefficients chosen uniformly at random from the integers
  //  mod maxLimit
  public static Polynomial randPolynomial(int deg, ArrayList<BigInteger> params,
    int distro) {
    ArrayList<Function<ArrayList<BigInteger>, BigInteger>> distros =
      new ArrayList<Function<ArrayList<BigInteger>, BigInteger>>();
    distros.add(p -> Mathematics.unifRandBigInt(p));
    distros.add(p -> Mathematics.dNormRandBigInt(p));
    ArrayList<BigInteger> coeffs= new ArrayList<BigInteger>(deg + 1);
    for (int i = 0; i < deg + 1; i++) {
      coeffs.add(distros.get(distro).apply(params));
    }
    return new Polynomial(coeffs);
  }


  // Standard sum of Polynomials
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
    ArrayList<BigInteger> sumCoeffs = this.plus(p).getCoeffs();
    for (int i = 0; i < sumCoeffs.size(); i++) {
      sumCoeffs.set(i, sumCoeffs.get(i).mod(z));
    }
    return new Polynomial(sumCoeffs);
  }


  // Sum of Polynomials mod a Polynomial
  public Polynomial plusModP(Polynomial p, Polynomial q) {
    ArrayList<BigInteger> sumCoeffs = this.plus(p).getCoeffs();
    for (int i = 0; i < sumCoeffs.size(); i++) {
      sumCoeffs.set(i, sumCoeffs.get(i).mod(q.getCoeffs().get(i)));
    }
    return new Polynomial(sumCoeffs);
  }
}
