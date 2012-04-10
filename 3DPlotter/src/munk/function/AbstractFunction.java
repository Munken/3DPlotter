package munk.function;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;

public abstract class AbstractFunction implements Function{
	
	private String[] expr;
	private Color3f color;
	private Boolean selected;
	private Boolean visible; 
	private ArrayList<ActionListener> listeners;
	private BranchGroup plot;
	private float[] bounds;
	private Shape3D shape;

	public AbstractFunction(String[] expr, Color3f color, float[] bounds, ActionListener a){
		this.expr = expr;
		this.visible = true;
		this.selected = false;
		this.color = color;
		listeners = new ArrayList<ActionListener>();
		addActionListener(a);
	}
	
	public AbstractFunction(AbstractFunction oldFunc, String[] newExpr) {
		this.color = oldFunc.color;
		this.selected = oldFunc.selected;
		this.visible = oldFunc.visible;
		this.listeners = oldFunc.listeners;
		this.plot = oldFunc.plot;
		this.bounds = oldFunc.bounds;
		this.shape = oldFunc.shape;
		this.expr = newExpr;
	}
	
	protected abstract BranchGroup plot();
	
	protected void setShape(Shape3D shape){
		this.shape = shape;
	}
	
	/*
	 * Lazy evaluation of plotting.
	 * @see munk.function.Function#getPlot()
	 */
	public BranchGroup getPlot(){
		if(plot == null){
			plot = plot();
		}
		return plot;
	}

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
		signallAll(new ActionEvent(this, 0, "Visibility Changed"));
	}
	
	public boolean isSelected(){
		return selected;
	}
	
	public void setSelected(boolean b){
		selected = b;
	}
	
	public void addActionListener(ActionListener a){
		listeners.add(a);
	}
	
	private void signallAll(ActionEvent event){
		for(ActionListener a : listeners){
			a.actionPerformed(event);
		}
	}
}
