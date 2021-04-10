package org.processmining.plugins.multiperspectivechainminer.algorithms;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.processmining.exporting.orgmodel.OrgModelExport;
import org.processmining.framework.log.LogFile;
import org.processmining.framework.log.LogReader;
import org.processmining.framework.log.LogReaderFactory;
import org.processmining.framework.models.orgmodel.OrgModel;
import org.processmining.framework.plugin.ProvidedObject;
import org.processmining.framework.ui.OpenLogSettings;
import org.processmining.framework.ui.slicker.logdialog.SlickerOpenLogSettings;
import org.processmining.mining.organizationmining.OrgMiner;
import org.processmining.mining.organizationmining.OrgMiningResult;

public class OrganizationalMiner{

	private LogReader logReader = null;
	private OrgModel orgModel = null;
	
	public OrganizationalMiner() {
	}
	
	public void mine(String xlogFilePath) throws IOException{
		openLog(xlogFilePath);
		mine();
		export();
	}
	
//	public static LogReaderFacade applyMapping(PetrinetLogMapper mapper, XLog log) {
//		XLog xlog = (XLog) log.clone();
//		for (XTrace xtrace : xlog) {
//			for (int i = 0; i < xtrace.size(); i++) {
//				if (!mapper.eventHasTransition(xtrace.get(i))) {
//					xtrace.remove(i);
//					i--;
//				}
//			}
//		}
//		return new LogReaderFacade(xlog);
//	}
	
	public void openLog(String xlogFilePath) {
		
		
		
		
		LogFile logFile = LogFile.getInstance(xlogFilePath);
		final OpenLogSettings settings = new SlickerOpenLogSettings(logFile);
		try {
			logReader = LogReaderFactory.createInstance(settings.getLogFilter(), logFile);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	public void mine() {
		
		if (logReader != null) {
			OrgMiner miningPlugin = new OrgMiner();
			OrgMiningResult result = (OrgMiningResult) miningPlugin.mine(logReader, 0);
			orgModel = result.getOrgModel();
		} else {
			System.err.println("No log reader could be constructed.");
		}
	}
	
	public void export() {
		
		if (orgModel != null) {
			// Export the Petri net as PNML.
			OrgModelExport exportPlugin = new OrgModelExport();
			Object[] objects = new Object[] {orgModel};
			ProvidedObject object = new ProvidedObject("temp", objects);
			FileOutputStream outputStream = null;
			try {
				File resourceTempFile = new File(System.getProperty("java.io.tmpdir"), "resourcePerspective.xml");
				
				outputStream = new FileOutputStream(resourceTempFile);
				exportPlugin.export(object, (outputStream != null ? outputStream : System.out));
			} catch (Exception e) {
				System.err.println("Unable to write to file: " + e.toString());
			}
		} else {
			System.err.println("No Petri net could be constructed.");
		}
	}

	

}
