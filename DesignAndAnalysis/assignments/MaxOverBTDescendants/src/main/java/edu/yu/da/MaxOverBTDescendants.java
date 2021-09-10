package edu.yu.da;

import java.util.Stack;

/** Encapsulates a set of "ancestors and descendants", modeled as a binary
 * tree, whose vertices are represented as unique non-negative integers.  Each
 * vertex_i is associated with a double-valued value_i which need not be unique
 * over the set of vertices.
 *
 * @author Avraham Leff
 */

public class MaxOverBTDescendants {

  public int[] tree;
  public double[] values;
  public int V;
  Stack<Integer> stack = new Stack<Integer>();
/*
  public static void main (String[] args){
    MaxOverBTDescendants t = new MaxOverBTDescendants(10, 0, 1);
    t.addChild(0, 1, 10);
    t.addChild(0, 2, 1);
    t.addChild(1, 3, 1);
    t.addChild(1, 4, 2);
    t.addChild(2, 5, 3);
    t.addChild(2, 6, 1);
    double[] result = t.maxOverAllBTDescendants();
    for (int i = 0; i < result.length; i++){
      System.out.print(result[i] + " ");
    }

  }

 */
  /** Constructor: initializes a binary tree to have a root and value, and
   * specifies the number of vertices that will eventually populate the tree.
   *
   * @param V the number of vertices that will eventually populate the tree,
   * must be >= 1 (because tree must at least have a root)
   * @param root must be >= 0 and < V, and specifies the root vertex of the tree.
   * @param value the value associated with the root.
   */
  public MaxOverBTDescendants(final int V, final int root, final double value) {
    this.V = V;
    if (V < 1 || root < 0 || root >= V){
      throw new IllegalArgumentException("Invalid Argument");
    }
    this.tree = new int[V+1];
    this.values = new double[V];
    for (int i = 0; i < tree.length; i++){
      tree[i] = -1;
    }
    this.tree[1] = root;
    this.values[root] = value;
  }

  /** Connects the specified child vertex to the specified parent (which must
   * already be connected to the tree).  All vertex ids must be >= 0 and < V.
   *
   * @param parent a non-negative integer that identifies a vertex already
   * connected to the tree
   * @param child a non-negative integer that identifies a vertex being added
   * for the first (and only) time to the tree.
   * @param value the value associated with the child node
   * @throw IllegalArgumentException if the invariants are violated
   */
  public void addChild(final int parent, final int child, final double value) {
    if (parent < 0 || child < 0 || child >= V){
      throw new IllegalArgumentException("Invalid Argument");
    }
    boolean found = false;
    for (int i = 0; i < tree.length; i++){
      if (tree[i] == parent){
        found = true;
        if (tree[i*2] == -1){
          tree[i*2] = child;
          values[child] = value;
        }
        else if (tree[(i*2) +1] == -1){
          tree[(i*2) + 1] = child;
          values[child] = value;
        }
        break;
      }
    }
    if(!found){
      throw new IllegalArgumentException("No Parent");
    }
  }

  /** Returns an array whose ith element is the the maximum value, over all
   * values associated with the ith element's descendants in the tree,
   * including the value associated with the ith element itself.
   *
   * @return array of doubles with the semantics specified above.
   */
  public double[] maxOverAllBTDescendants() {

    int curr = 1;
    while((curr < tree.length && tree[curr] != -1) || stack.size() > 0) {
      while (curr < tree.length && tree[curr] != -1) {
        stack.push(curr);
        stack.push(curr);
        curr = right(curr);
      }
      curr = stack.pop();
      if (curr > 1 && values[tree[curr]] > values[tree[curr / 2]]) {
        values[tree[curr / 2]] = values[tree[curr]];
      }
      curr = left(curr);
    }
    return values;
  }

  public static int left(int x){
    return (x*2) + 1;
  }

  public static int right(int x){
    return (x*2);
  }

}
