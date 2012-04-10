package munk.function;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.media.j3d.BranchGroup;
import javax.vecmath.Color3f;

public class ImplicitFunction extends AbstractFunction {

	public ImplicitFunction(String[] expr, Color3f color, float[] bounds, ActionListener a){
		this.expr = expr;
		this.visible = true;
		this.selected = false;
		this.color = color;
		listeners = new ArrayList<ActionListener>();
		addActionListener(a);
	}
	
	public ImplicitFunction(AbstractFunction oldFunc, String[] newExpr) {
		super(oldFunc, newExpr);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setAppearance() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected BranchGroup plot() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
