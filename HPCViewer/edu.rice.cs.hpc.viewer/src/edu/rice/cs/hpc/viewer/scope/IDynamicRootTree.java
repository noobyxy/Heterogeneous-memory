package edu.rice.cs.hpc.viewer.scope;

import edu.rice.cs.hpc.data.experiment.Experiment;
import edu.rice.cs.hpc.data.experiment.scope.RootScope;

/*******************************************
 * 
 * Interface to create a tree
 *
 *******************************************/
public interface IDynamicRootTree {
	public RootScope createTree(Experiment experiment);
}
