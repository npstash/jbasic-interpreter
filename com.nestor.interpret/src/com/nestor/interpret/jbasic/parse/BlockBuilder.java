/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-28
 ****************************************************************************/

package com.nestor.interpret.jbasic.parse;

import com.nestor.interpret.jbasic.statement.NOP;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.jbasic.statement.StatementBlock;

public final class BlockBuilder
{
    private boolean first = true;

    private Statement total = null;

    private final boolean newStackBlock;

    public BlockBuilder(boolean newStackBlock)
    {
        this.newStackBlock = newStackBlock;
    }

    public void addStatement(Statement statement)
    {
        // TODO reject NOP's?
        if (this.total == null)
            this.total = statement;
        else if (this.first)
        {
            StatementBlock block = new StatementBlock(this.newStackBlock);
            block.add(this.total);
            block.add(statement);
            this.total = block;
            this.first = false;
        } else
            ((StatementBlock) this.total).add(statement);
    }

    /**
     * Returns <code>null</code> if no statements were added.
     * 
     * @return
     */
    public Statement getStatement()
    {
        return this.total;
    }

    public Statement getStatementNonNull()
    {
        if (this.total == null)
            return NOP.INSTANCE;
        return this.total;
    }
}
