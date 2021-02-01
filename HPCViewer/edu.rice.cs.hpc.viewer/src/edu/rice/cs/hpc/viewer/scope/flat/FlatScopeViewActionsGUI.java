/**
 * 
 */
package edu.rice.cs.hpc.viewer.scope.flat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IWorkbenchWindow;

import edu.rice.cs.hpc.data.util.OSValidator;
import edu.rice.cs.hpc.data.experiment.scope.Scope;
import edu.rice.cs.hpc.viewer.resources.Icons;
import edu.rice.cs.hpc.viewer.scope.ScopeViewActions;
import edu.rice.cs.hpc.viewer.scope.ScopeViewActionsGUI;


/**
 * Actions GUI class specifically designed for flat view. 
 * This view includes toolbar for flatten and unflatten actions
 * 
 * @author laksonoadhianto
 *
 */
public class FlatScopeViewActionsGUI extends ScopeViewActionsGUI {

	private ToolItem tiFlatten;		//flatten button
	private ToolItem tiUnFlatten ;	// unflatten button
	protected FlatScopeViewActions objFlatAction;
	/**
	 * @param viewSite
	 * @param parent
	 * @param objActions
	 */
	public FlatScopeViewActionsGUI(Shell objShell, IWorkbenchWindow window, Composite parent,
			ScopeViewActions objActions) {
		super(objShell, window, parent, objActions);
		this.objFlatAction = (FlatScopeViewActions) objActions;
	}

	/**
	 * Method to start to build the GUI for the actions
	 * @param parent
	 * @return
	 */
	public Composite buildGUI(Composite parent, CoolBar coolbar) {
		// add toobar actions for flatten and unflatten
		Composite c =  addTooBarAction(coolbar);
		super.finalizeToolBar(parent, coolbar);
		return c;
	}

	/**
	 * Disables all actions
	 */
	public void resetActions() {
		super.resetActions();
		this.tiFlatten.setEnabled(false);
		this.tiUnFlatten.setEnabled(false);
	}
	
	/**
	 * Enable the some actions (resize and column properties) actions for this view
	 */
	public void enableActions() {
		super.enableActions();
		this.checkFlattenButtons();
	}

	/**
	 * Method to start to build the GUI for the actions
	 * @param parent
	 * @return
	 */
	protected Composite addTooBarAction(CoolBar parent)  {
    	// prepare the toolbar
    	ToolBar toolbar = new ToolBar(parent, SWT.FLAT);

    	// ------------- prepare the items
    	// flatten
    	tiFlatten = new ToolItem(toolbar, SWT.PUSH);
    	tiFlatten.setToolTipText("Flatten nodes one level");
    	tiFlatten.setImage(Icons.getImage(Icons.Image_Flatten));
    	tiFlatten.addSelectionListener(new SelectionAdapter() {
      	  	public void widgetSelected(SelectionEvent e) {
      	  		objFlatAction.flatten();
      	  	}
      	});
    	
    	// unflatten
    	tiUnFlatten = new ToolItem(toolbar, SWT.PUSH);
    	tiUnFlatten.setToolTipText("Unflatten nodes one level");
    	tiUnFlatten.setImage(Icons.getImage(Icons.Image_Unflatten));
    	tiUnFlatten.addSelectionListener(new SelectionAdapter(){
      	  	public void widgetSelected(SelectionEvent e) {
      	  		objFlatAction.unflatten();
      	  	}    		
    	});
    	
    	// 2009.04.21: temporary solution for windowz: add space to show unflatten buttons
    	if (OSValidator.isWindows()) {
        	new ToolItem(toolbar, SWT.NULL);
    	}
    	
    	this.createCoolItem(parent, toolbar);

    	// we need to add the parent's default actions
    	// Without this statement, the default actions disappear
		Composite objComposite = super.addTooBarAction(parent);
    	
		return objComposite;
	}
    /**
     * Check if flatten/unflatten buttons need to be disable or not.
     */
    public void checkFlattenButtons() {
    	tiFlatten.setEnabled( shouldFlattenBeEnabled() );
    	tiUnFlatten.setEnabled( shouldUnflattenBeEnabled() );
    }

	/**
	 * Update the GUI when a flatten actions are performed
	 * @param iLevel
	 * @param showAggregate show in the root node the aggregate metrics
	 */
	public void updateFlattenView(Scope parentNode) {
		//this.displayRootExperiment();	// display the aggregate metrics
		insertParentNode(parentNode);
		checkFlattenButtons();
	}

	/**
	 * Check if it is possible to flatten the tree
	 * @return
	 */
    public boolean shouldFlattenBeEnabled() {
    	
    	//return (objFlatAction.getScopeZoom().canZoomIn( ));
    	/*
    	// https://outreach.scidac.gov/tracker/index.php?func=detail&aid=342&group_id=22&atid=169
    	// BUGS #342: we need to disable flatten buttons when the tree has been zoomed
    	if ( objFlatAction.getScopeZoom().canZoomOut() )
    		return false;
    	*/
    	// DO NOT flatten if we reach to the point where there is no children 
    	//return this.myRootScope.getTreeNode().getDepth()>this.myRootScope.getFlattenLevel() + 1;
    	Object o = this.treeViewer.getInput();
    	if (o instanceof Scope) {
    		Scope objNode = (Scope) o;
    		for( int i=0; i<objNode.getChildCount(); i++ ) {
    			if (objNode.getChildAt(i).hasChildren())
    				return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Verify if unflatten can be done
     * @return
     */
    public boolean shouldUnflattenBeEnabled() {
    	
    	return (objFlatAction.canUnflatten());
    	
    	// DO NOT unflatten if we reach to root scope level
    	//return (objFlatAction.getFlattenLevel()>0);
    }


}
