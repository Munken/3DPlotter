package munk.graph.plot.implicit;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.media.j3d.Shape3D;
import javax.vecmath.Point3f;

import munk.emesp.*;
import munk.emesp.exceptions.IllegalExpressionException;
import munk.graph.marching.*;
import munk.graph.marching.Triangle;
import munk.graph.plot.AbstractPlotter;

import com.sun.j3d.utils.geometry.*;

public abstract class AbstractImplicit extends AbstractPlotter implements ImplicitPlotter {
	
	private static Pattern PATTERN = Pattern.compile("([^=]+)=([^=]+)$");
	protected float xMin;
	protected int xLength;
	
	protected float yMin;
	protected int yLength;
	
	protected float zMin;
	protected int zLength;
	
	protected float xStepsize;
	protected float yStepsize;
	protected float zStepsize;
	
	private List<Point3f> triangles;
	
	public static final MarchingCubes MARCHER = new MarchingCubes();
	private static final float ISOLEVEL = 0;
	
	private Shape3D plot;
	
	private Expression ex;
	private VariableValues vm;
	
	public AbstractImplicit(String expression, 
							float xMin, float xMax, 
							float yMin, float yMax, 
							float zMin, float zMax, 
							float xStepsize, float yStepsize, float zStepsize) 
									
									throws IllegalExpressionException {
		
		expression = preParse(expression);
		this.xMin = xMin - xStepsize/2;
		this.yMin = yMin - yStepsize/2;
		this.zMin = zMin - zStepsize/2;
		
		this.xStepsize = xStepsize;
		this.yStepsize = yStepsize;
		this.zStepsize = zStepsize;
		
		xLength = (int) Math.ceil((xMax - xMin) / xStepsize) + 1;
		yLength = (int) Math.ceil((yMax - yMin) / yStepsize) + 1;
		zLength = (int) Math.ceil((zMax - zMin) / zStepsize) + 1;
		
		ex = ExpressionParser.parse(expression, FunctionMap.getDefault());
		vm = new VariableValues();
		vm.setValue("x", xMin);
		vm.setValue("y", yMin);
		vm.setValue("z", zMin);
		
		ex.ensureVariablesDefined(vm);
	}
	
	public Shape3D getPlot() {
		if (plot == null) {
			
			plot = plot();
			setShape(plot);
			triangles = null;
		}
		
		return (!isCancelled()) ? plot : null;
	}
	
	protected abstract Shape3D plot();
	
	protected Shape3D buildGeomtryFromTriangles() {
		return buildGeomtryFromTriangles(triangles);
	}
	
	protected Shape3D buildGeomtryFromTriangles(List<Point3f> vertices) {
		// Build geometry from triangles
		if (vertices.size() >= 3 && vertices.size() % 3 == 0) {
			GeometryInfo gi = new GeometryInfo(GeometryInfo.TRIANGLE_ARRAY);
			Point3f[] points = new Point3f[vertices.size()];
			gi.setCoordinates((Point3f[]) vertices.toArray(points));
			
			NormalGenerator ng = new NormalGenerator();
			ng.generateNormals(gi);
			Stripifier strip = new Stripifier();
			strip.stripify(gi);
					
			return new Shape3D(gi.getGeometryArray());
		} else 
			return null;
	}
	
	protected int marchCube(float[] values, Point3f[] corners, Triangle[] tri) {
		return MARCHER.marchCube(values, corners, tri, ISOLEVEL);
	}
	
	protected int marchCube(MarchCell cell, Triangle[] tri) {
		return MARCHER.marchCube(cell.getValues(), cell.getCorners(), tri, ISOLEVEL);
	}
	
	protected float[][] bottomLayerValues() {
		float[][] result = new float[yLength][xLength];
		
		float y = yMin;
		for (int j = 0; j < yLength; j++) {
			float x = xMin;
			for (int i = 0; i < xLength; i++) {
				result[j][i] = value(x, y, zMin);
				x += xStepsize;
			}
			y += yStepsize;
		}
		
		return result;
	}
	
	protected void calcEdges(float[][] upperValues, float z) {
		float y = yMin;
		float x = xMin;
		for (int j = 0; j < upperValues.length; j++) {
			upperValues[j][0] = value(x, y, z);
			y += yStepsize;
		}
		
		y = yMin;
		for (int i = 0; i < upperValues[0].length; i++) {
			upperValues[0][i] = value(x, y, z);
			x += xStepsize;
		}
	}
	
	protected float value(Point3f point) {
		return value(point.x, point.y, point.z);
	}
	
	protected float value(float x, float y, float z) {
		vm.setValue("x", x);
		vm.setValue("y", y);
		vm.setValue("z", z);
		
		return (float) ex.eval(vm);
//		return x*x + y*y + z*z - 1;
	}
	
	protected void addTriangles(int nFacets, Triangle[] newTriangles) {
		if (triangles == null)
			triangles = new ArrayList<Point3f>(30000);
		
		addTriangles(nFacets, newTriangles, triangles);
	}
	
	protected void addTriangles(int nFacets, Triangle[] newTriangles, List<Point3f> list) {
		for (int q = 0; q < nFacets; q++) {
			Triangle tri = newTriangles[q];
			for (Point3f vertex : tri) {
				Point3f newVertex = (Point3f) vertex.clone();
				list.add(newVertex);
			}
		}
	}
	
	protected Point3f[] initPoint3fArray(int N) {
		Point3f[] result = new Point3f[N];
		for (int q = 0; q < N; q++) {
			result[q] = new Point3f();
		}
		return result;
	}
	
	protected Triangle[] initTriangleArray(int N) {
		Triangle[] result = new Triangle[N];
		for (int q = 0; q < N; q++) {
			result[q] = new Triangle();
		}
		return result;
	}
	
	protected String preParse(String expr) throws IllegalExpressionException {
		Matcher m = PATTERN.matcher(expr);
		boolean matches = m.matches();
		if (!matches)
			throw new IllegalExpressionException ("The expression must be of the form <Expression> = <Expression>");
		
		String lhs = m.group(1).trim();
		String rhs = m.group(2).trim();
		
		return lhs + "-(" + rhs + ")";
	}

	protected float getxMin() {
		return xMin;
	}

	protected int getxLength() {
		return xLength;
	}

	protected float getyMin() {
		return yMin;
	}

	protected int getyLength() {
		return yLength;
	}

	protected float getzMin() {
		return zMin;
	}

	protected int getzLength() {
		return zLength;
	}

	protected float getxStepsize() {
		return xStepsize;
	}

	protected float getyStepsize() {
		return yStepsize;
	}

	protected float getzStepsize() {
		return zStepsize;
	}

}
