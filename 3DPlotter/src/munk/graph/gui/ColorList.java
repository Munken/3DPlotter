package munk.graph.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.vecmath.Color3f;

import munk.graph.IO.ObjectReader;
import munk.graph.IO.ObjectWriter;
import munk.graph.function.Function;
import munk.graph.function.FunctionList;

@SuppressWarnings("serial")
public class ColorList extends ArrayList<Color3f> {
	
	/*
	 * Try to load colors from file, otherwise load default colors.
	 */
	public ColorList(){
		try{
			this.addAll((ColorList) ObjectReader.ObjectFromFile(new File("Files/config.color")));
		}
		catch(Exception e){
			System.out.println("Color config file not found. Loading default colors.");
			add(new Color3f(1, 0, 0)); 			//RED
			add(new Color3f(0, .0749f, 1)); 	//SKYBLUE
			add(new Color3f(0, 1, 0)); 			//GREEN
		}
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

	/*
	 * Return the next available color. If none are, return the one least in use.
	 */
	public Color3f getNextAvailableColor(FunctionList<Function> functionList){
		int[] frequence = new int[this.size()];
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		for(int i = 0; i < this.size() ; i++){
			frequence[i]=0;
			for(Function f : functionList){
				if(f.getColor().equals(this.get(i))) frequence[i]++;
			}
			indexList.add(frequence[i]);
		}
		Arrays.sort(frequence);
		return this.get(indexList.indexOf(frequence[0]));
	}
	
	/*
	 * Save the color list, when a new color has been added.
	 */
	public boolean add(Color3f color){
		Boolean returnBoolean = super.add(color);
		ObjectWriter.ObjectToFile(new File("Files/config.color"), this);
		return returnBoolean;
	}
}
