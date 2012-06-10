package munk.graph.gui.labels;

import javax.swing.JPanel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Caret;
import javax.swing.text.JTextComponent;

public abstract class AbstractFunctionLabel extends JPanel implements FunctionLabel {
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4247550891717359687L;


	
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
	

}
