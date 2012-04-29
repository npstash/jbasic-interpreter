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
import com.nestor.interpret.jbasic.statement.ExitStatement;
import com.nestor.interpret.jbasic.statement.FunctionEndStatement;
import com.nestor.interpret.jbasic.statement.IfEndStatement;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.jbasic.statement.SubEndStatement;
import com.nestor.interpret.jbasic.statement.ExitStatement.ExitType;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Scanner;

final class EndParser extends StatementParser
{
    EndParser()
    {
        super(true);
    }

    @Override
    protected Statement parseStatement(Scanner scanner, ParseContext context)
            throws ParseException, InterpretException
    {
        if (scanner.matchAdvance(StatementTokens.END) == null)
            return null;
        if (scanner.matchAdvance(StatementTokens.FUNCTION) != null)
        {
            // must be in function body
            if (context.stack().getCurrentBlock().getType() != Stack.BlockKind.FUNCTION)
                throw scanner.expected(StatementTokens.FUNCTION);
            return new FunctionEndStatement();

        } else if (scanner.matchAdvance(StatementTokens.IF) != null)
        {
            ParseException ifneeded = scanner.expected(StatementTokens.IF);
            if (context.stack().getCurrentBlock().getType() != Stack.BlockKind.IF)
                throw ifneeded;
            // its a no-op
            return new IfEndStatement();

        } else if (scanner.matchAdvance(StatementTokens.SUB) != null)
        {
            // must be in sub body
            if (context.stack().getCurrentBlock().getType() != Stack.BlockKind.SUB)
                throw scanner.expected(StatementTokens.SUB);
            return new SubEndStatement();

        } else
            return new ExitStatement(ExitType.END);
    }
}
