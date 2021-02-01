package edu.rice.cs.hpc.viewer.graph;

import org.eclipse.swt.events.MouseEvent;
import org.swtchart.ISeries;

public class UserSelectionData 
{
	public int index;
	public double valueX, valueY;
	public ISeries serie;
	
	public MouseEvent event;
}
