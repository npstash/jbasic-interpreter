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

package com.nestor.interpret.jbasic.statement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.jbasic.lang.RuntimeContext;
import com.nestor.interpret.jbasic.lang.Stack;

public final class StatementBlock extends Statement implements Iterable<Statement>
{
    private static final long serialVersionUID = 2249317006357650083L;

    private final ArrayList<Statement> statements = new ArrayList<Statement>();

    private final boolean newBlock;

    public StatementBlock(boolean newBlock, Collection<Statement> statements)
    {
        this.newBlock = newBlock;
        this.statements.addAll(statements);
    }

    public StatementBlock(boolean newBlock, Statement... statements)
    {
        this.newBlock = newBlock;
        for (Statement statement : statements)
            this.statements.add(statement);
    }

    /**
     * @param e
     * @return <code>true</code> if any statement has been actually added, <code>false</code>
     *         otherwise
     */
    public boolean add(Statement e)
    {
        if (e == NOP.INSTANCE || e == null)
            return false;
        if (e instanceof StatementBlock && !((StatementBlock) e).newBlock)
        {
            // statement block which require new-block cannot be merged.
            StatementBlock sb = (StatementBlock) e;
            boolean res = false;
            for (Statement statement : sb)
                res = res | add(statement);
            return res;
        }
        this.statements.add(e);
        return true;
    }

    public String describe()
    {
        return "statement";
    }

    @Override
    protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
    {
        if (this.newBlock)
            context.stack().new Block(Stack.BlockKind.CODE);
        for (Statement statement : this.statements)
        {
            ReturnCode code = statement.execute(context);
            if (code != ReturnCode.SUCCESS)
            {
                if (this.newBlock)
                    context.stack().blockEnd();
                return code;
            }
        }
        if (this.newBlock)
            context.stack().blockEnd();
        return ReturnCode.SUCCESS;
    }

    public Iterator<Statement> iterator()
    {
        return this.statements.iterator();
    }
}
