package munk.graph.gui.panel;

import java.util.HashMap;

import munk.graph.function.Function;
import munk.graph.gui.ColorList;
import munk.graph.gui.FunctionLabel;
import munk.graph.gui.Plotter3D;

@SuppressWarnings("serial")
public class SphericalFunctionTab extends AbstractFunctionTab {

	public SphericalFunctionTab(ColorList colorList, HashMap<Function, FunctionLabel> map, Function templateFunc, Plotter3D plotter) throws Exception{
		super(colorList, map, templateFunc, plotter);
		super.init();
	}

	public GridOptionPanel getGridOptionPanel(){
		return new StdGridOptionPanel(new String[]{"0","1","0","pi","0","2*pi"});
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
}
