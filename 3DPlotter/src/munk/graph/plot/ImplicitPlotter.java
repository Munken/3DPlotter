package munk.graph.plot;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.media.j3d.Shape3D;
import javax.vecmath.Point3f;

import munk.graph.marching.*;

import com.graphbuilder.math.*;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;

public class ImplicitPlotter {
	
	private static Pattern PATTERN = Pattern.compile("([^=]+)=([^=]+)$");
	private float xMin;
	private int xLength;
	
	private float yMin;
	private int yLength;
	
	private float zMin;
	private int zLength;
	
	private float stepsize;
	private Shape3D plot;
	
	private Expression ex;
	private VarMap vm;
	private FuncMap fm;
	
	
	public ImplicitPlotter(String expr, float xMin, float xMax, float yMin,
			float yMax, float zMin, float zMax, float stepsize) throws ExpressionParseException {
		
		expr = preParse(expr);
		this.xMin = xMin;
		this.yMin = yMin;
		this.zMin = zMin;
		
		xLength = (int) Math.ceil((xMax - xMin) / stepsize) + 1;
		yLength = (int) Math.ceil((yMax - yMin) / stepsize) + 1;
		zLength = (int) Math.ceil((zMax - zMin) / stepsize) + 1;
		
		this.stepsize = stepsize;
		
		ex = ExpressionTree.parse(expr);
		vm = new VarMap();
		vm.setValue("x", xMin);
		vm.setValue("y", yMin);
		vm.setValue("z", zMin);
		
		fm = new FuncMap();
		fm.loadDefaultFunctions();
	}

	private String preParse(String expr) {
		Matcher m = PATTERN.matcher(expr);
		boolean matches = m.matches();
		if (!matches)
			throw new IllegalStateException("The expression must be of the form <Expression> = <Expression>");
		
		String lhs = m.group(1).trim();
		String rhs = m.group(2).trim();
		
		return lhs + "-(" + rhs + ")";
	}

	public Shape3D getPlot() {
		if (plot == null) {
			plot = plot();
		}
		return plot;
	}

	private Shape3D plot() {
		MarchingCubes m = new MarchingCubes();

		Point3f[] corners = initPoint3fArray(8);
		float[] values = new float[8];
		
		GridCell grid = new GridCell(corners, values);
		Triangle[] tri = initTriangleArray(MarchingCubes.MAX_TRIANGLES_RETURNED);

		
		
		float z = zMin;
		
		float[][] lower = bottomLayerValues();
		float[][] upper = new float[yLength][xLength];
		
		List<Point3f> triangles = new ArrayList<Point3f>(3000);
		for (int k = 0; k < zLength - 1; k++) {
		
			calcEdges(upper, z+stepsize);
			float y = yMin;
			for (int j = 0; j < yLength - 1; j++) {
				
				float x = xMin;
				for (int i = 0; i < xLength - 1; i++) {
					
					corners[0].x = x;
					corners[0].y = y;
					corners[0].z = z;
					values[0] = lower[j][i];
					
					corners[1].x = x + stepsize;
					corners[1].y = y;
					corners[1].z = z; 
					values[1] = lower[j][i+1];
					
					corners[2].x = x + stepsize;
					corners[2].y = y + stepsize;
					corners[2].z = z;
					values[2] = lower[j+1][i+1];
					
					corners[3].x = x;
					corners[3].y = y + stepsize;
					corners[3].z = z;
					values[3] = lower[j+1][i];
					
					corners[4].x = x;
					corners[4].y = y;
					corners[4].z = z + stepsize;
					values[4] = upper[j][i];
					
					corners[5].x = x + stepsize;
					corners[5].y = y;
					corners[5].z = z + stepsize;
					values[5] = upper[j][i+1];
					
					corners[6].x = x + stepsize;
					corners[6].y = y + stepsize;
					corners[6].z = z + stepsize;
					values[6] = value(corners[6]);
					upper[j+1][i+1] = values[6];
					
					corners[7].x = x;
					corners[7].y = y + stepsize;
					corners[7].z = z + stepsize;
					values[7] = upper[j+1][i];					
					
					int facets = m.marchCube(grid, tri, 0);
					
					if (facets > 0) {
						for (int q = 0; q < facets; q++) {
							addVerticesToList(tri[q], triangles);
						}
					}
					
					x += stepsize;
				}
				
				y += stepsize;
			}
			
			float[][] tmp = upper;
			upper = lower;
			lower = tmp;
			
			z += stepsize;
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

	private Point3f[] initPoint3fArray(int N) {
		Point3f[] result = new Point3f[N];
		for (int q = 0; q < N; q++) {
			result[q] = new Point3f();
		}
		return result;
	}

	private Triangle[] initTriangleArray(int N) {
		Triangle[] result = new Triangle[N];
		for (int q = 0; q < N; q++) {
			result[q] = new Triangle();
		}
		return result;
	}
	
	private float value(Point3f point) {
		return value(point.x, point.y, point.z);
	}
	
	private float value(float x, float y, float z) {
		vm.setValue("x", x);
		vm.setValue("y", y);
		vm.setValue("z", z);
		
		return (float) ex.eval(vm, fm);
//		return x*x + y*y + z*z - 1;
	}
	
	private void addVerticesToList(Triangle tri, List<Point3f> list) {
		for (Point3f vertex : tri) {
			Point3f newVertex = (Point3f) vertex.clone();
			list.add(newVertex);
		}
	}
	
	private float[][] bottomLayerValues() {
		float[][] result = new float[yLength][xLength];
		
		float y = yMin;
		for (int j = 0; j < yLength; j++) {
			float x = xMin;
			for (int i = 0; i < xLength; i++) {
				result[j][i] = value(x, y, zMin);
				x += stepsize;
			}
			y += stepsize;
		}
		
		return result;
	}
	
	private void calcEdges(float[][] upperValues, float z) {
		float y = yMin;
		float x = xMin;
		for (int j = 0; j < upperValues.length; j++) {
			upperValues[j][0] = value(x, y, z);
			y += stepsize;
		}
		
		y = yMin;
		for (int i = 0; i < upperValues[0].length; i++) {
			upperValues[0][i] = value(x, y, z);
			x += stepsize;
		}
	}
		


}
