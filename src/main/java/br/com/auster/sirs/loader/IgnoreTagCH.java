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
 * Created on Jan 31, 2005
 */
package br.com.auster.sirs.loader;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * <p><b>Title:</b> IgnoreTagHandler</p>
 * <p><b>Description:</b> A content handler that does nothing</p>
 * <p><b>Copyright:</b> Copyright (c) 2004</p>
 * <p><b>Company:</b> Auster Solutions</p>
 *
 * @author etirelli
 * @version $Id: IgnoreTagCH.java 154 2007-05-27 23:50:16Z mtengelm $
 */

public class IgnoreTagCH implements ContentHandler {

	private static Logger log = Logger.getLogger(IgnoreTagCH.class);
  public IgnoreTagCH(LoadManager manager) {
    
  }
  
  /* (non-Javadoc)
   * @see org.xml.sax.ContentHandler#endDocument()
   */
  public void endDocument() throws SAXException {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see org.xml.sax.ContentHandler#startDocument()
   */
  public void startDocument() throws SAXException {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see org.xml.sax.ContentHandler#characters(char[], int, int)
   */
  public void characters(char[] ch, int start, int length) throws SAXException {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
   */
  public void ignorableWhitespace(char[] ch, int start, int length)
      throws SAXException {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
   */
  public void endPrefixMapping(String prefix) throws SAXException {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
   */
  public void skippedEntity(String name) throws SAXException {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
   */
  public void setDocumentLocator(Locator locator) {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
   */
  public void processingInstruction(String target, String data)
      throws SAXException {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
   */
  public void startPrefixMapping(String prefix, String uri) throws SAXException {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
   */
  public void endElement(String namespaceURI, String localName, String qName)
      throws SAXException {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void startElement(String namespaceURI, String localName, String qName,
      Attributes atts) throws SAXException {
  	log.debug("Ignoring TAG:" + localName);

  }

}
