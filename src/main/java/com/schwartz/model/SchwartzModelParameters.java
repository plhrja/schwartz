package com.schwartz.model;

import com.schwartz.matlab.IMatlabConvertable;

/**
 *  A dataholder for the parameters used in the Schwartz two factor model.
 * 
 *  The listings below correspond to the price dynamics <b> under the  real world measure P</b>.
 *  The parameters that are used in the spot price SDE:<br>
 *  <ul>
 *      <li>mu</li> 
 *      <li>sigmaSpot</li> 
 *  </ul><br>
 *  
 *  The parameters used in the convenience yield SDE:<br>
 *  <ul>
 *      <li>kappa</li> 
 *      <li>alpha</li>
 *      <li>sigmaConvenienceYield</li>
 *  </ul><br>
 * 
 *  Additionally, the model also includes the following parameters that are not explicitly present in the SDEs
 *  <ul>
 *      <li>interest</li> 
 *      <ul>
 *          <li>Denotes the risk-free interest rate.</li>
 *      </ul>
 *      <li>rho</li> 
 *      <ul>
 *          <li>The correlation between the increments of the BMs in the SDEs</li>
 *      </ul>
 *      <li>lambda</li> 
 *      <ul>
 *          <li>The market price of risk of the convenience yield.</li>
 *      </ul>
 *  </ul>
 * 
 * 
 * @author woope
 * @see ISchwartzCalculator
 */
public class SchwartzModelParameters implements IMatlabConvertable {
    private double mu;
    private double sigmaSpot;
    private double kappa;
    private double alpha;
    private double sigmaConvenienceYield;
    private double interest;
    private double rho;
    private double lambda;

    public SchwartzModelParameters(double mu, double sigmaSpot, double kappa, double alpha, 
            double sigmaConvenienceYield, double interest, double rho, double lambda) {
        this.mu = mu;
        this.sigmaSpot = sigmaSpot;
        this.kappa = kappa;
        this.alpha = alpha;
        this.sigmaConvenienceYield = sigmaConvenienceYield;
        this.interest = interest;
        this.rho = rho;
        this.lambda = lambda;
    }

    public double getMu() {
        return this.mu;
    }

    public double getSigmaSpot() {
        return this.sigmaSpot;
    }

    public double getKappa() {
        return this.kappa;
    }

    public double getAlpha() {
        return this.alpha;
    }

    public double getSigmaConvenienceYield() {
        return this.sigmaConvenienceYield;
    }

    public double getInterest() {
        return this.interest;
    }

    public double getRho() {
        return this.rho;
    }

    public double getLambda() {
        return this.lambda;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.mu) ^ (Double.doubleToLongBits(this.mu) >>> 32));
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.sigmaSpot) ^ (Double.doubleToLongBits(this.sigmaSpot) >>> 32));
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.kappa) ^ (Double.doubleToLongBits(this.kappa) >>> 32));
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.alpha) ^ (Double.doubleToLongBits(this.alpha) >>> 32));
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.sigmaConvenienceYield) ^ (Double.doubleToLongBits(this.sigmaConvenienceYield) >>> 32));
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.interest) ^ (Double.doubleToLongBits(this.interest) >>> 32));
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.rho) ^ (Double.doubleToLongBits(this.rho) >>> 32));
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.lambda) ^ (Double.doubleToLongBits(this.lambda) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SchwartzModelParameters other = (SchwartzModelParameters) obj;
        if (Double.doubleToLongBits(this.mu) != Double.doubleToLongBits(other.mu)) {
            return false;
        }
        if (Double.doubleToLongBits(this.sigmaSpot) != Double.doubleToLongBits(other.sigmaSpot)) {
            return false;
        }
        if (Double.doubleToLongBits(this.kappa) != Double.doubleToLongBits(other.kappa)) {
            return false;
        }
        if (Double.doubleToLongBits(this.alpha) != Double.doubleToLongBits(other.alpha)) {
            return false;
        }
        if (Double.doubleToLongBits(this.sigmaConvenienceYield) != Double.doubleToLongBits(other.sigmaConvenienceYield)) {
            return false;
        }
        if (Double.doubleToLongBits(this.interest) != Double.doubleToLongBits(other.interest)) {
            return false;
        }
        if (Double.doubleToLongBits(this.rho) != Double.doubleToLongBits(other.rho)) {
            return false;
        }
        if (Double.doubleToLongBits(this.lambda) != Double.doubleToLongBits(other.lambda)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SchwartzModelParameters{" + "mu=" + mu + ", sigmaSpot=" + sigmaSpot + ", kappa=" + kappa + ", alpha=" + alpha 
                + ", sigmaCY=" + sigmaConvenienceYield + ", interest=" + interest + ", rho=" + rho + ", lambda=" + lambda + '}';
    }

    @Override
    public double[][] getMatlabFormat() {
        return new double[][]{{mu, sigmaSpot, kappa, alpha, sigmaConvenienceYield, interest, rho, lambda}};
    }

}
