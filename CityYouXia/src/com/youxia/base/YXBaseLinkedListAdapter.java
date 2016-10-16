
package com.youxia.base;

import java.util.LinkedList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * KEBaseAdapter 适合用于下拉刷新、上拉加载 ListView,数据用链表实现，
 * 可以在下拉刷新时在链表头添加数据，在上拉加载时在链表尾部添加
 * @author nightrain
 * @date 2015-08-04
 */
public class YXBaseLinkedListAdapter extends BaseAdapter
{
	LinkedList<Object> linkedList = new LinkedList<Object>();	

	public YXBaseLinkedListAdapter()
	{
		
	}
	
	public YXBaseLinkedListAdapter(LinkedList<Object> list)
	{
		setList(list);
	}
	
	/**
	* 在链表指定位置插入一个对象
	*
	* @return
	*/
	public void addObject(int index, Object object)
	{
	    if ((index >= 0) && (index <= this.linkedList.size()) && (object != null))
	        this.linkedList.add(index, object);
	}

	/**
	* 在链表指定位置插入对象链表
	*
	* @return
	*/
	public void addList(int index,LinkedList<Object> list)
	{
		if ((index >= 0) && (index <= this.linkedList.size()) && list != null && (list.size() > 0))
	        this.linkedList.addAll(index, list);
	}
	
	/**
	* 在链表尾部添加一个对象
	*
	* @return
	*/
	public void addObject(Object object)
	{
		if (object != null) this.linkedList.add(object);
	}
	
	/**
	* 重置链表数据
	*
	* @return
	*/
	public void setList(LinkedList<Object> list)
	{
		this.linkedList.clear();
		
		this.linkedList.addAll(list);
	}
	
	/**
	* 在链表尾部添加链表对象
	*
	* @return
	*/
	public void addList(LinkedList<Object> list)
	{
		if (list != null && list.size() > 0)
			this.linkedList.addAll(list);
	}
	
	/**
	* 在链表头部添加一个对象
	*
	* @return
	*/
	public void addFirst(Object object)
	{
		if (object != null )
			this.linkedList.addFirst(object);
	}
	
	/**
	* 在链表头部添加链表对象
	*
	* @return
	*/
	public void addFirst(LinkedList<Object> list)
	{
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				addFirst(list.get(i));
			}
		}
	}
	
	/**
	* 在链尾部添加一个对象
	*
	* @return
	*/
	public void addLast(Object object)
	{
		if (object != null )
			this.linkedList.addLast(object);
	}
	
	/**
	* 在链表尾部添加链表对象
	*
	* @return
	*/
	public void addLast(LinkedList<Object> list)
	{
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				addLast(list.get(i));
			}
		}
	}
	
	/**
	* 移除链表指定位置的对象
	*
	* @return
	*/
	public void removeObject(int index)
	{
	   if ((index >= 0) && (index < this.linkedList.size()))
		      this.linkedList.remove(index);
	}
	
	/**
	* 移除链表所有对象
	*
	* @return
	*/
	public void removeAll()
	{
		this.linkedList.clear();
	}
	
	/**
	* 移除链表所有对象
	*
	* @return 适配器链表对象
	*/
	public LinkedList<Object> getList()
	{
		return this.linkedList;
	}
	
	@Override
	public int getCount() {		 
		return linkedList.size();
	}

	@Override
	public Object getItem(int position) {
		if (position >=0 && position < linkedList.size()){
			return linkedList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

}
