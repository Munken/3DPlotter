package munk.mesp.node.function;

import munk.mesp.Expression;
import munk.mesp.VariableValues;
import munk.mesp.node.operator.DivideNode;
import munk.mesp.node.operator.PowerNode;
import munk.mesp.node.values.ValueNode;

public class Tan extends DifferentialOneVariableFunctionNode {

	public Tan() {
		this(false);
	}
	
	public Tan(boolean negate) {
		super("tan", negate);
	}
	
	public Tan(Expression inner, boolean negate) {
		this(negate);
		addChild(inner);
	}
	
	@Override
	public double eval(VariableValues varVal) {
		double innerValue = getInner().eval(varVal);
		
		return Math.tan(innerValue);
	}

	@Override
	public Expression getOuterDerivative(String variable) {
		ValueNode nominator = (!isNegate()) ? ValueNode.ONE : ValueNode.MINUS_ONE;
		Cos cos = new Cos(getInner(), false);
		PowerNode cosSquared = new PowerNode(cos, ValueNode.TWO);
		
		return new DivideNode(nominator, cosSquared);
	}

}