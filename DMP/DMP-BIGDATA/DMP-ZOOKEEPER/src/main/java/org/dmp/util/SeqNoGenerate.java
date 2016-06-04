package org.dmp.util;

public class SeqNoGenerate {
	
	public SeqNoGenerate(){
		
	}
	
	public SeqNoGenerate(long min,long max){
		this.min=min;
		this.max=max;
		begin=min;
	}
	private long begin;
	private long max=Long.MAX_VALUE;
	private long min=0;
	public synchronized long next(){
		if(begin>=max){
			begin=min;
		}
		return begin++;
	}

}
