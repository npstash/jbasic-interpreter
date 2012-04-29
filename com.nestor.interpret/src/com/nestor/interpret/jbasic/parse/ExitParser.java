/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-04-21
 ****************************************************************************/

package com.nestor.interpret.jbasic.parse;

import com.nestor.interpret.jbasic.lang.ParseContext;
import com.nestor.interpret.jbasic.lang.Stack;
import com.nestor.interpret.jbasic.lang.Stack.Block;
import com.nestor.interpret.jbasic.statement.ExitStatement;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.jbasic.statement.ExitStatement.ExitType;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Scanner;

final class ExitParser extends StatementParser
{
    ExitParser()
    {
        super(true);
    }

    @Override
    protected Statement parseStatement(Scanner scanner, ParseContext context) throws ParseException
    {
        if (scanner.matchAdvance(StatementTokens.EXIT) == null)
            return null;
        if (scanner.matchAdvance(StatementTokens.FOR) != null)
        {
            Block block = context.stack().findBlock(Stack.BlockKind.FOR);
            if (block == null)
                // for block not found
                throw scanner.expected(StatementTokens.FOR);
            return new ExitStatement(ExitType.FOR);
        } else if (scanner.matchAdvance(StatementTokens.DO) != null)
        {
            Block block = context.stack().findBlock(Stack.BlockKind.DOWHILE);
            if (block == null)
                throw scanner.expected(StatementTokens.DO);
            return new ExitStatement(ExitType.DO);
        } else if (scanner.matchAdvance(StatementTokens.FUNCTION) != null)
        {
            // TODO exit fun
        } else if (scanner.matchAdvance(StatementTokens.SUB) != null)
        {
            // TODO exit sub
        } else
            throw scanner.expected("Control block type");
        return null;
    }
}
