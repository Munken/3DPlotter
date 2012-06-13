package com.graphbuilder.math.func;

import com.graphbuilder.math.*;

/**
The arc tangent function.

@see java.lang.Math#atan(double)
*/
public class AtanFunction extends AbstractOneVariableFunction {

	public AtanFunction() {}

	/**
	Returns the arc tangent of the value at index location 0.
	*/
	public double of(double[] d, int numParam) {
		return java.lang.Math.atan(d[0]);
	}

	/**
	Returns true only for 1 parameter, false otherwise.
	*/
	public boolean acceptNumParam(int numParam) {
		return numParam == 1;
	}

	public String toString() {
		return "atan(x)";
	}
	
	@Override
	public Expression getOuterDerivative(Expression innerFunction, FuncMap f, String varName) {
		PowNode innerFunctionSquared = new PowNode(innerFunction, Expression.TWO);
		
		AddNode denom = new AddNode(Expression.ONE, innerFunctionSquared);
		
		return new DivNode(Expression.ONE, denom);
	}
}