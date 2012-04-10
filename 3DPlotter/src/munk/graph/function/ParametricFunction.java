package munk.graph.function;

import java.awt.event.ActionListener;

import javax.media.j3d.BranchGroup;
import javax.vecmath.Color3f;

/*
 * Parametric functions; to be evaluated directly by Mesp.
 */
public class ParametricFunction extends AbstractFunction {
	
	public ParametricFunction(String[] expr, Color3f color, float[] bounds, ActionListener a){
		super(expr,color,bounds,a);
	}
	
	@Override
	protected BranchGroup plot() {
		return null;
	}
}
