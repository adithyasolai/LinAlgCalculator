import java.util.ArrayList;

public class Vector {
    private int size;
    private ArrayList<Double> values;

    public Vector() {
        this.size = 0;
        values = new ArrayList<Double>();
    }

    /*
        Initializes a Vector of dimension `size`
        with all 0s. The zero vector.
     */
    public Vector(int size) {
        this.size = size;
        values = new ArrayList<Double>();
        for(int i=0; i<size; i++)
            values.add(0.0);
    }

    public Vector(Vector other) {
        this.values = new ArrayList<Double>();
        this.values.addAll(other.values);
        this.size = other.size;
    }

    public Vector(double[] values) {
        this.values = new ArrayList<Double>();

        for(int i=0; i<values.length; i++)
            this.values.add(values[i]);

        this.size = this.values.size();
    }


    public int getSize() {
        return this.size;
    }

    /*
        `pos` doesn't follow the indexing conventions
        of ArrayList, so `pos`-1 is used,
     */
    public double getElement(int pos) {
        return values.get(pos-1);
    }

    public void addElement(double value) {
        this.values.add(value);
        this.size++;
    }

    /*
        `position` must be a valid spot, can not
        be greater than the size of the current
        vector.

        `position` does not follow the same
        indexing as the ArrayList used to
        represent the Vector.
     */
    public void setElement(int position, double value) {
        if(position >= 1 && position <= this.size) {
            /*
                -1 adjusts for the indexing used by
                ArrayList.
             */
            this.values.set(position-1, value);
        }
    }

    /*
        Since an ArrayList is used to represent the
        vector, calling the remove() function should
        appropriately shrink the ArrayList's size.
    */
    public void removeElement(int position) {
        if (position >= 1 && position <= this.size) {
            this.values.remove(position - 1);
            this.size--;
        }
    }

    public double[] getValuesArray() {
        double[] valuesArray = new double[this.size];

        for(int i=0; i < valuesArray.length; i++) {
            valuesArray[i] = this.getElement(i);
        }

        return valuesArray;
    }

    /*
        Do a deep copy.
     */
    public ArrayList<Double> getValues() {
        ArrayList<Double> arrayCopy = new ArrayList<Double>();

        for(int i=0; i < this.size; i++) {
            arrayCopy.add(this.getElement(i));
        }

        return arrayCopy;
    }



    /*
        BELOW ARE THE equals() AND
        toString() METHODS THAT
        OVERRIDE THE OBJECT CLASS
        EQUIVALENTS OF THOSE METHODS.
     */
    @Override
    public boolean equals(Object obj) {
        /*
            If referring to the same
            object, just return true.
         */
        if(this == obj)
            return true;
        /*
            If the other object is not
            even a Vector type, return
            false.
         */
        if(!(obj instanceof Vector))
            return false;

        /*
            The other object is a Vector
            type, so it can be cast as a
            Vector object.
         */
        Vector other = (Vector) obj;

        /*
            If the two Vectors do not have
            the same number of elements,
            there is no way they can be equal.
         */
        if(other.getSize() != this.getSize())
            return false;

        /*
            Iterate through the elements of
            both Vectors and ensure that they
            have the same elements in the
            SAME ORDER.
         */
        for(int i=0; i < other.getSize(); i++) {
            if(this.getElement(i) != other.getElement(i))
                return false;
        }

        return true;
    }

    public String toString() {
        String str="[";
        if(this.size > 0) {
            for(int i=1; i < this.size; i++)
                str += String.format("%.4f", this.getElement(i)) + " ";
            str += this.getElement(this.size);
        }
        str+="]";
        return str;
    }
}
