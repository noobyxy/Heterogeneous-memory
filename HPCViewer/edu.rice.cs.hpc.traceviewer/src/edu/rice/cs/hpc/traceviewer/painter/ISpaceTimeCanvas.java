package edu.rice.cs.hpc.traceviewer.painter;

public interface ISpaceTimeCanvas {

    /**Conversion factor from actual time to pixels on the x axis. To be implemented in subclasses.*/
    public double getScalePixelsPerTime();
    
    /**Conversion factor from actual processes to pixels on the y axis.  To be implemented in subclasses.*/
    public double getScalePixelsPerRank();

    /** dislay a temporary message on the canvas **/
	public void setMessage(String message);

}
