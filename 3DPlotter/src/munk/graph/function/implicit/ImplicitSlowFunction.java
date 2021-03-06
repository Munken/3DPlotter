package munk.graph.function.implicit;

import javax.vecmath.Color3f;

import munk.emesp.exceptions.IllegalExpressionException;
import munk.graph.function.AbstractFunction;
import munk.graph.gui.GuiUtil;
import munk.graph.plot.implicit.XYZMulti;

/*
 * Implicit functions; calculated numerically using Marching Cubes algorithm.
 */
public class ImplicitSlowFunction extends AbstractFunction implements ImplicitFunction{

	public ImplicitSlowFunction(String[] expr, Color3f color, String[] bounds, float[] stepSize) 
												throws IllegalExpressionException {
		super(expr,color,bounds,stepSize, 
				new XYZMulti(expr[0], 
						GuiUtil.evalStringArray(bounds),stepSize));
		
	}
	
}


