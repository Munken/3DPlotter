package munk.emesp.node.function;

import munk.emesp.Expression;
import munk.emesp.VariableValues;
import munk.emesp.node.operator.DivideNode;
import munk.emesp.node.operator.PowerNode;
import munk.emesp.node.values.ValueNode;

public class Tanh extends DifferentialOneVariableFunctionNode {

	public Tanh() {
		this(false);
	}
	
	public Tanh(boolean negate) {
		super("tanh", negate);
	}
	
	public Tanh(Expression inner, boolean negate) {
		this(negate);
		addChild(inner);
	}
	
	@Override
	public double eval(VariableValues varVal) {
		double innerValue = getInner().eval(varVal);
		
		return Math.tanh(innerValue);
	}

	@Override
	public Expression getOuterDerivative(String variable) {
		ValueNode nominator = (!isNegate()) ? ValueNode.ONE : ValueNode.MINUS_ONE;
		Cosh cosh = new Cosh(getInner(), false);
		PowerNode coshSquared = new PowerNode(cosh, ValueNode.TWO);
		
		return new DivideNode(nominator, coshSquared);
	}

}