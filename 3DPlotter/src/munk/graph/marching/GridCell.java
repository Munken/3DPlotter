package munk.graph.marching;

import javax.vecmath.Point3f;

public class GridCell {
	Point3f[] points;
	float[] values;

	/**
	 * 
	 * @param corners The corners of the cube. Length must be 8!
	 * @param values The values of these corners. Length must be 8!
	 */
	public GridCell(Point3f[] corners, float[] values) {
		this.points = corners;
		this.values = values;
	}
}


