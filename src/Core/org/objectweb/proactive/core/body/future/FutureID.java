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
package org.objectweb.proactive.core.body.future;

import java.io.Serializable;

import org.objectweb.proactive.core.UniqueID;


public class FutureID implements Serializable {

    /**
     * The ID of the "evaluator" of the future.
     */
    private UniqueID creatorID;

    /**
     * ID of the future
     * In fact, the sequence number of the request that generated this future
     */
    private long ID;

    public UniqueID getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(UniqueID creatorID) {
        this.creatorID = creatorID;
    }

    public long getID() {
        return ID;
    }

    public void setID(long id) {
        ID = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (int) (ID ^ (ID >>> 32));
        result = (prime * result) + ((creatorID == null) ? 0 : creatorID.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FutureID other = (FutureID) obj;
        if (ID != other.ID) {
            return false;
        }
        if (creatorID == null) {
            if (other.creatorID != null) {
                return false;
            }
        } else if (!creatorID.equals(other.creatorID)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[Future " + this.ID + " created by " + this.creatorID + "]";
    }
}
