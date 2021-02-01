package edu.rice.cs.hpc.viewer.editor;

import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;

import edu.rice.cs.hpc.data.experiment.Experiment;
/**
 * @author laksono
 *
 */
public class SourceCodeEditor extends TextEditor implements IViewerEditor {
	static public String ID = "edu.rice.cs.hpc.viewer.util.SourceCodeEditor";  
	private Experiment _experiment;
	
	public SourceCodeEditor() {
		super();
	}
	
	public void setExperiment(Experiment experiment) {
		this._experiment = experiment;
	}
	
	/**
	 * Disable editing the editor 
	 * 	 
	 * */
	public boolean validateEditorInputState() {
		// the inpus is never validated 
		return false;
	}

	/**
	 * Override the AbstractDecoratedTextEditor method to force to show
	 * the line number
	 */
	protected boolean isLineNumberRulerVisible() {
		this.getPreferenceStore().setValue(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_LINE_NUMBER_RULER, true);
		return true;
	}
	
	public void setPartNamePrefix(String partNamePrefix) {
		String partName = super.getPartName();
		if (partName.startsWith(partNamePrefix)) {
			// this method gets called each time you click the program scope but if the source file editor is 
			// already open we have already put the database number in the title and we do not want to do it again.
			return;
		}
		super.setPartName(partNamePrefix + partName);
		return;
	}
	
	/**
	 * Description copied from interface: ITextEditor
	 * Returns whether the text in this text editor can be changed by the user.
	 */
	public boolean isEditable() {
		return false;
	}

	public String getEditorPartName() {
		final FileStoreEditorInput input = (FileStoreEditorInput) this.getEditorInput();
		final String name = input.getName();
		return name;
	}

	public void setEditorPartName(String title) {
		this.setPartName(title);
		return;
	}

	public Experiment getExperiment() {
		return _experiment;
	}
}
