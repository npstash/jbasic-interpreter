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

import com.nestor.interpret.expression.Value;
import com.nestor.interpret.jbasic.lang.DeclarationException;
import com.nestor.interpret.jbasic.lang.ParseContext;
import com.nestor.interpret.parse.ParseEnv;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Parser;
import com.nestor.interpret.parse.Scanner;

public class ConstantValueParser implements Parser<Value>
{
    private final ParseContext context;

    ConstantValueParser(ParseContext context)
    {
        this.context = context;
    }

    public String describe()
    {
        return "variable";
    }

    public Value parse(Scanner scanner, ParseEnv env) throws ParseException
    {
        // get variable
        String identifier;
        if ((identifier = scanner.match(SyntaxTokens.IDENTIFIER)) == null)
            return null;
        if (!this.context.moduleBuilder().containsConstant(identifier))
            return null;
        scanner.advance();

        try
        {
            return this.context.moduleBuilder().getConstant(identifier);
        } catch (DeclarationException e)
        {
            throw scanner.parseFailure(e);
        }
    }
}
