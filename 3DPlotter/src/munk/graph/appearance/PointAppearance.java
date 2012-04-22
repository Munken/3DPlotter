package munk.graph.appearance;

import javax.media.j3d.*;
import javax.vecmath.Color3f;

public class PointAppearance extends Appearance{
	
	public PointAppearance() {
	    setPolygonAttributes(
	    		new PolygonAttributes(PolygonAttributes.POLYGON_POINT,
	    				PolygonAttributes.CULL_NONE,0.0f));
	    
	    setPointAttributes(new PointAttributes(2.5f, false));
	}
	
	public PointAppearance(Color3f color) {
		this();

		setColoringAttributes (
				new ColoringAttributes (
						color,ColoringAttributes.NICEST));
		Material mat = new Material();
		mat.setAmbientColor(color);
		mat.setDiffuseColor(new Color3f(0.7f, 0.7f, 0.7f));
		mat.setSpecularColor(new Color3f(0.2f, 0.2f, 0.2f));
		mat.setLightingEnable(true);
		setMaterial(mat);
	}

}
