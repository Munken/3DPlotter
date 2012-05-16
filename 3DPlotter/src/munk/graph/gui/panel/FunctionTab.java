package munk.graph.gui.panel;

import javax.vecmath.Color3f;

import munk.graph.function.Function;
import munk.graph.function.IllegalEquationException;
import munk.graph.gui.listener.FunctionListener;

import com.graphbuilder.math.ExpressionParseException;
import com.graphbuilder.math.UndefinedVariableException;

public interface FunctionTab{

	public void updateColors();
	public void updateReferences(Function f) throws ExpressionParseException;
	public void addFunctionListener(FunctionListener f);

	GridOptionPanel getGridOptionPanel();
	int getNoOfInputs();
	
	public void addPlot(String[] expr, Color3f color, String[] bounds, float[] stepSize) throws ExpressionParseException, IllegalEquationException, UndefinedVariableException;
	
}
