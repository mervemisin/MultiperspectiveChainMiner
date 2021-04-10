package org.processmining.plugins.multiperspectivechainminer.algorithms;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.processmining.contexts.cli.CLIContext;
import org.processmining.contexts.cli.CLIPluginContext;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagramFactory;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.models.graphbased.directed.bpmn.elements.Swimlane;
import org.processmining.plugins.bpmn.BPMNVisualization;
import org.processmining.plugins.bpmn.Bpmn;
import org.processmining.plugins.bpmn.parameters.BpmnSelectDiagramParameters;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;


public class Swimlanes{

	public Swimlanes() {
	}
	
	public BPMNDiagram show(String bpmnFileName){
		File file = new File(System.getProperty("java.io.tmpdir") + bpmnFileName);
		InputStream input;
		Bpmn bpmn = null;
		try {
			input = getInputStream(file);
			bpmn = importBpmnFromStream(input, System.getProperty("java.io.tmpdir") + bpmnFileName, file.length());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BpmnSelectDiagramParameters parameters = new BpmnSelectDiagramParameters();
		
		BPMNDiagram newDiagram = BPMNDiagramFactory.newBPMNDiagram("");
		Map<String, BPMNNode> id2node = new HashMap<String, BPMNNode>();
		Map<String, Swimlane> id2lane = new HashMap<String, Swimlane>();
		if (parameters.getDiagram() == BpmnSelectDiagramParameters.NODIAGRAM) {
			bpmn.unmarshall(newDiagram, id2node, id2lane);
		} else {
			Collection<String> elements = parameters.getDiagram().getElements();
			bpmn.unmarshall(newDiagram, elements, id2node, id2lane);
		}
		CLIContext context = new CLIContext();
		CLIPluginContext pluginContext = new CLIPluginContext(context, "test");	
		
		JComponent out = BPMNVisualization.visualize(pluginContext, newDiagram);
		JFrame frame = new JFrame();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.getContentPane().add(out);
		frame.pack();
		frame.setVisible(false);
		
		return newDiagram;
	}
	
	private static Bpmn importBpmnFromStream(InputStream input, String filename, long fileSizeInBytes)
			throws Exception {
		/*
		 * Get an XML pull parser.
		 */
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xpp = factory.newPullParser();
		/*
		 * Initialize the parser on the provided input.
		 */
		xpp.setInput(input, null);
		/*
		 * Get the first event type.
		 */
		int eventType = xpp.getEventType();
		/*
		 * Create a fresh PNML object.
		 */
		Bpmn bpmn = new Bpmn();

		/*
		 * Skip whatever we find until we've found a start tag.
		 */
		while (eventType != XmlPullParser.START_TAG) {
			eventType = xpp.next();
		}
		/*
		 * Check whether start tag corresponds to PNML start tag.
		 */
		if (xpp.getName().equals(bpmn.tag)) {
			/*
			 * Yes it does. Import the PNML element.
			 */
			bpmn.importElement(xpp, bpmn);
		} else {
			/*
			 * No it does not. Return null to signal failure.
			 */
			bpmn.log(bpmn.tag, xpp.getLineNumber(), "Expected " + bpmn.tag + ", got " + xpp.getName());
		}
		return bpmn;
	}
	
	private static InputStream getInputStream(File file) throws Exception {
		return new FileInputStream(file);
	}

}
