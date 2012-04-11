package munk.graph;

import munk.graph.function.ImplicitRecursive;

public class Test  {

  public static void main(String[] args) {
	ImplicitRecursive im = new ImplicitRecursive("x = 2", 0, 3, 0, 3, 0, 3, 0.1f);
	
	long current = System.currentTimeMillis();
//	System.out.println(im.findStartCube());
	im.plot();
	System.out.println(System.currentTimeMillis() - current);
  }
}