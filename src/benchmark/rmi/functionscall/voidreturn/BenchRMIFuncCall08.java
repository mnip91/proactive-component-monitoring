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
package benchmark.rmi.functionscall.voidreturn;

import java.net.InetAddress;

import org.objectweb.proactive.core.node.NodeImpl;

import benchmark.rmi.functionscall.RMIFunctionCall;
import benchmark.util.ReifiableObject;


public class BenchRMIFuncCall08 extends RMIFunctionCall {
    public BenchRMIFuncCall08() {
    }

    public BenchRMIFuncCall08(NodeImpl node) {
        super(node, "RMI Functions Call  --> void f(ReifiableObject)",
            "Mesure the time of a call Function who return void with 1 ReifiableObject argument.");
    }

    public long action() throws Exception {
        
        
        BenchRMIFuncCall08 activeObject = (BenchRMIFuncCall08) getRmiObject();
        ReifiableObject o = new ReifiableObject();
        this.timer.start();
        activeObject.f(o);
        this.timer.stop();
        return this.timer.getCumulatedTime();
    }

    public void f(ReifiableObject o) throws Exception {
        o.toString();
        if (logger.isDebugEnabled()) {
            logger.debug(InetAddress.getLocalHost().getHostName());
        }
    }
}
