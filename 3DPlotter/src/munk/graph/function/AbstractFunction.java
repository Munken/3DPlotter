package munk.graph.function;

import javax.media.j3d.*;
import javax.vecmath.Color3f;

import munk.graph.appearance.*;

public abstract class AbstractFunction implements Function{
	
	public static enum FILL {
		GRID, FILL, POINT
	}
	private String[] expr;
	private Color3f color;
	private Boolean selected;
	private Boolean visible; 
	private BranchGroup plot;
	private float[] bounds;
	private Shape3D shape;
	private float stepsize;
	private boolean havePlotted;
	private FILL state;

	public AbstractFunction(String[] expr, Color3f color, float[] bounds, float stepsize) {
		this.expr = expr;
		this.visible = true;
		this.selected = false;
		this.color = color;
		this.bounds = bounds;
		this.stepsize = stepsize;
		state = FILL.FILL;
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
			setColor(color);
			havePlotted = true;
		}
		return plot;
	}
	
	public float getStepsize() {
		return stepsize;
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
			Appearance app = null;
			
			if (state == FILL.FILL)
				app = new ColorAppearance(color);
			else if (state == FILL.GRID)
				app = new GridAppearance(color);
			else 
				app = new PointAppearance(color);
			
			shape.setAppearance(app);
		}
	}
	
	public void setGridAppearance() {
		if (state != FILL.GRID)
			state = FILL.GRID;
			shape.setAppearance(new GridAppearance(color));
	}
	
	public void setFillAppearance() {
		if (state != FILL.FILL)
			state = FILL.FILL;
			shape.setAppearance(new ColorAppearance(color));
	}
	
	public void setPointAppearance() {
		if (state != FILL.POINT) {
			state = FILL.POINT;
			shape.setAppearance(new PointAppearance(color));
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
