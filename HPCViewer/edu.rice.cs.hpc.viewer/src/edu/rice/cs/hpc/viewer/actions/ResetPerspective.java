/**
 * 
 */
package edu.rice.cs.hpc.viewer.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import edu.rice.cs.hpc.viewer.scope.AbstractBaseScopeView;


/**
 * Reset the layout
 * @author la5
 *
 */
public class ResetPerspective extends AbstractHandler {


	public Object execute(ExecutionEvent event) throws ExecutionException {

		final IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
		
		// ask to reset the layout
		page.resetPerspective();
		
		//----------------------------------------
		// hide unused views
		//----------------------------------------
		IViewReference viewRefs[] = page.getViewReferences();
		for (IViewReference viewRef: viewRefs) {
			
			final IViewPart view = viewRef.getView(false);
			if (view instanceof AbstractBaseScopeView) {
				final AbstractBaseScopeView scopeView = (AbstractBaseScopeView) view;
				if (scopeView.getExperiment() == null)
					page.hideView(view);
			} else {
				page.hideView(view);
			}
		}
		
		return null;
	}

}
