package munk.graph.function;

import java.awt.event.ActionListener;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;

import munk.graph.plot.ImplicitPlotter;
import static munk.graph.function.FunctionUtil.setApperancePackInBranchGroup;

/*
 * Implicit functions; calculated numerically using Marching Cubes algorithm.
 */
public class ImplicitFunction extends AbstractFunction {

	public ImplicitFunction(String expr, Color3f color, float[] bounds, ActionListener a){
		this(expressionArray(expr),color,bounds,a);
	}
	
	public ImplicitFunction(String[] expr, Color3f color, float[] bounds, ActionListener a){
		super(expr,color,bounds,a);
	}
	
	public ImplicitFunction(String expr, Color3f color, float[] bounds){
		this(expressionArray(expr),color,bounds);
	}
	
	public ImplicitFunction(String[] expr, Color3f color, float[] bounds){
		super(expr,color,bounds);
	}
	
	
	
	@Override
	protected BranchGroup plot() {
		float[] bounds = getBounds();
		String expr = getExpression()[0];
		ImplicitPlotter ip = new ImplicitPlotter(expr, 
				bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5], 0.05f);
		Shape3D shape = ip.getPlot();
		
		if (shape != null) {
			BranchGroup bg = setApperancePackInBranchGroup(getColor(), shape);
			return bg;
		} else 
			return null;
	}
	
	private static String[] expressionArray(String expr) {
		String[] result = {expr};
		return result;
	}

}


