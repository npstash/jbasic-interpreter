/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-04-20
 ****************************************************************************/

package com.nestor.interpret.jbasic.statement;

import com.nestor.interpret.jbasic.parse.StatementTokens;

public final class IfEndStatement extends NOP
{
    private static final long serialVersionUID = 4691516409773701025L;

    public IfEndStatement()
    {
        super(StatementTokens.ELSEIF.describe());
    }
}
