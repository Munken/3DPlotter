package munk.graph.plot.parametric;

import munk.graph.function.IllegalEquationException;
import munk.graph.plot.AbstractPlotter;

import com.graphbuilder.math.*;

public abstract class AbstractParametric extends AbstractPlotter implements ParametricPlotter{
	
	private VarMap vm;
	private FuncMap fm;
	private Expression xNode;
	private Expression yNode;
	private Expression zNode;
	
	public AbstractParametric(String xExpr, String yExpr, String zExpr, 
									String[] variables, float[] startValues) 
											throws ExpressionParseException, IllegalEquationException, UndefinedVariableException {
		if (variables.length != startValues.length) {
			throw new IllegalEquationException("There must be an equal amount of startvalues and variables");
		}
		
		vm = new VarMap();
		for (int i = 0; i < variables.length; i++) {
			String var = variables[i];
			vm.setValue(var, startValues[i]);
		}
		
		xNode = ExpressionTree.parse(xExpr);
		yNode = ExpressionTree.parse(yExpr);
		zNode = ExpressionTree.parse(zExpr);
		
		fm = new FuncMap();
		fm.loadDefaultFunctions();
		
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
		return (float) ex.eval(vm, fm);
	}
	


}
