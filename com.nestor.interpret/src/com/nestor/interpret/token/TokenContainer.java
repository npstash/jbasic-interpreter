/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-27
 ****************************************************************************/

package com.nestor.interpret.token;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;

public final class TokenContainer implements Iterable<Token>
{
    private static ArrayList<Token> extractTokens(Class fromClass)
    {
        // parse all fields in current class and find tokens, in order of appearance
        // - only public fields
        Field[] fields = fromClass.getFields();
        ArrayList<Token> tokens = new ArrayList<Token>();
        for (Field field : fields)
        {
            int modifiers = field.getModifiers();
            boolean assignableFrom = Token.class.isAssignableFrom(field.getType());
            if (assignableFrom && Modifier.isFinal(modifiers) && Modifier.isStatic(modifiers))
                try
                {
                    tokens.add((Token) field.get(null));
                } catch (IllegalArgumentException e)
                {
                    /* Empty */
                } catch (IllegalAccessException e)
                {
                    /* Empty */
                }
        }
        return tokens;
    }

    private ArrayList<Token> tokens = new ArrayList<Token>();

    public synchronized void addToken(Token token)
    {
        // check if the new token is not shadowing any other present
        // token
        for (Token t : this.tokens)
            if (t.isShadowing(token))
                throw new IllegalArgumentException("Token '" + t.describe()
                        + "' is shadowing the new token '" + token.describe() + "'");
        this.tokens.add(token);
    }

    public synchronized void addTokens(Class fromUtilityClass)
    {
        addTokens(extractTokens(fromUtilityClass));
    }

    public synchronized void addTokens(Iterable<Token> tokens1)
    {
        for (Token token : tokens1)
            addToken(token);
    }

    public synchronized void addTokens(MultiToken tokens1)
    {
        addTokens(tokens1.getTokens());
    }

    public synchronized void addTokens(Token... tokens1)
    {
        for (Token token : tokens1)
            addToken(token);
    }

    public synchronized boolean contains(Token token)
    {
        for (Token t : this.tokens)
            if (t.isSame(token))
                return true;
        return false;
    }

    public Iterator<Token> iterator()
    {
        return this.tokens.iterator();
    }
}
