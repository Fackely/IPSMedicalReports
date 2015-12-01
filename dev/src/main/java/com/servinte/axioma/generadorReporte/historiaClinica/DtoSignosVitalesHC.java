/** 
 * 
 */
package com.servinte.axioma.generadorReporte.historiaClinica;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author JorOsoVe
 *
 */
public class DtoSignosVitalesHC 
{
	   
	

	/**
	 * 
	 */
	private Collection signosVitalesInstitucionCcosto;
	
	/**
	 * 
	 */
	private Collection signosVitalesFijosHisto;
	
	/**
	 * 
	 */
	private Collection signosVitalesParamHisto;
	
	/**
	 * 
	 */
	private Collection signosVitalesHistoTodos;

	/**
	 * 
	 */
	public DtoSignosVitalesHC() 
	{
		this.signosVitalesInstitucionCcosto = new ArrayList();
		this.signosVitalesFijosHisto = new ArrayList();
		this.signosVitalesParamHisto = new ArrayList();
		this.signosVitalesHistoTodos = new ArrayList();
	}

	public Collection getSignosVitalesInstitucionCcosto() {
		return signosVitalesInstitucionCcosto;
	}

	public void setSignosVitalesInstitucionCcosto(
			Collection signosVitalesInstitucionCcosto) {
		this.signosVitalesInstitucionCcosto = signosVitalesInstitucionCcosto;
	}

	public Collection getSignosVitalesFijosHisto() {
		return signosVitalesFijosHisto;
	}

	public void setSignosVitalesFijosHisto(Collection signosVitalesFijosHisto) {
		this.signosVitalesFijosHisto = signosVitalesFijosHisto;
	}

	public Collection getSignosVitalesParamHisto() {
		return signosVitalesParamHisto;
	}

	public void setSignosVitalesParamHisto(Collection signosVitalesParamHisto) {
		this.signosVitalesParamHisto = signosVitalesParamHisto;
	}

	public Collection getSignosVitalesHistoTodos() {
		return signosVitalesHistoTodos;
	}

	public void setSignosVitalesHistoTodos(Collection signosVitalesHistoTodos) {
		this.signosVitalesHistoTodos = signosVitalesHistoTodos;
	}
	
	
	public DtoSignosVitalesHC(Collection signosVitalesInstitucionCcosto,
			Collection signosVitalesFijosHisto, Collection signosVitalesParamHisto,
			Collection signosVitalesHistoTodos) {
		super();
		this.signosVitalesInstitucionCcosto = signosVitalesInstitucionCcosto;
		this.signosVitalesFijosHisto = signosVitalesFijosHisto;
		this.signosVitalesParamHisto = signosVitalesParamHisto;
		this.signosVitalesHistoTodos = signosVitalesHistoTodos;
	}
	

}
