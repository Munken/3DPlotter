package munk.graph.function;


import javax.vecmath.Color3f;

import munk.graph.plot.parametric.*;

import com.graphbuilder.math.ExpressionParseException;
import com.graphbuilder.math.UndefinedVariableException;

/*
 * Parametric functions; to be evaluated directly by Mesp.
 */
public class ParametricFunction extends AbstractFunction {
	

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
		super(expressions, color, bounds, stepsize, 
				createPlotter(expressions, bounds, new float[] {stepsize}));
	}
	
	/**
	 * 
	 * @param expressions The x, y and z expression. Length must be 3!
	 * @param color The color of the function as a Color3f
	 * @param bounds The bounds for the variables [v1Min, v1Max, v2Min, v2Max] or [v1Min, v1Max].
	 * @param stepsizes
	 * @throws ExpressionParseException If you math sucks
	 * @throws IllegalEquationException 
	 * @throws UndefinedVariableException 
	 */
	public ParametricFunction(String[] expressions, Color3f color, float[] bounds, float[] stepsizes) throws ExpressionParseException, IllegalEquationException, UndefinedVariableException {
		super(expressions, color, bounds, stepsizes, 
				createPlotter(expressions, bounds, stepsizes));
	}
	
	private static ParametricPlotter createPlotter(String[] expressions, float[] bounds, float[] stepsize) throws ExpressionParseException, IllegalEquationException, UndefinedVariableException {
		String[] varNames = FunctionUtil.variableNames(expressions);
		int nVariables = varNames.length;
		
		String xExpr = expressions[0];
		String yExpr = expressions[1];
		String zExpr = expressions[2];
		
		if (nVariables == 1) {
			return new Parametric1D(xExpr, yExpr, zExpr, bounds[0], bounds[1], varNames[0], stepsize);
		} else if (nVariables == 2){
			return new Parametric2D(xExpr, yExpr, zExpr, bounds[0], bounds[1], bounds[2], bounds[3], varNames, stepsize);
		} else {
			throw new IllegalEquationException("There must be one or two variables in the expression!");
		}
	}
	

	
	private static String[] expressionArray(String... expr) {
		return expr;
	}
}
