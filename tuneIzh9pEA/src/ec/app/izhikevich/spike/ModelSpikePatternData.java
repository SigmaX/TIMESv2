package ec.app.izhikevich.spike;

import java.util.ArrayList;

import ec.app.izhikevich.model.IzhikevichSolver;
import ec.app.izhikevich.util.GeneralUtils;

public class ModelSpikePatternData {
	
	private static final double DvDtCRITERION = -30;//
			//(Izhikevich9pModel.V_R - Izhikevich9pModel.V_PEAK) + 5; 
			// Vrest - vpeak must be < criterion //=> e.g -> -60 - 43 = -103 ; so, criterion = -100;
			// IMPORTANT update: apparently different calculation for different vrest vpeak combination.
	
	private double[] time;
	private double[] voltage;
	private double[] spikeTimes;
	
	public ModelSpikePatternData(double[] time, double[] voltage) {
		this.time = time;
		this.voltage = voltage;
	}
	
	public ModelSpikePatternData(double[] time, double[] voltage, double[] spikeTimes) {
		this.time = time;
		this.voltage = voltage;
		this.spikeTimes = spikeTimes;
	}
	public double[] getSpikeTimes() {
		return this.spikeTimes;
	}
	/*
	public double[] getSpikeTimes() {		 
		ArrayList<Double> spikeTimes = new ArrayList<Double>();
		double[] dvdt = getDvDt();
		for(int i=0;i<dvdt.length;i++) {
			//System.out.println(time[i] + "\t"+ voltage[i] + "\t"+dvdt[i]);
			if(dvdt[i] < DvDtCRITERION ) {				
				spikeTimes.add(time[i]);
			}
		}			
		return GeneralUtils.listToArrayDouble(spikeTimes);
		
	}*/
	
	public double[] getDvDt() {		
		double[] dvdt = new double[voltage.length-1];
		for(int i=0; i<dvdt.length; i++) {			
			dvdt[i] = voltage[i+1] - voltage[i];
		}
		return dvdt;
	}
	
	public double[] getTime(){
		return this.time;
	}
	public double[] getVoltage(){
		return this.voltage;
	}
	public int getIndex(double time) {
		for(int i=0; i<this.time.length; i++) {
			// use step size for time comparison for close enough
			if(GeneralUtils.isCloseEnough(this.time[i], time, IzhikevichSolver.SS)) {
				return i;
			}
		}
		System.out.println("Invalid Time" + time);
		System.exit(0);
		return -1;
	}
	public double[] getVoltage(double[] time) {
		if(time==null || time.length ==0) {
			return null;
		}
		double[] v=new double[time.length];
		for(int i=0; i<time.length; i++){
			v[i] = getVoltageAt(time[i]);
		}
		return v;
	}
	public double[] getVoltage(double timeMin, double timeMax) {
		if(!(timeMin<timeMax)) {
			return null;
		}
		int minIdx = getIndex(timeMin);
		int maxIdx = getIndex(timeMax);
		
		double[] v=new double[maxIdx-minIdx+1];
		for(int i=0; i<v.length; i++){
			v[i] = this.voltage[minIdx+i];
		}
		return v;
	}
	public double getVoltageAt(double time) {
		int idx = getIndex(time);
		return this.voltage[idx];
	}
	public double getTimeToReach(double voltage) {		
		for(int i=0; i<this.voltage.length; i++) {			
			if(this.voltage[i] >= voltage) {				
				return this.time[i];
			}
		}
		return Double.MAX_VALUE;
	}
	
	public double getPeakVoltage(){
		double maxVolt = -Double.MAX_VALUE;
		for(double volt:this.voltage)
			if(volt>maxVolt)
				maxVolt = volt;
		return maxVolt;
	}
	
	public double getPeakVoltage(double timeMin, double timeMax, double timeStep){
		double maxVolt = -Double.MAX_VALUE;
		double volt = -Double.MAX_VALUE;
		for(double t=timeMin; t<=timeMax; t+=timeStep){
			volt = getVoltageAt(t);
			if(volt>maxVolt)
				maxVolt = volt;
		}
		return maxVolt;
	}
	public double getMinVoltage(double timeMin, double timeMax, double timeStep){
		double minVolt = Double.MAX_VALUE;
		double volt = Double.MAX_VALUE;
		for(double t=timeMin; t<=timeMax; t+=timeStep){
			volt = getVoltageAt(t);
			if(volt<minVolt)
				minVolt = volt;
		}
		return minVolt;
	}
	public void displayForPlot(){
		for(int i=0;i<time.length;i++){
			System.out.println(time[i]+"\t"+voltage[i]);
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
