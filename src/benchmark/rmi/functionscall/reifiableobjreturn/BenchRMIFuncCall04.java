/*
* ################################################################
*
* ProActive: The Java(TM) library for Parallel, Distributed,
*            Concurrent computing with Security and Mobility
*
* Copyright (C) 1997-2002 INRIA/University of Nice-Sophia Antipolis
* Contact: proactive-support@inria.fr
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
* USA
*
*  Initial developer(s):               The ProActive Team
*                        http://www.inria.fr/oasis/ProActive/contacts.html
*  Contributor(s):
*
* ################################################################
*/
package benchmark.rmi.functionscall.reifiableobjreturn;

import java.net.InetAddress;

import org.objectweb.proactive.core.node.NodeImpl;

import benchmark.rmi.functionscall.RMIFunctionCall;
import benchmark.util.ReifiableObject;

/**
 * @author Alexandre di Costanzo
 *
 */
public class BenchRMIFuncCall04 extends RMIFunctionCall {
    public BenchRMIFuncCall04() {
    }

    public BenchRMIFuncCall04(NodeImpl node) {
        super(node, "RMI Functions Call  --> ReifiableObject f(int, int, int)",
            "Mesure the time of a call Function who return ReifiableObject with 3 int arguments.");
    }

    public long action() throws Exception {
        
        
        BenchRMIFuncCall04 activeObject = (BenchRMIFuncCall04) getRmiObject();
        this.timer.start();
        activeObject.f(1, 2, 3);
        this.timer.stop();
        return this.timer.getCumulatedTime();
    }

    public ReifiableObject f(int i, int j, int k) throws Exception {
        i++;
        j++;
        k++;
        if (logger.isDebugEnabled()) {
            logger.debug(InetAddress.getLocalHost().getHostName());
        }
        return new ReifiableObject();
    }
}
