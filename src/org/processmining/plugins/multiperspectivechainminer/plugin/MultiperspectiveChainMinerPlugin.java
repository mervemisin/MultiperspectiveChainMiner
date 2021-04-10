package org.processmining.plugins.multiperspectivechainminer.plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.deckfour.xes.in.XesXmlParser;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.out.XMxmlSerializer;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.dataawareexplorer.explorer.DataAwareExplorer;
import org.processmining.dataawareexplorer.plugin.DataAwareExplorerPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginCategory;
import org.processmining.framework.util.ui.wizard.ListWizard;
import org.processmining.framework.util.ui.wizard.ProMWizardDisplay;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagramImpl;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.petrinets.PetriNetFileFormat;
import org.processmining.plugins.multiperspectivechainminer.MultiperspectiveChainMiner;
import org.processmining.plugins.multiperspectivechainminer.algorithms.OrganizationalMiner;
import org.processmining.plugins.multiperspectivechainminer.algorithms.Swimlanes;
import org.processmining.plugins.multiperspectivechainminer.bpmnapi.SendHTTPGetRequest;
import org.processmining.plugins.multiperspectivechainminer.bpmnapi.SendHTTPPostRequest;
import org.processmining.plugins.multiperspectivechainminer.bpmnapi.SendHTTPPutRequest;
import org.processmining.plugins.multiperspectivechainminer.parameters.BPMNParameters;
import org.processmining.plugins.multiperspectivechainminer.parameters.MultiPerspectiveChainMinerParameters;
import org.processmining.plugins.multiperspectivechainminer.ui.BPMNWizardStep;
import org.processmining.plugins.multiperspectivechainminer.ui.PerspectivesWizardStep;
import org.processmining.plugins.pnml.exporting.PnmlExportNetToEPNML;
import org.processmining.plugins.pnml.exporting.PnmlExportNetToPNML;

public class MultiperspectiveChainMinerPlugin {
        @Plugin(
                name = "Multiperspective Chain Miner Plugin", 
                parameterLabels = {}, 
                returnLabels = { "Multi-perspective BPMN"}, 
                returnTypes = { BPMNDiagramImpl.class
                				},
                userAccessible = true, 
                help = "Produces the Multi-perspective BPMN Files",
                categories = { PluginCategory.Discovery }
        )
        @UITopiaVariant(
                affiliation = "University of Hacettepe", 
                author = "Merve Nur TÝFTÝK", 
                email = "mervenur.tiftik@gmail.com"
        )
    	public static Object apply(UIPluginContext context, XLog log) {
        	Object returnObject = null;
    		List<ProMWizardStep<MultiPerspectiveChainMinerParameters>> wizStepList = new ArrayList<>();
    		PerspectivesWizardStep wizStep = new PerspectivesWizardStep(log);
    		wizStepList.add(wizStep);    		
 
    		ListWizard<MultiPerspectiveChainMinerParameters> listWizard = new ListWizard<>(wizStepList);
    		MultiPerspectiveChainMinerParameters params = ProMWizardDisplay.show(context, listWizard, new MultiPerspectiveChainMinerParameters());
    		if (params != null) {
    			    Object[] petrinetResult = null;
    				if(params.isControlFlowPerspectiveSelected()){
    					petrinetResult = mineForControlFlowPerspective(context, log, wizStep, params);
    				}
    				if(params.isResourcePerspectiveSelected()){
    					mineForResourcePerspective(log);
    				}
    				if(params.isDataPerspectiveSelected()){
	    			    mineForDataPerspective(context, log, petrinetResult);
    				}
    				try{
	    	    		List<ProMWizardStep<BPMNParameters>> bpmnWizStepList = new ArrayList<>();
	    	    		BPMNWizardStep bpmnWizStep = new BPMNWizardStep();
	    	    		bpmnWizStepList.add(bpmnWizStep);
	
	    	    		ListWizard<BPMNParameters> listBpmnWizard = new ListWizard<>(bpmnWizStepList);
	    	    		params.setBpmnParameters(ProMWizardDisplay.show(context, listBpmnWizard, new BPMNParameters()));
	    	    				
	    	    		returnObject = showSwimlanes(context,params);
    				}catch (NullPointerException e) {}
    		
    				return returnObject;
    		
    		} else {
    			context.getFutureResult(0).cancel(true);
    			return new Object[] { "eee", "fff","ggg", "ccc" };
    		}
    	}

		private static void mineForDataPerspective(UIPluginContext context, XLog log, Object[] petrinetResult) {
			DataAwareExplorerPlugin plugin = new DataAwareExplorerPlugin();
			DataAwareExplorer out = plugin.explore(context, (Petrinet)petrinetResult[0], log);
			JFrame frame = new JFrame();
			frame.getContentPane().add(out.getComponent());
			frame.pack();
			frame.setVisible(true);
		}

		private static void mineForResourcePerspective(XLog log) {
			try {
				XesXmlParser parser;
				XMxmlSerializer a = new XMxmlSerializer();
				File myTempFile = new File(System.getProperty("java.io.tmpdir"), "mxmlFile.mxml");
				FileOutputStream fos = new FileOutputStream(myTempFile);
			
				a.serialize(log, fos);
				OrganizationalMiner orgMiner = new OrganizationalMiner();
				orgMiner.mine(myTempFile.getAbsolutePath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private static Object[] mineForControlFlowPerspective(UIPluginContext context, XLog log,
				PerspectivesWizardStep wizStep, MultiPerspectiveChainMinerParameters params) {
			Object[] petrinetResult;
			petrinetResult = MultiperspectiveChainMiner.mine(context, log, wizStep.getEventClassifier(), params.getAlgorithm()); 
			File outputPNMLFile = new File(System.getProperty("java.io.tmpdir"), "controlFlowPerspective.pnml");
			Petrinet petriNetModel = (Petrinet) petrinetResult[0];
			PetriNetFileFormat format = PetriNetFileFormat.PNML;
			try {
				writeToFile(context, outputPNMLFile, petriNetModel, format);
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
			return petrinetResult;
		}
        
    	private static Object showSwimlanes(UIPluginContext context, MultiPerspectiveChainMinerParameters params){
    	   
    		Object returnObject=null;
    		String bpmnUri = params.getBpmnParameters().getBpmnUri();
    		String bpmnFileName = "output.bpmn";
    		if(params.isControlFlowPerspectiveSelected() &&  params.getBpmnParameters().isControlFlowBPMNCheckBoxSelected()){
    			SendHTTPPostRequest postRequest = new SendHTTPPostRequest();
    			SendHTTPGetRequest getRequest = new SendHTTPGetRequest();

    			try {
    				postRequest.callWithFile(bpmnUri, System.getProperty("java.io.tmpdir")+"/controlFlowPerspective.pnml", "1");
    				getRequest.callDownloadFile(bpmnUri + "?model=1", System.getProperty("java.io.tmpdir")+ "/output.bpmn");
    				bpmnFileName = "output.bpmn";
    				
       	    		Swimlanes swimlanes = new Swimlanes();
    	    		BPMNDiagram bpmnDiagram = swimlanes.show(bpmnFileName);
    	    		returnObject = bpmnDiagram;
    				context.getProvidedObjectManager().createProvidedObject(
    						"Control Perspective BPMN", bpmnDiagram, context);
					
    	    		//ProvidedObjectHelper.publish(context, "Control Flow in BPMN", bpmnDiagram, BPMNDiagram.class, true);
    	    		
    				
    			} catch (Exception e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}
    			
    		}
    		if(params.isControlFlowPerspectiveSelected() && params.isDataPerspectiveSelected() && params.getBpmnParameters().isControlFlowDataBPMNCheckBoxSelected()){
    			SendHTTPPostRequest postRequestcd = new SendHTTPPostRequest();
    			SendHTTPPutRequest putRequestcd = new SendHTTPPutRequest();
    			SendHTTPGetRequest getRequestcd = new SendHTTPGetRequest();

    			try {
    				postRequestcd.callWithFile(bpmnUri, System.getProperty("java.io.tmpdir")+"/controlFlowPerspective.pnml", "2");
    				putRequestcd.callWithFile(bpmnUri, System.getProperty("java.io.tmpdir")+"/dataPerspectiveOutput.pnml", "D", "2");
    				getRequestcd.callDownloadFile(bpmnUri + "?model=2", System.getProperty("java.io.tmpdir")+ "/dataPerspectiveOutput.bpmn");
    				getRequestcd.callDownloadFile(bpmnUri + "?model=2", System.getProperty("java.io.tmpdir")+ "/output.bpmn");
    				bpmnFileName = "output.bpmn";
    				
    	    		Swimlanes swimlanes = new Swimlanes();
    	    		BPMNDiagram bpmnDiagram = swimlanes.show(bpmnFileName);
    	    		returnObject = bpmnDiagram;
					context.getProvidedObjectManager().createProvidedObject(
    						"Control+Data Perspective BPMN", bpmnDiagram, context);
    	    		//ProvidedObjectHelper.publish(context, "Control Flow+Data in BPMN", bpmnDiagram, BPMNDiagram.class, true);
    				
    	 
    			} catch (Exception e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}
    		}
    		if(params.isControlFlowPerspectiveSelected() && params.isResourcePerspectiveSelected() &&params.getBpmnParameters().isControlFlowResourceBPMNCheckBoxSelected()){
    			SendHTTPPutRequest putRequestcr = new SendHTTPPutRequest();
    			SendHTTPPostRequest postRequestcr = new SendHTTPPostRequest();
    			SendHTTPGetRequest getRequestcr = new SendHTTPGetRequest();
    			try {
    				postRequestcr.callWithFile(bpmnUri, System.getProperty("java.io.tmpdir")+"/controlFlowPerspective.pnml", "4");
    				putRequestcr.callWithFile(bpmnUri, System.getProperty("java.io.tmpdir")+"/resourcePerspective.xml", "R", "4");
    				getRequestcr.callDownloadFile(bpmnUri + "?model=4", System.getProperty("java.io.tmpdir")+ "/output.bpmn");
    				bpmnFileName = "output.bpmn";
    				
       	    		Swimlanes swimlanes = new Swimlanes();
    	    		BPMNDiagram bpmnDiagram = swimlanes.show(bpmnFileName);
    	    		returnObject = bpmnDiagram;
					context.getProvidedObjectManager().createProvidedObject(
    						"Control+Resource Perspective BPMN", bpmnDiagram, context);
    	    		//ProvidedObjectHelper.publish(context.getParentContext(), "Control Flow+Resource in BPMN", bpmnDiagram, BPMNDiagram.class, true);
    	    		
    				
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}

    		}
    		
    		if(params.isControlFlowPerspectiveSelected() && params.isDataPerspectiveSelected() && params.isResourcePerspectiveSelected() &&params.getBpmnParameters().isControlFlowDataResourceBPMNCheckBoxSelected()){
    			SendHTTPPutRequest putRequestcdr = new SendHTTPPutRequest();
    			SendHTTPPostRequest postRequestcdr = new SendHTTPPostRequest();
    			SendHTTPGetRequest getRequestcdr = new SendHTTPGetRequest();

    			try {
    				postRequestcdr.callWithFile(bpmnUri, System.getProperty("java.io.tmpdir")+"/controlFlowPerspective.pnml", "3");
    				System.out.println(System.getProperty("java.io.tmpdir")+"/controlFlowPerspective.pnml");
    				putRequestcdr.callWithFile(bpmnUri, System.getProperty("java.io.tmpdir")+"/dataPerspectiveOutput.pnml", "D", "3");
    				putRequestcdr.callWithFile(bpmnUri, System.getProperty("java.io.tmpdir")+"/resourcePerspective.xml", "R", "3");
    				getRequestcdr.callDownloadFile(bpmnUri + "?model=3", System.getProperty("java.io.tmpdir")+ "/output.bpmn");
    				bpmnFileName = "output.bpmn";
    				
    	    		Swimlanes swimlanes = new Swimlanes();
    	    		BPMNDiagram bpmnDiagram = swimlanes.show(bpmnFileName);
    	    		returnObject = bpmnDiagram;
					context.getProvidedObjectManager().createProvidedObject(
    						"Control+Data+Resource Perspective BPMN", bpmnDiagram, context);
    	    		//ProvidedObjectHelper.publish(context.getParentContext(), "Control Flow+Data+Resource in BPMN", bpmnDiagram, BPMNDiagram.class, true);
    	    		
    				
    			} catch (Exception e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}
    		}

    		return returnObject;
    	}
        
        
        public static File createTempDirectory()
        	    throws IOException
        	{
        	    final File temp;

        	    temp = File.createTempFile("temp", Long.toString(System.nanoTime()));
        	    if(!(temp.delete()))
        	    {
        	        throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
        	    }

        	    if(!(temp.mkdir()))
        	    {
        	        throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
        	    }

        	    return (temp);
        	}
        
    	static void writeToFile(PluginContext context, File file, Petrinet object, PetriNetFileFormat format) throws IOException {
    		switch (format) {
    		case EPNML:
    		PnmlExportNetToEPNML exporterEPNML = new PnmlExportNetToEPNML();
    		exporterEPNML.exportPetriNetToEPNMLFile(context, object, file);
    		break;
    		case PNML:
    		default:
    		PnmlExportNetToPNML exporterPNML = new PnmlExportNetToPNML();
    		exporterPNML.exportPetriNetToPNMLFile(context, object, file);
    		break;
    		}
    		}

}
