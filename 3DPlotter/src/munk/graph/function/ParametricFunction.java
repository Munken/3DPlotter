package munk.graph.function;


import java.util.*;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;

import munk.graph.plot.*;

import com.graphbuilder.math.*;

/*
 * Parametric functions; to be evaluated directly by Mesp.
 */
public class ParametricFunction extends AbstractFunction {
	
	private ParametricPlotter pp;
	private String[] varNames;

	public ParametricFunction(String xExpr, String yExpr, String zExpr, Color3f color, float[] bounds, float stepsize) throws ExpressionParseException{
		this(expressionArray(xExpr, yExpr, zExpr), color, bounds, stepsize);
	}
	
	/**
	 * 
	 * @param expressions The x, y and z expression. Length must be 3!
	 * @param color The color of the function as a Color3f
	 * @param bounds The bounds for the variables [v1Min, v1Max, v2Min, v2Max] or [v1Min, v1Max].
	 * @param stepsize
	 * @throws ExpressionParseException If you math sucks
	 */
	public ParametricFunction(String[] expressions, Color3f color, float[] bounds, float stepsize) throws ExpressionParseException {
		super(expressions, color, bounds, stepsize);
		
		varNames = variableNames(expressions);
		int nVariables = varNames.length;
		
		String xExpr = expressions[0];
		String yExpr = expressions[1];
		String zExpr = expressions[2];
		
		if (nVariables == 1) {
			pp = new Parametric1D(xExpr, yExpr, zExpr, bounds[0], bounds[1], varNames[0], stepsize);
		} else if (nVariables == 2){
			pp = new Parametric2D(xExpr, yExpr, zExpr, bounds[0], bounds[1], bounds[2], bounds[3], varNames, stepsize);
		} else {
			throw new IllegalStateException("There must be one or two variables in the expression!");
		}
	}
	
	@Override
	protected BranchGroup plot() {
		Shape3D shape = pp.getPlot();
		pp = null;
		
		if (shape != null) {
			BranchGroup bg = FunctionUtil.setApperancePackInBranchGroup(getColor(), shape, shape);
			return bg;
		} else 
			return null;
	}
	
	private static String[] expressionArray(String... expr) {
		return expr;
	}
	
	private static String[] variableNames (String[] expressions) {
		Set<String> variables = new HashSet<String>();
		
		for (String ex : expressions) {
			Expression n = ExpressionTree.parse(ex);
			
			String[] variableNames = n.getVariableNames();
			for (String var : variableNames) {
				variables.add(var);
			}
		}
		
		String[] varNames = new String[variables.size()];
		variables.toArray(varNames);
		return varNames;
	}
}
