package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.princetonsa.dto.capitacion.DtoLogCierrePresuCapita;
import com.princetonsa.dto.capitacion.DtoMesesTotalServiciosArticulosValorizadosPorConvenio;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoTotalProcesoPresupuestoCapitado;
import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.ICierrePresupuestoCapitacionDAO;
import com.servinte.axioma.dto.capitacion.DtoTotalProceso;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierrePresupuestoCapitacionoMundo;
import com.servinte.axioma.orm.Articulo;
import com.servinte.axioma.orm.CierreNivelAteClasein;
import com.servinte.axioma.orm.CierreNivelAteGruServ;
import com.servinte.axioma.orm.ClaseInventario;
import com.servinte.axioma.orm.GruposServicios;
import com.servinte.axioma.orm.LogCierrePresuCapita;
import com.servinte.axioma.orm.Servicios;

public class CierrePresupuestoCapitacionoMundo implements ICierrePresupuestoCapitacionoMundo
{

	ICierrePresupuestoCapitacionDAO cierrePresupuestoCapitacionDAO;
	
	/**
	 * Metodo Constructor de la clase
	 * @author Camilo Gómez
	 */
	public CierrePresupuestoCapitacionoMundo (){
		cierrePresupuestoCapitacionDAO = CapitacionFabricaDAO.crearCierrePresupuestoCapitacionHibernateDAO();
	}
	
	@Override
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> obtenerCierresPresupuestocapitacion(DtoTotalProcesoPresupuestoCapitado parametros) 
	{
		return cierrePresupuestoCapitacionDAO.obtenerCierresPresupuestocapitacion(parametros);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ICierrePresupuestoCapitacionoMundo#obtenerTotalServiciosPorNivelPorProceso(int, long, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<DtoTotalProceso> obtenerTotalServiciosPorNivelPorProceso(int codigoContrato,
			long consecutivoNivel, String proceso, Date fechaInicio,
			Date fechaFin) {
		return cierrePresupuestoCapitacionDAO.obtenerTotalServiciosPorNivelPorProceso(codigoContrato,
					consecutivoNivel, proceso, fechaInicio, fechaFin);
	}



	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ICierrePresupuestoCapitacionoMundo#obtenerTotalArticulosPorNivelPorProceso(int, long, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<DtoTotalProceso> obtenerTotalArticulosPorNivelPorProceso(int codigoContrato,
			long consecutivoNivel, String proceso, Date fechaInicio,
			Date fechaFin) {
		return cierrePresupuestoCapitacionDAO.obtenerTotalArticulosPorNivelPorProceso(codigoContrato,
					consecutivoNivel, proceso, fechaInicio, fechaFin);
	}



	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ICierrePresupuestoCapitacionoMundo#obtenerTotalArticulosPorNivelPorClasePorProceso(int, long, int, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<DtoTotalProceso> obtenerTotalArticulosPorNivelPorClasePorProceso(
			int codigoContrato, long consecutivoNivel,
			int codigoClaseInventario, String proceso, Date fechaInicio,
			Date fechaFin) {
		return cierrePresupuestoCapitacionDAO.obtenerTotalArticulosPorNivelPorClasePorProceso(
												codigoContrato, consecutivoNivel, codigoClaseInventario, 
												proceso, fechaInicio, fechaFin);
	}



	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ICierrePresupuestoCapitacionoMundo#obtenerTotalServiciosPorNivelPorGrupoPorProceso(int, long, int, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<DtoTotalProceso> obtenerTotalServiciosPorNivelPorGrupoPorProceso(
			int codigoContrato, long consecutivoNivel, int codigoGrupoServicio,
			String proceso, Date fechaInicio, Date fechaFin) {
		return cierrePresupuestoCapitacionDAO.obtenerTotalServiciosPorNivelPorGrupoPorProceso(
												codigoContrato, consecutivoNivel, codigoGrupoServicio, 
												proceso, fechaInicio, fechaFin);
	}


	@Override
	public void guardarActualizarCierreNivelAteClasein(CierreNivelAteClasein instance) {
		cierrePresupuestoCapitacionDAO.guardarActualizarCierreNivelAteClasein(instance);
	}


	@Override
	public void guardarActualizarCierreNivelAteGruServ(CierreNivelAteGruServ instance) {
		cierrePresupuestoCapitacionDAO.guardarActualizarCierreNivelAteGruServ(instance);
	}

	@Override
	public void guardarActualizarLogCierrePresuCapita(LogCierrePresuCapita instance) {
		cierrePresupuestoCapitacionDAO.guardarActualizarLogCierrePresuCapita(instance);
	}

	@Override
	public ArrayList<DtoLogCierrePresuCapita> obtenerLogs(DtoProcesoPresupuestoCapitado parametros) {
		return cierrePresupuestoCapitacionDAO.obtenerLogs(parametros);
	}

	@Override
	public void eliminarCierreNivelAteClasein(CierreNivelAteClasein persistentInstance) {
		cierrePresupuestoCapitacionDAO.eliminarCierreNivelAteClasein(persistentInstance);
	}

	@Override
	public ArrayList<CierreNivelAteClasein> obtenerCierresCierreNivelAteClasein(DtoTotalProcesoPresupuestoCapitado parametros) {
		return cierrePresupuestoCapitacionDAO.obtenerCierresCierreNivelAteClasein(parametros);
	}

	@Override
	public void eliminarCierreNivelAteGruServ(CierreNivelAteGruServ persistentInstance) {
		cierrePresupuestoCapitacionDAO.eliminarCierreNivelAteGruServ(persistentInstance);
	}

	@Override
	public ArrayList<CierreNivelAteGruServ> obtenerCierresCierreNivelAteGruServ(DtoTotalProcesoPresupuestoCapitado parametros) {
		return cierrePresupuestoCapitacionDAO.obtenerCierresCierreNivelAteGruServ(parametros);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ICierrePresupuestoCapitacionoMundo#obtenerListadoMesesTotalServiciosPorNivelPorProceso(int, long, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalServiciosPorNivelPorProceso(
			int codigoConvenio, long consecutivoNivel, String proceso,
			List<Calendar> meses) {
		return cierrePresupuestoCapitacionDAO.obtenerListadoMesesTotalServiciosPorNivelPorProceso(
					codigoConvenio, consecutivoNivel, proceso, meses);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ICierrePresupuestoCapitacionoMundo#obtenerListadoMesesTotalServiciosPorNivelPorProcesoPorContrato(int, long, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalServiciosPorNivelPorProcesoPorContrato(
			int codigoContrato, long consecutivoNivel, String proceso,
			List<Calendar> meses) {
		return cierrePresupuestoCapitacionDAO.obtenerListadoMesesTotalServiciosPorNivelPorProcesoPorContrato(
					codigoContrato, consecutivoNivel, proceso, meses);
	}
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ICierrePresupuestoCapitacionoMundo#obtenerListadoMesesTotalArticulosPorNivelPorProceso(int, long, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalArticulosPorNivelPorProceso(
			int codigoConvenio, long consecutivoNivel, String proceso,
			List<Calendar> meses) {
		return cierrePresupuestoCapitacionDAO.obtenerListadoMesesTotalArticulosPorNivelPorProceso(
				codigoConvenio, consecutivoNivel, proceso, meses);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ICierrePresupuestoCapitacionoMundo#obtenerListadoMesesTotalArticulosPorNivelPorProcesoPorContrato(int, long, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalArticulosPorNivelPorProcesoPorContrato(
			int codigoContrato, long consecutivoNivel, String proceso,
			List<Calendar> meses) {
		return cierrePresupuestoCapitacionDAO.obtenerListadoMesesTotalArticulosPorNivelPorProcesoPorContrato(
					codigoContrato, consecutivoNivel, proceso, meses);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ICierrePresupuestoCapitacionoMundo#obtenerListadoMesesTotalServiciosPorNivelPorGrupoPorProcesoPorConvenio(int, long, com.servinte.axioma.orm.GruposServicios, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalServiciosPorNivelPorGrupoPorProcesoPorConvenio(
			int codigoConvenio, long consecutivoNivel,
			GruposServicios grupoServicio, String proceso, List<Calendar> meses) {
		return cierrePresupuestoCapitacionDAO.obtenerListadoMesesTotalServiciosPorNivelPorGrupoPorProcesoPorConvenio(
						codigoConvenio, consecutivoNivel, grupoServicio, proceso, meses);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ICierrePresupuestoCapitacionoMundo#obtenerListadoMesesTotalServiciosPorNivelPorGrupoPorProcesoPorContrato(int, long, com.servinte.axioma.orm.GruposServicios, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalServiciosPorNivelPorGrupoPorProcesoPorContrato(
			int codigoContrato, long consecutivoNivel,
			GruposServicios grupoServicio, String proceso, List<Calendar> meses) {
		return cierrePresupuestoCapitacionDAO.obtenerListadoMesesTotalServiciosPorNivelPorGrupoPorProcesoPorContrato(
						codigoContrato, consecutivoNivel, grupoServicio, proceso, meses);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ICierrePresupuestoCapitacionoMundo#obtenerListadoMesesTotalArticulosPorNivelPorClasePorProcesoPorConvenio(int, long, com.servinte.axioma.orm.ClaseInventario, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalArticulosPorNivelPorClasePorProcesoPorConvenio(
			int codigoConvenio, long consecutivoNivel,
			ClaseInventario claseInventario, String proceso,
			List<Calendar> meses) {
		return cierrePresupuestoCapitacionDAO.obtenerListadoMesesTotalArticulosPorNivelPorClasePorProcesoPorConvenio(
						codigoConvenio, consecutivoNivel, claseInventario, proceso, meses);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ICierrePresupuestoCapitacionoMundo#obtenerListadoMesesTotalArticulosPorNivelPorClasePorProcesoPorContrato(int, long, com.servinte.axioma.orm.ClaseInventario, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalArticulosPorNivelPorClasePorProcesoPorContrato(
			int codigoConvenio, long consecutivoNivel,
			ClaseInventario claseInventario, String proceso,
			List<Calendar> meses) {
		return cierrePresupuestoCapitacionDAO.obtenerListadoMesesTotalArticulosPorNivelPorClasePorProcesoPorContrato(
						codigoConvenio, consecutivoNivel, claseInventario, proceso, meses);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ICierrePresupuestoCapitacionoMundo#obtenerListadoMesesTotalServiciosPorNivelPorServicioPorProcesoPorConvenio(int, long, com.servinte.axioma.orm.Servicios, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalServiciosPorNivelPorServicioPorProcesoPorConvenio(
			int codigoConvenio, long consecutivoNivel, Servicios servicio,
			String proceso, List<Calendar> meses) {
		return cierrePresupuestoCapitacionDAO.obtenerListadoMesesTotalServiciosPorNivelPorServicioPorProcesoPorConvenio(
						codigoConvenio, consecutivoNivel, servicio, proceso, meses);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ICierrePresupuestoCapitacionoMundo#obtenerListadoMesesTotalServiciosPorNivelPorServicioPorProcesoPorContrato(int, long, com.servinte.axioma.orm.Servicios, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalServiciosPorNivelPorServicioPorProcesoPorContrato(
			int codigoContrato, long consecutivoNivel, Servicios servicio,
			String proceso, List<Calendar> meses) {
		return cierrePresupuestoCapitacionDAO.obtenerListadoMesesTotalServiciosPorNivelPorServicioPorProcesoPorContrato(
						codigoContrato, consecutivoNivel, servicio, proceso, meses);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ICierrePresupuestoCapitacionoMundo#obtenerListadoMesesTotalArticulosPorNivelPorArticuloPorProcesoPorConvenio(int, long, com.servinte.axioma.orm.Articulo, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalArticulosPorNivelPorArticuloPorProcesoPorConvenio(
			int codigoConvenio, long consecutivoNivel, Articulo articulo,
			String proceso, List<Calendar> meses) {
		return cierrePresupuestoCapitacionDAO.obtenerListadoMesesTotalArticulosPorNivelPorArticuloPorProcesoPorConvenio(
						codigoConvenio, consecutivoNivel, articulo, proceso, meses);
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ICierrePresupuestoCapitacionoMundo#obtenerListadoMesesTotalArticulosPorNivelPorArticuloPorProcesoPorContrato(int, long, com.servinte.axioma.orm.Articulo, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalArticulosPorNivelPorArticuloPorProcesoPorContrato(
			int codigoContrato, long consecutivoNivel, Articulo articulo,
			String proceso, List<Calendar> meses) {
		return cierrePresupuestoCapitacionDAO.obtenerListadoMesesTotalArticulosPorNivelPorArticuloPorProcesoPorContrato(
						codigoContrato, consecutivoNivel, articulo, proceso, meses);
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ICierrePresupuestoCapitacionoMundo#obtenerPrimerDiaCierrePresupuesto()
	 */
	@Override
	public Date obtenerPrimerDiaCierrePresupuesto() {
		return cierrePresupuestoCapitacionDAO.obtenerPrimerDiaCierrePresupuesto();
	}
	
	
	@Override
	public ArrayList<DtoLogCierrePresuCapita> obtenerLogsParaIndicativo(DtoProcesoPresupuestoCapitado parametros){
		return cierrePresupuestoCapitacionDAO.obtenerLogsParaIndicativo(parametros);
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ICierrePresupuestoCapitacionoMundo#obtenerLogsPorFechaSinTipoInconsistencia(java.util.Date, java.lang.String)
	 */
	@Override
	public ArrayList<DtoLogCierrePresuCapita> obtenerLogsPorFechaSinTipoInconsistencia(
			Date fechaCierre, String tipoInconsitencia) {
		return cierrePresupuestoCapitacionDAO.obtenerLogsPorFechaSinTipoInconsistencia(fechaCierre, tipoInconsitencia);
	}
	
	
}
