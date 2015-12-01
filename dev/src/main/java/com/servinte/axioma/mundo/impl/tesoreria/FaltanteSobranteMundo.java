package com.servinte.axioma.mundo.impl.tesoreria;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.tesoreria.DtoFaltanteSobrante;
import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IFaltanteSobranteDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.IFaltanteSobranteMundo;
import com.servinte.axioma.orm.FaltanteSobrante;
import com.servinte.axioma.orm.TiposMovimientoCaja;
import com.servinte.axioma.servicio.fabrica.TesoreriaFabricaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITiposMovimientoCajaServicio;

/**
 * Contiene la l&oacute;gica de Negocio para los turnos de caja
 * 
 * @author Cristhian Murillo
 * @see IFaltanteSobranteMundo
 */

public class FaltanteSobranteMundo implements IFaltanteSobranteMundo
{
	/**
	 * Atributo que permite la comunicaci&oacute;n con la interfaz encargada de definir el comportamiento del DAO.
	 */
	private IFaltanteSobranteDAO faltanteSobranteDAO;
	
	/**
	 * M&eacute;todo constructor de la clase
	 */
	public FaltanteSobranteMundo() {
		inicializar();
	}

	/**
	 * M&eacute;todo que permite la creaci&oacute;n de la interfaz que permite la comunicaci&oacute;n con el DAO 
	 */
	private void inicializar() {
		faltanteSobranteDAO	= TesoreriaFabricaDAO.crearFaltanteSobranteDAO();
	}
	
	@Override
	public List<DtoFaltanteSobrante> obtenerFaltantesSobrantesPorMovimiento(long idMovimiento) {
		return faltanteSobranteDAO.obtenerFaltantesSobrantesPorMovimiento(idMovimiento);
	}
	
	/**
	 * M&eacute;todo que retorma un listado con el tipo diferencia
	 */
	@Override
	public ArrayList<DtoIntegridadDominio> listarTipoDiferencia() {
		String[] listadoIntegridadDOminio=new String[]{ConstantesIntegridadDominio.acronimoDiferenciaFaltante,
				ConstantesIntegridadDominio.acronimoDiferenciaSobrante};
		Connection con=UtilidadBD.abrirConexion();
		ArrayList<DtoIntegridadDominio> listaTiposDiferencia=Utilidades.generarListadoConstantesIntegridadDominio(con, listadoIntegridadDOminio, false);
		UtilidadBD.closeConnection(con);
		return listaTiposDiferencia;
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
		
		String[] listadoEstado = new String[]{ConstantesIntegridadDominio.acronimoEstadoFaltanteSobranteGenerado,
				ConstantesIntegridadDominio.acronimoEstadoFaltanteSobranteConciliado};
		
		Connection con=UtilidadBD.abrirConexion();
		
		ArrayList<DtoIntegridadDominio> listaEstadoFaltanteSobrante=Utilidades.generarListadoConstantesIntegridadDominio(
				con, listadoEstado, false);
		
		UtilidadBD.closeConnection(con);
		
		return listaEstadoFaltanteSobrante;
		
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de consultar los turnos
	 * de apertura y cierre de las cajas
	 * 
	 * @return ArrayList<DtoIntegridadDominio>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<TiposMovimientoCaja> listarTurnosDeCaja(Integer filtro[]){
		
		ITiposMovimientoCajaServicio tiposMovimiento = TesoreriaFabricaServicio
		.crearTiposMovimentoCajaServicio();
	
	return tiposMovimiento.obtenerTiposMovimientoCajaFiltradoPorID(filtro);
		
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
		return faltanteSobranteDAO.consultarRegistroFaltanteSobrantePorID(registro);
	}
	
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de actualizar el registro
	 * faltante sobrante
	 * 
	 * @param FaltanteSobrante
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public boolean actualizarFaltanteSobrante(FaltanteSobrante registro) {		
		return faltanteSobranteDAO.actualizarFaltanteSobrante(registro);
	}
	

}
