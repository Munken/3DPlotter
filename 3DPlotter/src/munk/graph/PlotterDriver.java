package munk.graph;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JTextField;

import org.nfunk.jep.ParseException;

import munk.graph.appearance.Colors;
import munk.graph.plot.Plotter3D;

public class PlotterDriver {
	
	public static void main(String[] args) throws ParseException {
		final JFrame frame = new JFrame("Mega ultra");
		
		final Plotter3D p = new Plotter3D();
		final float i = 3;
//		p.plotFunction("z = y", i, -i, i, -i, Colors.MAGENTA);
//        p.plotFunction(-i, i, -i, i, "z = x", Colors.RED);
//        p.plotFunction(-i, i, -i, i, "x = y", Colors.CYAN);
//        p.plotFunction(-i, i, -i, i, "x = z", Colors.BLUE);
//        p.plotFunction(-i, i, -i, i, "y = x", Colors.TURQUISE);
//        p.plotFunction(-i, i, -i, i, "y = cos(z)*cos(x)", Colors.INDIGO);
//		p.plotParametricFunction("0.5*cos(t)", "0.5*sin(t)", "1", 0, (float) (2*Math.PI));
//		p.plotParametricFunction("0.5*cos(t)", "0", "0.5*sin(t)", 0, (float) (2*Math.PI));
		p.plotParametricFunction("0.5*cos(t)", "t/5", "0.5*sin(t)", 0, (float) (10*Math.PI));
		
		final JTextField function = new JTextField();
		function.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						try {
							p.plotFunction(function.getText(), -i, i, -i, i, Colors.RED);
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
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
