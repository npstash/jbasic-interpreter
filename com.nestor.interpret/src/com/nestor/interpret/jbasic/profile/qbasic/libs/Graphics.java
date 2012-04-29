/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 4 Dec 2007
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
import com.nestor.interpret.jbasic.statement.ReturnCode;
import com.nestor.interpret.type.TYPES;

public class Graphics extends AbstractLibrary
{
    private static Graphics instance = null;

    public static synchronized Graphics getInstance()
    {
        if (instance == null)
            instance = new Graphics();
        return instance;
    }

    @Override
    public void load(ModuleBuilder program) throws DeclarationException
    {
        loadStatic(program);
    }

    private static void loadStatic(ModuleBuilder program) throws RedeclarationException
    {
        // SCREEN
        {
            Parameter arg1 = new Parameter("mode", TYPES.INTEGER_TYPE);
            Parameter arg2 = new Parameter("color", TYPES.INTEGER_TYPE);
            Parameter arg3 = new Parameter("apage", TYPES.INTEGER_TYPE);
            Parameter arg4 = new Parameter("vpage", TYPES.INTEGER_TYPE);
            Sub sub = new Sub("SCREEN", arg1, arg2, arg3, arg4);
            sub.implement(new AbstractSub(sub)
            {
                private static final long serialVersionUID = -3906963767908180552L;

                @Override
                protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
                {
                    return ReturnCode.SUCCESS;
                }
            });
            program.addSub(sub);
        }
    }
}
