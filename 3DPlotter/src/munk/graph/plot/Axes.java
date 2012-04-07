package munk.graph.plot;

import javax.media.j3d.*;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import munk.graph.appearance.ColorAppearance;
import munk.graph.appearance.Colors;

import com.sun.j3d.utils.geometry.Cone;

public class Axes {

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
		cone.setAppearance(new ColorAppearance(Colors.MAGENTA));
		
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
		cone.setAppearance(new ColorAppearance(Colors.CYAN));
		
		return result;
	}
	
	private TransformGroup yArrow(float x, float y, float z, float radius) {
		Transform3D trans = new Transform3D();
		trans.setTranslation(new Vector3f(x, y, z));
		TransformGroup group = new TransformGroup();
		group.setTransform(trans);

		Cone cone = addArrow(radius);
		group.addChild(cone);
		cone.setAppearance(new ColorAppearance(Colors.RED));
		
		return group;
	}
	
	private Cone addArrow(float radius) {
		return new Cone(0.02f*radius, 0.05f*radius);

	}
}
