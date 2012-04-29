/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-04-22
 ****************************************************************************/

package com.nestor.interpret.jbasic.statement;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.jbasic.lang.RuntimeContext;
import com.nestor.interpret.jbasic.parse.StatementTokens;

public final class ExitStatement extends Statement
{
    public enum ExitType
    {
        FOR, DO, END
    }

    private static final long serialVersionUID = 6108701303459801899L;

    private final ExitType type;

    public ExitStatement(ExitType type)
    {
        this.type = type;
    }

    @Override
    protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
    {
        switch (this.type) {
        case FOR:
            return ReturnCode.EXIT_FOR;
        case DO:
            return ReturnCode.LOOP_DOWHILE;
        case END:
            return ReturnCode.END;
        }
        throw new IllegalArgumentException("unknown exit");
    }

    public String describe()
    {
        if (this.type == ExitType.END)
            return this.type.toString();
        return StatementTokens.EXIT + " " + this.type;
    }
}
