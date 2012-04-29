/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-30
 ****************************************************************************/

package com.nestor.interpret.jbasic.libs;

import java.util.Random;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.jbasic.lang.DeclarationException;
import com.nestor.interpret.jbasic.lang.Function;
import com.nestor.interpret.jbasic.lang.ModuleBuilder;
import com.nestor.interpret.jbasic.lang.Parameter;
import com.nestor.interpret.jbasic.lang.RedeclarationException;
import com.nestor.interpret.jbasic.lang.RuntimeContext;
import com.nestor.interpret.jbasic.lang.Sub;
import com.nestor.interpret.jbasic.statement.ReturnCode;
import com.nestor.interpret.type.DOUBLE;
import com.nestor.interpret.type.INTEGER;
import com.nestor.interpret.type.TYPES;

public class Std extends AbstractLibrary
{
    private Random random = new Random();

    @Override
    public void load(ModuleBuilder program) throws DeclarationException
    {
        loadStatic(program, this);
    }

    private static void loadStatic(ModuleBuilder program, Std std) throws RedeclarationException
    {
        // RND
        {
            final Function fn = new Function("RND", TYPES.DOUBLE_TYPE);
            final Random random = std.random;
            fn.implement(new AbstractFunction(fn)
            {
                private static final long serialVersionUID = 1091470126508741001L;

                @Override
                protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
                {
                    double rnd = random.nextDouble();
                    context.stack().getVariable(fn.getName()).setValue(new DOUBLE(rnd));
                    return ReturnCode.SUCCESS;
                }
            });
            program.addFunction(fn);
        }

        // RANDOMIZE
        {
            final Parameter arg = new Parameter("seed", TYPES.INTEGER_TYPE);
            final Sub sub = new Sub("RANDOMIZE", arg);
            final Random random = std.random;
            sub.implement(new AbstractSub(sub)
            {
                private static final long serialVersionUID = 0L;

                @Override
                protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
                {
                    INTEGER s = (INTEGER) context.stack().getVariable(arg.getName()).evaluate(
                            context);
                    random.setSeed(s.intValue());
                    return ReturnCode.SUCCESS;
                }
            });
            program.addSub(sub);
        }

        // TIMER
        {
            final Function fn = new Function("TIMER", TYPES.INTEGER_TYPE);
            fn.implement(new AbstractFunction(fn)
            {
                private static final long serialVersionUID = 1091470126508741001L;

                @Override
                protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
                {
                    // TODO
                    INTEGER time = new INTEGER((int) System.currentTimeMillis());
                    context.stack().getVariable(fn.getName()).setValue(time);
                    return ReturnCode.SUCCESS;
                }
            });
            program.addFunction(fn);
        }
    }
}
