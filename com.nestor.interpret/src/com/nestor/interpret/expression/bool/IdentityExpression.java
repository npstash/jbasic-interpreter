/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-13
 ****************************************************************************/

package com.nestor.interpret.expression.bool;

import java.util.Collection;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.expression.RuntimeEnv;
import com.nestor.interpret.expression.Value;
import com.nestor.interpret.type.BOOLEAN;
import com.nestor.interpret.type.TYPE;
import com.nestor.interpret.type.TYPES;

public final class IdentityExpression implements Value<BOOLEAN>
{
    private static final long serialVersionUID = 1009848142869669440L;

    private final Collection<Value<BOOLEAN>> values;

    public IdentityExpression(Collection<Value<BOOLEAN>> values)
    {
        this.values = values;
    }

    public String describe()
    {
        String res = null;
        for (Value<BOOLEAN> value : this.values)
            if (res == null)
                res = value.describe();
            else
                res = BooleanTokens.IDENTITY.describe() + value.describe();
        return res;
    }

    public BOOLEAN evaluate(RuntimeEnv arg) throws EvaluationException
    {
        BOOLEAN previous = null;
        for (Value<BOOLEAN> value : this.values)
            if (previous == null)
                previous = value.evaluate(arg);
            else if (value.evaluate(arg).booleanValue() != previous.booleanValue())
                return BOOLEAN.FALSE;
        return BOOLEAN.TRUE;
    }

    public TYPE<? extends BOOLEAN> getValueType()
    {
        return TYPES.BOOLEAN_TYPE;
    }
}
