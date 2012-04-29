/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 4 Dec 2007
 ****************************************************************************/

package com.nestor.interpret.jbasic.profile.qbasic.services.simple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.nestor.interpret.jbasic.profile.qbasic.parse.TextServices;

public class SimpleTextServices implements TextServices
{
    public void cls()
    {
        for (int i = 0; i < 40; i++)
            System.out.println();
    }

    public void log(String s)
    {
        System.out.println(s);
    }

    public void print(String s)
    {
        System.out.println(s);
    }

    public void color(int color)
    {
        /* Empty. No effect */
    }

    public String lineInput()
    {
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String line = br.readLine();
            br.close();
            return line;
        } catch (IOException e)
        {
            return "";
        }
    }
}
