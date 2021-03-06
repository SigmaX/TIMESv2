package ec.app.izhikevich.spike;

import java.util.ArrayList;

import ec.app.izhikevich.inputprocess.labels.PatternFeatureID;


public class PatternFeature {
	
	//private PatternFeature feature;
	private boolean range;
	private double weight;
	private double value;
	private double valueMin;
	private double valueMax;
	
	public static final ArrayList<PatternFeatureID> W_NOT_REQUIRED = new ArrayList<>();
	static {
		W_NOT_REQUIRED.add(PatternFeatureID.sfa_linear_b1);
		W_NOT_REQUIRED.add(PatternFeatureID.sfa_linear_m1);
		W_NOT_REQUIRED.add(PatternFeatureID.sfa_linear_b2);
		W_NOT_REQUIRED.add(PatternFeatureID.sfa_linear_m2);
		W_NOT_REQUIRED.add(PatternFeatureID.current);
		W_NOT_REQUIRED.add(PatternFeatureID.valid_max_v);
	}
	
	public PatternFeature(double value, double weight) {	
		this.range = false;
		this.value = value;
		this.weight = weight;
	}
	
	public PatternFeature(double valueMin, double valueMax, double weight) {		
		this.range = true;
		this.valueMin = valueMin;
		this.valueMax = valueMax;
		this.weight = weight;
	}
/*	public ConstraintFeature(PatternFeature feature, double value, double weight) {
		this.feature = feature;
		this.range = false;
		this.value = value;
		this.weight = weight;
	}
	
	public ConstraintFeature(PatternFeature feature, double valueMin, double valueMax, double weight) {
		this.feature = feature;
		this.range = true;
		this.valueMin = valueMin;
		this.valueMax = valueMax;
		this.weight = weight;
	}*/
	
	public boolean isRange() {
		return range;
	}
	public void setRange(boolean range) {
		this.range = range;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public double getValueMin() {
		return valueMin;
	}
	public void setValueMin(double valueMin) {
		this.valueMin = valueMin;
	}
	public double getValueMax() {
		return valueMax;
	}
	public void setValueMax(double valueMax) {
		this.valueMax = valueMax;
	}
	/*
	 * Util methods
	 */
	public static double[] convertToDoubleArray(PatternFeature[] features) {
		double[] doubleArray = new double[features.length];
		int i=0;
		for(PatternFeature feature: features) {
			doubleArray[i++] = feature.getValue();
		}
		return doubleArray;
	}

	/*public PatternFeature getFeature() {
		return feature;
	}

	public void setFeature(PatternFeature feature) {
		this.feature = feature;
	}*/
}
