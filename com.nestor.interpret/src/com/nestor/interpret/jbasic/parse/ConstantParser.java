/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-07-15
 ****************************************************************************/

package com.nestor.interpret.jbasic.parse;

import com.nestor.interpret.expression.relational.RelationalTokens;
import com.nestor.interpret.jbasic.lang.Constant;
import com.nestor.interpret.jbasic.lang.ParseContext;
import com.nestor.interpret.jbasic.lang.RedeclarationException;
import com.nestor.interpret.jbasic.lang.Variable;
import com.nestor.interpret.jbasic.statement.NOP;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.token.StandardTokens;
import com.nestor.interpret.type.DATA;
import com.nestor.interpret.type.TYPE;

final class ConstantParser extends StatementParser
{
    ConstantParser()
    {
        super(true);
    }

    @Override
    protected Statement parseStatement(Scanner scanner, ParseContext context)
            throws ParseException, RedeclarationException
    {
        // mandatory declaration statement
        if (scanner.matchAdvance(StatementTokens.CONST) == null)
            return null;
        do
        {
            String identifier = scanner.required(SyntaxTokens.IDENTIFIER);
            TYPE<? extends DATA> type;
            if (scanner.matchAdvance(StatementTokens.AS) != null)
                type = context.parseHelper().requireTypeDeclaration(scanner);
            else
                type = Variable.DEFAULT_TYPE;
            scanner.required(RelationalTokens.EQUAL);
            DATA value = context.parseHelper().requireConstantValue(scanner, type);
            context.moduleBuilder().addConstant(new Constant(identifier, value));
        } while (scanner.matchAdvance(StandardTokens.COMMA) != null);
        return NOP.INSTANCE;
    }
}
