package munk.emesp.node.function;

import munk.emesp.Expression;
import munk.emesp.VariableValues;
import munk.emesp.node.operator.*;
import munk.emesp.node.values.ValueNode;

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
