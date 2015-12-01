package com.servinte.axioma.mundo.interfaz.odontologia.ventaTarjetaCliente;

import com.servinte.axioma.orm.BeneficiarioTarjetaCliente;


/**
 * 
 * @author Edgar Carvajal
 *
 */
public interface IBeneficiarioMundo {
	
	
	/**
	 *  Metod que Guardar Beneficiarios
	 * @author Edgar Carvajal Ruiz
	 */
	public void insertar(BeneficiarioTarjetaCliente objeto);
	
	
	/**
	 * Metodo que recibe un objeto de Beneficiarios  y lo modifica de la base de datos
	 * @author Edgar Carvajal 
	 * @param objecto
	 */
	 public void modificar(BeneficiarioTarjetaCliente objeto);
	
	
	/**
	 * Metodo que recibe un objeto de tipo Beneficiarios y lo elimina de la base de datos
	 * @author Edgar Carvajal
	 * @param objecto
	 */
	public void eliminar( BeneficiarioTarjetaCliente objeto);

	
	/**
	 * Metodo que recive un Id y retorna un Tipo de Objeto Beneficiarios
	 * @author Edgar Carvajal
	 * @param objeto
	 * @param id
	 * @return
	 */
	public BeneficiarioTarjetaCliente buscarxId(Number id);
	
	/**
	 * Busca si el código de la persona pasada por parámetros es
	 * un beneficiario, lo que indica que ya tiene una tarjeta
	 * asociada.
	 * @author Juan David Ramírez
	 * @param codigoPersona Código del a persona pasada por parámetro
	 * @param filtrarTarjetasActivas Filtrar las tarjetas activas para el sistema
	 * @return {@link BeneficiarioTarjetaCliente} Beneficiario encontrado
	 * @since 06 Septiembre 2010
	 */
	public BeneficiarioTarjetaCliente obtenerBeneficiarioPersona(int codigoPersona, boolean filtrarTarjetasActivas);
	
	
	/**
	 * Obtener Beneficiario paciente
	 * @param codigoPersona
	 * @param filtrarTarjetasActivas
	 * @return
	 */
	public BeneficiarioTarjetaCliente obtenerBeneficiarioPaciente(int codigoPersona);
	
}
