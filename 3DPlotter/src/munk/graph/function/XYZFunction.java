package munk.graph.function;

import static munk.graph.function.FunctionUtil.expressionArray;

import javax.vecmath.Color3f;

import munk.graph.plot.XYZPlotter;

import com.graphbuilder.math.ExpressionParseException;
import com.graphbuilder.math.UndefinedVariableException;

/*
 * XYZ functions; to be evaluated directly by Mesp.
 */
public class XYZFunction extends AbstractFunction {

	public XYZFunction(String expr, Color3f color, float[] bounds, float stepsize) 
							throws ExpressionParseException, UndefinedVariableException{
		this(expressionArray(expr),color,bounds, stepsize);
	}
	
	public XYZFunction(String[] expr, Color3f color, float[] bounds, float stepsize) 
							throws ExpressionParseException, UndefinedVariableException{
		super(expr,color,bounds, stepsize,
				new XYZPlotter(expr[0], bounds[0], bounds[1], bounds[2], bounds[3], stepsize, stepsize));
	}
	
	public XYZFunction(String[] expr, Color3f color, float[] bounds, float[] stepsizes) 
			throws ExpressionParseException, UndefinedVariableException{
		super(expr,color,bounds, stepsize,
				new XYZPlotter(expr[0], bounds[0], bounds[1], bounds[2], bounds[3], stepsizes));
	}
	
//	@Override
//	protected BranchGroup plot() {
//		TransformGroup tg = fp.getPlot();
//		Shape3D shape = fp.getShape();
//		
//		if (shape != null) {
//			BranchGroup bg = setApperancePackInBranchGroup(getColor(), shape, tg);
//			setShape(shape);
//			return bg;
//		} else 
//			return null;
//	}
	
	@Override
	public String toString() {
		String result = "";
		for (String str : getExpression())
			result += str;
		return result;
	}
}
