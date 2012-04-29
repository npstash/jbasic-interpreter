/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-28
 ****************************************************************************/

package com.nestor.interpret.jbasic.parse;

import com.nestor.interpret.token.RegexToken;

public final class StatementToken extends RegexToken
{
    public enum Isolation
    {
        TRAILING, MIDDLE, NONE, LEADING;

        public static final String STARTER = "[ \\t]";

        public static final String TRAILER = "([ \\t]|$)";
    }

    private static String getStatementToken(String keyword, Isolation isolation)
    {
        String tokenString;
        switch (isolation) {
        case TRAILING:
            tokenString = keyword + Isolation.TRAILER;
            break;
        case LEADING:
            tokenString = Isolation.STARTER + keyword;
            break;
        case MIDDLE:
            tokenString = Isolation.STARTER + keyword + Isolation.TRAILER;
            break;
        case NONE:
        default:
            tokenString = keyword;
            break;
        }
        return tokenString;
    }

    private final String description;

    public StatementToken(String keyword, Isolation isolation)
    {
        super(getStatementToken(keyword, isolation), false);
        this.description = keyword;
    }

    StatementToken(String description, String keyword, Isolation isolation)
    {
        super(getStatementToken(keyword, isolation), false);
        this.description = description;
    }

    @Override
    public String describe()
    {
        return this.description;
    }
}