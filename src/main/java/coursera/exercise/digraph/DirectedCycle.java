package coursera.exercise.digraph;

import com.coursera.algorithms.algs4.Digraph;
import com.coursera.algorithms.algs4.Stack;
import com.coursera.algorithms.stdlib.In;
import com.coursera.algorithms.stdlib.StdOut;

/*************************************************************************
 *  Compilation:  javac DirectedCycle.java
 *  Execution:    java DirectedCycle < input.txt
 *  Dependencies: Digraph.java Stack.java com.coursera.algorithms.stdlib.StdOut.java com.coursera.algorithms.stdlib.In.java
 *  Data files:   http://algs4.cs.princeton.edu/42directed/tinyDG.txt
 *                http://algs4.cs.princeton.edu/42directed/tinyDAG.txt
 *
 *  Finds a directed cycle in a digraph.
 *  Runs in O(E + V) time.
 *
 *  % java DirectedCycle tinyDG.txt 
 *  Cycle: 3 5 4 3 
 *
 *  %  java DirectedCycle tinyDAG.txt 
 *  No cycle
 *
 *************************************************************************/

class DirectedCycle {
    private boolean[] marked;        // marked[v] = has vertex v been marked?
    private int[] edgeTo;            // edgeTo[v] = previous vertex on path to v
    private boolean[] onStack;       // onStack[v] = is vertex on the stack?
    private Stack<Integer> cycle;    // directed cycle (or null if no such cycle)

    public DirectedCycle(ExactDigraph G) {
        marked  = new boolean[G.V()];
        onStack = new boolean[G.V()];
        edgeTo  = new int[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marked[v]) dfs(G, v);

        // check that ExactDigraph has a cycle
        assert check(G);
    }


    // check that algorithm computes either the topological order or finds a directed cycle
    private void dfs(ExactDigraph G, int v) {
        onStack[v] = true;
        marked[v] = true;
        for (int w : G.adj(v)) {

            // short circuit if directed cycle found
            if (cycle != null) return;

            //found new vertex, so recur
            else if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w);
            }

            // trace back directed cycle
            else if (onStack[w]) {
                cycle = new Stack<Integer>();
                for (int x = v; x != w; x = edgeTo[x]) {
                    cycle.push(x);
                }
                cycle.push(w);
                cycle.push(v);
            }
        }

        onStack[v] = false;
    }

    public boolean hasCycle()        { return cycle != null;   }
    public Iterable<Integer> cycle() { return cycle;           }


    // certify that ExactDigraph is either acyclic or has a directed cycle
    private boolean check(ExactDigraph G) {

        if (hasCycle()) {
            // verify cycle
            int first = -1, last = -1;
            for (int v : cycle()) {
                if (first == -1) first = v;
                last = v;
            }
            if (first != last) {
                System.err.printf("cycle begins with %d and ends with %d\n", first, last);
                return false;
            }
        }


        return true;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        ExactDigraph G = new ExactDigraph(in);

        DirectedCycle finder = new DirectedCycle(G);
        if (finder.hasCycle()) {
            StdOut.print("Cycle: ");
            for (int v : finder.cycle()) {
                StdOut.print(v + " ");
            }
            StdOut.println();
        }

        else {
            StdOut.println("No cycle");
        }
    }

}
