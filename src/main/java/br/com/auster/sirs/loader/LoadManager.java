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

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.Logger;
import org.xml.sax.ContentHandler;

import br.com.auster.om.invoice.Account;
import br.com.auster.om.invoice.InvoiceModelObject;
import br.com.auster.om.invoice.Section;

/**
 * <p><b>Title:</b> ContentHandlerFactory</p>
 * <p><b>Description:</b> </p>
 * <p><b>Copyright:</b> Copyright (c) 2004</p>
 * <p><b>Company:</b> Auster Solutions</p>
 *
 * @author etirelli
 * @version $Id: LoadManager.java 185 2007-11-20 15:44:04Z mtengelm $
 */
public class LoadManager {
  Logger log = Logger.getLogger(LoadManager.class);
  //I18n i18n = I18n.getInstance(LoadManager.class); 	
  // Context stack - this stack will keep track of the
  // processing trail
  private Stack contextStack = new Stack(); 
  private Section summarySection = null;
  
  // Map<String accountNbr, Account account> of parsed accounts
  private Map accountMap = new HashMap();
  
  // Map<String className, ContentHandler handler> of handler instances
  private Map handlers = new HashMap();
  
  // A Map<String tag, ContentHandler handler> of handlers for
  // each tag
  private Map tagMap = new HashMap();
  
  // A usage holder to handle usage event breakings detection 
  private UsageHolder holder = new UsageHolder();
  
  public static final String IGNORE         	      	= "IGNORE";
  
  public LoadManager(Map tags) throws IllegalArgumentException, SecurityException,
                                             InstantiationException, IllegalAccessException, 
                                             InvocationTargetException, NoSuchMethodException, 
                                             ClassNotFoundException {
    // adding default map for ignored tags
    ContentHandler ignore    = new IgnoreTagCH(this);
    tagMap.put(IGNORE, ignore);

    for(Iterator i=tags.entrySet().iterator(); i.hasNext(); ) {
      Map.Entry entry = (Map.Entry) i.next();
      String tagName = (String) entry.getKey();
      String className = (String) entry.getValue();
      
      ContentHandler handler = (ContentHandler) handlers.get(className);
      if(handler == null) {
        handler = (ContentHandler) Class.forName(className)
                   .getConstructor(new Class[] {LoadManager.class})
                   .newInstance(new Object[] {this});
        handlers.put(className, handler);
      }
      tagMap.put(tagName, handler);
    }
  
  }
  
  /**
   * Returns the assigned ContentHandler for the specific tag
   * @param tag
   * @return
   */
  public ContentHandler getHandler(String tag) {
    ContentHandler handler = (ContentHandler) tagMap.get(tag);
    if(handler == null) {
      handler = (ContentHandler) tagMap.get(IGNORE);
    }
    return handler;
  }
  
  /**
   * Pushes a new object in the context stack
   * @param object
   */
  public void pushContext(InvoiceModelObject object) {
    contextStack.push(object);
  }
  
  /**
   * Pops an object from the context stack 
   * @return
   */
  public InvoiceModelObject popContext() {
    return (InvoiceModelObject) contextStack.pop();
  }
  
  /**
   * Returns the current context object from the context stack
   * @return
   */
  public InvoiceModelObject getContext() {
    return (InvoiceModelObject) contextStack.peek();
  }
  
  /**
   * Adds an account to the account list (with all related objects)
   * @param account
   */
  public void addAccount(Account account) {
    log.info("Account added: "+account.getAccountNumber());
    accountMap.put(account.getAccountNumber(), account);
  }
  
  /**
   * Returns the account Map of parsed accounts 
   * @return
   */
  public Map getObjects() {
    return accountMap;
  }

  /**
   * @return Returns the summarySection.
   */
  public Section getSummarySection() {
    return summarySection;
  }
  /**
   * @param summarySection The summarySection to set.
   */
  public void setSummarySection(Section summarySection) {
    log.debug("Setting summary section to = "+summarySection);
    this.summarySection = summarySection;
  }
  
  public UsageHolder getHolder() {
	return this.holder;  
  }
  
  public void clearObjects() {
    accountMap.clear();
    holder.clean();
  }
  
}
