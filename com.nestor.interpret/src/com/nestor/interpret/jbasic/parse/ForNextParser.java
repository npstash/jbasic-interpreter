/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-04-18
 ****************************************************************************/

package com.nestor.interpret.jbasic.parse;

import com.nestor.interpret.jbasic.lang.ParseContext;
import com.nestor.interpret.jbasic.lang.Stack;
import com.nestor.interpret.jbasic.lang.Stack.ForBlock;
import com.nestor.interpret.jbasic.statement.ForNextStatement;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Scanner;

final class ForNextParser extends StatementParser
{
    ForNextParser()
    {
        super(true);
    }

    @Override
    protected Statement parseStatement(Scanner scanner, ParseContext context) throws ParseException
    {
        if (scanner.match(StatementTokens.NEXT) == null)
            return null;
        ParseException forneeded = scanner.expected(StatementTokens.FOR);
        ForBlock forBlock;
        if (context.stack().getCurrentBlock().getType() == Stack.BlockKind.FOR)
            forBlock = (ForBlock) context.stack().getCurrentBlock();
        else
            throw forneeded;
        scanner.advance();

        // seek variable, optional
        String identifier = scanner.matchAdvance(SyntaxTokens.IDENTIFIER);
        if (identifier != null)
        {
            if (!identifier.equals(forBlock.getCounterVariableName()))
                throw scanner.expected(forBlock.getCounterVariableName());
            return new ForNextStatement(identifier);
        }
        return new ForNextStatement();
    }
}