/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-26
 ****************************************************************************/

package com.nestor.interpret.expression.bool;

import com.nestor.interpret.expression.ExpressionParser;
import com.nestor.interpret.expression.Value;
import com.nestor.interpret.parse.ChoiceParser;
import com.nestor.interpret.parse.ParseEnv;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Parser;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.type.BOOLEAN;

public final class SequentialBooleanEquationParser implements Parser<Value>
{
    private final Parser<Value> delegate;

    public SequentialBooleanEquationParser(Parser<? extends Value> parserBelow)
    {
        NegationExpressionParser negationParser = new NegationExpressionParser(this);

        ChoiceParser<Value> negationOrBaseParser = new ChoiceParser<Value>(
                BooleanExpressionParser.BOOLEAN_EXPRESSION_DESCRIPTION);
        negationOrBaseParser.addParser(parserBelow);
        negationOrBaseParser.addParser(negationParser);

        ExpressionParser<BOOLEAN> booleanParser = new BooleanExpressionParser(negationOrBaseParser,
                BooleanTokens.AND, BooleanTokens.OR, BooleanTokens.XOR, BooleanTokens.IDENTITY,
                BooleanTokens.IMPLY);

        this.delegate = booleanParser;
    }

    public String describe()
    {
        return BooleanExpressionParser.BOOLEAN_EXPRESSION_DESCRIPTION;
    }

    public Value parse(Scanner scanner, ParseEnv env) throws ParseException
    {
        return this.delegate.parse(scanner, env);
    }
}
