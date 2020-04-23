/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.company.node;

import com.company.analysis.*;

@SuppressWarnings("nls")
public final class AFuncinitInitializebody extends PInitializebody
{
    private PFunc _func_;

    public AFuncinitInitializebody()
    {
        // Constructor
    }

    public AFuncinitInitializebody(
        @SuppressWarnings("hiding") PFunc _func_)
    {
        // Constructor
        setFunc(_func_);

    }

    @Override
    public Object clone()
    {
        return new AFuncinitInitializebody(
            cloneNode(this._func_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFuncinitInitializebody(this);
    }

    public PFunc getFunc()
    {
        return this._func_;
    }

    public void setFunc(PFunc node)
    {
        if(this._func_ != null)
        {
            this._func_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._func_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._func_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._func_ == child)
        {
            this._func_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._func_ == oldChild)
        {
            setFunc((PFunc) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
