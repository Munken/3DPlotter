package munk.graph.plot.parametric;

import javax.media.j3d.LineArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Point3f;

import munk.graph.function.IllegalEquationException;
import munk.graph.plot.PlotUtil;

import com.graphbuilder.math.ExpressionParseException;
import com.graphbuilder.math.UndefinedVariableException;

public class Parametric1D extends AbstractParametric{
	
	private static final String STD_VAR_NAMES = "t";
	
	private float	tMin;
	private float	tMax;
	private float stepsize;
	private final String variable;


	// Mother constructor
	public Parametric1D(String xExpr, String yExpr, String zExpr, 
							float tMin, float tMax, 
							String varName, float stepSize) 
									throws ExpressionParseException, IllegalEquationException, UndefinedVariableException {
		super(xExpr, yExpr, zExpr, new String[] {varName}, new float[] {tMin}, stepSize);
		this.tMin = tMin;
		this.tMax = tMax;
		stepsize = stepSize;
		variable = varName;
	}
	
	public Parametric1D(String xExpr, String yExpr, String zExpr, 
							float tMin, float tMax, float stepSize) 
									throws ExpressionParseException, IllegalEquationException, UndefinedVariableException {
		this(xExpr, yExpr, zExpr, tMin, tMax, STD_VAR_NAMES, stepSize);
	}
	
	public Parametric1D(String xExpr, String yExpr, String zExpr, float tMin, float tMax) 
									throws ExpressionParseException, IllegalEquationException, UndefinedVariableException {
		this(xExpr, yExpr, zExpr, tMin, tMax, 0.1f);
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
