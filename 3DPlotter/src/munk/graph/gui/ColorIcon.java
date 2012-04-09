package munk.graph.gui;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.vecmath.Color3f;

@SuppressWarnings("serial")
public class ColorIcon extends ImageIcon {

	private BufferedImage bI;
	private Color3f color;
	
	public ColorIcon(Color3f color){
		this.color = color;
		bI = new BufferedImage(15, 15, BufferedImage.TYPE_INT_RGB);
		for(int i = 0; i < 15; i++){
			for(int j = 0; j < 15; j++)
				bI.setRGB(i, j, color.get().getRGB());
		}
		this.setImage(bI);
	}
	
	public Color3f getColor(){
		return color;
	}
}
