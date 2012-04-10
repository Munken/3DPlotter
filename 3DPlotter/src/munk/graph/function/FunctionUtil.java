package munk.graph.function;

import javax.media.j3d.*;
import javax.vecmath.Color3f;

import munk.graph.appearance.ColorAppearance;

public class FunctionUtil {

	public static BranchGroup setApperancePackInBranchGroup(Color3f color, Shape3D shape, Node handle) {
		shape.setAppearance(new ColorAppearance(color));
		shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);

		BranchGroup bg = new BranchGroup();
		bg.setCapability(BranchGroup.ALLOW_DETACH);
		bg.addChild(shape);

		return bg;
	}

	public static String[] expressionArray(String expr) {
		String[] result = {expr};
		return result;
	}

	/*
	 * Create function: initially.
	 */
	public static Function getFunction(String[] expr,Color3f color, float[] bounds,float stepsize){
		// Determine function type and return the correct function.
		return null;
	}
}
