package munk.graph.gui.panel;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import munk.graph.gui.GuiUtil;

import com.graphbuilder.math.ExpressionParseException;

public class StdGridOptionPanel extends AbstractGridOptionPanel implements GridOptionPanel{

	private static final long serialVersionUID = 1L;
	private JTextField xMin;
	private JTextField xMax;
	private JTextField yMin;
	private JTextField yMax;
	private JSlider ySlider;
	private JSlider xSlider;
	private JTextField zMin;
	private JTextField zMax;
	private JLabel zLabel;
	private JSlider zSlider;

	public StdGridOptionPanel(String[] bounds) {

		this.setPreferredSize(new Dimension(300,100));

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 40, 50, 40, 0, 40, 50, 40, 0, 0};
		gridBagLayout.rowHeights = new int[]{10, 0, 0, 10, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);

		xMin = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 1;
		add(xMin, gbc_textField);

		JLabel xLabel = new JLabel("< x <");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 2;
		gbc_lblNewLabel.gridy = 1;
		add(xLabel, gbc_lblNewLabel);

		xMax = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 3;
		gbc_textField_1.gridy = 1;
		add(xMax, gbc_textField_1);

		xSlider = new JSlider();
		GridBagConstraints gbc_slider_1 = new GridBagConstraints();
		gbc_slider_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_slider_1.gridwidth = 3;
		gbc_slider_1.insets = new Insets(0, 0, 5, 5);
		gbc_slider_1.gridx = 5;
		gbc_slider_1.gridy = 1;
		add(xSlider, gbc_slider_1);

		yMin = new JTextField();
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 5, 5);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 1;
		gbc_textField_2.gridy = 2;
		add(yMin, gbc_textField_2);

		JLabel yLabel = new JLabel("< y <");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 2;
		gbc_label.gridy = 2;
		add(yLabel, gbc_label);

		yMax = new JTextField();
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.insets = new Insets(0, 0, 5, 5);
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.gridx = 3;
		gbc_textField_3.gridy = 2;
		add(yMax, gbc_textField_3);

		ySlider = new JSlider();
		GridBagConstraints gbc_slider = new GridBagConstraints();
		gbc_slider.fill = GridBagConstraints.HORIZONTAL;
		gbc_slider.gridwidth = 3;
		gbc_slider.insets = new Insets(0, 0, 5, 5);
		gbc_slider.gridx = 5;
		gbc_slider.gridy = 2;
		add(ySlider, gbc_slider);
		
		zMin = new JTextField();
		GridBagConstraints gbc_textField_4 = new GridBagConstraints();
		gbc_textField_4.insets = new Insets(0, 0, 5, 5);
		gbc_textField_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_4.gridx = 1;
		gbc_textField_4.gridy = 3;
		add(zMin, gbc_textField_4);
		
		zLabel = new JLabel("< z <");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 2;
		gbc_label_1.gridy = 3;
		add(zLabel, gbc_label_1);
		
		zMax = new JTextField();
		GridBagConstraints gbc_textField_5 = new GridBagConstraints();
		gbc_textField_5.insets = new Insets(0, 0, 5, 5);
		gbc_textField_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_5.gridx = 3;
		gbc_textField_5.gridy = 3;
		add(zMax, gbc_textField_5);
		
		zSlider = new JSlider();
		GridBagConstraints gbc_slider_2 = new GridBagConstraints();
		gbc_slider_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_slider_2.gridwidth = 3;
		gbc_slider_2.insets = new Insets(0, 0, 5, 5);
		gbc_slider_2.gridx = 5;
		gbc_slider_2.gridy = 3;
		add(zSlider, gbc_slider_2);
		
     	GuiUtil.setupUndoListener(xMin);
     	GuiUtil.setupUndoListener(xMax);
     	GuiUtil.setupUndoListener(yMin);
     	GuiUtil.setupUndoListener(yMax);
     	GuiUtil.setupUndoListener(zMin);
     	GuiUtil.setupUndoListener(zMax);
		setGridBounds(bounds);
		
		MouseListener sliderListener = new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
					try {
//						// For now adjust all sliders accordingly.
//						JSlider j = (JSlider) arg0.getSource();
//						xSlider.setValue(j.getValue());
//						ySlider.setValue(j.getValue());
//						zSlider.setValue(j.getValue());
						signallAll();
					} catch (ExpressionParseException e) {
						e.printStackTrace();
					}
			}
		};
		xSlider.addMouseListener(sliderListener);
		ySlider.addMouseListener(sliderListener);
		zSlider.addMouseListener(sliderListener);	
		
		KeyListener boundsListener = super.getBoundsListener();
     	xMin.addKeyListener(boundsListener);
     	xMax.addKeyListener(boundsListener);
     	yMin.addKeyListener(boundsListener);
     	yMax.addKeyListener(boundsListener);
     	zMin.addKeyListener(boundsListener);
     	zMax.addKeyListener(boundsListener);
     	
		FocusListener updateBoundsListener = super.getUpdateBoundsListener(this);
		xMin.addFocusListener(updateBoundsListener);
		xMax.addFocusListener(updateBoundsListener);
		yMin.addFocusListener(updateBoundsListener);
		yMax.addFocusListener(updateBoundsListener);
		zMin.addFocusListener(updateBoundsListener);
		zMax.addFocusListener(updateBoundsListener);
	}

	public String[] getGridBounds(){
		String[] bounds = new String[6];
		bounds[0] = xMin.getText();
		bounds[1] = xMax.getText();
		bounds[2] = yMin.getText();
		bounds[3] = yMax.getText();
		bounds[4] = zMin.getText();
		bounds[5] = zMax.getText();
		return bounds;
	}
	
	public float[] getGridStepSize() throws ExpressionParseException{
		float[] bounds = GuiUtil.evalStringArray(getGridBounds());
		float[] stepSize = new float[3];
		stepSize[0] = Math.abs(bounds[1]-bounds[0])/xSlider.getValue();
		stepSize[1] = Math.abs(bounds[3]-bounds[2])/ySlider.getValue();
		stepSize[2] = Math.abs(bounds[5]-bounds[4])/zSlider.getValue();
		return stepSize;
	}
	
	public void setGridBounds(String[] bounds){
		xMin.setText(bounds[0]);
		xMax.setText(bounds[1]);
		yMin.setText(bounds[2]);
		yMax.setText(bounds[3]);
		zMin.setText(bounds[4]);
		zMax.setText(bounds[5]);
	}
	
	public void setSliders(float[] stepSize) throws ExpressionParseException{
		float[] bounds = GuiUtil.evalStringArray(selectedFunction.getBoundsString());
		xSlider.setValue((int) (Math.abs(bounds[1]-bounds[0])/stepSize[0]));
		ySlider.setValue((int) (Math.abs(bounds[3]-bounds[2])/stepSize[1]));
		zSlider.setValue((int) (Math.abs(bounds[5]-bounds[4])/stepSize[2]));
	}
}
