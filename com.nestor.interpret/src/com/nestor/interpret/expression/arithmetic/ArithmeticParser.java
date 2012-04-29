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

import com.nestor.interpret.expression.ExpressionParser;
import com.nestor.interpret.expression.Value;
import com.nestor.interpret.parse.ChoiceParser;
import com.nestor.interpret.parse.ParseEnv;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Parser;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.type.NUMBER;

public final class ArithmeticParser implements Parser<Value>
{
    private final Parser<Value> delegate;

    public ArithmeticParser(Parser<? extends Value> parserBelow)
    {
        NegativeExpressionParser negative = new NegativeExpressionParser(this);

        ChoiceParser<Value> possibleArithmNegation = new ChoiceParser<Value>(
                ArithmeticExpressionParser.ARITHMETIC_EXPRESSION_DESCRIPTION);
        possibleArithmNegation.addParser(parserBelow);
        possibleArithmNegation.addParser(negative);

        ExpressionParser<NUMBER> poweringParser = new ArithmeticExpressionParser(
                possibleArithmNegation, ArithmeticTokens.POWER);

        ExpressionParser<NUMBER> multiplicationParser = new ArithmeticExpressionParser(
                poweringParser, ArithmeticTokens.TIMES, ArithmeticTokens.DIV);

        ExpressionParser<NUMBER> intDivision = new ArithmeticExpressionParser(multiplicationParser,
                ArithmeticTokens.DIV_INT);

        ExpressionParser<NUMBER> modulo = new ArithmeticExpressionParser(intDivision,
                ArithmeticTokens.MODULO);

        ExpressionParser<NUMBER> additionParser = new ArithmeticExpressionParser(modulo,
                ArithmeticTokens.PLUS, ArithmeticTokens.MINUS);

        this.delegate = additionParser;
    }

    public String describe()
    {
        return ArithmeticExpressionParser.ARITHMETIC_EXPRESSION_DESCRIPTION;
    }

    public Value parse(Scanner scanner, ParseEnv env) throws ParseException
    {
        return this.delegate.parse(scanner, env);
    }
}
