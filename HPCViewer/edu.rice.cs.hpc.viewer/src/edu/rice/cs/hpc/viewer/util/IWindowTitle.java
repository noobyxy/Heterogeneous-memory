package edu.rice.cs.hpc.viewer.util;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;

public interface IWindowTitle {
	public String setTitle(IWorkbenchWindow window, IViewPart view);
	public String getWindowTitle(IWorkbenchWindow window);
	public String setEditorTitle(IWorkbenchWindow window, IEditorPart editorPart);

}
