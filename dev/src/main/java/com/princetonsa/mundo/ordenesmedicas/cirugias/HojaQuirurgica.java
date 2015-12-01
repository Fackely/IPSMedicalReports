/*
 *	Jhony Alexander Duque A.
 */
package com.princetonsa.mundo.ordenesmedicas.cirugias;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.Administracion.UtilidadesAdministracion;
import util.consultaExterna.UtilidadesConsultaExterna;
import util.facturacion.InfoResponsableCobertura;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.ordenesMedicas.UtilidadesOrdenesMedicas;
import util.salas.ConstantesBDSalas;
import util.salas.UtilidadesSalas;

import com.princetonsa.actionform.salasCirugia.HojaQuirurgicaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.HojaQuirurgicaDao;
import com.princetonsa.dao.sqlbase.SqlBaseHojaQuirurgicaDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseUtilidadesManejoPacienteDao;
import com.princetonsa.dto.historiaClinica.DtoValoracionHospitalizacion;
import com.princetonsa.dto.historiaClinica.DtoValoracionUrgencias;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.salasCirugia.DtoProfesionalesCirugia;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.agenda.Cita;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.UtilidadJustificacionPendienteArtServ;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.historiaClinica.Epicrisis1;
import com.princetonsa.mundo.historiaClinica.Valoraciones;
import com.princetonsa.mundo.inventarios.FormatoJustServNopos;
import com.princetonsa.mundo.ordenesmedicas.procedimientos.RespuestaProcedimientos;
import com.princetonsa.mundo.ordenesmedicas.solicitudes.Procedimiento;
import com.princetonsa.mundo.parametrizacion.Servicio;
import com.princetonsa.mundo.salasCirugia.HojaAnestesia;
import com.princetonsa.mundo.salasCirugia.LiquidacionServicios;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.servinte.axioma.dto.salascirugia.CargoSolicitudDto;
import com.servinte.axioma.dto.salascirugia.DestinoPacienteDto;
import com.servinte.axioma.dto.salascirugia.EspecialidadDto;
import com.servinte.axioma.dto.salascirugia.InformacionActoQxDto;
import com.servinte.axioma.dto.salascirugia.InformeQxDto;
import com.servinte.axioma.dto.salascirugia.InformeQxEspecialidadDto;
import com.servinte.axioma.dto.salascirugia.IngresoSalidaPacienteDto;
import com.servinte.axioma.dto.salascirugia.NotaAclaratoriaDto;
import com.servinte.axioma.dto.salascirugia.PeticionQxDto;
import com.servinte.axioma.dto.salascirugia.ProfesionalHQxDto;
import com.servinte.axioma.dto.salascirugia.ProgramacionPeticionQxDto;
import com.servinte.axioma.dto.salascirugia.SalaCirugiaDto;
import com.servinte.axioma.dto.salascirugia.ServicioHQxDto;
import com.servinte.axioma.dto.salascirugia.TipoProfesionalDto;
import com.servinte.axioma.dto.salascirugia.TipoSalaDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.IPSException;


/**
 * @author Jhony Alexander Duque A.
 *
 * Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * Hoja Quirurgica
 */
@SuppressWarnings("unchecked")
public class HojaQuirurgica 
{
	
	/**
	 * Para manjar los logger de la clase HojaQuirurgica
	 */
	private static Logger logger = Logger.getLogger(HojaQuirurgica.class);
	
	/**
	 * Se inicializa el Dao
	 */
	
	public static HojaQuirurgicaDao hojaQxDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHojaQuirurgicaDao();
	}
	
	
	static int codSolicitud;
	
	
	/******************************************************************************************************************************************/
	/*--------------------------------------------------------------------------------------------------------------------------------------
	 * ATRIBUTOS DE LA NUEVA HOJA QUIRURGICA
	 --------------------------------------------------------------------------------------------------------------------------------------*/
	/******************************************************************************************************************************************/
	
	/**
	 * String de indices de la consulta de peticiones
	 */
	public static final String [] indicesPeticiones ={"cuenta0_","paciente1_","codigoPeticion2_","fechaCirugia3_","consecutivoOrdenes4_",
													   "estadoMedico5_","numeroSolicitud6_","solicitante7_","especialidad8_","servicios9_",
													   "esAsociado10_","codigoSolicitante11_"};
	

	/**
	 * String de indices de los servicios por cada peticion
	 */
	public static final String [] indicesServicios ={"codigoServicio0_","codCups1_","servicio2_","nomEspecialidad3_","consecutivo4_","tipoServicio5_",
													 "desServicio6_","observaciones7_","estaBd8_","codigo9_","profecionales10_","diagPostOpera11_",
													 "tieneServRequerido12","finalidad13_","naturaleza14_","finalidades15_","viaCx16_","indBilateral17_",
													 "indViaAcceso18_","especialidadInter19_","tieneHojaQx20","especialidades21_","tiposHonorario22_",
													 "descripcionQxAnt23_","descripcionQx24_","operacion25_","totSev26","requiereServ27_","indViaAcces28_",
													 "numSol29_","reqInter30_","justificar31_","cobertura32_","subcuenta33_","espos34_","tieneFormularios35_",
													 "tieneRespuestas36_","especialidadServicio37_","eliminarServ38_"};
		 
	/**
	 * String con los indices de la informacion faltante de la solicitud, para generarla por debajo
	 */
	public static final String [] indicesSolicitud ={"ccSolicitado0","numAutorizacion1","fechaSolicitud2","horaSolicitud3","especialidad4",
													  "urgente5","nomCcSolicitado6","nomEspecialidad7","numeroSolicitud8","tipoPaciente9",
													  "nomTipoPaciente10","codigoSolicitante11","solicitante12","requiereUci13","tipoCargo14",
													  "estado15","orden16","reqInter17","codigoPeticion18","registradaDesde19", "ingreso20"};
	/**
	 * String con los indices de la informacion de los diagnosticos preoperatorios
	 */
	public static final String [] indicesDiagnosticos ={"codigo0_","numSolicitud1_","diagnostico2_","tipoCie3_","principal4_",
														"nomDiagnostico5_","estaBd6_","validacionCapitacion7_"};
	/**
	 * String con los indices de la informacion de los profecionales
	 */
	public static final String [] indicesProfesionales = {"consecutivo0_","tipoAsocio1_","especialidad2_","codigoProfecional3_","cobrable4_",
		   												   "pool5_","tipoEspecialista6_","estaBd7_","nombreProfesional8_","nomTipoAsocio9_",
		   												   "profesionales10_","especialidades11_","idTipoHonorario12_","eliminado13_","operacion14_",
		   												   "nombreEspecialidad15_","nombrePool16_","totProf17"};
	/**
	 * String con los indices de la informacion de los
	 * diagnosticos postoperatorios
	 */
	public static final String [] indicesDiagPostopera = {"codigo0_","diagnostico1_","tipoCie2_","principal3_","complicacion4_","nomDiagnostico5_",
														  "estaBd6_","seleccionados7","codSolCxServ8_","Ccodigo9_","CestaBd10_","codTemp11_"};
	/**
	 * String con los indices con l ainformacion de los
	 * parametros generales
	 */
	public static final String [] indicesParamGenerales = {"indNoCobrableCx0","indNoCobrableNcto1","permitModInfoCxAntesFinal2","secDespDetServ3_",
															"secProf4_","secDesQx5_","secDiagPostOper6_","indFinHQx7","indReqDescHQxCx8_",
															"indReqDescHQxNcto9_","indAsocCirujano10","indExistHAnes11","permitModInfoGenHqxAntesFinal12",
															"minLimRegNotasNcto13","minLimRegNotasCx14","indExistHQx15","indFinHAnes16","estaMuertoPac17",
															"postularFechas18","esLiquidadSolicitud19"};
	
	
	public static final String [] indicesDatosPeticion = {"codigo0","fechaCirugia1","duracion2","requiereUci3","solicitante4","solicitanteQx5",
														  "codSolicitante6","estadoPeticion7"};
	
	public static final String [] indicesFechasCx ={"fechaInicial0","horaInicial1","fechaFinal2","horaFinal3","fechaIngresoSala4","fechaSalidaSala5",
													"horaIngresoSala6","horaSalidaSala7","fechaPeticion8","horaPeticion9","duracionFinalCx10"};
	
	/**
	 * String con los indices para el manejo de 
	 * la informacion quirurgica. 
	 */
	public static final String [] indicesSecInfoQx ={"tipoHerida0","duracionFinalCx1","politrauma2","sala3","finalizada4","fechaFinaliza5",
													 "horaFinaliza6","partAnes7","tipoAnes8","tipoSala9","infoQxAnt10","secInfoQx11","infoQx12",
													 "secAlerta13","PerfuAnt14","secPerfu15","Perfu16","secHallaz17","hallaz18","hallazAnt19",
													 "mostrarSecPerfu20","mostrarSecHallaz21"};

	/**
	 * indices para el manejo de los profesionales de la informacion Quirurgica
	 */
	public static final String [] indicesProfInfoQx = {"consecutivo0_","numeroSolicitud1_","tipoAsocio2_","codigoProfesional3_","secProf4","estaBd5_",
														"operacion6_"};
	
	
	public static final String [] indicesPato = {"patoAnt0","pato1"};
	public static final String [] indicesObsGenerales = {"obsGenAnt0","obsGen1"};
	public static final String [] indicesNotasAcla = {"notasAclaAnt0","notasAcla1","PermitAddInfo2"};
	
	public static final String [] indicesSalidaSala = {"fechaIngresoSala0","horaIngresoSala1","fechaSalidaSala2","horaSalidaSala3","salidaPaciente4",
														"saliPaciSelect5","fechaFallece6","horaFallece7","fallece8","diagFallece9"};
	
	public static final String [] indicesOtrosProfesionales = {"codigoMedico0_","tipoParticipante1_","especialidad2_","nomEspecialidad3_",
															   "especialidades4_","operacion5_","estaBd6_","codigoMedicoOld7_","numReg8"};
	
	/******************************************************************************************************************************************/
	/*--------------------------------------------------------------------------------------------------------------------------------------
	 * FIN ATRIBUTOS DE LA NUEVA HOJA QUIRURGICA
	 --------------------------------------------------------------------------------------------------------------------------------------*/
	/******************************************************************************************************************************************/
	
	
	/******************************************************************************************************************************************/
	/*--------------------------------------------------------------------------------------------------------------------------------------
	 * METODOS DE LA NUEVA HOJA QUIRURGICA
	 --------------------------------------------------------------------------------------------------------------------------------------*/
	/******************************************************************************************************************************************/
	
	/**
	 * Metodo encargado de consultar los profesionales
	 * de de los servicios de cada cirugia en la tabla
	 * profesionales_cirugia
	 * @param connection
	 * @param criterios
	 * ---------------------------
	 * KEY'S DEL MAPA CRITERIOS	
	 * ---------------------------
	 * -- codigoSolCXSer
	 * @return Hashmap
	 * --------------------------------
	 * KEY'S DE LOS MAPAS QUE RETORNA
	 * --------------------------------
	 * -- consecutivo0_
	 * -- tipoAsocio1_
	 * -- especialidad2_
	 * -- codigoProfecional3_
	 * -- cobrable4_
	 * -- pool5_
	 * -- tipoEspecialista6_
	 * -- estaBd7_
	 */
	public static HashMap consultarProfecionalesCx (Connection connection,HashMap criterios)
	{
		return hojaQxDao().consultarProfecionalesCx(connection, criterios);
	}
	
	/**
	 * Metodo encargado de devolver un arrayList
	 * con la informacion de finalidad por servicio
	 * @param connection 
	 * @param criterios
	 * ----------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ----------------------------
	 * -- naturaleza
	 * -- institucion
	 * @return Arraylist<HashMap<>>
	 * ---------------------------------------
	 * KEY'S DEL HASHMAP DENTRO DEL ARRAYLIST
	 * ---------------------------------------
	 * -- codigo
	 * -- finalidad
	 * 
	 */
	public static ArrayList< HashMap<String, Object>>  consultarFinalidadServicio (Connection connection,HashMap criterios)
	{
		return hojaQxDao().consultarFinalidadServicio(connection, criterios);
	}
	
	
	/**
	 * Método para cargar los diagnósticos de un servicio
	 * @param con
	 * @param numeroServicio
	 * @return
	 */
	public static HashMap cargarDiagnosticosPorServicio(Connection con, int numeroServicio, int numeroSolicitud)
	{
		return hojaQxDao().cargarDiagnosticosPorServicio(con, numeroServicio, numeroSolicitud);
	}
	
	/**
	 * Metodo encargado Consulta el listado de peticiones
	 * que no tengan solicitud asociada, de ser asi
	 * se debe validar que la solicitud corresponda
	 * a la cuenta cargada y que se encuentren
	 * en estado diferente de ATENDIDO o CANSELADA,
	 * ademas consulta todos los servicios de cada
	 * peticion.
	 * @param connection
	 * @param criterios
	 * -----------------------------------------------
	 * 	KEY'S DEL MAPA CRITERIOS
	 * -----------------------------------------------
	 * --paciente --> Requerido
	 * --institucion --> Requerido
	 * --cuenta --> Requerido
	 * @return mapa
	 * ------------------------------------------------
	 * 			KEY'S DEL MAPA DE RESULTADO
	 * ------------------------------------------------
	 * -- cuenta0_
	 * -- paciente1_
	 * -- codigoPeticion2_
	 * -- fechaCirugia3_
	 * -- consecutivoOrdenes4_
	 * -- estadoMedico5_
	 * -- numeroSolicitud6_
	 * -- solicitante7_
	 * -- especialidad8_
	 * -- servicios9_ --> Este es un HashMap por tanto tiene otras llaves
	 * -- esAsociado10_
	 * ----------------------------------------------------------------------------------
	 * KEY'S DEL HASHMAP SERVICIOS9_ QUE LLEVA EL LISTADO DE SERVICIOS DE CADA PETICION
	 * ----------------------------------------------------------------------------------
	 * -- codigoServicio0_
	 * -- codCups1_
	 * -- servicio2_
	 * -- especialidad3_
	 */
	@Deprecated
	public static HashMap consultarPeticiones (Connection connection, int institucion, int paciente,int ingreso )
	{
		HashMap criterios = new HashMap ();
		
		criterios.put("institucion", institucion);
		criterios.put("ingreso", ingreso);
		criterios.put("paciente", paciente);
		
		return  hojaQxDao().consultarPeticiones(connection, criterios);
	}
	
	
	/**
	 * Consulta la informacion de las peticiones a partir de un HashMap de criterios
	 * @param Connection con
	 * @param HashMap criterios
	 * */
	public static HashMap consultarPeticiones (Connection connection, HashMap criterios)
	{	
		return  hojaQxDao().consultarPeticiones(connection, criterios);
	}
	
	
	
	/**
	 * Metodo encargado de insertar los datos
	 * de diagnosticos preoperativos.
	 * @param connection
	 * @param datos
	 * -------------------------------
	 * 		KEY'S DEL MAPA DATOS
	 * -------------------------------
	 * -- solicitud
	 * -- diagnostico
	 * -- tipoCie
	 * -- principal
	 * @return
	 */
	public static boolean insertarDiacnosticosPreoperatorios (Connection connection, HashMap datos)
	{
		return hojaQxDao().insertarDiacnosticosPreoperatorios(connection, datos);
	}
	
	
	/**
	 * Metodo encargado de consultar los datos de la
	 * solicitud.
	 * @param connection
	 * @param criterios
	 * -----------------------------------
	 * 		KEY'S DEL MAPA CRITERIOS
	 * -----------------------------------
	 * -- solicitud --> Requerido
	 * @return mapa
	 * -----------------------------------
	 * KEY'S DEL MAPA QUE RETORNA
	 * -----------------------------------
	 * -- ccSolicitado0
	 * -- numAutorizacion1
	 * -- fechaSolicitud2
	 * -- horaSolicitud3
	 * -- especialidad4
	 * -- urgente5
	 * -- nomCcSolicitado6
	 * -- nomEspecialidad7
	 * -- numeroSolicitud8
	 * -- tipoPaciente9
	 * -- nomTipoPaciente10
	 * -- codigoSolicitante11
	 * -- solicitante12
	 * -- requiereUci13
	 *	
	 */
	public static HashMap consultaSolicitud (Connection connection,HashMap criterios)
	{
		return hojaQxDao().consultaSolicitud(connection, criterios);
	}
	
	
	/**
	 * Metodo encargado de consultar los diagnosticos
	 * de una solicitud.
	 * @param connection
	 * @param criterios
	 * --------------------------------
	 * 	  KEY'S DEL MAPA CRITERIOS 
	 * --------------------------------
	 * -- solicitud --> Requerido
	 * 
	 * @return mapa
	 * -- codigo0_
	 * -- numSolicitud1_
	 * -- diagnostico2_
	 * -- tipoCie3_
	 * -- principal4_
	 * -- nomDiagnostico5_
	 * -- estaBd6_
	 */
	public static HashMap consultaDiagnosticos (Connection connection, HashMap criterios)
	{
		return hojaQxDao().consultaDiagnosticos(connection, criterios);
	}
	
	

	/**
	 * Metodo encargado de actualizar el 
	 * numero de autorizacion de la solicitud
	 * @param connection
	 * @param datos
	 * -------------------------
	 * KEY'S DEL MAPA DATOS
	 * -------------------------
	 * -- autorizacion --> Requerido
	 * -- solicitud --> Requerido
	 */
	public static boolean actualizarAutorizacion (Connection connection, HashMap datos)
	{
		return hojaQxDao().actualizarAutorizacion(connection, datos);
	}
	
	/**
	 * Metodo encargado de verificar si existe
	 * la hoja quirurgica.
	 * @param connection
	 * @param solicitud
	 */
	public static boolean existeHojaQx (Connection connection, String solicitud)
	{
		return hojaQxDao().existeHojaQx(connection, solicitud);
	}
	
	
	/**
	 * Metodo encargado de actualizar la subcuenta
	 * de la tabla solicitudes_cirugia cuando se 
	 * cambian los el servicio de consecutivo 1
	 * en la solicitud de cirugia.
	 */
	public static boolean actualizarSubCuenta (Connection connection,double subCuenta,String solicitud)
	{
		return hojaQxDao().actualizarSubCuenta(connection, subCuenta, solicitud);
	}
	
	
	/**
	 * Metodo encargado de modificar los diagnosticos
	 * preoperatorios 
	 * @param connection
	 * @param datos
	 * -----------------------
	 * KEY'S DE MAPA DATOS
	 * -----------------------
	 * -- diagnostico --> Requerido
	 * -- tipoCie --> Requerido
	 * -- principal --> Requerido
	 * -- codigo --> Requerido
	 * @return
	 */
	public static boolean modificarDiagnosticoPreoperatirio (Connection connection, HashMap datos)
	{
		return hojaQxDao().modificarDiagnosticoPreoperatirio(connection, datos);
	}
	
	

	/**
	 * Metodo encargado de consultar los servicios
	 * de una solicitud
	 * @param connection
	 * @param datos
	 * ---------------------
	 * KEY'S DEL MAPA DATOS
	 * ---------------------
	 * -- solicitud --> Requerido
	 * -- institucion --> Requerido
	 * -- codigoMedico --> Opcional
	 * -- funcionalidad --> Opcional
	 */
	public static HashMap consultarServicios (Connection connection,HashMap datos)
	{
		return hojaQxDao().consultarServicios(connection, datos);
	}
	
	/**
	 * Metodo encargado de eliminar 
	 * un diagnostico preoperatorio
	 * @param connection
	 * @param datos
	 * ------------------------
	 * KEY'S DEL MAPA DATOS
	 * ------------------------
	 * -- codigo --> Requerido
	 */
	public  static boolean eliminarDiagnostioPreoperatorio (Connection connection, HashMap datos)
	{
		return hojaQxDao().eliminarDiagnostioPreoperatorio(connection, datos);
	}
	
	
	/**
	 * Metodo encargado de insertar los datos
	 * de la seccion servicios en la tabla 
	 * sol_cirugia_por_servicio
	 * @param connection
	 * @param datos
	 * --------------------------
	 * KEY'S DEL MAPA DATOS
	 * -------------------------
	 * -- numSolicitud --> Requerido
	 * -- servicio --> Requerido
	 * -- consecutivo --> Requerido
	 * -- institucion --> Requerido
	 * -- indBilateral --> Requerido
	 * -- indViaAcceso --> Requerido
	 * -- especialidad --> Requerido
	 * -- finalidad --> Opcional
	 * -- viaCx --> Opcional
	 * @return (true/false)
	 */
	public static boolean insertarServiciosQx (Connection connection,HashMap datos)
	{
		return hojaQxDao().insertarServiciosQx(connection, datos);
	}
	
	
	/**
	 * Metodo encargado de eliminar un servicio
	 * de la tabla sol_cirugia_por_servicio
	 * @param connection
	 * @param codSolCxXServ
	 */
	public static boolean eliminarServicioQx (Connection connection,String codSolCxXServ)
	{
		return hojaQxDao().eliminarServicioQx(connection, codSolCxXServ);
	}
	
	
	/**
	 * mETODO ENCARGADO DE CONSULTAR EL NOMBRE DEL ASOCIO
	 */
	public static String getNombreAsocio (Connection connection,String codigo)
	{
		return hojaQxDao().getNombreAsocio(connection, codigo);
	}
	
	

	/**
	 * Actualizar los datos del servicio.
	 * @param connection
	 * @param datos
	 * -----------------------------------
	 * KEY'S DEL MAPA DATOS
	 * -----------------------------------
	 * -- consecutivo --> Requerido
	 * -- indBilateral --> Requerido
	 * -- indViaAcceso --> Requerido
	 * -- especialidad --> Requerido
	 * -- codigo --> Requerido
	 * -- finalidad --> Opcional
	 * -- viaCx --> Opcional
	 */
	public static boolean ActualizarServicioQx (Connection connection,HashMap datos)
	{
		return hojaQxDao().ActualizarServicioQx(connection, datos);
	}
	

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*********************************************
	 * Se adiciona metodo por tarea 3379
	 **********************************************/
	
	/**
	 * Metodo encargado de actualizar los datos de 
	 * fecha y hora fallece en la tabla solicitudes_cirugia
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param datos
	 * ------------------------------
	 * KEY'S DEL MAPA DATOS
	 * ------------------------------
	 * -- fechaFallece --> Requerido
	 * -- horaFallece --> Requerido
	 * -- numeroSolicitud --> Requerido
	 * -- diagAcronimo --> Requerido
	 * -- diagTipoCie --> Requerido
	 * @return true/false
	 */
	public static boolean ActualizarFallece (Connection connection, HashMap datos)
	{
		return hojaQxDao().ActualizarFallece(connection, datos);
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	public static void cargarselects (Connection connection,HojaQuirurgicaForm forma,UsuarioBasico usuario,PersonaBasica paciente)
	{
		forma.setCentroCostosMap(Utilidades.obtenerCentrosCostoUsuario(
				 connection, 
				 usuario.getLoginUsuario(),
				 usuario.getCodigoInstitucionInt(),
				 ConstantesBD.codigoTipoAreaSubalmacen+"",
				 paciente.getCodigoUltimaViaIngreso()+""));									
		 
		 forma.setEspecialidadesMap(Utilidades.obtenerEspecialidadesMedico(connection,usuario.getCodigoPersona()+""));
	}
	
	/**
	 * Metod encargado de insertar los datos basicos 
	 * en la hoja quirurgica
	 * @param connection
	 * @param datos
	 * -----------------------------------------
	 * 			KEY'S DEL MAPA DATOS
	 * -----------------------------------------
	 * -- numSolicitado --> Requerido
	 * -- finalizada --> Requerio
	 * -- datosMedico --> Requerido
	 * -- medicoFinaliza --> No Requerido
	 */
	public static boolean insertarHojaQxBasica (Connection connection,HashMap datos)
	{
		return hojaQxDao().insertarHojaQxBasica(connection, datos);
	}
	
	
	/**
	 * Metodo encargado de eliminar la descripcion Qx
	 * de la tabla sol_cirugia_por_servicio
	 * @param connection
	 * @param codSolCxXServ
	 */
	public static boolean eliminarDecQx (Connection connection,String codSolCxXServ)
	{
		return hojaQxDao().eliminarDecQx(connection, codSolCxXServ);
	}
	
	/**
	 * Metodo encargado de eliminar los diagnosticos
	 * postoperatorios de la tabla diag_post_opera_sol_cx
	 * @param connection
	 * @param codSolCxXServ
	 */
	public static  boolean eliminarDiagPostOpe(Connection connection,String codSolCxXServ,String codigo)
	{
		return hojaQxDao().eliminarDiagPostOpe(connection, codSolCxXServ,codigo);
	}
	
	
	/**
	 * Metodo encargado de m odificar los diagnosticos 
	 * PostOperatorios.
	 * @param connection
	 * @param datos
	 * --------------------------------
	 * KEY'S DEL MAPA DATOS
	 * --------------------------------	
	 * -- diagnosticos --> Requerido
	 * -- tipoCie --> Requerido
	 * -- usuarioModifica --> Requerido
	 * -- codigo --> Requerido 
	 */
	public static boolean modificarDiagPostOpera (Connection connection, HashMap datos)
	{
		return hojaQxDao() .modificarDiagPostOpera(connection, datos);
	}
	
	/**
	 * Metodo encargado de eliminar los profecionales
	 * de la cirugia
	 * @param connection
	 * @param codSolCxXServ
	 * @param consecutivo
	 */
	public static boolean eliminarProfesionalesCx (Connection connection,String codSolCxXServ,String consecutivo)
	{
		return hojaQxDao().eliminarProfesionalesCx(connection, codSolCxXServ, consecutivo);
	}
	
	
	 /**
     * Método que consulta las descripciones Qx de una cirugía
     * @param con
     * @param consecutivoSolCx
     * @return
     * ------------------------
	 * KEY'S DEL MAPA DATOS
	 * ------------------------
	 * codigo_, descripcion_, consecutivoServicio_, fechaGrabacion, horaGrabacion_, usuarioGrabacion_
     */
    public static HashMap consultarDescripcionesQx2(Connection con,String consecutivoSolCx)
    {
    	return hojaQxDao().consultarDescripcionesQx2(con, consecutivoSolCx);
    }
    
    
	/**
	 * Metodo encargado de actualiazar los datos de las
	 * tablas esp_intervienen_solcx y cirujanos_esp_int_solcx
	 * dependiendo de las especialidades y cirujanos del servicio.
	 * @param connection
	 * @param datos
	 * ---------------
	 * KEY'S DATOS
	 * ---------------
	 * -- solicitud --> Requerido
	 * @param usuario
	 * @return
	 */
	public static boolean ActualizarEspecialidadesAndCirujanos (Connection connection,HashMap datos,UsuarioBasico usuario)
	{
		return hojaQxDao().ActualizarEspecialidadesAndCirujanos(connection, datos, usuario);
	}
    
	/**
	 * Metodo encargado de consutar los datos de la peticion
	 * @return mapa
	 * ---------------------------------
	 * KEY'S DEL MAPA QUE DEVUELVE
	 * ---------------------------------
	 * -- codigo0
	 * -- fechaCirugia1 
	 * -- duracion2 
	 * -- requiereUci3 
	 * -- solicitante4 
	 */
	public static HashMap consultarDatosPeticion (Connection connection, String peticion)
	{
		return hojaQxDao().consultarDatosPeticion(connection, peticion);
	}
	
	
	/**
	 * 
	 * @param peticion
	 * @return
	 */
	public static HashMap consultarDatosPeticion (String peticion)
	{
		HashMap mapa=new HashMap();
		Connection con =UtilidadBD.abrirConexion();
		mapa = hojaQxDao().consultarDatosPeticion(con, peticion);
		UtilidadBD.closeConnection(con);
		return mapa;
	}
    
	/**
	 * Metodo encargado de consultar
	 * Las fechas de inicio y finalizacion de la
	 * Cirugia
	 */
	public static HashMap consultarDatosFechas (Connection connection, String solicitud)
	{
		return hojaQxDao().consultarDatosFechas(connection, solicitud);	
	}
	
	/**
	 * Metodo encargado de identificar si existe hoja
	 * de anestecia para una solicitud.
	 */
	public static String existeHojaAnestesia (Connection connection,String solicitud)
	{
		return hojaQxDao().existeHojaAnestesia(connection, solicitud);
	}
	
	
	/**
	 * Metodo encargado de consultar la informacion de la hoja quierugica
	 * @param connection
	 * @param numSol
	 * 
	 * @return mapa
	 * -----------------------------
	 * KEY'S DEL MAPA QUE DEVUELVE
	 * -----------------------------
	 * -- tipoHerida0
	 * -- duracionFinalCx1
	 * -- politrauma2
	 * -- sala3
	 * -- finalizada4
	 * -- fechaFinaliza5
	 * -- horaFinaliza6
	 * -- partAnes7
	 * -- tipoAnes8
	 * -- tipoSala9
	 */
	public static HashMap consultarDatosHQX (Connection connection,String numSol)
	{
		return hojaQxDao().consultarDatosHQX(connection, numSol);
	}
	
	/**
	 * Metodo encargado de consultar los profesionales
	 * de la informacion Qx.
	 * @param connection
	 * @param numSol
	 * @return mapa
	 * ----------------------------------
	 * KEY'S DEL MAPA QUE DEVUELVE
	 * ----------------------------------
	 * -- consecutivo0
	 * -- numeroSolicitud1
	 * -- tipoAsocio2
	 * -- codigo_profesional3
	 * 
	 */
	public static HashMap consultarProfInfQx (Connection connection,String numSol)
	{	
		return hojaQxDao().consultarProfInfQx(connection, numSol);
	}
	
	

	 /**
	  * Metodo encargado de consultar los campos de texto
	  * de la hoja Qx
	  */
  public static String consultarCamposTextoHQx (Connection con,String numSol,String tipo)
  {
	   return hojaQxDao().consultarCamposTextoHQx(con, numSol, tipo);
  }
	
  
  
	/**
	 * Metodo encargado de insertar los datos
	 * en la tabla campos_texto_hoja_qx
	 * @param connection
	 * @param datos
	 * -------------------------------
	 * KEY'S DEL MAPA DATOS
	 * -------------------------------
	 *  -- numSol --> Requerido
	 *  -- descripcion --> Requerido
	 *  -- tipo --> Requerido
	 *  -- usuario --> Requerido
	 *  @return false/true
	 */
	public static boolean insertarCamposTextHQx (Connection connection,HashMap datos)
	{
		return hojaQxDao().insertarCamposTextHQx(connection, datos);
	}
  
	/**
	 * Metodo encargado de actualizar la peticion
	 * @param connection
	 * @param datos
	 * --------------------------------
	 * KEY'S DEL MAPA DATOS
	 * --------------------------------
	 * -- requiereUci
	 * -- solicitante
	 * -- codigo
	 */
	public static boolean actualizarPeticion (Connection connection,HashMap datos)
	{
		return hojaQxDao().actualizarPeticion(connection, datos);
	}
  
  
	/**
	 * Metodo encargado de actualizar los datos de la fecha inicial,
	 * hora inicial, fecha final, hora final y duracion final de
	 * la cirugia.
	 * @param connection
	 * @param datos
	 * ------------------------------
	 * KEY'S DEL MAPA DATOS
	 * ------------------------------
	 * -- fechaIni
	 * -- horaIni
	 * -- fechaFin
	 * -- horaFin
	 * -- duracionFin
	 */
	public static boolean actualizarFechas (Connection connection,HashMap datos)
	{
		return hojaQxDao().actualizarFechas(connection, datos);
	}
	
	
	/**
	 * Metodo encargado de consultar si la
	 * solicitud esta finalizada totalmente
	 * @param numSol
	 * @return S/N
	 */
	public static String essolicitudtotalpendiente (Connection connection,String numSol)
	{
		return hojaQxDao().essolicitudtotalpendiente(connection, numSol);
	}
	
	/**
	 * Metodo encargado de actualizar los datos
	 * de la hoja quirurgica 
	 * @param connection
	 * @param datos
	 * ------------------------------------
	 * KEY'S DEL MAPA DATOS
	 * ------------------------------------
	 * -- poli
	 * -- tipoSala
	 * -- sala
	 * -- tipoHerida
	 * -- partAnest
	 * -- tipoAnest
	 * -- numSol
	 */
	public static boolean actualizarInfoQx (Connection connection,HashMap datos)
	{
		return hojaQxDao().actualizarInfoQx(connection, datos); 
	}
	
	
	/**
	 * Metodo encargado de consultar los 
	 * la sala y el tipo de la peticion en
	 * estado programada o reprogramada.
	 * @param connection
	 * @param peticion
	 * @return mapa
	 * ------------------------------
	 * KEY'S DEL MAPA QUE RETORNA
	 * ------------------------------
	 * -- sala0
	 * -- tipoSala1
	 */
	public static HashMap consultarSalaProgramada (Connection connection, String peticion)
	{
		return hojaQxDao().consultarSalaProgramada(connection, peticion);
	}
	
	/**
	 * Metodo encargado de insertar los profesionales
	 * de la informacion quirurgica
	 * @param connection
	 * @param datos
	 * -----------------------------
	 * KEY'S DEL MAPA DATOS
	 * -----------------------------
	 * -- numSol
	 * -- tipoAsoc
	 * -- prof
	 */
	public static boolean insertarProfInfoQx (Connection connection,HashMap datos)
	{
		return hojaQxDao().insertarProfInfoQx(connection, datos);
	}
	
	/**
	 * Método para insertar un profesional del acto quirúrgico
	 * @param connection
	 * @param numeroSolicitud
	 * @param tipoAsocio
	 * @param profesional
	 * @param cobrable
	 * @param usuario
	 * @param historiaClinica
	 * @return
	 */
	public static int insertarProfInfoQx (Connection connection,String numeroSolicitud,int tipoAsocio,int profesional,String cobrable,String usuario,String historiaClinica)
	{
		HashMap datos = new HashMap();
		datos.put("numSol",numeroSolicitud);
		datos.put("tipoAsoc",tipoAsocio);
		datos.put("prof",profesional);
		datos.put("cobrable", cobrable);
		datos.put("usuario", usuario);
		datos.put("historiaClinica", historiaClinica);
		if(hojaQxDao().insertarProfInfoQx(connection, datos))
			return 1;
		else
			return 0;
	}
	
	/**
 	 * Metodo encargado de actualizar los profesionales
 	 * de la informacion quirurgica
 	 * @param connection
 	 * @param datos
 	 * -------------------------
 	 * KEY'S DEL MAPA DATOS
 	 * -------------------------
 	 * -- tipoAsoc
 	 * -- prof
 	 * -- consec
 	 */
	public static boolean actualizarProfInfoQx (Connection connection,HashMap datos)
	{
		return hojaQxDao().actualizarProfInfoQx(connection, datos);
	}
	
	/**
	 * Metodo encargado de eliminar un profesional
	 * de l ainformacion Qx
	 */
	public static boolean eliminarProfInfoQx (Connection connection,String consec)
	{
		return hojaQxDao().eliminarProfInfoQx(connection, consec);
	}
	
	/**
	 * Metodo encargado de cambiar el estado
	 * de la peticion.
	 * @param connection
	 * @param estado
	 * @param peticion
	 * @return true/false
	 */
	@Deprecated
	public static boolean cambiarEstadoPeticion (Connection connection,String estado,String peticion)
	{
		return hojaQxDao().cambiarEstadoPeticion(connection, estado, peticion);
	}
	
	/**
	 * Metodo encargado de  consultar otros profesionales
	 * de la tabla otros_prof_hoja_qx
	 * @param connection
	 * @param numSol
	 * @return mapa
	 * ----------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * ----------------------------
	 * -- codigoMedico0
	 * -- tipoParticipante1
	 * -- especialidad2
	 * -- nomEspecialidad3
	 */
	public static HashMap consultarOtrosProfesionales (Connection connection, String numSol)
	{
		return hojaQxDao().consultarOtrosProfesionales(connection, numSol);
	}
	
	/**
	 * Metodo encargado de eliminar otros profesionales de la
	 * tabla otros_prof_hoja_qx
	 * @param connection
	 * @param numSol
	 * @param codMedico
	 * @return True/False
	 * 
	 */
	public static boolean eliminarOtrosProfesionales (Connection connection,String numSol,String codMedico)
	{
		return hojaQxDao().eliminarOtrosProfesionales(connection, numSol, codMedico);
	}
	
	/**
	 * Metodo encargado de actualizar otros profesionales de la 
	 * tabla otros_prof_hoja_qx
	 * @param connection
	 * @param datos
	 * ----------------------------
	 * KEY'S DEL MAPA DATOS
	 * ----------------------------
	 * -- tipoParticipante
	 * -- numSol
	 * -- codMedico
	 */
	public static boolean actualizarOtrosProfesionales (Connection connection,HashMap datos)
	{
		return hojaQxDao().actualizarOtrosProfesionales(connection, datos);
	}
	
	/**
	 * Metodo encargado de ingresar otros profesionales en la tabla
	 * otros_prof_hoja_qx.
	 * @param connection
	 * @param datos
	 * --------------------------
	 * KEY'S DEL MAPA DATOS
	 * --------------------------
	 * -- numSol
	 * -- codMedico
	 * -- tipoParticipante
	 * -- especialidad
	 * @return False/True
	 */
	public static boolean IngresarOtrosProfesionales (Connection connection,HashMap datos)
	{
		return hojaQxDao().IngresarOtrosProfesionales(connection, datos);
	}
	
	/**
	 * Metodo encargado de cambiar el estado de la solicitud
	 * @param connection
	 * @param estado
	 * @param numSol
	 * @param String codigoMedico(no requerido)
	 * @return true /false
	 */
	@Deprecated
	public static boolean cambiarEstadoSolicitud (Connection connection,String estado,String numSol,String codigoMedico)
	{
		HashMap parametros = new HashMap();
		parametros.put("estado",estado);
		parametros.put("numSol",numSol);
		parametros.put("codigoMedicoInterpretacion",codigoMedico);
		
		return hojaQxDao().cambiarEstadoSolicitud(connection,parametros);
	}
	
	/**
	 * Metodo encargado de Actualizar la salida del paciente
	 * en la tabla solicitudes_cirugia		
	 * @param connection
	 * @param datos
	 * -------------------------------
	 * KEY'S DEL MAPA DATOS
	 * -------------------------------										
	 *	-- fechaIngSala
	 *	-- horaIngSala
	 *	-- fechaSalSala
	 *	-- horaSalSala
	 *	-- salPac
	 *  -- desSelec S-->desseleccion ---- N--> Seleccion [Muy importante]
	 *  -- usuario
	 *  -- numSol
	 */
	public static boolean actualizarSalidaPaciente (Connection connection,HashMap datos)
	{
		return hojaQxDao().actualizarSalidaPaciente(connection, datos);
	}
	
	
	/**
	 * Consultar datos salida del paciente
	 * @param connection
	 * @param numSol
	 * @return mapa
	 * --------------------------------
	 * KEY'S DEL MAPA QUE DEVUELVE
	 * --------------------------------
	 * -- fechaIngresoSala0
	 * -- horaIngresoSala1
	 * -- fechaSalidaSala2
	 * -- horaSalidaSala3
	 * -- salidaPaciente4
	 * -- saliPaciSelect5
	 */
	public static HashMap consultarSalidaPaciente (Connection connection, String numSol)
	{
		return hojaQxDao().consultarSalidaPaciente(connection, numSol);
	}
	
	
	/**
	 * Metodo encargado de consultar los tipos de profesionales
	 * @param connection
	 * @param institucion
	 * @return  ArrayList<HashMap>
	 * -------------------------------
	 * KEY'S DEL HASHMAP
	 * --------------------------------
	 * --codigo
	 * --nombre
	 */
	public static ArrayList< HashMap<String, Object>>  obtenerTiposProfesional (Connection connection,String institucion)
	{
		return hojaQxDao().obtenerTiposProfesional(connection, institucion);
	}
	
	/**
	 * Metodo encargado de cambiar de estado la Hoja
	 * Qx
	 * @param connection
	 * @param datos
	 * ------------------------------------
	 * KEY'S DEL MAPA DATOS
	 * ------------------------------------
	 * -- finalizada (true/false) --> Requerido
	 * -- medicoFin --> Requerido
	 * -- numSol --> Requerido
	 * @return (true/false)
	 */
	public static boolean cambiarEstadoHqx (Connection connection,HashMap datos)
	{
		return hojaQxDao().cambiarEstadoHqx(connection, datos);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param infoSolicitud
	 * -------------------------------------------
	 * 		KEY'S DEL MAPA INFOSOLICITUD
	 * -------------------------------------------
	 * -- ccSolicitado0 -->Requerido
	 * -- numAutorizacion1 -->Opcional
	 * -- fechaSolicitud2 -->Requerida
	 * -- horaSolicitud3 -->Requerido
	 * -- especialidad4 -->Requerido
	 * -- requerido5 -->Requerido 
	 * @param servicios
	 * --------------------------------------------
	 * 			KEY'S DEL MAPA SERVICIOS
	 * --------------------------------------------
	 * @param forma
	 * @param errores
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	public static ActionErrors generarSolicitudCirugia(Connection con,HashMap peticion,HashMap infoSolicitud,HashMap servicios,ActionErrors errores, UsuarioBasico usuario, PersonaBasica paciente) throws IPSException 
	{
		logger.info("\n ENTRE A ********  generarSolicitudCirugia *********** peticion -->"+peticion+" -->"+infoSolicitud+" -->"+servicios);
		
		SolicitudesCx mundoSolCx= new SolicitudesCx();
		
		int codigoPeticion = 0, numeroSolicitud=0, resp1 = 0;
		boolean resp0 = false;
		//logger.info("\n\n\n info solicitud --> "+infoSolicitud);
		//*******************************************************************************************
		
		//*******************SE INSERTA UNA SOLICITUD BÁSICA*******************************************
		Solicitud objectSolicitud= new Solicitud();
		
		objectSolicitud.clean();
		@SuppressWarnings("unused")
		String numeroAutorizacion="";
		
		//se valida si el numero de autorizacion viene o no
		if (infoSolicitud.containsKey(indicesSolicitud[1]) && !(infoSolicitud.get(indicesSolicitud[1])+"").equals("") && !(infoSolicitud.get(indicesSolicitud[1])+"").equals(ConstantesBD.codigoNuncaValido+""))
			numeroAutorizacion=infoSolicitud.get(indicesSolicitud[1])+"";
		else 
			numeroAutorizacion="";
		
		if (peticion.containsKey(indicesPeticiones[2]+"0") && !(peticion.get(indicesPeticiones[2])+"0").equals("") && !(peticion.get(indicesPeticiones[2])+"0").equals(ConstantesBD.codigoNuncaValido+""))
			codigoPeticion=Integer.parseInt((peticion.get(indicesPeticiones[2]+"0")+""));
		
		objectSolicitud.setFechaSolicitud(infoSolicitud.get(indicesSolicitud[2])+"");
		objectSolicitud.setHoraSolicitud(infoSolicitud.get(indicesSolicitud[3])+"");
		
		//evaluar servicios
		objectSolicitud.setTipoSolicitud(new InfoDatosInt(ConstantesBD.codigoTipoSolicitudCirugia));
		//Numero de autorizacion
		//objectSolicitud.setNumeroAutorizacion(numeroAutorizacion);
		//especialidad
		objectSolicitud.setEspecialidadSolicitante(new InfoDatosInt(Integer.parseInt(infoSolicitud.get(indicesSolicitud[4])+"")));
		
		objectSolicitud.setOcupacionSolicitado(new InfoDatosInt(ConstantesBD.codigoNuncaValido));
		objectSolicitud.setCentroCostoSolicitante(new InfoDatosInt(paciente.getCodigoArea()));
		
		objectSolicitud.setCodigoMedicoSolicitante(Integer.parseInt(peticion.get(indicesPeticiones[11]+"0")+""));
		//centro de costo que solicita
		objectSolicitud.setCentroCostoSolicitado(new InfoDatosInt(Integer.parseInt(infoSolicitud.get(indicesSolicitud[0])+"")));
		objectSolicitud.setCodigoCuenta(paciente.getCodigoCuenta());
		
		objectSolicitud.setCobrable(true);
		
		objectSolicitud.setVaAEpicrisis(false);
		//urgente
		if (infoSolicitud.containsKey(indicesSolicitud[5]) && !(infoSolicitud.get(indicesSolicitud[5])+"").equals("") && (infoSolicitud.get(indicesSolicitud[5])+"").equals("N"))
			objectSolicitud.setUrgente(false);
		else
			objectSolicitud.setUrgente(true);
		
		objectSolicitud.setEstadoHistoriaClinica(new InfoDatosInt(ConstantesBD.codigoEstadoHCSolicitada));
		objectSolicitud.setSolPYP(false);
		objectSolicitud.setTieneCita(false);

		//liquidar sobre asocio, por default es "N"
		objectSolicitud.setLiquidarAsocio(ConstantesBD.acronimoNo);

		UtilidadBD.iniciarTransaccion(con);
		try
		{
		numeroSolicitud=objectSolicitud.insertarSolicitudGeneralTransaccional(con, ConstantesBD.continuarTransaccion);
		
		codSolicitud =numeroSolicitud;
		
		}
		catch(SQLException sqle)
		{
			logger.warn("Error al generar la solicitud basica de cirugías: "+sqle);
			errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LA SOLICITUD "));
		}
		
		
		//*******************SE CALCULA EL CONVENIO DE LA COBERTURA**************************************
		double subCuenta=ConstantesBD.codigoNuncaValido;
		subCuenta=calcularSubCuentaCobertura(con, servicios, paciente, usuario);
		//************************************************************************************************
		
		//*************SE INSERTA LA SOLICITUD DE CIRUGIA ************************************************
		//1) Se inserta encabezado de la cirugia
		try 
		{
			resp0 = mundoSolCx.insertarSolicitudCxGeneralTransaccional1(con,numeroSolicitud+"",codigoPeticion+"",false, 
																		ConstantesBD.continuarTransaccion,subCuenta,
																		indQx(servicios));				
		} 
		catch (SQLException e) 
		{
			resp0 = false;
			logger.error("Error al insertar solicitud general de cirugía: "+e);
		}
		
		if(!resp0)
			errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LA SOLICITUD Qx "));
		else
		{
			//2) Se inserta el detalle de la cirugia (El servicio)
			resp1 = insertarServiciosXSolicitud(con, servicios, numeroSolicitud+"", /*numeroAutorizacion, */usuario);
			
			if(resp1<=0)
				errores.add("",new ActionMessage("errors.noSeGraboInformacion","DEL DETALLE DE LA SOLICITUD Qx "));
		}
		
		if (errores.isEmpty())
			UtilidadBD.finalizarTransaccion(con);
		
		//*******************************************************************************************************
		
			return errores;
	}

	

	/**
	 * Metodo encargado de calcular la subcuenta de la
	 * cobertura
	 * @param connection
	 * @param servicios
	 * -----------------------------
	 * KEY'S DEL MAPA SERVICIOS
	 * -----------------------------
	 * -- codigoServicio0_ --> Requerido
	 * -- codCups1_ --> Requerido
	 * -- numRegistros --> Requerido
	 * @param paciente
	 * @param usuario
	 * @return
	 */
	public static double calcularSubCuentaCobertura (Connection connection, HashMap servicios,PersonaBasica paciente,UsuarioBasico usuario) throws IPSException
	{	
		double subCuenta=ConstantesBD.codigoNuncaValido;
		int codigoServicio=ConstantesBD.codigoNuncaValido;
		InfoResponsableCobertura infoResponsableCobertura= new InfoResponsableCobertura();
		
		for (int i=0;i<Integer.parseInt(servicios.get("numRegistros")+"");i++)
			if (Integer.parseInt(servicios.get(indicesServicios[0]+i)+"")==1)
				codigoServicio=Integer.parseInt(servicios.get(indicesServicios[1]+i)+"");
		
		infoResponsableCobertura=Cobertura.validacionCoberturaServicio(connection, paciente.getCodigoIngreso()+"", paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoTipoPaciente() ,codigoServicio, usuario.getCodigoInstitucionInt(),false, "" /*subCuentaCoberturaOPCIONAL*/);
		subCuenta=infoResponsableCobertura.getDtoSubCuenta().getSubCuentaDouble();
	
		return subCuenta;
	}

	/**
	 * Metodo encargado de insertar los servicios de la peticion
	 * en la solicitud, en la tabla sol_cirugia_por_servicio.
	 * @param connection
	 * @param servicios
	 * ------------------------------------
	 * 		KEY'S DEL MAPA SERVICIOS
	 * ------------------------------------
	 * -- codigoServicio0_ --> Requerido
	 * -- codCups1_ --> Requerido
	 * -- numRegistros --> Requerido
	 * @param numeroSolicitud
	 * @param numeroAutorizacion
	 * @param usuario
	 * @return
	 */
	public static int insertarServiciosXSolicitud (Connection connection,HashMap servicios,String numeroSolicitud,/*String numeroAutorizacion,*/UsuarioBasico usuario)
	{
		//logger.info("\n ************** ENTRE A INSERTAR SERVICIOS X SOLICITUD ***************"+servicios);
		int resp=0;

		SolicitudesCx mundoSolCx= new SolicitudesCx();
		for (int i=0;i<Integer.parseInt(servicios.get("numRegistros")+"");i++)
		{
			
				resp = mundoSolCx.insertarSolicitudCxXServicioTransaccional(
	                	connection, 
	                	numeroSolicitud+"", 
	                	servicios.get(indicesServicios[0]+i)+"", 
	                	ConstantesBD.codigoNuncaValido, // codigo tipo cirugia 
	                	Integer.parseInt(servicios.get(indicesServicios[4]+i)+""), 
	                	ConstantesBD.codigoNuncaValido, //esquema tarifario
	                	ConstantesBD.codigoNuncaValidoDouble, //grupo o uvr
	                	usuario.getCodigoInstitucionInt(), 
	                	/*numeroAutorizacion, */ 
	                	ConstantesBD.codigoNuncaValido, //finalidad
	                	"", //observaciones 
	                	"", //via Cx 
	                	"", //indicativo bilateral
	                	"", //indicativo via de acceso
	                	ConstantesBD.codigoNuncaValido, //codigo de la especialidad
	                	"", //liquidar servicio
	                	ConstantesBD.continuarTransaccion,
	                	"",
	                	null);
				
			
		}
		return resp;
	}
	
	/**
	 * Metodo encargado de consultar los servicios
	 * de la peticion
	 */
	public static HashMap consultarServiciosPeticion(Connection connection, String peticion)
	{
		return hojaQxDao().consultarServiciosPeticion(connection, peticion);
	}
	
	/**
	 * Metodo encargado de crear la solicitud si no existe.
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	public static ActionErrors crearSolicitud (Connection connection, HojaQuirurgicaForm forma,UsuarioBasico usuario, PersonaBasica paciente) throws IPSException
	{
		logger.info("\n entre a crearSolicitud  listado peticiones -->"+forma.getListadoPeticionesMap()+" index--> "+forma.getIndexPeticion()+" Datos solicitud -->"+forma.getSolicitudMap());
		ActionErrors errores = new ActionErrors();
		//se sacan los datos de la peticon con indice "0"
		HashMap peticion = new HashMap();
		logger.info("\n ******** 1 ***********");
		peticion.putAll(Listado.copyOnIndexMap(forma.getListadoPeticionesMap(), forma.getIndexPeticion(), indicesPeticiones));
		//se sacan los servicios de la peticion
		logger.info("\n ******** 2 ***********"); 
		HashMap servicios = new HashMap ();
		servicios.putAll((HashMap)forma.getListadoPeticionesMap(indicesPeticiones[9]+forma.getIndexPeticion()));
		logger.info("\n ******** 3 ***********");
		HashMap infoSolicitud = new HashMap ();
		infoSolicitud.putAll(forma.getSolicitudMap());
		logger.info("\n ******** 4 ***********");
		errores=generarSolicitudCirugia(connection, peticion, infoSolicitud, servicios, errores, usuario, paciente);
		forma.setCodSolicitud(codSolicitud);	
		return errores;
	}
	
	/**
	 * Metodo encargado de identificar cual es 
	 * el ind_qx que debe ir dentro de la tabla
	 * solicitudes_cirugia
	 * @param servicios
	 * --------------------------------
	 * 		KEY'S MAPA SERVICIOS
	 * --------------------------------
	 * --numRegistros --> Requerido
	 * --tipoServicio5_ --> Requerido
	 * 
	 * @return
	 */
	public static String indQx (HashMap servicios)
	{
		for (int i=0;i<Integer.parseInt(servicios.get("numRegistros")+"");i++)
		{
			if ((servicios.get(indicesServicios[5]+i)+"").equals(ConstantesBD.codigoServicioQuirurgico+"") || (servicios.get(indicesServicios[5]+i)+"").equals(ConstantesBD.codigoServicioPartosCesarea+"") )
			{
				logger.info("\n******************** IND QX ES CX ***************");
				return ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia;
			}
		}
		logger.info("\n******************** IND QX ES NCTO ***************");
		return ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento;
		
		
	}
	
	
	/**
	 * metodo encargado del ordenamiento de la forma
	 * @param forma
	 */
	public static void accionOrdenarMapa(HojaQuirurgicaForm forma)
	{
		
		int numReg = Integer.parseInt(forma.getListadoPeticionesMap("numRegistros")+"");
		forma.setListadoPeticionesMap(Listado.ordenarMapa(indicesPeticiones, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getListadoPeticionesMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setListadoPeticionesMap("numRegistros",numReg+"");
	}
	
	
	
	/**
	 * Metodo encargado de Verificar si el servicio de copnsecutivo "1" de la solicitud
	 * cambio para recalcular la covertura y modificar el subCuenta.
	 * @param connection
	 * @param serviciosOld
	 * -------------------------------------
	 * 	  KEY'S DEL MAPARA SERVICIOSOLD
	 * -------------------------------------
	 * -- numRegistros --> Requerido
	 * -- consecutivo4_ --> Requerido
	 * -- codigoServicio0_ --> Requerido
	 * @param servicios
	 * -------------------------------------
	 * 	  KEY'S DEL MAPARA SERVICIOS
	 * -------------------------------------
	 * -- numRegistros --> Requerido
	 * -- consecutivo4_ --> Requerido
	 * -- codigoServicio0_ --> Requerido
	 * 
	 * @param paciente
	 * @param usuario
	 * @param solicitud
	 * @return
	 */
	public static boolean actualizarSubCuentaXDifSer (Connection connection,HashMap serviciosOld,HashMap servicios,PersonaBasica paciente,UsuarioBasico usuario,String solicitud) throws IPSException
	{
		for (int i=0;i<Integer.parseInt(servicios.get("numRegistros")+"");i++)
			if (Integer.parseInt(servicios.get(indicesServicios[4]+i)+"")==1)
				for (int j=0;j<Integer.parseInt(serviciosOld.get("numRegistros")+"");j++)	
					if (Integer.parseInt(serviciosOld.get(indicesServicios[4]+j)+"")==1)
						if(!(servicios.get(indicesServicios[0]+i)).equals(serviciosOld.get(indicesServicios[0]+j)))
							return actualizarSubCuenta(connection, calcularSubCuentaCobertura(connection, servicios, paciente, usuario), solicitud);
						
							
						
		return false;
	}
	
	public static HashMap consultDatPeticion (Connection connection,HojaQuirurgicaForm forma,boolean esdummy)
	{
		logger.info("\n entre a consultar los datos de la peticion "+forma.getPeticion());
		HashMap mapa = new HashMap ();
		if (!esdummy)
		{
			mapa.put("peticion", forma.getListadoPeticionesMap(indicesPeticiones[2]+forma.getIndexPeticion()));
			mapa=consultaSolicitud(connection, mapa);
		}
		else
		{
			mapa.put("peticion", forma.getPeticion());
			mapa=consultaSolicitud(connection, mapa);	
		}
			
		return mapa;
	}
	
	/**
	 * Metodo encargado de cargar la seccion  de informacion
	 * general
	 * @param connection
	 * @param forma
	 */
	public static void cargarSecGeneral (Connection connection, HojaQuirurgicaForm forma,UsuarioBasico usuario,PersonaBasica paciente,boolean esdummy,HttpServletRequest request, boolean formatearOpera)
	{
		logger.info("\n ******* ENTRO A CARGARSECGENERAL ********");
		logger.info("\n centro costo -->"+usuario.getCodigoCentroCosto()+" centro Atencion --> "+usuario.getCodigoCentroAtencion()+" paciente -->"+paciente.getCodigoPersona());
		forma.reset_secGeneral(formatearOpera);
		//obtenerDatosRequest(forma, request);
		HashMap criterios = new HashMap ();
		//se consultan los datos de la 	peticion	
		forma.setSolicitudMap(consultDatPeticion(connection, forma, esdummy));
		//se carga el numero se solicitud en criterios para buscar los diagnosticos
		criterios.put("solicitud", forma.getSolicitudMap(indicesSolicitud[8]+"_0"));
		//se consultan los diagnosticos del numero de solicitud indicado
		criterios = consultaDiagnosticos(connection, criterios);
		
		//logger.info("\n los diagnosticos son "+criterios);
		//se saca una copia de los datos para comparar si cambiaron 
		forma.setDiagnosticosClone(Listado.copyMap(criterios, forma.getDiagnosticosClone(), Integer.parseInt(criterios.get("numRegistros")+""), indicesDiagnosticos));
		//logger.info("\n el valor de los diagnosticos clone es "+forma.getDiagnosticosClone());
		//se convierten los datos traidos de la bd a formato de la aplicacion
		forma.setDiagnosticos(convertirFormatoAAPDiagnosticos(criterios));
		//se carga si existe la hoja quirurgica o no
		forma.setExisteHojaQx(existeHojaQx(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""));
		//se carga el numero de autorizacion inicial para saber si fue modificado
		forma.setNumAutorizacionOld(forma.getSolicitudMap(indicesSolicitud[1]+"_0")+"");
		

		//MT - 4918 Alejandro Rosas
		String numSolicitud = forma.getSolicitudMap(indicesSolicitud[18] + "_0").toString();
		int estado = Integer.valueOf(forma.getSolicitudMap(indicesSolicitud[15] + "_0").toString());
int codIngreso = 0;
		
		if (paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoHospitalizacion || paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoUrgencias) {
			if (!forma.getSolicitudMap(indicesSolicitud[20] + "_0").toString().equals("") && forma.getSolicitudMap(indicesSolicitud[20] + "_0")!=null)
			{
			codIngreso = Integer.valueOf(forma.getSolicitudMap(indicesSolicitud[20] + "_0").toString());
			}
			else {
				codIngreso=paciente.getCodigoIngreso(); 
			}
		
		}
		else {
			
			codIngreso=paciente.getCodigoIngreso(); 
			
			// si se presenta un error de que no salen los diagnosticos, se debe a que es una orden  realizada con una cuenta asociada que no es la ultima
			// se debe cambiar entonces este método para que revise en varias cuentas; pero segun las pruebas que hice, nunca ocurre lo descrito anteriormente
			//-davgommo
		}
		
		if (paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoHospitalizacion) {
			if (estado == ConstantesBD.codigoEstadoHCSolicitada) {

				forma.setDiagnosticos("principal", HojaQuirurgica.consultarUltimaDiagnosticoPpal(connection, codIngreso));
				HojaQuirurgica.consultarUltimaDiagnosticoRelacionado(connection, codIngreso, forma, request);

			}/* else if (estado == ConstantesBD.codigoEstadoHCRespondida) {
				forma.setDiagnosticos("principal", HojaQuirurgica.consultarDiagnosticosHospitalizacion(connection,numSolicitud));
				HojaQuirurgica.consultarDiagnosticosHospitalizacion(connection, numSolicitud );
			}*/
		} else if (paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoUrgencias) {

			if (estado == ConstantesBD.codigoEstadoHCSolicitada) {

				forma.setDiagnosticos("principal", HojaQuirurgica.consultarDiagnosticosValoracionPPal(connection,String.valueOf(paciente.getCodigoPersona())));
				HojaQuirurgica.consultarDiagnosticoValoracionRela(connection,String.valueOf(paciente.getCodigoPersona()), forma);

			} /*else if (estado == ConstantesBD.codigoEstadoHCRespondida) {

				forma.setDiagnosticos("principal", HojaQuirurgica.consultarDiagnosticosUrgencias(connection,numSolicitud));
				HojaQuirurgica.consultarDiagnosticosUrgencias(connection, numSolicitud);
			}*/
		}
		//Fin MT - 4918
		
		//*******************VALIDACION POP OTROS SERVICIOS ATENCION CITAS*****************************************
		if(esdummy&&forma.getFuncionalidad().equals("AtencionCita"))
		{
			//Se verifica si se debe mostrar el link de otros servicios de la cita
			if(
				UtilidadValidacion.esValidoPacienteCargado(connection, paciente).puedoSeguir&&
				UtilidadesHistoriaClinica.obtenerEstadoIngreso(connection, paciente.getCodigoIngreso()).getAcronimo().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto)&&
				UtilidadesConsultaExterna.deboMostrarOtrosServiciosCita(connection, forma.getCodigoCita()))
				forma.setDeboMostrarOtrosServiciosCita(true);
			else
				forma.setDeboMostrarOtrosServiciosCita(false);
		}
		//***********************************************************************************************
		
		//////////////////////////////////////////////////////////////////////////////////////
		//modificado por tarea 20905
		
		forma.setPeticion(forma.getSolicitudMap(indicesSolicitud[18]+"_0")+"");
		forma.setSolicitud(forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"");
		/////////////////////////////////////////////////////////////////////////////////////
		consultParamGenerales(connection, usuario, forma,paciente);
	}
	
	/**
	 * Abre la plantilla del procedimiento asociado
	 * @param Connection con
	 * @param HojaQuirurgicaForm forma
	 * @param UsuarioBasico usuario
	 * */
	public static ActionForward accionAbrirProcedimientoServ(
			Connection con,
			HojaQuirurgicaForm forma,
			UsuarioBasico usuario,
			ActionMapping mapping,
			HttpServletResponse response,
			HttpServletRequest request,
			PersonaBasica paciente)
	{
		UtilidadBD.closeConnection(con);
		try
		{
			response.sendRedirect("../respuestaProcedimientosDummy/respuestaProcedimientos.do?estado=elegirPlantilla&peticion="+forma.getPeticion()+"&numeroSolicitud="+forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"&codigoPaciente="+paciente.getCodigoPersona()+"&servicio="+forma.getServicios(indicesServicios[0]+forma.getIndexServicio()).toString()+"&funcionalidad=hqx");
		}
		catch(IOException e)
		{
			logger.error("Error al direccion la hoja de respuesta de un servicio de procedimiento: "+e);
			ActionErrors errores = new ActionErrors();
			errores.add("",new ActionMessage("error.errorEnBlanco","Error al tratar de abrir la hoja de respuesta del servicio procedimiento. Por favor reportar el error al admisnitrador del sistema"));			
			return mapping.findForward("paginaErroresActionErrors");
		}
		
		return null;
	}
	
	/**
	 * Metodo encargado de cargar la seccion 
	 * de servicos
	 * @param connection
	 * @param forma
	 */
	public static void cargarSecServicios (Connection connection, HojaQuirurgicaForm forma,UsuarioBasico usuario,PersonaBasica paciente, boolean formatearParamGenerales) throws IPSException
	{
		logger.info("\n ******* ENTRO A CARGARSECSERVICIOS ********");
		//se inicializa la seccion
		forma.reset_secServicios(formatearParamGenerales);
		HashMap datos = new HashMap ();
		datos.put("solicitud", forma.getSolicitudMap(indicesSolicitud[8]+"_0"));
		datos.put("institucion", usuario.getCodigoInstitucion());
		datos.put("codigoMedico", usuario.getCodigoPersona());
		datos.put("funcionalidad", forma.getFuncionalidad());
		//se consultan los servicios de la peticion
		forma.setServicios(consultarServicios(connection, datos));
		//se saca una copia del mapa de los servicios
		forma.setServiciosOld((HashMap)forma.getServicios().clone());
		secServiciosConvertirFormatAP(connection, forma, usuario);
		//se ingresa el valor de los honorarios
		//forma.setTipoHonorario(Utilidades.obtenerAsocios(connection, "", ConstantesBD.codigoServicioHonorariosCirugia+"", ConstantesBD.acronimoSi));
		initTipoHonorario(connection, forma, usuario);
		//inicializa la subseccion profesionales
		initSubSecProf(connection, forma, usuario);
		consultParamGenerales(connection, usuario, forma,paciente);
		//logger.info("\n **************SERVICIOS >>>>> "+forma.getServicios());
		
		
		
		//Ingresar parametros para el maejo de la justificación No POS
		int numReg = Integer.parseInt(forma.getServicios("numRegistros")+"");
		String justificar="false";
		for(int i=0;i<numReg;i++)
		{			
    		//Evaluamos si el elemento es No POS
        	if (forma.getServicios(indicesServicios[34]+i).toString().equals("NOPOS")){
        		
        		//Evaluamos la cobertura del Servicio
        		InfoResponsableCobertura infoResponsableCobertura= new InfoResponsableCobertura();
        		infoResponsableCobertura = Cobertura.validacionCoberturaServicio(connection, paciente.getCodigoIngreso()+"", paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoTipoPaciente(), Integer.parseInt(forma.getServicios(indicesServicios[0]+i).toString()), usuario.getCodigoInstitucionInt(), false, "" /*subCuentaCoberturaOPCIONAL*/);
        		forma.setServicios(indicesServicios[32]+i, infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo()+"");
        		forma.setServicios(indicesServicios[33]+i, infoResponsableCobertura.getDtoSubCuenta().getSubCuenta()+"");
        		
        		//Evaluamos si el convenio que cubre el servicio requiere de justificación de servicios
        		if (UtilidadesFacturacion.requiereJustificacioServ(connection, infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo())){
        		
        			//Evaluamos si existe una justificación para el servicio
        			if(FormatoJustServNopos.existeJustificacion(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0").toString(), forma.getServicios(indicesServicios[0]+i).toString()))
        				justificar="justificado";
        			else
        				justificar="true";
        		}
        	}
        	
        	forma.setServicios(indicesServicios[31]+i, justificar);
        
        	//************************************************************************
        	//Se evalua si se debe de mostrar el indicador de información de plantilla
        	forma.setServicios(indicesServicios[35]+i,ConstantesBD.acronimoNo);
        	forma.setServicios(indicesServicios[36]+i,ConstantesBD.acronimoNo);
        				  	
        	if(Servicio.getNumeroFormulariosServicio(
        			connection,
        			Utilidades.convertirAEntero(forma.getServicios(indicesServicios[0]+i).toString()),
        			usuario.getCodigoInstitucionInt()) > 0)        	
        		forma.setServicios(indicesServicios[35]+i,ConstantesBD.acronimoSi);        	
        	
        	//Evalua si tiene respuestas de procedimientos
        	HashMap respuesta =  UtilidadesOrdenesMedicas.obtenerSolCirugiaServicio(connection,forma.getSolicitudMap(indicesSolicitud[8]+"_0").toString(),forma.getServicios(indicesServicios[0]+i).toString());
        	
        	if(!respuesta.get("numRegistros").toString().equals("0"))
        	{      	
        		if(Procedimiento.obtenerCodigosRespuestas(
    					connection,
    					forma.getSolicitudMap(indicesSolicitud[8]+"_0").toString(),
    					respuesta.get("codigo_0").toString()).size() > 0)	
        			forma.setServicios(indicesServicios[36]+i,ConstantesBD.acronimoSi);
        	}
        	//************************************************************************
        	
        	//*****************************************************************************************************
        	//se evalua si el servicio se puede eliminar
        	forma.setServicios(indicesServicios[38]+i,ConstantesBD.acronimoNo);
        	
        	//se evalua si ya existe registro en la tabla sol_cirugia_por_servicio
        	if (UtilidadCadena.noEsVacio(forma.getServicios(indicesServicios[9]+i)+""))
        	{
        		logger.info("\n el valor de cod_sol_cx -->"+forma.getServicios(indicesServicios[9]+i));
        		if (UtilidadesManejoPaciente.obtenerConsecutivoPartoXcodigoSolCxServ(connection, forma.getServicios(indicesServicios[9]+i)+"")<0)
        			forma.setServicios(indicesServicios[38]+i,ConstantesBD.acronimoSi);
        	}
        	else
        		forma.setServicios(indicesServicios[38]+i,ConstantesBD.acronimoSi);
        	
        	//*********************************************************************************************************
        	
        	
        	/*************************************************************************************
			 * Cambio por Anexo 728
			 * 	 * solo aplica cuando se llama la hoja quirurgica desde la Respuesta
			 * de procedimientos
			 */
			if (forma.getFuncionalidad().equals("RespProce") && !(Utilidades.convertirAEntero(forma.getServicios(indicesServicios[10]+i+"_numRegistros")+"")>0))
				if (UtilidadCadena.noEsVacio(forma.getServicios(indicesServicios[19]+i)+"") && Utilidades.convertirAEntero(forma.getServicios(indicesServicios[19]+i)+"")>0)
				{
					forma.setIndexServicio(i+"");
					adicionarProfesionales(connection, forma, usuario);
					forma.setServicios(indicesServicios[10]+i+"_"+indicesProfesionales[3]+"0",usuario.getCodigoPersona());
				}
			/*
			 * 
			 ****************************************************************************************************/
		}
	}
	
	/**
	 * Metodo encargado de cargar los datos de la
	 * seccion informacion quirurgica
	 * @param connection
	 * @param forma
	 */
	public static void cargarSecInfoQx (Connection connection, HojaQuirurgicaForm forma ,UsuarioBasico usuario,PersonaBasica paciente,boolean esDummy,boolean formatearOpera,HttpServletResponse response) throws IPSException
	{
		
		forma.reset_secInfoQx(formatearOpera);
		//se consultan los datos de la peticion
		
		if (forma.isEsDummy())
			forma.setDatosPeticion(consultarDatosPeticion(connection, forma.getPeticion()));
		else
			forma.setDatosPeticion(consultarDatosPeticion(connection, forma.getListadoPeticionesMap(indicesPeticiones[2]+forma.getIndexPeticion())+""));
		/**************************************************************************************************
		 SE VALIDA QUE SI VENGA EL MEDICO QUE SOLICITA SINO SE HACE LOS SIGUIENTE	 
		 ***************************************************************************************************/
		if((forma.getDatosPeticion(indicesDatosPeticion[4])+"").equals(""))
		{ 
		
			forma.setMedicos(UtilidadesAdministracion.obtenerProfesionales(connection, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoNuncaValido,true, true, ConstantesBD.codigoNuncaValido));
		
			forma.setDatosPeticion(indicesDatosPeticion[5], usuario.getCodigoPersona());
		}
		/*****************************************************************************************************/
		
		cargarSecServicios(connection, forma, usuario, paciente, false);
		
		HashMap tmp = new HashMap ();
		//se toma el codigo de la peticion
		tmp.put("peticion", forma.getDatosPeticion(indicesDatosPeticion[0])+"");
			
		tmp=consultaSolicitud(connection, tmp);
		
		//se saca una copia de los datos de la peticion
		forma.setDatosPeticionOld((HashMap)forma.getDatosPeticion().clone());
		
		
		//encargado de consultar los parametros generales
		consultParamGenerales(connection, usuario, forma,paciente);
		//se cargan las fechas y horas de inicio y fin de la cirugia
		forma.setFechasCx(consultarDatosFechas(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""));
		//se saca una copia de las fechas para verificar si fueron modificadas
		forma.setFechasCxOld((HashMap)forma.getFechasCx().clone());
		
		
		//se consulta la informacion de la hoja quirurgica
		forma.setSecInfoQx(consultarDatosHQX(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""));
		/**
		 * Se crea la verificacion de tipo de dato para el campo anestesiologo, cuando se consulta por postgresql este es Integer pero 
		 * por Oracle es BigDecimal
		 * 
		 * Se define que siempre sea BigDecimal para que en los otros metodos que lo invocan no haya problema
		 * 
		 * @author jeilones
		 * @date 05/06/2013
		 * */
		if(forma.getSecInfoQx("anestesiologo")!=null){
			if((forma.getSecInfoQx("anestesiologo") instanceof Number )){
				forma.setSecInfoQx("anestesiologo",BigDecimal.valueOf((Double.parseDouble(forma.getSecInfoQx("anestesiologo").toString()))));
			}else{
				if(forma.getSecInfoQx("anestesiologo") instanceof String && !forma.getSecInfoQx("anestesiologo").toString().trim().isEmpty()){
					forma.setSecInfoQx("anestesiologo",BigDecimal.valueOf(Double.parseDouble(forma.getSecInfoQx("anestesiologo").toString())));
				}else{
					forma.setSecInfoQx("anestesiologo",BigDecimal.valueOf(ConstantesBD.codigoNuncaValido));
				}
			}
		}else{
			forma.setSecInfoQx("anestesiologo",BigDecimal.valueOf(ConstantesBD.codigoNuncaValido));
		}
		forma.setAnestesiologo(Utilidades.convertirAEntero(forma.getSecInfoQx("anestesiologo")+""));
		forma.setFechaInicioAnestesia(forma.getSecInfoQx("fechainianestesia")+"");
		forma.setHoraInicioAnestesia(forma.getSecInfoQx("horainianestesia")+"");
		forma.setFechaFinAnestesia(forma.getSecInfoQx("fechafinanestesia")+"");
		forma.setHoraFinAnestesia(forma.getSecInfoQx("horafinanestesia")+"");
		

		
		//se saca una copia de la informacion par averificar que si fue modificada
		forma.setSecInfoQxOld((HashMap)forma.getSecInfoQx().clone());
		
		//logger.info("\n el valor de la consulta de informacion Qx es --> "+forma.getSecInfoQx());
		if(UtilidadTexto.isEmpty(forma.getSecInfoQx(indicesSecInfoQx[7])+""))
		{
			//se verifica si existe la hoja de anestesia para actualizar el campo de partipo anestesiologo a no
			if ((forma.getParamGenerSecServicos(indicesParamGenerales[11])+"").equals(ConstantesBD.acronimoSi)){
				forma.setSecInfoQx(indicesSecInfoQx[7], ConstantesBD.acronimoSi);
			}
			else
				//se verifica si la hoja Qx existe, si no entonces el campo participo 
				//anestesiologo ve por defecto en no
				if((forma.getParamGenerSecServicos(indicesParamGenerales[15])+"").equals(ConstantesBD.acronimoNo))
					forma.setSecInfoQx(indicesSecInfoQx[7], ConstantesBD.acronimoNo);
		}
		if ((forma.getSecInfoQx(indicesSecInfoQx[7])+"").equals(ConstantesBD.acronimoSi))
			forma.setSecInfoQx(indicesSecInfoQx[13], ConstantesBD.acronimoSi);
		else
			forma.setSecInfoQx(indicesSecInfoQx[13], ConstantesBD.acronimoNo);
		
		//primero verificar que si esta en la hqx luuego en la sala programada y luego muestra todo
		
		if ((forma.getParamGenerSecServicos(indicesParamGenerales[15])+"").equals(ConstantesBD.acronimoSi))
		{
			
			if (!(forma.getSecInfoQx(indicesSecInfoQx[3])+"").equals(""))
			forma.setSalas(UtilidadesSalas.obtenerSalas(connection, usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(), ConstantesBD.acronimoSi, forma.getSecInfoQx(indicesSecInfoQx[9])+""));
		
		}
		else
			//se evaluea si la peticione esta en esado programada o reprogrmada.
			if ((forma.getDatosPeticion(indicesDatosPeticion[7])+"").equals(ConstantesBD.codigoEstadoPeticionProgramada+"") || (forma.getDatosPeticion(indicesDatosPeticion[7])+"").equals(ConstantesBD.codigoEstadoPeticionReprogramada+"") )
			{
				HashMap datosSalaProm = new HashMap ();
				datosSalaProm=consultarSalaProgramada(connection, forma.getDatosPeticion(indicesDatosPeticion[0])+"");
				
				forma.setSecInfoQx(indicesSecInfoQx[9], datosSalaProm.get("tipoSala1"));
				forma.setSecInfoQx(indicesSecInfoQx[3], datosSalaProm.get("sala0"));
				forma.setSalas(UtilidadesSalas.obtenerSalas(connection, usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(), ConstantesBD.acronimoSi, datosSalaProm.get("tipoSala1")+""));
			
			}
			//Más información
		
			//se caran los tipos de salas
			forma.setTiposSalas(Utilidades.obtenerTiposSala(connection, usuario.getCodigoInstitucionInt(), ConstantesBD.acronimoTrueCorto, ConstantesBD.acronimoFalseCorto));
		
			
			
			logger.info("\n el valor del tmp -->"+tmp);
		//se cargan los tipos de anestesia
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaHojaAnestesia(usuario.getCodigoInstitucionInt())))
			forma.setTipoAnestesia(UtilidadesSalas.obtenerTiposAnestesiaInstitucionCentroCosto(connection, usuario.getCodigoInstitucionInt(), Utilidades.convertirAEntero(tmp.get(indicesSolicitud[0]+"_0")+""),"", ""));
		else
			forma.setTipoAnestesia(UtilidadesSalas.obtenerTiposAnestesiaInstitucionCentroCosto(connection, usuario.getCodigoInstitucionInt(), Utilidades.convertirAEntero(tmp.get(indicesSolicitud[0]+"_0")+""), ConstantesBD.acronimoSi, ""));
		//modificado por anexo 728
		if (forma.getFuncionalidad().equals("RespProce"))
			if (forma.getTipoAnestesia().size()==1)
				forma.setSecInfoQx(indicesSecInfoQx[8],forma.getTipoAnestesia().get(0).get("codigo"));
		
		//forma.setTipoAnestesia(Utilidades.obtenerTiposAnestesia(connection, ConstantesBD.acronimoTrueCorto));
		//se cargan los profesionales de la informacion quirurgica
		forma.setProfesionales(consultarProfInfQx(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""));
		//se saca una copia de los profesionales ya ingresdos
		//se saca una copia de los datos de la peticion
		forma.setProfesionalesOld((HashMap)forma.getProfesionales().clone());
		
		//se cargan los profesionales que hacen  cirugias
		forma.setProfesionalesList(UtilidadesAdministracion.obtenerProfesionales(connection, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoNuncaValido,true, true, ConstantesBD.codigoNuncaValido));		
		
		
		//encargado de consultar los asocios diferentes a ayudantia, cirujano, anestesiologo.
		forma.setAsocios(obtenerAsociosDifAyuCirAnes(connection,usuario.getCodigoInstitucionInt()));
		
		forma.setProfesionales(indicesProfInfoQx[4], ConstantesBD.acronimoNo);
		
	
		/********************************************************************
		 * SECCION DE LA INFORMACION QUIRURGICA
		 */
			//se carga el campo de texto informacion Qx
			forma.setSecInfoQx(indicesSecInfoQx[10], consultarCamposTextoHQx(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"", ConstantesIntegridadDominio.acronimoTipoInformacionQuirurgica));
			//se inicializa seccion de infofrmacion Qx en cerrada
			forma.setSecInfoQx(indicesSecInfoQx[11], ConstantesBD.acronimoNo);
		/*
		 * FIN SECCION DE LA INFORMACION QUIRURGICA
		 ********************************************************************/
		
		/********************************************************************
		 * SECCION PERFUSION
		 */
			//aqui se valida si esta seion va o no
			if (UtilidadesSalas.mostrarCampoTextoHqx(connection, ConstantesIntegridadDominio.acronimoTipoPerfusion, forma.getSolicitudMap(indicesSolicitud[0]+"_0")+"", usuario.getCodigoInstitucion()))
				forma.setSecInfoQx(indicesSecInfoQx[20],ConstantesBD.acronimoSi);
			else
				forma.setSecInfoQx(indicesSecInfoQx[20],ConstantesBD.acronimoNo);
			
			//se carga el campo de texto Perfusion
			forma.setSecInfoQx(indicesSecInfoQx[14], consultarCamposTextoHQx(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"", ConstantesIntegridadDominio.acronimoTipoPerfusion));
			//se inicializa seccion de Perfusion en cerrada
			forma.setSecInfoQx(indicesSecInfoQx[15], ConstantesBD.acronimoNo);
		/*
		 * FIN SECCION PERFUSION
		 ********************************************************************/
		
		/********************************************************************
		 * SECCION HALLAZGOS
		 */
			//aqui se valida si esta seion va o no
			if (UtilidadesSalas.mostrarCampoTextoHqx(connection, ConstantesIntegridadDominio.acronimoTipoHallazgos, forma.getSolicitudMap(indicesSolicitud[0]+"_0")+"", usuario.getCodigoInstitucion()))
				forma.setSecInfoQx(indicesSecInfoQx[21],ConstantesBD.acronimoSi);
			else
				forma.setSecInfoQx(indicesSecInfoQx[21],ConstantesBD.acronimoNo);
			
			//se carga el campo de texto Hallazgos 
			forma.setSecInfoQx(indicesSecInfoQx[19], consultarCamposTextoHQx(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"", ConstantesIntegridadDominio.acronimoTipoHallazgos));
			//se inicializa seccion de Hallazgos en cerrada
			forma.setSecInfoQx(indicesSecInfoQx[17], ConstantesBD.acronimoNo);
		/*
		 * FIN SECCION HALLAZGOS
		 ********************************************************************/
				
		//se consulta la salida de la sala
		forma.setSalPac(consultarSalidaPaciente(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""));
		
		HojaAnestesia mundoHAne = new HojaAnestesia();
		//se ingresan a la forma.
		forma.setSalidaPaciente(mundoHAne.consultarSalidasPacienteInstCCosto(connection, usuario.getCodigoInstitucion(), usuario.getCodigoCentroCosto()+"", UtilidadTexto.getBoolean(forma.getSalPac(indicesSalidaSala[8])+"")));		
				
		//se almacena la informacion para saber si es modificada
		forma.setSalPacOld((HashMap)forma.getSalPac().clone());
		
		if ((forma.getSecInfoQx(indicesSecInfoQx[7])+"").equals(ConstantesBD.acronimoSi))
		{
			SolicitudesCx solicitudCx = new SolicitudesCx();		
			solicitudCx.cargarEncabezadoSolicitudCx(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"");
			forma.setSalPac(indicesSalidaSala[0],solicitudCx.getFechaIngresoSala());
			forma.setSalPac(indicesSalidaSala[1],solicitudCx.getHoraIngresoSala());
		}
		
		if (forma.getFuncionalidad().equals("RespProce"))
			if (forma.getSalidaPaciente().size()==1)
				forma.setSalPac(indicesSalidaSala[4], forma.getSalidaPaciente().get(0).get("consecutivo"));
		
		/********************************************************************************
		 * Cambios por anexo 728
		 * se carga el tipo de sala
		 */
			if (forma.getFuncionalidad().equals("RespProce"))
			{
				int tipoSala=Utilidades.obtenerTipoSalaStandar(connection, forma.getServicios(indicesServicios[0]+"0")+"");
				
				if (tipoSala>0)
				{
					forma.setSecInfoQx(indicesSecInfoQx[9], tipoSala);
					forma.setIdTipoSala(tipoSala+"");
					accionFiltrarSalas(connection, forma, usuario, response);
					
				}
			}
		/*
		 * 
		 **********************************************************************************/
		/**
		 * CARGAR LOS ANESTESIOLOGOS
		 * 
		 */
		//Obtienen el listado de medicos por la especialidad
		forma.setAnestesiologos(Utilidades.obtenerMedicosEspecialidad(ValoresPorDefecto.getEspecialidadAnestesiologia(usuario.getCodigoInstitucionInt(), true)));
			
		
		//Carga la informacion de los articulos incluidos en el procedimiento
		forma.setArrayArticuloIncluidoDto(RespuestaProcedimientos.cargarArticulosIncluidosSolicitudDto(connection,forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""));
		forma.setSecInfoQx("abrir_seccion_art_incluidos",ConstantesBD.acronimoSi);	
		forma.setJustificacionMap(new HashMap());
		forma.setHiddens("");
	}
	
	/**
	 * Metodo encargado de cargar el select de la salida del paciente
	 * @param connection
	 * @param usuario
	 * @param esPacienteMuerto
	 * @return
	 */
	public static  ArrayList<HashMap<String, Object>> cargarSelectSalidaPaciente (Connection connection,UsuarioBasico usuario, boolean esPacienteMuerto )
	{
		HojaAnestesia mundoHAne = new HojaAnestesia();
		
		return mundoHAne.consultarSalidasPacienteInstCCosto(connection, usuario.getCodigoInstitucion(), usuario.getCodigoCentroCosto()+"", esPacienteMuerto);
	}
	
	
	
	/**
	 * Metodo enecargado de obtener los asocios
	 * diferentes a cirujano, ayuantia y anestesiologo
	 * @param connection
	 * @param institucion
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerAsociosDifAyuCirAnes (Connection connection, int institucion)
	{

		ArrayList<HashMap<String, Object>> asocios = new ArrayList<HashMap<String,Object>>();
		ArrayList<HashMap<String, Object>> asociosList = new ArrayList<HashMap<String,Object>>();
		asocios=Utilidades.obtenerAsocios(connection, "", ConstantesBD.codigoServicioHonorariosCirugia+"", ConstantesBD.acronimoNo);
		
		for (int i=0;i<asocios.size();i++)
		{
			HashMap elemento = new HashMap ();
			elemento = (HashMap)asocios.get(i);
			if (!(elemento.get("codigo")+"").equals(ValoresPorDefecto.getAsocioAyudantia(institucion)) && 
				!(elemento.get("codigo")+"").equals(ValoresPorDefecto.getAsocioAnestesia(institucion)) &&
				!(elemento.get("codigo")+"").equals(ValoresPorDefecto.getAsocioCirujano(institucion)))
			asociosList.add(elemento);
		}
		
		return asociosList;
	}
	
	/**
	 * Metodo encargado de iniciar la seccion patologia
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void cargarSecPatologia (Connection connection,HojaQuirurgicaForm forma,UsuarioBasico usuario,PersonaBasica paciente,boolean formatearOpera)
	{
		forma.reset_secPato(formatearOpera);
		
		forma.setPatologia(indicesPato[0] ,consultarCamposTextoHQx(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"", ConstantesIntegridadDominio.acronimoTipoPatologia));
		consultParamGenerales(connection, usuario, forma, paciente);
	}
	
	
	/**
	 * Metodo encargado de iniciar la secciuon de observaciones generales
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void cargarSecObsGenerales (Connection connection,HojaQuirurgicaForm forma,UsuarioBasico usuario,PersonaBasica paciente,boolean formatearOpera)
	{
		forma.reset_secObsGenerales(formatearOpera);
		
		forma.setObsGenerales(indicesObsGenerales[0] ,consultarCamposTextoHQx(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"", ConstantesIntegridadDominio.acronimoTipoObservaciones));
		consultParamGenerales(connection, usuario, forma, paciente);
	}
	

	/**
	 * Metodo encargado de iniciar la seccion de notas aclaratorias
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void cargarSecNotasAcla (Connection connection,HojaQuirurgicaForm forma,UsuarioBasico usuario,PersonaBasica paciente,boolean formatearOpera)
	{
		forma.reset_secNotasAcla(formatearOpera);
		HashMap infoQx = new HashMap ();
		forma.setNotasAcla(indicesNotasAcla[0] ,consultarCamposTextoHQx(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"", ConstantesIntegridadDominio.acronimoTipoNotasAclaratorias));
		consultParamGenerales(connection, usuario, forma,paciente);
		
		//se preguna si la hoja qx esta finalizada
		if ((forma.getParamGenerSecServicos(indicesParamGenerales[7])+"").equals(ConstantesBD.acronimoSi))
		{	
			//se pregunta la informacion de la hoja qx
			infoQx=consultarDatosHQX(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"");
			
			//se comparan las fechas 
			String minutos=UtilidadFecha.calcularDuracionEntreFechas(infoQx.get(indicesSecInfoQx[5])+"", infoQx.get(indicesSecInfoQx[6])+"", UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual());
			//se pregunta si el indicativo de cargo es no cruento
			logger.info("\n la diferencia de minutos es -->"+minutos);
			String mintmp[]=minutos.split(":");
			if (mintmp.length>1)
				minutos=mintmp[1];
			else
				minutos="0";
			if ((forma.getSolicitudMap(indicesSolicitud[14]+"_0")+"").equals(ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento))
			{
				logger.info("\n entre a calcular los minutos de limite de ncto minutos -->"+minutos+" min limite -->"+forma.getParamGenerSecServicos(indicesParamGenerales[13]));
				try 
				{
					if (Utilidades.convertirAEntero(minutos)<Utilidades.convertirAEntero(forma.getParamGenerSecServicos(indicesParamGenerales[13])+""))
						forma.setNotasAcla(indicesNotasAcla[2], ConstantesBD.acronimoSi);
					else
						forma.setNotasAcla(indicesNotasAcla[2], ConstantesBD.acronimoNo);
					
				} catch (Exception e)
				{
					logger.info("\n problema con comprobando minutos limite "+e);
				}
			}
			else
				if ((forma.getSolicitudMap(indicesSolicitud[14]+"_0")+"").equals(ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia))
				{
					logger.info("\n entre a calcular los minutos de limite de cx minutos -->"+minutos+" min limite -->"+forma.getParamGenerSecServicos(indicesParamGenerales[14])); 
					try 
					{
						if ( Utilidades.convertirAEntero(minutos)<Utilidades.convertirAEntero(forma.getParamGenerSecServicos(indicesParamGenerales[14])+""))
							forma.setNotasAcla(indicesNotasAcla[2], ConstantesBD.acronimoSi);
						else
							forma.setNotasAcla(indicesNotasAcla[2], ConstantesBD.acronimoNo);
						
					} catch (Exception e) 
					{
						logger.info("\n problema con comprobando minutos limite "+e);
					}
				}
		}
		else
			forma.setNotasAcla(indicesNotasAcla[2], ConstantesBD.acronimoSi);
			
			
		
	}
	
	
	
	/**
	 * Metodo encargado de cargar la seccion de
	 * otros profesionales
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void cargarSecOtrosProfesionales (Connection connection, HojaQuirurgicaForm forma,UsuarioBasico usuario,PersonaBasica paciente,boolean formatearOpera)
	{
		forma.reset_secOtrosProf(formatearOpera);
		//encargado de consultar los parametros generales
		consultParamGenerales(connection, usuario, forma,paciente);
		
		logger.info("\n estoy en otros profesionales solicitud "+forma.getSolicitudMap());
		//cargar los profesionales
	
		forma.setProfesionalesList(UtilidadesAdministracion.obtenerProfesionales(connection, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoNuncaValido, true, true, ConstantesBD.codigoNuncaValido));
	
		//cargar los asocios
		
		//se mira si ya existen almacenados otros profesionales en la tabla otros profesionales para ese numerio de solicitud
		forma.setOtrosProf(consultarOtrosProfesionales(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""));
		//se saca una copia para compara con las cambiadas
		forma.setOtrosProfOld((HashMap)forma.getOtrosProf().clone());
		logger.info("\n numRegistros --> "+forma.getOtrosProf("numRegistros"));
		
		//si no tiene almacenada nada en la tabla otros profesionales
		int numProf=Integer.parseInt(forma.getOtrosProf("numRegistros")+"");
		if (numProf<1)
		{
			logger.info("\n *************- si entre -************ ");
			
			/*-------------------------------------------------------------------*/
			/* PARA EL CASO EN QUE LA HQX ES LLAMADA DE LA FUNCIONALIDAD CITAS                                                                    */
			/*-------------------------------------------------------------------*/
			
			
				//1) se pregunta que si la que lo llama es la funcionalidad de citas o de consultaCitas
				if (forma.getFuncionalidad().equals("Citas") || forma.getFuncionalidad().equals("consultaCitas"))
				{
					logger.info("\n *************- entre citas -************ ");
					//se carga el profesional que solitica
					forma.setOtrosProf(indicesOtrosProfesionales[0]+"0", UtilidadesConsultaExterna.obtenerCodigoMedicoCita(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""));
					//especialidades
					logger.info("\n *************- el codigo medico es -************ "+forma.getOtrosProf(indicesOtrosProfesionales[0]+"0"));
					if (Integer.parseInt(forma.getOtrosProf(indicesOtrosProfesionales[0]+"0")+"")>0)
					{
						forma.setOtrosProf(indicesOtrosProfesionales[4]+"0", Utilidades.obtenerEspecialidadesEnArray(connection, Integer.parseInt(forma.getOtrosProf(indicesOtrosProfesionales[0]+"0")+""), ConstantesBD.codigoNuncaValido));
						forma.setOtrosProf("numRegistros", numProf+1);
						forma.setOtrosProf(indicesOtrosProfesionales[8], 1);
					}
					//especialidad seleccionada
					forma.setOtrosProf(indicesOtrosProfesionales[2]+"0", "");
					
					//asocio
					forma.setOtrosProf(indicesOtrosProfesionales[1]+"0", ConstantesBD.codigoNuncaValido);
					//operacion 
					forma.setOtrosProf(indicesOtrosProfesionales[5]+"0", ConstantesBD.acronimoInsertar);
					
				}
				else
				{
					//////////////////////////////////////////////////////////////////////////////
					 //cambio por tarea 2864
					 ///////////////////////////////////////////////////////////////////////////////
					forma.setOtrosProf("numRegistros", numProf);
					forma.setOtrosProf(indicesOtrosProfesionales[8], 0);
					/*
					logger.info("\n *************- entre flujo normal -************ ");
					//se carga el profesional que solitica ingresado en
					forma.setOtrosProf(indicesOtrosProfesionales[0]+"0", forma.getSolicitudMap(indicesSolicitud[11]+"_0"));
					//especialidades
					forma.setOtrosProf(indicesOtrosProfesionales[4]+"0", Utilidades.obtenerEspecialidadesEnArray(connection, Integer.parseInt(forma.getSolicitudMap(indicesSolicitud[11]+"_0")+""), ConstantesBD.codigoNuncaValido));
					//especialidad seleccionada
					forma.setOtrosProf(indicesOtrosProfesionales[2]+"0", forma.getSolicitudMap(indicesSolicitud[4]+"_0"));
					forma.setOtrosProf("numRegistros", numProf+1);
					
					*/
				}
				
			
			
			
			/*----------------------------------------------------------------------*/
		/*/////////////////////////////////////////////////////////////////////////////////////////
		 ///cambio por tarea 2864
		  //////////////////////////////////////////////////////////////////////////////////////
			//asocio
			forma.setOtrosProf(indicesOtrosProfesionales[1]+"0", ConstantesBD.codigoNuncaValido);
			//operacion 
			forma.setOtrosProf(indicesOtrosProfesionales[5]+"0", ConstantesBD.acronimoInsertar);
			*/
			
		}else
			forma.setOtrosProf(indicesOtrosProfesionales[8], numProf);
		
		//me muestra todas los asocios
		forma.setAsocios(obtenerTiposProfesional(connection, usuario.getCodigoInstitucion()));
		
		
		
	}
	
	
	
	/**
	 * Metodo encargado de darle el formato adecuado para la
	 * vista.
	 * @param forma
	 */
	public static void secServiciosConvertirFormatAP (Connection connection, HojaQuirurgicaForm forma, UsuarioBasico usuario)
	{
		//se calcula si los servicios que tiene alguno es de tipo cirugia o partos y cesarias
		if (Integer.parseInt(forma.getServicios("numRegistros")+"")>0)
		{
			boolean tieneServicioRequerido=false;
			//se inicializan los servicios insertados
			forma.setServicios("servicosInsertados","");
			
			for (int i=0;i< Integer.parseInt(forma.getServicios("numRegistros")+"");i++)
			{
				if ((forma.getSolicitudMap(indicesSolicitud[14]+"_0")+"").equals(ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia))
					if ((forma.getServicios(indicesServicios[5]+i)+"").equals(ConstantesBD.codigoServicioQuirurgico+"") || (forma.getServicios(indicesServicios[5]+i)+"").equals(ConstantesBD.codigoServicioPartosCesarea+""))
						tieneServicioRequerido=true;
				
				//se le indica a cada seccion que debe aparecer cerrada
				if (!forma.getParamGenerSecServicos().containsKey(indicesParamGenerales[3]+i))
				forma.setParamGenerSecServicos(indicesParamGenerales[3]+i, ConstantesBD.acronimoNo);
				//se indica que la seccion profecionales debe aparecer cerrada
				if (!forma.getParamGenerSecServicos().containsKey(indicesParamGenerales[4]+i))
				forma.setParamGenerSecServicos(indicesParamGenerales[4]+i, ConstantesBD.acronimoNo);
				//se indica que la seccion descripciones Qx debe aparecer cerrada
				if (!forma.getParamGenerSecServicos().containsKey(indicesParamGenerales[5]+i))
				forma.setParamGenerSecServicos(indicesParamGenerales[5]+i, ConstantesBD.acronimoNo);
				//se indica que la seccion diagnosticos postoperatorios debe aparecer cerrada
				if (!forma.getParamGenerSecServicos().containsKey(indicesParamGenerales[6]+i))
				forma.setParamGenerSecServicos(indicesParamGenerales[6]+i, ConstantesBD.acronimoNo);
				
				//se consulta si el servicio se encuentra en servicios de via de acceso
				forma.setServicios(indicesServicios[28]+i, UtilidadesSalas.esServicioViaAcceso(connection, Integer.parseInt(forma.getServicios(indicesServicios[0]+i)+""), usuario.getCodigoInstitucionInt())?"S":"N");
				
				//no se tienen en cuenta los que se van a eliminar
				if (!(forma.getServicios(indicesServicios[25]+i)+"").equals(ConstantesBD.acronimoEliminar))
				{
					//se indican los servicios ya ingresados
					if (!UtilidadCadena.noEsVacio(forma.getServicios("servicosInsertados")+""))
						forma.setServicios("servicosInsertados",forma.getServicios(indicesServicios[0]+i)+",");
					else
						forma.setServicios("servicosInsertados",forma.getServicios("servicosInsertados")+""+forma.getServicios(indicesServicios[0]+i)+",");
				}
			}
			forma.setNumeroDeRegistros((forma.getServicios("servicosInsertados").toString().split(",").length));
			if (tieneServicioRequerido==true)
				forma.setServicios(indicesServicios[12], ConstantesBD.acronimoSi);
			else
				forma.setServicios(indicesServicios[12], ConstantesBD.acronimoNo);
		}
	}
	
	
	
	
	/**
	 * Metodo encargado de inicializar los datos de la solicitud
	 * @param forma
	 */
	public static void initSolicitud (HojaQuirurgicaForm forma)
	{
		forma.setSolicitudMap("ccSolicitado0","");
		forma.setSolicitudMap("fechaSolicitud2",UtilidadFecha.getFechaActual());
		forma.setSolicitudMap("horaSolicitud3",UtilidadFecha.getHoraActual());
		forma.setSolicitudMap("especialidad4","");
		forma.setSolicitudMap("urgente5","");
	}
	
	
	/**
	 * Metodo encargado de organizar los datos para enviarcelos 
	 * al sql para ser ingresador a la BD
	 * cambian asi:
	 * -------------------------------------------------------------------------
	 * principal=K031@@@@@10@@@@@ABRASION DE LOS DIENTES
	 * ------------------------to---------------------------------------
	 * principal -->indice
	 * K031 -->codigo
	 * 10 -->tipo cie
	 * ABRASION DE LOS DIENTES -->descripcion
	 * --------------------------------------------------------------------
	 * @param forma
	 * @return mapa
	 */
	public static HashMap convertirFormatoABDDiagnosticos (HashMap diagnosticos, String solicitud, boolean validacionCapitacion)
	{
		//logger.info("\n ente a convertie ABDD "+diagnosticos);
		//se inicializa el vector
		String [] vector={ConstantesBD.codigoNuncaValido+"",ConstantesBD.codigoNuncaValido+"",ConstantesBD.codigoNuncaValido+""};
		//se crea el hashmap donde se van a almacenar los datos 
		//para ser ingresados a la BD.
		HashMap datos = new HashMap ();
		datos.put("numRegistros", 0);
		//se pregunta si el principal fue ingresado (este es requerido para CX no para NCTO)
		if (diagnosticos.containsKey("principal") && !(diagnosticos.get("principal")+"").equals(""))
		{
			//el valor principal viene con el siguiente formato principal=K031@@@@@10@@@@@ABRASION DE LOS DIENTES
			//descripcion del formato:
			//principal -->indice
			//K031 -->codigo
			//10 -->tipo cie
			//ABRASION DE LOS DIENTES -->descripcion
			try 
			{
				vector=(diagnosticos.get("principal")+"").split(ConstantesBD.separadorSplit);
			}
			catch (Exception e)
			{
				logger.info("problema haciendo Split en el diagnostico principal"+e);
			}
			
			
			//numero solicitud
			datos.put(indicesDiagnosticos[1]+"0", solicitud);
			//codigo diagnostico
			datos.put(indicesDiagnosticos[2]+"0", vector[0]);
			//tipo cie
			datos.put(indicesDiagnosticos[3]+"0", vector[1]);
			//principal 
			datos.put(indicesDiagnosticos[4]+"0", ConstantesBD.acronimoSi);
			//esta Bd 
			if (diagnosticos.containsKey("estaBd"))
			{
				//estaBd
				datos.put(indicesDiagnosticos[6]+"0", ConstantesBD.acronimoSi);
				//codigo
				datos.put(indicesDiagnosticos[0]+"0", diagnosticos.get("codigo"));
			}
			else
				datos.put(indicesDiagnosticos[6]+"0", ConstantesBD.acronimoNo);
			
			
			
			datos.put("numRegistros", 1);
			//Validacion Capitacion
			datos.put(indicesDiagnosticos[7]+"0", validacionCapitacion);
			
		}
		//recorremos el hashmap diagnosticos buscando los diagnosticos relacionados
		if (Integer.parseInt(diagnosticos.get("numRegistros")+"")>0)
		{
			for (int i=0;i<Integer.parseInt(diagnosticos.get("numRegistros")+"");i++)
			{
				if (diagnosticos.containsKey(i+"") && !(diagnosticos.get(i+"")+"").equals(""))
				{
					try 
					{
						vector=(diagnosticos.get(i+"")+"").split(ConstantesBD.separadorSplit);
					}
					catch (Exception e)
					{
						logger.info("problema haciendo Split en los diagnosticos relacionados "+e);
					}
					
					int j=(i+1);
					//numero solicitud
					datos.put(indicesDiagnosticos[1]+j, solicitud);
					//codigo diagnostico
					datos.put(indicesDiagnosticos[2]+j, vector[0]);
					//tipo cie
					datos.put(indicesDiagnosticos[3]+j, vector[1]);
					//principal 
					datos.put(indicesDiagnosticos[4]+j, ConstantesBD.acronimoNo);
					//eliminar
					//logger.info("\n el valor del chechk es "+diagnosticos.get("checkbox_"+i)+"   la i es "+i);
					if ((diagnosticos.get("checkbox_"+i)+"").equals("true"))
					{
						datos.put("eliminar_"+j, ConstantesBD.acronimoNo);
					}
					else
						datos.put("eliminar_"+j, ConstantesBD.acronimoSi);
					
					//estaBd
					if (diagnosticos.containsKey("estaBd_"+i))
					{
						datos.put(indicesDiagnosticos[6]+j, ConstantesBD.acronimoSi);
//						codigo
						datos.put(indicesDiagnosticos[0]+j, diagnosticos.get(indicesDiagnosticos[0]+i));
					}
					else
						datos.put(indicesDiagnosticos[6]+j, ConstantesBD.acronimoNo);
					
					datos.put("numRegistros", j+1);
					
				}
			}
			
		}
	
		//logger.info("\n \n saliendo de convertir ABD "+datos);
		return datos;
	}
	
	
	/**
	 * Metodo encargado de convertir a formato de
	 * aplicacion los que viene de la BD 
	 * cambian asi:
	 * -----------------------------------------------------------------
	 * principal -->indice
	 * K031 -->codigo
	 * 10 -->tipo cie
	 * ABRASION DE LOS DIENTES -->descripcion
	 * --------------------TO------------------------------------------------
	 * principal=K031@@@@@10@@@@@ABRASION DE LOS DIENTES
	 * -------------------------------------------------------------
	 * @param forma
	 * @return
	 */
	public static HashMap convertirFormatoAAPDiagnosticos (HashMap diagnosticos)
	{
		HashMap datos = new HashMap ();
		int j=0;
		int numReg = Integer.parseInt(diagnosticos.get("numRegistros")+"");
		//recorremos todo los diagnosticos para encontrar el principal
		for (int i=0;i<Integer.parseInt(diagnosticos.get("numRegistros")+"");i++)
		{
			
			if ((diagnosticos.get(indicesDiagnosticos[4]+i)+"").equals(ConstantesBD.acronimoSi))
			{
				datos.put("principal", diagnosticos.get(indicesDiagnosticos[2]+i)+ConstantesBD.separadorSplit+diagnosticos.get(indicesDiagnosticos[3]+i)+ConstantesBD.separadorSplit+diagnosticos.get(indicesDiagnosticos[5]+i));
				datos.put("estaBd", ConstantesBD.acronimoSi);
				datos.put("codigo", diagnosticos.get(indicesDiagnosticos[0]+i));
			}
			else
				if ((diagnosticos.get(indicesDiagnosticos[4]+i)+"").equals(ConstantesBD.acronimoNo))
				{
					datos.put(j+"", diagnosticos.get(indicesDiagnosticos[2]+i)+ConstantesBD.separadorSplit+diagnosticos.get(indicesDiagnosticos[3]+i)+ConstantesBD.separadorSplit+diagnosticos.get(indicesDiagnosticos[5]+i));
					if (!datos.containsKey("seleccionados"))
						datos.put("seleccionados","'"+diagnosticos.get(indicesDiagnosticos[2]+i)+"'");
					else
						datos.put("seleccionados",datos.get("seleccionados")+",'"+diagnosticos.get(indicesDiagnosticos[2]+i)+"'");
					
					datos.put("checkbox_"+j, true);
					datos.put("estaBd_"+j, ConstantesBD.acronimoSi);	
					datos.put(indicesDiagnosticos[0]+j, diagnosticos.get(indicesDiagnosticos[0]+i));
					j++;
				}
		}
		if (numReg>0)
			datos.put("numRegistros", numReg-1);
		else
			datos.put("numRegistros", numReg);
		
		return datos;
	}
	
	
	
	
	
	
	
	/**
	 * Metodo encargado de convertir los diagnosticos postoperatotios 
	 * a formato de aplicacion los que viene de la BD 
	 * cambian asi:
	 * -----------------------------------------------------------------
	 * principal -->indice
	 * K031 -->codigo
	 * 10 -->tipo cie
	 * ABRASION DE LOS DIENTES -->descripcion
	 * --------------------TO------------------------------------------------
	 * principal=K031@@@@@10@@@@@ABRASION DE LOS DIENTES
	 * -------------------------------------------------------------
	 * @param forma
	 * @return
	 */
	public static HashMap convertirFormatoAAPDiagnosticosPostOpe (HashMap diagnosticos,HashMap datos,int indexserv)
	{
		
		int j=0;
		int restar=0;
		int numReg = Integer.parseInt(diagnosticos.get("numRegistros")+"");
		//recorremos todo los diagnosticos para encontrar el principal
		for (int i=0;i<numReg;i++)
		{
			//se pegunta si es el principal
			if ((diagnosticos.get(indicesDiagPostopera[3]+i)+"").equals(ConstantesBD.acronimoSi))
			{
				datos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[3], diagnosticos.get(indicesDiagPostopera[1]+i)+ConstantesBD.separadorSplit+diagnosticos.get(indicesDiagPostopera[2]+i)+ConstantesBD.separadorSplit+diagnosticos.get(indicesDiagPostopera[5]+i));
				datos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[6], ConstantesBD.acronimoSi);
				datos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[0], diagnosticos.get(indicesDiagPostopera[0]+i));
			
				restar=restar+1;
			}
			else
				//se pregunta si es el diagnostico de complicacion
				if ((diagnosticos.get(indicesDiagPostopera[4]+i)+"").equals(ConstantesBD.acronimoSi))
				{
					datos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[4], diagnosticos.get(indicesDiagPostopera[1]+i)+ConstantesBD.separadorSplit+diagnosticos.get(indicesDiagPostopera[2]+i)+ConstantesBD.separadorSplit+diagnosticos.get(indicesDiagPostopera[5]+i));
					datos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[10], ConstantesBD.acronimoSi);
					datos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[9], diagnosticos.get(indicesDiagPostopera[0]+i));
					restar=restar+1;
				}
				else
					{
						datos.put(indicesServicios[11]+indexserv+"_"+j+"", diagnosticos.get(indicesDiagPostopera[1]+i)+ConstantesBD.separadorSplit+diagnosticos.get(indicesDiagPostopera[2]+i)+ConstantesBD.separadorSplit+diagnosticos.get(indicesDiagPostopera[5]+i));
						if (!datos.containsKey(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[7]))
							datos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[7],"'"+diagnosticos.get(indicesDiagPostopera[1]+i)+"'");
						else
							datos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[7],datos.get(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[7])+",'"+diagnosticos.get(indicesDiagPostopera[1]+i)+"'");
						
						datos.put(indicesServicios[11]+indexserv+"_checkbox_"+j, true);
						datos.put(indicesServicios[11]+indexserv+"_"+"estaBd_"+j, ConstantesBD.acronimoSi);	
						datos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[0]+j, diagnosticos.get(indicesDiagPostopera[0]+i));
						j++;
					}
		}
		
		
		
		if (numReg>0)
			datos.put(indicesServicios[11]+indexserv+"_"+"numRegistros", (numReg-restar));
		else
			datos.put(indicesServicios[11]+indexserv+"_"+"numRegistros", numReg);
		
	
		return datos;
	}
	
	

	/**
	 * Metodo encargado de organizar los datos para enviarcelos 
	 * al sql para ser ingresador a la BD
	 * cambian asi:
	 * -------------------------------------------------------------------------
	 * principal=K031@@@@@10@@@@@ABRASION DE LOS DIENTES
	 * ------------------------to---------------------------------------
	 * principal -->indice
	 * K031 -->codigo
	 * 10 -->tipo cie
	 * ABRASION DE LOS DIENTES -->descripcion
	 * --------------------------------------------------------------------
	 * @param forma
	 * @return mapa
	 */
	public static void convertirFormatoABDDiagnosticosPostOpe (HashMap diagnosticos)
	{
		
		for (int indexserv=0;indexserv<Integer.parseInt(diagnosticos.get("numRegistros")+"");indexserv++)
		{
		
			
			
			//logger.info("\n ente a convertie ABDD "+diagnosticos);
			//se inicializa el vector
			String [] vector={ConstantesBD.codigoNuncaValido+"",ConstantesBD.codigoNuncaValido+"",ConstantesBD.codigoNuncaValido+""};
			//se pregunta si el principal fue ingresado (este es requerido para CX no para NCTO)
			if (diagnosticos.containsKey(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[3]) && !(diagnosticos.get(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[3])+"").equals(""))
			{
				//el valor principal viene con el siguiente formato principal=K031@@@@@10@@@@@ABRASION DE LOS DIENTES
				//descripcion del formato:
				//principal -->indice
				//K031 -->codigo
				//10 -->tipo cie
				//ABRASION DE LOS DIENTES -->descripcion
				try 
				{
					vector=(diagnosticos.get(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[3])+"").split(ConstantesBD.separadorSplit);
				}
				catch (Exception e)
				{
					logger.info("problema haciendo Split en el diagnostico principal"+e);
				}
				
				
				
				//codigo diagnostico
				diagnosticos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[1]+"0", vector[0]);
				//tipo cie
				diagnosticos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[2]+"0", vector[1]);
				//principal 
				diagnosticos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[3]+"0", ConstantesBD.acronimoSi);
				//complicacion
				diagnosticos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[4]+"0", ConstantesBD.acronimoNo);
				//esta Bd 
				if (diagnosticos.containsKey(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[6]))
				{
					//estaBd
					diagnosticos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[6]+"0", ConstantesBD.acronimoSi);
					diagnosticos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[11]+"0", diagnosticos.get(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[0]));
				}
				else
				{
					diagnosticos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[6]+"0", ConstantesBD.acronimoNo);
					diagnosticos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[11]+"0", ConstantesBD.codigoNuncaValido);
				}
				
				
				
				
			}
			
			//se pregunta si el complicacion fue ingresado (este es requerido para CX no para NCTO)
			if (diagnosticos.containsKey(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[4]) && !(diagnosticos.get(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[4])+"").equals(""))
			{
				//el valor principal viene con el siguiente formato complicacion=K031@@@@@10@@@@@ABRASION DE LOS DIENTES
				//descripcion del formato:
				//principal -->indice
				//K031 -->codigo
				//10 -->tipo cie
				//ABRASION DE LOS DIENTES -->descripcion
				try 
				{
					vector=(diagnosticos.get(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[4])+"").split(ConstantesBD.separadorSplit);
				}
				catch (Exception e)
				{
					logger.info("problema haciendo Split en el diagnostico de complicacion"+e);
				}
				
				//codigo diagnostico
				diagnosticos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[1]+"1", vector[0]);
				//tipo cie
				diagnosticos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[2]+"1", vector[1]);
				//principal
				diagnosticos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[3]+"1", ConstantesBD.acronimoNo);
				//complicacion
				diagnosticos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[4]+"1", ConstantesBD.acronimoSi);
				//esta Bd 
				if (diagnosticos.containsKey(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[10]))
				{	//estaBd
					diagnosticos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[6]+"1", ConstantesBD.acronimoSi);
					//codigo
					diagnosticos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[11]+"1", diagnosticos.get(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[9]));
				}
				else
				{
					diagnosticos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[6]+"1", ConstantesBD.acronimoNo);
//					codigo
					diagnosticos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[11]+"1", ConstantesBD.codigoNuncaValido);
				}
				
			
				
			}
				int numDiag = Integer.parseInt(diagnosticos.get(indicesServicios[11]+indexserv+"_"+"numRegistros")+"");
				// se recorren los diagnosticos de cada servicio
			//	logger.info("\n %%%%%%%%%%%%%%%%% NUNGIAG %%%%%%%%%%%--> "+numDiag);
				for (int i=0;i<numDiag;i++)
				{
				//	logger.info("\n ############## el valor es --> "+diagnosticos.get(indicesServicios[11]+indexserv+"_"+i));
					if (diagnosticos.containsKey(indicesServicios[11]+indexserv+"_"+i) && !(diagnosticos.get(indicesServicios[11]+indexserv+"_"+i)+"").equals(""))
					{
						try 
						{
							vector=(diagnosticos.get(indicesServicios[11]+indexserv+"_"+i)+"").split(ConstantesBD.separadorSplit);
						}
						catch (Exception e)
						{
							logger.info("problema haciendo Split en los diagnosticos relacionados "+e);
						}
						
						int j=(i+2);
						//numero solicitud
						
							
						
							
						//codigo diagnostico
						diagnosticos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[1]+j, vector[0]);
						//tipo cie
						diagnosticos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[2]+j, vector[1]);
						//principal 
						diagnosticos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[3]+j, ConstantesBD.acronimoNo);
						//complicacion 
						diagnosticos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[4]+j, ConstantesBD.acronimoNo);
						//eliminar
						//logger.info("\n el valor del chechk es "+diagnosticos.get("checkbox_"+i)+"   la i es "+i);
						if ((diagnosticos.get(indicesServicios[11]+indexserv+"_"+"checkbox_"+i)+"").equals("true"))
							diagnosticos.put(indicesServicios[11]+indexserv+"_"+"eliminar_"+j, ConstantesBD.acronimoNo);
						else
							diagnosticos.put(indicesServicios[11]+indexserv+"_"+"eliminar_"+j, ConstantesBD.acronimoSi);
						
						//estaBd
						if (diagnosticos.containsKey(indicesServicios[11]+indexserv+"_"+"estaBd_"+i))
						{
							diagnosticos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[6]+j, ConstantesBD.acronimoSi);
							diagnosticos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[11]+j, diagnosticos.get(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[0]+i));
						}
						else
						{
							diagnosticos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[6]+j, ConstantesBD.acronimoNo);
							diagnosticos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[11]+j, ConstantesBD.codigoNuncaValido);
						}
						
						
					}
					
								
				}
				diagnosticos.put(indicesServicios[11]+indexserv+"_"+"numRegistros", numDiag+2);
				
				for (int j=0;j<Integer.parseInt(diagnosticos.get(indicesServicios[11]+indexserv+"_"+"numRegistros")+"");j++)
				{
					diagnosticos.put(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[0]+j, diagnosticos.get(indicesServicios[11]+indexserv+"_"+indicesDiagPostopera[11]+j));
				}
		
	}
		
		
		
		
	}
	
	
	/**
	 * Metodo encargado de organizar los datos del nuevo servicio adicionado a
	 * la hoja quirugica.
	 * 
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void cargarValoresPostulados(HojaQuirurgicaForm forma) {
		// logger.info("\n ******* FORMATEAR DATOS SERVICIO "+forma.getServicios());

		// MT-4919 Alejandro Rosas
		// Validación para que a partir del servicio dos parametrize los Dx
		// Principal y Relacionado del servicio 1, permitiendo que estos
		// se puedan modificar.
		if ((Integer.parseInt(String.valueOf(forma.getServicios("numRegistros")))) > 1) {
			
			String valorDiagnosticoPpal = 
				String.valueOf(forma.getServicios().get(indicesServicios[11] + forma.getIndexPostularDiagnostico() + "_"	+ indicesDiagPostopera[3])); 
			
			// operacion25_
			if (forma.getServicios("esNuevo_"+forma.getIndexPostularDiagnostico()) != null && 
					String.valueOf(forma.getServicios("esNuevo_"+forma.getIndexPostularDiagnostico())).equals(ConstantesBD.acronimoNo)) {
				forma.setServicios(indicesServicios[25] + forma.getIndexPostularDiagnostico(), ConstantesBD.acronimoInsertar);
			} else {
			forma.setServicios(indicesServicios[25] + forma.getIndexPostularDiagnostico(), ConstantesBD.acronimoModificar);
			}

			
			
			if (valorDiagnosticoPpal == null || valorDiagnosticoPpal.isEmpty()) {
				forma.getServicios().put(indicesServicios[11] + forma.getIndexPostularDiagnostico() + "_"	+ indicesDiagPostopera[3], 
						forma.getServicios().get(indicesServicios[11] + "0_"	+ indicesDiagPostopera[3]));
				//forma.setServicios(indicesServicios[25] + forma.getIndexPostularDiagnostico(), ConstantesBD.acronimoInsertar);
			}
	
			for (int k = 0; k < Integer.parseInt(String.valueOf(forma.getServicios(indicesServicios[11] + "0_numRegistros"))); k++) {

				// Se asigna al key del diagnostico relacionado de los servicios
				// 2 a n el valor de el diagnostico 1
				if (forma.getServicios().get(indicesServicios[11] + forma.getIndexPostularDiagnostico() + "_" + k) == null) {
					forma.getServicios().put(indicesServicios[11] + forma.getIndexPostularDiagnostico() + "_" + k, 
							forma.getServicios().get(indicesServicios[11] + "0_" + k));
					// Se asigna al key del diagnostico relacionado de los
					// servicio 2 a n su respectivo checkbox con valor por
					// defecto true
					forma.getServicios().put(indicesServicios[11] + forma.getIndexPostularDiagnostico() + "_checkbox_" + k, true);
					Log4JManager.info("Valor diag rel: " + indicesServicios[11]	+ forma.getIndexPostularDiagnostico() + "_" + k);
				}
			}

			if (forma.getServicios(indicesServicios[11] + "0_numRegistros") != null) {
				forma.getServicios().put(indicesServicios[11] + forma.getIndexPostularDiagnostico() + "_numRegistros",
						forma.getServicios(indicesServicios[11]+ "0_numRegistros"));
			}

		} else {
			// diagPostOpera11_
			forma.setServicios(indicesServicios[11] + forma.getIndexPostularDiagnostico() + "_numRegistros", 0);
		}
		// MT-4919
	}
	
	/**
	 * Metodo encargado de organizar los datos del nuevo servicio
	 * adicionado a la hoja quirugica.
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void formatearDatosServicios (Connection connection, HojaQuirurgicaForm forma,UsuarioBasico usuario)
	{
		//logger.info("\n ******* FORMATEAR DATOS SERVICIO "+forma.getServicios());
		
		for (int i=0;i<Integer.parseInt(forma.getServicios("numRegistros")+"");i++)
		{
			if (forma.getServicios().containsKey("esNuevo_"+i) && (forma.getServicios("esNuevo_"+i)+"").equals(ConstantesBD.acronimoSi))
			{				
				//consecutivo
				if(Utilidades.convertirAEntero(forma.getServicios(indicesServicios[26])+"") > 0)
					forma.setServicios(indicesServicios[4]+i,(Utilidades.convertirAEntero(forma.getServicios(indicesServicios[26])+"")+1));
				else
					forma.setServicios(indicesServicios[4]+i,1);
				
				//estaBd8_
				forma.setServicios(indicesServicios[8]+i,ConstantesBD.acronimoNo);
				
				//MT-4919 Alejandro Rosas
				//Validación para que a partir del servicio dos parametrize los Dx Principal y Relacionado del servicio 1, permitiendo que estos
				//se puedan modificar.
				if((Integer.parseInt(String.valueOf(forma.getServicios("numRegistros"))))>1)
				{
					//diagPostOpera11_
					forma.setServicios(indicesServicios[11]+i+"_numRegistros", forma.getServicios(indicesServicios[11]+ "0_numRegistros"));
					
					String DxPpal=String.valueOf(forma.getServicios(indicesServicios[11]+ "0" + "_" + indicesDiagPostopera[3]));
					
					forma.getServicios().put(indicesServicios[11]+ i + "_" + indicesDiagPostopera[3], DxPpal);
					
					for(int k=0;k<Integer.parseInt(String.valueOf(forma.getServicios(indicesServicios[11]+ "0_numRegistros")));k++)
					{
						
						//Se asigna al key del diagnostico relacionado de los servicios 2 a n el valor de el diagnostico 1
						forma.getServicios().put(indicesServicios[11]+ i + "_" +k, forma.getServicios(indicesServicios[11]+ "0_"+k));
						//Se asigna al key del diagnostico relacionado de los servicio 2 a n su respectivo checkbox con valor por defecto true
						forma.getServicios().put(indicesServicios[11]+ i + "_checkbox_" +k, true);

					}

				} else {
				//diagPostOpera11_
				forma.setServicios(indicesServicios[11]+i+"_numRegistros",0);
				}
				//MT-4919
				
				//profecionales10_
				forma.setServicios(indicesServicios[10]+i+"_numRegistros",0);
				//profecionales10_
				forma.setServicios(indicesServicios[10]+i+"_"+indicesProfesionales[17],0);
				
				//naturaleza14_
				forma.setServicios(indicesServicios[14]+i, Utilidades.obtenerNaturalezaServicio(connection, forma.getServicios(indicesServicios[0]+i)+""));
				//finalidad13_
				forma.setServicios(indicesServicios[13]+i,ConstantesBD.codigoNuncaValido);
				//finalidades15_
				HashMap criterios = new HashMap ();
				//naturaleza
				criterios.put("naturaleza", forma.getServicios(indicesServicios[14]+i));
				//institucion
				criterios.put("institucion", usuario.getCodigoInstitucionInt());
				//se consultan las finalidades del servicio
				forma.setServicios(indicesServicios[15]+i,consultarFinalidadServicio(connection, criterios));
				
				
				//observaciones7_
				forma.setServicios(indicesServicios[7]+i,"");
				//estaNuevo_
				forma.setServicios("esNuevo_"+i,ConstantesBD.acronimoNo);
				//viaCx16_
				forma.setServicios(indicesServicios[16]+i,"");
				//indBilateral17_
				forma.setServicios(indicesServicios[17]+i,ConstantesBD.acronimoNo);
				//indViaAcceso18_
				forma.setServicios(indicesServicios[18]+i,ConstantesBD.acronimoNo);
				
				logger.info("\n \n la especialidad del servicio -->"+forma.getServicios(indicesServicios[37]+i));
			
				
				/********************************************************************************************************
				 * especialidadInter19_
				 * Cambio por anexo 728
				 * se evalua si la especialidad que tiene
				 * el servicio la tiene el medico, de ser asi
				 * se postula esta especialidad.
				 * Este cambio solo apliaca cuando la hoja quirurgica
				 * se llama desde la Respuesta de Procedimientos
				 */
					if (forma.getFuncionalidad().equals("RespProce"))
					{
						HashMap especialidadesMedico=Utilidades.obtenerEspecialidadesMedico(connection, usuario.getCodigoPersona()+"");
						int numReg = Utilidades.convertirAEntero(especialidadesMedico.get("numRegistros")+"");
					
							//logger.info("\n \n entre a verificar la  especialidad ---"+mapa.get(indicesServicios[37]+j));
							if (numReg>1)
							{
								for (int j=0;j<numReg;j++)
								{
									//logger.info("\n \n especialidad medico -->"+especialidadesMedico.get("codigo_"+i)+" ---especialidad servicio  -->"+mapa.get(indicesServicios[37]+j));
									if ((especialidadesMedico.get("codigo_"+j)+"").equals(forma.getServicios(indicesServicios[37]+i)+""))
									{
										forma.setServicios(indicesServicios[19]+i, especialidadesMedico.get("codigo_"+j));
										//logger.info("\n\n LA ESPECIALIDAD A POSTULAR ES --> "+especialidadesMedico.get("codigo_"+i));
									}
								}
							}
							else
								if (numReg==1)
									forma.setServicios(indicesServicios[19]+i, especialidadesMedico.get("codigo_0"));
					}
					else
						//especialidadInter19_
						forma.setServicios(indicesServicios[19]+i,ConstantesBD.codigoNuncaValido);
				/*
				 * 
				 *******************************************************************************************************************************/
				
				
				//tiposHonorario22_
				cargarTipoHonorario(connection, forma, usuario);
				//descripcionQx24_
				forma.setServicios(indicesServicios[24]+i, "");
				//operacion25_
				forma.setServicios(indicesServicios[25]+i,ConstantesBD.acronimoInsertar);
				
				/**************************************************************************************************
				 * Selecciona el tipo de honorario cirujano
				 * Cambio por anexo 728 
				 * solo aplica cuando se llama la hoja quirurgica desde la Respuesta
				 * de procedimientos
				 */
				if (forma.getFuncionalidad().equals("RespProce"))
				//se postula el tipo de honorario cirujano
					forma.setServicios(indicesServicios[10]+i+"_"+indicesProfesionales[12]+forma.getServicios(indicesServicios[10]+i+"_numRegistros"),ValoresPorDefecto.getAsocioCirujano(usuario.getCodigoInstitucionInt()));
				/*
				 * 
				 *************************************************************************************************/
				
				/*************************************************************************************
				 * Cambio por Anexo 728
				 * 	 * solo aplica cuando se llama la hoja quirurgica desde la Respuesta
				 * de procedimientos
				 */
				//logger.info("\n ########## voyy a entrar a postular el profesional el servicio --> "+forma.getServicios(indicesServicios[19]+i));
				if (forma.getFuncionalidad().equals("RespProce"))
					if (UtilidadCadena.noEsVacio(forma.getServicios(indicesServicios[19]+i)+"") && Utilidades.convertirAEntero(forma.getServicios(indicesServicios[19]+i)+"")>0)
					{
						//logger.info("\n entre a postular el profesional ");
						forma.setIndexServicio(i+"");
						adicionarProfesionales(connection, forma, usuario);
						forma.setServicios(indicesServicios[10]+i+"_"+indicesProfesionales[3]+"0",usuario.getCodigoPersona());
					}
				/*
				 * 
				 ****************************************************************************************************/
				
				
				//totSev26				
				if(Utilidades.convertirAEntero(forma.getServicios(indicesServicios[26])+"") > 0)
					forma.setServicios(indicesServicios[26],(Utilidades.convertirAEntero(forma.getServicios(indicesServicios[26])+"")+1));
				else				
					forma.setServicios(indicesServicios[26],1);
				
				//************************************************************************
	        	//Se evalua si se debe de mostrar el indicador de información de plantilla
	        	forma.setServicios(indicesServicios[35]+i,ConstantesBD.acronimoNo);
	        	forma.setServicios(indicesServicios[36]+i,ConstantesBD.acronimoNo);
	        				  	
	        	if(Servicio.getNumeroFormulariosServicio(
	        			connection,
	        			Utilidades.convertirAEntero(forma.getServicios(indicesServicios[0]+i).toString()),
	        			usuario.getCodigoInstitucionInt()) > 0)        	
	        		forma.setServicios(indicesServicios[35]+i,ConstantesBD.acronimoSi);
			

	        	//*****************************************************************************************************
	        	//se evalua si el servicio se puede eliminar
	        	forma.setServicios(indicesServicios[38]+i,ConstantesBD.acronimoNo);
	        	
	        	//se evalua si ya existe registro en la tabla sol_cirugia_por_servicio
	        	if (UtilidadCadena.noEsVacio(forma.getServicios(indicesServicios[9]+i)+""))
	        	{
	        		if (UtilidadesManejoPaciente.obtenerConsecutivoPartoXcodigoSolCxServ(connection, forma.getServicios(indicesServicios[9]+i)+"")<0)
	        			forma.setServicios(indicesServicios[38]+i,ConstantesBD.acronimoSi);
	        	}
	        	else
	        		forma.setServicios(indicesServicios[38]+i,ConstantesBD.acronimoSi);
	        	
	        	//*********************************************************************************************************
	        	
			}
		}		
	}
	
		
	/**
	 * 
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	public static ActionForward guardarSecGeneral (Connection connection, HojaQuirurgicaForm forma, ActionMapping mapping,UsuarioBasico usuario,PersonaBasica paciente,boolean esDummy,HttpServletRequest request, boolean validacionCapitacion)
	{
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		
		HashMap diagnosticos = new HashMap ();
		diagnosticos=convertirFormatoABDDiagnosticos(forma.getDiagnosticos(), (forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""), validacionCapitacion);
		consultarOperaciones(diagnosticos,forma.getDiagnosticosClone());
		//logger.info("\n ******** diagnosticos *******> "+diagnosticos);
		//logger.info("\n ******** diagnosticos clone *******> "+forma.getDiagnosticosClone());
		//logger.info("\n ******** EXISTE HOJA QX *******> "+forma.isExisteHojaQx());
		
		//se pregunta si el numero de autorizacion fue modificado
		if (!(forma.getSolicitudMap(indicesSolicitud[1]+"_0")+"").equals(forma.getNumAutorizacionOld()))
		{
			//logger.info("\n ********Modificar Numero Autorizacion*********"+forma.getNumAutorizacionOld()+" la nueva "+(forma.getSolicitudMap(indicesSolicitud[1]+"_0")+""));
			if (transacction)
				transacction=modificarNumAutorizacion(connection, forma.getSolicitudMap());
		
		}
		
		
		//se pregunta si ya fue ingresada la solicitud en la hoja quirurgica.
		if (!forma.isExisteHojaQx())
		{
			//logger.info("\n ********insertar Hoja Qx Basica *********");
			if (transacction)
			transacction=insertarHojaQxBasica(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"" , usuario, validacionCapitacion);
		}
		
		for (int i=0;i<Integer.parseInt(diagnosticos.get("numRegistros")+"");i++)
		{
			if ((diagnosticos.get("operacion_"+i)+"").equals("E"))
			{
			//	logger.info("\n ********Eliminar*********");
				if (transacction)
					transacction=eliminarBDDiagPreope(connection, Listado.copyOnIndexMap(diagnosticos, i+"", indicesDiagnosticos));
			}
			else
				if ((diagnosticos.get("operacion_"+i)+"").equals("I"))
				{
					logger.info("\n ********Insertar*********");
					if (transacction)
						transacction=insertarBDDiagPreope(connection, Listado.copyOnIndexMap(diagnosticos, i+"", indicesDiagnosticos));
				}
				else
					if ((diagnosticos.get("operacion_"+i)+"").equals("M"))
					{
						logger.info("\n ********Modificar*********");
						if (transacction)
							transacction=modificarBDDiagPreope(connection, Listado.copyOnIndexMap(diagnosticos, i+"", indicesDiagnosticos));
					}
					else
						if ((diagnosticos.get("operacion_"+i)+"").equals("N"))
							logger.info("\n ********Nada*********");
		}
		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(connection);			
			logger.info("----->TERMINO SECCION GENERAL AL 100% ");
			forma.setOperacionTrue(true);
			
		}
		else
		{
			UtilidadBD.abortarTransaccion(connection);
		}
		//se consulta la seccion general
		cargarSecGeneral(connection, forma,usuario,paciente,esDummy,request,false);
		
		return mapping.findForward("principal");		 		
	}
	
	/**
	 * Metodo encargado de recorrer los diagnostico y evaluar que
	 * operacion realizar sobre cada tupla. 
	 *
	 * @param diagnosticos
	 * @return
	 */
	public static HashMap consultarOperaciones (HashMap diagnosticos, HashMap diagnosticosOld)
	{
		//se evalua la principal aparte que se puede insertar y modificar
		if (diagnosticos.containsKey(indicesDiagnosticos[6]+"0") && (diagnosticos.get(indicesDiagnosticos[6]+"0")+"").equals(ConstantesBD.acronimoNo))
			diagnosticos.put("operacion_0", "I");
		else
			if (diagnosticos.containsKey(indicesDiagnosticos[6]+"0") && (diagnosticos.get(indicesDiagnosticos[6]+"0")+"").equals(ConstantesBD.acronimoSi))
			{
				for (int j=0;j<Integer.parseInt(diagnosticosOld.get("numRegistros")+"");j++)
				{
					//se pregunta cual es el principal para compararlo
					if ((diagnosticosOld.get(indicesDiagnosticos[4]+j)+"").equals(ConstantesBD.acronimoSi))
						if (!(diagnosticos.get(indicesDiagnosticos[2]+"0")+""+diagnosticos.get(indicesDiagnosticos[3]+"0")+diagnosticos.get(indicesDiagnosticos[4]+"0")+"").equals(
							 diagnosticosOld.get(indicesDiagnosticos[2]+j)+""+diagnosticosOld.get(indicesDiagnosticos[3]+j)+diagnosticosOld.get(indicesDiagnosticos[4]+j)))
							diagnosticos.put("operacion_0", "M");
						else
							diagnosticos.put("operacion_0", "N");
				}
			}
		
		for (int i=1;i<Integer.parseInt(diagnosticos.get("numRegistros")+"");i++)
		{
			if (diagnosticos.containsKey(indicesDiagnosticos[6]+i) && (diagnosticos.get(indicesDiagnosticos[6]+i)+"").equals(ConstantesBD.acronimoSi))
			{
				if (diagnosticos.containsKey("eliminar_"+i) && (diagnosticos.get("eliminar_"+i)+"").equals(ConstantesBD.acronimoSi))
					diagnosticos.put("operacion_"+i, "E");
				else
					diagnosticos.put("operacion_"+i, "N");
		
			}
			else
				if (diagnosticos.containsKey("eliminar_"+i) && (diagnosticos.get("eliminar_"+i)+"").equals(ConstantesBD.acronimoNo))
					diagnosticos.put("operacion_"+i, "I");
				else
					diagnosticos.put("operacion_"+i, "N");
		}
		
		
		
		return diagnosticos;
	}
	
	
	
	/**
	 * Metodo encargado de insertar
	 * los datos de los diagnosticos en la BD
	 * @param connection
	 * @param diagnosticos
	 * @return
	 */
	public static boolean insertarBDDiagPreope (Connection connection,HashMap diagnosticos)
	{
		HashMap datos = new HashMap ();
	
			//logger.info("\n entro a  insertarBDDiagPreope "+diagnosticos);
			//solicitud
			datos.put("solicitud", diagnosticos.get(indicesDiagnosticos[1]+"0"));
			//diagnostico
			datos.put("diagnostico", diagnosticos.get(indicesDiagnosticos[2]+"0"));
			//tipoCie
			datos.put("tipoCie", diagnosticos.get(indicesDiagnosticos[3]+"0"));
			//principal
			if ((diagnosticos.get(indicesDiagnosticos[4]+"0")+"").equals("N"))
				datos.put("principal", false);
			else
				datos.put("principal", true);
			
			//logger.info("\n saliendo de   insertarBDDiagPreope "+datos);
			return insertarDiacnosticosPreoperatorios(connection, datos);
	
	}
	
	/**
	 * Metodo encargado de eliminar un diagnostico 
	 * preoperatorio.
	 * @param connection
	 * @param diagnosticos
	 * @return
	 */
	public static boolean eliminarBDDiagPreope (Connection connection,HashMap diagnosticos)
	{
		HashMap datos = new HashMap ();
		//logger.info("\n ENTRO A eliminarBDDiagPreope "+diagnosticos);
		//codigo
		datos.put("codigo", diagnosticos.get(indicesDiagnosticos[0]+"0"));
		
		return eliminarDiagnostioPreoperatorio(connection, datos);
	}
	
	
	/**
	 * Metodo encargado de modificar los diagnosticos
	 * preoperatorios.
	 * @param connection
	 * @param diagnosticos
	 * @return
	 */
	public static boolean modificarBDDiagPreope (Connection  connection ,HashMap diagnosticos)
	{
		HashMap datos = new HashMap ();
	//	logger.info("\n ENTRO A modificarBDDiagPreope "+diagnosticos);
		//diagnostico
		datos.put("diagnostico", diagnosticos.get(indicesDiagnosticos[2]+"0"));
		//tipoCie
		datos.put("tipoCie", diagnosticos.get(indicesDiagnosticos[3]+"0"));
		//principal
		if ((diagnosticos.get(indicesDiagnosticos[4]+"0")+"").equals(ConstantesBD.acronimoSi))
			datos.put("principal", true);
		else
			datos.put("principal", false);
		//codigo
		datos.put("codigo", diagnosticos.get(indicesDiagnosticos[0]+"0"));
		
		return modificarDiagnosticoPreoperatirio(connection, datos);
	}
	
	
	
	/**
	 * Metodo utilizado para ingresar la informacion basico
	 * a la hoja quirurgica
	 * @param connection
	 * @param diagnosticos
	 * @param usuario
	 * @return
	 */
	public static boolean insertarHojaQxBasica (Connection connection, String solicitud, UsuarioBasico usuario, boolean validacionCapitacion)
	{
		HashMap datos = new HashMap ();
		//numero solicitud
		datos.put("numSolicitud", solicitud);
		//finalizada
		datos.put("finalizada", false);
		//datos medico
		datos.put("datosMedico", usuario.getInformacionGeneralPersonalSalud());
		//Se valida parametro si es true se le asigna descripcion para guardar en la tabla hoja_quirurgica de la bd
		if(validacionCapitacion){
			String mensaje = "Se ha respondido la Orden sin autorizaciÃ³n de capitaciÃ³n subcontratada";
			datos.put("validacionCapitacion", mensaje);
		}
		
		cambiarEstadoSolicitud(connection, ConstantesBD.codigoEstadoHCRespondida+"", solicitud, "");
		Solicitud sol= new Solicitud();
		sol.actualizarMedicoResponde(connection, Integer.parseInt(solicitud), usuario);
		
		return insertarHojaQxBasica(connection, datos);
	}
	
	/**
	 * Metodo encargado de actualizar el numero de 
	 * autorizacion si cambia.
	 * @param connection
	 * @param solicitud
	 * @return
	 */
	public static boolean modificarNumAutorizacion (Connection connection, HashMap solicitud)
	{
		HashMap datos = new HashMap ();
		//numero autorizacion
		datos.put("autorizacion", solicitud.get(indicesSolicitud[1]+"_0"));
		//solicitud
		datos.put("solicitud", solicitud.get(indicesSolicitud[8]+"_0"));
		
		
		return actualizarAutorizacion(connection, datos);
	}
	
	 /**
     * Método implementado para insertar un profesional de la cirugía
     * @param con
     * @param profesional
     * @return
     */
    public static int insertarProfesionalCirugia(Connection con,DtoProfesionalesCirugia profesional,String usuario)
    {
  	  return hojaQxDao().insertarProfesionalCirugia(con, profesional,usuario);
    }
    
    /**
     * Método que consulta las descripciones Qx de una cirugía
     * @param con
     * @param consecutivoSolCx
     * @return
     * ------------------------
	 * KEY'S DEL MAPA DATOS
	 * ------------------------
	 * codigo_, descripcion_, consecutivoServicio_, fechaGrabacion, horaGrabacion_, usuarioGrabacion_
     */
    public static String consultarDescripcionesQx(Connection con,String consecutivoSolCx)
    {
    	return hojaQxDao().consultarDescripcionesQx(con, consecutivoSolCx);
    }
    
    /**
     * Método que realiza la inserción de un diagnóstico postoperatorio
     * @param con
     * @param campos
     * @return
     */
    public static int insertarDiagnosticoPostOperatorio(Connection con,String consecutivoSolCx,String acronimoDiagnostico,int codigoTipoCie,boolean principal,boolean complicacion,String usuario)
    {
    	HashMap campos = new HashMap();
    	campos.put("consecutivoSolCx",consecutivoSolCx);
    	campos.put("acronimoDiagnostico",acronimoDiagnostico);
    	campos.put("codigoTipoCie",codigoTipoCie);
    	campos.put("principal",principal);
    	campos.put("complicacion",complicacion);
    	campos.put("usuario",usuario);
    	return hojaQxDao().insertarDiagnosticoPostOperatorio(con, campos);
    }
    
    /**
     * Método para Insertar la descripcion qx de un servicio 
     * @param con
     * @param campos
     * @return
     */
    public static int insertarDescripcionQx(Connection con,String consecutivoSolCx,String descripcion,String loginUsuario)
    {
    	HashMap campos = new HashMap();
    	campos.put("consecutivoSolCx",consecutivoSolCx);
    	campos.put("descripcion",descripcion);
    	campos.put("usuario",loginUsuario);
    	return hojaQxDao().insertarDescripcionQx(con, campos);
    }
	
    /**
     * Método que realiza la modificacion de un profesional de una cirugia
     * @param con
     * @param campos
     * @return
     */
    public static int modificarProfesionalCirugia(Connection con,DtoProfesionalesCirugia profesional,String usuario)
    {
    	
    	return hojaQxDao().modificarProfesionalCirugia(con, profesional,usuario);
    }
    
    /**
	 * Método usado para modificar la hoja quirúrgica
	 * @param con
	 * @param numeroSolicitud
	 * @param duracion
	 * @param sala
	 * @param tipoSala
	 * @param politrauma
	 * @param tipoHerida
	 * @param finalizada
	 * @param usuarioFinaliza
	 * @param fechaFinaliza
	 * @param horaFinaliza
	 * @param datosMedico
	 * @param estado
	 * @return
	 */
	public static int modificarTransaccional(
			Connection con,int numeroSolicitud,
			String duracion,int sala,int tipoSala,String politrauma,
			int tipoHerida,String finalizada,String usuarioFinaliza,
			String fechaFinaliza,String horaFinaliza,String datosMedico,String estado)
	{
		return hojaQxDao().modificarTransaccional(con, numeroSolicitud, duracion, sala, tipoSala, politrauma, tipoHerida, finalizada, usuarioFinaliza, fechaFinaliza, horaFinaliza, datosMedico, estado);
	}
	
	/**
	 * Método implementado para actualizar los datos de un profesional de acto quirurgico
	 * @param con
	 * @param asocio (no requirido)
	 * @param codigoProfesional (no requerido)
	 * @param cobrable (no requerido)
	 * @param loginUsuario
	 * @param consecutivo
	 * @return
	 */
	public static int actualizarProfesionalActoQx(Connection con,int asocio,int codigoProfesional,String cobrable,String loginUsuario,String consecutivo)
	{
		HashMap campos = new HashMap();
		campos.put("asocio",asocio);
		campos.put("codigoProfesional",codigoProfesional);
		campos.put("cobrable",cobrable);
		campos.put("loginUsuario",loginUsuario);
		campos.put("consecutivo",consecutivo);
		
		return hojaQxDao().actualizarProfesionalActoQx(con, campos);
	}
	
	
	/**
	 * Metodo encargado de verificar
	 * si la hoja Qx esta finalizada o no.
	 * @param connection
	 * @param solicitud
	 * @return (S/N)
	 */
	public static String estaFinalizadaHojaQx (Connection connection,String solicitud)
	{
		return hojaQxDao().estaFinalizadaHojaQx(connection, solicitud);
	}
	
	
	public static void consultarEspecialidades (Connection connection, HojaQuirurgicaForm forma)
	{
		HojaAnestesia mundoAnestesia = new HojaAnestesia();
		HashMap espec = new HashMap ();
		@SuppressWarnings("unused")
		HashMap profecionales = new HashMap ();
		
		//aqui se examina la tabla esp_intervienen_solcx
		//para saber si existen especialidades y guardadas
		//(estas se pueden guardar desde la Hoja de anestesia o dede la hoja quirurgica)
		espec=mundoAnestesia.consultarEspecialidadesIntervienen(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"","");
		
		if (Integer.parseInt(espec.get("numRegistros")+"")>0)
		{
			for (int i=0;i< Integer.parseInt(espec.get("numRegistros")+"");i++)
			{
				HashMap criterios = new HashMap ();
				criterios.put("codigoSolCXSer",forma.getServicios(indicesServicios[9]));
				profecionales=consultarProfecionalesCx(connection, criterios);
			}
		}
	}
	
	/**
	 * Método que verifica si una orden de cirugia posee una sala que esta ocupada
	 * por otras ordenes
	 * @param con
	 * @return
	 */
	public static HashMap estaSalaOcupada(Connection con,int numSol,int codSala,String fechaIni,String horaIni,String fechaFin,String horaFin)
	{
		return hojaQxDao().estaSalaOcupada(con,numSol,codSala,fechaIni,horaIni,fechaFin,horaFin);
	}
	
	/**
	 * Metodo encargado de Adicionar profesionales 
	 * a cada servicio.
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void adicionarProfesionales (Connection connection,HojaQuirurgicaForm forma,UsuarioBasico usuario)
	{
		
		int indexSer=Integer.parseInt(forma.getIndexServicio());
		int numProf = Integer.parseInt(forma.getServicios(indicesServicios[10]+indexSer+"_numRegistros")+"");
		logger.info("\n entre a  adicionarProfesionales    numProf -->"+numProf);
		int totProf = Integer.parseInt(forma.getServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[17])+"");
		

		//profecionales10_0_tipoAsocio1_0
		//tipoAsocio1_
		forma.setServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[1]+numProf, forma.getServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[12]+numProf));
		///profecionales10_0_nomTipoAsocio9_0
		//nomTipoAsocio9_
		forma.setServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[9]+numProf,getNombreAsocio(connection, forma.getServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[12]+numProf)+""));
		
		
		//si el tipo de honorario es cirugia se hace lo siguiente.
		if ((forma.getServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[12]+numProf)+"").equals(ValoresPorDefecto.getAsocioCirujano(usuario.getCodigoInstitucionInt())))
		{		
			//////profecionales10_0_profesionales10_0
			//profesionales10_
			forma.setServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[10]+numProf,UtilidadesAdministracion.obtenerProfesionales(connection, usuario.getCodigoInstitucionInt(), Integer.parseInt(forma.getServicios(indicesServicios[19]+indexSer)+""), true, true, ConstantesBD.codigoNuncaValido));
			//profecionales10_0_codigoProfecional3_0
			//codigoProfecional3_
			forma.setServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[3]+numProf,ConstantesBD.codigoNuncaValido);
			//profecionales10_0_especialidad2_
			//especialidad2_
			forma.setServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[2]+numProf,forma.getServicios(indicesServicios[19]+indexSer));
			//profecionales10_0_especialidades11_0
			//especialidades11_
			forma.setServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[11]+numProf,Utilidades.obtenerEspecialidadesEnArray(connection, ConstantesBD.codigoNuncaValido, Integer.parseInt(forma.getServicios(indicesServicios[19]+indexSer)+"")));
			//profecionales10_0_cobrable4_0
			//cobrable4_
			forma.setServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[4]+numProf,ConstantesBD.acronimoSi);
			
			
			
		}
		else
		{
			///profecionales10_0_profesionales10_0
			//profesionales10_
			forma.setServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[10]+numProf,UtilidadesAdministracion.obtenerProfesionales(connection, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoNuncaValido, true, true, ConstantesBD.codigoNuncaValido));
			//profecionales10_0_codigoProfecional3_0
			//codigoProfecional3_
			forma.setServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[3]+numProf,ConstantesBD.codigoNuncaValido);
			//profecionales10_0_especialidades11_0
			//especialidades11_
			forma.setServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[11]+numProf,Utilidades.obtenerEspecialidadesEnArray(connection, ConstantesBD.codigoNuncaValido,ConstantesBD.codigoNuncaValido));
			//profecionales10_0_especialidad2_
			//especialidad2_
			forma.setServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[2]+numProf,ConstantesBD.codigoNuncaValido);
			//profecionales10_0_cobrable4_0
			//cobrable4_
			forma.setServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[4]+numProf,ConstantesBD.acronimoSi);
		}
			
		//estaBd7_		
		forma.setServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[7]+numProf , ConstantesBD.acronimoNo);
		//profecionales10_0_eliminado13_0
		//eliminado13_
		forma.setServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[13]+numProf,ConstantesBD.acronimoNo);
		
		//numRegistros
		forma.setServicios(indicesServicios[10]+indexSer+"_numRegistros", numProf+1);
		
		//totProf17
		forma.setServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[17], totProf+1);
		
		
		//se recargan los honorarios
		cargarTipoHonorario(connection, forma, usuario);
	}
	
	
		
		
	public static void cargarTipoHonorario (Connection connection,HojaQuirurgicaForm forma,UsuarioBasico usuario )
	{
		//se toma el numero de servicios que existen
		int numServicios  = Integer.parseInt(forma.getServicios("numRegistros")+"");
		//ahora se consultan los asocios por si cambiaron
		ArrayList tiposAsoc = new ArrayList ();
		tiposAsoc=Utilidades.obtenerAsocios(connection, "", ConstantesBD.codigoServicioHonorariosCirugia+"", ConstantesBD.acronimoSi);
		
		
		logger.info("\n \ntipos asoc-->"+tiposAsoc);
		
		//ahora se procede a iterar los servicios
		
		for(int i=0;i<numServicios;i++)
		{
			ArrayList tiposAsocios = new ArrayList ();
			String [] indicesTipoAsocios = {"codigo", "codigo_asocio", "tipos_servicio", "nombre_asocio", "participa_cir", "centro_costo_ejecuta", "seleccionado"};
			tiposAsocios=Listado.copyArray(tiposAsoc,indicesTipoAsocios);
			//tiposAsocios=Utilidades.obtenerAsocios(connection, "", ConstantesBD.codigoServicioHonorariosCirugia+"", ConstantesBD.acronimoSi);
			//logger.info("tipos asocios-->"+tiposAsocios);
			//ahora de cade servicios se busca la cantiadad de profecionales
			int numProfecionales = Integer.parseInt(forma.getServicios(indicesServicios[10]+i+"_numRegistros")+"");
			//logger.info("\n *************** numRegistros profe del servicio "+i+" -->"+forma.getServicios(indicesServicios[10]+i+"_numRegistros"));
			if (numProfecionales>0)
			{
				for (int j=0;j<tiposAsocios.size();j++)
				{
					
					boolean tieneasoc = false;
					HashMap elemento = (HashMap)tiposAsocios.get(j);
					
					for (int p=0;p<numProfecionales;p++)
					{
						//se valida si es el asocio de ayudantia para dejar crear N
						if (!(forma.getServicios(indicesServicios[10]+i+"_"+indicesProfesionales[12]+p)+"").equals(
								ValoresPorDefecto.getAsocioAyudantia(usuario.getCodigoInstitucionInt())))
						{
							//ahora se recorren todos los asocios
							//logger.info("\n "+elemento.get("codigo")+"=="+forma.getServicios(indicesServicios[10]+i+"_"+indicesProfesionales[1]+p));
							//logger.info("\n eliminado "+forma.getServicios(indicesServicios[10]+i+"_"+indicesProfesionales[13]+p));
								if ((elemento.get("codigo")+"").equals(forma.getServicios(indicesServicios[10]+i+"_"+indicesProfesionales[1]+p)+"") && 
										(forma.getServicios(indicesServicios[10]+i+"_"+indicesProfesionales[13]+p)+"").equals(ConstantesBD.acronimoNo))
								{
									tieneasoc=true;
									//elemento.put("seleccionado", ConstantesBD.acronimoSi);
									//logger.info("\n **** entre al if");
								}
						}
					}
					if (tieneasoc)
						elemento.put("seleccionado", ConstantesBD.acronimoSi);
					else
						elemento.put("seleccionado", ConstantesBD.acronimoNo);
				}
			}
			else
			{
				//logger.info("\n \n entre por el no ");
				for (int j=0;j<tiposAsocios.size();j++)
				{
					HashMap elemento = (HashMap)tiposAsocios.get(j);
					elemento.put("seleccionado", ConstantesBD.acronimoNo);
				}
			}	
			forma.setServicios(indicesServicios[22]+i, tiposAsocios);
			
			//logger.info("tipos asocio adic-->"+tiposAsocios);
		}
		
		
	}
	
	
	public static void initTipoHonorario (Connection connection , HojaQuirurgicaForm forma,UsuarioBasico usuario)
	{
		//se toma el numero de servicios que existen
		int numServicios  = Integer.parseInt(forma.getServicios("numRegistros")+"");
		//ahora se consultan los asocios por si cambiaron
		ArrayList tiposAsoc = new ArrayList ();
		tiposAsoc=Utilidades.obtenerAsocios(connection, "", ConstantesBD.codigoServicioHonorariosCirugia+"", ConstantesBD.acronimoSi);
		
		
		//logger.info("\n \ntipos asoc-->"+tiposAsoc);
		
		//ahora se procede a iterar los servicios
		
		for(int i=0;i<numServicios;i++)
		{
			ArrayList tiposAsocios = new ArrayList ();
			String [] indicesTipoAsocios = {"codigo", "codigo_asocio", "tipos_servicio", "nombre_asocio", "participa_cir", "centro_costo_ejecuta", "seleccionado"};
			tiposAsocios=Listado.copyArray(tiposAsoc,indicesTipoAsocios);
			
			int numProfecionales = Integer.parseInt(forma.getServicios(indicesServicios[10]+i+"_numRegistros")+"");
			//logger.info("\n *************** numRegistros profe del servicio "+i+" -->"+forma.getServicios(indicesServicios[10]+i+"_numRegistros"));
			if (numProfecionales>0)
			{
				for (int j=0;j<tiposAsocios.size();j++)
				{
					
					boolean tieneasoc = false;
					HashMap elemento = (HashMap)tiposAsocios.get(j);
					
					for (int p=0;p<numProfecionales;p++)
					{
						///se valida si es el asocio de ayudantia para dejar crear N
						if (!(forma.getServicios(indicesServicios[10]+i+"_"+indicesProfesionales[1]+p)+"").equals(
								ValoresPorDefecto.getAsocioAyudantia(usuario.getCodigoInstitucionInt())))
						{
							//ahora se recorren todos los asocios
						//	logger.info(elemento.get("codigo")+"=="+forma.getServicios(indicesServicios[10]+i+"_"+indicesProfesionales[12]+p));
								if ((elemento.get("codigo")+"").equals(forma.getServicios(indicesServicios[10]+i+"_"+indicesProfesionales[1]+p)+"")) 
									tieneasoc=true;
						}
						
					}
					if (tieneasoc)
						elemento.put("seleccionado", ConstantesBD.acronimoSi);
					else
						elemento.put("seleccionado", ConstantesBD.acronimoNo);
				}
			}
			else
			{
				for (int j=0;j<tiposAsocios.size();j++)
				{
					HashMap elemento = (HashMap)tiposAsocios.get(j);
					elemento.put("seleccionado", ConstantesBD.acronimoNo);
				}
			}
			
			forma.setServicios(indicesServicios[22]+i, tiposAsocios);
		}
		
	}
	
	
	
	
	/**
	 * Metodo encargado de filtrar los profesionales por la
	 * especialidad seleccionada.
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void filtarProfesionalesXEspecialidad (Connection connection,HojaQuirurgicaForm forma,UsuarioBasico usuario)
	{
		int indexSer=Integer.parseInt(forma.getIndexServicio());
		int indexProf = Integer.parseInt(forma.getIndexprofesional());
		//profesionales10_
		forma.setServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[10]+indexProf,UtilidadesAdministracion.obtenerProfesionales(connection, usuario.getCodigoInstitucionInt(), Integer.parseInt(forma.getServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[2]+indexProf)+""), true, true, ConstantesBD.codigoNuncaValido));
		
		
	}
	
	public static void limpiarDependenciasEspeInter (Connection connection, HojaQuirurgicaForm forma, UsuarioBasico usuario)
	{
		int indexSer=Integer.parseInt(forma.getIndexServicio());
		
		int numProfecionales = Integer.parseInt(forma.getServicios(indicesServicios[10]+indexSer+"_numRegistros")+"");
		
		String asocCX=ValoresPorDefecto.getAsocioCirujano(usuario.getCodigoInstitucionInt());
		/**
		 * se limpia si tiene el cirujano
		 */
		for (int i=0;i< numProfecionales;i++)
			if ((forma.getServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[12]+i)+"").equals(asocCX) ||
				(forma.getServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[1]+i)+"").equals(asocCX))
			{
				if (!(forma.getServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[13]+i)+"").equals(ConstantesBD.acronimoSi))
					eliminarProfecional(connection, forma, indexSer, i, usuario);
			}
			
	}
	
	
	/**
	 * Metodo encargado de filtrar las especialidades para
	 * cada profesional seleccionado.
	 * @param connection
	 * @param forma
	 */
	public static void filtarEspecialidadXProfesional (Connection connection,HojaQuirurgicaForm forma)
	{
		int indexSer=Integer.parseInt(forma.getIndexServicio());
		int indexProf = Integer.parseInt(forma.getIndexprofesional());
		
		//profecionales10_0_especialidades11_0
		//especialidades11_
		forma.setServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[11]+indexProf,Utilidades.obtenerEspecialidadesEnArray(connection, Integer.parseInt(forma.getServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[3]+indexProf)+""), ConstantesBD.codigoNuncaValido));
	}
	
	/**
	 * Metodo encargado de filtrar las especialidades por profesional.
	 * @param connection
	 * @param forma
	 */
	public static void filtrarEspecialidadXOtrosProfesionales (Connection connection,HojaQuirurgicaForm forma)
	{
		
		int indexProf = Integer.parseInt(forma.getIndexprofesional());
		
		forma.setOtrosProf(indicesOtrosProfesionales[4]+indexProf, Utilidades.obtenerEspecialidadesEnArray(connection, Integer.parseInt(forma.getOtrosProf(indicesOtrosProfesionales[0]+indexProf)+""), ConstantesBD.codigoNuncaValido));
			
	}
	
	/**
	 * Metodo encargado de inicializar el array list de
	 * especialidades 
	 * @param connection
	 * @param forma
	 */
	public static void  initSubSecProf (Connection connection, HojaQuirurgicaForm forma,UsuarioBasico usuario)
	{
		logger.info("\n **** entre a cargar la especialidad ****");
		int numReg = Integer.parseInt(forma.getServicios("numRegistros")+"");
		
		for (int i=0;i<numReg;i++)
		{
			//logger.info("\n **** numero de profesionales 1 ****");
			int numProf = Integer.parseInt(forma.getServicios(indicesServicios[10]+i+"_numRegistros")+"");
			logger.info("\n **** numero de profesionales  numRegistros  **** > "+numProf );
			forma.setServicios(indicesServicios[10]+i+"_"+indicesProfesionales[17],numProf);
			int totProf = Integer.parseInt(forma.getServicios(indicesServicios[10]+i+"_"+indicesProfesionales[17])+"");
			logger.info("\n **** total de profesionales  ****> "+totProf);
			for (int p=0;p<numProf;p++)
			{
				//logger.info("\n **** 2  ****> ");
				forma.setServicios(indicesServicios[10]+i+"_"+indicesProfesionales[11]+p,Utilidades.obtenerEspecialidadesEnArray(connection, Integer.parseInt(forma.getServicios(indicesServicios[10]+i+"_"+indicesProfesionales[3]+p)+""), ConstantesBD.codigoNuncaValido));
				forma.setServicios(indicesServicios[10]+i+"_"+indicesProfesionales[10]+p,UtilidadesAdministracion.obtenerProfesionales(connection, usuario.getCodigoInstitucionInt(), Integer.parseInt(forma.getServicios(indicesServicios[10]+i+"_"+indicesProfesionales[2]+p)+""), true, true, ConstantesBD.codigoNuncaValido));
			}
		}
		
		
	}
	

	
	
	public static void eliminarProfecional (Connection connection, HojaQuirurgicaForm forma,int indexSer,int indexProf, UsuarioBasico usuario)
	{
		forma.setServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[13]+indexProf, ConstantesBD.acronimoSi);
		int totprof = Utilidades.convertirAEntero(forma.getServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[17])+"");
		forma.setServicios(indicesServicios[10]+indexSer+"_"+indicesProfesionales[17], totprof-1);
		
		cargarTipoHonorario(connection, forma, usuario);
			
	}
	
	
	/**
	 * Metodo encargado de eliminar el campo servicio
	 * @param forma
	 */
	public static void eliminarServicio (HojaQuirurgicaForm forma)
	{
		logger.info("\n entre a eliminarServicio "+forma.getServicios("servicosInsertados"));
			
		forma.setServicios(indicesServicios[25]+forma.getIndexServicio(), ConstantesBD.acronimoEliminar);
		
		int cantServ=Integer.parseInt(forma.getServicios(indicesServicios[26])+"");
		forma.setServicios(indicesServicios[26], (cantServ-1));
		int sec=1;
		int numReg = Integer.parseInt(forma.getServicios("numRegistros")+"");
		forma.setServicios("servicosInsertados", "");
		for (int i=0;i<numReg;i++)
		{
			if (!(forma.getServicios(indicesServicios[25]+i)+"").equals(ConstantesBD.acronimoEliminar))
			{
				
				if (sec<=cantServ)
				{
					forma.setServicios(indicesServicios[4]+i, sec);
					sec++;
				}
				
				//		se indican los servicios ya ingresados
				if (!UtilidadCadena.noEsVacio(forma.getServicios("servicosInsertados")+""))
					forma.setServicios("servicosInsertados",forma.getServicios(indicesServicios[0]+i)+",");
				else
					forma.setServicios("servicosInsertados",forma.getServicios("servicosInsertados")+""+forma.getServicios(indicesServicios[0]+i)+",");
			}
		}
		forma.setNumeroDeRegistros((forma.getServicios("servicosInsertados").toString().split(",").length));
		logger.info("\n sali a eliminarServicio "+forma.getServicios("servicosInsertados"));
	}
	
	
	/**
	 * Metodo utilizado para obtener los parametros generales
	 * que rigen la aplicacion
	 * @param usuario
	 * @param forma
	 */
	public static void consultParamGenerales (Connection connection,UsuarioBasico usuario, HojaQuirurgicaForm forma, PersonaBasica paciente)
	{	//permitir indicativo de no cobrable en hoja quirurgica cirugias.
		forma.setParamGenerSecServicos(indicesParamGenerales[0], ValoresPorDefecto.getIndicativoCobrableHonorariosCirugia(usuario.getCodigoInstitucionInt()));
		//permitir indicativo de no cobrable en hoja quirurgica no cruentos.
		forma.setParamGenerSecServicos(indicesParamGenerales[1], ValoresPorDefecto.getIndicativoCobrableHonorariosNoCruento(usuario.getCodigoInstitucionInt()));
		//permitir modificar informacion en descripcion qx y diagnosticos hastes de finalizar
		forma.setParamGenerSecServicos(indicesParamGenerales[2], ValoresPorDefecto.getModificarInformacionDescripcionQuirurgica(usuario.getCodigoInstitucionInt()));
		//permitir modificar informacion general en hoja quirurgica antes de finalizar
		forma.setParamGenerSecServicos(indicesParamGenerales[12], ValoresPorDefecto.getModificarInformacionQuirurgica(usuario.getCodigoInstitucionInt()));
		//se pregunta si la hoja quirurgica existe 
		forma.setParamGenerSecServicos(indicesParamGenerales[15], existeHojaQx(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"")?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		//la Hoja Qx esta Finalizada
		if((forma.getParamGenerSecServicos(indicesParamGenerales[15])+"").equals(ConstantesBD.acronimoSi))
			forma.setParamGenerSecServicos(indicesParamGenerales[7], estaFinalizadaHojaQx(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""));
		else
			forma.setParamGenerSecServicos(indicesParamGenerales[7],ConstantesBD.acronimoNo);
	
		//hacer requerida en hoja quirurgica la descripcion por especialidad para Cirugias
		forma.setParamGenerSecServicos(indicesParamGenerales[8], ValoresPorDefecto.getRequeridaDescripcionEspecialidadCirugias(usuario.getCodigoInstitucionInt()));
		//hacer requerida en hoja quirurgica la descripcion por especialidad para No Cruentos
		forma.setParamGenerSecServicos(indicesParamGenerales[9], ValoresPorDefecto.getRequeridaDescripcionEspecialidadNoCruentos(usuario.getCodigoInstitucionInt()));
		
		//indicador del asocio del cirujano
		forma.setParamGenerSecServicos(indicesParamGenerales[10],ValoresPorDefecto.getAsocioCirujano(usuario.getCodigoInstitucionInt()));  
		//indicador de si existe hoja de anestesia.
		forma.setParamGenerSecServicos(indicesParamGenerales[11],existeHojaAnestesia(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""));
		//minutos limitepara registro de notas aclaratorias en hoja quirurgica de no cruentos despues de finalizada.
		forma.setParamGenerSecServicos(indicesParamGenerales[13], ValoresPorDefecto.getMinutosRegistroNotasNoCruentos(usuario.getCodigoInstitucionInt()));
		//minutos limitepara registro de notas aclaratorias en hoja quirurgica de cirugia despues de finalizada.
		forma.setParamGenerSecServicos(indicesParamGenerales[14], ValoresPorDefecto.getMinutosRegistroNotasCirugia(usuario.getCodigoInstitucionInt()));
		//se verifica si la H Anes esta Finalizada
		HojaAnestesia mundoAnes = new HojaAnestesia();
		forma.setParamGenerSecServicos(indicesParamGenerales[16],mundoAnes.esFinalizadaHojaAnestesia(connection,Integer.parseInt(forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""))?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		//se verifica si el paciente esta muerto.
		forma.setParamGenerSecServicos(indicesParamGenerales[17],UtilidadValidacion.esPacienteMuerto(connection, paciente.getCodigoPersona())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		//se verifica si el parametro postular fechas en respuesta porocedimientos no cruentos
		forma.setParamGenerSecServicos(indicesParamGenerales[18], ValoresPorDefecto.getPostularFechasEnRespuestaDyT(usuario.getCodigoInstitucionInt()));
		//se verifica si la la solicitud ya fue liquidada.
		 if (UtilidadesFacturacion.esSolicitudTotalPendiente(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""))
			 forma.setParamGenerSecServicos(indicesParamGenerales[19], ConstantesBD.acronimoNo);
		 else
			 forma.setParamGenerSecServicos(indicesParamGenerales[19], ConstantesBD.acronimoSi);
		
		
}
	
	
	
	
	

	public static void guardarSecServicios (Connection connection,HttpServletRequest request, HojaQuirurgicaForm forma,UsuarioBasico usuario,PersonaBasica paciente, boolean validacionCapitacion)  throws IPSException
	{
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		//se inserta l ainformacion basica de la hoja qx
		if((forma.getParamGenerSecServicos(indicesParamGenerales[15])+"").equals(ConstantesBD.acronimoNo))
			transacction=insertarHojaQxBasica(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"", usuario, validacionCapitacion);
		
		transacction=guardarServicios(connection, request,forma, usuario, paciente);
		HashMap datos = new HashMap();
		datos.put("solicitud", forma.getSolicitudMap(indicesSolicitud[8]+"_0"));
		if (transacction)
			transacction=ActualizarEspecialidadesAndCirujanos(connection, datos, usuario);
		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(connection);			
			logger.info("\n----->TERMINO SECCION SERVICIOS AL 100% ");
			forma.setOperacionTrue(true);			
		}
		else
		{
			UtilidadBD.abortarTransaccion(connection);
			logger.info("----->	ABORTO TRANSACCION SECCION SERVICIOS");
		}	
		
		cargarSecServicios(connection, forma, usuario,paciente,false);
	}

	
	
	/**
	 * Metodo encargado de verificar que operacion
	 * se debe de hacer a cada servicio.
	 * @param servicios
	 * @param serviciosOld
	 * @return
	 */
	public static HashMap consultarOperacionesServ(HashMap servicios, HashMap serviciosOld)
	{
		logger.info("\n entre a consultarOperacionesServ ");
		int numReg = Integer.parseInt(servicios.get("numRegistros")+"");
		
		//int numRegOld = Integer.parseInt(serviciosOld.get("numRegistros")+"");
		//servicios old
		//for (int o=0;o<numRegOld;o++)
		//{
			
			//servicios actuales
			for (int i=0;i<numReg;i++)
			{
				//estabd
				if ((servicios.get(indicesServicios[8]+i)+"").equals(ConstantesBD.acronimoSi))
				{
					//HashMap diagnosticos=consultOperaDiagSecServicios(Listado.obtenerMapaInterno(servicios, indicesServicios[11]+i+"_"), Listado.obtenerMapaInterno(serviciosOld, indicesServicios[11]+i+"_"));
					
					//se evalua si es diferente de eliminar pues estos estan listos para ser eliminados
					if (!(servicios.get(indicesServicios[25]+i)+"").equals(ConstantesBD.acronimoEliminar))
					{
						String [] indiceConsult= {indicesServicios[4],indicesServicios[13],indicesServicios[16],indicesServicios[17],indicesServicios[18],indicesServicios[19],indicesServicios[24]}; 
						for (int k=0;k<indiceConsult.length;k++)
						{
						
							if (!(servicios.get(indiceConsult[k]+i)+"").equals(serviciosOld.get(indiceConsult[k]+i)+""))
							{
								servicios.put(indicesServicios[25]+i, ConstantesBD.acronimoModificar);
								logger.info("\n entre a colocar el indicativo de modificar -- "+servicios.get(indiceConsult[k]+i)+" == "+serviciosOld.get(indiceConsult[k]+i));
							}							
							
						}
						
					}
					
				}
				else
					if ((servicios.get(indicesServicios[8]+i)+"").equals(ConstantesBD.acronimoNo))
					{
						if (!(servicios.get(indicesServicios[25]+i)+"").equals(ConstantesBD.acronimoInsertar))
							servicios.put(indicesServicios[25]+i, ConstantesBD.acronimoNada);
					}
					else
						logger.info("\n problema en estaBd de consultarOperacionesServ");
					
				
			}
			
		//}
		//logger.info("servicios --> "+servicios);
		return servicios;
		
	}
	
	
	

	
	/**
	 * Metodo encargado de guardar los servicios
	 * @param connection
	 * @param request 
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @return
	 * @throws IPSException
	 */
	public static boolean guardarServicios (Connection connection, HttpServletRequest request, HojaQuirurgicaForm forma,UsuarioBasico usuario, PersonaBasica paciente) throws IPSException
	{
		logger.info("\n ENTRO A GUARDARSERVICIOS ");
		@SuppressWarnings("unused")
		HashMap servicios = new HashMap ();
		HashMap diagnosticos = new HashMap ();
		HashMap profesionales = new HashMap ();
		boolean operacionTrue=true;
		convertirFormatoABDDiagnosticosPostOpe(forma.getServicios());
		servicios=consultarOperacionesServ(forma.getServicios(), forma.getServiciosOld());
		//numServicios
		int numReg = Integer.parseInt(forma.getServicios("numRegistros")+"");
		Integer consecutivoPartoXcodigoSolCxServ = null;
		
		for(int i=0;i<numReg;i++)
		{
			diagnosticos=consultOperaDiagSecServicios(Listado.obtenerMapaInterno(forma.getServicios(), indicesServicios[11]+i+"_"), Listado.obtenerMapaInterno(forma.getServiciosOld(), indicesServicios[11]+i+"_"));
			
			profesionales=consultaOperacionesProfesionales( Listado.obtenerMapaInterno(forma.getServicios(), indicesServicios[10]+i+"_"), Listado.obtenerMapaInterno(forma.getServiciosOld(), indicesServicios[10]+i+"_"));
			
			if (operacionTrue && (forma.getServicios(indicesServicios[25]+i)+"").equals(ConstantesBD.acronimoEliminar))
			{
				//eliminar
				logger.info("\n ENTRO A ELIMINAR "+i);
				//se eliminan primero las descripciones Qx
				if(operacionTrue)
					if (!(forma.getServicios(indicesServicios[23]+i)+"").equals(""))
						operacionTrue=eliminarDecQx(connection, forma.getServicios(indicesServicios[9]+i)+"");
				//se eliminan los diagnosticos postOperatorios
				if (operacionTrue)
				{
					
					if (Integer.parseInt(forma.getServicios(indicesServicios[11]+i+"_numRegistros")+"")>0)
						if (!(forma.getServicios(indicesServicios[11]+i+"_"+indicesDiagPostopera[1]+"0")+"").equals("null") || !(forma.getServicios(indicesServicios[11]+i+"_"+indicesDiagPostopera[1]+"1")+"").equals("null"))
							operacionTrue=eliminarDiagPostOpe(connection, forma.getServicios(indicesServicios[9]+i)+"","");
				}
				//se eliminan los profecionales
				if (operacionTrue)
					if (Integer.parseInt(forma.getServicios(indicesServicios[10]+i+"_numRegistros")+"")>0)
						operacionTrue= eliminarProfesionalesCx(connection, forma.getServicios(indicesServicios[9]+i)+"", ConstantesBD.codigoNuncaValido+"");
				//se eliminan los servicios
				if (operacionTrue)
				{
					//************************************************************************************************
					//Se elimina la informacion de Formularios de Respuesta Asociado al servicio
					if(forma.getServicios(indicesServicios[36]+i).toString().equals(ConstantesBD.acronimoSi))
					{
						logger.info("entra a eliminar formularios de respuesta asociados ");
						HashMap respuesta =  UtilidadesOrdenesMedicas.obtenerSolCirugiaServicio(
								connection,
								forma.getSolicitudMap(indicesSolicitud[8]+"_0").toString(),
								forma.getServicios(indicesServicios[0]+i)+"");
						
						if(!respuesta.get("numRegistros").toString().equals("0"))
						{
							RespuestaProcedimientos.eliminarRespuestaProcedimientos(
									connection,
									Utilidades.convertirAEntero(forma.getSolicitudMap(indicesSolicitud[8]+"_0").toString()),
									Utilidades.convertirAEntero(respuesta.get("codigo_0").toString()));
						}
					}
					//SI hay informacion de parto asociada al servicio, se obtiene el consecutivo para asociarla al nuevo servicio
					if(forma.getServicios(indicesServicios[9]+i)!=null && forma.getServicios(indicesServicios[38]+i).equals(ConstantesBD.acronimoNo)){
						consecutivoPartoXcodigoSolCxServ = SqlBaseUtilidadesManejoPacienteDao.obtenerConsecutivoPartoXcodigoSolCxServ(connection, forma.getServicios(indicesServicios[9]+i).toString());
						eliminarInformacionParto(connection, Utilidades.convertirAEntero(forma.getServicios(indicesServicios[9]+i).toString()));
					}					
					operacionTrue = eliminarServicioQx(connection, forma.getServicios(indicesServicios[9]+i)+"");
					//************************************************************************************************
				}				
			}
			else
				if (operacionTrue && (forma.getServicios(indicesServicios[25]+i)+"").equals(ConstantesBD.acronimoModificar))
				{
					//modificar
					logger.info("\n ENTRO A MODIFICAR "+i);
					operacionTrue=modificarServicio(connection, forma, i);
					//se inserta la descripcion qx
					if(operacionTrue)
						operacionTrue=insertNewDescInDesc(connection, forma, i, usuario);

					
					
					// Insertar Justificaciones No POS
					FormatoJustServNopos fjsn = new FormatoJustServNopos();
					// SI EL INDICADOR DE JUSTIFICAR ES VERDADERO O PENDIENTE SE HACE EL INGRESO DE LA JUSTIFICACIï¿½N NO POS
	                if (forma.getServicios(indicesServicios[31]+i).toString().equals("pendiente")){
	                	
	                	double subcuenta = Cargos.obtenerCodigoSubcuentaDetalleCargo(connection, Integer.parseInt(forma.getServicios(indicesServicios[0]+i).toString()), Integer.parseInt(forma.getServicios(indicesServicios[29]+i).toString()), ConstantesBD.codigoNuncaValido, false);
	                	
	    				UtilidadJustificacionPendienteArtServ.insertarJusNP(connection, Integer.parseInt(forma.getServicios(indicesServicios[29]+i).toString()), Integer.parseInt(forma.getServicios(indicesServicios[0]+i).toString()), 1, usuario.getLoginUsuario(), false, false, Utilidades.convertirAEntero(subcuenta+""),"");
	    			}
	                if (forma.getServicios(indicesServicios[31]+i).toString().equals("true")){
	                	logger.info("\n\n ###############################################>>>>> "+forma.getSolicitudMap(indicesSolicitud[8]+"_0"));
	                	
	                	HashMap justificacion=(HashMap) request.getSession().getAttribute(forma.getServicios().get(indicesServicios[0]+i)+"MAPAJUSSERV");
	                	
	                	fjsn.ingresarJustificacion(
	                		connection,
	                		usuario.getCodigoInstitucionInt(), 
	                		usuario.getLoginUsuario(), 
	                		justificacion, 
	                		Integer.parseInt(forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""),//numero de solicitud
	                		ConstantesBD.codigoNuncaValido,
	                		Integer.parseInt(forma.getServicios().get(indicesServicios[0]+i).toString()),
	                		usuario.getCodigoPersona());
	                }		
	                //Asociar la informacion del parto (se elimino del servicio anterior) al nuevo servicio
	                if(SqlBaseHojaQuirurgicaDao.codigoServicio != null && consecutivoPartoXcodigoSolCxServ != null){
	                	migrarInformacionParto(connection, SqlBaseHojaQuirurgicaDao.codigoServicio, consecutivoPartoXcodigoSolCxServ);
	                	SqlBaseHojaQuirurgicaDao.codigoServicio = null;
	                	consecutivoPartoXcodigoSolCxServ = null;
	                }					
						
				}
				else						
						if (operacionTrue && (forma.getServicios(indicesServicios[25]+i)+"").equals(ConstantesBD.acronimoInsertar))
							{
								//insertar
								logger.info("\n ENTRO A INSERTAR");
								
								//hermorhu - MT5642
								//validacion Cobertura para el servicio ingresado -437-
								int codigoViaIngreso = Cuenta.obtenerCodigoViaIngresoCuenta(connection, paciente.getCodigoCuenta()+"");
								
								InfoResponsableCobertura infoResponsableCobertura = 
				            			Cobertura.validacionCoberturaServicio(connection, paciente.getCodigoIngreso()+"", codigoViaIngreso, paciente.getCodigoTipoPaciente(), Integer.parseInt(forma.getServicios(indicesServicios[0]+i)+""), usuario.getCodigoInstitucionInt(), Utilidades.esSolicitudPYP(connection,Utilidades.convertirAEntero(forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"")), "" /*subCuentaCoberturaOPCIONAL*/);
				            
				    			if(infoResponsableCobertura.getInfoCobertura().incluido() && infoResponsableCobertura.getInfoCobertura().existe()) {
				    				forma.getServicios().put("cubierto_"+i, ConstantesBD.acronimoSi);
				    	  		} else {
				    	  			forma.getServicios().put("cubierto_"+i, ConstantesBD.acronimoNo);
				    	  		}
								
				    			forma.getServicios().put("contrato_convenio_"+i, infoResponsableCobertura.getDtoSubCuenta().getContrato());
								//
				    			
								operacionTrue=insertarServicio(connection, forma, i, usuario);
								//se inserta la descripcion qx
								if (operacionTrue)
									operacionTrue=insertNewDescInNewDesc(connection, forma, i, usuario);
								//se insertan los diagnosticos 
								if (operacionTrue)
									operacionTrue=insertNuevoDiagPostOpera(connection, diagnosticos,usuario);
								//se insertan los profecionales
								if (operacionTrue)
									operacionTrue=insertarProfecionales(connection, profesionales, usuario);
									
								
								// Insertar Justificaciones No POS
								FormatoJustServNopos fjsn = new FormatoJustServNopos();
								// SI EL INDICADOR DE JUSTIFICAR ES VERDADERO O PENDIENTE SE HACE EL INGRESO DE LA JUSTIFICACIï¿½N NO POS
				                if (forma.getServicios(indicesServicios[31]+i).toString().equals("pendiente")){
				                	
				                	double subcuenta = Cargos.obtenerCodigoSubcuentaDetalleCargo(connection, Integer.parseInt(forma.getServicios(indicesServicios[0]+i).toString()), Integer.parseInt(forma.getServicios(indicesServicios[29]+i).toString()), ConstantesBD.codigoNuncaValido, false);
				                	
				    				UtilidadJustificacionPendienteArtServ.insertarJusNP(connection, Integer.parseInt(forma.getServicios(indicesServicios[29]+i).toString()), Integer.parseInt(forma.getServicios(indicesServicios[0]+i).toString()), 1, usuario.getLoginUsuario(), false, false, Utilidades.convertirAEntero(subcuenta+""),"");
				    			}
				                if (forma.getServicios(indicesServicios[31]+i).toString().equals("true")){
				                	logger.info("\n\n ###############################################>>>>> "+forma.getSolicitudMap(indicesSolicitud[8]+"_0"));
				                	fjsn.ingresarJustificacion(
				                		connection,
				                		usuario.getCodigoInstitucionInt(), 
				                		usuario.getLoginUsuario(), 
				                		forma.getJustificacionesServicios(), 
				                		Integer.parseInt(forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""),//numero de solicitud
				                		ConstantesBD.codigoNuncaValido,
				                		i,
				                		usuario.getCodigoPersona());
				                }
				                //Asociar la informacion del parto (se elimino del servicio anterior) al nuevo servicio
				                if(SqlBaseHojaQuirurgicaDao.codigoServicio != null && consecutivoPartoXcodigoSolCxServ != null){
				                	migrarInformacionParto(connection, SqlBaseHojaQuirurgicaDao.codigoServicio, consecutivoPartoXcodigoSolCxServ);
				                	SqlBaseHojaQuirurgicaDao.codigoServicio = null;
				                	consecutivoPartoXcodigoSolCxServ = null;
				                }				                
							}
			
			
			if (operacionTrue && esModificadoDiagPostOpe(diagnosticos) && !(forma.getServicios(indicesServicios[25]+i)+"").equals(ConstantesBD.acronimoInsertar) && !(forma.getServicios(indicesServicios[25]+i)+"").equals(ConstantesBD.acronimoEliminar))
			{
				logger.info("\n ENTRO A MODIFICAR DIAG POST OPERA "+i);
				//numRegistros de los diagnosticos
				int numDiag = Integer.parseInt(diagnosticos.get("numRegistros")+"");
				
				for (int d=0;d<numDiag;d++)
				{
					if (operacionTrue && diagnosticos.containsKey("operacion_"+d) && (diagnosticos.get("operacion_"+d)+"").equals(ConstantesBD.acronimoEliminar))
					{
						operacionTrue=eliminarDiagPostOpe(connection, forma.getServicios(indicesServicios[9]+i)+"", diagnosticos.get(indicesDiagPostopera[0]+d)+"");
					}
					else
						if (operacionTrue && diagnosticos.containsKey("operacion_"+d) && (diagnosticos.get("operacion_"+d)+"").equals(ConstantesBD.acronimoModificar))
						{
							operacionTrue=modificarDiagPotOpera(connection, diagnosticos.get(indicesDiagPostopera[1]+d)+"", diagnosticos.get(indicesDiagPostopera[2]+d)+"", usuario.getLoginUsuario(), diagnosticos.get(indicesDiagPostopera[0]+d)+"");
							
						}
						else
							if (operacionTrue && diagnosticos.containsKey("operacion_"+d) && (diagnosticos.get("operacion_"+d)+"").equals(ConstantesBD.acronimoInsertar))
							{
								
								int result=insertarDiagnosticoPostOperatorio(connection, forma.getServicios(indicesServicios[9]+i)+"", diagnosticos.get(indicesDiagPostopera[1]+d)+"", Integer.parseInt(diagnosticos.get(indicesDiagPostopera[2]+d)+""),UtilidadTexto.getBoolean(diagnosticos.get(indicesDiagPostopera[3]+d)+""), UtilidadTexto.getBoolean(diagnosticos.get(indicesDiagPostopera[4]+d)+""),usuario.getLoginUsuario());
								if (result>0)
									operacionTrue=true;
								else
									operacionTrue=false;
							}
				}
				
			}
			
			
			if (operacionTrue && esModificadoProfesionales(profesionales) && !(forma.getServicios(indicesServicios[25]+i)+"").equals(ConstantesBD.acronimoInsertar) && !(forma.getServicios(indicesServicios[25]+i)+"").equals(ConstantesBD.acronimoEliminar))
			{
				logger.info("\n ENTRO A MODIFICA FROFESIONALES "+i+"profesionales -->"+profesionales);
				int numProf = Integer.parseInt(profesionales.get("numRegistros")+"");
				
				for (int p=0;p<numProf;p++)
				{
					if (operacionTrue && profesionales.containsKey(indicesProfesionales[14]+p) && (profesionales.get(indicesProfesionales[14]+p)+"").equals(ConstantesBD.acronimoEliminar))
					{
						operacionTrue=eliminarProfesionalesCx(connection, forma.getServicios(indicesServicios[9]+i)+"", profesionales.get(indicesProfesionales[0]+p)+"");
					}
					else
						if (operacionTrue && profesionales.containsKey(indicesProfesionales[14]+p) && (profesionales.get(indicesProfesionales[14]+p)+"").equals(ConstantesBD.acronimoModificar))
						{
							operacionTrue=modificarProfesionales(connection, usuario, forma.getServicios(indicesServicios[9]+i)+"", Integer.parseInt(profesionales.get(indicesProfesionales[1]+p)+""), Integer.parseInt(profesionales.get(indicesProfesionales[2]+p)+""), Integer.parseInt(profesionales.get(indicesProfesionales[3]+p)+""), profesionales.get(indicesProfesionales[4]+p)+"",profesionales.get(indicesProfesionales[0]+p)+"");
						}
						else
							if (operacionTrue && profesionales.containsKey(indicesProfesionales[14]+p) && (profesionales.get(indicesProfesionales[14]+p)+"").equals(ConstantesBD.acronimoInsertar))
							{
								operacionTrue=insertarProfesionales(connection, usuario, forma.getServicios(indicesServicios[9]+i)+"", Integer.parseInt(profesionales.get(indicesProfesionales[1]+p)+""), Integer.parseInt(profesionales.get(indicesProfesionales[2]+p)+""), Integer.parseInt(profesionales.get(indicesProfesionales[3]+p)+""), profesionales.get(indicesProfesionales[4]+p)+"");
							}
						
				}
				
				
			}
			
			
			
				
		}
		
		return operacionTrue;
		
	}
	
	public static Integer[] getConsecutivoInformacionPartoYeliminar(Integer solicitud) throws IPSException{
		Integer resp[] = new Integer[2];
		Connection connection = UtilidadBD.abrirConexion();
		try
		{
			String consulta =
					"SELECT "+ 
					  "informacion_parto.consecutivo, sol_cirugia_por_servicio.servicio "+ 
					"FROM  "+
					  "salascirugia.sol_cirugia_por_servicio, "+ 
					  "historiaclinica.informacion_parto "+
					"WHERE "+
					  "sol_cirugia_por_servicio.codigo = informacion_parto.cirugia AND "+
					  "sol_cirugia_por_servicio.numero_solicitud = ? ";
			PreparedStatement ps = connection.prepareStatement(consulta);
			ps.setInt(1, solicitud);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				resp[0] = rs.getInt("consecutivo");
				resp[1] = rs.getInt("servicio");
			}
			ps = connection.prepareStatement("UPDATE informacion_parto SET cirugia = null WHERE consecutivo = ?");
			ps.setInt(1, resp[0]);
			ps.executeUpdate();
		}
		catch (SQLException e) {
			throw new IPSException(e);
		}
		finally
		{
			try {
				UtilidadBD.cerrarConexion(connection);
			}
			catch (SQLException e){
				logger.error(e);
			}
		}
		return resp;
	}
	
	public static void eliminarInformacionParto(Connection connection, Integer idDestinoInfoParto) throws IPSException{
		try
		{
			PreparedStatement ps = connection.prepareStatement("UPDATE informacion_parto SET cirugia = null WHERE cirugia = ?");
			ps.setInt(1, idDestinoInfoParto);
			ps.executeUpdate();
		}
		catch (SQLException e) {
			throw new IPSException(e);
		}
	}
	
	public static void migrarInformacionParto(Connection connection, Integer idOrigenInfoParto, Integer idDestinoInfoParto) throws IPSException{
		try
		{
			PreparedStatement ps = connection.prepareStatement("UPDATE informacion_parto SET cirugia = ? WHERE consecutivo = ?");
			ps.setInt(1, idOrigenInfoParto);
			ps.setInt(2, idDestinoInfoParto);
			ps.executeUpdate();
		}
		catch (SQLException e) {
			throw new IPSException(e);
		}
	}	

	public static boolean insertarServicio (Connection connection, HojaQuirurgicaForm forma,int index,UsuarioBasico usuario)
	{
		boolean operacionTrue=true;
		
		HashMap datos = new HashMap();
		
		//numeroSolicitud
		datos.put("numSolicitud", forma.getSolicitudMap(indicesSolicitud[8]+"_0"));
		//servicio
		datos.put("servicio", forma.getServicios(indicesServicios[0]+index));
		//consecutivo
		datos.put("consecutivo", forma.getServicios(indicesServicios[4]+index));
		//institucion
		datos.put("institucion", usuario.getCodigoInstitucion());
		//indBilateral
		datos.put("indBilateral", forma.getServicios(indicesServicios[17]+index));
		//indViaAcceso
		datos.put("indViaAcceso", forma.getServicios(indicesServicios[18]+index));
		//especialidad
		datos.put("especialidad", forma.getServicios(indicesServicios[19]+index));
		//finalidad
		datos.put("finalidad", forma.getServicios(indicesServicios[13]+index));
		//viaCx
		datos.put("viaCx", forma.getServicios(indicesServicios[16]+index));
		
		//hermurhu - MT5642
		//cubierto
		datos.put("cubierto", forma.getServicios("cubierto_"+index));
		//contrato/convenio
		datos.put("contratoConvenio", forma.getServicios("contrato_convenio_"+index));
		
		operacionTrue=insertarServiciosQx(connection, datos);

		return operacionTrue;
	}
	
	
	public static boolean modificarServicio (Connection connection,HojaQuirurgicaForm forma, int index )
	{
		boolean operacionTrue = true;
		HashMap datos = new HashMap ();
		
		datos.put("consecutivo", forma.getServicios(indicesServicios[4]+index));
		datos.put("indBilateral", forma.getServicios(indicesServicios[17]+index));
		datos.put("indViaAcceso", forma.getServicios(indicesServicios[18]+index));
		datos.put("especialidad", forma.getServicios(indicesServicios[19]+index));
		datos.put("codigo", forma.getServicios(indicesServicios[9]+index));
		datos.put("finalidad", forma.getServicios(indicesServicios[13]+index));
		datos.put("viaCx", forma.getServicios(indicesServicios[16]+index));
		
		
		operacionTrue=ActualizarServicioQx(connection, datos);
		return operacionTrue;
	}
	
	
	/**
	 * Metodo encargado de verificar cuales son las nuevas especialidades
	 * @param connection
	 * @param forma
	 * @return
	 */
	public static HashMap verificaNewEsp (Connection connection,HojaQuirurgicaForm forma)
	{
		HashMap esp = new HashMap ();
		String [] indicesEspecialidadesInter = HojaAnestesia.indicesEspecialidadesInter;
		//se evalia la especialidad
		HojaAnestesia mundoAnestesia = new HojaAnestesia();
		HashMap especialidades = new HashMap ();
		//se consultan las especialidades existentes en la tabla esp_intervienen_sol_cx
		//se valida si la especialidad que interviene esta vacia
		int can=0;
		
			especialidades=mundoAnestesia.consultarEspecialidadesIntervienen(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"","");
			//se recorren servicios
			int numEspecialidades = Integer.parseInt(especialidades.get("numRegistros")+"");
			
			for (int i=0;i<Integer.parseInt(forma.getServicios("numRegistros")+"");i++)
			{
				//se recorren las especialidades
				if (numEspecialidades>0)
				{
					for (int e=0;e<Integer.parseInt(especialidades.get("numRegistros")+"");e++)
					{
						if (!especialidades.get(indicesEspecialidadesInter[1]+e).equals(forma.getServicios(indicesServicios[19]+i)))
						{
							esp.put(indicesServicios[19]+can, forma.getServicios(indicesServicios[19]+i));
							can++;
						}
					}
				}
				else
					{
						esp.put(indicesServicios[19]+can, forma.getServicios(indicesServicios[19]+i));
						can++;
					}
			}
			esp.put("numRegistros", can);
			return esp;
	}
	
	/**
	 * inserta una descripcion en un servicio nuevo
	 * @param connection
	 * @param forma
	 * @return
	 */
	public static boolean insertNewDescInNewDesc (Connection connection, HojaQuirurgicaForm forma, int index,UsuarioBasico usuario)
	{
		
		int result=1;
		int codigoServicio=ConstantesBD.codigoNuncaValido;
		//se toma el numero de la secuencia
		try
		{
			codigoServicio=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(connection, "seq_sol_cx_ser");
		}
		catch (Exception e) 
		{
			logger.info("\n problema consultando el numero del consecutivo de la tabla sol_cirugia_por_servicio");
		}
		
		//se inserta la descripcion
		if (!(forma.getServicios(indicesServicios[24]+index)+"").equals("") && codigoServicio>ConstantesBD.codigoNuncaValido)
			result=insertarDescripcionQx(connection, codigoServicio+"", forma.getServicios(indicesServicios[24]+index)+"", usuario.getLoginUsuario());
		
		if(result>0)
			return true;
		else
			return false;
	}
	
	
	/**
	 * Metodo en cargado de insertar los diagnosticos post peratorios
	 * @param connection
	 * @param forma
	 * @param index
	 * @param usuario
	 * @param codigoServicio
	 * @return
	 */
	public static boolean insertNuevoDiagPostOpera (Connection connection, HashMap servicios,UsuarioBasico usuario)
	{
		logger.info("\n entro a insertNuevoDiagPostOpera");
		int result=1;
		int codigoServicio=0;
		{
			//se toma el numero de la secuencia
			try
			{
				codigoServicio=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(connection, "seq_sol_cx_ser");
			}
			catch (Exception e) 
			{
				logger.info("\n problema consultando el numero del consecutivo de la tabla sol_cirugia_por_servicio");
			}
		}
		//se inserta la descripcion
	
		if (codigoServicio>ConstantesBD.codigoNuncaValido)
		{
			logger.info("\n  ******* 1 **********");
			for (int i=0;i<Integer.parseInt(servicios.get("numRegistros")+"");i++)
			{
				logger.info("\n  ******* 2 **********");
				if ( result>0 && servicios.containsKey("operacion_"+i) && (servicios.get("operacion_"+i)+"").equals(ConstantesBD.acronimoInsertar))
					result=insertarDiagnosticoPostOperatorio(connection, codigoServicio+"", servicios.get(indicesDiagPostopera[1]+i)+"", Integer.parseInt(servicios.get(indicesDiagPostopera[2]+i)+""), UtilidadTexto.getBoolean(servicios.get(indicesDiagPostopera[3]+i)+""), UtilidadTexto.getBoolean(servicios.get(indicesDiagPostopera[4]+i)+""),usuario.getLoginUsuario());
						
			}
		}
		logger.info("\n sali de insertNuevoDiagPostOpera -->"+result);
		if(result>0)
			return true;
		else
			return false;
	}
	
	
	
	
	/**
	 * inserta una descripcion en un servicio nuevo
	 * @param connection
	 * @param forma
	 * @return
	 */
	public static boolean insertNewDescInDesc (Connection connection, HojaQuirurgicaForm forma, int index,UsuarioBasico usuario)
	{
		
		int result=1;
		//se inserta la descripcion
		if (!(forma.getServicios(indicesServicios[24]+index)+"").equals(""))
			result=insertarDescripcionQx(connection, forma.getServicios(indicesServicios[9]+index)+"", forma.getServicios(indicesServicios[24]+index)+"", usuario.getLoginUsuario());
		
		if(result>0)
			return true;
		else
			return false;
	}
	
	/**
	 * Metodo encargado de consultar las operaciones a realizar 
	 * en los diagnosticos postOperatorios
	 * @param servicios
	 * @param serviciosOld
	 * @param index
	 * @return
	 */
	public static HashMap consultOperaDiagSecServicios (HashMap servicios, HashMap serviciosOld)
	{
	
		logger.info("\n ##########################################################################");
	//	logger.info("\n ENTRE A CONSULTAR OPERACIONES DIAGNOSTICOS SERVICIOS \n servicios --> "+servicios+" \n serviciosOld --> "+serviciosOld);
		//logger.info("listado en i -->"+index+" -- "+Listado.obtenerMapaInterno(servicios, indicesServicios[11]+index+"_"));
		
		
		//se evalua la principal aparte que se puede insertar y modificar
		if (servicios.containsKey(indicesDiagPostopera[6]+"0") && (servicios.get(indicesDiagPostopera[6]+"0")+"").equals(ConstantesBD.acronimoNo))
			servicios.put("operacion_0", "I");
		else
			if (servicios.containsKey(indicesDiagPostopera[6]+"0") && (servicios.get(indicesDiagPostopera[6]+"0")+"").equals(ConstantesBD.acronimoSi))
			{
				
					//se pregunta cual es el principal para compararlo
					if (!(servicios.get(indicesDiagPostopera[3])+"").equals(serviciosOld.get(indicesDiagPostopera[3])+""))
							servicios.put("operacion_0", "M");
						else
							servicios.put("operacion_0", "N");
				
			}
		
		
		//se evalua la principal aparte que se puede insertar y modificar
		if (servicios.containsKey(indicesDiagPostopera[6]+"1") && (servicios.get(indicesDiagPostopera[6]+"1")+"").equals(ConstantesBD.acronimoNo))
			servicios.put("operacion_1", "I");
		else
			if (servicios.containsKey(indicesDiagPostopera[6]+"1") && (servicios.get(indicesDiagPostopera[6]+"1")+"").equals(ConstantesBD.acronimoSi))
			{
				
					//se pregunta cual es el de complicacion para compararlo
					if (!(servicios.get(indicesDiagPostopera[4])+"").equals(serviciosOld.get(indicesDiagPostopera[4])+""))
							servicios.put("operacion_1", "M");
						else
							servicios.put("operacion_1", "N");
				
			}
		
		
		
		for (int i=2;i<Integer.parseInt(servicios.get("numRegistros")+"");i++)
		{
			if (servicios.containsKey(indicesDiagPostopera[6]+i) && (servicios.get(indicesDiagPostopera[6]+i)+"").equals(ConstantesBD.acronimoSi))
			{
				if (servicios.containsKey("eliminar_"+i) && (servicios.get("eliminar_"+i)+"").equals(ConstantesBD.acronimoSi))
					servicios.put("operacion_"+i, "E");
				else
					servicios.put("operacion_"+i, "N");
		
			}
			else
				if (servicios.containsKey("eliminar_"+i) && (servicios.get("eliminar_"+i)+"").equals(ConstantesBD.acronimoNo))
					servicios.put("operacion_"+i, "I");
				else
					servicios.put("operacion_"+i, "N");
		}
		
		//logger.info("\n al salir de consult operaciones "+servicios);
		
		return servicios;
	}
	
	
	/**
	 * Metodo encargado de identificar si existe modificacion o no 
	 * de los diadnosticos.
	 * @param diagnosticos
	 * @return
	 */
	public static boolean esModificadoDiagPostOpe (HashMap diagnosticos)
	{
		int numReg = Integer.parseInt(diagnosticos.get("numRegistros")+"");
		
		for(int i=0;i<numReg;i++)
		{
			if (diagnosticos.containsKey("operacion_"+i) && !(diagnosticos.get("operacion_"+i)+"").equals(ConstantesBD.acronimoNada))
				return true;
		}
		
		return false;
		
	}
	
	/**
	 * Metodo encargado de Modificar los
	 * datos de diagnostico postoperatorio
	 * @param connection
	 * @param diagnostico
	 * @param tipoCie
	 * @param usuarioModifica
	 * @param codigo
	 * @return
	 */
	public static boolean modificarDiagPotOpera (Connection connection, String diagnostico,String tipoCie,String usuarioModifica,String codigo)
	{
		
		HashMap datos = new HashMap ();
		datos.put("diagnostico", diagnostico);
		datos.put("tipoCie", tipoCie);
		datos.put("usuarioModifica", usuarioModifica);
		datos.put("codigo", codigo);
		
		return modificarDiagPostOpera(connection, datos);
	}
	
	
	
	public static boolean insertarProfecionales (Connection connection, HashMap profesionales,UsuarioBasico usuario)
	{
		
		logger.info("\n entre a insertarProfecionales --> "+profesionales);
			int result=1;
			int codigoServicio=0;
			{
				//se toma el numero de la secuencia
				try
				{
					codigoServicio=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(connection, "seq_sol_cx_ser");
				}
				catch (Exception e) 
				{
					logger.info("\n problema consultando el numero del consecutivo de la tabla sol_cirugia_por_servicio");
				}
			}
			
		
			
		
	
			for (int i=0;i<Integer.parseInt(profesionales.get("numRegistros")+"");i++)
			{
				DtoProfesionalesCirugia profesional = new  DtoProfesionalesCirugia();
				
				if (result>0 && profesionales.containsKey(indicesProfesionales[14]+i) && (profesionales.get(indicesProfesionales[14]+i)+"").equals(ConstantesBD.acronimoInsertar))
				{
					
					profesional.setCodSolCxServ(codigoServicio+"");
					profesional.setCodigoAsocio(Integer.parseInt(profesionales.get(indicesProfesionales[1]+i)+""));
					profesional.setCodigoEspecialidad(Integer.parseInt(profesionales.get(indicesProfesionales[2]+i)+""));
					profesional.setCodigoProfesional(Integer.parseInt(profesionales.get(indicesProfesionales[3]+i)+""));
					logger.info("\n ************ entre a insertarProfecionales en el mundo profesional -->"+profesionales.get(indicesProfesionales[3]+i)+"");
					profesional.setCobrable(profesionales.get(indicesProfesionales[4]+i)+"");
					profesional.setCodigoPool(ConstantesBD.codigoNuncaValido);
					profesional.setTipoEspecialista("");
					result=insertarProfesionalCirugia(connection, profesional, usuario.getLoginUsuario());	
				}
					
			}
			
		if (result>0)
			return true;
		else
			return false;
	}
	
	
	public static HashMap consultaOperacionesProfesionales (HashMap profesionales,HashMap profesionalesOld)
	{
		logger.info("\n ENTRO A CONSULTAR OPERACIONES PROFECIONALES ");
		int numReg = Integer.parseInt(profesionales.get("numRegistros")+"");
		
		int numOld = Integer.parseInt(profesionalesOld.get("numRegistros")+"");
		
		String indices [] = {indicesProfesionales[1],indicesProfesionales[2],indicesProfesionales[3],indicesProfesionales[4]};
		
		for(int i=0;i<numReg;i++)
		{
			logger.info("\n **** 1 ****");
			if ((profesionales.get(indicesProfesionales[7]+i)+"").equals(ConstantesBD.acronimoSi))
			{
				logger.info("\n **** 2 ****");
				if ((profesionales.get(indicesProfesionales[13]+i)+"").equals(ConstantesBD.acronimoSi))
				{
					logger.info("\n **** 3 ****");
					profesionales.put(indicesProfesionales[14]+i, ConstantesBD.acronimoEliminar);
				}
							
			}
			else
				if ((profesionales.get(indicesProfesionales[7]+i)+"").equals(ConstantesBD.acronimoNo))
				{
					logger.info("\n **** 4 ****");
					if (!(profesionales.get(indicesProfesionales[13]+i)+"").equals(ConstantesBD.acronimoSi))
					{
						logger.info("\n **** 5 ****");
						profesionales.put(indicesProfesionales[14]+i, ConstantesBD.acronimoInsertar);
					}
					else
					{
						logger.info("\n **** 6 ****");
						profesionales.put(indicesProfesionales[14]+i, ConstantesBD.acronimoNada);
					}
					
				}
				else
					logger.info("\n problema consultando las operaciones de profesionales ");
		}
		
		//se verifica cuales fueron modificados
		for (int o=0;o<numOld;o++)
		{
			for (int key=0;key<indices.length;key++)
			{
				if (!(profesionales.equals(indicesProfesionales[13]+o)+"").equals(ConstantesBD.acronimoSi))
					if (!(profesionales.get(indices[key]+o)+"").equals(profesionalesOld.get(indices[key]+o)+""))
					{
						
						logger.info("\n entro a asignar modificar "+profesionales.get(indices[key]+o)+"  "+profesionalesOld.get(indices[key]+o));
						profesionales.put(indicesProfesionales[14]+o, ConstantesBD.acronimoModificar);
					}
				
			}
		}
		return profesionales;
	}
	
	
	
	/**
	 * Metodo encargado de identificar si existe modificacion o no 
	 * de los diadnosticos.
	 * @param diagnosticos
	 * @return
	 */
	public static boolean esModificadoProfesionales (HashMap profesionales)
	{
		int numReg = Integer.parseInt(profesionales.get("numRegistros")+"");
		
		for(int i=0;i<numReg;i++)
		{
			if (profesionales.containsKey(indicesProfesionales[14]+i) && !(profesionales.get(indicesProfesionales[14]+i)+"").equals(ConstantesBD.acronimoNada))
				return true;
		}
		
		return false;
		
	}
	
	public static boolean modificarProfesionales (Connection connection,UsuarioBasico usuario,String codigoServicio, int asocio,int esp,int prof,String cobrable,String consecutivo)
	{
		
		
		DtoProfesionalesCirugia profesional = new DtoProfesionalesCirugia();
			
		profesional.setCodSolCxServ(codigoServicio);
		profesional.setCodigoAsocio(asocio);
		profesional.setCodigoEspecialidad(esp);
		profesional.setCodigoProfesional(prof);
		profesional.setCobrable(cobrable);
		profesional.setCodigoPool(ConstantesBD.codigoNuncaValido);
		profesional.setTipoEspecialista("");
		profesional.setCodigo(consecutivo);
		int result=modificarProfesionalCirugia(connection, profesional, usuario.getLoginUsuario());
			
		if (result>0)
			return true;
		else
			return false;
	}
	
	
	public static boolean insertarProfesionales (Connection connection,UsuarioBasico usuario,String codigoServicio, int asocio,int esp,int prof,String cobrable)
	{
		
		
		DtoProfesionalesCirugia profesional = new DtoProfesionalesCirugia();
			
		profesional.setCodSolCxServ(codigoServicio);
		profesional.setCodigoAsocio(asocio);
		profesional.setCodigoEspecialidad(esp);
		profesional.setCodigoProfesional(prof);
		profesional.setCobrable(cobrable);
		profesional.setCodigoPool(ConstantesBD.codigoNuncaValido);
		profesional.setTipoEspecialista("");
		int result=insertarProfesionalCirugia(connection, profesional, usuario.getLoginUsuario());
			
		if (result>0)
			return true;
		else
			return false;
	}
	
	
	public static ActionForward accionFiltrarSalas(Connection con, HojaQuirurgicaForm forma, UsuarioBasico usuario, HttpServletResponse response) 
	{
		//Se consultan las salas
		forma.setSalas(UtilidadesSalas.obtenerSalas(con, usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(), ConstantesBD.acronimoSi, forma.getIdTipoSala()));
		
		logger.info("salas --> "+forma.getSalas().size()+" ---  "+forma.getSalas());
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
				"<id-select>"+indicesSecInfoQx[3]+"</id-select>" +
				"<id-arreglo>sala</id-arreglo>" + //nombre de la etiqueta de cada elemento
			"</infoid>";
		
		if (forma.getFuncionalidad().equals("RespProce"))
		{
			if (forma.getSalas().size()==1)
				forma.setSecInfoQx(indicesSecInfoQx[3], forma.getSalas().get(0).get("consecutivo"));
		}
		
		for(HashMap elemento:forma.getSalas())
		{
			resultado += "<sala>";
				resultado += "<codigo>"+elemento.get("consecutivo")+"</codigo>";
				resultado += "<descripcion>"+elemento.get("descripcion")+"</descripcion>";
			resultado += "</sala>";
		}
		
		
		resultado += "</respuesta>";
		
		
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltrarSalas: "+e);
		}
		return null;
	}
	
	public static ActionForward accionFiltrarMedicoSala(Connection con,	HojaQuirurgicaForm forma, HttpServletResponse response) 
	{
		//Se consulta la tarifa servicio
		InfoDatosInt info= UtilidadesSalas.obtenerMedicoSalaMaterialesCx(forma.getIdSala());
		
		String contenido= "";
		
		if(!UtilidadTexto.isEmpty(info.getNombre()))
		{
			contenido+="Los honorarios para los asocios tipo sala y materiales seran cargados para el medico: "+info.getNombre();
		}
		
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoInnerHtml</sufijo>" +
				"<id-div>divFiltrarMedicoSala</id-div>" + //id del div a modificar
				"<contenido>"+contenido.toUpperCase()+"</contenido>" + //tabla
			"</infoid>"+
			"</respuesta>";
	
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltrarMedicoSala: "+e);
		}
		return null;
	}
	
	
	
	/**
	 * Metodo en cargado de adicionar profesionales a la  informacion Qx
	 * @param forma
	 */
	public static void adicionarProfesionalInfoQx (HojaQuirurgicaForm forma)
	{
		int numProf = Integer.parseInt(forma.getProfesionales("numRegistros")+"");
		
		forma.setProfesionales(indicesProfInfoQx[0]+numProf, ConstantesBD.codigoNuncaValido);
		forma.setProfesionales(indicesProfInfoQx[1]+numProf, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"");
		forma.setProfesionales(indicesProfInfoQx[2]+numProf, ConstantesBD.codigoNuncaValido);
		forma.setProfesionales(indicesProfInfoQx[3]+numProf, ConstantesBD.codigoNuncaValido);
		forma.setProfesionales(indicesProfInfoQx[5]+numProf, ConstantesBD.acronimoNo);
		forma.setProfesionales(indicesProfInfoQx[6]+numProf, ConstantesBD.acronimoInsertar);
		forma.setProfesionales("numRegistros", numProf+1);
	}
	
	public static void eliminarCampoProfInfoQx (HojaQuirurgicaForm forma)
	{
		int index = Integer.parseInt(forma.getIndexprofesional());
		
		if ((forma.getProfesionales(indicesProfInfoQx[5]+index)+"").equals(ConstantesBD.acronimoSi))
			forma.setProfesionales(indicesProfInfoQx[6]+index, ConstantesBD.acronimoEliminar);
		else
			forma.setProfesionales(indicesProfInfoQx[6]+index, ConstantesBD.acronimoNada);
		
		
	}
	
	
	public static void validarDatosRequeridos (Connection connection,HojaQuirurgicaForm forma,UsuarioBasico usuario,boolean secGen)
	{
		//**********************************************************************************************************************
		//se formatean los mensajes
		forma.setMensajes(new ArrayList<String>());

		//se c arga la seccion general
		HashMap datos = new HashMap ();
		HashMap secGeneral = new HashMap ();
		datos.put("solicitud", forma.getSolicitudMap(indicesSolicitud[8]+"_0"));
		datos.put("institucion", usuario.getCodigoInstitucion());
		datos.put("codigoMedico", usuario.getCodigoPersona());
		datos.put("funcionalidad", forma.getFuncionalidad());
		secGeneral = consultaDiagnosticos(connection, datos);
		int numGeneral = Utilidades.convertirAEntero(secGeneral.get("numRegistros")+"");
		String tipoCx = forma.getSolicitudMap(indicesSolicitud[14]+"_0")+"";
		
		//**********************************************************************************************************************
		//**************************************************************************************************************************
		//se carga la seeccion Servicios
		HashMap secServicios = new HashMap ();
		secServicios=consultarServicios(connection, datos);
		
		int numServicos = Utilidades.convertirAEntero(secServicios.get("numRegistros")+"");
		int numProf = Utilidades.convertirAEntero(secServicios.get(indicesServicios[10]+"0_numRegistros")+"");
		//************************************************************************************************************************
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//modificado por tarea 2864
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//************************************************************************************************************************
		//se valida la seccion de otros profesionales
		/*
		HashMap otrosProf= new HashMap ();
		otrosProf=consultarOtrosProfesionales(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"");
		int numOtrosProf = Integer.parseInt(otrosProf.get("numRegistros")+"");
		//*************************************************************************************************************************
		*/
				
		
		/////////////////////////////////////////////////////////////////
		//se debe verificar si no participo anesteciolo y luego evaluar
		//si trae o no las fechas, si no las trae se postulan 
		//modificacion por tarea 19847
		if (forma.getFuncionalidad().equals("RespProce"))
		{
			if (!(forma.getParamGenerSecServicos(indicesParamGenerales[7])+"").equals(ConstantesBD.acronimoSi) && (forma.getParamGenerSecServicos(indicesParamGenerales[18])+"").equals(ConstantesBD.acronimoSi))
				if (!(forma.getSecInfoQx(indicesSecInfoQx[7])+"").equals(ConstantesBD.acronimoSi))
					if (!UtilidadCadena.noEsVacio(forma.getFechasCx(indicesFechasCx[0])+"") && !UtilidadCadena.noEsVacio(forma.getFechasCx(indicesFechasCx[2])+"") )
					{
						forma.setFechasCx(indicesFechasCx[0], UtilidadFecha.getFechaActual());
						forma.setFechasCx(indicesFechasCx[1], UtilidadFecha.getHoraActual());
						//forma.setFechasCx(indicesFechasCx[2], UtilidadFecha.getFechaActual());
						
					}
		}
		else
		{
			if (!(forma.getParamGenerSecServicos(indicesParamGenerales[7])+"").equals(ConstantesBD.acronimoSi))
				if (!(forma.getSecInfoQx(indicesSecInfoQx[7])+"").equals(ConstantesBD.acronimoSi))
					if (!UtilidadCadena.noEsVacio(forma.getFechasCx(indicesFechasCx[0])+"") && !UtilidadCadena.noEsVacio(forma.getFechasCx(indicesFechasCx[2])+"") )
					{
						forma.setFechasCx(indicesFechasCx[0], UtilidadFecha.getFechaActual());
						forma.setFechasCx(indicesFechasCx[2], UtilidadFecha.getFechaActual());
					}
		}
		////////////////////////////////////////////////////////////////
		
		
		ArrayList<String> aux = new ArrayList<String>();
		//si el parametro secgen se pone en true se evalua la seccion general
		if (secGen)
			if (numGeneral<1 && !tipoCx.equals(ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento))
				aux.add("Es Requerido Llenar La Sección General ");
		
		logger.info("*************************************numServicios: "+numServicos);
		logger.info("*************************************numProf: "+numProf);
		if (numServicos<1 || numProf<1)
			aux.add("Es Requerido Llenar La Sección de Servicios ");
		
		/////////////////////////////////////////////////////////////////////////////////////////////
		//modificado por tarea 2864
		/////////////////////////////////////////////////////////////////
		/*
		if (numOtrosProf<1)
			aux.add("Es Requerido Llenar La Sección de Otros Profesionales ");
		*/
		////////////////////////////////////////////////////////////////////////////////////////////
		
		forma.setMensajes(aux);
	}
	
	/**
	 * Metodo encargado de almacenar las notas aclaratorias
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void guardarSecNotasAcla (Connection connection, HojaQuirurgicaForm forma,UsuarioBasico usuario,PersonaBasica paciente,boolean validacionCapitacion)
	{
		if (!(forma.getNotasAcla(indicesNotasAcla[1])+"").equals(""))
		{
			boolean transaccion=UtilidadBD.iniciarTransaccion(connection);
			HashMap datos = new HashMap();
			datos.put("numSol",forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"");
			datos.put("descripcion", forma.getNotasAcla(indicesNotasAcla[1]));
			datos.put("tipo", ConstantesIntegridadDominio.acronimoTipoNotasAclaratorias);
			datos.put("usuario",usuario.getLoginUsuario());
			
			//se inserta l ainformacion basica de la hoja qx
			if((forma.getParamGenerSecServicos(indicesParamGenerales[15])+"").equals(ConstantesBD.acronimoNo))
				transaccion=insertarHojaQxBasica(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"", usuario, validacionCapitacion);
						
			
			transaccion=insertarCamposTextHQx(connection, datos);
			
			if(transaccion)
			{
				UtilidadBD.finalizarTransaccion(connection);			
				logger.info("----->TERMINO SECCION NOTAS ACLARATORIAS AL 100% ");
				forma.setOperacionTrue(true);
				
			}
			else
			{
				UtilidadBD.abortarTransaccion(connection);
			}
			
			cargarSecNotasAcla(connection, forma, usuario,paciente,false);
			
		}
	}
	
	
	/**
	 * Metodo encargado de almacenar la patologia
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void guardarSecPatologia (Connection connection, HojaQuirurgicaForm forma,UsuarioBasico usuario,PersonaBasica paciente, boolean validacionCapitacion)
	{
		if (!(forma.getPatologia(indicesPato[1])+"").equals(""))
		{
			boolean transaccion=UtilidadBD.iniciarTransaccion(connection);
			HashMap datos = new HashMap();
			
			//se inserta l ainformacion basica de la hoja qx
			if((forma.getParamGenerSecServicos(indicesParamGenerales[15])+"").equals(ConstantesBD.acronimoNo))
				transaccion=insertarHojaQxBasica(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"", usuario, validacionCapitacion );
			
			datos.put("numSol",forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"");
			datos.put("descripcion", forma.getPatologia(indicesPato[1]));
			datos.put("tipo", ConstantesIntegridadDominio.acronimoTipoPatologia);
			datos.put("usuario",usuario.getLoginUsuario());
			
			
			transaccion=insertarCamposTextHQx(connection, datos);
			
			if(transaccion)
			{
				UtilidadBD.finalizarTransaccion(connection);			
				logger.info("----->TERMINO SECCION PATOLOGIA AL 100% ");
				forma.setOperacionTrue(true);
				
			}
			else
			{
				UtilidadBD.abortarTransaccion(connection);
			}
			
			cargarSecPatologia(connection, forma, usuario,paciente,false);
			
		}
	}
	
	/**
	 * Metodo encargado de guardar la seccion
	 * de observaciones quirurgicas
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void guardarSecObsGen (Connection connection, HojaQuirurgicaForm forma,UsuarioBasico usuario,PersonaBasica paciente, boolean validacionCapitacion)
	{
		if (!(forma.getObsGenerales(indicesObsGenerales[1])+"").equals(""))
		{
			boolean transaccion=UtilidadBD.iniciarTransaccion(connection);
			HashMap datos = new HashMap();
			datos.put("numSol",forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"");
			datos.put("descripcion", forma.getObsGenerales(indicesObsGenerales[1]));
			datos.put("tipo", ConstantesIntegridadDominio.acronimoTipoObservaciones);
			datos.put("usuario",usuario.getLoginUsuario());
			
			//se inserta l ainformacion basica de la hoja qx
			if((forma.getParamGenerSecServicos(indicesParamGenerales[15])+"").equals(ConstantesBD.acronimoNo))
				transaccion=insertarHojaQxBasica(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"", usuario, validacionCapitacion);
			
			transaccion=insertarCamposTextHQx(connection, datos);
			
			if(transaccion)
			{
				UtilidadBD.finalizarTransaccion(connection);			
				logger.info("----->TERMINO SECCION OBSERVACIONES GENERALES AL 100% ");
				forma.setOperacionTrue(true);
				
			}
			else
			{
				UtilidadBD.abortarTransaccion(connection);
			}
			
			cargarSecObsGenerales(connection, forma, usuario,paciente,false);
			
		}
	}
	
	public static boolean guardarCamposTexto (Connection connection,HojaQuirurgicaForm forma,UsuarioBasico usuario)
	{
		boolean result=true;
		HashMap datos = new HashMap();
		datos.put("numSol",forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"");
		datos.put("usuario",usuario.getLoginUsuario());
		//seccion informacion quirurgica
		if (UtilidadCadena.noEsVacio(forma.getSecInfoQx(indicesSecInfoQx[12])+""))
		{
			logger.info("\n ********** guardar campo info qx ");
			datos.put("descripcion", forma.getSecInfoQx(indicesSecInfoQx[12]));
			datos.put("tipo", ConstantesIntegridadDominio.acronimoTipoInformacionQuirurgica);
			result=insertarCamposTextHQx(connection, datos);
			logger.info("\n ********** sali guardar campo info qx --> "+result);
		}
		//seccion perfusion
		if (result && UtilidadCadena.noEsVacio(forma.getSecInfoQx(indicesSecInfoQx[16])+""))
		{
			logger.info("\n ********** guardar campo perfusion ");
			datos.put("descripcion", forma.getSecInfoQx(indicesSecInfoQx[16]));
			datos.put("tipo", ConstantesIntegridadDominio.acronimoTipoPerfusion);
			result=insertarCamposTextHQx(connection, datos);
			logger.info("\n ********** sali guardar campo perfusion --> "+result);
		}
		//seccion hallazgos
		if (result && UtilidadCadena.noEsVacio(forma.getSecInfoQx(indicesSecInfoQx[18])+""))
		{
			logger.info("\n ********** guardar campo hallazgos ");
			datos.put("descripcion", forma.getSecInfoQx(indicesSecInfoQx[18]));
			datos.put("tipo", ConstantesIntegridadDominio.acronimoTipoHallazgos);
			result=insertarCamposTextHQx(connection, datos);
			logger.info("\n ********** sali guardar campo hallazgos --> "+result);
		}
		
		return result;
	}
	
	/**
	 * Metodo encargado de guardar la seccion de
	 * informacion general
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static ArrayList<String> guardarSecInfoQx (Connection connection, HojaQuirurgicaForm forma, UsuarioBasico usuario, PersonaBasica paciente, boolean esDummy, HttpServletResponse response, boolean validacionCapitacion) throws IPSException
	{
		ArrayList<String> listaMensajes=new ArrayList<String>();
		
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		
		
		HojaAnestesia mundoAnes = new HojaAnestesia();
		//verifica el indicativo registrado desde 
		String registroDesde =  mundoAnes.consultarIndicativoRegistroDesde(connection, Integer.parseInt(forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""));
	
		//Actualizar el indicativo de registro desde 
		if(registroDesde==null || registroDesde.isEmpty()){
			registroDesde=ConstantesBDSalas.acronimoRegistroHojaQuirurgica;
			mundoAnes.actualizarIndicativoRegistroDesde(connection, Utilidades.convertirAEntero(forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""), registroDesde);
		}
		
		forma.setSolicitudMap(consultDatPeticion(connection, forma, esDummy));
		
		//se inserta la informacion basica de la hoja qx
		if((forma.getParamGenerSecServicos(indicesParamGenerales[15])+"").equals(ConstantesBD.acronimoNo)){
			transacction=insertarHojaQxBasica(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"", usuario, validacionCapitacion);
		}
		
		//guarda los cambios de la peticion
		if (transacction){
			transacction=guardarCambiosPeticion(connection, forma);
		}
		
		//se guardan las fechas de inicio y fin de la cirugia
		if (transacction){
			transacction=guardarFechasCx(connection, forma);
		}
		
		//guarda la inforamcion quirurgica
		if (transacction){
			transacction=guardarInfoQx(connection, forma);
		}
		
		//se guarda la informacion de los profesionales 
		if (transacction){
			transacction=guardarProfInfoQx(connection, forma,usuario);
		}
		
		//guardar el campo informacion quirurgica
		if (transacction){
			transacction=guardarCamposTexto(connection, forma, usuario);
		}
		
		//se guarda la salida del paciente
		if (transacction){
			//se valida que la solicitud no este liquidada
			if (!UtilidadTexto.getBoolean(forma.getParamGenerSecServicos(indicesParamGenerales[19])+"")){
				transacction=salidaPaciente(connection, forma, usuario,paciente);
			}
		}
		
		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(connection);
			Log4JManager.info("TERMINO SECCION INFORMACION QX AL 100% ");
			forma.setOperacionTrue(true);
		}
		else
		{
			UtilidadBD.abortarTransaccion(connection);
			Log4JManager.info("ABORTO TRANSACCION SECCION INFORMACION QX");
		}
		
		
		UtilidadBD.iniciarTransaccion(connection);
		//
		boolean transaccion = false;
			
		Log4JManager.info("se llama la liquidacion");
		
		LiquidacionServicios mundoLiqServ=null;
		
		//Valida que la HQ este finalizada y el campo "participo anestesiologo"
		if(UtilidadTexto.getBoolean(estaFinalizadaHojaQx(connection,forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"")) 
				&& UtilidadTexto.getBoolean(forma.getSecInfoQx(indicesSecInfoQx[7]))){
			//Valida el parametro "Se maneja Hoja de Anestesia"
			if(UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaHojaAnestesia(usuario.getCodigoInstitucionInt()))){
				//Valida la HA este finalizada y el Consumo de materiales este finalizado
				if(UtilidadTexto.getBoolean(forma.getParamGenerSecServicos(indicesParamGenerales[16])+"") && UtilidadesSalas.consultarConsumoMaterialesFinalizado(connection, Utilidades.convertirAEntero(forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""))){
					//realizar Liquidacion
					transaccion = realizarLiquidacion(connection, forma, usuario, true);
				}
			//Si el parametro "Se maneja Hoja de Anestesia" es No
			}else
				//Valida el indicativo Registrada Desde sea HQ y el Consumo de Materiales este finalizado
				if(forma.getSolicitudMap(indicesSolicitud[19]+"_0").equals(ConstantesBDSalas.acronimoRegistroHojaQuirurgica) 
						&& UtilidadesSalas.consultarConsumoMaterialesFinalizado(connection, Utilidades.convertirAEntero(forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""))){
					
					//realizar Liquidacion
					transaccion = realizarLiquidacion(connection, forma, usuario, true);
					
				} else 
					//Valida el indicativo Registrada Desde sea HA y la HA este finalizada y el consumo de materiales este finalizado
					if(forma.getSolicitudMap(indicesSolicitud[19]+"_0").equals(ConstantesBDSalas.acronimoRegistroHojaAnestesia) 
							&& UtilidadTexto.getBoolean(forma.getParamGenerSecServicos(indicesParamGenerales[16])+"") 
							&& UtilidadesSalas.consultarConsumoMaterialesFinalizado(connection, Utilidades.convertirAEntero(forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""))){
						
						//realizar Liquidacion
						transaccion = realizarLiquidacion(connection, forma, usuario, true);
						
				}	
			//Valida que no participo Anestesiologo
		}else{
			//Valida el consumo de materiales este finalizado
			if(UtilidadesSalas.consultarConsumoMaterialesFinalizado(connection, Utilidades.convertirAEntero(forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""))){
				
				//realizar Liquidacion
				transaccion = realizarLiquidacion(connection, forma, usuario, true);
				
			}	
		}
		
	
		if(transaccion)
		{
			UtilidadBD.finalizarTransaccion(connection);
			// FINALIZÓ TRANSACCION LIQUIDACION AUTOMATICA QX
		}
		else
		{
			UtilidadBD.abortarTransaccion(connection);
			// ABORTO TRANSACCION LIQUIDACION AUTOMATICA QX
			listaMensajes.add("No se pudo relizar la liquidación automática");	
		}
		
		Log4JManager.info("se inserta la informacion automatica de epicrisis");
		
		//se inserta la informacion automatica de epicrisis
		if( UtilidadTexto.getBoolean(estaFinalizadaHojaQx(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"")))
		{
			transaccion = UtilidadBD.iniciarTransaccion(connection);
			
			transaccion= Epicrisis1.insertarInfoAutomaticaCxEpicrisis(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"", usuario, paciente);
			
			
			if(transaccion)
			{
				UtilidadBD.finalizarTransaccion(connection);			
			}
			else
			{
				UtilidadBD.abortarTransaccion(connection);
				listaMensajes.add("No se pudo insertar en epicrisis");	
			}
		}
		
		cargarSecInfoQx(connection, forma, usuario,paciente,esDummy,false,response);
		validarDatosRequeridos(connection, forma, usuario, true);
		
		return listaMensajes;
	}
	
	
	
	/**
	 * Metodo encargado de verificar y guardar los datos de la
	 * peticion que pueden ser cambiado por el usuario
	 * @param connection
	 * @param forma
	 * @return
	 */
	public static boolean guardarCambiosPeticion (Connection connection,HojaQuirurgicaForm forma)
	{
		logger.info("\n ***** entro a guardarCambiosPeticion ***********");
		HashMap datos = new HashMap ();
		datos.put("codigo", forma.getDatosPeticion(indicesDatosPeticion[0]));
		
	
		boolean result=true,entrar=false;
		
		if (!(forma.getDatosPeticion(indicesDatosPeticion[3])+"").equals(forma.getDatosPeticionOld(indicesDatosPeticion[3])+""))
		{
				datos.put("requiereUci", forma.getDatosPeticion(indicesDatosPeticion[3]));
				if ((forma.getDatosPeticion(indicesDatosPeticion[3])+"").equals(ConstantesBD.acronimoSi))
					datos.put("requiereUci", true);
				else
					datos.put("requiereUci", false);
				
				entrar=true;
		}
		else
			datos.put("requiereUci", forma.getDatosPeticion(indicesDatosPeticion[3]));
		
		if (forma.getDatosPeticion().containsKey(indicesDatosPeticion[5]) && !(forma.getDatosPeticion(indicesDatosPeticion[5])+"").equals("") && !(forma.getDatosPeticion(indicesDatosPeticion[5])+"").equals(ConstantesBD.codigoNuncaValido+""))
		{
			logger.info("*******2*********");
			datos.put("solicitante", forma.getDatosPeticion(indicesDatosPeticion[5]));
			entrar=true;
		}
		else
			datos.put("solicitante", forma.getDatosPeticion(indicesDatosPeticion[6]));
		
		if (entrar)
			result=actualizarPeticion(connection, datos);
		
		return result;
	}
	
	/**
	 * Metodo encargado de guardar las fechas de la
	 * entrada y salida de la cirugia
	 * @param connection
	 * @param forma
	 * @return
	 */
	public static boolean guardarFechasCx (Connection connection,HojaQuirurgicaForm forma)
	{
		logger.info("\n ******* entre a guardarFechasCx ****");
		boolean result=true;
		
			if(esmodFechas(forma))
			{
				HashMap datos = new HashMap ();
				
				datos.put("fechaIni", forma.getFechasCx(indicesFechasCx[0]));
				datos.put("horaIni", forma.getFechasCx(indicesFechasCx[1]));
				datos.put("fechaFin", forma.getFechasCx(indicesFechasCx[2]));
				datos.put("horaFin", forma.getFechasCx(indicesFechasCx[3]));
				datos.put("duracionFin", forma.getFechasCx(indicesFechasCx[10]));
				datos.put("numSol", forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"");
			
				result=actualizarFechas(connection, datos);
			}
		
		logger.info("\n ******* salida de guardarFechasCx **** "+result);
		return result; 
	}
	
	
	/**
	 * Metodo encargado de almacenar los datos de la informacion 
	 * quirurgica.
	 * @param connection
	 * @param forma
	 * @return
	 */
	public static boolean guardarInfoQx (Connection connection,HojaQuirurgicaForm forma)
	{
		logger.info("\n entre a guardarInfoQx ");
		boolean result=true;
	
		forma.setSecInfoQx("anestesiologo", BigDecimal.valueOf(forma.getAnestesiologo()));
		
		if (esmodInfoQx(forma))
		{
			HashMap datos = new HashMap ();
			logger.info("\n**** entre a modificar ******");
			datos.put("poli", UtilidadTexto.getBoolean(forma.getSecInfoQx(indicesSecInfoQx[2])+""));
			datos.put("tipoSala", forma.getSecInfoQx(indicesSecInfoQx[9]));
			datos.put("sala",  forma.getSecInfoQx(indicesSecInfoQx[3]));
			datos.put("tipoHerida", forma.getSecInfoQx(indicesSecInfoQx[0]));
			datos.put("partAnest", UtilidadTexto.getBoolean(forma.getSecInfoQx(indicesSecInfoQx[7])+""));
			datos.put("tipoAnest",  forma.getSecInfoQx(indicesSecInfoQx[8]));
			datos.put("numSol", forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"");
			datos.put("anestesiologo", forma.getAnestesiologo()+"");
			datos.put("fechaInicioAnestesia", forma.getFechaInicioAnestesia()+"");
			datos.put("fechaFinAnestesia", forma.getFechaFinAnestesia()+"");
			datos.put("horaInicioAnestesia", forma.getHoraInicioAnestesia()+"");
			datos.put("horaFinAnestesia", forma.getHoraFinAnestesia()+"");
			// Anexo 179. Cambio 1.50
			String observacionCapitacion = null;
			if(forma.isSinAutorizacionEntidadsubcontratada()){
				observacionCapitacion = ConstantesIntegridadDominio.acronimoRegistroSinAutorizacionIngresoEstancia;
			} else{
				observacionCapitacion = ConstantesIntegridadDominio.acronimoRegistroConAutorizacionIngresoEstancia;
			}
			datos.put("observacionCapitacion", observacionCapitacion);
			// ---------------------------------------------------
			
			
			result=actualizarInfoQx(connection, datos);
		}
		
		logger.info("\n salida guardarInfoQx "+result);
		return result; 
	}
	
	
	
	
	public static boolean guardarProfInfoQx (Connection connection,HojaQuirurgicaForm forma,UsuarioBasico usuario)
	{
		logger.info("\n ****** entro a guardar profesionales informacion quirurgica ******");
		int numProf = Integer.parseInt(forma.getProfesionales("numRegistros")+"");
		operaModificaProfInfoQx(forma);
		boolean result=true;

		for (int i=0;i<numProf;i++)
		{
			if ((forma.getProfesionales(indicesProfInfoQx[6]+i)+"").equals(ConstantesBD.acronimoEliminar))
			{
				//eliminar
				if (result)
						result=eliminarProfInfoQx(connection, forma.getProfesionales(indicesProfInfoQx[0]+i)+"");
								
			}
			else
				if ((forma.getProfesionales(indicesProfInfoQx[6]+i)+"").equals(ConstantesBD.acronimoModificar))
				{
					//modifica
					if (result)
					{ 
						HashMap datos = new HashMap ();
						datos.put("tipoAsoc", forma.getProfesionales(indicesProfInfoQx[2]+i));
						datos.put("prof", forma.getProfesionales(indicesProfInfoQx[3]+i));
						datos.put("consec", forma.getProfesionales(indicesProfInfoQx[0]+i));
						datos.put("usuario", usuario.getLoginUsuario());
						result=actualizarProfInfoQx(connection, datos);
					}
				}
				else
				if ((forma.getProfesionales(indicesProfInfoQx[6]+i)+"").equals(ConstantesBD.acronimoInsertar))
				{
					//inserta
					if (result)
					{
						HashMap datos = new HashMap ();
						
						datos.put("numSol", forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"");
						datos.put("tipoAsoc", forma.getProfesionales(indicesProfInfoQx[2]+i));
						datos.put("prof", forma.getProfesionales(indicesProfInfoQx[3]+i));
						datos.put("usuario", usuario.getLoginUsuario());
						result=insertarProfInfoQx(connection, datos);
					}
				}
			
		}
		logger.info("\n ****** resultado al salir  ******-->"+result);
		return result;
		
		
		
	}
	
	public static void operaModificaProfInfoQx (HojaQuirurgicaForm forma)
	{
		int numProf = Integer.parseInt(forma.getProfesionalesOld("numRegistros")+"");
		
		for (int i=0;i<numProf;i++)
		{
			if (!((forma.getProfesionales(indicesProfInfoQx[6]+i)+"").equals(ConstantesBD.acronimoEliminar) && (forma.getProfesionales(indicesProfInfoQx[6]+i)+"").equals(ConstantesBD.acronimoInsertar)))
			{
				
				if (!(forma.getProfesionales(indicesProfInfoQx[2]+i)+"").equals(forma.getProfesionalesOld(indicesProfInfoQx[2]+i)+""))
					forma.setProfesionales(indicesProfInfoQx[6]+i, ConstantesBD.acronimoModificar);
				if (!(forma.getProfesionales(indicesProfInfoQx[3]+i)+"").equals(forma.getProfesionalesOld(indicesProfInfoQx[3]+i)+""))
					forma.setProfesionales(indicesProfInfoQx[6]+i, ConstantesBD.acronimoModificar);
				
			}
		}
		
		
		
	}
	
	/**
	 * Metod encargado de adicionar profesionales a la forma.
	 * @param forma
	 */
	public static void addOtrosProfesionales (HojaQuirurgicaForm forma)
	{
		int numProf=Integer.parseInt(forma.getOtrosProf("numRegistros")+"");
		int numProfVer=Integer.parseInt(forma.getOtrosProf(indicesOtrosProfesionales[8])+"");
		forma.setOtrosProf(indicesOtrosProfesionales[0]+numProf, ConstantesBD.codigoNuncaValido);
		forma.setOtrosProf(indicesOtrosProfesionales[1]+numProf, ConstantesBD.codigoNuncaValido);
		forma.setOtrosProf(indicesOtrosProfesionales[2]+numProf, ConstantesBD.codigoNuncaValido);
		ArrayList<HashMap<String, Object>>elemento = new ArrayList<HashMap<String,Object>>();
		forma.setOtrosProf(indicesOtrosProfesionales[4]+numProf, elemento);
		forma.setOtrosProf(indicesOtrosProfesionales[5]+numProf, ConstantesBD.acronimoInsertar);
		forma.setOtrosProf("numRegistros", numProf+1);
		forma.setOtrosProf(indicesOtrosProfesionales[8], numProfVer+1);
		
	}
	
	/**
	 * metodo encargado de  eliminar profesionales de la forma.
	 * @param forma
	 */
	public static void eliminarOtrosProfesionales (HojaQuirurgicaForm forma)
	{
		int index = Integer.parseInt(forma.getIndexprofesional());
		int numProfVer=Integer.parseInt(forma.getOtrosProf(indicesOtrosProfesionales[8])+"");
		forma.setOtrosProf(indicesOtrosProfesionales[8], numProfVer-1);
		if ((forma.getOtrosProf(indicesOtrosProfesionales[6]+index)+"").equals(ConstantesBD.acronimoSi))
			forma.setOtrosProf(indicesOtrosProfesionales[5]+index, ConstantesBD.acronimoEliminar);
		else
			forma.setOtrosProf(indicesOtrosProfesionales[5]+index, ConstantesBD.acronimoNada);
	}
	
	
	/**
	 * Metodo encargado de guardar la seccion de 
	 * otros profesionales en la BD.
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void guardarSecOtrosProf (Connection connection, HojaQuirurgicaForm forma, UsuarioBasico usuario,PersonaBasica paciente, boolean validacionCapitacion)
	{
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		int numProf = Integer.parseInt(forma.getOtrosProf("numRegistros")+"");
		//consultar operaciones datos
		consultOperaOtrosProf(forma);
		logger.info("\n entre a guardar otros profesionales -->"+forma.getOtrosProf());
		
		@SuppressWarnings("unused")
		boolean result=true;
		//se inserta l ainformacion basica de la hoja qx
		if((forma.getParamGenerSecServicos(indicesParamGenerales[15])+"").equals(ConstantesBD.acronimoNo))
			result=insertarHojaQxBasica(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"", usuario, validacionCapitacion);
		
		for (int i=0;i<numProf;i++)
		{
			if ((forma.getOtrosProf(indicesOtrosProfesionales[5]+i)+"").equals(ConstantesBD.acronimoEliminar))
			{
				//eliminar
				if (transacction)
				{
					transacction=eliminarOtrosProfesionales(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"", forma.getOtrosProf(indicesOtrosProfesionales[0]+i)+"");
				}
			}
		}	
		
		for (int i=0;i<numProf;i++)
		{
				if ((forma.getOtrosProf(indicesOtrosProfesionales[5]+i)+"").equals(ConstantesBD.acronimoModificar))
				{
					//modificar
					if (transacction)
					{
						HashMap datos = new HashMap ();
					
						datos.put("tipoParticipante", forma.getOtrosProf(indicesOtrosProfesionales[1]+i));
						datos.put("numSol", forma.getSolicitudMap(indicesSolicitud[8]+"_0"));
						datos.put("codMedico", forma.getOtrosProf(indicesOtrosProfesionales[0]+i));
						datos.put("especialidad", forma.getOtrosProf(indicesOtrosProfesionales[2]+i));
						datos.put("codMedicoOld", forma.getOtrosProf(indicesOtrosProfesionales[7]+i));
						transacction=actualizarOtrosProfesionales(connection, datos);
					}
					
				}
				else
					if ((forma.getOtrosProf(indicesOtrosProfesionales[5]+i)+"").equals(ConstantesBD.acronimoInsertar))
					{
						//insertar
						if (transacction)
						{
							HashMap datos = new HashMap ();
							
							datos.put("tipoParticipante", forma.getOtrosProf(indicesOtrosProfesionales[1]+i));
							datos.put("numSol", forma.getSolicitudMap(indicesSolicitud[8]+"_0"));
							datos.put("codMedico", forma.getOtrosProf(indicesOtrosProfesionales[0]+i));
							datos.put("especialidad", forma.getOtrosProf(indicesOtrosProfesionales[2]+i));
							transacction=IngresarOtrosProfesionales(connection, datos);
						}
					}
			
			
			
		}
		
		
		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(connection);			
			logger.info("----->TERMINO SECCION OTROS PROFESIONALES AL 100% ");
			forma.setOperacionTrue(true);
			
		}
		else
		{
			UtilidadBD.abortarTransaccion(connection);
		}
		
		cargarSecOtrosProfesionales(connection, forma, usuario,paciente,false);
		
	}
	
	
	/**
	 * metodo encargado de identificar cuales datos han sido modificados
	 * @param forma
	 */
	public static void consultOperaOtrosProf (HojaQuirurgicaForm forma)
	{
		int numProf = Integer.parseInt(forma.getOtrosProfOld("numRegistros")+"");
		
		for (int i=0;i<numProf;i++)
		{
			if (!((forma.getOtrosProf(indicesOtrosProfesionales[5]+i)+"").equals(ConstantesBD.acronimoEliminar) && (forma.getOtrosProf(indicesOtrosProfesionales[5]+i)+"").equals(ConstantesBD.acronimoInsertar)))
			{
				if (!(forma.getOtrosProf(indicesOtrosProfesionales[0]+i)+"").equals(forma.getOtrosProfOld(indicesOtrosProfesionales[0]+i)+""))
					forma.setOtrosProf(indicesOtrosProfesionales[5]+i, ConstantesBD.acronimoModificar);
			
				if (!(forma.getOtrosProf(indicesOtrosProfesionales[1]+i)+"").equals(forma.getOtrosProfOld(indicesOtrosProfesionales[1]+i)+""))
					forma.setOtrosProf(indicesOtrosProfesionales[5]+i, ConstantesBD.acronimoModificar);
				
				if (!(forma.getOtrosProf(indicesOtrosProfesionales[2]+i)+"").equals(forma.getOtrosProfOld(indicesOtrosProfesionales[2]+i)+""))
					forma.setOtrosProf(indicesOtrosProfesionales[5]+i, ConstantesBD.acronimoModificar);
			}
		}
	}
	
	
	public static boolean finalizarHqx (Connection connection, HojaQuirurgicaForm forma,UsuarioBasico usuario,String finaliza)
	{
		logger.info("\n ******************* entro a finalizar la hqx ***************************");
		boolean result=true;

		//boolean entrarFinHqx=true;

				//*****************************************FINALIZO LA HQX ************************************
				result=finalizarHQX(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"", usuario.getCodigoPersona()+"", UtilidadTexto.getBoolean(finaliza)+"");
			//********************************************************************************************	
				
		if (result && UtilidadTexto.getBoolean(finaliza))
			if (forma.getFuncionalidad().equals("AtencionCita"))
			{	
				logger.info("\n entre a cambiar el estado de la cita "+forma.getCodigoCita());
				Cita cita = new Cita();
				ResultadoBoolean resp = cita.actualizarEstadoCitaTransaccional(connection, ConstantesBD.codigoEstadoCitaAtendida, Integer.parseInt(forma.getCodigoCita()), ConstantesBD.continuarTransaccion, usuario.getLoginUsuario());
				if (resp.isTrue())
					result=true;
				else
					result=false;
			}
		
		if (result && UtilidadTexto.getBoolean(finaliza))
		{
			//esto solo aplica si la solicitud es de pyp
			if(Utilidades.esSolicitudPYP(connection,Utilidades.convertirAEntero(forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"")))
			{
				
				String codigoActividad=Utilidades.obtenerCodigoActividadProgramaPypPacienteDadaSolicitud(connection,Utilidades.convertirAEntero(forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""));
				
				//se actualiza el estado de la actividad
				Utilidades.actualizarEstadoActividadProgramaPypPaciente(connection,codigoActividad,ConstantesBD.codigoEstadoProgramaPYPEjecutado,usuario.getLoginUsuario(),"");
				//se actualiza el acumulado
				Utilidades.actualizarAcumuladoPYP(connection,forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"",usuario.getCodigoCentroAtencion()+"");
			}
		}
		
		else
		{	
		
		HashMap salPac = new HashMap ();
		// se consulta la info de la salida de la sala del paciente
		salPac=consultarSalidaPaciente(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"");
			
			if(Utilidades.esSolicitudPYP(connection,Utilidades.convertirAEntero(forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"")))
			{
				
				String codigoActividad=Utilidades.obtenerCodigoActividadProgramaPypPacienteDadaSolicitud(connection,Utilidades.convertirAEntero(forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""));
				
				//se actualiza el estado de la actividad
				Utilidades.actualizarEstadoActividadProgramaPypPaciente(connection,codigoActividad,ConstantesBD.codigoEstadoProgramaPYPSolicitado,usuario.getLoginUsuario(),"");
				//se actualiza el acumulado
				Utilidades.disminuirAcumuladoPYP(connection,forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"", usuario.getCodigoCentroAtencion()+"", salPac.get(indicesSalidaSala[2])+"");
			}
		}
		
	//ACTUALIZACION DEL ESTADO MEDICO DE LA SOLICITUD A INTERPRETADA...
			
		//Se valida que la Hoja Quirurgica este Finalizada
		if(result && UtilidadTexto.getBoolean(finaliza)){
			
			//Se valida los tipos de cargo permitidos
			if ((forma.getSolicitudMap(indicesSolicitud[14]+"_0")+"").equals(ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia) 
					|| (forma.getSolicitudMap(indicesSolicitud[14]+"_0")+"").equals(ConstantesIntegridadDominio.acronimoIndicativoCargoCirugiaCargoDirecto) 
					|| (forma.getSolicitudMap(indicesSolicitud[14]+"_0")+"").equals(ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento)
					|| (forma.getSolicitudMap(indicesSolicitud[14]+"_0")+"").equals(ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruentoCargoDirecto))
		{
				// Se verifica si alguno de los servicios no requiera interpretacion 
				boolean transaccion=true;
			
				HashMap<String, Object> datos = new HashMap<String,Object>();
				datos.put("solicitud", forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"");
				datos.put("institucion", usuario.getCodigoInstitucion());
				datos.put("codigoMedico", usuario.getCodigoPersona());
				datos.put("funcionalidad", forma.getFuncionalidad());
				datos=consultarServicios(connection, datos);
				int numReg = Utilidades.convertirAEntero(datos.get("numRegistros")+"");
				
				for (int i=0;i<numReg;i++){
					if ((forma.getServicios(indicesServicios[30]+i)+"").equals(ConstantesBD.acronimoSi))
						transaccion=false;
				}
				
				//Si ningun servicio requiere Interpretacion
				if (transaccion){
					
					//Participo Anestesiologo 
					if(UtilidadTexto.getBoolean(forma.getSecInfoQx(indicesSecInfoQx[7]))){
						
						//Valida el parametro "Se maneja Hoja de Anestesia"
						if(UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaHojaAnestesia(usuario.getCodigoInstitucionInt()))){
						
							//Valida la Hoja de Anestesia este Finalizada 
							if(UtilidadTexto.getBoolean(forma.getParamGenerSecServicos(indicesParamGenerales[16])+"")){
								//Se actualiza el estado medico de la solicitud a INTERPRETADA
								result=cambiarEstadoSolicitud(connection, ConstantesBD.codigoEstadoHCInterpretada+"", forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"", usuario.getCodigoPersona()+"");
				}
				
						} 
						//Si el parametro "Se maneja Hoja de Anestesia" es No
						else{
							//Se actualiza el estado medico de la solicitud a INTERPRETADA
					result=cambiarEstadoSolicitud(connection, ConstantesBD.codigoEstadoHCInterpretada+"", forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"", usuario.getCodigoPersona()+"");
			}
						
					} 
					//Valida que no participo Anestesiologo
					else{
						//Se actualiza el estado medico de la solicitud a INTERPRETADA
				result=cambiarEstadoSolicitud(connection, ConstantesBD.codigoEstadoHCInterpretada+"", forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"", usuario.getCodigoPersona()+"");
			}

				}
				
			}
			
			//Cambiar estado de la peticion a atendida
			if(result) 
				result=cambiarEstadoPeticion(connection, ConstantesBD.codigoEstadoPeticionAtendida+"", forma.getDatosPeticion(indicesDatosPeticion[0])+"");
		}
		
		//*********************************************************************************************

		else if (result && (forma.getSecInfoQx(indicesSecInfoQx[7])+"").equals(ConstantesBD.acronimoNo) && !UtilidadTexto.getBoolean(finaliza) )
			{
				logger.info("\n ********** 2 ************ ");
				//3)se pasa el estado de la solicitud a respondida
				result=cambiarEstadoSolicitud(connection, ConstantesBD.codigoEstadoHCRespondida+"", forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"", usuario.getCodigoPersona()+"");
				if (result)
				//cambiar estado de la peticion a  pendiente
				result=cambiarEstadoPeticion(connection, ConstantesBD.codigoEstadoPeticionPendiente+"", forma.getDatosPeticion(indicesDatosPeticion[0])+"");
				
				//Si es de consulta externa se reversa el estado de la cita
				if (forma.getFuncionalidad().equals("AtencionCita")&&
					//solo si la cita ya fue cumplida
					Utilidades.convertirAEntero(Cita.obtenerEstadoCita(connection,Utilidades.convertirAEntero(forma.getCodigoCita())))==ConstantesBD.codigoEstadoCitaAtendida)
				{	
					logger.info("\n entre a cambiar el estado de la cita "+forma.getCodigoCita());
					Cita cita = new Cita();
					ResultadoBoolean resp = cita.actualizarEstadoCitaTransaccional(connection, ConstantesBD.codigoEstadoCitaAsignada, Integer.parseInt(forma.getCodigoCita()), ConstantesBD.continuarTransaccion, usuario.getLoginUsuario());
					if (resp.isTrue())
						result=true;
					else
						result=false;
				}
			}
		
		else if (result && (forma.getSecInfoQx(indicesSecInfoQx[7])+"").equals(ConstantesBD.acronimoSi) && !UtilidadTexto.getBoolean(finaliza) )
			{
				//3)se pasa el estado de la solicitud a respondida
				result=cambiarEstadoSolicitud(connection, ConstantesBD.codigoEstadoHCRespondida+"", forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"", usuario.getCodigoPersona()+"");
				if(result) 
				//cambiar estado de la peticion a  pendiente
				result=cambiarEstadoPeticion(connection, ConstantesBD.codigoEstadoPeticionPendiente+"", forma.getDatosPeticion(indicesDatosPeticion[0])+"");
				
			}
			
		
		logger.info("\n ********** al salir de finalizar hqx ************ ---> "+result);
		return result;
	}
	
	
	public static boolean salidaPaciente (Connection connection,HojaQuirurgicaForm forma,UsuarioBasico usuario, PersonaBasica paciente) throws IPSException
	{
		boolean result=false;
			logger.info("\n ********************entre a salida del pacienete --> "+forma.getSecInfoQx()+"salida pac -->"+forma.getSalPac());
		
		//Si participo anestesiologo
		if((forma.getSecInfoQx(indicesSecInfoQx[7])+"").equals(ConstantesBD.acronimoSi))
		{
			//Parametro general maneja hoja anestesia = SI
			if(UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaHojaAnestesia(usuario.getCodigoInstitucionInt())))
			{	
				//Finalizar Hoja Quirurgica 
					if(varificarCirujanosEnServicios(connection,forma))
						result=finalizarHqx(connection, forma, usuario, ConstantesBD.acronimoSi);
					else
						result=finalizarHqx(connection, forma, usuario, ConstantesBD.acronimoNo);
					
					//Realiza las solicitudes de cargos directos de articulos  Incluidos
					if(result)
						result = RespuestaProcedimientos.generarCargosDirectosArtIncluidosHojasQA(
								connection, 
								usuario,
								paciente,
								Utilidades.convertirAEntero(forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""),
								forma.getArrayArticuloIncluidoDto(),
								forma.getJustificacionMap(),
								forma.getMedicamentosNoPosMap(),
								forma.getMedicamentosPosMap(),
								forma.getSustitutosNoPosMap(),
								forma.getDiagnosticosDefinitivos(),							
								true);
			}else
			{
				result=generarSalidaPacienteGenerica(connection,forma,usuario,paciente);
			}
		}else
		{
			result=generarSalidaPacienteGenerica(connection,forma,usuario,paciente);
		}
		
		//SE COMENTA POR INCIDENCIA AL SELECCIONAR NO EN PARTICIPO ANESTESIOLOGO 
		/*if(!UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaHojaAnestesia(usuario.getCodigoInstitucionInt())))
		{
			if((forma.getParamGenerSecServicos().get(indicesParamGenerales[11])+"").equals(ConstantesBD.acronimoNo)&&((forma.getSecInfoQx(indicesSecInfoQx[7])+"").equals(ConstantesBD.acronimoSi)))
			{
				result=generarSalidaPacienteGenerica(connection,forma,usuario,paciente);
			}
		}
		else
		{
//-------------------------------------------------------------------------------------------------------------------------------//
//################################################################################################################################
//-------------------------ESTA PARTE ES SI NO PARTISIPO ANESTSIOLO.-------------------------------------------------------------		
		//if (existeModSalPac(forma))
			
			if (((forma.getSecInfoQx(indicesSecInfoQx[7])+"").equals(ConstantesBD.acronimoNo)))
			{
				result=generarSalidaPacienteGenerica(connection,forma,usuario,paciente);
				
			}
//-------------------------------------------------------------------------------------------------------------------------------//
//################################################################################################################################
//-------------------------ESTA PARTE ES SI SI PARTISIPO ANESTSIOLO.-------------------------------------------------------------
		
			if ((forma.getSecInfoQx(indicesSecInfoQx[7])+"").equals(ConstantesBD.acronimoSi))
			{
				HashMap salPac = new HashMap ();
				// se consulta la info de la salida de la sala del paciente
				salPac=consultarSalidaPaciente(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"");
				
				//para desfinalizar la hqx si tiene anestesiologo
				if((forma.getParamGenerSecServicos(indicesParamGenerales[7])+"").equals(ConstantesBD.acronimoSi))
					result=finalizarHqx(connection, forma, usuario, ConstantesBD.acronimoNo,salPac.get(indicesSalidaSala[2])+"");
				else
					result=finalizarHqx(connection, forma, usuario, ConstantesBD.acronimoSi,salPac.get(indicesSalidaSala[2])+"");
				
				//Realiza las solicitudes de cargos directos de articulos  Incluidos
				if(result)
					result = RespuestaProcedimientos.generarCargosDirectosArtIncluidosHojasQA(
							connection, 
							usuario,
							paciente,
							Utilidades.convertirAEntero(forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""),
							forma.getArrayArticuloIncluidoDto(),
							forma.getJustificacionMap(),
							forma.getMedicamentosNoPosMap(),
							forma.getMedicamentosPosMap(),
							forma.getSustitutosNoPosMap(),
							forma.getDiagnosticosDefinitivos(),							
							true);
			}
		
		}*/
		logger.info("\n******* sali a salida del pacienete -->"+result);
		return result;
	}
	
	
	private static boolean generarSalidaPacienteGenerica(Connection connection,
			HojaQuirurgicaForm forma, UsuarioBasico usuario,
			PersonaBasica paciente) throws IPSException
	{
		boolean result=true;
		int ban=0;
		HashMap salPac = new HashMap ();
		// se consulta la info de la salida de la sala del paciente
		salPac=consultarSalidaPaciente(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"");
//		if(!UtilidadTexto.isEmpty(salPac.get(indicesSalidaSala[0])+"") && !UtilidadTexto.isEmpty(salPac.get(indicesSalidaSala[1])+"") && !UtilidadTexto.isEmpty(salPac.get(indicesSalidaSala[2])+"") && !UtilidadTexto.isEmpty(salPac.get(indicesSalidaSala[3])+"") && !UtilidadTexto.isEmpty(salPac.get(indicesSalidaSala[4])+"")){}else{}

		logger.info("\n ***** 1 *******");
		//se verifica si ya existe una salida del paciente
		if (UtilidadCadena.noEsVacio(forma.getSalPac(indicesSalidaSala[4])+"") &&  !(forma.getSalPac(indicesSalidaSala[4])+"").equals(forma.getSalPac(indicesSalidaSala[5])+""))
		{
			logger.info("\n ***** 2 *******");
			HojaAnestesia mundoAnes = new HojaAnestesia();
		
			//se almacenan los datos en la tabla salidas_sala_paciente
			ban=mundoAnes.insertarSalidaSalaPaciente(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"", forma.getSalPac(indicesSalidaSala[4])+"", usuario.getLoginUsuario());
		}
			
		//se verifica si se debe actualizar la fecha y hora fallece en la tabla solicitudes
		if (result)
		{
			if (!(forma.getSalPac(indicesSalidaSala[8])+"").equals(forma.getSalPacOld(indicesSalidaSala[8])+"") )
			{
				HashMap datos = new HashMap ();
				//fecha fallece
				datos.put("fechaFallece", forma.getSalPac(indicesSalidaSala[6]));
				//hora fallece
				datos.put("horaFallece", forma.getSalPac(indicesSalidaSala[7]));
				//numeroSolicitud
				datos.put("numeroSolicitud", forma.getSolicitudMap(indicesSolicitud[8]+"_0"));
				logger.info("\n ***************************** el diagnostico es "+forma.getSalPac(indicesSalidaSala[9]));
				
				try 
				{
					//diagAcronimo
					datos.put("diagAcronimo", (forma.getSalPac(indicesSalidaSala[9])+"").split(ConstantesBD.separadorSplit)[0]);
					//diagTipoCie
					datos.put("diagTipoCie", (forma.getSalPac(indicesSalidaSala[9])+"").split(ConstantesBD.separadorSplit)[1]);
				} catch (Exception e) 
				{
					datos.put("diagAcronimo", "");
					//diagTipoCie
					datos.put("diagTipoCie", "");
				}
					
			
				//se actualiza fecha y hora de fallece en la tabla solicitudes_cirugia
				result=ActualizarFallece(connection, datos);
			}
		
			
		}
		
		
		if (ban>0)
		{
			logger.info("\n ***** 5 *******");
			//actualiza la salida del paciente en la tabla solicitudes_cirugia
			result=actualizarSalidaPaciente (connection,forma.getSalPac(indicesSalidaSala[0])+"",forma.getSalPac(indicesSalidaSala[1])+"",
					 forma.getSalPac(indicesSalidaSala[2])+"",forma.getSalPac(indicesSalidaSala[3])+"",ConstantesBD.acronimoNo,
					 ban+"",usuario.getLoginUsuario(), forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"");
			
			
			if (result)
			{
				logger.info("\n ***** 6 *******");
				result = finalizarHqx(connection, forma, usuario, ConstantesBD.acronimoSi);
				   
				//Realiza las solicitudes de cargos directos de articulos  Incluidos
				if(result)
					result = RespuestaProcedimientos.generarCargosDirectosArtIncluidosHojasQA(
							connection,
							usuario, 
							paciente,
							Utilidades.convertirAEntero(forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""),
							forma.getArrayArticuloIncluidoDto(),
							forma.getJustificacionMap(),
							forma.getMedicamentosNoPosMap(),
							forma.getMedicamentosPosMap(),
							forma.getSustitutosNoPosMap(),
							forma.getDiagnosticosDefinitivos(),									
							true);					
			}
		}
		else 
			if(!(forma.getSalPac(indicesSalidaSala[5])+"").equals(""))//AQUI SE PREGUNTA POR LA SALIDA DE LA SALA QUE ESTABA EN LA BD
		{
			
			logger.info("\n ***** 3 *******");
			result=actualizarSalidaPaciente (connection,forma.getSalPac(indicesSalidaSala[0])+"",forma.getSalPac(indicesSalidaSala[1])+"",
											forma.getSalPac(indicesSalidaSala[2])+"",forma.getSalPac(indicesSalidaSala[3])+"",
											ConstantesBD.acronimoSi,forma.getSalPac(indicesSalidaSala[5])+"",usuario.getLoginUsuario(), 
											forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"");
			if (result)
			{
				logger.info("\n ***** 4 *******");
				result=finalizarHqx(connection, forma, usuario, ConstantesBD.acronimoNo);
										
				//Realiza las solicitudes de cargos directos de articulos  Incluidos
				if(result)
					result = RespuestaProcedimientos.generarCargosDirectosArtIncluidosHojasQA(
							connection, 
							usuario,
							paciente,
							Utilidades.convertirAEntero(forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""),
							forma.getArrayArticuloIncluidoDto(),
							forma.getJustificacionMap(),
							forma.getMedicamentosNoPosMap(),
							forma.getMedicamentosPosMap(),
							forma.getSustitutosNoPosMap(),
							forma.getDiagnosticosDefinitivos(),
							false);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param connection
	 * @param fechaIngSala
	 * @param horaIngSala
	 * @param fechaSalSala
	 * @param horaSalSala
	 * @param desSelec
	 * @param salidaPac
	 * @param login
	 * @param numSol
	 * @return
	 */
	public static boolean actualizarSalidaPaciente (Connection connection,String fechaIngSala,String horaIngSala,
													String fechaSalSala,String horaSalSala,String desSelec,
													String salidaPac,String login,String numSol)
	{
		HashMap datos = new HashMap ();
		datos.put("fechaIngSala", fechaIngSala);
		datos.put("horaIngSala", horaIngSala);
		datos.put("fechaSalSala",fechaSalSala);
		datos.put("horaSalSala", horaSalSala);
		datos.put("salPac", salidaPac);
		datos.put("desSelec", desSelec);
		datos.put("usuario", login);
		datos.put("numSol", numSol);
					
		return actualizarSalidaPaciente(connection, datos);
	}
	
	
	/**
	 * Metodo encargado de consultar su se modifico la 
	 * salida del paciente.
	 * @param forma
	 * @return
	 */
	public static boolean existeModSalPac (HojaQuirurgicaForm forma )
	{
		logger.info("\n\n\n\n\n\n  existeModSalPac "+forma.getSalPac(indicesSalidaSala[4])+"   "+forma.getSalPacOld(indicesSalidaSala[4]));
		if (!(forma.getSalPac(indicesSalidaSala[4])+"").equals(forma.getSalPacOld(indicesSalidaSala[4])+""))
			return true;

		return false;
	}
	
	/**
	 * Metodo encargado de finalizar la hqx
	 * @param connection
	 * @param numSol
	 * @param usuario
	 * @param finaliza
	 * @return
	 */
	public static boolean finalizarHQX (Connection connection, String numSol,String usuario,String finaliza)
	{
		HashMap datos = new HashMap ();
		datos.put("finalizada", finaliza);
		datos.put("medicoFin", usuario);
		datos.put("numSol",numSol);

		return cambiarEstadoHqx(connection, datos);
		
	}
	
	/**
	 * Metodo encargado de verificar si se modificaron
	 * las fechas
	 * @param forma
	 * @return
	 */
	public static boolean esmodFechas (HojaQuirurgicaForm forma)
	{
		if (!(forma.getFechasCx(indicesFechasCx[0])+"").equals(forma.getFechasCxOld(indicesFechasCx[0])+""))
			return true;
		if (!(forma.getFechasCx(indicesFechasCx[1])+"").equals(forma.getFechasCxOld(indicesFechasCx[1])+""))
			return true;
		if (!(forma.getFechasCx(indicesFechasCx[2])+"").equals(forma.getFechasCxOld(indicesFechasCx[2])+""))
			return true;
		if (!(forma.getFechasCx(indicesFechasCx[3])+"").equals(forma.getFechasCxOld(indicesFechasCx[3])+""))
			return true;
		
		return false;
	}
	
	/**
	 * Metodo encargado de verificar si
	 * se modifico la informacion
	 * @param forma
	 * @return
	 */
	public static boolean esmodInfoQx (HojaQuirurgicaForm forma)
	{
		logger.info("\n entro a esmodInfoQx informacion -->"+forma.getSecInfoQx()+" Old--->"+forma.getSecInfoQxOld());
		if (!(forma.getSecInfoQx(indicesSecInfoQx[2])+"").equals(forma.getSecInfoQxOld(indicesSecInfoQx[2])+""))
			return true;
		if (!(forma.getSecInfoQx(indicesSecInfoQx[9])+"").equals(forma.getSecInfoQxOld(indicesSecInfoQx[9])+""))
			return true;
		if (!(forma.getSecInfoQx(indicesSecInfoQx[3])+"").equals(forma.getSecInfoQxOld(indicesSecInfoQx[3])+""))
			return true;
		if (!(forma.getSecInfoQx(indicesSecInfoQx[0])+"").equals(forma.getSecInfoQxOld(indicesSecInfoQx[0])+""))
			return true;
		if (!(forma.getSecInfoQx(indicesSecInfoQx[7])+"").equals(forma.getSecInfoQxOld(indicesSecInfoQx[7])+""))
			return true;
		if (!(forma.getSecInfoQx(indicesSecInfoQx[8])+"").equals(forma.getSecInfoQxOld(indicesSecInfoQx[8])+""))
			return true;
		if(forma.getSecInfoQx("anestesiologo")!=null&&forma.getSecInfoQxOld("anestesiologo")!=null){
			if(((BigDecimal)forma.getSecInfoQx("anestesiologo")).compareTo((BigDecimal)forma.getSecInfoQxOld("anestesiologo"))!=0){
				return true;
			}
		}
		logger.info("\n voy a salir en false");
		return false;
	}
	
	
	public static void obtenerDatosRequest (HojaQuirurgicaForm forma,HttpServletRequest request)
	{
		
		//los valores enviados por request
		//---------------------------------------------------------------
		//1) esDummy
		//if (UtilidadCadena.noEsVacio(request.getParameter("esDummy")+""))
		if (forma.getEstado().equals("cargarHQxDummy") || forma.getEstado().equals("listadoPeticiones"))
		{
			forma.setEsDummy(UtilidadTexto.getBoolean(request.getParameter("esDummy")));
		
				//2) peticion
				forma.setPeticion(request.getParameter("peticion")!=null?request.getParameter("peticion"):"");
				//3)funcionalidad
				forma.setFuncionalidad(request.getParameter("funcionalidad")!=null?request.getParameter("funcionalidad"):"");
				//4)codigoCita
				forma.setCodigoCita(request.getParameter("codigoCita")!=null?request.getParameter("codigoCita"):"");
				//5)esModificable
				forma.setEsModificable(UtilidadTexto.getBoolean(request.getParameter("esModificable")+""));
				//6)ocultarEncabezado
				forma.setOcultarEncabezado(UtilidadTexto.getBoolean(request.getParameter("ocultarEncabezado")+""));
				//7)Codigo Paciente
				forma.setCodigoPaciente(request.getParameter("codigoPaciente")!=null?request.getParameter("codigoPaciente"):"");
			
		}
		//---------------------------------------------------------------
	}
	
	
	/**
	 * Metodo enecargado de obtener los asocios
	 * diferentes a cirujano y anestesiologo 
	 * que participan en cirugías
	 * @param connection
	 * @param institucion
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerAsociosDifCirAnes (Connection connection, int institucion)
	{

		ArrayList<HashMap<String, Object>> asocios = new ArrayList<HashMap<String,Object>>();
		ArrayList<HashMap<String, Object>> asociosList = new ArrayList<HashMap<String,Object>>();
		asocios=Utilidades.obtenerAsocios(connection, "", ConstantesBD.codigoServicioHonorariosCirugia+"", ConstantesBD.acronimoSi);
		
		for (int i=0;i<asocios.size();i++)
		{
			HashMap elemento = new HashMap ();
			elemento = (HashMap)asocios.get(i);
			if (!(elemento.get("codigo")+"").equals(ValoresPorDefecto.getAsocioAnestesia(institucion)) &&
				!(elemento.get("codigo")+"").equals(ValoresPorDefecto.getAsocioCirujano(institucion)))
			asociosList.add(elemento);
		}
		
		return asociosList;
	}
	
	
	public static ActionForward imprimirHqx (Connection connection,HojaQuirurgicaForm forma,HttpServletRequest request,PersonaBasica paciente,UsuarioBasico usuario, ActionMapping mapping,HttpServletResponse response) throws IPSException
	{
		//se carga la seccion general
		cargarSecGeneral(connection, forma, usuario, paciente,  forma.isEsDummy(), request, false);
		//se carga la seccion de servicios
		cargarSecServicios(connection, forma, usuario, paciente, false);
		//se carga la seccion de informacion qx
		cargarSecInfoQx(connection, forma, usuario, paciente, forma.isEsDummy(), false, response);
		//se carga la seccion de patologia
		cargarSecPatologia(connection, forma, usuario, paciente, false);
		//se carga la seccion de otros profesionales
		cargarSecOtrosProfesionales(connection, forma, usuario, paciente, false);
		// se carga la seccion de observaciones generales
		cargarSecObsGenerales(connection, forma, usuario, paciente, false);
		//se carga la seccion de notas aclaratorias
		cargarSecNotasAcla(connection, forma, usuario, paciente, false);
		
		return  mapping.findForward("imprimir");
	}

	
	public static String reversarInfoQx(Connection connection,HojaQuirurgicaForm forma,UsuarioBasico usuario){
		
		HashMap salPac = new HashMap ();
		// se consulta la info de la salida de la sala del paciente
		salPac=consultarSalidaPaciente(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"");

		boolean result=finalizarHqx(connection, forma, usuario, ConstantesBD.acronimoNo);
	
		if(result){
			//esto solo aplica si la solicitud es de pyp
			if(Utilidades.esSolicitudPYP(connection,Utilidades.convertirAEntero(forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"")))
			{
				
				String codigoActividad=Utilidades.obtenerCodigoActividadProgramaPypPacienteDadaSolicitud(connection,Utilidades.convertirAEntero(forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""));
				
				//se actualiza el estado de la actividad
				Utilidades.actualizarEstadoActividadProgramaPypPaciente(connection,codigoActividad,ConstantesBD.codigoEstadoProgramaPYPSolicitado,usuario.getLoginUsuario(),"");
				//se actualiza el acumulado
				Utilidades.disminuirAcumuladoPYP(connection,forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"", usuario.getCodigoCentroAtencion()+"", salPac.get(indicesSalidaSala[2])+"");
			}
			
			
		}
		
		if(result)	
			return  ConstantesBD.acronimoNo;
		
		return ConstantesBD.acronimoSi;
	}
	
	public static boolean varificarCirujanosEnServicios(Connection connection, HojaQuirurgicaForm forma){
		
		String [] indicesCirujanosInter = HojaAnestesia.indicesCirujanosPrincipales;
		int count=0;
		//se evalua la especialidad
		HojaAnestesia mundoAnestesia = new HojaAnestesia();
		HashMap especialidades = new HashMap ();
		
		especialidades=mundoAnestesia.consultarCirujanosIntervienenSolicitud(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"");
		
		int numEspecialidades = Integer.parseInt(especialidades.get("numRegistros")+"");
		
		for(int i=0;i<numEspecialidades;i++){
			//verifica las especialidades de la Hoja Anestesia se utilicen en los servicios de la Hoja Quirurgica
			if(especialidades.get(indicesCirujanosInter[6]+i).equals(ConstantesBD.acronimoNo))
				count++;
		}
		
		if(count>0)
			return false;
		else 
			if(count==0)
				return true;	
		
		return false;
	}
	
	
	public static boolean realizarLiquidacion(Connection connection, HojaQuirurgicaForm forma, UsuarioBasico usuario, boolean deboConsultar) throws IPSException{
		
		LiquidacionServicios mundoLiqServ=null;
		//Valida parametros Liquidación Automática de Cirugías o Liquidación Automática de No Qx este activo
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getLiquidacionAutomaticaCirugias(usuario.getCodigoInstitucionInt())) ||
				UtilidadTexto.getBoolean(ValoresPorDefecto.getLiquidacionAutomaticaNoQx(usuario.getCodigoInstitucionInt()))){
			
			//4) se llama la liquidacion automatica
			mundoLiqServ = new LiquidacionServicios();
			return mundoLiqServ.realizarLiquidacion(connection, forma.getSolicitudMap(indicesSolicitud[8]+"_0")+"", usuario, true); 
				
		}
		
		return false;
	}
	
	/******************************************************************************************************************************************/
	/*--------------------------------------------------------------------------------------------------------------------------------------
	 * FIN METODOS DE LA NUEVA HOJA QUIRURGICA
	 --------------------------------------------------------------------------------------------------------------------------------------*/
	/******************************************************************************************************************************************/
	
	
	
	/*
	 * Metodo que permite consultar ultimos Dx Principal cuando
	 * la vía de ingreso es respondida
	 */
	public static String consultarDiagnosticosHospitalizacion(Connection con,
			String numeroSolicitud) {

		StringBuilder diagnostico = new StringBuilder();

		DtoValoracionHospitalizacion dtoValoracionHospitalizacion = Valoraciones
				.valoracionDao().cargarHospitalizacion(con, numeroSolicitud);
		if (dtoValoracionHospitalizacion.getDiagnosticos() != null
				&& !dtoValoracionHospitalizacion.getDiagnosticos().isEmpty()) {
			diagnostico.append(dtoValoracionHospitalizacion.getDiagnosticos()
					.get(0).getAcronimo());
			diagnostico.append(ConstantesBD.separadorSplit);
			diagnostico.append(dtoValoracionHospitalizacion.getDiagnosticos()
					.get(0).getTipoCIE());
			diagnostico.append(ConstantesBD.separadorSplit);
			diagnostico.append(dtoValoracionHospitalizacion.getDiagnosticos()
					.get(0).getNombre());
		}
		return diagnostico.toString();

	}
	/**
	 * TODO Metodo que permite consultar ultimos Dx's relacionados cuando la vía de ingreso es
	 * respondida
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	
	
	
	public static String consultarDiagnosticosUrgencias(Connection con,
			String numeroSolicitud) {

		StringBuilder urgencias = new StringBuilder();
		DtoValoracionUrgencias dtoValoracionUrgencias = Valoraciones
				.valoracionDao().cargarUrgencias(con, numeroSolicitud);
		if (dtoValoracionUrgencias.getDiagnosticos() != null
				&& !dtoValoracionUrgencias.getDiagnosticos().isEmpty()) {
			urgencias.append(dtoValoracionUrgencias.getDiagnosticos()
					.get(0).getAcronimo());
			urgencias.append(ConstantesBD.separadorSplit);
			urgencias.append(dtoValoracionUrgencias.getDiagnosticos()
					.get(0).getTipoCIE());
			urgencias.append(ConstantesBD.separadorSplit);
			urgencias.append(dtoValoracionUrgencias.getDiagnosticos()
					.get(0).getNombre());
		}
		return urgencias.toString();

	}

	
	/**
	 * Metodo que permite consultar el ultimo Dx Principal asignado por vï¿½a
	 * hospitalización, cuando su estado es solicitado
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */

	public static String consultarUltimaDiagnosticoPpal(Connection con,
			int codigoIngreso) {

		StringBuilder ultimaDiagnosticoP = new StringBuilder();
		List<Diagnostico> diagnosticos = UtilidadesHistoriaClinica
				.obtenerUltimosDiagnosticoIngreso(con, codigoIngreso, true);

		for (Diagnostico diagnostico : diagnosticos) {
			if (diagnostico.isPrincipal()) {

				ultimaDiagnosticoP.append(diagnosticos.get(0).getAcronimo());
				ultimaDiagnosticoP.append(ConstantesBD.separadorSplit);
				ultimaDiagnosticoP.append(diagnosticos.get(0).getTipoCIE());
				ultimaDiagnosticoP.append(ConstantesBD.separadorSplit);
				ultimaDiagnosticoP.append(diagnosticos.get(0).getNombre());

				break;
			}

		}
		return ultimaDiagnosticoP.toString();
	}

	/*
	 * metodo que permite consultar los ï¿½ltimos Dx Relacionados por via
	 * hospitalización cuando su estado es solicitado
	 */

	public static List<String> consultarUltimaDiagnosticoRelacionado(
			Connection con, int codigoIngreso, HojaQuirurgicaForm forma,
			HttpServletRequest request) {

		// relacionados
		StringBuilder ultimaDiagnosticoR = new StringBuilder();
		List<Diagnostico> diagnosticos = UtilidadesHistoriaClinica
				.obtenerUltimosDiagnosticoIngreso(con, codigoIngreso, true);
		List<String> descripcionesDiagRelacionados = new ArrayList<String>(0);
		int conta = 0;
		for (int i = 0; i < diagnosticos.size(); i++) {

			if (!diagnosticos.get(i).isPrincipal()
					&& !diagnosticos.get(i).isComplicacion()) {
				ultimaDiagnosticoR = new StringBuilder();
				ultimaDiagnosticoR.append(diagnosticos.get(i).getAcronimo());
				ultimaDiagnosticoR.append(ConstantesBD.separadorSplit);
				ultimaDiagnosticoR.append(diagnosticos.get(i).getTipoCIE());
				ultimaDiagnosticoR.append(ConstantesBD.separadorSplit);
				ultimaDiagnosticoR.append(diagnosticos.get(i).getNombre());

				forma.getDiagnosticos().put(String.valueOf(conta),
						ultimaDiagnosticoR.toString());
				forma.getDiagnosticos().put("checkbox_" + conta, true);
				conta++;

			}

		}
		forma.getDiagnosticos().put("numRegistros", conta);

		return descripcionesDiagRelacionados;
	}

	// //////////////////////////////////////////Para Urgencias

	/*
	 * Metodo que permite consultar el ultimo Dx Principal asignado por vï¿½a
	 * urgencias cuando la vía de Ingreso es respondida y solicitada.
	 */

	public static String consultarDiagnosticosValoracionPPal(Connection con,
			String codigoPaciente) {

		StringBuilder diagnosticoUrg = new StringBuilder();
		List<Diagnostico> diagnosticosU = UtilidadesHistoriaClinica
				.obtenerUltimosDiagnosticosPaciente(con, codigoPaciente);
		for (Diagnostico diagnostico : diagnosticosU) {
			if (diagnostico.isPrincipal()) {

				diagnosticoUrg.append(diagnosticosU.get(0).getAcronimo());
				diagnosticoUrg.append(ConstantesBD.separadorSplit);
				diagnosticoUrg.append(diagnosticosU.get(0).getTipoCIE());
				diagnosticoUrg.append(ConstantesBD.separadorSplit);
				diagnosticoUrg.append(diagnosticosU.get(0).getNombre());

				break;
			}

		}
		return diagnosticoUrg.toString();
	}

	public static List<String> consultarDiagnosticoValoracionRela(
			Connection con, String codigoPaciente, HojaQuirurgicaForm forma) {

		// relacionados
		StringBuilder ultimaDiagnosticoRelUrg = new StringBuilder();
		List<Diagnostico> diagnosticosRelUrg = UtilidadesHistoriaClinica
				.obtenerUltimosDiagnosticosPaciente(con, codigoPaciente);
		List<String> descripcionesDiagRelacionadosUrg = new ArrayList<String>(0);
		int conta = 0;
		for (int i = 0; i < diagnosticosRelUrg.size(); i++) {

			if (!diagnosticosRelUrg.get(i).isPrincipal()
					&& !diagnosticosRelUrg.get(i).isComplicacion()) {
				ultimaDiagnosticoRelUrg = new StringBuilder();
				ultimaDiagnosticoRelUrg.append(diagnosticosRelUrg.get(i)
						.getAcronimo());
				ultimaDiagnosticoRelUrg.append(ConstantesBD.separadorSplit);
				ultimaDiagnosticoRelUrg.append(diagnosticosRelUrg.get(i)
						.getTipoCIE());
				ultimaDiagnosticoRelUrg.append(ConstantesBD.separadorSplit);
				ultimaDiagnosticoRelUrg.append(diagnosticosRelUrg.get(i)
						.getNombre());

				forma.getDiagnosticos().put(String.valueOf(conta),
						ultimaDiagnosticoRelUrg.toString());
				forma.getDiagnosticos().put("checkbox_" + conta, true);
				conta++;

			}

		}
		forma.getDiagnosticos().put("numRegistros", conta);

		return descripcionesDiagRelacionadosUrg;
	}
	
	/*************************************
	 *   INICIO MT 6497 Usabilidad HQx   *
	 *************************************/
	
	/**
	 * Consulta de peticiones o solicitudes de cirugia de un paciente
	 * <br><br>
	 * Cuando el parametro <b>codigoPeticion</b> sea null, consultara todas las peticiones, en caso contrario consultara la peticion especifica
	 * 
	 * @param connection
	 * @param codigoTarifario
	 * @param codigoIngreso
	 * @param codigoPaciente
	 * @param codigoPeticion 
	 * @param numeroSolicitud
	 * @return lista de peticiones/solicitudes
	 * @throws IPSException
	 * @author jeilones
	 * @created 24/06/2013
	 */
	public static List<PeticionQxDto> consultarPeticiones (String codigoTarifario,int codigoIngreso, int codigoPaciente, Integer codigoPeticion, Integer numeroSolicitud,Connection connection) throws IPSException{
		return  hojaQxDao().consultarPeticiones(connection, codigoTarifario, codigoIngreso, codigoPaciente,codigoPeticion,numeroSolicitud);
	}
	
	/**
	 * Consulta los servicios de la peticion/solicitud de cirugia.
	 * <br/><br/>
	 * Para el nombre del servicio se consulta el codigo manual estandar para busqueda de servicios, si este no esta definido
	 * se consulta el nombre del servicio definido para el codigo tarifario CUPS.
	 * <br/><br/>
	 * Si el parametro <b>vienePeticion</b> se encuentra en <b>true</b>, consultara todos los servicios que esten asociados a la peticion/solicitud que hagan sido agregados
	 * unicamente al momento de la creacion de la solicitud/peticion, en caso contrario, consultara todos los servicios, incluso los que hayan sido registrados en la Hoja Qx. 
	 * 
	 * @param connection
	 * @param peticion
	 * @param especialidadDto
	 * @param codigoTarifario
	 * @param vienePeticion
	 * @param consultarProfesionales
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @param consultarProfesionales 
	 * @created 25/06/2013
	 */
	public static List<ServicioHQxDto> consultarServiciosPeticion(Connection connection,
			PeticionQxDto peticion, EspecialidadDto especialidadDto, String codigoTarifario, boolean vienePeticion, boolean consultarProfesionales) throws IPSException {
		return  hojaQxDao().consultarServiciosPeticion(connection, peticion, especialidadDto, codigoTarifario, vienePeticion, consultarProfesionales);
	}
	
	/**
	 * Consulta las especialidades que intervienen en una cirugia
	 * 
	 * @param connection
	 * @param codigoSolicitudCx
	 * @return Lista de especialidades
	 * @throws IPSException
	 * @author jeilones
	 * @created 19/06/2013
	 */
	public static List<EspecialidadDto> consultarEspecialidadesInformeQx(
			Connection connection,int codigoSolicitudCx) throws IPSException {
		return  hojaQxDao().consultarEspecialidadesInformeQx(connection, codigoSolicitudCx);
	}

	/**
	 * Consulta la informacion del acto quirurgica
	 *  
	 * @param connection
	 * @param numeroSolicitud
	 * @return InformacionActoQxDto
	 * @throws IPSException
	 * @author jeilones
	 * @created 20/06/2013
	 */
	public static InformacionActoQxDto consultarInformacionActoQx (Connection connection,int numeroSolicitud) throws IPSException
	{
		return  hojaQxDao().consultarInformacionActoQx(connection, numeroSolicitud);
	}
	
	/**
	 * Consulta el ultimo diagnostico registrado en el ingreso del paciente
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 20/06/2013
	 */
	public static DtoDiagnostico consultarUltimoDiagnosticoPpal(Connection con,
			int codigoIngreso) throws IPSException{

		StringBuilder ultimaDiagnosticoP = new StringBuilder();
		List<Diagnostico> diagnosticos = UtilidadesHistoriaClinica
				.obtenerUltimosDiagnosticoIngreso(con, codigoIngreso, false);

		DtoDiagnostico dtoDiagnostico=new DtoDiagnostico();
		for (Diagnostico diagnostico : diagnosticos) {
			if (diagnostico.isPrincipal()) {
				dtoDiagnostico.setAcronimoDiagnostico(diagnostico.getAcronimo());
				dtoDiagnostico.setTipoCieDiagnosticoInt(diagnostico.getTipoCIE());
				dtoDiagnostico.setNombreDiagnostico(diagnostico.getNombre());
				dtoDiagnostico.organizarNombreCompletoDiagnostico();
				
				break;
			}

		}
		return dtoDiagnostico;
	}
	
	/**
	 * Consulta los ultimos diagnosticos relacionados registrados en el ingreso de un paciente
	 *  
	 * @param con
	 * @param codigoIngreso
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 20/06/2013
	 */
	public static List<DtoDiagnostico> consultarUltimosDiagnosticoRelacionado(Connection con,
			int codigoIngreso) throws IPSException{

		StringBuilder ultimaDiagnosticoP = new StringBuilder();
		List<Diagnostico> diagnosticos = UtilidadesHistoriaClinica
				.obtenerUltimosDiagnosticoIngreso(con, codigoIngreso, false);

		List<DtoDiagnostico>diagnosticosRelacionados=new ArrayList<DtoDiagnostico>(0);
		
		for (Diagnostico diagnostico : diagnosticos) {
			if (!diagnostico.isPrincipal()&!diagnostico.isComplicacion()) {
				DtoDiagnostico dtoDiagnostico=new DtoDiagnostico();
				dtoDiagnostico.setAcronimoDiagnostico(diagnostico.getAcronimo());
				dtoDiagnostico.setTipoCieDiagnosticoInt(diagnostico.getTipoCIE());
				dtoDiagnostico.setNombreDiagnostico(diagnostico.getNombre());
				dtoDiagnostico.organizarNombreCompletoDiagnostico();
				
				diagnosticosRelacionados.add(dtoDiagnostico);
			}

		}
		return diagnosticosRelacionados;
	}
	
	/**
	 * Consulta el diagnostico principal registrado en la HQx
	 * 
	 * @param connection
	 * @param numeroSolicitud
	 * @return Diagnostico principal
	 * @throws IPSException
	 * @author jeilones
	 * @created 20/06/2013
	 */
	public static DtoDiagnostico consultarDiagnosticoPrincipalPreoperatorio(
			Connection connection, int numeroSolicitud) throws IPSException {
		return hojaQxDao().consultarDiagnosticoPrincipalPreoperatorio(connection, numeroSolicitud);
	}

	/**
	 * Consulta los diagnosticos relacionados registrados en la HQx
	 * 
	 * @param connection
	 * @param numeroSolicitud
	 * @return Diagnosticos relacionados
	 * @throws IPSException
	 * @author jeilones
	 * @created 20/06/2013
	 */
	public static List<DtoDiagnostico> consultarDiagnosticosRelacionadosPreoperatorio(
			Connection connection, int numeroSolicitud) throws IPSException {
		return hojaQxDao().consultarDiagnosticosRelacionadosPreoperatorio(connection, numeroSolicitud);
	}
	
	/**
	 * Consultar anestesiologos activos en el aplicativo segun parametro general:
	 * "Codigo Especialidad Anestesiologia".
	 * 
	 * @param connection
	 * @return
	 * @author jeilones
	 * @param codigoEspecialidadAnestesiologia 
	 * @throws IPSException 
	 * @created 24/06/2013
	 */
	public static List<ProfesionalHQxDto> consultarAnestesiologos(Connection connection,int codigoInstitucion, String codigoEspecialidadAnestesiologia) throws IPSException{
		return hojaQxDao().consultarProfesionales(connection, codigoInstitucion,true, codigoEspecialidadAnestesiologia);
	}
	
	/**
	 * Consulta la informacion del informe qx por especialidad, las descripciones operatorias registradas,
	 * patologias, complicaciones, hallazgos y materiales especiales registrados.
	 * 
	 * @param connection
	 * @param numeroSolicitud
	 * @param codigoEspecialidad
	 * @return informe qx por especialidad
	 * @throws IPSException
	 * @author jeilones
	 * @created 26/06/2013
	 */
	public static InformeQxEspecialidadDto consultarDescripcionOperatoriaXEspecialidad(Connection connection, int numeroSolicitud, int codigoEspecialidad) throws IPSException{
		return hojaQxDao().consultarDescripcionOperatoriaXEspecialidad(connection, numeroSolicitud, codigoEspecialidad);
	}
	
	/**
	 * Consulta el diagnostico principal postoperatorio registrado en el informe Qx, si el parametro codigoInformeQx es null, consulta el primer diagnostico
	 * principal postoperatorio registrado en el informe qx 
	 * 
	 * @param connection
	 * @param numeroSolicitud
	 * @return Diagnostico principal
	 * @throws IPSException
	 * @author jeilones
	 * @created 20/06/2013
	 */
	public static DtoDiagnostico consultarDiagnosticoPrincipalPostoperatorio(Connection connection, Integer codigoInformeQx) throws IPSException{
		return hojaQxDao().consultarDiagnosticoPrincipalPostoperatorio(connection, codigoInformeQx);
	}
	
	/**
	 * Consulta los diagnosticos relacionados postoperatorios registrados en el informe Qx, si el parametro codigoInformeQx es null, consulta los primeros diagnosticos
	 * relacionados postoperatorios registrado en el informe qx 
	 * 
	 * @param connection
	 * @param numeroSolicitud
	 * @return Diagnosticos relacionados
	 * @throws IPSException
	 * @author jeilones
	 * @created 20/06/2013
	 */
	public static List<DtoDiagnostico> consultarDiagnosticosRelacionadosPostoperatorio(Connection connection,int numeroSolicitud,Integer codigoInformeQx) throws IPSException{
		return hojaQxDao().consultarDiagnosticosRelacionadosPostoperatorio(connection, numeroSolicitud, codigoInformeQx);
	}
	
	/**
	 * Consulta los diagnosticos de complicacion postoperatorios registrados en el informe Qx, si el parametro codigoInformeQx es null, consulta los primeros diagnosticos
	 * relacionados postoperatorios registrado en el informe qx 
	 * 
	 * @param connection
	 * @param numeroSolicitud
	 * @return Diagnosticos relacionados
	 * @throws IPSException
	 * @author jeilones
	 * @created 20/06/2013
	 */
	public static DtoDiagnostico consultarDiagnosticoComplicacionPostoperatorio(Connection connection,Integer codigoInformeQx) throws IPSException{
		return hojaQxDao().consultarDiagnosticoComplicacionPostoperatorio(connection, codigoInformeQx);
	}

	/**
	 * Permite persistir la informacion de la seccion informacion del acto Qx (Diagnosticos y participacion de anestesiologia)
	 * 
	 * @param connection
	 * @param actualizacion
	 * @param peticionQxDto
	 * @param informacionActoQxDto
	 * @param dxRelacionadosEliminar
	 * @param usuarioBasico
	 * 
	 * @throws IPSException
	 * @author jeilones
	 * @created 27/06/2013
	 */
	public static void guardarInformacionActoQuirurgico(Connection connection,boolean actualizacion,PeticionQxDto peticionQxDto,InformacionActoQxDto informacionActoQxDto, List<DtoDiagnostico> dxRelacionadosEliminar, UsuarioBasico usuarioBasico)throws IPSException{
		hojaQxDao().guardarInformacionActoQuirurgico(connection, actualizacion, peticionQxDto, informacionActoQxDto, dxRelacionadosEliminar, usuarioBasico);
	}

	/**
	 * Consulta si se ha guardado un informe qx por especialidad de una solicitud de cirugia 
	 * 
	 * @param connection
	 * @param numeroSolicitud
	 * @param codigoEspecialidad
	 * @return existe
	 * @throws IPSException
	 * @author jeilones
	 * @created 2/07/2013
	 */
	public static boolean existeInformeQxEspecialidad(Connection connection,int numeroSolicitud, Integer codigoInformeQx, Integer codigoEspecialidad) throws IPSException{
		return hojaQxDao().existeInformeQxEspecialidad(connection, numeroSolicitud, codigoInformeQx, codigoEspecialidad);
	}
	
	/**
	 * Permite persistir la informacion de la seccion descripcion operatoria del Informe Qx (Servicios, Diagnosticos y Patologias)
	 * 
	 * @param connection
	 * @param existeIQxE
	 * @param peticionQxDto
	 * @param informeQxEspecialidadDto
	 * @param usuarioModifica
	 * @param dxRelacionadosEliminar
	 * @param serviciosEliminar 
	 * @param codigoTarifario 
	 * @throws BDException
	 * @author jeilones
	 * @created 2/07/2013
	 */
	public static void guardarDescripcionOperatoria(Connection connection, boolean existeIQxE, PeticionQxDto peticionQxDto, InformeQxEspecialidadDto informeQxEspecialidadDto, UsuarioBasico usuarioModifica, List<DtoDiagnostico> dxRelacionadosEliminar, List<ServicioHQxDto> serviciosEliminar, String codigoTarifario)throws IPSException {
		hojaQxDao().guardarDescripcionOperatoria(connection, existeIQxE, peticionQxDto, informeQxEspecialidadDto, usuarioModifica, dxRelacionadosEliminar, serviciosEliminar,codigoTarifario);
	}
	
	/**
	 * Consultar profesionales activos en el aplicativo
	 * 
	 * @param connection
	 * @return
	 * @author jeilones
	 * @param codigoEspecialidadAnestesiologia 
	 * @throws IPSException 
	 * @created 24/06/2013
	 */
	public static List<ProfesionalHQxDto> consultarProfesionales(Connection connection,int codigoInstitucion) throws IPSException{
		return hojaQxDao().consultarProfesionales(connection, codigoInstitucion,true, null);
	}
	
	/**
	 * Consulta los profesionales relacionados a una especialidad que intervino en una cirugia
	 * 
	 * @param connection
	 * @param servicioHQxDto
	 * @return lista de profesionales
	 * @throws IPSException
	 * @author jeilones
	 * @created 8/07/2013
	 */
	public static List<ProfesionalHQxDto> consultarProfesionalesXEspecialidad(Connection connection,int numeroSolicitud, int codigoEspecialidad) throws IPSException{
		return hojaQxDao().consultarProfesionalesXEspecialidad(connection, numeroSolicitud, codigoEspecialidad);
	}
	
	/**
	 * Consulta otros profesionales relacionados que participan en una cirugia
	 * 
	 * @param connection
	 * @param numeroSolicitud
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 9/07/2013
	 */
	public static List<ProfesionalHQxDto> consultarOtrosProfesionalesInfoQx(Connection connection,int numeroSolicitud) throws IPSException{
		return hojaQxDao().consultarOtrosProfesionalesInfoQx(connection, numeroSolicitud);
	}
	
	/**
	 * Consulta los tipos de asocio que se pueden usar para relacionar un profesional a una cirugia
	 * 
	 * @param connection
	 * @param codigoInstitucion
	 * @return tipos de profesional (tipos de asocio)
	 * @throws IPSException
	 * @author jeilones
	 * @created 10/07/2013
	 */
	public static List<TipoProfesionalDto> consultarTiposProfesionales(Connection connection,int codigoInstitucion) throws IPSException{
		return hojaQxDao().consultarTiposProfesionales(connection, codigoInstitucion);
	}

	/**
	 * Guarda los profesionales que participan por servicio, por especialidad y otros profesionales relacionados al acto Qx
	 * 
	 * @param connection
	 * @param peticionQxDto
	 * @param informeQxDto
	 * @param usuarioBasico
	 * @throws IPSException
	 * @author jeilones
	 * @created 12/07/2013
	 */
	public static void guardarProfesionalesInformeQx(Connection connection,PeticionQxDto peticionQxDto,InformeQxDto informeQxDto, UsuarioBasico usuarioBasico) throws IPSException{
		hojaQxDao().guardarProfesionalesInformeQx(connection, peticionQxDto, informeQxDto, usuarioBasico);
	}
	
	 /**
	  * Metodo que Consulta seccion de ingreso / salida paciente de la hoja quirurgica dado un numero de Solicitud
	  * @param con
	  * @param numeroSolicitud
	  * @return IngresoSalidaPacienteDto dto
	  * @throws BDException
	  * @author Oscar Pulido
	  * @created 10/07/2013
	  */
	public static IngresoSalidaPacienteDto consultarIngresoSalidaPaciente(Connection con,int numeroSolicitud) throws BDException{
		return  hojaQxDao().consultarIngresoSalidaPaciente(con,numeroSolicitud);
	}
	
	/**
	 * Metodo que consulta los tipos de sala para una institucion dada
	 * @param con
	 * @param institucion
	 * @return List<TipoSalaDto> lista de TipoSala
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 10/07/2013
	 */
	public static List<TipoSalaDto> consultarTiposSala(Connection con, int institucion) throws BDException{
		return hojaQxDao().consultarTiposSala(con, institucion);
	}

	/**
	 * Metodo que consulta las salas de cirugia de un tipo de sala dado
	 * @param con
	 * @param tipoSalaDto
	 * @return List<SalaCirgugiaDto> lista salas
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 10/07/2013
	 */
	public static List<SalaCirugiaDto> consultarSalasCirugia(Connection con,TipoSalaDto tipoSalaDto) throws BDException{
		return hojaQxDao().consultarSalasCirugia(con, tipoSalaDto);
	}

	/**
	 * Metodo que consulta los destinosPaciente parametrizados para una institucion dada
	 * @param con
	 * @param institucion
	 * @return List<DestinoPacienteDto> lista de destinos
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 10/07/2013
	 */
	public static List<DestinoPacienteDto> consultarDestinosPaciente(Connection con, int institucion) throws BDException {
		return hojaQxDao().consultarDestinosPaciente(con, institucion);
	}
	
	/**
	 * Metodo que registra el indicativo ha_sido_reversada de la hoja quirurgica
	 * @param con
	 * @param numeroSolicitud
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 10/07/2013
	 */
	public static void reversarHojaQx(Connection con, int numeroSolicitud) throws BDException {
		hojaQxDao().reversarHojaQx(con,numeroSolicitud);
	}

	/**
	 * Metodo que Persiste la informacion relacionada con la seccion "IngresoSalidaPaciente" de la Hoja quirurgica,
	 * si la hqx ya existe y no es necesario crearla se actualiza, 
	 * de lo contrario se hace la creacion de un nuevo registro de hqx.
	 * @param con
	 * @param ingresoSalidaPacienteDto
	 * @param actualizacion
	 * @param usuarioModifica
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 11/07/2013
	 */
	public static void guardarIngresoSalidaPaciente(Connection con, IngresoSalidaPacienteDto ingresoSalidaPacienteDto, UsuarioBasico usuarioModifica) throws BDException {
		hojaQxDao().guardarIngresoSalidaPaciente(con, ingresoSalidaPacienteDto, usuarioModifica);
	}

	/**
	 * Metodo que consulta las notas aclaratorias relacionadas con un Informe Qx por Especialidad.
	 * @param con
	 * @param codigoInformeQxEspecialidad
	 * @return List<NotaAclaratoriaDto> 
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 11/07/2013
	 */
	public static List<NotaAclaratoriaDto> consultarNotasAclaratorias(Connection con, int codigoInformeQxEspecialidad, boolean esAscendente)  throws BDException {
		return hojaQxDao().consultarNotasAclaratorias(con, codigoInformeQxEspecialidad, esAscendente);
	}
	
	/**
	 * Metodo que persiste la informacion de una nota aclaratoria, el objeto "notaAclaratoriaDto" debe tener descripcion y 
	 * codigoInformeQxEspecialidad seteados.
	 * @param con
	 * @param notaAclaratoriaDto
	 * @param usuarioModifica
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 12/07/2013
	 */
	public static void guardarNotaAclaratotia(Connection con, NotaAclaratoriaDto notaAclaratoriaDto, UsuarioBasico usuarioModifica) throws BDException {
		hojaQxDao().guardarNotaAclaratotia(con, notaAclaratoriaDto, usuarioModifica);
	}

	/**
	 * Metodo que consulta los estados de facturacion y liquidacion de los cargos asociados a una solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * @throws BDException
	 * @autor Oscar Pulido
	 * @created 22/07/2013
	 */
	public static List<CargoSolicitudDto> consultarEstadoCargosSolicitud(Connection con, int numeroSolicitud)  throws BDException {
		return hojaQxDao().consultarEstadoCargosSolicitud(con, numeroSolicitud);
	}
	
	/**
	 * Metodo que consulta la programacion de cirugia, si existe, para una peticion dada
	 * @param con
	 * @param codigoPeticion
	 * @return
	 * @throws BDException
	 * @autor Oscar Pulido
	 * @created 23/07/2013
	 */
	public static ProgramacionPeticionQxDto consultarProgramacionPeticionQx(Connection con, int codigoPeticion) throws BDException  {
		return hojaQxDao().consultarProgramacionPeticionQx(con, codigoPeticion);
	}

	/**
	 * Consulta el cirujano que participa en la peticion de la cirugia y la especialidad que se asigna
	 * 
	 * @param connection
	 * @param codigoPeticion
	 * @return cirujano y especialidad
	 * @throws IPSException
	 * @author jeilones
	 * @created 16/07/2013
	 */
	public static ProfesionalHQxDto consultarCirujanoPeticionCx(Connection connection,int codigoPeticion) throws IPSException{
		return hojaQxDao().consultarCirujanoPeticionCx(connection, codigoPeticion);
	}
	
	/**
	 * Cambia el estado de una solicitud, si se va a cambiar a estado Interpretada, el parametro medicoInterpreta es obligatorio
	 * 
	 * @param connection
	 * @param codigoSolicitud
	 * @param estado
	 * @param medicoInterpreta
	 * @throws BDException
	 * @author jeilones
	 * @created 29/07/2013
	 */
	public static void cambiarEstadoSolicitud(Connection  connection,int codigoSolicitud, int estado,UsuarioBasico medicoInterpreta) throws BDException{
		hojaQxDao().cambiarEstadoSolicitud(connection, codigoSolicitud, estado, medicoInterpreta);
	}
	
	/**
	 * Cambia el estado de una peticion de cirugia
	 * 
	 * @param connection
	 * @param codigoPeticion
	 * @param estado
	 * @throws BDException
	 * @author jeilones
	 * @created 29/07/2013
	 */
	public static void cambiarEstadoPeticion(Connection  connection,int codigoPeticion, int estado) throws BDException{
		hojaQxDao().cambiarEstadoPeticion(connection, codigoPeticion, estado);
	}
	
	/**
	 * @param acronimoDiagnostico
	 * @param tipoCieDiagnosticoInt
	 * @return
	 */
	public static String getNombreDiagnostico(Connection con, String acronimoDiagnostico, Integer tipoCieDiagnosticoInt) throws BDException
	{
		return hojaQxDao().getNombreDiagnostico(con, acronimoDiagnostico, tipoCieDiagnosticoInt);
	}
	/*************************************
	 *    FIN MT 6497 Usabilidad HQx     *
	 *************************************/

}
