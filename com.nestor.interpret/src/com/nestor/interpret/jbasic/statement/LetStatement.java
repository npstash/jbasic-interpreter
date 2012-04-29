/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-28
 ****************************************************************************/

package com.nestor.interpret.jbasic.statement;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.expression.Value;
import com.nestor.interpret.jbasic.lang.RuntimeContext;
import com.nestor.interpret.jbasic.lang.VariableAccessor;
import com.nestor.interpret.type.DATA;

public final class LetStatement extends Statement
{
    private static final long serialVersionUID = 8704597994266814260L;

    private final VariableAccessor accessor;

    private final Value<? extends DATA> value;

    public LetStatement(VariableAccessor accessor, Value<? extends DATA> value)
    {
        this.accessor = accessor;
        this.value = value;
    }

    public String describe()
    {
        return "LET " + this.accessor.describe() + " = " + this.value.describe();
    }

    @Override
    protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
    {
        this.accessor.setVariableValue(context, this.value.evaluate(context));
        return ReturnCode.SUCCESS;
    }
}
