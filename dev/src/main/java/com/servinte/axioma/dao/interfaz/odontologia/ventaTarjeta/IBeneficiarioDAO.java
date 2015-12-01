package com.servinte.axioma.dao.interfaz.odontologia.ventaTarjeta;

import com.servinte.axioma.dao.interfaz.IBaseDAO;
import com.servinte.axioma.orm.BeneficiarioTarjetaCliente;

/**
 * 
 * @author Edgar Carvajal
 *
 */
public interface IBeneficiarioDAO extends IBaseDAO<BeneficiarioTarjetaCliente> {
	
	/**
	 * Busca si el código de la persona pasada por parámetros es
	 * un beneficiario, lo que indica que ya tiene una tarjeta
	 * asociada.
	 * @author Juan David Ramírez
	 * @param codigoPersona Código del a persona pasada por parámetro
	 * @param filtrarTarjetasActivas Filtrar las tarjetas que se encuentren activas en el sistema
	 * @return {@link BeneficiarioTarjetaCliente} Beneficiario encontrado
	 * @since 06 Septiembre 2010
	 */
	public BeneficiarioTarjetaCliente obtenerBeneficiarioPersona(int codigoPersona, boolean filtrarTarjetasActivas);

	/**
	 * Validar la existencia del serial asociado a un tipo de tarjeta.
	 * 
	 * @param serial
	 * @param codigoTipoTarjeta
	 * @return true en caso de existir el serial, false de lo contrario
	 */
	public boolean existeSerial(Long serial, long codigoTipoTarjeta);
	
	
	/**
	 * Metodo que carga los beneficiarios 
	 * @param codigoPersonas
	 * @return
	 */
	public BeneficiarioTarjetaCliente obtenerBeneficiarioPersona(int codigoPersonas);

}
