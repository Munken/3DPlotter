package munk.graph.function;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.media.j3d.BranchGroup;
import javax.vecmath.Color3f;

public class ImplicitFunction extends AbstractFunction {

	public ImplicitFunction(String[] expr, Color3f color, float[] bounds, ActionListener a){
		super(String[] expr, Color3f color, float[] bounds, ActionListener a);
	}
	
	public ImplicitFunction(AbstractFunction oldFunc, String[] newExpr) {
		super(oldFunc, newExpr);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected BranchGroup plot() {
		// TODO Auto-generated method stub
		return null;
	}
}


