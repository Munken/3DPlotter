package munk.graph;

import javax.vecmath.Point3f;


public class Test {


	public static void main(String[] args) {

	}
	
	private static float value(Point3f point) {
		return point.x*point.x + point.y*point.y + point.z*point.z - 1;
	}
}