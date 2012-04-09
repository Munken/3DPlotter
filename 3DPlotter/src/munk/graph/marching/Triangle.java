package munk.graph.marching;

import javax.vecmath.Point3f;

public class Triangle {
	Point3f[] vertices = new Point3f[3];
	
	@Override
	public String toString() {
		String result = "[";
		for (Point3f p : vertices) {
			result += p + ", ";
		}
		return result.substring(0, result.length()-2) + "]";
	}
}