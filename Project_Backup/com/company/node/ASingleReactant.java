/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.company.node;

import com.company.analysis.*;

@SuppressWarnings("nls")
public final class ASingleReactant extends PReactant
{
    private PFactor _factor_;
    private TTMult _tMult_;
    private TTString _tString_;

    public ASingleReactant()
    {
        // Constructor
    }

    public ASingleReactant(
        @SuppressWarnings("hiding") PFactor _factor_,
        @SuppressWarnings("hiding") TTMult _tMult_,
        @SuppressWarnings("hiding") TTString _tString_)
    {
        // Constructor
        setFactor(_factor_);

        setTMult(_tMult_);

        setTString(_tString_);

    }

    @Override
    public Object clone()
    {
        return new ASingleReactant(
            cloneNode(this._factor_),
            cloneNode(this._tMult_),
            cloneNode(this._tString_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseASingleReactant(this);
    }

    public PFactor getFactor()
    {
        return this._factor_;
    }

    public void setFactor(PFactor node)
    {
        if(this._factor_ != null)
        {
            this._factor_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._factor_ = node;
    }

    public TTMult getTMult()
    {
        return this._tMult_;
    }

    public void setTMult(TTMult node)
    {
        if(this._tMult_ != null)
        {
            this._tMult_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._tMult_ = node;
    }

    public TTString getTString()
    {
        return this._tString_;
    }

    public void setTString(TTString node)
    {
        if(this._tString_ != null)
        {
            this._tString_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._tString_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._factor_)
            + toString(this._tMult_)
            + toString(this._tString_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._factor_ == child)
        {
            this._factor_ = null;
            return;
        }

        if(this._tMult_ == child)
        {
            this._tMult_ = null;
            return;
        }

        if(this._tString_ == child)
        {
            this._tString_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._factor_ == oldChild)
        {
            setFactor((PFactor) newChild);
            return;
        }

        if(this._tMult_ == oldChild)
        {
            setTMult((TTMult) newChild);
            return;
        }

        if(this._tString_ == oldChild)
        {
            setTString((TTString) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
