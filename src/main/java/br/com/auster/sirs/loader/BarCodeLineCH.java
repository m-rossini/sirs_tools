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

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import br.com.auster.om.invoice.BarCode;
import br.com.auster.om.invoice.Invoice;
import br.com.auster.om.util.ParserUtils;



/**
 * @author framos
 * @version $Id: BarCodeLineCH.java 154 2007-05-27 23:50:16Z mtengelm $
 */

public class BarCodeLineCH implements ContentHandler {
	

	Logger log = Logger.getLogger(BarCodeLineCH.class);
	LoadManager manager = null;
	


	
	public BarCodeLineCH(LoadManager manager) {
		this.manager = manager;
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
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {}

	
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		BarCode barcode = new BarCode();
		barcode.setElementType(localName);
		barcode.setTag(ParserUtils.getString(atts.getValue("key")));
    log.debug("Processing:" + barcode.getTag() + "@" + barcode.getElementType());
    
		barcode.setAlphaNumericBarCode(atts.getValue("BarCodeLine"));
		barcode.setOCRLeftLine(removeSpaces(atts.getValue("OcrScanLineLeft")));
		barcode.setOCRRightLine(removeSpaces(atts.getValue("OcrScanLineRight")));
		Invoice inv = (Invoice) manager.getContext();
		inv.setBarCode(barcode);
	}

	
	
	private String removeSpaces(String _barCode) {
		StringBuffer bf = new StringBuffer();
		for (int i=0; i < _barCode.length(); i++) {
			String currChar = _barCode.substring(i,i+1).trim();
			if (currChar.length() > 0) {
				bf.append(currChar);
			}
		}
		return bf.toString();
	}
	
}
