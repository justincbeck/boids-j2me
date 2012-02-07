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

import javax.microedition.lcdui.*;


class Boid
        implements BoidsConstants
{
    private int x;
    private int y;
    private int vx;
    private int vy;
    private int speed;
    private final int red;
    private final int green;
    private final int blue;
    private final BoidsCanvas canvas;

    private static final BoidRule gatherRule = new GatherRule();
    private static final BoidRule repelRule = new RepelRule();
    private static final BoidRule alignRule = new AlignRule();

    private static final int MAX_SPEED = 2 << FIXED_POINT_SHIFT;
    private static final int MAX_FLAP = 10;
    private static final int LENGTH = 15 << FIXED_POINT_SHIFT;
    private static final int EDGE_REPEL_DISTANCE = 20 << FIXED_POINT_SHIFT;
    private static final int EDGE_REPEL_FACTOR = (1 << FIXED_POINT_SHIFT) / 4;


    Boid(int x, int y,
         int vx, int vy,
         int red, int green, int blue,
         BoidsCanvas canvas)
    {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.canvas = canvas;
    }


    int getX()
    {
        return x;
    }


    int getY()
    {
        return y;
    }


    int getVx()
    {
        return vx;
    }


    int getVy()
    {
        return vy;
    }


    void tick()
    {
        gatherRule.reset(this);
        canvas.applyRule(gatherRule, this);
        repelRule.reset(this);
        canvas.applyRule(repelRule, this);
        alignRule.reset(this);
        canvas.applyRule(alignRule, this);

        vx += gatherRule.getVx() + repelRule.getVx() + alignRule.getVx();
        vy += gatherRule.getVy() + repelRule.getVy() + alignRule.getVy();

        repelFromEdges();

        speed = BoidsUtils.squareRoot(vx * vx + vy * vy);
        if (speed > MAX_SPEED)
        {
            int factor = (MAX_SPEED << FIXED_POINT_SHIFT) / speed;
            vx = (vx * factor) >> FIXED_POINT_SHIFT;
            vy = (vy * factor) >> FIXED_POINT_SHIFT;
            speed = MAX_SPEED;
        }

        x += vx;
        y += vy;
    }


    private void repelFromEdges()
    {
        if (x < EDGE_REPEL_DISTANCE)
        {
            vx += ((EDGE_REPEL_DISTANCE - x) * EDGE_REPEL_FACTOR)
                    >> FIXED_POINT_SHIFT;
        }
        else
        {
            int width = canvas.getWidth() << FIXED_POINT_SHIFT;
            if (x > width - EDGE_REPEL_DISTANCE)
            {
                vx -= ((x - (width - EDGE_REPEL_DISTANCE)) * EDGE_REPEL_FACTOR)
                        >> FIXED_POINT_SHIFT;
            }
        }

        if (y < EDGE_REPEL_DISTANCE)
        {
            vy += ((EDGE_REPEL_DISTANCE - y) * EDGE_REPEL_FACTOR)
                    >> FIXED_POINT_SHIFT;
        }
        else
        {
            int height = canvas.getHeight() << FIXED_POINT_SHIFT;
            if (y > height - EDGE_REPEL_DISTANCE)
            {
                vy -= ((y - (height - EDGE_REPEL_DISTANCE)) * EDGE_REPEL_FACTOR)
                        >> FIXED_POINT_SHIFT;
            }
        }
    }


    void draw(Graphics g)
    {
        // calculate lengths; for simplicity, don't draw
        // stationary beckproduct (how would we show their direction?)
        if (speed != 0)
        {
            int lx = vx * LENGTH / speed;
            int ly = vy * LENGTH / speed;
            int xHead = (x + (lx >> 1)) >> FIXED_POINT_SHIFT;
            int yHead = (y + (ly >> 1)) >> FIXED_POINT_SHIFT;

            int xTail = (x - (lx >> 2)) >> FIXED_POINT_SHIFT;
            int yTail = (y - (ly >> 2)) >> FIXED_POINT_SHIFT;
            int xLeft = (x - ((lx + ly) >> 1)) >> FIXED_POINT_SHIFT;
            int yLeft = (y - ((ly - lx) >> 1)) >> FIXED_POINT_SHIFT;
            int xRight = (x - ((lx - ly) >> 1)) >> FIXED_POINT_SHIFT;
            int yRight = (y - ((ly + lx) >> 1)) >> FIXED_POINT_SHIFT;
            g.setColor(red, green, blue);
            g.drawLine(xLeft, yLeft, xTail, yTail);
            g.drawLine(xRight, yRight, xTail, yTail);
            g.drawLine(xLeft, yLeft, xHead, yHead);
            g.drawLine(xRight, yRight, xHead, yHead);

        }
    }
}
