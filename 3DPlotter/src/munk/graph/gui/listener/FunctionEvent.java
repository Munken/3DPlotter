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
	private float[]	bounds;
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
	public FunctionEvent(Function oldFunction, String[] newExpr, Color3f newColor, float[] newBounds, float[] newStepsizes, ACTION action) {
		this.oldFunction = oldFunction;
		this.newExpr = newExpr;
		this.color = newColor;
		this.bounds = newBounds;
		this.stepsizes = newStepsizes;
		this.action = action;
	}
	
	public FunctionEvent(Function oldFunction, ACTION action) {
		this(oldFunction, oldFunction.getExpression(), 
				oldFunction.getColor(), oldFunction.getBounds(), 
				oldFunction.getStepsizes(), action);
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

	public float[] getBounds() {
		return bounds;
	}

	public float[] getStepsizes() {
		return stepsizes;
	}

	public ACTION getAction() {
		return action;
	}

}
