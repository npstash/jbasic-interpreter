/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 5 Dec 2007
 ****************************************************************************/

package com.nestor.interpret.jbasic.statement;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.jbasic.lang.RuntimeContext;
import com.nestor.interpret.jbasic.parse.StatementTokens;

public class DoWhileLoopStatement extends Statement
{
    private static final long serialVersionUID = -705149381851618939L;

    public DoWhileLoopStatement()
    {
        /* Empty */
    }

    @Override
    protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
    {
        return ReturnCode.LOOP_DOWHILE;
    }

    public String describe()
    {
        return StatementTokens.LOOP.toString();
    }
}
