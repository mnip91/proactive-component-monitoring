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
package org.objectweb.proactive.extensions.calcium;

import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.objectweb.proactive.api.PAFuture;
import org.objectweb.proactive.core.util.log.Loggers;
import org.objectweb.proactive.core.util.log.ProActiveLogger;
import org.objectweb.proactive.extensions.calcium.exceptions.PanicException;
import org.objectweb.proactive.extensions.calcium.futures.CalFutureImpl;
import org.objectweb.proactive.extensions.calcium.task.Task;
import org.objectweb.proactive.extensions.calcium.task.TaskId;
import org.objectweb.proactive.extensions.calcium.task.TaskPool;


/**
 * This class provides a Facade access to the kernel.
 * Since the kernel handles tasks for multiple streams at the same
 * time, this class is in charge of redirecting:
 *  -Tasks from streams into the taskpool
 *  -Tasks coming out from the taskpool into their respective streams.
 *
 * @author The ProActive Team
 */
class Facade {
    static Logger logger = ProActiveLogger.getLogger(Loggers.SKELETONS_KERNEL);
    private TaskPool taskpool;
    private FutureUpdateThread results;
    private PanicException panic;
    private int counter;

    Facade(TaskPool taskpool) {
        this.taskpool = taskpool;
        this.results = new FutureUpdateThread();
        this.panic = null;
        this.counter = 0;
    }

    synchronized void putTask(Task<?> task, CalFutureImpl<?> future) throws PanicException {
        task.taskId.setFamilyId(counter * (-1));
        counter++;
        taskpool.addReadyRootTask(task);
        results.put(future);
    }

    synchronized void boot() {
        results.start();
        notifyAll();
    }

    /**
     * This class stores references to the futures, and updates
     * the references once the results are available.
     *
     * @author The ProActive Team
     */
    class FutureUpdateThread extends Thread {
        Hashtable<TaskId, CalFutureImpl<?>> pending;
        boolean shutdown;

        public FutureUpdateThread() {
            pending = new Hashtable<TaskId, CalFutureImpl<?>>();
            shutdown = false;
        }

        /**
         * Stores a future in the strucutre.
         * The stream id must be stored inside the task before storing it.
         * @param task The task to store.
         */
        synchronized void put(CalFutureImpl<?> future) {
            TaskId taskId = future.getTaskId();

            if (pending.containsKey(taskId)) {
                logger.error("Future already registered for task=" + taskId);
                return;
            }

            pending.put(taskId, future);
        }

        private synchronized void updateFuture(Task<?> task) {
            if (!pending.containsKey(task.taskId)) {
                logger.error("No future is waiting for task:" + task.taskId.value());
                return;
            }

            CalFutureImpl<?> future = pending.remove(task.taskId);
            future.setFinishedTask(task);
        }

        /**
         * @return The number of total elements in this structure.
         */
        synchronized int size() {
            return pending.size();
        }

        @Override
        public void run() {
            //TODO add termination condition
            while ((panic == null) || shutdown) {
                Task<?> taskResult = null;
                try {
                    taskResult = taskpool.getResult();
                } catch (PanicException ex) {
                    //logger.error("Facade has encounterd skernel panic! Stopping future update thread");
                    ex.printStackTrace();
                    panic = ex;
                    //TODO update all the remaining future with the panic exception
                    return;
                }

                //TODO Temporary ProActive generics bug workaround
                //This is the supelec trick
                taskResult = (Task<?>) PAFuture.getFutureValue(taskResult);
                results.updateFuture(taskResult);
            }
        }

        public void shutdown() {
            shutdown = true;
        }
    }

    public void shutdown() {
        results.shutdown();
        //TODO finish the shutdown
    }
}
