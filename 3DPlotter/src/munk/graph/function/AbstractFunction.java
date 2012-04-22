package munk.graph.function;

import javax.media.j3d.*;
import javax.vecmath.Color3f;

import munk.graph.appearance.*;
import munk.graph.plot.Plotter;

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
	private FILL state;
	private Plotter	plotter;
	private float[]	stepsizes;

	public AbstractFunction(String[] expr, Color3f color, float[] bounds, float stepsize, Plotter plotter) {
		this(expr, color, bounds, new float[] {stepsize, stepsize}, plotter);
	}
	
	public AbstractFunction(String[] expr, Color3f color, float[] bounds, float[] stepsizes, Plotter plotter) {
		this.expr = expr;
		this.plotter = plotter;
		this.visible = true;
		this.selected = false;
		this.color = color;
		this.bounds = bounds;
		this.stepsizes = stepsizes;
		state = FILL.FILL;
		
		// TODO remove thiø
		this.stepsize = stepsizes[0];
	}
	
	private BranchGroup setApperancePackInBranchGroup(Color3f color2, Shape3D shape2, Node handle) {
		shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);

		BranchGroup bg = new BranchGroup();
		bg.setCapability(BranchGroup.ALLOW_DETACH);
		bg.addChild(handle);
		bg.compile();
		return bg;
	}

	/*
	 * Lazy evaluation of plotting.
	 */
	public BranchGroup getPlot() {
		if(plotter != null){
			Node handle = plotter.getPlot();
			shape = plotter.getShape();
			plotter = null;
			
			if (shape != null) 
				plot = setApperancePackInBranchGroup(getColor(), shape, handle);

			setColor(color);
		}
		return plot;
	}
	
	public float getStepsize() {
		return stepsize;
	}
	
	public float[] getStepsizes() {
		return stepsizes;
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
	
	public void cancel() {
		if (plotter != null)
			plotter.cancel();
	}

}
