/*****************************************************************************
 * Copyright (C) 2006 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 08/08/2006
 ****************************************************************************/

package com.nestor.shared.event;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.WeakHashMap;

import com.nestor.shared.util.ImmutableList;
import com.nestor.shared.util.PassiveIterator;

public class MultiPublisher
{
    private ImmutableList<Object> rootNode = null;

    private WeakHashMap<Class, Object> proxyCache = new WeakHashMap<Class, Object>();

    public synchronized void add(Object listener)
    {
        this.rootNode = new ImmutableList<Object>(this.rootNode, listener);
    }

    @SuppressWarnings("unchecked")
    public synchronized <T> T cast(Class<T> theInterface)
    {
        T castType = (T) this.proxyCache.get(theInterface);
        if (castType == null)
        {
            castType = (T) Proxy.newProxyInstance(theInterface.getClassLoader(), new Class[] {
                theInterface
            }, createInvocationHandler(theInterface));
            this.proxyCache.put(theInterface, castType);
        }
        return castType;
    }

    private <T> InvocationHandler createInvocationHandler(final Class<T> theInterface)
    {
        return new InvocationHandler()
        {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
            {
                // will eventually throw RuntimeException
                // wrapping exception of any type
                MultiPublisher.this.publish(theInterface, new InvocationIterator<T>(method, args));

                // listeners should not return result
                return null;
            }
        };
    }

    public <T> void publish(final Class<T> lClass, final PassiveIterator<T> iterator)
    {
        ImmutableList<Object> node = this.rootNode;
        if (node != null)
        {
            PassiveIterator<Object> invocationIterator = new PassiveIterator<Object>()
            {
                @SuppressWarnings("unchecked")
                public void next(Object n)
                {
                    if (n != null && lClass.isAssignableFrom(n.getClass()))
                    {
                        iterator.next((T) n);
                    }
                }
            };
            node.traverse(invocationIterator);
        }
    }

    public synchronized void remove(Object listener)
    {
        if (this.rootNode != null)
            this.rootNode = this.rootNode.remove(listener);
    }

    public synchronized void removeAll()
    {
        this.rootNode = null;
    }
}
