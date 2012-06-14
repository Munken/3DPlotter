package munk.mesp.node.function;

import munk.mesp.Expression;
import munk.mesp.VariableValues;
import munk.mesp.node.operator.*;
import munk.mesp.node.values.ValueNode;

public class Atanh extends DifferentialOneVariableFunctionNode {

	public Atanh() {
		this(false);
	}
	
	public Atanh(boolean negate) {
		super("atanh", negate);
	}
	
	public Atanh(Expression inner, boolean negate) {
		this(negate);
		addChild(inner);
	}

	
	@Override
	/**
	Returns the value of (ln(1+x) - ln(1-x)) / 2, where x is the value at index location 0.
	*/
	public double eval(VariableValues varVal) {
		double inner = getInner().eval(varVal);
		return (Math.log(1 + inner) - Math.log(1 - inner)) / 2;
	}
	
	
	@Override
	protected Expression getOuterDerivative(String variable) {
		PlusNode plus = new PlusNode(getInner(), ValueNode.ONE);
		
		MinusNode minus = new MinusNode(getInner(), ValueNode.ONE);
		
		ValueNode nominator = ValueNode.HALF;
		
		DivideNode left = new DivideNode(nominator, plus);
		DivideNode right = new DivideNode(nominator, minus);
		
		return (isNegate()) ? new MinusNode(right, left) : new MinusNode(left, right);
		
	}

}
