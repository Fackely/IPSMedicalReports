package com.servinte.axioma.dao.interfaz.tesoreria;


import java.util.List;

import com.servinte.axioma.orm.TransportadoraValores;


/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de Transportadoras de Valores
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */


public interface ITransportadoraValoresDAO {

	/**
	 * LISTA TODAS LA TRANPORTADORAS CON SUS RESPECTIVAS ASOCIACIONES
	 * @param dtoTransportadora
	 * @param institucion
	 * @return
	 */
	public List<TransportadoraValores> listarTodos(TransportadoraValores dtoTransportadora, int institucion);

	/**
	 * Retorna una Transportadora de Valores espec&iacute;fica
	 * 
	 * @param codigoTransportadora
	 * @return
	 */
	public TransportadoraValores consultarTransportadoraValores(int codigoTransportadora);
		
	/**
	 * Retorna una transportadora de valores por centro de atenci&oacute;n
	 * @author Diana Carolina G
	 * @param dtoTransportadora
	 * @param institucion
	 * @param consecutivoCentroAtencion
	 * @return
	 */
	public List<TransportadoraValores> listarTodos(TransportadoraValores dtoTransportadora, int institucion , int consecutivoCentroAtencion );
	
	
	
}
