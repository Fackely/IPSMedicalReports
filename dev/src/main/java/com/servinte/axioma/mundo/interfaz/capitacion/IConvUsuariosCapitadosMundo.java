package com.servinte.axioma.mundo.interfaz.capitacion;

import java.util.Date;
import java.util.List;

import com.princetonsa.dto.facturacion.DtoContrato;
import com.servinte.axioma.orm.ConvUsuariosCapitados;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * para la entidad ConvUsuariosCapitados
 * @author Ricardo Ruiz
 *
 */
public interface IConvUsuariosCapitadosMundo {

	
	/**
	 * attachDirty
	 * @param instance
	 */
	public void attachDirty(ConvUsuariosCapitados  instance);
	
	/**
	 * 
	 * Este Método se encarga de consultar el registro más reciente en la tabla para
	 * retornar los datos a postular
	 * @param codigoUsuarioCapitado
	 * @return ConvUsuariosCapitados
	 * @author Ricardo Ruiz
	 *
	 */
	public ConvUsuariosCapitados buscarConvUsuariosCapitadosRecientePorUsuarioCapitado(long codigoUsuarioCapitado);
	
	
	/**
	 * 
	 * Este Método se encarga de obtener los registros para un usuario capitado
	 * y cuya fecha inicio y fin no esten dentro del rango de fechas ó que se traslapen o que los contenga 
	 * @param codigoUsuarioCapitado
	 * @param fechaInicio
	 * @param fechaFin
	 * @return List<DtoContrato>
	 * @author Ricardo Ruiz
	 *
	 */
	public List<DtoContrato> obtenerCarguesPreviosPorUsuarioCapitadoPorRangoFechas(long codigoUsuarioCapitado, Date fechaInicio, Date fechaFin);
}
