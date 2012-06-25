package munk.graph.function;


import java.util.*;

import javax.vecmath.Color3f;

import munk.emesp.*;
import munk.emesp.exceptions.ExpressionParseException;
import munk.emesp.exceptions.IllegalExpressionException;
import munk.graph.gui.GuiUtil;
import munk.graph.plot.parametric.*;


/*
 * Parametric functions; to be evaluated directly by Mesp.
 */
public class ParametricFunction extends AbstractFunction {
	
	private static final List<String> ALLOWED_VARIABLES = 
			Arrays.asList("t", "u");
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
	public ParametricFunction(String[] expressions, Color3f color, String[] bounds, float[] stepSize) 
			throws IllegalExpressionException {
		super(expressions, color, bounds, stepSize, 
				createPlotter(expressions, GuiUtil.evalStringArray(bounds), stepSize));
	}
	
	private static ParametricPlotter createPlotter(String[] expressions, float[] bounds, float[] stepsize) 
									throws IllegalExpressionException {
		
		String[] varNames = variableNames(expressions);
		int nVariables = varNames.length;
		
		String xExpr = expressions[0];
		String yExpr = expressions[1];
		String zExpr = expressions[2];
		
		if (nVariables == 1) {
			int varIndex = ALLOWED_VARIABLES.indexOf(varNames[0].toLowerCase());
			int lower = 2*varIndex;
			int upper = 2*varIndex + 1;
			
			return new Parametric1D(xExpr, yExpr, zExpr, bounds[lower], bounds[upper], varNames[0], stepsize);
		} else if (nVariables == 2){
			return new Parametric2D(xExpr, yExpr, zExpr, bounds[0], bounds[1], bounds[2], bounds[3], varNames, stepsize);
		} else {
			throw new IllegalExpressionException("There must be one or two variables in the expression!");
		}
	}
	
	private static String[] variableNames (String[] expressions) throws ExpressionParseException {
		Set<String> variables = new HashSet<String>();
		
		for (String ex : expressions) {
			
			if (!ex.equals("")) {
				Expression n = ExpressionParser.parse(ex, FunctionMap.getDefault());
				String[] variableNames = n.getVariableNames();
				for (String var : variableNames) {
					
					if (ALLOWED_VARIABLES.contains(var.toLowerCase())) {
						variables.add(var);	
					} else {
						
						throw new ExpressionParseException("The allowed variables are t and u", 0);
					}
				}
			} else {
				throw new ExpressionParseException("You must specify all coordinates", -1);
			}
		}
		
		String[] varNames = new String[variables.size()];
		variables.toArray(varNames);
		Arrays.sort(varNames);
		return varNames;
	}
}
