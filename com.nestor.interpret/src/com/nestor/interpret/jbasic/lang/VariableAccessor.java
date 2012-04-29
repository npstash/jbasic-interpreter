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

package com.nestor.interpret.jbasic.lang;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.expression.RuntimeEnv;
import com.nestor.interpret.expression.Value;
import com.nestor.interpret.type.DATA;
import com.nestor.interpret.type.Qualifier;
import com.nestor.interpret.type.TYPE;

public final class VariableAccessor implements Value<DATA>
{
    private static final long serialVersionUID = -3561318882961409977L;

    private final VariableLookup lookup;

    private final Qualifier qualifier;

    public VariableAccessor(VariableLookup lookup)
    {
        this.lookup = lookup;
        this.qualifier = null;
    }

    public VariableAccessor(VariableLookup lookup, Qualifier qualifier)
    {
        this.lookup = lookup;
        this.qualifier = qualifier;
    }

    public String describe()
    {
        return this.lookup.describe();
    }

    public DATA evaluate(RuntimeEnv env) throws EvaluationException
    {
        RuntimeContext context = (RuntimeContext) env;
        DATA data = this.lookup.lookup(context).evaluate(env);
        if (this.qualifier != null)
            return this.qualifier.get(env, data).evaluate(env);
        return data;
    }

    public VariableLookup getLookup()
    {
        return this.lookup;
    }

    public String getName()
    {
        return this.lookup.getName();
    }

    public TYPE<? extends DATA> getValueType()
    {
        if (this.qualifier != null)
            return this.qualifier.getQualifiedType();
        return this.lookup.getValueType();
    }

    public void setVariableValue(RuntimeContext context, DATA value) throws EvaluationException
    {
        Variable variable = this.lookup.lookup(context);
        if (this.qualifier != null)
            this.qualifier.set(context, variable.evaluate(context), value);
        else
            variable.setValue(value);
    }

    @Override
    public String toString()
    {
        return describe();
    }
}