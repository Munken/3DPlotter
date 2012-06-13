package com.graphbuilder.math.func;

import com.graphbuilder.math.*;

/**
The hyperbolic tangent sine function.
*/
public class AtanhFunction extends AbstractOneVariableFunction {

	public AtanhFunction() {}

	/**
	Returns the value of (ln(1+x) - ln(1-x)) / 2, where x is the value at index location 0.
	*/
	public double of(double[] d, int numParam) {
		return (Math.log(1 + d[0]) - Math.log(1 - d[0])) / 2;
	}

	/**
	Returns true only for 1 parameter, false otherwise.
	*/
	public boolean acceptNumParam(int numParam) {
		return numParam == 1;
	}

	public String toString() {
		return "atanh(x)";
	}
	
	@Override
	public Expression getOuterDerivative(Expression innerFunction, FuncMap f, String varName) {
		ValNode oneHalf = new ValNode(0.5);
		
		AddNode leftDenom = new AddNode(innerFunction, Expression.ONE);
		DivNode left = new DivNode(oneHalf, leftDenom);
		
		SubNode rightDenom = new SubNode(innerFunction, Expression.ONE);
		DivNode right = new DivNode(oneHalf, rightDenom);
		
		return new SubNode(left, right);
	}
	
}