package munk.graph.function;

import munk.emesp.exceptions.IllegalExpressionException;
import munk.graph.appearance.Colors;
import munk.graph.gui.ColorList;

public class TemplateFunction extends AbstractFunction {
	
	public TemplateFunction(String[] bounds, float[] stepSize, ColorList colorList) throws IllegalExpressionException {
		super(new String[]{""}, Colors.RED, bounds, stepSize, null);
	}

	public void setView(FILL state){
		super.state = state;
	}

}
