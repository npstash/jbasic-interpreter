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

import com.nestor.interpret.expression.RefinedValueParser;
import com.nestor.interpret.expression.Value;
import com.nestor.interpret.parse.ParseEnv;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Parser;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.type.BOOLEAN;
import com.nestor.interpret.type.TYPES;

public final class NegationExpressionParser extends RefinedValueParser<BOOLEAN>
{
    public NegationExpressionParser(Parser<? extends Value> subparser)
    {
        super(TYPES.BOOLEAN_TYPE, subparser);
    }

    public String describe()
    {
        return BooleanExpressionParser.BOOLEAN_EXPRESSION_DESCRIPTION;
    }

    public Value parse(Scanner scanner, ParseEnv env) throws ParseException
    {
        if (scanner.matchAdvance(BooleanTokens.NOT) == null)
            return null;
        return new NegationExpression(super.requireCompatibleValue(scanner, env));
    }
}
