package munk.graph.gui;

import java.awt.Color;
import java.awt.event.FocusListener;

import munk.graph.function.Function;

public interface FunctionLabel {

	static final Color	FAILED_COLOR	= Color.RED;
	static final Color	NORMAL_COLOR	= Color.WHITE;
	static final Color  WARNING_COLOR = new Color(255, 215, 0); // Golden yellow
	
	static final int UPDATE = 0;
	static final int FOCUS_GAINED = 1;
	static final int DELETE = 2;
	static final int VISIBILITY = 3;
	static final int HIDEEDIT = 4;
	
	public void setMother(Function f);
	public void setIndeterminate(boolean b);
	
	public void addFocusListener(FocusListener f);
	
}
