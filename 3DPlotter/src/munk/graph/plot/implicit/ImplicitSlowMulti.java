package munk.graph.plot.implicit;

import java.util.*;
import java.util.concurrent.*;

import javax.media.j3d.Shape3D;
import javax.vecmath.Point3f;

import munk.graph.function.IllegalEquationException;
import munk.graph.marching.MarchingCubes;
import munk.graph.marching.Triangle;

import com.graphbuilder.math.*;

public class ImplicitSlowMulti extends AbstractImplicit {
	
	private static final FuncMap FUNC_MAP;
	static {
		FUNC_MAP = new FuncMap();
		FUNC_MAP.loadDefaultFunctions();
	}
	
	private String expression;
	
	
	public ImplicitSlowMulti(String expression, float xMin, float xMax,
			float yMin, float yMax, float zMin, float zMax, float xStepsize,
			float yStepsize, float zStepsize) 
					throws ExpressionParseException,
						    IllegalEquationException, 
						    UndefinedVariableException {
		super(expression, xMin, xMax, yMin, yMax, zMin, zMax, xStepsize, yStepsize,
				zStepsize);
		
		this.expression = preParse(expression);
	}
	
	public ImplicitSlowMulti(String expression, float[] bounds, float[] stepsizes) 
					throws ExpressionParseException,
						    IllegalEquationException, 
						    UndefinedVariableException {
		this(expression, 
				bounds[0], bounds[1], bounds[2], 
				bounds[3], bounds[4], bounds[5], 
				stepsizes[0], stepsizes[1], stepsizes[2]);
	}



	@Override
	protected Shape3D plot() {
		int N = 2;
		List<Future<List<Point3f>>> future = new ArrayList<Future<List<Point3f>>>();
		ExecutorService threadPool = Executors.newFixedThreadPool(4);
		
		
		int zSteps = N + zLength / N + zLength % N;	float zStart = zMin;		
		for (int i = 0; i < N; i++) {
			
			int ySteps = N + yLength / N + yLength % N;	float yStart = yMin;			
			for (int j = 0; j < N; j++) {
				
				int xSteps = N + xLength / N + xLength % N;	float xStart = xMin;				
				for (int k = 0; k < N; k++) {

					Callable<List<Point3f>> callable = createSubMarcher(xStart, yStart, zStart, xSteps, ySteps, zSteps);
					Future<List<Point3f>> f = threadPool.submit(callable);
					future.add(f);

//					xStart = nextStartValue(xStart, xStepsize, xSteps);
					xStart += (xSteps - 1) * xStepsize;
					xSteps = xLength / N;
				}
				
//				yStart = nextStartValue(yStart, yStepsize, ySteps);
				yStart += (ySteps - 1) * yStepsize;				
				ySteps = yLength / N;
			}
			
//			zStart = nextStartValue(zStart, zStepsize, zSteps);
			zStart += (zSteps - 1) * zStepsize;			
			zSteps = zLength / N;
			
		}

		List<Point3f> result = concatResults(future);
		return buildGeomtryFromTriangles(result);
	}

	private List<Point3f> concatResults(List<Future<List<Point3f>>> future) {
		List<List<Point3f>> results = new ArrayList<List<Point3f>>(future.size());
		int totalSize = 0; 
		
		for (Future<List<Point3f>> f : future) {
			
			try {
				List<Point3f> result = f.get();
				results.add(result);
				
				totalSize += result.size();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		List<Point3f> points = new ArrayList<Point3f>(totalSize);
		
		for (List<Point3f> list : results) {
			points.addAll(list);
		}
		
//		Collections.sort(points, createPoint3fComparator());
		
		return points;
	}

	private Callable<List<Point3f>> createSubMarcher(final float xStart, final float yStart, final float zStart, 
			final int xSteps, final int ySteps,  final int zSteps) {
		
		return new Callable<List<Point3f>>() {
			
			@Override
			public List<Point3f> call() throws Exception {
				SubCubeMarcher scm = new SubCubeMarcher(expression, xStart, yStart, zStart, xSteps, ySteps, zSteps);
				return scm.marchCubes();
			}
		};
	}
	
	
	
	private class SubCubeMarcher {
		
		Expression expr;
		VarMap vm;
		float xStart, yStart, zStart;
		int xLength, yLength, zLength;

		
		private SubCubeMarcher(String expression, float xStart, float yStart, float zStart,
				int xLength, int yLength, int zLength) {
			this.xStart = xStart;
			this.yStart = yStart;
			this.zStart = zStart;
			this.xLength = xLength;
			this.yLength = yLength;
			this.zLength = zLength;
			
			try {
				vm = new VarMap();
				expr = ExpressionTree.parse(expression);
			} catch(ExpressionParseException e) {
				// The
			}
		}
		
		private List<Point3f> marchCubes() {
			Point3f[] corners = initPoint3fArray(8);
			float[] values = new float[8];
			
			Triangle[] tri = initTriangleArray(MarchingCubes.MAX_TRIANGLES_RETURNED);
			List<Point3f> points = new ArrayList<Point3f>(20000);
			
			
			float z = zStart;
			
			float[][] lower = bottomLayerValues();
			float[][] upper = new float[yLength][xLength];
			
			for (int k = 0; k < zLength - 1; k++) {
			
				calcEdges(upper, z+zStepsize);
				float y = yStart;
				for (int j = 0; j < yLength - 1; j++) {
					
					float x = xStart;
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
							addTriangles(nFacets, tri, points);
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
			
			return points;
		}
		
		private float value() {
			return (float) expr.eval(vm, FUNC_MAP);
		}
		
		private float value(Point3f point) {
			return value(point.x, point.y, point.z);
		}
		
		private float value(float x, float y, float z) {
			setAllVariable(x, y, z);
			return value();
		}
		
		private void setVariable(String name, float value) {
			vm.setValue(name, value);
		}
		
		private void setAllVariable(float x, float y, float z) {
			vm.setValue("x", x);
			vm.setValue("y", y);
			vm.setValue("z", z);
		}
		
		protected float[][] bottomLayerValues() {
			float[][] result = new float[yLength][xLength];
			setVariable("z", zStart);
			
			float y = yStart;
			setVariable("y", y);
			for (int j = 0; j < yLength; j++) {
				float x = xStart;
				
				for (int i = 0; i < xLength; i++) {
					setVariable("x", x);
//					setVariable("y", y);
//					result[j][i] = value(x, y, zStart);
					result[j][i] = value();
					x += xStepsize;
				}
				y += yStepsize;
				setVariable("y", y);
			}
			
			return result;
		}
		
		protected void calcEdges(float[][] upperValues, float z) {
			float y = yStart;
			float x = xStart;
			setAllVariable(x, y, z);
			for (int j = 0; j < upperValues.length; j++) {
				upperValues[j][0] = value();
				y += yStepsize;
				setVariable("y", y);
			}
			
			y = yStart;
			setVariable("y", y);
			for (int i = 0; i < upperValues[0].length; i++) {
				upperValues[0][i] = value();
				x += xStepsize;
				setVariable("x", x);
			}
		}
		
	}
}
