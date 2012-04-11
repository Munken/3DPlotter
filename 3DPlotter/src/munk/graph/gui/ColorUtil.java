package munk.graph.gui;

import java.util.ArrayList;

import javax.vecmath.Color3f;

import munk.graph.function.Function;
import munk.graph.function.FunctionList;

public class ColorUtil {

	/*
	 * Load default colors.
	 */
	public static ArrayList<Color3f> getDefaultColors(){
		ArrayList<Color3f> defaultColors = new ArrayList<Color3f>();
		defaultColors.add(new Color3f(1, 0, 0)); 		//RED
		defaultColors.add(new Color3f(0, .0749f, 1)); 	//SKYBLUE
		defaultColors.add(new Color3f(0, 1, 0)); 		//GREEN
		//TODO More colors should be added.
		return defaultColors;
	}

	/*
	 * Create an icon list from the current color list.
	 */
	public static ColorIcon[] getIconList(ArrayList<Color3f> colorList){
		ColorIcon[] iconList = new ColorIcon[colorList.size()];
		int i = 0;
		for(Color3f c : colorList){
			iconList[i] = new ColorIcon(c);
			i++;
		}
		return iconList;
	}

	/*
	 * Return the next available color. If none are, just return the first.
	 */
	public static Color3f getNextAvailableColor(ArrayList<Color3f> colorList, FunctionList<Function> functionList){
		Boolean colorInUse = false;
		for(Color3f c : colorList){
			for(Function f : functionList){
				if(f.getColor().equals(c)) colorInUse = true;
			}
			if(!colorInUse) return c;
			colorInUse = false;
		}
		return colorList.get(1);
	}
}
