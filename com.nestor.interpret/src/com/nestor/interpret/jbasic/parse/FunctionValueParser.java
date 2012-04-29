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

import java.util.Collection;
import java.util.List;

import com.nestor.interpret.expression.Value;
import com.nestor.interpret.jbasic.lang.Function;
import com.nestor.interpret.jbasic.lang.Parameter;
import com.nestor.interpret.jbasic.lang.ParseContext;
import com.nestor.interpret.jbasic.lang.UndeclaredIdentifierException;
import com.nestor.interpret.parse.ParseEnv;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Parser;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.type.DATA;

final class FunctionValueParser implements Parser<Value>
{
    private final ParseContext context;

    FunctionValueParser(ParseContext context)
    {
        this.context = context;
    }

    public String describe()
    {
        return "function call";
    }

    public Value parse(Scanner scanner, ParseEnv env) throws ParseException
    {
        String identifier;
        if ((identifier = scanner.match(SyntaxTokens.IDENTIFIER)) == null)
            return null;
        if (this.context.moduleBuilder().containsFunction(identifier))
        {
            scanner.advance();
            Function function;
            try
            {
                function = this.context.moduleBuilder().getFunction(identifier);
            } catch (UndeclaredIdentifierException e)
            {
                throw scanner.parseFailure(e);
            }

            // parse arguments
            Collection<Parameter> parameters = function.getParameters();
            List<Value<? extends DATA>> arguments = this.context.parseHelper().requireParameterValueList(
                    scanner, parameters, false);
            return function.createCall(arguments);
        }
        return null;
    }
}
