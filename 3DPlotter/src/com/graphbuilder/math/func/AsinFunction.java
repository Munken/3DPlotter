package com.graphbuilder.math.func;

import com.graphbuilder.math.*;

/**
The arc sine function.

@see java.lang.Math#asin(double)
*/
public class AsinFunction extends AbstractOneVariableFunction {

	public AsinFunction() {}

	/**
	Returns the arc sine of the value at index location 0.
	*/
	public double of(double[] d, int numParam) {
		return java.lang.Math.asin(d[0]);
	}

	/**
	Returns true only for 1 parameter, false otherwise.
	*/
	public boolean acceptNumParam(int numParam) {
		return numParam == 1;
	}

	public String toString() {
		return "asin(x)";
	}

	@Override
	public Expression getOuterDerivative(Expression innerFunction, FuncMap f, String varName) {
		FuncNode denom = new FuncNode("sqrt", false);
		PowNode innerFunctionSquared = new PowNode(innerFunction, Expression.TWO);
		
		SubNode inner = new SubNode(Expression.ONE, innerFunctionSquared);
		denom.add(inner);
		
		return new DivNode(Expression.ONE, denom);
	}
}