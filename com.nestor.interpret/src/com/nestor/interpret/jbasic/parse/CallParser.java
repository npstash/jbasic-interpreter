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

import java.util.List;

import com.nestor.interpret.expression.Value;
import com.nestor.interpret.expression.relational.RelationalTokens;
import com.nestor.interpret.jbasic.lang.Function;
import com.nestor.interpret.jbasic.lang.Parameter;
import com.nestor.interpret.jbasic.lang.ParseContext;
import com.nestor.interpret.jbasic.lang.Sub;
import com.nestor.interpret.jbasic.lang.UndeclaredIdentifierException;
import com.nestor.interpret.jbasic.lang.VariableAccessor;
import com.nestor.interpret.jbasic.lang.VariableLookup;
import com.nestor.interpret.jbasic.statement.LetStatement;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.type.DATA;

final class CallParser extends StatementParser
{
    CallParser()
    {
        super(true);
    }

    @Override
    protected Statement parseStatement(Scanner scanner, ParseContext context)
            throws ParseException, UndeclaredIdentifierException
    {
        String identifier = scanner.match(SyntaxTokens.IDENTIFIER);
        if (identifier == null)
            return null;
        if (context.moduleBuilder().containsFunction(identifier))
        {
            scanner.advance();

            // check if we're this this' function body - it might be a return-assignment
            // TODO check it
            if (context.stack().getCurrentFrameName().equals(identifier))
                if (scanner.matchAdvance(RelationalTokens.EQUAL) != null)
                {
                    Function function = context.moduleBuilder().getFunction(identifier);
                    Value<? extends DATA> value = context.parseHelper().requireValue(scanner,
                            function.getReturnType());

                    // treat as local variable, with type as function return type
                    VariableLookup lookup = VariableLookup.createFnReturnVarLookup(context,
                            function);
                    VariableAccessor varAcc = new VariableAccessor(lookup);
                    return new LetStatement(varAcc, value);
                }
            Function function = context.moduleBuilder().getFunction(identifier);
            List<Parameter> parameters = function.getParameters();
            List<Value<? extends DATA>> arguments = context.parseHelper().requireParameterValueList(
                    scanner, parameters, true);
            return function.createCall(arguments);
        } else if (context.moduleBuilder().containsSub(identifier))
        {
            scanner.advance();
            Sub sub = context.moduleBuilder().getSub(identifier);
            List<Parameter> parameters = sub.getParameters();
            List<Value<? extends DATA>> arguments = context.parseHelper().requireParameterValueList(
                    scanner, parameters, true);
            return sub.createCall(arguments);
        }
        return null;
    }
}
