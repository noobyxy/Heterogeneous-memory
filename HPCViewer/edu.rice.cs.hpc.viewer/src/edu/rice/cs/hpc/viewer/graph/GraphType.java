package edu.rice.cs.hpc.viewer.graph;

public class GraphType {
	public enum PlotType {PLOT, SORTED, HISTO};

	static public String toString(PlotType type) {
		
		switch (type) {
		case PLOT:
			return "Plot graph";
		case SORTED:
			return "Sorted plot graph";
		case HISTO:
			return "Histogram graph";
		}
		
		return null;
	}
}
