package org.processmining.plugins.multiperspectivechainminer.ui;

import java.util.EnumSet;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.framework.boot.Boot;
import org.processmining.framework.plugin.annotations.PluginLevel;
import org.processmining.framework.util.ui.widgets.ProMComboBox;
import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;
import org.processmining.plugins.multiperspectivechainminer.parameters.ControlFlowAlgorithm;
import org.processmining.plugins.multiperspectivechainminer.parameters.DefaultEventClassifier;
import org.processmining.plugins.multiperspectivechainminer.parameters.MultiPerspectiveChainMinerParameters;

public class PerspectivesWizardStep extends ProMPropertiesPanel implements ProMWizardStep<MultiPerspectiveChainMinerParameters> {

	private static final long serialVersionUID = 7043633275971617176L;

	private static final String TITLE = "Start Multiperspective Miner";

	private final ProMComboBox<XEventClassifier> classifierList;

	private final ProMComboBox<ControlFlowAlgorithm> algorithmList;
	
	private JCheckBox controlFlowPerspectiveCheckBox;
	private JCheckBox dataPerspectiveCheckBox;
	private JCheckBox resourcePerspectiveCheckBox;


	public PerspectivesWizardStep(XLog log) {
		super(TITLE);
		controlFlowPerspectiveCheckBox = addCheckBox("Control Flow Perspective", true);
		controlFlowPerspectiveCheckBox.setEnabled(false);
		List<XEventClassifier> classifiers = log.getClassifiers();
		if (classifiers.isEmpty()) {
			classifiers.add(DefaultEventClassifier.get());
		}
		classifierList = addComboBox("Event Classifier", classifiers);
		if (Boot.PLUGIN_LEVEL_THRESHOLD.getValue() >= PluginLevel.PeerReviewed.getValue()) {
			algorithmList = addComboBox("Algorithm",
					EnumSet.of(ControlFlowAlgorithm.CLASSIC, ControlFlowAlgorithm.PLUS, ControlFlowAlgorithm.PLUS_PLUS, ControlFlowAlgorithm.ROBUST, ControlFlowAlgorithm.INDUCTIVE, ControlFlowAlgorithm.BASED_EXPRESS, ControlFlowAlgorithm.HEURISTICS, ControlFlowAlgorithm.CNETMINER, ControlFlowAlgorithm.INDUCTIVE_PROCESS_TREE, ControlFlowAlgorithm.FLOWER_MINER));
		} else {
			algorithmList = addComboBox("Algorithm", EnumSet.of(ControlFlowAlgorithm.CLASSIC, ControlFlowAlgorithm.PLUS,
					ControlFlowAlgorithm.PLUS_PLUS, ControlFlowAlgorithm.ROBUST, ControlFlowAlgorithm.INDUCTIVE, ControlFlowAlgorithm.BASED_EXPRESS, ControlFlowAlgorithm.HEURISTICS, ControlFlowAlgorithm.CNETMINER, ControlFlowAlgorithm.INDUCTIVE_PROCESS_TREE, ControlFlowAlgorithm.FLOWER_MINER));
		}
		
		dataPerspectiveCheckBox = addCheckBox("Data Perspective", false);
		resourcePerspectiveCheckBox = addCheckBox("Resource Perspective", false);


		
	}
	
	public MultiPerspectiveChainMinerParameters apply(MultiPerspectiveChainMinerParameters model, JComponent component) {
		if (canApply(model, component)) {
			PerspectivesWizardStep step = (PerspectivesWizardStep) component;
			model.setAlgorithm(step.getAlgorithm());
			model.setClassifier(step.getEventClassifier());
			model.setControlFlowPerspectiveSelection(step.isControlFlowPerspectiveSelected());
			model.setDataPerspectiveSelection(step.isDataPerspectiveSelected());
			model.setResourcePerspectiveSelection(step.isResourcePerspectiveSelected());

		}
		return model;
	}

	public boolean canApply(MultiPerspectiveChainMinerParameters model, JComponent component) {
		return component instanceof PerspectivesWizardStep;
	}

	public JComponent getComponent(MultiPerspectiveChainMinerParameters model) {
		return this;
	}

	public XEventClassifier getEventClassifier() {
		return (XEventClassifier) classifierList.getSelectedItem();
	}

	public String getTitle() {
		return TITLE;
	}

	public ControlFlowAlgorithm getAlgorithm() {
		return (ControlFlowAlgorithm) algorithmList.getSelectedItem();
	}
	
	
	public Boolean isControlFlowPerspectiveSelected() {
		return controlFlowPerspectiveCheckBox.isSelected();
	}
	
	public Boolean isDataPerspectiveSelected() {
		return dataPerspectiveCheckBox.isSelected();
	}
	
	public Boolean isResourcePerspectiveSelected() {
		return resourcePerspectiveCheckBox.isSelected();
	}
	
}
