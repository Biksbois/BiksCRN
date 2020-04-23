/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.company.node;

import java.util.*;
import com.company.analysis.*;

@SuppressWarnings("nls")
public final class AInitializedclInit extends PInit
{
    private TTInitializedcl _tInitializedcl_;
    private TTLTurborg _tLTurborg_;
    private final LinkedList<PInitializebody> _initializebody_ = new LinkedList<PInitializebody>();
    private TTRTurborg _tRTurborg_;

    public AInitializedclInit()
    {
        // Constructor
    }

    public AInitializedclInit(
        @SuppressWarnings("hiding") TTInitializedcl _tInitializedcl_,
        @SuppressWarnings("hiding") TTLTurborg _tLTurborg_,
        @SuppressWarnings("hiding") List<?> _initializebody_,
        @SuppressWarnings("hiding") TTRTurborg _tRTurborg_)
    {
        // Constructor
        setTInitializedcl(_tInitializedcl_);

        setTLTurborg(_tLTurborg_);

        setInitializebody(_initializebody_);

        setTRTurborg(_tRTurborg_);

    }

    @Override
    public Object clone()
    {
        return new AInitializedclInit(
            cloneNode(this._tInitializedcl_),
            cloneNode(this._tLTurborg_),
            cloneList(this._initializebody_),
            cloneNode(this._tRTurborg_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAInitializedclInit(this);
    }

    public TTInitializedcl getTInitializedcl()
    {
        return this._tInitializedcl_;
    }

    public void setTInitializedcl(TTInitializedcl node)
    {
        if(this._tInitializedcl_ != null)
        {
            this._tInitializedcl_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._tInitializedcl_ = node;
    }

    public TTLTurborg getTLTurborg()
    {
        return this._tLTurborg_;
    }

    public void setTLTurborg(TTLTurborg node)
    {
        if(this._tLTurborg_ != null)
        {
            this._tLTurborg_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._tLTurborg_ = node;
    }

    public LinkedList<PInitializebody> getInitializebody()
    {
        return this._initializebody_;
    }

    public void setInitializebody(List<?> list)
    {
        for(PInitializebody e : this._initializebody_)
        {
            e.parent(null);
        }
        this._initializebody_.clear();

        for(Object obj_e : list)
        {
            PInitializebody e = (PInitializebody) obj_e;
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
            this._initializebody_.add(e);
        }
    }

    public TTRTurborg getTRTurborg()
    {
        return this._tRTurborg_;
    }

    public void setTRTurborg(TTRTurborg node)
    {
        if(this._tRTurborg_ != null)
        {
            this._tRTurborg_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._tRTurborg_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._tInitializedcl_)
            + toString(this._tLTurborg_)
            + toString(this._initializebody_)
            + toString(this._tRTurborg_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._tInitializedcl_ == child)
        {
            this._tInitializedcl_ = null;
            return;
        }

        if(this._tLTurborg_ == child)
        {
            this._tLTurborg_ = null;
            return;
        }

        if(this._initializebody_.remove(child))
        {
            return;
        }

        if(this._tRTurborg_ == child)
        {
            this._tRTurborg_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._tInitializedcl_ == oldChild)
        {
            setTInitializedcl((TTInitializedcl) newChild);
            return;
        }

        if(this._tLTurborg_ == oldChild)
        {
            setTLTurborg((TTLTurborg) newChild);
            return;
        }

        for(ListIterator<PInitializebody> i = this._initializebody_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((PInitializebody) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        if(this._tRTurborg_ == oldChild)
        {
            setTRTurborg((TTRTurborg) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
