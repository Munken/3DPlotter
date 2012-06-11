package munk.graph.function.implicit;

import javax.vecmath.Color3f;

import munk.graph.function.AbstractFunction;
import munk.graph.function.IllegalEquationException;
import munk.graph.gui.GuiUtil;
import munk.graph.plot.implicit.XYZMulti;

import com.graphbuilder.math.ExpressionParseException;
import com.graphbuilder.math.UndefinedVariableException;

/*
 * Implicit functions; calculated numerically using Marching Cubes algorithm.
 */
public class ImplicitMultiFunction extends AbstractFunction implements ImplicitFunction{

	public ImplicitMultiFunction(String[] expr, Color3f color, String[] bounds, float[] stepSize) 
												throws ExpressionParseException, IllegalEquationException, UndefinedVariableException{
		super(expr,color,bounds,stepSize, new XYZMulti(expr[0], GuiUtil.evalString(bounds[0]),GuiUtil.evalString(bounds[1]),GuiUtil.evalString(bounds[2]),GuiUtil.evalString(bounds[3]),GuiUtil.evalString(bounds[4]),GuiUtil.evalString(bounds[5]),stepSize));
	}
	
//	public ImplicitMultiFunction(String[] expr, Color3f color, float[] bounds, float[] stepsizes) 
//												throws ExpressionParseException, IllegalEquationException, UndefinedVariableException {
//		super(expr,color,bounds, stepsizes, new ImplicitMulti(expr[0], 
//				bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5], stepsizes));
//	}
	
}


