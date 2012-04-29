/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-06
 ****************************************************************************/

package com.nestor.interpret.expression.bool;

import com.nestor.interpret.token.RegexToken;
import com.nestor.interpret.token.SimpleToken;
import com.nestor.interpret.token.Token;

public final class BooleanTokens
{
    private BooleanTokens()
    {
        /* Empty */
    }

    public static final Token AND = new RegexToken("AND", false);

    public static final Token OR = new RegexToken("OR", false);

    public static final Token XOR = new RegexToken("XOR", false);

    public static final Token NOT = new RegexToken("NOT", false);

    public static final Token IMPLY = new SimpleToken("->");

    public static final Token IDENTITY = new SimpleToken("<=>");
}
