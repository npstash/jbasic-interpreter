/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-12
 ****************************************************************************/

package com.nestor.interpret.token;

public final class StandardTokens
{
    private StandardTokens()
    {
        /* Empty */
    }

    public static final Token COMMA = new SimpleToken(",");

    public static final Token DOT = new SimpleToken(".");

    public static final Token COLON = new SimpleToken(":");

    public static final Token SEMICOLON = new SimpleToken(";");

    public static final Token LP = new SimpleToken("(");

    public static final Token RP = new SimpleToken(")");
}
