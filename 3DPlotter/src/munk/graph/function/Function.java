package munk.graph.function;

import javax.media.j3d.BranchGroup;
import javax.vecmath.Color3f;

import munk.graph.appearance.*;
import munk.graph.function.AbstractFunction.FILL;

public interface Function {

	public BranchGroup getPlot();
	
	public String[] getExpression();
	
	public Color3f getColor();
	
	public float[] getBounds();
	
	public void setColor(Color3f color);
	
//	public void setGridAppearance();
//	
//	public void setPointAppearance();
//	
//	public void setFillAppearance();
	
	public boolean isVisible();
	
	public void setVisible(boolean b);
	
	public boolean isSelected();
	
	public void setSelected(boolean b);
	
	public float getStepsize();
	
	public void setView(FILL state);
	
	public FILL getView();
	
	public void setFastImplicit(boolean b);
	
	public boolean getFastImplicit();

}

