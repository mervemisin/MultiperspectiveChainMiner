package org.processmining.plugins.multiperspectivechainminer.parameters;

import org.processmining.basicutils.parameters.impl.PluginParametersImpl;

public class BPMNParameters extends PluginParametersImpl {

	private String bpmnUri;
	private Boolean isControlFlowBPMNCheckBoxSelected;
	private Boolean isControlFlowDataBPMNCheckBoxSelected;
	private Boolean isControlFlowDataResourceBPMNCheckBoxSelected;
	private Boolean isControlFlowResourceBPMNCheckBoxSelected;


	public BPMNParameters() {
	}


	public Boolean isControlFlowBPMNCheckBoxSelected() {
		return isControlFlowBPMNCheckBoxSelected;
	}

	public void setControlFlowBPMNCheckBoxSelection(Boolean isControlFlowBPMNCheckBoxSelected) {
		this.isControlFlowBPMNCheckBoxSelected = isControlFlowBPMNCheckBoxSelected;
	}

	public Boolean isControlFlowDataBPMNCheckBoxSelected() {
		return isControlFlowDataBPMNCheckBoxSelected;
	}

	public void setControlFlowDataBPMNCheckBoxSelection(Boolean isControlFlowDataBPMNCheckBoxSelected) {
		this.isControlFlowDataBPMNCheckBoxSelected = isControlFlowDataBPMNCheckBoxSelected;
	}
	
	public Boolean isControlFlowDataResourceBPMNCheckBoxSelected() {
		return isControlFlowDataResourceBPMNCheckBoxSelected;
	}

	public void setControlFlowDataResourceBPMNCheckBoxSelection(Boolean isControlFlowDataResourceBPMNCheckBoxSelected) {
		this.isControlFlowDataResourceBPMNCheckBoxSelected = isControlFlowDataResourceBPMNCheckBoxSelected;
	}
	
	public Boolean isControlFlowResourceBPMNCheckBoxSelected() {
		return isControlFlowResourceBPMNCheckBoxSelected;
	}

	public void setControlFlowResourceBPMNCheckBoxSelection(Boolean isControlFlowResourceBPMNCheckBoxSelected) {
		this.isControlFlowResourceBPMNCheckBoxSelected = isControlFlowResourceBPMNCheckBoxSelected;
	}


	public String getBpmnUri() {
		return bpmnUri;
	}


	public void setBpmnUri(String bpmnUri) {
		this.bpmnUri = bpmnUri;
	}
	
}
