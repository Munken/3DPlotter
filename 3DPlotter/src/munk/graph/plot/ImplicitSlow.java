package munk.graph.plot;

import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.Shape3D;
import javax.vecmath.Point3f;

import munk.graph.marching.MarchingCubes;
import munk.graph.marching.Triangle;

import com.graphbuilder.math.ExpressionParseException;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;

public class ImplicitSlow extends AbstractImplicit{
	
	public ImplicitSlow(String expr, float xMin, float xMax, float yMin,
			float yMax, float zMin, float zMax, float stepsize) throws ExpressionParseException {
		super(expr, xMin, xMax, yMin, yMax, zMin, zMax, stepsize, stepsize, stepsize);
	}

	protected Shape3D plot() {

		Point3f[] corners = initPoint3fArray(8);
		float[] values = new float[8];
		
		Triangle[] tri = initTriangleArray(MarchingCubes.MAX_TRIANGLES_RETURNED);

		
		
		float z = getzMin();
		
		float[][] lower = bottomLayerValues();
		float[][] upper = new float[yLength][xLength];
		
		List<Point3f> triangles = new ArrayList<Point3f>(3000);
		for (int k = 0; k < zLength - 1; k++) {
		
			calcEdges(upper, z+zStepsize);
			float y = yMin;
			for (int j = 0; j < yLength - 1; j++) {
				
				float x = xMin;
				for (int i = 0; i < xLength - 1; i++) {
					
					corners[0].x = x;
					corners[0].y = y;
					corners[0].z = z;
					values[0] = lower[j][i];
					
					corners[1].x = x + xStepsize;
					corners[1].y = y;
					corners[1].z = z; 
					values[1] = lower[j][i+1];
					
					corners[2].x = x + xStepsize;
					corners[2].y = y + yStepsize;
					corners[2].z = z;
					values[2] = lower[j+1][i+1];
					
					corners[3].x = x;
					corners[3].y = y + yStepsize;
					corners[3].z = z;
					values[3] = lower[j+1][i];
					
					corners[4].x = x;
					corners[4].y = y;
					corners[4].z = z + zStepsize;
					values[4] = upper[j][i];
					
					corners[5].x = x + xStepsize;
					corners[5].y = y;
					corners[5].z = z + zStepsize;
					values[5] = upper[j][i+1];
					
					corners[6].x = x + xStepsize;
					corners[6].y = y + yStepsize;
					corners[6].z = z + zStepsize;
					values[6] = value(corners[6]);
					upper[j+1][i+1] = values[6];
					
					corners[7].x = x;
					corners[7].y = y + yStepsize;
					corners[7].z = z + zStepsize;
					values[7] = upper[j+1][i];					
					
					int nFacets = marchCube(values, corners, tri);
					
					if (nFacets > 0) {
						addTriangles(nFacets, tri);
					}
					
					x += xStepsize;
				}
				
				y += yStepsize;
			}
			
			float[][] tmp = upper;
			upper = lower;
			lower = tmp;
			
			z += zStepsize;
		}
		
		// Build geometry from triangles
		if (triangles.size() >= 3) {
			GeometryInfo gi = new GeometryInfo(GeometryInfo.TRIANGLE_ARRAY);
			Point3f[] points = new Point3f[triangles.size()];
			gi.setCoordinates((Point3f[]) triangles.toArray(points));
			NormalGenerator ng = new NormalGenerator();
			ng.generateNormals(gi);
			return new Shape3D(gi.getGeometryArray());
		} else 
			return null;
	}


}
