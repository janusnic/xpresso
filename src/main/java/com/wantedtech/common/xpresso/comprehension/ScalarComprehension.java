package com.wantedtech.common.xpresso.comprehension;

import java.io.Serializable;
import java.util.Iterator;
import java.util.ArrayList;

import com.wantedtech.common.xpresso.x;
import com.wantedtech.common.xpresso.functional.Function;
import com.wantedtech.common.xpresso.functional.Predicate;
import com.wantedtech.common.xpresso.types.list;
import com.wantedtech.common.xpresso.types.tuple.tuple;

class ScalarComprehension<O> implements Iterable<O>, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7495040683343678101L;

	boolean isBeforeFor = true;
			
	Function<Object,O> ifFunction = x.<Object,O>chainOf(x.doNothing);
	Function<Object,O> elseFunction = x.<Object,O>chainOf(x.doNothing);
	Predicate<Object> filterPredicate = x.TRUE;
	Predicate<Object> ifPredicate = x.TRUE;
	
	Iterable<O> transformedElements = list.<O>newArrayList();
	Iterable<Object> originalElements = list.<Object>newArrayList();
	
	@SuppressWarnings("unchecked")
	protected ScalarComprehension<O> transformWith(Function<Object,?> scalarFunction){
		ifFunction = (Function<Object,O>)scalarFunction;
		return this;
	}
	
	@SuppressWarnings("unchecked")
	protected final ScalarComprehension<O> elseTransformWith(Function<Object,?> scalarFunction){
		elseFunction = (Function<Object,O>)scalarFunction;
		return this;
	}
	
	protected ScalarComprehension<O> forElementIn(Iterable<?> listOfElements){
		ArrayList<O> new_transformed_scalars = list.<O>newArrayList();
		ArrayList<Object> new_original_scalars = list.<Object>newArrayList();
		for(Object scalar: listOfElements){
			if(ifPredicate.apply(scalar)){
				new_transformed_scalars.add(ifFunction.apply(scalar));
				new_original_scalars.add(scalar);
			}else{
				new_transformed_scalars.add(elseFunction.apply(scalar));
				new_original_scalars.add(scalar);
			}
		}
		transformedElements = new_transformed_scalars;
		originalElements = new_original_scalars;
		isBeforeFor = false;
		return this;
	}
	protected ScalarComprehension<O> forElementIn(list<?> listOfElements){
		ArrayList<O> new_transformed_scalars = list.<O>newArrayList();
		ArrayList<Object> new_original_scalars = list.<Object>newArrayList();
		for(Object scalar: listOfElements){
			if(ifPredicate.apply(scalar)){
				new_transformed_scalars.add(ifFunction.apply(scalar));
				new_original_scalars.add(scalar);
			}else{
				new_transformed_scalars.add(elseFunction.apply(scalar));
				new_original_scalars.add(scalar);
			}
		}
		transformedElements = new_transformed_scalars;
		originalElements = new_original_scalars;
		isBeforeFor = false;
		return this;
	}
	
	protected ScalarComprehension<O> forElementIn(Object scalar0,Object scalar1,Object... scalars){
		ArrayList<Object> scalarsList = list.newArrayList(scalar0, scalar1, scalars);
		return forElementIn(scalarsList);
	}
	
	protected ScalarComprehension<O> ifElement(Predicate<Object> scalarPredicate){
		if(isBeforeFor){
			ifPredicate = scalarPredicate;
		}else{
			ArrayList<O> new_transformed_scalars = new ArrayList<O>();
			for(tuple tuple : x.enumerate(originalElements)){
				int index = (int)(tuple.get(0));
				Object scalar = tuple.get(1);
				if(scalarPredicate.apply(scalar)){
					new_transformed_scalars.add(((ArrayList<O>)transformedElements).get(index));
				}
			}
			transformedElements = new_transformed_scalars;
		}
		return this;
	}
	
	@SuppressWarnings("unchecked")
	protected ScalarComprehension<O> ifNotElement(Predicate<?> scalarPredicate){
		return ifElement(x.NOT((Predicate<Object>)scalarPredicate));
	}
	
	@SuppressWarnings("unchecked")
	protected ScalarComprehension<O> ifElementNot(Predicate<?> scalarPredicate){
		return ifElement(x.NOT((Predicate<Object>)scalarPredicate));
	}
	
    public Iterator<O> iterator(){
    	return transformedElements.iterator();
    }

}