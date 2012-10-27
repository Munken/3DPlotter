package munk.graph.plot.implicit;

import munk.emesp.exceptions.IllegalExpressionException;

public class SphericalMulti extends ImplicitSlowMulti {

	
	float phiMin;
	float phiMax;
	float thetaMin;
	float thetaMax;
	float rMin;
	float rMax;
		
	public SphericalMulti(String expression, 
			float rMin, float rMax,
			float thetaMin, float thetaMax, 
			float phiMin, float phiMax,
			float rStepsize, float thetaStepsize, float phiStepsize) 
					throws IllegalExpressionException {
		
		super(expression, 
				-rMax, rMax, -rMax, rMax, -rMax, rMax, 
					rStepsize, thetaStepsize, phiStepsize);

		this.phiMin = phiMin;
		this.phiMax = phiMax;
		this.thetaMin = thetaMin;
		this.thetaMax = thetaMax;
		this.rMin = rMin;
		this.rMax = rMax;
		
	}

	public SphericalMulti(String expression, 
			float[] bounds,	float[] stepsize) 
					throws IllegalExpressionException {
		
		this(expression, 
				bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5],
				stepsize[0], stepsize[1], stepsize[2]);
	}

	@Override
	protected boolean validCube(float x, float y, float z) {
		double r = Math.sqrt(x*x + y*y + z*z);
		
		if (r > rMax || r < rMin) 
			return false;
		
		double phi = Math.atan2(y, x) + Math.PI;
		if (phi < phiMin || phi > phiMax)
			return false;
		
		
		double theta = Math.acos(z / r); 
		if (theta < thetaMin || theta > thetaMax)
			return false;
		
		
		return true;
	}

}
