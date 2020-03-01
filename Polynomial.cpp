class Polynomial {
  private:
    int degree;
    int[] coeffs;


  public:
    Polynomial(int[] coeffs) {
      this->coeffs = coeffs;
      this->degree = *(&coeffs + 1) - coeffs - 1;
    }


    int getDegree() {
      return this->degree;
    }


    int[] getCoeffs() {
      return this->coeffs;
    }


    int getCoeff(int k) {
      return this->coeffs[this->degree - k];
    }
}
