package munk.mesp.node.operator;

import munk.mesp.Expression;

public interface OperatorNode extends Expression {

	public Expression getLeftChild();
	public Expression getRightChild();
}
