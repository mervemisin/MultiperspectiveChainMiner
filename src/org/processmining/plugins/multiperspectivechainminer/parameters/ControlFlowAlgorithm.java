package org.processmining.plugins.multiperspectivechainminer.parameters;

public enum ControlFlowAlgorithm {
	CLASSIC("Alpha"), PLUS("Alpha+"), PLUS_PLUS("Alpha++"), ROBUST("AlphaRobust"), INDUCTIVE("InductiveMiner"), BASED_EXPRESS("ILP-BasedExpress"), HEURISTICS("HeuristicsMiner"), CNETMINER("CNet Miner"), INDUCTIVE_PROCESS_TREE("Process Tree Inductive Miner"), FLOWER_MINER("Flower Miner");

	private final String name;

	private ControlFlowAlgorithm(final String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

}
