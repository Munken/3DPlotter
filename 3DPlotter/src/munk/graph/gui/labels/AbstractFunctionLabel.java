package munk.graph.gui.labels;

import java.awt.Color;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Caret;
import javax.swing.text.JTextComponent;
import javax.vecmath.Color3f;

import munk.graph.appearance.Colors;
import munk.graph.function.Function;
import munk.graph.gui.ToggleButton;
import munk.graph.gui.listener.FunctionEvent;
import munk.graph.gui.listener.FunctionListener;

public abstract class AbstractFunctionLabel extends JPanel implements FunctionLabel {
	
	protected enum STATE {
		FAILED,
		CHANGED,
		SELECTED;
	}
	private STATE state;
	
	private static final long	serialVersionUID	= -4247550891717359687L;
	private Function function;
	
	protected JButton btnDelete;
	protected ToggleButton toggleButton;
	
	protected boolean selected;
	
	private List<FunctionListener> listeners = new ArrayList<FunctionListener>();
	
	AbstractFunctionLabel(Function mother) {
		this.function = mother;
		
	}
	
	public void setMother (Function mother) {
		this.function = mother;
	}
	
	public Function getMother(){
		return function;
	}
	
	protected void notifyPlotUpdate(String... text) {
		FunctionEvent ev = new FunctionEvent(function, text, 
												function.getColor(), function.getBoundsString(), 
												function.getStepsize(), FunctionEvent.ACTION.UPDATE);
		notifyListeners(ev);
	}
	
	protected ActionListener getDeleteListener() {
		return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				FunctionEvent ev = new FunctionEvent(function, FunctionEvent.ACTION.DELETE);
				notifyListeners(ev);
			}
		};
	}
	
	protected ActionListener getToogleListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				function.setVisible(toggleButton.isSelected());
				FunctionEvent ev = new FunctionEvent(function, FunctionEvent.ACTION.VISIBILITY);
				
				notifyListeners(ev);
			}
		};
	}
	
	public KeyAdapter blinkFunctionListener() {
		return new KeyAdapter() {
			
			Color3f color;
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_B && e.getModifiers() == KeyEvent.CTRL_MASK) {
						color = (color == null) ? function.getColor() : color;
						function.setColor(Colors.WHITE);
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (color != null) {
					function.setColor(color);
					color = null;
				}
			}
			
		};
	}
	
	public void setIndeterminate(boolean b) {
		toggleButton.setIndeterminate(b);
	}


	protected void notifyListeners(FunctionEvent e) {
		for (FunctionListener l : listeners) {
			l.functionChanged(e);
		}
	}
	
	public void setSelected(boolean b){
		selected = b;
		if(selected){
			setExpressionFieldBackground(SELECTED_COLOR);
		}
		else{
			setExpressionFieldBackground(NORMAL_COLOR);
		}
	}
	
	protected abstract void setExpressionFieldBackground(Color color);
	
	public void addFunctionListener(FunctionListener l) {
		listeners.add(l);
	}
	
	protected void fireCaretUpdate(JTextComponent c) {
		
		CaretEvent e = new MutableCaretEvent(c);
		for (CaretListener l : c.getCaretListeners()) {
			l.caretUpdate(e);
		}
	}
	
	
	class MutableCaretEvent extends CaretEvent {

		private static final long	serialVersionUID	= 3573431986836372179L;
		
		
		private int dot;
		private int mark;
		MutableCaretEvent(JTextComponent c) {
			super(c);
            if (c != null) {
                Caret caret = c.getCaret();
                dot = caret.getDot();
                mark = caret.getMark();
            }
		}

		public final String toString() {
			return "dot=" + dot + "," + "mark=" + mark;
		}

		// --- CaretEvent methods -----------------------

		public final int getDot() {
			return dot;
		}

		public final int getMark() {
			return mark;
		}

	}
	
	protected void updateColor() {
		if (state == STATE.FAILED) {
			setExpressionFieldBackground(FAILED_COLOR);
			
		} else if (state == STATE.CHANGED) {
			setExpressionFieldBackground(WARNING_COLOR);
		} else {
			state = STATE.SELECTED;
			setExpressionFieldBackground(SELECTED_COLOR);
		}
	}
	
	protected void setState(STATE state) {
		this.state = state;
	}
	

}
