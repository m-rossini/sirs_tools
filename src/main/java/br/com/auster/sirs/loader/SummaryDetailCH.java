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
 * Created on Feb 1, 2005
 */
package br.com.auster.sirs.loader;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import br.com.auster.om.invoice.Section;
import br.com.auster.om.invoice.UsageSummaryDetail;
import br.com.auster.om.util.ParserUtils;
import br.com.auster.om.util.UnitCounter;

/**
 * <p><b>Title:</b> SummaryDetailCH</p>
 * <p><b>Description:</b> </p>
 * <p><b>Copyright:</b> Copyright (c) 2004</p>
 * <p><b>Company:</b> Auster Solutions</p>
 *
 * @author etirelli
 * @version $Id: SummaryDetailCH.java 154 2007-05-27 23:50:16Z mtengelm $
 */

public class SummaryDetailCH implements ContentHandler {
  Logger log = Logger.getLogger(SummaryDetailCH.class);
  //I18n i18n = I18n.getInstance(SummaryDetailCH.class); 	
  LoadManager manager = null;
  UsageSummaryDetail summary = null;
  
  public SummaryDetailCH(LoadManager manager) {
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
    if(manager.getSummarySection() == null) {
	  Section summarySection = new Section();
	  summarySection.setElementType("SUMMARY_SECTION");
	  summarySection.setTag("000T");
	  summarySection.setSectionName("Chamadas Locais");
	  summarySection.setCaption("ChamadasLocais");
	  summarySection.setUsage(true);
	  Section section = (Section)manager.getContext();
	  summarySection.setCarrierCode(section.getCarrierCode());
	  summarySection.setCarrierState(section.getCarrierState());
	  summarySection.setAccessNbr(section.getAccessNbr());
	  section.addSection(summarySection);
	  manager.setSummarySection(summarySection);
    }
    
    try {
      summary = new UsageSummaryDetail();
      summary.setTag(ParserUtils.getString(atts.getValue("key")));
      summary.setElementType(localName);
      log.debug("Processing:" + summary.getTag() + "@" + summary.getElementType());
      
      summary.setSeqNbr(ParserUtils.getInt(atts.getValue("ItemNo")));
      summary.setCaption(ParserUtils.getString(atts.getValue("Descricao")));
      summary.setTotalAmount(ParserUtils.getDouble(atts.getValue("Amount")));
      summary.setCarrierCode(ParserUtils.getString(atts.getValue("CarrierCode")));
      summary.setCarrierState(ParserUtils.getString(atts.getValue("CarrierState")));
      summary.setGroupId(ParserUtils.getString(atts.getValue("GroupID")));
      
      summary.setUnitType(ParserUtils.getString(atts.getValue("UnitsDescription")));
      summary.setIncludedUnits(createCounter(summary.getUnitType(), atts.getValue("IncludedUnits")));      
      summary.setRolloverReceived(createCounter(summary.getUnitType(), atts.getValue("RolloverReceived")));      
      summary.setUsedUnits(createCounter(summary.getUnitType(), atts.getValue("UsedUnits")));      
      summary.setBilledUnits(createCounter(summary.getUnitType(), atts.getValue("BilledUnits")));      
      summary.setRolloverForwarded(createCounter(summary.getUnitType(), atts.getValue("RolloverForwarded")));      
      
      manager.getSummarySection().addDetail(summary);
    } catch (Exception e) {
      log.fatal("Error processing ELEMENT above.",e);
      e.printStackTrace();
      throw new SAXException(e);
    }
  }
  
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    summary = null;
  }
  
  private UnitCounter createCounter(String type, String val) {
    UnitCounter counter = null;
    if(type.equalsIgnoreCase("MINUTES")) {
      counter = new UnitCounter();
      counter.setType(UnitCounter.TIME_COUNTER);
      counter.addMinutes(ParserUtils.getDouble(val));
    }
    return counter;
  }

}
