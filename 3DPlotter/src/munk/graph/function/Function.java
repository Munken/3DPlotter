package munk.graph.function;

import javax.media.j3d.BranchGroup;
import javax.vecmath.Color3f;

import munk.graph.function.AbstractFunction.FILL;

public interface Function {

	public BranchGroup getPlot();
	
	public String[] getExpression();
	
	public Color3f getColor();
	
	public float[] getBounds();
	
	public void setColor(Color3f color);

	public boolean isVisible();
	
	public void setVisible(boolean b);
	
	public boolean isSelected();
	
	public void setSelected(boolean b);

	public float[] getStepsize();
	
	public String[] getBoundsString();
	
	public void cancel();
	
	public void setView(FILL state);
	
	public FILL getView();
	
	public void setFastImplicit(boolean b);
	
	public void setStepsize(float[] stepSize);
	
	public void setBoundsString(String[] bounds);
	
	public boolean getFastImplicit();

}

