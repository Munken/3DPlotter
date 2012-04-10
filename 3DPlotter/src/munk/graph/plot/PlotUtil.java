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
		int xSize = points[1].length;
		
		QuadArray quad = new QuadArray (4 * (xSize - 1) * (ySize - 1), QuadArray.COORDINATES);
		Vector3f normal = new Vector3f ();
		int vertice = 0; 
		
		for (int y = 0; y < ySize - 1; y++) {
			for (int x = 0; x < xSize - 1; x++) {
				Vector3f v1 = directionVector(points[y][x], points[y+1][x]);
				Vector3f v2 = directionVector(points[y+1][x+1], points[y+1][x]);
				normal.cross(v1, v2);
				normal.normalize();
				
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
	
	public static Vector3f directionVector(Point3f p1, Point3f p2) {
		return new Vector3f(p2.x-p1.x, p2.y-p1.y, p2.z-p1.z);
	}

}
