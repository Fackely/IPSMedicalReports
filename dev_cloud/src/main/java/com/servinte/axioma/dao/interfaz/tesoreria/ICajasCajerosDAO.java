package com.servinte.axioma.dao.interfaz.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoCajaCajeros;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.CajasCajeros;

/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de Cajas Cajeros
 * 
 * @author Jorge Armando Agudelo Quintero - Luis Alejandro Echandia
 *
 */

public interface ICajasCajerosDAO {
	
	
	
	/**
	 * Retorna un listado de Cajeros relacionados en la entidad cajas_cajeros
	 * 
	 * @autor Jorge Armando Agudelo - Luis Alejandro Echandia
	 * @return List<{@link CajasCajeros}>
	 */
	public List<DtoUsuarioPersona> obtenerListadoCajeros();
	
	
	/**
	 * Valida que el usuario este habilitado como cajero
	 * @param usuario
	 * @return {@link Boolean}
	 */
	public boolean validarUsuarioEsCajero(UsuarioBasico usuario);
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de consultar los usuarios cajeros pertenecientes
	 * a un centro de atención específico
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
	 * Este metodo permite consultar para todos los usuarios las cajas que tiene asociadas y que se encuentran activas
	 * @param usuario
	 * @return
	 * @author Diana Ruiz
	 */
	
	public ArrayList<CajasCajeros> obtenerTodasCajasCajero(int codigo_caja);
	/**
	 * Este metodo permite realizar una merge a la clase
	 * @param instance
	 * @return
	 * @author Diana Ruiz
	 */
	public CajasCajeros merge(CajasCajeros instance);
	
	
	/**
	 * Este metodo permite consultar por usuario las cajas que este tiene activas
	 * @param usuario
	 * @return
	 */
	public ArrayList<CajasCajeros> obtenerCajasCajero(UsuarioBasico usuario);
	
	
	/**
	 * @param codigoInstitucion
	 * @param centroAtencion
	 * @return caja y cajeros parametrizados 
	 */
	public List<DtoCajaCajeros> consultarCajaCajerosParametrizados(String codigoInstitucion, String centroAtencion);
	
	/**
	 * @param codigoInstitucion
	 * @param centroAtencion
	 * @return
	 */
	public List<DtoCajaCajeros> consultarDatosCajaParametrizados(Integer codigoCajero,String codigoInstitucion, String centroAtencion);
	
	
	/**
	 * @param codigoCajero
	 * @return cajero asociado al código dado
	 */
	public DtoUsuarioPersona obtenerCajeroXCodigo(Integer codigoCajero);
	
}
