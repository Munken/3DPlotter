package munk.mesp.node.function;

import munk.mesp.Expression;
import munk.mesp.VariableValues;
import munk.mesp.node.operator.*;
import munk.mesp.node.values.ValueNode;

public class Acos extends DifferentialOneVariableFunctionNode {

	public Acos() {
		this(false);
	}
	
	public Acos(boolean negate) {
		super("acos", negate);
	}
	
	public Acos(Expression inner, boolean negate) {
		this(negate);
		addChild(inner);
	}

	@Override
	public double eval(VariableValues varVal) {
		double innerValue = getInner().eval(varVal);
		
		return Math.acos(innerValue);
	}

	@Override
	public Expression getOuterDerivative(String variable) {
		PowerNode innerSquared = new PowerNode(getInner(), ValueNode.TWO);
		MinusNode sqrtInner = new MinusNode(ValueNode.ONE, innerSquared);
		
		Sqrt sqrt = new Sqrt(sqrtInner, !isNegate());
		
		return new DivideNode(ValueNode.ONE, sqrt);
	}

}
