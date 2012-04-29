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

import com.nestor.interpret.jbasic.lang.ParseContext;
import com.nestor.interpret.jbasic.statement.NOP;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.parse.ParseEnv;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Parser;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.token.SimpleToken;
import com.nestor.interpret.token.Token;

public abstract class StatementParser implements Parser<Statement>
{
    public static final Token EOL = new SimpleToken("End of line", "\n");

    public static final String STATEMENT_DESCRIPTION = "statement";

    private final boolean requiresEOLbyDefault;

    protected StatementParser(boolean requiresEOLbyDefault)
    {
        this.requiresEOLbyDefault = requiresEOLbyDefault;
    }

    public String describe()
    {
        return STATEMENT_DESCRIPTION;
    }

    protected final boolean endOfStatementReached(Scanner scanner) throws ParseException
    {
        if (scanner.match(StatementTokens.COMMENT) != null)
            return true;
        if (scanner.match(StatementParser.EOL) != null)
            return true;
        return false;
    }

    protected final boolean isStatementValid(Statement s)
    {
        return s != null && !(s instanceof NOP);
    }

    public final Statement parse(Scanner scanner, ParseEnv env) throws ParseException
    {
        Statement statement;
        try
        {
            statement = parseStatement(scanner, (ParseContext) env);
        } catch (InterpretException e)
        {
            throw scanner.parseFailure(e);
        }
        if (statement == null)
            return null;
        if (this.requiresEOLbyDefault)
            requireEOL(scanner);
        return statement;
    }

    protected abstract Statement parseStatement(Scanner scanner, ParseContext context)
            throws ParseException, InterpretException;

    protected final void requireEOL(Scanner scanner) throws ParseException
    {
        // there can be comment at the end of line
        scanner.matchAdvance(StatementTokens.COMMENT);
        // match end of line
        scanner.required(StatementParser.EOL);
    }
}
