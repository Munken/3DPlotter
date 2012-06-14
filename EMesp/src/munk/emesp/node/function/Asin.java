package munk.emesp.node.function;

import munk.emesp.Expression;
import munk.emesp.VariableValues;
import munk.emesp.node.operator.*;
import munk.emesp.node.values.ValueNode;

public class Asin extends DifferentialOneVariableFunctionNode {

	public Asin() {
		this(false);
	}
	
	public Asin(boolean negate) {
		super("asin", negate);
	}
	
	public Asin(Expression inner, boolean negate) {
		this(negate);
		addChild(inner);
	}

	@Override
	public double eval(VariableValues varVal) {
		double innerValue = getInner().eval(varVal);
		
		return Math.asin(innerValue);
	}

	@Override
	public Expression getOuterDerivative(String variable) {
		PowerNode innerSquared = new PowerNode(getInner(), ValueNode.TWO);
		MinusNode sqrtInner = new MinusNode(ValueNode.ONE, innerSquared);
		
		Sqrt sqrt = new Sqrt(sqrtInner, isNegate());
		
		return new DivideNode(ValueNode.ONE, sqrt);
	}

}
