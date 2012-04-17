package munk.graph.gui;

import java.awt.Color;

import munk.graph.function.Function;

public interface FunctionLabel {

	static final Color	FAILED_COLOR	= Color.RED;
	static final Color	NORMAL_COLOR	= Color.WHITE;
	static final Color  WARNING_COLOR = new Color(255, 215, 0); // Golden yellow
	
	public void setMother(Function f);
	public void setIndeterminate(boolean b);
	
}
