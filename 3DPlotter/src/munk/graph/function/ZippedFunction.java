package munk.graph.function;

import java.io.Serializable;

import javax.vecmath.Color3f;

public class ZippedFunction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6392346219383074768L;
	private String[] expr;
	private Color3f color;
	private Boolean selected;
	private Boolean visible; 
	private float[] bounds;
	private float stepsize;
	
	public ZippedFunction(String[] expr, Color3f color, float[] bounds, float stepsize, boolean selected, boolean visible) {
		this.expr = expr;
		this.visible = visible;
		this.selected = selected;
		this.color = color;
		this.bounds = bounds;
		this.stepsize = stepsize;
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

	public float getStepsize(){
		return stepsize;
	}
	
	public float[] getBounds(){
		return bounds;
	}
}