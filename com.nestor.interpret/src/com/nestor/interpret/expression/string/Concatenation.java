/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-02-03
 ****************************************************************************/

package com.nestor.interpret.expression.string;

import java.util.Collection;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.expression.RuntimeEnv;
import com.nestor.interpret.expression.Value;
import com.nestor.interpret.type.STRING;
import com.nestor.interpret.type.TYPE;
import com.nestor.interpret.type.TYPES;

public final class Concatenation implements Value<STRING>
{
    private static final long serialVersionUID = 5523999176716513370L;

    private final Collection<Value<STRING>> strings;

    public Concatenation(Collection<Value<STRING>> strings)
    {
        this.strings = strings;
    }

    public String describe()
    {
        String res = "";
        for (Value<STRING> v : this.strings)
            res += v.describe();
        return res;
    }

    public STRING evaluate(RuntimeEnv env) throws EvaluationException
    {
        String res = "";
        for (Value<STRING> v : this.strings)
            res += v.evaluate(env).describe();
        return new STRING(res);
    }

    public TYPE<? extends STRING> getValueType()
    {
        return TYPES.STRING_TYPE;
    }
}
