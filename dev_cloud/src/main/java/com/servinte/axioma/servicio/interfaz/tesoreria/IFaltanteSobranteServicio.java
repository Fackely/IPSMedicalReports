package com.servinte.axioma.servicio.interfaz.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.tesoreria.DtoFaltanteSobrante;
import com.servinte.axioma.mundo.impl.tesoreria.FaltanteSobranteMundo;
import com.servinte.axioma.orm.FaltanteSobrante;
import com.servinte.axioma.orm.TiposMovimientoCaja;

public interface IFaltanteSobranteServicio {
	/**
	 * Retorna los faltantes sobrantes del movimento dado
	 * @param long
	 * @return {@link FaltanteSobranteMundo}
	 */
	public List<DtoFaltanteSobrante> obtenerFaltantesSobrantesPorMovimiento(long idMovimiento);

	/**
	 * Lista los tipos de diferencia existentes en el sistema
	 * @return
	 */
	public ArrayList<DtoIntegridadDominio> listarTipoDiferencia();
	
	/**
	 * 
	 * Este m$eacutetodo se encarga de consultar los estados de faltante / sobrante
	 * 
	 * @return  ArrayList<DtoIntegridadDominio> 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DtoIntegridadDominio> listarEstadoFaltanteSobrante();
	
	/**
	 * 
	 * Este m$eacutetodo se encarga de consultar los turnos
	 * de apertura y cierre de las cajas
	 * 
	 * @return ArrayList<TiposMovimientoCaja>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<TiposMovimientoCaja> listarTurnosDeCaja(Integer[] filtro);
	
		
	/**
	 * 
	 * Este m$eacutetodo se encarga de consular los datos del registro faltante
	 * sobrante
	 * 
	 * @param FaltanteSobrante
	 * @return FaltanteSobrante
	 * @author, Angela Maria Aguirre
	 *
	 */
	public FaltanteSobrante consultarRegistroFaltanteSobrantePorID(FaltanteSobrante registro);
		
}
