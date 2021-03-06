import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class DijkstraShortestReach {
  // http://algs4.cs.princeton.edu/24pq/IndexMinPQ.java.html
  static class IndexMinPQ<Key extends Comparable<Key>> {
    private int maxN;
    private int N;
    private int[] pq;
    private int[] qp;
    private Key[] keys;

    public IndexMinPQ(int maxN) {
      this.maxN = maxN;
      keys = (Key[]) new Comparable[maxN + 1];
      pq   = new int[maxN + 1];
      qp   = new int[maxN + 1];
      for (int i = 0; i <= maxN; i++)
        qp[i] = -1;
    }

    public boolean isEmpty() {
      return N == 0;
    }

    public boolean contains(int i) {
      return qp[i] != -1;
    }

    public void insert(int i, Key key) {
      N++;
      qp[i] = N;
      pq[N] = i;
      keys[i] = key;
      swim(N);
    }

    public int delMin() {
      int min = pq[1];
      exch(1, N--);
      sink(1);
      qp[min] = -1;
      keys[min] = null;
      pq[N+1] = -1;
      return min;
    }

    public void change(int i, Key key) {
      keys[i] = key;
      swim(qp[i]);
      sink(qp[i]);
    }

    public void delete(int i) {
      int index = qp[i];
      exch(index, N--);
      swim(index);
      sink(index);
      keys[i] = null;
      qp[i] = -1;
    }

    private boolean greater(int i, int j) {
      return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
    }

    private void exch(int i, int j) {
      int swap = pq[i];
      pq[i] = pq[j];
      pq[j] = swap;
      qp[pq[i]] = i;
      qp[pq[j]] = j;
    }

    private void swim(int k) {
      while (k > 1 && greater(k/2, k)) {
        exch(k, k/2);
        k = k/2;
      }
    }

    private void sink(int k) {
      while (2*k <= N) {
        int j = 2*k;
        if (j < N && greater(j, j+1)) j++;
        if (!greater(k, j)) break;
        exch(k, j);
        k = j;
      }
    }
  }

  static class EdgeWeightedGraph {
    final int V;
    List<Edge>[] adj;

    EdgeWeightedGraph(int V) {
      this.V = V;
      adj = new LinkedList[V];
      for (int i = 0; i < V; i++)
        adj[i] = new LinkedList<Edge>();
    }

    void addEdge(Edge e) {
      adj[e.v].add(e);
      adj[e.w].add(e);
    }
  }

  static class Edge {
    int weight;
    int v;
    int w;

    Edge(int v, int w, int weight) {
      this.v = v; this.w = w; this.weight = weight;
    }

    int other(int x) {
      if (x == v) return w;
      return v;
    }
  }

  static class Dijkstra {
    int[] distTo;

    public Dijkstra(EdgeWeightedGraph g, int S) {
      distTo = new int[g.V];
      for (int i = 0; i < g.V; i++)
        distTo[i] = Integer.MAX_VALUE;
      distTo[S] = 0;

      IndexMinPQ<Integer> pq = new IndexMinPQ<>(g.V);
      pq.insert(S, 0);

      while (!pq.isEmpty()) {
        int v = pq.delMin();

        for (Edge e : g.adj[v]) {
          int w = e.other(v);
          if (distTo[w] > distTo[v] + e.weight) {
            distTo[w] = distTo[v] + e.weight;
            if (pq.contains(w)) pq.change(w, distTo[w]);
            else pq.insert(w, distTo[w]);
          }
        }
      }
    }
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    for (int c = scanner.nextInt(); c > 0; c--) {
      int V = scanner.nextInt();
      int E = scanner.nextInt();
      EdgeWeightedGraph g = new EdgeWeightedGraph(V);

      for (int i = 0; i < E; i++) {
        g.addEdge(new Edge(scanner.nextInt()-1, scanner.nextInt()-1, scanner.nextInt()));
      } 

      int S = scanner.nextInt() - 1;
      Dijkstra dijkstra = new Dijkstra(g, S);

      for (int v = 0; v < V; v++) {
        if (v == S) continue;
        if (dijkstra.distTo[v] == Integer.MAX_VALUE) System.out.print("-1 ");
        else System.out.print(dijkstra.distTo[v] + " ");
      }
      System.out.println();
    }
    scanner.close();
  }
}

