/*****************************************************************************
 * Copyright (C) 2006 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 07/08/2006
 ****************************************************************************/

package com.nestor.shared.event;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.nestor.shared.util.ImmutableList;
import com.nestor.shared.util.PassiveIterator;

public class MonoPublisher<T>
{
    private T castType = null;

    private Class<T> listenerInterface;

    private ImmutableList<T> rootNode = null;

    public MonoPublisher(Class<T> listenerClass)
    {
        super();
        this.listenerInterface = listenerClass;
        if (!this.listenerInterface.isInterface())
        {
            throw new IllegalArgumentException("Provide an interface");
        }
    }

    public synchronized void add(T listener)
    {
        this.rootNode = new ImmutableList<T>(this.rootNode, listener);
    }

    @SuppressWarnings("unchecked")
    public synchronized T cast()
    {
        if (this.castType == null)
        {
            Class[] classes = new Class[] {
                this.listenerInterface
            };
            this.castType =
                    (T) Proxy.newProxyInstance(this.listenerInterface.getClassLoader(), classes,
                            createInvocationHandler());
        }
        return this.castType;
    }

    private InvocationHandler createInvocationHandler()
    {
        return new InvocationHandler()
        {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
            {
                // will eventually throw RuntimeException
                // wrapping exception of any type
                MonoPublisher.this.publish(new InvocationIterator<T>(method, args));

                // listeners do not return result
                return null;
            }
        };
    }

    public void publish(PassiveIterator<T> visitor)
    {
        ImmutableList<T> node = this.rootNode;
        if (node != null)
        {
            node.traverse(visitor);
        }
    }

    public synchronized void remove(T listener)
    {
        if (this.rootNode != null)
            this.rootNode = this.rootNode.remove(listener);
    }

    public synchronized void removeAll()
    {
        this.rootNode = null;
    }
}
