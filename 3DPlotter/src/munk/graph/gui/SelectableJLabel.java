package munk.graph.gui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextField;

@SuppressWarnings("serial")
public class SelectableJLabel extends JTextField {
	
	Boolean selected;
	private String text;
	
	public SelectableJLabel(String input) {
		super(input);
		text = input;
		selected = false;
		this.setEditable(false);
		updateColor();
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(!text.equals("")){
				selected=!selected;
				updateColor();
				}
			}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {	
			}
			public void mousePressed(MouseEvent e) {	
			}
			public void mouseReleased(MouseEvent e) {
			}
		});
	}
	
	public Boolean isSelected(){
		return selected;
	}
	
	private void updateColor(){
		if(isSelected()) this.setBackground(Color.BLUE);
		else this.setBackground(Color.WHITE);
	}
}

