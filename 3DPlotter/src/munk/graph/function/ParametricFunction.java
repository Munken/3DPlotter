package munk.graph.function;


import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;

import munk.graph.plot.parametric.*;

import com.graphbuilder.math.ExpressionParseException;
import com.graphbuilder.math.UndefinedVariableException;

/*
 * Parametric functions; to be evaluated directly by Mesp.
 */
public class ParametricFunction extends AbstractFunction {
	
	private ParametricPlotter pp;
	private String[] varNames;

	public ParametricFunction(String xExpr, String yExpr, String zExpr, Color3f color, float[] bounds, float stepsize) 
								throws ExpressionParseException, IllegalEquationException, UndefinedVariableException{
		this(expressionArray(xExpr, yExpr, zExpr), color, bounds, stepsize);
	}
	
	/**
	 * 
	 * @param expressions The x, y and z expression. Length must be 3!
	 * @param color The color of the function as a Color3f
	 * @param bounds The bounds for the variables [v1Min, v1Max, v2Min, v2Max] or [v1Min, v1Max].
	 * @param stepsize
	 * @throws ExpressionParseException If you math sucks
	 * @throws IllegalEquationException 
	 * @throws UndefinedVariableException 
	 */
	public ParametricFunction(String[] expressions, Color3f color, float[] bounds, float stepsize) throws ExpressionParseException, IllegalEquationException, UndefinedVariableException {
		super(expressions, color, bounds, stepsize);
		
		varNames = FunctionUtil.variableNames(expressions);
		int nVariables = varNames.length;
		
		String xExpr = expressions[0];
		String yExpr = expressions[1];
		String zExpr = expressions[2];
		
		if (nVariables == 1) {
			pp = new Parametric1D(xExpr, yExpr, zExpr, bounds[0], bounds[1], varNames[0], stepsize);
		} else if (nVariables == 2){
			pp = new Parametric2D(xExpr, yExpr, zExpr, bounds[0], bounds[1], bounds[2], bounds[3], varNames, stepsize);
		} else {
			throw new IllegalEquationException("There must be one or two variables in the expression!");
		}
	}
	
	@Override
	protected BranchGroup plot() {
		Shape3D shape = pp.getPlot();
		pp = null;
		
		if (shape != null) {
			BranchGroup bg = FunctionUtil.setApperancePackInBranchGroup(getColor(), shape, shape);
			setShape(shape);
			return bg;
		} else 
			return null;
	}
	
	private static String[] expressionArray(String... expr) {
		return expr;
	}
	
	@Override
	public String toString() {
		return "Parametric " + varNames.length + "D " + super.toString();
	}
}
