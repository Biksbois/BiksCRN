/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.company.node;

import com.company.analysis.*;

@SuppressWarnings("nls")
public final class AAddTitrationdcl extends PTitrationdcl
{
    private TTAddmol _tAddmol_;

    public AAddTitrationdcl()
    {
        // Constructor
    }

    public AAddTitrationdcl(
        @SuppressWarnings("hiding") TTAddmol _tAddmol_)
    {
        // Constructor
        setTAddmol(_tAddmol_);

    }

    @Override
    public Object clone()
    {
        return new AAddTitrationdcl(
            cloneNode(this._tAddmol_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAAddTitrationdcl(this);
    }

    public TTAddmol getTAddmol()
    {
        return this._tAddmol_;
    }

    public void setTAddmol(TTAddmol node)
    {
        if(this._tAddmol_ != null)
        {
            this._tAddmol_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._tAddmol_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._tAddmol_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._tAddmol_ == child)
        {
            this._tAddmol_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._tAddmol_ == oldChild)
        {
            setTAddmol((TTAddmol) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
