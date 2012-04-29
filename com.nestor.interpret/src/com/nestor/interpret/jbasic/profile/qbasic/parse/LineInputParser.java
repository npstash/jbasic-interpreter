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

package com.nestor.interpret.jbasic.profile.qbasic.parse;

import com.nestor.interpret.jbasic.lang.DeclarationException;
import com.nestor.interpret.jbasic.lang.ParseContext;
import com.nestor.interpret.jbasic.lang.VariableAccessor;
import com.nestor.interpret.jbasic.parse.StatementParser;
import com.nestor.interpret.jbasic.parse.SyntaxTokens;
import com.nestor.interpret.jbasic.profile.qbasic.QBasicTokens;
import com.nestor.interpret.jbasic.profile.qbasic.statement.LineInputStatement;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.type.TYPES;

public final class LineInputParser extends StatementParser
{
    public LineInputParser()
    {
        super(true);
    }

    @Override
    protected Statement parseStatement(Scanner scanner, ParseContext context) throws ParseException
    {
        if (scanner.matchAdvance(QBasicTokens.LINE_INPUT) == null)
            return null;

        String varName = scanner.required(SyntaxTokens.IDENTIFIER);
        VariableAccessor var;
        try
        {
            var = context.parseHelper().parseVariableAccess(scanner, varName, TYPES.BOOLEAN_TYPE);
        } catch (DeclarationException e)
        {
            throw scanner.parseFailure(e);
        }

        return new LineInputStatement(var);
    }
}