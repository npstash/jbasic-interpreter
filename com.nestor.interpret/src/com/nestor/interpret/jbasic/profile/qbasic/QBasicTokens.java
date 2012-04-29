/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 6 Dec 2007
 ****************************************************************************/

package com.nestor.interpret.jbasic.profile.qbasic;

import com.nestor.interpret.jbasic.parse.StatementToken;
import com.nestor.interpret.jbasic.parse.StatementToken.Isolation;
import com.nestor.interpret.token.Token;

public final class QBasicTokens
{
    public static final Token LINE_INPUT = new StatementToken("LINE\\s+INPUT", Isolation.TRAILING);

    public static final Token PRINT = new StatementToken("PRINT", Isolation.TRAILING);

    private QBasicTokens()
    {
        /* Empty */
    }
}
