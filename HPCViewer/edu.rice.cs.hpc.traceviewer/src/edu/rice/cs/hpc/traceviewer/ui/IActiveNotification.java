package edu.rice.cs.hpc.traceviewer.ui;

/************************************************************************
 * 
 * Interface to notify that an event has been activated (or deactivated)
 *
 ************************************************************************/
public interface IActiveNotification 
{
	/****
	 * Called when an event has been changed
	 * 
	 * @param isActive : true if it's activated, false otherwise
	 */
	public void active(boolean isActive);
}
