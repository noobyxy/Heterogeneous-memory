package edu.rice.cs.hpc.viewer.util;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import edu.rice.cs.hpc.data.experiment.Experiment;
import edu.rice.cs.hpc.data.experiment.scope.RootScope;
import edu.rice.cs.hpc.viewer.editor.IViewerEditor;
import edu.rice.cs.hpc.viewer.scope.AbstractBaseScopeView;
import edu.rice.cs.hpc.viewer.window.ViewerWindow;
import edu.rice.cs.hpc.viewer.window.ViewerWindowManager;

public class BaseWindowTitle implements IWindowTitle {

	final private static String MAIN_TITLE = "hpcviewer";

	public BaseWindowTitle() {
	}

	/***
	 * Get the title of the main window
	 * 
	 * @param window
	 * @param experiment
	 * @return the new title
	 */
	public String getWindowTitle(IWorkbenchWindow window) {
		String winTitle = MAIN_TITLE;
		int numDB =  ViewerWindowManager.getNumberOfDatabases(window);
		// get the number of open main windows
		int numWin = ViewerWindowManager.size();

		if (numWin > 1) {
			// if multiple windows open add the window number
			winTitle = (ViewerWindowManager.getWindowNumber(window)+1) + "-" + MAIN_TITLE;
		}
		if (numDB > 1) {  // if more than 1 DB open only display the app title
			return winTitle;
		}
		// if we only have one opened database, get its name for the window title
		if (numDB == 1) {
			ViewerWindow vw = ViewerWindowManager.getViewerWindow(window);
			Experiment exp[] = vw.getExperiments();
			if (exp != null) {
				return winTitle + ": "+exp[0].getName();
			}
		}
		return winTitle;
	}

	/***
	 * Set the title of the view window
	 * 
	 * @param window
	 * @param view
	 * @return the new title
	 */
	public String setTitle(IWorkbenchWindow window, IViewPart view) { 

		if (view instanceof AbstractBaseScopeView) {
			final RootScope root = ((AbstractBaseScopeView) view).getRootScope();
			if (root != null) {
				String sTitle = root.getRootName();
				if (ViewerWindowManager.getNumberOfDatabases(window) <= 1) {
					((AbstractBaseScopeView) view).setViewTitle(sTitle);
					return sTitle;
				}
				final AbstractBaseScopeView scopeView = (AbstractBaseScopeView) view;
				final Experiment experiment = scopeView.getExperiment();
				ViewerWindow vw = ViewerWindowManager.getViewerWindow(window);
				int dbNum = 1 + vw.getDbNum(experiment);
				sTitle = dbNum + "-" + sTitle + "("+experiment.getName()+")";
				((AbstractBaseScopeView) view).setViewTitle(sTitle);
				return sTitle;
			}
		}
		return null;
	}

	/***
	 * Set the title of the Editor window
	 * 
	 * @param window
	 * @param experiment
	 * @param sTitle
	 * @return the new title
	 */
	public String setEditorTitle(IWorkbenchWindow window, IEditorPart editorPart) { 

		if (editorPart instanceof IViewerEditor) {
			IViewerEditor editor = (IViewerEditor) editorPart;
			String sTitle = editor.getEditorPartName();
			int numDB =  ViewerWindowManager.getNumberOfDatabases(window);
			Experiment exp = editor.getExperiment();
			if ((exp == null) || (numDB <= 1)) {
				editor.setEditorPartName(sTitle);
				return sTitle;
			}

			ViewerWindow vw = ViewerWindowManager.getViewerWindow(window);
			numDB = 1 + vw.getDbNum(exp);
			String sResult = numDB + "-" + sTitle ;
			editor.setEditorPartName(sResult);
			return sResult;
		}
		return null;
	}

}
