package munk.graph.marching;

import javax.vecmath.Point3f;

public class MarchCell extends GridCell {
	
	public static final int FACE_0 = 0;
	public static final int FACE_1 = 1;
	public static final int FACE_2 = 2;
	public static final int FACE_3 = 3;
	public static final int FACE_4 = 4;
	public static final int FACE_5 = 5;
	public static final int STARTING_CELL = -1;

	private int marchFace;
	private int x;
	private int y;
	private int z;
	
	public MarchCell(Point3f[] corners, float[] values, int x, int y, int z, int marchface) {
		super(corners, values);
		this.marchFace = marchface;
		this.x = x;
		this.y = y;
		this.z = z;
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
		return marchFace + ": [" + x + ", " + y + ", " + z + "]"; 
	}
	

}
