package munk.graph.gui.panel;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.vecmath.Color3f;

import munk.graph.gui.ColorList;

@SuppressWarnings("serial")
public class ColorOptionPanel extends JPanel {

	JColorChooser colorChooser;
	ArrayList<ActionListener> listeners;
	ColorList colors;
	JList iconList;
	
	private static int ADD = 0;
	private static int REMOVE = 1;
	private static int CLOSE = 2;
	
	public ColorOptionPanel(ColorList colorList){
		
		this.setSize(new Dimension(800,300));
		this.colors = colorList;
		listeners = new ArrayList<ActionListener>();
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{5, 0, 0, 0, 0, 0, 0, 0, 10, 100, 0, 0};
		gridBagLayout.rowHeights = new int[]{5, 5, 5, 0, 0, 0, 5, 0, 0, 5, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		colorChooser = new JColorChooser();
		GridBagConstraints gbc_colorChooser = new GridBagConstraints();
		gbc_colorChooser.fill = GridBagConstraints.BOTH;
		gbc_colorChooser.gridheight = 8;
		gbc_colorChooser.gridwidth = 7;
		gbc_colorChooser.insets = new Insets(0, 0, 5, 5);
		gbc_colorChooser.gridx = 1;
		gbc_colorChooser.gridy = 1;
		
		this.add(colorChooser, gbc_colorChooser);
		
		JLabel lblCurrent = new JLabel("Color List");
		GridBagConstraints gbc_lblCurrent = new GridBagConstraints();
		gbc_lblCurrent.insets = new Insets(0, 0, 5, 5);
		gbc_lblCurrent.gridx = 9;
		gbc_lblCurrent.gridy = 2;
		add(lblCurrent, gbc_lblCurrent);
		
		iconList = new JList(colorList.getIconList());
		iconList.setFixedCellWidth(30);
		
		JScrollPane scrollPane = new JScrollPane(iconList);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 9;
		gbc_scrollPane.gridy = 3;
		add(scrollPane, gbc_scrollPane);
		
		JButton btnADD = new JButton("Add Color");
		GridBagConstraints gbc_btnOk = new GridBagConstraints();
		gbc_btnOk.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnOk.insets = new Insets(0, 0, 5, 5);
		gbc_btnOk.gridx = 9;
		gbc_btnOk.gridy = 5;
		add(btnADD, gbc_btnOk);
		
		JButton btnRemoveColor = new JButton("Remove Color");
		GridBagConstraints gbc_btnRemoveColor = new GridBagConstraints();
		gbc_btnRemoveColor.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRemoveColor.insets = new Insets(0, 0, 5, 5);
		gbc_btnRemoveColor.gridx = 9;
		gbc_btnRemoveColor.gridy = 6;
		add(btnRemoveColor, gbc_btnRemoveColor);
		
		JButton btnClose = new JButton("Close");
		GridBagConstraints gbc_btnClose = new GridBagConstraints();
		gbc_btnClose.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnClose.insets = new Insets(0, 0, 5, 5);
		gbc_btnClose.gridx = 9;
		gbc_btnClose.gridy = 7;
		add(btnClose, gbc_btnClose);
		btnClose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				signalAll(CLOSE);
			}
		});
		
		btnADD.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				signalAll(ADD);
				iconList.setModel(new JList<>(colors.getIconList()).getModel());
			}
		});
		
		btnRemoveColor.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				signalAll(REMOVE);
				iconList.setModel(new JList<>(colors.getIconList()).getModel());
			}
		});
		
	}
	
	public void addActionListener(ActionListener a){
		listeners.add(a);
	}
	
	public void signalAll(int command){
		ActionEvent event = null;
		if(command == ADD){
			event = new ActionEvent(new Color3f(colorChooser.getColor()), ADD, "");
		}
		if(command == REMOVE){
			event = new ActionEvent(iconList.getSelectedIndex(), REMOVE, "");
		}
		if(command == CLOSE){
			event = new ActionEvent("", CLOSE, "");
		}
		for(ActionListener a : listeners){
			a.actionPerformed(event);
		}
	}
}
