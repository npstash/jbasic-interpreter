/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 5 Dec 2007
 ****************************************************************************/

package com.nestor.interpret.jbasic.parse;

import com.nestor.interpret.expression.Value;
import com.nestor.interpret.jbasic.lang.DeclarationException;
import com.nestor.interpret.jbasic.lang.ParseContext;
import com.nestor.interpret.jbasic.lang.Stack.BlockKind;
import com.nestor.interpret.jbasic.statement.DoWhileLoopStatement;
import com.nestor.interpret.jbasic.statement.DoWhileStatement;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.type.BOOLEAN;
import com.nestor.interpret.type.TYPES;

final class DoWhileParser extends StatementParser
{
    DoWhileParser()
    {
        super(false);
    }

    @Override
    protected Statement parseStatement(Scanner scanner, ParseContext context)
            throws ParseException, DeclarationException
    {
        if (scanner.matchAdvance(StatementTokens.DO) == null)
            return null;

        // condition - optional
        Value<BOOLEAN> condition = null;
        if (scanner.matchAdvance(StatementTokens.WHILE) != null)
            condition = context.parseHelper().requireValue(scanner, TYPES.BOOLEAN_TYPE);
        super.requireEOL(scanner);

        // now the loop body, create a block first
        context.stack().new Block(BlockKind.DOWHILE);
        Statement loop = context.parseHelper().requireStatementBlock(scanner,
                DoWhileLoopStatement.class);
        context.stack().blockEnd();

        return new DoWhileStatement(loop, condition);
    }
}
