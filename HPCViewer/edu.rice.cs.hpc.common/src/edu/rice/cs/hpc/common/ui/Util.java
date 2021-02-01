package edu.rice.cs.hpc.common.ui;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.State;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.RegistryToggleState;
import org.eclipse.ui.part.EditorPart;

import edu.rice.cs.hpc.data.util.JavaValidator;

public class Util {
	
	/****
	 * utility to get a window's command object of a given ID
	 * @param window : window ID
	 * @param commandID : command ID
	 * 
	 * @return the command (usually a menu command)
	 */
	static public Command getCommand( IWorkbenchWindow window, String commandID ) {
		Command command = null;
		ICommandService commandService = (ICommandService) window.getService(ICommandService.class);

		if (commandService != null)
			command = commandService.getCommand( commandID );
		
		return command;
	}
	
	/***
	 * verify if the menu "Show trace records" is checked
	 * 
	 * @return true of the menu is checked. false otherwise
	 */
	static public boolean isOptionEnabled(Command command)
	{
		boolean isEnabled = false;

		final State state = command.getState(RegistryToggleState.STATE_ID);
		if (state != null)
		{
			final Boolean b = (Boolean) state.getValue();
			isEnabled = b.booleanValue();
		}
		return isEnabled;
	}

	/****
	 * get the status line of the current active window
	 * 
	 * @return IStatusLineManager the status line manager
	 */
	public static IStatusLineManager getActiveStatusLineManager() {
		if (!PlatformUI.isWorkbenchRunning())
			return null;
		
		final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window == null) {
			return null;
		}

		final IWorkbenchPartSite site = window.getActivePage().getActivePart().getSite();
		
		IStatusLineManager statusLine = null;
		
		// --------------------------------------------------------------
		// the current active site can be either editor or view
		// if none of them is active, then we have nothing
		//
		//	@TODO ugly code: this is a hack version to identify the current "site"
		//		   status bar is hosted in a view site or editor site. Other than that, we are doomed.
		// --------------------------------------------------------------
		if (site instanceof IViewSite)
			statusLine = ((IViewSite)site).getActionBars().getStatusLineManager();
		else if (site instanceof IEditorSite)
			statusLine = ((IEditorSite)site).getActionBars().getStatusLineManager();
		else if (site instanceof EditorPart){
			statusLine = ((EditorPart) site).getEditorSite().getActionBars().getStatusLineManager();
		} else {
			System.out.println("unknown active site: " + site + " \t class: " + site.getClass());
		}

		return statusLine;
	}

	/******
	 * Retrieve the shell of the current active window
	 * <p>Do NOT use this method for creating a window as it will mislead the parent of the child. 
	 * @return a shell window
	 */
	static public Shell getActiveShell() {
		Shell shell = null;
		final IWorkbenchWindow window = getActiveWindow();
		if (window != null)
			shell = window.getShell();
		return shell;
	}	
	
	/*****
	 * get the current active window
	 * @return
	 */
	static public IWorkbenchWindow getActiveWindow() {
		final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		return window;
	}
	
	/****
	 * check if we run the correct version of JVM (assuming all JVM except GIJ will work)
	 * Up to now, only Gnu Java (GCJ) that doesn't work properly with Eclipse. 
	 * It works most cases, but it can crash if we use Java API that is not fully supported
	 * 	by GCJ
	 * 
	 * @param shell
	 * @return true if we can continue, false otherwise
	 */
	static public boolean checkJavaVendor(Shell shell) {
		boolean ok  = true;
		if (JavaValidator.isGCJ()) {
			String vendor = JavaValidator.getJavaVendor();
			ok = MessageDialog.openQuestion(shell, "Java vendor is not supported", 
					"Warning ! JVM vendor is not supported: " + vendor + "\n" + 
					"The application may not work properly with this JVM\nDo you want to continue ?");
		}
		return ok;
	}
}
