package munk.graph.function;

import javax.vecmath.Color3f;

import munk.emesp.exceptions.IllegalExpressionException;
import munk.graph.gui.GuiUtil;
import munk.graph.plot.spherical.SphericalSimplePlotter;

public class SphericalSimple extends AbstractFunction {

	
	public SphericalSimple(String[] expr, Color3f color, String[] bounds, float[] stepSize) 
			throws IllegalExpressionException {
		super(expr,color,bounds, stepSize, 
				new SphericalSimplePlotter(expr[0], GuiUtil.evalStringArray(bounds), stepSize));
	}
	
	
}
