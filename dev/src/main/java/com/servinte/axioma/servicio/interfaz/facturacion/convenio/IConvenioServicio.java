package com.servinte.axioma.servicio.interfaz.facturacion.convenio;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.facturacion.DTOFacturasConvenios;
import com.princetonsa.dto.facturacion.DtoContrato;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.facturacion.DtoReporteValoresFacturadosPorConvenio;
import com.servinte.axioma.orm.Convenios;


/**
 * Esta clase se encarga de definir los métodos de
 * negocio para la entidad Convenios
 * 
 * @author Edgar Carvajal Ruiz
 *
 */
public interface IConvenioServicio {
	
	
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
	 * Buscar los contratos pertenecientes a un convenio
	 * pasado por parámetros
	 * @param convenio DtoConvenio con el código del convenio asignado
	 * @author Juan David Ramírez
	 * @since 09 Septiembre 2010
	 */
	public ArrayList<DtoContrato> listarContratosConvenio(DtoConvenio convenio);
	
	
	/**
	 * Carga los convenios parametrizados por defecto en el sistema
	 * @author Cristhian Murillo
	 * @return ArrayList<Convenios>
	 */
	public ArrayList<Convenios> cargarConveniosParametrizadosPorDefecto();

	
	
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
	 * 
	 * Este Método se encarga de consultar un convenio por su id
	 * 
	 * @param int id
	 * @returnConvenios
	 * @author, Angela Maria Aguirre
	 *
	 */
	public Convenios findById(int id);
	
	

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
	
	
	
}
