/**
 * 
 */
package edu.rice.cs.hpc.viewer.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.FileEditorInput;

import edu.rice.cs.hpc.viewer.help.HTMLEditor;

/**
 * @author laksonoadhianto
 *
 */
public class HelpManual extends AbstractHandler {
	final static private String HELP_FILE_PATH = "/doc/hpcviewer-users-manual.html";

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow objWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		
		IWorkspace objWorkspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = objWorkspace.getRoot();

		IFile file = root.getFile(new Path(HELP_FILE_PATH));
		
		IWorkbenchPage objPage = objWindow.getActivePage(); 
				
		try {
			FileEditorInput objInput = new FileEditorInput(file);
			objPage.openEditor(objInput, HTMLEditor.ID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
