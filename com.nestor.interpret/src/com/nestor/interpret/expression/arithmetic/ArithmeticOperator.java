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

package com.nestor.interpret.expression.arithmetic;

import java.util.Collection;

import com.nestor.interpret.expression.AbstractOperator;
import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.token.Token;
import com.nestor.interpret.type.DOUBLE;
import com.nestor.interpret.type.INTEGER;
import com.nestor.interpret.type.NUMBER;
import com.nestor.interpret.type.TYPE;
import com.nestor.interpret.type.TYPES;

public abstract class ArithmeticOperator extends AbstractOperator<NUMBER, NUMBER>
{
    private static final long serialVersionUID = 1L;

    public static final ArithmeticOperator ADDITION = new ArithmeticOperator(ArithmeticTokens.PLUS)
    {
        private static final long serialVersionUID = 6883611857263050776L;

        public NUMBER operation(NUMBER total, NUMBER next) throws EvaluationException
        {
            if (total instanceof INTEGER && next instanceof INTEGER)
                return new INTEGER(total.intValue() + next.intValue());
            return new DOUBLE(total.doubleValue() + next.doubleValue());
        }

        public TYPE<? extends NUMBER> resultType(Collection<TYPE<? extends NUMBER>> types)
        {
            return standardResultType(types);
        }
    };

    public static final ArithmeticOperator DIVISION = new ArithmeticOperator(ArithmeticTokens.DIV)
    {
        private static final long serialVersionUID = 6602261183518998036L;

        public NUMBER operation(NUMBER total, NUMBER next) throws EvaluationException
        {
            try
            {
                if (total instanceof INTEGER && next instanceof INTEGER)
                    return new INTEGER(total.intValue() / next.intValue());
                return new DOUBLE(total.doubleValue() / next.doubleValue());
            } catch (ArithmeticException e)
            {
                throw new EvaluationException(e);
            }
        }

        public TYPE<? extends NUMBER> resultType(Collection<TYPE<? extends NUMBER>> types)
        {
            return standardResultType(types);
        }
    };

    public static final ArithmeticOperator DIVISION_INT = new ArithmeticOperator(
            ArithmeticTokens.DIV_INT)
    {
        private static final long serialVersionUID = 2946870172647024805L;

        public NUMBER operation(NUMBER total, NUMBER next) throws EvaluationException
        {
            try
            {
                return new INTEGER((int) (total.doubleValue() / next.doubleValue()));
            } catch (ArithmeticException e)
            {
                throw new EvaluationException(e);
            }
        }

        public TYPE<? extends NUMBER> resultType(Collection<TYPE<? extends NUMBER>> types)
        {
            return TYPES.INTEGER_TYPE;
        }
    };

    public static final ArithmeticOperator MULTIPLICATION = new ArithmeticOperator(
            ArithmeticTokens.TIMES)
    {
        private static final long serialVersionUID = 8441644507864634036L;

        public NUMBER operation(NUMBER total, NUMBER next) throws EvaluationException
        {
            if (total instanceof INTEGER && next instanceof INTEGER)
                return new INTEGER(total.intValue() * next.intValue());
            return new DOUBLE(total.doubleValue() * next.doubleValue());
        }

        public TYPE<? extends NUMBER> resultType(Collection<TYPE<? extends NUMBER>> types)
        {
            return standardResultType(types);
        }
    };

    public static final ArithmeticOperator POWERING = new ArithmeticOperator(ArithmeticTokens.POWER)
    {
        private static final long serialVersionUID = -6763566821237159082L;

        public NUMBER operation(NUMBER total, NUMBER next) throws EvaluationException
        {
            if (total instanceof INTEGER && next instanceof INTEGER)
                return new INTEGER((int) Math.pow(total.intValue(), next.intValue()));
            return new DOUBLE(Math.pow(total.doubleValue(), next.doubleValue()));
        }

        public TYPE<? extends NUMBER> resultType(Collection<TYPE<? extends NUMBER>> types)
        {
            return standardResultType(types);
        }
    };

    public static final ArithmeticOperator SUBSTRACTION = new ArithmeticOperator(
            ArithmeticTokens.MINUS)
    {
        private static final long serialVersionUID = -3268237261487364214L;

        public NUMBER operation(NUMBER total, NUMBER next) throws EvaluationException
        {
            if (total instanceof INTEGER && next instanceof INTEGER)
                return new INTEGER(total.intValue() - next.intValue());
            return new DOUBLE(total.doubleValue() - next.doubleValue());
        }

        public TYPE<? extends NUMBER> resultType(Collection<TYPE<? extends NUMBER>> types)
        {
            return standardResultType(types);
        }
    };

    public static final ArithmeticOperator MODULO = new ArithmeticOperator(ArithmeticTokens.MODULO)
    {
        private static final long serialVersionUID = 2515218061274547090L;

        public NUMBER operation(NUMBER total, NUMBER next) throws EvaluationException
        {
            try
            {
                if (total instanceof INTEGER && next instanceof INTEGER)
                    return new INTEGER(total.intValue() % next.intValue());
                return new DOUBLE(total.doubleValue() % next.doubleValue());
            } catch (ArithmeticException e)
            {
                throw new EvaluationException(e);
            }
        }

        public TYPE<? extends NUMBER> resultType(Collection<TYPE<? extends NUMBER>> types)
        {
            return standardResultType(types);
        }
    };

    public static ArithmeticOperator getOperator(Token operationToken)
    {
        if (operationToken.isSame(ArithmeticTokens.PLUS))
            return ADDITION;
        else if (operationToken.isSame(ArithmeticTokens.MINUS))
            return SUBSTRACTION;
        else if (operationToken.isSame(ArithmeticTokens.TIMES))
            return MULTIPLICATION;
        else if (operationToken.isSame(ArithmeticTokens.DIV))
            return DIVISION;
        else if (operationToken.isSame(ArithmeticTokens.DIV_INT))
            return DIVISION_INT;
        else if (operationToken.isSame(ArithmeticTokens.POWER))
            return POWERING;
        else if (operationToken.isSame(ArithmeticTokens.MODULO))
            return MODULO;
        throw new IllegalArgumentException("Unknown operation");
    }

    public ArithmeticOperator(Token operationToken)
    {
        super(operationToken.describe());
    }

    protected static TYPE<? extends NUMBER> standardResultType(
            Collection<TYPE<? extends NUMBER>> types)
    {
        TYPE<? extends NUMBER> result = null;
        for (TYPE<? extends NUMBER> type : types)
            if (result == null)
                result = type;
            else
                result = NUMBER.getBestBaseType(result, type);
        return result;
    }
}
