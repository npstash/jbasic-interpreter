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

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.expression.RuntimeEnv;
import com.nestor.interpret.expression.Value;
import com.nestor.interpret.type.BOOLEAN;
import com.nestor.interpret.type.TYPE;
import com.nestor.interpret.type.TYPES;

public final class NegationExpression implements Value<BOOLEAN>
{
    private static final long serialVersionUID = 6577486254984448508L;

    private final Value<BOOLEAN> negated;

    public NegationExpression(Value<BOOLEAN> negated)
    {
        this.negated = negated;
    }

    public String describe()
    {
        return BooleanTokens.NOT.describe() + this.negated.describe();
    }

    public BOOLEAN evaluate(RuntimeEnv arg) throws EvaluationException
    {
        return new BOOLEAN(!this.negated.evaluate(arg).booleanValue());
    }

    public TYPE<? extends BOOLEAN> getValueType()
    {
        return TYPES.BOOLEAN_TYPE;
    }
}
