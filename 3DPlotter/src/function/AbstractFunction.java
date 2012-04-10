package function;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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

	public AbstractFunction(String[] expr, Color3f color, ActionListener a){
		this.expr = expr;
		this.visible = true;
		this.selected = false;
		this.color = color;
		listeners = new ArrayList<ActionListener>();
		addActionListener(a);
	}
	
	public AbstractFunction(Function oldFunc, String[] newExpr) {
		this.color = oldFunc.getColor();
		this.selected = oldFunc.isSelected();
		this.visible = oldFunc.isVisible();
		this.listeners = oldFunc.getListeners();
		this.expr = newExpr;
	}
	
	protected abstract void setShape();
	
	protected abstract BranchGroup plot();
	
	public BranchGroup getPlot(){
		if(plot == null){
			plot = plot();
		}
		return plot;
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
	
	public ArrayList<ActionListener> getListeners(){
		return listeners;
	}
	
	public void addActionListener(ActionListener a){
		listeners.add(a);
	}
	
	private void signallAll(ActionEvent event){
		for(ActionListener a : listeners){
			a.actionPerformed(event);
		}
	}
}
