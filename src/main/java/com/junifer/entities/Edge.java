package com.junifer.entities;

import org.jgrapht.graph.DefaultWeightedEdge;

public class Edge extends DefaultWeightedEdge
{
	private static final long serialVersionUID = -2981259194561557491L;
	
	public Block getSource()
	{
		return (Block) super.getSource();
	}
	
	public Block getTarget()
	{
		return (Block) super.getTarget();
	}
}
