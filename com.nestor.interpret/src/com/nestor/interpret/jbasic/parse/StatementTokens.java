/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-14
 ****************************************************************************/

package com.nestor.interpret.jbasic.parse;

import com.nestor.interpret.jbasic.parse.StatementToken.Isolation;
import com.nestor.interpret.token.RegexToken;
import com.nestor.interpret.token.Token;

public final class StatementTokens
{
    public static final Token AS = new StatementToken("AS", Isolation.MIDDLE);

    public static final Token COMMENT = new RegexToken("'[^\\n]*$", true);

    public static final Token CONST = new StatementToken("CONST", Isolation.TRAILING);

    public static final Token DECLARE = new StatementToken("DECLARE", Isolation.TRAILING);

    public static final Token DIM = new StatementToken("DIM", Isolation.TRAILING);

    public static final Token DO = new StatementToken("DO", Isolation.TRAILING);

    public static final Token ELSE = new StatementToken("ELSE", Isolation.TRAILING);

    public static final Token ELSEIF = new StatementToken("ELSEIF", Isolation.TRAILING);

    public static final Token END = new StatementToken("END", Isolation.TRAILING);

    public static final Token EXIT = new StatementToken("EXIT", Isolation.TRAILING);

    public static final Token FOR = new StatementToken("FOR", Isolation.TRAILING);

    public static final Token FUNCTION = new StatementToken("FUNCTION", Isolation.TRAILING);

    public static final Token IF = new StatementToken("IF", Isolation.TRAILING);

    public static final Token LET = new StatementToken("LET", Isolation.TRAILING);

    public static final Token LOOP = new StatementToken("LOOP", Isolation.NONE);

    public static final Token NEXT = new StatementToken("NEXT", Isolation.NONE);

    public static final Token REDIM = new StatementToken("REDIM", Isolation.TRAILING);

    public static final Token REM = new RegexToken("^\\s*REM [^\\n]*$", true);

    public static final Token STEP = new StatementToken("STEP", Isolation.MIDDLE);

    public static final Token SUB = new StatementToken("SUB", Isolation.TRAILING);

    public static final Token THEN = new StatementToken("THEN", Isolation.LEADING);

    public static final Token TO = new StatementToken("TO", Isolation.MIDDLE);

    public static final Token TYPE = new StatementToken("TYPE", Isolation.TRAILING);

    public static final Token WHILE = new StatementToken("WHILE", Isolation.TRAILING);

    private StatementTokens()
    {
        /* Empty */
    }
}
