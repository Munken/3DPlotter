package munk.graph.gui.panel.gridoption;

import munk.emesp.exceptions.IllegalExpressionException;
import munk.graph.function.Function;
import munk.graph.gui.listener.FunctionListener;

public interface GridOptionPanel{

	public void updateFuncReference(Function f) throws IllegalExpressionException;
	public void addFunctionListener(FunctionListener f);
	
	public String[] getGridBounds() throws IllegalExpressionException;
	public float[] getGridStepSize() throws IllegalExpressionException;
	public void setGridBounds(String[] bounds);
	public void setSliders(float[] stepSize) throws IllegalExpressionException;
	
}
