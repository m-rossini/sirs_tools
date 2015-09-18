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
 * <p>
 * <b>Title:</b> SubscriptionSection
 * </p>
 * <p>
 * <b>Description:</b>
 * </p>
 * <p>
 * <b>Copyright:</b> Copyright (c) 2004
 * </p>
 * <p>
 * <b>Company:</b> Auster Solutions
 * </p>
 * 
 * @author etirelli
 * @version $Id: SubscriptionSectionCH.java 211 2008-04-10 14:51:30Z anardo $
 */

public class SubscriptionSectionCH implements ContentHandler {

	public static final String	SUBSCRIPTION_TAG				= "SUBSCRIPTION_INFO";
	public static final String	SUBSCRIPTION_TOTAL_TAG	= "ENTIRE_SUBSCRIPTION_TOTAL";

	Logger											log											= Logger
																													.getLogger(SubscriptionSectionCH.class);
	// I18n i18n = I18n.getInstance(SubscriptionSectionCH.class);
	LoadManager									manager									= null;
	Section											subscription						= null;

	public SubscriptionSectionCH(LoadManager manager) {
		this.manager = manager;
	}

	public void endDocument() throws SAXException {
	}

	public void startDocument() throws SAXException {
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
	}

	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
	}

	public void endPrefixMapping(String prefix) throws SAXException {
	}

	public void skippedEntity(String name) throws SAXException {
	}

	public void setDocumentLocator(Locator locator) {
	}

	public void processingInstruction(String target, String data)
			throws SAXException {
	}

	public void startPrefixMapping(String prefix, String uri) throws SAXException {
	}

	public void startElement(String namespaceURI, String localName, String qName,
			Attributes atts) throws SAXException {
		if (localName.equals(SUBSCRIPTION_TAG)) {
			subscription = new Section();

			try {
				subscription.setElementType(localName);
				subscription.setTag(ParserUtils.getString(atts.getValue("key")));
				log.debug("Processing:" + subscription.getTag() + "@" + subscription.getElementType());
				
				subscription.setSectionName("Assinante");
				subscription.setCaption("Assinante: "
						+ ParserUtils.getString(atts.getValue("SubscriberName")));
				subscription.setAccessNbr(ParserUtils.getString(
						atts.getValue("PhoneNumber")).replaceAll("-", ""));
				subscription.setSubscriptionid(ParserUtils.getString(atts
						.getValue("subscriptionid")));
				subscription.setChannelId(ParserUtils.getString(atts
						.getValue("ChannelID")));
				subscription.setSubscriptionState(ParserUtils.getString(atts
						.getValue("subscriberState")));
				subscription.setPlanName(ParserUtils.getString(atts
						.getValue("planName")));
				((Invoice) manager.getContext()).addSection(subscription);
				manager.pushContext(subscription);
			} catch (Exception e) {
	      log.fatal("Error processing ELEMENT above.",e);
				e.printStackTrace();
			}
		} else if (localName.equals(SUBSCRIPTION_TOTAL_TAG)) {
			subscription.setTotalAmount(ParserUtils.getDouble(atts
					.getValue("SubscriptionAmount")));
		}
	}

	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.equals(SUBSCRIPTION_TAG)) {
			manager.popContext();
			subscription = null;
		}
	}

}
