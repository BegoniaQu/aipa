package com.qy.data.common.domain.redis;

import java.util.Comparator;

public class TupleStringDouble extends TupleObjectDouble{
	public final static Comparator<TupleStringDouble> ASC_COMPARATOR = new Comparator<TupleStringDouble>() {

		@Override
		public int compare(TupleStringDouble o1, TupleStringDouble o2) {
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
	
	public final static Comparator<TupleStringDouble> DESC_COMPARATOR = new Comparator<TupleStringDouble>() {

		@Override
		public int compare(TupleStringDouble o1, TupleStringDouble o2) {
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
	
	private String elementId;

	public TupleStringDouble() { }
	
	public TupleStringDouble(String elementId, double score) {
		this.elementId = elementId;
		this.score = score;
	}
	public String getElementId() {
		return elementId;
	}
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
}
