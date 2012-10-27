package munk.graph.appearance;

import javax.swing.Icon;
import javax.vecmath.Color3f;

import munk.graph.gui.ColorIcon;

public class Colors {
	
	public static final Color3f	RED			= new Color3f(1, 0, 0);
	public static final Color3f	YELLOW		= new Color3f(1, 1, 0);
	public static final Color3f	ORANGE		= new Color3f(0.933f, 0.5f, 0);
	public static final Color3f	SKYBLUE		= new Color3f(0, .0749f, 1);
	public static final Color3f	INDIGO		= new Color3f(0.294f, 0, 0.51f);
	public static final Color3f	GREEN		= new Color3f(0, 1, 0);
	public static final Color3f	CYAN		= new Color3f(0, 1, 1);
	public static final Color3f	BLUE		= new Color3f(0, 0, 1);
	public static final Color3f	MAGENTA		= new Color3f(1, 0, 1);
	public static final Color3f	PINK		= new Color3f(1, .0784f, .574f);
	public static final Color3f	TURQUISE	= new Color3f(0, 0.96f, 1);
	public static final Color3f	TEAL		= new Color3f(0.2196f, 0.557f, 0.557f);
	public static final Color3f	WHITE		= new Color3f(1, 1, 1);
	

	/*
	 * Render an array of icons of all available colors.
	 */
	public static Icon[] getAllColors(){
		return new Icon[]{new ColorIcon(Colors.RED),
				new ColorIcon(Colors.YELLOW),
				new ColorIcon(Colors.ORANGE),
				new ColorIcon(Colors.SKYBLUE),
				new ColorIcon(Colors.INDIGO),
				new ColorIcon(Colors.GREEN),
				new ColorIcon(Colors.CYAN),
				new ColorIcon(Colors.BLUE),
				new ColorIcon(Colors.MAGENTA),
				new ColorIcon(Colors.PINK),
				new ColorIcon(Colors.TURQUISE)};
	}
	
	/*
	 * Get the color index.
	 */
	public static int getIndex(Color3f color){
		Icon[] allColors = getAllColors();
		for(int i = 0; i < allColors.length ; i++){
			if(((ColorIcon) allColors[i]).getColor().equals(color)) return i;
		}
		return -1;
	}
}
