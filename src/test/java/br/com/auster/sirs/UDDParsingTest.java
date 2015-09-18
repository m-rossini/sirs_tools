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
 * Created on 02/01/2007
 */
package br.com.auster.sirs;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import br.com.auster.common.xml.DOMUtils;
import br.com.auster.udd.reader.TaggedFileReader;

/**
 * @author mtengelm
 *
 */
public class UDDParsingTest extends TestCase {

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	protected String getFileName() {
		String name = "conf/sirs-definition.xml";		
		return name;		
	}
	
	public void testConfigure() {
		try {
			Element element = DOMUtils.openDocument(getFileName(), false);
	    TaggedFileReader reader = new TaggedFileReader(element);
    } catch (ParserConfigurationException e) {
	    e.printStackTrace();
	    fail("Problemas parsing da configuração.");
    } catch (SAXException e) {
    	fail("Problemas de XML ao inicializar reader.");
	    e.printStackTrace();
    } catch (IOException e) {    	
	    e.printStackTrace();
	    fail("Problemas de IO ao ler configuração.");
    } catch (GeneralSecurityException e) {
	    fail("Problemas de IO ao ler configuração.");
	    e.printStackTrace();
    }
	}
}
