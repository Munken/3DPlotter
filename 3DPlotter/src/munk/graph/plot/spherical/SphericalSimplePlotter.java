package munk.graph.plot.spherical;

import static munk.graph.plot.PlotUtil.initAxisArray;

import javax.media.j3d.*;
import javax.vecmath.Point3f;

import munk.emesp.*;
import munk.emesp.exceptions.IllegalExpressionException;
import munk.graph.plot.AbstractPlotter;
import munk.graph.plot.PlotUtil;

public class SphericalSimplePlotter extends AbstractPlotter {
	
	private float phiMin;
	private int phiLength;
	
	private float thetaMin;
	private int thetaLength;
	
	private float phiStepsize;
	private float thetaStepsize;
	
	private Expression expression;
	private VariableValues vm;
	private Shape3D	shape;
	
	public SphericalSimplePlotter(String expression, 
			float rMin, float rMax, 
			float phiMin, float phiMax, 
			float thetaMin, float thetaMax, 
			float rStepsize, float phiStepsize, float thetaStepsize) 
					throws IllegalExpressionException {
		this.phiMin = phiMin;
		this.thetaMin = thetaMin;
		
		thetaLength = (int) ((thetaMax - thetaMin) / thetaStepsize) + 1;
		phiLength = (int) ((phiMax - phiMin) / phiStepsize) + 2;
		
		this.phiStepsize = phiStepsize;
		this.thetaStepsize = thetaStepsize;
		
		this.expression = ExpressionParser.parse(expression, FunctionMap.getDefaultFunctionMap());
		vm = new VariableValues();
		vm.setValue("phi", phiMin);
		vm.setValue("theta", thetaMin);
		
		this.expression.ensureVariablesDefined(vm);
	}
	
	public SphericalSimplePlotter(String expression, 
			float[] bounds, float[] stepsize) 
					throws IllegalExpressionException {
		this(expression, bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5],
				stepsize[0], stepsize[1], stepsize[2]);
	}
	

	@Override
	protected Node plot() {
		
		Point3f[][] points = new Point3f[thetaLength][phiLength];
		float[] thetaValues = initAxisArray(thetaMin, thetaLength, thetaStepsize);
		float[] phiValues = initAxisArray(phiMin, phiLength, phiStepsize);
		
		for (int i = 0; i < thetaValues.length; i++) {
			float theta = thetaValues[i];
			vm.setValue("theta", theta);
			for (int j = 0; j < phiValues.length; j++) {
				float phi = phiValues[j];
				vm.setValue("phi", phi);
				
				float r = (float) expression.eval(vm);
				
				float x = (float) (r * Math.sin(theta) * Math.cos(phi));
				float y = (float) (r * Math.sin(theta) * Math.sin(phi));
				float z = (float) (r * Math.cos(theta));
				
				points[i][j] = new Point3f(x, y, z);
			}
		}
		
		if (points.length > 1) {
			GeometryArray quad = PlotUtil.buildQuadArray(points);

			shape = new Shape3D(quad);

			setShape(shape);
			return shape;
		} else 
			return null;
	}

}
