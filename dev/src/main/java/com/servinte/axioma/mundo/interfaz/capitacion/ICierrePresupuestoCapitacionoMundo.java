/**
 * 
 */
package com.servinte.axioma.mundo.interfaz.capitacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.princetonsa.dto.capitacion.DtoLogCierrePresuCapita;
import com.princetonsa.dto.capitacion.DtoMesesTotalServiciosArticulosValorizadosPorConvenio;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoTotalProcesoPresupuestoCapitado;
import com.servinte.axioma.dto.capitacion.DtoTotalProceso;
import com.servinte.axioma.orm.Articulo;
import com.servinte.axioma.orm.CierreNivelAteClasein;
import com.servinte.axioma.orm.CierreNivelAteGruServ;
import com.servinte.axioma.orm.ClaseInventario;
import com.servinte.axioma.orm.GruposServicios;
import com.servinte.axioma.orm.LogCierrePresuCapita;
import com.servinte.axioma.orm.Servicios;

/**
 * @author Cristhian Murillo
 *
 */
public interface ICierrePresupuestoCapitacionoMundo 
{
	
	/**
	 * Lista todos los cierres de presupuesto según los parametros
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> obtenerCierresPresupuestocapitacion(DtoTotalProcesoPresupuestoCapitado parametros);

	


	/**
	 * Guarda o actualiza la isntancia
	 * @param instance
	 *
	 * @autor Cristhian Murillo
	 */
	public void guardarActualizarCierreNivelAteClasein(CierreNivelAteClasein instance);


	/**
	 * Guarda o actualiza la isntancia
	 * @param instance
	 *
	 * @autor Cristhian Murillo
	 */
	public void guardarActualizarCierreNivelAteGruServ(CierreNivelAteGruServ instance);

	/**
	 * Guarda o actualiza la isntancia
	 * @param instance
	 *
	 * @autor Cristhian Murillo
	 */
	public void guardarActualizarLogCierrePresuCapita(LogCierrePresuCapita instance);
	
	/**
	 * Lista todos según los parametros
	 * @param parametros
	 * @return ArrayList<DtoLogCierrePresuCapita>
	 *
	 * @autor Cristhian Murillo
	 */
	public ArrayList<DtoLogCierrePresuCapita> obtenerLogs(DtoProcesoPresupuestoCapitado parametros);
	

	/**
	 * Obtiene la sumatoria de los valores totales de los servicios de un proceso
	 * para un rango de fechas determinado para un nivel de atención. 
	 * 
	 * @author Ricardo Ruiz
	 * @param consecutivoNivel
	 * @param codigoContrato
	 * @param proceso
	 * @param fechaInicio
	 * @param fechaFin
	 * @return ArrayList<Convenios>
	*/
	public List<DtoTotalProceso> obtenerTotalServiciosPorNivelPorProceso(int codigoContrato, long consecutivoNivel, String proceso,
			Date fechaInicio, Date fechaFin);
	
	/**
	 * Obtiene la sumatoria de los valores totales de los articulos de un proceso
	 * para un rango de fechas determinado para un nivel de atención. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param proceso
	 * @param fechaInicio
	 * @param fechaFin
	 * @return ArrayList<Convenios>
	*/
	public List<DtoTotalProceso> obtenerTotalArticulosPorNivelPorProceso(int codigoContrato, long consecutivoNivel, String proceso,
										Date fechaInicio, Date fechaFin);
	
	
	/**
	 * Obtiene la sumatoria de los valores totales de las clases de inventario de un proceso
	 * para un rango de fechas determinado para un nivel de atención. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param codigoClaseInventario
	 * @param proceso
	 * @param fechaInicio
	 * @param fechaFin
	 * @return ArrayList<Convenios>
	*/
	public List<DtoTotalProceso> obtenerTotalArticulosPorNivelPorClasePorProceso(int codigoContrato, long consecutivoNivel, 
			int codigoClaseInventario, String proceso, Date fechaInicio, Date fechaFin);
	
	
	/**
	 * Obtiene la sumatoria de los valores totales de los grupos de servicios de un proceso
	 * para un rango de fechas determinado para un nivel de atención. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param codigoGrupoServicio
	 * @param proceso
	 * @param fechaInicio
	 * @param fechaFin
	 * @return ArrayList<Convenios>
	*/
	public List<DtoTotalProceso> obtenerTotalServiciosPorNivelPorGrupoPorProceso(int codigoContrato, long consecutivoNivel, 
			int codigoGrupoServicio, String proceso, Date fechaInicio, Date fechaFin);
	
	
	
	/**
	 * Elimina la isntancia
	 * @param persistentInstance
	 */
	public void eliminarCierreNivelAteClasein(CierreNivelAteClasein persistentInstance);
	
	
	
	/**
	 * Lista por parametros
	 * 
	 * @param parametros
	 * 
	 * @author Cristhian Murillo
	 * @return ArrayList<CierreNivelAteClasein>
	 */
	public ArrayList<CierreNivelAteClasein> obtenerCierresCierreNivelAteClasein(DtoTotalProcesoPresupuestoCapitado parametros);
	
	
	
	/**
	 * Elimina la isntancia
	 * @param persistentInstance
	 */
	public void eliminarCierreNivelAteGruServ(CierreNivelAteGruServ persistentInstance) ;
	
	
	
	/**
	 * Lista por parametros
	 * 
	 * @param parametros
	 * 
	 * @author Cristhian Murillo
	 * @return ArrayList<CierreNivelAteGruServ>
	 */
	public ArrayList<CierreNivelAteGruServ> obtenerCierresCierreNivelAteGruServ(DtoTotalProcesoPresupuestoCapitado parametros);
	
	/**
	 * Obtiene el consolidado por cada mes de la sumatoria de los valores totales de los servicios de un proceso
	 * para un nivel de atención para cada mes. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoConvenio
	 * @param consecutivoNivel
	 * @param proceso
	 * @param List<Calendar> meses
	 * @return ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>
	*/
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalServiciosPorNivelPorProceso(int codigoConvenio, 
							long consecutivoNivel, String proceso, List<Calendar> meses);
	
	
	/**
	 * Obtiene el consolidado por cada mes de la sumatoria de los valores totales de los servicios de un proceso
	 * para un nivel de atención para cada mes para un contrato. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param proceso
	 * @param List<Calendar> meses
	 * @return ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>
	*/
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalServiciosPorNivelPorProcesoPorContrato(int codigoContrato, 
							long consecutivoNivel, String proceso, List<Calendar> meses);
	
	/**
	 * Obtiene el consolidado por cada mes de la sumatoria de los valores totales de los articulos de un proceso
	 * para un nivel de atención para cada mes para un convenio. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoConvenio
	 * @param consecutivoNivel
	 * @param proceso
	 * @param List<Calendar> meses
	 * @return ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>
	*/
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalArticulosPorNivelPorProceso(int codigoConvenio, 
							long consecutivoNivel, String proceso, List<Calendar> meses);
	
	/**
	 * Obtiene el consolidado por cada mes de la sumatoria de los valores totales de los articulos de un proceso
	 * para un nivel de atención para cada mes para un contrato. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param proceso
	 * @param List<Calendar> meses
	 * @return ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>
	*/
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalArticulosPorNivelPorProcesoPorContrato(int codigoContrato, 
							long consecutivoNivel, String proceso, List<Calendar> meses);
	
	/**
	 * Obtiene el consolidado por cada mes de la sumatoria de los valores totales de los servicios de un proceso
	 * para un nivel de atención para cada mes para un convenio. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoConvenio
	 * @param consecutivoNivel
	 * @param grupoServicio
	 * @param proceso
	 * @param List<Calendar> meses
	 * @return ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>
	*/
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalServiciosPorNivelPorGrupoPorProcesoPorConvenio(
							int codigoConvenio, long consecutivoNivel, GruposServicios grupoServicio, String proceso, List<Calendar> meses);
							
	/**
	 * Obtiene el consolidado por cada mes de la sumatoria de los valores totales de los servicios de un proceso
	 * para un nivel de atención para cada mes para un contrato. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param grupoServicio
	 * @param proceso
	 * @param List<Calendar> meses
	 * @return ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>
	*/
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalServiciosPorNivelPorGrupoPorProcesoPorContrato(
							int codigoContrato, long consecutivoNivel, GruposServicios grupoServicio, String proceso, List<Calendar> meses);

	/**
	 * Obtiene el consolidado por cada mes de la sumatoria de los valores totales de los articulos de un proceso
	 * para un nivel de atención para cada mes para un convenio. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoConvenio
	 * @param consecutivoNivel
	 * @param claseInventario
	 * @param proceso
	 * @param List<Calendar> meses
	 * @return ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>
	*/
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalArticulosPorNivelPorClasePorProcesoPorConvenio(
							int codigoConvenio, long consecutivoNivel, ClaseInventario claseInventario, String proceso, List<Calendar> meses);
	
	/**
	 * Obtiene el consolidado por cada mes de la sumatoria de los valores totales de los articulos de un proceso
	 * para un nivel de atención para cada mes para un contrato. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param claseInventario
	 * @param proceso
	 * @param List<Calendar> meses
	 * @return ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>
	*/
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalArticulosPorNivelPorClasePorProcesoPorContrato(
							int codigoConvenio, long consecutivoNivel, ClaseInventario claseInventario, String proceso, List<Calendar> meses);
	
	
	/**
	 * Obtiene el consolidado por cada mes de la sumatoria de los valores totales de los servicios de un proceso
	 * para un nivel de atención para cada mes para un convenio. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoConvenio
	 * @param consecutivoNivel
	 * @param servicio
	 * @param proceso
	 * @param List<Calendar> meses
	 * @return ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>
	*/
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalServiciosPorNivelPorServicioPorProcesoPorConvenio(
							int codigoConvenio, long consecutivoNivel, Servicios servicio, String proceso, List<Calendar> meses);
							
	/**
	 * Obtiene el consolidado por cada mes de la sumatoria de los valores totales de los servicios de un proceso
	 * para un nivel de atención para cada mes para un contrato. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param servicio
	 * @param proceso
	 * @param List<Calendar> meses
	 * @return ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>
	*/
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalServiciosPorNivelPorServicioPorProcesoPorContrato(
							int codigoContrato, long consecutivoNivel, Servicios servicio, String proceso, List<Calendar> meses);
	
	/**
	 * Obtiene el consolidado por cada mes de la sumatoria de los valores totales de los articulos de un proceso
	 * para un nivel de atención para cada mes para un convenio. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoConvenio
	 * @param consecutivoNivel
	 * @param articulo
	 * @param proceso
	 * @param List<Calendar> meses
	 * @return ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>
	*/
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalArticulosPorNivelPorArticuloPorProcesoPorConvenio(
							int codigoConvenio, long consecutivoNivel, Articulo articulo, String proceso, List<Calendar> meses);
							
	/**
	 * Obtiene el consolidado por cada mes de la sumatoria de los valores totales de los articulos de un proceso
	 * para un nivel de atención para cada mes para un contrato. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param articulo
	 * @param proceso
	 * @param List<Calendar> meses
	 * @return ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>
	*/
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalArticulosPorNivelPorArticuloPorProcesoPorContrato(
							int codigoContrato, long consecutivoNivel, Articulo articulo, String proceso, List<Calendar> meses);
	
	
	/**
	 * Obtiene la fecha del primer día en que se generó cierre si existe si no retorna null
	 * 
	 * @author Ricardo Ruiz
	 * 
	 * @return Date
	 */
	public Date obtenerPrimerDiaCierrePresupuesto();
	
	/**
	 * Lista todos según los parametros
	 * @param parametros
	 * @return ArrayList<LogCierrePresuCapita>
	 *
	 * @autor Fabián Becerra
	 */
	public ArrayList<DtoLogCierrePresuCapita> obtenerLogsParaIndicativo(DtoProcesoPresupuestoCapitado parametros);
	
	
	/**
	 * Lista los logs según la fecha de cierre y que no tengan una inconsitencia del tipo que llega por parámetro
	 * @param fechaCierre
	 * @param tipoInconsistencia
	 * @return ArrayList<LogCierrePresuCapita>
	 *
	 * @autor Ricardo Ruiz
	 */
	public ArrayList<DtoLogCierrePresuCapita> obtenerLogsPorFechaSinTipoInconsistencia(Date fechaCierre, String tipoInconsitencia);
}
