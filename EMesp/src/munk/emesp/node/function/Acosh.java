package munk.emesp.node.function;

import munk.emesp.Expression;
import munk.emesp.VariableValues;
import munk.emesp.node.operator.*;
import munk.emesp.node.values.ValueNode;

public class Acosh extends DifferentialOneVariableFunctionNode {

	public Acosh() {
		this(false);
	}
	
	public Acosh(boolean negate) {
		super("acosh", negate);
	}
	
	public Acosh(Expression inner, boolean negate) {
		this(negate);
		addChild(inner);
	}

	
	@Override
	/**
	Returns the value of 2 * ln(sqrt((x+1)/2) + sqrt((x-1)/2)), where x is the
	value at index location 0.
	*/
	public double eval(VariableValues varVal) {
		double inner = getInner().eval(varVal);
		double a = Math.sqrt((inner + 1) / 2);
		double b = Math.sqrt((inner - 1) / 2);
		return 2 * Math.log(a + b);
	}
	@Override
	protected Expression getOuterDerivative(String variable) {
		MinusNode firstInside = new MinusNode(ValueNode.ONE, getInner());
		FunctionNode firstSqrt = new Sqrt(firstInside, isNegate());
		
		PlusNode secondInside = new PlusNode(ValueNode.ONE, getInner());
		FunctionNode secondSqrt = new Sqrt(secondInside, false);
		
		MultiplyNode denom = new MultiplyNode(firstSqrt, secondSqrt);
		
		return new DivideNode(ValueNode.ONE, denom);
	}

}
