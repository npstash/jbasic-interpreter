/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-27
 ****************************************************************************/

package com.nestor.interpret.expression;

import java.io.Serializable;
import java.util.Collection;

import com.nestor.interpret.type.DATA;
import com.nestor.interpret.type.TYPE;
import com.nestor.shared.Description;

public interface Operator<VAL extends DATA, ARG extends DATA> extends Serializable, Description
{
    public VAL operation(ARG total, ARG next) throws EvaluationException;

    public TYPE<? extends VAL> resultType(Collection<TYPE<? extends ARG>> types);
}
