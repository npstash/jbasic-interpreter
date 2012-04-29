/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-09
 ****************************************************************************/

package com.nestor.interpret.expression;

import com.nestor.interpret.parse.Match;
import com.nestor.interpret.type.DATA;
import com.nestor.interpret.type.TYPE;

public interface Value<VAL extends DATA> extends Match
{
    public VAL evaluate(RuntimeEnv env) throws EvaluationException;

    public TYPE<? extends VAL> getValueType();
}
