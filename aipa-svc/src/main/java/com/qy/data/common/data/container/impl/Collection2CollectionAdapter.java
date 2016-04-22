/**
 * 
 */
package com.qy.data.common.data.container.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;

/**
 * @author qy
 * @Date 2015-4-24
 */
public class Collection2CollectionAdapter<T, K> implements Collection<T>{
	private Collection<K> srcCollection;
	private GetValueFromCollectionElement<T, K> getter;
	
	public interface GetValueFromCollectionElement<T, K>{
		public T getFieldFromCollectionElement(K k);
	}
	
	public Collection2CollectionAdapter(Collection<K> srcCollection, GetValueFromCollectionElement<T, K> getter){
		this.srcCollection = srcCollection;
		this.getter = getter;
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#size()
	 */
	@Override
	public int size() {
		return srcCollection.size();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return srcCollection.isEmpty();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#contains(java.lang.Object)
	 */
	@Deprecated
	@Override
	public boolean contains(Object o) {
		Iterator<K> ik = srcCollection.iterator();
		K curEle;
		while(ik.hasNext()){
			curEle = ik.next();
			T t = getter.getFieldFromCollectionElement(curEle);
			if(t != null && t.equals(o)) return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		final Iterator<K> srcIterator = srcCollection.iterator();
		return new Iterator<T>(){

			@Override
			public boolean hasNext() {
				return srcIterator.hasNext();
			}

			@Override
			public T next() {
				return getter.getFieldFromCollectionElement(srcIterator.next());
			}

			@Override
			public void remove() {
				srcIterator.remove();
			}
		};
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#toArray()
	 */
	@Override
	public Object[] toArray() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#toArray(T[])
	 */
	@Override
	public <E> E[] toArray(E[] a) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	@Override
	public boolean add(T e) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object o) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#containsAll(java.util.Collection)
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends T> c) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#retainAll(java.util.Collection)
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#clear()
	 */
	@Override
	public void clear() {
		srcCollection.clear();
	}

	public static void main(String[] args){
		List<String> l = new ArrayList<String>();
		l.add("123");
		l.add("456");
		Collection<Integer> res = new Collection2CollectionAdapter<Integer, String>(l, new GetValueFromCollectionElement<Integer, String>() {
			
			@Override
			public Integer getFieldFromCollectionElement(String k) {
				return k.length();
			}
		});
		for (Integer integer : res) {
			System.out.println(integer);
		}
	}
}
