/*
 * ################################################################
 *
 * ProActive: The Java(TM) library for Parallel, Distributed,
 *            Concurrent computing with Security and Mobility
 *
 * Copyright (C) 1997-2007 INRIA/University of Nice-Sophia Antipolis
 * Contact: proactive@objectweb.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version
 * 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 * ################################################################
 */
package org.objectweb.proactive.core.component.group;

import java.util.List;

import org.objectweb.proactive.core.group.DispatchBehavior;
import org.objectweb.proactive.core.mop.MethodCall;


public class ComponentDispatchControllerProxy implements DispatchBehavior {

    DispatchBehavior dispatchController;

    // @Override
    // public int getExpectedNumberOfTasks(MethodCall originalMethodCall,
    // List<MethodCall> generatedMethodCalls, int nbWorkers) {
    // return dispatchController.getExpectedNumberOfTasks(originalMethodCall,
    // generatedMethodCalls, nbWorkers);
    // }

    public List<Integer> getTaskIndexes(MethodCall originalMethodCall, List<MethodCall> generatedMethodCalls,
            int nbWorkers) {
        return dispatchController.getTaskIndexes(originalMethodCall, generatedMethodCalls, nbWorkers);
    }

    //	public void setContext(String context) {
    ////		try {
    //		    throw new RuntimeException("set context ");
    ////			Object o = ((ComponentBody) PAActiveObject
    ////					.getBodyOnThis()).getProActiveComponentImpl()
    ////					.getFcInterface(context);
    ////			dispatchController = (AllocationBehavior)o;
    //////			dispatchController = (AllocationBehavior) ((ComponentBody) ProActiveObject
    //////					.getBodyOnThis()).getProActiveComponentImpl()
    //////					.getFcInterface(context);
    ////		} catch (NoSuchInterfaceException e) {
    ////			e.printStackTrace();
    ////		}
    //	}

}