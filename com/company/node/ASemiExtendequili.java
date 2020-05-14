/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.company.node;

import com.company.analysis.*;

@SuppressWarnings("nls")
public final class ASemiExtendequili extends PExtendequili
{
    private PTimestep _timestep_;

    public ASemiExtendequili()
    {
        // Constructor
    }

    public ASemiExtendequili(
        @SuppressWarnings("hiding") PTimestep _timestep_)
    {
        // Constructor
        setTimestep(_timestep_);

    }

    @Override
    public Object clone()
    {
        return new ASemiExtendequili(
            cloneNode(this._timestep_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseASemiExtendequili(this);
    }

    public PTimestep getTimestep()
    {
        return this._timestep_;
    }

    public void setTimestep(PTimestep node)
    {
        if(this._timestep_ != null)
        {
            this._timestep_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._timestep_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._timestep_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._timestep_ == child)
        {
            this._timestep_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._timestep_ == oldChild)
        {
            setTimestep((PTimestep) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
