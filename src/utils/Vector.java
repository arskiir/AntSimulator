package utils;

/**
 * The Class Vector. Simple mathematical vector class.
 */
public class Vector {

	/** The x component. */
	double x;
	
	/** The y component. */
	double y;
	
	/** The z component. */
	double z;

	/** Constructor to create a null vector i.e. 0i+0j+0k */
	public Vector() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	/**
	 *  Constructor to create a vector with the given values of i,j and k.
	 *
	 * @param x the x component
	 * @param y the y component
	 * @param z the z component
	 */
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 *  Copy constructor.
	 *
	 * @param v the vector to be copied
	 */
	public Vector(Vector v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}

	/**
	 * Constructor to create a 2D vector with the given angle in degree and length.
	 *
	 * @param angle the angle in degree
	 * @param length the length of the vector
	 * @return the vector
	 */
	public static Vector createVector2FromAngle(double angle, double length) {
		return new Vector(Math.cos(angle * Math.PI / 180) * length, Math.sin(angle * Math.PI / 180) * length, 0);
	}

	/**
	 * Reverse Y.
	 */
	public void reverseY() {
		this.y = this.y * -1;
	}

	/**
	 * Reverse X.
	 */
	public void reverseX() {
		this.x = this.x * -1;
	}

	/**
	 * Reverse Z.
	 */
	public void reverseZ() {
		this.z = this.z * -1;
	}

	/**
	 *  Return the string notation of the Vector.
	 *
	 * @return the complete string notation
	 */
	public String getCompleteStringNotation() {
		return this.x + "i + " + this.y + "j + " + this.z + "k";
	}

	/**
	 * Return the string notation of the Vector.Reduced it if any of x,y and z are
	 * integers
	 *
	 * @return the reduced string notation
	 */
	public String getReducedStringNotation() {
		var not = "";
		if (this.x != 0) {
			if (this.x == (int) this.x)
				not = not + (int) this.x + "i^+";
			else
				not = not + this.x + "i^+";
		}
		if (this.y != 0) {
			if (this.y == (int) this.y)
				not = not + (int) this.y + "j^+";
			else
				not = not + this.y + "j^+";
		}

		if (this.z != 0) {
			if (this.z == (int) this.z)
				not = not + (int) this.z + "k^";
			else
				not = not + this.z + "k^";
		}

		if (not.equals(""))
			return null;

		if (not.charAt(not.length() - 1) == '+')
			not = not + "\b";

		return not;
	}

	/**
	 *  Gives the length of the vector.
	 *
	 * @return the length of the vector
	 */
	public double modulus() {
		return Math.sqrt(Math.hypot(x, Math.hypot(y, z)));
	}

	/**
	 *  Check if two vectors are parallel or not.
	 *
	 * @param v the vector to be compared
	 * @return true, if is parallel
	 */
	public boolean isParallel(Vector v) {
		double xr = this.x / v.x;
		double yr = this.y / v.y;
		double zr = this.z / v.z;
		return (xr == yr && yr == zr && zr == xr);
	}

	/**
	 *  Returns the unit vector in the direction of the Calling vector object.
	 *
	 * @return the unit vector
	 */
	public Vector getUnitVector() {
		double mod = this.modulus();
		return new Vector(this.x / mod, this.y / mod, this.z / mod);
	}

	/**
	 *  Computes and return the dot product between two vector.
	 *
	 * @param v the vector to be dotted with
	 * @return the double
	 */
	public double dotProduct(Vector v) {
		return this.x * v.x + this.y * v.y + this.z * v.z;
	}

	/**
	 *  Compute and returns the angle between two vectors in radians.
	 *
	 * @param v the vector to be compared with
	 * @return the angle between two vectors in radians
	 */
	public double angleBetween(Vector v) {
		double dotPro = this.dotProduct(v);
		double modObj = this.modulus();
		double modV = v.modulus();
		return Math.acos(dotPro / (modObj * modV));
	}

	/**
	 *  Computes and returns the cross product of the two vector.
	 *
	 * @param v the vector to be crossed with
	 * @return the vector from the cross product
	 */
	public Vector crossProduct(Vector v) {
		double resx = (this.y * v.z) - (v.y * this.z);
		double resy = -((this.x * v.z) - (v.x * this.z));
		double resz = (this.x * v.y) - (v.x * this.y);
		return new Vector(resx, resy, resz);
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y component
	 */
	public double getY() {
		return y;
	}

	/**
	 * Gets the z.
	 *
	 * @return the z component
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Sets the x.
	 *
	 * @param x the new x component
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Sets the y.
	 *
	 * @param y the new y component
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Sets the z.
	 *
	 * @param z the new z component
	 */
	public void setZ(double z) {
		this.z = z;
	}

	/**
	 *  returns the angle in degree between x and y.
	 *
	 * @return the angle in degree
	 */
	public double getAngle() {
		final double angle = Math.atan(this.y / this.x) * 180 / Math.PI;
		if ((this.x < 0 && this.y > 0) || (this.x < 0 && this.y < 0)) {
			// vector points in the 2nd or the 3rd quadrant
			// NOTE: the returned angle of Math.atan is in the range -pi/2 through pi/2. 
			return angle + 180;
		}
		return angle;
	}

}