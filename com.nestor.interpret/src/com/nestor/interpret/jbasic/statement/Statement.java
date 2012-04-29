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
import com.nestor.interpret.parse.Match;

public abstract class Statement implements Match
{
    private static final long serialVersionUID = 2060685847048886238L;

    public final ReturnCode execute(RuntimeContext context) throws ExecutionException
    {
        // TODO here is place for some debug support
        try
        {
            return executeImpl(context);
        } catch (ExecutionException e)
        {
            throw e;
        } catch (EvaluationException e)
        {
            throw new ExecutionException(this, e);
        }
    }

    protected abstract ReturnCode executeImpl(RuntimeContext context) throws EvaluationException;

    @Override
    public String toString()
    {
        return describe();
    }
}
