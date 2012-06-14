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
		// > 0 parameters
		setFunction("avg", new Avg());
		setFunction("max", new Max());
		setFunction("min", new Min());
		
		// 0 parameter
		setFunction("e", new EulerNumber());
		setFunction("pi", new Pi());
		setFunction("rand", new Rand());
		
		// 1 parameter
		setFunction("sign", new Sign());
		setFunction("round", new Round());
		setFunction("ceil", new Ceil());
		setFunction("floor", new Floor());
		setFunction("abs", new Abs());
		
		setFunction("sqrt", new Sqrt());
		
		// Logaritms
		setFunction("ln", new Ln());
		setFunction("log", new Log());
		setFunction("lg", new Lg());
		
		// Normal trig formulas
		setFunction("cos", new Cos());
		setFunction("sin", new Sin());
		setFunction("tan", new Tan());
		
		setFunction("acos", new Acos());
		setFunction("asin", new Asin());
		setFunction("atan", new Atan());
		
		setFunction("cosh", new Cosh());
		setFunction("sinh", new Sinh());
		setFunction("tanh", new Tanh());
		
		setFunction("asinh", new Asinh());
		setFunction("acosh", new Acosh());
		setFunction("atanh", new Atanh());
		
		// 2 parameter
		setFunction("mod", new Mod());
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
