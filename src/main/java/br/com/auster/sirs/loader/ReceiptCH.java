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

import br.com.auster.om.invoice.ChargedTax;
import br.com.auster.om.invoice.Invoice;
import br.com.auster.om.invoice.Receipt;
import br.com.auster.om.invoice.ReceiptDetail;
import br.com.auster.om.invoice.ReceiptDiscount;
import br.com.auster.om.util.ParserUtils;
import br.com.auster.om.util.UnitCounter;
import br.com.auster.sirs.loader.exceptions.InputFileParserException;

/**
 * @author framos
 * @version $Id: ReceiptCH.java 218 2008-04-24 14:32:24Z anardo $
 */

public class ReceiptCH implements ContentHandler {

	public static final String	RECEIPT_TAG	       = "LD_GENERAL_INFO";
	public static final String	RECEIPT_TAXES_TAG	 = "TOTAL_TAX";
	public static final String	RECEIPT_ICMS_TAG	 = "ICMS_SUMMARY";
	public static final String	RECEIPT_DETAIL_TAG	= "SUMMARY_ACCUMULATION_TAXES";
	public static final String	DISCOUNTS_TAG	     = "DiscountAndPromotions";

	public static final int	   COUNTER_TYPE	       = 1;
	public static final int	   TIME_TYPE	         = 2;

	Logger	                   log	               = Logger.getLogger(ReceiptCH.class);
	// I18n i18n = I18n.getInstance(ReceiptCH.class);

	LoadManager	               manager	           = null;

	public ReceiptCH(LoadManager manager) {
		this.manager = manager;
	}

	public void startElement(String _namespaceURI, String _localName, String _qName, Attributes _atts)
	    throws SAXException {

		if (_localName.equals(RECEIPT_TAG)) {
			handleReceipt(_localName, _atts);
		} else if (_localName.equals(RECEIPT_TAXES_TAG)) {
			handleReceiptTaxes(_localName, _atts);
		} else if (_localName.equals(RECEIPT_ICMS_TAG)) {
			handleReceiptIcms(_localName, _atts);
		} else if (_localName.equals(RECEIPT_DETAIL_TAG)) {
			handleReceiptDetail(_localName, _atts);
		} else if (_localName.equals(DISCOUNTS_TAG)) {
			handleNFDiscounts(_localName, _atts);
		}
	}

	private void handleNFDiscounts(String _localName, Attributes _atts) {
		try {
			Receipt receipt = (Receipt) manager.getContext();
			ReceiptDiscount dis = new ReceiptDiscount();
			dis.setElementType(_localName);
			dis.setTag(ParserUtils.getString(_atts.getValue("key")));
			log.debug("Processing:" + dis.getTag() + "@" + dis.getElementType());
			
			dis.setDiscontName(ParserUtils.getString(_atts.getValue("caption")));
			dis.setOriginalDiscountAmount(ParserUtils.getString(_atts.getValue("amount")));
			dis.setOriginalDiscountDate(ParserUtils.getString(_atts.getValue("endDate")));
			dis.setDiscountAmount(ParserUtils.getDouble(_atts.getValue("amount")));
			dis.setDiscountDate(ParserUtils.getDate(_atts.getValue("endDate")));
			receipt.addDiscount(dis);
		} catch (ClassCastException e) {
			InputFileParserException spe = new InputFileParserException("Input File is Corrupted.");
			log.fatal("Sirs structure not as expected.", spe);					
			spe.initCause(e);			
			throw spe;
		}
	}

	private void handleReceipt(String _localName, Attributes _atts) {

		Receipt receipt = new Receipt();
		receipt.setElementType(_localName);
		receipt.setTag(ParserUtils.getString(_atts.getValue("key")));
		log.debug("Processing:" + receipt.getTag() + "@" + receipt.getElementType());
		
		receipt.setReceiptClass(ParserUtils.getString(_atts.getValue("LongDistanceCarrierSeries")));
		receipt.setReceiptSubclass(ParserUtils.getString(_atts
		    .getValue("LongDistanceCarrierSubSeries")));
		receipt.setReceiptNbr(ParserUtils.getString(_atts.getValue("ItemNo")));
		receipt.setCarrierCode(ParserUtils.getString(_atts.getValue("LongDistanceCarrierID")));
		receipt.setCarrierState(ParserUtils.getString(_atts.getValue("LongDistanceCarrierUF")));
		receipt.setCarrierName(ParserUtils.getString(_atts.getValue("LongDistanceCarrierName")));
		receipt.setInvoiceIssueDate(ParserUtils.getString(_atts.getValue("InvoiceIssueDate")));
				
		try {
			Invoice invoice = (Invoice) manager.getContext();
			invoice.addReceipt(receipt);
			manager.pushContext(receipt);
			log.debug("Found receipt nbr. " + receipt.getReceiptNbr());
		} catch (ClassCastException e) {			
			InputFileParserException spe = new InputFileParserException("Input File is Corrupted.");
			log.fatal("Sirs structure not as expected.", spe);			
			spe.initCause(e);			
			throw spe;
		}
	}

	private void handleReceiptTaxes(String _localName, Attributes _atts) {

		try {
			Receipt receipt = (Receipt) manager.getContext();
		
			// create for FUST
			ChargedTax tax = new ChargedTax();
			tax.setElementType(_localName);
			tax.setTag(ParserUtils.getString(_atts.getValue("key")));
			log.debug("Processing:" + tax.getTag() + "@" + tax.getElementType());
			
			tax.setNonTaxableAmount(0d);
			tax.setTaxableAmount(ParserUtils.getDouble(_atts.getValue("TotalTaxableAmount")));
			tax.setTotalTaxesAmount(ParserUtils.getDouble(_atts.getValue("TotalTaxesAmount")));
			tax.setTaxAmount(ParserUtils.getDouble(_atts.getValue("TotalFUSTAmount")));
			tax.setTaxName("FUST");
			tax.setTaxRate(1d);
			receipt.addTax(tax);
			log.debug("FUST for receipt : " + tax.getTaxRate() + "% ==> $" + tax.getTaxAmount());

			// create for FUNTTEL
			tax = new ChargedTax();
			tax.setElementType(_localName);
			tax.setTag(ParserUtils.getString(_atts.getValue("key")));
			log.debug("Processing:" + tax.getTag() + "@" + tax.getElementType());
			
			tax.setNonTaxableAmount(0d);
			tax.setTaxableAmount(ParserUtils.getDouble(_atts.getValue("TotalTaxableAmount")));
			tax.setTotalTaxesAmount(ParserUtils.getDouble(_atts.getValue("TotalTaxesAmount")));
			tax.setTaxAmount(ParserUtils.getDouble(_atts.getValue("TotalFUNTTELAmount")));
			tax.setTaxName("FUNTTEL");
			tax.setTaxRate(0.5d);
			receipt.addTax(tax);
			log.debug("FUNTTEL for receipt : " + tax.getTaxRate() + "% ==> $" + tax.getTaxAmount());

			// setting receipt total amount
			receipt.setTotalAmount(ParserUtils.getDouble(_atts.getValue("TotalAmountWithTaxes")));
			receipt.setTotalInvoiceAmount(ParserUtils.getDouble(_atts.getValue("TotalInvoiceAmount"))); //Key 202X
			log.debug("Receipt total amount: $" + receipt.getTotalAmount());

		} catch (ClassCastException e) {
			InputFileParserException spe = new InputFileParserException("Input File is Corrupted.");
			log.fatal("Sirs structure not as expected.", spe);			
			spe.initCause(e);			
			throw spe;
		}
	}

	private void handleReceiptIcms(String _localName, Attributes _atts) {
		
		try {
			Receipt receipt = (Receipt) manager.getContext();

			ChargedTax tax = new ChargedTax();
			tax.setElementType(_localName);
			tax.setTag(ParserUtils.getString(_atts.getValue("key")));
			log.debug("Processing:" + tax.getTag() + "@" + tax.getElementType());
			
			tax.setNonTaxableAmount(ParserUtils.getDouble(_atts.getValue("NonTaxableAmount")));
			tax.setTaxableAmount(ParserUtils.getDouble(_atts.getValue("BaseCalculo")));
			tax.setTaxAmount(ParserUtils.getDouble(_atts.getValue("TaxesAmt")));
			tax.setTaxName("ICMS");
			tax.setTaxRate(ParserUtils.getDouble(_atts.getValue("Aliquota")));
			receipt.addTax(tax);
			log.debug("ICMS for receipt : " + tax.getTaxRate() + "% ==> $" + tax.getTaxAmount());

		} catch (ClassCastException e) {
			InputFileParserException spe = new InputFileParserException("Input File is Corrupted.");
			log.fatal("Sirs structure not as expected.", spe);			
			spe.initCause(e);			
			throw spe;
		}
	}

	private void handleReceiptDetail(String _localName, Attributes _atts) {

		try {
			Receipt receipt = (Receipt) manager.getContext();

			ReceiptDetail detail = new ReceiptDetail();
			detail.setElementType(_localName);
			detail.setTag(ParserUtils.getString(_atts.getValue("key")));
			log.debug("Processing:" + detail.getTag() + "@" + detail.getElementType());
			
			detail.setFiscalCode(ParserUtils.getString(_atts.getValue("CodigoServico")));
			detail.setCaption(ParserUtils.getString(_atts.getValue("NomeServico")));
			detail.setReceipt(receipt);
			detail.setSequenceNbr(ParserUtils.getInt(_atts.getValue("ItemNo")));
			detail.setTotalAmount(ParserUtils.getDouble(_atts.getValue("TotalAmount")));
			int type = ParserUtils.getInt(_atts.getValue("IndicadorQTD"));
			double counter = 0d;
			switch (type) {
			case COUNTER_TYPE: 
			{
				detail.setUnitType(UnitCounter.UNIT_COUNTER);
				counter = ParserUtils.getDouble(_atts.getValue("Quantidade"));
				detail.setEventUnits(ParserUtils.getUnitCounter(UnitCounter.UNIT_COUNTER, _atts.getValue("Quantidade")));
				break;
			}
			case TIME_TYPE:
			{
				detail.setUnitType(UnitCounter.TIME_COUNTER);
				counter = 60 * ParserUtils.getDouble(_atts.getValue("Duracao"));
				detail.setEventUnits(ParserUtils.getUnitCounter(UnitCounter.TIME_COUNTER, Double.toString(counter)));
				break;
			}
			default:
			{
				detail.setUnitType(null);
				counter = ParserUtils.getDouble(_atts.getValue("Quantidade"));
				break;
			}
			}
			detail.setUnitCount(String.valueOf(counter));
			
			detail.setEventQuantity(ParserUtils.getInt(_atts.getValue("Quantidade")));

			
			log
			    .debug("Receipt detail : " + detail.getSequenceNbr() + " ==> $"
			        + detail.getTotalAmount());
			// creating ICMS tax for detail
			double rate = ParserUtils.getDouble(_atts.getValue("AliquotaICMS"));
			double base = ParserUtils.getDouble(_atts.getValue("TotalAmount"));
			ChargedTax tax = new ChargedTax();
			tax.setElementType(_localName);
			tax.setTag(ParserUtils.getString(_atts.getValue("key")));
			log.debug("Processing:" + tax.getTag() + "@" + tax.getElementType());
			
			tax.setNonTaxableAmount(0d);
			tax.setTaxableAmount(base);
			tax.setTaxAmount(rate * base / 100);
			tax.setTaxName("ICMS");
			tax.setTaxRate(rate);
			detail.addTax(tax);
			log.debug("ICMS for receipt detail : " + tax.getTaxRate() + "% ==> $" + tax.getTaxAmount());

			receipt.addDetail(detail);

		} catch (ClassCastException e) {
			InputFileParserException spe = new InputFileParserException("Input File is Corrupted.");
			log.fatal("Sirs structure not as expected.", spe);			
			spe.initCause(e);			
			throw spe;
		}

	}

	public void endElement(String _namespaceURI, String _localName, String _qName)
	    throws SAXException {
		try {
			if (_localName.equals(RECEIPT_TAG)) {
				Receipt receipt = (Receipt) manager.popContext();
				log.debug("Finished receipt with nbr. " + receipt.getReceiptNbr());
			}
		} catch (ClassCastException cce) {
			log.fatal("Sirs structure not as expected.", cce);
			throw cce;
		}
	}

	public void endDocument() throws SAXException {
	}

	public void startDocument() throws SAXException {
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
	}

	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
	}

	public void endPrefixMapping(String prefix) throws SAXException {
	}

	public void skippedEntity(String name) throws SAXException {
	}

	public void setDocumentLocator(Locator locator) {
	}

	public void processingInstruction(String target, String data) throws SAXException {
	}

	public void startPrefixMapping(String prefix, String uri) throws SAXException {
	}

}
