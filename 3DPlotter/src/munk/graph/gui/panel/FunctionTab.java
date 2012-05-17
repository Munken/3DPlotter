package munk.graph.gui.panel;

import java.awt.event.ActionListener;
import java.util.List;

import javax.vecmath.Color3f;

import munk.graph.function.Function;
import munk.graph.function.IllegalEquationException;

import com.graphbuilder.math.ExpressionParseException;
import com.graphbuilder.math.UndefinedVariableException;

public interface FunctionTab{

	public void updateColors();
	public void updateReferences(Function f) throws ExpressionParseException;
	
	public void deletePlot(Function f);
	public void addPlot(Function f);
	public List<Function> getFunctionList();
	
	GridOptionPanel getGridOptionPanel();
	int getNoOfInputs();
	
	public void addPlot(String[] expr, Color3f color, String[] bounds, float[] stepSize) throws ExpressionParseException, IllegalEquationException, UndefinedVariableException;
	public void addActionListener(ActionListener a);
}
