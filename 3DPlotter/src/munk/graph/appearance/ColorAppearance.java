package munk.graph.appearance;

import javax.media.j3d.*;
import javax.vecmath.Color3f;

public class ColorAppearance extends Appearance{

	public ColorAppearance(Color3f color) {
		
		setColoringAttributes (
				new ColoringAttributes (
						color,ColoringAttributes.FASTEST));
		
	    setPolygonAttributes(
	    		new PolygonAttributes(PolygonAttributes.POLYGON_FILL,
	    				PolygonAttributes.CULL_NONE,0.0f));
		
		Material mat = new Material();
		mat.setAmbientColor(color);
		mat.setDiffuseColor(new Color3f(0.7f, 0.7f, 0.7f));
		mat.setSpecularColor(new Color3f(0.2f, 0.2f, 0.2f));
		mat.setLightingEnable(true);
		setMaterial(mat);
	}
	
}
