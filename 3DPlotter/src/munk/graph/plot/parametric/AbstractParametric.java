package munk.graph.plot.parametric;

import munk.emesp.*;
import munk.emesp.exceptions.IllegalExpressionException;
import munk.graph.plot.AbstractPlotter;


public abstract class AbstractParametric extends AbstractPlotter implements ParametricPlotter{
	
	private VariableValues vm;
	private Expression xNode;
	private Expression yNode;
	private Expression zNode;
	
	public AbstractParametric(String xExpr, String yExpr, String zExpr, 
									String[] variables, float[] startValues) 
											throws IllegalExpressionException {
		if (variables.length != startValues.length) {
			throw new IllegalExpressionException("There must be an equal amount of startvalues and variables");
		}
		
		vm = new VariableValues();
		for (int i = 0; i < variables.length; i++) {
			String var = variables[i];
			vm.setValue(var, startValues[i]);
		}
		
		FunctionMap fm = FunctionMap.getDefault();
		xNode = ExpressionParser.parse(xExpr, fm);
		yNode = ExpressionParser.parse(yExpr, fm);
		zNode = ExpressionParser.parse(zExpr, fm);
		
		xNode.ensureVariablesDefined(vm);
		yNode.ensureVariablesDefined(vm);
		zNode.ensureVariablesDefined(vm);
	}
	
	public void setVariable(String var, float value) {
		vm.setValue(var, value);
	}
	
	public float xValue() {
		return evalNode(xNode);
	}
	
	public float yValue() {
		return evalNode(yNode);
	}
	
	public float zValue() {
		return evalNode(zNode);
	}
	
	private float evalNode(Expression ex) {
		return (float) ex.eval(vm);
	}
	


}
