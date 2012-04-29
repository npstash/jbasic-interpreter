/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-06
 ****************************************************************************/

package com.nestor.interpret.expression;

import java.util.ArrayList;
import java.util.Collection;

import com.nestor.interpret.type.DATA;
import com.nestor.interpret.type.TYPE;

public final class Expression<VAL extends DATA> implements Value<VAL>
{
    private static final long serialVersionUID = -4949326201753148632L;

    private final Operator<VAL, VAL> operation;

    private TYPE<? extends VAL> resultType = null;

    private final Collection<? extends Value<? extends VAL>> values;

    public Expression(Operator<VAL, VAL> operation,
            Collection<? extends Value<? extends VAL>> values)
    {
        this.operation = operation;
        this.values = values;
    }

    public String describe()
    {
        String res = null;
        for (Value value : this.values)
            if (res == null)
                res = value.describe();
            else
                res += this.operation.describe() + value.describe();
        return res;
    }

    public VAL evaluate(RuntimeEnv env) throws EvaluationException
    {
        VAL result = null;
        boolean first = true;
        for (Value<? extends VAL> value : this.values)
            if (first)
            {
                result = value.evaluate(env);
                first = false;
            } else
                result = this.operation.operation(result, value.evaluate(env));
        return result;
    }

    public TYPE<? extends VAL> getValueType()
    {
        if (this.resultType != null)
            return this.resultType;
        ArrayList<TYPE<? extends VAL>> types = new ArrayList<TYPE<? extends VAL>>(
                this.values.size());
        for (Value<? extends VAL> v : this.values)
            types.add(v.getValueType());
        return this.resultType = this.operation.resultType(types);
    }
}
