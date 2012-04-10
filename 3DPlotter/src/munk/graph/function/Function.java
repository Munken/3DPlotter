package munk.graph.function;

import java.awt.event.ActionListener;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.vecmath.Color3f;

public interface Function {

	public BranchGroup getPlot();
	
	public void setAppearance(Appearance a); 
	
	public Appearance getApprearance();

	public String[] getExpression();
	
	public Color3f getColor();
	
	public void setColor(Color3f color);
	
	public boolean isVisible();
	
	public void setVisible(boolean b);
	
	public boolean isSelected();
	
	public void setSelected(boolean b);
	
	public void addActionListener(ActionListener a);

}

