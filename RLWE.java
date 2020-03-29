import java.math.BigInteger;

public class RLWE
{
  private Polynomial pk;
  private BigInteger q;
  private int deg;


  public RLWE(Polynomial pk, BigInteger q, int n) {
    this.pk = pk;
    this.q = q;
    this.deg = n - 1;
  }


  public RLWE() {
    
  }
}
