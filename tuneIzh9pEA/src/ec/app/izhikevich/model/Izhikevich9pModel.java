package ec.app.izhikevich.model;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;

import ec.app.izhikevich.spike.SpikePatternAdapting;
import ec.app.izhikevich.util.GeneralUtils;

/*
 * inherits parms from 5p model, and 4 additional parameters
 */
public class Izhikevich9pModel extends IzhikevichModel{
	//Default values
/*	private static final double K = 0.55d;
	public static final double V_R = -58d;
	private static final double V_T = -38d;	
	private static final double CM = 457.61;	
	*/
	//Attributes
	protected   double k;	
	protected   double vR ;
	protected   double vT ;
	protected   double cM ;	
	
	public Izhikevich9pModel() {		
		//Default model parameters while model initialization
		super();
	//	this.setEpParameters(V_PEAK, V_MIN, V_R, V_T, CM);
	//	this.setModelParameters(K, A, B, D);
	}
	
	public void setEpParameters(double vpeak, double vmin, double vrest, double vthresh, double cm) {
		super.setEpParameters(vpeak, vmin);
		this.vR = vrest;		
		this.vT = vthresh;		
		this.cM = cm;
	}	
	
	public void setModelParameters(double k, double a, double b, double d) {		
		super.setModelParameters(a, b, d);
		this.setK(k);
	}	
	
	@Override
	public void computeDerivatives(double t, double[] y, double[] dy)
			throws MaxCountExceededException, DimensionMismatchException {
		double appCurrent;
		if(t>=timeMin && t<=timeMax) {
			appCurrent = current;
		}else
			appCurrent = 0;		
		
		/*if(y[0] >= vPeak) {
			y[0] = c;
			y[1] += d;					
		}*/	
		double V = y[0];
		double U = y[1];		
		dy[0] = ((k * (V - vR) * (V - vT))  - U + appCurrent) / cM;
		dy[1] = a * ((b * (V - vR)) - U);		
		
			
	}
	
	public double getK() {	return k;}
	public void setK(double k) {this.k = k;	}
	public double getvR() {	return vR;}
	public void setvR(double vR) {	this.vR = vR;}
	public double getvT() {	return vT;}
	public void setvT(double vT) {this.vT = vT;}
	public double getcM() {	return cM;}
	public void setcM(double cM) {this.cM = cM;	}
	public double getvMinOffset() {return this.vMin-this.vR;}
	
	public float  getRheo(float currDur, float iMin, float iMax, float incStep)
	   {
	     float rheo = Float.MAX_VALUE;
	     float holdIMax = iMax;
	     int nSpikes = 0;
	     while(iMin <= iMax)    {	
	    	rheo = (iMin + iMax) / 2;	
			this.setInputParameters(rheo, timeMin, currDur);
			
			IzhikevichSolver solver = new IzhikevichSolver(this);
			//solver.setsS(1.0);
			SpikePatternAdapting modelSpikePattern = solver.getSpikePatternAdapting();			
			if(modelSpikePattern == null){				
				iMin = rheo + incStep;   
				continue;
				//return Float.MAX_VALUE;	 										
			}
			
			nSpikes = modelSpikePattern.getNoOfSpikes();
			
			/*if( nSpikes == 1){
				return rheo;
			}else{*/
				 if (nSpikes >= 1){                                             
		              iMax = rheo - incStep;   
		         } else {                                                        
		              iMin = rheo + incStep;   
		         }		       
			//}			
	     } 
	   if(GeneralUtils.isCloseEnough(holdIMax, iMax, 1.0)) {
		   return Float.MAX_VALUE;
	   }
	   return rheo;	         
	  }

	public float getVDefAt(float I, float Idur, float vAT)
	   {
		
		this.setInputParameters(I, timeMin, Idur);			
		IzhikevichSolver solver = new IzhikevichSolver(this);
		//solver.setsS(1.0);
		SpikePatternAdapting modelSpikePattern = solver.getSpikePatternAdapting();			
		if(modelSpikePattern == null){					
			return Float.MAX_VALUE;	 										
		}		
		
		double voltage_min = modelSpikePattern.getSpikePatternData().getMinVoltage(timeMin, timeMin+Idur, 1);
		double voltage_max = modelSpikePattern.getSpikePatternData().getPeakVoltage(timeMin, timeMin+Idur, 1);
		
		float def1 = (float) Math.abs(voltage_max - this.vR);
		float def2 = (float) Math.abs(this.vR - voltage_min);
		if(def1>def2) return def1;
		else return def2;		       
	  }
}
