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
import com.nestor.interpret.expression.Value;
import com.nestor.interpret.jbasic.lang.RuntimeContext;
import com.nestor.interpret.jbasic.lang.Stack;
import com.nestor.interpret.jbasic.parse.StatementTokens;
import com.nestor.interpret.type.BOOLEAN;

public class DoWhileStatement extends Statement
{
    private static final long serialVersionUID = -9093497898091311255L;

    private final Statement loop;

    private final Value<BOOLEAN> condition;

    public DoWhileStatement(Statement loop, Value<BOOLEAN> condition)
    {
        this.loop = loop;
        this.condition = condition;
    }

    public String describe()
    {
        if (this.condition == null)
            return StatementTokens.DO.toString();
        return StatementTokens.DO + " " + StatementTokens.WHILE + " " + this.condition.describe();
    }

    @Override
    protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
    {
        // do loop
        ReturnCode rc = ReturnCode.SUCCESS;
        // comparing var <= to
        while (this.condition == null || this.condition.evaluate(context).booleanValue())
        {
            context.stack().new Block(Stack.BlockKind.CODE);
            rc = this.loop.execute(context);
            context.stack().blockEnd();
            if (rc == ReturnCode.LOOP_DOWHILE)
            {
                // next iteration
            } else if (rc == ReturnCode.EXIT_DO)
                break;
            else if (rc != ReturnCode.SUCCESS)
                return rc;
        }
        return ReturnCode.SUCCESS;
    }

}
