/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-02-03
 ****************************************************************************/

package com.nestor.interpret.jbasic.statement;

import java.util.ArrayList;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.expression.Value;
import com.nestor.interpret.jbasic.lang.RuntimeContext;
import com.nestor.interpret.jbasic.lang.Stack;
import com.nestor.interpret.jbasic.parse.StatementTokens;
import com.nestor.interpret.type.BOOLEAN;

public final class IfStatement extends Statement
{
    private static class ElseIf extends Statement
    {
        private static final long serialVersionUID = -3068049927927598871L;

        final Value<BOOLEAN> condition;

        private final Statement statement;

        public ElseIf(Value<BOOLEAN> condition, Statement statement)
        {
            this.condition = condition;
            this.statement = statement;
        }

        public String describe()
        {
            return this.statement.describe();
        }

        @Override
        protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
        {
            context.stack().new Block(Stack.BlockKind.CODE);
            ReturnCode code = this.statement.execute(context);
            context.stack().blockEnd();
            return code;
        }
    }

    private static final long serialVersionUID = 8587550167494441602L;

    private final Value<BOOLEAN> condition;

    private final ArrayList<ElseIf> elses = new ArrayList<ElseIf>();

    private Statement elseStatement = null;

    private final Statement mainStatement;

    public IfStatement(Value<BOOLEAN> condition, Statement body)
    {
        this.condition = condition;
        this.mainStatement = body;
    }

    public final void addElseIfSatement(Value<BOOLEAN> condition1, Statement elseIfStatement)
    {
        this.elses.add(new ElseIf(condition1, elseIfStatement));
    }

    public String describe()
    {
        String res = StatementTokens.IF + this.condition.describe() + StatementTokens.THEN;
        res += this.mainStatement.describe();
        for (ElseIf elseIf : this.elses)
        {
            res += StatementTokens.ELSEIF + elseIf.condition.describe() + StatementTokens.THEN;
            res += elseIf.describe();
        }
        if (this.elseStatement != null)
        {
            res += StatementTokens.ELSE.describe();
            res += this.elseStatement.describe();
        }
        res += StatementTokens.END.describe() + " " + StatementTokens.IF.describe();
        return res;
    }

    @Override
    protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
    {
        if (this.condition.evaluate(context).booleanValue())
        {
            context.stack().new Block(Stack.BlockKind.CODE);
            ReturnCode code = this.mainStatement.execute(context);
            context.stack().blockEnd();
            return code;
        }
        for (ElseIf elseIf : this.elses)
            if (elseIf.condition.evaluate(context).booleanValue())
                return elseIf.execute(context);
        if (this.elseStatement != null)
        {
            context.stack().new Block(Stack.BlockKind.CODE);
            ReturnCode code = this.elseStatement.execute(context);
            context.stack().blockEnd();
            return code;
        }
        return ReturnCode.SUCCESS;
    }

    public final void setElseSatement(Statement elseStatement)
    {
        this.elseStatement = elseStatement;
    }
}
