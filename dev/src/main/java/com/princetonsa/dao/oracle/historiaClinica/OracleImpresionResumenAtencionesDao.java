package com.princetonsa.dao.oracle.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.ImpresionResumenAtencionesDao;
import com.princetonsa.dao.sqlbase.SqlBaseImpresionResumenAtencionesDao;
import com.princetonsa.dto.manejoPaciente.DtoObservacionesGeneralesOrdenesMedicas;
import com.princetonsa.mundo.PersonaBasica;
import com.servinte.axioma.dto.historiaClinica.InfoIngresoDto;
import com.servinte.axioma.generadorReporte.historiaClinica.DtoFiltroImpresionHistoriaClinica;
import com.servinte.axioma.generadorReporte.historiaClinica.DtoResultadoImpresionHistoriaClinica;

public class OracleImpresionResumenAtencionesDao implements
		ImpresionResumenAtencionesDao
{

	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public HashMap consultarAdminMedicamentos(Connection con, HashMap vo,Integer numeroArticulo)
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarAdminMedicamentos(con,vo,numeroArticulo);
	}
	
	
	/**
	 * M�todo que consulta el detalle de adminsitraci�n de un articulo
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap consultarDetalleArticuloAdmin(Connection con, HashMap vo)
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarDetalleArticuloAdmin(con, vo);
	}
	
	public HashMap consultarDetalleArticuloAdminE(Connection con, int artppal, String ingreso)
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarDetalleArticuloAdminE(con, artppal, ingreso);
	}
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public HashMap consultarInsumos(Connection con, HashMap vo)
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarInsumos(con,vo);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public HashMap consultarRespuestaInterpretacionProcedimientos(Connection con,  HashMap vo)
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarRespuestaInterpretacionProcedimientos(con,vo);
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap consultarOrdenesTipoMonitore(Connection con, HashMap vo)
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarOrdenesTipoMonitore(con,vo);
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap consultarOrdenesSoporteRespiratorio(Connection con, HashMap vo)
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarOrdenesSoporteRespiratorio(con,vo);
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap consultarOrdenesDieta(Connection con, HashMap vo)
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarOrdenesDieta(con,vo);
	}
	


	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap consultarOrdenesCirugias(Connection con, HashMap vo)
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarOrdenesCirugias(con,vo);
	}
	


	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap consultarSolicitudesMedicamentoInsumos(Connection con, HashMap vo)
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarSolicitudesMedicamentoInsumos(con,vo);
	}
	

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap consultarSolicitudesProcedimientos(Connection con, HashMap vo)
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarSolicitudesProcedimientos(con,vo);
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap consultarSolicitudesProcedimientos(int numeroSolicitud)throws Exception
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarSolicitudesProcedimientos(numeroSolicitud);
	}
/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap consultarSolicitudesInterconsultas(Connection con, HashMap vo)
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarSolicitudesInterconsultas(con,vo);
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap consultarOrdenMedica(Connection con, HashMap vo,Integer ingreso)
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarOrdenMedica(con,vo,ingreso);
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap consultarOrdenMedica(int numeroSolicitud,Integer ingreso,Integer cuenta)
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarOrdenMedica(numeroSolicitud,ingreso,cuenta);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoEstado
	 * @param tipoSolicitud
	 * @param vo
	 * @return
	 */
	public  HashMap obtenerSolicitudesEstadoTipoFiltro(Connection con, int[] codigoEstado, int tipoSolicitud, HashMap vo)
	{
		return SqlBaseImpresionResumenAtencionesDao.obtenerSolicitudesEstadoTipoFiltro(con,codigoEstado,tipoSolicitud,vo);
	}	
	
	/**
	 * Metodo para cargar informacion dados unos para metros de busqueda sobre una consulta especifica. 
	 * @param con
	 * @param mp
	 * @return
	 */
	public HashMap consultarInformacion(Connection con, HashMap mp)
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarInformacion(con, mp, DaoFactory.ORACLE);
	}
	
	/**
	 * evoluciones
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap obtenerEvoluciones(Connection con, HashMap vo)
	{
		return SqlBaseImpresionResumenAtencionesDao.obtenerEvoluciones(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public HashMap obtenerEncabezadoPaciente(Connection con, String idIngreso)
	{
		return SqlBaseImpresionResumenAtencionesDao.obtenerEncabezadoPaciente(con, idIngreso);
	}
	

	/**
	 * 
	 * @param con
	 * @param map
	 * @return
	 */
	public HashMap consultarSoporteRespiratorio(Connection con, HashMap vo)
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarSoporteRespiratorio (con,vo);
	}
	
	/**
	 * M�todo que consulta los c�digos de petici�n de cirug�a de acuerdo a los par�metros
	 * de b�squeda en la impresi�n de la historia cl�nica
	 * @param con
	 * @param cuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion
	 * @return
	 */
	public HashMap consultarPeticionesCirugiaImpresionHc(Connection con, String cuenta, String cuentaAsocio, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion, String solicitudesFactura)
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarPeticionesCirugiaImpresionHc (con, cuenta, cuentaAsocio, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion, solicitudesFactura);
	}
	
	/**
	 * M�todo que consulta los encabezados de cirug�a y preanestesia  para la hoja 
	 * de anestesia de cirug�as
	 * @param con
	 * @param listadoPeticiones
	 * @return
	 */
	public HashMap consultarEncabezadosHojaAnestesia(Connection con, String listadoPeticiones) 
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarEncabezadosHojaAnestesia(con, listadoPeticiones);
	}
	
	/**
	 * M�todo que consulta los ex�menes de laboratorio de preanestesia
	 * de acuerdo a la lista de peticiones que cumplen el filtro de impresion hc
	 * @param con
	 * @param listadoPeticiones
	 * @return
	 */
	public HashMap consultarExamenesLaboratorioPreanestesia(Connection con, String listadoPeticiones) 
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarExamenesLaboratorioPreanestesia(con,listadoPeticiones);
	}
	
	/**
	 * M�todo que consulta el hist�rico de los ex�menes f�sicos de preanestesia, text y textarea
	 * de acuerdo a la lista de peticiones que cumplen el filtro de impresion hc
	 * @param con
	 * @param listadoPeticiones
	 * @param esTexto
	 * @return
	 */
	public HashMap consultarHistoExamenesFisicos(Connection con, String listadoPeticiones, boolean esTexto) 
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarHistoExamenesFisicos(con, listadoPeticiones, esTexto);
	}
	
	/**
	 * M�todo que consulta las conclusiones de preanestesia
	 * de acuerdo a la lista de peticiones que cumplen el filtro de impresion hc
	 * @param con
	 * @param listadoPeticiones
	 * @return
	 */
	public HashMap consultarHistoConclusiones(Connection con, String listadoPeticiones) 
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarHistoConclusiones(con, listadoPeticiones);
	}
	
	/**
	 * M�todo que consulta el hist�rico de las secciones de la hoja de anestesia
	 * de acuerdo al parametro nroConsulta y a la lista de solicitudes que cumplen el filtro de impresion hc
	 * @param con
	 * @param listadoSolicitudes
	 * @param nroConsulta
	 * @return
	 */
	public HashMap consultarHistoSeccionesHojaAnestesia(Connection con, String listadoSolicitudes, int nroConsulta)
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarHistoSeccionesHojaAnestesia(con, listadoSolicitudes, nroConsulta);
	}
	
	/**
	 * M�todo que consulta el hist�rico de las t�cnicas de anestesia 
	 * @param con
	 * @param listadoSolicitudes
	 * @return
	 */
	public Collection consultarHistoTecnicaAnestesia(Connection con, String listadoSolicitudes)
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarHistoTecnicaAnestesia(con, listadoSolicitudes);
	}
	
	/**
	 * M�todo que consulta el hist�rico de signos vitales, los tiempos y
	 * los valores de cada uno de los signos vitales
	 * @param con
	 * @param listadoSolicitudes
	 * @param nroConsulta  1-> Tiempos Signos Vitales
	 * 					   2-> Valores Signos Vitales	
	 * @return
	 */
	public Collection consultarHistoSignosVitales(Connection con, String listadoSolicitudes, int nroConsulta) 
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarHistoSignosVitales(con, listadoSolicitudes, nroConsulta);
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public  HashMap obtenerEnlacesValoracionesConsultaExterna(Connection con, HashMap vo) 
	{
		return SqlBaseImpresionResumenAtencionesDao.obtenerEnlacesValoracionesConsultaExterna(con, vo);
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public  HashMap consultarOrdenesAmbulatorias(Connection con, HashMap vo) 
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarOrdenesAmbulatorias(con, vo);
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public  HashMap obtenerConsultasPYP(Connection con, HashMap vo) 
	{
		return SqlBaseImpresionResumenAtencionesDao.obtenerConsultasPYP(con, vo);
	}


	@Override
	public HashMap consultarResultatadosLaboratorios(Connection con,
			String idIngreso, String fechaInicial, String fechaFinal,
			String horaInicial, String horaFinal) {
		return SqlBaseImpresionResumenAtencionesDao.consultarResultatadosLaboratorios(con, idIngreso, fechaInicial, fechaFinal, horaInicial, horaFinal);
	}


	@Override
	public HashMap consultarrValoracionesEnfermeria(Connection con,
			String idIngreso, String fechaInicial, String fechaFinal,
			String horaInicial, String horaFinal) {
		return SqlBaseImpresionResumenAtencionesDao.consultarrValoracionesEnfermeria(con, idIngreso, fechaInicial, fechaFinal, horaInicial, horaFinal);
	}


	@Override
	public ArrayList<DtoResultadoImpresionHistoriaClinica> consultarInformacionHistoriaClinica(
			Connection con, DtoFiltroImpresionHistoriaClinica filtro,int identificadorSeccion) {
		return SqlBaseImpresionResumenAtencionesDao.consultarInformacionHistoriaClinica(con, filtro,identificadorSeccion);
	}

	/**
	 * Tipo Modificacion: Segun incidencia 5055
	 * Funcion: consultarInformacionHistoriaClinica
	 * Autor: Alejandro Aguirre Luna
	 * Fecha: 18/01/2013
	 * Descripcion: Sobrecarga del metodo consultarInformacionHistoriaClinica
	 * 				con el argumento adicional paciente de tipo PersonaBasica
	 * 				el cual sirve para obtener posteriormente en la clase 
	 * 				SqlBaseImpresionResumenAtencionesDao el 
	 * 				tipoPacienteHospitalizado el cual puede ser de tipo; H,A,C.
	 * Argumentos:
	 * 			- Connection con	
	 * 			- DtoFiltroImpresionHistoriaClinica filtro
	 * 			- int identificadorSeccion
	 * 			- PersonaBasica paciente
	 * Retorno: ArrayList<DtoResultadoImpresionHistoriaClinica>
	 * */
	@Override
	public ArrayList<DtoResultadoImpresionHistoriaClinica> consultarInformacionHistoriaClinica(
			Connection con, DtoFiltroImpresionHistoriaClinica filtro,int identificadorSeccion, PersonaBasica paciente) {
		return SqlBaseImpresionResumenAtencionesDao.consultarInformacionHistoriaClinica(con, filtro,identificadorSeccion,paciente);
	}
	/** Tipo Modificacion: Segun incidencia 5055
	* Funcion: consultarInformacionHistoriaClinica
	* Autor: Alejandro Aguirre Luna
	* Fecha: 25/02/2013
	* Descripcion: Sobrecarga del metodo consultarInformacionHistoriaClinica
	* 	       con el argumento adicional IdIngreso que permite establecer 
	* 		   posteriromente las cuentas del paciente. 
	* Argumentos:
	* 	- Connection con	
	* 	- DtoFiltroImpresionHistoriaClinica filtro
	* 	- int identificadorSeccion
	* 	- PersonaBasica paciente
	*   - String IdIngreso
	* Retorno: ArrayList<DtoResultadoImpresionHistoriaClinica>
	**/
	@Override
	public ArrayList<DtoResultadoImpresionHistoriaClinica> consultarInformacionHistoriaClinica(
			Connection con, DtoFiltroImpresionHistoriaClinica filtro,int identificadorSeccion, PersonaBasica paciente, String IdIngreso) {
		return SqlBaseImpresionResumenAtencionesDao.consultarInformacionHistoriaClinica(con, filtro,identificadorSeccion,paciente, IdIngreso);
	}
	
	@Override
	public Collection consultarSignosVitalesFijosHistoImpresionHC(int codigoRegistroEnfermeria) 
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarSignosVitalesFijosHistoImpresionHC(codigoRegistroEnfermeria);
	}


	@Override
	public Collection consultarSignosVitalesParamHistoImpresionHC(int codigoRegistroEnfermeria) 
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarSignosVitalesParamHistoImpresionHC(codigoRegistroEnfermeria);
	}


	@Override
	public Collection consultarSignosVitalesHistoTodosImpresionHC(int codigoRegistroEnfermeria) 
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarSignosVitalesHistoTodosImpresionHC(codigoRegistroEnfermeria);
	}
	
	@Override
	public HashMap consultarSolicitudesMedicamentoInsumos(int codigoPk)
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarSolicitudesMedicamentoInsumos(codigoPk);
	}


	@Override
	public HashMap consultarOrdenesCirugias(int codigoPk) 
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarOrdenesCirugias(codigoPk);
	}


	@Override
	public HashMap consultarSolicitudesInterconsultas(int codigoPk) 
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarSolicitudesInterconsultas(codigoPk);
	}
	
	/**
	 * @see com.princetonsa.dao.historiaClinica.ImpresionResumenAtencionesDao#consultarPeticionesCirugiaImpresionHcXNumeroSolicitud(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer)
	 */
	public  HashMap consultarPeticionesCirugiaImpresionHcXNumeroSolicitud(Connection con, String cuenta, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion, String solicitudesFactura,Integer numeroSolicitud){
		return SqlBaseImpresionResumenAtencionesDao.consultarPeticionesCirugiaImpresionHcXNumeroSolicitud(con, cuenta, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion, solicitudesFactura, numeroSolicitud);
	}
	
	/**
	 * @see com.princetonsa.dao.historiaClinica.ImpresionResumenAtencionesDao#consultarTiposNotasRecuperacion(java.sql.Connection, int)
	 */
	public  HashMap consultarTiposNotasRecuperacion (Connection con, int numeroSolicitud) throws SQLException
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarTiposNotasRecuperacion(con, numeroSolicitud);
	}
	
	/**
	 * @param codigoCuenta
	 * @return
	 */
	public  List<DtoObservacionesGeneralesOrdenesMedicas> consultarObservacionesOrdenMedica( int codigoCuenta)
	{
		return SqlBaseImpresionResumenAtencionesDao.consultarObservacionesOrdenMedica(codigoCuenta);
	}

	/** (non-Javadoc)
	 * @see com.princetonsa.dao.historiaClinica.ImpresionResumenAtencionesDao#obtenerValoraciones(java.sql.Connection, java.util.HashMap, boolean)
	 */
	@Override
	public HashMap<Object, Object> obtenerValoraciones(Connection con, HashMap<Object, Object> parametros) {
		return SqlBaseImpresionResumenAtencionesDao.obtenerValoraciones(con, parametros);
	}

	/** (non-Javadoc)
	 * @see com.princetonsa.dao.historiaClinica.ImpresionResumenAtencionesDao#obtenerDatosUltimaValoracionConsultaExternaXCuenta(java.sql.Connection, int)
	 */
	@Override
	public InfoIngresoDto obtenerDatosUltimaValoracionConsultaExternaXCuenta(Connection con, int idCuenta) {
		
		String consulta = "SELECT * FROM(" +
							"SELECT " +
							"cu.id_ingreso AS idIngreso, " +
							"val.fecha_valoracion AS fecha, " +
						 	"val.hora_valoracion AS hora " +
						 	"FROM valoraciones val " +
							"INNER JOIN solicitudes sol  " +
							"ON(sol.numero_solicitud = val.numero_solicitud) " +
							"INNER JOIN cuentas cu " +
							"ON(cu.id = sol.cuenta) " +
							"WHERE sol.cuenta = ? " +
							"ORDER BY val.fecha_valoracion DESC, val.hora_valoracion DESC) " +
						  "WHERE  rownum <=1 ";	
		
		return SqlBaseImpresionResumenAtencionesDao.obtenerDatosUltimaValoracionConsultaExternaXCuenta(con, idCuenta, consulta);
	}
}
