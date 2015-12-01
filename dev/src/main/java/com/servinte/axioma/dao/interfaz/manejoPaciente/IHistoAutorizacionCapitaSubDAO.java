package com.servinte.axioma.dao.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;
import com.servinte.axioma.orm.HistoAutorizacionCapitaSub;

/**
 * Esta clase se encarga de definir los métodos de 
 * negocio para la entidad HistoAutorizacionCapitaSub
 * 
 * @author Angela Maria Aguirre
 * @since 17/12/2010
 */
public interface IHistoAutorizacionCapitaSubDAO {
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro del historial de autorización de capitación subcontratada
	 * 
	 * @param AutorizacionesCapitacionSub autorizacion
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarAutorizacionEntidadSubcontratada(HistoAutorizacionCapitaSub historial);

	
	/**
	 * 
	 * Este Método se encarga de consultar por el ID las autorizaciones historicas de
	 * entidades subcontratadas y su respectiva autorización de capitación
	 * 
	 * @param DTOAutorEntidadSubcontratadaCapitacion dto
	 * @return ArrayList<DTOAutorEntidadSubcontratadaCapitacion>
	 * @author, Camilo Gomez
	 *
	 */
	public ArrayList<DTOAutorEntidadSubcontratadaCapitacion> obtenerHistoricoAutorizacionEntidadSubCapitacionPorID(
			DTOAutorEntidadSubcontratadaCapitacion dto);
}
