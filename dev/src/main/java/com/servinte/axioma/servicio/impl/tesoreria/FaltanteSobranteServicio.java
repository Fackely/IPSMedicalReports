package com.servinte.axioma.servicio.impl.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.tesoreria.DtoFaltanteSobrante;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IFaltanteSobranteMundo;
import com.servinte.axioma.orm.FaltanteSobrante;
import com.servinte.axioma.orm.TiposMovimientoCaja;
import com.servinte.axioma.servicio.interfaz.tesoreria.IFaltanteSobranteServicio;

public class FaltanteSobranteServicio implements IFaltanteSobranteServicio{

	/**
	 * Permite la comunicaci&oacute;n con la fabrica para la creaci&oacute;n de faltanteSobranteMundo.
	 */
	IFaltanteSobranteMundo faltanteSobranteMundo=TesoreriaFabricaMundo.crearFaltanteSobranteMundo();

	@Override
	public List<DtoFaltanteSobrante> obtenerFaltantesSobrantesPorMovimiento(
			long idMovimiento) {
		return faltanteSobranteMundo.obtenerFaltantesSobrantesPorMovimiento(idMovimiento);
	}

	/**
	 * Permite obtener un listado con los tipoDiferencia existentes en el sistema.
	 */
	@Override
	public ArrayList<DtoIntegridadDominio> listarTipoDiferencia() {
		return faltanteSobranteMundo.listarTipoDiferencia();
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de consultar los estados de faltante / sobrante
	 * 
	 * @return  ArrayList<DtoIntegridadDominio> 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DtoIntegridadDominio> listarEstadoFaltanteSobrante(){
		return faltanteSobranteMundo.listarEstadoFaltanteSobrante();
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de consultar los turnos
	 * de apertura y cierre de las cajas
	 * 
	 * @return ArrayList<TiposMovimientoCaja>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<TiposMovimientoCaja> listarTurnosDeCaja(Integer[] filtro){
		return faltanteSobranteMundo.listarTurnosDeCaja(filtro);
	}	
	
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
	public FaltanteSobrante consultarRegistroFaltanteSobrantePorID(FaltanteSobrante registro){
		return faltanteSobranteMundo.consultarRegistroFaltanteSobrantePorID(registro);
	}
	
}
