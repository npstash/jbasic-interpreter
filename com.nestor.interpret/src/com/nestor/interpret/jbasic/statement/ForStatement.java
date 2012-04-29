/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-02-04
 ****************************************************************************/

package com.nestor.interpret.jbasic.statement;

import java.util.ArrayList;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.expression.Expression;
import com.nestor.interpret.expression.Value;
import com.nestor.interpret.expression.arithmetic.ArithmeticOperator;
import com.nestor.interpret.expression.relational.RelationalExpression;
import com.nestor.interpret.expression.relational.RelationalOperator;
import com.nestor.interpret.expression.relational.RelationalTokens;
import com.nestor.interpret.jbasic.lang.RuntimeContext;
import com.nestor.interpret.jbasic.lang.Stack;
import com.nestor.interpret.jbasic.lang.VariableAccessor;
import com.nestor.interpret.jbasic.parse.StatementTokens;
import com.nestor.interpret.type.INTEGER;
import com.nestor.interpret.type.NUMBER;

public final class ForStatement extends Statement
{
    private static final long serialVersionUID = 8292507149760421432L;

    private final Value<? extends NUMBER> from;

    private final Statement loop;

    private final Value<? extends NUMBER> step;

    private final Value<? extends NUMBER> to;

    private final VariableAccessor var;

    public ForStatement(Statement loop, VariableAccessor var, Value<? extends NUMBER> from,
            Value<? extends NUMBER> to, Value<? extends NUMBER> step)
    {
        this.loop = loop;
        this.var = var;
        this.from = from;
        this.to = to;
        this.step = step == null ? new INTEGER(1) : step;
    }

    public String describe()
    {
        return StatementTokens.FOR + " " + this.var + RelationalTokens.EQUAL + this.from.describe()
                + " " + StatementTokens.TO + " " + this.to.describe() + " " + StatementTokens.STEP
                + " " + this.step.describe();
    }

    @SuppressWarnings({
            "unchecked", "cast"
    })
    @Override
    protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
    {
        // initial set
        Value<? extends NUMBER> init = this.from;

        this.var.setVariableValue(context, init.evaluate(context));

        // it must be a number - checked in parser
        Value<? extends NUMBER> numVar = (Value<? extends NUMBER>) ((Value) this.var);

        // build adding statement, for later use
        ArrayList<Value<? extends NUMBER>> adders = new ArrayList<Value<? extends NUMBER>>();
        adders.add(numVar);
        adders.add(this.step);
        Expression<NUMBER> add = new Expression<NUMBER>(ArithmeticOperator.ADDITION, adders);

        RelationalExpression cmp = new RelationalExpression(RelationalOperator.LESS_OR_EQUAL,
                numVar, this.to);

        // do loop
        ReturnCode rc = ReturnCode.SUCCESS;
        // comparing var <= to
        while (cmp.evaluate(context).booleanValue())
        {
            context.stack().new Block(Stack.BlockKind.CODE);
            rc = this.loop.execute(context);
            context.stack().blockEnd();
            if (rc == ReturnCode.NEXT_FOR)
            {
                // next iteration
            } else if (rc == ReturnCode.EXIT_FOR)
                break;
            else if (rc != ReturnCode.SUCCESS)
                return rc;
            // adding var + step
            this.var.setVariableValue(context, add.evaluate(context));
        }
        return ReturnCode.SUCCESS;
    }
}
