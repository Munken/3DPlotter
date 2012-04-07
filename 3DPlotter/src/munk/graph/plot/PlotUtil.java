package munk.graph.plot;

public class PlotUtil {
	
	public static float[] initAxisArray(float min, int length, float stepsize) {
		float[] result = new float[length];
		
		float current = min;
		for (int i = 0; i < length; i++) {
			result[i] = current;
			current += stepsize;
		}
		
		return result;
	}

}
