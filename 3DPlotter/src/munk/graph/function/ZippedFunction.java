package munk.graph.function;

import java.io.Serializable;

import javax.vecmath.Color3f;

import munk.graph.function.AbstractFunction.FILL;

public class ZippedFunction implements Serializable {

	private static final long serialVersionUID = 6392346219383074768L;
	private String[] expr;
	private Color3f color;
	private Boolean selected;
	private Boolean visible; 
	private String[] bounds;
	private float[] stepSize;
	private FILL state;
	private boolean fastImplicit;
	
	public ZippedFunction(String[] expr, Color3f color, String[] bounds, float[] stepsize, boolean selected, boolean visible, FILL state, boolean fastImplicit) {
		this.expr = expr;
		this.visible = visible;
		this.selected = selected;
		this.color = color;
		this.bounds = bounds;
		this.stepSize = stepsize;
		this.state = state;
		this.fastImplicit = fastImplicit;
	}
	
	public String[] getExpression(){
		return expr;
	}
	
	public Color3f getColor(){
		return color;
	}
	
	public boolean isVisible(){
		return visible;
	}

	public boolean isSelected(){
		return selected;
	}

	public float[] getStepsize(){
		return stepSize;
	}
	
	public String[] getBounds(){
		return bounds;
	}
	
	public FILL getState(){
		return state;
	}
	
	public boolean getFastImplicit(){
		return fastImplicit;
	}
}