/*
 * ################################################################
 *
 * ProActive: The Java(TM) library for Parallel, Distributed,
 *            Concurrent computing with Security and Mobility
 *
 * Copyright (C) 1997-2004 INRIA/University of Nice-Sophia Antipolis
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
 *  Initial developer(s): Eric Bruneton
 *  Contributor(s): Matthieu Morel
 *
 * ################################################################
 */
package org.objectweb.proactive.core.component.adl;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.fractal.adl.Factory;
import org.objectweb.fractal.adl.FactoryFactory;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.LifeCycleController;
import org.objectweb.fractal.util.Fractal;
import org.objectweb.proactive.ProActive;
import org.objectweb.proactive.core.ProActiveException;
import org.objectweb.proactive.core.component.adl.behaviour.gui.BehaviourGUI;
import org.objectweb.proactive.core.component.adl.component.ADL2NComponent;
import org.objectweb.proactive.core.descriptor.data.ProActiveDescriptor;


/**
 * A class to launch a component from its definition from the command line. <br>
 * Usage: Launcher [-java|-fractal] &lt;definition&gt; [ &lt;itf&gt; ]
 * [deployment-descriptor]) where &lt;definition&gt; is the name of the
 * component to be instantiated and started, &lt;itf&gt; is the name of its
 * Runnable interface, if it has one, and deployment-descriptor the location of
 * the ProActive deployment descriptor to use. <br>
 * It is also possible to use this class directly from its static main method.
 */
public class ADL2NLauncher {
    static ProActiveDescriptor deploymentDescriptor;
    public static HashMap componentsMap = new HashMap();
    private ADL2NLauncher() {
    }

    public static void main(final String[] args) throws Exception {
    	System.setProperty("fractal.provider","org.objectweb.proactive.core.component.Fractive");
    	//System.setProperty("java.security.policy","org/objectweb/proactive/core/component/adl/ADL2N.policy");
    	//System.setProperty("log4j.configuration","org/objectweb/proactive/core/component/adl/ADL2N_log.conf");
        String[] pargs = parseArgs(args);
        
        System.out.println("-------------------------------------");
        System.out.println("------------GUI Launcher-------------");
        System.out.println("-------------------------------------\n");
        BehaviourGUI gui = new BehaviourGUI(pargs);
        
        System.out.println("-------------------------------------");
        System.out.println("-----------Parse de l'ADL------------");
        System.out.println("-------------------------------------\n");
        Object comp = createComponent(pargs);
        System.out.println();
        if (comp instanceof Component) {
        
            LifeCycleController lc = null;
            try {
                lc = Fractal.getLifeCycleController((Component) comp);
            } catch (NoSuchInterfaceException ignored) {
            }
            if (lc != null) {
                lc.startFc();
            }
            Runnable r = null;
            try {
                r = (Runnable) ((Component) comp).getFcInterface(pargs[2]);
            } catch (NoSuchInterfaceException ignored) {
            }
            if (r != null) {
                r.run();
            }
        } else {
            if (comp instanceof LifeCycleController) {
                ((LifeCycleController) comp).startFc();
            }
            if (comp instanceof Runnable) {
                ((Runnable) comp).run();
            }
        }
        System.out.println("-------------------------------------");
        System.out.println("-Production de l'arbre de composants-");
        System.out.println("-------------------------------------\n");

        gui.loadComponent((ADL2NComponent)comp);
        
        System.out.println(comp.toString());
       
        System.out.println("\n\n------------------------------");
        System.out.println("-------Production de FC2------");
        System.out.println("------------------------------\n");

        /*
         * out = new FileOutputStream("myfile.txt");
         * new PrintStream(out);
         */
        //new Fc2Decompiler((LotosComponentImpl)comp,System.out).decompile(false);
        
        
        //System.out.println("\n\n------------------------------");
        //System.out.println("-Production de FC2paramétré--");
        //System.out.println("------------------------------\n");

        /*
         * out = new FileOutputStream("myfile.txt");
         * new PrintStream(out);
         */
       // new Fc2Decompiler((LotosComponentImpl)comp,System.out).decompile(true);
        
        //new LotosDecompiler((LotosComponentImpl)comp,System.out).decompile();
        
    }

    public static Object createComponent(String[] pargs)
        throws Exception {
        if (pargs[0].equals("-java")) {
            Factory f = FactoryFactory.getFactory(FactoryFactory.JAVA_BACKEND);
            return ((Map) f.newComponent(pargs[1], new HashMap())).get(pargs[2]);
        } else {
            Factory f;
            if ("org.objectweb.proactive.core.component.Fractive".equals(System.getProperty("fractal.provider"))) {
                // return the ProActive factory as defined in
                // org.objectweb.proactive.core.component.adl.FactoryFactory
                f = org.objectweb.proactive.core.component.adl.ADL2NFactoryFactory.getFactory();
            } else {
                f = FactoryFactory.getFactory(FactoryFactory.FRACTAL_BACKEND);
            }
            //f = org.objectweb.proactive.core.component.adl.FactoryFactory.getFactory();
            
//          PROACTIVE
            if (pargs[3] != null) {
                deploymentDescriptor = ProActive.getProactiveDescriptor(pargs[3]);
                HashMap context = new HashMap(1);
                context.put("deployment-descriptor", deploymentDescriptor);
                return f.newComponent(pargs[1], context);
            } else {
                try {
                    return f.newComponent(pargs[1], new HashMap());
                } catch (ClassCastException e) {
                    if (e.getMessage().indexOf("attribute_controller_representative")!=(-1)) {
                        System.out.println(
                            "Error while parsing the ADL. This could be due to the setting of attributes without implementing AttributeController. ");
                        throw e;
                    } else {
                    	e.printStackTrace();
                    	return null;
                    }
                }
            }
        }
    }

    private static String[] parseArgs(final String[] args) {
        if ((args.length < 1) || (args.length > 4)) {
            parseError();
        }

        // PROACTIVE added a parameter for the deployment descriptor
        String[] result = new String[4];
        if (args[0].equals("-java") || args[0].equals("-fractal")) {
            if (args.length < 2) {
                parseError();
            }
            result[0] = args[0];
            result[1] = args[1];
            result[2] = (args.length == 3) ? args[2] : "run";
            result[3] = (args.length == 4) ? args[3] : null;
        } else {
            result[0] = "-java";
            result[1] = args[0];
            result[2] = (args.length >= 2) ? args[1] : "run";
        }
        return result;
    }

    private static void parseError() {
        System.out.println(
            "Usage: Launcher [-java|-fractal] <definition> [ <itf> ] [deployment-descriptor]");
        System.out.print(
            "where <definition> is the name of the component to be ");
        System.out.print("instantiated and started,\n <itf> is the name of ");
        System.out.println("its Runnable interface, if it has one,");
        System.out.println(
            "\nand [deployment-descriptor] is the deployment descriptor that should be used for ProActive");
        System.exit(1);
    }

    public static void killNodes(boolean softly) throws ProActiveException {
        deploymentDescriptor.killall(softly);
    }
}
