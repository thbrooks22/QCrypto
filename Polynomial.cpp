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
    Polynomial(vector<mpz_t*> coeffs) {
      this->coeffs = coeffs;
      this->degree = coeffs.size() - 1;
    }


    int getDegree() {
      return this->degree;
    }


    vector<mpz_t*> getCoeffs() {
      return this->coeffs;
    }


    // Get coefficient of x^k.
    mpz_t* getCoeff(int k) {
      return this->coeffs.at(this->degree - k);
    }


    // Clear current x^k coefficient, set it to *coeff.
    void setCoeff(mpz_t* coeff, int k) {
      mpz_clear(*this->coeffs.at(this->degree - k));
      this->coeffs.at(this->degree - k) = coeff;
    }


    Polynomial* operator +(Polynomial* p1, Polynomial* p2) {
      int this_degree = p1->getDegree();
      int that_degree = p2->getDegree();
      vector<mpz_t*> coeffs;
      int min_degree;
      Polynomial* max_poly;

      if (this_degree > that_degree) {
        min_degree = that_degree;
        max_poly = p1;
      }
      else {
        min_degree = this_degree;
        max_poly = p2;
      }

      for (int i = 0; i < min_degree + 1; i++) {
        mpz_t coeff;
        mpz_init(coeff);
        mpz_add(coeff, *p1->coeffs.at(this_degree - i),
          *p2->coeffs.at(that_degree - i));
        coeffs.push_back(&coeff);
      }

      int max_degree = max_poly->getDegree();

      for (int i = min_degree + 1; i < max_degree + 1; i++) {
        mpz_t coeff;
        mpz_init(coeff);
        mpz_add(coeff, coeff, *max_poly->coeffs.at(max_degree - i));
        coeffs.push_back(&coeff);
      }

      reverse(coeffs.begin(), coeffs.end());

      return new Polynomial(coeffs);
    }
};


int main() {
  int base = 10;
  const char* val_1 = "3";
  const char* val_2 = "1";
  mpz_t coeff_1, coeff_2;
  mpz_init_set_str(coeff_1, val_1, base);
  mpz_init_set_str(coeff_2, val_2, base);
  mpz_t* coeff_arr[1] = {&coeff_1};
  mpz_t* coeff_arr_2[1] = {&coeff_2};
  vector<mpz_t*> coeffs (coeff_arr, coeff_arr + sizeof(coeff_arr) / sizeof(mpz_t*));
  vector<mpz_t*> coeffs_2 (coeff_arr_2, coeff_arr_2 + sizeof(coeff_arr_2) / sizeof(mpz_t*));
  Polynomial* p = new Polynomial(coeffs);
  Polynomial* p2 = new Polynomial(coeffs_2);
  Polynomial* p3 = p + p2;
  gmp_printf("check it out: %Zd :)\n", *((*p3).getCoeff(0)));
  mpz_clear(coeff_1);
  mpz_clear(coeff_2);
  delete p;
  delete p2;
  delete p3;
}
