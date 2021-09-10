package edu.yu.da;

/** Skeleton file for the MaxRectangle assignment.
 *
 * @author Avraham Leff
 */

public class MaxRectangle {
/*
	public static void main (String[] args){
		int[] array = {5, 5, 5,5};
		Answer answer = get(array);
		System.out.println("Left: " + answer.left + "\nRight: " + answer.right + "\nHeight: " + answer.height + "\nArea: " + answer.max);
		Answer bruteForce = bruteForce(array);
		System.out.println("\nBrute Force:");
		System.out.println("Left: " + bruteForce.left + "\nRight: " + bruteForce.right + "\nHeight: " + bruteForce.height + "\nArea: " + bruteForce.max);
	}

 */

    public static class Answer {
	final int max;
	final int left;
	final int right;
	final int height;

	/** Constructor: do not change signature, or implementation.
	 */
	public Answer(final int max, final int left, final int right,
		      final int height)
	{
	    assert max >= 0: "max must be non-negative: "+max;
	    assert left >= 0: "left must be non-negative: "+left;
	    assert right >= 0: "right must be non-negative: "+right;
	    assert height >= 0: "height must be non-negative: "+height;

	    this.max = max;
	    this.left = left;
	    this.right = right;
	    this.height = height;
	}

	// you may add as many methods as you want
    }

    /** Returns the area of the biggest rectangle that can be constructed from
     * the "heights" representation.
     *
     * @param heights an array of integers which implicitly specify one or more
     * rectangles per the assignment's requirements doc.
     * @return an Answer "struct" containing the area of the biggest
     * rectangle that can be constructed, the left and right
     * x-coordinates of the selected rectangle, and the height of that
     * rectangle.
     * @throws IllegalArgumentException if null array or fewer than 2 elements
     */
    public static Answer get(final int[] heights) {

    	if (heights == null){
    		throw new IllegalArgumentException("Null array");
		}
    	if (heights.length < 2){
			throw new IllegalArgumentException("Less than 2 elements");
		}

    	int start = 0;
    	int end = heights.length - 1;
    	int maxArea = Math.min(heights[start], heights[end])*(end-start);
    	int maxLC = start;
    	int maxRC = end;

    	if (heights[start] > heights[end]){
    		end--;
		} else {
    		start++;
		}

    	while (start != end){
			if (Math.min(heights[start], heights[end])*(end-start) > maxArea){
				maxArea = Math.min(heights[start], heights[end])*(end-start);
				maxLC = start;
				maxRC = end;
			}
			if (heights[start] > heights[end]){
				end--;
			} else {
				start++;
			}
		}

		return new Answer(maxArea, maxLC, maxRC, Math.min(heights[maxLC], heights[maxRC]));
    }

	public static Answer bruteForce(final int[] heights) {
    	int max = 0;
		int left = 0;
		int right = 0;
    	for (int i = 0; i < heights.length; i++){
    		for (int j = 0; j < heights.length; j++){
    			if (i != j){
    				if (Math.min(heights[i], heights[j])*(Math.abs(i-j)) > max){
    					max = Math.min(heights[i], heights[j])*(Math.abs(i-j));
    					left = i;
    					right = j;
					}
				}
			}
		}
		return new Answer(max, right, left, Math.min(heights[right], heights[left]));
	}

    // add as many methods as you want
}
