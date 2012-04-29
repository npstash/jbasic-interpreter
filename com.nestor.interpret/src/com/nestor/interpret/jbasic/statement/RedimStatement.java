/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-07-17
 ****************************************************************************/

package com.nestor.interpret.jbasic.statement;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.jbasic.lang.RuntimeContext;
import com.nestor.interpret.jbasic.lang.Variable;
import com.nestor.interpret.jbasic.parse.StatementTokens;
import com.nestor.interpret.type.ARRAY_TYPE;
import com.nestor.interpret.type.DATA;

public class RedimStatement extends Statement
{
    private static final long serialVersionUID = 5123221656866838110L;

    private final String name;

    private final ARRAY_TYPE<? extends DATA> redimType;

    public RedimStatement(String name, ARRAY_TYPE<? extends DATA> variableType)
    {
        this.name = name;
        this.redimType = variableType;
    }

    public String describe()
    {
        return StatementTokens.REDIM + " " + this.name + " AS " + this.redimType.describe();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
    {
        Variable var = null;
        if (context.stack().containsVariable(this.name))
            var = context.stack().getVariable(this.name);
        else
            var = context.module().getVariable(this.name);
        ARRAY_TYPE<? extends DATA> varType = (ARRAY_TYPE<? extends DATA>) var.getValueType();
        varType.redim(this.redimType.getDimensions());
        // keep data in sync with type - create a new, empty array
        var.setValue(this.redimType.newInstance());
        return ReturnCode.SUCCESS;
    }
}
