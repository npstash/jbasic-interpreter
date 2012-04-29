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

package com.nestor.interpret.expression.arithmetic;

import com.nestor.interpret.token.RegexToken;
import com.nestor.interpret.token.SimpleToken;
import com.nestor.interpret.token.Token;

public final class ArithmeticTokens
{
    private ArithmeticTokens()
    {
        /* Empty */
    }

    public static final Token MODULO = new RegexToken("\\sMOD\\s", false);

    public static final Token DIV = new SimpleToken("/");

    public static final Token DIV_INT = new SimpleToken("\\");

    public static final Token MINUS = new SimpleToken("-");

    public static final Token PLUS = new SimpleToken("+");

    public static final Token POWER = new SimpleToken("^");

    public static final Token TIMES = new SimpleToken("*");
}
