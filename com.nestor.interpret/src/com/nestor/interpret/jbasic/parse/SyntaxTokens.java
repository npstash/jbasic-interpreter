/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-25
 ****************************************************************************/

package com.nestor.interpret.jbasic.parse;

import com.nestor.interpret.token.RegexToken;
import com.nestor.interpret.token.Token;

public final class SyntaxTokens
{
    private SyntaxTokens()
    {
        /* Empty */
    }

    public static final Token IDENTIFIER = new RegexToken("identifier", "[A-Z][A-Z0-9]*[%$]?",
            false);
}
