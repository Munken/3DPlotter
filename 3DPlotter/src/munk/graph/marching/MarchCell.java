package munk.graph.marching;

import javax.vecmath.Point3f;

public class MarchCell extends GridCell {

	int marchFace;
	int x;
	int y;
	int z;
	
	public MarchCell(Point3f[] corners, float[] values, int x, int y, int z, int marchface) {
		super(corners, values);
		this.marchFace = marchface;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public MarchCell(Point3f[] corners, float[] values, int x, int y, int z) {
		this(corners, values, x, y, z, -1);
	}
	
	public int getMarchFace() {
		return marchFace;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}
	
	@Override
	public String toString() {
		return marchFace + "[" + x + ", " + y + ", " + z + "]"; 
	}
	

}
