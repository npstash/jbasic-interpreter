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

package com.nestor.interpret.expression.arithmetic;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.expression.RuntimeEnv;
import com.nestor.interpret.expression.Value;
import com.nestor.interpret.type.DOUBLE;
import com.nestor.interpret.type.INTEGER;
import com.nestor.interpret.type.NUMBER;
import com.nestor.interpret.type.TYPE;
import com.nestor.interpret.type.TYPES;

public final class NegativeExpression implements Value<NUMBER>
{
    private static final long serialVersionUID = 4375675944510300103L;

    private final Value<? extends NUMBER> negated;

    public NegativeExpression(Value<? extends NUMBER> negated)
    {
        this.negated = negated;
    }

    public String describe()
    {
        return ArithmeticTokens.MINUS.describe() + this.negated.describe();
    }

    public NUMBER evaluate(RuntimeEnv arg) throws EvaluationException
    {
        if (TYPES.INTEGER_TYPE.isAssignableFrom(this.negated.getValueType()))
            return new INTEGER(-this.negated.evaluate(arg).intValue());
        if (TYPES.DOUBLE_TYPE.isAssignableFrom(this.negated.getValueType()))
            return new DOUBLE(-this.negated.evaluate(arg).intValue());
        throw new IllegalArgumentException("Unknown value type");
    }

    public TYPE<? extends NUMBER> getValueType()
    {
        return this.negated.getValueType();
    }
}
