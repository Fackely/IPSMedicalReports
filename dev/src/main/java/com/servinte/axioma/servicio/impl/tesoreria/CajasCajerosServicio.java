package com.servinte.axioma.servicio.impl.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoCajaCajeros;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICajasCajerosMundo;
import com.servinte.axioma.servicio.interfaz.tesoreria.ICajasCajerosServicio;

/**
 * Implementaci&oacute;n de la interfaz {@link ICajasCajerosServicio}
 * 
 * @author Jorge Armando Agudelo Quintero - Luis Alejandro Echandia
 *
 */
public class CajasCajerosServicio  implements ICajasCajerosServicio{

	private ICajasCajerosMundo cajasCajerosMundo;
	
	public CajasCajerosServicio() {
		cajasCajerosMundo =  TesoreriaFabricaMundo.crearCajasCajerosMundo();
	}

	@Override
	public List<DtoUsuarioPersona> obtenerListadoCajeros() {
		
		return cajasCajerosMundo.obtenerListadoCajeros();
	}

	@Override
	public boolean validarUsuarioEsCajero(UsuarioBasico usuario) {
		
		return cajasCajerosMundo.validarUsuarioEsCajero(usuario);
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
	public ArrayList<DtoUsuarioPersona> obtenerCajerosCentrosAtencion(
			int consecutivoCentroAtencion){		
		return cajasCajerosMundo.obtenerCajerosCentrosAtencion(consecutivoCentroAtencion);		
	}
	
	@Override
	public ArrayList<DtoUsuarioPersona> obtenerCajerosPorInstitucion(int codigoInstitucion){
		return cajasCajerosMundo.obtenerCajerosPorInstitucion(codigoInstitucion);
	}

	/**
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.ICajasCajerosServicio#consultarCajaCajerosParametrizados(java.lang.String, java.lang.String)
	 */
	@Override
	public List<DtoCajaCajeros> consultarCajaCajerosParametrizados(
			String codigoInstitucion, String centroAtencion) {
		return cajasCajerosMundo.consultarCajaCajerosParametrizados(codigoInstitucion, centroAtencion);
	}

	/**
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.ICajasCajerosServicio#consultarDatosCajaParametrizados(java.lang.String, java.lang.String)
	 */
	@Override
	public List<DtoCajaCajeros> consultarDatosCajaParametrizados(Integer codigoCajero,
			String codigoInstitucion, String centroAtencion) {
		return cajasCajerosMundo.consultarDatosCajaParametrizados(codigoCajero,codigoInstitucion, centroAtencion);
	}
	
	/**
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.ICajasCajerosServicio#obtenerCajeroXCodigo(java.lang.Integer)
	 */
	public DtoUsuarioPersona obtenerCajeroXCodigo(Integer codigoCajero){
		return cajasCajerosMundo.obtenerCajeroXCodigo(codigoCajero);
	}
}
