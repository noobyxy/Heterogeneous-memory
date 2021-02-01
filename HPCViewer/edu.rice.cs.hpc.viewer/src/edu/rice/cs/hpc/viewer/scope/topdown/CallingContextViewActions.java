package edu.rice.cs.hpc.viewer.scope.topdown;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;

import edu.rice.cs.hpc.data.experiment.scope.Scope;
import edu.rice.cs.hpc.viewer.scope.BaseScopeViewActions;


public class CallingContextViewActions extends BaseScopeViewActions 
{

	public CallingContextViewActions(Shell shell, IWorkbenchWindow window,
			Composite parent, CoolBar coolbar) 
	{
		super(shell, window, parent, coolbar);
	}
	
	public void checkStates(Scope nodeSelected)
	{
		super.checkStates(nodeSelected);
		((CallingContextActionsGUI)objActionsGUI).checkStates(nodeSelected);
	}

    /**
     * Each class has its own typical GUI creation
     */
	/*
	 * (non-Javadoc)
	 * @see edu.rice.cs.hpc.viewer.scope.BaseScopeViewActions#createGUI(org.eclipse.swt.widgets.Composite, org.eclipse.swt.widgets.CoolBar)
	 */
	@Override
	protected  Composite createGUI(Composite parent, CoolBar coolbar) 
	{
    	this.objActionsGUI = new CallingContextActionsGUI(this.objShell, 
    			this.objWindow, parent,  this, true);
    	return objActionsGUI.buildGUI(parent, coolbar);
	}
}
