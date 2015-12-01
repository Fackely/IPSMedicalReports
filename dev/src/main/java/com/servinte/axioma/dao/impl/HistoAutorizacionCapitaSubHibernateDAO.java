package com.servinte.axioma.dao.impl;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IHistoAutorizacionCapitaSubDAO;
import com.servinte.axioma.orm.HistoAutorizacionCapitaSub;
import com.servinte.axioma.orm.delegate.manejoPaciente.HistoAutorizacionCapitaSubDelegate;

/**
 * Esta clase se encarga de ejecutar los m�todos de 
 * negocio para la entidad HistoAutorizacionCapitaSub
 * 
 * @author Angela Maria Aguirre
 * @since 17/12/2010
 */
public class HistoAutorizacionCapitaSubHibernateDAO implements
		IHistoAutorizacionCapitaSubDAO {
	
	HistoAutorizacionCapitaSubDelegate delegate ;
	
	
	/**
	 * 
	 * M�todo constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public HistoAutorizacionCapitaSubHibernateDAO(){
		delegate = new HistoAutorizacionCapitaSubDelegate();
	}
	
	
	/**
	 * 
	 * Este M�todo se encarga de insertar en la base de datos
	 * un registro del historial de autorizaci�n de capitaci�n subcontratada
	 * 
	 * @param AutorizacionesCapitacionSub autorizacion
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarAutorizacionEntidadSubcontratada(HistoAutorizacionCapitaSub historial){		
		return delegate.guardarAutorizacionEntidadSubcontratada(historial);
	}
	
	
	/**
	 * 
	 * Este M�todo se encarga de consultar por el ID las autorizaciones historicas de
	 * entidades subcontratadas y su respectiva autorizaci�n de capitaci�n
	 * 
	 * @param DTOAutorEntidadSubcontratadaCapitacion dto
	 * @return ArrayList<DTOAutorEntidadSubcontratadaCapitacion>
	 * @author, Camilo Gomez
	 *
	 */
	public ArrayList<DTOAutorEntidadSubcontratadaCapitacion> obtenerHistoricoAutorizacionEntidadSubCapitacionPorID(
			DTOAutorEntidadSubcontratadaCapitacion dto){
		return delegate.obtenerHistoricoAutorizacionEntidadSubCapitacionPorID(dto);
	}

}
