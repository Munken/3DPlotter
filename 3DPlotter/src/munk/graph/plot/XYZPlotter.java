package munk.graph.plot;

import static munk.graph.plot.PlotUtil.initAxisArray;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.media.j3d.*;
import javax.vecmath.Point3f;

import munk.emesp.*;
import munk.emesp.exceptions.*;

public class XYZPlotter extends AbstractPlotter {
	
	private static Pattern PATTERN = Pattern.compile(" *([xyz]) *=([^=]+)$|([^=]+)= *([xyz]) *");
	private static final Transform3D ROT_Y;
	private static final Transform3D ROT_X;
	private Transform3D rotation;
	
	private float	xStepsize;
	private float	yStepsize;
	
	private float	xMin;
	private float	xMax;
	private float	yMin;
	private float	yMax;

	private Expression expression;
	private VariableValues vm;
	
	private String var1 = "x";
	private String var2 = "y";
	private int	factorV1 = 1;
	private int	factorV2 = 1;
	
	private Shape3D shape;
	private TransformGroup plot;

	
	private XYZPlotter(String expr, float xMin, float xMax, float yMin, float yMax) 
							throws IllegalExpressionException {
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		
		expr = preParse(expr);
		expression = ExpressionParser.parse(expr, FunctionMap.getDefaultFunctionMap());
		
		vm = new VariableValues();
		vm.setValue(var1, xMin);
		vm.setValue(var2, yMin);

		expression.ensureVariablesDefined(vm);
	}
	
	public XYZPlotter(String expr, float xMin, float xMax, float yMin, float yMax, float xStepsize, float yStepsize) 
			throws IllegalExpressionException {
		this(expr, xMin, xMax, yMin, yMax);
		this.xStepsize = xStepsize;
		this.yStepsize = yStepsize;
	}
	
	public XYZPlotter(String expr, float xMin, float xMax, float yMin, float yMax, float[] stepsizes) 
			throws IllegalExpressionException {
		this(expr, xMin, xMax, yMin, yMax, stepsizes[0], stepsizes[1]);
	}
	
	public XYZPlotter(String expr, float[] bounds, float[] stepsizes) 
			throws IllegalExpressionException {
		this(expr, bounds[0], bounds[1], bounds[2], bounds[3], stepsizes);
		
		
		// x = f(y,z) plot
		if (rotation == ROT_X) {
			xMin = bounds[4];
			xMax = bounds[5];
			xStepsize = stepsizes[2];
			
		// y = f(x,z) plot	
		} else if (rotation == ROT_Y){
			yMin = bounds[4];
			yMax = bounds[5];
			yStepsize = stepsizes[2];
		}
	}
	
	static {
		ROT_X = new Transform3D();
		ROT_X.rotY(-Math.PI/2);
		
		ROT_Y = new Transform3D();
		ROT_Y.rotX(Math.PI/2);
	}
	

	public Shape3D getShape() {
		if (shape == null)
			plot();
		return shape;
	}
	
	public TransformGroup getPlot() {
		if (plot == null)
			plot = plot();
		
		return plot; 
	}

	protected TransformGroup plot() {
		int xSize = (int) ((xMax - xMin) / xStepsize) + 1;
		int ySize = (int) ((yMax - yMin) / yStepsize) + 1;
		
		float[] xValues = initAxisArray(xMin, xSize, xStepsize);
		float[] yValues = initAxisArray(yMin, ySize, yStepsize);
		
		Point3f[][] points = new Point3f[ySize][xSize];
		
		for (int y = 0; y < ySize; y++) {
			vm.setValue(var2, factorV2 * yValues[y]);
			for (int x = 0; x < xSize; x++) {
				vm.setValue(var1, factorV1 * xValues[x]);
				
				float value = (float) expression.eval(vm);
				points[y][x] = new Point3f(xValues[x], yValues[y], value);
			}
		}

		if (points.length > 1) {
			GeometryArray quad = PlotUtil.buildQuadArray(points);

			shape = new Shape3D(quad);

			TransformGroup result = new TransformGroup();
			result.addChild(shape);
			result.setTransform(rotation);
			setShape(shape);
			return result;
		} else 
			return null;
	}	
	
	private String preParse(String expr) {
		Matcher m = PATTERN.matcher(expr);
		if (m.matches()) {
			String lhs = (m.group(1) != null) ? m.group(1).trim() : m.group(4);
			String rhs = (m.group(2) != null) ? m.group(2).trim() : m.group(3);
			
			if (lhs.equals("x")) {
				rotation = ROT_X;
				factorV1 = -1;
				var1 = "z";
			}
			else if (lhs.equals("y")) {
				rotation = ROT_Y;
				var2 = "z";
				factorV2 = -1;
			}
			else {
				rotation = new Transform3D();
			}
			
			return rhs;
		} else {
			throw new IllegalStateException("Expression must be of the form <var> = <expression>");
		}
		
	}
	
}
