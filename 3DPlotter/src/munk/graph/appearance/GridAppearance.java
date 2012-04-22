package munk.graph.appearance;

import javax.media.j3d.*;
import javax.vecmath.Color3f;

public class GridAppearance extends Appearance{
	
	public GridAppearance() {
	    setPolygonAttributes(
	    		new PolygonAttributes(PolygonAttributes.POLYGON_LINE,
	    				PolygonAttributes.CULL_NONE,0.0f));
	}
	
	public GridAppearance(Color3f color) {
		this();

		setColoringAttributes (
				new ColoringAttributes (
						color,ColoringAttributes.FASTEST));
		Material mat = new Material();
		mat.setAmbientColor(color);
		mat.setDiffuseColor(new Color3f(0.7f, 0.7f, 0.7f));
		mat.setSpecularColor(new Color3f(0.2f, 0.2f, 0.2f));
		mat.setLightingEnable(true);
		setMaterial(mat);
	}
	

}
