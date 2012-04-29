/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-05-29
 ****************************************************************************/

package com.nestor.interpret.expression;

public class EvaluationException extends Exception
{
    private static final long serialVersionUID = -163822401936915810L;

    public EvaluationException(String message)
    {
        super(message);
    }

    public EvaluationException(Throwable cause)
    {
        super(cause);
    }

    public EvaluationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
