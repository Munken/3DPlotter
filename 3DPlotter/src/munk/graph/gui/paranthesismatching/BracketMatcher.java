package munk.graph.gui.paranthesismatching;

import java.awt.Color;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.*;


/**
 * A class to support highlighting of parenthesis.  To use it, add it as a
 * caret listener to your text component.
 * 
 * It listens for the location of the dot.  If the character before the dot
 * is a close paren, it finds the matching start paren and highlights both
 * of them.  Otherwise it clears the highlighting.
 *
 * This object can be shared among multiple components.  It will only
 * highlight one at a time.
 **/
public class BracketMatcher implements CaretListener
{
	/** The tags returned from the highlighter, used for clearing the
        current highlight. */
	private Object start, end;

	/** The last highlighter used */
	private Highlighter highlighter;

	/** Used to paint good parenthesis matches */
	private Highlighter.HighlightPainter goodPainter;

	/** Used to paint bad parenthesis matches */
	private Highlighter.HighlightPainter badPainter;

	/** Highlights using a good painter for matched parens, and a bad
        painter for unmatched parens */
	public BracketMatcher(Highlighter.HighlightPainter goodHighlightPainter,
			Highlighter.HighlightPainter badHighlightPainter)
			{
		this.goodPainter = goodHighlightPainter;
		this.badPainter = badHighlightPainter;
			}

	/** A BracketMatcher with the default highlighters (cyan and magenta) */
	public BracketMatcher()
	{
		this(new DefaultHighlighter.DefaultHighlightPainter(Color.MAGENTA),
				new DefaultHighlighter.DefaultHighlightPainter(Color.CYAN));
	}

	private void clearHighlights()
	{
		if(highlighter != null) {
			if(start != null)
				highlighter.removeHighlight(start);
			if(end != null)
				highlighter.removeHighlight(end);
			start = end = null;
			highlighter = null;
		}
	}

	/** Returns the character at position p in the document*/
	private static char getCharAt(Document doc, int p) 
			throws BadLocationException
			{
		return doc.getText(p, 1).charAt(0);
			}

	/** Returns the position of the matching parenthesis (bracket,
	 * whatever) for the character at paren.  It counts all kinds of
	 * brackets, so the "matching" parenthesis might be a bad one.  For
	 * this demo, we're not going to take quotes or comments into account
	 * since that's not the point.
	 * 
	 * It's assumed that paren is the position of some parenthesis
	 * character
	 * 
	 * @return the position of the matching paren, or -1 if none is found 
	 **/
	private static int findMatchingParenBackwards(Document d, int paren) 
			throws BadLocationException	{
		int parenCount = 1;
		int i = paren-1;
		for(; i >= 0; i--) {
			char c = getCharAt(d, i);
			switch(c) {
			case ')':
			case '}':
			case ']':
				parenCount++;
				break;
			case '(':
			case '{':
			case '[':
				parenCount--;
				break;
			}
			if(parenCount == 0)
				break;
		}
		return i;
	}
	
	private static int findMatchingParenForwards(Document d, int paren) 
			throws BadLocationException	{
		int parenCount = 1;
		int i = paren+1;
		for(; i < d.getLength(); i++) {
			char c = getCharAt(d, i);
			switch(c) {
			case ')':
			case '}':
			case ']':
				parenCount--;
				break;
			case '(':
			case '{':
			case '[':
				parenCount++;
				break;
			}
			if(parenCount == 0)
				break;
		}
		return (i < d.getLength()) ? i : -1;
	}

	/** Called whenever the caret moves, it updates the highlights */
	public void caretUpdate(CaretEvent e) {
		clearHighlights();
		JTextComponent source = (JTextComponent) e.getSource();
		highlighter = source.getHighlighter();
		Document doc = source.getDocument();
		if(e.getDot() == 0) {
			highlighter.removeAllHighlights();
			return;
		}

		// The character we want is the one before the current position
		int thisParen = e.getDot()-1;
		try {
			char c = getCharAt(doc, thisParen);
			int openParen = -1;

			if (!isClosingBracket(c) && !isOpeningBracket(c))
				return;

			if(isClosingBracket(c)) 
				openParen = findMatchingParenBackwards(doc, thisParen);

			else if (isOpeningBracket(c))
				openParen = findMatchingParenForwards(doc, thisParen);

			if(openParen >= 0) {
				char c2 = getCharAt(doc, openParen);
				if(doBracketsMatch(c, c2) || doBracketsMatch(c2, c)) {
					start = highlighter.addHighlight(openParen,
							openParen+1,
							goodPainter);
					end = highlighter.addHighlight(thisParen,
							thisParen+1,
							goodPainter);
				}
				else {
					start = highlighter.addHighlight(openParen,
							openParen+1,
							badPainter);
					end = highlighter.addHighlight(thisParen,
							thisParen+1,
							badPainter);
				}
			}
			else {
				end = highlighter.addHighlight(thisParen,
						thisParen+1,
						badPainter);		
			}


		}
		catch(BadLocationException ex) {
			throw new Error(ex);
		}
	}

	private boolean doBracketsMatch(char c, char c2) {
		return (c2 == '(' && c == ')') ||
				(c2 == '{' && c == '}') ||
				(c2 == '[' && c == ']');
	}

	private boolean isClosingBracket(char c) {
		return c == ')' || c == ']' || c == '}';
	}
	
	private boolean isOpeningBracket(char c) {
		return c == '(' || c == '[' || c == '{';
	}


}


