/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-04-29
 ****************************************************************************/

package com.nestor.shared.event;

import java.lang.reflect.Method;

import com.nestor.shared.util.PassiveIterator;

class InvocationIterator<T> implements PassiveIterator<T>
{
    private final Object[] args;

    private final Method m;

    public InvocationIterator(Method m, Object[] args)
    {
        super();
        this.m = m;
        this.args = args;
    }

    public void next(T listener)
    {
        try
        {
            this.m.invoke(listener, this.args);
        } catch (Exception e)
        {
            // cannot handle it anyway
            throw new RuntimeException(e);
        }
    }
}