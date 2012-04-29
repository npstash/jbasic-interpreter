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

import com.nestor.interpret.expression.ExpressionParser;
import com.nestor.interpret.expression.Operator;
import com.nestor.interpret.expression.Value;
import com.nestor.interpret.parse.Parser;
import com.nestor.interpret.token.Token;
import com.nestor.interpret.type.BOOLEAN;
import com.nestor.interpret.type.TYPES;

public final class BooleanExpressionParser extends ExpressionParser<BOOLEAN>
{
    static final String BOOLEAN_EXPRESSION_DESCRIPTION = "boolean expression";

    public BooleanExpressionParser(Parser<? extends Value> subparser, Token... operations)
    {
        super(TYPES.BOOLEAN_TYPE, subparser, operations);
    }

    public String describe()
    {
        return BOOLEAN_EXPRESSION_DESCRIPTION;
    }

    @Override
    protected Operator<BOOLEAN, BOOLEAN> getOperator(Token operationToken)
    {
        return BooleanOperator.getOperator(operationToken);
    }
}
