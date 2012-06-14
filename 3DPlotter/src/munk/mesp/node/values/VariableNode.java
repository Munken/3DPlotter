package munk.mesp.node.values;

import munk.mesp.*;
import munk.mesp.node.NamedNode;

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
		// TODO Auto-generated method stub
		return null;
	}

}
