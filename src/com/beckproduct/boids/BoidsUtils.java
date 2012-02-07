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

import java.util.*;


class BoidsUtils
{
    private static Random random = new Random();


    private BoidsUtils()
    {
        // private so no-one can create an instance of this class
    }


    static int random(int scale)
    {
        int randomInt = random.nextInt();
        // extend to long, but remove sign-extend
        long randomIntAsLong = (long) randomInt & 0xFFFFFFFFL;
        int result = (int) ((randomIntAsLong * (long) scale) >> 32);
        return result;
    }

    // This seems to produce the square root rounded down to the next
    // integer, except for numbers very near the next square, when we
    // get the square root rounded up to the next integer. That's good
    // enough for our purposes here anyway. In use in the beckproduct MIDlet,
    // it averages about 14 iterations per use.
    static int squareRoot(int square)
    {
        int guess = square;
        if (square < 0)
        {
            throw new IllegalArgumentException(
                    "Negative argument to squareRoot");
        }
        else if (square != 0)
        {
            int oldGuess;
            do
            {
                // Newton's method
                oldGuess = guess;
                guess = (oldGuess + square / oldGuess) >> 1;
            }
            while ((guess < oldGuess) && (guess > 0));
        }

        return guess;
    }
}
