package munk.graph.plot;

import javax.media.j3d.LineArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Point3f;

import com.graphbuilder.math.ExpressionParseException;

public class Parametric1D extends ParametricPlotter{
	
	private static final String STD_VAR_NAMES = "t";
	
	private float	tMin;
	private float	tMax;
	private float stepsize;
	private final String variable;


	// Mother constructor
	public Parametric1D(String xExpr, String yExpr, String zExpr, float tMin, float tMax, String varName, float stepSize) throws ExpressionParseException {
		super(xExpr, yExpr, zExpr, new String[] {varName}, new float[] {tMin}, stepSize);
		this.tMin = tMin;
		this.tMax = tMax;
		stepsize = stepSize;
		variable = varName;
	}
	
	public Parametric1D(String xExpr, String yExpr, String zExpr, float tMin, float tMax, float stepSize) throws ExpressionParseException {
		this(xExpr, yExpr, zExpr, tMin, tMax, STD_VAR_NAMES, stepSize);
	}
	
	public Parametric1D(String xFunc, String yFunc, String zFunc, float tMin, float tMax) throws ExpressionParseException {
		this(xFunc, yFunc, zFunc, tMin, tMax, 0.1f);
	}
	
	public Shape3D getPlot() {
		int length = (int) Math.ceil((tMax - tMin) / stepsize) + 1;
		
		float t = tMin;
		Point3f[] points = new Point3f[length];
		
		for (int i = 0; i < length; i++) {
			setVariable(variable, t);

			points[i] = new Point3f(xValue(), yValue(), zValue());
			t += stepsize;
		}
		
		LineArray la = PlotUtil.buildLineArray(points);
		
		return new Shape3D(la);
	}
	


}
