package munk.mesp.node.function;

import munk.mesp.Expression;
import munk.mesp.VariableValues;
import munk.mesp.node.operator.*;
import munk.mesp.node.values.ValueNode;

public class Atan extends DifferentialOneVariableFunctionNode {

	public Atan() {
		this(false);
	}
	
	public Atan(boolean negate) {
		super("atan", negate);
	}
	
	public Atan(Expression inner, boolean negate) {
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
		ValueNode nominator = (isNegate()) ? ValueNode.MINUS_ONE : ValueNode.ONE;
		
		PowerNode innerSquared = new PowerNode(getInner(), ValueNode.TWO);
		PlusNode denominator = new PlusNode(ValueNode.ONE, innerSquared);
		
		
		return new DivideNode(nominator, denominator);
	}

}
