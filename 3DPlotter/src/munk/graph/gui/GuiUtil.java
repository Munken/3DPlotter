package munk.graph.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;
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

}
