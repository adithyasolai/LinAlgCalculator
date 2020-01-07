import java.util.ArrayList;

public class Matrix {
    private ArrayList<Vector> matrix;
    private int rows;
    private int cols;
    private static double EPSILON = 0.000001;
    /*
        THE FOLLOWING ARE THE VALID
        CONSTRUCTORS FOR THIS MATRIX
        OBJECT.
     */
    public Matrix() {
        this.matrix = new ArrayList<Vector>();
        this.rows = 0;
        this.cols = 0;
    }

    public Matrix(Matrix matrix) {
        this.matrix = new ArrayList<Vector>();
        this.rows = matrix.numRows();
        this.cols = 0;

        for(int i=1; i<=matrix.rows; i++)
            this.addColumn(matrix.getColumn(i));
    }

    /*
        Assume that the given `otherMatrix` is
        in a proper "m x n" format.
     */
    public Matrix(double[][] otherMatrix) {
        this.matrix = new ArrayList<Vector>();
        this.rows = otherMatrix.length;
        this.cols = 0;

        for(int r = 0; r < otherMatrix.length; r++) {
            /*
                In each int array within `otherMatrix`,
                transfer the elements of those int arrays
                into a Vector object, and then add that
                Vector as a new column.
             */
            Vector currVector = new Vector();
            for(int c = 0; c < otherMatrix[r].length; c++) {
                currVector.addElement(otherMatrix[r][c]);
            }
            this.addColumn(currVector);
        }
    }

    /*
        THE FOLLOWING METHODS INVOLVE ADDING
        AND REMOVING COLUMNS FROM THE
        MATRIX AND RETRIEVING THE NUMBER OF
        COLUMNS AND ROWS.
     */

    public int numCols() {
        return this.cols;
    }

    public int numRows() {
        return this.rows;
    }

    public void addColumn(Vector col) {
        /*
            SPECIAL CASE:
            If this is the first vector that is being
            added to the list, this will ensure that
            this Vector will pass the subsequent
            conditional and be added to the list.
         */
        if (this.cols == 0) {
            this.rows = col.getSize();
        }

        /*
            TYPICAL CASE:
            The Vector `col` will only be added if
            it has the same number of values as the
            other Vectors in the matrix.

            This maintains the "m x n" structure of
            this matrix implementation, which will
            allow many of Linear Algebra's theorems
            and properties to be applied to this
            matrix.
         */
        if (col.getSize() == this.rows) {
            this.matrix.add(col);
            this.cols++;
        }

    }

    /*
        Removes the specified column
        from the list.

        NOTE: The `col` parameter does
        not follow the indexing conventions
        of ArrayLists.
     */
    public void removeColumn(int col) {
        this.matrix.remove(col-1);
        this.cols--;
    }

    /*
        Returns the Vector object stored
        in the specified column.

        `col` does not follow the
        indexing conventions of the
        ArrayList used to represent
        the matrix.
     */
    public Vector getColumn(int col) {
        return this.matrix.get(col-1);
    }

    /*
        THE FOLLOWING METHODS PERFORM
        THE ELEMENTARY ROW OPERATIONS:
        - ROW SWAP
        - ROW SCALE
        - ROW SUBSTITUTION
          (ADD A SCALED ROW TO
           ANOTHER ROW.)
     */

    /*
        r1 and r2 do not follow the indexing
        conventions of the ArrayLists used to
        represent the matrix and the vectors.

        Therefore, r1 and r2 are checked to
        see if they have valid row numbers.
     */
    public void rowSwap(int r1, int r2) {
        if ((r1 >= 1 && r1 <= this.numRows()) &&
            (r2 >= 1 && r2 <= this.numRows())) {

            for (int i=0; i < this.numCols(); i++) {
                Vector currCol = this.matrix.get(i);

                double temp = currCol.getElement(r1);

                currCol.setElement(r1, currCol.getElement(r2));
                currCol.setElement(r2, temp);
            }

        }
    }

    /*
        This method scales a specified `row` in the
        matrix by a specified `coefficient`.

        Both of these parameters do not follow the
        indexing conventions of the ArrayLists used
        to represent the matrix and vectors.
     */
    public void rowScale(int row, double coefficient) {
        if (row >= 1 && row <= this.numRows()) {
            for (int i=0; i < this.numCols(); i++) {
                Vector currCol = this.matrix.get(i);
                currCol.setElement(row, coefficient * currCol.getElement(row));
            }
        }
    }

    /*
        Unlike the `rowScale()` method, this method
        does not modify the current matrix. It returns
        a Vector that contains a specified `row` of the
        current matrix if it were scaled by a
        `coefficient`.

        This will be used to implement the
        `rowSub()` method in a readable fashion.

        Since this is not one of the elementary
        row operations and is not really
        needed by the user for any reason,
        this method is private.
     */
    private Vector scaledRow(int row, double coefficient) {
        if (row >= 1 && row <= this.numRows()) {
            Vector scaledRow = new Vector();
            for (int i=0; i < this.numCols(); i++) {
                Vector currCol = this.matrix.get(i);
                scaledRow.addElement(coefficient * currCol.getElement(row));
            }
            return scaledRow;
        }
        return null;
    }

    /*
        This method implements the Row Substitution
        elementary row operation. In this row
        operation, one row (`row1`) is added with
        another row (`row2`) as if it (`row2`) were
        scaled by a `coefficient`.

        This will do row1 = row1 + (coefficient) * row2
     */
    public void rowSub(int row1, int row2, double coefficient) {
        if((row1 >= 1 && row1 <= this.numRows()) &&
           (row2 >= 1 && row2 <= this.numRows())) {
            Vector otherRow = this.scaledRow(row2, coefficient);
            for(int i=1; i <= this.numCols(); i++) {
                Vector currCol = this.getColumn(i);
                currCol.setElement(row1, currCol.getElement(row1) + otherRow.getElement(i));
            }
        }
    }

    /*
        THE FOLLOWING METHODS UTILIZE THE ROW
        OPERATIONS ABOVE TO DO MORE COMPLEX
        ALTERATIONS TO THE MATRIX SUCH AS
        FINDING THE ROW-REDUCED ECHELON FORM
        OF THE MATRIX AND THE DETERMINANT OF THE
        MATRIX (IF IT IS A SQUARE MATRIX)
     */


    /*
        This method brings the current matrix
        to Reduced Row Echelon Form. The algorithm for
        doing this is as follows:

        Divide Row 1 by the coefficient of the
        first element in the row so that the
        pivot element has a coefficient of 1.

        For Row 1, the pivot element is the 1st
        element in that row. For Row 2, the pivot
        element is the 2nd element in that row, etc.

        Clear the values below the pivot
        element of Row 1 using row substitution.

        Repeat for the other rows.
     */
    public void rref() {
        for (int row = 1; row <= this.numRows() ; row++) {
            /*
                Scale current row so that first element is
                1.

                If the current pivot element is 0, then
                there will be a free variable in this case,
                and no further scaling is needed.
             */
            if (this.getColumn(row).getElement(row) != 0) {
                this.rowScale(row, (1 / this.getColumn(row).getElement(row)));
            }

            /*
                If the pivot values are very close to 1, just make them 1.
             */
            if (Math.abs(1 - this.getColumn(row).getElement(row)) < Matrix.EPSILON) {
                this.getColumn(row).setElement(row, 1);
            }

            /*
                Clear the values below the pivot element.
            */

            for (int rowBelow = row + 1; rowBelow <= this.numRows(); rowBelow++) {
                this.rowSub(rowBelow, row, (-1) * this.getColumn(row).getElement(rowBelow));
            }

            /*
                Clear the values above the pivot element.
             */
            for (int rowAbove = 1; rowAbove < row; rowAbove++) {
                this.rowSub(rowAbove, row, (-1) * this.getColumn(row).getElement(rowAbove));
            }
        }
    }

    public class NotSquareMatrixException extends Exception {
        NotSquareMatrixException() { }

        NotSquareMatrixException(String str) {
            super(str);
        }
    }


    /*
        This method will return the determinant
        of the current matrix, if it is a
        square matrix.

        This method will find the deteminant recursively
        using a helper method.
     */
    public double det() throws NotSquareMatrixException {

        /*
            Check if the matrix is square first.
         */
        if (this.numRows() == this.numCols()) {
            /*
                If the current matrix is upper triangular or
                lower triangular, the determinant is equal
                to the product of the elements on the main
                diagonal of the matrix.

                This is much more efficient than calling the
                detHelper() function.
            */
            if(this.isTriangular()) {
                double diagTotal = 1;
                for(int col = 1; col <= this.numCols(); col++) {
                    diagTotal *= this.getColumn(col).getElement(col);
                }
                return diagTotal;
            } else {
                return detHelper(this);
            }
        }
        /*
            If the matrix is not square, throw an
            exception.
        */
        throw new NotSquareMatrixException("Determinants only valid for square matrices.");
    }

    /*
        Assume that the Matrix `m` is already a
        square matrix.

        This method finds the determinant of
        `m` with the following algorithm:

        For each element in the first/top
        row of `m`, find the determinant of
        the sub-matrix of `m` formed by
        removing the current element's
        column and row. The same row will
        be eliminated for each element/iteration
        in this game. Multiply the determinant
        of each sub-matrix by the original element
        from the first/top row. Multiply this value
        by -1 for every other iteration starting at
        the second element on the first/top row.
        Add these values up, and that is the determinant
        of the overall matrix `m`.

        The algorithm above implies a recursive approach
        of continuously finding sub-matrices. The stopping
        cases are if the sub-matrix formed is a 2x2 matrix
        or a 1x1 matrix. The determinant of a 1x1 matrix is
        simply the one value in the matrix, no further sub-matrices
        or recursion is required. The determinant of a 2x2 matrix
        is equal to ab-cd where the 2x2 matrix is organized like this:

       [ a   b
         c   d ]

        One way to optimize this method is to
        find the row/column with the most
        amount of 0s, which will result in
        potentially fewer recursive calls.
     */
    private double detHelper(Matrix m) {
        /*
            Base case is if the Matrix  `m`
            if just a 1x1 matrix, which would
            make its determinant just the element
            itself.
        */
        if (m.numCols() == 1 && m.numRows() == 1) {
            return this.getColumn(1).getElement(1);
        } else if (m.numCols() == 2 && m.numRows() == 2) {
            return (m.getColumn(1).getElement(1) * m.getColumn(2).getElement(2)) -
                    (m.getColumn(2).getElement(1) * m.getColumn(1).getElement(2));
        } else {
            /*
                In this implementation, simply do the
                recursive process along the top row.

                Construct a new column of `nextMatrix`
                in each iteration that will be passed
                to the next recursive call.
             */
            int detTotal = 0;

            for (int col = 1; col <= m.numCols(); col++) {
                /*
                    Determines the sign of the determinant when
                    adding it to the total sum.

                    As per the algorithm, this will alternate
                    in a checkered pattern, depending on the
                    current row and column. Since the row is
                    constant in this non-optimized approach,
                    just the `col` value needs to be analyzed
                    to determine the appropriate sign.

                    The top-left most col/row is positive, meaning
                    col is 1, and every odd `col` value after that
                    is positive, meaning the even `col` values are
                    negative.
                 */
                int sign = (col % 2 == 0) ? -1 : 1;
                double coeff = this.getColumn(col).getElement(1);
                Matrix nextMatrix = new Matrix();

                // Add columns before and after `col`
                for(int currCol = 1; currCol <= m.numCols(); currCol++) {
                    if (currCol != col) {
                        Vector newCol = new Vector(m.getColumn(currCol));
                        // Remove first element in column
                        newCol.removeElement(1);

                        nextMatrix.addColumn(newCol);
                    }
                }

                detTotal += (sign * coeff * detHelper(nextMatrix));
            }

            return detTotal;
        }
    }

    /*
        This method finds the inverse of the
        current matrix.

        This is currently done using a naive
        algorithm that basically augments the
        current matrix with the Identity Matrix,
        finds the rref of the current matrix,
        and replicates any row operations to the
        Identity Matrix. After reaching rref
        with the current matrix, the altered
        Identity Matrix must be the inverse of
        the current matrix.

        It is assumed that the current matrix is
        invertible before this method is called.
        Therefore, the user is expected to call
        isInvertible() on the current matrix
        and ensure that the return value is true
        before calling this function. There is
        no "error inverse matrix" that can be
        returned because any matrix deemed to
        be the "error inverse matrix" may be
        the genuine inverse of some other
        matrix.
     */
    public Matrix inverse() {
        //Construct the Identity Matrix for the current matrix's dimensions
        double[][] identity2D = new double[this.numRows()][this.numCols()];
        for (int col = 0; col < this.numCols(); col++) {
            identity2D[col][col] = 1;
        }

        /*
            Although the current matrix will not be altered, the
            row operations must be done on some copy of the current
            matrix to ensure the rref algorithm is done properly,
            which will ensure the proper row operations are
            done on the augmented identity matrix.

            Therefore, this copy of the current matrix will be used.
         */
        Matrix currCopy = new Matrix(this);

        /*
            Use the constructor that takes a 2-D
            array of doubles as a parameter to
            create an identity matrix to work with.
         */
        Matrix identity = new Matrix(identity2D);

        /*
            THE FOLLOWING IS THE SAME ALGORITHM USED
            IN THE RREF() METHOD, BUT THE CHANGES ARE
            ALSO APPLIED TO THE `identity` MATRIX.
         */
        for (int row = 1; row <= currCopy.numRows() ; row++) {
            /*
                Scale current row so that first element is
                1.
             */
            if (currCopy.getColumn(row).getElement(row) != 0) {
                double scalar = (1 / currCopy.getColumn(row).getElement(row));
                currCopy.rowScale(row, scalar);
                identity.rowScale(row, scalar);
            }

            /*
                Clear the values below the pivot element.
            */

            for (int rowBelow = row + 1; rowBelow <= currCopy.numRows(); rowBelow++) {
                double scalar = (-1) * currCopy.getColumn(row).getElement(rowBelow);
                currCopy.rowSub(rowBelow, row, scalar);
                identity.rowSub(rowBelow, row, scalar);
            }

            /*
                Clear the values above the pivot element.
             */
            for (int rowAbove = 1; rowAbove < row; rowAbove++) {
                double scalar = (-1) * currCopy.getColumn(row).getElement(rowAbove);
                currCopy.rowSub(rowAbove, row, scalar);
                identity.rowSub(rowAbove, row, scalar);
            }
        }
        return identity;
    }

    /*
        THE FOLLOWING METHODS CHECK TO SEE
        IF THE MATRIX CONTAINS CERTAIN
        SPECIAL PROPERTIES SUCH AS
        BEING INVERTIBLE OR TRIANGULAR OR
        DIAGONAL.
     */

    /*
        This method returns true if the current
        matrix is upper or lower triangular, and
        false otherwise.
     */
    public boolean isTriangular() {
        //Only square matrices can be triangular.
        if(this.numCols() != this.numRows())
            return false;


        /*
            In each column, start from the "col"th
            position, all elements below this position
            must be 0 to be upper triangular.

            Don't need to check for final column.
         */

        boolean isUpper = true;
        boolean isLower = true;
        for (int col = 1; col < this.numCols(); col++) {
            if(isUpper || isLower) {
                //Check for upper triangular first
                int upperRow = col + 1;
                while (upperRow <= this.getColumn(col).getSize() &&
                        this.getColumn(col).getElement(upperRow) == 0) {
                    upperRow++;
                }

                if (upperRow <= this.getColumn(col).getSize() &&
                        this.getColumn(col).getElement(upperRow) != 0) {
                    isUpper = false;
                }

                //Check for lower triangular
                int lowerRow = 1;
                while (lowerRow < col &&
                       this.getColumn(col).getElement(lowerRow) == 0) {
                    lowerRow++;
                }

                if (lowerRow < col &&
                    this.getColumn(col).getElement(lowerRow) != 0) {
                    isLower = false;
                }
            }
        }

        return (isUpper || isLower);
    }
    /*
        The Invertible Matrix Theorem describes
        all of the conditions in which a matrix
        is invertible.

        This method first ensures that the current
        matrix is a square matrix. Then, if the
        determinant of the matrix is not equal to 0,
        the current matrix is invertible.

        If either of these conditions are not met,
        the current matrix is not invertible and false
        is returned.
     */
    public boolean isInvertible() throws NotSquareMatrixException {
        if (this.numRows() == this.numCols() &&
            this.det() != 0) {
            return true;
        } else {
            return false;
        }
    }


    /*
        THE FOLLOWING METHODS ARE
        OVERRIDES OF THE OBJECT
        CLASS: equals() TO SEE IF
        TWO MATRICES ARE THE SAME
        AND toString() TO PRINT
        THE CONTENTS OF THE MATRIX.
     */

    @Override
    public boolean equals(Object other) {
        /*
            If pointing to the same object,
            return true.
         */
        if (this == other) {
            return true;
        }
        /*
            If the other object is not even
            a Matrix type, then return false.
         */
        if(!(other instanceof Matrix)) {
            return false;
        }

        /*
            The other object is a Matrix type,
            so it can be cast as a Matrix object
            to compare its columns to the current
            Matrix.
         */
        Matrix otherMatrix = (Matrix) other;

        /*
            If the two matrices do not have the same
            number of columns and rows, there is no
            way they can be equivalent.
         */
        if(this.numCols() != otherMatrix.numCols() ||
                this.numRows() != otherMatrix.numRows()) {
            return false;
        }

        /*
            Iterate through the columns of each matrix and
            invoke the .equals() method of each of the
            column Vector objects to see if all of the
            values of the two matrices are the same and
            in the same order.
         */
        for(int i=0; i < this.numCols(); i++) {
            if(! this.getColumn(i).equals(otherMatrix.getColumn(i)))
                return false;
        }

        return true;
    }

    public String toString() {
        String str = "";

        for(int r = 1; r <= this.rows; r++) {
            for(int c = 0; c < this.cols; c++) {
                str += String.format("%.4f", this.matrix.get(c).getElement(r)) + "\t";
            }
            str += "\n";
        }

        return str;

    }
}
