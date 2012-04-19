package munk.graph.plot;

import static munk.graph.plot.PlotUtil.initAxisArray;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.media.j3d.*;
import javax.vecmath.Point3f;



import com.graphbuilder.math.*;

public class XYZPlotter {
	
	private static Pattern PATTERN = Pattern.compile(" *([xyz]) *=([^=]+)$|([^=]+)= *([xyz]) *");
	private static final Transform3D ROT_Y;
	private static final Transform3D ROT_X;
	private Transform3D rotation;
	
	private float stepsize = 0.05f;

	private float	xMin;
	private float	xMax;
	private float	yMin;
	private float	yMax;

	private VarMap vm;
	private FuncMap fm;
	private Expression expression;
	
	private String var1 = "x";
	private String var2 = "y";
	private int	factorV1 = 1;
	private int	factorV2 = 1;
	
	private Shape3D shape;
	private TransformGroup plot;
	
	public XYZPlotter(String expr, float xMin, float xMax, float yMin, float yMax) 
							throws ExpressionParseException, UndefinedVariableException{
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		
		expr = preParse(expr);
		expression = ExpressionTree.parse(expr);
		
		vm = new VarMap();
		vm.setValue(var1, xMin);
		vm.setValue(var2, yMin);
		
		fm = new FuncMap();
		fm.loadDefaultFunctions();

		expression.ensureVariablesDefined(vm);
	}
	
	public XYZPlotter(String expr, float xMin, float xMax, float yMin, float yMax, float stepSize) 
							throws ExpressionParseException, UndefinedVariableException{
		this(expr, xMin, xMax, yMin, yMax);
		stepsize = stepSize;
	}
	
	static {
		ROT_X = new Transform3D();
		ROT_X.rotY(Math.PI/2);
		
		ROT_Y = new Transform3D();
		ROT_Y.rotX(-Math.PI/2);
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

	private TransformGroup plot() {
		int xSize = (int) ((xMax - xMin) / stepsize) + 1;
		int ySize = (int) ((yMax - yMin) / stepsize) + 1;
		
		float[] xValues = initAxisArray(xMin, xSize, stepsize);
		float[] yValues = initAxisArray(yMin, ySize, stepsize);
		
		Point3f[][] points = new Point3f[ySize][xSize];
		
		for (int y = 0; y < ySize; y++) {
			vm.setValue(var2, factorV2 * yValues[y]);
			for (int x = 0; x < xSize; x++) {
				vm.setValue(var1, factorV1 * xValues[x]);
				
				float value = (float) expression.eval(vm, fm);
				points[y][x] = new Point3f(xValues[x], yValues[y], value);
			}
		}

		GeometryArray quad = PlotUtil.buildQuadArray(points);
		
		shape = new Shape3D(quad);

		TransformGroup result = new TransformGroup();
		result.addChild(shape);
		result.setTransform(rotation);
		return result;
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
