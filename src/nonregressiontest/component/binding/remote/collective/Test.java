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
package nonregressiontest.component.binding.remote.collective;

import nonregressiontest.component.I1;
import nonregressiontest.component.I2;
import nonregressiontest.component.Message;
import nonregressiontest.component.PrimitiveComponentB;
import nonregressiontest.component.PrimitiveComponentD;

import nonregressiontest.descriptor.defaultnodes.TestNodes;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.factory.GenericFactory;
import org.objectweb.fractal.api.type.ComponentType;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.fractal.api.type.TypeFactory;
import org.objectweb.fractal.util.Fractal;

import org.objectweb.proactive.core.component.Constants;
import org.objectweb.proactive.core.component.ContentDescription;
import org.objectweb.proactive.core.component.ControllerDescription;
import org.objectweb.proactive.core.group.ProActiveGroup;

import testsuite.test.FunctionalTest;


/**
 * @author Matthieu Morel
 * a test for bindings on client collective interfaces between remote components
 */
public class Test extends FunctionalTest {
    public static String MESSAGE = "-->Main";
    Component pD1;
    Component pB1;
    Component pB2;
    Message message;

    public Test() {
        super("Communication between remote primitive components through client collective interface",
            "Communication between remote primitive components through client collective interface ");
    }

    /**
     * @see testsuite.test.FunctionalTest#action()
     */
    public void action() throws Exception {
        System.setProperty("proactive.future.ac", "enable");
        // start a new thread so that automatic continuations are enabled for components
        ACThread acthread = new ACThread();
        acthread.start();
        acthread.join();
        System.setProperty("proactive.future.ac", "disable");
    }

    private class ACThread extends Thread {
        public void run() {
            try {
                Component boot = Fractal.getBootstrapComponent();
                TypeFactory type_factory = Fractal.getTypeFactory(boot);
                GenericFactory cf = Fractal.getGenericFactory(boot);

                ComponentType D_Type = type_factory.createFcType(new InterfaceType[] {
                            type_factory.createFcItfType("i1",
                                I1.class.getName(), TypeFactory.SERVER,
                                TypeFactory.MANDATORY, TypeFactory.SINGLE),
                            type_factory.createFcItfType("i2",
                                I2.class.getName(), TypeFactory.CLIENT,
                                TypeFactory.MANDATORY, TypeFactory.COLLECTION)
                        });
                ComponentType B_Type = type_factory.createFcType(new InterfaceType[] {
                            type_factory.createFcItfType("i2",
                                I2.class.getName(), TypeFactory.SERVER,
                                TypeFactory.MANDATORY, TypeFactory.SINGLE)
                        });

                // instantiate the components
                pD1 = cf.newFcInstance(D_Type,
                        new ControllerDescription("pD1", Constants.PRIMITIVE),
                        new ContentDescription(
                            PrimitiveComponentD.class.getName(),
                            new Object[] {},
                            TestNodes.getRemoteACVMNode()));
                pB1 = cf.newFcInstance(B_Type,
                        new ControllerDescription("pB1", Constants.PRIMITIVE),
                        new ContentDescription(
                            PrimitiveComponentB.class.getName(),
                            new Object[] {  }));
                pB2 = cf.newFcInstance(B_Type,
                        new ControllerDescription("pB2", Constants.PRIMITIVE),
                        new ContentDescription(
                            PrimitiveComponentB.class.getName(),
                            new Object[] {  }, TestNodes.getRemoteACVMNode()));

                // bind the components
                Fractal.getBindingController(pD1).bindFc("i2",
                    pB1.getFcInterface("i2"));
                Fractal.getBindingController(pD1).bindFc("i2",
                    pB2.getFcInterface("i2"));

                // start them
                Fractal.getLifeCycleController(pD1).startFc();
                Fractal.getLifeCycleController(pB1).startFc();
                Fractal.getLifeCycleController(pB2).startFc();

                message = null;
                I1 i1 = (I1) pD1.getFcInterface("i1");
                Message msg1 = i1.processInputMessage(new Message(MESSAGE));
                message = msg1.append(MESSAGE);
            } catch (Exception e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * @see testsuite.test.AbstractTest#initTest()
     */
    public void initTest() throws Exception {
    }

    /**
     * @see testsuite.test.AbstractTest#endTest()
     */
    public void endTest() throws Exception {
    }

    public boolean postConditions() throws Exception {
        StringBuffer resulting_msg = new StringBuffer();
        int message_size = ProActiveGroup.size(message);
        for (int i = 0; i < message_size; i++) {
            resulting_msg.append(((Message) ProActiveGroup.get(message, i)).toString());
        }

        // this --> primitiveA --> primitiveB --> primitiveA --> this  (message goes through composite components)
        String single_message = Test.MESSAGE + PrimitiveComponentD.MESSAGE +
            PrimitiveComponentB.MESSAGE + PrimitiveComponentD.MESSAGE +
            Test.MESSAGE;

        return resulting_msg.toString().equals(single_message + single_message);
    }

    public static void main(String[] args) {
        Test test = new Test();
        try {
            test.action();
            test.postConditions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
