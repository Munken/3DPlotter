package munk.graph.function;

import static munk.graph.function.FunctionUtil.expressionArray;
import static munk.graph.function.FunctionUtil.setApperancePackInBranchGroup;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;

import munk.graph.plot.ImplicitPlotter;

import com.graphbuilder.math.ExpressionParseException;

/*
 * Implicit functions; calculated numerically using Marching Cubes algorithm.
 */
public class ImplicitFunction extends AbstractFunction {

	private ImplicitPlotter ip;
	
	public ImplicitFunction(String expr, Color3f color, float[] bounds, float stepsize) throws ExpressionParseException{
		this(expressionArray(expr),color,bounds, stepsize);
	}
	
	public ImplicitFunction(String[] expr, Color3f color, float[] bounds, float stepsize) throws ExpressionParseException {
		super(expr,color,bounds, stepsize);
		
		String expression = getExpression()[0];
		ip = new ImplicitPlotter(expression, 
				bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5], getStepsize());
	}
	
	@Override
	protected BranchGroup plot() {
		Shape3D shape = ip.getPlot();
		ip = null;
		
		if (shape != null) {
			BranchGroup bg = setApperancePackInBranchGroup(getColor(), shape, shape);
			return bg;
		} else 
			return null;
	}

}


