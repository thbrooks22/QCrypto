#include "stdio.h"
#include "gmp.h"
#include <vector>

using namespace std;

class Polynomial {
  private:
    int degree;
    vector<mpz_t*> coeffs;


  public:
    // Construct polynomial with array of mpz_t coefficients — this is sufficient
    //  for finite fields with which we will deal.
    Polynomial(vector<mpz_t*> coeffs, int degree) {
      this->coeffs = coeffs;
      this->degree = degree;
    }


    int getDegree() {
      return this->degree;
    }


    vector<mpz_t*> getCoeffs() {
      return this->coeffs;
    }


    mpz_t* getCoeff(int k) {
      return this->coeffs.at(this->degree - k);
    }
};


int main() {
  int base = 10;
  const char* val_1 = "1234567891011121314";
  const char* val_2 = "10132845761011121314";
  mpz_t coeff_1, coeff_2;
  mpz_init_set_str(coeff_1, val_1, base);
  mpz_init_set_str(coeff_2, val_2, base);
  mpz_t* coeff_arr[2] = {&coeff_1, &coeff_2};
  vector<mpz_t*> coeffs (coeff_arr, coeff_arr + sizeof(coeff_arr) / sizeof(mpz_t*));
  Polynomial *p = new Polynomial(coeffs, 2);
  gmp_printf("check it out: %Zd :)\n", *((*p).getCoeff(1)));
  mpz_clear(coeff_1);
  mpz_clear(coeff_2);
}
