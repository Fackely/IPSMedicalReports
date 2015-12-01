/*
 * Nov 17, 2005
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
 * @author Sebastián Gómez R
 *
 * Interface utilizada para gestionar los métodos DAO de la funcionalidad
 * Hoja Quirurgica
 */
public interface HojaQuirurgicaDao 
{
	
	/******************************************************************************************************************************************/
	/*--------------------------------------------------------------------------------------------------------------------------------------
	 * METODOS DE LA NUEVA HOJA QUIRURGICA
	 --------------------------------------------------------------------------------------------------------------------------------------*/
	/******************************************************************************************************************************************/
	
	/**
	 * Método para cargar los diagnósticos de un servicio
	 * @param con
	 * @param numeroServicio
	 * @param numeroSolicitud @todo
	 * @return
	 */
	public HashMap cargarDiagnosticosPorServicio(Connection con, int numeroServicio, int numeroSolicitud);
	
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
	public HashMap consultarPeticiones (Connection connection, HashMap criterios);
	
	
	/**
	 * Metodo encargado de actualizar el convenio
	 * de la tabla solicitudes_cirugia cuando se 
	 * cambian los el servicio de consecutivo 1
	 * en la solicitud de cirugia.
	 */
	public boolean actualizarSubCuenta (Connection connection, double subCuenta,String solicitud);
	
	
	/**
	 * Metodo encargado de consultar los datos de la
	 * solicitud.
	 * @param connection
	 * @param criterios
	 * -----------------------------------
	 * 		KEY'S DEL MAPA CRITERIOS
	 * -----------------------------------
	 * -- Peticion  --> Opcional(requerido solicitud)
	 * -- Solicitud --> Opcional(requerido peticion)
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
	public HashMap consultaSolicitud (Connection connection,HashMap criterios);
	
	
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
	public HashMap consultaDiagnosticos (Connection connection, HashMap criterios);
	
	
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
	public boolean insertarDiacnosticosPreoperatorios (Connection connection, HashMap datos);
	
	
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
	public boolean insertarHojaQxBasica (Connection connection,HashMap datos);
	
	
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
	public boolean actualizarAutorizacion (Connection connection, HashMap datos);
	
	/**
	 * Metodo encargado de verificar si existe
	 * la hoja quirurgica.
	 * @param connection
	 * @param solicitud
	 */
	public boolean existeHojaQx (Connection connection, String solicitud);
	
	
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
	public boolean modificarDiagnosticoPreoperatirio (Connection connection, HashMap datos);
	
	
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
	public  boolean eliminarDiagnostioPreoperatorio (Connection connection, HashMap datos);
	
	/**
     * Método implementado para insertar un profesional de la cirugía
     * @param con
     * @param profesional
     * @return
     */
    public int insertarProfesionalCirugia(Connection con,DtoProfesionalesCirugia profesional,String usuario);
	
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
    public String consultarDescripcionesQx(Connection con,String consecutivoSolCx);
    
    /**
     * Método que realiza la inserción de un diagnóstico postoperatorio
     * @param con
     * @param campos
     * @return
     */
    public int insertarDiagnosticoPostOperatorio(Connection con,HashMap campos);
    
    /**
     * Método para Insertar la descripcion qx de un servicio 
     * @param con
     * @param campos
     * @return
     */
    public int insertarDescripcionQx(Connection con,HashMap campos);
    
     /**
     * Método que realiza la modificacion de un profesional de una cirugia
     * @param con
     * @param campos
     * @return
     */
    public int modificarProfesionalCirugia(Connection con,DtoProfesionalesCirugia profesional,String usuario);
	
    

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
	public HashMap consultarServicios (Connection connection,HashMap datos);
	
	
	
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
			String fechaFinaliza,String horaFinaliza,String datosMedico,String estado);
	
	
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
	public ArrayList< HashMap<String, Object>>  consultarFinalidadServicio (Connection connection,HashMap criterios);
	
	
	
	
	/**
	 * Método implementado para actualizar los datos de un profesional de acto quirurgico
	 * @param con
	 * @param campos
	 * @return
	 */
	public int actualizarProfesionalActoQx(Connection con,HashMap campos);
	
	
	
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
	public HashMap consultarProfecionalesCx (Connection connection,HashMap criterios);
	
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
			String fechaInicial,String horaInicial,String fechaFinal,String horaFinal);
	
	
	/**
	 * mETODO ENCARGADO DE CONSULTAR EL NOMBRE DEL ASOCIO
	 */
	public String getNombreAsocio (Connection connection,String codigo);
	
	/**
	 * Metodo encargado de verificar
	 * si la hoja Qx esta finalizada o no.
	 * @param connection
	 * @param solicitud
	 * @return (S/N)
	 */
	public  String estaFinalizadaHojaQx (Connection connection,String solicitud);
	
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
	public boolean insertarServiciosQx (Connection connection,HashMap datos);
	
	/**
	 * Metodo encargado de eliminar un servicio
	 * de la tabla sol_cirugia_por_servicio
	 * @param connection
	 * @param codSolCxXServ
	 */
	public boolean eliminarServicioQx (Connection connection,String codSolCxXServ);
	
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
	public boolean ActualizarServicioQx (Connection connection,HashMap datos);
	
	/**
	 * Metodo encargado de eliminar la descripcion Qx
	 * de la tabla sol_cirugia_por_servicio
	 * @param connection
	 * @param codSolCxXServ
	 */
	public boolean eliminarDecQx (Connection connection,String codSolCxXServ);
	
		
	/**
	 * Metodo encargado de eliminar los diagnosticos
	 * postoperatorios de la tabla diag_post_opera_sol_cx
	 * @param connection
	 * @param codSolCxXServ
	 */
	public boolean eliminarDiagPostOpe (Connection connection,String codSolCxXServ,String codigo);
	
	
	
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
	public boolean modificarDiagPostOpera (Connection connection, HashMap datos);
	
	/**
	 * Metodo encargado de eliminar los profecionales
	 * de la cirugia
	 * @param connection
	 * @param codSolCxXServ
	 * @param consecutivo
	 */
	public boolean eliminarProfesionalesCx (Connection connection,String codSolCxXServ,String consecutivo);
	
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
    public HashMap consultarDescripcionesQx2(Connection con,String consecutivoSolCx);
    
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
	public boolean ActualizarEspecialidadesAndCirujanos (Connection connection,HashMap datos,UsuarioBasico usuario);
	
	
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
	public HashMap consultarDatosPeticion (Connection connection, String peticion);
	
	/**
	 * Metodo encargado de consultar
	 * Las fechas de inicio y finalizacion de la
	 * Cirugia
	 */
	public HashMap consultarDatosFechas (Connection connection, String solicitud);
	
	/**
	 * Metodo encargado de identificar si existe hoja
	 * de anestecia para una solicitud.
	 */
	public String existeHojaAnestesia (Connection connection,String solicitud);
	
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
	public HashMap consultarDatosHQX (Connection connection,String numSol);
	
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
	public HashMap consultarProfInfQx (Connection connection,String numSol);
	

	 /**
	  * Metodo encargado de consultar los campos de texto
	  * de la hoja Qx
	  */
   public String consultarCamposTextoHQx (Connection con,String numSol,String tipo);
   
   
	
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
	public boolean insertarCamposTextHQx (Connection connection,HashMap datos);
	
	
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
	public boolean actualizarPeticion (Connection connection,HashMap datos);
	
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
	public boolean actualizarFechas (Connection connection,HashMap datos);
	
	

	/**
	 * Metodo encargado de consultar si la
	 * solicitud esta finalizada totalmente
	 * @param numSol
	 * @return S/N
	 */
	public String essolicitudtotalpendiente (Connection connection,String numSol);
	
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
	public boolean actualizarInfoQx (Connection connection,HashMap datos);
	
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
	public HashMap consultarSalaProgramada (Connection connection, String peticion);
	
	
	
	
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
	public boolean insertarProfInfoQx (Connection connection,HashMap datos);
	
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
	public boolean actualizarProfInfoQx (Connection connection,HashMap datos);

		
	/**
	 * Metodo encargado de eliminar un profesional
	 * de l ainformacion Qx
	 */
	public boolean eliminarProfInfoQx (Connection connection,String consec);
	
	
	/**
	 * Metodo encargado de cambiar el estado
	 * de la peticion.
	 * @param connection
	 * @param estado
	 * @param peticion
	 * @return true/false
	 */
	@Deprecated
	public boolean cambiarEstadoPeticion (Connection connection,String estado,String peticion);
	
	/**
	 * Metodo encargado de cambiar el estado de la solicitud
	 * @param connection
	 * @param HashMap parametros
	 * @return true /false
	 */
	@Deprecated
	public boolean cambiarEstadoSolicitud (Connection connection,HashMap parametros);
	
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
	public boolean actualizarSalidaPaciente (Connection connection,HashMap datos);
	
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
	public HashMap consultarSalidaPaciente (Connection connection, String numSol);
	
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
	public boolean IngresarOtrosProfesionales (Connection connection,HashMap datos);
	
	
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
	public boolean actualizarOtrosProfesionales (Connection connection,HashMap datos);
	
	
	
	
	/**
	 * Metodo encargado de eliminar otros profesionales de la
	 * tabla otros_prof_hoja_qx
	 * @param connection
	 * @param numSol
	 * @param codMedico
	 * @return True/False
	 * 
	 */
	public boolean eliminarOtrosProfesionales (Connection connection,String numSol,String codMedico);
	
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
	public HashMap consultarOtrosProfesionales (Connection connection, String numSol);


	
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
	public ArrayList< HashMap<String, Object>>  obtenerTiposProfesional (Connection connection,String institucion);
	
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
	public boolean cambiarEstadoHqx (Connection connection,HashMap datos);
	
	/**
	 * Metodo encargado de consultar los servicios
	 * de la peticion
	 */
	public HashMap consultarServiciosPeticion(Connection connection, String peticion);
	
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
	public boolean ActualizarFallece (Connection connection, HashMap datos);
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
	 * @throws BDException
	 * @author jeilones
	 * @created 24/06/2013
	 */
	List<PeticionQxDto> consultarPeticiones (Connection connection, String codigoTarifario,int codigoIngreso, int codigoPaciente, Integer codigoPeticion, Integer numeroSolicitud) throws BDException;
	
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
	 * @throws BDException
	 * @author jeilones
	 * @param consultarProfesionales 
	 * @created 25/06/2013
	 */
	List<ServicioHQxDto> consultarServiciosPeticion(Connection connection,
			PeticionQxDto peticion, EspecialidadDto especialidadDto, String codigoTarifario, boolean vienePeticion,boolean consultarProfesionales) throws BDException ;
	
	/**
	 * Consulta las especialidades que intervienen en una cirugia
	 * 
	 * @param connection
	 * @param codigoSolicitudCx
	 * @return
	 * @throws BDException
	 * @author jeilones
	 * @created 19/06/2013
	 */
	List<EspecialidadDto> consultarEspecialidadesInformeQx (Connection connection, int codigoSolicitudCx) throws BDException;
	
	/**
	 * Consulta la informacion del acto quirurgica
	 *  
	 * @param connection
	 * @param numeroSolicitud
	 * @return InformacionActoQxDto
	 * @throws BDException
	 * @author jeilones
	 * @created 20/06/2013
	 */
	InformacionActoQxDto consultarInformacionActoQx (Connection connection,int numeroSolicitud) throws BDException;
	
	/**
	 * Consulta el diagnostico principal registrado en la HQx
	 * 
	 * @param connection
	 * @param numeroSolicitud
	 * @return Diagnostico principal
	 * @throws BDException
	 * @author jeilones
	 * @created 20/06/2013
	 */
	DtoDiagnostico consultarDiagnosticoPrincipalPreoperatorio(Connection connection,int numeroSolicitud) throws BDException;
	
	/**
	 * Consulta los diagnosticos relacionados registrados en la HQx
	 * 
	 * @param connection
	 * @param numeroSolicitud
	 * @return Diagnosticos relacionados
	 * @throws BDException
	 * @author jeilones
	 * @created 20/06/2013
	 */
	List<DtoDiagnostico> consultarDiagnosticosRelacionadosPreoperatorio(Connection connection,int numeroSolicitud) throws BDException;

	/**
	 * Consulta los profesionales de una especialidad (codigoEspecialidad) especifica y segun el indicativo profesionalActivo lo indique, ordenados ascendemente por sus apellidos y nombres,
	 * si profesionalActivo es null entonces consultara tanto los profesionales activos como los inactivos
	 * 
	 * @param connection
	 * @param codigoInsitucion
	 * @param profesionalActivo
	 * @param codigoEspecialidad
	 * @return lista de profesionales
	 * @throws BDException
	 * @author jeilones
	 * @created 10/07/2013
	 */
	List<ProfesionalHQxDto> consultarProfesionales(Connection connection, int codigoInsitucion,Boolean profesionalActivo,String codigoEspecialidad) throws BDException;
	
	/**
	 * Consulta la informacion del informe qx por especialidad, las descripciones operatorias registradas,
	 * patologias, complicaciones, hallazgos y materiales especiales registrados.
	 * 
	 * @param connection
	 * @param numeroSolicitud
	 * @param codigoEspecialidad
	 * @return informe qx por especialidad
	 * @throws BDException
	 * @author jeilones
	 * @created 26/06/2013
	 */
	InformeQxEspecialidadDto consultarDescripcionOperatoriaXEspecialidad(Connection connection, int numeroSolicitud, int codigoEspecialidad) throws BDException;
	
	/**
	 * Consulta el diagnostico principal postoperatorio registrado en el informe Qx, si el parametro codigoInformeQx es null, consulta el primer diagnostico
	 * principal postoperatorio registrado en el informe qx 
	 * 
	 * @param connection
	 * @param numeroSolicitud
	 * @return Diagnostico principal
	 * @throws BDException
	 * @author jeilones
	 * @created 20/06/2013
	 */
	DtoDiagnostico consultarDiagnosticoPrincipalPostoperatorio(Connection connection, Integer codigoInformeQx) throws BDException;
	
	/**
	 * Consulta los diagnosticos relacionados postoperatorios registrados en el informe Qx, si el parametro codigoInformeQx es null, consulta los primeros diagnosticos
	 * relacionados postoperatorios registrado en el informe qx 
	 * 
	 * @param connection
	 * @param numeroSolicitud
	 * @return Diagnosticos relacionados
	 * @throws BDException
	 * @author jeilones
	 * @created 20/06/2013
	 */
	List<DtoDiagnostico> consultarDiagnosticosRelacionadosPostoperatorio(Connection connection, int numeroSolicitud,Integer codigoInformeQx) throws BDException;
	
	/**
	 * Consulta los diagnosticos de complicacion postoperatorios registrados en el informe Qx, si el parametro codigoInformeQx es null, consulta los primeros diagnosticos
	 * relacionados postoperatorios registrado en el informe qx 
	 * 
	 * @param connection
	 * @param numeroSolicitud
	 * @return Diagnostico de complicacion
	 * @throws BDException
	 * @author jeilones
	 * @created 20/06/2013
	 */
	DtoDiagnostico consultarDiagnosticoComplicacionPostoperatorio(Connection connection,Integer codigoInformeQx) throws BDException;
	
	/**
	 * Permite persistir la informacion de la seccion informacion del acto Qx (Diagnosticos y participacion de anestesiologia)
	 * 
	 * @param connection
	 * @param actualizacion
	 * @param peticionQxDto
	 * @param informacionActoQxDto
	 * @param dxRelacionadosEliminar
	 * @param usuarioBasico
	 * @throws BDException
	 * @author jeilones
	 * @created 27/06/2013
	 */
	void guardarInformacionActoQuirurgico(Connection connection,boolean actualizacion,PeticionQxDto peticionQxDto,InformacionActoQxDto informacionActoQxDto, List<DtoDiagnostico> dxRelacionadosEliminar,UsuarioBasico usuarioBasico)throws BDException;
	
	/**
	 * Consulta si se ha guardado un informe qx por especialidad de una solicitud de cirugia 
	 * 
	 * @param connection
	 * @param numeroSolicitud
	 * @param codigoEspecialidad
	 * @return existe
	 * @throws BDException
	 * @author jeilones
	 * @created 2/07/2013
	 */
	boolean existeInformeQxEspecialidad(Connection connection,int numeroSolicitud, Integer codigoInformeQx, Integer codigoEspecialidad) throws BDException;
	
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
	void guardarDescripcionOperatoria(Connection connection,boolean existeIQxE, PeticionQxDto peticionQxDto, InformeQxEspecialidadDto informeQxEspecialidadDto, UsuarioBasico usuarioModifica, List<DtoDiagnostico> dxRelacionadosEliminar,List<ServicioHQxDto> serviciosEliminar, String codigoTarifario) throws BDException ;

	/**
	 * Consulta los profesionales relacionados a una especialidad que intervino en una cirugia
	 * 
	 * @param connection
	 * @param servicioHQxDto
	 * @return lista de profesionales
	 * @throws BDException
	 * @author jeilones
	 * @created 8/07/2013
	 */
	List<ProfesionalHQxDto> consultarProfesionalesXEspecialidad(Connection connection,int numeroSolicitud, int codigoEspecialidad) throws BDException;
	
	/**
	 * Consulta otros profesionales relacionados que participan en una cirugia
	 * 
	 * @param connection
	 * @param numeroSolicitud
	 * @return
	 * @throws BDException
	 * @author jeilones
	 * @created 9/07/2013
	 */
	List<ProfesionalHQxDto> consultarOtrosProfesionalesInfoQx(Connection connection,int numeroSolicitud) throws BDException;

	/**
	 * Consulta los tipos de asocio que se pueden usar para relacionar un profesional a una cirugia
	 * 
	 * @param connection
	 * @param codigoInstitucion
	 * @return tipos de profesional (tipos de asocio)
	 * @throws BDException
	 * @author jeilones
	 * @created 10/07/2013
	 */
	List<TipoProfesionalDto> consultarTiposProfesionales(Connection connection,int codigoInstitucion) throws BDException;

	/**
	 * Guarda los profesionales que participan por servicio, por especialidad y otros profesionales relacionados al acto Qx
	 * 
	 * @param connection
	 * @param peticionQxDto
	 * @param informeQxDto
	 * @param usuarioBasico
	 * @throws BDException
	 * @author jeilones
	 * @created 12/07/2013
	 */
	void guardarProfesionalesInformeQx(Connection connection,PeticionQxDto peticionQxDto,InformeQxDto informeQxDto, UsuarioBasico usuarioBasico) throws BDException;
	/**
	 * Metodo que Consulta seccion de ingreso / salida paciente de la hoja quirurgica  dado un numero de Solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 10/07/2013
	 */
	IngresoSalidaPacienteDto consultarIngresoSalidaPaciente(Connection con,int numeroSolicitud) throws BDException;

	/**
	 * Metodo que consulta los tipos de sala para una institucion dada
	 * @param con
	 * @param institucion
	 * @return List<TipoSalaDto> lista de TipoSala
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 10/07/2013
	 */
	List<TipoSalaDto> consultarTiposSala(Connection con, int institucion) throws BDException;

	/**
	 * Metodo que consulta las salas de cirugia de un tipo de sala dado
	 * @param con
	 * @param tipoSalaDto
	 * @return List<SalaCirgugiaDto> lista salas
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 10/07/2013
	 */
	List<SalaCirugiaDto> consultarSalasCirugia(Connection con,TipoSalaDto tipoSalaDto) throws BDException;

	/**
	 * Metodo que consulta los destinosPaciente parametrizados para una institucion dada
	 * @param con
	 * @param institucion
	 * @return List<SalaCirgugiaDto> lista salas
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 10/07/2013
	 */
	List<DestinoPacienteDto> consultarDestinosPaciente(Connection con, int institucion) throws BDException;

	/**
	 * Metodo que registra el indicativo ha_sido_reversada de la hoja quirurgica
	 * @param con
	 * @param numeroSolicitud
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 10/07/2013
	 */
	void reversarHojaQx(Connection con, int numeroSolicitud) throws BDException;


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
	public void guardarIngresoSalidaPaciente(Connection con, IngresoSalidaPacienteDto ingresoSalidaPacienteDto, UsuarioBasico usuarioModifica) throws BDException;

	/**
	 * Metodo que consulta las notas aclaratorias relacionadas con un Informe Qx por Especialidad.
	 * @param con
	 * @param codigoInformeQxEspecialidad
	 * @return List<NotaAclaratoriaDto> 
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 11/07/2013
	 */
	List<NotaAclaratoriaDto> consultarNotasAclaratorias(Connection con, int codigoInformeQxEspecialidad, boolean esAscendente) throws BDException;

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
	public void guardarNotaAclaratotia(Connection con, NotaAclaratoriaDto notaAclaratoriaDto, UsuarioBasico usuarioModifica) throws BDException;
	
	/**
	 * Metodo que consulta los estados de facturacion y liquidacion de los cargos asociados a una solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * @throws BDException
	 * @autor Oscar Pulido
	 * @created 01/01/2013
	 */
	List<CargoSolicitudDto> consultarEstadoCargosSolicitud(Connection con, int numeroSolicitud)  throws BDException;
	
	/**
	 * Metodo que consulta la programacion de cirugia, si existe, para una peticion dada
	 * @param con
	 * @param codigoPeticion
	 * @return
	 * @throws BDException
	 * @autor Oscar Pulido
	 * @created 23/07/2013
	 */
	ProgramacionPeticionQxDto consultarProgramacionPeticionQx(Connection con, int codigoPeticion) throws BDException;
	
	/**
	 * Consulta el cirujano que participa en la peticion de la cirugia y la especialidad que se asigna
	 * 
	 * @param connection
	 * @param codigoPeticion
	 * @return cirujano y especialidad
	 * @throws BDException
	 * @author jeilones
	 * @created 16/07/2013
	 */
	ProfesionalHQxDto consultarCirujanoPeticionCx(Connection connection,int codigoPeticion) throws BDException;

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
	void cambiarEstadoSolicitud(Connection  connection,int codigoSolicitud, int estado,UsuarioBasico medicoInterpreta) throws BDException;
	
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
	void cambiarEstadoPeticion(Connection  connection,int codigoPeticion, int estado) throws BDException;

	/**
	 * @param acronimoDiagnostico
	 * @param tipoCieDiagnosticoInt
	 * @return
	 * @throws BDException 
	 */
	public String getNombreDiagnostico(Connection con, String acronimoDiagnostico, Integer tipoCieDiagnosticoInt) throws BDException;

	/*************************************
	 *    FIN MT 6497 Usabilidad HQx     *
	 *************************************/
	
	/******************************************************************************************************************************************/
	/*--------------------------------------------------------------------------------------------------------------------------------------
	 * FIN METODOS DE LA NUEVA HOJA QUIRURGICA
	 --------------------------------------------------------------------------------------------------------------------------------------*/
	/******************************************************************************************************************************************/
	
	
	

}
