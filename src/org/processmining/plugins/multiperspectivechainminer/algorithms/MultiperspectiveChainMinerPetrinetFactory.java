package org.processmining.plugins.multiperspectivechainminer.algorithms;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.alphaminer.algorithms.AlphaClassicMinerImpl;
import org.processmining.alphaminer.algorithms.AlphaPlusMinerImpl;
import org.processmining.alphaminer.algorithms.AlphaPlusPlusMinerImproved1Impl;
import org.processmining.alphaminer.algorithms.AlphaRobustMinerImpl;
import org.processmining.alphaminer.parameters.AlphaMinerParameters;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.util.Pair;
import org.processmining.hybridilpminer.plugins.HybridILPMinerPlugin;
import org.processmining.models.connections.petrinets.behavioral.InitialMarkingConnection;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.heuristics.HeuristicsNet;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.InductiveMiner.mining.MiningParameters;
import org.processmining.plugins.InductiveMiner.plugins.IMPetriNet;
import org.processmining.plugins.InductiveMiner.plugins.IMProcessTree;
import org.processmining.plugins.cnet.mining.CNetMiner;
import org.processmining.plugins.flowerMiner.FlowerMiner;
import org.processmining.plugins.heuristicsnet.miner.heuristics.converter.HeuristicsNetToPetriNetConverter;
import org.processmining.plugins.heuristicsnet.miner.heuristics.miner.FlexibleHeuristicsMinerPlugin;
import org.processmining.plugins.heuristicsnet.miner.heuristics.miner.settings.HeuristicsMinerSettings;
import org.processmining.plugins.multiperspectivechainminer.parameters.ControlFlowAlgorithm;
import org.processmining.processtree.ProcessTree;
import org.processmining.processtree.conversion.ProcessTree2Petrinet;
import org.processmining.processtree.conversion.ProcessTree2Petrinet.PetrinetWithMarkings;

public class MultiperspectiveChainMinerPetrinetFactory {

	public static Object[] createPetrinet(
			PluginContext context, XLog log, XEventClassifier classifier, ControlFlowAlgorithm algorithm) {
		switch (algorithm) {
			case CLASSIC :
				AlphaMinerParameters paramClassic = new org.processmining.alphaminer.parameters.AlphaMinerParameters(org.processmining.alphaminer.parameters.AlphaVersion.CLASSIC);
				Pair<Petrinet, Marking> pairsClassic = AlphaClassicMinerImpl.run(log, classifier, paramClassic);
				context.getConnectionManager()
				.addConnection(new InitialMarkingConnection(pairsClassic.getFirst(), pairsClassic.getSecond()));
				return new Object[] { pairsClassic.getFirst(), pairsClassic.getSecond(), pairsClassic.getSecond()};
			case PLUS :
				AlphaMinerParameters paramPlus = new org.processmining.alphaminer.parameters.AlphaMinerParameters(org.processmining.alphaminer.parameters.AlphaVersion.PLUS);
				Pair<Petrinet, Marking> pairsPlus = AlphaPlusMinerImpl.run(log, classifier, paramPlus);
				context.getConnectionManager()
				.addConnection(new InitialMarkingConnection(pairsPlus.getFirst(), pairsPlus.getSecond()));
				return new Object[] { pairsPlus.getFirst(), pairsPlus.getSecond(), pairsPlus.getSecond() };
			case PLUS_PLUS :
				AlphaMinerParameters paramPlusPlus = new org.processmining.alphaminer.parameters.AlphaMinerParameters(org.processmining.alphaminer.parameters.AlphaVersion.PLUS_PLUS);
				Pair<Petrinet, Marking> pairsPlusPlus = AlphaPlusPlusMinerImproved1Impl.run(log, classifier, paramPlusPlus);
				context.getConnectionManager()
				.addConnection(new InitialMarkingConnection(pairsPlusPlus.getFirst(), pairsPlusPlus.getSecond()));
				return new Object[] { pairsPlusPlus.getFirst(), pairsPlusPlus.getSecond(), pairsPlusPlus.getSecond() };
			case ROBUST :
				AlphaMinerParameters paramRobust = new org.processmining.alphaminer.parameters.AlphaRobustMinerParameters(org.processmining.alphaminer.parameters.AlphaVersion.ROBUST);
				Pair<Petrinet, Marking> pairsRobust = AlphaRobustMinerImpl.run(context, log, classifier, paramRobust);
				context.getConnectionManager()
				.addConnection(new InitialMarkingConnection(pairsRobust.getFirst(), pairsRobust.getSecond()));
				return new Object[] { pairsRobust.getFirst(), pairsRobust.getSecond(), pairsRobust.getSecond() };
			case INDUCTIVE :
				MiningParameters params = new org.processmining.plugins.InductiveMiner.mining.MiningParametersIMi();
				Object[] inductiveResult = IMPetriNet.minePetriNet(context, log, params);
				context.getConnectionManager()
				.addConnection(new InitialMarkingConnection((Petrinet)inductiveResult[0], (Marking)inductiveResult[1]));
				return inductiveResult;
			case BASED_EXPRESS :
				Object[] hybridResult = HybridILPMinerPlugin.applyExpress(context, log, classifier);
				context.getConnectionManager()
				.addConnection(new InitialMarkingConnection((Petrinet)hybridResult[0], (Marking)hybridResult[1]));
				return hybridResult;
			case HEURISTICS :
				HeuristicsMinerSettings settings = new HeuristicsMinerSettings();
				settings.setClassifier(classifier);
				HeuristicsNet heuristicsNet = FlexibleHeuristicsMinerPlugin.run(context, log, settings);
				Object[] heuristicsResult = HeuristicsNetToPetriNetConverter.converter(context, heuristicsNet);
				context.getConnectionManager()
				.addConnection(new InitialMarkingConnection((Petrinet)heuristicsResult[0], (Marking)heuristicsResult[1]));
				return heuristicsResult;
			case CNETMINER :
				CNetMiner miner = new CNetMiner();
				Object[] object = miner.mine(log, classifier);
				Petrinet petri = (Petrinet) object[1];
				Marking marking = (Marking)object[2];
				context.getConnectionManager()
				.addConnection(new InitialMarkingConnection(petri, marking));
				return new Object[] { petri, marking, marking};
			case INDUCTIVE_PROCESS_TREE :
				ProcessTree processTree = IMProcessTree.mineProcessTree(log);
				try {
					PetrinetWithMarkings petriWithMarkings = ProcessTree2Petrinet.convert(processTree);
					context.getConnectionManager()
					.addConnection(new InitialMarkingConnection(petriWithMarkings.petrinet, petriWithMarkings.initialMarking));
					return new Object[] { petriWithMarkings.petrinet, petriWithMarkings.initialMarking, petriWithMarkings.finalMarking};
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			case FLOWER_MINER :
				FlowerMiner flowerMiner = new FlowerMiner();
				Object[] flowerResult = flowerMiner.mineDefaultPetrinet(context, log);
				context.getConnectionManager()
				.addConnection(new InitialMarkingConnection((Petrinet)flowerResult[0], (Marking)flowerResult[1]));
				return flowerResult;
			default :
				MiningParameters paramsInd = new org.processmining.plugins.InductiveMiner.mining.MiningParametersIMi();
				Object[] inductiveRes = IMPetriNet.minePetriNet(context, log, paramsInd);
				context.getConnectionManager()
				.addConnection(new InitialMarkingConnection((Petrinet)inductiveRes[0], (Marking)inductiveRes[1]));
				return inductiveRes;
		}
	}

}
