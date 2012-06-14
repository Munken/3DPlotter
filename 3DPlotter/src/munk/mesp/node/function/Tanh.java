package munk.mesp.node.function;

import munk.mesp.Expression;
import munk.mesp.VariableValues;
import munk.mesp.node.operator.DivideNode;
import munk.mesp.node.operator.PowerNode;
import munk.mesp.node.values.ValueNode;

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