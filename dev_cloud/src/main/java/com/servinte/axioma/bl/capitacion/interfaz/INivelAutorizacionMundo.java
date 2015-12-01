package com.servinte.axioma.bl.capitacion.interfaz;

import java.util.List;

import com.servinte.axioma.dto.capitacion.NivelAutorizacionDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * Interface que expone los servicios de Negocio correspondientes a los
 * catalogos o parámetricas del modulo de Capitación
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:01 p.m.
 */
public interface INivelAutorizacionMundo {

	/**
	 * Método utilizado para consultar los niveles de autorizacion parametrizados
	 * para el usuario dependiendo el tipo de autorización y si esta activo
	 * 
	 * @param loginUsuario
	 * @param codigoPersonaUsuario
	 * @param tipoAutorizacion
	 * @param isActivo
	 * @return List<NivelAutorizacionDto>
	 * @throws IPSException
	 */
	List<NivelAutorizacionDto> consultarNivelesAutorizacionUsuario(String loginUsuario, int codigoPersonaUsuario, String tipoAutorizacion, boolean isActivo, boolean requiereTransaccion) throws IPSException;

	
	/**
	 * Metodo que verifica si existe parametrización de niveles por artículos (DCU 1006) Versus
	 * los Niveles de Autorización del usuario que aplican para la vía de Ingreso de la orden y 
	 * que apliquen para al menos uno de los medicamentos o insumos   
	 * 
	 * @param medicamentosInsumosOrden
	 * @param nivelesAutorizacionUsuario
	 * @return boolean
	 * @throws IPSException
	 */
	boolean existeNivelAutorizacionMedicamentoInsumo(List<MedicamentoInsumoAutorizacionOrdenDto> medicamentosInsumosOrden, List<Integer> nivelesAutorizacionUsuario) throws IPSException;

	
	/**
	 * Metodo que verifica si existe parametrización de niveles por servicios (DCU 1006) Versus
	 * los Niveles de Autorización del usuario que aplican para la vía de Ingreso de la orden y 
	 * que apliquen para al menos uno de los servicios
	 * 
	 * @param serviciosOrden
	 * @param nivelesAutorizacionUsuario
	 * @return
	 * @throws IPSException
	 */
	boolean existeNivelAutorizacionServicio(List<ServicioAutorizacionOrdenDto> serviciosOrden, List<Integer> nivelesAutorizacionUsuario) throws IPSException;

	/**
	 * Método encargado de realizar la validación de nivel automático para autorizaciones automáticas
	 * Recibe un DTO con la información de la orden que se requiere validar
	 * @param autorizacionCapitacionDto
	 * @return List<AutorizacionCapitacionDto>
	 * @throws IPSException
	 */
	List<AutorizacionCapitacionDto> validarNivelAutorizacionParaAutorizacionAutomatica(AutorizacionCapitacionDto autorizacionCapitacionDto, boolean requiereTransaccion) throws IPSException;
	
	/**
	 * Metodo que verifica si existe parametrización de niveles por artículos (DCU 1006) Versus
	 * los Niveles de Autorización del usuario que aplican para la vía de Ingreso de la orden y 
	 * que apliquen para al menos uno de los servicios o medicamentos o insumos 
	 * @param ordenesAutorizacion
	 * @param nivelesAutorizacionUsuario
	 * @param requiereTransaccion
	 * @return List<Integer>
	 * @throws IPSException
	 */
	List<Integer> verificarNivelesAutorizacionOrdenes(
			List<OrdenAutorizacionDto> ordenesAutorizacion,
			List<NivelAutorizacionDto> nivelesAutorizacionUsuario,
			boolean requiereTransaccion, AutorizacionCapitacionDto autorizacionCapitacionDto)
			throws IPSException;
	
	/**
	 * Método encargado de realizar la validación de niveles de autorización
	 * para las autorizaciones de población capitada.
	 * Este metodo debe recibir un objeto AutorizacionCapitacionDto con la 
	 * Entidad SubContratada de la autorización y los datos del usuario que
	 * se requiere validar (login - codigoUsuario) y las ordenes asociadas
	 * a la autorización con sus respectivos servicios o medicamentos.
	 * Si el parámetro esTodosRequeridos viene en true todos los servicios o
	 * medicamentos deben pasar la validación, caso contrario se cancela el 
	 * proceso de validación. Las modificaciones son guardadas en el objeto
	 * autorizacionCapitacionDto y pasadas por referencia
	 * DCU - 1105
	 * @author Diego Corredor
	 * @param autorizacionCapitacionDto
	 * @param esTodosRequeridos
	 * @param requiereTransaccion
	 * @throws IPSException
	 */
	void validarNivelesAutorizacionAutorizacionesPoblacionCapitada(
			AutorizacionCapitacionDto autorizacionCapitacionDto,
			boolean esTodosRequeridos,
			boolean requiereTransaccion) throws IPSException;

	/**
	* Metodo de encargado de realizar la validacion para autorizaciones Capitación Subcontratada
	* Autorización Servicios Medicamentos de Ingreso Estancia 
	* En este no se tiene en cuenta la prioridad de los niveles de autorizacion DCU 1105
	* @author ginsotfu
	* @param autorizacionCapitacionDto
	* @param esTodosRequeridos
	* @param requiereTransaccion
	* @throws IPSException
	*
	*/
	void validarNivelesAutorizacionAutorizacionesPoblacionCapitadaAutorServMedIngresoInstancia (
			AutorizacionCapitacionDto autorizacionCapitacionDto,
			boolean esTodosRequeridos,
			boolean requiereTransaccion)throws IPSException;
	
}