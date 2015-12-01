package com.servinte.axioma.dao.interfaz.capitacion;

import java.util.Date;
import java.util.List;

import com.princetonsa.dto.facturacion.DtoContrato;
import com.servinte.axioma.orm.ConvUsuariosCapitados;

/**
 * Esta clase se encarga de definir los m�todos de negocio
 * para la entidad ConvUsuariosCapitados
 * @author Ricardo Ruiz
 *
 */
public interface IConvUsuariosCapitadosDAO {

	/**
	 * attachDirty
	 * @param instance
	 */
	public void attachDirty(ConvUsuariosCapitados instance);
	
	/**
	 * 
	 * Este M�todo se encarga de consultar el registro m�s reciente en la tabla para
	 * retornar los datos a postular
	 * @param codigoUsuarioCapitado
	 * @return ConvUsuariosCapitados
	 * @author Ricardo Ruiz
	 *
	 */
	public ConvUsuariosCapitados buscarConvUsuariosCapitadosRecientePorUsuarioCapitado(long codigoUsuarioCapitado);
	
	
	/**
	 * 
	 * Este M�todo se encarga de obtener los registros para un usuario capitado
	 * y cuya fecha inicio y fin no esten dentro del rango de fechas � que se traslapen o que los contenga 
	 * @param codigoUsuarioCapitado
	 * @param fechaInicio
	 * @param fechaFin
	 * @return List<DtoContrato>
	 * @author Ricardo Ruiz
	 *
	 */
	public List<DtoContrato> obtenerCarguesPreviosPorUsuarioCapitadoPorRangoFechas(long codigoUsuarioCapitado, Date fechaInicio, Date fechaFin);
}
