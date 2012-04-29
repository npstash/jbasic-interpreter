/*****************************************************************************
 * Copyright (C) 2006 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2006-12-09
 ****************************************************************************/

package com.nestor.shared.util;

public interface Traversable<T>
{
    /**
     * Uses a passive iterator to visit elements.
     * 
     * @param iterator
     */
    public void traverse(final PassiveIterator<T> iterator);
}
