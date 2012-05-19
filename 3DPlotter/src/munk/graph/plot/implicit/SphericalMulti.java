package munk.graph.plot.implicit;

import munk.graph.function.IllegalEquationException;

import com.graphbuilder.math.ExpressionParseException;
import com.graphbuilder.math.UndefinedVariableException;

public class SphericalMulti extends ImplicitMulti {

	float phiMin;
	float phiMax;
	float thetaMin;
	float thetaMax;
	float rMin;
	float rMax;
	
	public SphericalMulti(String expression, float rMin, float rMax,
			float thetaMin, float thetaMax, float phiMin, float phiMax, float[] stepsizes)
			throws ExpressionParseException, IllegalEquationException,
			UndefinedVariableException {
		// Set up a maximum cube.
		super(expression, -rMax, rMax, -rMax, rMax, -rMax, rMax, new float[]{stepsizes[0],stepsizes[0],stepsizes[0]});
		this.rMin = rMin;
		this.rMax = rMax;
		this.thetaMin = thetaMin;
		this.thetaMax = thetaMax;
		this.phiMin = phiMin;
		this.phiMax = phiMax;
	}

	protected boolean validPosition(int x, int y, int z) {
		boolean validCube = super.validPosition(x, y, z);
		
		if (validCube) {
			float xReal = x*stepsizes[0]-rMax;
			float yReal = y*stepsizes[0]-rMax;
			float zReal = z*stepsizes[0]-rMax;
			// Radius bounds.
			double r = Math.sqrt(Math.pow(xReal, 2)+ Math.pow(yReal, 2) + Math.pow(zReal, 2));
			if((r+0.1f) > rMin && (r-0.1f) < rMax){
				//Phi bounds.
				//			if(Math.atan(yReal/xReal) > phiMin && Math.atan(yReal/xReal) < phiMax){
				// Theta bounds
				//				if(Math.acos(zReal/r) > thetaMin && Math.acos(zReal/r) < thetaMax){
				return true;
				//				}
				//			}
			}
			
			return false;
		}
		else 
			return false;
	}	
}