/*****************************************************************************
 * Copyright (C) 2006 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2006-12-29
 ****************************************************************************/

package com.nestor.interpret.token;

public class SimpleToken implements Token
{
    private final String lex;

    private final String description;

    public SimpleToken(String lex)
    {
        super();
        this.description = lex;
        this.lex = lex;
    }

    public SimpleToken(String description, String lex)
    {
        super();
        this.description = description;
        this.lex = lex;
    }

    public String describe()
    {
        return this.description;
    }

    public final boolean isSame(Token obj)
    {
        if (obj instanceof SimpleToken)
            return ((SimpleToken) obj).lex.equals(this.lex);
        return false;
    }

    public final String match(String input, int offset)
    {
        if (input.startsWith(this.lex, offset))
            return this.lex;
        return null;
    }

    public final boolean isShadowing(Token other)
    {
        if (other instanceof SimpleToken)
        {
            SimpleToken st = (SimpleToken) other;
            // shadowing happens if this token is a subset
            // of the other token from index 0
            return st.lex.indexOf(this.lex) == 0;
        }
        if (other instanceof RegexToken)
            // shadowing regex tokens by simple
            // tokens is undefined
            return false;
        return false;
    }

    public final String getLex()
    {
        return this.lex;
    }

    @Override
    public String toString()
    {
        return describe();
    }
}
