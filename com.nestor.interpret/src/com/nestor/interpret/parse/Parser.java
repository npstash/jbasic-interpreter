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

package com.nestor.interpret.parse;

import com.nestor.shared.Description;

public interface Parser<M extends Match> extends Description
{
    public M parse(Scanner scanner, ParseEnv env) throws ParseException;

    public String describe();
}
