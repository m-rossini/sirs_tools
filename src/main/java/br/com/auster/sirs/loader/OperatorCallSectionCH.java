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

import br.com.auster.om.invoice.Invoice;
import br.com.auster.om.invoice.Section;
import br.com.auster.om.util.ParserUtils;

/**
 * <p><b>Title:</b> OperatorCallSectionCH</p>
 * <p><b>Description:</b> </p>
 * <p><b>Copyright:</b> Copyright (c) 2004</p>
 * <p><b>Company:</b> Auster Solutions</p>
 *
 * @author etirelli
 * @version $Id: OperatorCallSectionCH.java 154 2007-05-27 23:50:16Z mtengelm $
 */

public class OperatorCallSectionCH implements ContentHandler {
  Logger log = Logger.getLogger(OperatorCallSectionCH.class);
  //I18n i18n = I18n.getInstance(OperatorCallSectionCH.class); 	
  LoadManager manager = null;
  Section section = null;
  
  public OperatorCallSectionCH(LoadManager manager) {
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
    section = new Section();

    try {
      section.setElementType(localName);
      section.setTag(ParserUtils.getString(atts.getValue("key")));
      log.debug("Processing:" + section.getTag() + "@" + section.getElementType());
      
      section.setSectionName("Eventos de Uso");
      section.setCaption("Eventos de Uso");
      section.setUsage(true);
      section.setCarrierCode(ParserUtils.getString(atts.getValue("CarrierCode")));
      section.setCarrierState(ParserUtils.getString(atts.getValue("CarrierState")));
     
      if(manager.getContext() instanceof Section) {
        section.setAccessNbr(((Section) manager.getContext()).getAccessNbr());
        section.setSubscriptionid(((Section) manager.getContext()).getSubscriptionid());
        ((Section) manager.getContext()).addSection(section);
      } else if(manager.getContext() instanceof Invoice) { 
        ((Invoice) manager.getContext()).addSection(section);
      } else {
        throw new ClassCastException("Not supposed to find an instance of "+
                  manager.getContext().getClass().getName()+ " in the context stack");
      }
	  manager.pushContext(section);
	  
    } catch (Exception e) {
      log.fatal("Error processing ELEMENT above.",e);
    	e.printStackTrace();
      throw new SAXException(e);
    }
  }
  
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    manager.popContext();
    manager.setSummarySection(null);
  }

}
