/**
 * 
 */
package com.servinte.axioma.dao.interfaz.inventario;

import com.princetonsa.dto.inventario.DTOEsqTarInventarioContrato;
import com.servinte.axioma.orm.EsqTarInventariosContrato;


public interface IEsqTarInventariosContratoDAO {
	
	public DTOEsqTarInventarioContrato buscarEsquematarifarioPorContrato(DTOEsqTarInventarioContrato dto);
	

}
