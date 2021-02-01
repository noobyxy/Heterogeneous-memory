package edu.rice.cs.hpc.viewer.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

public class NewWindow extends AbstractHandler implements IHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			IWorkbenchWindow workbench = PlatformUI.getWorkbench().openWorkbenchWindow(null);
			if(workbench == null) {
				throw new java.lang.RuntimeException("Unable to create an RCP workbench window");
			}
		} catch (WorkbenchException e) {
			e.printStackTrace();
		}
		return null;
	}

}
