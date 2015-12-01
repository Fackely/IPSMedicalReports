package com.servinte.axioma.mundo.interfaz.administracion;

import java.util.ArrayList;

import util.UtilidadFecha;

import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dao.fabrica.AdministracionFabricaDAO;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.ParametrizacionSemaforizacion;
import com.servinte.axioma.orm.Usuarios;
public interface IParametrizacionSemaforizacionMundo {
	


	/**
	 * Metodo que consulta las parametrizaciones en el sistema segun el reporte seleccionado
	 * @param reporte
	 * @return Lista de parametrizaciones registradas en el sistema
	 */
	public ArrayList<com.servinte.axioma.orm.ParametrizacionSemaforizacion> consultarParametrizaciones(
			String reporte);


	/**
	 * Eliminar los registros seleccionados
	 * @param index
	 * @param listaParam
	 */
	public void eliminarParametrizacion(String index, ArrayList<Long> listaParam) ;

	/**
	 * Se actualiza o a se ingresan nuevos registros al sistema
	 * @param usu
	 * @param lista
	 * @param tipoReporte
	 */
	public void adicionarModificar(
			UsuarioBasico usu,
			ArrayList<com.servinte.axioma.orm.ParametrizacionSemaforizacion> lista,
			String tipoReporte);
	

}
