package munk.graph;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JTextField;

import munk.graph.appearance.Colors;
import munk.graph.function.*;
import munk.graph.gui.Plotter3D;

import com.graphbuilder.math.ExpressionParseException;
import com.graphbuilder.math.UndefinedVariableException;

public class PlotterDriver {
	
	public static void main(String[] args) throws ExpressionParseException, IllegalEquationException, UndefinedVariableException {
		final JFrame frame = new JFrame("Mega ultra");
		
		final Plotter3D p = new Plotter3D();
		final float i = 1f;
		
//		float[] bounds = {0, (float) (2*Math.PI)};
//		ParametricFunction pp = new ParametricFunction("0.5*cos(u)", "0.5*sin(u)", "u/10", Colors.RED, bounds, 0.1f);
//		p.plotFunction(pp);
//		
//		bounds = new float[] {0, (float) (2*Math.PI), 0, 6.28f};
//		ParametricFunction p2 = new ParametricFunction("(3*(1+sin(v)) + 2*(1-cos(v)/2)*cos(u))*cos(v)",
//				 									   "(4+2*(1-cos(v)/2)*cos(u))*sin(v)", 
//				 									   "-2*(1-cos(v)/2) * sin(u)", Colors.RED, bounds, 0.1f);
//		p.plotFunction(p2);
		
		
		final float[] recBound = {-i, i, -i, i, -i, i};
		String expr = "z*z - x*(cos(y)*cos(x)) = 0";
//		expr = "x^2 + y^2 + z^2 = 3";
//		expr = "0 = 0";
//		ImplicitFunction ip = new ImplicitFunction(expr, Colors.RED, recBound, 0.05f);
//		p.plotFunction(ip);
		
//		ImplicitRecursiveFunction rec = new ImplicitRecursiveFunction(expr, Colors.BLUE, recBound, 0.05f);
//		p.plotFunction(rec);
		
		ImplicitIterativeFunction it = new ImplicitIterativeFunction("x = .97", Colors.BLUE, recBound, 0.05f);
		p.plotFunction(it);
		it = new ImplicitIterativeFunction("y = 0.97", Colors.BLUE, recBound, 0.05f);
		p.plotFunction(it);
		it = new ImplicitIterativeFunction("x = -0.97", Colors.BLUE, recBound, 0.05f);
		p.plotFunction(it);
		it = new ImplicitIterativeFunction("y = -0.97", Colors.BLUE, recBound, 0.05f);
		p.plotFunction(it);
		it = new ImplicitIterativeFunction("z = -0.97", Colors.BLUE, recBound, 0.05f);
		p.plotFunction(it);
		it = new ImplicitIterativeFunction("z = 0.97", Colors.BLUE, recBound, 0.05f);
		p.plotFunction(it);
		
		
		
//		p.plotFunction("z = y", -i, i, -i, i, Colors.MAGENTA);
//        p.plotFunction(-i, i, -i, i, "z = x", Colors.RED);
//        p.plotFunction(-i, i, -i, i, "x = y", Colors.CYAN);
//        p.plotFunction(-i, i, -i, i, "x = z", Colors.BLUE);
//        p.plotFunction(-i, i, -i, i, "y = x", Colors.TURQUISE);	
//        p.plotFunction("x = cos(z)*cos(y)", -i, i, -i, i, Colors.INDIGO);
//		p.plotParametric1D("0.5*cos(u)", "0.5*sin(u)", "u/10", 0, (float) (2*Math.PI), Colors.RED);
//		p.plotParametric2D("(3*(1+sin(v)) + 2*(1-cos(v)/2)*cos(u))*cos(v)",
//								 "(4+2*(1-cos(v)/2)*cos(u))*sin(v)", 
//								 "-2*(1-cos(v)/2) * sin(u)", 0, (float) (2*Math.PI), 0, 6.28f, Colors.BLUE);
//		
//		p.plotImplicit("x^2 + y^2 + z^2 = 1", -i, i, -i, i, -i, i, Colors.RED);
//		p.plotImplicit("2 - ((1/2.3)^2 *(x^2 + y^2 + z^2))^-6 - ( (1/2)^8 * (x^8 + y^8 + z^8) )^6 = 0", -i, i, -i, i, -i, i, Colors.RED);
//		p.plotParametricFunction("0.5*cos(t)", "t/5", "0.5*sin(t)", 0, (float) (5*Math.PI));
		
		final JTextField function = new JTextField();
		function.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {

//							p.plotFunction(function.getText(), -i, i, -i, i, Colors.RED);
							try {
								
								ImplicitIterativeFunction ip = 
										new ImplicitIterativeFunction(function.getText(), Colors.RED, recBound, 0.1f);
								p.plotFunction(ip);
							} catch (ExpressionParseException | IllegalEquationException | UndefinedVariableException e1) {
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
