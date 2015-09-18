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
import br.com.auster.om.util.ParserUtils;
import br.com.auster.om.util.UnitCounter;

/**
 * <p><b>Title:</b> UsageSectionCH</p>
 * <p><b>Description:</b> </p>
 * <p><b>Copyright:</b> Copyright (c) 2004</p>
 * <p><b>Company:</b> Auster Solutions</p>
 *
 * @author etirelli
 * @version $Id: UsageSectionCH.java 174 2007-10-08 16:50:24Z mtengelm $
 */

public class UsageSectionCH implements ContentHandler {
  Logger log = Logger.getLogger(UsageSectionCH.class);
  //I18n i18n = I18n.getInstance(UsageSectionCH.class); 	
  LoadManager manager = null;
  Section usage = null;
  
  public UsageSectionCH(LoadManager manager) {
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
    usage = new Section();

    try {
      usage.setElementType(localName);
      usage.setTag(ParserUtils.getString(atts.getValue("key")));
      log.debug("Processing:" + usage.getTag() + "@" + usage.getElementType());
            
      usage.setSectionName(ParserUtils.getString(atts.getValue("label")));
      usage.setCaption(ParserUtils.getString(atts.getValue("ServicePlan")));
      usage.setTotalAmount(ParserUtils.getDouble(atts.getValue("Subtotal")));
      usage.setAccessNbr(ParserUtils.getString(atts.getValue("ChannelID")));
      
      usage.setUnitType(ParserUtils.getString(atts.getValue("UnitsDescription")));
      usage.setIncludedUnits(createCounter(usage.getUnitType(), atts.getValue("IncludedUnits")));      
      usage.setUsedUnits(createCounter(usage.getUnitType(), atts.getValue("UsedUnits")));      
      usage.setBilledUnits(createCounter(usage.getUnitType(), atts.getValue("BilledUnits")));      

      usage.setSubscriptionid(((Section) manager.getContext()).getSubscriptionid());
      
      usage.setUsage(true);
      
      ((Section) manager.getContext()).addSection(usage);
      manager.pushContext(usage);
    } catch (Exception e) {
      log.fatal("Error processing ELEMENT above.",e);
      e.printStackTrace();
    }
  }
  
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    manager.popContext();
    usage = null;
  }
  
  private UnitCounter createCounter(String type, String val) {
    UnitCounter counter = null;
    if(type.equalsIgnoreCase("MINUTES")) {
      counter = new UnitCounter();
      counter.setType(UnitCounter.TIME_COUNTER);
      counter.addMinutes(ParserUtils.getDouble(val));
    } else if(type.equalsIgnoreCase("EVENT")) {
        counter = new UnitCounter();
        counter.setType(UnitCounter.UNIT_COUNTER);
        counter.addUnits((long) ParserUtils.getDouble(val));
    } else if(type.equalsIgnoreCase("KB")) {
      counter = new UnitCounter();
      counter.setType(UnitCounter.DATA_COUNTER);
      counter.addKBytes( ParserUtils.getDouble(val));
    } else if(type.equalsIgnoreCase("BYTES")) {
      counter = new UnitCounter();
      counter.setType(UnitCounter.DATA_COUNTER);
      counter.addBytes((long) ParserUtils.getDouble(val));
    } else if(type.equalsIgnoreCase("BYTE")) {
      counter = new UnitCounter();
      counter.setType(UnitCounter.DATA_COUNTER);
      counter.addBytes((long) ParserUtils.getDouble(val));
    } else if(type.equalsIgnoreCase("MB")) {
      counter = new UnitCounter();
      counter.setType(UnitCounter.DATA_COUNTER);
      counter.addMBytes(ParserUtils.getDouble(val));
    }
    
    return counter;
  }
  
}
