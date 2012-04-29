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

package com.nestor.interpret.jbasic.libs;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.jbasic.lang.Function;
import com.nestor.interpret.jbasic.lang.RuntimeContext;
import com.nestor.interpret.jbasic.statement.ReturnCode;
import com.nestor.interpret.jbasic.statement.Statement;

public abstract class AbstractFunction extends Statement
{
    private static final long serialVersionUID = 1L;
    private final String name;

    public AbstractFunction(Function fn)
    {
        this.name = fn.getName();
    }

    public String describe()
    {
        return this.name;
    }

    @Override
    protected abstract ReturnCode executeImpl(RuntimeContext context) throws EvaluationException;
}
