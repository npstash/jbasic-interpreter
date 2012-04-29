/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-05-21
 ****************************************************************************/

package com.nestor.interpret.type;

import java.io.Serializable;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.expression.RuntimeEnv;
import com.nestor.shared.Description;

public interface Qualifier extends Serializable, Description
{
    public void set(RuntimeEnv env, DATA unqualified, DATA data) throws EvaluationException;

    public TYPE<? extends DATA> getQualifiedType();

    public DATA get(RuntimeEnv env, DATA unqualified) throws EvaluationException;
}