CXX = g++
CXXFLAGS = -lgmp

poly:
	$(CXX) $(CXXFLAGS) Polynomial.cpp -o poly
	
