package munk.graph.function;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;

import munk.graph.appearance.ColorAppearance;

public abstract class AbstractFunction implements Function{
	
	private String[] expr;
	private Color3f color;
	private Boolean selected;
	private Boolean visible; 
	private BranchGroup plot;
	private float[] bounds;
	private Shape3D shape;
	private float stepsize;
	private boolean havePlotted;

	public AbstractFunction(String[] expr, Color3f color, float[] bounds, float stepsize) {
		this.expr = expr;
		this.visible = true;
		this.selected = false;
		this.color = color;
		this.bounds = bounds;
		this.stepsize = stepsize;
	}
	
	/*
	 * Implemented differently for each function type.
	 */
	protected abstract BranchGroup plot() ;
	
	/*
	 * Lazy evaluation of plotting.
	 */
	public BranchGroup getPlot() {
		if(plot == null && !havePlotted){
			plot = plot();
			havePlotted = true;
		}
		return plot;
	}
	
	public float getStepsize() {
		return stepsize;
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
	
	public String[] getExpression(){
		return expr;
	}
	
	public Color3f getColor() {
		return color;
	}
	
	public void setColor(Color3f color) {
		this.color = color;
		if (shape != null) {
			shape.setAppearance(new ColorAppearance(color));
		}
	}
	
	public boolean isVisible(){
		return visible;
	}
	
	public void setVisible(boolean b){
		visible = b;
	}
	
	public boolean isSelected(){
		return selected;
	}
	
	public void setSelected(boolean b){
		selected = b;
	}

	public float[] getBounds() {
		return bounds;
	}

}
