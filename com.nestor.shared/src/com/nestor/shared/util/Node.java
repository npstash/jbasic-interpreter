/*****************************************************************************
 * Copyright (C) 2006 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 27/07/2006
 ****************************************************************************/

package com.nestor.shared.util;

public interface Node<T>
{
    public enum TraverseDirection
    {
        BOTTOM_UP, TOP_DOWN, DEFAULT
    }

    public void append(Node<T> c);

    public T get();

    public void traverse(PassiveIterator<T> v, TraverseDirection td);

}
