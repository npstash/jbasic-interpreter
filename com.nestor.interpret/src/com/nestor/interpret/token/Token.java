/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-08
 ****************************************************************************/

package com.nestor.interpret.token;

import com.nestor.shared.Description;

public interface Token extends Description
{
    public String match(String input, int offset);

    public boolean isSame(Token other);

    public boolean isShadowing(Token other);
}
