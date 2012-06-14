package munk.mesp.node.function;

import munk.mesp.Expression;
import munk.mesp.VariableValues;
import munk.mesp.node.operator.*;
import munk.mesp.node.values.ValueNode;

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
