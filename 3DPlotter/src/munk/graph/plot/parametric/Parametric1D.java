package munk.graph.plot.parametric;

import javax.media.j3d.*;
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
							String varName, float[] stepSize) 
									throws ExpressionParseException, IllegalEquationException, UndefinedVariableException {
		super(xExpr, yExpr, zExpr, new String[] {varName}, new float[] {tMin});
		this.tMin = tMin;
		this.tMax = tMax;
		stepsize = stepSize[0];
		variable = varName;
	}
	
	public Parametric1D(String xExpr, String yExpr, String zExpr, 
							float tMin, float tMax, float[] stepSize) 
									throws ExpressionParseException, IllegalEquationException, UndefinedVariableException {
		this(xExpr, yExpr, zExpr, tMin, tMax, STD_VAR_NAMES, stepSize);
	}

	
	protected Shape3D plot() {
		int length = (int) Math.ceil((tMax - tMin) / stepsize) + 1;
		
		float t = tMin;
		Point3f[] points = new Point3f[length];
		
		for (int i = 0; i < length; i++) {
			setVariable(variable, t);

			points[i] = new Point3f(xValue(), yValue(), zValue());
			t += stepsize;
		}
		
		LineStripArray la = PlotUtil.buildLineStripArray(points);
		
		Shape3D shape = new Shape3D(la);
		setShape(shape);
		return shape;
	}
	


}
