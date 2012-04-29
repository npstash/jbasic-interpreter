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

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.expression.RuntimeEnv;
import com.nestor.interpret.expression.Value;
import com.nestor.interpret.type.DATA;
import com.nestor.interpret.type.STRING;
import com.nestor.interpret.type.TYPE;
import com.nestor.interpret.type.TYPES;

public final class StringValueAdapter implements Value<STRING>
{
    private static final long serialVersionUID = -3932412253248850914L;

    private final Value<DATA> delegate;

    public StringValueAdapter(Value<DATA> delegate)
    {
        this.delegate = delegate;
    }

    public String describe()
    {
        return this.delegate.describe();
    }

    public STRING evaluate(RuntimeEnv env) throws EvaluationException
    {
        return new STRING(this.delegate.evaluate(env).describe());
    }

    public TYPE<? extends STRING> getValueType()
    {
        return TYPES.STRING_TYPE;
    }
}
