package munk.graph.gui.panel.tab;

import java.util.HashMap;

import javax.vecmath.Color3f;

import munk.emesp.exceptions.IllegalExpressionException;
import munk.graph.function.Function;
import munk.graph.function.implicit.SphericalFunction;
import munk.graph.gui.*;
import munk.graph.gui.labels.FunctionLabel;
import munk.graph.gui.panel.gridoption.GridOptionPanel;
import munk.graph.gui.panel.gridoption.SphGridOptionPanel;

@SuppressWarnings("serial")
public class SphericalFunctionTab extends AbstractFunctionTab {

	public SphericalFunctionTab(ColorList colorList, HashMap<Function, FunctionLabel> map, Function templateFunc, Plotter3D plotter) throws Exception{
		super(colorList, map, templateFunc, plotter);
		super.init();
	}

	public GridOptionPanel getGridOptionPanel(){
		return new SphGridOptionPanel(new String[]{"0","1","0","pi","0","2*pi"});
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
	
	public Function createNewFunction(String[] expressions, Color3f color, String[] bounds, float[] stepSize) throws IllegalExpressionException {
		for (int i = 0; i < bounds.length; i+=2) {
			if (GuiUtil.evalString(bounds[i]) > GuiUtil.evalString(bounds[i+1])) {
				String tmp = bounds[i+1];
				bounds[i+1] = bounds[i];
				bounds[i] = tmp;
			}
		}
		return new SphericalFunction(expressions, color, bounds, stepSize);
	}
}
