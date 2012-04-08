package munk.graph.plot;

import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Point3f;

import org.nfunk.jep.ParseException;

public class Parametric2D extends ParametricPlotter{
	
	private static final String[] STD_VAR_NAMES = {"t", "u"};
	
	private float	tMin;
	private float	tMax;
	private float   uMin;
	private float   uMax;
	
	private float stepsize;

	// The mother of all constructors !
	public Parametric2D(String xExpr, String yExpr, String zExpr, 
						float tMin, float tMax, float uMin, float uMax, 
						String[] varNames, float stepSize) throws ParseException {
		
		super(xExpr, yExpr, zExpr, varNames, new float[] {tMin, uMin});
		
		if (varNames.length > 2) 
			throw new IllegalStateException("There must only be two parameters!");
		
		
		this.tMin = tMin;
		this.tMax = tMax;
		this.uMin = uMin;
		this.uMax = uMax;
		stepsize = stepSize;
	}
	
	public Parametric2D(String xExpr, String yExpr, String zExpr, 
			float tMin, float tMax, float uMin, float uMax, float stepSize) throws ParseException {
		this(xExpr, yExpr, zExpr, tMin, tMax, uMin, uMax, STD_VAR_NAMES, stepSize);
	}
	
	public Parametric2D(String xExpr, String yExpr, String zExpr, 
			float tMin, float tMax, float uMin, float uMax) throws ParseException {
		this(xExpr, yExpr, zExpr, tMin, tMax, uMin, uMax, 0.1f);
	}

	@Override
	public Shape3D getPlot() {
		int tLength = (int) Math.ceil((tMax - tMin) / stepsize) + 1;
		int uLength = (int) Math.ceil((uMax - uMin) / stepsize) + 1;
		
		float[] tValues = PlotUtil.initAxisArray(tMin, tLength, stepsize);
		float[] uValues = PlotUtil.initAxisArray(uMin, uLength, stepsize);
		
		Point3f[][] points = new Point3f[tLength][uLength];
		
		for (int i = 0; i < tLength; i++) {
			setVariable("t", tValues[i]);
			for (int j = 0; j < uLength; j++) {
				setVariable("u", uValues[j]);
				
				points[i][j] = new Point3f(xValue(), yValue(), zValue());
			}
		}
		
		QuadArray quad = PlotUtil.buildQuadArray(points);
		
		return new Shape3D(quad);
	}
	


}
