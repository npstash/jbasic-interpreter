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

package com.nestor.shared.resource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * This implementaion of guard can restrict access an instance of a interface by controlling if that
 * object's methods can be invoked.
 * 
 * @param <T>
 */
public class InvocationGuard<T> implements Guard<T>, InvocationHandler
{
    private boolean scopeClosed = false;

    /**
     * Proxy which restricts access to the target object.
     */
    private T guardProxy = null;

    /**
     * The object to which access is restricted.
     */
    private Object guardedObject;

    public InvocationGuard(T guarded)
    {
        this.guardedObject = guarded;
    }

    @SuppressWarnings("unchecked")
    public synchronized T open()
    {
        if (this.guardProxy == null)
        {
            ClassLoader classLoader = this.getClass().getClassLoader();
            Class[] interfaces = this.guardedObject.getClass().getInterfaces();
            this.guardProxy = (T) Proxy.newProxyInstance(classLoader, interfaces, this);
        }
        return this.guardProxy;
    }

    public synchronized void close()
    {
        this.scopeClosed = true;
    }

    /**
     * This handler controls invocations of target's methods.
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        if (!InvocationGuard.this.scopeClosed)
        {
            return method.invoke(InvocationGuard.this.guardedObject, args);
        }
        throw new ObjectUnavailableException(InvocationGuard.this.guardedObject);
    }
}
