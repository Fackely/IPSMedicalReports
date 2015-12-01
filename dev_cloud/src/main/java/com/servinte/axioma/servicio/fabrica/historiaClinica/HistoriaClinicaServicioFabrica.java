package com.servinte.axioma.servicio.fabrica.historiaClinica;

import com.servinte.axioma.servicio.impl.historiaClinica.CausasExternasServicio;
import com.servinte.axioma.servicio.impl.historiaClinica.FinalidadesConsultaServicio;
import com.servinte.axioma.servicio.interfaz.historiaClinica.ICausasExternasServicio;
import com.servinte.axioma.servicio.interfaz.historiaClinica.IFinalidadesConsultaServicio;

public class HistoriaClinicaServicioFabrica {

	public HistoriaClinicaServicioFabrica() {
	
	}
	
	/**
	 * Método que retorna una de instancia de {@link CausasExternasServicio }
	 * 
	 * @author Fabián Becerra
	 * @return ICausasExternasServicio
	 */
	public static ICausasExternasServicio crearCausasExternasServicio(){
		return new CausasExternasServicio();				
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IFinalidadesConsultaServicio
	 * 
	 * @return FinalidadesConsultaServicio
	 * @author, Fabián Becerra
	 *
	 */
	public static IFinalidadesConsultaServicio crearFinalidadesConsultaServicio(){
		return new FinalidadesConsultaServicio();
	}
}
