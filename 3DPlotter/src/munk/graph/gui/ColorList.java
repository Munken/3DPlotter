package munk.graph.gui;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Color3f;

import munk.graph.IO.XMLReader;
import munk.graph.IO.XMLWriter;

@SuppressWarnings("serial")
public class ColorList extends ArrayList<Color3f> {
	
//	List<Function> functionList = new ArrayList<Function>();
	
	private static final String	COLOR_PATH	= "Files/colors.xml";

	/*
	 * Try to load colors from file, otherwise load default colors.
	 */
	public ColorList(){
		// Beware the file must be read before adding to the list. 
		// Else overwrite will occur
		XMLReader reader = new XMLReader("Files/colors.xml");
		add(new Color3f(1, 0, 0)); 			//RED
		
		List<Color3f> list = reader.processColors();
		for (Color3f c : list)
			add(c);
	}

	/*
	 * Create a color icon list.
	 */
	public ColorIcon[] getIconList(){
		ColorIcon[] iconList = new ColorIcon[this.size()];
		int i = 0;
		
		for(Color3f c : this){
			iconList[i] = new ColorIcon(c);
			i++;
		}
		return iconList;
	}

//	/*
//	 * Return the next available color. If none are, return the one least in use.
//	 */
//	
//	public Color3f getNextAvailableColor() {
//		int[] frequence = new int[this.size()];
//		ArrayList<Integer> indexList = new ArrayList<Integer>();
//		for(int i = 0; i < this.size() ; i++){
//			frequence[i]=0;
//			for(Function f : functionList){
//				if(f.getColor().equals(this.get(i))) frequence[i]++;
//			}
//			indexList.add(frequence[i]);
//		}
//		Arrays.sort(frequence);
//		return this.get(indexList.indexOf(frequence[0]));
//	}
	
	/*
	 * Save the color list, when a new color has been added.
	 */
	@Override
	public boolean add(Color3f color){
		
		if (!contains(color)) {
			boolean returnBoolean = super.add(color);
			outputColors();
			return returnBoolean; 
		}
		return false;
	}

	private void outputColors() {
		XMLWriter writer = new XMLWriter();
		for (Color3f c : this) {
			writer.addColor(c);
		}
		writer.output(COLOR_PATH);
	}

	/*
	 * Save the color list, when a new color has been removed.
	 */
	public Color3f remove(int i){
		Color3f returnColor = null;
		
		if(this.size() > 1){
			returnColor = super.remove(i);
			outputColors();
		}
		return returnColor;
	}
}
