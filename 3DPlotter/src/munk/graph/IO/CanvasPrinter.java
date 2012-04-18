package munk.graph.IO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

public class CanvasPrinter {

	public static void writeCanvasCapture(File file, BufferedImage SSH) throws IOException{
		ImageIO.write(SSH, "png", file);
	}
	
	public static BufferedImage getSSH(GraphicsDevice device, int x, int y, int w, int h){
		Rectangle bounds = new Rectangle(x, y, w, h);		
		Robot robot = null;
		try {
			robot = new Robot(device);
		} catch (AWTException e) {
			e.printStackTrace();
		}
		return robot.createScreenCapture(bounds);
	}
}