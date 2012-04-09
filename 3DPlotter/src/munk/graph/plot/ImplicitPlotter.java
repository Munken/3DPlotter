package munk.graph.plot;

import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.Shape3D;
import javax.media.j3d.TriangleArray;
import javax.vecmath.Point3f;

import munk.graph.marching.*;

public class ImplicitPlotter {
	
	private float xMin;
	private float xMax;
	private float yMin;
	private float yMax;
	private float zMin;
	private float zMax;
	private float stepsize;
	private Shape3D plot;
	
	
	public ImplicitPlotter(float xMin, float xMax, float yMin, float yMax,
			float zMin, float zMax, float stepsize) {
		super();
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		this.zMin = zMin;
		this.zMax = zMax;
		this.stepsize = stepsize;
	}

	public Shape3D getPlot() {
		if (plot == null) {
			plot = plot();
		}
		return plot;
	}

	private Shape3D plot() {
		MarchingCubes m = new MarchingCubes();

		Point3f[] corners = new Point3f[8];
		float[] values = new float[8];
		Triangle[] tri = new Triangle[5];

		for (int q = 0; q < tri.length; q++) {
			tri[q] = new Triangle();
		}
		for (int q = 0; q < corners.length; q++) {
			corners[q] = new Point3f();
		}
		
		List<Triangle> triangles = new ArrayList<Triangle>();
		for (float z = zMin; z <= zMax; z += stepsize) {
			for (float y = yMin; y <= yMax; y += stepsize) {
				for (float x = xMin; x < xMax; x += stepsize) {
					corners[0].x = x;
					corners[0].y = y;
					corners[0].z = z;
					values[0] = value(corners[0]);
					
					corners[1].x = x+stepsize;
					corners[1].y = y;
					corners[1].z = z; 
					values[1] = value(corners[1]);
					
					corners[2].x = x+ stepsize;
					corners[2].y = y+ stepsize;
					corners[2].z = z;
					values[2] = value(corners[2]);
					
					corners[3].x = x;
					corners[3].y = y+ stepsize;
					corners[3].z = z;
					values[3] = value(corners[3]);
					
					corners[4].x = x;
					corners[4].y = y;
					corners[4].z = z+ stepsize;
					values[4] = value(corners[4]);
					
					corners[5].x = x+ stepsize;
					corners[5].y = y;
					corners[5].z = z+ stepsize;
					values[5] = value(corners[5]);
					
					corners[6].x = x+ stepsize;
					corners[6].y = y+ stepsize;
					corners[6].z = z+ stepsize;
					values[6] = value(corners[6]);
					
					corners[7].x = x;
					corners[7].y = y+ stepsize;
					corners[7].z = z+ stepsize;
					values[7] = value(corners[7]);
					
					GridCell grid = new GridCell(corners, values);
					
					int facets = m.Pologynise(grid, tri, 0);
					
					if (facets > 0) {
						for (int i = 0; i < facets; i++) {
							Triangle newTri = new Triangle(tri[i]);
							triangles.add(newTri);
						}
					}
				}
			}
		}
		TriangleArray triArray = new TriangleArray(3*triangles.size(), TriangleArray.COORDINATES);
		int vertice = 0;
		for (Triangle t : triangles) {
			for (int i = 0; i < 3; i++) {
				triArray.setCoordinate(vertice++, t.getVertex(i));
			}
		}
		
		
		return new Shape3D(triArray);
	}
	
	private float value(Point3f point) {
		return point.x*point.x + point.y*point.y + point.z*point.z - 1;
	}

}
