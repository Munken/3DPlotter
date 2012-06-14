package munk.mesp.node.function;

import munk.mesp.Expression;
import munk.mesp.node.operator.MultiplyNode;

public abstract class DifferentialFunctionNode extends AbstractFunctionNode{

	public DifferentialFunctionNode(String name, boolean negate) {
		super(name, negate);
	}
	
	@Override
	public Expression getDerivative(String variable) {
		Expression inner = getChild(0);
		Expression innerDiff = inner.getDerivative(variable);
		
		Expression outerDiff = getOuterDerivative(variable);
		
		return new MultiplyNode(innerDiff, outerDiff);
	}

	protected abstract Expression getOuterDerivative(String variable);

}
