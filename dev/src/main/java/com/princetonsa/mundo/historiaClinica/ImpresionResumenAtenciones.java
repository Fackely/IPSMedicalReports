package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import util.Utilidades;

import com.princetonsa.actionform.historiaClinica.ImpresionResumenAtencionesForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.ImpresionResumenAtencionesDao;
import com.princetonsa.dto.manejoPaciente.DtoObservacionesGeneralesOrdenesMedicas;
import com.princetonsa.mundo.PersonaBasica;
import com.servinte.axioma.dto.historiaClinica.InfoIngresoDto;
import com.servinte.axioma.generadorReporte.historiaClinica.DtoFiltroImpresionHistoriaClinica;
import com.servinte.axioma.generadorReporte.historiaClinica.DtoImpresionHistoriaClinica;
import com.servinte.axioma.generadorReporte.historiaClinica.DtoResultadoImpresionHistoriaClinica;

public class ImpresionResumenAtenciones
{
	
	ImpresionResumenAtencionesDao dao;

	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores v�lidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
	    if ( dao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			dao= myFactory.getImpresionResumenAtencionesDao();
			if( dao!= null )
				return true;
		}
		return false;
	}
	
	
	/**
	 * 
	 *
	 */
	public ImpresionResumenAtenciones()
	{
		init(System.getProperty("TIPOBD"));
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
	public HashMap consultarPeticionesCirugiaImpresionHc(Connection con, String cuenta, String cuentaAsocio, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion, String solicitudesFacturas) 
	{
		return dao.consultarPeticionesCirugiaImpresionHc(con, cuenta, cuentaAsocio, fechaInicial,fechaFinal, horaInicial, horaFinal, mostrarInformacion, solicitudesFacturas);
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
		return dao.consultarEncabezadosHojaAnestesia(con, listadoPeticiones);
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
		return dao.consultarExamenesLaboratorioPreanestesia(con, listadoPeticiones);
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
		return dao.consultarHistoExamenesFisicos(con, listadoPeticiones, esTexto);
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
		return dao.consultarHistoConclusiones(con, listadoPeticiones);
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
		return dao.consultarHistoSeccionesHojaAnestesia(con, listadoSolicitudes, nroConsulta);
	}
	
	/**
	 * M�todo que consulta el hist�rico de las t�cnicas de anestesia 
	 * @param con
	 * @param listadoSolicitudes
	 * @return
	 */
	public Collection consultarHistoTecnicaAnestesia(Connection con, String listadoSolicitudes) 
	{
		return dao.consultarHistoTecnicaAnestesia(con, listadoSolicitudes);
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
		return dao.consultarHistoSignosVitales(con, listadoSolicitudes, nroConsulta);
	}

	
	/**
	 * 
	 * @param con 
	 * @param vo
	 * @return
	 */
	public HashMap consultarAdminMedicamentos(Connection con, HashMap vo,Integer numeroArticulo)
	{
		return dao.consultarAdminMedicamentos(con,vo,numeroArticulo);
	}
	
	/**
	 * M�todo que consulta el detalle de adminsitraci�n de un articulo
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap consultarDetalleArticuloAdmin(Connection con, HashMap vo)
	{
		return dao.consultarDetalleArticuloAdmin(con, vo);
	}
	
	public HashMap consultarDetalleArticuloAdminE(Connection con, int artppal, String ingreso)
	{
		return dao.consultarDetalleArticuloAdminE(con, artppal,ingreso);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap consultarInsumos(Connection con, HashMap vo)
	{
		return dao.consultarInsumos(con,vo);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap consultarRespuestaInterpretacionProcedimientos(Connection con, HashMap vo)
	{
		return dao.consultarRespuestaInterpretacionProcedimientos(con,vo);
	}


	/**
	 * 
	 * @param con
	 * @param map
	 * @return
	 */
	public HashMap consultarOrdenesTipoMonitore(Connection con, HashMap vo)
	{
		return dao.consultarOrdenesTipoMonitore(con,vo);
	}


	/**
	 * 
	 * @param con
	 * @param map
	 * @return
	 */
	public HashMap consultarOrdenesSoporteRespiratorio(Connection con, HashMap vo)
	{
		return dao.consultarOrdenesSoporteRespiratorio(con,vo);
	}


	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap consultarOrdenesDieta(Connection con, HashMap vo)
	{
		return dao.consultarOrdenesDieta(con,vo);
	}


	/**
	 * 
	 * @param con
	 * @param map
	 * @return
	 */
	public HashMap consultarOrdenesCirugias(Connection con, HashMap vo)
	{
		return dao.consultarOrdenesCirugias(con,vo);
	}


	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap consultarSolicitudesMedicamentoInsumos(Connection con, HashMap vo)
	{
		return dao.consultarSolicitudesMedicamentoInsumos(con,vo);
	}


	/**
	 * 
	 * @param con
	 * @param map
	 * @return
	 */
	public HashMap consultarSolicitudesProcedimientos(Connection con, HashMap vo)
	{
		return dao.consultarSolicitudesProcedimientos(con,vo);
	}
	
	/**
	 * 
	 * @param con
	 * @param map
	 * @return
	 */
	public static HashMap consultarSolicitudesProcedimientos(int numeroSolicitud)throws Exception
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getImpresionResumenAtencionesDao().consultarSolicitudesProcedimientos(numeroSolicitud);
	}


	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap consultarSolicitudesInterconsultas(Connection con, HashMap vo)
	{
		return dao.consultarSolicitudesInterconsultas(con,vo);
	}


	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap consultarOrdenMedica(Connection con, HashMap vo,Integer ingreso)
	{
		return dao.consultarOrdenMedica(con,vo,ingreso);
	}

	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarOrdenMedica(int numeroSolicitud,Integer ingreso,Integer cuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getImpresionResumenAtencionesDao().consultarOrdenMedica(numeroSolicitud,ingreso,cuenta);
	}


	/**
	 * 
	 * @param con
	 * @param codigoEstado
	 * @param tipoSolicitud
	 * @param vo
	 * @return
	 */

	public HashMap obtenerSolicitudesEstadoTipoFiltro(Connection con, int[] codigoEstado, int tipoSolicitud,HashMap vo)
	{
		return dao.obtenerSolicitudesEstadoTipoFiltro(con,codigoEstado,tipoSolicitud,vo);
	}


	/**
	 * Metodo para cargar informacion dados unos para metros de busqueda sobre una consulta especifica.
	 * @param con
	 * @param mp
	 */
	public HashMap consultarInformacion(Connection con, HashMap mp)
	{
		return dao.consultarInformacion(con, mp); 
	}
	
	/**
	 * evoluciones
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap obtenerEvoluciones(Connection con, HashMap vo)
	{
		return dao.obtenerEvoluciones(con, vo);
	}
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static HashMap obtenerEncabezadoPaciente(Connection con, String idIngreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getImpresionResumenAtencionesDao().obtenerEncabezadoPaciente(con, idIngreso);
	}


	public HashMap consultarSoporteRespiratorio(Connection con, HashMap vo)
	{
		return dao.consultarSoporteRespiratorio(con,  vo);
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap obtenerEnlacesValoracionesConsultaExterna(Connection con, HashMap vo) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getImpresionResumenAtencionesDao().obtenerEnlacesValoracionesConsultaExterna(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param crearValueObjectFiltro
	 * @return
	 */
	public HashMap consultarOrdenesAmbulatorias(Connection con,
			HashMap vo) {
		return dao.consultarOrdenesAmbulatorias(con,vo);
	}

	/**
	 * 
	 * @param con
	 * @param crearValueObjectFiltro
	 * @return
	 */
	public HashMap obtenerConsultasPYP(Connection con,
			HashMap vo) {
		return dao.obtenerConsultasPYP(con,vo);
	}


	public HashMap consultarResultatadosLaboratorios(Connection con,
			String idIngreso, String fechaInicial, String fechaFinal,
			String horaInicial, String horaFinal) {
		return dao.consultarResultatadosLaboratorios(con,idIngreso,fechaInicial,fechaFinal,horaInicial,horaFinal);
	}


	public HashMap consultarrValoracionesEnfermeria(Connection con,
			String idIngreso, String fechaInicial, String fechaFinal,
			String horaInicial, String horaFinal) {
		return dao.consultarrValoracionesEnfermeria(con,idIngreso,fechaInicial,fechaFinal,horaInicial,horaFinal);
	}

	/**
	 * 
	 * @param con
	 * @param filtro
	 * @return
	 */
	public static ArrayList<DtoResultadoImpresionHistoriaClinica> consultarInformacionHistoriaClinica(Connection con, DtoFiltroImpresionHistoriaClinica filtro,int identificadorSeccion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getImpresionResumenAtencionesDao().consultarInformacionHistoriaClinica(con, filtro,identificadorSeccion);
	}

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
	public static ArrayList<DtoResultadoImpresionHistoriaClinica> consultarInformacionHistoriaClinica(Connection con, DtoFiltroImpresionHistoriaClinica filtro,int identificadorSeccion, PersonaBasica paciente) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getImpresionResumenAtencionesDao().consultarInformacionHistoriaClinica(con, filtro,identificadorSeccion,paciente);
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
	public static ArrayList<DtoResultadoImpresionHistoriaClinica> consultarInformacionHistoriaClinica(Connection con, DtoFiltroImpresionHistoriaClinica filtro,int identificadorSeccion, PersonaBasica paciente, String IdIngreso) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getImpresionResumenAtencionesDao().consultarInformacionHistoriaClinica(con, filtro,identificadorSeccion,paciente, IdIngreso);
	}

	/**
	 * 
	 * @param codigoRegistroEnfermeria
	 * @return
	 */
	public static Collection consultarSignosVitalesFijosHistoImpresionHC(int codigoRegistroEnfermeria) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getImpresionResumenAtencionesDao().consultarSignosVitalesFijosHistoImpresionHC(codigoRegistroEnfermeria);
	}


	/**
	 * 
	 * @param codigoRegistroEnfermeria
	 * @return
	 */
	public static Collection consultarSignosVitalesParamHistoImpresionHC(int codigoRegistroEnfermeria) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getImpresionResumenAtencionesDao().consultarSignosVitalesParamHistoImpresionHC(codigoRegistroEnfermeria);
	}


	/**
	 * 
	 * @param codigoRegistroEnfermeria
	 * @return
	 */
	public static Collection consultarSignosVitalesHistoTodosImpresionHC(int codigoRegistroEnfermeria) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getImpresionResumenAtencionesDao().consultarSignosVitalesHistoTodosImpresionHC(codigoRegistroEnfermeria);
	}


	/**
	 * @param codigoPk
	 * @return
	 */
	public static HashMap consultarSolicitudesMedicamentoInsumos(int codigoPk) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getImpresionResumenAtencionesDao().consultarSolicitudesMedicamentoInsumos(codigoPk);
	}


	/**
	 * @param codigoPk
	 * @return
	 */
	public static HashMap consultarOrdenesCirugias(int codigoPk) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getImpresionResumenAtencionesDao().consultarOrdenesCirugias(codigoPk);
	}


	/**
	 * @param codigoPk
	 * @return
	 */
	public static HashMap consultarSolicitudesInterconsultas(int codigoPk) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getImpresionResumenAtencionesDao().consultarSolicitudesInterconsultas(codigoPk);
	}
	
	/**
	 * @param con
	 * @param mp
	 * @return
	 */
	public HashMap consultarOtrosAntecedentes(Connection con, HashMap mp)
	{
		return dao.consultarInformacion(con, mp); 
	}
	
	/**
	 * @param con
	 * @param cuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion
	 * @param solicitudesFacturas
	 * @param numeroSolicitud
	 * @return HashMap de peticiones
	 */
	public HashMap consultarPeticionesCirugiaImpresionHcXNumeroSolicitud(Connection con, String cuenta, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion, String solicitudesFacturas,Integer numeroSolicitud) 
	{
		return dao.consultarPeticionesCirugiaImpresionHcXNumeroSolicitud(con, cuenta, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion, solicitudesFacturas, numeroSolicitud);
	}

	/**
	 * @param con
	 * @param numeroSolicitud
	 * @return  informacion de notas de recuperacion
	 * @throws SQLException
	 */
	public  HashMap consultarTiposNotasRecuperacion (Connection con, int numeroSolicitud) throws SQLException
	{
		return dao.consultarTiposNotasRecuperacion(con, numeroSolicitud);
	}
	
	/**
	 * @param numeroSolicitud
	 * @return
	 */
	public static List<DtoObservacionesGeneralesOrdenesMedicas> consultarOrdenMedica(int numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getImpresionResumenAtencionesDao().consultarObservacionesOrdenMedica(numeroSolicitud);
	}
	
	/**
	 * Metodo encargado de consultar las Valoraciones de un ingreso especifico
	 * @param con
	 * @param parametros
	 * @return HashMap<Object, Object>
	 * @author hermorhu
	 */
	public HashMap<Object, Object> obtenerValoraciones(Connection con, HashMap<Object, Object> parametros)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getImpresionResumenAtencionesDao().obtenerValoraciones(con, parametros);
	}
	
	/**
	 * Metodo encargado de consultar la fecha y hora de la ultima valoracion de Consulta Externa para 
	 * una cuenta determinada
	 * @param con
	 * @param idCuenta
	 * @return InfoIngresoDto
	 * @author hermorhu
	 * @created 24-Abr-2013
	 */
	public InfoIngresoDto obtenerDatosUltimaValoracionConsultaExternaXCuenta(Connection con, int idCuenta){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getImpresionResumenAtencionesDao().obtenerDatosUltimaValoracionConsultaExternaXCuenta(con, idCuenta);
	}
	
	/**
	 * Metodo que valida si existe informacion en la historia clinica de un paciente en el flujo del boton resumen HC
	 * @param   forma 
	 * @param   Impresion Historia Clinica 
	 * @return  NoInformacionHC
	 * @author  leoquico
	 * @created 26/07/2013 
	 */
		
	public boolean validarHistoriaClinica(ImpresionResumenAtencionesForm forma, DtoImpresionHistoriaClinica dto){
		
		boolean noInformacionHC = false;
		int numero = 0;
		if (forma.getImpresionResumenHC().equals("Valoracion, Interconsultas y Evoluciones")
		    && dto.getResultadoSeccionEvolucion().size() == 0){
			
			  noInformacionHC = true;
		}
		else if (forma.getImpresionResumenHC().equals("Registro de Enfermeria") &&
			//Valoraciones Enfermeria
			((forma.getValoracionesEnfermeria().containsKey("numRegistros") && 
			Utilidades.convertirAEntero(forma.getValoracionesEnfermeria().get("numRegistros")+"") == 0)
			|| forma.getValoracionesEnfermeria().size() == 0) &&	
			//Signos Vitales
			(forma.getSignosVitalesHistoTodos().isEmpty() && forma.getSignosVitalesHistoTodos().size() == 0) &&
			//Soporte Respiratorio
			((forma.getSoporteRespiratorio().containsKey("numRegistros") 
			&& Utilidades.convertirAEntero(forma.getSoporteRespiratorio().get("numRegistros")+"") == 0) 
			|| forma.getSoporteRespiratorio().size() == 0) &&
			//Control liquidos
			((forma.getMapaControlLiq().containsKey("numRegistros") && 
			Utilidades.convertirAEntero(forma.getMapaControlLiq().get("numRegistros")+"") == 0)
			|| forma.getMapaControlLiq().size() == 0) &&	
			//Cateteres y Sondas
			((dto.getCateteresSondas().containsKey("numRegistros") && 
			Utilidades.convertirAEntero(dto.getCateteresSondas().get("numRegistros")+"") == 0)
			|| dto.getCateteresSondas().size() == 0) &&
			//Cuidados Especiales
			((forma.getMapaHistoricoCuidadosEspeciales().containsKey("numRegistros") && 
			Utilidades.convertirAEntero(forma.getMapaHistoricoCuidadosEspeciales().get("numRegistros")+"") == 0)
			|| forma.getMapaHistoricoCuidadosEspeciales().size() == 0)	&&
			//Notas de Enfermeria
			(forma.getHistoricoAnotacionesEnfermeria().isEmpty() && forma.getHistoricoAnotacionesEnfermeria().size() == 0)
		    //Notas de Recuperacion
		    && forma.getMapaNotasRecuperacion().isEmpty() && forma.getMapaNotasRecuperacion().size() ==0) {
					
			  noInformacionHC = true;
		}
		else if (forma.getImpresionResumenHC().equals("Administracion de Medicamentos") &&
				//Hoja Administracion Medicamentos
				((forma.getAdminMedicamentos().containsKey("numRegistros") && 
				Utilidades.convertirAEntero(forma.getAdminMedicamentos().get("numRegistros")+"") == 0)
				|| forma.getAdminMedicamentos().size() == 0) &&
				//Consumos Insumos
				((forma.getInsumos().containsKey("numRegistros") && 
				Utilidades.convertirAEntero(forma.getInsumos().get("numRegistros")+"") == 0)
				|| forma.getInsumos().size() == 0)) {
			
			  noInformacionHC = true;
			
		}
		else if (forma.getImpresionResumenHC().equals("Paraclinicos") &&
				//Respuesta e Interpretacion de Procedimientos
				((forma.getRepuestaInterpretacionProcedimientos().containsKey("numRegistros") && 
				Utilidades.convertirAEntero(forma.getRepuestaInterpretacionProcedimientos().get("numRegistros")+"") == 0)
				|| forma.getRepuestaInterpretacionProcedimientos().size() == 0)) { 
		    
	
		  	  noInformacionHC = true;
			
		}
		
		return noInformacionHC;
	}
	
}
