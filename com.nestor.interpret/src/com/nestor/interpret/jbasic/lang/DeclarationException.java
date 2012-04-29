/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-07-01
 ****************************************************************************/

package com.nestor.interpret.jbasic.lang;

import com.nestor.interpret.jbasic.parse.InterpretException;

public class DeclarationException extends InterpretException
{
    private static final long serialVersionUID = 7472821255827307988L;

    public DeclarationException(String msg)
    {
        super(msg);
    }
}
