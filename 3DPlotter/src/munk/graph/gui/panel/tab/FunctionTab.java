package munk.graph.gui.panel.tab;

import java.awt.event.ActionListener;
import java.util.List;

import javax.vecmath.Color3f;

import munk.emesp.exceptions.IllegalExpressionException;
import munk.graph.function.Function;
import munk.graph.gui.panel.gridoption.GridOptionPanel;

public interface FunctionTab {

	public void updateColors();
	public void updateReferences(Function f) throws IllegalExpressionException;
	
	public void deletePlot(Function f);
	public void addPlot(Function f);
	public List<Function> getFunctionList();
	
	GridOptionPanel getGridOptionPanel();
	int getNoOfInputs();
	
	public void addPlot(String[] expr, Color3f color, String[] bounds, float[] stepSize) throws IllegalExpressionException;
	public void addActionListener(ActionListener a);
	public void setSelected(Function function);
}
