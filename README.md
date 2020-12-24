# LinAlgEngine
With this project, I am seeking to implement the concepts learned in my Linear Algebra course at UMD (MATH240) using Java objects that I define myself. To do this, I constructed a robust Matrix object that is essentially an ArrayList of Vector objects that I also constructed.

Currently, my Linear Algebra Calculator can do the following functions:  
-3 major row operations (Row Substitution, Row Scale, and Row Swap)  
-Convert a Matrix to Row-Reduced Echelon Form (RREF)  
-Find the determinant of a Matrix  
-Determine if a Matrix is triangular  
-Determine if a Matrix is invertible  
-Find the inverse of a Matrix  

(The implementation of these functions can be found in the `Matrix` class.)

In the future, I plan to add the following features to this calculator: 
-Using the co-factor algorithm in finding the inverse of a Matrix -Checking for other axioms of the Invertible Matrix Theorem to optimize other functions (finding the inverse) 
-Finding the Null Space and Col Space of a matrix   
-Finding the QR Factorization of a Matrix   
-Determining if a Matrix is diagonalizable  
-Diagonalizing a Matrix.  
