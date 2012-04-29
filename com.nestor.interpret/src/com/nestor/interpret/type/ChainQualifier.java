/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-05-20
 ****************************************************************************/

package com.nestor.interpret.type;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.expression.RuntimeEnv;

public abstract class ChainQualifier<FROM extends DATA> implements Qualifier
{
    private static final long serialVersionUID = 1316272410123767469L;
    
    private final Qualifier delegate;

    protected ChainQualifier(Qualifier delegate)
    {
        this.delegate = delegate;
    }

    @SuppressWarnings("unchecked")
    public final DATA get(RuntimeEnv env, DATA unqualified) throws EvaluationException
    {
        if (this.delegate != null)
        {
            FROM qv = (FROM) this.delegate.get(env, unqualified);
            return getChained(env, qv);
        }
        // no chaining
        return getChained(env, (FROM) unqualified);
    }

    protected abstract DATA getChained(RuntimeEnv env, FROM unqualified) throws EvaluationException;

    @SuppressWarnings("unchecked")
    public final void set(RuntimeEnv env, DATA unqualified, DATA data) throws EvaluationException
    {
        if (this.delegate != null)
        {
            FROM chained = (FROM) this.delegate.get(env, unqualified);
            setChained(env, chained, data);
        } else
            setChained(env, (FROM) unqualified, data);
    }

    protected abstract void setChained(RuntimeEnv env, FROM unqualified, DATA data)
            throws EvaluationException;

    @Override
    public String toString()
    {
        return describe();
    }
}