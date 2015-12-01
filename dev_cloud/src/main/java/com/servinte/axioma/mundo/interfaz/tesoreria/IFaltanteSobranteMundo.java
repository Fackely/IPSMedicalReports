package com.servinte.axioma.mundo.interfaz.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.tesoreria.DtoFaltanteSobrante;
import com.servinte.axioma.mundo.impl.tesoreria.FaltanteSobranteMundo;
import com.servinte.axioma.orm.FaltanteSobrante;
import com.servinte.axioma.orm.TiposMovimientoCaja;

/**
 * Define la l&oacute;gica de negocio relacionada con los turnos de caja
 * 
 * @author Cristhian Murillo
 * @see com.servinte.axioma.mundo.impl.tesoreria.TurnoDeCajaMundo
 */

public interface IFaltanteSobranteMundo {
	
	/**
	 * Retorna los faltantes sobrantes del movimento dado
	 * @param long
	 * @return {@link FaltanteSobranteMundo}
	 */
	public List<DtoFaltanteSobrante> obtenerFaltantesSobrantesPorMovimiento(long idMovimiento);

	/**
	 * M&eacute;todo que retorna el listado con el tipo diferencia existentes en el sistema.
	 * @return
	 */
	public ArrayList<DtoIntegridadDominio> listarTipoDiferencia();
	
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de consultar los estados de faltante / sobrante
	 * 
	 * @return  ArrayList<DtoIntegridadDominio> 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DtoIntegridadDominio> listarEstadoFaltanteSobrante();
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de consultar los turnos
	 * de apertura y cierre de las cajas
	 * 
	 * @return ArrayList<TiposMovimientoCaja>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<TiposMovimientoCaja> listarTurnosDeCaja(Integer[] filtros);
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de consular los datos del registro faltante
	 * sobrante
	 * 
	 * @param FaltanteSobrante
	 * @return FaltanteSobrante
	 * @author, Angela Maria Aguirre
	 *
	 */
	public FaltanteSobrante consultarRegistroFaltanteSobrantePorID(FaltanteSobrante registro);
	
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de actualizar el registro
	 * faltante sobrante
	 * 
	 * @param FaltanteSobrante
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarFaltanteSobrante(FaltanteSobrante registro);
		
	
}
