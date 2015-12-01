/**
 * 
 */
package com.servinte.axioma.dao.interfaz.capitacion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.princetonsa.dto.facturacion.DtoContrato;
import com.servinte.axioma.orm.UsuarioXConvenio;

/**
 * Esta clase se encarga de definir los métodos de negocio
 * para la entidad UsuarioXConvenio
 * @author Cristhian Murillo
 *
 */
public interface IUsuarioXConvenioDAO 
{

	/**
	 * Lista todos
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<UsuarioXConvenio> listarTodos();
	
	
	/**
	 * attachDirty
	 * @param instance
	 */
	public void attachDirty(UsuarioXConvenio instance);
	
	/**
	 * 
	 * Este Método se encarga de consultar un registro de la tabla 
	 * usuario_x_convenio a partir del código del paciente
	 * @param int codigoPersona
	 * @return UsuarioXConvenio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public UsuarioXConvenio buscarUsuarioConvenioPorPaciente(int codigoPersona);
	
	
	/**
	 * 
	 * Este Método se encarga de consultar el registro mas reciente en la tabla para
	 * retornar los datos a postular
	 * @param int codigoPersona
	 * @return UsuarioXConvenio
	 * @author Ricardo Ruiz
	 *
	 */
	public UsuarioXConvenio buscarUsuarioConvenioRecientePorPaciente(int codigoPersona);
	
	
	/**
	 * 
	 * Este Método se encarga de obtener los registros para una persona
	 * y cuya fecha inicio y fin no esten dentro del rango de fechas ó que se traslapen o que los contenga 
	 * @param codigoPersona
	 * @param fechaInicio
	 * @param fechaFin
	 * @return List<DtoContrato>
	 * @author Ricardo Ruiz
	 *
	 */
	public List<DtoContrato> obtenerCarguesPreviosPorPersonaPorRangoFechas(int codigoPersona, Date fechaInicio, Date fechaFin);
	
	/**
	 * 
	 * Este Método se encarga de validar si existe un cargue vigente para un contrato
	 * y y una persona
	 * @param codigoPersona
	 * @param fechaActual
	 * @param codigoContrato
	 * @return Boolean
	 * @author Ricardo Ruiz
	 *
	 */
	public boolean existeCargueVigentePorPersonaPorContrato(int codigoPersona, Date fechaActual, int codigoContrato);

}
