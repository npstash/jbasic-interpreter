/*****************************************************************************
 * Copyright (C) 2006 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 27/07/2006
 ****************************************************************************/

package com.nestor.shared.module;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import com.nestor.shared.resource.Guard;
import com.nestor.shared.resource.InvocationGuard;
import com.nestor.shared.util.NetNode;
import com.nestor.shared.util.Node;
import com.nestor.shared.util.PassiveIterator;
import com.nestor.shared.util.Traversable;
import com.nestor.shared.util.Node.TraverseDirection;

public class ModuleManager implements Iterable<Module>, Traversable<Module>
{

    /**
     * Used to build dependency net.
     */
    private class ModuleNode extends NetNode<Module>
    {
        /**
         * 
         */
        int usesCnt = 0;

        ModuleNode(Module sc)
        {
            super(sc);
        }
    }

    /**
     * This iterator traverses a net of possibly repeating modules, and adds each of them to a set,
     * only once.
     */
    private static abstract class SetBuilderIterator implements PassiveIterator<Module>
    {
        private Set<Module> visited = new HashSet<Module>();

        SetBuilderIterator()
        {
            /* Empty */
        }

        public void next(Module n)
        {
            if (!this.visited.contains(n))
            {
                visitOnce(n);
                this.visited.add(n);
            }
        }

        public abstract void visitOnce(Module s);
    }

    /**
     * Will be used as a root for module dependency forest
     */
    private static Module dummy = new Module()
    {
        public void shutdown()
        {
            /* Empty */
        }

        public void startup(DependencyCache dc)
        {
            /* Empty */
        }
    };

    private static Traversable instance = null;

    public static synchronized Traversable getInstance()
    {
        if (instance == null)
        {
            instance = new ModuleManager();
        }
        return instance;
    }

    /**
     * Forest of module dependencies.
     */
    private Node<Module> dependencies = null;

    Map<Module, ModuleNode> registeredModules = null;

    /**
     * Throws an exception when cyclic dependency is detected.
     * 
     * @param root
     * @throws DependencyException
     */
    void detectDependencyLoop(final Node<Module> root) throws DependencyException
    {
        root.traverse(new PassiveIterator<Module>()
        {
            int count = 0;

            public void next(Module node)
            {
                if (node == root.get())
                {
                    this.count++;
                }
                if (this.count > 1)
                {
                    throw new DependencyException(node);
                }
            }
        }, TraverseDirection.DEFAULT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Iterable#iterator()
     */
    public synchronized Iterator<Module> iterator()
    {
        HashSet<Module> currentSet = new HashSet<Module>(this.registeredModules.keySet());
        final Iterator<Module> i = currentSet.iterator();
        return new Iterator<Module>()
        {
            public boolean hasNext()
            {
                return i.hasNext();
            }

            public Module next()
            {
                return i.next();
            }

            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }

    private synchronized Node<Module> rebuildDependencies(Collection<ModuleNode> modules)
    {
        Node<Module> forest = new NetNode<Module>(dummy);
        for (ModuleNode module : modules)
        {
            if (module.usesCnt == 0)
            {
                forest.append(module);
            }
        }
        return forest;
    }

    synchronized void registerModule(Module m, DependencyCache cache)
    {
        if (this.registeredModules == null)
        {
            this.registeredModules = new HashMap<Module, ModuleNode>();
        }
        if (!this.registeredModules.containsKey(m))
        {
            this.registeredModules.put(m, new ModuleNode(m));
            m.startup(cache);
        }
    }

    /**
     * This method should be invoked by application to notify all modules of shutdown. All modules
     * will be notified in respect to dependency tree - the independent modules will be notified
     * last. After this method returns, module cache is cleared.
     */
    public synchronized void shutdown()
    {
        if (this.dependencies != null)
        {
            this.dependencies.traverse(new SetBuilderIterator()
            {
                @Override
                public void visitOnce(Module comp)
                {
                    comp.shutdown();
                }
            }, TraverseDirection.BOTTOM_UP);

            this.dependencies = null;
            this.registeredModules.clear();
        }
    }

    /**
     * Starts the module manager. The Starter implementation should contain code which registers
     * modules. After registering modules, every of them will be asked for its dependencies. The
     * least dependat module will be then started first (dependency tree is built).
     * 
     * @param module
     *            first, independent module
     */
    public synchronized void start(final Module module)
    {
        // dependency cache updates number of uses for every module
        // and checks if there are no circular dependencies
        DependencyCache dc = new DependencyCache()
        {
            // desctibes module which is now being registered
            Stack<Module> currentlyRegistering = new Stack<Module>();

            {
                this.currentlyRegistering.push(module);
            }

            public void addDependency(Module to) throws DependencyException
            {
                // check if registered. If not, add to repository
                if (!ModuleManager.this.registeredModules.containsKey(to))
                {
                    // register and invoke startup()
                    this.currentlyRegistering.push(to);
                    registerModule(to, this);
                    this.currentlyRegistering.pop();
                } else if (this.currentlyRegistering.peek() != null)
                {
                    // now ask for dependencies
                    ModuleNode asked =
                            ModuleManager.this.registeredModules.get(this.currentlyRegistering
                                    .peek());
                    ModuleNode toNode = ModuleManager.this.registeredModules.get(to);
                    asked.usesCnt++;
                    toNode.append(asked);
                    detectDependencyLoop(toNode);
                }
            }
        };

        // dependency cache is guarded to prevent module form attempts to
        // modify dependency cache after returning from this method. (such
        // dependencies would not work, since they need rebuilding)
        Guard<DependencyCache> cacheGuard = new InvocationGuard<DependencyCache>(dc);
        DependencyCache guardedDC = cacheGuard.open();

        // register module in argument. This module can register other modules
        // by using the received DependencyCache
        registerModule(module, guardedDC);

        // further modification of dependency cache is excluded
        // until next invocation of start() (which is synchronized)
        cacheGuard.close();

        // rebuild dependencies into structure
        this.dependencies = rebuildDependencies(this.registeredModules.values());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nestor.commons.module.Traversable#traverse(com.nestor.commons.util.PassiveIterator)
     */
    public synchronized void traverse(final PassiveIterator<Module> iterator)
    {
        if (this.dependencies != null)
        {
            this.dependencies.traverse(new SetBuilderIterator()
            {
                @Override
                public void visitOnce(Module comp)
                {
                    iterator.next(comp);
                }
            }, TraverseDirection.TOP_DOWN);
        }
    }
}
