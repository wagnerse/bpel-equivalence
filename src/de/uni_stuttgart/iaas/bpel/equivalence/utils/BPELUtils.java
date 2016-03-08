package de.uni_stuttgart.iaas.bpel.equivalence.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.BPELPlugin;
import org.eclipse.bpel.model.Link;
import org.eclipse.bpel.model.Process;
import org.eclipse.bpel.model.Source;
import org.eclipse.bpel.model.Target;
import org.eclipse.bpel.model.resource.BPELResource;
import org.eclipse.bpel.model.resource.BPELResourceFactoryImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.wst.wsdl.internal.util.WSDLResourceFactoryImpl;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;

@SuppressWarnings("restriction")
public class BPELUtils {

	public static Process readProcessFromFile(String path) {
		URI uri = URI.createFileURI(path);

		try {
			@SuppressWarnings("unused")
			BPELPlugin bpelPlugin = new BPELPlugin();
			ResourceSet rs = new ResourceSetImpl();
			rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("bpel", new BPELResourceFactoryImpl());
			rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("wsdl", new WSDLResourceFactoryImpl());
			rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xsd", new XSDResourceFactoryImpl());

			BPELResource resource = (BPELResource) rs.createResource(uri);
			resource.setOptionUseNSPrefix(false);
			resource.load(null);

			Process process = resource.getProcess();

			return process;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static List<Activity> getSources(Link link) {
		List<Activity> result = new ArrayList<Activity>();
		for (Source src: link.getSources()) {
			result.add(src.getActivity());
		}
		return result;
	}

	public static List<Activity> getTargets(Link link) {
		List<Activity> result = new ArrayList<Activity>();
		for (Target target: link.getTargets()) {
			result.add(target.getActivity());
		}
		return result;	
	}

}
