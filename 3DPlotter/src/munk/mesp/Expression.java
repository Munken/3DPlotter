package munk.mesp;

public interface Expression {
	
	public double eval(VariableValues varVal);
	
	public Expression getDerivative(String variable);
	
	public String[] getVariableNames();
	
	public String[] getFunctionNames();
	
	public void ensureVariablesDefined(VariableValues map) throws UndefinedVariableException;

}
