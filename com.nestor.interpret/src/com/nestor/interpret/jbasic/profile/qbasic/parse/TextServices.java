/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-06-11
 ****************************************************************************/

package com.nestor.interpret.jbasic.profile.qbasic.parse;

public interface TextServices
{
    public void print(String s);

    public void cls();

    public void color(int color);

    public String lineInput();
}
