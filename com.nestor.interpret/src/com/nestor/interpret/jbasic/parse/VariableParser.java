/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-27
 ****************************************************************************/

package com.nestor.interpret.jbasic.parse;

import com.nestor.interpret.expression.Value;
import com.nestor.interpret.jbasic.lang.DeclarationException;
import com.nestor.interpret.jbasic.lang.ParseContext;
import com.nestor.interpret.jbasic.lang.Variable;
import com.nestor.interpret.parse.ParseEnv;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Parser;
import com.nestor.interpret.parse.Scanner;

final class VariableParser implements Parser<Value>
{
    private final ParseContext context;

    VariableParser(ParseContext context)
    {
        this.context = context;
    }

    public String describe()
    {
        return "variable";
    }

    public Value parse(Scanner scanner, ParseEnv env) throws ParseException
    {
        // get variable / function identifier
        String identifier;
        if ((identifier = scanner.match(SyntaxTokens.IDENTIFIER)) == null)
            return null;
        scanner.advance();

        // will either find or return default value of default type
        try
        {
            return this.context.parseHelper().parseVariableAccess(scanner, identifier,
                    Variable.DEFAULT_TYPE);
        } catch (DeclarationException e)
        {
            // cannot throw it, so change to parse exception
            throw scanner.parseFailure(e);
        }
    }
}