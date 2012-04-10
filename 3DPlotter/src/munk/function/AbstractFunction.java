package munk.function;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.media.j3d.Appearance;
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
	
	public Appearance getApprearance(){
		return shape.getAppearance();
	}
	
	public void setAppearance(Appearance a){
		shape.setAppearance(a);
	}
	
	protected void setShape(Shape3D shape){
		this.shape = shape;
	}
}