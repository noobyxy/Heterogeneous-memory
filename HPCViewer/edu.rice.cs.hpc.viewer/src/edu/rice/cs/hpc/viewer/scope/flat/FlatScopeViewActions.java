/**
 * 
 */
package edu.rice.cs.hpc.viewer.scope.flat;

import java.util.Stack;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.swt.widgets.CoolBar;

import edu.rice.cs.hpc.data.experiment.metric.IMetricManager;
import edu.rice.cs.hpc.data.experiment.scope.CallSiteScope;
import edu.rice.cs.hpc.data.experiment.scope.Scope;
import edu.rice.cs.hpc.viewer.scope.BaseScopeViewActions;

/**
 * @author laksonoadhianto
 *
 */
public class FlatScopeViewActions extends BaseScopeViewActions {
	
	private enum FlatAction implements IActionType{ Flatten, Unflatten };
	
	private Stack<Scope> 	stackFlatNodes;
	private Stack<Object[]> 	stackExpandedNodes;
	private Stack<IActionType> stackActions;
	
	//-----------------------------------------------------------------------
	// 					METHODS
	//-----------------------------------------------------------------------

	/**
	 * @param viewSite
	 * @param parent
	 */
	public FlatScopeViewActions(Shell shell, IWorkbenchWindow window, Composite parent, CoolBar coolbar) {
		super(shell, window, parent, coolbar);
		
		stackActions = new Stack<IActionType>();
		stackFlatNodes = new Stack<Scope>();
	}

	/**
	 * Create your own specific GUI
	 * @param parent
	 * @return
	 */
	protected Composite createGUI(Composite parent, CoolBar coolbar) {
		objActionsGUI = new FlatScopeViewActionsGUI(this.objShell, this.objWindow, parent, this);
		objActionsGUI.buildGUI(parent, coolbar);
		
		return parent;
	}

	//-----------------------------------------------------------------------
	// 					FLATTEN: PUBLIC INTERFACES
	//-----------------------------------------------------------------------
	

	/**
	 * Flatten the tree one level more
	 */
	public void flatten() {
		// save the current root scope
		Scope objParentNode = (Scope) treeViewer.getInput();
		
		// -------------------------------------------------------------------
		// copy the "root" of the current input
		// -------------------------------------------------------------------
		Scope objFlattenedNode = (objParentNode.duplicate());
		//objFlattenedNode.setExperiment( objParentNode.getExperiment() );
		objParentNode.copyMetrics(objFlattenedNode, 0);
		
		boolean hasKids = false;

		// create the list of flattened node
		for (int i=0;i<objParentNode.getChildCount();i++) {
			Scope node =  (Scope) objParentNode.getChildAt(i);
			if(node.getChildCount()>0) {
				
				// this node has children, add the children
				addChildren(node, objFlattenedNode);
				hasKids = true;
			} else {
				// no children: add the node itself !
				objFlattenedNode.add(node);
			}
		}
		if(hasKids) {
			if (objFlattenedNode.hasChildren()) {
				pushElementStates();
				
				stackFlatNodes.push(objParentNode);

				this.treeViewer.getTree().setRedraw(false);
				// we update the data of the table
				this.treeViewer.setInput(objFlattenedNode);
				// refreshing the table to take into account a new data
				this.treeViewer.refresh();
				
				updateAction(FlatAction.Flatten, objFlattenedNode);

				this.treeViewer.getTree().setRedraw(true);
				checkStates(getSelectedNode());
			} else {
				// the original tree has children, but only contains call sites
				// since we do not allow call site as a leaf node, we need to
				// forbid this kind of flatten operation
				showErrorMessage("Cannot flatten a tree that has only callsite nodes");
			}
		}
	}

	/**
	 * Unflatten flattened tree (tree has to be flattened before)
	 */
	public void unflatten() {
		if (stackFlatNodes.isEmpty())
			return;
		
		Scope objParentNode = stackFlatNodes.pop();
		if(objParentNode != null) {
			this.treeViewer.setInput(objParentNode);

			updateAction(FlatAction.Unflatten, objParentNode);
			
			popElementStates();
			
			checkStates(getSelectedNode());
		}
	}

	public boolean canUnflatten() {
		return (!stackActions.isEmpty() && stackActions.peek()==FlatAction.Flatten);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.rice.cs.hpc.viewer.scope.IToolbarManager#checkStates(edu.rice.cs.hpc.data.experiment.scope.Scope.Node)
	 */
	public void checkStates(Scope nodeSelected) {
		boolean bCanZoomIn = objZoom.canZoomIn(nodeSelected);
		objActionsGUI.enableHotCallPath( bCanZoomIn );
		objActionsGUI.enableZoomIn( bCanZoomIn );

		((FlatScopeViewActionsGUI) objActionsGUI).checkFlattenButtons();

		boolean bCanZoomOut = objZoom.canZoomOut() && 
				(!stackActions.isEmpty() && stackActions.peek()==ActionType.ZoomIn);
		objActionsGUI.enableZoomOut( bCanZoomOut );
	}


	
	//-----------------------------------------------------------------------
	// 					FLATTEN: PRIVATE METHODS
	//-----------------------------------------------------------------------

	/**
	 * store the current expanded elements into a stack
	 */
	private void pushElementStates() {
		Object []arrNodes = this.treeViewer.getExpandedElements();
		if (stackExpandedNodes == null)
			stackExpandedNodes = new java.util.Stack<Object[]>();

		stackExpandedNodes.push(arrNodes);
	}
	
	/**
	 * recover the latest expanded element into stack
	 */
	private void popElementStates() {
		Object []arrNodes = stackExpandedNodes.pop();
		this.treeViewer.setExpandedElements(arrNodes);
	}
	
	/****
	 * Once an action is performed, we need to update the buttons and register the action
	 * 
	 * @param type
	 * @param root
	 */
	private void updateAction(IActionType type, Scope root) {
		registerAction(type);
		
		// post processing: inserting the "aggregate metric" into the top row of the table
		((FlatScopeViewActionsGUI) objActionsGUI).updateFlattenView(root);
	}

	private void addChildren(Scope node, Scope arrNodes) {
		int nbChildren = node.getChildCount();
		for(int i=0;i<nbChildren;i++) {
			// Laksono 2009.03.04: do not add call site !
			Scope nodeKid = ((Scope) node.getChildAt(i));

			if (!(nodeKid instanceof CallSiteScope)) {
				// the kid is a callsite: do nothing
				// otherwise add the kid into the list of scopes to display
				arrNodes.add(nodeKid);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.rice.cs.hpc.viewer.scope.ScopeViewActions#registerAction(edu.rice.cs.hpc.viewer.scope.ScopeViewActions.IActionType)
	 */
	protected void registerAction(IActionType type) {

		if (type == ActionType.ZoomIn || type == FlatAction.Flatten ) {
			stackActions.push(type);
		} else {
			stackActions.pop();
		}
	}

	@Override
	protected IMetricManager getMetricManager() {
		return (IMetricManager) myRootScope.getExperiment();
	}
}
