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
 * Created on Feb 3, 2005
 */
package br.com.auster.sirs.loader;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import br.com.auster.om.invoice.RedebitDetail;
import br.com.auster.om.invoice.Section;
import br.com.auster.om.util.ParserUtils;

/**
 * <p><b>Title:</b> RedebitDetailCH</p>
 * <p><b>Description:</b> </p>
 * <p><b>Copyright:</b> Copyright (c) 2004</p>
 * <p><b>Company:</b> Auster Solutions</p>
 *
 * @author etirelli
 * @version $Id: RedebitDetailCH.java 154 2007-05-27 23:50:16Z mtengelm $
 */

public class RedebitDetailCH implements ContentHandler {
  Logger log = Logger.getLogger(RedebitDetailCH.class);
  //I18n i18n = I18n.getInstance(RedebitDetailCH.class); 	
  LoadManager manager = null;
  RedebitDetail detail = null;
  
  public RedebitDetailCH(LoadManager manager) {
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
      detail = new RedebitDetail();
      detail.setTag(ParserUtils.getString(atts.getValue("key")));
      detail.setElementType(localName);
      log.debug("Processing:" + detail.getTag() + "@" + detail.getElementType());
      
      detail.setSeqNbr(ParserUtils.getInt(atts.getValue("ItemNo")));
      detail.setCaption(ParserUtils.getString(atts.getValue("TextoLivre")));
      detail.setTotalAmount(ParserUtils.getDouble(atts.getValue("Amount")));
      detail.setCarrierCode(ParserUtils.getString(atts.getValue("CarrierCode")));
      detail.setCarrierState(ParserUtils.getString(atts.getValue("CarrierState")));
      detail.setGroupId(ParserUtils.getString(atts.getValue("GroupID")));
      detail.setEventType(atts.getValue("TipoEvento"));
      detail.setUnits(ParserUtils.getUnitCounter(detail.getEventType(),atts.getValue("Unidades")));
      detail.setCallDirection(ParserUtils.getInt(atts.getValue("CallDirection")));
      detail.setCalledNbr(ParserUtils.getString(atts.getValue("CalledNo")).replaceAll("-",""));
      detail.setCallingNbr(ParserUtils.getString(atts.getValue("CallingNo")).replaceAll("-",""));
      detail.setAccessNbr(ParserUtils.getString(atts.getValue("PhoneNo")).replaceAll("-",""));
      detail.setOriginCity(ParserUtils.getString(atts.getValue("CidadeOrigem")));
      detail.setOriginState(ParserUtils.getString(atts.getValue("EstadoOrigem")));
      detail.setDestinationCity(ParserUtils.getString(atts.getValue("CidadeDestino")));
      detail.setDestinationState(ParserUtils.getString(atts.getValue("EstadoDestino")));
      
      
      String tempData = atts.getValue("DataEvento");
      String tempTime = atts.getValue("HoraEvento");
      if (((tempData != null) && !tempData.equals(""))
               && ((tempTime != null) && !tempTime.equals(""))) {
            detail.setDatetime(ParserUtils.getDateTime(tempData + tempTime));

         }      
      detail.setServicePlan(ParserUtils.getString(atts.getValue("ServicePlan")));
      detail.setInternational(ParserUtils.getBoolean(atts.getValue("InternationalInd")));
      detail.setInternational(ParserUtils.getBoolean(atts.getValue("RoamingInd")));
      
      ((Section)manager.getContext()).addDetail(detail);
    } catch (Exception e) {
      log.fatal("Error processing ELEMENT above.",e);
      e.printStackTrace();
      throw new SAXException(e);
    }
  }
  
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    detail = null;
  }
}
