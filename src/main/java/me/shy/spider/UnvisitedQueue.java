/**
 * @Since: 2020-03-27 21:48:31
 * @Author: shy
 * @Email: yushuibo@ebupt.com / hengchen2005@gmail.com
 * @Version: v1.0
 * @Description: -
 */

package me.shy.spider;

import java.util.Set;
import java.util.HashSet;
import java.util.LinkedList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import me.shy.spider.util.Util;

public final class UnvisitedQueue{
	private LinkedList<HtmlAnchor> queue = new LinkedList<HtmlAnchor>();
	private Set<String> visitedHrefs = new HashSet<String>();

	/**
	 *  入队列
	 * @param anchor
	*/
	public void enQueue(HtmlAnchor anchor, boolean isInit){
		if(isInit){
			queue.addLast(anchor);
		}else{
			if(!visitedHrefs.contains(Util.md5(anchor.getHrefAttribute())) && !contains(anchor)){
				queue.addLast(anchor);
			}
		}
	}

	/**
	 *  出对列
	 * @return
	*/
	public HtmlAnchor deQueue(){
		visitedHrefs.add(Util.md5(queue.getFirst().getHrefAttribute()));
		return queue.removeFirst();
	}

	/**
	 *  判断队列是否为空
	 * @return
	 */
	public boolean isEmpty(){
		return queue.isEmpty();
	}

	/**
	 * 判断anchor是否被访问过
	 * @param anchor
	 * @return
	 */
	public boolean contains(HtmlAnchor anchor) {
		for(int i=0,j=queue.size();i<j;i++){
			if(anchor.getHrefAttribute().equals(queue.get(i).getHrefAttribute())){
				return true;
			}
		}
		return false;
	}
}
