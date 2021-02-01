package edu.rice.cs.hpc.traceviewer.framework;

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import edu.rice.cs.hpc.common.ui.Util;
import edu.rice.cs.hpc.traceviewer.db.TraceDatabase;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	final private String args[];
	
	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer, String []_args) {
		super(configurer);
		args = _args;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#createActionBarAdvisor(org.eclipse.ui.application.IActionBarConfigurer)
	 */
	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#preWindowOpen()
	 */
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();

		configurer.setShowCoolBar(false);
		configurer.setShowStatusLine(true);
		configurer.setShowProgressIndicator(true);
		
		final IWorkbenchWindow window = configurer.getWindow();
		if (!Util.checkJavaVendor(window.getShell()))
			window.close();
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#postWindowOpen()
	 */ 
	public void postWindowOpen() {
		
		//---------------------------------------------------------------------
		// once the widgets have been created, we ask user a database to open
		// ---------------------------------------------------------------------
		
		final IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		IWorkbenchWindow window = configurer.getWindow();
		
		//process command line argument - currently only works for local but can be easily modified to work with remote
		if (args != null && args.length > 0) {
			for (String arg : args) {
				if (arg != null && arg.charAt(0) != '-') {
					// this must be the name of the database to open
					TraceDatabase.openLocalDatabase(window, arg);
					return;
				} 
			}
		}
		// Eclipse indigo MAC export will add -showlocation flag in front of the executable
		// It is also possible the next version of Eclipse will add other flags
		// Hence, we need calling open dialog box here to make sure it displays on all Eclipse versions
		TraceDatabase.openLocalDatabase(window, null);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#postWindowClose()
	 */
	public void postWindowClose() {

		// remove all the allocated resources of this window
		TraceDatabase.removeInstance(this.getWindowConfigurer().getWindow());
	}
}
