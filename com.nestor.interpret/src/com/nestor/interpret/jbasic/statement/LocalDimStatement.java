/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-14
 ****************************************************************************/

package com.nestor.interpret.jbasic.statement;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.jbasic.lang.RuntimeContext;
import com.nestor.interpret.jbasic.lang.Variable;
import com.nestor.interpret.type.DATA;
import com.nestor.interpret.type.TYPE;

public final class LocalDimStatement extends Statement
{
    private static final long serialVersionUID = 6493951489305886203L;

    private final String name;

    private final TYPE<? extends DATA> variableType;

    public LocalDimStatement(final String name, final TYPE<? extends DATA> variableType)
    {
        this.name = name;
        this.variableType = variableType;
    }

    public String describe()
    {
        return "DIM " + this.name + " AS " + this.variableType.describe();
    }

    @Override
    protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
    {
        context.stack().addVariable(new Variable(this.name, this.variableType));
        return ReturnCode.SUCCESS;
    }
}
