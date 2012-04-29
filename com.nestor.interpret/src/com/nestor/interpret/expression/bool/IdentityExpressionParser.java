/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-13
 ****************************************************************************/

package com.nestor.interpret.expression.bool;

import java.util.ArrayList;
import java.util.Collection;

import com.nestor.interpret.expression.RefinedValueParser;
import com.nestor.interpret.expression.Value;
import com.nestor.interpret.parse.ParseEnv;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Parser;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.type.BOOLEAN;
import com.nestor.interpret.type.TYPES;

public final class IdentityExpressionParser extends RefinedValueParser<BOOLEAN>
{
    public IdentityExpressionParser(Parser<Value> parserBelow)
    {
        super(TYPES.BOOLEAN_TYPE, parserBelow);
    }

    public String describe()
    {
        return BooleanExpressionParser.BOOLEAN_EXPRESSION_DESCRIPTION;
    }

    public Value parse(Scanner scanner, ParseEnv env) throws ParseException
    {
        Value valueMatch = super.seekCompatibleMatch(scanner, env);
        Value<BOOLEAN> booleanMatch = super.castIfValueCompatible(valueMatch);
        if (booleanMatch == null)
            return valueMatch;
        if (scanner.match(BooleanTokens.IDENTITY) == null)
            return valueMatch;
        Collection<Value<BOOLEAN>> parts = new ArrayList<Value<BOOLEAN>>();
        parts.add(booleanMatch);
        while (true)
            if (scanner.matchAdvance(BooleanTokens.IDENTITY) != null)
                parts.add(super.requireCompatibleValue(scanner, env));
            else
                break;
        Value<BOOLEAN> identityMatch = new IdentityExpression(parts);
        return identityMatch;
    }
}
