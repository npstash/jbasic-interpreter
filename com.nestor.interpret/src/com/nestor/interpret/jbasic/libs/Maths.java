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

import java.io.Serializable;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.jbasic.lang.DeclarationException;
import com.nestor.interpret.jbasic.lang.Function;
import com.nestor.interpret.jbasic.lang.ModuleBuilder;
import com.nestor.interpret.jbasic.lang.Parameter;
import com.nestor.interpret.jbasic.lang.RedeclarationException;
import com.nestor.interpret.jbasic.lang.RuntimeContext;
import com.nestor.interpret.jbasic.statement.ReturnCode;
import com.nestor.interpret.type.DOUBLE;
import com.nestor.interpret.type.TYPES;

public final class Maths extends AbstractLibrary
{
    private static abstract class FunctionComputer implements Serializable
    {
        private static final long serialVersionUID = 1L;
        final String name;

        public FunctionComputer(String name)
        {
            this.name = name;
        }

        public abstract double compute(double x);
    }

    private static Function createMathFunction(final FunctionComputer mfn)
    {
        final Parameter arg = new Parameter("x", TYPES.DOUBLE_TYPE);
        Function fn = new Function(mfn.name, TYPES.DOUBLE_TYPE, arg);
        fn.implement(new AbstractFunction(fn)
        {
            private static final long serialVersionUID = -8504940597271333707L;

            @Override
            protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
            {
                DOUBLE X = (DOUBLE) context.stack().getVariable(arg.getName()).evaluate(context);
                double x = X.doubleValue();
                double computed = mfn.compute(x);
                context.stack().getVariable(mfn.name).setValue(new DOUBLE(computed));
                return ReturnCode.SUCCESS;
            }
        });
        return fn;
    }

    private static void loadImpl(ModuleBuilder program) throws RedeclarationException
    {
        program.addFunction(createMathFunction(new FunctionComputer("ASIN")
        {
            private static final long serialVersionUID = 7493806180025521957L;

            @Override
            public double compute(double x)
            {
                return java.lang.Math.asin(x);
            }
        }));
        program.addFunction(createMathFunction(new FunctionComputer("SIN")
        {
            private static final long serialVersionUID = 7360701363791056757L;

            @Override
            public double compute(double x)
            {
                return java.lang.Math.sin(x);
            }
        }));
        program.addFunction(createMathFunction(new FunctionComputer("SINH")
        {
            private static final long serialVersionUID = -6910739665737927723L;

            @Override
            public double compute(double x)
            {
                return java.lang.Math.sinh(x);
            }
        }));
        program.addFunction(createMathFunction(new FunctionComputer("ACOS")
        {
            private static final long serialVersionUID = 4569536840565336930L;

            @Override
            public double compute(double x)
            {
                return java.lang.Math.acos(x);
            }
        }));
        program.addFunction(createMathFunction(new FunctionComputer("COS")
        {
            private static final long serialVersionUID = 7366765717927342731L;

            @Override
            public double compute(double x)
            {
                return java.lang.Math.cos(x);
            }
        }));
        program.addFunction(createMathFunction(new FunctionComputer("COSH")
        {
            private static final long serialVersionUID = -6358503642248026722L;

            @Override
            public double compute(double x)
            {
                return java.lang.Math.cosh(x);
            }
        }));
        program.addFunction(createMathFunction(new FunctionComputer("ATAN")
        {
            private static final long serialVersionUID = -6163533437531302302L;

            @Override
            public double compute(double x)
            {
                return java.lang.Math.atan(x);
            }
        }));
        program.addFunction(createMathFunction(new FunctionComputer("TAN")
        {
            private static final long serialVersionUID = -5473426421708567626L;

            @Override
            public double compute(double x)
            {
                return java.lang.Math.tan(x);
            }
        }));
        program.addFunction(createMathFunction(new FunctionComputer("TANH")
        {
            private static final long serialVersionUID = 925980524477539065L;

            @Override
            public double compute(double x)
            {
                return java.lang.Math.tanh(x);
            }
        }));
        program.addFunction(createMathFunction(new FunctionComputer("LN")
        {
            private static final long serialVersionUID = -2010253135554394904L;

            @Override
            public double compute(double x)
            {
                return java.lang.Math.log(x);
            }
        }));
        program.addFunction(createMathFunction(new FunctionComputer("ABS")
        {
            private static final long serialVersionUID = -6142008498040942403L;

            @Override
            public double compute(double x)
            {
                return java.lang.Math.abs(x);
            }
        }));
        program.addFunction(createMathFunction(new FunctionComputer("EXP")
        {
            private static final long serialVersionUID = 8593368617112451059L;

            @Override
            public double compute(double x)
            {
                return java.lang.Math.exp(x);
            }
        }));
        program.addFunction(createMathFunction(new FunctionComputer("SQRT")
        {
            private static final long serialVersionUID = 7133075133587519122L;

            @Override
            public double compute(double x)
            {
                return java.lang.Math.sqrt(x);
            }
        }));
        program.addFunction(createMathFunction(new FunctionComputer("SGN")
        {
            private static final long serialVersionUID = 5785953006537442058L;

            @Override
            public double compute(double x)
            {
                return java.lang.Math.signum(x);
            }
        }));
    }

    @Override
    public void load(ModuleBuilder program) throws DeclarationException
    {
        addConstant(program, "PI", new DOUBLE(java.lang.Math.PI));
        addConstant(program, "E", new DOUBLE(java.lang.Math.E));

        loadImpl(program);
    }
}
