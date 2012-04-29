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

public class NOP extends Statement
{
    private static final long serialVersionUID = -8041346978885650285L;

    public static final Statement INSTANCE = new NOP("empty statement");

    private final String descr;

    public NOP(String descr)
    {
        this.descr = descr;
    }

    @Override
    protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
    {
        return ReturnCode.SUCCESS;
    }

    public String describe()
    {
        return this.descr;
    }
}
