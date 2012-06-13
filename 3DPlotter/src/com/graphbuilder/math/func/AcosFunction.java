package com.graphbuilder.math.func;

import com.graphbuilder.math.*;


/**
The arc cosine function.

@see java.lang.Math#acos(double)
*/
public class AcosFunction extends AbstractOneVariableFunction {

	public AcosFunction() {}

	/**
	Returns the arc cosine of the value at index location 0.
	*/
	public double of(double[] d, int numParam) {
		return java.lang.Math.acos(d[0]);
	}

	/**
	Returns true only for 1 parameter, false otherwise.
	*/
	public boolean acceptNumParam(int numParam) {
		return numParam == 1;
	}

	public String toString() {
		return "acos(x)";
	}

	@Override
	protected Expression getOuterDerivative(Expression innerFunction, FuncMap fm, String varName) {
		FuncNode sqrt = new FuncNode("sqrt", true);
		MultNode innerFunctionSquared = new MultNode(innerFunction, Expression.TWO);
		SubNode inner = new SubNode(Expression.ONE, innerFunctionSquared);
		
		sqrt.add(inner);
		return new DivNode(Expression.ONE, sqrt);
	}
}