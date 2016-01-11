package com.junifer.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.junifer.entities.Block;
import com.junifer.entities.Edge;
import com.junifer.entities.Maze;
import com.junifer.entities.Point;

public class MazeBuilder
{
	private static final Logger LOG = LoggerFactory.getLogger(MazeBuilder.class);

	private static final Pattern SEPARATOR = Pattern.compile(" ");

	private static final String WALL = "1";

	private static final String SPACE = "0";

	public Maze createMaze(final String mazeSourceFile)
			throws FileNotFoundException, URISyntaxException, MazeBuilderException
	{
		File mazeFile = loadMazeFile(mazeSourceFile);

		BufferedReader br = new BufferedReader(new FileReader(mazeFile));

		Maze maze = new Maze();

		String line;
		try
		{
			// Read maze dimensions
			line = br.readLine();
			Point dimensions = readPoint(line);
			maze.setHeight(dimensions.getY());
			maze.setWidth(dimensions.getX());

			// Read maze start point
			line = br.readLine();
			Point startCoordinates = readPoint(line);
			validatePoint(startCoordinates, maze.getHeight(), maze.getWidth());
			maze.setStart(startCoordinates);

			// Read maze end point
			line = br.readLine();
			Point endCoordinates = readPoint(line);
			validatePoint(endCoordinates, maze.getHeight(), maze.getWidth());
			maze.setEnd(endCoordinates);

			// Read maze description
			Map<Point, Block> blocks = new LinkedHashMap<Point, Block>();
			int height = 0;
			while ((line = br.readLine()) != null)
			{
				if (height == maze.getHeight())
				{
					throw new MazeBuilderException(
							"Maze description exceeded expected height!");
				}

				processLine(blocks, line, height, maze.getWidth());
				++height;
			}
			if (height < maze.getHeight())
			{
				throw new MazeBuilderException(
						"Maze description is missing expected rows!");
			}

			// Sanity check
			if (blocks.size() != maze.getHeight() * maze.getWidth())
			{
				throw new MazeBuilderException("The maze is missing blocks.");
			}

			// Set the start and end point blocks
			Block startBlock = blocks.get(maze.getStart());
			Block endBlock = blocks.get(maze.getEnd());
			if (startBlock.isTraversable() && endBlock.isTraversable())
			{
				startBlock.setStart(true);
				endBlock.setEnd(true);
			}
			else
			{
				throw new MazeBuilderException(
						"The start and end point of the maze should be traversable; not walls!");
			}

			maze.setBlocks(blocks);
		}
		catch (IOException e)
		{
			throw new MazeBuilderException(e.getMessage());
		}
		finally
		{
			try
			{
				if (br != null)
				{
					br.close();
				}
			}
			catch (IOException e)
			{
				LOG.warn(e.getMessage());
			}
		}

		// Create graph.
		maze.setMazeGraph(createMazeGraph(maze.getBlocks()));

		return maze;
	}

	/*
	 * Reads a line that defines coordinates.
	 */
	private Point readPoint(String line) throws MazeBuilderException
	{
		if (line == null || line.isEmpty())
		{
			throw new MazeBuilderException("The line is empty.");
		}

		String[] coordinates = SEPARATOR.split(line, 3);
		if (coordinates.length != 2)
		{
			throw new MazeBuilderException(
					"The line must be in the  2D format: X Y. (i.e. 20 40)");
		}

		int x;
		try
		{
			x = Integer.valueOf(coordinates[0]);
		}
		catch (NumberFormatException e)
		{
			throw new MazeBuilderException(
					"The x value is invalid. [" + coordinates[0] + "]");
		}

		if (x < 1)
		{
			throw new MazeBuilderException(
					"The x value is invalid. [" + coordinates[0] + "]");
		}

		int y;
		try
		{
			y = Integer.valueOf(coordinates[1]);
		}
		catch (NumberFormatException e)
		{
			throw new MazeBuilderException(
					"The y value is invalid. [" + coordinates[1] + "]");
		}

		if (y < 1)
		{
			throw new MazeBuilderException(
					"The y value is invalid. [" + coordinates[1] + "]");
		}

		return new Point(x, y);
	}

	/*
	 * Validates point. Makes sure it's in the expected range.
	 */
	private void validatePoint(Point coordinates, int height, int width)
			throws MazeBuilderException
	{
		if (coordinates.getX() >= width || coordinates.getX() < 0
				|| coordinates.getY() >= height || coordinates.getY() < 0)
		{
			throw new MazeBuilderException("Point " + coordinates.toString() + " is "
					+ "out of range. Range is [0,0] to " + "[" + (width - 1) + ","
					+ (height - 1) + "]");
		}
	}

	/*
	 * Finds the resource with the maze information and returns the file to it.
	 */
	private File loadMazeFile(final String mazeSourceFile)
			throws FileNotFoundException, URISyntaxException
	{
		ClassLoader classLoader = getClass().getClassLoader();
		URL sourceFileURL = classLoader.getResource(mazeSourceFile);
		if (sourceFileURL == null)
		{
			throw new FileNotFoundException("File [" + mazeSourceFile + "] not found");
		}

		return new File(sourceFileURL.toURI());
	}

	/*
	 * Process a line from the maze description and processes it. For each symbol it
	 * creates the block it represents and adds it to a map of the maze blocks keyed by
	 * the blocks' coordinates.
	 */
	private void processLine(Map<Point, Block> blocks, String line, int height, int width)
			throws MazeBuilderException
	{
		String[] mazeSegments = SEPARATOR.split(line, width + 1);
		if (mazeSegments.length != width)
		{
			throw new MazeBuilderException(
					"Actual blocks in line No" + height + " [" + mazeSegments.length
							+ "] is different than expected [" + width + "]");
		}

		for (int i = 0; i < width; ++i)
		{
			Point coordinates = new Point(i, height);

			if (WALL.equals(mazeSegments[i]))
			{
				blocks.put(coordinates, new Block(coordinates, false));
			}
			else if (SPACE.equals(mazeSegments[i]))
			{
				blocks.put(coordinates, new Block(coordinates, true));
			}
			else
			{
				throw new MazeBuilderException("Invalid maze description symbol ["
						+ mazeSegments[i] + "]. " + "Wall is '1' and space is '0'");
			}
		}
	}

	/*
	 * Creates a graph for the maze based on the map of its blocks.
	 */
	private WeightedGraph<Block, Edge> createMazeGraph(Map<Point, Block> blocks)
	{
		WeightedGraph<Block, Edge> mazeGraph = new DefaultDirectedWeightedGraph<Block, Edge>(
				Edge.class);

		for (Block block : blocks.values())
		{
			if (block.isTraversable())
			{
				// Add it to the graph if it's not a wall.
				mazeGraph.addVertex(block);

				// Look around
				Point xy = block.getCoordinates();

				// Up
				Point upP = new Point(xy.getX(), xy.getY() - 1);
				Block upB = blocks.get(upP);
				if (upB != null)
				{
					if (upB.isTraversable())
					{
						mazeGraph.addEdge(block, upB, new Edge());
						mazeGraph.addEdge(upB, block, new Edge());
					}
				}

				// Previous
				Point prP = new Point(xy.getX() - 1, xy.getY());
				Block prB = blocks.get(prP);
				if (prB != null)
				{
					if (prB.isTraversable())
					{
						mazeGraph.addEdge(block, prB, new Edge());
						mazeGraph.addEdge(prB, block, new Edge());
					}
				}
			}
		}

		return mazeGraph;
	}

}
