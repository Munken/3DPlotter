package munk.graph.plot;

import javax.media.j3d.Shape3D;

import com.graphbuilder.math.*;

public abstract class ParametricPlotter {
	
	private VarMap vm;
	private FuncMap fm;
	private Expression xNode;
	private Expression yNode;
	private Expression zNode;
	
	public ParametricPlotter(String xExpr, String yExpr, String zExpr, String[] variables, float[] startValues, float stepSize) throws ExpressionParseException {
		if (variables.length != startValues.length) {
			throw new IllegalStateException("There must be an equal amount of startvalues and variables");
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
	}
	
	public ParametricPlotter(String xExpr, String yExpr, String zExpr, String[] variables, float[] startValues) throws ExpressionParseException {
		this(xExpr, yExpr, zExpr, variables, startValues, 0.1f);
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
	
	public abstract Shape3D getPlot();

}
