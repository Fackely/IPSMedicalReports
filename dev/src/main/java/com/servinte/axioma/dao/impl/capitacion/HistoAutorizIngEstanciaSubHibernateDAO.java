package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;
import com.princetonsa.dto.manejoPaciente.DTOAutorizacionIngresoEstancia;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IHistoricoIngEstanciaSubcontratadaDAO;
import com.servinte.axioma.orm.delegate.manejoPaciente.HistoAutorizacionCapitaSubDelegate;
import com.servinte.axioma.orm.delegate.manejoPaciente.HistoAutorizacionIngEstanciaSubDelegate;

public class HistoAutorizIngEstanciaSubHibernateDAO implements IHistoricoIngEstanciaSubcontratadaDAO{

	
	
	HistoAutorizacionIngEstanciaSubDelegate delegate ;

	/**
	 * 
	 * Método constructor de la clase
	 * @author, Camilo Gomez
	 */
	public HistoAutorizIngEstanciaSubHibernateDAO() {
	
		delegate = new HistoAutorizacionIngEstanciaSubDelegate();
	}
	/**
	 * 
	 * Este Método se encarga de consultar por el ID las autorizaciones historicas de
	 * Ingreso Estancia 
	 * 
	 * @param DTOAutorEntidadSubcontratadaCapitacion dto
	 * @return ArrayList<DTOAutorEntidadSubcontratadaCapitacion>
	 * @author, Camilo Gomez
	 *
	 */
	public ArrayList<DTOAutorizacionIngresoEstancia> obtenerHistoricoAutorizEntiSubIngEstanciaPorID(
			DTOAutorizacionIngresoEstancia dto){
		return delegate.obtenerHistoAutorizacionIngEstanciaSubDelegate(dto);
	}
	
	
}
