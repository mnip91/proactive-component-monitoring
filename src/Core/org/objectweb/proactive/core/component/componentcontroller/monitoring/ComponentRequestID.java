/*
 * ################################################################
 *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2012 INRIA/University of
 *                 Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org or contact@activeeon.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 * ################################################################
 * $$PROACTIVE_INITIAL_DEV$$
 */
package org.objectweb.proactive.core.component.componentcontroller.monitoring;

import java.io.Serializable;

import org.objectweb.proactive.core.UniqueID;

/** 
 * Unique identifier for a request.
 * It's based on the sequenceNumber of the request, which is based
 * on bodyID hashcode + a sequenceID, which is included in the message.
 *  
 * @author cruz
 *
 */
public class ComponentRequestID implements Serializable {

	private Long reqID;
	
	public ComponentRequestID(long reqID) {
		this.reqID = reqID;
	}
	
	public long getComponentRequestID() {
		return reqID.longValue();
	}
	
	public String toString() {
		return ""+reqID.longValue();
	}
	
	/**
     * Overrides equals ...
     * @return true if and only if o is a ComponentRequestID with the same value that this ComponentRequestID
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof ComponentRequestID) {
            return this.reqID.longValue() == ((ComponentRequestID) o).getComponentRequestID();
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return reqID.hashCode();
    }
    

}
