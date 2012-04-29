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

import com.nestor.interpret.jbasic.lang.ParseContext;
import com.nestor.interpret.jbasic.lang.Stack;
import com.nestor.interpret.jbasic.statement.DoWhileLoopStatement;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Scanner;

final class DoWhileLoopParser extends StatementParser
{
    DoWhileLoopParser()
    {
        super(true);
    }

    @Override
    protected Statement parseStatement(Scanner scanner, ParseContext context) throws ParseException
    {
        if (scanner.match(StatementTokens.LOOP) == null)
            return null;
        ParseException doNeeded = scanner.expected(StatementTokens.DO);
        if (context.stack().getCurrentBlock().getType() != Stack.BlockKind.DOWHILE)
            throw doNeeded;
        scanner.advance();
        return new DoWhileLoopStatement();
    }
}
