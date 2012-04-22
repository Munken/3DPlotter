package munk.graph.plot.parametric;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Point3f;

import munk.graph.function.IllegalEquationException;
import munk.graph.plot.PlotUtil;

import com.graphbuilder.math.ExpressionParseException;
import com.graphbuilder.math.UndefinedVariableException;

public class Parametric2D extends AbstractParametric{
	
	private static final String[] STD_VAR_NAMES = {"t", "u"};
	
	private float	tMin;
	private float	tMax;
	private float   uMin;
	private float   uMax;
	
	private float tStepsize;
	private float uStepsize;
	
	private String var1;
	private String var2;

	// The mother of all constructors !
	public Parametric2D(String xExpr, String yExpr, String zExpr, 
						float tMin, float tMax, float uMin, float uMax, 
						String[] varNames, float[] stepSize) 
								throws ExpressionParseException, IllegalEquationException, UndefinedVariableException {
		
		super(xExpr, yExpr, zExpr, varNames, new float[] {tMin, uMin});
		
		if (varNames.length > 2) 
			throw new IllegalStateException("There must only be two parameters!");
		
		var1 = varNames[0];
		var2 = varNames[1];
		
		this.tMin = tMin;
		this.tMax = tMax;
		this.uMin = uMin;
		this.uMax = uMax;
		tStepsize = stepSize[0];
		uStepsize = stepSize[1];
	}
	
	public Parametric2D(String xExpr, String yExpr, String zExpr, 
			float tMin, float tMax, float uMin, float uMax, float[] stepSizes) 
					throws ExpressionParseException, IllegalEquationException, UndefinedVariableException  {
		this(xExpr, yExpr, zExpr, tMin, tMax, uMin, uMax, STD_VAR_NAMES, stepSizes);
	}
	
	@Override
	protected Shape3D plot() {
		int tLength = (int) Math.ceil((tMax - tMin) / tStepsize) + 1;
		int uLength = (int) Math.ceil((uMax - uMin) / uStepsize) + 1;
		
		float[] tValues = PlotUtil.initAxisArray(tMin, tLength, tStepsize);
		float[] uValues = PlotUtil.initAxisArray(uMin, uLength, uStepsize);
		
		Point3f[][] points = new Point3f[tLength][uLength];
		for (int i = 0; i < tLength; i++) {
			setVariable(var1, tValues[i]);
			for (int j = 0; j < uLength; j++) {
				setVariable(var2, uValues[j]);
				
				points[i][j] = new Point3f(xValue(), yValue(), zValue());
			}
		}
		
		GeometryArray quad = PlotUtil.buildQuadArray(points);
		
		Shape3D shape = new Shape3D(quad);
		setShape(shape);
		return shape;
	}
	


}
