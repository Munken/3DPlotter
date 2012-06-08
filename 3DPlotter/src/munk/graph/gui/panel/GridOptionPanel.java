package munk.graph.gui.panel;

import munk.graph.function.Function;
import munk.graph.gui.listener.FunctionListener;

import com.graphbuilder.math.ExpressionParseException;

public interface GridOptionPanel{

	public void updateFuncReference(Function f) throws ExpressionParseException;
	public void addFunctionListener(FunctionListener f);
	
	public String[] getGridBounds() throws ExpressionParseException;
	public float[] getGridStepSize() throws ExpressionParseException;
	public void setGridBounds(String[] bounds);
	public void setSliders(float[] stepSize) throws ExpressionParseException;
	
}
