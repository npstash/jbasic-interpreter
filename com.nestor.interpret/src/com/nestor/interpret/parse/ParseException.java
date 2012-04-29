/*****************************************************************************
 * Copyright (C) 2006 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2006-12-31
 ****************************************************************************/

package com.nestor.interpret.parse;

import com.nestor.shared.Description;

public class ParseException extends Exception
{
    private static final long serialVersionUID = 16822529045355243L;

    public ParseException(Exception e, Description position)
    {
        super(position.describe(), e);
    }

    public ParseException(String explanation, Description position)
    {
        super(position.describe() + ": " + explanation);
    }

    public ParseException(Description expectation, Description position)
    {
        super(position.describe() + ": expected " + expectation.describe());
    }
}
