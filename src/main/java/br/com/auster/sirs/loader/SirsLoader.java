/*
 * Copyright (c) 2004 Auster Solutions. All Rights Reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Created on Jan 28, 2005
 */
package br.com.auster.sirs.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import br.com.auster.common.io.IOUtils;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.om.invoice.InvoiceObjectModelLoader;

/**
 * <p><b>Title:</b> SirsLoader</p>
 * <p><b>Description:</b> A content handler that is able to populate the Invoice OM</p>
 * <p><b>Copyright:</b> Copyright (c) 2004</p>
 * <p><b>Company:</b> Auster Solutions</p>
 *
 * @author etirelli
 * @version $Id: SirsLoader.java 55 2005-09-07 13:30:27Z etirelli $
 */
public class SirsLoader implements ContentHandler, InvoiceObjectModelLoader {
	public static final String CONFIG_LOADER_ELEMENT      = "loader-config";
	public static final String CONFIG_LOADER_ATTR_MAPFILE = "tag-map-file";
	public static final String CONFIG_LOADER_ATTR_ENCRYPT = "encrypted";
	
	Logger log = Logger.getLogger(SirsLoader.class);
	LoadManager manager = null;
	
	public SirsLoader() {
	}
	
	public void configure(Element config) throws Exception {
		if(!CONFIG_LOADER_ELEMENT.equals(config.getLocalName())) {
			config = DOMUtils.getElement(config, CONFIG_LOADER_ELEMENT, true);
		}
		String tagMapFile = DOMUtils.getAttribute(config, CONFIG_LOADER_ATTR_MAPFILE, true);
		boolean encrypted = Boolean.valueOf(DOMUtils.getAttribute(config, CONFIG_LOADER_ATTR_ENCRYPT, 
				false)).booleanValue();
		
		Properties tagMap = new Properties();
		tagMap.load(IOUtils.openFileForRead(tagMapFile, encrypted));
		manager = new LoadManager(tagMap);
	}
	
	public void endDocument() throws SAXException {}
	public void startDocument() throws SAXException {}
	public void characters(char[] ch, int start, int length) throws SAXException {}
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}
	public void endPrefixMapping(String prefix) throws SAXException {}
	public void skippedEntity(String name) throws SAXException {}
	public void setDocumentLocator(Locator locator) {}
	public void processingInstruction(String target, String data) throws SAXException {}
	public void startPrefixMapping(String prefix, String uri) throws SAXException {}
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		ContentHandler handler = manager.getHandler(localName);
		handler.startElement(namespaceURI, localName, qName, atts);
	}
	
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		ContentHandler handler = manager.getHandler(localName);
		handler.endElement(namespaceURI, localName, qName);
	}
	
	public List getObjects() {
		return new ArrayList(manager.getObjects().values());
	}
	
	public void cleanup() {
		manager.clearObjects();
	}
	
	
}
