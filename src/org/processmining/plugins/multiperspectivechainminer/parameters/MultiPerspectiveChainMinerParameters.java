package org.processmining.plugins.multiperspectivechainminer.parameters;

import org.deckfour.xes.classification.XEventClassifier;
import org.processmining.basicutils.parameters.impl.PluginParametersImpl;

public class MultiPerspectiveChainMinerParameters extends PluginParametersImpl {

	private ControlFlowAlgorithm algorithm;
	private XEventClassifier classifier;
	private Boolean isControlFlowPerspectiveSelected;
	private Boolean isResourcePerspectiveSelected;
	private Boolean isDataPerspectiveSelected;
	private BPMNParameters bpmnParameters = new BPMNParameters();


	public MultiPerspectiveChainMinerParameters() {
	}

	public MultiPerspectiveChainMinerParameters(final ControlFlowAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	public ControlFlowAlgorithm getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(ControlFlowAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	public XEventClassifier getClassifier() {
		return classifier;
	}

	public void setClassifier(XEventClassifier classifier) {
		this.classifier = classifier;
	}

	public Boolean isResourcePerspectiveSelected() {
		return isResourcePerspectiveSelected;
	}

	public void setResourcePerspectiveSelection(Boolean isResourcePerspectiveSelected) {
		this.isResourcePerspectiveSelected = isResourcePerspectiveSelected;
	}

	public Boolean isControlFlowPerspectiveSelected() {
		return isControlFlowPerspectiveSelected;
	}

	public void setControlFlowPerspectiveSelection(Boolean isControlFlowPerspectiveSelected) {
		this.isControlFlowPerspectiveSelected = isControlFlowPerspectiveSelected;
	}

	public Boolean isDataPerspectiveSelected() {
		return isDataPerspectiveSelected;
	}

	public void setDataPerspectiveSelection(Boolean isDataPerspectiveSelected) {
		this.isDataPerspectiveSelected = isDataPerspectiveSelected;
	}

	public BPMNParameters getBpmnParameters() {
		return bpmnParameters;
	}

	public void setBpmnParameters(BPMNParameters bpmnParameters) {
		this.bpmnParameters = bpmnParameters;
	}

}
