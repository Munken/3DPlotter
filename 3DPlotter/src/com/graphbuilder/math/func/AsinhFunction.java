package com.graphbuilder.math.func;

import com.graphbuilder.math.*;

/**
The hyperbolic arc sine function.
*/
public class AsinhFunction extends AbstractOneVariableFunction {

	public AsinhFunction() {}

	/**
	Returns the value of ln(x + sqrt(1 + x<sup>2</sup>)), where x is the value at index
	location 0.
	*/
	public double of(double[] d, int numParam) {
		return Math.log(d[0] + Math.sqrt(1 + d[0] * d[0]));
	}

	/**
	Returns true only for 1 parameter, false otherwise.
	*/
	public boolean acceptNumParam(int numParam) {
		return numParam == 1;
	}

	public String toString() {
		return "asinh(x)";
	}
	
	@Override
	public Expression getOuterDerivative(Expression innerFunction, FuncMap f, String varName) {
		FuncNode denom = new FuncNode("sqrt", false);
		PowNode innerFunctionSquared = new PowNode(innerFunction, Expression.TWO);
		
		AddNode inner = new AddNode(Expression.ONE, innerFunctionSquared);
		denom.add(inner);
		
		return new DivNode(Expression.ONE, denom);
	}
}