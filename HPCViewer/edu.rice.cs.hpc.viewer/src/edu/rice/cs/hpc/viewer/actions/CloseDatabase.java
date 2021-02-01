/**
 * 
 */
package edu.rice.cs.hpc.viewer.actions;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import edu.rice.cs.hpc.data.experiment.Experiment;
import edu.rice.cs.hpc.viewer.editor.IViewerEditor;
import edu.rice.cs.hpc.viewer.framework.ApplicationWorkbenchAdvisor;
import edu.rice.cs.hpc.viewer.scope.AbstractBaseScopeView;
import edu.rice.cs.hpc.viewer.util.WindowTitle;
import edu.rice.cs.hpc.viewer.window.ViewerWindow;
import edu.rice.cs.hpc.viewer.window.ViewerWindowManager;

/**
 *
 */
public class CloseDatabase extends AbstractHandler {

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException 
	{
		final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		// get an array of open databases for this window
		final ViewerWindow vWin = ViewerWindowManager.getViewerWindow(window);
		if ( vWin == null) {
			return null;		// get method already issued error dialog
		}
		
		// make sure the viewer perspective is the current active page
		String perspectiveId = ApplicationWorkbenchAdvisor.PERSPECTIVE_ID;
		try {
			window.getWorkbench().showPerspective(perspectiveId, window);
		} catch (WorkbenchException e) {
			e.printStackTrace();
		}
		
		final IWorkbenchPage curPage = window.getActivePage();
		final String[] dbArray = vWin.getDatabasePaths();
		Object[] databasesToClose;
		
		if (dbArray.length == 0) {
			MessageDialog.openError(window.getShell(), 
					"Error: No Open Database's Found.", 
					"There are no databases in this window which can be closed.");
			return null;		// set method already issued error dialog
			
		} else if (dbArray.length == 1) {
			
			// ------------------------------------------------------------
			// if only one database is opened, we just close everything
			//	no need to ask which database to close !
			// ------------------------------------------------------------

			databasesToClose = dbArray;
		} else {
			
			final List<String> dbList = Arrays.asList(dbArray);

			// put up a dialog with the open databases in the current window in a drop down selection box
			ListSelectionDialog dlg = new ListSelectionDialog(window.getShell(), dbList, 
				new ArrayContentProvider(), new LabelProvider(), "Select the databases to close:");
			dlg.setTitle("Select Databases");
			if (dlg.open() == Dialog.OK) {
				Object[] selectedDatabases = dlg.getResult();

				if ((selectedDatabases == null) || (selectedDatabases.length <= 0)) {
					return null;
				}
				databasesToClose = selectedDatabases;
			} else {
				return null;
			}
		}

		
		// -----------------------------------------------------------------------
		// close the databases, and all editors and views associated with them
		// -----------------------------------------------------------------------
		for (Object selectedDatabase: databasesToClose) {
			
			// remove the database from our database manager information
			int dbNum = vWin.removeDatabase(selectedDatabase.toString());
			if (dbNum < 0) {
				// can't close parts for an entry we could not find
				continue;
			}

			// close any open editor windows for this database
			final org.eclipse.ui.IEditorReference editors[] = curPage.getEditorReferences();
			for (IEditorReference editor: editors) {
				IEditorPart edPart = editor.getEditor(false);
				
				// ----------------------------------------------------------
				// if the editor is an instance of hpcviewer's editor, then we close it
				// 		if the database associated with it is the same as the database we
				//		want to close
				// ----------------------------------------------------------
				if (edPart instanceof IViewerEditor) {
					final IViewerEditor viewerEditor = (IViewerEditor) edPart;
					final Experiment exp = viewerEditor.getExperiment();
					if (exp != null) {
						final File dir = exp.getDefaultDirectory();
						// ----------------------------------------------------------
						// at the moment we don't have mechanism to compare database
						// thus, we just compare the path 
						// ----------------------------------------------------------
						if (dir.getAbsolutePath().equals(selectedDatabase)) {
							curPage.closeEditor(edPart, false);
						}
					}
				}
			}
			
			// close this databases metrics views
			org.eclipse.ui.IViewReference views[] = curPage.getViewReferences();
			int nbViews = views.length;
			for(int j=0;j<nbViews;j++) {
				IViewPart objPart = views[j].getView(false);
				if (objPart instanceof AbstractBaseScopeView) {
					final AbstractBaseScopeView objView = (AbstractBaseScopeView) objPart;
					final Experiment experiment = objView.getExperiment();
					if (experiment != null) {
						String xmlFileName = experiment.getDefaultDirectory().getAbsolutePath();
						
						if (selectedDatabase.equals(xmlFileName)) {
							curPage.hideView(objView);
						}
					}
				}
			}
		}
		WindowTitle wt = new WindowTitle();
		wt.refreshAllTitles();
		return null;
	}

}
