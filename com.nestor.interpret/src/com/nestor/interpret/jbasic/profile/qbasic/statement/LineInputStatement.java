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

package com.nestor.interpret.jbasic.profile.qbasic.statement;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.jbasic.lang.RuntimeContext;
import com.nestor.interpret.jbasic.lang.VariableAccessor;
import com.nestor.interpret.jbasic.profile.qbasic.QBasicTokens;
import com.nestor.interpret.jbasic.profile.qbasic.parse.TextServices;
import com.nestor.interpret.jbasic.statement.ReturnCode;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.type.STRING;

public class LineInputStatement extends Statement
{
    private static final long serialVersionUID = -7294524854120077122L;

    private final VariableAccessor variable;

    public LineInputStatement(VariableAccessor variable)
    {
        this.variable = variable;
    }

    public String describe()
    {
        return QBasicTokens.LINE_INPUT.describe() + this.variable.describe();
    }

    @Override
    protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
    {
        String line = context.getRuntimeService(TextServices.class).lineInput();
        this.variable.setVariableValue(context, new STRING(line));
        return ReturnCode.SUCCESS;
    }
}
