/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-05-28
 ****************************************************************************/

package com.nestor.interpret.jbasic.lang;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.expression.RuntimeEnv;
import com.nestor.interpret.jbasic.profile.RuntimeServiceProvider;
import com.nestor.interpret.jbasic.profile.qbasic.parse.TextServices;

/**
 * 
 */
public final class RuntimeContext extends RuntimeServiceProvider<EvaluationException> implements
        RuntimeEnv
{
    private final Module module;

    private final RuntimeServiceProvider<? extends Throwable> serviceProvider;

    private Map<Class, Object> servicesMap = new HashMap<Class, Object>();

    private final RuntimeStack stack = new RuntimeStack();

    public RuntimeContext(RuntimeServiceProvider<? extends Throwable> rsp, Module module)
    {
        this.serviceProvider = rsp;
        this.module = module;
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized <S> S getRuntimeService(Class<S> serviceClass) throws EvaluationException
    {
        if (this.servicesMap.containsKey(serviceClass))
            return (S) this.servicesMap.get(serviceClass);
        try
        {
            S service = this.serviceProvider.getRuntimeService(serviceClass);
            if (service == null)
                throw new EvaluationException("Runtime service " + serviceClass
                        + " could not be instantiated");
            this.servicesMap.put(serviceClass, service);
            return service;
        } catch (Throwable e)
        {
            throw new EvaluationException("Runtime service " + serviceClass
                    + " could not be instantiated", e);
        }
    }

    /**
     * Convenience method. It also does not throw an exception.
     * 
     * @param message
     */
    public void log(String message)
    {
        try
        {
            // use reflection to prevent linking
            Object logSrv = this.getRuntimeService(TextServices.class.getName());
            Method logMth = logSrv.getClass().getMethod("log", String.class);
            logMth.invoke(logSrv, message);
        } catch (EvaluationException e)
        {
            /* Empty */
        } catch (Exception e)
        {
            /* Empty */
        }
    }

    public Module module()
    {
        return this.module;
    }

    public RuntimeStack stack()
    {
        return this.stack;
    }
}
