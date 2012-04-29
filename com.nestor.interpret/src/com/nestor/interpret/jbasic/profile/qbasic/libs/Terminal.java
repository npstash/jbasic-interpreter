/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 6 Dec 2007
 ****************************************************************************/

package com.nestor.interpret.jbasic.profile.qbasic.libs;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.jbasic.lang.DeclarationException;
import com.nestor.interpret.jbasic.lang.ModuleBuilder;
import com.nestor.interpret.jbasic.lang.Parameter;
import com.nestor.interpret.jbasic.lang.RedeclarationException;
import com.nestor.interpret.jbasic.lang.RuntimeContext;
import com.nestor.interpret.jbasic.lang.Sub;
import com.nestor.interpret.jbasic.libs.AbstractLibrary;
import com.nestor.interpret.jbasic.libs.AbstractSub;
import com.nestor.interpret.jbasic.profile.qbasic.parse.TextServices;
import com.nestor.interpret.jbasic.statement.ReturnCode;
import com.nestor.interpret.type.INTEGER;
import com.nestor.interpret.type.TYPES;

public class Terminal extends AbstractLibrary
{
    @Override
    public void load(ModuleBuilder program) throws DeclarationException
    {
        loadStatic(program);
    }

    private static void loadStatic(ModuleBuilder program) throws RedeclarationException
    {
        // CLS
        {
            Sub sub = new Sub("CLS");
            sub.implement(new AbstractSub(sub)
            {
                private static final long serialVersionUID = -3906963767908180552L;

                @Override
                protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
                {
                    context.getRuntimeService(TextServices.class).cls();
                    return ReturnCode.SUCCESS;
                }
            });
            program.addSub(sub);
        }

        // COLOR
        {
            final Parameter arg = new Parameter("colorName", TYPES.INTEGER_TYPE);
            Sub sub = new Sub("COLOR", arg);
            sub.implement(new AbstractSub(sub)
            {
                private static final long serialVersionUID = 770401467731574186L;

                @Override
                protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
                {
                    INTEGER c = (INTEGER) context.stack().getVariable(arg.getName()).evaluate(
                            context);
                    context.getRuntimeService(TextServices.class).color(c.intValue());
                    return ReturnCode.SUCCESS;
                }
            });
            program.addSub(sub);
        }
    }
}
