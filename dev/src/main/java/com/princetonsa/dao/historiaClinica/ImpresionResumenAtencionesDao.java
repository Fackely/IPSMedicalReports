package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.princetonsa.dto.manejoPaciente.DtoObservacionesGeneralesOrdenesMedicas;
import com.princetonsa.mundo.PersonaBasica;
import com.servinte.axioma.dto.historiaClinica.InfoIngresoDto;
import com.servinte.axioma.generadorReporte.historiaClinica.DtoFiltroImpresionHistoriaClinica;
import com.servinte.axioma.generadorReporte.historiaClinica.DtoResultadoImpresionHistoriaClinica;

public interface ImpresionResumenAtencionesDao
{

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultarAdminMedicamentos(Connection con, HashMap vo,Integer numeroArticulo);

	/**
	 * M�todo que consulta el detalle de adminsitraci�n de un articulo
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultarDetalleArticuloAdmin(Connection con, HashMap vo);
	
	
	public abstract HashMap consultarDetalleArticuloAdminE(Connection con, int artppal, String ingreso);
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultarInsumos(Connection con, HashMap vo);
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultarRespuestaInterpretacionProcedimientos(Connection con, HashMap vo);


	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultarOrdenesTipoMonitore(Connection con, HashMap vo);

	

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultarOrdenesSoporteRespiratorio(Connection con, HashMap vo);


	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultarOrdenesDieta(Connection con, HashMap vo);


	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultarOrdenesCirugias(Connection con, HashMap vo);


	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultarSolicitudesMedicamentoInsumos(Connection con, HashMap vo);


	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultarSolicitudesProcedimientos(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultarSolicitudesProcedimientos(int numeroSolicitud)throws Exception;

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultarSolicitudesInterconsultas(Connection con, HashMap vo);


	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultarOrdenMedica(Connection con, HashMap vo,Integer ingreso);
	

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultarOrdenMedica(int numeroSolicitud,Integer ingreso,Integer cuenta);


	/**
	 * 
	 * @param con
	 * @param codigoEstado
	 * @param tipoSolicitud
	 * @param vo
	 * @return
	 */
	public abstract HashMap obtenerSolicitudesEstadoTipoFiltro(Connection con, int[] codigoEstado, int tipoSolicitud, HashMap vo);

	

	/**
	 * Metodo para cargar informacion dados unos para metros de busqueda sobre una consulta especifica. 
	 * @param con
	 * @param mp
	 * @return
	 */
	public abstract HashMap consultarInformacion(Connection con, HashMap mp);
	
	/**
	 * evoluciones
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap obtenerEvoluciones(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public abstract HashMap obtenerEncabezadoPaciente(Connection con, String idIngreso);


	/**
	 * 
	 * @param con
	 * @param map
	 * @return
	 */
	public abstract HashMap consultarSoporteRespiratorio(Connection con, HashMap map);
	
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
	public HashMap consultarPeticionesCirugiaImpresionHc(Connection con, String cuenta, String cuentaAsocio, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion, String solicitudesFacturas);
	
	/**
	 * M�todo que consulta los encabezados de cirug�a y preanestesia  para la hoja 
	 * de anestesia de cirug�as
	 * @param con
	 * @param listadoPeticiones
	 * @return
	 */
	public HashMap consultarEncabezadosHojaAnestesia(Connection con, String listadoPeticiones);
	
	/**
	 * M�todo que consulta los ex�menes de laboratorio de preanestesia
	 * de acuerdo a la lista de peticiones que cumplen el filtro de impresion hc
	 * @param con
	 * @param listadoPeticiones
	 * @return
	 */
	public HashMap consultarExamenesLaboratorioPreanestesia(Connection con, String listadoPeticiones);
	
	/**
	 * M�todo que consulta el hist�rico de los ex�menes f�sicos de preanestesia, text y textarea
	 * de acuerdo a la lista de peticiones que cumplen el filtro de impresion hc
	 * @param con
	 * @param listadoPeticiones
	 * @param esTexto
	 * @return
	 */
	public HashMap consultarHistoExamenesFisicos(Connection con, String listadoPeticiones, boolean esTexto);
	
	/**
	 * M�todo que consulta las conclusiones de preanestesia
	 * de acuerdo a la lista de peticiones que cumplen el filtro de impresion hc
	 * @param con
	 * @param listadoPeticiones
	 * @return
	 */
	public HashMap consultarHistoConclusiones(Connection con, String listadoPeticiones);
	
	/**
	 * M�todo que consulta el hist�rico de las secciones de la hoja de anestesia
	 * de acuerdo al parametro nroConsulta y a la lista de solicitudes que cumplen el filtro de impresion hc
	 * @param con
	 * @param listadoSolicitudes
	 * @param nroConsulta
	 * @return
	 */
	public HashMap consultarHistoSeccionesHojaAnestesia(Connection con, String listadoSolicitudes, int nroConsulta);
	
	/**
	 * M�todo que consulta el hist�rico de las t�cnicas de anestesia 
	 * @param con
	 * @param listadoSolicitudes
	 * @return
	 */
	public Collection consultarHistoTecnicaAnestesia(Connection con, String listadoSolicitudes);
	
	/**
	 * M�todo que consulta el hist�rico de signos vitales, los tiempos y
	 * los valores de cada uno de los signos vitales
	 * @param con
	 * @param listadoSolicitudes
	 * @param nroConsulta  1-> Tiempos Signos Vitales
	 * 					   2-> Valores Signos Vitales	
	 * @return
	 */
	public Collection consultarHistoSignosVitales(Connection con, String listadoSolicitudes, int nroConsulta);

	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap obtenerEnlacesValoracionesConsultaExterna(Connection con, HashMap vo) ;

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultarOrdenesAmbulatorias(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap obtenerConsultasPYP(Connection con, HashMap vo);

	public abstract HashMap consultarResultatadosLaboratorios(Connection con,
			String idIngreso, String fechaInicial, String fechaFinal,
			String horaInicial, String horaFinal);

	public abstract HashMap consultarrValoracionesEnfermeria(Connection con,
			String idIngreso, String fechaInicial, String fechaFinal,
			String horaInicial, String horaFinal);

	/**
	 * 
	 * @param con
	 * @param filtro
	 * @return
	 */
	public abstract ArrayList<DtoResultadoImpresionHistoriaClinica> consultarInformacionHistoriaClinica(Connection con, DtoFiltroImpresionHistoriaClinica filtro,int identificadorSeccion);

	/** Tipo Modificacion: Segun incidencia 5055
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
	public abstract ArrayList<DtoResultadoImpresionHistoriaClinica> consultarInformacionHistoriaClinica(Connection con, DtoFiltroImpresionHistoriaClinica filtro,int identificadorSeccion,PersonaBasica paciente);
	public abstract ArrayList<DtoResultadoImpresionHistoriaClinica> consultarInformacionHistoriaClinica(Connection con, DtoFiltroImpresionHistoriaClinica filtro,int identificadorSeccion,PersonaBasica paciente, String IdIngreso);
	
	/**
	 * 
	 * @param codigoRegistroEnfermeria
	 * @return
	 */
	public abstract Collection consultarSignosVitalesFijosHistoImpresionHC(int codigoRegistroEnfermeria);

	/**
	 * 
	 * @param codigoRegistroEnfermeria
	 * @return
	 */
	public abstract Collection consultarSignosVitalesParamHistoImpresionHC(int codigoRegistroEnfermeria);

	/**
	 * 
	 * @param codigoRegistroEnfermeria
	 * @return
	 */
	public abstract Collection consultarSignosVitalesHistoTodosImpresionHC(int codigoRegistroEnfermeria);

	/**
	 * 
	 * @param codigoPk
	 * @return
	 */
	public abstract HashMap consultarSolicitudesMedicamentoInsumos(int codigoPk);

	/**
	 * 
	 * @param codigoPk
	 * @return
	 */
	public abstract HashMap consultarOrdenesCirugias(int codigoPk);

	/**
	 * 
	 * @param codigoPk
	 * @return
	 */
	public abstract HashMap consultarSolicitudesInterconsultas(int codigoPk);
	
	/**
	 * @param con
	 * @param cuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion
	 * @param solicitudesFactura
	 * @param numeroSolicitud
	 * @return
	 */
	public  HashMap consultarPeticionesCirugiaImpresionHcXNumeroSolicitud(Connection con, String cuenta, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion, String solicitudesFactura,Integer numeroSolicitud);
	
	
	/**
	 * @param con
	 * @param numeroSolicitud
	 * @return  informacion de las notas de recuperacion 
	 * @throws SQLException
	 */
	public  HashMap consultarTiposNotasRecuperacion (Connection con, int numeroSolicitud) throws SQLException;
	
	/**
	 * @param codigoCuenta
	 * @return
	 */
	public  List<DtoObservacionesGeneralesOrdenesMedicas> consultarObservacionesOrdenMedica( int codigoCuenta);
	
	/**
	 * Metodo encargado de consultar las Valoraciones de un ingreso especifico
	 * @param con
	 * @param parametros
	 * @return HashMap<Object, Object>
	 * @author hermorhu
	 */
	public HashMap<Object, Object> obtenerValoraciones(Connection con, HashMap<Object, Object> parametros);
	
	/**
	 * Metodo encargado de consultar la fecha y hora de la ultima valoracion de Consulta Externa para 
	 * una cuenta determinada
	 * @param con
	 * @param idCuenta
	 * @return InfoIngresoDto
	 * @author hermorhu
	 * @created 24-Abr-2013
	 */
	public InfoIngresoDto obtenerDatosUltimaValoracionConsultaExternaXCuenta(Connection con, int idCuenta);

}
