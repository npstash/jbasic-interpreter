/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-09
 ****************************************************************************/

package com.nestor.interpret.expression.arithmetic;

import com.nestor.interpret.expression.ExpressionParser;
import com.nestor.interpret.expression.Operator;
import com.nestor.interpret.expression.Value;
import com.nestor.interpret.parse.Parser;
import com.nestor.interpret.token.Token;
import com.nestor.interpret.type.NUMBER;
import com.nestor.interpret.type.TYPES;

public final class ArithmeticExpressionParser extends ExpressionParser<NUMBER>
{
    static final String ARITHMETIC_EXPRESSION_DESCRIPTION = "arithmetic expression";

    public ArithmeticExpressionParser(Parser<? extends Value> subparser, Token... operations)
    {
        super(TYPES.NUMBER_TYPE, subparser, operations);
    }

    @Override
    protected Operator<NUMBER, NUMBER> getOperator(Token operationToken)
    {
        return ArithmeticOperator.getOperator(operationToken);
    }

    public final String describe()
    {
        return ARITHMETIC_EXPRESSION_DESCRIPTION;
    }
}
