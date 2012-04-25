package munk.graph.gui.listener;

import javax.vecmath.Color3f;

import munk.graph.function.Function;

public class FunctionEvent {
	
	public static enum ACTION {
		COLOR_CHANGE,
		UPDATE,
		FOCUS_GAINED,
		STEPSIZE_CHANGED,
		DELETE,
		VISIBILITY
	}
	private Function	oldFunction;
	private String[]	newExpr;
	private Color3f	color;
	private String[]	bounds;
	private float[] stepsizes;
	private ACTION action;

	/**
	 * 
	 * @param oldFunction
	 * @param newExpr
	 * @param newColor
	 * @param newBounds
	 * @param newStepsizes
	 * @param action
	 */
	public FunctionEvent(Function oldFunction, String[] newExpr, Color3f newColor, String[] newBounds, float[] newStepsizes, ACTION action) {
		this.oldFunction = oldFunction;
		this.newExpr = newExpr;
		this.color = newColor;
		this.bounds = newBounds;
		this.stepsizes = newStepsizes;
		this.action = action;
	}
	
	public FunctionEvent(Function oldFunction, ACTION action) {
		this(oldFunction, oldFunction.getExpression(), 
				oldFunction.getColor(), oldFunction.getBoundsString(), 
				oldFunction.getStepsize(), action);
	}
	
	public FunctionEvent(Function oldFunction, String[] bounds, float[] stepSize) {
		this.bounds = bounds;
		this.stepsizes = stepSize;
	}

	public Function getOldFunction() {
		return oldFunction;
	}

	public String[] getNewExpr() {
		return newExpr;
	}

	public Color3f getColor() {
		return color;
	}

	public String[] getBounds() {
		return bounds;
	}

	public float[] getStepsizes() {
		return stepsizes;
	}

	public ACTION getAction() {
		return action;
	}

}
