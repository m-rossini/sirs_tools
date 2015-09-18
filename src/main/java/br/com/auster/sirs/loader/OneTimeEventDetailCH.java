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

import br.com.auster.om.invoice.OneTimeEventDetail;
import br.com.auster.om.invoice.Section;
import br.com.auster.om.util.ParserUtils;

/**
 * <p><b>Title:</b> OneTimeEventDetailCH</p>
 * <p><b>Description:</b> </p>
 * <p><b>Copyright:</b> Copyright (c) 2004</p>
 * <p><b>Company:</b> Auster Solutions</p>
 *
 * @author etirelli
 * @version $Id: OneTimeEventDetailCH.java 154 2007-05-27 23:50:16Z mtengelm $
 */

public class OneTimeEventDetailCH implements ContentHandler {
  Logger log = Logger.getLogger(OneTimeEventDetailCH.class);
  //I18n i18n = I18n.getInstance(OneTimeEventDetailCH.class); 	
  LoadManager manager = null;
  OneTimeEventDetail oneTimeDetail = null;
  
  public OneTimeEventDetailCH(LoadManager manager) {
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
    oneTimeDetail = new OneTimeEventDetail();

    try {
      oneTimeDetail.setTag(ParserUtils.getString(atts.getValue("key")));
      oneTimeDetail.setElementType(localName);
      log.debug("Processing:" + oneTimeDetail.getTag() + "@" + oneTimeDetail.getElementType());
      
      oneTimeDetail.setSeqNbr(ParserUtils.getInt(atts.getValue("ItemNo")));
      oneTimeDetail.setCaption(ParserUtils.getString(atts.getValue("Descricao")));
      oneTimeDetail.setCarrierCode(ParserUtils.getString(atts.getValue("CarrierID")));
      oneTimeDetail.setCarrierState(ParserUtils.getString(atts.getValue("CarrierState")));
      oneTimeDetail.setStartDate(ParserUtils.getDate(atts.getValue("DateStart")));
      oneTimeDetail.setEventDate(ParserUtils.getDate(atts.getValue("DataElemento")));
      oneTimeDetail.setTotalAmount(ParserUtils.getDouble(atts.getValue("Amount")));
      oneTimeDetail.setAccessNumber(ParserUtils.getString(atts.getValue("accessNumber")));
      
      ((Section) manager.getContext()).addDetail(oneTimeDetail);
    } catch (Exception e) {
    	log.error("Error Processing Objects.", e);
      e.printStackTrace();
    }
  }
  
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    oneTimeDetail = null;
  }

}
