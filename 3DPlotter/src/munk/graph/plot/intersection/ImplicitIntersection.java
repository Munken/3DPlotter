package munk.graph.plot.intersection;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.media.j3d.*;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import munk.graph.appearance.ColorAppearance;
import munk.graph.appearance.Colors;
import munk.graph.function.Function;
import munk.graph.function.IllegalEquationException;
import munk.graph.marching.*;

import com.graphbuilder.math.*;
import com.sun.j3d.utils.geometry.Sphere;

public class ImplicitIntersection {
	
	private int startX;
	private int startY;
	private int startZ;
	
	protected float xMin;
	protected int xLength;
	
	protected float yMin;
	protected int yLength;
	
	protected float zMin;
	protected int zLength;
	
	protected float xStepsize;
	protected float yStepsize;
	protected float zStepsize;
	
	private int[][][] visited;
	
	private static Pattern PATTERN = Pattern.compile("([^=]+)=([^=]+)$");
	private static final float ISOLEVEL = 0;
	private static final Appearance	APPEARANCE	= new ColorAppearance(Colors.RED);
	
	private List<MarchCell> cells = new LinkedList<MarchCell>();
	
	private Expression ex;
	private Expression differnce;
	private VarMap vm;
	private FuncMap fm;

	private int	visitedValue;
	
	private List<Point3f> points = new ArrayList<Point3f>(10000);
	
	private Function f1;
	private Function f2;
	
	private BranchGroup plot = new BranchGroup();
	
	public ImplicitIntersection(Function f1, Function f2) throws ExpressionParseException, UndefinedVariableException, IllegalEquationException {
		float[] bounds = f1.getBounds();
		xMin = bounds[0]; float xMax = bounds[1];
		yMin = bounds[2]; float yMax = bounds[3];
		zMin = bounds[4]; float zMax = bounds[5];
		
		float[] stepsize = f1.getStepsize();
		this.xStepsize = stepsize[0];
		this.yStepsize = stepsize[1];
		this.zStepsize = stepsize[2];
		
		this.xMin = xMin - xStepsize/2;
		this.yMin = yMin - yStepsize/2;
		this.zMin = zMin - zStepsize/2;
		
		xLength = (int) Math.ceil((xMax - xMin) / xStepsize) + 1;
		yLength = (int) Math.ceil((yMax - yMin) / yStepsize) + 1;
		zLength = (int) Math.ceil((zMax - zMin) / zStepsize) + 1;
		
		this.f1 = f1;
		this.f2 = f2;
		

		vm = new VarMap();
		fm = new FuncMap();
		fm.loadDefaultFunctions();
		
		differnce = ExpressionTree.parse(preParse(f1.getExpression()[0]) + "-1*(" + preParse(f1.getExpression()[0]) + ")");
	}

	private void setExpression(Function func) throws IllegalEquationException,
			ExpressionParseException {
		String expr = preParse(func.getExpression()[0]);
		
		ex = ExpressionTree.parse(expr);
		vm.setValue("x", xMin);
		vm.setValue("y", yMin);
		vm.setValue("z", zMin);
		

	}
	
	public  BranchGroup plot() {
		
		if (marchFirstCubes()) {
			if (marchSecondCubes()) {
				return plot;
			}

			return null;
		} else 
			return null;
	}
	


	private boolean marchFirstCubes() {
		try {
			setExpression(f1);
		} catch (IllegalEquationException | ExpressionParseException e) {}
		
		Point3f startPoint =  findStartCube(xStepsize, yStepsize, zStepsize);
		
		visited = new int[zLength][yLength][xLength];
		visitedValue = 1;
		MarchCell startCell = initStartCube(startPoint, startX, startY, startZ);
		if (MarchingCubes.isInside(startCell.getValues(), ISOLEVEL)) {
			visited[startCell.getZ()][startCell.getY()][startCell.getX()] = 2;
		}
		
		addStartCells(startCell);
		
		while (cells.size() > 0) {
			// Next cell to process
			MarchCell next = cells.remove(0);
		
			if (MarchingCubes.isInside(next.getValues(), ISOLEVEL)) {
				visited[next.getZ()][next.getY()][next.getX()] = 2;
				marchNext(next);
			}
		}
		
		return startPoint != null;
	}
	
	private boolean marchSecondCubes() {
		try {
			setExpression(f2);
		} catch (IllegalEquationException | ExpressionParseException e) {}
		
		Point3f startPoint =  findStartCube(xStepsize, yStepsize, zStepsize);
		
		visitedValue = 3;
		MarchCell startCell = initStartCube(startPoint, startX, startY, startZ);
		if (MarchingCubes.isInside(startCell.getValues(), ISOLEVEL) &&
				visited[startCell.getZ()][startCell.getY()][startCell.getX()] == 5) {
			addToPlot(startCell);
		}
		
		addStartCells(startCell);
		
		while (cells.size() > 0) {
			// Next cell to process
			MarchCell next = cells.remove(0);
			if (MarchingCubes.isInside(next.getValues(), ISOLEVEL)) {
				marchNext(next);
				if (visited[next.getZ()][next.getY()][next.getX()] == 5) {
					addToPlot(next);
					
				}
			}
		}
		
		return startPoint != null;
	}

	private void addToPlot(MarchCell cell) {
		
		Point3f corner = cell.getCorners()[0];
		
		Sphere sphere = new Sphere(xStepsize, APPEARANCE);
		
		Transform3D t = new Transform3D();
//		t.setTranslation(new Vector3f(corner.x+xStepsize/2, corner.y+yStepsize/2, corner.z+zStepsize/2));
		t.setTranslation(new Vector3f(interpolate(cell)));
		
		TransformGroup tg = new TransformGroup(t);
		tg.addChild(sphere);
		plot.addChild(tg);
	}
	
	private Point3f interpolate(MarchCell cell) {
		
		Point3f[] corners = cell.getCorners();
		float[] values = new float[8];
		
		for (int i = 0; i < values.length; i++) {
			values[i] = difference(corners[i]);
		}
		
		Point3f x0 = vertexInterp(ISOLEVEL, corners[0], corners[1], values[0], values[1]);
		Point3f x1 = vertexInterp(ISOLEVEL, corners[3], corners[2], values[3], values[2]);
		Point3f x2 = vertexInterp(ISOLEVEL, corners[4], corners[5], values[4], values[5]);
		Point3f x3 = vertexInterp(ISOLEVEL, corners[7], corners[6], values[7], values[6]);
		
		Point3f y0 = vertexInterp(ISOLEVEL, x0, x1, difference(x0), difference(x1));
		Point3f y1 = vertexInterp(ISOLEVEL, x2, x3, difference(x2), difference(x3));
		
		
		return vertexInterp(ISOLEVEL, y0, y1, difference(y0), difference(y1));
	}
	
	private float difference(Point3f p) {
		vm.setValue("x", p.x);
		vm.setValue("y", p.y);
		vm.setValue("z", p.z);
			
		return (float) differnce.eval(vm, fm);
	}
	
	private void marchNext(MarchCell next) {
		float[] values = next.getValues();
		Point3f[] corners = next.getCorners();
		int x = next.getX();
		int y = next.getY();
		int z = next.getZ();
		
		switch (next.getMarchFace()) {
			case MarchCell.FACE_0: marchFace0(values, corners, x, y, z); 
			break;
		
			case MarchCell.FACE_1: marchFace1(values, corners, x, y, z); 
			break;	

			case MarchCell.FACE_2: marchFace2(values, corners, x, y, z);
			break;

			case MarchCell.FACE_3: marchFace3(values, corners, x, y, z);
			break;

			case MarchCell.FACE_4: marchFace4(values, corners, x, y, z);
			break;

			case MarchCell.FACE_5: marchFace5(values, corners, x, y, z);
			break;
		}
	}
	
	private void addStartCells(MarchCell startCell) {
		float[] values = startCell.getValues();
		Point3f[] corners = startCell.getCorners();
		
		int x = startCell.getX();
		int y = startCell.getY();
		int z = startCell.getZ();
		
		addFace0(values, corners, x, y, z);
		addFace1(values, corners, x, y, z);
		addFace2(values, corners, x, y, z);
		addFace3(values, corners, x, y, z);
		addFace4(values, corners, x, y, z);
		addFace5(values, corners, x, y, z);
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

	private void marchFace0(float[] values, Point3f[] corners, int x, int y, int z) {
		addFace0(values, corners, x, y, z);
		addFace1(values, corners, x, y, z);
//		addFace2(values, corners, x, y, z);
		addFace3(values, corners, x, y, z);
		addFace4(values, corners, x, y, z);
		addFace5(values, corners, x, y, z);
	}
	
	private void addFace0(float[] prevValues, Point3f[] prevCorners, int x, int y, int z) {
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
		values[0] = value(corner0);
		
		Point3f corner1 = new Point3f(corners[2]);
		corner1.y -= yStepsize;
		corners[1] = corner1;
		values[1] = value(corner1);
		
		Point3f corner4 = new Point3f(corners[7]);
		corner4.y -= yStepsize;
		corners[4] = corner4;
		values[4] = value(corner4);
		
		Point3f corner5 = new Point3f(corners[6]);
		corner5.y -= yStepsize;
		corners[5] = corner5;
		values[5] = value(corner5);
		
		cells.add(new MarchCell(corners, values, x, y, z, MarchCell.FACE_0));
	}
	
	private void marchFace1(float[] values, Point3f[] corners, int x, int y, int z) {

		addFace0(values, corners, x, y, z);
		addFace1(values, corners, x, y, z);
		addFace2(values, corners, x, y, z);
//		addFace3(values, corners, x, y, z);
		addFace4(values, corners, x, y, z);
		addFace5(values, corners, x, y, z);
	}
	
	private void addFace1(float[] prevValues, Point3f[] prevCorners, int x, int y, int z) {
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
		values[1] = value(corner1);
		
		Point3f corner2 = new Point3f(corners[3]);
		corner2.x += xStepsize;
		corners[2] = corner2;
		values[2] = value(corner2);
		
		Point3f corner5 = new Point3f(corners[4]);
		corner5.x += xStepsize;
		corners[5] = corner5;
		values[5] = value(corner5);
		
		Point3f corner6 = new Point3f(corners[7]);
		corner6.x += xStepsize;
		corners[6] = corner6;
		values[6] = value(corner6);
		
		cells.add(new MarchCell(corners, values, x, y, z, MarchCell.FACE_1));
	}
	
	private void marchFace2(float[] values, Point3f[] corners, int x, int y, int z) {

//		addFace0(values, corners, x, y, z);
		addFace1(values, corners, x, y, z);
		addFace2(values, corners, x, y, z);
		addFace3(values, corners, x, y, z);
		addFace4(values, corners, x, y, z);
		addFace5(values, corners, x, y, z);
	}
	
	private void addFace2(float[] prevValues, Point3f[] prevCorners, int x, int y, int z) {
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
		values[2] = value(corner2);
		
		Point3f corner3 = new Point3f(corners[0]);
		corner3.y += xStepsize;
		corners[3] = corner3;
		values[3] = value(corner3);
		
		Point3f corner6 = new Point3f(corners[5]);
		corner6.y += xStepsize;
		corners[6] = corner6;
		values[6] = value(corner6);
		
		Point3f corner7 = new Point3f(corners[4]);
		corner7.y += xStepsize;
		corners[7] = corner7;
		values[7] = value(corner7);
		
		cells.add(new MarchCell(corners, values, x, y, z, MarchCell.FACE_2));
	}
	
	private void marchFace3(float[] values, Point3f[] corners, int x, int y, int z) {

		addFace0(values, corners, x, y, z);
//		addFace1(values, corners, x, y, z);
		addFace2(values, corners, x, y, z);
		addFace3(values, corners, x, y, z);
		addFace4(values, corners, x, y, z);
		addFace5(values, corners, x, y, z);
	}
	
	private void addFace3(float[] prevValues, Point3f[] prevCorners, int x, int y, int z) {
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
		values[0] = value(corner0);
		
		Point3f corner3 = new Point3f(corners[2]);
		corner3.x -= xStepsize;
		corners[3] = corner3;
		values[3] = value(corner3);
		
		Point3f corner4 = new Point3f(corners[5]);
		corner4.x -= xStepsize;
		corners[4] = corner4;
		values[4] = value(corner4);
		
		Point3f corner7 = new Point3f(corners[6]);
		corner7.x -= xStepsize;
		corners[7] = corner7;
		values[7] = value(corner7);
		
		cells.add(new MarchCell(corners, values, x, y, z, MarchCell.FACE_3));
	}
	
	private void marchFace4(float[] values, Point3f[] corners, int x, int y, int z) {

		addFace0(values, corners, x, y, z);
		addFace1(values, corners, x, y, z);
		addFace2(values, corners, x, y, z);
		addFace3(values, corners, x, y, z);
		addFace4(values, corners, x, y, z);
//		addFace5(values, corners, x, y, z);
	}
	
	private void addFace4(float[] prevValues, Point3f[] prevCorners, int x, int y, int z) {
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
		values[4] = value(corner4);
		
		Point3f corner5 = new Point3f(corners[1]);
		corner5.z += zStepsize;
		corners[5] = corner5;
		values[5] = value(corner5);
		
		Point3f corner6 = new Point3f(corners[2]);
		corner6.z += zStepsize;
		corners[6] = corner6;
		values[6] = value(corner6);
		
		Point3f corner7 = new Point3f(corners[3]);
		corner7.z += zStepsize;
		corners[7] = corner7;
		values[7] = value(corner7);
		
		cells.add(new MarchCell(corners, values, x, y, z, MarchCell.FACE_4));
	}
	
	private void marchFace5(float[] values, Point3f[] corners, int x, int y, int z) {

		addFace0(values, corners, x, y, z);
		addFace1(values, corners, x, y, z);
		addFace2(values, corners, x, y, z);
		addFace3(values, corners, x, y, z);
//		addFace4(values, corners, x, y, z);
		addFace5(values, corners, x, y, z);
	}
	
	private void addFace5(float[] prevValues, Point3f[] prevCorners, int x, int y, int z) {
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
		values[0] = value(corner0);
		
		Point3f corner1 = new Point3f(corners[5]);
		corner1.z -= zStepsize;
		corners[1] = corner1;
		values[1] = value(corner1);
		
		Point3f corner2 = new Point3f(corners[6]);
		corner2.z -= zStepsize;
		corners[2] = corner2;
		values[2] = value(corner2);
		
		Point3f corner3 = new Point3f(corners[7]);
		corner3.z -= zStepsize;
		corners[3] = corner3;
		values[3] = value(corner3);
		
		cells.add(new MarchCell(corners, values, x, y, z, MarchCell.FACE_5));
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
	
	private boolean validPosition(int x, int y, int z) {
		return (x >= 0 && x < xLength) && (y >= 0 && y < yLength) && (z >= 0 && z < zLength);
	}

	private void markAsVisited(int x, int y, int z) {
		visited[z][y][x] += visitedValue;		
	}

	private boolean isVisited(int x, int y, int z) {
		return visited[z][y][x] >= visitedValue;
	}
	
	protected float value(Point3f point) {
		return value(point.x, point.y, point.z);
	}
	
	protected float value(float x, float y, float z) {
		vm.setValue("x", x);
		vm.setValue("y", y);
		vm.setValue("z", z);
		
		return (float) ex.eval(vm, fm);
//		return x*x + y*y + z*z - 1;
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
	
	protected String preParse(String expr) throws IllegalEquationException {
		Matcher m = PATTERN.matcher(expr);
		boolean matches = m.matches();
		if (!matches)
			throw new IllegalEquationException("The expression must be of the form <Expression> = <Expression>");
		
		String lhs = m.group(1).trim();
		String rhs = m.group(2).trim();
		
		return lhs + "-(" + rhs + ")";
	}
	
	private Point3f vertexInterp(float isolevel, Point3f p1,
			Point3f p2, float val1, float val2) {
		if (Math.abs(isolevel-val1) < 0.00001)
			return p1;
		if (Math.abs(isolevel-val2) < 0.00001)
			return p2;
		if (Math.abs(val1-val2) < 0.00001)
			return p1;

		float mu = (isolevel - val1) / (val2 - val1);
		Point3f p = new Point3f();
		p.x = p1.x + mu * (p2.x - p1.x);
		p.y = p1.y + mu * (p2.y - p1.y);
		p.z = p1.z + mu * (p2.z - p1.z);

		return(p);
	}
}
