package munk.graph.function;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;

import com.graphbuilder.math.ExpressionParseException;

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
		this(expr, color, bounds);
		addActionListener(a);
	}
	
	public AbstractFunction(String[] expr, Color3f color, float[] bounds) {
		this.expr = expr;
		this.visible = true;
		this.selected = false;
		this.color = color;
		this.bounds = bounds;
		listeners = new ArrayList<ActionListener>();
	}
	
	/*
	 * Implemented differently for each function type.
	 */
	protected abstract BranchGroup plot() throws ExpressionParseException;
	
	/*
	 * Lazy evaluation of plotting.
	 */
	public BranchGroup getPlot() throws ExpressionParseException{
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

	public float[] getBounds() {
		return bounds;
	}

}