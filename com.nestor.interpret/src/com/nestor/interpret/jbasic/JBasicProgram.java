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

package com.nestor.interpret.jbasic;

import java.io.Serializable;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.jbasic.lang.Module;
import com.nestor.interpret.jbasic.lang.RuntimeContext;
import com.nestor.interpret.jbasic.lang.Stack;
import com.nestor.interpret.jbasic.profile.RuntimeServiceProvider;

public class JBasicProgram implements Serializable
{
    private static final long serialVersionUID = -832688518330411864L;

    private final Module program;

    public JBasicProgram(Module program)
    {
        this.program = program;
    }

    public void run(RuntimeServiceProvider<? extends Throwable> rsp)
    {
        RuntimeContext context = new RuntimeContext(rsp, this.program);

        context.log("Starting " + this.program);

        // initial execution frame
        context.stack().new Frame("MAIN", Stack.BlockKind.TOPLEVEL);
        try
        {
            this.program.execute(context);
        } catch (EvaluationException e)
        {
            context.log(e.getMessage());
        }

        // destroy stack
        context.stack().popFrame();
    }
}
