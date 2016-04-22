/**
 * 
 */
package com.qy.data.common.data.container.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 暂时还没有提供对数组的支持
 * @author yonka
 * @Date 2015-5-13
 */
public class IterableObjectsJoinerAdapter<T> implements Iterable<T>{
	List<Iterable<T>> iterables;
	
	public IterableObjectsJoinerAdapter(){}
	
	@SafeVarargs
	public IterableObjectsJoinerAdapter(Iterable<T>... iterables){
		this.iterables = Arrays.asList(iterables);
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		if(iterables == null || iterables.size() == 0) return null;
		else {
			List<Iterator<T>> iterators = new ArrayList<Iterator<T>>();
			for (Iterable<T> iterable : iterables) {
				if(iterable == null) continue;
				Iterator<T> iterator = iterable.iterator();
				if(iterator != null) iterators.add(iterator);
			}
			return new InnerIterator<T>(iterators);
		}
	}
	
	static class InnerIterator<T> implements Iterator<T>{
		List<Iterator<T>> iterators = new ArrayList<Iterator<T>>();
		int curIteratorIndex = 0;
		
		public InnerIterator(List<Iterator<T>> iterators){
			this.iterators = iterators;
		}
		
		/* (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			if(iterators == null || iterators.size() == 0) throw new UnsupportedOperationException();
			return getCurrentIterator().hasNext();
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		@Override
		public T next() {
			if(iterators == null || iterators.size() == 0) throw new UnsupportedOperationException();
			return getCurrentIterator().next();
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {
			if(iterators == null || iterators.size() == 0) throw new UnsupportedOperationException();
			getCurrentIterator().remove();
		}
		
		private Iterator<T> getCurrentIterator(){
			while(curIteratorIndex < iterators.size() - 1){
				if(!iterators.get(curIteratorIndex).hasNext()) curIteratorIndex++;
				else break;
			}
			return iterators.get(curIteratorIndex);
		}
	} 
	
	public static void main(String[] args){
		Integer[] i1 = new Integer[]{1,2,3,4,5};
		Integer[] i2 = new Integer[]{6,7,8};
//		Integer[] i3 = null;
		
		List<Integer> l1 = Arrays.asList(i1);
		List<Integer> l2 = Arrays.asList(i2);
		List<Integer> l3 = null;
		IterableObjectsJoinerAdapter<Integer> ii = new IterableObjectsJoinerAdapter<Integer>(l1, l2, l3);
		for (Integer integer : ii) {
			System.out.println(integer);
		}
	}
}
