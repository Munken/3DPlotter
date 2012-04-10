package munk.graph.rotaters;

import java.awt.event.*;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

public class WheelZoomer implements MouseWheelListener {

	private final TransformGroup target;

	public WheelZoomer(TransformGroup viewTransform) {
		if (viewTransform == null) throw new IllegalStateException();
		target = viewTransform;
	}
	

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
		Transform3D trans = new Transform3D();
		target.getTransform(trans);
		
		Transform3D out = new Transform3D();
		out.setTranslation(new Vector3f(0,0, e.getUnitsToScroll() * 0.1f));
		trans.mul(out);
		target.setTransform(trans);
		
	}

}
