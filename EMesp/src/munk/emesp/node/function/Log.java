package munk.emesp.node.function;

import munk.emesp.Expression;
import munk.emesp.VariableValues;
import munk.emesp.node.operator.DivideNode;
import munk.emesp.node.values.ValueNode;

public class Log extends DifferentialOneVariableFunctionNode {

	public Log() {
		this(false);
	}
	
	public Log(boolean negate) {
		super("log", negate);
	}
	
	public Log(Expression inner, boolean negate) {
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
		double log10toLn = 1 / Math.log(10);
		if (isNegate()) 
			log10toLn = -log10toLn;
		
		ValueNode nominator = new ValueNode(log10toLn);
		
		return new DivideNode(nominator, getInner());
	}



}
