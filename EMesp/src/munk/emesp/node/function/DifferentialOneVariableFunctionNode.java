package munk.emesp.node.function;

import munk.emesp.Expression;
import munk.emesp.node.operator.MultiplyNode;

public abstract class DifferentialOneVariableFunctionNode extends AbstractFunctionNode{

	public DifferentialOneVariableFunctionNode(String name, boolean negate) {
		super(name, negate);
	}
	
	@Override
	public Expression getDerivative(String variable) {
		Expression inner = getInner();
		Expression innerDiff = inner.getDerivative(variable);
		
		Expression outerDiff = getOuterDerivative(variable);
		
		return new MultiplyNode(innerDiff, outerDiff);
	}
	
	protected Expression getInner() {
		return getChild(0);
	}
	
	@Override
	protected int nParameters() {
		return 1;
	}

	protected abstract Expression getOuterDerivative(String variable);

}
