/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-29
 ****************************************************************************/

package com.nestor.interpret.jbasic.profile.qbasic.statement;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.expression.Value;
import com.nestor.interpret.jbasic.lang.RuntimeContext;
import com.nestor.interpret.jbasic.profile.qbasic.QBasicTokens;
import com.nestor.interpret.jbasic.profile.qbasic.parse.TextServices;
import com.nestor.interpret.jbasic.statement.ReturnCode;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.type.STRING;

public final class PrintStatement extends Statement
{
    private static final long serialVersionUID = -995242332854060638L;

    private final Value<STRING> stringValue;

    public PrintStatement(Value<STRING> stringValue)
    {
        this.stringValue = stringValue;
    }

    public String describe()
    {
        return QBasicTokens.PRINT.describe() + this.stringValue.describe();
    }

    @Override
    protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
    {
        context.getRuntimeService(TextServices.class).print(
                this.stringValue.evaluate(context).describe());
        return ReturnCode.SUCCESS;
    }
}
