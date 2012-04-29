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

package com.nestor.interpret.jbasic.lang;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.expression.RuntimeEnv;
import com.nestor.interpret.expression.Value;
import com.nestor.interpret.jbasic.statement.ReturnCode;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.type.DATA;
import com.nestor.interpret.type.TYPE;

public class FunctionCall extends Statement implements Value<DATA>
{
    private static final long serialVersionUID = 3623714531135282148L;

    private final List<Value<? extends DATA>> arguments;

    private final Function function;

    public FunctionCall(Function function, List<Value<? extends DATA>> arguments)
    {
        this.function = function;
        this.arguments = arguments;
    }

    public String describe()
    {
        return this.function.describe();
    }

    public DATA evaluate(RuntimeEnv env) throws EvaluationException
    {
        RuntimeContext context = (RuntimeContext) env;

        List<Parameter> parameters = this.function.getParameters();

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
        context.stack().new Frame(this.function.getName(), Stack.BlockKind.CODE);
        context.stack().addVariable(
                new Variable(this.function.getName(), this.function.getReturnType()));

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

        // ignore return code
        this.function.execute(context);

        // get result
        Variable variable = context.stack().getVariable(this.function.getName());

        // destroy frame
        context.stack().popFrame();

        // that variable value is already
        // evaluated and only stores value
        return variable.evaluate(context);
    }

    @Override
    protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
    {
        this.evaluate(context);
        return ReturnCode.SUCCESS;
    }

    public TYPE<? extends DATA> getValueType()
    {
        return this.function.getReturnType();
    }
}