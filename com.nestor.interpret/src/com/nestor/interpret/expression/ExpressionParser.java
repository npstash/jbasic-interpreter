/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-06
 ****************************************************************************/

package com.nestor.interpret.expression;

import java.util.ArrayList;
import java.util.Arrays;

import com.nestor.interpret.parse.ParseEnv;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Parser;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.token.Token;
import com.nestor.interpret.token.MultiToken;
import com.nestor.interpret.type.DATA;
import com.nestor.interpret.type.TYPE;

public abstract class ExpressionParser<VAL extends DATA> extends RefinedValueParser<VAL>
{
    private final MultiToken operations;

    public ExpressionParser(TYPE<VAL> valueClass, Parser<? extends Value> subparser,
            Token... operations)
    {
        super(valueClass, subparser);
        this.operations = new MultiToken(Arrays.asList(operations));
    }

    private Value<VAL> createExpressionMatch(Operator<VAL, VAL> operation, Value<VAL> left,
            Value<VAL> right)
    {
        ArrayList<Value<VAL>> values = new ArrayList<Value<VAL>>();
        values.add(left);
        values.add(right);
        return new Expression<VAL>(operation, values);
    }

    protected abstract Operator<VAL, VAL> getOperator(Token operationToken);

    public final Value parse(Scanner scanner, ParseEnv env) throws ParseException
    {
        Value leftValue = super.seekCompatibleMatch(scanner, env);
        Value<VAL> left = super.castIfValueCompatible(leftValue);
        if (left == null)
            return leftValue;
        while (true)
        {
            Token operation = this.operations.matchAdvance(scanner);
            if (operation != null)
            {
                Value<VAL> right = super.requireCompatibleValue(scanner, env);
                // type compatibility is assured by checks in superclass
                left = createExpressionMatch(getOperator(operation), left, right);
            } else
                break;
        }
        return left;
    }
}
