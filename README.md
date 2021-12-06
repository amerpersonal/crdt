# LWW (Last Write Win) Graph

This is the Scala implementation of a regular directed graph and LWW Graph.

### Built with

- SBT 1.2.7
- Scala 2.12.7

### Tests

Clone this repo, navigate to the root and execute `sbt test`

### Design and implementation concerns

Below is [Wikipedia definition](https://en.wikipedia.org/wiki/Conflict-free_replicated_data_type) of LWW Set:

> LWW-Element-Set is similar to 2P-Set in that it consists of an "add set" and a "remove set", with a timestamp for each element. Elements are added to an LWW-Element-Set by inserting the element into the add set, with a timestamp. Elements are removed from the LWW-Element-Set by being added to the remove set, again with a timestamp. An element is a member of the LWW-Element-Set if it is in the add set, and either not in the remove set, or in the remove set but with an earlier timestamp than the latest timestamp in the add set. Merging two replicas of the LWW-Element-Set consists of taking the union of the add sets and the union of the remove sets. When timestamps are equal, the "bias" of the LWW-Element-Set comes into play. A LWW-Element-Set can be biased towards adds or removals.

In graphs, we have two types of data: nodes and adjancencies (one possible representation of graphs). To make  graph LWW compliant, we need to make sure both nodes and edges can be added and removed multiple times on each replica, to ensure the correct state after merging with other replicas.

#### Nodes representation

Graph has a 2 structures that maintain nodes: `addNodes` and `removeNodes`.
Map is used to represent nodes with all their timestamped operations. The representation has the following format fo reach key-value pair:

- key is represented by the node
- value contains a list of add/remove operations on that particular node. `addNodes` contains add, which `removeNodes` contains remove operations.

List is used because :: (add) operation in Scala list has O(1) complexity. Naturally, new operations have higher priority that the older ones, so list will be automatically ordered in newest-first direction. This implies that we can get last add operation with `addNodes.head`, maintaining complexity O(1).

For example, node is added to graph, removed from graph and then added to the graph again. We would represent that as follows:

```
addNodes = {
    1: [(1, 789), (1, 123]
}
removeNodes = {
    1: [(1, 456)]
}
```

#### Adjacency (edges) representation

Graph has a 2 structures that maintain adjacencies: `addAdjacency`, `removeAdjacency`

Each of them is represented by map, with following structure:

- key is source node of the edge
- value is map with the following format:
    - a) key is destination node of the edge
    - b) value is a list of add/remove operation of edge between source and destination node, where each operation has a timestamp associated with it

The list is ordered in newest-first direction. This means that we can easily get last add or remove operation with simple `.head` call and complexity 0(1).

For example, is edge is added from node 1 to node 2, then removed and the added again, that would be represented as:

```
addAdjacency = {
    1: { 
        2: [(2, 789), (2, 123)]
    }
}

removeAdjacency = {
    1: {
        2: [(2, 456)]
    }
}
```

#### Performances

Described implementation is means to optimise get operations performances (check if node is in the graph, find all edges from particular node and find all paths between 2 nodes). However, it makes a merge operation slower than if we used simple lists instead of maps.

### Status and Further work

The implementation is under development and it still requires a few improvements:

- redundant and unneeded information about node from lists of timestamped operations can be removed. Timestamp is enough
- merging currently works only for 2 replicas, but can easily be extended to n replicas
- Vertex wrapper for generic type A can be removed to make code more readable and easier to understand, since A can be composite type which can contain additional information about node
- this implementation does not take into account edge weight or any other attributes about edges
