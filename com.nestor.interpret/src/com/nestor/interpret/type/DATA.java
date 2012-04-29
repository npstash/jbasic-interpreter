/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-27
 ****************************************************************************/

package com.nestor.interpret.type;

import java.io.Serializable;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.expression.RuntimeEnv;
import com.nestor.interpret.expression.Value;
import com.nestor.shared.Description;

public abstract class DATA<T extends DATA> implements Description, Serializable, Value<T>
{
    private static final long serialVersionUID = 363539982078765158L;

    @SuppressWarnings("unchecked")
    @Override
    public final boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (this.getClass().isAssignableFrom(obj.getClass()))
            return equalsImpl((T) obj);
        return false;
    }

    @SuppressWarnings("unchecked")
    public final T evaluate(RuntimeEnv env) throws EvaluationException
    {
        return (T) this;
    }

    public abstract boolean equalsImpl(T t);

    @Override
    public String toString()
    {
        return describe();
    }
}
