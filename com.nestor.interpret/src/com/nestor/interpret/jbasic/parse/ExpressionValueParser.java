/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-14
 ****************************************************************************/

package com.nestor.interpret.jbasic.parse;

import com.nestor.interpret.expression.ParenthesisParser;
import com.nestor.interpret.expression.RefinedValueParser;
import com.nestor.interpret.expression.Value;
import com.nestor.interpret.expression.arithmetic.ArithmeticParser;
import com.nestor.interpret.expression.bool.SequentialBooleanEquationParser;
import com.nestor.interpret.expression.relational.RelationalExpressionParser;
import com.nestor.interpret.expression.string.StringParser;
import com.nestor.interpret.parse.ChoiceParser;
import com.nestor.interpret.parse.ParseEnv;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Parser;
import com.nestor.interpret.parse.ParserDelegate;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.type.DATA;
import com.nestor.interpret.type.PromotionNotSupportedException;
import com.nestor.interpret.type.TYPE;
import com.nestor.interpret.type.ValuePromotor;

final class ExpressionValueParser<VAL extends DATA> extends RefinedValueParser<VAL>
{
    ExpressionValueParser(TYPE<? extends VAL> valueType)
    {
        this(valueType, null);
    }

    public ExpressionValueParser(TYPE<? extends VAL> valueType, Parser<Value> valueParser)
    {
        super(valueType, null);
        ParserDelegate<Value> delegate = new ParserDelegate<Value>();
        ParenthesisParser<? extends Value> parenthesisParser = new ParenthesisParser<Value>(
                delegate);

        ChoiceParser<Value> baseParser = new ChoiceParser<Value>("value");
        baseParser.addParser(valueParser);
        baseParser.addParser(parenthesisParser);

        Parser<Value> arithmeticParser = new ArithmeticParser(baseParser);
        Parser<Value> relationalParser = new RelationalExpressionParser(arithmeticParser);
        Parser<Value> stringParser = new StringParser(relationalParser);
        Parser<Value> booleanParser = new SequentialBooleanEquationParser(stringParser);

        delegate.setDelegate(booleanParser);
        super.changeSubparser(delegate);
    }

    public String describe()
    {
        return "value of type " + super.getValueClass().describe();
    }

    @SuppressWarnings("unchecked")
    public Value<VAL> parse(Scanner scanner, ParseEnv env) throws ParseException
    {
        ParseException exception = scanner.expected(this);
        Value match = super.seekCompatibleMatch(scanner, env);
        if (match == null)
            // nothing matched at all
            // TODO this is a serious change - expression parser does not require
            // anything to be found at all! test it
            return null;
            // exception.fillInStackTrace();
            // throw exception;
        Value<VAL> value = super.castIfValueCompatible(match);
        if (value == null)
            // attempt to promote
            try
            {
                return (Value<VAL>) ValuePromotor.createPromotor(match, getValueClass());
            } catch (PromotionNotSupportedException e)
            {
                // automatic promotion failed
                exception.fillInStackTrace();
                throw exception;
            }
        return value;
    }
}
