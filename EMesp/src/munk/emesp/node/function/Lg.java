package munk.emesp.node.function;

import munk.emesp.Expression;
import munk.emesp.VariableValues;
import munk.emesp.node.operator.DivideNode;
import munk.emesp.node.values.ValueNode;

public class Lg extends DifferentialOneVariableFunctionNode {

	public Lg() {
		this(false);
	}
	
	public Lg(boolean negate) {
		super("lg", negate);
	}
	
	public Lg(Expression inner, boolean negate) {
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
		double log2toLn = 1 / Math.log(2);
		if (isNegate()) 
			log2toLn = -log2toLn;
		
		ValueNode nominator = new ValueNode(log2toLn);
		
		return new DivideNode(nominator, getInner());
	}



}
