package munk.graph.function;

import java.awt.event.ActionListener;

import javax.media.j3d.BranchGroup;
import javax.vecmath.Color3f;

/*
 * XYZ functions; to be evaluated directly by Mesp.
 */
public class XYZFunction extends AbstractFunction {

	public XYZFunction(String[] expr, Color3f color, float[] bounds, ActionListener a){
		super(expr,color,bounds,a);
	}
	
	public XYZFunction(AbstractFunction oldFunc, String[] newExpr) {
		super(oldFunc, newExpr);
	}

	@Override
	protected BranchGroup plot() {
		return null;
	}
}
