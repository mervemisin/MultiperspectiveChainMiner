package org.processmining.plugins.multiperspectivechainminer.ui;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;
import org.processmining.framework.util.ui.widgets.ProMTextField;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;
import org.processmining.plugins.multiperspectivechainminer.parameters.BPMNParameters;

public class BPMNWizardStep extends ProMPropertiesPanel implements ProMWizardStep<BPMNParameters> {

	private static final long serialVersionUID = 7043633275971617176L;

	private static final String TITLE = "BPMN Perspective Selection";

	private ProMTextField bpmnUri;
	private JCheckBox controlFlowBPMNCheckBox;
	private JCheckBox controlFlowDataBPMNCheckBox;
	private JCheckBox controlFlowResourceBPMNCheckBox;
	private JCheckBox controlFlowDataResourceBPMNCheckBox;



	public BPMNWizardStep() {
		super(TITLE);
		
		controlFlowBPMNCheckBox = addCheckBox("Control", false);
		controlFlowDataBPMNCheckBox = addCheckBox("Control + Data", false);
		controlFlowResourceBPMNCheckBox = addCheckBox("Control + Resource", false);
		controlFlowDataResourceBPMNCheckBox = addCheckBox("Control + Data + Resource", false);

		bpmnUri = addTextField("BPMN Data Model URI", "http://localhost:0146/api/datamodel");

		
	}
	

	public BPMNParameters apply(BPMNParameters model, JComponent component) {
		if (canApply(model, component)) {
			BPMNWizardStep step = (BPMNWizardStep) component;
			model.setBpmnUri(step.getBpmnUri());
			model.setControlFlowBPMNCheckBoxSelection(step.isControlFlowPerspectiveBPMNSelected());
			model.setControlFlowDataBPMNCheckBoxSelection(step.isControlFlowDataPerspectiveBPMNSelected());
			model.setControlFlowDataResourceBPMNCheckBoxSelection(step.isControlFlowDataResourcePerspectiveBPMNSelected());
			model.setControlFlowResourceBPMNCheckBoxSelection(step.isControlFlowResourcePerspectiveBPMNSelected());
		}
		return model;
	}

	public boolean canApply(BPMNParameters model, JComponent component) {
		return component instanceof BPMNWizardStep;
	}

	public JComponent getComponent(BPMNParameters model) {
		return this;
	}

	public Boolean isControlFlowPerspectiveBPMNSelected() {
		return controlFlowBPMNCheckBox.isSelected();
	}
	
	public Boolean isControlFlowDataPerspectiveBPMNSelected() {
		return controlFlowDataBPMNCheckBox.isSelected();
	}
	
	public Boolean isControlFlowDataResourcePerspectiveBPMNSelected() {
		return controlFlowDataResourceBPMNCheckBox.isSelected();
	}
	
	public Boolean isControlFlowResourcePerspectiveBPMNSelected() {
		return controlFlowResourceBPMNCheckBox.isSelected();
	}
	
	public String getBpmnUri() {
		return bpmnUri.getText();
	}

	public String getTitle() {
		return TITLE;
	}

}