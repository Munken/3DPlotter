package munk.graph.rotaters;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;

public class KeyRotate extends KeyAdapter{
	
	private TransformGroup target;
	
	private Transform3D currXform = new Transform3D();
	private Transform3D transformX = new Transform3D();
	private Transform3D transformY = new Transform3D();
	
	private Set<Integer> pressed = new HashSet<Integer>();
	
	public KeyRotate(TransformGroup group) {
		if (group == null) throw new IllegalStateException();
		target = group;		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (isArrowKey(e)) pressed.add(e.getKeyCode());
		if (pressed.size() < 1) return;
			
    	double xAngle = 0;
    	double yAngle = 0;
    	
    	for (int key : pressed) {
    		if (key == KeyEvent.VK_LEFT) yAngle -= 0.1;
    		else if (key == KeyEvent.VK_RIGHT) yAngle += 0.1;
    		else if (key == KeyEvent.VK_UP) xAngle -= 0.1;
    		else if (key == KeyEvent.VK_DOWN) xAngle += 0.1;
    	}
		
		

		transformX.rotX(xAngle);
		transformY.rotY(yAngle);
		
		Matrix4d mat = new Matrix4d();
		
		// Remember
		target.getTransform(currXform);
		currXform.get(mat);
		
		currXform.setTranslation(new Vector3d(0.0, 0.0, 0.0));
		
		currXform.mul(transformX, currXform);
        currXform.mul(transformY, currXform);
		
        // Set old translation back
        Vector3d translation = new Vector3d(mat.m03,
                mat.m13, mat.m23);
        currXform.setTranslation(translation);
        
        target.setTransform(currXform);
		
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		pressed.remove(e.getKeyCode());
	}
	
	private boolean isArrowKey(KeyEvent e) {
		return (e.getKeyCode() >= KeyEvent.VK_LEFT) && (e.getKeyCode() <= KeyEvent.VK_DOWN);
	}

}
