package com.junifer.program;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.junifer.entities.Maze;
import com.junifer.helpers.MazeBuilder;
import com.junifer.helpers.MazeBuilderException;
import com.junifer.helpers.MazeFunctions;

public class MazeSolver
{
	private static final Logger LOG = LoggerFactory.getLogger(MazeSolver.class);
	
	private static final String EXIT = "exit";
	
	public static void main(String[] args)
	{
		MazeBuilder mazeBuilder = new MazeBuilder();
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		String userInput;
		boolean keepRunning = true;
		
		System.out.println("Welcome to the MazeSolver!!!");
		
		do
		{
			System.out.print("Enter maze file to solve> ");
			try
			{
				userInput = console.readLine();
				
				if(EXIT.equalsIgnoreCase(userInput))
				{
					keepRunning = false;
				}
				else
				{
					processInput(mazeBuilder, userInput);
				}
			}
			catch (IOException e)
			{
				LOG.error(e.getMessage());
			}
		}
		while(keepRunning);

	}

	private static void processInput(MazeBuilder mazeBuilder, String userInput)
	{
		Maze maze = null;
		try
		{
			maze = mazeBuilder.createMaze(userInput);
		}
		catch (FileNotFoundException e)
		{
			LOG.error(e.getMessage());
		}
		catch (URISyntaxException e)
		{
			LOG.error(e.getMessage());
		}
		catch (MazeBuilderException e)
		{
			LOG.error(e.getMessage());
		}
		
		if (maze == null)
		{
			System.out.println();
			return;
		}
		
		MazeFunctions mazeFunctions = new MazeFunctions(maze);
		System.out.println("\nMaze\n====");
		mazeFunctions.printMaze();
		
		System.out.println("\nSolved Maze\n===========");
		mazeFunctions.solveMaze();
	}
}
