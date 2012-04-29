/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 8 Dec 2007
 ****************************************************************************/

package com.nestor.interpret.jbasic.libs;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.jbasic.lang.DeclarationException;
import com.nestor.interpret.jbasic.lang.Function;
import com.nestor.interpret.jbasic.lang.ModuleBuilder;
import com.nestor.interpret.jbasic.lang.Parameter;
import com.nestor.interpret.jbasic.lang.RedeclarationException;
import com.nestor.interpret.jbasic.lang.RuntimeContext;
import com.nestor.interpret.jbasic.statement.ReturnCode;
import com.nestor.interpret.type.DOUBLE;
import com.nestor.interpret.type.INTEGER;
import com.nestor.interpret.type.NUMBER;
import com.nestor.interpret.type.STRING;
import com.nestor.interpret.type.TYPES;

public class Numbers extends AbstractLibrary
{
    @Override
    public void load(ModuleBuilder program) throws DeclarationException
    {
        loadStatic(program);
    }

    private static void loadStatic(ModuleBuilder program) throws RedeclarationException
    {
        // CLNG
        {
            final Parameter arg = new Parameter("x", TYPES.NUMBER_TYPE);
            final Function fn = new Function("CLNG", TYPES.INTEGER_TYPE, arg);
            fn.implement(new AbstractFunction(fn)
            {
                private static final long serialVersionUID = -3906963767908180552L;

                @Override
                protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
                {
                    NUMBER x = (NUMBER) context.stack().getVariable(arg.getName()).evaluate(context);
                    context.stack().getVariable(fn.getName()).setValue(new INTEGER(x.intValue()));
                    return ReturnCode.SUCCESS;
                }
            });
            program.addFunction(fn);
        }

        // CINT
        {
            final Parameter arg = new Parameter("x", TYPES.NUMBER_TYPE);
            final Function fn = new Function("CINT", TYPES.INTEGER_TYPE, arg);
            fn.implement(new AbstractFunction(fn)
            {
                private static final long serialVersionUID = -3906963767908180552L;

                @Override
                protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
                {
                    NUMBER x = (NUMBER) context.stack().getVariable(arg.getName()).evaluate(context);
                    context.stack().getVariable(fn.getName()).setValue(new INTEGER(x.intValue()));
                    return ReturnCode.SUCCESS;
                }
            });
            program.addFunction(fn);
        }

        // INT
        {
            final Parameter arg = new Parameter("x", TYPES.DOUBLE_TYPE);
            final Function fn = new Function("INT", TYPES.INTEGER_TYPE, arg);
            fn.implement(new AbstractFunction(fn)
            {
                private static final long serialVersionUID = -3906963767908180552L;

                @Override
                protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
                {
                    DOUBLE x = (DOUBLE) context.stack().getVariable(arg.getName()).evaluate(context);
                    context.stack().getVariable(fn.getName()).setValue(new INTEGER(x.intValue()));
                    return ReturnCode.SUCCESS;
                }
            });
            program.addFunction(fn);
        }

        // VAL
        {
            final Parameter arg = new Parameter("str", TYPES.STRING_TYPE);
            final Function fn = new Function("VAL", TYPES.DOUBLE_TYPE, arg);
            fn.implement(new AbstractFunction(fn)
            {
                private static final long serialVersionUID = -3906963767908180552L;

                @Override
                protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
                {
                    STRING str = (STRING) context.stack().getVariable(arg.getName()).evaluate(
                            context);
                    double d;
                    try
                    {
                        d = Double.parseDouble(str.stringValue());
                    } catch (NumberFormatException e)
                    {
                        d = 0;
                    }
                    context.stack().getVariable(fn.getName()).setValue(new DOUBLE(d));
                    return ReturnCode.SUCCESS;
                }
            });
            program.addFunction(fn);
        }

    }
}
