package munk.graph.function;

import java.awt.event.ActionListener;

import javax.media.j3d.BranchGroup;
import javax.vecmath.Color3f;

/*
 * Implicit functions; calculated numerically using Marching Cubes algorithm.
 */
public class ImplicitFunction extends AbstractFunction {

	public ImplicitFunction(String[] expr, Color3f color, float[] bounds, ActionListener a){
		super(expr,color,bounds,a);
	}
	
	public ImplicitFunction(AbstractFunction oldFunc, String[] newExpr) {
		super(oldFunc, newExpr);
	}

	@Override
	protected BranchGroup plot() {
		return null;
	}
}


