package com.servinte.axioma.dao.interfaz.tesoreria;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.servinte.axioma.dto.tesoreria.DTONotaPaciente;
import com.servinte.axioma.dto.tesoreria.DtoBusquedaNotasPacientePorRango;
import com.servinte.axioma.dto.tesoreria.DtoConsultaNotasDevolucionAbonosPacientePorRango;
import com.servinte.axioma.dto.tesoreria.DtoNotasPorNaturaleza;
import com.servinte.axioma.dto.tesoreria.DtoResumenNotasPaciente;
import com.servinte.axioma.orm.NotaPaciente;

/**
 * Define la logica de negocio relacionada con 
 * Notas de Paciente
 * @author diecorqu
 * 
 */
public interface INotaPacienteDAO {

	/**
	 * M�todo encargado de persistir la entidad NotaPaciente
	 * @param NotaPaciente
	 * @return boolean - resultado de la operaci�n
	 */
	public boolean guardarNotaPaciente(NotaPaciente notaPaciente);
	
	/**
	 * M�todo encargado de eliminar un registro de NotaPaciente
	 * @param NotaPaciente
	 * @return boolean - resultado de la operaci�n
	 */
	public boolean eliminarNotaPaciente(NotaPaciente notaPaciente);
	
	/**
	 * M�todo encargado de modificar la entidad NotaPaciente
	 * @param NotaPaciente
	 * @return NotaPaciente
	 */
	public NotaPaciente modificarNotaPaciente(NotaPaciente notaPaciente);
	
	/**
	 * M�todo encargado de buscar una NotaPaciente por codigo
	 * @param codigo
	 * @return NotaPaciente
	 */
	public NotaPaciente findById(long codigo);
	
	/**
	 * Este M�todo se encarga de obtener las devoluciones de 
	 * abonos filtrada por paciente y por consecutivo y el parametro general ManejoEspecialInstiOdontologicas
	 * @param codigoPaciente,consecutivo,parametroManejoEspecialInstiOdontologicas
	 * @return ArrayList<DTONotaPaciente>
	 */

	public ArrayList<DTONotaPaciente> obtenerDevolucionAbonoPacienteConsecutivo(int codigoPaciente, BigDecimal consecutivo, String naturaleza, String parametroManejoEspecialInstiOdontologicas);
	
	/**
	 * Este M�todo se encarga de obtener las devoluciones de 
	 * abonos realizadas por instituci�n.
	 * @param codigoInstitucion
	 * @return
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DTONotaPaciente> obtenerDevolucionAbonoPorInstitucion(int codigoInstitucion);
	
	/**
	 * M�todo encargado de obtener el listado de la consulta de notas de
	 * devoluci�n abonos paciente.
	 * 
	 * @param dtoFiltro
	 * @param listaConsecutivosCA
	 * @return listaNotasDevolucionAbonosPacienteRango
	 * 
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public ArrayList<DtoConsultaNotasDevolucionAbonosPacientePorRango> buscarNotaDevolucionAbonosPacienteRango(
			DtoConsultaNotasDevolucionAbonosPacientePorRango dtoFiltro,
			ArrayList<Integer> listaConsecutivosCA);
	
	/**
	 * M�todo que permite consultar las Notas de Paciente por Rango
	 * @param DtoBusquedaNotasPacientePorRango
	 * @return ArrayList<DtoNotasPacientePorRango>
	 */
	public ArrayList<DtoResumenNotasPaciente> consultarNotasPacientePorRango(
			DtoBusquedaNotasPacientePorRango dtoBusqueda, 
			boolean esInstitucionMultiempresa, 
			boolean controlaAbonoPacientePorNumIngreso);
	
	/**
	 * M�todo que permite consultar las Notas de Paciente por Paciente
	 * @param long codigoPaciente
	 * @param boolean esMultiInstitucion
	 * @return ArrayList<DtoNotasPacientePorRango>
	 */
	public ArrayList<DtoResumenNotasPaciente> consultarNotasPacientePorPaciente(int codigoPaciente, 
			boolean esMultiInstitucion, 
			boolean controlaAbonoPacientePorNumIngreso);
}
