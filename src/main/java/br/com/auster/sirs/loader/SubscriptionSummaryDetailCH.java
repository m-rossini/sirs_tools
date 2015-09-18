/*
 * Copyright (c) 2008 Auster Solutions. All Rights Reserved.
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
 * Created on May 28, 2008
 */
package br.com.auster.sirs.loader;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import br.com.auster.om.invoice.Section;
import br.com.auster.om.invoice.SubscriptionSummaryDetail;
import br.com.auster.om.util.ParserUtils;

/**
 * @author anardo
 */

public class SubscriptionSummaryDetailCH implements ContentHandler {
  Logger log = Logger.getLogger(SubscriptionSummaryDetailCH.class);
  LoadManager manager = null;
  SubscriptionSummaryDetail detail = null;
  
  public static final String	SUBSCRIPTION_SUMMARY_GROUP	= "SUBSCRIPTION_SUMMARY_GROUP";
  public static final String	SUBSCRIPTION_SUMMARY_DETAIL	= "SUBSCRIPTION_SUMMARY_DETAIL";

  
  public SubscriptionSummaryDetailCH(LoadManager manager) {
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
		if (localName.equals(SUBSCRIPTION_SUMMARY_GROUP)) {
			SubscriptionSummaryDetail detail = (SubscriptionSummaryDetail) manager.getContext();
			detail.setPlanName(ParserUtils.getString(atts.getValue("planName")));
			detail.setChannelId(ParserUtils.getString(atts.getValue("SummaryGroupName")).replaceAll("-", ""));
		} else if (localName.equals(SUBSCRIPTION_SUMMARY_DETAIL)) {
			  detail = new SubscriptionSummaryDetail();
			  detail.setTag(ParserUtils.getString(atts.getValue("key")));
			  detail.setElementType(localName);
			  detail.setTotalAmount(ParserUtils.getDouble(atts.getValue("TotalSubscrChargesAmt")));
			  manager.pushContext(detail);
		}
    } catch (Exception e) {
      log.fatal("Error processing ELEMENT above.",e);
      e.printStackTrace();
      throw new SAXException(e);
    }
  }
  
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
	  if (localName.equals(SUBSCRIPTION_SUMMARY_DETAIL)) {
		  manager.popContext();
		  ((Section) manager.getContext()).addDetail(detail);
		  detail = null;
	  }
  }  
}
