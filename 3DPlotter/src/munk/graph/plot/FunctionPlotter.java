package munk.graph.plot;

import javax.media.j3d.*;
import javax.vecmath.*;

import org.nfunk.jep.JEP;
import org.nfunk.jep.ParseException;
import static munk.graph.plot.PlotUtil.*;

public class FunctionPlotter {
	
	private static final Transform3D ROT_Y;
	private static final Transform3D ROT_X;
	private Transform3D rotation;
	
	private float stepsize = 0.05f;

	private float	xMin;
	private float	xMax;
	private float	yMin;
	private float	yMax;

	private JEP	jep;
	private org.nfunk.jep.Node node;
	private String var1 = "x";
	private String var2 = "y";
	private int	factorV1 = 1;
	private int	factorV2 = 1;
	
	private Shape3D shape;
	
	public FunctionPlotter(float xMin, float xMax, float yMin, float yMax, String expr) throws ParseException {
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		
		jep = new JEP();
		jep.addStandardFunctions();
		jep.addStandardConstants();
//		jep.setAllowUndeclared(true);
		jep.setAllowAssignment(true);
		preParse(expr);
		initVariables();
		
		node = jep.parse(expr);
	}
	
	public FunctionPlotter(float xMin, float xMax, float yMin, float yMax, String expr, float stepSize) throws ParseException {
		this(xMin, xMax, yMin, yMax, expr);
		stepsize = stepSize;
	}
	
	static {
		ROT_X = new Transform3D();
		ROT_X.rotY(Math.PI/2);
		
		ROT_Y = new Transform3D();
		ROT_Y.rotX(-Math.PI/2);
	}
	


	
	public TransformGroup getPlot() {
		int xSize = (int) ((xMax - xMin) / stepsize);
		int ySize = (int) ((yMax - yMin) / stepsize);
		
		float[] xValues = initAxisArray(xMin, xSize, stepsize);
		float[] yValues = initAxisArray(yMin, ySize, stepsize);
		
		float[][] zValues = new float[ySize][xSize];
		
		for (int y = 0; y < ySize; y++) {
			jep.addVariable(var2, factorV2 * yValues[y]);
			for (int x = 0; x < xSize; x++) {
				jep.addVariable(var1, factorV1 * xValues[x]);
				
				try {
					double value = (Double) jep.evaluate(node);
					zValues[y][x] = (float) value;
				} catch (ParseException e) {
					// Does not happen !
				}
			}
		}
		
		QuadArray quadGeom = new QuadArray (4 * (xSize - 1) * (ySize - 1), QuadArray.COORDINATES | QuadArray.NORMALS);
		Vector3f normal = new Vector3f ();
		int vertice = 0; 
		for (int y = 0; y < ySize - 1; y++) {
			for (int x = 0; x < xSize - 1; x++) {
				
				// Vectors to define the plane
				Vector3f v1 = new Vector3f(0, yValues[y+1] - yValues[y], zValues[y+1][x] - zValues[y][x]);
				Vector3f v2 = new Vector3f(xValues[x] - xValues[x+1], yValues[y+1] - yValues[y], zValues[y+1][x] - zValues[y][x+1]);
				normal.cross(v1, v2);
				normal.normalize();
				
				
				quadGeom.setCoordinate (vertice, new Point3f(xValues[x], yValues[y], zValues[y][x]));
				quadGeom.setNormal(vertice++, normal);
				quadGeom.setCoordinate (vertice, new Point3f(xValues[x], yValues[y+1], zValues[y+1][x]));
				quadGeom.setNormal(vertice++, normal);
				quadGeom.setCoordinate (vertice, new Point3f(xValues[x+1], yValues[y+1], zValues[y+1][x+1]));
				quadGeom.setNormal(vertice++, normal);
				quadGeom.setCoordinate (vertice, new Point3f(xValues[x+1], yValues[y], zValues[y][x+1]));
				quadGeom.setNormal(vertice++, normal);
			}
		}
		
		
		shape = new Shape3D(quadGeom);

		TransformGroup result = new TransformGroup();
		result.addChild(shape);
		result.setTransform(rotation);
		
		return result; 
	}	
	
	private void initVariables() {
		jep.addVariable("x", 0);
		jep.addVariable("y", 0);
		jep.addVariable("z", 0);
	}
	
	private void preParse(String expr) {
		
		if (expr.matches("^x *=.*")) {
			rotation = ROT_X;
			factorV1 = -1;
			var1 = "z";
		}
		else if (expr.matches("^y *=.*")) {
			rotation = ROT_Y;
			var2 = "z";
			factorV2 = -1;
		}
		else {
			rotation = new Transform3D();
		}
		
	}
	
	public Shape3D getShape() {
		return shape;
	}
	
	

}
