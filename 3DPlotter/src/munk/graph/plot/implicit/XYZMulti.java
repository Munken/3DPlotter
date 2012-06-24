package munk.graph.plot.implicit;

import munk.emesp.exceptions.IllegalExpressionException;

public class XYZMulti extends ImplicitSlowMulti{

	public XYZMulti(String expression, 
			float xMin, float xMax, 
			float yMin, float yMax, 
			float zMin, float zMax, 
			float xStepsize, float yStepsize, float zStepsize) 
					throws IllegalExpressionException {
		
		super(expression, xMin, xMax, yMin, yMax, zMin, zMax, xStepsize, yStepsize,
				zStepsize);
	}

	public XYZMulti(String expression, float[] bounds, float[] stepsizes)
			throws IllegalExpressionException {
		super(expression, bounds, stepsizes);
	}

	@Override
	protected boolean validCube(float x, float y, float z) {
		return true;
	}

}
