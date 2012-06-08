package munk.graph.plot.implicit;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import javax.media.j3d.Shape3D;
import javax.vecmath.Point3f;

import munk.graph.function.IllegalEquationException;
import munk.graph.marching.*;

import com.graphbuilder.math.*;

public abstract class ImplicitMulti extends AbstractImplicit{
	
	private int startX;
	private int startY;
	private int startZ;
	
	private Triangle[] newTriangles;
	private boolean[][][] visited;
	
	private static final MarchingCubes MARCHER = new MarchingCubes();
	private static final float ISOLEVEL = 0;
	
//	private List<MarchCell> cells;
	private Queue<MarchCell> cells;

	private AtomicInteger nThreadsDone = new AtomicInteger(0);

	private List<Point3f>	triangles;
	
	private FuncMap fm;
	private Expression ex;
	
	protected float[] stepsizes;

	private String	preParse;
	
	public ImplicitMulti(String expression, 
			 float xMin, float xMax, 
			 float yMin, float yMax, 
			 float zMin, float zMax, 
			 float[] stepsizes) throws ExpressionParseException, IllegalEquationException, UndefinedVariableException {
		super(expression, xMin, xMax, yMin, yMax, zMin, zMax, stepsizes[0], stepsizes[1], stepsizes[2]);
		
		this.stepsizes = stepsizes;
		
		cells = new ConcurrentLinkedQueue<MarchCell>();
		try {
			preParse = preParse(expression);
			ex = ExpressionTree.parse(preParse);
			fm = new FuncMap();
			fm.loadDefaultFunctions();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public  Shape3D plot() {
		Point3f startCube =  findStartCube(xStepsize, yStepsize, zStepsize);
		
		triangles = new ArrayList<Point3f>();
		if (startCube != null) {
			marchCubes(startCube);
		}
		else 
			return null;
		
		Shape3D shape = null;
		if (!isCancelled()) {
			 shape = buildGeomtryFromTriangles(triangles);
		}
			
		triangles = null;
		
		return (!isCancelled()) ? shape : null;
	}
	
	private void marchCubes(Point3f startcube) {
		newTriangles = initTriangleArray(MarchingCubes.MAX_TRIANGLES_RETURNED);
		visited = new boolean[zLength][yLength][xLength];
		
		MarchCell startCell = initStartCube(startcube, startX, startY, startZ);
		int nFacets = marchCube(startCell, newTriangles);
		if (nFacets > 0)
			addTriangles(nFacets, newTriangles);
		
		addStartCells(startCell);
		
		int nThreads = Runtime.getRuntime().availableProcessors();
//		nThreads = 10;
		List<Thread> threads = new ArrayList<Thread>(nThreads);
		List<List<Point3f>> triLists = new ArrayList<List<Point3f>>(); 
		
		for (int i = 0; i < nThreads; i++) {
			List<Point3f> newList = new ArrayList<Point3f>(10000);
			triLists.add(newList);
			Thread t = new Thread(createNewThread(nThreads, newList));
			threads.add(t);
			t.start();
		}
		waitForThreads(threads);
		threads = null;
		visited = null;
		
		if (!isCancelled()) {
			for (List<Point3f> list : triLists) {
				triangles.addAll(list);
			}
		}
	}

	private Runnable createNewThread(final int nThreads, final List<Point3f> list) {
		return new Runnable() {
			

			public void run() {
				try {
					Triangle[] newTri = initTriangleArray(MarchingCubes.MAX_TRIANGLES_RETURNED);;
					VarMap vm = new VarMap();
					
					Expression ex = ExpressionTree.parse(preParse);
					
					while (!isCancelled()) {
						// Next cell to process
						MarchCell next;
						while ((next = cells.poll()) != null && !isCancelled()) {
							
							// How many triangles do we need for this cell
							int nTriangles = MARCHER.marchCube(next, newTri, ISOLEVEL);

							if (nTriangles > 0) {
								addTriangles(nTriangles, newTri, list);

								// Add the triangles from this cell and add neighbours
								marchNext(next, vm, ex);
							}
						}
						nThreadsDone.getAndIncrement();

						while (cells.peek() == null) {
							try {
								Thread.sleep(5);
								if (nThreadsDone.get() == nThreads || !isCancelled()) {
									return;
								}


							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						nThreadsDone.getAndDecrement();
					}
				} catch (ExpressionParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}
	
	private void waitForThreads(List<Thread> threads) {
		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private void marchNext(MarchCell next, VarMap vm, Expression ex) {
		float[] values = next.getValues();
		Point3f[] corners = next.getCorners();
		int x = next.getX();
		int y = next.getY();
		int z = next.getZ();
		
		switch (next.getMarchFace()) {
			case MarchCell.FACE_0: marchFace0(values, corners, x, y, z, vm, ex); 
			break;
		
			case MarchCell.FACE_1: marchFace1(values, corners, x, y, z, vm, ex); 
			break;	

			case MarchCell.FACE_2: marchFace2(values, corners, x, y, z, vm, ex);
			break;

			case MarchCell.FACE_3: marchFace3(values, corners, x, y, z, vm, ex);
			break;

			case MarchCell.FACE_4: marchFace4(values, corners, x, y, z, vm, ex);
			break;

			case MarchCell.FACE_5: marchFace5(values, corners, x, y, z, vm, ex);
			break;
		}
	}
	
	private void addStartCells(MarchCell startCell) {
		float[] values = startCell.getValues();
		Point3f[] corners = startCell.getCorners();
		
		int x = startCell.getX();
		int y = startCell.getY();
		int z = startCell.getZ();
		
		VarMap vm = new VarMap();
		addFace0(values, corners, x, y, z, vm, ex);
		addFace1(values, corners, x, y, z, vm, ex);
		addFace2(values, corners, x, y, z, vm, ex);
		addFace3(values, corners, x, y, z, vm, ex);
		addFace4(values, corners, x, y, z, vm, ex);
		addFace5(values, corners, x, y, z, vm, ex);
	}

	private MarchCell initStartCube(Point3f startcube, int startX, int startY, int startZ) {
		Point3f[] corners = initPoint3fArray(8);
		float[] values = new float[8];
		
		corners[0].x = startcube.x;
		corners[0].y = startcube.y;
		corners[0].z = startcube.z;
		values[0] = value(corners[0]);
		
		corners[1].x = startcube.x + xStepsize;
		corners[1].y = startcube.y;
		corners[1].z = startcube.z; 
		values[1] = value(corners[1]);
		
		corners[2].x = startcube.x + xStepsize;
		corners[2].y = startcube.y + yStepsize;
		corners[2].z = startcube.z;
		values[2] = value(corners[2]);
		
		corners[3].x = startcube.x;
		corners[3].y = startcube.y + yStepsize;
		corners[3].z = startcube.z;
		values[3] = value(corners[3]);
		
		corners[4].x = startcube.x;
		corners[4].y = startcube.y;
		corners[4].z = startcube.z + zStepsize;
		values[4] = value(corners[4]);
		
		corners[5].x = startcube.x + xStepsize;
		corners[5].y = startcube.y;
		corners[5].z = startcube.z + zStepsize;
		values[5] = value(corners[5]);
		
		corners[6].x = startcube.x + xStepsize;
		corners[6].y = startcube.y + yStepsize;
		corners[6].z = startcube.z + zStepsize;
		values[6] = value(corners[6]);
		
		corners[7].x = startcube.x;
		corners[7].y = startcube.y + yStepsize;
		corners[7].z = startcube.z + zStepsize;
		values[7] = value(corners[7]);
		
		return new MarchCell(corners, values, startX, startY, startZ, MarchCell.STARTING_CELL);
	}
	
	private Point3f findStartCube(float xStepsize, float yStepsize, float zStepsize) {
		float[] values = new float[8];
		float z = zMin;
		
		float[][] lower = bottomLayerValues();
		float[][] upper = new float[yLength][xLength];
		
		for (int k = 0; k < zLength - 1; k++) {
		
			calcEdges(upper, z + zStepsize);
			float y = yMin;
			for (int j = 0; j < yLength - 1; j++) {
				
				float x = xMin;
				for (int i = 0; i < xLength - 1; i++) {
					values[0] = lower[j][i];
					
					values[1] = lower[j][i+1];
					
					values[2] = lower[j+1][i+1];
					
					values[3] = lower[j+1][i];
					
					values[4] = upper[j][i];
					
					values[5] = upper[j][i+1];
					
					values[6] = value(x + xStepsize, y + yStepsize, z + zStepsize);
					
					values[7] = upper[j+1][i];	
					
					upper[j+1][i+1] = values[6];
					
					if (MarchingCubes.isInside(values, ISOLEVEL)) {
						startX = i;
						startY = j;
						startZ = k;
						return new Point3f(x, y, z);
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
			
		
		return null;
	}

	private void marchFace0(float[] values, Point3f[] corners, int x, int y, int z, VarMap vm, Expression ex) {
		addFace0(values, corners, x, y, z, vm, ex);
		addFace1(values, corners, x, y, z, vm, ex);
//		addFace2(values, corners, x, y, z, vm, ex);
		addFace3(values, corners, x, y, z, vm, ex);
		addFace4(values, corners, x, y, z, vm, ex);
		addFace5(values, corners, x, y, z, vm, ex);
	}
	
	private void addFace0(float[] prevValues, Point3f[] prevCorners, int x, int y, int z, VarMap vm, Expression ex) {
		y--;
		
		if (!validPosition(x, y, z) || isVisited(x, y, z))
			return;
		
		markAsVisited(x, y, z);
		float[] values = new float[8];
		values[2] = prevValues[1];
		values[3] = prevValues[0];
		values[6] = prevValues[5];
		values[7] = prevValues[4];
		
		Point3f[] corners = new Point3f[8];
		corners[2] = prevCorners[1];
		corners[3] = prevCorners[0];
		corners[6] = prevCorners[5];
		corners[7] = prevCorners[4];
		
		Point3f corner0 = new Point3f(corners[3]);
		corner0.y -= yStepsize;
		corners[0] = corner0;
		values[0] = value(corner0, vm, ex);
		
		Point3f corner1 = new Point3f(corners[2]);
		corner1.y -= yStepsize;
		corners[1] = corner1;
		values[1] = value(corner1, vm, ex);
		
		Point3f corner4 = new Point3f(corners[7]);
		corner4.y -= yStepsize;
		corners[4] = corner4;
		values[4] = value(corner4, vm, ex);
		
		Point3f corner5 = new Point3f(corners[6]);
		corner5.y -= yStepsize;
		corners[5] = corner5;
		values[5] = value(corner5, vm, ex);
		
		cells.add(new MarchCell(corners, values, x, y, z, MarchCell.FACE_0));
	}
	
	private void marchFace1(float[] values, Point3f[] corners, int x, int y, int z, VarMap vm, Expression ex) {

		addFace0(values, corners, x, y, z, vm, ex);
		addFace1(values, corners, x, y, z, vm, ex);
		addFace2(values, corners, x, y, z, vm, ex);
//		addFace3(values, corners, x, y, z, vm, ex);
		addFace4(values, corners, x, y, z, vm, ex);
		addFace5(values, corners, x, y, z, vm, ex);
	}
	
	private void addFace1(float[] prevValues, Point3f[] prevCorners, int x, int y, int z, VarMap vm, Expression ex) {
		x++;
		
		if (!validPosition(x, y, z) || isVisited(x, y, z))
			return;
		
		markAsVisited(x, y, z);
		float[] values = new float[8];
		values[0] = prevValues[1];
		values[3] = prevValues[2];
		values[4] = prevValues[5];
		values[7] = prevValues[6];
		
		Point3f[] corners = new Point3f[8];
		corners[0] = prevCorners[1];
		corners[3] = prevCorners[2];
		corners[4] = prevCorners[5];
		corners[7] = prevCorners[6];
		
		Point3f corner1 = new Point3f(corners[0]);
		corner1.x += xStepsize;
		corners[1] = corner1;
		values[1] = value(corner1, vm, ex);
		
		Point3f corner2 = new Point3f(corners[3]);
		corner2.x += xStepsize;
		corners[2] = corner2;
		values[2] = value(corner2, vm, ex);
		
		Point3f corner5 = new Point3f(corners[4]);
		corner5.x += xStepsize;
		corners[5] = corner5;
		values[5] = value(corner5, vm, ex);
		
		Point3f corner6 = new Point3f(corners[7]);
		corner6.x += xStepsize;
		corners[6] = corner6;
		values[6] = value(corner6, vm, ex);
		
		cells.add(new MarchCell(corners, values, x, y, z, MarchCell.FACE_1));
	}
	
	private void marchFace2(float[] values, Point3f[] corners, int x, int y, int z, VarMap vm, Expression ex) {

//		addFace0(values, corners, x, y, z, vm, ex);
		addFace1(values, corners, x, y, z, vm, ex);
		addFace2(values, corners, x, y, z, vm, ex);
		addFace3(values, corners, x, y, z, vm, ex);
		addFace4(values, corners, x, y, z, vm, ex);
		addFace5(values, corners, x, y, z, vm, ex);
	}
	
	private void addFace2(float[] prevValues, Point3f[] prevCorners, int x, int y, int z, VarMap vm, Expression ex) {
		y++;
		
		if (!validPosition(x, y, z) || isVisited(x, y, z))
			return;
		
		markAsVisited(x, y, z);
		float[] values = new float[8];
		values[1] = prevValues[2];
		values[0] = prevValues[3];
		values[4] = prevValues[7];
		values[5] = prevValues[6];
		
		Point3f[] corners = new Point3f[8];
		corners[1] = prevCorners[2];
		corners[0] = prevCorners[3];
		corners[4] = prevCorners[7];
		corners[5] = prevCorners[6];
		
		Point3f corner2 = new Point3f(corners[1]);
		corner2.y += xStepsize;
		corners[2] = corner2;
		values[2] = value(corner2, vm, ex);
		
		Point3f corner3 = new Point3f(corners[0]);
		corner3.y += xStepsize;
		corners[3] = corner3;
		values[3] = value(corner3, vm, ex);
		
		Point3f corner6 = new Point3f(corners[5]);
		corner6.y += xStepsize;
		corners[6] = corner6;
		values[6] = value(corner6, vm, ex);
		
		Point3f corner7 = new Point3f(corners[4]);
		corner7.y += xStepsize;
		corners[7] = corner7;
		values[7] = value(corner7, vm, ex);
		
		cells.add(new MarchCell(corners, values, x, y, z, MarchCell.FACE_2));
	}
	
	private void marchFace3(float[] values, Point3f[] corners, int x, int y, int z, VarMap vm, Expression ex) {

		addFace0(values, corners, x, y, z, vm, ex);
//		addFace1(values, corners, x, y, z, vm, ex);
		addFace2(values, corners, x, y, z, vm, ex);
		addFace3(values, corners, x, y, z, vm, ex);
		addFace4(values, corners, x, y, z, vm, ex);
		addFace5(values, corners, x, y, z, vm, ex);
	}
	
	private void addFace3(float[] prevValues, Point3f[] prevCorners, int x, int y, int z, VarMap vm, Expression ex) {
		x--;
		
		if (!validPosition(x, y, z) || isVisited(x, y, z))
			return;
		
		markAsVisited(x, y, z);
		float[] values = new float[8];
		values[1] = prevValues[0];
		values[2] = prevValues[3];
		values[5] = prevValues[4];
		values[6] = prevValues[7];
		
		Point3f[] corners = new Point3f[8];
		corners[1] = prevCorners[0];
		corners[2] = prevCorners[3];
		corners[5] = prevCorners[4];
		corners[6] = prevCorners[7];
		
		Point3f corner0 = new Point3f(corners[1]);
		corner0.x -= xStepsize;
		corners[0] = corner0;
		values[0] = value(corner0, vm, ex);
		
		Point3f corner3 = new Point3f(corners[2]);
		corner3.x -= xStepsize;
		corners[3] = corner3;
		values[3] = value(corner3, vm, ex);
		
		Point3f corner4 = new Point3f(corners[5]);
		corner4.x -= xStepsize;
		corners[4] = corner4;
		values[4] = value(corner4, vm, ex);
		
		Point3f corner7 = new Point3f(corners[6]);
		corner7.x -= xStepsize;
		corners[7] = corner7;
		values[7] = value(corner7, vm, ex);
		
		cells.add(new MarchCell(corners, values, x, y, z, MarchCell.FACE_3));
	}
	
	private void marchFace4(float[] values, Point3f[] corners, int x, int y, int z, VarMap vm, Expression ex) {

		addFace0(values, corners, x, y, z, vm, ex);
		addFace1(values, corners, x, y, z, vm, ex);
		addFace2(values, corners, x, y, z, vm, ex);
		addFace3(values, corners, x, y, z, vm, ex);
		addFace4(values, corners, x, y, z, vm, ex);
//		addFace5(values, corners, x, y, z, vm, ex);
	}
	
	private void addFace4(float[] prevValues, Point3f[] prevCorners, int x, int y, int z, VarMap vm, Expression ex) {
		z++;
		
		if (!validPosition(x, y, z) || isVisited(x, y, z))
			return;
		
		markAsVisited(x, y, z);
		float[] values = new float[8];
		values[0] = prevValues[4];
		values[1] = prevValues[5];
		values[2] = prevValues[6];
		values[3] = prevValues[7];
		
		Point3f[] corners = new Point3f[8];
		corners[0] = prevCorners[4];
		corners[1] = prevCorners[5];
		corners[2] = prevCorners[6];
		corners[3] = prevCorners[7];
		
		Point3f corner4 = new Point3f(corners[0]);
		corner4.z += zStepsize;
		corners[4] = corner4;
		values[4] = value(corner4, vm, ex);
		
		Point3f corner5 = new Point3f(corners[1]);
		corner5.z += zStepsize;
		corners[5] = corner5;
		values[5] = value(corner5, vm, ex);
		
		Point3f corner6 = new Point3f(corners[2]);
		corner6.z += zStepsize;
		corners[6] = corner6;
		values[6] = value(corner6, vm, ex);
		
		Point3f corner7 = new Point3f(corners[3]);
		corner7.z += zStepsize;
		corners[7] = corner7;
		values[7] = value(corner7, vm, ex);
		
		cells.add(new MarchCell(corners, values, x, y, z, MarchCell.FACE_4));
	}
	
	private void marchFace5(float[] values, Point3f[] corners, int x, int y, int z, VarMap vm, Expression ex) {

		addFace0(values, corners, x, y, z, vm, ex);
		addFace1(values, corners, x, y, z, vm, ex);
		addFace2(values, corners, x, y, z, vm, ex);
		addFace3(values, corners, x, y, z, vm, ex);
//		addFace4(values, corners, x, y, z, vm, ex);
		addFace5(values, corners, x, y, z, vm, ex);
	}
	
	private void addFace5(float[] prevValues, Point3f[] prevCorners, int x, int y, int z, VarMap vm, Expression ex) {
		z--;
		
		if (!validPosition(x, y, z) || isVisited(x, y, z))
			return;
		
		markAsVisited(x, y, z);
		float[] values = new float[8];
		values[4] = prevValues[0];
		values[5] = prevValues[1];
		values[6] = prevValues[2];
		values[7] = prevValues[3];
		
		Point3f[] corners = new Point3f[8];
		corners[4] = prevCorners[0];
		corners[5] = prevCorners[1];
		corners[6] = prevCorners[2];
		corners[7] = prevCorners[3];
		
		Point3f corner0 = new Point3f(corners[4]);
		corner0.z -= zStepsize;
		corners[0] = corner0;
		values[0] = value(corner0, vm, ex);
		
		Point3f corner1 = new Point3f(corners[5]);
		corner1.z -= zStepsize;
		corners[1] = corner1;
		values[1] = value(corner1, vm, ex);
		
		Point3f corner2 = new Point3f(corners[6]);
		corner2.z -= zStepsize;
		corners[2] = corner2;
		values[2] = value(corner2, vm, ex);
		
		Point3f corner3 = new Point3f(corners[7]);
		corner3.z -= zStepsize;
		corners[3] = corner3;
		values[3] = value(corner3, vm, ex);
		
		cells.add(new MarchCell(corners, values, x, y, z, MarchCell.FACE_5));
	}
	
	//TODO: Fix here: First correct accoring to R. Then do somthing with theta/phi
	//TODO: xFloat = x*xStepsize - xMin
	protected boolean validPosition(int x, int y, int z) {
		return (x >= 0 && x < xLength) && (y >= 0 && y < yLength) && (z >= 0 && z < zLength);
	}

	private void markAsVisited(int x, int y, int z) {
		synchronized (visited[z][y]) {
			visited[z][y][x] = true;		
		}
	}

	private boolean isVisited(int x, int y, int z) {
		synchronized (visited[z][y]) {
			return visited[z][y][x];
		}
	}
	
	
	protected float value(Point3f point, VarMap vm, Expression ex) {
		return value(point.x, point.y, point.z, vm, ex);
	}
	
	protected float value(float x, float y, float z, VarMap vm, Expression ex) {
//		return value(x, y, z);
		vm.setValue("x", x);
		vm.setValue("y", y);
		vm.setValue("z", z);
//		FuncMap fm = new FuncMap();
//		fm.loadDefaultFunctions();
		
		return (float) ex.eval(vm, fm);
	}
	
}

//private void marchCubes(Point3f startcube) {
//
//	newTriangles = initTriangleArray(MarchingCubes.MAX_TRIANGLES_RETURNED);
//	visited = new boolean[zLength][yLength][xLength];
//	
//	MarchCell startCell = initStartCube(startcube, startX, startY, startZ);
//	int nFacets = marchCube(startCell, newTriangles);
//	if (nFacets > 0)
//		addTriangles(nFacets, newTriangles);
//	
//	addStartCells(startCell);
//	
//	while (cells.size() > 0) {
//		// Next cell to process
//		MarchCell next = cells.remove(0);
//
//		// How many triangles do we need for this cell
//		int nTriangles = MARCHER.marchCube(next, newTriangles, ISOLEVEL);
//		
//		if (nTriangles > 0) {
//			addTriangles(nTriangles, newTriangles);
//			
//			// Add the triangles from this cell and add neighbours
//			marchNext(next);
//		}
//	}
//	visited = null;
//	
//}

//private void markAsVisited(int x, int y, int z) {
//	visited[z][y][x] = true;		
//}
//
//private boolean isVisited(int x, int y, int z) {
//	return visited[z][y][x];
//}