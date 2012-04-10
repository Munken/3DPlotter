package munk.graph.plot;

import java.awt.Font;

import javax.media.j3d.*;
import javax.vecmath.*;

import munk.graph.appearance.ColorAppearance;
import munk.graph.appearance.Colors;

import com.sun.j3d.utils.geometry.Cone;

public class Axes {

	private static final Color3f	X_COLOR	= Colors.MAGENTA;
	private static final Color3f	Z_COLOR	= Colors.CYAN;
	private static final Color3f	Y_COLOR	= Colors.RED;
	private static final float	HEIGHT_FACTOR	= 0.05f;
	private static final float	RADIUS_FACTOR	= 0.02f;

	public Group getSquareAxis(float min, float max) {
	    return drawAxis(min, max, min, max, min, max);
	}
	
	public Group getBoundingAxes(Group group) {
		BoundingSphere bounds = (BoundingSphere) group.getBounds();
		float radius = (float) bounds.getRadius();
		
		return drawAxis(-radius, radius, -radius, radius, -radius, radius);
	}
	
	public Group drawAxis(float xMin, float xMax, float yMin, float yMax, float zMin, float zMax) {
		
		Point3f x1 = new Point3f(xMin,0,0);
		Point3f x2 = new Point3f(xMax,0,0);
		Point3f y1 = new Point3f(0,yMin,0);
		Point3f y2 = new Point3f(0,yMax,0);
		Point3f z1 = new Point3f(0,0,zMin);
		Point3f z2 = new Point3f(0,0,zMax);
		
		LineArray l = new LineArray(6, LineArray.COORDINATES);
		l.setCoordinate(0, x1);
		l.setCoordinate(1, x2);
		l.setCoordinate(2, y1);
		l.setCoordinate(3, y2);		
		l.setCoordinate(4, z1);
		l.setCoordinate(5, z2);
			
		Group result = new Group();
		result.addChild(new Shape3D(l));
		
		float radius = (float) ((BoundingSphere) result.getBounds()).getRadius();
		result.addChild(yArrow(0, yMax, 0, radius));
		result.addChild(xArrow(xMax, 0, 0, radius));
		result.addChild(zArrow(0, 0, zMax, radius));
		result.addChild(addText(xMax, yMax, zMax, radius));
		return result;
	}
	
	private TransformGroup xArrow(float x, float y, float z, float radius) {
		TransformGroup result = new TransformGroup();
		
		Transform3D trans = new Transform3D();
		trans.setTranslation(new Vector3f(x, y, z));
		
		Transform3D rot = new Transform3D();
		rot.rotZ(-Math.PI/2);
		trans.mul(rot);
		
		result.setTransform(trans);
		
		Cone cone = addArrow(radius);
		result.addChild(cone);
		cone.setAppearance(new ColorAppearance(X_COLOR));
		
		return result;
	}
	
	private TransformGroup zArrow(float x, float y, float z, float radius) {
		TransformGroup result = new TransformGroup();
		
		Transform3D trans = new Transform3D();
		trans.setTranslation(new Vector3f(x, y, z));
		
		Transform3D rot = new Transform3D();
		rot.rotX(Math.PI/2);
		trans.mul(rot);
		
		result.setTransform(trans);
		
		Cone cone = addArrow(radius);
		result.addChild(cone);
		cone.setAppearance(new ColorAppearance(Z_COLOR));
		
		return result;
	}
	
	private TransformGroup yArrow(float x, float y, float z, float radius) {
		Transform3D trans = new Transform3D();
		trans.setTranslation(new Vector3f(x, y, z));
		TransformGroup group = new TransformGroup();
		group.setTransform(trans);

		Cone cone = addArrow(radius);
		group.addChild(cone);
		cone.setAppearance(new ColorAppearance(Y_COLOR));
		
		return group;
	}
	
	private Cone addArrow(float radius) {
		return new Cone(RADIUS_FACTOR*radius, HEIGHT_FACTOR*radius);

	}
	
	private TransformGroup addText(float xMax, float yMax, float zMax, float radius) {
		Font3D font = new Font3D(new Font("Test", Font.PLAIN, 1),  new FontExtrusion()); 
		Text3D xLabel = new Text3D(font, "X");
		Text3D yLabel = new Text3D(font, "Y");
		Text3D zLabel = new Text3D(font, "Z");
		
		Shape3D xShape = new Shape3D(xLabel);
		Shape3D yShape = new Shape3D(yLabel);
		Shape3D zShape = new Shape3D(zLabel);
		
		Point3d upper = new Point3d();
		((BoundingBox) xShape.getBounds()).getUpper(upper);
		float textHeight = (float) upper.y;
		float textWidth = (float) upper.x;
		float textDepth = (float) upper.z;
		
		float coneFactor = 0.6f*HEIGHT_FACTOR*radius;
		xLabel.setPosition(new Point3f(xMax + coneFactor, -textHeight/2, 0));
		yLabel.setPosition(new Point3f(-textWidth/2, yMax + coneFactor, -textDepth/2));
		
		
		xShape.setAppearance(new ColorAppearance(X_COLOR));
		yShape.setAppearance(new ColorAppearance(Y_COLOR));
		zShape.setAppearance(new ColorAppearance(Z_COLOR));
		
		TransformGroup zTG = new TransformGroup();
		Transform3D zRot = new Transform3D();
		zRot.rotY(-Math.PI/2);
		zTG.addChild(zShape);
		Transform3D zTrans = new Transform3D();
		zTrans.setTranslation(new Vector3f(0, - textHeight/2, zMax + coneFactor));
		zTrans.mul(zRot);
		zTG.setTransform(zTrans);
		
		TransformGroup tg = new TransformGroup();
		
		tg.addChild(xShape);
		tg.addChild(yShape);
		tg.addChild(zTG);
		
		
		return tg;
	}
}
