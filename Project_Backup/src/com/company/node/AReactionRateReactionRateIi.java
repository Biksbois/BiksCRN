/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.company.node;

import com.company.analysis.*;

@SuppressWarnings("nls")
public final class AReactionRateReactionRateIi extends PReactionRateIi
{
    private PReactionRateI _reactionRateI_;

    public AReactionRateReactionRateIi()
    {
        // Constructor
    }

    public AReactionRateReactionRateIi(
        @SuppressWarnings("hiding") PReactionRateI _reactionRateI_)
    {
        // Constructor
        setReactionRateI(_reactionRateI_);

    }

    @Override
    public Object clone()
    {
        return new AReactionRateReactionRateIi(
            cloneNode(this._reactionRateI_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAReactionRateReactionRateIi(this);
    }

    public PReactionRateI getReactionRateI()
    {
        return this._reactionRateI_;
    }

    public void setReactionRateI(PReactionRateI node)
    {
        if(this._reactionRateI_ != null)
        {
            this._reactionRateI_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._reactionRateI_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._reactionRateI_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._reactionRateI_ == child)
        {
            this._reactionRateI_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._reactionRateI_ == oldChild)
        {
            setReactionRateI((PReactionRateI) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
