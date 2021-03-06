package edu.rice.cs.hpc.traceviewer.operation;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import edu.rice.cs.hpc.traceviewer.data.db.Frame;

public class WindowResizeOperation extends ZoomOperation 
{
	final static private IUndoContext context = new IUndoContext() {
		
		private final static String label = "WindoResizeOperation";
		
		@Override
		public boolean matches(IUndoContext context) {
			return context.getLabel() == label;
		}
		
		@Override
		public String getLabel() {
			return label;
		}
	};

	public WindowResizeOperation(Frame frame) {
		super("Resize", frame);
		
		// we don't want this operation in undo list
		removeContext(undoableContext);
		
		addContext(context);
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return Status.OK_STATUS;
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return execute(monitor, info);
	}

}
