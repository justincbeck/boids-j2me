// Copyright 2002 Nokia Corporation. 
// 
// THIS SOURCE CODE IS PROVIDED 'AS IS', WITH NO WARRANTIES WHATSOEVER, 
// EXPRESS OR IMPLIED, INCLUDING ANY WARRANTY OF MERCHANTABILITY, FITNESS 
// FOR ANY PARTICULAR PURPOSE, OR ARISING FROM A COURSE OF DEALING, USAGE 
// OR TRADE PRACTICE, RELATING TO THE SOURCE CODE OR ANY WARRANTY OTHERWISE 
// ARISING OUT OF ANY PROPOSAL, SPECIFICATION, OR SAMPLE AND WITH NO 
// OBLIGATION OF NOKIA TO PROVIDE THE LICENSEE WITH ANY MAINTENANCE OR 
// SUPPORT. FURTHERMORE, NOKIA MAKES NO WARRANTY THAT EXERCISE OF THE 
// RIGHTS GRANTED HEREUNDER DOES NOT INFRINGE OR MAY NOT CAUSE INFRINGEMENT 
// OF ANY PATENT OR OTHER INTELLECTUAL PROPERTY RIGHTS OWNED OR CONTROLLED 
// BY THIRD PARTIES 
// 
// Furthermore, information provided in this source code is preliminary, 
// and may be changed substantially prior to final release. Nokia Corporation 
// retains the right to make changes to this source code at 
// any time, without notice. This source code is provided for informational 
// purposes only. 
// 
// Nokia and Nokia Connecting People are registered trademarks of Nokia
// Corporation.
// Java and all Java-based marks are trademarks or registered trademarks of
// Sun Microsystems, Inc.
// Other product and company names mentioned herein may be trademarks or
// trade names of their respective owners.
// 
// A non-exclusive, non-transferable, worldwide, limited license is hereby 
// granted to the Licensee to download, print, reproduce and modify the 
// source code. The licensee has the right to market, sell, distribute and 
// make available the source code in original or modified form only when 
// incorporated into the programs developed by the Licensee. No other 
// license, express or implied, by estoppel or otherwise, to any other 
// intellectual property rights is granted herein.


package com.beckproduct.boids;


class GatherRule
        implements BoidRule, BoidsConstants
{
    private int totalX;
    private int totalY;
    private int count;
    private Boid currentBoid;
    private static final int ATTRACTION_FACTOR = (1 << FIXED_POINT_SHIFT) / 40;


    GatherRule()
    {
    }


    public void reset(Boid boid)
    {
        totalX = 0;
        totalY = 0;
        count = 0;
        currentBoid = boid;
    }


    public void applyTo(Boid boid)
    {
        totalX += boid.getX();
        totalY += boid.getY();
        count++;
    }


    public int getVx()
    {
        int vx = 0;
        if (count > 0)
        {
            vx = ((totalX / count - currentBoid.getX()) * ATTRACTION_FACTOR)
                    >> FIXED_POINT_SHIFT;
        }
        return vx;
    }


    public int getVy()
    {
        int vy = 0;
        if (count > 0)
        {
            vy = ((totalY / count - currentBoid.getY()) * ATTRACTION_FACTOR)
                    >> FIXED_POINT_SHIFT;
        }
        return vy;
    }
}
