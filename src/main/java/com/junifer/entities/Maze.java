package com.junifer.entities;

import java.util.Map;

import org.jgrapht.WeightedGraph;

public class Maze
{
	private int width;
	
	private int height;
	
	private Point start;
	
	private Point end;
	
	private Map<Point, Block> blocks;
	
	private WeightedGraph<Block, Edge> mazeGraph;
	
	public Maze()
	{
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}

	/**
	 * @return the width
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}

	/**
	 * @return the height
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(Point start)
	{
		this.start = start;
	}

	/**
	 * @return the start
	 */
	public Point getStart()
	{
		return start;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(Point end)
	{
		this.end = end;
	}

	/**
	 * @return the end
	 */
	public Point getEnd()
	{
		return end;
	}

	/**
	 * @return the blocks
	 */
	public Map<Point, Block> getBlocks()
	{
		return blocks;
	}

	/**
	 * @param blocks the blocks to set
	 */
	public void setBlocks(Map<Point, Block> blocks)
	{
		this.blocks = blocks;
	}

	/**
	 * @return the mazeGraph
	 */
	public WeightedGraph<Block, Edge> getMazeGraph()
	{
		return mazeGraph;
	}

	/**
	 * @param mazeGraph the mazeGraph to set
	 */
	public void setMazeGraph(WeightedGraph<Block, Edge> mazeGraph)
	{
		this.mazeGraph = mazeGraph;
	}
}
