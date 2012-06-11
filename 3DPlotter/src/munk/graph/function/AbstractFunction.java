package munk.graph.function;

import javax.media.j3d.*;
import javax.vecmath.Color3f;

import com.graphbuilder.math.ExpressionParseException;

import munk.graph.appearance.*;
import munk.graph.gui.GuiUtil;
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
	private String[] boundsString;
	private Shape3D shape;
	protected FILL state;
	private Plotter	plotter;
	private float[]	stepSize;
	private boolean fasterImplicit;
	
	public AbstractFunction(String[] expr, Color3f color, String[] bounds, float[] stepSize, Plotter plotter) throws ExpressionParseException {
		this.expr = expr;
		this.plotter = plotter;
		this.visible = true;
		this.selected = false;
		this.color = color;
		this.boundsString = bounds;
		this.stepSize = stepSize;
		this.bounds = new float[6];
		for(int i = 0; i < bounds.length; i++){
			this.bounds[i] = GuiUtil.evalString(bounds[i]);
		}
		
		state = FILL.FILL;
	}
	
	private BranchGroup setApperancePackInBranchGroup(Shape3D shape, Node handle) {
		shape.setUserData(this);
		
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
			if(!plotter.isCancelled()){
				shape = plotter.getShape();
				plotter = null;

				if (shape != null) 
					plot = setApperancePackInBranchGroup(shape, handle);

				setColor(color);
			}
		}
		return plot;
	}

	public float[] getStepsize() {
		return stepSize;
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
			setApperance();
		}
	}
	
	public void setView(FILL state){
		this.state = state;
		if(shape != null){
			this.state = state;
			setApperance();
		}
	}
	
	public FILL getView(){
		return state;
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
	
	public String[] getBoundsString(){
		return boundsString;
	}
	
	public void cancel() {
		if (plotter != null)
			plotter.cancel();
	}

	public void setApperance(){
		Appearance app = null;
		if(state == FILL.POINT) 
			app = new PointAppearance(color);
		else if(state == FILL.GRID) 
			app = new GridAppearance(color);
		else
			app = new ColorAppearance(color);
		shape.setAppearance(app);
	}
	
	public void setFastImplicit(boolean b){
		fasterImplicit = b;
	}
	
	public boolean getFastImplicit(){
		return fasterImplicit;
	}
	
	public void setStepsize(float[] stepSize){
		this.stepSize = stepSize;
	}
	
	public void setBoundsString(String[] bounds){
		this.boundsString = bounds;
	}
}
