package org.processmining.plugins.multiperspectivechainminer.ui;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;
import org.processmining.plugins.multiperspectivechainminer.parameters.MultiPerspectiveChainMinerParameters;

public class ResourcePerspectiveWizardStep extends ProMPropertiesPanel implements ProMWizardStep<MultiPerspectiveChainMinerParameters> {

	private static final long serialVersionUID = 7043633275971617176L;

	private static final String TITLE = "Resource Perspective Selection";

	private JCheckBox resourcePersectiveCheckBox;

	public ResourcePerspectiveWizardStep() {
		super(TITLE);
		resourcePersectiveCheckBox = addCheckBox("Resource Perspective", false);
	}
	

	public MultiPerspectiveChainMinerParameters apply(MultiPerspectiveChainMinerParameters model, JComponent component) {
		if (canApply(model, component)) {
			ResourcePerspectiveWizardStep step = (ResourcePerspectiveWizardStep) component;
			model.setResourcePerspectiveSelection(step.isResourcePerspectiveSelected());
		}
		return model;
	}

	public boolean canApply(MultiPerspectiveChainMinerParameters model, JComponent component) {
		return component instanceof ResourcePerspectiveWizardStep;
	}

	public JComponent getComponent(MultiPerspectiveChainMinerParameters model) {
		return this;
	}

	public Boolean isResourcePerspectiveSelected() {
		return resourcePersectiveCheckBox.isSelected();
	}

	public String getTitle() {
		return TITLE;
	}
}