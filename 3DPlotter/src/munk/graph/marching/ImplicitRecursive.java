package munk.graph.marching;

import javax.vecmath.Point3f;

public class ImplicitRecursive {
	
	private float xMin;
	private int xLength;
	
	private float yMin;
	private int yLength;
	
	private float zMin;
	private int zLength;
	
	private float stepsize;

	public ImplicitRecursive(float xMin, float xMax, float yMin, float yMax, float zMin, float zMax, float stepsize) {
		this.xMin = xMin;
		this.yMin = yMin;
		this.zMin = zMin;
		this.stepsize = stepsize;
		
		xLength = (int) Math.ceil((xMax - xMin) / stepsize) + 1;
		yLength = (int) Math.ceil((yMax - yMin) / stepsize) + 1;
		zLength = (int) Math.ceil((zMax - zMin) / stepsize) + 1;
	}
	
	public Point3f findStartCube() {
		float[] values = new float[8];
		float z = zMin;
		
		float[][] lower = bottomLayerValues();
		float[][] upper = new float[yLength][xLength];
		
		for (int k = 0; k < zLength - 1; k++) {
		
			calcEdges(upper, z+stepsize);
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
					
					values[6] = value(x + stepsize, y + stepsize, z + stepsize);
					
					values[7] = upper[j+1][i];	
					
					upper[j+1][i+1] = values[6];
					
					if (cubeindex(values, 0) == 0) {
						return new Point3f(x, y, z);
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
			
		
		return null;
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
	
	private static int cubeindex(float[] values, float isolevel) {
		int cubeindex = 0;
		if (values[0] < isolevel) cubeindex |= 1;
		if (values[1] < isolevel) cubeindex |= 2;
		if (values[2] < isolevel) cubeindex |= 4;
		if (values[3] < isolevel) cubeindex |= 8;
		if (values[4] < isolevel) cubeindex |= 16;
		if (values[5] < isolevel) cubeindex |= 32;
		if (values[6] < isolevel) cubeindex |= 64;
		if (values[7] < isolevel) cubeindex |= 128;
		
		return cubeindex;
	}

	
	private float value(float x, float y, float z) {
		return x*x + y*y + z*z - 3;
	}
}
