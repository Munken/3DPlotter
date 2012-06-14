package munk.emesp.node.function;

import munk.emesp.Expression;
import munk.emesp.VariableValues;
import munk.emesp.node.operator.DivideNode;
import munk.emesp.node.values.ValueNode;

public class Ln extends DifferentialOneVariableFunctionNode {

	public Ln() {
		this(false);
	}
	
	public Ln(boolean negate) {
		super("ln", negate);
	}
	
	public Ln(Expression inner, boolean negate) {
		this(negate);
		addChild(inner);
	}

	@Override
	public double eval(VariableValues varVal) {
		double innerValue = getInner().eval(varVal);
		
		return Math.log10(innerValue);
	}

	@Override
	protected Expression getOuterDerivative(String variable) {
		
		ValueNode nominator = (isNegate()) ? ValueNode.MINUS_ONE : ValueNode.ONE;
		
		return new DivideNode(nominator, getInner());
	}



}
