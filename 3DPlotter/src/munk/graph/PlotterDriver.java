package munk.graph;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JTextField;

import munk.graph.appearance.Colors;
import munk.graph.plot.Plotter3D;

import org.nfunk.jep.ParseException;

public class PlotterDriver {
	
	public static void main(String[] args) throws ParseException {
		final JFrame frame = new JFrame("Mega ultra");
		
		final Plotter3D p = new Plotter3D();
		final float i = .3f;
//		p.plotFunction("z = y", i, -i, i, -i, Colors.MAGENTA);
//        p.plotFunction(-i, i, -i, i, "z = x", Colors.RED);
//        p.plotFunction(-i, i, -i, i, "x = y", Colors.CYAN);
//        p.plotFunction(-i, i, -i, i, "x = z", Colors.BLUE);
//        p.plotFunction(-i, i, -i, i, "y = x", Colors.TURQUISE);
//        p.plotFunction("y = cos(z)*cos(x)", -i, i, -i, i, Colors.INDIGO);
//		p.plotParametric1D("0.5*cos(t)", "0.5*sin(t)", "t/10+5", 0, (float) (2*Math.PI), Colors.RED);
//		p.plotParametric2D("(3*(1+sin(t)) + 2*(1-cos(t)/2)*cos(u))*cos(t)",
//								 "(4+2*(1-cos(t)/2)*cos(u))*sin(t)", 
//								 "-2*(1-cos(t)/2) * sin(u)", 0, (float) (2*Math.PI), 0, 6.28f, Colors.BLUE);
		
		p.plotImplicit("x^2 + y^2 + z^2 = 1", -i, i, -i, i, -i, i, Colors.RED);
//		p.plotImplicit("x^2 + y^2 + z^2 = 1", 0, i, 0, i, 0, i, Colors.RED);
//		p.plotParametricFunction("0.5*cos(t)", "t/5", "0.5*sin(t)", 0, (float) (5*Math.PI));
		
		final JTextField function = new JTextField();
		function.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						try {
//							p.plotFunction(function.getText(), -i, i, -i, i, Colors.RED);
							p.plotImplicit(function.getText(), -i, i, -i, i, -i, i, Colors.RED);
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
				}
			}
		});
		frame.add(function,BorderLayout.NORTH);
     	frame.add(p);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.pack();
		
	}

}
