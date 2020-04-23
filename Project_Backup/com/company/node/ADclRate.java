/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.company.node;

import com.company.analysis.*;

@SuppressWarnings("nls")
public final class ADclRate extends PRate
{
    private TTRatedcl _tRatedcl_;
    private PRates _rates_;

    public ADclRate()
    {
        // Constructor
    }

    public ADclRate(
        @SuppressWarnings("hiding") TTRatedcl _tRatedcl_,
        @SuppressWarnings("hiding") PRates _rates_)
    {
        // Constructor
        setTRatedcl(_tRatedcl_);

        setRates(_rates_);

    }

    @Override
    public Object clone()
    {
        return new ADclRate(
            cloneNode(this._tRatedcl_),
            cloneNode(this._rates_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseADclRate(this);
    }

    public TTRatedcl getTRatedcl()
    {
        return this._tRatedcl_;
    }

    public void setTRatedcl(TTRatedcl node)
    {
        if(this._tRatedcl_ != null)
        {
            this._tRatedcl_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._tRatedcl_ = node;
    }

    public PRates getRates()
    {
        return this._rates_;
    }

    public void setRates(PRates node)
    {
        if(this._rates_ != null)
        {
            this._rates_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._rates_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._tRatedcl_)
            + toString(this._rates_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._tRatedcl_ == child)
        {
            this._tRatedcl_ = null;
            return;
        }

        if(this._rates_ == child)
        {
            this._rates_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._tRatedcl_ == oldChild)
        {
            setTRatedcl((TTRatedcl) newChild);
            return;
        }

        if(this._rates_ == oldChild)
        {
            setRates((PRates) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
