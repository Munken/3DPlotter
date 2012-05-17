package munk.graph.gui.panel;

import java.util.HashMap;

import javax.vecmath.Color3f;

import munk.graph.function.Function;
import munk.graph.function.FunctionUtil;
import munk.graph.function.IllegalEquationException;
import munk.graph.gui.ColorList;
import munk.graph.gui.FunctionLabel;
import munk.graph.gui.GuiUtil;
import munk.graph.gui.Plotter3D;

import com.graphbuilder.math.ExpressionParseException;
import com.graphbuilder.math.UndefinedVariableException;

@SuppressWarnings("serial")
public class XYZFunctionTab extends AbstractFunctionTab {
	
	public XYZFunctionTab(ColorList colorList, HashMap<Function, FunctionLabel> map, Function templateFunc, Plotter3D plotter) throws Exception{
		super(colorList, map, templateFunc, plotter);
		super.init();
	}

	public GridOptionPanel getGridOptionPanel(){
		return new StdGridOptionPanel(new String[]{"-1","1","-1","1","-1","1"});
	}
	
	public int getNoOfInputs(){
		return 1;
	}
	
	public void addPlot(Function function) {
		FunctionLabel label = null;
		label = addXYZPlot(function);
		label.addFunctionListener(createFunctionListener());
		map.put(function, label);
		spawnNewPlotterThread(function);
	}
	
	public Function createNewFunction(String[] expressions, Color3f color, String[] bounds, float[] stepSize) throws ExpressionParseException, UndefinedVariableException, IllegalEquationException{
		for (int i = 0; i < bounds.length; i+=2) {
			if (GuiUtil.evalString(bounds[i]) > GuiUtil.evalString(bounds[i+1])) {
				String tmp = bounds[i+1];
				bounds[i+1] = bounds[i];
				bounds[i] = tmp;
			}
		}
		return FunctionUtil.createFunction(expressions, color, bounds, stepSize);
	}
}
