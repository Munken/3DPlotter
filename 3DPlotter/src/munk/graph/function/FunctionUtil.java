package munk.graph.function;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;

import munk.graph.appearance.ColorAppearance;

public class FunctionUtil {
	
	public static BranchGroup setApperancePackInBranchGroup(Color3f color, Shape3D shape) {
		shape.setAppearance(new ColorAppearance(color));
		shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		
		BranchGroup bg = new BranchGroup();
		bg.setCapability(BranchGroup.ALLOW_DETACH);
		bg.addChild(shape);
		
		return bg;
	}

}
