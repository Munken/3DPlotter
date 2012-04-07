package munk.graph.rotaters;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

public class ViewZoomer extends KeyAdapter{
	
	private final TransformGroup target;

	public ViewZoomer(TransformGroup viewTransform) {
		if (viewTransform == null) throw new IllegalStateException();
		target = viewTransform;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		Transform3D trans = new Transform3D();
		target.getTransform(trans);
		
		if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
			Transform3D out = new Transform3D();
			out.setTranslation(new Vector3f(0,0, -0.1f));
			trans.mul(out);
			target.setTransform(trans);
		} else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
			Transform3D out = new Transform3D();
			out.setTranslation(new Vector3f(0,0, 0.1f));
			trans.mul(out);
			target.setTransform(trans);
		}
	}

}
