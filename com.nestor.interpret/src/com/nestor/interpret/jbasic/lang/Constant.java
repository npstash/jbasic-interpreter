/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-07-15
 ****************************************************************************/

package com.nestor.interpret.jbasic.lang;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.expression.RuntimeEnv;
import com.nestor.interpret.expression.Value;
import com.nestor.interpret.type.DATA;
import com.nestor.interpret.type.TYPE;

public class Constant implements Value<DATA>
{
    private static final long serialVersionUID = -4889453452393374168L;

    private final String name;

    private DATA value;

    public Constant(String name, DATA value)
    {
        if (value == null)
            throw new NullPointerException("value");
        this.name = name;
        this.value = value;
    }

    public String describe()
    {
        return this.name + "[" + this.value.getValueType().describe() + "]";
    }

    public DATA evaluate(RuntimeEnv env) throws EvaluationException
    {
        return this.value;
    }

    public String getName()
    {
        return this.name;
    }

    @SuppressWarnings("unchecked")
    public TYPE<? extends DATA> getValueType()
    {
        return this.value.getValueType();
    }

    @Override
    public String toString()
    {
        return describe();
    }
}
