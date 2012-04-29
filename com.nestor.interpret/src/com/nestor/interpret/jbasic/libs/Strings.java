/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 5 Dec 2007
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
import com.nestor.interpret.type.INTEGER;
import com.nestor.interpret.type.STRING;
import com.nestor.interpret.type.TYPES;

public class Strings extends AbstractLibrary
{
    @Override
    public void load(ModuleBuilder program) throws DeclarationException
    {
        loadStatic(program);
    }

    private static void loadStatic(ModuleBuilder program) throws RedeclarationException
    {
        // STRING$
        {
            final Parameter arg1 = new Parameter("len", TYPES.INTEGER_TYPE);
            final Parameter arg2 = new Parameter("chr", TYPES.INTEGER_TYPE);
            final Function fn = new Function("STRING$", TYPES.STRING_TYPE, arg1, arg2);
            fn.implement(new AbstractFunction(fn)
            {
                private static final long serialVersionUID = -5474072150376118264L;

                @Override
                protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
                {
                    INTEGER len = (INTEGER) context.stack().getVariable(arg1.getName()).evaluate(
                            context);
                    INTEGER chr = (INTEGER) context.stack().getVariable(arg2.getName()).evaluate(
                            context);
                    StringBuilder sb = new StringBuilder();
                    int ilen = len.intValue();
                    char cchr = (char) chr.intValue();
                    for (int i = 0; i < ilen; i++)
                        sb.append(cchr);
                    context.stack().getVariable(fn.getName()).setValue(new STRING(sb.toString()));
                    return ReturnCode.SUCCESS;
                }
            });
            program.addFunction(fn);
        }

        // LEFT$
        {
            final Parameter arg1 = new Parameter("str", TYPES.STRING_TYPE);
            final Parameter arg2 = new Parameter("offs", TYPES.INTEGER_TYPE);
            final Function fn = new Function("LEFT$", TYPES.STRING_TYPE, arg1, arg2);
            fn.implement(new AbstractFunction(fn)
            {
                private static final long serialVersionUID = -813465557653728567L;

                @Override
                protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
                {
                    STRING str = (STRING) context.stack().getVariable(arg1.getName()).evaluate(
                            context);
                    int strlen = str.stringValue().length();
                    INTEGER offs = (INTEGER) context.stack().getVariable(arg2.getName()).evaluate(
                            context);
                    int realOffs = offs.intValue();
                    realOffs = realOffs > strlen ? strlen : realOffs;
                    String res = str.stringValue().substring(0, realOffs);
                    context.stack().getVariable(fn.getName()).setValue(new STRING(res));
                    return ReturnCode.SUCCESS;
                }
            });
            program.addFunction(fn);
        }

        // RIGHT$
        {
            final Parameter arg1 = new Parameter("str", TYPES.STRING_TYPE);
            final Parameter arg2 = new Parameter("offs", TYPES.INTEGER_TYPE);
            final Function fn = new Function("RIGHT$", TYPES.STRING_TYPE, arg1, arg2);
            fn.implement(new AbstractFunction(fn)
            {
                private static final long serialVersionUID = -813465557653728567L;

                @Override
                protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
                {
                    STRING str = (STRING) context.stack().getVariable(arg1.getName()).evaluate(
                            context);
                    int strlen = str.stringValue().length();
                    INTEGER offs = (INTEGER) context.stack().getVariable(arg2.getName()).evaluate(
                            context);
                    int realOffs = offs.intValue();
                    realOffs = realOffs > strlen ? strlen : realOffs;
                    String res = str.stringValue().substring(strlen - realOffs, strlen);
                    context.stack().getVariable(fn.getName()).setValue(new STRING(res));
                    return ReturnCode.SUCCESS;
                }
            });
            program.addFunction(fn);
        }

        // MID$
        {
            final Parameter arg1 = new Parameter("str", TYPES.STRING_TYPE);
            final Parameter arg2 = new Parameter("offs", TYPES.INTEGER_TYPE);
            final Function fn = new Function("MID$", TYPES.STRING_TYPE, arg1, arg2);
            fn.implement(new AbstractFunction(fn)
            {
                private static final long serialVersionUID = -813465557653728567L;

                @Override
                protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
                {
                    STRING str = (STRING) context.stack().getVariable(arg1.getName()).evaluate(
                            context);
                    int strlen = str.stringValue().length();
                    INTEGER offs = (INTEGER) context.stack().getVariable(arg2.getName()).evaluate(
                            context);
                    int realOffs = offs.intValue();
                    realOffs = realOffs > strlen ? strlen : realOffs;
                    String res = str.stringValue().substring(realOffs);
                    context.stack().getVariable(fn.getName()).setValue(new STRING(res));
                    return ReturnCode.SUCCESS;
                }
            });
            program.addFunction(fn);
        }

        // LTRIM$
        {
            final Parameter arg1 = new Parameter("str", TYPES.STRING_TYPE);
            final Function fn = new Function("LTRIM$", TYPES.STRING_TYPE, arg1);
            fn.implement(new AbstractFunction(fn)
            {
                private static final long serialVersionUID = -813465557653728567L;

                @Override
                protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
                {
                    STRING str = (STRING) context.stack().getVariable(arg1.getName()).evaluate(
                            context);
                    String s = str.stringValue();
                    String allTrim = s.trim();
                    String lTrim = s.substring(s.indexOf(allTrim));
                    context.stack().getVariable(fn.getName()).setValue(new STRING(lTrim));
                    return ReturnCode.SUCCESS;
                }
            });
            program.addFunction(fn);
        }

        // RTRIM$
        {
            final Parameter arg1 = new Parameter("str", TYPES.STRING_TYPE);
            final Function fn = new Function("RTRIM$", TYPES.STRING_TYPE, arg1);
            fn.implement(new AbstractFunction(fn)
            {
                private static final long serialVersionUID = 6941514854182895117L;

                @Override
                protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
                {
                    STRING str = (STRING) context.stack().getVariable(arg1.getName()).evaluate(
                            context);
                    String s = str.stringValue();
                    String allTrim = s.trim();
                    int trimIdx = s.indexOf(allTrim);
                    String lTrim = s.substring(0, trimIdx + allTrim.length());
                    context.stack().getVariable(fn.getName()).setValue(new STRING(lTrim));
                    return ReturnCode.SUCCESS;
                }
            });
            program.addFunction(fn);
        }

        // TAB
        {
            final Parameter arg = new Parameter("pos", TYPES.INTEGER_TYPE);
            final Function fn = new Function("TAB", TYPES.STRING_TYPE, arg);
            fn.implement(new AbstractFunction(fn)
            {
                private static final long serialVersionUID = -5474072150376118264L;

                @Override
                protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
                {
                    INTEGER pos = (INTEGER) context.stack().getVariable(arg.getName()).evaluate(
                            context);
                    StringBuilder sb = new StringBuilder();
                    int ipos = pos.intValue();
                    for (int i = 0; i < ipos; i++)
                        sb.append(' ');
                    context.stack().getVariable(fn.getName()).setValue(new STRING(sb.toString()));
                    return ReturnCode.SUCCESS;
                }
            });
            program.addFunction(fn);
        }

        // INSTRN
        {
            final Parameter arg1 = new Parameter("start", TYPES.INTEGER_TYPE);
            final Parameter arg2 = new Parameter("search", TYPES.STRING_TYPE);
            final Parameter arg3 = new Parameter("pattern", TYPES.STRING_TYPE);
            final Function fn = new Function("INSTRN", TYPES.INTEGER_TYPE, arg1, arg2, arg3);
            fn.implement(new AbstractFunction(fn)
            {
                private static final long serialVersionUID = 5377871764500923480L;

                @Override
                protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
                {
                    INTEGER start = (INTEGER) context.stack().getVariable(arg1.getName()).evaluate(
                            context);
                    STRING search = (STRING) context.stack().getVariable(arg2.getName()).evaluate(
                            context);
                    STRING pattern = (STRING) context.stack().getVariable(arg3.getName()).evaluate(
                            context);
                    context.stack().getVariable(fn.getName()).setValue(
                            new INTEGER(search.stringValue().indexOf(pattern.stringValue(),
                                    start.intValue())));
                    return ReturnCode.SUCCESS;
                }
            });
            program.addFunction(fn);
        }

        // INSTR
        {
            final Parameter arg1 = new Parameter("search", TYPES.STRING_TYPE);
            final Parameter arg2 = new Parameter("pattern", TYPES.STRING_TYPE);
            final Function fn = new Function("INSTR", TYPES.INTEGER_TYPE, arg1, arg2);
            fn.implement(new AbstractFunction(fn)
            {
                private static final long serialVersionUID = 5377871764500923480L;

                @Override
                protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
                {
                    STRING search = (STRING) context.stack().getVariable(arg1.getName()).evaluate(
                            context);
                    STRING pattern = (STRING) context.stack().getVariable(arg2.getName()).evaluate(
                            context);
                    context.stack().getVariable(fn.getName()).setValue(
                            new INTEGER(search.stringValue().indexOf(pattern.stringValue())));
                    return ReturnCode.SUCCESS;
                }
            });
            program.addFunction(fn);
        }
    }
}
