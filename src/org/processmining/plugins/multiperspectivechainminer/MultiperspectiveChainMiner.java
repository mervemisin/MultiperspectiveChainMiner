package org.processmining.plugins.multiperspectivechainminer;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.plugins.multiperspectivechainminer.algorithms.MultiperspectiveChainMinerPetrinetFactory;
import org.processmining.plugins.multiperspectivechainminer.parameters.ControlFlowAlgorithm;

public class MultiperspectiveChainMiner {

	public MultiperspectiveChainMiner() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public static Object[] mine(UIPluginContext context, XLog log, XEventClassifier eventClassifier,
			ControlFlowAlgorithm algorithm) {
		Object[] object = MultiperspectiveChainMinerPetrinetFactory.createPetrinet(context, log, eventClassifier, algorithm);
		if (context.getProgress().isCancelled()) {
			context.getFutureResult(0).cancel(true);
			return new Object[] { "ccc", "ddd", "eee" };
		}
		return object;
	}

}
