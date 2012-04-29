/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-13
 ****************************************************************************/

package com.nestor.interpret.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import com.nestor.interpret.token.Token;
import com.nestor.interpret.token.TokenContainer;
import com.nestor.shared.Description;

public final class LineScanner extends Scanner
{
    private class Cursor implements Description
    {
        String input;

        int line;

        int offset;

        public Cursor()
        {
            /* Empty */
        }

        private Cursor(int offset, int line, String input)
        {
            super();
            this.offset = offset;
            this.line = line;
            this.input = input;
        }

        @Override
        public Description clone()
        {
            return new Cursor(this.offset, this.line, this.input);
        }

        public String describe()
        {
            Token token = findTokenAtCurrentPosition();
            String hint = null;
            String res;
            if (token != null)
                hint = matchToken(token);
            if (hint == null && this.offset < this.input.length())
                hint = String.valueOf(this.input.charAt(this.offset));
            if (hint == null)
                res = "At end of input, column " + this.offset + ", line " + this.line;
            else
                res = "At '" + hint + "', column " + this.offset + ", line " + this.line;
            char[] shift = new char[this.offset];
            for (int i = 0; i < shift.length; i++)
            {
                char c = this.input.charAt(i);
                if (Character.isWhitespace(c))
                    shift[i] = c;
                else
                    shift[i] = ' ';
            }
            // input already contains EOL character
            return "\n" + this.input + new String(shift) + "^\n" + res;
        }
    }

    private StringBuilder lastSkip = new StringBuilder();

    private final Cursor position = new Cursor();

    private final BufferedReader reader;

    private final Skip skipPolicy;

    public LineScanner(ParseEnv env, Skip skipPolicy, TokenContainer tokens, Reader reader)
    {
        super(env, tokens);
        this.skipPolicy = skipPolicy;
        this.reader = reader instanceof BufferedReader ? (BufferedReader) reader
                : new BufferedReader(reader);
        nextLine();
    }

    public LineScanner(ParseEnv env, Skip skipPolicy, TokenContainer tokens, String input)
    {
        this(env, skipPolicy, tokens, new StringReader(input));
    }

    public LineScanner(ParseEnv env, TokenContainer tokens, Reader reader)
    {
        this(env, new WhitespaceSkip(), tokens, reader);
    }

    public LineScanner(ParseEnv env, TokenContainer tokens, String input)
    {
        this(env, new WhitespaceSkip(), tokens, new StringReader(input));
    }

    @Override
    public Token advance() throws ParseException
    {
        if (getLastLexeme() == null)
            return null;
        // increase offset at which tokens are read
        this.position.offset += getLastLexeme().getLex().length();

        // end of string?
        while (this.position.offset >= this.position.input.length())
            if (!nextLine())
            {
                terminate();
                return null;
            }
        // before skipping, try to match any token - possible is that
        // some tokens parse characters which are normally skipped
        this.lastSkip.delete(0, this.lastSkip.length());
        while (true)
        {
            if (matchNextToken())
                return getLastLexeme().getToken();
            // not matched - skip one character of whitespace
            char nextChar = this.position.input.charAt(this.position.offset);
            if (this.skipPolicy.skip(nextChar))
            {
                this.lastSkip.append(nextChar);
                this.position.offset++;
                if (this.position.offset >= this.position.input.length())
                    if (!nextLine())
                    {
                        terminate();
                        return null;
                    }
            } else
                break;
        }
        throw parseFailure("Unknown token");
    }

    @Override
    protected Description getCurrentPositionDescription()
    {
        return this.position.clone();
    }

    @Override
    public String getLastSkips()
    {
        return this.lastSkip.toString();
    }

    @Override
    protected String matchToken(Token token)
    {
        // this method can be used outside of advance(), so its
        // possible to produce an index-out-of-bounds exception
        if (this.position.offset >= this.position.input.length())
            return "eol";
        return token.match(this.position.input, this.position.offset);
    }

    private boolean nextLine()
    {
        try
        {
            String in = this.reader.readLine();
            boolean next = in != null;
            if (next)
            {
                this.position.offset = 0;
                this.position.line++;
                this.position.input = in + "\n";
            }
            return next;
        } catch (IOException e)
        {
            return false;
        }
    }

    @Override
    public String toString()
    {
        return this.reader.toString();
    }
}
