package munk.graph.function;

import javax.vecmath.Color3f;

import munk.graph.gui.GuiUtil;
import munk.graph.plot.XYZPlotter;

import com.graphbuilder.math.*;

/*
 * XYZ functions; to be evaluated directly by Mesp.
 */
public class XYZFunction extends AbstractFunction {
	
	public XYZFunction(String[] expr, Color3f color, String[] bounds, float[] stepSize) 
			throws ExpressionParseException, UndefinedVariableException{
		super(expr,color,bounds, stepSize,
				new XYZPlotter(expr[0], GuiUtil.evalStringArray(bounds), stepSize));
	}
	
	@Override
	public String toString() {
		return "XYZ: " + super.toString();
	}
}
