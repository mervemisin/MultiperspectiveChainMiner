package org.processmining.plugins.multiperspectivechainminer.ui;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;
import org.processmining.plugins.multiperspectivechainminer.parameters.MultiPerspectiveChainMinerParameters;

public class DataPerspectiveWizardStep extends ProMPropertiesPanel implements ProMWizardStep<MultiPerspectiveChainMinerParameters> {

	private static final long serialVersionUID = 7043633275971617176L;

	private static final String TITLE = "Data Perspective Selection";

	private JCheckBox dataPerspectiveCheckBox;

	public DataPerspectiveWizardStep() {
		super(TITLE);
		dataPerspectiveCheckBox = addCheckBox("Data Perspective", false);
	}
	

	public MultiPerspectiveChainMinerParameters apply(MultiPerspectiveChainMinerParameters model, JComponent component) {
		if (canApply(model, component)) {
			DataPerspectiveWizardStep step = (DataPerspectiveWizardStep) component;
			model.setDataPerspectiveSelection(step.isDataPerspectiveSelected());
		}
		return model;
	}

	public boolean canApply(MultiPerspectiveChainMinerParameters model, JComponent component) {
		return component instanceof DataPerspectiveWizardStep;
	}

	public JComponent getComponent(MultiPerspectiveChainMinerParameters model) {
		return this;
	}

	public Boolean isDataPerspectiveSelected() {
		return dataPerspectiveCheckBox.isSelected();
	}

	public String getTitle() {
		return TITLE;
	}
}