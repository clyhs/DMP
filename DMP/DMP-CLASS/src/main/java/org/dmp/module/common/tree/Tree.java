package org.dmp.module.common.tree;

import java.util.ArrayList;
import java.util.List;


public class Tree
{
	private String text = "";
	private String id = "0";
	private String cls = "file";
	private String url = "";
	private Boolean leaf = Boolean.TRUE;
	private Boolean expanded = Boolean.FALSE;
	private Boolean checked = null;
	private List<Tree> children = null;
	
	public String getText()
	{
		return text;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
	
	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public String getCls()
	{
		return cls;
	}
	
	public void setCls(String cls)
	{
		this.cls = cls;
	}
	
	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}	
	
	public Boolean getLeaf()
	{
		return leaf;
	}
	
	public void setLeaf(Boolean leaf)
	{
		this.leaf = leaf;
	}
	
	public Boolean getExpanded()
	{
		return expanded;
	}

	public void setExpanded(Boolean expanded)
	{
		this.expanded = expanded;
	}

	public Boolean getChecked()
	{
		return checked;
	}

	public void setChecked(Boolean checked)
	{
		this.checked = checked;
	}	
	
	public List<Tree> getChildren()
	{
		return children;
	}
	
	public void setChildren(List<Tree> children)
	{
		this.children = children;
	}
	
	public void addNode(Tree oTree)
	{
		if(children == null)
		{
			children = new ArrayList<Tree>();
		}
		children.add(oTree);
	}
}
