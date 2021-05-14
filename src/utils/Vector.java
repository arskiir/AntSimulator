package utils;

public class Vector {

	double x;
	double y;
	double z;

	/** Constructor to create a null vector i.e. 0i+0j+0k */
	public Vector() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	/** Constructor to create a vector with the given values of i,j and k */
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/** Constructor to create a 2D vector with the given angle and length */
	public Vector(double angle, double length) {
		this.z = 0d;
		this.x = Math.cos(angle) * length;
		this.y = Math.sin(angle) * length;
	}

	/** Return the string notation of the Vector */
	public String getCompleteStringNotation() {
		return this.x + "i^+" + this.y + "j^+" + this.z + "k^";
	}

	/**
	 * Return the string notation of the Vector.Reduced it if any of x,y and z are
	 * integers
	 */
	public String getReducedStringNotation() {
		String not = "";
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

	/** Gives the length of the vector */
	public double modulus() {
		return Math.sqrt(Math.hypot(x, Math.hypot(y, z)));
	}

	/** Check if two vectors are parallel or not */
	public boolean isParallel(Vector v) {
		double xr = this.x / v.x;
		double yr = this.y / v.y;
		double zr = this.z / v.z;
		return (xr == yr && yr == zr && zr == xr);
	}

	/** Returns the unit vector in the direction of the Calling vector object */
	public Vector getUnitVector() {
		double mod = this.modulus();
		return new Vector(this.x / mod, this.y / mod, this.z / mod);
	}

	/** Computes and return the dot product between two vector */
	public double dotProduct(Vector v) {
		return this.x * v.x + this.y * v.y + this.z * v.z;
	}

	/** Compute and returns the angle between two vectors in radians */
	public double angleBetween(Vector v) {
		double dotPro = this.dotProduct(v);
		double modObj = this.modulus();
		double modV = v.modulus();
		return Math.acos(dotPro / (modObj * modV));
	}

	/** Computes and returns the cross product of the two vector */
	public Vector crossProduct(Vector v) {
		double resx = (this.y * v.z) - (v.y * this.z);
		double resy = -((this.x * v.z) - (v.x * this.z));
		double resz = (this.x * v.y) - (v.x * this.y);
		return new Vector(resx, resy, resz);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}

}