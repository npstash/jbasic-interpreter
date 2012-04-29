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
import com.nestor.interpret.expression.relational.RelationalTokens;
import com.nestor.interpret.type.BOOLEAN;
import com.nestor.interpret.type.STRING;
import com.nestor.interpret.type.TYPE;
import com.nestor.interpret.type.TYPES;

public final class Comparison implements Value<BOOLEAN>
{
    private static final long serialVersionUID = 7421577304265554668L;

    private final Value<STRING> left;

    private final Value<STRING> right;

    public Comparison(Value<STRING> left, Value<STRING> right)
    {
        this.left = left;
        this.right = right;
    }

    public String describe()
    {
        return this.left.describe() + RelationalTokens.EQUAL.describe() + this.right.describe();
    }

    public BOOLEAN evaluate(RuntimeEnv env) throws EvaluationException
    {
        return new BOOLEAN(this.left.evaluate(env).equals(this.right.evaluate(env)));
    }

    public TYPE<? extends BOOLEAN> getValueType()
    {
        return TYPES.BOOLEAN_TYPE;
    }
}
