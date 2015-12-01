/*
 * Nov 17, 2005
 *
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import util.ValoresPorDefecto;

import com.princetonsa.dao.HojaQuirurgicaDao;
import com.princetonsa.dao.sqlbase.SqlBaseHojaQuirurgicaDao;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.salasCirugia.DtoProfesionalesCirugia;
import com.princetonsa.mundo.UsuarioBasico;
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

/**
 * @author Sebastián Gómez
 *
 * Clase que maneja los métodos propìos de Postgres para el acceso a la fuente
 * de datos en la funcionalidad Hoja Quirurgica
 */
@SuppressWarnings("unchecked")
public class PostgresqlHojaQuirurgicaDao implements HojaQuirurgicaDao 
{
	
	private static final String strEssolicitudTotalFinalizada = "SELECT essolicitudtotalpendiente(?)"; 
	
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(PostgresqlHojaQuirurgicaDao.class);
	
	
	
	/******************************************************************************************************************************************/
	/*--------------------------------------------------------------------------------------------------------------------------------------
	 * METODOS DE LA NUEVA HOJA QUIRURGICA
	 --------------------------------------------------------------------------------------------------------------------------------------*/
	/******************************************************************************************************************************************/
	
	/**
	 * Método para cargar los diagnósticos de un servicio
	 * @param con
	 * @param numeroServicio
	 * @return
	 */
	public HashMap cargarDiagnosticosPorServicio(Connection con, int numeroServicio, int numeroSolicitud)
	{
		return SqlBaseHojaQuirurgicaDao.cargarDiagnosticosPorServicio(con, numeroServicio, numeroSolicitud);
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
	 * ----------------------------------------------------------------------------------
	 * KEY'S DEL HASHMAP SERVICIOS9_ QUE LLEVA EL LISTADO DE SERVICIOS DE CADA PETICION
	 * ----------------------------------------------------------------------------------
	 * -- codigoServicio0_
	 * -- codCups1_
	 * -- servicio2_
	 * -- especialidad3_
	 */
	public HashMap consultarPeticiones (Connection connection, HashMap criterios)
	{
		return SqlBaseHojaQuirurgicaDao.consultarPeticiones(connection, criterios);
	}
	
	/**
	 * Metodo encargado de actualizar el subCuenta
	 * de la tabla solicitudes_cirugia cuando se 
	 * cambian los el servicio de consecutivo 1
	 * en la solicitud de cirugia.
	 */
	public boolean actualizarSubCuenta (Connection connection,double subCuenta,String solicitud)
	{
		return SqlBaseHojaQuirurgicaDao.actualizarSubCuenta(connection, subCuenta, solicitud);
	}
	
	/**
	 * Metodo encargado de consultar los datos de la
	 * solicitud.
	 * @param connection
	 * @param criterios
	 * -----------------------------------
	 * 		KEY'S DEL MAPA CRITERIOS
	 * -----------------------------------
	 * -- peticion --> Requerido
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
	public HashMap consultaSolicitud (Connection connection,HashMap criterios)
	{
		return SqlBaseHojaQuirurgicaDao.consultaSolicitud(connection, criterios);
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
	public HashMap consultaDiagnosticos (Connection connection, HashMap criterios)
	{
		return SqlBaseHojaQuirurgicaDao.consultaDiagnosticos(connection, criterios);
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
	public boolean insertarDiacnosticosPreoperatorios (Connection connection, HashMap datos)
	{
		return SqlBaseHojaQuirurgicaDao.insertarDiacnosticosPreoperatorios(connection, datos);
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
	 * 
	 */
	public boolean insertarHojaQxBasica (Connection connection,HashMap datos)
	{
		return SqlBaseHojaQuirurgicaDao.insertarHojaQxBasica(connection, datos);
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
	public boolean actualizarAutorizacion (Connection connection, HashMap datos)
	{
		return SqlBaseHojaQuirurgicaDao.actualizarAutorizacion(connection, datos);
	}
	
	/**
	 * Metodo encargado de verificar si existe
	 * la hoja quirurgica.
	 * @param connection
	 * @param solicitud
	 */
	public boolean existeHojaQx (Connection connection, String solicitud)
	{
		return SqlBaseHojaQuirurgicaDao.existeHojaQx(connection, solicitud);
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
	public boolean modificarDiagnosticoPreoperatirio (Connection connection, HashMap datos)
	{
		return SqlBaseHojaQuirurgicaDao.modificarDiagnosticoPreoperatirio(connection, datos);
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
	public  boolean eliminarDiagnostioPreoperatorio (Connection connection, HashMap datos)
	{
		return SqlBaseHojaQuirurgicaDao.eliminarDiagnostioPreoperatorio(connection, datos);
	}
	
	 /**
     * Método implementado para insertar un profesional de la cirugía
     * @param con
     * @param profesional
     * @return
     */
    public int insertarProfesionalCirugia(Connection con,DtoProfesionalesCirugia profesional,String usuario)
    {
  	  return SqlBaseHojaQuirurgicaDao.insertarProfesionalCirugia(con, profesional,usuario);
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
    public String consultarDescripcionesQx(Connection con,String consecutivoSolCx)
    {
    	return SqlBaseHojaQuirurgicaDao.consultarDescripcionesQx(con, consecutivoSolCx);
    }
	
    /**
     * Método que realiza la inserción de un diagnóstico postoperatorio
     * @param con
     * @param campos
     * @return
     */
    public int insertarDiagnosticoPostOperatorio(Connection con,HashMap campos)
    {
    	return SqlBaseHojaQuirurgicaDao.insertarDiagnosticoPostOperatorio(con, campos);
    }
    
    /**
     * Método para Insertar la descripcion qx de un servicio 
     * @param con
     * @param campos
     * @return
     */
    public int insertarDescripcionQx(Connection con,HashMap campos)
    {
    	return SqlBaseHojaQuirurgicaDao.insertarDescripcionQx(con, campos);
    }
    
     /**
     * Método que realiza la modificacion de un profesional de una cirugia
     * @param con
     * @param campos
     * @return
     */
    public int modificarProfesionalCirugia(Connection con,DtoProfesionalesCirugia profesional,String usuario)
    {
    	return SqlBaseHojaQuirurgicaDao.modificarProfesionalCirugia(con,profesional,usuario);
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
	 */
	public HashMap consultarServicios (Connection connection,HashMap datos)
	{
		return SqlBaseHojaQuirurgicaDao.consultarServicios(connection, datos);
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
	public int modificarTransaccional(
			Connection con,int numeroSolicitud,
			String duracion,int sala,int tipoSala,String politrauma,
			int tipoHerida,String finalizada,String usuarioFinaliza,
			String fechaFinaliza,String horaFinaliza,String datosMedico,String estado)
	{
		return SqlBaseHojaQuirurgicaDao.modificarTransaccional(con, numeroSolicitud, duracion, sala, tipoSala, politrauma, tipoHerida, finalizada, usuarioFinaliza, fechaFinaliza, horaFinaliza, datosMedico, estado);
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
	public ArrayList< HashMap<String, Object>>  consultarFinalidadServicio (Connection connection,HashMap criterios)
	{
		return SqlBaseHojaQuirurgicaDao.consultarFinalidadServicio(connection, criterios);
	}
	
	
	/**
	 * Método implementado para actualizar los datos de un profesional de acto quirurgico
	 * @param con
	 * @param campos
	 * @return
	 */
	public int actualizarProfesionalActoQx(Connection con,HashMap campos)
	{
		return SqlBaseHojaQuirurgicaDao.actualizarProfesionalActoQx(con, campos);
	}
	
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
	public HashMap consultarProfecionalesCx (Connection connection,HashMap criterios)
	{
		return SqlBaseHojaQuirurgicaDao.consultarProfecionalesCx(connection, criterios);
	}
	
	/**
	 * Método que verifica si una orden de cirugia posee una sala que esta ocupada
	 * por otras ordenes
	 * @param con
	 * @param numeroSolicitud
	 * @param sala
	 * @param fechaInicial
	 * @param horaInicial
	 * @param fechaFinal
	 * @param horaFinal
	 * @return
	 */
	public HashMap estaSalaOcupada(Connection con,int numeroSolicitud,int sala,
			String fechaInicial,String horaInicial,String fechaFinal,String horaFinal)
	{
		return SqlBaseHojaQuirurgicaDao.estaSalaOcupada(con,numeroSolicitud,sala,fechaInicial,horaInicial,fechaFinal,horaFinal);
	}
	
	
	/**
	 * mETODO ENCARGADO DE CONSULTAR EL NOMBRE DEL ASOCIO
	 */
	public String getNombreAsocio (Connection connection,String codigo)
	{
		return SqlBaseHojaQuirurgicaDao.getNombreAsocio(connection, codigo);
	}
	
	/**
	 * Metodo encargado de verificar
	 * si la hoja Qx esta finalizada o no.
	 * @param connection
	 * @param solicitud
	 * @return (S/N)
	 */
	public String estaFinalizadaHojaQx (Connection connection,String solicitud)
	{
		return SqlBaseHojaQuirurgicaDao.estaFinalizadaHojaQx(connection, solicitud);
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
	public boolean insertarServiciosQx (Connection connection,HashMap datos)
	{
		return SqlBaseHojaQuirurgicaDao.insertarServiciosQx(connection, datos);
	}
	
	
	/**
	 * Metodo encargado de eliminar un servicio
	 * de la tabla sol_cirugia_por_servicio
	 * @param connection
	 * @param codSolCxXServ
	 */
	public boolean eliminarServicioQx (Connection connection,String codSolCxXServ)
	{
		return SqlBaseHojaQuirurgicaDao.eliminarServicioQx(connection, codSolCxXServ);
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
	public boolean ActualizarServicioQx (Connection connection,HashMap datos)
	{
		return SqlBaseHojaQuirurgicaDao.ActualizarServicioQx(connection, datos);
	}
		
	/**
	 * Metodo encargado de eliminar la descripcion Qx
	 * de la tabla sol_cirugia_por_servicio
	 * @param connection
	 * @param codSolCxXServ
	 */
	public boolean eliminarDecQx (Connection connection,String codSolCxXServ)
	{
		return SqlBaseHojaQuirurgicaDao.eliminarDecQx(connection, codSolCxXServ);
	}
	
	/**
	 * Metodo encargado de eliminar los diagnosticos
	 * postoperatorios de la tabla diag_post_opera_sol_cx
	 * @param connection
	 * @param codSolCxXServ
	 */
	public boolean eliminarDiagPostOpe (Connection connection,String codSolCxXServ,String codigo)
	{
		return SqlBaseHojaQuirurgicaDao.eliminarDiagPostOpe(connection, codSolCxXServ,codigo);
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
	public boolean modificarDiagPostOpera (Connection connection, HashMap datos)
	{
		return SqlBaseHojaQuirurgicaDao.modificarDiagPostOpera(connection, datos);
	}
	
	/**
	 * Metodo encargado de eliminar los profecionales
	 * de la cirugia
	 * @param connection
	 * @param codSolCxXServ
	 * @param consecutivo
	 */
	public boolean eliminarProfesionalesCx (Connection connection,String codSolCxXServ,String consecutivo)
	{
		return SqlBaseHojaQuirurgicaDao.eliminarProfesionalesCx(connection, codSolCxXServ, consecutivo);
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
    public HashMap consultarDescripcionesQx2(Connection con,String consecutivoSolCx)
    {
    	return SqlBaseHojaQuirurgicaDao.consultarDescripcionesQx2(con, consecutivoSolCx);
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
	public boolean ActualizarEspecialidadesAndCirujanos (Connection connection,HashMap datos,UsuarioBasico usuario)
	{
		return SqlBaseHojaQuirurgicaDao.ActualizarEspecialidadesAndCirujanos(connection, datos, usuario);
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
	public HashMap consultarDatosPeticion (Connection connection, String peticion)
	{
		return SqlBaseHojaQuirurgicaDao.consultarDatosPeticion(connection, peticion);
	}
	
	/**
	 * Metodo encargado de consultar
	 * Las fechas de inicio y finalizacion de la
	 * Cirugia
	 */
	public HashMap consultarDatosFechas (Connection connection, String solicitud)
	{
		return SqlBaseHojaQuirurgicaDao.consultarDatosFechas(connection, solicitud);	
	}
	
	
	/**
	 * Metodo encargado de identificar si existe hoja
	 * de anestecia para una solicitud.
	 */
	public String existeHojaAnestesia (Connection connection,String solicitud)
	{
		return SqlBaseHojaQuirurgicaDao.existeHojaAnestesia(connection, solicitud);
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
	public HashMap consultarDatosHQX (Connection connection,String numSol)
	{
		return SqlBaseHojaQuirurgicaDao.consultarDatosHQX(connection, numSol);
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
	public HashMap consultarProfInfQx (Connection connection,String numSol)
	{	
		return SqlBaseHojaQuirurgicaDao.consultarProfInfQx(connection, numSol);
	}
	

	 /**
	  * Metodo encargado de consultar los campos de texto
	  * de la hoja Qx
	  */
   public String consultarCamposTextoHQx (Connection con,String numSol,String tipo)
   {
	   return SqlBaseHojaQuirurgicaDao.consultarCamposTextoHQx(con, numSol, tipo);
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
	public boolean insertarCamposTextHQx (Connection connection,HashMap datos)
	{
		return SqlBaseHojaQuirurgicaDao.insertarCamposTextHQx(connection, datos);
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
	public boolean actualizarPeticion (Connection connection,HashMap datos)
	{
		return SqlBaseHojaQuirurgicaDao.actualizarPeticion(connection, datos);
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
	public boolean actualizarFechas (Connection connection,HashMap datos)
	{
		return SqlBaseHojaQuirurgicaDao.actualizarFechas(connection, datos);
	}
	
	
	/**
	 * Metodo encargado de consultar si la
	 * solicitud esta finalizada totalmente
	 * @param numSol
	 * @return S/N
	 */
	public String essolicitudtotalpendiente (Connection connection,String numSol)
	{
		return SqlBaseHojaQuirurgicaDao.essolicitudtotalpendiente(connection, numSol,strEssolicitudTotalFinalizada);
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
	 * -- partAnest
	 * -- tipoAnest
	 * -- numSol
	 */
	public boolean actualizarInfoQx (Connection connection,HashMap datos)
	{
		return SqlBaseHojaQuirurgicaDao.actualizarInfoQx(connection, datos); 
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
	public HashMap consultarSalaProgramada (Connection connection, String peticion)
	{
		return SqlBaseHojaQuirurgicaDao.consultarSalaProgramada(connection, peticion);
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
	public boolean insertarProfInfoQx (Connection connection,HashMap datos)
	{
		return SqlBaseHojaQuirurgicaDao.insertarProfInfoQx(connection, datos);
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
	public boolean actualizarProfInfoQx (Connection connection,HashMap datos)
	{
		return SqlBaseHojaQuirurgicaDao.actualizarProfInfoQx(connection, datos);
	}
	
	/**
	 * Metodo encargado de eliminar un profesional
	 * de l ainformacion Qx
	 */
	public boolean eliminarProfInfoQx (Connection connection,String consec)
	{
		return SqlBaseHojaQuirurgicaDao.eliminarProfInfoQx(connection, consec);
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
	public boolean cambiarEstadoPeticion (Connection connection,String estado,String peticion)
	{
		return SqlBaseHojaQuirurgicaDao.cambiarEstadoPeticion(connection, estado, peticion);
	}
	
	
	/**
	 * Metodo encargado de cambiar el estado de la solicitud
	 * @param connection
	 * @param HashMap
	 * @return true /false
	 */
	@Deprecated
	public boolean cambiarEstadoSolicitud (Connection connection,HashMap parametros)
	{
		return SqlBaseHojaQuirurgicaDao.cambiarEstadoSolicitud(connection,parametros);
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
	 */
	public boolean actualizarSalidaPaciente (Connection connection,HashMap datos)
	{
		return SqlBaseHojaQuirurgicaDao.actualizarSalidaPaciente(connection, datos);
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
	public HashMap consultarSalidaPaciente (Connection connection, String numSol)
	{
		return SqlBaseHojaQuirurgicaDao.consultarSalidaPaciente(connection, numSol);
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
	public HashMap consultarOtrosProfesionales (Connection connection, String numSol)
	{
		return SqlBaseHojaQuirurgicaDao.consultarOtrosProfesionales(connection, numSol);
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
	public boolean eliminarOtrosProfesionales (Connection connection,String numSol,String codMedico)
	{
		return SqlBaseHojaQuirurgicaDao.eliminarOtrosProfesionales(connection, numSol, codMedico);
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
	public boolean actualizarOtrosProfesionales (Connection connection,HashMap datos)
	{
		return SqlBaseHojaQuirurgicaDao.actualizarOtrosProfesionales(connection, datos);
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
	public boolean IngresarOtrosProfesionales (Connection connection,HashMap datos)
	{
		return SqlBaseHojaQuirurgicaDao.IngresarOtrosProfesionales(connection, datos);
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
	public ArrayList< HashMap<String, Object>>  obtenerTiposProfesional (Connection connection,String institucion)
	{
		return SqlBaseHojaQuirurgicaDao.obtenerTiposProfesional(connection, institucion);
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
	public boolean cambiarEstadoHqx (Connection connection,HashMap datos)
	{
		return SqlBaseHojaQuirurgicaDao.cambiarEstadoHqx(connection, datos);
	}
	
	/**
	 * Metodo encargado de consultar los servicios
	 * de la peticion
	 */
	public HashMap consultarServiciosPeticion(Connection connection, String peticion)
	{
		return SqlBaseHojaQuirurgicaDao.consultarServiciosPeticion(connection, peticion);
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
	 * @return true/false
	 */
	public boolean ActualizarFallece (Connection connection, HashMap datos)
	{
		return SqlBaseHojaQuirurgicaDao.ActualizarFallece(connection, datos);
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/*************************************
	 *   INICIO MT 6497 Usabilidad HQx   *
	 *************************************/
	
	/* (non-Javadoc)
	 * @see com.princetonsa.dao.HojaQuirurgicaDao#consultarPeticiones(java.sql.Connection, int, int, int)
	 */
	@Override
	public List<PeticionQxDto> consultarPeticiones (Connection connection, String codigoTarifario,int codigoIngreso, int codigoPaciente,Integer codigoPeticion, Integer numeroSolicitud) throws BDException{
		return SqlBaseHojaQuirurgicaDao.consultarPeticiones(connection, codigoTarifario, codigoIngreso, codigoPaciente,codigoPeticion,numeroSolicitud,ValoresPorDefecto.getValorTrueParaConsultas());
	}
	
	@Override
	public List<ServicioHQxDto> consultarServiciosPeticion(Connection connection,
			PeticionQxDto peticion, EspecialidadDto especialidadDto, String codigoTarifario, boolean vienePeticion, boolean consultarProfesionales) throws BDException {
		return SqlBaseHojaQuirurgicaDao.consultarServiciosPeticion(connection, peticion, especialidadDto, codigoTarifario, vienePeticion, consultarProfesionales);
	}
	
	@Override
	public List<EspecialidadDto> consultarEspecialidadesInformeQx (Connection connection, int codigoSolicitudCx) throws BDException{
		return SqlBaseHojaQuirurgicaDao.consultarEspecialidadesInformeQx(connection, codigoSolicitudCx);
	}
	
	@Override
	public InformacionActoQxDto consultarInformacionActoQx (Connection connection,int numeroSolicitud) throws BDException{
		return SqlBaseHojaQuirurgicaDao.consultarInformacionActoQx(connection, numeroSolicitud);
	}
	
	@Override
	public DtoDiagnostico consultarDiagnosticoPrincipalPreoperatorio(
			Connection connection, int numeroSolicitud) throws BDException {
		return SqlBaseHojaQuirurgicaDao.consultarDiagnosticoPrincipalPreoperatorio(connection, numeroSolicitud);
	}

	@Override
	public List<DtoDiagnostico> consultarDiagnosticosRelacionadosPreoperatorio(
			Connection connection, int numeroSolicitud) throws BDException {
		return SqlBaseHojaQuirurgicaDao.consultarDiagnosticosRelacionadosPreoperatorio(connection, numeroSolicitud);
	}
	
	@Override
	public List<ProfesionalHQxDto> consultarProfesionales(Connection connection,int codigoInstitucion, Boolean profesionalActivo,String codigoEspecialidad) throws BDException{
		return SqlBaseHojaQuirurgicaDao.consultarProfesionales(connection, codigoInstitucion, profesionalActivo, codigoEspecialidad);
	}
	
	@Override
	public InformeQxEspecialidadDto consultarDescripcionOperatoriaXEspecialidad(Connection connection, int numeroSolicitud, int codigoEspecialidad) throws BDException{
		return SqlBaseHojaQuirurgicaDao.consultarDescripcionOperatoriaXEspecialidad(connection, numeroSolicitud, codigoEspecialidad);
	}
	
	@Override
	public DtoDiagnostico consultarDiagnosticoPrincipalPostoperatorio(
			Connection connection, Integer codigoInformeQx) throws BDException {
		return SqlBaseHojaQuirurgicaDao.consultarDiagnosticoPrincipalPostoperatorio(connection, codigoInformeQx);
	}

	@Override
	public List<DtoDiagnostico> consultarDiagnosticosRelacionadosPostoperatorio(
			Connection connection, int numeroSolicitud, Integer codigoInformeQx) throws BDException {
		return SqlBaseHojaQuirurgicaDao.consultarDiagnosticosRelacionadosPostoperatorio(connection, numeroSolicitud, codigoInformeQx);
	}

	@Override
	public DtoDiagnostico consultarDiagnosticoComplicacionPostoperatorio(
			Connection connection, Integer codigoInformeQx) throws BDException {
		return SqlBaseHojaQuirurgicaDao.consultarDiagnosticoComplicacionPostoperatorio(connection, codigoInformeQx);
	}

	@Override
	public void guardarInformacionActoQuirurgico(Connection connection,
			boolean actualizacion, PeticionQxDto peticionQxDto,
			InformacionActoQxDto informacionActoQxDto,
			List<DtoDiagnostico> dxRelacionadosEliminar, UsuarioBasico usuarioBasico) throws BDException {
		SqlBaseHojaQuirurgicaDao.guardarInformacionActoQuirurgico(connection, actualizacion, peticionQxDto, informacionActoQxDto, dxRelacionadosEliminar, usuarioBasico);
	}
	
	@Override
	public boolean existeInformeQxEspecialidad(Connection connection,int numeroSolicitud, Integer codigoInformeQx, Integer codigoEspecialidad) throws BDException{
		return SqlBaseHojaQuirurgicaDao.existeInformeQxEspecialidad(connection, numeroSolicitud, codigoInformeQx, codigoEspecialidad);
	}
	
	@Override
	public void guardarDescripcionOperatoria(Connection connection, boolean existeIQxE, PeticionQxDto peticionQxDto,InformeQxEspecialidadDto informeQxEspecialidadDto, UsuarioBasico usuarioModifica, List<DtoDiagnostico> dxRelacionadosEliminar, List<ServicioHQxDto> serviciosEliminar, String codigoTarifario) throws BDException {
		SqlBaseHojaQuirurgicaDao.guardarDescripcionOperatoria(connection, existeIQxE, peticionQxDto, informeQxEspecialidadDto, usuarioModifica, dxRelacionadosEliminar, serviciosEliminar,codigoTarifario);
	}
	
	@Override
	public List<ProfesionalHQxDto> consultarProfesionalesXEspecialidad(Connection connection,int numeroSolicitud, int codigoEspecialidad) throws BDException{
		return SqlBaseHojaQuirurgicaDao.consultarProfesionalesXEspecialidad(connection, numeroSolicitud, codigoEspecialidad);
	}

	@Override
	public List<ProfesionalHQxDto> consultarOtrosProfesionalesInfoQx(Connection connection,int numeroSolicitud) throws BDException{
		return SqlBaseHojaQuirurgicaDao.consultarOtrosProfesionalesInfoQx(connection, numeroSolicitud);
	}
	
	@Override
	public List<TipoProfesionalDto> consultarTiposProfesionales(
			Connection connection, int codigoInstitucion) throws BDException {
		return SqlBaseHojaQuirurgicaDao.consultarTiposProfesionales(connection, codigoInstitucion);
	}

	@Override
	public void guardarProfesionalesInformeQx(Connection connection,PeticionQxDto peticionQxDto,InformeQxDto informeQxDto, UsuarioBasico usuarioBasico) throws BDException{
		SqlBaseHojaQuirurgicaDao.guardarProfesionalesInformeQx(connection, peticionQxDto, informeQxDto, usuarioBasico);
	}
	
	@Override
	public IngresoSalidaPacienteDto consultarIngresoSalidaPaciente(Connection con,int numeroSolicitud) throws BDException {
		return SqlBaseHojaQuirurgicaDao.consultarIngresoSalidaPaciente(con,numeroSolicitud);
	}

	@Override
	public List<TipoSalaDto> consultarTiposSala(Connection con, int institucion) throws BDException {
		return SqlBaseHojaQuirurgicaDao.consultarTiposSala(con, institucion);
	}

	@Override
	public List<SalaCirugiaDto> consultarSalasCirugia(Connection con, TipoSalaDto tipoSalaDto) throws BDException {
		return SqlBaseHojaQuirurgicaDao.consultarSalasCirugia(con, tipoSalaDto);
	}

	@Override
	public List<DestinoPacienteDto> consultarDestinosPaciente(Connection con, int institucion) throws BDException {
		return SqlBaseHojaQuirurgicaDao.consultarDestinosPaciente(con, institucion);
	}
	
	@Override
	public void reversarHojaQx(Connection con, int numeroSolicitud) throws BDException {
		SqlBaseHojaQuirurgicaDao.reversarHojaQx(con, numeroSolicitud);
	}

	@Override
	public void guardarIngresoSalidaPaciente(Connection con, IngresoSalidaPacienteDto ingresoSalidaPacienteDto, UsuarioBasico usuarioModifica)throws BDException {
		SqlBaseHojaQuirurgicaDao.guardarIngresoSalidaPaciente(con, ingresoSalidaPacienteDto, usuarioModifica);
	}

	@Override
	public List<NotaAclaratoriaDto> consultarNotasAclaratorias(Connection con, int codigoInformeQxEspecialidad, boolean esAscendente) throws BDException {
		return SqlBaseHojaQuirurgicaDao.consultarNotasAclaratorias(con, codigoInformeQxEspecialidad, esAscendente);
	}

	@Override
	public void guardarNotaAclaratotia(Connection con, NotaAclaratoriaDto notaAclaratoriaDto, UsuarioBasico usuarioModifica) throws BDException {
		SqlBaseHojaQuirurgicaDao.guardarNotaAclaratotia(con, notaAclaratoriaDto, usuarioModifica);
	}
	
	@Override
	public ProfesionalHQxDto consultarCirujanoPeticionCx(Connection connection,
			int codigoPeticion) throws BDException {
		return SqlBaseHojaQuirurgicaDao.consultarCirujanoPeticionCx(connection, codigoPeticion);
	}
	
	@Override
	public List<CargoSolicitudDto> consultarEstadoCargosSolicitud(Connection con, int numeroSolicitud) throws BDException {
		return SqlBaseHojaQuirurgicaDao.consultarEstadoCargosSolicitud(con, numeroSolicitud);
	}

	@Override
	public ProgramacionPeticionQxDto consultarProgramacionPeticionQx(Connection con, int codigoPeticion) throws BDException {
		return SqlBaseHojaQuirurgicaDao.consultarProgramacionPeticionQx(con, codigoPeticion);
	}
	
	@Override
	public void cambiarEstadoSolicitud(Connection  connection,int codigoSolicitud, int estado,UsuarioBasico medicoInterpreta) throws BDException{
		SqlBaseHojaQuirurgicaDao.cambiarEstadoSolicitud(connection, codigoSolicitud, estado, medicoInterpreta);
	}
	
	@Override
	public void cambiarEstadoPeticion(Connection  connection,int codigoPeticion, int estado) throws BDException{
		SqlBaseHojaQuirurgicaDao.cambiarEstadoPeticion(connection, codigoPeticion, estado);
	}
	
	@Override
	public String getNombreDiagnostico(Connection con, String acronimoDiagnostico, Integer tipoCieDiagnosticoInt) throws BDException
	{
		return SqlBaseHojaQuirurgicaDao.getNombreDiagnostico(con, acronimoDiagnostico, tipoCieDiagnosticoInt);
	}
	
	/*************************************
	 *    FIN MT 6497 Usabilidad HQx     *
	 *************************************/
	
	/******************************************************************************************************************************************/
	/*--------------------------------------------------------------------------------------------------------------------------------------
	 * FIN METODOS DE LA NUEVA HOJA QUIRURGICA
	 --------------------------------------------------------------------------------------------------------------------------------------*/
	/******************************************************************************************************************************************/
	
	

}
