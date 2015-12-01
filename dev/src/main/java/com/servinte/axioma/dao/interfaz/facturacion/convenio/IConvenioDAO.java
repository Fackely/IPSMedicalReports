package com.servinte.axioma.dao.interfaz.facturacion.convenio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.princetonsa.dto.facturacion.DTOFacturasConvenios;
import com.princetonsa.dto.facturacion.DtoContrato;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.facturacion.DtoReporteValoresFacturadosPorConvenio;
import com.servinte.axioma.dao.interfaz.IBaseDAO;
import com.servinte.axioma.orm.Convenios;


/**
 * Esta clase se encarga de definir los métodos de
 * negocio para la entidad Convenios
 * 
 * @author Edgar Carvajal
 *
 */
public interface IConvenioDAO extends IBaseDAO<Convenios> {
	
	/**
	 * Método para lista convenios Odontológicos
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public List<DtoConvenio> listaConveniosOdontologicos(DtoConvenio dto);
	
	
	/**
	 * 
	 * Este Método se encarga de consultar todos los convenios
	 * que manejen montos. 
	 * 
	 * @return ArrayList<DtoConvenio>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DtoConvenio> obtenerConveniosManejanMonto();
	
	
	/**
	 * 
	 * Este Método se encarga de consultar un convenio
	 * por su ID
	 * 
	 * @param int
	 * @return DtoConvenio 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DtoConvenio buscarConvenio(int id);

	
	/**
	 * Listar los contratos asignados al convenio
	 * @param convenioTarjeta
	 * @return Lista de contratos
	 */
	public ArrayList<DtoContrato> listarContratosConvenio(DtoConvenio convenio);
	
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.orm.ConveniosHome#findById(int)
	 */
	public Convenios findById(int id);
	
	
	/**
	 * Lista los convenios activos del paciente
	 * @param codPaciente
	 * @param acronimoEstadoActivo
	 * @return Lista de {@link DtoConvenio} con los convenios asociados al paciente en estado activo
	 */
	public ArrayList<DtoConvenio> listarConveniosPaciente(int codPaciente, char acronimoEstadoActivo);
	
	
	/**
	 * Lista los convenios activos e inactivos asociados a la institución.
	 * Tipo Atención = Odontológica
	 * Es convenio tarjeta cliente = NO o isNull
	 * @param codInstitucion
	 * @return ArrayList<Convenios>
	 * @author Diana Carolina G
	 */
	public ArrayList<Convenios> listarConveniosActivosInactivosOdont(int codInstitucion);


	/**
	 * Retorna el convenio (unique result) del detalle del cargo asociado a una solicitud enviada.
	 *
	 * @author Cristhian Murillo
	 * @param numeroSolicitud
	 * @return ArrayList<Convenios>
	*/
	public ArrayList<Convenios> obtenerConvenioDetCargoPorSolicitud(int numeroSolicitud, int codInstitucion);
	

	/**
	 * Retorna los convenios activos asociados al ingreso paciente y filtrados por institución
	 * @param codInstitucion
	 * @param codIngreso
	 * @return ArrayList<Convenios>
	 */
	public ArrayList<Convenios> obtenerConveniosPorIngresoPaciente(int codInstitucion, int codIngreso);

	
	/**
	 * Lista los convenios activos e inactivos de una empresa seleccionada
	 * cuando el c&oacute;digo de la empresa es v&aacute;lido o todos los convenios
	 * cuando el c&oacute;digo de la empresa no es v&aacute;lido.
	 * 
	 * @param codigoEmpresa
	 * @return ArrayList<Convenios>
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<Convenios> listarConveniosPorEmpresa(int codigoEmpresa);

	
	/**
	 * M&eacute;todo encargado de obtener los valores facturados por convenios.
	 * 
	 * @param dto
	 * @return ArrayList<DTOFacturasConvenios>
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<DTOFacturasConvenios> obtenerValoresFacturadosConvenio(
			DtoReporteValoresFacturadosPorConvenio dto);

	
	
	/**
	 * Lista los convenios activos asociados a la institución.
	 * 
	 * @author Cristhian Murillo
	 * @param codInstitucion
	 * @return ArrayList<Convenios>
	 */
	public ArrayList<Convenios> listarConveniosActivosPorInstitucion(int codInstitucion);
	
	
	
	/**
	 * Lista los convenios Capitados activos asociados a la institución.
	 * 
	 * @author Cristhian Murillo
	 * @param codInstitucion
	 * @return ArrayList<Convenios>
	 */
	public ArrayList<Convenios> listarConveniosCapitadosActivosPorInstitucion(int codInstitucion);
	
	
	/**
	 * Obtiene el codigo del convenio, asociado al usuario capitado en el momento
	 * de realizar una autorizaci&oacute;n de ingreso estancia
	 * @param codPaciente
	 * @return ArrayList<Convenios>
	 * @author Diana Carolina G
	 */
	public ArrayList<Convenios>  obtenerConvenioPorUsuarioCapitado(int codPaciente);

/**
	 * M&eacute;todo encargado de obtener el tipo de contrato asociado 
	 * a un convenio en espec&iacute;fico
	 * @param codigo
	 * @return DtoConvenio
	 * @author Diana Carolina G
	 */
	public DtoConvenio obtenerTipoContratoConvenio(int codigo);


	/**
	 * Lista los convenios activos/inactivos asociados a la institución.
	 * 
	 * @author Ricardo Ruiz
	 * @param codInstitucion
	 * @return ArrayList<Convenios>
	 */
	public ArrayList<Convenios> listarConveniosPorInstitucion(int codInstitucion);
	
	
	
	/**
	 * Lista los convenios Capitados activos/inactivos asociados a la institución.
	 * 
	 * @author Ricardo Ruiz
	 * @param codInstitucion
	 * @return ArrayList<Convenios>
	 */
	public ArrayList<Convenios> listarConveniosCapitadosPorInstitucion(int codInstitucion);

	/**
	 * Lista los convenios activos en el sistema
	 *  
	 * @return ArrayList<Convenios>
	 * @author Camilo Gómez
	 */
	public ArrayList<Convenios> listarConveniosActivos();
	
	/**
	 * Lista todos los convenios en el sistema
	 *  
	 * @return ArrayList<Convenios>
	 * @author Camilo Gómez
	 */
	public ArrayList<Convenios> listarConvenios();
	
	/**
	 * Lista los convenios Capitados activos/inactivos asociados a la institución y que tengan una
	 * parametrización de presupuesto. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codInstitucion
	 * @param esCapitacionSubcontratada
	 * @param mesAnio
	 * @return ArrayList<Convenios>
	*/
	public ArrayList<Convenios> listarConveniosConParametrizacionPresupuestoPorInstitucionPorCapitacion(int codInstitucion, char esCapitacionSubcontratada, Calendar mesAnio);
	
	/**
	 * Lista los convenios Capitados activos asociados a la institución que manejen presupuesto.
	 * 
	 * @author Ricardo Ruiz
	 * @param codInstitucion
	 * @return ArrayList<Convenios>
	*/
	public ArrayList<Convenios> listarConveniosCapitadosActivosPorInstitucionManejaPresupuesto(int codInstitucion);
		
	/**
	 * Lista todos los convenios Capitados activos asociados a la institución.
	 * 
	 * @author Cesar Gomez
	 * @param codInstitucion
	 * @return ArrayList<DtoConvenio>
	*/
	public ArrayList<DtoConvenio> listarTodosConveniosCapitadosPorInstitucion(int codInstitucion);
		
}
