package munk.graph.function;

import javax.vecmath.Color3f;

import munk.emesp.exceptions.*;
import munk.graph.gui.GuiUtil;
import munk.graph.plot.XYZPlotter;

/*
 * XYZ functions; to be evaluated directly by Mesp.
 */
public class XYZFunction extends AbstractFunction {
	
	public XYZFunction(String[] expr, Color3f color, String[] bounds, float[] stepSize) 
			throws IllegalExpressionException   {
		super(expr,color,bounds, stepSize,
				new XYZPlotter(expr[0], GuiUtil.evalStringArray(bounds), stepSize));
	}
	
	@Override
	public String toString() {
		String result = "";
		for (String str : getExpression())
			result += str;
		return result;
	}
}
