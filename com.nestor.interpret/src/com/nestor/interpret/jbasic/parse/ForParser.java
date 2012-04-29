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
import com.nestor.interpret.expression.relational.RelationalTokens;
import com.nestor.interpret.jbasic.lang.DeclarationException;
import com.nestor.interpret.jbasic.lang.ParseContext;
import com.nestor.interpret.jbasic.lang.VariableAccessor;
import com.nestor.interpret.jbasic.statement.ForNextStatement;
import com.nestor.interpret.jbasic.statement.ForStatement;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.type.NUMBER;
import com.nestor.interpret.type.PromotionNotSupportedException;
import com.nestor.interpret.type.TYPE;
import com.nestor.interpret.type.TYPES;
import com.nestor.interpret.type.ValuePromotor;

final class ForParser extends StatementParser
{
    ForParser()
    {
        super(false);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Statement parseStatement(Scanner scanner, ParseContext context)
            throws ParseException, DeclarationException
    {
        if (scanner.matchAdvance(StatementTokens.FOR) == null)
            return null;
        ParseException expectedNumber = scanner.expected("numeric variable");
        String identifier = scanner.required(SyntaxTokens.IDENTIFIER);
        scanner.required(RelationalTokens.EQUAL);

        // find ranges
        Value<? extends NUMBER> from = context.parseHelper().requireValue(scanner,
                TYPES.NUMBER_TYPE);
        scanner.required(StatementTokens.TO);
        Value<? extends NUMBER> to = context.parseHelper().requireValue(scanner, TYPES.NUMBER_TYPE);

        // check if identifier has proper type, and if it is known
        TYPE<? extends NUMBER> bestBaseType = NUMBER.getBestBaseType(from.getValueType(),
                to.getValueType());
        VariableAccessor var = context.parseHelper().parseVariableAccess(scanner, identifier,
                bestBaseType);

        // must be a number - see for-statement
        if (!TYPES.NUMBER_TYPE.isAssignableFrom(var.getValueType()))
        {
            expectedNumber.fillInStackTrace();
            throw expectedNumber;
        }

        // create promotors for bounds - could not be done earlier because exact counter type
        // was not known
        try
        {
            from = (Value<? extends NUMBER>) ValuePromotor.createPromotor(from, var.getValueType());
            to = (Value<? extends NUMBER>) ValuePromotor.createPromotor(to, var.getValueType());
        } catch (PromotionNotSupportedException e)
        {
            throw scanner.parseFailure(e);
        }

        // optional STEP instruction
        Value<? extends NUMBER> step = null;
        if (scanner.matchAdvance(StatementTokens.STEP) != null)
            // require that step is same value or var, or promotable to it (automatically
            // checked by ValueParser)
            step = (Value<? extends NUMBER>) context.parseHelper().requireValue(scanner,
                    var.getValueType());
        super.requireEOL(scanner);

        // now the loop body, create a block first
        context.stack().new ForBlock(identifier);
        Statement loop = context.parseHelper().requireStatementBlock(scanner,
                ForNextStatement.class);
        context.stack().blockEnd();

        return new ForStatement(loop, var, from, to, step);
    }
}
