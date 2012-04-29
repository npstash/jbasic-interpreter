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

package com.nestor.interpret.expression.relational;

import java.util.Collection;

import com.nestor.interpret.expression.AbstractOperator;
import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.token.Token;
import com.nestor.interpret.type.BOOLEAN;
import com.nestor.interpret.type.NUMBER;
import com.nestor.interpret.type.TYPE;
import com.nestor.interpret.type.TYPES;

public abstract class RelationalOperator extends AbstractOperator<BOOLEAN, NUMBER>
{
    private static final long serialVersionUID = 1L;

    public static final RelationalOperator EQUAL = new RelationalOperator(RelationalTokens.EQUAL)
    {
        private static final long serialVersionUID = -3121262740382922978L;

        public BOOLEAN operation(NUMBER previous, NUMBER next) throws EvaluationException
        {
            return new BOOLEAN(previous.doubleValue() == next.doubleValue());
        }
    };

    public static final RelationalOperator GREATER = new RelationalOperator(
            RelationalTokens.GREATER)
    {
        private static final long serialVersionUID = -547774211432527353L;

        public BOOLEAN operation(NUMBER previous, NUMBER next) throws EvaluationException
        {
            return new BOOLEAN(previous.doubleValue() > next.doubleValue());
        }
    };

    public static final RelationalOperator GREATER_OR_EQUAL = new RelationalOperator(
            RelationalTokens.GREATER_OR_EQUAL)
    {
        private static final long serialVersionUID = -8066958577373904383L;

        public BOOLEAN operation(NUMBER previous, NUMBER next) throws EvaluationException
        {
            return new BOOLEAN(previous.doubleValue() >= next.doubleValue());
        }
    };

    public static final RelationalOperator LESS = new RelationalOperator(RelationalTokens.LESS)
    {
        private static final long serialVersionUID = 7034724645998381263L;

        public BOOLEAN operation(NUMBER previous, NUMBER next) throws EvaluationException
        {
            return new BOOLEAN(previous.doubleValue() < next.doubleValue());
        }
    };

    public static final RelationalOperator LESS_OR_EQUAL = new RelationalOperator(
            RelationalTokens.LESS_OR_EQUAL)
    {
        private static final long serialVersionUID = 4321360464574182725L;

        public BOOLEAN operation(NUMBER previous, NUMBER next) throws EvaluationException
        {
            return new BOOLEAN(previous.doubleValue() <= next.doubleValue());
        }
    };

    public static final RelationalOperator NOT_EQUAL = new RelationalOperator(
            RelationalTokens.NOT_EQUAL)
    {
        private static final long serialVersionUID = -962793813238454807L;

        public BOOLEAN operation(NUMBER previous, NUMBER next) throws EvaluationException
        {
            return new BOOLEAN(previous.doubleValue() != next.doubleValue());
        }
    };

    public static RelationalOperator getOperator(Token operationToken)
    {
        if (operationToken.isSame(RelationalTokens.EQUAL))
            return EQUAL;
        else if (operationToken.isSame(RelationalTokens.GREATER))
            return GREATER;
        else if (operationToken.isSame(RelationalTokens.GREATER_OR_EQUAL))
            return GREATER_OR_EQUAL;
        else if (operationToken.isSame(RelationalTokens.LESS))
            return LESS;
        else if (operationToken.isSame(RelationalTokens.LESS_OR_EQUAL))
            return LESS_OR_EQUAL;
        else if (operationToken.isSame(RelationalTokens.NOT_EQUAL))
            return NOT_EQUAL;
        throw new IllegalArgumentException("Unknown operation");
    }

    public RelationalOperator(Token operationToken)
    {
        super(operationToken.describe());
    }

    public final TYPE<? extends BOOLEAN> resultType(Collection<TYPE<? extends NUMBER>> types)
    {
        return TYPES.BOOLEAN_TYPE;
    }
}
