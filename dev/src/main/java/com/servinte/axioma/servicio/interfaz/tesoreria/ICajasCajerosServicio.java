package com.servinte.axioma.servicio.interfaz.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoCajaCajeros;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.CajasCajeros;

/**
 * Servicio que le delega al negocio las operaciones relacionados con las Cajas y los Cajeros
 * 
 * @author Jorge Armando Agudelo Quintero - Luis Alejandro Echandia
 * @see com.servinte.axioma.servicio.impl.tesoreria.CajasCajerosServicio
 */


public interface ICajasCajerosServicio {

	/**
	 * Retorna un listado de Cajeros relacionados en la entidad cajas_cajeros
	 * 
	 * @autor Jorge Armando Agudelo - Luis Alejandro Echandia
	 * @return List<{@link CajasCajeros}>
	 */
	public List<DtoUsuarioPersona> obtenerListadoCajeros() ;
	
	
	/**
	 * Valida que el usuario este habilitado como cajero
	 * @param usuario
	 * @return {@link Boolean}
	 */
	public boolean validarUsuarioEsCajero(UsuarioBasico usuario); 
	
	
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
	public ArrayList<DtoUsuarioPersona> obtenerCajerosCentrosAtencion(int consecutivoCentroAtencion);	
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de consultar los usuarios cajeros pertenecientes 
	 * a una instituci&oacute;n en particular.
	 * 
	 * @param codigoInstitucion
	 * @return ArrayList<DtoUsuarioPersona> 
	 * 
	 * @author Yennifer Guerrero
	 *
	 */
	public ArrayList<DtoUsuarioPersona> obtenerCajerosPorInstitucion(int codigoInstitucion);
	
	/**
	 * @param codigoInstitucion
	 * @param centroAtencion
	 * @return lista de cajas y cajeros parametrizados 
	 */
	public List<DtoCajaCajeros> consultarCajaCajerosParametrizados(String codigoInstitucion, String centroAtencion);
	
	/**
	 * @param codigoInstitucion
	 * @param centroAtencion
	 * @return lista con datos de caja
	 */
	public List<DtoCajaCajeros> consultarDatosCajaParametrizados(Integer codigoCajero,
			String codigoInstitucion, String centroAtencion);
	
	/**
	 * @param codigoCajero
	 * @return cajero asociado al código ingresado
	 */
	public DtoUsuarioPersona obtenerCajeroXCodigo(Integer codigoCajero);
	
}
