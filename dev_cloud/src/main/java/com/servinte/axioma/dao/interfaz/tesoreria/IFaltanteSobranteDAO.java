package com.servinte.axioma.dao.interfaz.tesoreria;

import java.util.List;

import com.princetonsa.dto.tesoreria.DtoFaltanteSobrante;
import com.servinte.axioma.mundo.impl.tesoreria.FaltanteSobranteMundo;
import com.servinte.axioma.orm.FaltanteSobrante;

/**
 * Interfaz donde se define el comportamiento del DAO
 * @author Cristhian Murillo
 */
public interface IFaltanteSobranteDAO {
	
	/**
	 * Retorna los faltantes sobrantes del movimento dado
	 * @param long
	 * @return {@link FaltanteSobranteMundo}
	 */
	public List<DtoFaltanteSobrante> obtenerFaltantesSobrantesPorMovimiento(long idMovimiento);
	
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
	public FaltanteSobrante consultarRegistroFaltanteSobrantePorID(FaltanteSobrante registro);
	
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de actualizar el registro
	 * faltante sobrante.
	 * 
	 * @param FaltanteSobrante
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarFaltanteSobrante(FaltanteSobrante registro);
	
	
}
