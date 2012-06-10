package munk.graph.function.implicit;

import javax.vecmath.Color3f;

import munk.graph.function.AbstractFunction;
import munk.graph.function.IllegalEquationException;
import munk.graph.gui.GuiUtil;
import munk.graph.plot.implicit.ImplicitSlowMulti;

import com.graphbuilder.math.ExpressionParseException;
import com.graphbuilder.math.UndefinedVariableException;

/*
 * Implicit functions; calculated numerically using Marching Cubes algorithm.
 */
public class ImplicitSlowFunction extends AbstractFunction implements ImplicitFunction{

	public ImplicitSlowFunction(String[] expr, Color3f color, String[] bounds, float[] stepSize) 
												throws ExpressionParseException, IllegalEquationException, UndefinedVariableException{
		super(expr,color,bounds,stepSize, 
				new ImplicitSlowMulti(expr[0], 
						GuiUtil.evalStringArray(bounds),stepSize));
		
	}
	
}


