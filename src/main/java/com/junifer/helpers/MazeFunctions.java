package com.junifer.helpers;

import java.util.Map;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.DijkstraShortestPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.junifer.entities.Block;
import com.junifer.entities.Edge;
import com.junifer.entities.Maze;
import com.junifer.entities.Point;

public class MazeFunctions
{
	private static final Logger LOG = LoggerFactory.getLogger(MazeFunctions.class);
	
	private final Maze maze;
	
	public MazeFunctions(Maze maze)
	{
		this.maze = maze;
	}
	
	public void solveMaze()
	{
		// Get origin and end
		Block start = maze.getBlocks().get(maze.getStart());
		Block end = maze.getBlocks().get(maze.getEnd());
		
		// Calculate path
		DijkstraShortestPath<Block, Edge> dsp = 
				new DijkstraShortestPath<Block, Edge>(maze.getMazeGraph(), start, end);
		
		// Get path, list of blocks
		GraphPath<Block, Edge> path = dsp.getPath();		
		
		for(Edge edge : path.getEdgeList())
		{
			// edge.getSource().setPath(true);
			edge.getTarget().setPath(true);
		}
		
		printMaze();		
	}
	
	public void printMaze()
	{
		Map<Point, Block> blocks = maze.getBlocks();
		for (int y = 0; y < maze.getHeight(); ++y)
		{
			for (int x = 0; x < maze.getWidth(); ++x)
			{
				Block block = blocks.get(new Point(x, y));
				if (!block.isTraversable())
				{
					System.out.print("#");
				}
				else
				{
					if (block.isStart())
					{
						System.out.print("S");
					}
					else if (block.isEnd())
					{
						System.out.print("E");
					}
					else if (block.isPath())
					{
						System.out.print("X");
					}
					else
					{
						System.out.print(" ");
					}
				}
				
			}
			System.out.println();
		}
	}
}
