/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-02-04
 ****************************************************************************/

package com.nestor.interpret.jbasic.parse;

import com.nestor.interpret.expression.Value;
import com.nestor.interpret.jbasic.lang.ParseContext;
import com.nestor.interpret.jbasic.lang.Stack;
import com.nestor.interpret.jbasic.statement.IfEndStatement;
import com.nestor.interpret.jbasic.statement.IfStatement;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.type.BOOLEAN;
import com.nestor.interpret.type.TYPES;

final class IfParser extends StatementParser
{
    IfParser()
    {
        super(false);
    }

    @Override
    protected Statement parseStatement(Scanner scanner, ParseContext context) throws ParseException
    {
        if (scanner.matchAdvance(StatementTokens.IF) == null)
            return null;
        Value<BOOLEAN> cond = context.parseHelper().requireValue(scanner, TYPES.BOOLEAN_TYPE);
        scanner.required(StatementTokens.THEN);
        context.stack().new Block(Stack.BlockKind.IF);
        // parse statement in the same line
        Statement consq = context.statementParser().parse(scanner, context);
        context.stack().blockEnd();
        if (super.isStatementValid(consq))
            // single-line IF
            return new IfStatement(cond, consq);

        // multi-line
        Statement body = nextBlock(scanner, context);
        IfStatement ifstat = new IfStatement(cond, body);

        // load all elseif's
        while (scanner.matchAdvance(StatementTokens.ELSEIF) != null)
        {
            Value<BOOLEAN> alternative = context.parseHelper().requireValue(scanner,
                    TYPES.BOOLEAN_TYPE);
            scanner.required(StatementTokens.THEN);
            Statement elseIfStatement = nextBlock(scanner, context);
            ifstat.addElseIfSatement(alternative, elseIfStatement);
        }

        // load possible else
        if (scanner.matchAdvance(StatementTokens.ELSE) != null)
        {
            context.stack().new Block(Stack.BlockKind.IF);
            Statement elseStatement = context.parseHelper().requireStatementBlock(scanner,
                    IfEndStatement.class);
            context.stack().blockEnd();
            ifstat.setElseSatement(elseStatement);
        }
        // both elseif and EOL already were found
        return ifstat;
    }

    private Statement nextBlock(Scanner scanner, ParseContext context) throws ParseException
    {
        BlockBuilder loopBody = new BlockBuilder(true);
        context.stack().new Block(Stack.BlockKind.IF);
        statements: while (true)
        {
            Statement next = context.statementParser().parse(scanner, context);
            loopBody.addStatement(next);
            if (IfEndStatement.class.isInstance(next))
                break statements;
            if (next == null && scanner.end())
                throw scanner.expected("Unexpected end of file");
            else if (next == null)
            {
                if (scanner.match(StatementTokens.ELSE) != null)
                    break;
                if (scanner.match(StatementTokens.ELSEIF) != null)
                    break;
            }
        }
        context.stack().blockEnd();
        return loopBody.getStatementNonNull();
    }
}