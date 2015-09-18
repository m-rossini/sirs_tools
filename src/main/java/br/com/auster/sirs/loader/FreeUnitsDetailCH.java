/*
 * Copyright (c) 2004-2005 Auster Solutions. All Rights Reserved.
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
 * Created on Oct 6, 2005
 */
package br.com.auster.sirs.loader;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import br.com.auster.om.invoice.FreeUnitsDetail;
import br.com.auster.om.invoice.Section;
import br.com.auster.om.util.ParserUtils;

/**
 * <p><b>Title:</b> FreeUnitsDetailCH</p>
 * <p><b>Description:</b> A content handler for free units detail</p>
 * <p><b>Copyright:</b> Copyright (c) 2004-2005</p>
 * <p><b>Company:</b> Auster Solutions</p>
 *
 * @author etirelli
 * @version $Id: FreeUnitsDetailCH.java 154 2007-05-27 23:50:16Z mtengelm $
 */
public class FreeUnitsDetailCH implements ContentHandler {
	  Logger log = Logger.getLogger(FreeUnitsDetailCH.class);
	  LoadManager manager = null;
	  FreeUnitsDetail detail = null;
	  
	  public FreeUnitsDetailCH(LoadManager manager) {
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
	  
	  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
	    try {
	      detail = new FreeUnitsDetail();
	      detail.setTag(ParserUtils.getString(atts.getValue("key")));
	      detail.setElementType(localName);
	      log.debug("Processing:" + detail.getTag() + "@" + detail.getElementType());
	      
	      detail.setSeqNbr(ParserUtils.getInt(atts.getValue("ItemNo")));
	      detail.setCaption(ParserUtils.getString(atts.getValue("shareName")));
	      detail.setServiceName(ParserUtils.getString(atts.getValue("shareName")));
	      detail.setAccessNumber(ParserUtils.getString(atts.getValue("accessNumber")));
	      detail.setStartDate(ParserUtils.getDate(atts.getValue("DataInicio")));
	      detail.setEndDate(ParserUtils.getDate(atts.getValue("DataFim")));
	      detail.setAllocationType(ParserUtils.getString(atts.getValue("AllocationType")));
	      detail.setSharingType(ParserUtils.getString(atts.getValue("AllocationSharing")));
	      detail.setAllocationKey(ParserUtils.getString(atts.getValue("AllocationKey")));
	      
	      // TODO: replace hardcoded units for a configurable one...
	      
	      detail.setReceivedRolloverUnits(ParserUtils.getUnitCounter("MINUTES", atts.getValue("previousBalance")));
	      detail.setTotalIncludedUnits(ParserUtils.getUnitCounter("MINUTES", atts.getValue("contractedShare")));
	      detail.setUsedUnits(ParserUtils.getUnitCounter("MINUTES", atts.getValue("usedShare")));
	      detail.setTotalUsedUnits(ParserUtils.getUnitCounter("MINUTES", atts.getValue("UsedUnitsALLSubs")));
	      detail.setRemainingUnits(ParserUtils.getUnitCounter("MINUTES", atts.getValue("RemainingUnits")));
	      detail.setMaxRolloverUnits(ParserUtils.getUnitCounter("MINUTES", atts.getValue("MaxRolloverUnits")));
	      detail.setRolledOverUnits(ParserUtils.getUnitCounter("MINUTES", atts.getValue("rollOverShare")));

	      ((Section)manager.getContext()).addDetail(detail);
	      
	    } catch (Exception e) {
	      log.error("Error loading a FreeUnits Detail record", e);
	      e.printStackTrace();
	      throw new SAXException(e);
	    }
	  }
	  
	  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
	    detail = null;
	  }
}
