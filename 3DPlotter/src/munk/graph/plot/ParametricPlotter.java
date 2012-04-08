package munk.graph.plot;

import javax.media.j3d.Shape3D;

import org.nfunk.jep.*;

public abstract class ParametricPlotter {
	
	private JEP jep;
	private Node xNode;
	private Node yNode;
	private Node zNode;
	
	public ParametricPlotter(String xExpr, String yExpr, String zExpr, String[] variables, float[] startValues, float stepSize) throws ParseException {
		jep = new JEP();
		jep.addStandardFunctions();
		jep.addStandardConstants();
		
		if (variables.length != startValues.length) {
			throw new IllegalStateException("There must be an equal amount of startvalues and variables");
		}
		
		for (int i = 0; i < variables.length; i++) {
			String var = variables[i];
			jep.addVariable(var, startValues[i]);
		}
		
		xNode = jep.parse(xExpr);
		yNode = jep.parse(yExpr);
		zNode = jep.parse(zExpr);
	}
	
	public ParametricPlotter(String xExpr, String yExpr, String zExpr, String[] variables, float[] startValues) throws ParseException {
		this(xExpr, yExpr, zExpr, variables, startValues, 0.1f);
	}
	
	public void setVariable(String var, float value) {
		jep.addVariable(var, value);
	}
	
	public float getXValue() {
		return evalNode(xNode);
	}
	
	public float getYValue() {
		return evalNode(yNode);
	}
	
	public float getZValue() {
		return evalNode(zNode);
	}
	
	private float evalNode(Node node) {
		try {
			double value = (double) jep.evaluate(node);
			return (float) value;
		} catch (ParseException e) {
			// Does not happen
			return Float.NaN;
		}
	}
	
	public abstract Shape3D getPlot();

}
