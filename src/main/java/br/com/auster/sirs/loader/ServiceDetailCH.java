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
import br.com.auster.om.invoice.ServiceDetail;
import br.com.auster.om.util.ParserUtils;

/**
 * <p><b>Title:</b> ServiceDetailCH</p>
 * <p><b>Description:</b> </p>
 * <p><b>Copyright:</b> Copyright (c) 2004</p>
 * <p><b>Company:</b> Auster Solutions</p>
 *
 * @author etirelli
 * @version $Id: ServiceDetailCH.java 191 2007-12-14 14:52:28Z gportuga $
 */

public class ServiceDetailCH implements ContentHandler {
  Logger log = Logger.getLogger(ServiceDetailCH.class);
  //I18n i18n = I18n.getInstance(ServiceDetailCH.class); 	
  LoadManager manager = null;
  ServiceDetail serviceDetail = null;
  
  public ServiceDetailCH(LoadManager manager) {
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
    serviceDetail = new ServiceDetail();

    try {
      serviceDetail.setTag(ParserUtils.getString(atts.getValue("key")));
      serviceDetail.setElementType(localName);
      log.debug("Processing:" + serviceDetail.getTag() + "@" + serviceDetail.getElementType());
      
      serviceDetail.setSeqNbr(ParserUtils.getInt(atts.getValue("ItemNo")));
      serviceDetail.setCaption(ParserUtils.getString(atts.getValue("Descricao")));
      serviceDetail.setStartDate(ParserUtils.getDate(atts.getValue("StartDate")));
      serviceDetail.setOriginalStartDate(ParserUtils.getString(atts.getValue("StartDate")));
      serviceDetail.setEndDate(ParserUtils.getDateLastMiliSecond(atts.getValue("EndDate"), "yyyyMMdd"));
      // serviceDetail.setEndDate(ParserUtils.getDate(atts.getValue("EndDate"),86399999));
      serviceDetail.setOriginalEndDate(ParserUtils.getString(atts.getValue("EndDate")));
      serviceDetail.setTotalAmount(ParserUtils.getDouble(atts.getValue("Amount")));
      serviceDetail.setProRate(ParserUtils.getBoolean(atts.getValue("ProRatedInd")));
      
  	  /* GPORTUGA - 20071212 - BEGIN - Changes due to P8 new fields */
      serviceDetail.setTaxRate(ParserUtils.getDouble(atts.getValue("TaxRateTotalPct")));
      serviceDetail.setSvcId(ParserUtils.getString(atts.getValue("ServicePrice")));
  	  /* GPORTUGA - 20071212 - END */
      
      ((Section) manager.getContext()).addDetail(serviceDetail);
    } catch (Exception e) {
      log.fatal("Error processing ELEMENT above.",e);
      e.printStackTrace();
    }
  }
  
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    serviceDetail = null;
  }

}
