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

import java.io.File;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.objectweb.proactive.annotation.PublicAPI;
import org.objectweb.proactive.core.util.log.Loggers;
import org.objectweb.proactive.core.util.log.ProActiveLogger;
import org.objectweb.proactive.extensions.calcium.environment.FileServerClient;
import org.objectweb.proactive.extensions.calcium.exceptions.PanicException;
import org.objectweb.proactive.extensions.calcium.futures.CalFuture;
import org.objectweb.proactive.extensions.calcium.futures.CalFutureImpl;
import org.objectweb.proactive.extensions.calcium.skeletons.InstructionBuilderVisitor;
import org.objectweb.proactive.extensions.calcium.skeletons.Skeleton;
import org.objectweb.proactive.extensions.calcium.system.SkeletonSystemImpl;
import org.objectweb.proactive.extensions.calcium.system.files.FileStaging;
import org.objectweb.proactive.extensions.calcium.task.Task;
import org.objectweb.proactive.extensions.calcium.task.TaskPriority;


/**
 * A <code>Stream</code> is used to input parameters into the Calcium framework,
 * and collect results.
 *
 * All parameters inputed into the framework will execute the skeleton program
 * specified during the creation of the <code>Stream</code>.
 *
 * @author The ProActive Team
 */
@PublicAPI
public class Stream<T extends java.io.Serializable, R extends java.io.Serializable> {
    static Logger logger = ProActiveLogger.getLogger(Loggers.SKELETONS_KERNEL);
    private Facade facade;
    private Skeleton<T, R> skeleton;
    private int streamPriority;
    private FileServerClient fserver;
    private static File DEFAULT_OUTPUT_ROOT_DIR = SkeletonSystemImpl.newDirInTmp("calcium-output");
    BlockingQueue<CalFuture<R>> list;

    Stream(Facade facade, FileServerClient fserver, Skeleton<T, R> skeleton) {
        this.skeleton = skeleton;
        this.facade = facade;
        this.fserver = fserver;

        this.streamPriority = TaskPriority.DEFAULT_PRIORITY;
        this.list = new LinkedBlockingQueue<CalFuture<R>>();
    }

    public CalFuture<R> input(T param) throws PanicException {
        return input(param, DEFAULT_OUTPUT_ROOT_DIR);
    }

    /**
     * Inputs a new T to be computed.
     * @param param The T to be computed.
     * @param outputRootDir The root directory where files resulting from the computation are to be stored.
     * @throws PanicException
     */
    @SuppressWarnings("unchecked")
    public CalFuture<R> input(T param, File outputRootDir) throws PanicException {
        //Put the parameters in a Task container
        Task<T> task = new Task<T>(param);

        //Stage-in files (ie upload files)
        //Replace references to File with ProxyFile
        task = FileStaging.stageInput(fserver, task);

        InstructionBuilderVisitor builder = new InstructionBuilderVisitor();

        skeleton.accept(builder);

        task.setStack(builder.stack);
        task.setPriority(new TaskPriority(streamPriority));

        CalFutureImpl<R> future = new CalFutureImpl<R>(task.taskId, fserver, outputRootDir);
        facade.putTask(task, future);

        return future;
    }

    public List<CalFuture<R>> input(List<T> paramV) throws InterruptedException, PanicException {
        return input(paramV, DEFAULT_OUTPUT_ROOT_DIR);
    }

    /**
     * Inputs a list of T to be computed.
     *
     * @param paramV A list containing the T.
     * @param outputRootDir The root directory where files resulting from the computation are to be stored.
     * @return A list of futures {@link CalFuture} that will be updated with the results.
     * @throws PanicException
     * @throws InterruptedException
     */
    public List<CalFuture<R>> input(List<T> paramV, File outputRootDir) throws InterruptedException,
            PanicException {
        Vector<CalFuture<R>> vector = new Vector<CalFuture<R>>(paramV.size());
        for (T param : paramV)
            vector.add(input(param, outputRootDir));

        return vector;
    }

    /**
     * Inputs a new T to be computed. Once the computation is finished
     * the future holding the result will be stored in the blocking queue.
     *
     * @param param The parameter to compute
     * @param queue  The queue to put the future once the result is available.
     * @param outputRootDir The root directory where files resulting from the computation are to be stored.
     * @return The future  {@link CalFuture}  that will hold the result.
     *
     * @throws PanicException
     */
    @SuppressWarnings("unchecked")
    public void input(T param, BlockingQueue<CalFuture<R>> queue, File outputRootDir) throws PanicException {
        CalFutureImpl<R> future = (CalFutureImpl) this.input(param, outputRootDir);
        future.setCallBackQueue(queue);
    }

    public void input(T param, BlockingQueue<CalFuture<R>> queue) throws PanicException {
        input(param, queue, DEFAULT_OUTPUT_ROOT_DIR);
    }

    /**
     * Inputs a new T to be computed. Once the computation is finished
     * the future holding the result will be available by invoking the
     * {@link #retrieve(long, TimeUnit) waitForAny} method.
     * @param param  The parameter to compute
     * @param outputRootDir The root directory where files resulting from the computation are to be stored.
     * @throws PanicException
     */
    public void submit(T param, File outputRootDir) throws PanicException {
        @SuppressWarnings("unchecked")
        CalFutureImpl<R> future = (CalFutureImpl) this.input(param, outputRootDir);
        future.setCallBackQueue(list);
    }

    /**
     * Like {@link #submit(java.io.Serializable, File)},
     * but uses the default output directory.
     */
    public void submit(T param) throws PanicException {
        submit(param, DEFAULT_OUTPUT_ROOT_DIR);
    }

    /**
     * This method can be used to block for a future holding a result.
     * For each parameter submitted into the stream using the {@link #submit(T) submit} method,
     * a future holding the already available result can be obtained with this method.
     * @param timeout how long to wait before giving up, in units of
     * <tt>unit</tt>
     * @param unit a <tt>TimeUnit</tt> determining how to interpret the
     * <tt>timeout</tt> parameter
     * @return The first available result.
     * @throws InterruptedException
     */
    public CalFuture<R> retrieve(long timeout, TimeUnit unit) throws InterruptedException {
        return list.poll(timeout, unit);
    }

    /**
     * Like {@link #retrieve(long, TimeUnit) retrieve} but waits indefinitely.
     */
    public CalFuture<R> retrieve() throws InterruptedException {
        return list.take();
    }
}
