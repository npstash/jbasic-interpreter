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

import com.nestor.interpret.expression.Value;
import com.nestor.interpret.parse.ParseEnv;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Parser;
import com.nestor.interpret.parse.Scanner;

public final class StringParser implements Parser<Value>
{
    private final Parser<? extends Value> delegate;

    public StringParser(Parser<? extends Value> parserBelow)
    {
        Parser<? extends Value> concatParser = new ConcatenationParser(parserBelow);
        Parser<? extends Value> comparParser = new StringEqualityParser(concatParser);
        this.delegate = comparParser;
    }

    public String describe()
    {
        return "string expression";
    }

    public Value parse(Scanner scanner, ParseEnv env) throws ParseException
    {
        return this.delegate.parse(scanner, env);
    }
}
