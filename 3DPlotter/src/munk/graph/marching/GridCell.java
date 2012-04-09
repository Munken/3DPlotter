package munk.graph.marching;

import javax.vecmath.Point3f;

public class GridCell {
	Point3f[] points;
	float[] values;

	public GridCell(Point3f[] corners, float[] values) {

		if (corners.length < 8 || values.length < 8)
			throw new IllegalStateException("A cube has 8 corners");

		this.points = corners;
		this.values = values;
	}
}


