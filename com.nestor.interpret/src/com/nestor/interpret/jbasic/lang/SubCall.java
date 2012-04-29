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

package com.nestor.interpret.jbasic.lang;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.expression.Value;
import com.nestor.interpret.jbasic.statement.ReturnCode;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.type.DATA;
import com.nestor.interpret.type.TYPE;

public class SubCall extends Statement
{
    private static final long serialVersionUID = 2484776462951233631L;

    private final List<Value<? extends DATA>> arguments;

    private final Sub sub;

    public SubCall(Sub function, List<Value<? extends DATA>> arguments)
    {
        this.sub = function;
        this.arguments = arguments;
    }

    public String describe()
    {
        return this.sub.describe();
    }

    @Override
    protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
    {
        List<Parameter> parameters = this.sub.getParameters();

        // first evaluate argument values, then put those values on the new frame
        LinkedList<DATA> argumentValues = new LinkedList<DATA>();
        {
            Iterator<Value<? extends DATA>> argumentIterator = this.arguments.iterator();
            while (argumentIterator.hasNext())
            {
                Value<? extends DATA> argument = argumentIterator.next();
                argumentValues.add(argument.evaluate(context));
            }
        }

        // put arguments on the stack - create frame and variables
        context.stack().new Frame(this.sub.getName(), Stack.BlockKind.CODE);

        Iterator<Parameter> parameterIterator = parameters.iterator();
        Iterator<DATA> argumentValueIterator = argumentValues.iterator();
        while (parameterIterator.hasNext())
        {
            Parameter parameter = parameterIterator.next();
            DATA argumentValue = argumentValueIterator.next();
            String parameterName = parameter.getName();
            TYPE<? extends DATA> parameterType = parameter.getType();
            Variable variable = new Variable(parameterName, parameterType);
            context.stack().addVariable(variable);
            context.stack().getVariable(variable.getName()).setValue(argumentValue);
        }

        // return code
        ReturnCode code = this.sub.execute(context);

        // destroy frame
        context.stack().popFrame();
        return code;
    }
}