/**
 * 
 */
package com.servinte.axioma.mundo.interfaz.odontologia.presupuesto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionErrors;

import util.ResultadoBoolean;
import util.odontologia.InfoInclusionExclusionBoca;
import util.odontologia.InfoInclusionExclusionPiezaSuperficie;

import com.princetonsa.dto.odontologia.DtoContratarInclusion;
import com.princetonsa.dto.odontologia.DtoEncabezadoInclusion;
import com.princetonsa.dto.odontologia.DtoPresuOdoProgServ;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologicoDescuento;
import com.princetonsa.dto.odontologia.DtoPresupuestoTotalConvenio;
import com.princetonsa.dto.odontologia.DtoRegistroContratarInclusion;
import com.princetonsa.dto.odontologia.DtoRegistroGuardarExclusion;
import com.princetonsa.dto.odontologia.DtoTotalesContratarInclusion;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.orm.ExcluPresuEncabezado;
import com.servinte.axioma.orm.IncluPresuEncabezado;

/**
 * @author Juan David Ramírez
 * @since Dec 29, 2010
 */
public interface IPresupuestoExclusionesInclusionesMundo
{

	/**
	 * Seleccionar o desseleccionar un programa y realizar los cálculos necesarios
	 * @param registrosContratarInclusion lista de registros de inclusión
	 * @param indexRegInclusion Índice de la inclusión
	 * @param indexConvenio Índice del convenio
	 * @param checkContratado Indica si el programa fue seleccionado o desseleccionado
	 */
	public void seleccionarPrograma(ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion, int indexRegInclusion, int indexConvenio, boolean checkContratado);

	/**
	 * Seleccionar o deseleccionar una promocion
	 * @param registrosContratarInclusion lista de registros de inclusión
	 * @param indexRegInclusion Índice de la inclusión
	 * @param indexConvenio Índice del convenio
	 * @param checkContratado Indica si el programa fue seleccionado o desseleccionado
	 */
	public void seleccionarPromocion(ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion, int indexRegInclusion, int indexConvenio, boolean checkPromocion);

	/**
	 * Calcula los totales de los convenios según los programas seleccionado
	 * @param registrosContratarInclusion Lista de registros para ser evaluados
	 * @param listaSumatoriaConvenios Lista de los totales de los convenios
	 * @param totalesContratarInclusion Totales calculados de  las inclusiones a contratar
	 */
	public void calcularTotalesConvenios(ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion, ArrayList<DtoPresupuestoTotalConvenio> listaSumatoriaConvenios, DtoTotalesContratarInclusion totalesContratarInclusion) throws IPSException;

	/**
	 * Verifica si los valores de los programas de los
	 * arreglos de valores del presupuesto son iguales
	 * @param registrosContratarInclusion
	 * @param registrosContratarInclusionClon
	 * @return true en caso de que sean iguales, false de lo contrario
	 */
	public boolean verificarSonIgualesValoresConvenios(ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion, ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusionClon);

	/**
	 * Seleccionar todos los programas de un convenio
	 * @param registrosContratarInclusion Lista de registros para ser evaluados
	 * @param indexConvenio Índice del convenio
	 * @param checkPropiedadPrograma Indica si el programa fue seleccionado o desseleccionado
	 */
	public void seleccionarTodosProgramasConvenio(ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion, int indexConvenio, boolean checkSelectTodosConvenio);
	
	

//	/**
//	 * Método que realiza todas las validaciones necesarias para permitir la contratación
//	 * de las inclusiones realizadas al presupuesto del paciente.
//	 * 
//	 * @param errores
//	 * @param registrosContratarInclusion
//	 * @param listaSumatoriaConvenios
//	 * @param codigoInstitucion
//	 * @param codigoPlanTratamiento
//	 * @param codigoIngreso
//	 * @return
//	 */
//	public ArrayList<DtoValorAnticipoPresupuesto> validarGuardarContratar(ActionErrors errores, 
//			ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion,  
//			ArrayList<DtoPresupuestoTotalConvenio> listaSumatoriaConvenios,
//			int codigoInstitucion, BigDecimal codigoPlanTratamiento,  int codigoIngreso);
	
	
//	/**
//	 * Método que se encarga de realizar todo el proceso necesario para la contratación
//	 * de las inclusiones.
//	 * 
//	 * @param errores
//	 * @param paciente
//	 * @param registrosContratarInclusion
//	 * @param listaSumatoriaConvenios
//	 * @param codigoInstitucion
//	 * @param codigoPlanTratamiento
//	 * @param loginUsuario
//	 * @param con
//	 * @param codigoPresupuesto
//	 * @return
//	 */
//	public ResultadoBoolean contratarInclusiones (ActionErrors errores, PersonaBasica paciente,
//			ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion,  
//			ArrayList<DtoPresupuestoTotalConvenio> listaSumatoriaConvenios,
//			int codigoInstitucion, BigDecimal codigoPlanTratamiento, String loginUsuario, 
//			Connection con, BigDecimal codigoPresupuesto);
//	
//	
	
	/**
	 * Método que centraliza todo el proceso para guardar las exclusiones seleccionadas
	 *
	 * @param errores
	 * @param listadoRegistroGuardarExclusion
	 * @param con
	 * @param loginUsuario
	 * @param codigoPresupuesto
	 * @param codigoInstitucion
	 * @param codigoCentroAtencion
	 * @return
	 */
	public ResultadoBoolean guardarExclusiones( ActionErrors errores, ArrayList<DtoRegistroGuardarExclusion> listadoRegistroGuardarExclusion,
												Connection con, String loginUsuario, BigDecimal codigoPresupuesto, 
												int codigoInstitucion,  int codigoCentroAtencion);
	
	
	/**
	 * Método que consulta los registros de Exclusión realizados al presupuesto del paciente
	 * 
	 * @param codigoPresupuesto
	 * @return 
	 */
	public List<ExcluPresuEncabezado> cargarRegistrosExclusion (long codigoPresupuesto);
	
	/**
	 * 
	 * Método que permite guardar la información del proceso de contratación y precontratación
	 * de las inclusiones realizadas al plan de tratamiento y al presupuesto del paciente
	 * 
	 * @param dtoContratarInclusion
	 * @return true  = proceso sin errores,  false = proceso con errores o con advertencias y notificaciones al proceso
	 */
	public ResultadoBoolean guardarInclusiones(DtoContratarInclusion dtoContratarInclusion) throws IPSException;
	

	/**
	 * Método que consulta las inclusiones asociadas al presupuesto activo del paciente
	 * 
	 * @param codigoPresupuesto
	 */
	public ArrayList<DtoEncabezadoInclusion> cargarRegistrosInclusion(long codigoPresupuesto);
	
	
	/**
	 * 
	 * Método que consulta un registro del proceso de contratación de Inclusiones
	 * 
	 * @param codigoIncluPresuEncabezado
	 * @param inclusionesPiezas
	 * @param inclusionesBoca
	 * @param usuario
	 * @return
	 */
	public ArrayList<DtoRegistroContratarInclusion> cargarDetalleRegistroInclusion (long codigoIncluPresuEncabezado,
			ArrayList<InfoInclusionExclusionPiezaSuperficie> inclusionesPiezas,	ArrayList<InfoInclusionExclusionBoca> inclusionesBoca, UsuarioBasico usuario);
	
	
	/**
	 * Método que se encarga de transformar la información de los listados de Exclusiones Pieza Dental y Boca
	 * en un solo listado de {@link DtoPresuOdoProgServ}
	 * 
	 * @param exclusionesPiezas
	 * @param exclusionesBoca
	 * @param loginUsuario
	 * @param codigoInstitucion
	 * @return
	 */
	public ArrayList<DtoRegistroGuardarExclusion> obtenerListadoExclusiones (ArrayList<InfoInclusionExclusionPiezaSuperficie> exclusionesPiezas, 
			ArrayList<InfoInclusionExclusionBoca> exclusionesBoca, String loginUsuario, int codigoInstitucion);
	
	
	/**
	 * Método que se encarga de transformar la información de los listados de Inclusión Pieza Dental y Boca
	 * en un solo listado de {@link DtoPresuOdoProgServ}
	 * 
	 * @param inclusionesPiezas
	 * @param inclusionesBoca
	 * @param loginUsuario
	 * @param codigoInstitucion
	 * @return
	 */
	public ArrayList<DtoPresuOdoProgServ> obtenerListadoInclusiones (ArrayList<InfoInclusionExclusionPiezaSuperficie> inclusionesPiezas, 
			ArrayList<InfoInclusionExclusionBoca> inclusionesBoca, String loginUsuario, int codigoInstitucion);
	
	
	/**
	 * Método que realiza el proceso de actualización de un registro de 
	 * Inclusión precontratada
	 * 
	 * @param incluPresuEncabezado
	 * @return true en caso de actualizar correctamente el registro, false de lo contrario
	 */
	public boolean actualizarIncluPresuEncabezado (IncluPresuEncabezado incluPresuEncabezado);
	 
	 
	 /**
	 * Generar una solicitud de descuento
	 * @param dtoDcto DTO con los datos del descuento
	 * @return true en caso de proceso exitoso, false de lo contrario
	 */
	public boolean generarSolicitudDescuento(DtoPresupuestoOdontologicoDescuento dtoDcto);

	/**
	 * Relacionar la solicitud de descuento con las inclusiones
	 * @param dtoDcto Dto con los datos del descuento
	 * @param encabezado Encabezado de la inclusión
	 * @return true en caso de ingresar correctamente, false de lo contrario
	 */
	public boolean relacionarConInclusiones(DtoPresupuestoOdontologicoDescuento dtoDcto, IncluPresuEncabezado encabezado);
	

	/**
	 * Método que carga un encabezado de Inclusión específico
	 * 
	 * @param codigoPresupuesto
	 */
	public IncluPresuEncabezado cargarEncabezadoInclusion(long codigoIncluPresuEncabezado);

	/**
	 * Calcula el valor total de la inclusión
	 * @param codigoPkEncabezadoInclusion Llave primaria del encabezado de la inclusión
	 * @param codigoIngreso Código del ingreso del paciente
	 * @return Valor total de la inclusión
	 */
	public DtoTotalesContratarInclusion calcularTotalInclusion(long codigoPkEncabezadoInclusion, int codigoIngreso) throws IPSException;

	/**

	 * Método que permite obtener los convenios que estan relacionados con el 
	 * registro de inclusión
	 * 
	 * @param registrosInclusionDetalle
	 * @param codigoIngreso
	 * @return
	 */
	public ArrayList<DtoPresupuestoTotalConvenio> obtenerListadoSumatoriaConvenios(ArrayList<DtoRegistroContratarInclusion> registrosInclusionDetalle, int codigoIngreso);

	/**
	 * Eliminar todo el detalle de la inclusión sin eliminar el encabezado
	 * ya que no se puede perder el consecutivo asignado
	 * @param encabezadoInclusionPresupuesto Llave primaria del encabezado (Tabla inclu_presu_encabezado, Columna codigo_pk)
	 * @return true en caso de guardar correctamente, false de lo contrario
	 */
	public boolean eliminarDetalleInclusiones(long encabezadoInclusionPresupuesto);
	
	

 	/**
	 * Método que anula la solicitud de descuento Odontológico
	 * asociada al encabezado de inclusión 
	 * 
	 * @param codigoPresupuestoDctoOdon
	 * @param loginUsuario
	 * @param codigoInstitucion
	 * @return {@link ResultadoBoolean} true en caso de realizar la modificación, false en caso contrario
	 */
	public ResultadoBoolean anularSolicitudDescuentoOdontologico (BigDecimal codigoPresupuestoDctoOdon, String loginUsuario, int codigoInstitucion);
	
}
