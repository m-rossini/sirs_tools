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

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import br.com.auster.om.invoice.Section;
import br.com.auster.om.invoice.UsageDetail;
import br.com.auster.om.invoice.VoiceUsageDetail;
import br.com.auster.om.util.ParserUtils;
import br.com.auster.om.util.UnitCounter;

/**
 * <p><b>Title:</b> VoiceUsageDetailCH</p>
 * <p><b>Description:</b> </p>
 * <p><b>Copyright:</b> Copyright (c) 2004</p>
 * <p><b>Company:</b> Auster Solutions</p>
 *
 * @author etirelli
 * @version $Id: VoiceUsageDetailCH.java 188 2007-12-06 17:53:34Z gportuga $
 */

public class VoiceUsageDetailCH implements ContentHandler {
  Logger log = Logger.getLogger(VoiceUsageDetailCH.class);
  LoadManager manager = null;
  UsageDetail detail = null;
  UsageDetail previous = null;
  
  public VoiceUsageDetailCH(LoadManager manager) {
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
      detail = new VoiceUsageDetail();
      detail.setTag(ParserUtils.getString(atts.getValue("key")));
      detail.setElementType(localName);
      log.debug("Processing:" + detail.getTag() + "@" + detail.getElementType());
      

//      detail.setOriginalDatetime(ParserUtils.getString(atts.getValue("Date")+atts.getValue("Time")));
      detail.setOriginalDatetime(
      			ParserUtils.getString(atts.getValue("Date")) + 
      			ParserUtils.getString(atts.getValue("Time")));           
      detail.setOriginalTotalAmount(ParserUtils.getString(atts.getValue("Amount")));
      detail.setOriginalUsageBilledUnits(ParserUtils.getString(atts.getValue("BilledUnits")));
      detail.setOriginalUsageDuration(ParserUtils.getString(atts.getValue("Duration")));
      
      detail.setSeqNbr(ParserUtils.getInt(atts.getValue("ItemNo")));
      detail.setCaption(ParserUtils.getString(atts.getValue("ServicePlan")));
      detail.setTotalAmount(ParserUtils.getDouble(atts.getValue("Amount")));
      detail.setCarrierCode(ParserUtils.getString(atts.getValue("CarrierCode")));
      detail.setCarrierState(ParserUtils.getString(atts.getValue("CarrierState")));
      detail.setGroupId(ParserUtils.getString(atts.getValue("GroupID")));
      
      String billed  = atts.getValue("BilledUnits");
      String duration = atts.getValue("Duration"); 

      if(((duration == null) || (ParserUtils.getDouble(duration) == 0D)) &&
          (billed != null) ) {
        duration = billed;
      }
          
      detail.setUsageBilledUnits(ParserUtils.getUnitCounter(atts.getValue("UnitDescription"), billed));
      detail.setUsageDuration(ParserUtils.getUnitCounter(atts.getValue("UnitDescription"), duration));

      detail.setCallDirection(ParserUtils.getInt(atts.getValue("CallDirection")));
      detail.setCalledNumber(ParserUtils.getString(atts.getValue("NumberCalled")).replaceAll("-",""));
      detail.setCallIndicator(ParserUtils.getString(atts.getValue("CalIndicator")));    
      detail.setSvcId(ParserUtils.getString(atts.getValue("ServicePrice")));
      detail.setSvcPlan(ParserUtils.getString(atts.getValue("captionPhrase")));
      detail.setChannelId(ParserUtils.getString(atts.getValue("ChannelID")));
      
      detail.setFreeIndicator(ParserUtils.getString(atts.getValue("freeIndicator")));
      
//      if ( (null ==  ParserUtils.getDate(atts.getValue("Date"))) && (null !=  ParserUtils.getDate(atts.getValue("Time", "HHmmss"))) ) {
//      	detail.setDatetime(ParserUtils.getDate(atts.getValue("Time","HHmmss"))); 
//      } else if ( (null !=  ParserUtils.getDate(atts.getValue("Date"))) && (null ==  ParserUtils.getDate(atts.getValue("Time","HHmmss"))) ) {
//      	detail.setDatetime(ParserUtils.getDate(atts.getValue("Date")));      	
//      } else {
        detail.setDatetime(ParserUtils.getDateTime(atts.getValue("Date")+atts.getValue("Time")));
//      }

      detail.setOriginSID(ParserUtils.getString(atts.getValue("SIDOrigem")));
      
  	  /* GPORTUGA - 20071129 - BEGIN - Changes due to P8 new fields */
      if (atts.getValue("SplitChargeInfo") != null && atts.getValue("SplitChargeInfo").length() == 2) {
    	detail.setSplitPart(ParserUtils.getInt(atts.getValue("SplitChargeInfo").substring(0,1),1));
      	detail.setSplitTotal(ParserUtils.getInt(atts.getValue("SplitChargeInfo").substring(1,2),1));
      } else {
    	  detail.setSplitPart(1);
    	  detail.setSplitTotal(1);
      }
      
      detail.setRealDuration(ParserUtils.getUnitCounter("SECONDS", atts.getValue("RealCallDuration")));
      detail.setDisplacement(ParserUtils.getString(atts.getValue("DisplacementInd")).equals("S") ? true : false);
      detail.setTaxRate(ParserUtils.getDouble(atts.getValue("TaxRateTotalPct")));
      if((detail.getRealDuration() != null) && 	(detail.getDatetime() != null) &&
  			(detail.getRealDuration().getType().equals(UnitCounter.TIME_COUNTER))) {
  			Calendar cal = Calendar.getInstance();
  			cal.setTime(detail.getDatetime());
  			cal.add(Calendar.SECOND, (int)detail.getRealDuration().getSeconds());
  			detail.setRealEndDateTime(cal.getTime());
      }
      /* GPORTUGA - 20071129 - END */

      detail.setTariff(ParserUtils.getString(atts.getValue("Tariff")));
//      if((detail.getTariff() == null) || (detail.getTariff().length() == 0)) {
//    	  detail.setTariff(SIRSConstants.CALL_TARIFF_UNIQUE);
//      }
      
      detail.setOriginCity(getCity(ParserUtils.getString(atts.getValue("Origin"))));
      detail.setOriginState(getState(ParserUtils.getString(atts.getValue("Origin"))));

      if(localName.indexOf("INTERNATIONAL") < 0) {
        detail.setDestinationCity(getCity(ParserUtils.getString(atts.getValue("Destination"))));
        detail.setDestinationState(getState(ParserUtils.getString(atts.getValue("Destination"))));
      } else {
        detail.setDestinationCity(null);
        detail.setDestinationState(null);
        detail.setDestinationCountry(ParserUtils.getString(atts.getValue("Destination")));
      }
      
      SIRSUtils.populateCallClass(detail);
      if (((Section)manager.getContext()).getDetails().size() == 0) {
    	  previous = null;
      }
      ((Section)manager.getContext()).addDetail(detail);
      
      UsageDetail aggregate = manager.getHolder().addUsage(detail);
      if(aggregate != null) {   
      	if (aggregate.getObjectType().equals("A")) {
      		aggregate.setCallClass(SIRSConstants.CALL_CLASS_AGGREGATE);
      	}
    	  ((Section)manager.getContext()).addDetail(aggregate);
      }
       
    } catch (Exception e) {
      log.error("Error loading a Voice Usage Detail record", e);
      throw new SAXException(e);
    }
  }
  
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
	if (previous != null) {
		previous.setNextUsage(detail);
	}
	previous = detail;
    detail = null;
  }
  
  private String getCity(String val) {
    String ret = null;
    if ( (null == val) || (val.length() == 0) ) {
    	return "";
    }    
    if(val.indexOf('/') >= 0) {
      ret = ( (null == val.substring(0, val.indexOf('/'))) || (val.substring(0, val.indexOf('/')).length() == 0) )? "" : val.substring(0, val.indexOf('/')).trim();
      ret = val.substring(0, val.indexOf('/')).trim();      
    } else {
      ret = val;
    }
    return ret;
  }
  
  private String getState(String val) {
    String ret = null;
    if ( (null == val) || (val.length() == 0) ) {
    	return "";
    }
    if(val.indexOf('/') >= 0) {
    	if ( ((val.indexOf('/')+3) > val.length()) ) {
    		return "";
    	}
      ret = val.substring(val.indexOf('/')+1,val.indexOf('/')+3);
    } else {
      ret = "";
    }
    return ret;
  }
  
}
