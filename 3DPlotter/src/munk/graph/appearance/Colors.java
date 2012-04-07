package munk.graph.appearance;

import javax.vecmath.Color3f;

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
	
	// Disse skal staves på samme måde som i getAllColorNames
	public static String getColorName(Color3f color){
		if(color.equals(new Color3f(1, 0, 0))) return "Red"; 
		if(color.equals(new Color3f(1, 1, 0))) return "Yellow";
		if(color.equals(new Color3f(1, 0.6f, 0))) return "Orange";
		if(color.equals(new Color3f(0, .0749f, 1))) return "Skyblue";		
		return "ERROR";
	}
	
	public static Color3f getColor(String name){
		if(name.equals("Red")) return new Color3f(1, 0, 0);
		if(name.equals("Yellow")) return new Color3f(1, 1, 0);
		if(name.equals("Orange")) return new Color3f(1, 0.6f, 0);
		if(name.equals("Skyblue")) return new Color3f(0, .0749f, 1);
		return new Color3f(1,0,0);
	}
	
	public static String[] getAllColorNames(){
		return new String[]{"Red", 
			"Yellow", 
			"Orange", 
			"Skyblue", 
			"Indigo",
			"Green",
			"Cyan",
			"Blue",
			"Magenta",
			"Pink",
			"Turquise"};
	}
}
