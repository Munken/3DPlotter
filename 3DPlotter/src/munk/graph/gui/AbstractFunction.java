package munk.graph.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.media.j3d.BranchGroup;
import javax.vecmath.Color3f;

public abstract class AbstractFunction {
	
	private String[] expr;
	private Color3f color;
	private Boolean selected;
	private Boolean visible; 
	private ActionListener a;

	public AbstractFunction(String[] expr, Color3f color, ActionListener a){
		this.expr = expr;
		this.visible = true;
		this.selected = false;
		this.color = color;
		this.a = a;
	}
	
	public AbstractFunction(Function oldFunc, String[] newExpr) {
		
	}
	
	public abstract BranchGroup getPlot();

	public String[] getExpression(){
		return expr;
	}
	public Color3f getColor() {
		return color;
	}
	
	public void setColor(Color3f color) {
		this.color = color;
	}
	
	public boolean isVisible(){
		return visible;
	}
	
	public void setVisible(boolean b){
		visible = b;
		a.actionPerformed(new ActionEvent(this, 0, "Visibility Changed"));
	}
	
	public boolean isSelected(){
		return selected;
	}
	
	public void setSelected(boolean b){
		selected = b;
	}
}
