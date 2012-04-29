/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-26
 ****************************************************************************/

package com.nestor.interpret.jbasic.parse;

import com.nestor.interpret.expression.Value;
import com.nestor.interpret.expression.relational.RelationalTokens;
import com.nestor.interpret.jbasic.lang.ParseContext;
import com.nestor.interpret.jbasic.lang.Variable;
import com.nestor.interpret.jbasic.lang.VariableAccessor;
import com.nestor.interpret.jbasic.lang.VariableLookup;
import com.nestor.interpret.jbasic.statement.LetStatement;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.type.DATA;
import com.nestor.interpret.type.TYPE;
import com.nestor.interpret.type.TYPES;

final class LetParser extends StatementParser
{
    LetParser()
    {
        super(true);
    }

    @Override
    protected Statement parseStatement(Scanner scanner, ParseContext context)
            throws ParseException, InterpretException
    {
        // LET statement is optional
        String let = scanner.matchAdvance(StatementTokens.LET);

        String identifier;
        if ((identifier = scanner.match(SyntaxTokens.IDENTIFIER)) == null)
        {
            if (let == null)
                return null;
            throw scanner.expected(SyntaxTokens.IDENTIFIER);
        }
        scanner.advance();

        // cannot use variable parser here, because for unknown
        // variables it returns deafult values of type INTEGER
        final VariableAccessor var;
        final Value<? extends DATA> value;
        if (context.identify(identifier) == null)
        {
            // not known, a new identifier
            scanner.required(RelationalTokens.EQUAL);
            value = context.parseHelper().requireValue(scanner, TYPES.DATA_TYPE);
            TYPE<? extends DATA> valueType = value.getValueType();
            var = new VariableAccessor(VariableLookup.createLookup(context, identifier, valueType));
        } else
        {
            var = context.parseHelper().parseVariableAccess(scanner, identifier,
                    Variable.DEFAULT_TYPE);
            scanner.required(RelationalTokens.EQUAL);
            value = context.parseHelper().requireValue(scanner, var.getValueType());
        }

        return new LetStatement(var, value);
    }
}
