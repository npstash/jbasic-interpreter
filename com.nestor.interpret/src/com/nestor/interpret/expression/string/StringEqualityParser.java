/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-02-03
 ****************************************************************************/

package com.nestor.interpret.expression.string;

import com.nestor.interpret.expression.RefinedValueParser;
import com.nestor.interpret.expression.Value;
import com.nestor.interpret.expression.relational.RelationalTokens;
import com.nestor.interpret.parse.ParseEnv;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Parser;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.type.STRING;
import com.nestor.interpret.type.TYPES;

public final class StringEqualityParser extends RefinedValueParser<STRING>
{
    public StringEqualityParser(Parser<? extends Value> subparser)
    {
        super(TYPES.STRING_TYPE, subparser);
    }

    public String describe()
    {
        return "string comparison";
    }

    public Value parse(Scanner scanner, ParseEnv env) throws ParseException
    {
        Value value = super.seekCompatibleMatch(scanner, env);
        Value<STRING> stringValue = super.castIfValueCompatible(value);
        if (stringValue == null)
            return value;
        if (scanner.matchAdvance(RelationalTokens.EQUAL) != null)
        {
            Value<STRING> rightSide = super.requireCompatibleValue(scanner, env);
            return new Comparison(stringValue, rightSide);
        }
        return value;
    }
}
