/*
 * Nov 14, 2006
 */
package com.princetonsa.mundo.pyp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.pyp.ActEjecutadasXCargarDao;
import com.princetonsa.dto.historiaClinica.DtoValoracion;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;
import com.princetonsa.mundo.historiaClinica.Valoraciones;
import com.princetonsa.mundo.ordenesmedicas.OrdenesAmbulatorias;
import com.princetonsa.mundo.ordenesmedicas.procedimientos.RespuestaProcedimientos;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.solicitudes.SolicitudConsultaExterna;
import com.princetonsa.mundo.solicitudes.SolicitudProcedimiento;

/**
 * @author Sebastián Gómez 
 *
 *Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * Actividades PYP Ejecutadas por Cargar
 */
public class ActEjecutadasXCargar 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(ActEjecutadasXCargar.class);
	
	
	/**
	 * DAO para el manejo de ActEjecutadasXCargarDao
	 */
	ActEjecutadasXCargarDao actividadesDao = null;
	
	//**********ATRIBUTOS*****************************************
	/**
	 *Objeto usado para pasar parámetros al dao
	 */
	private HashMap campos = new HashMap();
	
	/**
	 * Variable que indica la generación exitosa del archivo de inconsistencias
	 */
	private boolean existeArchivo; 
	
	/**
	 * Objeto donde se alamcenan los errores en el momento de generar la solicitud x orden
	 */
	private ArrayList errores = new ArrayList();
	//************************************************************
	
	//**********INICIALIZADORES & CONSTRUCTORES***********************
	/**
	 * Constructor
	 */
	public ActEjecutadasXCargar() {
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
		this.existeArchivo = false;
		this.errores = new ArrayList();
	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (actividadesDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			actividadesDao = myFactory.getActEjecutadasXCargarDao();
		}	
	}
	//****************************************************************************
	//********************METODOS*************************************************
	/**
	 * Método implementado para cargar las ordenes ambulatorias pendientes por cargar de un convenio específico
	 * @param con
	 * @param String codigoConvenio
	 * @return
	 */
	public HashMap consultarOrdenesAmbXConvenio(Connection con,String codigoConvenio)
	{
		campos.put("codigoConvenio",codigoConvenio);
		return actividadesDao.consultarOrdenesAmbXConvenio(con,campos);
	}
	
	/**
	 * Método implementado para cargar información detallada de una orden ambulatoria
	 * @param con
	 * @param codigoOrden
	 * @return
	 */
	public HashMap consultarDetalleOrdenAmb(Connection con,String codigoOrden)
	{
		campos.put("codigoOrden",codigoOrden);
		return actividadesDao.consultarDetalleOrdenAmb(con,campos);
	}
	
	/**
	 * Método implementado para generar el archivo de inconsistencias sobre el proceso
	 * de cargar ordenes ambulatorias
	 * @param con
	 * @param usuario
	 * @param mensajes
	 * @return
	 */
	public String generarArchivoInconsistencias(Connection con,UsuarioBasico usuario,ArrayList mensajes)
	{
		String nombreArchivo = "";
		this.existeArchivo = true;
		
		try
		{
			String contenido=""; //variable para almacenar el contenido del archivo
			String separador=System.getProperty("file.separator"); //separador
			
			//preparación del nombre del archivo *******************************
			String hora=UtilidadFecha.convertirHoraACincoCaracteres(UtilidadFecha.getHoraActual());
			String vectorFecha[]=UtilidadFecha.getFechaActual().split("/");
			String vectorHora[]=hora.split(":");
			nombreArchivo="InconActEjecutadas-"+vectorFecha[0]+vectorFecha[1]+vectorFecha[2]+vectorHora[0]+vectorHora[1]+".txt";
			
			//se valida el directorio*******************************************************
			File directorio=new File(LogsAxioma.getRutaLogs()+separador+ConstantesBD.logFolderModuloPYP+separador+"logActividadesEjecutadasXCargar");
			
			if(!directorio.isDirectory() && !directorio.exists())	{
				if(!directorio.mkdirs()) {
					logger.error("Error creando el directorio "+LogsAxioma.getRutaLogs());
					return "¡Error Interno al generar al archivo "+nombreArchivo+" !";
				}
			}
			
			//apertura del archivo Inconsistencias********************************
			File archivoIncon= new File(directorio,nombreArchivo);
			FileWriter streamIncon=new FileWriter(archivoIncon,true); 
			BufferedWriter bufferIncon=new BufferedWriter(streamIncon);
			
			//Generación del Encabezado del Archivo ********************************
				//1) asignación del nombre de la institucion
				ParametrizacionInstitucion institucion=new ParametrizacionInstitucion();
				institucion.cargar(con,usuario.getCodigoInstitucionInt());
				contenido+=institucion.getRazonSocial()+"\n";
				//2) Tipo de estancia a generar
				contenido+="Generación de Solicitudes de Ordenes Ambulatorias Ejecutadas por Cargar "+UtilidadFecha.getFechaActual()+" "+UtilidadFecha.getHoraActual()+": \n\n";
				
			//ADICION DE LOS MENSAJES DE ERROR *********
			for(int i=0;i<mensajes.size();i++)
			{
				contenido += " *  " + mensajes.get(i) + "\n";
			}
				
			contenido +="\n\n";
			//se escirbe el contenido
			bufferIncon.write(contenido);
			bufferIncon.close();
			return archivoIncon.getAbsolutePath();
		
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo Incon al generarlo: "+e);
			this.existeArchivo = false;
			return "¡Error Interno al generar al archivo "+nombreArchivo+"!";
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo Incon: "+e);
			this.existeArchivo = false;
			return "¡Error Interno al generar al archivo "+nombreArchivo+"!";
		}
	}
	
	/**
	 * Método implementado para cargar el archivo de inconsistencias del cargue de ordenes ambulatorias
	 * y almacenarlo en un HashMap
	 * @param path
	 * @return
	 */
	public ArrayList cargarArchivoInconsistencias(String path)
	{
		ArrayList lineas = new ArrayList();
		try
		{
			String cadena = "";
			//******SE INICIALIZA ARCHIVO*************************
			File archivo=new File(path);
			FileReader stream=new FileReader(archivo); //se coloca false para el caso de que esté repetido
			BufferedReader buffer=new BufferedReader(stream);
			
			//********SE RECORRE LÍNEA POR LÍNEA**************
			cadena=buffer.readLine();
			while(cadena!=null)
			{
				lineas.add(cadena);	
				cadena=buffer.readLine();
			}
			//***************CERRAR ARCHIVO****************************
			buffer.close();
			
			return lineas;
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo "+path+" al cargarlo: "+e);
			return lineas;
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo "+path+" al cargarlo: "+e);
			return lineas;
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @param numeroSolicitud
	 * @param datosOrden
	 * @param usuario
	 * @return
	 */
	public int generarSolicitudXOrdenAmbulatoria(Connection con,String idCuenta,String numeroSolicitud,HashMap datosOrden,UsuarioBasico usuario)
	{
		logger.info("DATOS ORDEN");
		Utilidades.imprimirMapa(datosOrden);
		
		String tipoServicio = datosOrden.get("codigo_tipo_servicio").toString();
		String fechaSistema = UtilidadFecha.getFechaActual(con);
		String horaSistema = UtilidadFecha.convertirHoraACincoCaracteres(UtilidadFecha.getHoraActual(con));
		int resultado = 0, resp0 = 0, resp1 = 0, resp2 = 0, resp3 = 0;
		//se inicializa el arreglo de errores
		this.errores = new ArrayList();
		
		//Se verifica el tipo de servicio
		if(tipoServicio.equals(ConstantesBD.codigoServicioInterconsulta+""))
		{
			//************SERVICIOS DE CONSULTA*********************************
			
			//********SE GENERA SOLICITUD GENERAL*********************************
			SolicitudConsultaExterna solicitud = new SolicitudConsultaExterna();
			try 
			{
				if(!UtilidadTexto.isEmpty(numeroSolicitud))
					solicitud.cargar(con,Utilidades.convertirAEntero(numeroSolicitud));
			} 
			catch (Exception e) 
			{
				logger.error("Error cargando los datos de la solicitud de referencia en generarSolicitudXOrdenAmbulatoria de ActEjecutadasXCargar: "+e);
			} 
			//se ajustan los datos a la nueva solicitud
			solicitud.setSolPYP(true);
			solicitud.getEstadoHistoriaClinica().setCodigo(ConstantesBD.codigoEstadoHCInterpretada);
			solicitud.setFechaSolicitud(fechaSistema);
			solicitud.setHoraSolicitud(horaSistema);
			solicitud.setCodigoCuenta(Integer.parseInt(idCuenta));
			solicitud.getTipoSolicitud().setCodigo(ConstantesBD.codigoTipoSolicitudCita);
			
			if(UtilidadTexto.isEmpty(numeroSolicitud)){
				// como no se ha cargado información base de la solicitud referenciada se completan los datos requeridos desde la orden ambulatoria
				// GIOOOO 
				solicitud.setEspecialidadSolicitante(new InfoDatosInt(Utilidades.convertirAEntero(datosOrden.get("especialidad_solicita")+""))); // Prueba
				solicitud.setOcupacionSolicitado(new InfoDatosInt(Utilidades.convertirAEntero(datosOrden.get("ocupacion_medica")+""))); // Prueba
				solicitud.setCentroCostoSolicitado(new InfoDatosInt(Utilidades.convertirAEntero(datosOrden.get("centro_costo_solicita")+""))); // Prueba
				solicitud.setCentroCostoSolicitante(new InfoDatosInt(Utilidades.convertirAEntero(datosOrden.get("centro_costo_solicita")+""))); // Prueba
				solicitud.setCodigoMedicoResponde(Utilidades.convertirAEntero(datosOrden.get("codigo_medico")+""));
				solicitud.setCobrable(true);
				solicitud.setVaAEpicrisis(false);
				solicitud.setUrgente(false);
				
			}
				
			
			//datos específicos de la consulta
			solicitud.setCodigoServicioSolicitado(Integer.parseInt(datosOrden.get("codigo_servicio").toString()));
			
			//se inserta nueva solicitud
			try 
			{
				resp0 = solicitud.insertarSolicitudConsultaExternaTransaccional(con,ConstantesBD.continuarTransaccion);
			} 
			catch (SQLException e) 
			{
				logger.error("Error generando nueva solicitud en generarSolicitudXOrdenAmbulatoria de ActEjecutadasXCargar: "+e);
				errores.add("Error generando solicitud para la orden "+datosOrden.get("orden"));
				resp0 = 0;
			}
		
			//*********************************************************************
			
			
			
			//========Se graba valoracion basándose de la solicitud de referencia==================
			if(resp0>0)
			{
				//Se carga la valoracion de la solicitud de referencia---------------------------------------------
				PersonaBasica paciente = new PersonaBasica();
				try 
				{
					paciente.cargar(con,Integer.parseInt(datosOrden.get("codigo_paciente").toString()));
				} 
				catch (Exception e) 
				{
					logger.error("Error cargando PersonaBasica en generarSolicitudXOrdenAmbulatoria de ActEjecutadasXCargar: "+e);
					errores.add("Error al consultar información del paciente para generar valoración de la orden "+datosOrden.get("orden"));
				} 
				
				
				DtoValoracion valoracion = Valoraciones.cargarBase(con, numeroSolicitud);
				
				if(UtilidadTexto.isEmpty(numeroSolicitud)){
					// como no se ha cargado información base de la solicitud referenciada se completan los datos requeridos
					try {
						logger.info("\n\n\n\nCodigo "+datosOrden.get("login_profesional"));
						valoracion.getProfesional().cargarUsuarioBasico(con, datosOrden.get("login_profesional")+"");
						logger.info("Nombreeeeeeeee "+valoracion.getProfesional().getNombreUsuario());
					} catch (Exception e) {logger.error("ERROR", e);}
				}
					
				
				//Se guarda la valoracion para la nueva solicitud-----------------------------------------------------
				
				//se cambia la fecha
				valoracion.setFechaValoracion(datosOrden.get("fecha").toString());
				valoracion.setHoraValoracion(UtilidadFecha.convertirHoraACincoCaracteres(datosOrden.get("hora").toString()));
				valoracion.setDiagnosticos(new ArrayList<Diagnostico>());
				//se verifica si tenía diagnostico
				if(!datosOrden.get("acronimo_diagnostico").toString().equals(""))
				{
					Diagnostico diag = new Diagnostico();
					diag.setAcronimo(datosOrden.get("acronimo_diagnostico").toString());
					diag.setTipoCIE(Integer.parseInt(datosOrden.get("cie_diagnostico").toString()));
					diag.setNombre(datosOrden.get("nombre_diagnostico").toString());
					diag.setPrincipal(true);
					diag.setNumero(0);
					
					//Se añade diagnostico a nueva valoracion
					valoracion.getDiagnosticos().add(diag);
				}
				
				//se verifica si tenía finalidad consulta
				if(!datosOrden.get("codigo_fin_consulta").toString().equals(""))
					valoracion.getValoracionConsulta().setFinalidadConsulta(new InfoDatosString(datosOrden.get("codigo_fin_consulta").toString()));
				
				//se verifica si tenía causa externa
				if(Integer.parseInt(datosOrden.get("codigo_causa_externa").toString())>0)
				{
					valoracion.setCodigoCausaExterna(Integer.parseInt(datosOrden.get("codigo_causa_externa").toString()));
					valoracion.setNombreCausaExterna(datosOrden.get("nombre_causa_externa").toString());
				}
				
				valoracion.setNumeroSolicitud(resp0+"");
				valoracion.setFechaGrabacion(UtilidadFecha.getFechaActual(con));
				valoracion.setHoraGrabacion(UtilidadFecha.getHoraActual(con));
				
				String observacionCapitacion = null;
				ResultadoBoolean resultadoVal = Valoraciones.insertarBase(con, valoracion, observacionCapitacion);
				
				if(!resultadoVal.isTrue())
				{
					resp2 = 0;
					logger.error("Error insertando nueva valoracion en generarSolicitudXOrdenAmbulatoria de ActEjecutadasXCargar: ");
					errores.add(resultadoVal.getDescripcion());
				}
				else
				{
					resp2 = 1;
					//Se interpreta la solicitud
					if(Solicitud.interpretarSolicitud(con, "", valoracion.getProfesional().getCodigoPersona(), valoracion.getFechaGrabacion(), valoracion.getHoraGrabacion(), valoracion.getNumeroSolicitud())<=0)
						resp2 = 0;
					
					//Actualizar el médico responde
					if(Solicitud.actualizarMedicoResponde(con, Integer.parseInt(valoracion.getNumeroSolicitud()), valoracion.getProfesional())<=0)
						resp2 = 0;
					
					//Se intenta actualizar el pool del médico
					Solicitud sol=new Solicitud();
					ArrayList array=Utilidades.obtenerPoolesMedico(con,valoracion.getFechaGrabacion(),valoracion.getProfesional().getCodigoPersona());
					if(array.size()==1)
						resp2 = sol.actualizarPoolSolicitud(con,Integer.parseInt(valoracion.getNumeroSolicitud()),Integer.parseInt(array.get(0)+""));
				}
				
				
				
			}
			//*******************************************************************
			
			///********+SE GENERA EL CARGO DE LA SOLICITUD*******************************
			resp1 = this.generarCargo(con,resp0,idCuenta,datosOrden,usuario,tipoServicio);
			
			//***************************************************************************
			
		}
		else
		{
			//***************SERVICIOS DE PROCEDIMIENTOS*******************************************
			
			///********SE GENERA SOLICITUD GENERAL*********************************
			SolicitudProcedimiento solicitud = new SolicitudProcedimiento();
			try 
			{
				if(!UtilidadTexto.isEmpty(numeroSolicitud))
					solicitud.cargar(con,Utilidades.convertirAEntero(numeroSolicitud));
			} 
			catch (Exception e) 
			{
				logger.error("Error cargando los datos de la solicitud de referencia en generarSolicitudXOrdenAmbulatoria de ActEjecutadasXCargar: "+e);
			} 
			//se ajustan los datos a la nueva solicitud
			solicitud.setSolPYP(true);
			solicitud.getEstadoHistoriaClinica().setCodigo(ConstantesBD.codigoEstadoHCInterpretada);
			solicitud.setFechaSolicitud(fechaSistema);
			solicitud.setHoraSolicitud(horaSistema);
			solicitud.setCodigoCuenta(Integer.parseInt(idCuenta));
			solicitud.getTipoSolicitud().setCodigo(ConstantesBD.codigoTipoSolicitudProcedimiento);
			
			//datos específicos del procedimiento
			int numeroDocumento = solicitud.numeroDocumentoSiguiente(con);
			solicitud.setMultiple(false);
			solicitud.setFinalidad(Integer.parseInt(datosOrden.get("codigo_fin_servicio").toString()));
			solicitud.setCodigoServicioSolicitado(Integer.parseInt(datosOrden.get("codigo_servicio").toString()));
			
			if(UtilidadTexto.isEmpty(numeroSolicitud)){
				solicitud.setEspecialidadSolicitante(new InfoDatosInt(Utilidades.convertirAEntero(datosOrden.get("especialidad_solicita")+""))); // Prueba
				solicitud.setOcupacionSolicitado(new InfoDatosInt(Utilidades.convertirAEntero(datosOrden.get("ocupacion_medica")+""))); // Prueba
				solicitud.setCentroCostoSolicitado(new InfoDatosInt(Utilidades.convertirAEntero(datosOrden.get("centro_costo_solicita")+""))); // Prueba
				solicitud.setCentroCostoSolicitante(new InfoDatosInt(Utilidades.convertirAEntero(datosOrden.get("centro_costo_solicita")+""))); // Prueba
				solicitud.setCodigoMedicoResponde(Utilidades.convertirAEntero(datosOrden.get("codigo_medico")+""));
				solicitud.setCobrable(true);
				solicitud.setVaAEpicrisis(false);
				solicitud.setUrgente(false);
			}
			
			//se inserta nueva solicitud
			try 
			{
				resp0 = solicitud.insertarTransaccional(con,ConstantesBD.continuarTransaccion,numeroDocumento,Integer.parseInt(idCuenta),false,ConstantesBD.codigoNuncaValido+"");
			} 
			catch (SQLException e) 
			{
				logger.error("Error generando nueva solicitud en generarSolicitudXOrdenAmbulatoria de ActEjecutadasXCargar: "+e);
				errores.add("Error generando solicitud para la orden "+datosOrden.get("orden"));
				resp0 = 0;
			}
		
			//*********************************************************************
			
			//==========Se graba la respuesta de procedimientos===================================
			if(resp0>0)
			{
				logger.info("VALOR DE LA SOLICITUD B=> "+resp0);
				RespuestaProcedimientos respuestaPro = new RespuestaProcedimientos();
				if(!respuestaPro.insertarRespuestaProcedimiento(
						con,
						resp0+"",
						datosOrden.get("fecha").toString(),
						datosOrden.get("resultados").toString(),
						"",
						ConstantesBD.codigoTipoRecargoSinRecargo,
						"",
						UtilidadFecha.convertirHoraACincoCaracteres(datosOrden.get("hora").toString()),
						usuario.getCodigoPersona(),
						usuario.getLoginUsuario(),ConstantesBD.codigoNuncaValido, null).equals(""))
					resp2 = 1;
				else
					resp2 = 0;
				
				if(resp2>0)
				{
					Solicitud sol=new Solicitud();
					String loginMedico = datosOrden.get("login_profesional").toString();
					UsuarioBasico usuProf = new UsuarioBasico();
					try 
					{
						usuProf.cargarUsuarioBasico(con,loginMedico);
					} 
					catch (SQLException e) 
					{
						logger.info("Error cargando usuario basico en generarSolicitudXOrdenAmbulatoria de ActEjecutadasXCargar: "+e);
						errores.add("Error al consultar información del profesional para generar solicitud a partir de la orden "+datosOrden.get("orden"));
					}
					 
					 //se actualiza el pool del médico--------------------------------------------------------------
					 ArrayList array=Utilidades.obtenerPoolesMedico(con,UtilidadFecha.getFechaActual(),usuProf.getCodigoPersona());
					if(array.size()==1)
						sol.actualizarPoolSolicitud(con,resp0,Integer.parseInt(array.get(0)+""));
					//se actualiza el médico que responde --------------------------------------------------------------------------
				    try 
				    {
						resp2 = sol.actualizarMedicoRespondeTransaccional(con, resp0, usuProf, ConstantesBD.continuarTransaccion);
					} catch (SQLException e) 
					{
						logger.error("Error al actualizar el medico que responde en generarSolicitudXOrdenAmbulatoria de ActEjecutadasXCargar: "+e);
						errores.add("Error al insertar datos de respuesta de la solicitud generada a partir de la orden "+datosOrden.get("orden"));
						resp2 = 0;
					}
					
					if(resp2>0)
					{
						//Se inserta la intepretacion de la solicitud
						HashMap parametros = new HashMap();
						parametros.put("interpretacion","");
						parametros.put("codigoMedico",usuProf.getCodigoPersona()+"");
						parametros.put("fechaInterpretacion",datosOrden.get("fecha").toString());
						parametros.put("horaInterpretacion",datosOrden.get("hora").toString());
						parametros.put("numeroSolicitud",resp0+"");
						
						resp2 = Solicitud.interpretarSolicitud(con,parametros);
					}
					
				}
				else
					errores.add("Error al insertar respuesta de la solicitud de procedimientos generada a partir de la orden "+datosOrden.get("orden"));
				
				//********+SE GENERA EL CARGO DE LA SOLICITUD*******************************
				resp1 = this.generarCargo(con,resp0,idCuenta,datosOrden,usuario,tipoServicio);
				//***************************************************************************
			
			}
			//***************************************************************************************
		}
		
		
		//*********ACTUALUIZACIONES EN LA ORDEN AMBULATORIA Y LA ACTIVIDAD PYP*****************
		if(resp0>0)
		{
			logger.info("VALOR DE LA SOLICITUD C=> "+resp0);
			//Actualizar estado y numero de solicitud en la orden ambulatoria
			HashMap parametros = new HashMap();
			parametros.put("numeroSolicitud",resp0+"");
			parametros.put("numeroOrden",datosOrden.get("codigo").toString());
			parametros.put("estadoOrden",ConstantesBD.codigoEstadoOrdenAmbulatoriaSolicitada+"");
			resp3 = OrdenesAmbulatorias.actualizarSolicitudEnOrdenAmbulatoria(con,parametros);
			
			if(resp3>0)
			{
				//Se Asigna el nuevo numero de solicitud a la actividad asociada a la orden ambulatoria
				boolean exito = Utilidades.asignarSolicitudToActividadPYP(con,datosOrden.get("codigo").toString(),resp0+"");
				if(exito)
					resp3 = 1;
				else
					resp3 = 0;
				
				if(resp3<=0)
					errores.add("Error al asignar el número de la solicitud en la actividad PYP asociada a la orden "+datosOrden.get("orden"));
			}
			else
				errores.add("Error al actualizar el número de la solicitud en la orden "+datosOrden.get("orden"));
		}
		//*********************************************************************************************************
		logger.info("resp0=> "+resp0+", resp1=> "+resp1+", resp2=> "+resp2+" resp3=> "+resp3);
		if(resp0>0&&resp1>0&&resp2>0&&resp3>0)
			resultado = 1;
		else
			resultado = 0;
		
		return resultado;
	}
	
	
	/**
	 * Método implementado para generar el cargo de una solicitud
	 * @param con
	 * @param resp0 (Corresponde al numero de la solicitud)
	 * @param idCuenta
	 * @param datosOrden
	 * @param usuario
	 * @param tipoServicio 
	 * @return
	 */
	private int generarCargo(Connection con, int resp0, String idCuenta, HashMap datosOrden, UsuarioBasico usuario, String tipoServicio) 
	{
		Cargos cargos = new Cargos();
		cargos.setPyp(Utilidades.esSolicitudPYP(con, resp0));
		 int resp1 = 0;
		 
		if(resp0>0)
		{
			//Se carga cuenta
			Cuenta cuenta = new Cuenta();
			cuenta.cargar(con,idCuenta);
			
			PersonaBasica paciente= new PersonaBasica();
			try 
			{
				paciente.cargar(con, Integer.parseInt(cuenta.getCuenta().getCodigoPaciente()));
				paciente.setCodigoIngreso(Integer.parseInt(cuenta.getCuenta().getIdIngreso()));
			} 
			catch (SQLException e1) 
			{
				e1.printStackTrace();
			}
			
			//Se genera el cargo
			resp1 = 1;
			//cargo.setEsCita(false); //se dice que no es cita para que pueda generar cargo pendiente en caso de que no haya tarifa
			//cargo.setServicio(Integer.parseInt(datosOrden.get("codigo_servicio").toString()));
			Vector erroresCargo = new Vector();
			try 
			{
				logger.info("FECHA DE LA ORDEN AMBULATORIA=> "+datosOrden.get("fecha").toString());
				cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
																					usuario, 
																					paciente, 
																					false/*dejarPendiente*/, 
																					resp0 /*numeroSolicitud()*/, 
																					tipoServicio.equals(ConstantesBD.codigoServicioInterconsulta+"")?ConstantesBD.codigoTipoSolicitudCita:ConstantesBD.codigoTipoSolicitudProcedimiento /*codigoTipoSolicitudOPCIONAL*/, 
																					Integer.parseInt(cuenta.getCuenta().getIdCuenta()), 
																					ConstantesBD.codigoCentroCostoConsultaExterna/*codigoCentroCostoEjecutaOPCIONAL*/, 
																					Integer.parseInt(datosOrden.get("codigo_servicio").toString())/*codigoServicioOPCIONAL*/, 
																					1/*cantidadServicioOPCIONAL*/, 
																					ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOPCIONAL*/, 
																					ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
																					/* "" -- numeroAutorizacionOPCIONAL*/
																					"" /*esPortatil*/,false,
																					"", /*fechaGeneracionCargoOPCIONAL*/
																					"" /*subCuentaCoberturaOPCIONAL*/
																				);
				
				erroresCargo=cargos.getInfoErroresCargo().getMensajesErrorDetalle();
			} 
			catch (Exception e) 
			{
				logger.error("Error al generar el cargo en generarSolicitudXOrdenAmbulatoria de ActEjecutadasXCargar: "+e);
				errores.add("Error al generar el cargo de la solicitud de la orden "+datosOrden.get("orden"));
			} 
			
			for(int l=0;l<erroresCargo.size();l++)
			{
				//su acontece un error diferente a que  no haya tarifa
				//no se puede continuar
				if(!erroresCargo.get(l).toString().equals("error.cargo.noHayValorTarifa"))
				{
					resp1 = 0;
					if(erroresCargo.get(l).toString().equals("error.cargo.contratoVencido"))
						errores.add("Contrato vencido al generar el cargo de la orden "+datosOrden.get("orden"));
					else if(erroresCargo.get(l).toString().equals("error.cargo.noHayRecargo"))
						errores.add("Error el calcular el recargo en la generación del cargo de la orden "+datosOrden.get("orden"));
					
				}
			}
			
			/*/Cuando se genera el cargo en Consultas cambia el estado a Respondida (se debe pasar a interpretada)
			if(tipoServicio.equals(ConstantesBD.codigoServicioInterconsulta+"")&&resp1>0)
			{
				boolean exito = false;
				if(erroresCargo.size()>0)
					exito = Solicitud.cambiarEstadosSolicitudStatico(con,resp0,ConstantesBD.codigoEstadoFPendiente,ConstantesBD.codigoEstadoHCInterpretada).isTrue();
				else
					exito = Solicitud.cambiarEstadosSolicitudStatico(con,resp0,ConstantesBD.codigoEstadoFCargada,ConstantesBD.codigoEstadoHCInterpretada).isTrue();
				
				if(exito)
					resp1 = 1;
				else
					resp1 = 0;
				
			}**/
			
			
		}
		
		return resp1;
	}
	//*****************************************************************************
	//***********GETTERS & SETTERS*************************************************
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
	/**
	 * @return Returns the existeArchivo.
	 */
	public boolean isExisteArchivo() {
		return existeArchivo;
	}
	/**
	 * @param existeArchivo The existeArchivo to set.
	 */
	public void setExisteArchivo(boolean existeArchivo) {
		this.existeArchivo = existeArchivo;
	}
	/**
	 * @return Returns the errores.
	 */
	public ArrayList getErrores() {
		return errores;
	}
	/**
	 * @param errores The errores to set.
	 */
	public void setErrores(ArrayList errores) {
		this.errores = errores;
	}

}
