/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-04-18
 ****************************************************************************/

package com.nestor.interpret.jbasic.statement;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.jbasic.lang.RuntimeContext;
import com.nestor.interpret.jbasic.parse.StatementTokens;

public final class ForNextStatement extends Statement
{
    private static final long serialVersionUID = 1123475531259339452L;

    private final String counterName;

    public ForNextStatement()
    {
        this.counterName = "";
    }

    public ForNextStatement(String counterName)
    {
        this.counterName = counterName;
    }

    @Override
    protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
    {
        return ReturnCode.NEXT_FOR;
    }

    public String describe()
    {
        return StatementTokens.NEXT + " " + this.counterName;
    }
}
