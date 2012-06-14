package munk.mesp;

import java.util.*;

import munk.mesp.node.function.*;

public class FunctionMap {
	
	private Map<String, FunctionNode> map = new HashMap<String, FunctionNode>();
	private boolean caseSensitive = false;
	
	public FunctionMap() {}

	public FunctionMap(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}
	
	public void loadDefaultFunctions() {
		setFunction("sqrt", new Sqrt());
		
		// Normal trig formulas
		setFunction("cos", new Cos());
		setFunction("sin", new Sin());
		setFunction("tan", new Tan());
		
		setFunction("acos", new Acos());
		setFunction("asin", new Asin());
		setFunction("atan", new Atan());
	}
	
	public FunctionNode getFunction(String functionName, boolean negate) {
		if (!caseSensitive)
			functionName = functionName.toLowerCase();
			
		FunctionNode function = map.get(functionName);
		if (function != null) {
			FunctionNode result = function.clone();
			result.setNegate(negate);
			return result;
		}
		
		throw new RuntimeException("Function not found: " + functionName);
	}
	
	public void setFunction(String functionName, FunctionNode prototype) {
		if (functionName == null)
			throw new IllegalArgumentException("function name cannot be null");

		if (prototype == null)
			throw new IllegalArgumentException("function cannot be null");
		
		if (prototype.getNumberChildren() > 0)
			throw new IllegalArgumentException("The prototype must not have children");
		
		if (!caseSensitive)
			functionName = functionName.toLowerCase();
		
		map.put(functionName, prototype);
	}
	
	public List<String> getFunctionNames() {
		return new ArrayList<String>(map.keySet());
	}
	
	public void remove(String functionName) {
		map.remove(functionName);
	}

}
