package munk.mesp.node.function;

import munk.mesp.Expression;
import munk.mesp.VariableValues;
import munk.mesp.node.operator.*;
import munk.mesp.node.values.ValueNode;

public class Asinh extends DifferentialOneVariableFunctionNode {

	public Asinh() {
		this(false);
	}
	
	public Asinh(boolean negate) {
		super("asinh", negate);
	}
	
	public Asinh(Expression inner, boolean negate) {
		this(negate);
		addChild(inner);
	}

	
	@Override
	/**
	Returns the value of ln(x + sqrt(1 + x<sup>2</sup>)), where x is the value at index
	location 0.
	*/
	public double eval(VariableValues varVal) {
		double innerValue = getInner().eval(varVal);
		
		return Math.log(innerValue + Math.sqrt(1 + innerValue * innerValue));
	}
	@Override
	protected Expression getOuterDerivative(String variable) {
		PowerNode innerFunctionSquared = new PowerNode(getInner(), ValueNode.TWO);
		PlusNode innerSqrt = new PlusNode(ValueNode.ONE, innerFunctionSquared);
		
		Sqrt denom = new Sqrt(innerSqrt, isNegate());
		
		return new DivideNode(ValueNode.ONE, denom);
	}

}
