/*
 * AGO 13, 2006
 */
package com.princetonsa.mundo.pyp;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.ConstantesBD;
import util.InfoDatos;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.pyp.ProgramasPYPDao;
import com.princetonsa.dto.pyp.DtoObservacionProgramaPYP;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Sebastián Gómez 
 *
 *Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * Programas de Promoción y Prevención 
 */
public class ProgramasPYP 
{
	/**
	 * DAO para el manejo de ProgramasPYPDao
	 */
	ProgramasPYPDao programasDao = null;
	
	//**********ATRIBUTOS*****************************************
	/**
	 *Objeto usado para pasar parámetros al dao
	 */
	HashMap campos = new HashMap();
	//************************************************************
	
	//**********INICIALIZADORES & CONSTRUCTORES***********************
	/**
	 * Constructor
	 */
	public ProgramasPYP() {
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.campos = new HashMap();
	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (programasDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			programasDao = myFactory.getProgramasPYPDao();
		}	
	}
	//****************************************************************
	//***************METODOS********************************************************
	/**
	 * Método implementado para consultar los programas de un paciente de acuerdo
	 * al convenio de su cuenta activa y programas existentes
	 * @param paciente 
	 * @param usuario 
	 * @param listadoConvenios 
	 * @param porConvenio 
	 * @param ultimaCuentaAsociada 
	 * @param pacienteCuentaCerrada 
	 */
	public HashMap consultarProgramasPaciente(Connection con, PersonaBasica paciente, UsuarioBasico usuario, String listadoConvenios, boolean porConvenio, HashMap ultimaCuentaAsociada, boolean pacienteCuentaCerrada)
	{
		
		this.campos.put("porConvenio",porConvenio);
		//se asigna la institucion
		this.campos.put("institucion",usuario.getCodigoInstitucion());
		//se asigna codigo del paciente
		this.campos.put("codigoPaciente",paciente.getCodigoPersona());
		
		//se verifica si es por convenio
		if(porConvenio)
		{
			///se asigna el convenio
			this.campos.put("listadoConvenios",listadoConvenios);
			
			//**********preparar datos validacion del GRUPO ETAREO********************************************
			//asignacion de las edades del paciente
			this.campos.put("edadAnio",paciente.getEdad());
			this.campos.put("edadMeses",UtilidadFecha.numeroMesesEntreFechas(paciente.getFechaNacimiento(),UtilidadFecha.getFechaActual(),false));
			this.campos.put("edadDias",UtilidadFecha.numeroDiasEntreFechas(paciente.getFechaNacimiento(),UtilidadFecha.getFechaActual()));
			//asignacion del sexo del paciente
			this.campos.put("sexo",paciente.getCodigoSexo());
			//*********************************************************************************************
			
			//******preparar datos validacion de los DIAGNÓSTICOS******************************************
			if(pacienteCuentaCerrada){
				this.campos.put("idCuenta",ultimaCuentaAsociada.get("cuenta"));
				this.campos.put("viaIngreso",ConstantesBD.codigoViaIngresoConsultaExterna);
			} else {	
				this.campos.put("idCuenta",paciente.getCodigoCuenta());
				this.campos.put("viaIngreso",paciente.getCodigoUltimaViaIngreso());
			}
			String diagnosticos = programasDao.consultarDiagnosticosPaciente(con,campos);
			this.campos.put("diagnosticos",diagnosticos);
			//********************************************************************************************
			
			//****preparar datos validacion de informacion. EMBARAZO*********************************
			if(paciente.getCodigoSexo()==ConstantesBD.codigoSexoFemenino&&programasDao.tieneHojaObsSinFinalizar(con,campos))
				this.campos.put("esEmbarazo","true");
			else
				this.campos.put("esEmbarazo","false");
			//****************************************************************************************
		}
		
		//se consultan los programas del paciente x convenio
		return programasDao.consultarProgramasPaciente(con,campos);
	}
	
	/**
	 * Método implementado para insertar un programa PYP a un paciente
	 * @param con
	 * @return
	 */
	public int insertarPrograma(Connection con)
	{
		return programasDao.insertarPrograma(con,campos);
	}
	
	/**
	 * Método implementado para modificar un programa PYP a un paciente
	 * @param con
	 * @return
	 */
	public int modificarPrograma(Connection con)
	{
		return programasDao.modificarPrograma(con,campos);
	}
	
	/**
	 * Método implementado para consultar un programa de un paciente
	 * @param con
	 * @return
	 */
	public HashMap consultarPrograma(Connection con)
	{
		return programasDao.consultarPrograma(con,campos);
	}
	
	/**
	 * Método que consulta las actividades de un programa dependiendo de las
	 * características del paciente
	 * @param con
	 * @param paciente
	 * @param usuario
	 * @param porConvenio
	 * @param ultimaCuentaAsociada 
	 * @param pacienteCuentaCerrada 
	 * @return
	 */
	public HashMap consultarActividadesProgramaPaciente(Connection con, PersonaBasica paciente, UsuarioBasico usuario, boolean porConvenio, boolean pacienteCuentaCerrada, HashMap ultimaCuentaAsociada)
	{
		this.campos.put("porConvenio",porConvenio);
		//se asigna la institucion
		this.campos.put("institucion",usuario.getCodigoInstitucion());
		//se asigna codigo del paciente
		this.campos.put("codigoPaciente",paciente.getCodigoPersona());
		
		
		//se verifica si es por convenio
		if(porConvenio)
		{
			///se asigna el convenio
			if(pacienteCuentaCerrada)
				this.campos.put("codigoConvenio",ultimaCuentaAsociada.get("convenio"));
			else
				this.campos.put("codigoConvenio",paciente.getCodigoConvenio());
			//se asigna la ocupacion medica
			this.campos.put("codigoOcupacion",usuario.getCodigoOcupacionMedica());
			
			//**********preparar datos validacion del GRUPO ETAREO********************************************
			//asignacion de las edades del paciente
			this.campos.put("edadAnio",paciente.getEdad());
			this.campos.put("edadMeses",UtilidadFecha.numeroMesesEntreFechas(paciente.getFechaNacimiento(),UtilidadFecha.getFechaActual(),false));
			this.campos.put("edadDias",UtilidadFecha.numeroDiasEntreFechas(paciente.getFechaNacimiento(),UtilidadFecha.getFechaActual()));
			//asignacion del sexo del paciente
			this.campos.put("sexo",paciente.getCodigoSexo());
			//*********************************************************************************************
			
			//******preparar datos validacion de los DIAGNÓSTICOS******************************************
			if(pacienteCuentaCerrada){
				this.campos.put("idCuenta",ultimaCuentaAsociada.get("cuenta"));
				this.campos.put("viaIngreso",ConstantesBD.codigoViaIngresoConsultaExterna);
			} else {
				this.campos.put("idCuenta",paciente.getCodigoCuenta()==0?paciente.getCodigoCuentaAsocio():paciente.getCodigoCuenta());
				this.campos.put("viaIngreso",paciente.getCodigoUltimaViaIngreso());
			}
			String diagnosticos = programasDao.consultarDiagnosticosPaciente(con,campos);
			this.campos.put("diagnosticos",diagnosticos);
			//********************************************************************************************
			
			//****preparar datos validacion de informacion. EMBARAZO*********************************
			if(paciente.getCodigoSexo()==ConstantesBD.codigoSexoFemenino&&programasDao.tieneHojaObsSinFinalizar(con,campos))
				this.campos.put("esEmbarazo","true");
			else
				this.campos.put("esEmbarazo","false");
			//****************************************************************************************
		}
		
		
		//se consultan las actividades del programa
		HashMap actividades = programasDao.consultarActividadesProgramaPaciente(con,campos);
		
		//************VALIDACION ACCIONES DEPENDIENDO DEL TIPO DE SERVICIO***************************
		for(int i=0;i<Integer.parseInt(actividades.get("numRegistros").toString());i++)
		{
			//Se verifica el tipo de servicio
			String tipoServicio = Utilidades.obtenerTipoServicio(con,actividades.get("codigo_actividad_"+i).toString());
			
			if(tipoServicio.equals(ConstantesBD.codigoServicioInterconsulta+""))
				actividades.put("solicitar_"+i,ValoresPorDefecto.getValorFalseParaConsultas());
			
			if(tipoServicio.equals(ConstantesBD.codigoServicioPartosCesarea+"")||tipoServicio.equals(ConstantesBD.codigoServicioPaquetes+"")||tipoServicio.equals(ConstantesBD.codigoServicioQuirurgico+"")||tipoServicio.equals(ConstantesBD.codigoServicioNoCruentos+"")){
				actividades.put("ejecutar_"+i,ValoresPorDefecto.getValorFalseParaConsultas());
			}
			
			if(actividades.get("codigo_estado_actividad_"+i).equals(ConstantesBD.codigoEstadoProgramaPYPProgramado))
			{
				actividades.put("cancelar_"+i, true);
			}else
			{
				actividades.put("cancelar_"+i, false);
			}
				
			
		}
		//*******************************************************************************************
		
		return actividades;
	}
	
	/**
	 * Método que consulta los articulos x programa de un paciente
	 * @param con
	 * @param paciente
	 * @return
	 */
	public HashMap consultarArticulosProgramaPaciente(Connection con,PersonaBasica paciente, UsuarioBasico usuario)
	{
		//NOTA * Se tiene en cuenta que la llave tipoRegimen y porConvenio ya estan incluidas en el mapa campos
		//si vienen por el flujo normal de PYP (ProgramasPYPAction), de lo contrario se deben asignar
		
		//se asigna la institucion
		this.campos.put("institucion",usuario.getCodigoInstitucion());
		//se asigna codigo del paciente
		this.campos.put("codigoPaciente",paciente.getCodigoPersona());
		
		//**********preparar datos validacion del GRUPO ETAREO********************************************
		//asignacion de las edades del paciente
		this.campos.put("edadAnio",paciente.getEdad());
		this.campos.put("edadMeses",UtilidadFecha.numeroMesesEntreFechas(paciente.getFechaNacimiento(),UtilidadFecha.getFechaActual(),false));
		this.campos.put("edadDias",UtilidadFecha.numeroDiasEntreFechas(paciente.getFechaNacimiento(),UtilidadFecha.getFechaActual()));
		//asignacion del sexo del paciente
		this.campos.put("sexo",paciente.getCodigoSexo());
		//*********************************************************************************************
		//******preparar datos validacion de VÍAS INGRESO************************************************
		this.campos.put("viaIngreso",paciente.getCodigoUltimaViaIngreso());
		this.campos.put("codigoOcupacion",usuario.getCodigoOcupacionMedica());
		
		return programasDao.consultarArticulosProgramaPaciente(con,campos);
	}
	
	/**
	 * Método implementado para insertar una actividad al paciente PYP
	 * @param con
	 * @return
	 */
	public int insertarActividad(Connection con)
	{
		return programasDao.insertarActividad(con,campos);
	}
	
	/**
	 * Método implementado para modificar la actividad pyp de un paciente
	 * @param con
	 * @return
	 */
	public int modificarActividad(Connection con)
	{
		return programasDao.modificarActividad(con,campos);
	}
	
	/**
	 * Método implementado para consultar los centros de atención de una actividad
	 * @param con
	 * @return
	 */
	public String consultarCentrosAtencionActividad(Connection con)
	{
		return programasDao.consultarCentrosAtencionActividad(con,campos);
	}
	
	/**
	 * Método implementado para consultar el histórico de una  actividad
	 * @param con
	 * @return
	 */
	public HashMap consultarHistoricosActividad(Connection con)
	{
		return programasDao.consultarHistoricosActividad(con,campos);
	}
	
	/**
	 * Método implementado para actualizar el acumulado de las actividades pyp ejecutadas de un paciente
	 * 
	 * En el mapa campos se deben añadir las siguientes keys:
	 * codigoPaciente => codigo del paciente
	 * centroAtencion => consecutivo del centro atencion (tabla centro_atencion)
	 * tipoRegimen => acronimo del tipo de regimen
	 * codigoPrograma => campo codigo de la tabla programas_salud_pyp
	 * institucion => campo institucion de la tabla programas_salud_pyp (es llave compuesta)
	 * consecutivoActividad => consecutivo de la actividad (tabla actividades_pyp)
	 * codigoConvenio => codigo del convenio
	 * fecha => fecha en formato AAAA/MM/DD
	 * 
	 * Nota* si no se parametrizan estas keys el método no funciona
	 * @param con
	 * @return
	 */
	public int actualizarAcumuladoActividades(Connection con)
	{
		int resp = 0;
		String consecutivoAcumulado = programasDao.consultarConsecutivoActividadAcumulada(con,campos);
		if(consecutivoAcumulado.equals(""))
		{
			//como no existe acumulado para esta actividad se inserta una nueva
			resp = programasDao.insertarActividadAcumulada(con,campos);
			
		}
		else
		{
			//Se aumenta el acumulado de la actividad ya existente
			this.setCampos("consecutivoAcumulado",consecutivoAcumulado);
			resp = programasDao.aumentarAcumuladoActividad(con,campos);
			
		}
		
		return resp;
	}
	
	/**
	 * Método que verifica si una actividad ya fue ejecutada para la fecha actual
	 * @param con
	 * @return
	 */
	public boolean estaActividadEjecutada(Connection con)
	{
		return programasDao.estaActividadEjecutada(con,campos);
	}
	
	/**
	 * Método que verifica si la actividad ya fue ejecutada para la fecha,
	 * y si la actividad permite ser ejecutada varias veces al día
	 *
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean permiteEjecutarActividad(Connection con)
	{
		return programasDao.permiteEjecutarActividad(con,campos);
	}
	
	/**
	 * Método implementado para consultar la finalidad de una actividad
	 * @param con
	 * @return
	 */
	public String consultarFinalidadActividad(Connection con)
	{
		return programasDao.consultarFinalidadActividad(con,campos);
	}
	
	/**
	 * Método que consulta la finalidad de una consulta PYP
	 * basándose de lo parametrizado de actividades x programa
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static InfoDatos consultarFinalidadActividadConsulta(Connection con,String numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProgramasPYPDao().consultarFinalidadActividadConsulta(con,numeroSolicitud);
	}
	
	/**
	 * Método que consulta el consecutivo de un programa ya existente
	 * @param con
	 * @param codigoPaciente
	 * @param codigoPrograma
	 * @param institucion
	 * @return
	 */
	public static String consultarConsecutivoProgramaExistente(Connection con,String codigoPaciente,String codigoPrograma,String institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProgramasPYPDao().consultarConsecutivoProgramaExistente(con,codigoPaciente,codigoPrograma,institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @param object
	 */
	public static ArrayList<DtoObservacionProgramaPYP> obtenerObservacionesProgramaPYP(Connection con,int codigoPaciente, int codigoPrograma, int institucion) {
		HashMap campos = new HashMap();
		campos.put("paciente", codigoPaciente);
		campos.put("programa", codigoPrograma);
		campos.put("institucion", institucion);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProgramasPYPDao().obtenerObservacionesProgramaPYP(con,campos);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @param object
	 */
	public static boolean guardarObservacioProgramaPYP(Connection con, DtoObservacionProgramaPYP dto) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProgramasPYPDao().guardarObservacioProgramaPYP(con,dto);
	}
	
	//*******************************************************************************
	//**************GETTERS & SETTERS ************************************************+
	
	/**
	 * @return Returns the campos.
	 */
	public HashMap getCampos() {
		return campos;
	}
	/**
	 * @param campos The campos to set.
	 */
	public void setCampos(HashMap campos) {
		this.campos = campos;
	}
	/**
	 * @return Retorna un elemento del mapa campos.
	 */
	public Object getCampos(String key) {
		return campos.get(key);
	}
	/**
	 * @param Asigna un elemento al mapa campos.
	 */
	public void setCampos(String key, Object obj) {
		this.campos.put(key,obj);
	}
	
}
