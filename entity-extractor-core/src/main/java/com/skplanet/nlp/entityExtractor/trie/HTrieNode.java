package com.skplanet.nlp.entityExtractor.trie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * HashMap 기반 Trie Node
 *
 *
 */
public class HTrieNode {	
	private static Logger logger = Logger.getLogger(HTrieNode.class.getName());

    // key
	protected char keyword									= 0;
	

    // value
	protected ArrayList<String> ovalues						= null;

    // nodes
	protected Map<Object, HTrieNode> nodes			= null;
	

	/**
	 * 주제어 트라이 노드 초기화 함수
	 */
	public void initObject()
	{
		nodes										= new HashMap<Object, HTrieNode>(1);
	}
	
	/**
	 * 주제어 트라이 노드 초기화 함수
	 * @param keyword
	 */
	public HTrieNode(char keyword)
	{
		this.keyword								= keyword;
	}
	
	/**
	 * 주제어 트라이 노드 초기화 함수
	 * @param keyword 
	 * @param value 
	 */
	public HTrieNode( char keyword, String value )
	{
		this(keyword);
				
		ovalues										= new ArrayList<String>();
		this.ovalues.add(value);
	}
	
	/**
	 * leaf node 체크
	 * @return
	 */
	public boolean isLeafObject()
	{
		if( ovalues == null ) 
		{
			return false;
		}
				
		return ovalues.size() > 0 ? true : false;

	}
	
	/**
	 * ArrayList value leaf node 체크 
	 * @param value
	 * @return
	 */
	public boolean isLeafObject(String value)
	{
		if( ovalues == null) 
		{
			ovalues = new ArrayList<String>();
			return false;
		}
		
		// array 리스트를 돌며 중복키에 대한 value 확인
		for(int i=0; i<ovalues.size(); i++) 
		{
			String odata = ovalues.get(i);
	
			if( odata.equals(value) )
			{
				return true;
			}
		}
		
		return false;
	}
	

	/**
	 * 트라이 삽입 ( key와 value를 같이 넣음 )
	 * @param key
	 * @param data
	 * @return
	 */
	public HTrieNode put(char key, String data)
	{
		HTrieNode node								= null;
		
		// root가 null일경우 초기화함
		if(nodes == null) 
		{
			initObject();
		}

		// key를 이용하여 node를 가져온다
		node										= nodes.get(key);
				
		// node가 null이 아닐경우 leaf node 체크
		if(node != null)
		{			
			if( data != null ) 
			{
				if(!node.isLeafObject(data)) 
				{		
					node.setValue(data);
				}
			}
			return node;
		}
		
		if( data == null) 
		{
			node = new HTrieNode(key);
		} 
		else 
		{
			node = new HTrieNode(key, data);
		}
				
		nodes.put(key, node);
		
		return node;
	}
	
	/**
	 * 트라이의 다음 노드를 가져온다
	 * @param key
	 * @return
	 */
	public HTrieNode getNode(char key)
	{
		if(nodes == null)
		{
			return null;
		}
		
		return nodes.get(key);
	}

	/**
	 * 트라이 value list를 가져온다
	 * @return
	 */
	public ArrayList<String> getValuesObject()
	{		
		return ovalues;
	}
	
	/**
	 * 단일 값 셋팅
	 * @param value
	 */
	public void setValue(String value)
	{
		this.ovalues.add(value);
	}
}
