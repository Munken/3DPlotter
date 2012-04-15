package munk.graph.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class ToggleButton extends JButton {

	private ImageIcon selectedIcon;
	private ImageIcon notSelectedIcon;
	private Boolean isSelected;
	
	public ToggleButton(ImageIcon selected, ImageIcon notSelected){
		selectedIcon = selected;
		notSelectedIcon = notSelected;
		isSelected = false;
		
		// Handle change of graphics
		this.setIcon(selectedIcon);
		this.setFocusable(false);
		this.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ToggleButton thisButton = (ToggleButton) arg0.getSource();
				if(!isSelected){
					thisButton.setIcon(notSelectedIcon);
				}
				else{
					thisButton.setIcon(selectedIcon);
				}
				isSelected = !isSelected;
			}
		});
	}
	
	public boolean isSelected(){
		return(isSelected);
	}
}
