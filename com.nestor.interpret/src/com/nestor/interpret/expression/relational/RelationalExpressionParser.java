/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-12
 ****************************************************************************/

package com.nestor.interpret.expression.relational;

import com.nestor.interpret.expression.RefinedValueParser;
import com.nestor.interpret.expression.Value;
import com.nestor.interpret.parse.ParseEnv;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Parser;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.token.Token;
import com.nestor.interpret.type.NUMBER;
import com.nestor.interpret.type.TYPES;

public final class RelationalExpressionParser extends RefinedValueParser<NUMBER>
{
    public RelationalExpressionParser(Parser<? extends Value> subparser)
    {
        super(TYPES.NUMBER_TYPE, subparser);
    }

    public String describe()
    {
        return "relational expression";
    }

    private RelationalOperator getOperator(Token operationToken)
    {
        return RelationalOperator.getOperator(operationToken);
    }

    public Value parse(Scanner scanner, ParseEnv env) throws ParseException
    {
        Value firstValue = super.seekCompatibleMatch(scanner, env);
        Value<NUMBER> first = super.castIfValueCompatible(firstValue);
        if (first == null)
            return firstValue;
        RelationalExpression equation = null;
        while (true)
        {
            Token operation = RelationalTokens.ALL.matchAdvance(scanner);
            if (operation != null)
            {
                Value<NUMBER> next = super.requireCompatibleValue(scanner, env);
                RelationalOperator operator = getOperator(operation);
                if (equation == null)
                    equation = new RelationalExpression(operator, first, next);
                else
                    equation = new RelationalExpression(operator, equation, next);
            } else
                break;
        }
        if (equation == null)
            return first;
        return equation;
    }
}
