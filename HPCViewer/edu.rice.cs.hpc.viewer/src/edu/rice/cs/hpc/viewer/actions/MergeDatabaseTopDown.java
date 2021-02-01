package edu.rice.cs.hpc.viewer.actions;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import edu.rice.cs.hpc.data.experiment.scope.RootScopeType;

public class MergeDatabaseTopDown extends MergeDatabase {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		return super.execute(event, RootScopeType.CallingContextTree);
	}

}
