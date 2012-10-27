package munk.emesp.node.function;

import munk.emesp.Expression;
import munk.emesp.VariableValues;
import munk.emesp.node.operator.DivideNode;
import munk.emesp.node.values.ValueNode;

public class Sqrt extends DifferentialOneVariableFunctionNode {

	public Sqrt() {
		this(false);
	}
	
	public Sqrt(boolean negate) {
		super("sqrt", negate);
	}
	
	public Sqrt(Expression inner, boolean negate) {
		this(negate);
		addChild(inner);
	}

	@Override
	public double eval(VariableValues varVal) {
		double innerValue = getInner().eval(varVal);
		
		return Math.sqrt(innerValue);
	}

	@Override
	public Expression getOuterDerivative(String variable) {
		return new DivideNode(ValueNode.HALF, this);
	}

}

