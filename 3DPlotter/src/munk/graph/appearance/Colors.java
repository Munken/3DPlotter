package munk.graph.appearance;

import javax.swing.Icon;
import javax.vecmath.Color3f;

import munk.graph.gui.ColorIcon;

public class Colors {
	
	public static final Color3f RED = new Color3f(1, 0, 0);
	public static final Color3f YELLOW	 = new Color3f(1, 1, 0);
	public static final Color3f ORANGE	 = new Color3f(1, 0.6f, 0);
	public static final Color3f SKYBLUE = new Color3f(0, .0749f, 1);
	public static final Color3f INDIGO = new Color3f(0.294f, 0, 0.51f);
	public static final Color3f GREEN = new Color3f(0, 1, 0);
	public static final Color3f CYAN = new Color3f(0, 1, 1);
	public static final Color3f BLUE = new Color3f(0, 0, 1);
	public static final Color3f MAGENTA	 = new Color3f(1, 0, 1);
	public static final Color3f PINK = new Color3f(1, .0784f, .574f);
	public static final Color3f TURQUISE = new Color3f(0, 0.96f, 1);

	/*
	 * Render an array of icons of all available colors.
	 */
	public static Icon[] getColorIcons(){
		Color3f[] colorList = getAllColors();
		ColorIcon[] iconList = new ColorIcon[colorList.length];
		for (int i = 0; i < colorList.length ; i++) {
			iconList[i] = new ColorIcon(colorList[i]);
		}
		return iconList;
	}
	
	public static int getNumberOfColors() {
		return getAllColors().length;
	}
	
	public static Color3f getColorOfIndex(int i) {
		Color3f[] allColors = getAllColors();
		return allColors[i];
	}
	
	public static Color3f[] getAllColors() {
		return new Color3f[]{
				Colors.RED,
				Colors.YELLOW,
				Colors.ORANGE,
				Colors.SKYBLUE,
				Colors.INDIGO,
				Colors.GREEN,
				Colors.CYAN,
				Colors.BLUE,
				Colors.MAGENTA,
				Colors.PINK,
				Colors.TURQUISE};
	}

	public static int getIndex(Color3f color){
		Color3f[] allColors = getAllColors();
		for(int i = 0; i < allColors.length ; i++){
			if(allColors[i].equals(color)) return i;
		}
		return -1;
	}
}
