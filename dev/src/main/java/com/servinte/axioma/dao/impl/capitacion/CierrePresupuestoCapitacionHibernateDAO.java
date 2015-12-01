package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.princetonsa.dto.capitacion.DtoLogCierrePresuCapita;
import com.princetonsa.dto.capitacion.DtoMesesTotalServiciosArticulosValorizadosPorConvenio;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoTotalProcesoPresupuestoCapitado;
import com.servinte.axioma.dao.interfaz.capitacion.ICierrePresupuestoCapitacionDAO;
import com.servinte.axioma.dto.capitacion.DtoTotalProceso;
import com.servinte.axioma.orm.Articulo;
import com.servinte.axioma.orm.CierreNivelAteClasein;
import com.servinte.axioma.orm.CierreNivelAteGruServ;
import com.servinte.axioma.orm.ClaseInventario;
import com.servinte.axioma.orm.GruposServicios;
import com.servinte.axioma.orm.LogCierrePresuCapita;
import com.servinte.axioma.orm.Servicios;
import com.servinte.axioma.orm.delegate.capitacion.CierreNivelAteClaseinDelegate;
import com.servinte.axioma.orm.delegate.capitacion.CierreNivelAteGruServDelegate;
import com.servinte.axioma.orm.delegate.capitacion.LogCierrePresuCapitaDelegate;

/**
 * @author Cristhian Murillo
 */
public class CierrePresupuestoCapitacionHibernateDAO implements ICierrePresupuestoCapitacionDAO 
{

	// Articulos
	CierreNivelAteClaseinDelegate cierreNivelAteClaseinDelegate;
	
	// Servicios
	CierreNivelAteGruServDelegate cierreNivelAteGruServDelegate;
	
	//Log
	LogCierrePresuCapitaDelegate logCierrePresuCapitaDelegate;
	
	
	
	/**
	 * Metodo constructor de la clase
	 * @author Cristhian Murillo
	 */
	public CierrePresupuestoCapitacionHibernateDAO(){
		cierreNivelAteClaseinDelegate	= new CierreNivelAteClaseinDelegate();
		cierreNivelAteGruServDelegate	= new CierreNivelAteGruServDelegate();
		logCierrePresuCapitaDelegate	= new LogCierrePresuCapitaDelegate();
	}

	
	
	@Override
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> obtenerCierresPresupuestocapitacion(DtoTotalProcesoPresupuestoCapitado parametros) 
	{
		ArrayList<DtoTotalProcesoPresupuestoCapitado> listaCierres = new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
		
		listaCierres.addAll(cierreNivelAteClaseinDelegate.obtenerCierres(parametros));
		listaCierres.addAll(cierreNivelAteGruServDelegate.obtenerCierres(parametros));
		
		return listaCierres;
	}



	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ICierrePresupuestoCapitacionDAO#obtenerTotalServiciosPorNivelPorProceso(int, long, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<DtoTotalProceso> obtenerTotalServiciosPorNivelPorProceso(int codigoContrato,
			long consecutivoNivel, String proceso, Date fechaInicio,
			Date fechaFin) {
		return cierreNivelAteGruServDelegate.obtenerTotalServiciosPorNivelPorProceso(codigoContrato,
						consecutivoNivel, proceso, fechaInicio, fechaFin);
	}
	
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ICierrePresupuestoCapitacionDAO#obtenerTotalArticulosPorNivelPorProceso(int, long, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<DtoTotalProceso> obtenerTotalArticulosPorNivelPorProceso(int codigoContrato,
			long consecutivoNivel, String proceso, Date fechaInicio,
			Date fechaFin) {
		return cierreNivelAteClaseinDelegate.obtenerTotalArticulosPorNivelPorProceso(codigoContrato,
						consecutivoNivel, proceso, fechaInicio, fechaFin);
	}



	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ICierrePresupuestoCapitacionDAO#obtenerTotalArticulosPorNivelPorClasePorProceso(int, long, int, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<DtoTotalProceso> obtenerTotalArticulosPorNivelPorClasePorProceso(
			int codigoContrato, long consecutivoNivel,
			int codigoClaseInventario, String proceso, Date fechaInicio,
			Date fechaFin) {
		return cierreNivelAteClaseinDelegate.obtenerTotalArticulosPorNivelPorClasePorProceso(
												codigoContrato, consecutivoNivel, codigoClaseInventario, 
												proceso, fechaInicio, fechaFin);
	}



	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ICierrePresupuestoCapitacionDAO#obtenerTotalServiciosPorNivelPorGrupoPorProceso(int, long, int, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<DtoTotalProceso> obtenerTotalServiciosPorNivelPorGrupoPorProceso(
			int codigoContrato, long consecutivoNivel, int codigoGrupoServicio,
			String proceso, Date fechaInicio, Date fechaFin) {
		return cierreNivelAteGruServDelegate.obtenerTotalServiciosPorNivelPorGrupoPorProceso(
												codigoContrato, consecutivoNivel, codigoGrupoServicio, 
												proceso, fechaInicio, fechaFin);
	}
	
	
	@Override
	public void guardarActualizarCierreNivelAteClasein(CierreNivelAteClasein instance){
		cierreNivelAteClaseinDelegate.attachDirty(instance);
	}


	@Override
	public void guardarActualizarCierreNivelAteGruServ(CierreNivelAteGruServ instance){
		cierreNivelAteGruServDelegate.attachDirty(instance);
	}
	

	@Override 
	public void guardarActualizarLogCierrePresuCapita(LogCierrePresuCapita instance){
		logCierrePresuCapitaDelegate.attachDirty(instance);
	}

	@Override
	public ArrayList<DtoLogCierrePresuCapita> obtenerLogs(DtoProcesoPresupuestoCapitado parametros) {
		return logCierrePresuCapitaDelegate.obtenerLogs(parametros);
	}



	@Override
	public void eliminarCierreNivelAteClasein(CierreNivelAteClasein persistentInstance) {
		cierreNivelAteClaseinDelegate.eliminarCierreNivelAteClasein(persistentInstance);
	}



	@Override
	public ArrayList<CierreNivelAteClasein> obtenerCierresCierreNivelAteClasein(DtoTotalProcesoPresupuestoCapitado parametros) {
		return cierreNivelAteClaseinDelegate.obtenerCierresCierreNivelAteClasein(parametros);
	}



	@Override
	public void eliminarCierreNivelAteGruServ(CierreNivelAteGruServ persistentInstance) {
		cierreNivelAteGruServDelegate.eliminarCierreNivelAteGruServ(persistentInstance);
	}



	@Override
	public ArrayList<CierreNivelAteGruServ> obtenerCierresCierreNivelAteGruServ(DtoTotalProcesoPresupuestoCapitado parametros) {
		return cierreNivelAteGruServDelegate.obtenerCierresCierreNivelAteGruServ(parametros);
	}
	
	
	



	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ICierrePresupuestoCapitacionDAO#obtenerListadoMesesTotalServiciosPorNivelPorProceso(int, long, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalServiciosPorNivelPorProceso(
			int codigoConvenio, long consecutivoNivel, String proceso,
			List<Calendar> meses) {
		return cierreNivelAteGruServDelegate.obtenerListadoMesesTotalServiciosPorNivelPorProceso(
					codigoConvenio, consecutivoNivel, proceso, meses);
	}



	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ICierrePresupuestoCapitacionDAO#obtenerListadoMesesTotalServiciosPorNivelPorProcesoPorContrato(int, long, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalServiciosPorNivelPorProcesoPorContrato(
			int codigoContrato, long consecutivoNivel, String proceso,
			List<Calendar> meses) {
		return cierreNivelAteGruServDelegate.obtenerListadoMesesTotalServiciosPorNivelPorProcesoPorContrato(
													codigoContrato, consecutivoNivel, proceso, meses);
	}



	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ICierrePresupuestoCapitacionDAO#obtenerListadoMesesTotalArticulosPorNivelPorProceso(int, long, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalArticulosPorNivelPorProceso(
			int codigoConvenio, long consecutivoNivel, String proceso,
			List<Calendar> meses) {
		return cierreNivelAteClaseinDelegate.obtenerListadoMesesTotalArticulosPorNivelPorProceso(
						codigoConvenio, consecutivoNivel, proceso, meses);
	}



	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ICierrePresupuestoCapitacionDAO#obtenerListadoMesesTotalArticulosPorNivelPorProcesoPorContrato(int, long, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalArticulosPorNivelPorProcesoPorContrato(
			int codigoContrato, long consecutivoNivel, String proceso,
			List<Calendar> meses) {
		return cierreNivelAteClaseinDelegate.obtenerListadoMesesTotalArticulosPorNivelPorProcesoPorContrato(
						codigoContrato, consecutivoNivel, proceso, meses);
	}



	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ICierrePresupuestoCapitacionDAO#obtenerListadoMesesTotalServiciosPorNivelPorGrupoPorProcesoPorConvenio(int, long, com.servinte.axioma.orm.GruposServicios, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalServiciosPorNivelPorGrupoPorProcesoPorConvenio(
			int codigoConvenio, long consecutivoNivel,
			GruposServicios grupoServicio, String proceso, List<Calendar> meses) {
		return cierreNivelAteGruServDelegate.obtenerListadoMesesTotalServiciosPorNivelPorGrupoPorProcesoPorConvenio(
						codigoConvenio, consecutivoNivel, grupoServicio, proceso, meses);
	}



	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ICierrePresupuestoCapitacionDAO#obtenerListadoMesesTotalServiciosPorNivelPorGrupoPorProcesoPorContrato(int, long, com.servinte.axioma.orm.GruposServicios, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalServiciosPorNivelPorGrupoPorProcesoPorContrato(
			int codigoContrato, long consecutivoNivel,
			GruposServicios grupoServicio, String proceso, List<Calendar> meses) {
		return cierreNivelAteGruServDelegate.obtenerListadoMesesTotalServiciosPorNivelPorGrupoPorProcesoPorContrato(
						codigoContrato, consecutivoNivel, grupoServicio, proceso, meses);
	}



	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ICierrePresupuestoCapitacionDAO#obtenerListadoMesesTotalArticulosPorNivelPorClasePorProcesoPorConvenio(int, long, com.servinte.axioma.orm.ClaseInventario, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalArticulosPorNivelPorClasePorProcesoPorConvenio(
			int codigoConvenio, long consecutivoNivel,
			ClaseInventario claseInventario, String proceso,
			List<Calendar> meses) {
		return cierreNivelAteClaseinDelegate.obtenerListadoMesesTotalArticulosPorNivelPorClasePorProcesoPorConvenio(
						codigoConvenio, consecutivoNivel, claseInventario, proceso, meses);
	}



	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ICierrePresupuestoCapitacionDAO#obtenerListadoMesesTotalArticulosPorNivelPorClasePorProcesoPorContrato(int, long, com.servinte.axioma.orm.ClaseInventario, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalArticulosPorNivelPorClasePorProcesoPorContrato(
			int codigoContrato, long consecutivoNivel,
			ClaseInventario claseInventario, String proceso,
			List<Calendar> meses) {
		return cierreNivelAteClaseinDelegate.obtenerListadoMesesTotalArticulosPorNivelPorClasePorProcesoPorContrato(
						codigoContrato, consecutivoNivel, claseInventario, proceso, meses);
	}



	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ICierrePresupuestoCapitacionDAO#obtenerListadoMesesTotalServiciosPorNivelPorServicioPorProcesoPorConvenio(int, long, com.servinte.axioma.orm.Servicios, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalServiciosPorNivelPorServicioPorProcesoPorConvenio(
			int codigoConvenio, long consecutivoNivel, Servicios servicio,
			String proceso, List<Calendar> meses) {
		return cierreNivelAteGruServDelegate.obtenerListadoMesesTotalServiciosPorNivelPorServicioPorProcesoPorConvenio(
						codigoConvenio, consecutivoNivel, servicio, proceso, meses);
	}



	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ICierrePresupuestoCapitacionDAO#obtenerListadoMesesTotalServiciosPorNivelPorServicioPorProcesoPorContrato(int, long, com.servinte.axioma.orm.Servicios, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalServiciosPorNivelPorServicioPorProcesoPorContrato(
			int codigoContrato, long consecutivoNivel, Servicios servicio,
			String proceso, List<Calendar> meses) {
		return cierreNivelAteGruServDelegate.obtenerListadoMesesTotalServiciosPorNivelPorServicioPorProcesoPorContrato(
						codigoContrato, consecutivoNivel, servicio, proceso, meses);
	}



	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ICierrePresupuestoCapitacionDAO#obtenerListadoMesesTotalArticulosPorNivelPorArticuloPorProcesoPorConvenio(int, long, com.servinte.axioma.orm.Articulo, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalArticulosPorNivelPorArticuloPorProcesoPorConvenio(
			int codigoConvenio, long consecutivoNivel, Articulo articulo,
			String proceso, List<Calendar> meses) {
		return cierreNivelAteClaseinDelegate.obtenerListadoMesesTotalArticulosPorNivelPorArticuloPorProcesoPorConvenio(
						codigoConvenio, consecutivoNivel, articulo, proceso, meses);
	}



	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ICierrePresupuestoCapitacionDAO#obtenerListadoMesesTotalArticulosPorNivelPorArticuloPorProcesoPorContrato(int, long, com.servinte.axioma.orm.Articulo, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalArticulosPorNivelPorArticuloPorProcesoPorContrato(
			int codigoContrato, long consecutivoNivel, Articulo articulo,
			String proceso, List<Calendar> meses) {
		return cierreNivelAteClaseinDelegate.obtenerListadoMesesTotalArticulosPorNivelPorArticuloPorProcesoPorContrato(
						codigoContrato, consecutivoNivel, articulo, proceso, meses);
	}
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ICierrePresupuestoCapitacionDAO#obtenerPrimerDiaCierrePresupuesto()
	 */
	@Override
	public Date obtenerPrimerDiaCierrePresupuesto() {
		return logCierrePresuCapitaDelegate.obtenerPrimerDiaCierrePresupuesto();
	}
	
	@Override
	public ArrayList<DtoLogCierrePresuCapita> obtenerLogsParaIndicativo(DtoProcesoPresupuestoCapitado parametros){
		return logCierrePresuCapitaDelegate.obtenerLogsParaIndicativo(parametros);
	}



	/** (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ICierrePresupuestoCapitacionDAO#obtenerLogsPorFechaSinTipoInconsistencia(java.util.Date, java.lang.String)
	 */
	@Override
	public ArrayList<DtoLogCierrePresuCapita> obtenerLogsPorFechaSinTipoInconsistencia(
			Date fechaCierre, String tipoInconsitencia) {
		return logCierrePresuCapitaDelegate.obtenerLogsPorFechaSinTipoInconsistencia(fechaCierre, tipoInconsitencia);
	}

}
