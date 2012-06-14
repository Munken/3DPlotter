package munk.mesp.node.function;

import munk.mesp.Expression;
import munk.mesp.VariableValues;
import munk.mesp.node.operator.*;
import munk.mesp.node.values.ValueNode;

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

