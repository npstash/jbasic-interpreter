/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-12
 ****************************************************************************/

package com.nestor.interpret.expression.relational;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.expression.RuntimeEnv;
import com.nestor.interpret.expression.Value;
import com.nestor.interpret.type.BOOLEAN;
import com.nestor.interpret.type.NUMBER;
import com.nestor.interpret.type.TYPE;
import com.nestor.interpret.type.TYPES;

public final class RelationalExpression implements Value<BOOLEAN>
{
    private static final long serialVersionUID = -710002202223143119L;

    private final RelationalExpression leftEquation;

    private final Value<? extends NUMBER> leftNumber;

    private final Value<? extends NUMBER> next;

    private final RelationalOperator operator;

    public RelationalExpression(RelationalOperator operator, RelationalExpression equation,
            Value<? extends NUMBER> next)
    {
        this.operator = operator;
        this.leftEquation = equation;
        this.leftNumber = null;
        this.next = next;
    }

    public RelationalExpression(RelationalOperator operator, Value<? extends NUMBER> first,
            Value<? extends NUMBER> next)
    {
        this.operator = operator;
        this.leftNumber = first;
        this.leftEquation = null;
        this.next = next;
    }

    public String describe()
    {
        if (this.leftEquation != null)
            return this.leftEquation.describe() + this.operator.describe() + this.next.describe();
        return this.leftNumber.describe() + this.operator.describe() + this.next.describe();
    }

    public BOOLEAN evaluate(RuntimeEnv env) throws EvaluationException
    {
        if (this.leftNumber != null)
            return this.operator.operation(this.leftNumber.evaluate(env), this.next.evaluate(env));
        if (this.leftEquation.evaluate(env).booleanValue())
            return this.operator.operation(this.leftEquation.getRightMostValue().evaluate(env),
                    this.next.evaluate(env));
        return new BOOLEAN(false);
    }

    private Value<? extends NUMBER> getRightMostValue()
    {
        return this.next;
    }

    public TYPE<? extends BOOLEAN> getValueType()
    {
        return TYPES.BOOLEAN_TYPE;
    }
}
