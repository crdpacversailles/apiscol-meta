package fr.ac_versailles.crdp.apiscol.meta.representations;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import fr.ac_versailles.crdp.apiscol.database.DBAccessException;
import fr.ac_versailles.crdp.apiscol.meta.dataBaseAccess.IResourceDataHandler;
import fr.ac_versailles.crdp.apiscol.meta.fileSystemAccess.MetadataNotFoundException;
import fr.ac_versailles.crdp.apiscol.meta.fileSystemAccess.ResourceDirectoryInterface;
import fr.ac_versailles.crdp.apiscol.meta.searchEngine.ISearchEngineResultHandler;
import fr.ac_versailles.crdp.apiscol.utils.HTMLUtils;
import fr.ac_versailles.crdp.apiscol.utils.XMLUtils;

public class XHTMLRepresentationBuilder extends
		AbstractRepresentationBuilder<String> {

	private static DocumentBuilderFactory dbFactory = DocumentBuilderFactory
			.newInstance();
	private static DocumentBuilder dBuilder;
	static {
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private XMLRepresentationBuilder innerBuilder;

	public XHTMLRepresentationBuilder() {
		innerBuilder = new XMLRepresentationBuilder();
	}

	@Override
	public MediaType getMediaType() {
		return MediaType.TEXT_HTML_TYPE;
	}

	@Override
	public String getMetadataRepresentation(String realPath, UriInfo uriInfo,
			String apiscolInstanceName, String metadataId,
			boolean includeDescription, Map<String, String> params,
			IResourceDataHandler resourceDataHandler, String editUri)
			throws MetadataNotFoundException {

		File xmlFile = ResourceDirectoryInterface.getMetadataFile(metadataId);

		Document metadataDocument = null;

		try {
			metadataDocument = dBuilder.parse(xmlFile);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Node result = XMLUtils.xsltTransform(
				new StringBuilder().append(realPath)
						.append("/xsl/metadataXMLToHTMLTransformer.xsl")
						.toString(), metadataDocument, params);
		return HTMLUtils.WrapInHTML5Headers((Document) result);

	}

	@Override
	public String getMetadataSuccessfulDestructionReport(Object realPath,
			UriInfo uriInfo, String apiscolInstanceName, String metadataId,
			String warnings) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSuccessfullOptimizationReport(String requestedFormat,
			UriInfo uriInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSuccessfulGlobalDeletionReport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSuccessfulRecoveryReport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMetadataSnippetRepresentation(String realPath,
			UriInfo uriInfo, String apiscolInstanceName, String metadataId,
			String version) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCompleteMetadataListRepresentation(String realPath,
			UriInfo uriInfo, String apiscolInstanceLabel,
			String apiscolInstanceName, int start, int rows,
			boolean includeDescription,
			IResourceDataHandler resourceDataHandler, String editUri,
			String version) throws DBAccessException {
		// TODO Auto-generated method stub
		return XMLUtils.XMLToString(innerBuilder
				.getCompleteMetadataListRepresentation(realPath, uriInfo,
						apiscolInstanceName, apiscolInstanceLabel, start, rows,
						includeDescription, resourceDataHandler, editUri,
						version));
	}

	@Override
	public String selectMetadataFollowingCriterium(String realPath,
			UriInfo uriInfo, String apiscolInstanceLabel,
			String apiscolInstanceName, ISearchEngineResultHandler handler,
			int start, int rows, boolean includeDescription,
			IResourceDataHandler resourceDataHandler, String editUri,
			String version) throws NumberFormatException, DBAccessException {
		Document xmlResponse = innerBuilder.selectMetadataFollowingCriterium(
				realPath, uriInfo, apiscolInstanceName, apiscolInstanceLabel,
				handler, start, rows, true, resourceDataHandler, editUri,
				version);
		Node result = XMLUtils.xsltTransform(realPath
				+ "/xsl/metadataListXMLToHTMLTransformer.xsl", xmlResponse,
				Collections.<String, String> emptyMap());
		return HTMLUtils.WrapInHTML5Headers((Document) result);

	}
}
