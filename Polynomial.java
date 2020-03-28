import java.util.ArrayList;
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


  public BigInteger getCoeffOf(int k) {
    if (k < 0)
      throw IndexOutOfBoundsException("No such coefficient.");
    else if (k > this.degree) return new BigInteger("0");
    return this.coeffs.get(k);
  }


  public void setCoeffOf(int k, BigInteger z) {
    if (k < 0)
      throw IndexOutOfBoundsException("No such coefficient.");
    else if (k > this.degree) {
      for (int i = this.degree; i < k; i++) {
        this.coeffs.add(BigInteger("0"));
      }
      this.coeffs.add(z);
    }
    else this.coeffs.set(k, z);
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
}
