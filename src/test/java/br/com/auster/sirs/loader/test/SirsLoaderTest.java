package br.com.auster.sirs.loader.test;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import br.com.auster.sirs.loader.SirsLoader;

public class SirsLoaderTest extends TestCase {
	public static final String tagMapFile = "src/test/sirs-tag-map.properties";
	SirsLoader loader = new SirsLoader();

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public final void testConfigure() {
		try {
			loader.configure(this.getLoaderConfig(tagMapFile));
		} catch (Exception e) {
			Assert.fail("Error configuring SIRS Loader");
			e.printStackTrace();
		}
	}

	/**
	 * Creating configuration element for loader
	 * @param tagMapFile
	 * @return
	 */
	private final Element getLoaderConfig(String tagMapFile) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();
			
			Element config = doc.createElementNS("","loader-config");
			config.setAttributeNS("", "tag-map-file", tagMapFile);
			
			return config;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}

}
