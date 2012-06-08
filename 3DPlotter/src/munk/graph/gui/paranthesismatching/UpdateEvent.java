package munk.graph.gui.paranthesismatching;

import javax.swing.event.CaretEvent;

public class UpdateEvent extends CaretEvent {

	public UpdateEvent(Object source) {
		super(source);
	}

	@Override
	public int getDot() {
		return 0;
	}

	@Override
	public int getMark() {
		return 0;
	}

}
