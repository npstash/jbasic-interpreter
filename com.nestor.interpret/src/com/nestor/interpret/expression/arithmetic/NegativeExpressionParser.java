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

package com.nestor.interpret.expression.arithmetic;

import com.nestor.interpret.expression.RefinedValueParser;
import com.nestor.interpret.expression.Value;
import com.nestor.interpret.parse.ParseEnv;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Parser;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.type.NUMBER;
import com.nestor.interpret.type.TYPES;

public final class NegativeExpressionParser extends RefinedValueParser<NUMBER>
{
    public NegativeExpressionParser(Parser<? extends Value> baseParser)
    {
        super(TYPES.NUMBER_TYPE, baseParser);
    }

    public String describe()
    {
        return ArithmeticExpressionParser.ARITHMETIC_EXPRESSION_DESCRIPTION;
    }

    public Value<NUMBER> parse(Scanner scanner, ParseEnv env) throws ParseException
    {
        if (scanner.matchAdvance(ArithmeticTokens.MINUS) == null)
            return null;
        Value<NUMBER> match = super.requireCompatibleValue(scanner, env);
        return new NegativeExpression(match);
    }
}
