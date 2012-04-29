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

package com.nestor.interpret.expression.relational;

import com.nestor.interpret.token.MultiToken;
import com.nestor.interpret.token.RegexToken;
import com.nestor.interpret.token.SimpleToken;
import com.nestor.interpret.token.Token;

public final class RelationalTokens
{
    private RelationalTokens()
    {
        /* Empty */
    }

    public static final Token NOT_EQUAL = new SimpleToken("<>");

    public static final Token LESS_OR_EQUAL = new SimpleToken("<=");

    public static final Token LESS = new RegexToken("<");

    public static final Token GREATER_OR_EQUAL = new SimpleToken(">=");

    public static final Token GREATER = new SimpleToken(">");

    public static final Token EQUAL = new SimpleToken("=");

    public static final MultiToken ALL = new MultiToken(NOT_EQUAL, LESS_OR_EQUAL, LESS,
            GREATER_OR_EQUAL, GREATER, EQUAL);
}
