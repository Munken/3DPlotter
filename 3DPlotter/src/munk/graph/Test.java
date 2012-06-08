package munk.graph;

import java.awt.*;
import javax.swing.*;

class Test extends JFrame
{
	JSlider[] sliders = new JSlider[3];
	BoundedRangeModel[] models = new BoundedRangeModel[sliders.length];
	JPanel colorPanel = new JPanel();
	SliderListener listener = new SliderListener();
	public Test()
	{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocation(200,100);
		colorPanel.setPreferredSize(new Dimension(400,200));
		colorPanel.setBackground(new Color(0,0,0));
		JPanel panel = new JPanel(new GridLayout(3,1));
		for(int x =0; x < sliders.length; x++)
		{
			sliders[x] = new JSlider(0,255,0);
			sliders[x].setMajorTickSpacing(20);
			sliders[x].setMinorTickSpacing(10);
			sliders[x].setPaintTicks(true);
			sliders[x].setPaintLabels(true);
			sliders[x].addChangeListener(listener);
			models[x] = sliders[x].getModel();
			panel.add(sliders[x]);
		}
		JPanel p = new JPanel();
		final JCheckBox cbx = new JCheckBox("Lock");
		p.add(cbx);
		getContentPane().add(panel,BorderLayout.NORTH);
		getContentPane().add(colorPanel,BorderLayout.CENTER);
		getContentPane().add(p,BorderLayout.SOUTH);
		pack();
		cbx.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent ae){
				if(cbx.isSelected())
				{
					sliders[1].setModel(sliders[0].getModel());
					sliders[2].setModel(sliders[0].getModel());
					colorPanel.setBackground(new
							Color(sliders[0].getValue(),sliders[1].getValue(),sliders[2].getValue()));
				}
				else
				{
					sliders[1].setModel(models[1]);
					sliders[2].setModel(models[2]);
					colorPanel.setBackground(new
							Color(sliders[0].getValue(),sliders[1].getValue(),sliders[2].getValue()));
				}}});
	}
	class SliderListener implements javax.swing.event.ChangeListener
	{
		public void stateChanged(javax.swing.event.ChangeEvent ce)
		{
			colorPanel.setBackground(new
					Color(sliders[0].getValue(),sliders[1].getValue(),sliders[2].getValue()));
		}
	}
	public static void main(String[] args){new Test().setVisible(true);}
} 