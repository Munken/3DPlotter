package munk.graph;

import javax.vecmath.Point3f;
import munk.graph.plot.*;


public class Test {
	
	
	public static void main(String[] args) {
		int n = 3;
		Point3f p = new Point3f(1,1,1);
		
		Point3f[][] pa = new Point3f[n][n];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				pa[i][j] = p;
			}
		}
		
		PlotUtil.buildQuadStripArray(pa);
	}
}