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

import br.com.auster.om.invoice.Account;
import br.com.auster.om.invoice.Address;
import br.com.auster.om.invoice.Identity;
import br.com.auster.om.invoice.Invoice;
import br.com.auster.om.util.ParserUtils;

/**
 * <p><b>Title:</b> InvoiceLoader</p>
 * <p><b>Description:</b> </p>
 * <p><b>Copyright:</b> Copyright (c) 2004</p>
 * <p><b>Company:</b> Auster Solutions</p>
 *
 * @author etirelli
 * @version $Id: InvoiceCH.java 228 2008-05-30 18:15:37Z dcunha $
 */

public class InvoiceCH implements ContentHandler {
	public static final String INVOICE_TAG="CYCLE_SUMMARY";
	public static final String ADDRESS_TAG="BILLING_ADDRESS";
	public static final String	ATTR_CNPJ	= "CNPJ";
	public static final String	ATTR_CPF	= "CPF";
	public static final String ATTR_RESIDENCIAL = "RESIDENCIAL";
    public static final String ATTR_COMERCIAL = "COMERCIAL";
	
	Logger log = Logger.getLogger(InvoiceCH.class);
	//I18n i18n = I18n.getInstance(InvoiceCH.class); 	
	LoadManager manager = null;
	
	private Invoice invoice = null;
	private Account account = null;
	
	public InvoiceCH(LoadManager manager) {
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
		if(localName.equals(INVOICE_TAG)) {
			invoice = new Invoice();
			account = new Account();
			
			try {
				invoice.setTag(ParserUtils.getString(atts.getValue("key")));
				invoice.setElementType(localName);
				log.debug("Processing:" + invoice.getTag() + "@" + invoice.getElementType());
				
				invoice.setCycleCode(ParserUtils.getString(atts.getValue("CycleCode"))); 
				
				invoice.setCycleStartDate(ParserUtils.getDate(atts.getValue("CycleStartDate")));
				invoice.setOriginalCycleStartDate(ParserUtils.getString(atts.getValue("CycleStartDate")));
				
				//Changed the CycleEndDate load due to tkt #3.
				//invoice.setCycleEndDate(ParserUtils.getDate(atts.getValue("CycleEndDate"),86399999));
				invoice.setCycleEndDate(ParserUtils.getDateLastMiliSecond(atts.getValue("CycleEndDate"),"yyyyMMdd"));
				invoice.setOriginalCycleEndDate(ParserUtils.getString(atts.getValue("CycleEndDate")));
				
				invoice.setIssueDate(ParserUtils.getDate(atts.getValue("BillingDate")));
				invoice.setOriginalIssueDate(ParserUtils.getString(atts.getValue("BillingDate")));
				
				invoice.setDueDate(ParserUtils.getDate(atts.getValue("DueDate")));
				invoice.setOriginalDueDate(ParserUtils.getString(atts.getValue("DueDate")));				
				
				invoice.setPaymentsAmount(ParserUtils.getDouble(atts.getValue("PaymentsTotal")));
				invoice.setCurrentPeriodAmount(ParserUtils.getDouble(atts.getValue("MonthChargesTotal")));
				invoice.setPenaltiesAmount(ParserUtils.getDouble(atts.getValue("PenaltyTotal")));
				invoice.setInterestsAmount(ParserUtils.getDouble(atts.getValue("InterstTotal")));
				invoice.setMonthlyInstmtAmount(ParserUtils.getDouble(atts.getValue("MonthlyInstmnt")));
				invoice.setEqpInstmtAmount(ParserUtils.getDouble(atts.getValue("MonthlyEquipmentAmt")));
				invoice.setPreviousBalance(ParserUtils.getDouble(atts.getValue("PreviousBalance")));
				invoice.setAdjustmentsAmount(ParserUtils.getDouble(atts.getValue("AdjustmentsTotal")));
				invoice.setInterestsReturnAmount(ParserUtils.getDouble(atts.getValue("LatePymInterestCredit")));
				invoice.setPenaltiesReturnAmount(ParserUtils.getDouble(atts.getValue("LatePymCreditAmt")));
				
				invoice.setPastDueBalanceAmount(ParserUtils.getDouble(atts.getValue("PastDueBalanceAmt")));
				invoice.setStateTaxAmount(ParserUtils.getDouble(atts.getValue("StateTaxAmount")));
				invoice.setChargesAmount(ParserUtils.getDouble(atts.getValue("CurrentChargesAmt")));
				invoice.setTotalAmount(ParserUtils.getDouble(atts.getValue("TotalAmtDue")));			
				invoice.setCfopCode(ParserUtils.getString(atts.getValue("InvoiceCFOP")));
				invoice.setDisputeCredits(ParserUtils.getDouble(atts.getValue("CreditsAmount")));
				
				invoice.setDirectDebit(ParserUtils.getString(atts.getValue("DirectDebit")));
				invoice.setDirDebitMsgInd(ParserUtils.getString(atts.getValue("DirDebitMsgInd")));
				
				invoice.setHeldBillAmount(ParserUtils.getDouble(atts.getValue("HeldBill")));
				invoice.setRetentionAmount(ParserUtils.getDouble(atts.getValue("RetencaoAmount")));
				invoice.setTotalAmountDue(ParserUtils.getDouble(atts.getValue("TotalDueAmount")));
				
//				invoice.setCurrentMonthlyInstmt(Integer.parseInt(atts.getValue("")));
//				invoice.setTotalMonthlyInstmts(Integer.parseInt(atts.getValue("")));
				
				account.setAccountNumber(ParserUtils.getString(atts.getValue("AccountNumber")));
				account.setAccountState(ParserUtils.getString(atts.getValue("AccountStateID")));
				account.setAccountType(ParserUtils.getString(atts.getValue("CustTypeInd")));
				account.setCarrierState(ParserUtils.getString(atts.getValue("AccountStateID")));
				account.setCarrierCode("00");
				account.addInvoice(invoice);
				account.setInvoice(invoice);
				
				account.setAccountStateModified(false);
				account.setChangedAccountState(null);
				account.setCustomerServiceArea(ParserUtils.getString(atts.getValue("LeafLevelCSA")));
				
				manager.addAccount(account);
				manager.pushContext(invoice);
			} catch (Exception e) {
				log.error("Error loading invoice and account", e);
				e.printStackTrace();
			}
		}
		if(localName.equals(ADDRESS_TAG)) {
			account.setAccountName(ParserUtils.getString(atts.getValue("CustomerName")));
			
			Identity iden = new Identity();
			iden.setTag(ParserUtils.getString(atts.getValue("key")));
			iden.setElementType(localName);
			log.debug("Processing:" + iden.getTag() + "@" + iden.getElementType());
			
			if (atts.getValue("CustomerID1") == null) {
				iden.setIdentityNumber("");
			} else {
				iden.setIdentityNumber(atts.getValue("CustomerID1").trim());
			}
			
			Address address = new Address();
			
			if (iden.getIdentityNumber().length() == 11) {
				iden.setIdentityType(ATTR_CPF);	
				address.setAddressType(ATTR_RESIDENCIAL);
			} else if (iden.getIdentityNumber().length() == 14) {
				iden.setIdentityType(ATTR_CNPJ);
				address.setAddressType(ATTR_COMERCIAL);
			} else {
				iden.setIdentityType("");
				address.setAddressType("");
			}
			
			iden.setInvoice(invoice);
			account.addIdentity(iden);
			
			address.setAddressStreet(ParserUtils.getString(atts.getValue("AddressLine2")));
			
			if (ParserUtils.getString(atts.getValue("AddressLine5")) == null
						|| ParserUtils.getString(atts.getValue("AddressLine5")).trim().equals("")) {
					address.setNeighborhood(ParserUtils.getString(atts.getValue("AddressLine3")));
					address.setCity(ParserUtils.getString(atts.getValue("AddressLine4")));
			} else {
				address.setAddressComplement(ParserUtils.getString(atts.getValue("AddressLine3")));
				address.setNeighborhood(ParserUtils.getString(atts.getValue("AddressLine4")));
				address.setCity(ParserUtils.getString(atts.getValue("AddressLine5")));
			}

			address.setPostalCode(ParserUtils.getString(atts.getValue("PostalCode")));
			account.addAddress(address);
			
		}
	}
	
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		if(localName.equals(INVOICE_TAG)) {
			manager.popContext();
			invoice = null;
			account = null;
		}
	}
	
	
	
}
