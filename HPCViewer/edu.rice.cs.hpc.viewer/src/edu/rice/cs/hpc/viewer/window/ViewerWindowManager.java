package edu.rice.cs.hpc.viewer.window;

import java.util.Vector;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import edu.rice.cs.hpc.viewer.util.WindowTitle;

/** 
 * The viewer window manager keeps track of which performance databases have been opened for each 
 * window created by the hpcviewer.
 * 
 * When a window is created it creates a new ViewerWindow class and (normally) adds it to end of the vWindows vector.  
 * The index into this vector will be used as the window number and its value+1 (so the first window will have a
 * window number of 1 instead of 0) will be used in the window title.  When windows are closed its entry in the 
 * vector will be set to null so that it does not affect the window number of other windows in the vector.  When 
 * a new ViewerWindow is being added to the vector it will reuse a slot in the vector which has a null value if 
 * such an entry exists.  The ViewerWindow class will keep track of all the databases opened (using the Database class) 
 * in that window.  This allows us to provide a list of databases which can be closed for each window.  It also gives 
 * us a place to keep track of the experiment data for each opened database.
  * 
 * @author mohrg
 *
 */
public class ViewerWindowManager {
	/**
	 * Vector of tables to keep track of what has happened in each open hpcviewer window.
	 */
	private static Vector<ViewerWindow> vWindows = new Vector<ViewerWindow>(3);

	/**
	 * Returns the index into the vWindows vector of the current window being used.
	 * @return -1 if the window doesn't exist
	 *  	   otherwise: the window number
	 */
	static public int getWindowNumber (IWorkbenchWindow window) {
		for (int i=0 ; i<vWindows.size() ; i++) {
			if (vWindows.get(i) == null) {
				continue;
			}
			// if this is our callers window, return its index
			if (vWindows.get(i).getWinObj().equals(window)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Adds a new window to the database manager tables.
	 */
	public void addNewWindow (IWorkbenchWindow window) {
		// get a new viewer window object and set the workbench window it is tied to
		ViewerWindow vWin = new ViewerWindow();
		vWin.setWinObj(window);

		// see if there are any unused slots
		vWindows.add(vWin);
		
		// after the new window is added, we must update all the old windows titles
		WindowTitle wt = new WindowTitle();
		wt.refreshAllTitles();
		return;
	}

	/**
	 * Remove a window from the database managers tables.
	 */
	static public Boolean removeWindow (IWorkbenchWindow window) {
		for (int i=0 ; i<vWindows.size() ; i++) {
			final ViewerWindow vw = vWindows.get(i);
			if (vw == null) {
				continue;
			}

			// if this is the one to remove, set it to null so it can be reused later
			if (vw.getWinObj().equals(window)) {
				vw.dispose();
				// set this to something that will cause problems if used before being reset
				vWindows.remove(i);
				// after the window is removed, we must update remaining window titles (may need to remove window numbers)
				WindowTitle wt = new WindowTitle();
				wt.refreshAllTitles();
				return true;
			}
		}

		MessageDialog.openError(window.getShell(), 
				"Error: Removing Window from Database Manager.", 
				"Window " + window.toString() + " not found in the list of window objects.");
		return false;
	}

	/***
	 * return the number of opened database for a specified window
	 * @param window
	 * @return
	 */
	static public int getNumberOfDatabases(IWorkbenchWindow window) {
		ViewerWindow vw = ViewerWindowManager.getViewerWindow(window);
		if (vw != null) {
			return vw.getOpenDatabases();
		}
		return 0;
	}

	/**
	 * Returns the hpcviewer window class associated with the workbench window passed as an argument.
	 */
	static public ViewerWindow getViewerWindow(IWorkbenchWindow window) {
		for (int i=0 ; i<vWindows.size() ; i++) {
			if (vWindows.get(i) == null) {
				continue;
			}
			if (window.equals(vWindows.get(i).getWinObj())) {
				return vWindows.get(i);
			}
		}
		return null;
	}
	
	static public int size() {
		return vWindows.size();
	}
	
	static public ViewerWindow getViewerWindow(int index) {
		return vWindows.get(index);
	}
}
