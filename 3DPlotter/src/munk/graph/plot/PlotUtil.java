package munk.graph.plot;

import javax.media.j3d.*;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;

public class PlotUtil {
	
	public static float[] initAxisArray(float min, int length, float stepsize) {
		float[] result = new float[length];
		
		float current = min;
		for (int i = 0; i < length; i++) {
			result[i] = current;
			current += stepsize;
		}
		
		return result;
	}
	
	public static GeometryArray buildQuadArray(Point3f[][] points) {
		int ySize = points.length;
		int xSize = points[0].length;
		if (ySize <= 1 || xSize <= 1)
			return null;
		
		
		
		
		QuadArray quad = new QuadArray (4 * (xSize - 1) * (ySize - 1), QuadArray.COORDINATES);
		int vertice = 0; 
		
		for (int y = 0; y < ySize - 1; y++) {
			for (int x = 0; x < xSize - 1; x++) {
				
				quad.setCoordinate (vertice++, points[y][x]);
				quad.setCoordinate (vertice++, points[y+1][x]);
				quad.setCoordinate (vertice++, points[y+1][x+1]);
				quad.setCoordinate (vertice++, points[y][x+1]);
			}
		}
		
		GeometryInfo gi = new GeometryInfo(quad);
		NormalGenerator ng = new NormalGenerator();
		ng.generateNormals(gi);
		return gi.getGeometryArray();
	}
	
	public static GeometryArray buildQuadArray(Point3f[][] points, Vector3f[][] normals) {
		int ySize = points.length;
		int xSize = points[0].length;
		if (ySize <= 1 || xSize <= 1)
			return null;
		
		
		
		
		QuadArray quad = new QuadArray (4 * (xSize - 1) * (ySize - 1), QuadArray.COORDINATES | QuadArray.NORMALS);
		int vertice = 0; 
		
		for (int y = 0; y < ySize - 1; y++) {
			for (int x = 0; x < xSize - 1; x++) {
				
				quad.setCoordinate (vertice, points[y][x]);
				quad.setNormal(vertice++, normals[y][x]);
				
				quad.setCoordinate (vertice, points[y+1][x]);
				quad.setNormal(vertice++, normals[y+1][x]);
				
				quad.setCoordinate (vertice, points[y+1][x+1]);
				quad.setNormal(vertice++, normals[y+1][x+1]);
				
				quad.setCoordinate (vertice, points[y][x+1]);
				quad.setNormal(vertice++, normals[y][x+1]);
			}
		}
		
		return quad;
	}
	
	public static GeometryArray buildQuadStripArray(Point3f[][] points) {
		int ySize = points.length;
		int xSize = points[0].length;
		if (ySize <= 1 || xSize <= 1)
			return null;
		
		Point3f[] transPoints = new Point3f[xSize * ySize];
		int[] indices = new int[4 * (xSize - 1) * (ySize - 1)];
		
		int vertex = 0;
		for (int y = 0; y < ySize - 1; y++) {
			for (int x = 0; x < xSize - 1; x++) {
				
	             int lowerLeft = x + y * xSize;
	             int lowerRight = (x + 1) + y * xSize;
	             int topLeft = x + (y + 1) * xSize;
	             int topRight = (x + 1) + (y + 1) * xSize;
	             
	             indices[vertex++] = lowerLeft;
	             indices[vertex++] = topLeft;
	             indices[vertex++] = topRight;
	             indices[vertex++] = lowerRight;
	             
	             transPoints[lowerLeft] = points[y][x];
			}
		}
		
		for (int y = 0; y < ySize; y++) {
			int x = xSize - 1;
			int indice = x + y * xSize;
			
			transPoints[indice] = points[y][x];
		}
		
		for (int x = 0; x < ySize; x++) {
			int y = ySize - 1;
			int indice = x + y * xSize;
			
			transPoints[indice] = points[y][x];
		}
		
		
		
		GeometryInfo g = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
		
		g.setCoordinates(transPoints);
		g.setCoordinateIndices(indices);
		
		
		NormalGenerator ng = new NormalGenerator();
		ng.generateNormals(g);
		return g.getGeometryArray();
	}
	
	public static LineArray buildLineArray(Point3f[] points) {
		int length = points.length;
		LineArray la = new LineArray(2 * (length - 1), LineArray.COORDINATES);
		int vertice = 0;
		
		for (int i = 0; i < length - 1; i++) {
			la.setCoordinate(vertice++, points[i]);
			la.setCoordinate(vertice++, points[i+1]);
		}
		return la;
	}
	
	public static LineStripArray buildLineStripArray(Point3f[] points) {
		int length = points.length;
		int[] lineArray = {length};
		LineStripArray la = new LineStripArray(length, LineArray.COORDINATES, lineArray);
		
		la.setCoordinates(0, points);
		return la;
	}
	
	public static Vector3f directionVector(Point3f p1, Point3f p2) {
		return new Vector3f(p2.x-p1.x, p2.y-p1.y, p2.z-p1.z);
	}

}
