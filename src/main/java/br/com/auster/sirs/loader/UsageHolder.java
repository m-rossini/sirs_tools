/*
 * Copyright (c) 2004-2005 Auster Solutions. All Rights Reserved.
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
 * Created on Aug 29, 2005
 */
package br.com.auster.sirs.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.auster.om.invoice.UsageDetail;

/**
 * <p><b>Title:</b> UsageHolder</p>
 * <p><b>Description:</b> A helper class to help classifying partial/complete events</p>
 * <p><b>Copyright:</b> Copyright (c) 2004-2005</p>
 * <p><b>Company:</b> Auster Solutions</p>
 *
 * @author etirelli
 * @version $Id: UsageHolder.java 107 2006-11-15 14:03:30Z mtengelm $
 */
public class UsageHolder {
	private Map usageMap = new HashMap();
	
	/**
	 * Register a new UsageDetail object in the internal structures checking
	 * for usage event breaks to aggregate then again 
	 * @param detail
	 * @return
	 */
	public UsageDetail addUsage(UsageDetail detail) {
		UsageDetail aggregate = null;
		// getting dateMap <usageDatetime, List<usageDetail>>
		Map dateMap = (Map) usageMap.get(detail.getChannelId());
		if(dateMap == null) {
			dateMap = new HashMap();
			usageMap.put(detail.getChannelId(), dateMap);
		}
		// getting usageList
		List usageList = (List) dateMap.get(detail.getDatetime());
		if(usageList == null) {
			usageList = new ArrayList();
			dateMap.put(detail.getDatetime(), usageList);
		}
		
		// if list is empty, first object for that datetime
		if(usageList.isEmpty()) {
			detail.setObjectType(UsageDetail.OBJECT_TYPE_COMPLETE);
			usageList.add(detail);
		} else {
			// else, iterate the usage list to find matches for the given usage detail
			for(Iterator i = usageList.iterator(); i.hasNext(); ) {
				UsageDetail existing = (UsageDetail) i.next();
				
				// if the conditions are met, then the usage event is part of the same event
				if( (null != existing.getDatetime()) && existing.getDatetime().equals(detail.getDatetime()) &&
				   existing.getCalledNumber().equals(detail.getCalledNumber()) &&
				   (!existing.getSvcId().equals(detail.getSvcId()))) {
					
					// if existing is already an aggregate, just add the new event as part of it
					if(existing.getObjectType().equals(UsageDetail.OBJECT_TYPE_AGGREGATE)) {
						detail.setObjectType(UsageDetail.OBJECT_TYPE_PARTIAL);
						existing.addPart(detail);
					// otherwise, create an aggregate event and add both to it
					} else {
						aggregate = (UsageDetail) existing.clone();
						aggregate.getDuration().reset();
						aggregate.setObjectType(UsageDetail.OBJECT_TYPE_AGGREGATE);
						aggregate.setTariff(null);
						aggregate.setCallClass(null);
						aggregate.setCallSubclass(null);
						aggregate.setTariffClass(null);
						aggregate.setPartNumber(0);
						aggregate.setTotalAmount(0);
						aggregate.addPart(existing);
						aggregate.addPart(detail);
						// removing previous object from list
						i.remove();
					}
					break;
				}
			}
			if(aggregate != null) {
				usageList.add(aggregate);
			} else {
				usageList.add(detail);
			}
		}
		return aggregate;
	}
	
	public void clean() {
		usageMap.clear();
	}

}
