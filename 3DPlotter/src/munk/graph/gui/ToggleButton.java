package munk.graph.gui;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class ToggleButton extends JButton {

	private ImageIcon selectedIcon;
	private ImageIcon notSelectedIcon;
	private Boolean isSelected;
	private Boolean isIndeterminate;
	
	public ToggleButton(ImageIcon selected, ImageIcon notSelected){
		selectedIcon = selected;
		notSelectedIcon = notSelected;
		isSelected = false;
		isIndeterminate = false;
		
		// Handle change of graphics
		this.setIcon(selectedIcon);
		this.setFocusable(false);
		this.setRolloverEnabled(false);
		this.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!isIndeterminate){
				ToggleButton thisButton = (ToggleButton) arg0.getSource();
				if(!isSelected){
					thisButton.setIcon(notSelectedIcon);
				}
				else{
					thisButton.setIcon(selectedIcon);
				}
				isSelected = !isSelected;
				}
			}
		});
	}

	public void setIndeterminate(boolean b){
		// Set indeterminate icon.
		if(b){
			isIndeterminate = true;
			Toolkit kit = Toolkit.getDefaultToolkit();
			Image spinner = kit.getImage("Icons/4-1.gif");
			this.setIcon(new ImageIcon(spinner.getScaledInstance(15, 15, Image.SCALE_DEFAULT)));
		}
		// Set normal icon.
		else{
			isIndeterminate = false;
			this.setIcon(selectedIcon);
		}
	}
	
	public boolean isSelected(){
		return(isSelected);
	}
	
	public boolean isIndeterminate(){
		return isIndeterminate;
	}
}
