package munk.graph.marching;

import javax.vecmath.Point3f;

public class Triangle {
	Point3f[] vertices = new Point3f[3];
	
	/**
	 * 
	 * @param index Must be between 0 and 2;
	 * @return A Point3f object with the coordinates of the vertice
	 */
	public Point3f getVertex(int index) {
		return vertices[index];
	}
	
	public void setVertex(int index, Point3f newVertex) {
		vertices[index] =  newVertex;
	}
	
	@Override
	public String toString() {
		String result = "[";
		for (Point3f p : vertices) {
			result += p + ", ";
		}
		return result.substring(0, result.length()-2) + "]";
	}
}