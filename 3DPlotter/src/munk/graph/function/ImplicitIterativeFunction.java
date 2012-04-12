package munk.graph.function;

import static munk.graph.function.FunctionUtil.expressionArray;
import static munk.graph.function.FunctionUtil.setApperancePackInBranchGroup;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;

import munk.graph.plot.implicit.ImplicitIterative;

import com.graphbuilder.math.ExpressionParseException;

/*
 * Implicit functions; calculated numerically using Marching Cubes algorithm.
 */
public class ImplicitIterativeFunction extends AbstractFunction {

	private ImplicitIterative ip;
	
	public ImplicitIterativeFunction(String expr, Color3f color, float[] bounds, float stepsize) 
												throws ExpressionParseException, IllegalEquationException{
		this(expressionArray(expr),color,bounds, stepsize);
	}
	
	public ImplicitIterativeFunction(String[] expr, Color3f color, float[] bounds, float stepsize) 
												throws ExpressionParseException, IllegalEquationException {
		super(expr,color,bounds, stepsize);
		
		String expression = getExpression()[0];
		ip = new ImplicitIterative(expression, 
				bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5], getStepsize());
	}
	
	@Override
	protected BranchGroup plot() {
		Shape3D shape = ip.getPlot();
		ip = null;
		
		if (shape != null) {
			BranchGroup bg = setApperancePackInBranchGroup(getColor(), shape, shape);
			setShape(shape);
			return bg;
		} else 
			return null;
	}

}


