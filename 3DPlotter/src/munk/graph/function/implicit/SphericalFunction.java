package munk.graph.function.implicit;

import javax.vecmath.Color3f;

import munk.graph.function.AbstractFunction;
import munk.graph.function.IllegalEquationException;
import munk.graph.gui.GuiUtil;
import munk.graph.plot.implicit.SphericalMulti;

import com.graphbuilder.math.ExpressionParseException;
import com.graphbuilder.math.UndefinedVariableException;

public class SphericalFunction extends AbstractFunction {

	public SphericalFunction(String[] expr, Color3f color, String[] bounds, float[] stepSize) 
			throws ExpressionParseException, IllegalEquationException, UndefinedVariableException{
		super(expr,color,bounds, stepSize, new SphericalMulti(sphericalToImplicit(expr[0]), GuiUtil.evalString(bounds[0]),GuiUtil.evalString(bounds[1]),GuiUtil.evalString(bounds[2]),GuiUtil.evalString(bounds[3]),GuiUtil.evalString(bounds[4]),GuiUtil.evalString(bounds[5]),stepSize));
	}
	
	private static String sphericalToImplicit(String s){
		s = s.replace("theta", "(acos(z/r))");
		s = s.replace("phi", "(atan(y/x))");
		s = s.replace("r", "(x^2+y^2+z^2)^0.5");
		return s;
	}
}
