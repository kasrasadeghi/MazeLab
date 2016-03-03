/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazelab;

import apcsmaze.Maze;
import apcsmaze.MazeDirection;

import java.util.*;

/**
 *
 * @author DSTIGANT
 */
public class TreasureMazeSolver
{
    public TreasureMazeSolver(){}
    public int moveCount = 0;

    class MazeLocation {
        public int r, c;
        MazeLocation(int row, int col) {
            this.r = row;
            this.c = col;
        }

        double distance(MazeLocation i2) {
            return Math.sqrt((double)(this.r * i2.r + this.c * i2.c));
        }

        int distSq(MazeLocation i2) {
            return this.r * i2.r + this.c * i2.c;
        }

        MazeLocation execute(MazeDirection md) {
            switch (md) {
                case NORTH: return new MazeLocation(r-1, c);
                case SOUTH: return new MazeLocation(r+1, c);
                case EAST: return new MazeLocation(r, c+1);
                case WEST: return new MazeLocation(r, c-1);
            }
            return this;
        }

        @Override
        public boolean equals(Object o) {

            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MazeLocation that = (MazeLocation) o;

            return r == that.r && c == that.c;
        }

        @Override
        public int hashCode() {
            int result = r;
            result = 31 * result + c;
            return result;
        }

        @Override
        public String toString() {
            return "[ " + r + ", " + c + " ]";
        }
    }

    class Node {
        Node parent;                //the node that was executed on to create this node.
        MazeDirection parentMove;   //the move that was made to come to this node from the parent
        MazeLocation l;             //the location of this node, used for equivalency
        /**
         * f = g + h;
         * g = groot * groot;
         * g = distance travelled to get to this node
         *      = parent.g + 1; because we travel 1 each time we execute
         * h = heuristic distance to the goal
         * all calculated in Node.calculate(goal);
         */
        double f, groot, h;         //the values used for comparison

        public Node() {
            this.parent = null;
            l = null;
            f = Double.MAX_VALUE;
        }

        public Node (MazeLocation l, Node p, MazeLocation goal, MazeDirection pm) {
            this(l, p, goal);
            this.parentMove = pm;
        }

        public Node(MazeLocation l, Node n, MazeLocation goal) {
            this(l, n);
            calculate(goal);
        }

        public Node(MazeLocation l, Node n) {
            this.l = l;
            this.parent = n;
        }

        /**
         * groot = the distance travelled
         * h = the squared distance to the goal
         * f = the calculated heuristic
         *
         * calculates f and h based on the goal
         * @param goal - the goal the heuristics are calculated with
         */
        public void calculate(MazeLocation goal) {
            this.h = l.r*goal.r + l.c*goal.c;
            this.f = groot * groot + h;
        }

        /**
         * Creates and returns a child of the current node.
         *
         * @param md - the direction travelled and executed upon.
         * @param goal - the goal the calculator is trying to reach.
         * @return the child node
         */
        public Node execute(MazeDirection md, MazeLocation goal) {
            Node poop = new Node(l.execute(md), this, goal, md);
            poop.groot = this.groot + 1;
            return poop;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Node node = (Node) o;

            return l.equals(node.l);
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = parent != null ? parent.hashCode() : 0;
            result = 31 * result + (l != null ? l.hashCode() : 0);
            temp = Double.doubleToLongBits(f);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(groot);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(h);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
    }
    // You probably want a function to get directions from one location in a maze to another.
    // Add that here.
    // Hint: each maze square has several edges.  These edges have type MazeEdge<Integer,Object>
    // and can be obtained from the square using the function getEdge.  getEdge consumes a 
    // MazeDirection (MazeDirection.NORTH, MazeDirection.SOUTH etc).  Each edge is either a wall
    // or not.  You can test that by calling the isWall function (true --> wall, false --> not a wall)
    // What should your get directions function return?  (Hint: the bot accepts commands to move 
    // using MazeDirection objects)

    private List<MazeDirection> generateDirections(TreasureMaze maze, TreasureBot bot, MazeLocation mazeLocation){
        Node finalNode = AStarSearch(maze, bot, mazeLocation);
        return getParentPath(finalNode);
    }

    private List<MazeDirection> findValid(int row, int col, Maze maze) {
        List<MazeDirection> valid = new ArrayList<>();
        for (MazeDirection md : MazeDirection.values())
            if (maze.getSquare(row, col).getEdge(md).isPassage())
                valid.add(md);
        return valid;
    }

    private List<MazeDirection> getParentPath(Node node) {
        List<MazeDirection> output = new ArrayList<>();
        while(node.parent != null) {
            output.add(node.parentMove);
            node = node.parent;
        }
        return output;
    }

    private List<MazeDirection> DFSsolve(Maze maze, TreasureBot bot, MazeLocation goal) {
        List<MazeLocation> visited = new ArrayList<>();
        return DFS(maze, new MazeLocation(bot.getRow(), bot.getColumn()), goal, visited);
    }

    private List<MazeDirection> DFS(Maze maze, MazeLocation ml, MazeLocation goal, List<MazeLocation> visited) {
        //if this is the goal return this node.
        //execute all the moves on this one.
        //if this isn't the goal, then keep going.

        //if we've been here, return null;
        if (visited.contains(ml))
            return null;

        //if we're at the end, return a new list;
        if (ml.equals(goal))
            return new ArrayList<>();

        //if both of the above are untrue, then we need to do stuff
        //add this to the visited, because this is a new place we haven't been before
        visited.add(ml);

        //for each direction that we can move to, make a child there
        for (MazeDirection md : findValid(ml.r, ml.c, maze)) {
            MazeLocation child = ml.execute(md);
            List<MazeDirection> solution = DFS(maze, child, goal, visited);
            if (solution != null) {
                solution.add(0, md);
                return solution;
            }
        }
        return null;
    }

    private Node AStarSearch(Maze maze, TreasureBot bot, MazeLocation goal) {
        MazeLocation start = new MazeLocation(bot.getRow(), bot.getColumn());

        LinkedList<Node> open = new LinkedList<>();
        LinkedList<Node> closed = new LinkedList<>();
        open.add(new Node(start, null, goal));

        while(!open.isEmpty()) {
            Node q = removeBestNode(open);
            if (q.l.equals(goal))
                return q;
            List<MazeDirection> moves = findValid(q.l.r, q.l.c, maze);
            check:
            for (MazeDirection move : moves) {
                Node child = q.execute(move, goal);
                //if the child's position is found in the open list
                while(open.contains(child)) {
                    //get the found node
                    Node foundNode = open.remove(open.indexOf(child));
                    if (foundNode.f < child.f) {
                        //if it has a better value than the child then add it back and stop processing the child;
                        open.add(foundNode);
                        break check;
                    }
                }
                while (closed.contains(child)) {
                    Node foundNode = closed.remove(closed.indexOf(child));
                    if (foundNode.f < child.f) {
                        closed.add(foundNode);
                        break check;
                    }
                }
                //if we haven't found this node in the open and closed lists with a better value,
                //then add it to the open list to be inspected
                open.add(child);
            }
            closed.push(q);
        }
        return null;
    }

    private Node removeBestNode(List<Node> nodes) {
        int index = 0;
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            if (node.f < nodes.get(index).f)
                index = i;
        }
        return nodes.remove(index);
    }
    
    // You might also want a function to get the bot from it's current location to another one in the maze.
    // (Hint: my solution utilized the above function to accomplish this function)
    // Add that here:
    public void executeBotMove(TreasureMaze maze, TreasureBot bot, MazeLocation goal) {
//        executeDirectionList(generateDirections(maze, bot, goal), bot);
        executeDirectionList(DFSsolve(maze, bot, goal), bot);
    }

    public void executeDirectionList(List<MazeDirection> directions, TreasureBot bot) {
        directions.forEach(bot::move);
        bot.pickUp();
    }
    
    // solve : takes a treasure maze with treasure and a treasure bot
    // The bot is guided through the maze, picking up treasures in order of value (that is lower numbered
    // treasures are picked up first)
    public void solve( TreasureMaze maze, TreasureBot bot )
    {
        // Scan the maze for all the treasures
        // In order to solve this problem, what information about each treasure do you need
        // to know?  Create a class called TreasureInfo to consolidate this information.
        // What should you use to keep track of ALL of the TreasureInfos?
        // Run through the maze, get each square and find/keep track of the treasures.
        // Hint:  The squares in the maze have type MazeSquare<Integer,Object>
        // Each square has a getData function which returns null (if the square is empty) or 
        // an Integer if there is a treasure in that square.
        class TreasureLocation {
            public int value;
            public MazeLocation ml;
            TreasureLocation(int v, MazeLocation m) {
                this.value = v;
                this.ml = m;
            }
            public int getValue() {
                return value;
            }

            @Override
            public String toString() {
                return value + ": " + ml;
            }
        }
        List<TreasureLocation> treasureMap = new ArrayList<>();
        for (int r = 0; r < maze.getNumColumns(); ++r) {
            for (int c = 0; c < maze.getNumRows(); ++c) {
                if (maze.getSquare(r, c).getData() != null)
                    treasureMap.add(new TreasureLocation(maze.getSquare(r, c).getData(), new MazeLocation(r, c)));
            }
        }
        // You need to pick up the treasures in order from least to greatest...
        // What do you need to do before you start moving the bot?
        // You might want to create a class to help you order the TreasureInfos
        // Given two treasure info objects, how do you decide which comes first?
        // Also, you may use the sort function in Collections to accomplish this part.
        Collections.sort(treasureMap, Comparator.comparingInt( TreasureLocation::getValue));
//        for (TreasureLocation tl : treasureMap) {
//            System.out.println(tl);
//        }
        // Run through your list of TreasureInfos and guide the bot to each in turn.
        // When you reach the next treasure, pick it up!
        treasureMap.forEach(tl ->  {
                executeBotMove(maze, bot, tl.ml);
        });
    }
}
