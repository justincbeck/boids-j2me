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


class RepelRule
        implements BoidRule, BoidsConstants
{
    private int totalVx;
    private int totalVy;
    private Boid currentBoid;
    private static final int REPEL_FACTOR = (1 << FIXED_POINT_SHIFT);
    private static final int REPEL_RANGE = 10;
    private static final int REPEL_RANGE_SQUARED = REPEL_RANGE * REPEL_RANGE;


    RepelRule()
    {
    }


    public void reset(Boid boid)
    {
        totalVx = 0;
        totalVy = 0;
        currentBoid = boid;
    }


    public void applyTo(Boid boid)
    {
        int diffX = currentBoid.getX() - boid.getX();
        int diffY = currentBoid.getY() - boid.getY();
        // range squared can be too large for our fixed point, so do this
        // in normal integers
        int dx = diffX >> FIXED_POINT_SHIFT;
        int dy = diffY >> FIXED_POINT_SHIFT;
        int rangeSquared = dx * dx + dy * dy;
        if (rangeSquared < REPEL_RANGE_SQUARED)
        {
            int factor = (REPEL_FACTOR * (REPEL_RANGE_SQUARED - rangeSquared))
                    / REPEL_RANGE_SQUARED;
            totalVx += (factor * diffX) >> FIXED_POINT_SHIFT;
            totalVy += (factor * diffY) >> FIXED_POINT_SHIFT;
        }
    }


    public int getVx()
    {
        return totalVx;
    }


    public int getVy()
    {
        return totalVy;
    }
}
