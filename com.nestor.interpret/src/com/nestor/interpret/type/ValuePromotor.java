/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-30
 ****************************************************************************/

package com.nestor.interpret.type;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.expression.RuntimeEnv;
import com.nestor.interpret.expression.Value;

/**
 * Wrapper {@link Value}, converts type of one value to another type at runtime. Promotion ability
 * must be detected at parse time.
 * 
 * @param <D>
 */
public final class ValuePromotor<D extends DATA> implements Value<D>
{
    private static final long serialVersionUID = -4174755339869995805L;

    @SuppressWarnings("unchecked")
    public static <D extends DATA> Value<D> createPromotor(Value value, TYPE<D> promoteTo)
            throws PromotionNotSupportedException
    {
        if (value.getValueType().areEqual(promoteTo))
            return value;
        if (!(value.getValueType().canPromote(promoteTo)))
            throw new PromotionNotSupportedException(value.getValueType(), promoteTo);
        return new ValuePromotor<D>(value, promoteTo);
    }

    private final Value original;

    private final TYPE<? extends D> promoteTo;

    private ValuePromotor(Value value, TYPE<? extends D> promoteTo)
    {
        this.original = value;
        this.promoteTo = promoteTo;
    }

    public String describe()
    {
        return "(" + this.promoteTo.describe() + ")" + this.original.describe();
    }

    @SuppressWarnings("unchecked")
    public D evaluate(RuntimeEnv arg) throws EvaluationException
    {
        DATA orgValue = this.original.evaluate(arg);
        return (D) this.original.getValueType().promote(orgValue, this.promoteTo);
    }

    public TYPE<? extends D> getValueType()
    {
        return this.promoteTo;
    }

    @Override
    public String toString()
    {
        return describe();
    }
}
