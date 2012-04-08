package munk.graph.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.vecmath.Color3f;

public class Function {
	
	private String equation;
	private Color3f color;
	private Boolean selected;
	private Boolean visible;
	private ActionListener a;

	public Function(String equation, Color3f color, ActionListener a){
		this.equation = equation;
		this.visible = true;
		this.selected = false;
		this.color = color;
		this.a = a;
	}

	public String getEquation() {
		return equation;
	}

	public void setEquation(String equation) {
		this.equation = equation;
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
		a.actionPerformed(new ActionEvent(this, 0, "Visibility Changed"));
	}
	
	public boolean isSelected(){
		return selected;
	}
	
	public void setSelected(boolean b){
		selected = b;
	}
}
