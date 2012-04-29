/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-28
 ****************************************************************************/

package com.nestor.interpret.jbasic.lang;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.expression.RuntimeEnv;
import com.nestor.interpret.expression.Value;
import com.nestor.interpret.type.DATA;
import com.nestor.interpret.type.INTEGER;
import com.nestor.interpret.type.TYPE;
import com.nestor.interpret.type.TYPES;

public class Variable implements Value<DATA>
{
    /**
     * Tinker here to support identifier name-based types.
     */
    public static final TYPE<INTEGER> DEFAULT_TYPE = TYPES.INTEGER_TYPE;

    private static final long serialVersionUID = -4889453452393374168L;

    private final String name;

    private DATA value;

    private final TYPE<? extends DATA> variableType;

    public Variable(String name, TYPE<? extends DATA> variableClass)
    {
        this.name = name;
        this.variableType = variableClass;
        this.value = variableClass.newInstance();
    }

    public String describe()
    {
        return this.name + "[" + this.variableType.describe() + "]";
    }

    public DATA evaluate(RuntimeEnv env) throws EvaluationException
    {
        return this.value;
    }

    public String getName()
    {
        return this.name;
    }

    public TYPE<? extends DATA> getValueType()
    {
        return this.variableType;
    }

    public void setValue(DATA newValue)
    {
        if (newValue != null && !this.variableType.isInstance(newValue))
            throw new IllegalArgumentException("Incompatible value type: got "
                    + newValue.getValueType().describe() + " expected "
                    + this.variableType.describe());
        this.value = newValue;
    }

    @Override
    public String toString()
    {
        return describe();
    }
}
