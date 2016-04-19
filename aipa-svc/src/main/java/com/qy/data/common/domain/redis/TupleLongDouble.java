package com.qy.data.common.domain.redis;

import java.util.Comparator;

public class TupleLongDouble extends TupleObjectDouble{
	public final static Comparator<TupleLongDouble> ASC_COMPARATOR = new Comparator<TupleLongDouble>() {

		@Override
		public int compare(TupleLongDouble o1, TupleLongDouble o2) {
			// asc
			if(o1.getScore() < o2.getScore()){
				return -1;
			} else if(o1.getScore() > o2.getScore()){
				return 1;
			} else {
				return 0;
			}
		}
	};
	
	public final static Comparator<TupleLongDouble> DESC_COMPARATOR = new Comparator<TupleLongDouble>() {

		@Override
		public int compare(TupleLongDouble o1, TupleLongDouble o2) {
			// asc
			if(o1.getScore() < o2.getScore()){
				return 1;
			} else if(o1.getScore() > o2.getScore()){
				return -1;
			} else {
				return 0;
			}
		}
	};
	
	public final static Comparator<TupleLongDouble> ASC_COMPARATOR_ID = new Comparator<TupleLongDouble>() {

		@Override
		public int compare(TupleLongDouble o1, TupleLongDouble o2) {
			// asc
			if(o1.getElementId() < o2.getElementId()){
				return -1;
			} else if(o1.getElementId() > o2.getElementId()){
				return 1;
			} else {
				return 0;
			}
		}
	};
	
	public final static Comparator<TupleLongDouble> DESC_COMPARATOR_ID = new Comparator<TupleLongDouble>() {

		@Override
		public int compare(TupleLongDouble o1, TupleLongDouble o2) {
			// asc
			if(o1.getElementId() < o2.getElementId()){
				return 1;
			} else if(o1.getElementId() > o2.getElementId()){
				return -1;
			} else {
				return 0;
			}
		}
	};
	
//	public static void main(String[] argc){
//		List<TupleLongDouble> lt = new ArrayList<TupleLongDouble>();
//		
//		lt.add(new TupleLongDouble(1, 453));
//		lt.add(new TupleLongDouble(5, 23));
//		lt.add(new TupleLongDouble(2, 458));
//		lt.add(new TupleLongDouble(3, 300));
//		Collections.sort(lt, ASC_COMPARATOR);
//		System.out.println(JsonUtil.getJsonFromObject(lt));
//		
//		
//		List<TupleLongDouble> result = new LinkedList<TupleLongDouble>();
//		for (TupleLongDouble tupleLongDouble : lt) {
//			if(tupleLongDouble.getScore() > 300){
//				result.add(0, tupleLongDouble);
//			}
//			
//			if(result.size() >= 2){
//				break;
//			}
//		}
//		
//		System.out.println(JsonUtil.getJsonFromObject(result));
//	}
	
	private long elementId;
	
	public TupleLongDouble() { }
	
	public TupleLongDouble(long elementId, double score) {
		this.elementId = elementId;
		this.score = score;
	}
	public long getElementId() {
		return elementId;
	}
	public void setElementId(long elementId) {
		this.elementId = elementId;
	}
}
