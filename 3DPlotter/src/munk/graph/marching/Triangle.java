package munk.graph.marching;

import java.util.Iterator;

import javax.vecmath.Point3f;

public class Triangle implements Iterable<Point3f>{
	Point3f[] vertices;
	
	public Triangle(Triangle triangle) {
		this();
		for (int i = 0; i < vertices.length; i++) {
			Point3f newVertex = new Point3f();
			Point3f	otherVertex = triangle.vertices[i];
			
			newVertex.x = otherVertex.x;
			newVertex.y = otherVertex.y;
			newVertex.z = otherVertex.z;
			vertices[i] = newVertex;
		}
	}
	
	public Triangle() {
		vertices = new Point3f[3];
	}

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

	@Override
	public Iterator<Point3f> iterator() {
		return new Iterator<Point3f>() {
			int index = 0;
			
			@Override
			public boolean hasNext() {
				return index < vertices.length;
			}

			@Override
			public Point3f next() {
				return vertices[index++];
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}
}