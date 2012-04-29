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

import java.util.HashMap;
import java.util.Map;

public class NetNode<T> implements Node<T>
{
    private T content;

    private Map<T, Node<T>> subnodes = null;

    public NetNode(T content)
    {
        super();
        this.content = content;
    }

    public void append(Node<T> subNode)
    {
        if (this.subnodes == null)
        {
            this.subnodes = new HashMap<T, Node<T>>();
        }
        this.subnodes.put(subNode.get(), subNode);
    }

    public T get()
    {
        return this.content;
    }

    public void traverse(PassiveIterator<T> v, TraverseDirection td)
    {
        TraverseDirection traverseSubDir = td;
        if (traverseSubDir == TraverseDirection.DEFAULT)
        {
            traverseSubDir = TraverseDirection.TOP_DOWN;
        }

        if (traverseSubDir == TraverseDirection.TOP_DOWN)
        {
            v.next(this.content);
        }

        if (this.subnodes != null)
        {
            for (Node<T> subnode : this.subnodes.values())
            {
                subnode.traverse(v, traverseSubDir);
            }
        }
        if (traverseSubDir != TraverseDirection.TOP_DOWN)
        {
            v.next(this.content);
        }
    }

}
