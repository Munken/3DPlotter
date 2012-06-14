package munk.emesp.node.values;

import munk.emesp.*;
import munk.emesp.node.NamedNode;

public class VariableNode extends NamedNode {
	
	
	public VariableNode(String name, boolean negate) {
		super(name, negate);
	}
	
	@Override
	public double eval(VariableValues varVal) {
		double val = varVal.getValue(getName());

		if (getNegate()) val = -val;

		return val;
	}

	@Override
	public Expression getDerivative(String variable) {
		return (getName().equals(variable)) ? ValueNode.ONE : ValueNode.ZERO;
	}

	@Override
	public void toString(StringBuffer buffer) {
		if (getNegate()) {
			buffer.append("(");
			buffer.append("-");
		}
		
		buffer.append(getName());
		
		if (getNegate())
			buffer.append(")");
	}

}
