package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoCajaCajeros;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ICajasCajerosDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICajasCajerosMundo;


/**
 * Contiene la l&oacute;gica de Negocio para todo lo relacionado con las Cajas Cajeros
 * 
 * 
 * @author Jorge Armando Agudelo Quintero
 */

public class CajasCajerosMundo implements ICajasCajerosMundo{

	
	private ICajasCajerosDAO cajasCajerosDAO;

	
	public CajasCajerosMundo() {
		inicializar();
	}

	private void inicializar() {
		cajasCajerosDAO = TesoreriaFabricaDAO.crearCajasCajerosDAO();
	}
	
	public List<DtoUsuarioPersona> obtenerListadoCajeros() {
		
		//UtilidadTransaccion.getTransaccion().begin();
		
		List<DtoUsuarioPersona>  listadoCajeros = cajasCajerosDAO.obtenerListadoCajeros();
		
		//UtilidadTransaccion.getTransaccion().commit();	
		
		return listadoCajeros;
	}
	
	
	@Override
	public boolean validarUsuarioEsCajero(UsuarioBasico usuario)
	{
		//UtilidadTransaccion.getTransaccion().begin();
		Boolean flag = cajasCajerosDAO.validarUsuarioEsCajero(usuario);
		//UtilidadTransaccion.getTransaccion().commit();
		 
		return flag;
	}
	
	
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de consultar los usuarios cajeros pertenecientes
	 * a un centro de atenci&oacute;n espec&iacute;fico
	 * 
	 * @param consecutivoCentroAtencion
	 * @return ArrayList<DtoUsuarioPersona> 
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public ArrayList<DtoUsuarioPersona> obtenerCajerosCentrosAtencion(int consecutivoCentroAtencion){
		return cajasCajerosDAO.obtenerCajerosCentrosAtencion(consecutivoCentroAtencion);
	}
	
	@Override
	public ArrayList<DtoUsuarioPersona> obtenerCajerosPorInstitucion(int codigoInstitucion){
		return cajasCajerosDAO.obtenerCajerosPorInstitucion(codigoInstitucion);
	}
	
	/**
	 * @param codigoInstitucion
	 * @param centroAtencion
	 * @return cajas cajeros parametrizados
	 */
	public List<DtoCajaCajeros> consultarCajaCajerosParametrizados(String codigoInstitucion, String centroAtencion){
		return cajasCajerosDAO.consultarCajaCajerosParametrizados(codigoInstitucion,centroAtencion);
	}
	
	/**
	 * @param codigoInstitucion
	 * @param centroAtencion
	 * @return datos de caja 
	 */
	public List<DtoCajaCajeros> consultarDatosCajaParametrizados(Integer codigoCajero,String codigoInstitucion, String centroAtencion){
		return cajasCajerosDAO.consultarDatosCajaParametrizados(codigoCajero,codigoInstitucion,centroAtencion);
	}
	
	/**
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.ICajasCajerosMundo#obtenerCajeroXCodigo(java.lang.Integer)
	 */
	public DtoUsuarioPersona obtenerCajeroXCodigo(Integer codigoCajero){
		return cajasCajerosDAO.obtenerCajeroXCodigo(codigoCajero);
	}
	
}