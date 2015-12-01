package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.EgresoDao;
import com.princetonsa.dao.historiaClinica.EvolucionesDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseEvolucionesDao;
import com.princetonsa.dto.historiaClinica.DtoEvolucion;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.servinte.axioma.dto.manejoPaciente.InformacionCentroCostoValoracionDto;

public class Evoluciones 
{
	
	
	/**
	 * 
	 */
	private static Logger logger= Logger.getLogger(Evoluciones.class);
	
	
	private EvolucionesDao evolucionDao= null;
	
	private EgresoDao egresoDao= null;
	
	
	
	private DtoEvolucion evolucion;
	
	private DtoPlantilla plantilla;
	
	
	private ActionErrors errores = new ActionErrors();
	
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (evolucionDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			evolucionDao = myFactory.getEvolucionesDao();
		}
		
		if (egresoDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			egresoDao = myFactory.getEgresoDao();
		}	
	}
	
	/**
	 * 
	 *
	 */
	public Evoluciones()
	{
		this.init(System.getProperty("TIPOBD"));
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static EvolucionesDao evolucionDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEvolucionesDao();
	}

	
	/**
	 * 
	 *
	 */
	public void clean()
	{
		this.evolucion= new DtoEvolucion();
		this.plantilla= new DtoPlantilla();
	}
	
	
	/**
	 * 
	 * @param con
	 * @param evolucion
	 * @return
	 */
	public static DtoEvolucion cargarEvolucion(Connection con, String evolucion)
	{
		return evolucionDao().cargarEvolucion(con, evolucion);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param valoracion
	 * @param diagnosticoComplicacion
	 * @param diagnosticoComplicacionCie
	 * @param fechaEvolucion
	 * @param horaEvolucion
	 * @param datosSubjetivos
	 * @param analisis
	 * @param hallazgosImportantes
	 * @param tipoMonitoreo
	 * @param planManejo
	 * @param codigoMedico
	 * @param ordenSalida
	 * @param tipoEvolucion
	 * @param recargo
	 * @param cobrable
	 * @param tipoDiagnosticoPrincipal
	 * @param datosMedico
	 * @param centroCosto
	 * @param conductaSeguir
	 * @param tipoReferencia
	 * @param diasIncapacidad
	 * @param observacionesIncapacidad
	 * @return
	 */
	public static int insertarEvolucionBase(
			Connection con,
			int valoracion,			
			String diagnosticoComplicacion,			
			String diagnosticoComplicacionCie,
			String fechaEvolucion,
			String horaEvolucion,
			String datosSubjetivos,
			String analisis,
			String hallazgosImportantes,
			int tipoMonitoreo,
			String planManejo,
			int codigoMedico,
			boolean ordenSalida,
			int tipoEvolucion,
			int recargo,
			boolean cobrable,
			int tipoDiagnosticoPrincipal,
			String datosMedico,
			int centroCosto,
			int conductaSeguir,
			String tipoReferencia,
			int diasIncapacidad,
			String observacionesIncapacidad, 
			String especialidadProfesionalResponde
			)
	{
		HashMap mapa = new HashMap();
		mapa.put("valoracion",valoracion);		
		mapa.put("diagnostico_complicacion",diagnosticoComplicacion);
		mapa.put("diagnostico_complicacion_cie",diagnosticoComplicacionCie);
		mapa.put("fecha_evolucion",fechaEvolucion);
		mapa.put("hora_evolucion",horaEvolucion);
		mapa.put("datos_subjetivos",datosSubjetivos);		
		mapa.put("analisis",analisis);	
		mapa.put("hallazgos_importantes",hallazgosImportantes);
		mapa.put("tipo_monitoreo",tipoMonitoreo);
		mapa.put("plan_manejo",planManejo);
		mapa.put("codigo_medico",codigoMedico);
		mapa.put("orden_salida",ordenSalida);
		mapa.put("tipo_evolucion",tipoEvolucion);
		mapa.put("recargo",recargo);
		mapa.put("cobrable",cobrable);
		mapa.put("tipo_diagnostico_principal",tipoDiagnosticoPrincipal);
		mapa.put("datos_medico",datosMedico);
		mapa.put("centro_costo",centroCosto);
		mapa.put("conducta_seguir",conductaSeguir);
		mapa.put("tipo_referencia",tipoReferencia);
		mapa.put("dias_incapacidad", diasIncapacidad);
		mapa.put("observaciones_incapacidad", observacionesIncapacidad);
		mapa.put("fecha_grabacion",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mapa.put("hora_grabacion",UtilidadFecha.getHoraActual());
		mapa.put("especialidadProfesional", especialidadProfesionalResponde);
		return evolucionDao().insertarEvolucionBase(con, mapa);
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param consecutivoEvolucion
	 * @param datosSubjetivos
	 * @param datosObjetivos
	 * @param hallazgosEpicrisis
	 * @param analisis
	 * @param diagnosticosDefinitivos
	 * @param balanceLiquidos
	 * @param planManejo
	 * @return
	 */
	public static boolean insertarEvolucionesHospitalarias(
			Connection con, 
			int consecutivoEvolucion, 
			boolean datosSubjetivos, 
			boolean hallazgosEpicrisis, 
			boolean analisis, 
			boolean diagnosticosDefinitivos, 
			boolean balanceLiquidos, 
			boolean planManejo
			) 
	{
		
		HashMap mapa = new HashMap();
		
		mapa.put("consecutivo_evolucion", consecutivoEvolucion);
		mapa.put("datos_subjetivos", datosSubjetivos);
		mapa.put("hallazgos_epicrisis", hallazgosEpicrisis);
		mapa.put("analisis", analisis);
		mapa.put("diagnosticos_definitivos", diagnosticosDefinitivos);
		mapa.put("balance_liquidos", balanceLiquidos);
		mapa.put("plan_manejo", planManejo);
		
		return evolucionDao().insertarEvolucionesHospitalarias(con, mapa);
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param consecutivoEvolucion
	 * @param signoVital
	 * @param descripcion
	 * @param valor
	 * @return
	 */
	public static boolean insertarEvolSignosVitales(
			Connection con,
			int consecutivoEvolucion,
			int signoVital,
			String descripcion,
			String valor
			)
	{
		
		HashMap mapa = new HashMap();
		
		mapa.put("consecutivo_evolucion", consecutivoEvolucion);
		mapa.put("signo_vital", signoVital);
		mapa.put("descripcion", descripcion);
		mapa.put("valor", valor);
		
		return evolucionDao().insertarEvolSignosVitales(con, mapa);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param consecutivoEvolucion
	 * @param acronimoDiagnostico
	 * @param tipoCieDiagnostico
	 * @param numero
	 * @param principal
	 * @param definitivo
	 * @return
	 */
	public static boolean insertarDiagnosticosEvolucion(
			Connection con,
			int consecutivoEvolucion,
			String acronimoDiagnostico,
			String tipoCieDiagnostico,
			int numero,
			boolean principal,
			boolean definitivo
			)
	
	{
		
		HashMap mapa = new HashMap();
		
		mapa.put("consecutivo_evolucion", consecutivoEvolucion);
		mapa.put("acronimo_diagnostico", acronimoDiagnostico);
		mapa.put("tipo_cie_diagnostico", tipoCieDiagnostico);
		mapa.put("numero", numero);
		mapa.put("principal", principal);
		mapa.put("definitivo", definitivo);
		
		return evolucionDao().insertarDiagnosticosEvolucion(con, mapa);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param consecutivoEvolucion
	 * @param balanceLiquidos
	 * @param valor
	 * @return
	 */
	public static boolean insertarBalanceLiquidosEvol(
			Connection con,
			int consecutivoEvolucion,
			int balanceLiquidos,
			String valor)
	
	{
		
		HashMap mapa = new HashMap();
		
		mapa.put("consecutivo_evolucion", consecutivoEvolucion);
		mapa.put("balance_liquidos", balanceLiquidos);
		mapa.put("valor", valor);
		
		return evolucionDao().insertarBalanceLiquidosEvol(con, mapa);
		
	}
	
	
	/**
	 * 
	 * @return
	 */
	public DtoEvolucion getEvolucion() {
		return evolucion;
	}

	/**
	 * 
	 * @param evolucion
	 */
	public void setEvolucion(DtoEvolucion evolucion) {
		this.evolucion = evolucion;
	}

	/**
	 * 
	 * @return
	 */
	public DtoPlantilla getPlantilla() {
		return plantilla;
	}

	/**
	 * 
	 * @param plantilla
	 */
	public void setPlantilla(DtoPlantilla plantilla) {
		this.plantilla = plantilla;
	}




	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @param consecutivoEvolucion
	 * @param estadoSalida
	 * @param codigoDestinoSalida
	 * @param otroDestinoSalida
	 * @param numeroAutorizacion
	 * @param causaExterna
	 * @param acronimoDiagnosticoMuerte
	 * @param diagnosticoMuerteCie
	 * @param acronimoDiagnosticoPrincipal
	 * @param diagnosticoPrincipalCie
	 * @param acronimoDiagnosticoRelacionado1
	 * @param diagnosticoRelacionado1Cie
	 * @param acronimoDiagnosticoRelacionado2
	 * @param diagnosticoRelacionado2Cie
	 * @param acronimoDiagnosticoRelacionado3
	 * @param diagnosticoRelacionado3Cie
	 * @param codigoMedico
	 * @param acronimoDiagnosticoComplicacion
	 * @param diagnosticoComplicacionCie
	 */
	public boolean insertarEgresoAutomatico(Connection con, int codigoCuenta, int consecutivoEvolucion, boolean estadoSalida, int codigoDestinoSalida, String otroDestinoSalida, String numeroAutorizacion, int causaExterna, String acronimoDiagnosticoMuerte, int diagnosticoMuerteCie, String acronimoDiagnosticoPrincipal, int diagnosticoPrincipalCie, String acronimoDiagnosticoRelacionado1, int diagnosticoRelacionado1Cie, String acronimoDiagnosticoRelacionado2, int diagnosticoRelacionado2Cie, String acronimoDiagnosticoRelacionado3, int diagnosticoRelacionado3Cie, int codigoMedico, String acronimoDiagnosticoComplicacion, int diagnosticoComplicacionCie) 
	{
		
		try 
		{
			if(egresoDao.crearEgresoDesdeEvolucionTransaccional(
					con, 
					codigoCuenta, 
					consecutivoEvolucion, 
					estadoSalida, 
					codigoDestinoSalida, 
					otroDestinoSalida, 
					numeroAutorizacion, 
					causaExterna, 
					acronimoDiagnosticoMuerte, 
					diagnosticoMuerteCie, 
					acronimoDiagnosticoPrincipal, 
					diagnosticoPrincipalCie, 
					acronimoDiagnosticoRelacionado1, 
					diagnosticoRelacionado1Cie, 
					acronimoDiagnosticoRelacionado2, 
					diagnosticoRelacionado2Cie, 
					acronimoDiagnosticoRelacionado3, 
					diagnosticoRelacionado3Cie, 
					codigoMedico, 
					acronimoDiagnosticoComplicacion, 
					diagnosticoComplicacionCie,
					ConstantesBD.continuarTransaccion)>0)
				return true;
				
				
				errores.add("",new ActionMessage("errors.problemasGenericos","insertando el egreso automático"));
		} 
		catch (SQLException e) 
		 {
			logger.error("Error en insertarEgresoAutomatico: "+e);
			errores.add("",new ActionMessage("errors.problemasGenericos","insertando el egreso automático: "+e));
		}
		
		return false;
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @param tipoMonitoreo
	 * @param consecutivoEvolucion
	 * @param medico
	 * @return
	 */
	public boolean insertarIngresoCuidadoEspecial(
			Connection con, 
			int codigoIngreso, 
			String tipoMonitoreo, 
			int consecutivoEvolucion, 
			UsuarioBasico medico) 
	{
		
		HashMap mapa= new HashMap();
		
		mapa.put("id_ingreso", codigoIngreso);
		mapa.put("estado_ingreso", ConstantesIntegridadDominio.acronimoEstadoActivo);
		mapa.put("indicativo_ingreso", ConstantesIntegridadDominio.acronimoManual	);
		mapa.put("tipo_monitoreo", tipoMonitoreo);
		mapa.put("usuario_resp", medico.getLoginUsuario());
		mapa.put("fecha_resp", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mapa.put("hora_resp", UtilidadFecha.getHoraActual());
		mapa.put("usuario_modifica", medico.getLoginUsuario());
		mapa.put("fecha_modifica", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mapa.put("hora_modifica", UtilidadFecha.getHoraActual());
		mapa.put("evolucion", consecutivoEvolucion);
		
		return evolucionDao().insertarIngresoCuidadoEspecial(con,mapa);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @param codigoTipoEvolucion
	 * @return
	 */
	public int consultarCausaExternaValoracion(Connection con, int codigoCuenta, int codigoTipoEvolucion) 
	{
		return evolucionDao().consultarCausaExternaValoracion(con, codigoCuenta, codigoTipoEvolucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public int obtenerCodigoUltimaEvolucion(Connection con, int codigoCuenta) 
	{
		return evolucionDao().obtenerCodigoUltimaEvolucion(con, codigoCuenta);
	}

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @param procedQuirurgicosObst
	 * @param consecutivoEvolucion
	 * @param centroCostoMonitoreo
	 * @param medico
	 * @return
	 */
	public boolean actualizarIngresoCuidadoEspecial(
			Connection con, 
			int codigoIngreso, 
			String procedQuirurgicosObst, 
			int consecutivoEvolucion, 
			int centroCostoMonitoreo, 
			UsuarioBasico medico) 
	{
		
		HashMap mapa= new HashMap();
		
		mapa.put("id_ingreso", codigoIngreso);
		mapa.put("tipo_monitoreo", procedQuirurgicosObst);
		mapa.put("codigo_evolucion", consecutivoEvolucion);
		mapa.put("area_monitoreo", centroCostoMonitoreo);
		mapa.put("usuario_modifica", medico.getLoginUsuario());
		mapa.put("fecha_modifica", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mapa.put("hora_modifica", UtilidadFecha.getHoraActual());
		
		return evolucionDao().actualizarIngresoCuidadoEspecial(con, mapa);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEvolucion
	 * @return
	 */
	public String[] obtenerDiagnosticoComplicacion(Connection con, int codigoEvolucion) 
	{
		return evolucionDao().obtenerDiagnosticoComplicacion(con, codigoEvolucion);
	}

	/**
	 * 
	 * @param con
	 * @param codigoEvolucion
	 * @return
	 */
	public String[] obtenerDiagnosticoPrincipal(Connection con, int codigoEvolucion) 
	{
		return evolucionDao().obtenerDiagnosticoPrincipal(con, codigoEvolucion);
	}

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public String[] consultarTipoMonitoreo(Connection con, int codigoIngreso) 
	{
		return evolucionDao().consultarTipoMonitoreo(con, codigoIngreso);
	}

	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @param centroCosto
	 * @param medico
	 * @return
	 */
	public boolean actualizarAreaCuenta(
			Connection con, 
			int codigoCuenta, 
			int centroCosto,
			UsuarioBasico medico) 
	{
		HashMap mapa= new HashMap();
		
		mapa.put("id_cuenta", codigoCuenta);
		mapa.put("area", centroCosto);
		mapa.put("usuario_modifica", medico.getLoginUsuario());
		mapa.put("fecha_modifica", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mapa.put("hora_modifica", UtilidadFecha.getHoraActual());
		
		return evolucionDao().actualizarAreaCuenta(con, mapa);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEvolucion
	 * @return
	 */
	public ArrayList<Diagnostico> consultaDiagnosticosRelacionados(Connection con, int codigoEvolucion) 
	{
		return evolucionDao().consultaDiagnosticosRelacionados(con, codigoEvolucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public String[] consultarInfoFallecimiento(Connection con, int codigoCuenta) 
	{
		return evolucionDao().consultarInfoFallecimiento(con, codigoCuenta);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEvolucion
	 * @param seccionFija
	 * @return
	 */
	public static boolean enviadoEpicrisis(Connection con, String codigoEvolucion) 
	{
		return evolucionDao().enviadoEpicrisis(con, codigoEvolucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoEvolucion
	 * @param valor
	 * @param loginUsuario
	 * @return
	 */
	public static boolean insertarComentariosEvolucion(
			Connection con,
			int consecutivoEvolucion, 
			String valor, 
			String loginUsuario) 
	{
		if(System.getProperty("TIPOBD").equals("POSTGRESQL"))
		{
        	Statement stmt;
			try {
				stmt = con.createStatement();
				   stmt.execute("SET standard_conforming_strings TO off");
		            stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         
		}
		
		HashMap mapa = new HashMap();
		
		mapa.put("evolucion", consecutivoEvolucion);
		mapa.put("valor", valor);
		mapa.put("usuario", loginUsuario);
		mapa.put("fecha", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mapa.put("hora", UtilidadFecha.getHoraActual());
		
		return evolucionDao().insertarComentariosEvolucion(con, mapa);
		
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoEvolucion
	 * @return
	 */
	public String[] consultarOrdenEgreso(Connection con, int codigoEvolucion) 
	{
		return evolucionDao().consultarOrdenEgreso(con, codigoEvolucion);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoEvolucion
	 * @return
	 */
	public int obtenerUltimaConducta(Connection con, int codigoEvolucion) 
	{
		return evolucionDao().obtenerUltimaConducta(con, codigoEvolucion);
	}

	
	/**
	 * @see com.princetonsa.dao.historiaClinica.EvolucionesDao#informacionCentroCostoDeIngresoActivoCuidadoEspecialXCuenta(Connection, int)
	 */
	public  InformacionCentroCostoValoracionDto informacionCentroCostoDeIngresoActivoCuidadoEspecialXCuenta(Connection con, int idCuenta){
		return evolucionDao().informacionCentroCostoDeIngresoActivoCuidadoEspecialXCuenta(con, idCuenta);
	}	

}
