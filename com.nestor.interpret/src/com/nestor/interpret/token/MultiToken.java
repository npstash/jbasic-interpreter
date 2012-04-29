/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-09
 ****************************************************************************/

package com.nestor.interpret.token;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Scanner;
import com.nestor.shared.Description;

public final class MultiToken implements Serializable, Description
{
    private static final long serialVersionUID = -2638205124930227220L;

    private final Collection<Token> tokens;

    public MultiToken(Collection<Token> options)
    {
        this.tokens = options;
    }

    public MultiToken(Token... options)
    {
        this.tokens = Arrays.asList(options);
    }

    public String describe()
    {
        String res = "{ ";
        boolean first = true;
        for (Token token : this.tokens)
            if (first)
            {
                res += token.describe();
                first = false;
            } else
                res += " | " + token.describe();
        return res + " }";
    }

    public Collection<Token> getTokens()
    {
        return this.tokens;
    }

    public Token match(Scanner scanner) throws ParseException
    {
        for (Token token : this.tokens)
            if (scanner.match(token) != null)
                return token;
        return null;
    }

    public Token matchAdvance(Scanner scanner) throws ParseException
    {
        for (Token token : this.tokens)
            if (scanner.matchAdvance(token) != null)
                return token;
        return null;
    }

    public Token required(Scanner scanner) throws ParseException
    {
        ParseException exception = scanner.parseFailure(this.describe());
        for (Token token : this.tokens)
            if (scanner.matchAdvance(token) != null)
                return token;
        throw exception;
    }
}
