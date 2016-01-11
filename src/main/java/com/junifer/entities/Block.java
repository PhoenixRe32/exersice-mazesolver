package com.junifer.entities;

public class Block
{
	private final Point coordinates;
	
	// If it's a wall or not.
	private final boolean traversable;
	
	private boolean start;
	
	private boolean end;
	
	private boolean path;
	
	public Block(int x, int y, boolean traversable)
	{
		this.coordinates = new Point(x, y);
		this.traversable = traversable;
		this.start = false;
		this.end = false;
		this.path = false;
	}
	
	public Block(Point coordinates, boolean traversable)
	{
		this.coordinates = coordinates;
		this.traversable = traversable;
	}

	/**
	 * @return the coordinates
	 */
	public Point getCoordinates()
	{
		return coordinates;
	}

	/**
	 * @return true of the block is traversable (not a wall)
	 */
	public boolean isTraversable()
	{
		return traversable;
	}

	/**
	 * @return true if the block is the start of the path
	 */
	public boolean isStart()
	{
		return start;
	}

	/**
	 * @param start set the start point of the path
	 */
	public void setStart(boolean start)
	{
		this.start = start;
	}

	/**
	 * @return true if the block is the end of the path
	 */
	public boolean isEnd()
	{
		return end;
	}

	/**
	 * @param end set the end point of the path
	 */
	public void setEnd(boolean end)
	{
		this.end = end;
	}

	/**
	 * @return the path
	 */
	public boolean isPath()
	{
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(boolean path)
	{
		this.path = path;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coordinates == null) ? 0 : coordinates.hashCode());
		result = prime * result + (traversable ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Block other = (Block) obj;
		if (coordinates == null)
		{
			if (other.coordinates != null)
				return false;
		}
		else if (!coordinates.equals(other.coordinates))
			return false;
		if (traversable != other.traversable)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Block [coordinates=" + coordinates + "]";
	}
}
