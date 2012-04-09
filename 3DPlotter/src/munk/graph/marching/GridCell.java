package munk.graph.marching;

import javax.vecmath.Point3f;
/**
 * The progression of vertices is clockwise around the bottom face
 * and then clockwise around the top face of the cube.  Edge 0 goes from
 * vertex 0 to vertex 1, Edge 1 is from 2->3 and so on around clockwise to
 * vertex 0 again. Then Edge 4 to 7 make up the top face, 4->5, 5->6, 6->7
 * and 7->4.  Edge 8 thru 11 are the vertical edges from vert 0->4, 1->5,
 * 2->6, and 3->7.
 *     4--------5     *---4----*
 *    /|       /|    /|       /|
 *   / |      / |   7 |      5 |
 *  /  |     /  |  /  8     /  9
 * 7--------6   | *----6---*   |
 * |   |    |   | |   |    |   |
 * |   0----|---1 |   *---0|---*
 * |  /     |  /  11 /     10 /
 * | /      | /   | 3      | 1
 * |/       |/    |/       |/
 * 3--------2     *---2----*
 * @author Munk
 *
 */
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


