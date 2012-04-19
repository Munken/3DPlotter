package munk.graph.function;

import javax.media.j3d.BranchGroup;
import javax.vecmath.Color3f;

public interface Function {

	public BranchGroup getPlot();
	
	public String[] getExpression();
	
	public Color3f getColor();
	
	public float[] getBounds();
	
	public void setColor(Color3f color);
	
	public void setGridAppearance();
	
	public void setPointAppearance();
	
	public void setFillAppearance();
	
	public boolean isVisible();
	
	public void setVisible(boolean b);
	
	public boolean isSelected();
	
	public void setSelected(boolean b);
	
	public float getStepsize();

}

