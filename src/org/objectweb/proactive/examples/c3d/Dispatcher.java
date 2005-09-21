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
package org.objectweb.proactive.examples.c3d;

import org.objectweb.proactive.core.util.wrapper.StringWrapper;
import org.objectweb.proactive.examples.c3d.geom.Vec;
import org.objectweb.proactive.examples.c3d.prim.Sphere;


/** Services proposed by a Dispatcher, without all the GUI stuff */
public interface Dispatcher {

    /**
     * Rotate every object by the given angle
     */
    public abstract void rotateScene(int i_user, Vec angles);

    public abstract void addSphere(Sphere s);

    public abstract void resetScene();

    /** Register a user, so he can join the fun */

    //SYNCHRONOUS CALL. All "c3duser." calls in this method happen AFTER the int[] is returned
    public abstract int[] registerUser(User c3duser, String userName);

    public abstract void registerMigratedUser(int userNumber);

    /** removes user from userList, so he cannot receive any more messages or images */
    public abstract void unregisterConsumer(int number);

    /** Get the list of users in an asynchronous call, entries being separated by \n */
    public abstract StringWrapper getUserList();

    /** Find the name of the machine this Dispatcher is running on */
    public abstract String getMachineName();

    /** Find the name of the OS the Dispatcher is running on */
    public abstract String getOSString();

    /** send message to all users except one */
    public abstract void userWriteMessageExcept(int i_user, String s_message);

    /** Shows a message to a user*/
    public abstract void userWriteMessage(int i_user, String s_message);
}
