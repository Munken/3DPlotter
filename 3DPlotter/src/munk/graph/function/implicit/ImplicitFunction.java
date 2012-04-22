package munk.graph.function.implicit;

import static munk.graph.function.FunctionUtil.expressionArray;

import javax.vecmath.Color3f;

import munk.graph.function.AbstractFunction;
import munk.graph.function.IllegalEquationException;
import munk.graph.plot.implicit.ImplicitSlow;

import com.graphbuilder.math.ExpressionParseException;
import com.graphbuilder.math.UndefinedVariableException;

/*
 * Implicit functions; calculated numerically using Marching Cubes algorithm.
 */
public class ImplicitFunction extends AbstractFunction {

	public ImplicitFunction(String expr, Color3f color, float[] bounds, float stepsize) 
												throws ExpressionParseException, IllegalEquationException, UndefinedVariableException{
		this(expressionArray(expr),color,bounds, stepsize);
	}
	
	public ImplicitFunction(String[] expr, Color3f color, float[] bounds, float stepsize) 
												throws ExpressionParseException, IllegalEquationException, UndefinedVariableException {
		super(expr,color,bounds, stepsize,
				new ImplicitSlow(expr[0], 
						bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5], stepsize));
	}
	
}


