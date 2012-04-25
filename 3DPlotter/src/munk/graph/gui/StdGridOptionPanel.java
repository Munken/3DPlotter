package munk.graph.gui;

import java.awt.*;

import javax.swing.*;

import com.graphbuilder.math.ExpressionParseException;

public class StdGridOptionPanel extends JPanel{

	/**
	 * 
	 */
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

	public StdGridOptionPanel(float[] bounds) {

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
	}

	public float[] getGridBounds() throws ExpressionParseException{
		float[] bounds = new float[6];
		bounds[0] = GuiUtil.evalString(xMin.getText());
		bounds[1] = GuiUtil.evalString(xMax.getText());
		bounds[2] = GuiUtil.evalString(yMin.getText());
		bounds[3] = GuiUtil.evalString(yMax.getText());
		bounds[4] = GuiUtil.evalString(zMin.getText());
		bounds[5] = GuiUtil.evalString(zMax.getText());
		return bounds;
	}
	
	public float[] getGridStepsize() throws ExpressionParseException{
		float[] bounds = getGridBounds();
		float[] stepSize = new float[3];
		stepSize[0] = Math.abs(bounds[1]-bounds[0])/xSlider.getValue();
		stepSize[1] = Math.abs(bounds[3]-bounds[2])/ySlider.getValue();
		stepSize[2] = Math.abs(bounds[5]-bounds[4])/zSlider.getValue();
		return stepSize;
	}
	
	public void setGridBounds(float[] bounds){
		xMin.setText(bounds[0] + "");
		xMax.setText(bounds[1] + "");
		yMin.setText(bounds[2] + "");
		yMax.setText(bounds[3] + "");
		zMin.setText(bounds[4] + "");
		zMax.setText(bounds[5] + "");
	}
	
	public void setSliders(float[] stepSize) throws ExpressionParseException{
		float[] bounds = getGridBounds();
		xSlider.setValue((int) (Math.abs(bounds[1]-bounds[0])/stepSize[0]));
		ySlider.setValue((int) (Math.abs(bounds[3]-bounds[2])/stepSize[1]));
		zSlider.setValue((int) (Math.abs(bounds[5]-bounds[4])/stepSize[2]));
	}
}