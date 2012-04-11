package munk.graph.gui;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.vecmath.Color3f;

@SuppressWarnings("serial")
public class ColorIcon extends ImageIcon {

	private BufferedImage bI;
	private Color3f color;
	
	/*
	 * Draw the color icon upon creation.
	 */
	public ColorIcon(Color3f color){
		this.color = color;
		bI = new BufferedImage(15, 15, BufferedImage.TYPE_INT_RGB);
		for(int i = 0; i < 15; i++){
			for(int j = 0; j < 15; j++)
				bI.setRGB(i, j, color.get().getRGB());
		}
		this.setImage(bI);
	}
	
	/*
	 * Return the color of the icon.
	 */
	public Color3f getColor(){
		return color;
	}
	
	/*
	 * Equal if they have the same color.
	 */
	public boolean equals(Object other){
		if(other == null) return false;
		if(this == other) return true;
		if(other.getClass().equals(this.getClass())){
			ColorIcon otherIcon = (ColorIcon) other;
			if(this.getColor().equals(otherIcon.getColor())) return true; 
		}
		return false;
	}
}
