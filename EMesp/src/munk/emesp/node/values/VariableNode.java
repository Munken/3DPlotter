package munk.emesp.node.values;

import munk.emesp.Expression;
import munk.emesp.VariableValues;
import munk.emesp.node.NamedNode;
import munk.emesp.visitor.ExpressionVisitor;

public class VariableNode extends NamedNode {
	
	public VariableNode(String name) {
		this(name, false);
	}
	
	public VariableNode(String name, boolean negate) {
		super(name, negate);
	}
	
	@Override
	public double eval(VariableValues varVal) {
		double val = varVal.getValue(getName());

		if (isNegate()) val = -val;

		return val;
	}

	@Override
	public Expression getDerivative(String variable) {
		if (getName().equals(variable))
			return (isNegate()) ? ValueNode.MINUS_ONE : ValueNode.ONE;
		else 
			return ValueNode.ZERO;
	}

	@Override
	public void toString(StringBuffer buffer) {
		if (isNegate()) {
			buffer.append("(");
			buffer.append("-");
		}
		
		buffer.append(getName());
		
		if (isNegate())
			buffer.append(")");
	}
	
	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visit(this);
	}

}
