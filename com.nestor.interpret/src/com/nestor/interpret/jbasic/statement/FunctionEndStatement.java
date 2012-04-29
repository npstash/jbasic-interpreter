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

import com.nestor.interpret.jbasic.parse.StatementTokens;

public final class FunctionEndStatement extends NOP
{
    private static final long serialVersionUID = -8459973016591453103L;

    public FunctionEndStatement()
    {
        super(StatementTokens.END.describe() + " " + StatementTokens.FUNCTION.describe());
    }
}
