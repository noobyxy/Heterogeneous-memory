package edu.rice.cs.hpc.viewer.framework;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import edu.rice.cs.hpc.viewer.scope.bottomup.CallerScopeView;
import edu.rice.cs.hpc.viewer.scope.flat.FlatScopeView;
import edu.rice.cs.hpc.viewer.scope.topdown.ScopeView;
import edu.rice.cs.hpc.viewer.window.ViewerWindow;


/*****
 * 
 * Default perspective for the viewer
 *
 */
public class Perspective implements IPerspectiveFactory {
	
	final private static String FOLDER_ID = "metricViews";
	
	// we have to use IFoderLayout instead of IPlaceFolderLayout since its states will be stored. 
	private IFolderLayout[] objLayout = new IFolderLayout[ViewerWindow.maxDbNum];

	public void createInitialLayout(IPageLayout layout) {
		// get the main area
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.setFixed(false);
		
		// configure the view layout, create a separate partition (layout folder) for each of the databases that can be opened
		// this will group the views for each database so they can be treated as a one thing (minimize, restore, resizing, relocating)
		for (int i=0 ; i<ViewerWindow.maxDbNum ; i++) {
			if (i==0) {
				objLayout[i] = layout.createFolder(FOLDER_ID + i, IPageLayout.BOTTOM, 0.6f, editorArea);
			} else {
				objLayout[i] = layout.createFolder(FOLDER_ID + i, IPageLayout.RIGHT, 0.3f, "metricViews"+ (i-1));
			}
			objLayout[i].addView(ScopeView.ID + ":" + i);
			objLayout[i].addView(CallerScopeView.ID + ":" + i);
			objLayout[i].addView(FlatScopeView.ID + ":" + i);
		}
	}
	
	// This method allows an external package to add views to the layout folders created above.
	// It is not needed by any code in the Rice viewer but Bull's viewer extensions will need something like 
	// this and it is general enough that it could also be used by other organizations wishing to extend the 
	// features offered in the standard Rice viewer.
	public void createInitialLayout(IPageLayout layout, String baseViewId) {
		for (int i=0 ; i<ViewerWindow.maxDbNum ; i++) {
			objLayout[i].addPlaceholder(baseViewId +  ":" + i);
		}
	}
}
