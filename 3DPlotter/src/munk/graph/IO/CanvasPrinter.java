package munk.graph.IO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

import munk.graph.gui.Plotter3D;

public class CanvasPrinter {

	public static void writeCanvasCapture(File file, Plotter3D plotter, Point corner, int width, int height) throws AWTException, IOException{
		BufferedImage b = null;
		Rectangle bounds = new Rectangle((int) corner.getX(), (int) corner.getY(), width, height);		
		Robot robot = new Robot(plotter.getGraphicsConfiguration().getDevice());
		b = robot.createScreenCapture(bounds);
		ImageIO.write(b, "png", file);
	}
}