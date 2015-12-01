/*
 * @(#)ReservarCamaAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */

package com.princetonsa.action.manejoPaciente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.RespuestaInsercionPersona;
import util.RespuestaValidacion;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.ConstantesBDManejoPaciente;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.manejoPaciente.ReservarCamaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.Cama;
import com.princetonsa.mundo.Paciente;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;



public class ReservarCamaAction extends Action 
{
	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(ReservarCamaAction.class);
    
    /**
	 * Metodo execute del Action
	 */
	public ActionForward execute(   ActionMapping mapping,
	        						ActionForm form,
	        						HttpServletRequest request,
	        						HttpServletResponse response ) throws Exception
	        						{	   
		Connection con=null;
		try{
			if (response==null);
			if(form instanceof ReservarCamaForm)
			{

				con = UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico"); 
				ReservarCamaForm forma =(ReservarCamaForm)form;
				String estado=forma.getEstado();
				logger.warn("\n\n\nEl estado en Reservar Cama es------->"+estado+"\n");

				if(estado == null)
				{
					forma.reset(usuario.getCodigoCentroAtencion()); 
					logger.warn("Estado no valido dentro del flujo de Reservar Cama (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}	       


				//Inicia la funcionalidad
				else if(estado.equals("empezar"))
				{					
					return this.accionEmpezar(forma, mapping, con, usuario);
				}	       
				//Valida los datos capturados por el paciente
				else if (estado.equals("validarPaciente"))
				{
					return this.accionValidarPaciente(forma, mapping, con, usuario, request);
				}
				//guarda la informacion de la Reserva   
				else if(estado.equals("guardar"))
				{
					return this.accionGuardar(con, forma, usuario, mapping,  request);

				}      
				//estado del cambio de Centro de Atencion
				else if(estado.equals("detalle"))
				{	        	
					return this.accionDetalle(forma, mapping, con);    			
				}
				/*-------------------------------------------------------------------
				 * Adicionado Por Jhony Alexander Duque A.
				 * Estado adicionado para el manejo de llamadas a esta funcionalidad.
				 * ====================---LLAMADORESERVARCAMA---=====================
	         -------------------------------------------------------------------*/
				else if (estado.equals("llamadoReservarCama"))
				{
					return this.accionFormatearReserva(con, forma, mapping, request, usuario);
				}
			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	 }
	  
	
	
	//------------------------- METODOS----------------------------------	
	
	
	
	/*------------------------------------------------------------------
	 * Adicionado Por  Jhony Alexander Duque A.
	 * Metodo encargado de formatear los valores para la reserva de 
	 * camas.
	 * Este metodo es utilizado cuando la reserva de camas es llada 
	 * desde otra funcionalidad; para esto se apoya en un parametro 
	 * que indica de donde ha sido llamada, este es "origenLlamado"
	 ------------------------------------------------------------------*/
	private ActionForward accionFormatearReserva (Connection connection,ReservarCamaForm forma,ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario)
	{
		ActionForward retorno = new ActionForward();
		ActionErrors errores = new ActionErrors();
		saveErrors(request, errores);
		errores.add("",new ActionMessage("Error llamando Reserva de Camas"));
	
		if (request.getParameter("origenLlamado")!=null)
			forma.setOrigenLlamado(request.getParameter("origenLlamado"));
		else
			forma.setOrigenLlamado("");
		
		if (forma.getOrigenLlamado().equals("censoCamas"))
			retorno =this.accionValidarPaciente(forma, mapping, connection, usuario, request);
		else
			if (forma.getOrigenLlamado().equals("censoCamas2"))
			{
				retorno = this.accionEmpezarReserva(forma, mapping, connection, usuario,request);
			}
			else
				if (forma.getOrigenLlamado().equals(""))
					retorno =  mapping.findForward("paginaErroresActionErrorsSinCabezote");
			
			
		return retorno;
	}
	
	
	
	/**
	 * Metodo encargado de Consutlar Los Datos de la cama
	 * Que se muestran en la jsp de reservacama
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	private void consultaDatosCama (Connection connection,ReservarCamaForm forma,UsuarioBasico usuario)
	{
		HashMap parametros = new HashMap();
		//logger.info("\n\n ****** entro  a la consulta de los datos de las camas");
		parametros.put("codigoCama", forma.getReservarMap("codigoCama"));
		parametros.put("institucion",usuario.getCodigoInstitucionInt());
	//	logger.info("\n **el valor de los parametros es "+parametros);
		forma.setReservarMap(UtilidadesManejoPaciente.obtenerDatosCama(connection, parametros));
		forma.setReservarMap("estabd",ConstantesBD.acronimoNo);
		forma.setReservarMap("fechaOcupacion", UtilidadFecha.getFechaActual());
		forma.setCentroAtencion(Integer.parseInt(forma.getReservarMap("centroAtencion")+""));
		//logger.info("\n **** al buscar la cama da como resultado "+forma.getReservarMap());
		
		
		
	}
	
	/**
	 * Método que inicia el flujo de la reserva de camas postulando el ingreso del tipo y numero de identificacion del paciente
	 * @param usuario 
	 */
	private ActionForward accionEmpezarReserva (ReservarCamaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario,HttpServletRequest request) 
	{
		forma.reset(usuario.getCodigoCentroAtencion());
		
		//logger.info("\n\n *** el valor del codigo de cama "+request.getParameter("codigoCama"));
		
		if (request.getParameter("codigoCama")!=null)
			forma.setReservarMap("codigoCama", request.getParameter("codigoCama"));
		if (request.getParameter("origenLlamado")!=null)
			forma.setOrigenLlamado(request.getParameter("origenLlamado"));
		
		//logger.info("el valor de codigo de la cama "+forma.getReservarMap("codigoCama"));
		//Se consultan los tipos de identificación
		forma.setTiposIdentificacion(Utilidades.obtenerTiposIdentificacion(con, "ingresoPaciente", usuario.getCodigoInstitucionInt()));
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ingresarPaciente");
	}
	
	
	/**
	 * Método que inicia el flujo de la reserva de camas postulando el ingreso del tipo y numero de identificacion del paciente
	 * @param usuario 
	 */
	private ActionForward accionEmpezar(ReservarCamaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoCentroAtencion());
		
		//Se consultan los tipos de identificación
		forma.setTiposIdentificacion(Utilidades.obtenerTiposIdentificacion(con, "ingresoPaciente", usuario.getCodigoInstitucionInt()));
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ingresarPaciente");
	}



	/**
	 * Carga la informacion de Reserva de Camas
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionValidarPaciente(ReservarCamaForm forma,
										ActionMapping mapping, 
										Connection con, 
										UsuarioBasico usuario, 
										HttpServletRequest request)
	{
		ActionErrors errores = new ActionErrors();
		String[] datosTipoId = new String[0];
		String nuevoNumeroId = "";
		
		//***************VALIDACIONES DE LOS CAMPOS**************************************
		if(forma.getCodigoTipoIdentificacion().equals(""))
		{
			errores.add("",new ActionMessage("errors.required","El tipo de identificación"));
			
			if(forma.getNumeroIdentificacion().trim().equals(""))
				errores.add("",new ActionMessage("errors.required","El número de identificación"));
		}
		else
		{
			datosTipoId = forma.getCodigoTipoIdentificacion().split(ConstantesBD.separadorSplit);
			
			//Se verifica si el tipo de identificacion es consecutivo para saber si se debe validar el número de identificacion
			if(!UtilidadTexto.getBoolean(datosTipoId[1]))
			{
				if(forma.getNumeroIdentificacion().trim().equals(""))
					errores.add("",new ActionMessage("errors.required","El número de identificación"));
			}
			
			forma.setCodigoTipoIdentificacion(datosTipoId[0]);
			forma.setNombreTipoIdentificacion(datosTipoId[2]);
		}
		
		//Si se ingresó número de identificación se verifica que tenga letras, número o guión
		nuevoNumeroId = forma.getNumeroIdentificacion().trim();
		if(UtilidadCadena.tieneCaracteresEspecialesNumeroId(nuevoNumeroId))
		{
			errores.add("",new ActionMessage("errores.paciente.caracteresInvalidos",nuevoNumeroId));
		}
		else
			forma.setNumeroIdentificacion(nuevoNumeroId);
		
		if(!errores.isEmpty())
		{
			if(datosTipoId.length>1)
				forma.setCodigoTipoIdentificacion(datosTipoId[0]+ConstantesBD.separadorSplit+datosTipoId[1]+ConstantesBD.separadorSplit+datosTipoId[2]);
			
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("ingresarPaciente");
		}
		
		//*******************************************************************************
		
		
		//inicializa datos
		forma.inicializarTags(usuario.getCodigoInstitucionInt());
		
		//verifica si existe reserva de camas		
		existeReservaCama(con, usuario, forma);
		
		//carga la informacion de la reserva segun el centro de atencion actual 
		
		if (forma.getOrigenLlamado().equals("censoCamas2"))
			this.consultaDatosCama(con, forma, usuario);
		else
		cargarCamaExistente(con,usuario.getCodigoCentroAtencion()+"",forma);
		
		
		//************SI EL PACIENTE NO EXISTE SE REALIZAN OTRAS VALIDACIONES***************
		if(forma.getCodigoPaciente()<=0)
		{
			//****************************VALIDACION DEL CONSECUTIVO DE HISTORIA CLINICA************************************
			errores = validacionConsecutivoHistoriaClinica(con, forma, usuario, errores);
			
			//*****************************VALIDACION DE TIPO DE IDENTIFICACION AUTOMATICA****************************************
			//Si el paciente no existe se verifica si tiene tipo id automático para asignarle el numero de la secuencia
			if(UtilidadTexto.getBoolean(datosTipoId[1]))
			{
				try 
				{
					forma.setNumeroIdentificacion((DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con, "seq_personas_sin_id")+1)+"");
					
					boolean existePaciente = UtilidadValidacion.existePaciente(con, forma.getCodigoTipoIdentificacion(), forma.getNumeroIdentificacion());
					
					//Si ya existe un paciente con ese consecutivo, se debe continuar con la siguiente secuencia, hasta que haya una disponible
					while(existePaciente)
					{
						DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).incrementarValorSecuencia(con, "seq_personas_sin_id");
						forma.setNumeroIdentificacion((DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con, "seq_personas_sin_id")+1)+"");
						existePaciente = UtilidadValidacion.existePaciente(con, forma.getCodigoTipoIdentificacion(), forma.getNumeroIdentificacion());
					}
					
				} 
				catch (SQLException e) 
				{
					logger.error("Error al obtener AL REALIZAR LA VALIDACION DEL NUMERO DE IDENTIFICACION AUTOMATICA: "+e);
				}
				
				
				forma.setIdentificacionAutomatica(true);
			}
			else
				forma.setIdentificacionAutomatica(false);
			//****************************************************************************************************************
			
			if(!errores.isEmpty())
			{
				if(datosTipoId.length>1)
					forma.setCodigoTipoIdentificacion(datosTipoId[0]+ConstantesBD.separadorSplit+datosTipoId[1]+ConstantesBD.separadorSplit+datosTipoId[2]);
				
				saveErrors(request, errores);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("ingresarPaciente");
				
			}
		}
		//******************SI EL PACIENTE YA EXISTE ENTONCES SE CARGA SU INFORMACION*********************
		else
		{
			logger.info("el paciente existe!!!");
			
			//Se carg paciente en sesión
			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			paciente.setCodigoPersona(forma.getCodigoPaciente());
			UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
			
			/**
			 * Se verifica que si el paciente tiene ingreso abierto en hospitalizacion o urgencias y es de un centro de atencion
			 * diferente al centro de atencion del usuario, entonces no se puede continuar con el proceso
			 */
			int codigoCentroAtencion = UtilidadesManejoPaciente.getCentroAtencionCuentaIngresoAbiertoPaciente(con, forma.getCodigoPaciente(), ConstantesBD.codigoViaIngresoUrgencias+","+ConstantesBD.codigoViaIngresoHospitalizacion);
			
			//El centro de atencion del paciente debe ser igual al el usuario
			if(codigoCentroAtencion>0&&codigoCentroAtencion!=usuario.getCodigoCentroAtencion())
				errores.add("",new ActionMessage("error.cama.validacionCentroAtencionReserva",forma.getCodigoTipoIdentificacion()+" "+forma.getNumeroIdentificacion(),Utilidades.obtenerNombreCentroAtencion(con,codigoCentroAtencion)));
			//Se verifica si es hospital día
			else if(paciente.isHospitalDia())
				errores.add("",new ActionMessage("errores.paciente.ingresoHospitalDia"));
			
			
			//Se verifica si hubo errores
			if(!errores.isEmpty())
			{
				if(datosTipoId.length>1)
					forma.setCodigoTipoIdentificacion(datosTipoId[0]+ConstantesBD.separadorSplit+datosTipoId[1]+ConstantesBD.separadorSplit+datosTipoId[2]);
				
				saveErrors(request, errores);
				UtilidadBD.closeConnection(con);
				if(request.getServletPath().startsWith("/reservarCamaDummy"))
					return mapping.findForward("paginaErroresActionErrorsSinCabezote");
				else
					return mapping.findForward("ingresarPaciente");
			}
			
			HashMap datosPersona = Utilidades.obtenerDatosPersona(con, forma.getCodigoTipoIdentificacion(), forma.getNumeroIdentificacion());
			
			forma.setPrimerApellido(datosPersona.get("primer_apellido_0").toString());
			forma.setSegundoApellido(datosPersona.get("segundo_apellido_0").toString());
			forma.setPrimerNombre(datosPersona.get("primer_nombre_0").toString());
			forma.setSegundoNombre(datosPersona.get("segundo_nombre_0").toString());
			forma.setCodigoSexo(datosPersona.get("codigo_sexo_0").toString());
			forma.setFechaNacimiento(datosPersona.get("fecha_nacimiento_0").toString());
			forma.setTelefono(datosPersona.get("telefono_0").toString());
		}
		//***********************************************************************************************
		
		//Se cargan los sexos
		forma.setSexos(Utilidades.obtenerSexos(con));
		
		//Se verifica que si no hay centro de atencion entonces se postula el del usuario en sesión
		if(forma.getCentroAtencion()<=0)
			forma.setCentroAtencion(usuario.getCodigoCentroAtencion());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Método implementado para efectuar las validaciones del consecutivo disponible de historia clinica
	 * @param con
	 * @param forma
	 * @param usuarioActual
	 * @param errores 
	 * @return
	 */
	private ActionErrors validacionConsecutivoHistoriaClinica(Connection con, ReservarCamaForm forma, UsuarioBasico usuarioActual, ActionErrors errores) 
	{
		
		String consecutivo=UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoHistoriaClinica, usuarioActual.getCodigoInstitucionInt());
		
		
		if(!UtilidadCadena.noEsVacio(consecutivo) || consecutivo.equals("-1"))
			errores.add("Falta consecutivo disponible",new ActionMessage("error.paciente.faltaDefinirConsecutivo","la historia clínica"));
		
		return errores;
	}
	
	
	/**
	 * Método alamace la informacion de la reserva si existe 
	 * @param con
	 * @param usuario
	 * @param forma
	 * @return
	 */
	private boolean existeReservaCama(Connection con,
									  UsuarioBasico usuario,
									  ReservarCamaForm forma) 
	{
		
		String[] datosTipoId = forma.getCodigoTipoIdentificacion().split(ConstantesBD.separadorSplit);
		
		try 
		{
			forma.setCodigoPaciente(UtilidadValidacion.getCodigoPersona(con, datosTipoId[0], forma.getNumeroIdentificacion()));
		} 
		catch (SQLException e) 
		{
			logger.info("Error al tratar de consultar el codigo del paciente: "+e);
		}
		
		
		if(forma.getCodigoPaciente()>0)
			//consulta la informacion de la reserva activa
			forma.setReservaActivaMap(UtilidadesManejoPaciente.consultarReservaCama(con,forma.getCodigoPaciente()+"",usuario.getCodigoInstitucionInt()));
		else
			forma.setReservaActivaMap("numRegistros", "0");
		
		logger.info("MAPA DE LA RESERVA ACTIVA=> "+forma.getReservaActivaMap());
		
		if(Integer.parseInt(forma.getReservaActivaMap("numRegistros").toString())>0)
		{
			forma.setReservaActivaMap("existeReservaActiva",ConstantesBD.acronimoSi);
			forma.setObservaciones(forma.getReservaActivaMap("observaciones").toString());
			return true;
		}
		else		
			forma.setReservaActivaMap("existeReservaActiva",ConstantesBD.acronimoNo);
		
		return false;
	}
		
	
	/**
	 * Método para cargar la informacion de la cama reservada del paciente si existe
	 * @param con
	 * @param usuario
	 * @param forma
	 * @return
	 */
	private void cargarCamaExistente(Connection con, 
									 String codigoCentroAtencion, 
									 ReservarCamaForm forma)
	{			
		if(forma.getCodigoPaciente()>0)
			forma.setReservarMap(UtilidadesManejoPaciente.consultarCamaReservada(con,forma.getCodigoPaciente()+"",codigoCentroAtencion));
		else
			forma.setReservarMap("numRegistros", "0");
		
		if(Integer.parseInt(forma.getReservarMap("numRegistros").toString())>0)
		{
			forma.setReservarMap("estabd",ConstantesBD.acronimoSi);
			forma.setReservarMap("codigoCama",forma.getReservarMap("codigo"));
		}
		else
			forma.setReservarMap("estabd",ConstantesBD.acronimoNo);		
	}	
	
	
	
	/**
	 * Carga la informacion de la reserva por el cambio de centro de Atencion 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionDetalle(ReservarCamaForm forma,
										ActionMapping mapping,
										Connection con) 
	{
		
		cargarCamaExistente(con, forma.getCentroAtencion()+"",forma);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
		
	
	
	/**
	 * Metodo Guarda / Modificar la Reserva de la Cama
	 * @param request 
     * @param Connection con
     * @param ReservarCamaForm forma
     * @param UsuarioBasico usuario
     * @param ActionMapping mapping
     * @param PersonaBasica paciente
     */
    private ActionForward accionGuardar(Connection con,
    						   ReservarCamaForm forma,
    						   UsuarioBasico usuario,
    						   ActionMapping mapping,
    						   HttpServletRequest request)
    {
    	int consecutivo = 0, resp1 = 0, resp0= 0, resp2 = 0,resp3 = 0;  
    	    	
		
    	//*********SE REALIZA INGRESO / MODIFICACION DEL PACIENTE*********************************
    	Paciente mundoPaciente = new Paciente();
    	Paciente pacienteAnterior = new Paciente();
    	
    	if(forma.getCodigoPaciente()>0)
    	{
    		try 
    		{
				mundoPaciente.cargarPaciente(con, forma.getCodigoPaciente());
				
				//Si había paciente se carga de nuevo cama registro de paciente anterior
				if(mundoPaciente.getCodigoPersona()>0)
					pacienteAnterior.cargarPaciente(con, forma.getCodigoPaciente());
			} 
    		catch (SQLException e) 
    		{
				logger.info("Error al tratar de cargar el paciente: "+e);
			}
    	}
    	
    	
    	//Se verifica si el paciente existe
    	if(mundoPaciente.getCodigoPersona()>0)
    	{
    		//***************MODIFICACION***************************************
    		
    		
    		mundoPaciente.setPrimerApellidoPersona(forma.getPrimerApellido());
    		mundoPaciente.setSegundoApellidoPersona(forma.getSegundoApellido());
    		mundoPaciente.setPrimerNombrePersona(forma.getPrimerNombre());
    		mundoPaciente.setSegundoNombrePersona(forma.getSegundoNombre());
    		mundoPaciente.setCodigoSexo(forma.getCodigoSexo());
    		String[] datosFecha = forma.getFechaNacimiento().split("/");
    		mundoPaciente.setAnioNacimiento(datosFecha[2]);
    		mundoPaciente.setMesNacimiento(datosFecha[1]);
    		mundoPaciente.setDiaNacimiento(datosFecha[0]);
    		mundoPaciente.setTelefono(forma.getTelefono());
    		
    		try 
    		{
				resp2 = mundoPaciente.modificarPaciente(con, usuario, pacienteAnterior);
			} catch (SQLException e) 
			{
				logger.error("Error al modificar el paciente: "+e);
				resp2 = 0;
			}
    		//*******************************************************************
    	}
    	else
    	{
    		//***************INSERCION*****************************************
    		//Se verifica de nuevo el consecutivo de historia clincia-------------------------------
    		ActionErrors errores = validacionConsecutivoHistoriaClinica(con, forma, usuario, new ActionErrors());
    		
    		if(!errores.isEmpty())
    		{
    			saveErrors(request, errores);
    			UtilidadBD.abortarTransaccion(con);
    			UtilidadBD.closeConnection(con);
    	    	return mapping.findForward("principal");
    		}
    		//-------------------------------------------------------------------------------
    		
    		UtilidadBD.iniciarTransaccion(con);
    		//Si existe como persona se carga su informacion
    		if(forma.getCodigoPaciente()>0)
    		{
    			HashMap datosPersona = Utilidades.obtenerDatosPersona(con, forma.getCodigoTipoIdentificacion(), forma.getNumeroIdentificacion());
    			
    			mundoPaciente.setCodigoPaisIdentificacion(datosPersona.get("codigo_pais_nacimiento_0").toString());
    			mundoPaciente.setCodigoCiudadIdentificacion(datosPersona.get("codigo_ciudad_nacimiento_0").toString());
    			mundoPaciente.setCodigoDepartamentoIdentificacion(datosPersona.get("codigo_depto_nacimiento_0").toString());
    			mundoPaciente.setCodigoEstadoCivil(datosPersona.get("codigo_estado_civil_0").toString());
    			mundoPaciente.setDireccion(datosPersona.get("direccion_0").toString());
    			mundoPaciente.setCodigoPais(datosPersona.get("codigo_pais_vivienda_0").toString());
    			mundoPaciente.setCodigoDepartamento(datosPersona.get("codigo_depto_vivienda_0").toString());
    			mundoPaciente.setCodigoCiudad(datosPersona.get("codigo_ciudad_vivienda_0").toString());
    			mundoPaciente.setCodigoBarrio(datosPersona.get("codigo_barrio_0").toString());
    			mundoPaciente.setEmail(datosPersona.get("email_0").toString());
    			mundoPaciente.setCodigoPaisId(datosPersona.get("codigo_pais_id_0").toString());
    			mundoPaciente.setCodigoCiudadId(datosPersona.get("codigo_ciudad_id_0").toString());
    			mundoPaciente.setCodigoDepartamentoId(datosPersona.get("codigo_depto_id_0").toString());
    		}
    		else
    		{
    			//**************SE POSTULA LA INFORMACION POR DEFECTO***********************************
				//Consulta del parametro país nacimiento
				String paisNacimiento = ValoresPorDefecto.getPaisNacimiento(usuario.getCodigoInstitucionInt());
				String ciudadNacimiento = ValoresPorDefecto.getCiudadNacimiento(usuario.getCodigoInstitucionInt());
				if(UtilidadCadena.noEsVacio(paisNacimiento)&&!paisNacimiento.equals(" - "))
				{
					mundoPaciente.setCodigoPaisIdentificacion(paisNacimiento.split("-")[0]);
					mundoPaciente.setCodigoPaisId(paisNacimiento.split("-")[0]);
					if(UtilidadCadena.noEsVacio(ciudadNacimiento)&&!ciudadNacimiento.equals(" - - "))
					{
						mundoPaciente.setCodigoCiudadIdentificacion(ciudadNacimiento.split("-")[1]);
						mundoPaciente.setCodigoCiudadId(ciudadNacimiento.split("-")[1]);
						mundoPaciente.setCodigoDepartamentoIdentificacion(ciudadNacimiento.split("-")[0]);
						mundoPaciente.setCodigoDepartamentoId(ciudadNacimiento.split("-")[0]);
					}
				}
				if(mundoPaciente.getCodigoPaisIdentificacion().equals(""))
					errores.add("", new ActionMessage("error.parametrosGenerales.faltaDefinirParametro","País nacimiento"));
				if(mundoPaciente.getCodigoCiudadIdentificacion().equals(""))
					errores.add("", new ActionMessage("error.parametrosGenerales.faltaDefinirParametro","Ciudad nacimiento"));
		
				//Consulta del parametro país residencia
				String paisResidencia = ValoresPorDefecto.getPaisResidencia(usuario.getCodigoInstitucionInt());
				String ciudadResidencia = ValoresPorDefecto.getCiudadVivienda(usuario.getCodigoInstitucionInt());
				if(UtilidadCadena.noEsVacio(paisResidencia)&&!paisResidencia.equals(" - "))
				{
					mundoPaciente.setCodigoPais(paisResidencia.split("-")[0]);
		
					if(UtilidadCadena.noEsVacio(ciudadResidencia)&&!ciudadResidencia.equals(" - - "))
					{
						mundoPaciente.setCodigoCiudad(ciudadResidencia.split("-")[1]);
						mundoPaciente.setCodigoDepartamento(ciudadResidencia.split("-")[0]);
					}
				}
				if(mundoPaciente.getCodigoPais().equals(""))
					errores.add("", new ActionMessage("error.parametrosGenerales.faltaDefinirParametro","País residencia"));
				if(mundoPaciente.getCodigoCiudad().equals(""))
					errores.add("", new ActionMessage("error.parametrosGenerales.faltaDefinirParametro","Ciudad residencia"));
		
				//Consulta del barrio
				String barrio = ValoresPorDefecto.getBarrioResidencia(usuario.getCodigoInstitucionInt());
				if(UtilidadCadena.noEsVacio(barrio))
					mundoPaciente.setCodigoBarrio(barrio.split("-")[2]);
		
				//Se toma los valores de parametros generales
				/**ingresoPacienteForm.setDireccion(ValoresPorDefecto.getDireccionPaciente(usuarioActual.getCodigoInstitucionInt())==null?"":ValoresPorDefecto.getDireccionPaciente(usuarioActual.getCodigoInstitucionInt()));
				ingresoPacienteForm.setZonaDomicilio(ValoresPorDefecto.getZonaDomicilio(usuarioActual.getCodigoInstitucionInt())==null?"":ValoresPorDefecto.getZonaDomicilio(usuarioActual.getCodigoInstitucionInt()));
				ingresoPacienteForm.setOcupacion(ValoresPorDefecto.getOcupacion(usuarioActual.getCodigoInstitucionInt())==null?"":ValoresPorDefecto.getOcupacion(usuarioActual.getCodigoInstitucionInt()));
				ingresoPacienteForm.setLeeEscribe(ValoresPorDefecto.getValorTrueParaConsultas());**/
    			
    			
    			mundoPaciente.setCodigoEstadoCivil(ConstantesBD.acronimoEstadoCivilDesconocido);
    		}
    		
    		mundoPaciente.setCodigoTipoPersona("1"); //natural
			mundoPaciente.setCodigoTipoSangre(ConstantesBDManejoPaciente.codigoTipoSangreDesconocido);
			mundoPaciente.setCodigoGrupoPoblacional(ConstantesIntegridadDominio.acronimoOtrosGruposPoblacionales);
			mundoPaciente.setCodigoOcupacion(ConstantesBD.codigoNuncaValido+"");
			
    		
    		mundoPaciente.setNumeroHistoriaClinica(forma.getNumeroHistoriaClinica());
    		mundoPaciente.setAnioHistoriaClinica(forma.getAnioHistoriaClinica());
    		mundoPaciente.setCodigoTipoIdentificacion(forma.getCodigoTipoIdentificacion());
    		mundoPaciente.setNumeroIdentificacion(forma.getNumeroIdentificacion());
    		mundoPaciente.setPrimerApellidoPersona(forma.getPrimerApellido());
    		mundoPaciente.setSegundoApellidoPersona(forma.getSegundoApellido());
    		mundoPaciente.setPrimerNombrePersona(forma.getPrimerNombre());
    		mundoPaciente.setSegundoNombrePersona(forma.getSegundoNombre());
    		mundoPaciente.setCodigoSexo(forma.getCodigoSexo());
    		String[] datosFecha = forma.getFechaNacimiento().split("/");
    		mundoPaciente.setAnioNacimiento(datosFecha[2]);
    		mundoPaciente.setMesNacimiento(datosFecha[1]);
    		mundoPaciente.setDiaNacimiento(datosFecha[0]);
    		mundoPaciente.setTelefono(forma.getTelefono());
    		
    		RespuestaValidacion respuesta = new RespuestaValidacion("No se pudo asociar al paciente con la institucion",false);
    		//Si no hubo errores se prosigue a isnertar el paciente
    		if(errores.isEmpty())
    		{
	    		try 
	    		{
					RespuestaInsercionPersona resultado =  mundoPaciente.insertarPaciente(con, usuario.getCodigoInstitucionInt());
					if(resultado.isSalioBien())
					{
						resp2 = 1;
						forma.setCodigoPaciente(resultado.getCodigoPersona());
					}
				} 
	    		catch (SQLException e) 
	    		{
					logger.error("Error al tratar de ingresar el paciente: "+e);
					resp2 = 0;
				}
	
	    		//se asocia el paciente en la institucion
				try 
				{
					respuesta = UtilidadValidacion.validacionPermisosInstitucionPaciente2
					 (
					         con,
					         forma.getCodigoPaciente(),
					         usuario.getCodigoInstitucion(),
					         usuario.getCodigoInstitucion()
					 );
				}
				catch (SQLException e) 
				{
					logger.error("Error en el método validacionPermisosInstitucionPaciente2: "+e);
				}
    		}
    		else
    			resp2 = 0;
			
			if(resp2>0&&respuesta.puedoSeguir)
				UtilidadBD.finalizarTransaccion(con);
			else
			{
				if(errores.isEmpty())
					errores.add("",new ActionMessage("errors.problemasGenericos","al ingresar el paciente"));
				saveErrors(request, errores);
    			UtilidadBD.abortarTransaccion(con);
    			UtilidadBD.closeConnection(con);
    	    	return mapping.findForward("principal");
			}
    		
    		//*******************************************************************
    	}
    	
    	//****************************************************************************************
    	
    	ActionErrors errores = new ActionErrors();
    	UtilidadBD.iniciarTransaccion(con);
		//Si el proceso de ingresar/modificar paciente fue exitoso entonces se prosigue a la reserva de cama
    	if(resp2>0)
    	{
    		//*******************************REGISTRO DE LA RESERVA DE CAMA*****************************************
    		// Verifica si ya existe la informacion de reserva   
			if(forma.getReservaActivaMap("existeReservaActiva").toString().equals(ConstantesBD.acronimoSi))
			{			
				if(!forma.getReservaActivaMap("codigocama").toString().equals(forma.getReservarMap("codigoCama").toString())||
					!forma.getReservaActivaMap("fechaocupacion").toString().equals(forma.getReservarMap("fechaOcupacion").toString())||
					!forma.getReservaActivaMap("observaciones").toString().equals(forma.getObservaciones()))
				{
					boolean resp = UtilidadesManejoPaciente.modificarReservarCamas(
						con,
						forma.getReservarMap("codigoReserva")!=null?forma.getReservarMap("codigoReserva").toString():forma.getReservaActivaMap("codigo").toString(),				 
						forma.getCodigoPaciente()+"",
						forma.getReservarMap("codigoCama").toString(),
						ConstantesIntegridadDominio.acronimoEstadoActivo,
						forma.getReservarMap("fechaOcupacion").toString(),				 
						usuario.getLoginUsuario(), 
						forma.getPrimerNombre()+" "+forma.getSegundoNombre()+" "+forma.getPrimerApellido()+" "+forma.getSegundoApellido(), 
						forma.getCodigoTipoIdentificacion(), 
						forma.getNumeroIdentificacion(), 
						forma.getCentroAtencion()+"", 
						Utilidades.obtenerNombreCentroAtencion(con, forma.getCentroAtencion()), 
						forma.getReservarMap("codigoCama").toString(),				 
						usuario.getCodigoCentroAtencion()+"", 
						usuario.getCentroAtencion(),
						forma.getObservaciones());
					
					if(resp)
						consecutivo = 1;
				}
				else
					consecutivo = 1;
			}
			else
			{
				consecutivo = UtilidadesManejoPaciente.insertarReservarCamas(con,
						forma.getCodigoPaciente()+"",
						forma.getReservarMap("codigoCama").toString(),					
						forma.getReservarMap("fechaOcupacion").toString(),
						usuario.getCodigoInstitucion(),
						usuario.getLoginUsuario(),
						forma.getPrimerNombre()+" "+forma.getSegundoNombre()+" "+forma.getPrimerApellido()+" "+forma.getSegundoApellido(), 
						forma.getCodigoTipoIdentificacion(), 
						forma.getNumeroIdentificacion(),
						forma.getCentroAtencion()+"",
						Utilidades.obtenerNombreCentroAtencion(con, forma.getCentroAtencion()),
						forma.getReservarMap("codigoCama").toString(),
						usuario.getCodigoCentroAtencion()+"",
						usuario.getCentroAtencion(),
						forma.getObservaciones());
			}
			//***********************************************************************************************
			
			if(consecutivo>0)
			{
				//***********************CAMBIAR ESTADO DE LA CAMA A RESERVAR************************************************************
				Cama cama = new Cama();
				//Cambios de los estados de las camas--------------------------------------------
				try
				{							
					resp0 = cama.cambiarEstadoCama(con, forma.getReservarMap("codigoCama").toString(), ConstantesBD.codigoEstadoCamaReservada);
					
					if(forma.getReservaActivaMap("existeReservaActiva").toString().equals(ConstantesBD.acronimoSi))
						resp1 = cama.cambiarEstadoCama(con, forma.getReservaActivaMap("codigocama").toString(), ConstantesBD.codigoEstadoCamaDisponible);
					else
						resp1 = 1;
					
				}
				catch(SQLException e)
				{				
					resp1 = 0;
					logger.error("Error al actualizar los estados de las camas");
				}
				//*************************************************************************************************
				
				
				if(resp1>0&&resp0>0)
				{
					//*************CAMBIO ESTADO CAMA ACTUAL A PENDIENTE POR TRASLADAR******************************+
					//Este proceso solo aplica para pacientes que tienen cuenta de hospitalizacion en el mismo centro de atencion
					//************************************************************************************************
					//Se consulta el id de la cuenta del posible ingreso abierto de hispitalizacion del paciente
					String idCuenta = UtilidadesManejoPaciente.getIdCuentaIngresoAbiertoPaciente(con, forma.getCodigoPaciente(), ConstantesBD.codigoViaIngresoHospitalizacion, usuario.getCodigoCentroAtencion());
					
					if(!idCuenta.equals(""))
					{
						///Se consulta el codigo de la cama del ultimo traslado de camas
						int codigoCama = Utilidades.getUltimaCamaTraslado(con, Integer.parseInt(idCuenta));
						
						Cama mundoCama = new Cama();
						try 
						{
							int codigoEstadoCamaActual = Utilidades.obtenerCodigoEstadoCama(con, codigoCama);
							
							//Si cambió el estado de la cama ocupada entonces se actualiza la informacion
							if(codigoEstadoCamaActual!=ConstantesBD.codigoEstadoCamaPendientePorTrasladar)
							{
							
								resp3 = mundoCama.cambiarEstadoCama(con, codigoCama+"", ConstantesBD.codigoEstadoCamaPendientePorTrasladar);
								
								
								//si el cambio de la cama fue exitoso se procede a insertar el log
								if(resp3>0)
								{
									//Insercion de log Estados Cama
									resp3 = UtilidadesManejoPaciente.insertarLogCambioEstadoCama(
										con, 
										codigoCama, 
										codigoEstadoCamaActual, 
										ConstantesBD.codigoEstadoCamaPendientePorTrasladar, 
										UtilidadFecha.getFechaActual(con), 
										UtilidadFecha.getHoraActual(con), 
										usuario.getLoginUsuario(), 
										idCuenta, 
										usuario.getCodigoInstitucionInt(), 
										forma.getCentroAtencion());
								}
							}
							else
								resp3 = 1;
						} 
						catch (SQLException e) 
						{
							logger.error("Error al realizar la actualizacion de la cama: "+e);
							resp3 = 0;
						}
					}
					else
						resp3 = 1;
					
					if(resp3<=0)
						errores.add("",new ActionMessage("errors.problemasGenericos","al cambiar el estado de la cama actual a pendiente por trasladar"));
					
					//************************************************************************************************************
					//*************************************************************************************************************
				}
				else
					errores.add("",new ActionMessage("errors.problemasGenericos","al cambiar el estado de la cama a reservada"));
				
			}
			else
				errores.add("",new ActionMessage("errors.problemasGenericos","al registrar la reserva de la cama"));
			
    	}
    	else
    		errores.add("",new ActionMessage("errors.problemasGenericos","al tratar de ingresar/modificar los datos del paciente"));
		
		
		
		
		
		
		
    	if(errores.isEmpty())
		{
    		forma.setEstado("resumen");
			forma.setMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			
			//Se carga el paciente en sesión
			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			paciente.setCodigoPersona(forma.getCodigoPaciente());
			UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
			
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			
			saveErrors(request, errores);
			UtilidadBD.abortarTransaccion(con);
		}	
    	
    	existeReservaCama(con, usuario, forma);
    	cargarCamaExistente(con,forma.getCentroAtencion()+"",forma);
    	UtilidadBD.closeConnection(con);
    	return mapping.findForward("principal");
    }	
}