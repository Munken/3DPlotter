package munk.graph.function.implicit;

import javax.vecmath.Color3f;

import munk.graph.function.AbstractFunction;
import munk.graph.function.FunctionUtil;
import munk.graph.function.IllegalEquationException;
import munk.graph.gui.GuiUtil;
import munk.graph.plot.implicit.ImplicitMulti;

import com.graphbuilder.math.ExpressionParseException;
import com.graphbuilder.math.UndefinedVariableException;

public class SphericalFunction extends AbstractFunction {

	public SphericalFunction(String[] expr, Color3f color, String[] bounds, float[] stepSize) 
			throws ExpressionParseException, IllegalEquationException, UndefinedVariableException{
		super(expr,color,bounds, stepSize, new ImplicitMulti(FunctionUtil.sphericalToImplicit(expr[0]), GuiUtil.evalString(bounds[0]),GuiUtil.evalString(bounds[1]),GuiUtil.evalString(bounds[2]),GuiUtil.evalString(bounds[3]),GuiUtil.evalString(bounds[4]),GuiUtil.evalString(bounds[5]),stepSize));
	}
}
