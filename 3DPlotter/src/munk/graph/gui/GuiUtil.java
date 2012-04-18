package munk.graph.gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.undo.*;

public class GuiUtil {
	
	public static void setupUndoListener(JTextField textfield) {
		final UndoManager manager = new UndoManager();
     	Document document = textfield.getDocument();
     	document.addUndoableEditListener(manager);
     	
     	textfield.addKeyListener(new KeyAdapter() {
     		
     		@Override
     		public void keyPressed(KeyEvent e) {
     			if (e.getKeyCode() == KeyEvent.VK_Z && e.getModifiers() == KeyEvent.CTRL_MASK) {
     				try {
     					manager.undo();
     				} catch (CannotUndoException excep) {
     					// Ignore
     				}
     			} else if (e.getKeyCode() == KeyEvent.VK_Z && e.getModifiers() == KeyEvent.ALT_MASK) {
     				try {
     					manager.redo();
     				} catch (CannotRedoException excep) {
     					// Ignore
     				}
     			}
     		}
		});
	}
	
	/*
	 * Spawn simple export dialog.
	 */
	public static File spawnExportDialog(String filePath, JFrame frame){
		File outputFile = null;
		JFileChooser fc = new JFileChooser(new File(filePath));
		fc.showSaveDialog(frame);
		outputFile = fc.getSelectedFile();
		return outputFile;
	}
	
	/*
	 * Spawn simple import dialog.
	 */
	public static File spawnImportDialog(String filePath, JFrame frame){
		File inputFile = null;
		JFileChooser fc = new JFileChooser(new File(filePath));
		fc.showOpenDialog(frame);
		inputFile = fc.getSelectedFile();
		return inputFile;
	}

	/*
	 * Get screen shot of the plotter.
	 */
	public static BufferedImage getSSH(GraphicsDevice device, Point p, int w, int h){
		Rectangle bounds = new Rectangle((int) p.getX(), (int) p.getY(), w, h);		
		Robot robot = null;
		try {
			robot = new Robot(device);
		} catch (AWTException e) {
			e.printStackTrace();
		}
		return robot.createScreenCapture(bounds);
	}
}
