/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-08
 ****************************************************************************/

package com.nestor.interpret.parse;

import com.nestor.interpret.token.Token;
import com.nestor.interpret.token.TokenContainer;
import com.nestor.shared.Description;

public abstract class Scanner
{
    protected static final class Lexeme
    {
        private final String lex;

        private final Token token;

        public Lexeme(Token token, String lex)
        {
            super();
            this.token = token;
            this.lex = lex;
        }

        final String getLex()
        {
            return this.lex;
        }

        final Token getToken()
        {
            return this.token;
        }

        @Override
        public String toString()
        {
            return this.lex + " [" + this.token.describe() + "]";
        }
    }

    protected static final Lexeme BEGIN_LEXEME = new Lexeme(null, "");

    private Lexeme lastLexeme = BEGIN_LEXEME;

    private final TokenContainer tokens;

    private final ParseEnv env;

    public Scanner(ParseEnv env, TokenContainer tokens)
    {
        this.tokens = tokens;
        this.env = env;
    }

    public abstract Token advance() throws ParseException;

    private void checkToken(Token token)
    {
        if (!this.tokens.contains(token))
        {
            String string = "unknown token: " + token + "(" + token.describe() + ")";
            throw new IllegalArgumentException(string);
        }
    }

    public final ParseException expected(Description expectation)
    {
        return new ParseException(expectation, getCurrentPositionDescription());
    }

    public final ParseException expected(String expectation)
    {
        return new ParseException("expected " + expectation, getCurrentPositionDescription());
    }

    protected final Token findTokenAtCurrentPosition()
    {
        for (Token token : this.tokens)
        {
            String lexeme = matchToken(token);
            if (lexeme != null)
                return token;
        }
        return null;
    }

    protected abstract Description getCurrentPositionDescription();

    protected final Lexeme getLastLexeme()
    {
        return this.lastLexeme;
    }

    @Deprecated
    public abstract String getLastSkips();

    public boolean end()
    {
        return this.lastLexeme == null;
    }

    public final String match(Token token) throws ParseException
    {
        checkToken(token);
        if (this.lastLexeme == BEGIN_LEXEME)
            this.advance();
        if (this.lastLexeme == null)
            return null;
        if (token.isSame(this.lastLexeme.getToken()))
            return this.lastLexeme.getLex();
        return null;
    }

    public final String matchAdvance(Token token) throws ParseException
    {
        String lexeme;
        if ((lexeme = match(token)) != null)
        {
            advance();
            return lexeme;
        }
        return null;
    }

    /**
     * @return <code>true</code> if any of the tokens in this scanner matched and
     *         {@link #getLastLexeme()} will return value other than <code>null</code>.
     */
    protected final boolean matchNextToken()
    {
        String bestLexeme = null;
        Token bestToken = null;
        for (Token token : this.tokens)
        {
            String lexeme = matchToken(token);
            if (lexeme != null)
            {
                if (bestLexeme == null)
                {
                    bestLexeme = lexeme;
                    bestToken = token;
                } else if (bestLexeme.length() < lexeme.length())
                {
                    this.env.log("Overriding " + bestLexeme + " with " + lexeme);
                    bestLexeme = lexeme;
                    bestToken = token;
                }
                continue;
            }
        }
        if (bestLexeme != null)
        {
            this.lastLexeme = new Lexeme(bestToken, bestLexeme);
            return true;
        }
        return false;
    }

    /**
     * Called by subclasses to indicate end of parsing
     */
    protected void terminate()
    {
        this.lastLexeme = null;
    }

    protected abstract String matchToken(Token token);

    public final ParseException parseFailure(String explanation)
    {
        return new ParseException(explanation, getCurrentPositionDescription());
    }

    public final ParseException parseFailure(Exception reason)
    {
        return new ParseException(reason, getCurrentPositionDescription());
    }

    private String requiredImpl(Token token, String fullExplanation) throws ParseException
    {
        String lex;
        ParseException exception = expected(fullExplanation);
        if ((lex = matchAdvance(token)) == null)
        {
            exception.fillInStackTrace();
            throw exception;
        }
        return lex;
    }

    public final String required(Token token, String explanation) throws ParseException
    {
        return requiredImpl(token, explanation + " (" + token + ")");
    }

    public final String required(Token token) throws ParseException
    {
        return requiredImpl(token, token.describe());
    }

    /**
     * Skips all tokens until the token given as argument is found. Does not advance after matching
     * the token, so the lexeme still can be received via match functions.
     * 
     * @param token
     *            token to find
     * @return <code>true</code> if the element to skip until was found, false otherwise (which
     *         indicates end of data).
     * @throws ParseException
     *             forwarded from {@link #advance()}
     */
    @Deprecated
    public boolean skipUntil(Token token) throws ParseException
    {
        while (this.lastLexeme != null && !this.lastLexeme.getToken().isSame(token))
            advance();
        if (this.lastLexeme != null && this.lastLexeme.getToken().isSame(token))
            return true;
        return false;
    }
}
