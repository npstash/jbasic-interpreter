/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-27
 ****************************************************************************/

package com.nestor.interpret.expression.bool;

import java.util.Collection;

import com.nestor.interpret.expression.AbstractOperator;
import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.token.Token;
import com.nestor.interpret.type.BOOLEAN;
import com.nestor.interpret.type.TYPE;
import com.nestor.interpret.type.TYPES;

public abstract class BooleanOperator extends AbstractOperator<BOOLEAN, BOOLEAN>
{
    private static final long serialVersionUID = 1L;

    public static final BooleanOperator ALTERNATIVE = new BooleanOperator(BooleanTokens.OR)
    {
        private static final long serialVersionUID = -4267880699572077783L;

        public BOOLEAN operation(BOOLEAN total, BOOLEAN next) throws EvaluationException
        {
            return new BOOLEAN(total.booleanValue() || next.booleanValue());
        }
    };

    public static final BooleanOperator CONJUNCTION = new BooleanOperator(BooleanTokens.AND)
    {
        private static final long serialVersionUID = 2710295965100731100L;

        public BOOLEAN operation(BOOLEAN total, BOOLEAN next) throws EvaluationException
        {
            return new BOOLEAN(total.booleanValue() && next.booleanValue());
        }
    };

    public static final BooleanOperator EXCLUSION = new BooleanOperator(BooleanTokens.XOR)
    {
        private static final long serialVersionUID = -2486523920450690213L;

        public BOOLEAN operation(BOOLEAN total, BOOLEAN next) throws EvaluationException
        {
            return new BOOLEAN(total.booleanValue() ^ next.booleanValue());
        }
    };

    public static final BooleanOperator IDENTITY = new BooleanOperator(BooleanTokens.IDENTITY)
    {
        private static final long serialVersionUID = 8088015789299989812L;

        public BOOLEAN operation(BOOLEAN total, BOOLEAN next) throws EvaluationException
        {
            return new BOOLEAN(total.booleanValue() == next.booleanValue());
        }
    };

    public static final BooleanOperator IMPLICATION = new BooleanOperator(BooleanTokens.IMPLY)
    {
        private static final long serialVersionUID = -9043525379482547547L;

        public final BOOLEAN operation(BOOLEAN total, BOOLEAN next) throws EvaluationException
        {
            return new BOOLEAN(!total.booleanValue() || next.booleanValue());
        }
    };

    public static BooleanOperator getOperator(Token operationToken)
    {
        if (operationToken.isSame(BooleanTokens.AND))
            return CONJUNCTION;
        else if (operationToken.isSame(BooleanTokens.OR))
            return ALTERNATIVE;
        else if (operationToken.isSame(BooleanTokens.XOR))
            return EXCLUSION;
        else if (operationToken.isSame(BooleanTokens.IDENTITY))
            return IDENTITY;
        else if (operationToken.isSame(BooleanTokens.IMPLY))
            return IMPLICATION;
        throw new IllegalArgumentException("Unknown operation");
    }

    public BooleanOperator(Token operationToken)
    {
        super(operationToken.describe());
    }

    public final TYPE<? extends BOOLEAN> resultType(Collection<TYPE<? extends BOOLEAN>> types)
    {
        return TYPES.BOOLEAN_TYPE;
    }
}
