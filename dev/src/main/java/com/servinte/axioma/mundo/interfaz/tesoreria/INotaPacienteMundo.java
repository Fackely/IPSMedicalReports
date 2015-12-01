package com.servinte.axioma.mundo.interfaz.tesoreria;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.princetonsa.actionform.tesoreria.NotasPacientesForm;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.servinte.axioma.dto.tesoreria.DTONotaPaciente;
import com.servinte.axioma.dto.tesoreria.DtoBusquedaNotasPacientePorRango;
import com.servinte.axioma.dto.tesoreria.DtoConceptoNotasPacientes;
import com.servinte.axioma.dto.tesoreria.DtoConsultaNotasDevolucionAbonosPacientePorRango;
import com.servinte.axioma.dto.tesoreria.DtoInfoIngresoPacienteControlarAbonoPacientes;
import com.servinte.axioma.dto.tesoreria.DtoNotasPorNaturaleza;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.NotaPaciente;
import com.servinte.axioma.orm.Pacientes;
import com.servinte.axioma.orm.Usuarios;

/**
 * Define la logica de negocio relacionada con 
 * Notas de Paciente
 * @author diecorqu
 * 
 */
public interface INotaPacienteMundo {

	/**
	 * Método encargado de persistir la entidad NotaPaciente
	 * @param NotaPaciente
	 * @return boolean - resultado de la operación
	 */
	public boolean guardarNotaPaciente(NotaPaciente notaPaciente);
	
	/**
	 * Método encargado de eliminar un registro de NotaPaciente
	 * @param NotaPaciente
	 * @return boolean - resultado de la operación
	 */
	public boolean eliminarNotaPaciente(NotaPaciente notaPaciente);
	
	/**
	 * Método encargado de modificar la entidad NotaPaciente
	 * @param NotaPaciente
	 * @return NotaPaciente
	 */
	public NotaPaciente modificarNotaPaciente(NotaPaciente notaPaciente);
	
	/**
	 * Método encargado de buscar una NotaPaciente por codigo
	 * @param codigo
	 * @return NotaPaciente
	 */
	public NotaPaciente findById(long codigo);
	
	/**
	 * Lista los centros de atención activos en el sistema
	 * @return ArrayList<CentroAtencion>
	 */
	public ArrayList<CentroAtencion> obtenerCentrosAtencionInstitucion();
	
	/**
	 * Obtiene el centro de atencion por el codigo
	 * @param codigo
	 * @return CentroAtencion
	 */
	public CentroAtencion obtenerCentroAtencion(int codigo);
	
	/**
	 * Obtiene el centro de atencion asociados al usuario
	 * @param loginUsuario
	 * @return CentroAtencion
	 */
	public ArrayList<CentroAtencion> obtenerCentrosAtencionActivosUsuario(String loginUsuario);
	
	/**
	 * Inserta en movimientos abonos las devoluciones de abono del paciente
	 */
	public int insertarMovimientoAbonos(Connection con,int codigoPaciente,int codigoDocumento,int tipoDocumento,double valor,int institucion, Integer ingreso, int codigoCentroAtencion);

	
	/**
	 * Este Método se encarga de obtener las devoluciones de 
	 * abonos filtrada por paciente y por consecutivo y el parametro general ManejoEspecialInstiOdontologicas
	 * @param codigoPaciente,consecutivo,parametroManejoEspecialInstiOdontologicas
	 * @return ArrayList<DTONotaPaciente>
	 */
	
	public ArrayList<DTONotaPaciente> obtenerDevolucionAbonoPacienteConsecutivo(int codigoPaciente, BigDecimal consecutivo, String naturaleza, String parametroManejoEspecialInstiOdontologicas);
	
	/**
	 * Retorna los ingresos de un paciente.
	 * El parametro General Controlar Abono Pacientes X Ingreso define si se debe mostrar detallado cada uno de los 
	 * ingresos con su valor o si se debe listar el totalizado de estos para el paciente dado
	 * 
	 * @param codPaciente
	 * @param parametroGeneralControlarAbonoPacientesXIngreso
	 * @return
	 */
	public  ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes> cargarIngresosPorPaciente(int codigoPaciente, String parametroGeneralControlarAbonoPacientesXIngreso);
	
	/**
	 * Retorna el abonos disponible de un paciente.
	 * 
	 * @param codPaciente
	 * @return
	 */
	public  ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes> cargarAbonoDisponiblePorPaciente(int codigoPaciente);
	
	/**
	 * Método que valida si tiene movimientos abonos
	 * 
	 * @param usuario
	 * @param institucion
	 * @param parametros
	 * @return
	 */
	public String validarSiTieneMovimientosAbonos (int codigoPaciente);
	
	/**
	 * Este Método se encarga de obtener las devoluciones de 
	 * abonos realizadas por institución.
	 * @param codigoInstitucion
	 * @return
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DTONotaPaciente> obtenerDevolucionAbonoPorInstitucion(int codigoInstitucion);

		/**
	 * Método encargado de obtener el listado de la consulta de notas de
	 * devolución abonos paciente.
	 * 
	 * @param dtoFiltro
	 * @param listaConsecutivosCA
	 * @return listaInstitucion
	 * 
	 * @author Luis Fernando Hincapié Ospina
	 */
	public ArrayList<DtoConsultaNotasDevolucionAbonosPacientePorRango> buscarNotaDevolucionAbonosPacienteRango(
			DtoConsultaNotasDevolucionAbonosPacientePorRango dtoFiltro,
			ArrayList<Integer> listaConsecutivosCA);

	

	/**
	 * Guarda en cascada en las tablas Devolucion Abono y Detalle Devolucion Abono
	 * 
	 * @param 
	 * @return S/N
	 */
	
	public boolean guardarDevolucionAbono (BigDecimal consecutivo,  
			   String observaciones, Pacientes paciente, Usuarios usuario, 
			   CentroAtencion centroAtencionOrigen, CentroAtencion centroAtencionRegistro,
			   String naturalezaNota, DtoConceptoNotasPacientes dtoConceptoNota,
			   ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes> dtoInfoIngresoPacienteControlarAbonoPacientes);
	
	/**
	 * Método encargado de obtener la lista de Concepto Notas Pacientes
	 * por Naturaleza de la Nota ya sea Débito o Crédito
	 * @param naturalezaNota
	 * @return ArrayList<DtoConceptoNotasPacientes>
	 */
	public ArrayList<DtoConceptoNotasPacientes> listarConceptoNotaPacientexNaturaleza(String naturalezaNota);
	
	/**
	 * Método encargado de obtener la lista de Concepto Notas Pacientes
	 * por Naturaleza de la Nota ya sea Débito o Crédito y estado activo = S
	 * @param naturalezaNota
	 * @return ArrayList<DtoConceptoNotasPacientes>
	 */
	public ArrayList<DtoConceptoNotasPacientes> listarConceptoNotaPacientexNaturalezaEstadoActivo(String naturalezaNota);
	
	/**
	 * Método encargado de obtener la lista de todos los Concepto Notas Pacientes
	 * existentes
	 * @param naturalezaNota
	 * @return ArrayList<DtoConceptoNotasPacientes>
	 */
	public ArrayList<DtoConceptoNotasPacientes> listarConceptosNotaPaciente();
	
	/**
	 * Método que permite consultar las Notas de Paciente por Rango
	 * @param DtoBusquedaNotasPacientePorRango
	 * @return HashMap<String, ArrayList<DtoResumenNotasPaciente>>
	 */
	public LinkedHashMap<String, DtoNotasPorNaturaleza> consultarNotasPacientePorRango(
			DtoBusquedaNotasPacientePorRango dtoBusqueda, 
			boolean esInstitucionMultiempresa, 
			String nombreInstitucion, 
			boolean controlaAbonoPacientePorNumIngreso,
			boolean manejoEspecialInstOdonto,
			NotasPacientesForm forma);
	
	/**
	 * 
	 * @param consecutivoNota
	 * @return
	 */
	public DTONotaPaciente obtenerNotaPacienteConsecutivo(long consecutivoNota, 
			boolean controlaAbonoPacientePorNumIngreso);
	
	/**
	 * Método que permite consultar las Notas de Paciente por Paciente
	 * @param long codigoPaciente
	 * @param boolean esMultiInstitucion
	 * @return LinkedHashMap<String, DtoNotasPorNaturaleza>
	 */
	public LinkedHashMap<String, DtoNotasPorNaturaleza> consultarNotasPacientePorPaciente(int codigoPaciente, 
			boolean esInstitucionMultiempresa, String nombreInstitucion,
			boolean controlaAbonoPacientePorNumIngreso, NotasPacientesForm forma);
	
	/**
	 * Método encargado de obtener el nombre completo del paciente
	 * @param paciente
	 * @return
	 */
	public String obtenerNombrePaciente(Pacientes paciente);
	
	/**
	 * Método encargado de obtener la identificación del paciente
	 * @param paciente
	 * @return
	 */
	public String obtenerIdentificacionPaciente(Pacientes paciente);
	
	/**
	 * Método encargado de obtener los datos de dirección y teléfono para 
	 * el centro atencion dueño del paciente
	 * @param codigoPaciente
	 * @return String Descripcion Centro atención
	 */
	public String obtenerDatosCentroAtencionDuenioPaciente(int codigoCentroAtencion);
	
	/**
	 * Método que permite obterner los datos de la persona
	 * @param codigoPaciente
	 * @return
	 */
	public DtoPersonas obtenerDatosPersona(int codigoPersona);
	
	/**
	 * Método encargado de consultar el ingreso abierto del paciente
	 * @param codigoPaciente
	 * @return
	 */
	public int obtenerIngresoPaciente(int codigoPaciente);
}
