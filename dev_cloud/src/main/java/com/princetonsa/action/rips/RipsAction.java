/*
 * Created on Jun 10, 2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.action.rips;

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
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.rips.RipsForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.cartera.MovimientosCuentaCobro;
import com.princetonsa.mundo.rips.Rips;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author sebacho
 *
 * Controlador para la creación de los archivos RIPS
 */
public class RipsAction extends Action {
	/**
	 * Para hacer logs de esta funcionalidad.
	 * 
	 * @uml.property name="logger"
	 * @uml.associationEnd 
	 * @uml.property name="logger" multiplicity="(1 1)"
	 */
	private Logger logger = Logger.getLogger(RipsAction.class);
	

	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */	
	public ActionForward execute(	ActionMapping mapping, 
														ActionForm form, 
														HttpServletRequest request,
														HttpServletResponse response) throws Exception
														{	
		Connection con=null;
		try{
			String estado=""; //para verificar estado del Form
			String nombrePath=request.getServletPath();
			/* Revisión de instancia de Form */
			if( form instanceof RipsForm )
			{
				RipsForm ripsForm = (RipsForm)form;


				String tipoBD;
				try
				{
					/* Conexión con un DaoFactory */
					tipoBD = System.getProperty("TIPOBD");
					DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
					con = myFactory.getConnection();
				}
				catch(Exception e)
				{
					e.printStackTrace(); /* es lo mismo que un logger.info */

					ripsForm.reset();

					logger.warn("Problemas con la base de datos "+e);
					request.setAttribute("codigoDescripcionError", "errors.problemasBD");
					//Salir en caso de error (se busca cerrar bien la conexión)
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.problemasBD", "", true);
				}

				//*****OBJETOS QUE SE VAN A USAR*****************************************
				UsuarioBasico usu=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				String ripsConFactura=ValoresPorDefecto.getRipsPorFactura(usu.getCodigoInstitucionInt());

				estado=ripsForm.getEstado();
				//*********VALIDACIONES******************************************
				if(!nombrePath.equals("/ripsRangos/ripsRangos.do")&&!nombrePath.equals("/consultaRips/consultaRips.do"))
				{
					if(ripsConFactura.equals(""))
					{
						ripsForm.reset();
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "parámetro rips con factura no definido", "error.rips.parametroGeneral", true);
					}
				}
				//Si el estado es 'consultar' se cambia a 'empezar'
				if(estado.equals("consultar"))
					estado="empezar";

				logger.info("============================");
				logger.info("Estado :"+estado);
				logger.info("============================");


				//__________________________________________________
				//Según el estado el Action va por diferentes caminos
				if(estado.equals("empezar"))
				{
					ripsForm.reset();
					UtilidadBD.cerrarConexion(con);
					ripsForm.setOpcionRips("convenio"); //solo se usa para consultorios
					return mapping.findForward("principal");
				}
				//******ESTADOS PARA RIPS DE CARTERA****************
				else if(estado.equals("generar"))
				{
					return accionGenerar(mapping,request,con,usu,ripsForm,ripsConFactura);
				}
				else if(estado.equals("detalle"))
				{
					return accionDetalle(mapping,usu,con,ripsForm);
				}
				else if(estado.equals("buscar"))
				{
					return accionBuscar(mapping,con,usu,ripsForm);
				}
				//*****ESTADOS PARA RIPS CONSULTORIOS********
				//estado usado para elegir entre las dos opciones de RIPS consultorios
				else if(estado.equals("opciones"))
				{
					ripsForm.reset();
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("opciones");
				}
				//estado usado para la opción Por Paciente
				else if(estado.equals("busquedaRangosPaciente"))
				{
					return accionBusquedaRangosPaciente(mapping,con,ripsForm,usu,request);
				}
				else if(estado.equals("busquedaRangos"))
				{
					return accionBusquedaRangos(mapping,con,ripsForm,usu);
				}
				else if(estado.equals("redireccion"))
				{
					return this.accionRedireccion(con,ripsForm,response);
				}
				else if(estado.equals("ordenarRangos"))
				{
					return this.accionOrdenarRangos(ripsForm,mapping,con);
				}
				else if(estado.equals("informacionRips"))
				{
					return this.accionInformacionRips(ripsForm,mapping,con);
				}
				else if(estado.equals("guardarRips"))
				{
					return this.accionGuardarRips(ripsForm,mapping,con,usu);
				}
				else if(estado.equals("generarConsultorios"))
				{
					return this.accionGenerarConsultorios(ripsForm,mapping,request,con,usu);
				}
				//*********ESTADOS DE CONSULTA GENERACIÓN RIPS**********
				else if(estado.equals("resultadoConsulta"))
				{
					return this.accionResultadoBusqueda(ripsForm,mapping,con);
				}
				else if(estado.equals("ordenarConsulta"))
				{
					return this.accionOrdenarConsulta(ripsForm,mapping,con);
				}
				else
				{
					//con el objetivo de cerrar conexiones que se puedan quedar activas
					UtilidadBD.cerrarConexion(con);
				}
				UtilidadBD.cerrarConexion(con);
			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}

	/**
	 * Método para buscar los registros iniciales por paciente
	 * @param mapping
	 * @param con
	 * @param ripsForm
	 * @param usu
	 * @param request
	 * @return
	 */
	private ActionForward accionBusquedaRangosPaciente(ActionMapping mapping, Connection con, RipsForm ripsForm, UsuarioBasico usu, HttpServletRequest request) {
		////****CONSULTA DE LOS REGISTROS POR PACIENTE************
		ripsForm.setOpcionRips("paciente");
		PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
		
		//******VALIDACIONES*************
		//verificar si hay paciente cargado en sesión
		if(paciente==null||paciente.getCodigoPersona()==-1){
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente no cargado", "errors.paciente.noCargado", true);
		}
		//verificar si el paciente tiene cuenta activa
		if(paciente.getCodigoCuenta()==0){
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La cuenta no está abierta", "errors.paciente.cuentaNoAbierta", true);
		}
		
		//se carga la cuenta del paciente
		try
		{
			Cuenta cuenta=new Cuenta();
			cuenta.cargarCuenta(con,paciente.getCodigoCuenta()+"");
			Convenio convenio=new Convenio();
			//se carga el convenio
			convenio.cargarResumen(con,Integer.parseInt(cuenta.getCodigoConvenio()));
			ripsForm.setConvenio(convenio.getCodigo()+"-"+convenio.getNombre());
			
			//como rangos se coloca la fecha de apertura de la cuenta
			ripsForm.setFechaInicial(cuenta.getDiaApertura()+"/"+cuenta.getMesApertura()+"/"+cuenta.getAnioApertura());
			ripsForm.setFechaFinal(cuenta.getDiaApertura()+"/"+cuenta.getMesApertura()+"/"+cuenta.getAnioApertura());
			
		}
		catch(Exception e)
		{
			logger.error("Error en accionBusquedaRangosPaciente de RipsAction:"+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error al cargar la cuenta", "errors.problemasBd", true);
		}
		
		
		
		Rips rips=new Rips();
		//se llenan los datos para el objeto RIPS
		rips.setConvenio(Integer.parseInt((ripsForm.getConvenio().split("-"))[0]));
		rips.setFechaInicial(ripsForm.getFechaInicial());
		rips.setFechaFinal(ripsForm.getFechaFinal());
		
		ripsForm.setRegistrosRangos(rips.consultaRegistrosRangosPaciente(con,
				paciente.getCodigoCuenta()));

		
		
		//se hacen inserciones de registros que tienen los datos Rips llenos
		ripsForm.setRegistrosRangos(
				rips.verificacionRegistrosIniciales(con,
						ripsForm.getRegistrosRangos(),
						usu.getCodigoInstitucionInt())
					);
		logger.info("inicio busqueda rangos 3");
		try
		{
			ripsForm.setEstado("busquedaRangos");
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("principal");
		}
		catch(SQLException e)
		{
			logger.error("Error en accionBusquedaRangosPaciente de RipsAction:"+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error al cerrar la conexion", "errors.problemasDatos", true);
		}
	}

	/**
	 * @param ripsForm
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionOrdenarConsulta(RipsForm ripsForm, ActionMapping mapping, Connection con) throws SQLException 
	{
		//columnas del listado
		String[] indices={
				"fecha_factura_",
				"numero_factura_",
				"convenio_",
				"fecha_generacion_",
				"numero_remision_",
				"fecha_remision_",
				"fecha_inicial_",
				"fecha_final_",
				"usuario_"
			};
		
		int numeroElementos=Integer.parseInt(ripsForm.getRegistrosRangos("numRegistros")+"");
		logger.info("Número de registros en ordenacion=> "+numeroElementos);
		ripsForm.setRegistrosRangos(Listado.ordenarMapa(indices,
				ripsForm.getIndice(),
				ripsForm.getUltimoIndice(),
				ripsForm.getRegistrosRangos(),
				numeroElementos));
		
		ripsForm.setRegistrosRangos("numRegistros",numeroElementos+"");
		ripsForm.setUltimoIndice(ripsForm.getIndice());
		ripsForm.setEstado("resultadoConsulta");
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método para consultar la generación de RIPS
	 * @param ripsForm
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionResultadoBusqueda(RipsForm ripsForm, ActionMapping mapping, Connection con) {
		//se genera instacnia de RIPS
		Rips rips=new Rips();
		ripsForm.setRegistrosRangos(rips.consultaGeneracionRips(con,
				Integer.parseInt(ripsForm.getConvenio().split("-")[0]),
				ripsForm.getFechaFactura()
				));
		try
		{
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.error("Error cerrando la conexión "+e);
			return null;
		}
		return mapping.findForward("principal");
	}

	/**
	 * Método para generar los archivos RIPS consultorios
	 * @param ripsForm
	 * @param mapping
	 * @param request
	 * @param con
	 * @param usu
	 * @return
	 */
	private ActionForward accionGenerarConsultorios(RipsForm ripsForm, ActionMapping mapping, HttpServletRequest request, Connection con, UsuarioBasico usu) throws SQLException, IPSException {
	
		//redirección a la generación de RIPS
		return this.accionGenerar(mapping,request,con,usu,ripsForm,"true");
		
	}

	/**
	 * Método usado para guardar los datos RIPS del registro
	 * @param ripsForm
	 * @param mapping
	 * @param con
	 * @param usu
	 * @return
	 */
	private ActionForward accionGuardarRips(RipsForm ripsForm, ActionMapping mapping, Connection con, UsuarioBasico usu) throws SQLException
	{
		Rips rips=new Rips();
		logger.info("Consecutivo del registro al guardar=> "+ripsForm.getRegistrosRangos("consecutivo_"+ripsForm.getPosicionHashMap()));
		String consecutivoStr=ripsForm.getRegistrosRangos("consecutivo_"+ripsForm.getPosicionHashMap())+"";
		
		//se asigna la fecha del registro
		if((ripsForm.getDatosRips("esOtro_0")+"").equals("true"))
			ripsForm.setDatosRips("fecha_0",UtilidadFecha.conversionFormatoFechaABD(ripsForm.getDatosRips("fecha_0")+""));
		else
			ripsForm.setDatosRips("fecha_0",UtilidadFecha.conversionFormatoFechaABD(ripsForm.getRegistrosRangos("fecha_"+ripsForm.getPosicionHashMap())+""));
		
		//CASO 1=> SE VERIFICA SI EL REGISTRO ES DE TIPO CITA
		if(ripsForm.getTipoRegistro()==1)
		{
			if(consecutivoStr.equals("")||consecutivoStr.equals("null"))
			{
				int consecutivo=rips.insertarDatosRipsCita(
						con,ripsForm.getDatosRips(),
						Integer.parseInt(ripsForm.getConvenio().split("-")[0]),
						ripsForm.getFechaInicial(),ripsForm.getFechaFinal(),
						usu.getCodigoInstitucionInt());
				//se añade consecutivo al registro
				if(consecutivo>0)
					ripsForm.setRegistrosRangos("consecutivo_"+ripsForm.getPosicionHashMap(),consecutivo+"");
			}
			else
			{
				rips.actualizarDatosRips(con,Integer.parseInt(consecutivoStr),ripsForm.getDatosRips());
			}
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("salirGuardar");	
		}
		//CASO 2=> SE VERIFICA SI EL REGISTRO NO ES CITA PERO TIENE SERVICIO ASOCIADO
		else if(ripsForm.getTipoRegistro()==2)
		{
			if(consecutivoStr.equals("")||consecutivoStr.equals("null"))
			{
				int consecutivo=rips.insertarDatosRipsNoCita(
						con,ripsForm.getDatosRips(),
						Integer.parseInt(ripsForm.getConvenio().split("-")[0]),
						ripsForm.getFechaInicial(),ripsForm.getFechaFinal(),
						usu.getCodigoInstitucionInt());
				//se añade consecutivo al registro
				if(consecutivo>0)
					ripsForm.setRegistrosRangos("consecutivo_"+ripsForm.getPosicionHashMap(),consecutivo+"");
			}
			else
			{
				rips.actualizarDatosRips(con,Integer.parseInt(consecutivoStr),ripsForm.getDatosRips());
			}
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("salirGuardar");	
		}
		//CASO 3=> SE VERIFICA SI EL REGISTRO (SOLICITUD O SIN SOLICITUD) NO TENÍA SERVICIO ASOCIADO
		else if(ripsForm.getTipoRegistro()==3)
		{
			if(consecutivoStr.equals("")||consecutivoStr.equals("null"))
			{
				int consecutivo=rips.insertarDatosRipsSinServicio(
						con,ripsForm.getDatosRips(),
						Integer.parseInt(ripsForm.getConvenio().split("-")[0]),
						ripsForm.getFechaInicial(),ripsForm.getFechaFinal(),
						usu.getCodigoInstitucionInt());
				//se añade consecutivo al registro
				if(consecutivo>0)
					ripsForm.setRegistrosRangos("consecutivo_"+ripsForm.getPosicionHashMap(),consecutivo+"");
			}
			else
			{
				rips.actualizarDatosRips(con,Integer.parseInt(consecutivoStr),ripsForm.getDatosRips());
			}
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("salirGuardar");
		}
		UtilidadBD.cerrarConexion(con);
		return null;
	}

	/**
	 * Método usado para cargar los datos rips de las citas en un popup aparte
	 * @param ripsForm
	 * @param mapping
	 * @param con
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private ActionForward accionInformacionRips(RipsForm ripsForm, ActionMapping mapping, Connection con) throws SQLException 
	{
		Rips rips=new Rips();
		logger.info("Tipo de la solicitud=> "+ripsForm.getTipoSolicitud());
		logger.info("Número de la solicitud=> "+ripsForm.getNumeroSolicitud());
		logger.info("Tiene servicio=> "+ripsForm.isTieneServicio());
		logger.info("Posicion HashMap=> "+ripsForm.getPosicionHashMap());
		logger.info("servicio en informacionRips "+ripsForm.getRegistrosRangos("servicio_"+ripsForm.getPosicionHashMap())+" de posicion "+ripsForm.getPosicionHashMap());
		
		String consecutivoRegistro=ripsForm.getRegistrosRangos("consecutivo_"+ripsForm.getPosicionHashMap())+"";
		logger.info("El consecutivo es=> "+consecutivoRegistro);
		//se verifica que el registro no se haya ingresado a RIPS
		if(consecutivoRegistro.equals("")||consecutivoRegistro.equals("null"))
		{
				if(ripsForm.getTipoSolicitud()!=-1&&ripsForm.getNumeroSolicitud()!=0)
				{
					String[] datosServicio=(ripsForm.getRegistrosRangos("datos_servicio_"+ripsForm.getPosicionHashMap())+"").split(ConstantesBD.separadorSplit);
					if(datosServicio[0].equals(ConstantesBD.codigoServicioInterconsulta+"")&&(datosServicio[1].equals("01")||datosServicio[1].equals("05")))
					{
						//CASO 1=> caso en el que es registro Consultas
						logger.info("CASO 1");
						ripsForm.setDatosRips(
								rips.cargarDatosRipsCita(con,ripsForm.getNumeroSolicitud())
								);
						
						logger.info("servicio=> "+ripsForm.getDatosRips("servicio_0"));
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("informacionRips");
					}
					else
					{
						//CASO 2=> caso en el que es registro de Procedimientos	
						logger.info("CASO 2");
						ripsForm.setDatosRips(
								rips.cargarDatosRipsNoCita(con,ripsForm.getNumeroSolicitud())
								);
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("informacionRips");
					}
				}
				
				//CASO 3=> caso en el que hay o no solicitud, no tiene servicio asociado pero editó uno para la busqueda
				else if(ripsForm.isTieneServicio()&&ripsForm.getTipoSolicitud()==-1)
				{
					logger.info("CASO 3");
					
					
					//se verifica si es un registro Otro no existente
					if(ripsForm.getNumeroSolicitud()==0&&
						((ripsForm.getRegistrosRangos("servicio_"+ripsForm.getPosicionHashMap())+"").equals("")||
						(ripsForm.getRegistrosRangos("servicio_"+ripsForm.getPosicionHashMap())+"").equals("null"))&&
						ripsForm.getPosicionPadre()!=-1)
					{
						//se pasan los datos del padre al hijo
						int posRegistro=ripsForm.getPosicionHashMap();
						int posPadre=ripsForm.getPosicionPadre();
						ripsForm.setRegistrosRangos("fecha_"+posRegistro,ripsForm.getRegistrosRangos("fecha_"+posPadre));
						ripsForm.setRegistrosRangos("paciente_"+posRegistro,ripsForm.getRegistrosRangos("paciente_"+posPadre));
						ripsForm.setRegistrosRangos("tipo_num_id_"+posRegistro,ripsForm.getRegistrosRangos("tipo_num_id_"+posPadre));
						ripsForm.setRegistrosRangos("servicioEditado_"+posRegistro,ripsForm.getCodigoCups());
						ripsForm.setRegistrosRangos("tipo_"+posRegistro,ripsForm.getTipoSolicitud()+"");
						ripsForm.setRegistrosRangos("servicio_"+posRegistro,"");
						ripsForm.setRegistrosRangos("posPadre_"+posRegistro,ripsForm.getPosicionPadre()+"");
						ripsForm.setRegistrosRangos("cuenta_"+posRegistro,ripsForm.getRegistrosRangos("cuenta_"+posPadre));
						ripsForm.setRegistrosRangos("codigo_paciente_"+posRegistro,ripsForm.getRegistrosRangos("codigo_paciente_"+posPadre));
						ripsForm.setRegistrosRangos("rips_"+posRegistro,"");
						ripsForm.setRegistrosRangos("solicitud_"+posRegistro,"0");
						ripsForm.setRegistrosRangos("esOtro_"+posRegistro,"true");
						
						
					}
					//Se revisa si el servicio ya se consultó
					if((ripsForm.getRegistrosRangos("servicio_"+ripsForm.getPosicionHashMap())+"").equals(""))
					{
						logger.info("Estado de búsqueda del servicio=> "+ripsForm.getEstadoBusquedaServicio());
						//dependiendo del estado de búsqueda del servicio se toma el camino de
						//búsqueda apropiado
						if(ripsForm.getEstadoBusquedaServicio().equals("buscarCodigo")||
								ripsForm.getEstadoBusquedaServicio().equals(""))
						{
							//camino 1: usado para el primer caso donde se digita un código CUPS desde el formulario principal
							//camino 2: caso en que no hay servicio para código CUPS y se desea buscar de nuevo
							ripsForm.setDatosRips(rips.consultarServicios(con,ripsForm.getCodigoCups()));
							ripsForm.setOpcionBusquedaServicio("codigo");
						}
						else
						{
							//camino 3: caso en que no hay servicio para código CUPS y se desea buscar por nombre
							ripsForm.setDatosRips(rips.consultarServicioXNombre(con,ripsForm.getNombreServicio()));
							ripsForm.setOpcionBusquedaServicio("nombre");
						}
						
						//se reinicia el estado de Búsqueda del servicio
						ripsForm.setEstadoBusquedaServicio("");
						
						int numeroRegistros=Integer.parseInt(ripsForm.getDatosRips("numRegistros")+"");
						logger.info("Número de servicios consultados=>"+numeroRegistros+" del servicio=>"+ripsForm.getCodigoCups());
						
						UtilidadBD.cerrarConexion(con);
						if(numeroRegistros==1)
						{	
							//Se editan los nuevos datos RIPS
							ripsForm.setDatosRips("solicitud_0",ripsForm.getRegistrosRangos("solicitud_"+ripsForm.getPosicionHashMap()));
							ripsForm.setDatosRips("cuenta_0",ripsForm.getRegistrosRangos("cuenta_"+ripsForm.getPosicionHashMap()));
							ripsForm.setDatosRips("paciente_0",ripsForm.getRegistrosRangos("codigo_paciente_"+ripsForm.getPosicionHashMap()));
							
							//	se verifica si es OTRO para añadir la fecha
							if((ripsForm.getRegistrosRangos("esOtro_"+ripsForm.getPosicionHashMap())+"").equals("true"))
							{
								ripsForm.setDatosRips("fecha_0",ripsForm.getRegistrosRangos("fecha_"+ripsForm.getPosicionHashMap()));
								ripsForm.setDatosRips("esOtro_0","true");
							}
							else
							{
								ripsForm.setDatosRips("esOtro_0","false");
							}
							
							//se consulta la tarifa del servicio
							String valores=rips.consultarValoresServicio(con,
									Integer.parseInt((ripsForm.getDatosRips("servicio_0")+"").split(ConstantesBD.separadorSplit)[2]),
									Integer.parseInt(ripsForm.getDatosRips("cuenta_0")+""));
							if(valores!=null)
							{
								ripsForm.setDatosRips("valor_total_0",valores.split(ConstantesBD.separadorSplit)[0]);
								ripsForm.setDatosRips("valor_copago_0",valores.split(ConstantesBD.separadorSplit)[1]);
							}
							
							//	se verifica que ya se haya guardado el servicio a la BD RIPS
							if((ripsForm.getRegistrosRangos("consecutivo_"+ripsForm.getPosicionHashMap())+"").equals("")||
									(ripsForm.getRegistrosRangos("consecutivo_"+ripsForm.getPosicionHashMap())+"").equals("null"))
							{
								logger.info("Se borra el servicio!!!!");
								//se vuelve y se borra el servicio
								ripsForm.setRegistrosRangos("servicio_"+ripsForm.getPosicionHashMap(),"");
							}
							
							return mapping.findForward("informacionRips");
						}	
						else
						{
							return mapping.findForward("seleccionServicio");
						}
					}
					//de lo contrario se hace un forward al formulario
					else
					{
						//Se editan los nuevos datos RIPS
						ripsForm.setDatosRips(new HashMap());
						ripsForm.setDatosRips("servicio_0",ripsForm.getRegistrosRangos("servicio_"+ripsForm.getPosicionHashMap()));
						ripsForm.setDatosRips("solicitud_0",ripsForm.getRegistrosRangos("solicitud_"+ripsForm.getPosicionHashMap()));
						ripsForm.setDatosRips("cuenta_0",ripsForm.getRegistrosRangos("cuenta_"+ripsForm.getPosicionHashMap()));
						ripsForm.setDatosRips("paciente_0",ripsForm.getRegistrosRangos("codigo_paciente_"+ripsForm.getPosicionHashMap()));
						
						//se verifica si es OTRO para añadir la fecha
						if((ripsForm.getRegistrosRangos("esOtro_"+ripsForm.getPosicionHashMap())+"").equals("true"))
						{
							ripsForm.setDatosRips("fecha_0",ripsForm.getRegistrosRangos("fecha_"+ripsForm.getPosicionHashMap()));
							ripsForm.setDatosRips("esOtro_0","true");
						}
						else
						{
							ripsForm.setDatosRips("esOtro_0","false");
						}
						
						//se consulta la tarifa del servicio
						String valores=rips.consultarValoresServicio(con,
								Integer.parseInt((ripsForm.getDatosRips("servicio_0")+"").split(ConstantesBD.separadorSplit)[2]),
								Integer.parseInt(ripsForm.getDatosRips("cuenta_0")+""));
						if(valores!=null)
						{
							ripsForm.setDatosRips("valor_total_0",valores.split(ConstantesBD.separadorSplit)[0]);
							ripsForm.setDatosRips("valor_copago_0",valores.split(ConstantesBD.separadorSplit)[1]);
						}
						
						//	se verifica que ya se haya guardado el servicio a la BD RIPS
						if((ripsForm.getRegistrosRangos("consecutivo_"+ripsForm.getPosicionHashMap())+"").equals("")||
								(ripsForm.getRegistrosRangos("consecutivo_"+ripsForm.getPosicionHashMap())+"").equals("null"))
						{
							logger.info("Se borra el servicio!!!!");
							//se vuelve y se borra el servicio
							ripsForm.setRegistrosRangos("servicio_"+ripsForm.getPosicionHashMap(),"");
						}
						
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("informacionRips");
					}
						
				}
				//CASO 4=> caso en el que hay o no solicitud, no tiene servicio asociado y no se ingresó ninguno para la búsqued
				else if(!ripsForm.isTieneServicio()&&ripsForm.getTipoSolicitud()==-1)
				{
					logger.info("CASO 4");
					
					//se verifica si es un registro Otro no existente
					if(ripsForm.getNumeroSolicitud()==0&&
						((ripsForm.getRegistrosRangos("servicio_"+ripsForm.getPosicionHashMap())+"").equals("")||
						(ripsForm.getRegistrosRangos("servicio_"+ripsForm.getPosicionHashMap())+"").equals("null"))&&
						ripsForm.getPosicionPadre()!=-1)
					{
						//se pasan los datos del padre al hijo
						int posRegistro=ripsForm.getPosicionHashMap();
						int posPadre=ripsForm.getPosicionPadre();
						ripsForm.setRegistrosRangos("fecha_"+posRegistro,ripsForm.getRegistrosRangos("fecha_"+posPadre));
						ripsForm.setRegistrosRangos("paciente_"+posRegistro,ripsForm.getRegistrosRangos("paciente_"+posPadre));
						ripsForm.setRegistrosRangos("tipo_num_id_"+posRegistro,ripsForm.getRegistrosRangos("tipo_num_id_"+posPadre));
						ripsForm.setRegistrosRangos("servicioEditado_"+posRegistro,ripsForm.getCodigoCups());
						ripsForm.setRegistrosRangos("tipo_"+posRegistro,ripsForm.getTipoSolicitud()+"");
						ripsForm.setRegistrosRangos("servicio_"+posRegistro,"");
						ripsForm.setRegistrosRangos("posPadre_"+posRegistro,ripsForm.getPosicionPadre()+"");
						ripsForm.setRegistrosRangos("cuenta_"+posRegistro,ripsForm.getRegistrosRangos("cuenta_"+posPadre));
						ripsForm.setRegistrosRangos("codigo_paciente_"+posRegistro,ripsForm.getRegistrosRangos("codigo_paciente_"+posPadre));
						ripsForm.setRegistrosRangos("rips_"+posRegistro,"");
						ripsForm.setRegistrosRangos("solicitud_"+posRegistro,"0");
						ripsForm.setRegistrosRangos("esOtro_"+posRegistro,"true");
						
						
					}
					
					//se revisa que se haya seleccionado un servicio/ sino se reinicia la pantalla
					if(ripsForm.getRegistrosRangos("servicio_"+ripsForm.getPosicionHashMap()).equals("")
							&&ripsForm.getEstadoBusquedaServicio().equals("informacionRips"))
					{
						ripsForm.setEstadoBusquedaServicio("");
					}
					
					//cuando es vacío quiere decir que se va a consultar el servicio desde el principio
					if(ripsForm.getEstadoBusquedaServicio().equals(""))
					{
						ripsForm.setNombreServicio("");
						ripsForm.setCodigoCups("");
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("busquedaServicio");
					}
					//cuando se va a consultar el servicio por código cups o por nombre
					else if(ripsForm.getEstadoBusquedaServicio().equals("buscarCodigo")||ripsForm.getEstadoBusquedaServicio().equals("buscarNombre"))
					{
						logger.info("Pasó por aquí!!!! en el estado de busqueda buscarCódigo/Nombre");
						if(ripsForm.getEstadoBusquedaServicio().equals("buscarCodigo"))
						{
							ripsForm.setDatosRips(rips.consultarServicios(con,ripsForm.getCodigoCups()));
							ripsForm.setOpcionBusquedaServicio("codigo");
						}
						else
						{
							ripsForm.setDatosRips(rips.consultarServicioXNombre(con,ripsForm.getNombreServicio()));
							ripsForm.setOpcionBusquedaServicio("nombre");
						}
						
						int numeroRegistros=Integer.parseInt(ripsForm.getDatosRips("numRegistros")+"");
						logger.info("Número de servicios consultados=>"+numeroRegistros+" del servicio=>"+ripsForm.getCodigoCups());
						
						
						if(numeroRegistros==1)
						{	
							//Se editan los nuevos datos RIPS
							ripsForm.setDatosRips("solicitud_0",ripsForm.getRegistrosRangos("solicitud_"+ripsForm.getPosicionHashMap()));
							ripsForm.setDatosRips("cuenta_0",ripsForm.getRegistrosRangos("cuenta_"+ripsForm.getPosicionHashMap()));
							ripsForm.setDatosRips("paciente_0",ripsForm.getRegistrosRangos("codigo_paciente_"+ripsForm.getPosicionHashMap()));
							
							if(ripsForm.getEstadoBusquedaServicio().equals("buscarNombre"))
								ripsForm.setCodigoCups((ripsForm.getDatosRips("servicio_0")+"").split(ConstantesBD.separadorSplit)[0]);
							
							//	se verifica si es OTRO para añadir la fecha
							if((ripsForm.getRegistrosRangos("esOtro_"+ripsForm.getPosicionHashMap())+"").equals("true"))
							{
								ripsForm.setDatosRips("fecha_0",ripsForm.getRegistrosRangos("fecha_"+ripsForm.getPosicionHashMap()));
								ripsForm.setDatosRips("esOtro_0","true");
							}
							else
							{
								ripsForm.setDatosRips("esOtro_0","false");
							}
							
							//se consulta la tarifa del servicio
							String valores=rips.consultarValoresServicio(con,
									Integer.parseInt((ripsForm.getDatosRips("servicio_0")+"").split(ConstantesBD.separadorSplit)[2]),
									Integer.parseInt(ripsForm.getDatosRips("cuenta_0")+""));
							if(valores!=null)
							{
								ripsForm.setDatosRips("valor_total_0",valores.split(ConstantesBD.separadorSplit)[0]);
								ripsForm.setDatosRips("valor_copago_0",valores.split(ConstantesBD.separadorSplit)[1]);
							}
							
							//	se verifica que ya se haya guardado el servicio a la BD RIPS
							if((ripsForm.getRegistrosRangos("consecutivo_"+ripsForm.getPosicionHashMap())+"").equals("")||
									(ripsForm.getRegistrosRangos("consecutivo_"+ripsForm.getPosicionHashMap())+"").equals("null"))
							{
								logger.info("Se borra el servicio!!!!");
								//se vuelve y se borra el servicio
								ripsForm.setRegistrosRangos("servicio_"+ripsForm.getPosicionHashMap(),"");
							}
							ripsForm.setEstadoBusquedaServicio("");
							UtilidadBD.cerrarConexion(con);
							return mapping.findForward("informacionRips");
						}	
						else
						{
							if(numeroRegistros>0)
								ripsForm.setEstadoBusquedaServicio("informacionRips");
							UtilidadBD.cerrarConexion(con);
							return mapping.findForward("seleccionServicio");
						}
					}
					//se direcciona al popUp de ingreso de la información rips
					else if(ripsForm.getEstadoBusquedaServicio().equals("informacionRips"))
					{
						logger.info("Pasó por aquí!!!! en el estado de busqueda información Rips");
						//Se editan los nuevos datos RIPS
						ripsForm.setDatosRips(new HashMap());
						ripsForm.setDatosRips("servicio_0",ripsForm.getRegistrosRangos("servicio_"+ripsForm.getPosicionHashMap()));
						ripsForm.setDatosRips("solicitud_0",ripsForm.getRegistrosRangos("solicitud_"+ripsForm.getPosicionHashMap()));
						ripsForm.setDatosRips("cuenta_0",ripsForm.getRegistrosRangos("cuenta_"+ripsForm.getPosicionHashMap()));
						ripsForm.setDatosRips("paciente_0",ripsForm.getRegistrosRangos("codigo_paciente_"+ripsForm.getPosicionHashMap()));
						
						logger.info("Servicio: "+ripsForm.getRegistrosRangos("servicio_"+ripsForm.getPosicionHashMap()));
						logger.info("Solicitud: "+ripsForm.getRegistrosRangos("solicitud_"+ripsForm.getPosicionHashMap()));
						logger.info("Cuenta: "+ripsForm.getRegistrosRangos("cuenta_"+ripsForm.getPosicionHashMap()));
						logger.info("codigo Paciente: "+ripsForm.getRegistrosRangos("codigo_paciente_"+ripsForm.getPosicionHashMap()));
						//se asignar el codigo CUPS consultado
						ripsForm.setCodigoCups((ripsForm.getDatosRips("servicio_0")+"").split(ConstantesBD.separadorSplit)[0]);
						
						//se verifica si es OTRO para añadir la fecha
						if((ripsForm.getRegistrosRangos("esOtro_"+ripsForm.getPosicionHashMap())+"").equals("true"))
						{
							ripsForm.setDatosRips("fecha_0",ripsForm.getRegistrosRangos("fecha_"+ripsForm.getPosicionHashMap()));
							ripsForm.setDatosRips("esOtro_0","true");
						}
						else
						{
							ripsForm.setDatosRips("esOtro_0","false");
						}
						
						//se consulta la tarifa del servicio
						String valores=rips.consultarValoresServicio(con,
								Integer.parseInt((ripsForm.getDatosRips("servicio_0")+"").split(ConstantesBD.separadorSplit)[2]),
								Integer.parseInt(ripsForm.getDatosRips("cuenta_0")+""));
						if(valores!=null)
						{
							ripsForm.setDatosRips("valor_total_0",valores.split(ConstantesBD.separadorSplit)[0]);
							ripsForm.setDatosRips("valor_copago_0",valores.split(ConstantesBD.separadorSplit)[1]);
						}
						
						//	se verifica que ya se haya guardado el servicio a la BD RIPS
						if((ripsForm.getRegistrosRangos("consecutivo_"+ripsForm.getPosicionHashMap())+"").equals("")||
								(ripsForm.getRegistrosRangos("consecutivo_"+ripsForm.getPosicionHashMap())+"").equals("null"))
						{
							logger.info("Se borra el servicio!!!!");
							//se vuelve y se borra el servicio
							ripsForm.setRegistrosRangos("servicio_"+ripsForm.getPosicionHashMap(),"");
						}
						ripsForm.setEstadoBusquedaServicio("");
						UtilidadBD.cerrarConexion(con);
						
						return mapping.findForward("informacionRips");
					}
					
				}
				return null;
		}
		else
		{
			//se consultan los datos RIPS
			ripsForm.setDatosRips(rips.consultarRegistroRips(con,Integer.parseInt(consecutivoRegistro)));
			
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("informacionRips");
		}
	}

	/**
	 * @param con
	 * @param ripsForm
	 * @param response
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, RipsForm ripsForm, HttpServletResponse response)  throws Exception
	{
		UtilidadBD.cerrarConexion(con);
		ripsForm.setEstado("busquedaRangos");
		response.sendRedirect(ripsForm.getLinkSiguiente());
		return null;
	}

	/**
	 * Método usaod para ordenar el listado de los rangos
	 * @param ripsForm
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionOrdenarRangos(RipsForm ripsForm, ActionMapping mapping, Connection con) throws SQLException
	{
		//columnas del listado
		String[] indices={
				"fecha_",
				"paciente_",
				"tipo_num_id_",
				"servicio_",
				"rips_",
				"servicioEditado_",
				"posPadre_",
				"esOtro_",
				"cuenta_",
				"codigo_paciente_",
				"tipo_",
				"solicitud_",
				"consecutivo_",
				"estado_"
			};
		
		int numeroElementos=Integer.parseInt(ripsForm.getRegistrosRangos("numRegistros")+"");
		logger.info("Número de registros en ordenacion=> "+numeroElementos);
		ripsForm.setRegistrosRangos(Listado.ordenarMapa(indices,
				ripsForm.getIndice(),
				ripsForm.getUltimoIndice(),
				ripsForm.getRegistrosRangos(),
				numeroElementos));
		
		ripsForm.setRegistrosRangos("numRegistros",numeroElementos+"");
		ripsForm.setUltimoIndice(ripsForm.getIndice());
		ripsForm.setEstado("busquedaRangos");
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método para consultar las solicitudes según los rangos
	 * @param mapping
	 * @param con
	 * @param ripsForm
	 * @param usu
	 * @return
	 */
	private ActionForward accionBusquedaRangos(ActionMapping mapping, Connection con, RipsForm ripsForm, UsuarioBasico usu) throws SQLException
	{
		//****CONSULTA DE LOS REGISTROS POR RANGOS************
		
		Rips rips=new Rips();
		ripsForm.setRegistrosRangos(rips.consultaRegistrosPorRangos(con,
				Integer.parseInt((ripsForm.getConvenio().split("-"))[0]),
				ripsForm.getFechaInicial(),
				ripsForm.getFechaFinal()));
		//se llenan los datos
		rips.setConvenio(Integer.parseInt((ripsForm.getConvenio().split("-"))[0]));
		rips.setFechaInicial(ripsForm.getFechaInicial());
		rips.setFechaFinal(ripsForm.getFechaFinal());
		
		
		//se hacen inserciones de registros que tienen los datos Rips llenos
		ripsForm.setRegistrosRangos(
				rips.verificacionRegistrosIniciales(con,
						ripsForm.getRegistrosRangos(),
						usu.getCodigoInstitucionInt())
					);
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método para consultar el convenio de la cuenta de cobro
	 * @param mapping
	 * @param con
	 * @param usu
	 * @param ripsForm
	 * @return
	 */
	private ActionForward accionBuscar(ActionMapping mapping, Connection con, UsuarioBasico usu, RipsForm ripsForm) throws SQLException 
	{
		//se instancia objeto para cargar la cuenta de cobro
		MovimientosCuentaCobro movimientosCxC=new MovimientosCuentaCobro();
		movimientosCxC.setNumCuentaCobro(Double.parseDouble(ripsForm.getNumeroCuentaCobro().equals("")?"0":ripsForm.getNumeroCuentaCobro()));
		int resp=movimientosCxC.cargarCuentaCobro(con,usu.getCodigoInstitucionInt());
		
		String convenio="";
		
		logger.info("Resultado de la cosnulta=>"+resp);
		//se verifica si la cuenta de cobro tiene convenio asociado
		if(resp>0&&
			(movimientosCxC.getEstado()==ConstantesBD.codigoEstadoCarteraGenerado||
			movimientosCxC.getEstado()==ConstantesBD.codigoEstadoCarteraRadicada || movimientosCxC.getEstado()==ConstantesBD.codigoEstadoCarteraAprobado))
		{
			convenio=movimientosCxC.getConvenio();
			ripsForm.setConvenio((convenio.split(ConstantesBD.separadorTags))[0]+"-"+(convenio.split(ConstantesBD.separadorTags))[1]);
		}
		else
		{
			convenio="0-0";
			ripsForm.setConvenio(convenio);
		}
		
		logger.info("Convenio=>"+convenio);
		
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método para consultar el contenido de un archivo
	 * y almacenarlo en un HashMap
	 * @param mapping
	 * @param usu
	 * @param con
	 * @param ripsForm
	 * @return
	 */
	private ActionForward accionDetalle(ActionMapping mapping, UsuarioBasico usu, Connection con, RipsForm ripsForm) throws SQLException, IPSException
	{
		Rips rips=new Rips();
		this.llenarMundo(ripsForm,rips);
		
		//se consulta el contenido del archivo
		ripsForm.setContenidoArchivo(
				rips.cargarArchivo(
						con,
						usu.getCodigoInstitucionInt(),
						ripsForm.getArchivo()
									)
						);
		
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("resumen");
	}

	/**
	 * @param mapping
	 * @param request
	 * @param con
	 * @param usu
	 * @param ripsForm
	 * @param ripsConFactura
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ActionForward accionGenerar(ActionMapping mapping, HttpServletRequest request, Connection con, UsuarioBasico usu, RipsForm ripsForm, String ripsConFactura) throws SQLException, IPSException 
	{
		Rips rips=new Rips();
		
		// verificar si es busqueda por numero de envio, en este caso llenar las variables de convenio y numero de codigo tarifario
		
		
		//llamar la funcion d consulta de ax_rips y guardar el numero de la factura en una variable global.
		// verificar si el campo de numero de envio es vacio, si no lo es es una consulta de interfaz
		
		
		if (!ripsForm.getNumeroEnvio().equals("")) // para el caso en que el parametro General Interfaz RIPS este activo
		{
			logger.info("\n\n  .....------:::::  INTERFAZ RIPS ACTIVA  :::::------...... \n\n");
			logger.info("NUMERO DE ENVIO--->"+ripsForm.getNumeroEnvio()+"<-----");
			//consultar el numero de factura referente al numero de envio dado	
			// cargar el numero de la factura en la variable correspondiente
			logger.info("*******NUMERO DE ENVÍO AX: "+ripsForm.getNumeroEnvio()+"**********************");
			
			ripsForm.setNroFacturaAx(rips.consultarNroFacInterfaz(ripsForm.getNumeroEnvio(), usu.getCodigoInstitucionInt()));
			
			if (UtilidadTexto.getBoolean(ripsForm.getNroFacturaAx("isError")+""))
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, ripsForm.getNroFacturaAx("descError")+"",ripsForm.getNroFacturaAx("descError")+"", false);
			
			
			int numRegistrosAx = Utilidades.convertirAEntero(ripsForm.getNroFacturaAx().get("numRegistros")+"");
			
			logger.info("Tamaño Mapa NroFacturaAx--->"+numRegistrosAx+"<--");
			
			// Consulto y adiciona el NIT de cada Convenio al mapa por medio del codigo interfaz referente a cada factura.
			for(int w=0; w < numRegistrosAx; w++)
			{
				ripsForm.getNroFacturaAx().put("nit_"+w,Utilidades.obtenerNitConveniodeCodInterfaz(con, ripsForm.getNroFacturaAx("AXCONVENIO_"+w).toString().trim()));
			}
			
			
			String tmp1 = "";
			String tmp2 = "";
			
			ripsForm.setFechaRemision(UtilidadFecha.getFechaActual(con));
			String Fecha = UtilidadFecha.conversionFormatoFechaABD(ripsForm.getFechaRemision());
			String Hora = UtilidadFecha.getHoraSegundosActual(con);
			
			String splif[] = Fecha.split("-"); 
			String splith[] = Hora.split(":");
			
			Fecha = splif[0]+splif[1]+splif[2];
			Hora = splith[0]+splith[1]+splith[2];
			
			logger.info("FECHA-->"+Fecha+"<--");
			logger.info("HORA -->"+Hora+"<--");
			
			
			for (int x=0; x < numRegistrosAx; x++)
			{
				if(!tmp2.equals(ripsForm.getNroFacturaAx("nit_"+x).toString()))				
					tmp2 = ripsForm.getNroFacturaAx("nit_"+x).toString().trim();
										
				if(!ripsForm.getNroFacturaAx().containsKey("ind_"+x))
				{	
					for (int y=0; y < numRegistrosAx; y++)
					{
						if(tmp2.equals(ripsForm.getNroFacturaAx("nit_"+y).toString().trim()))
						{
							ripsForm.getNroFacturaAx().put("ind_"+y,ConstantesBD.acronimoSi);
							
							tmp1 += ripsForm.getNroFacturaAx("AXFACTURA_"+y).toString().trim()+",";							
						}
					}
					
					tmp1 = tmp1.substring(0,tmp1.length()-1);
					
					ripsForm.setTipoCodigo(rips.consulartConvenioXNroFactura(con, Utilidades.convertirAEntero(ripsForm.getNumeroFactura())));
					
					// cargar el convenio
					ripsForm.setConvenio(Utilidades.obtenerCodigoConvenioDeCodInterfaz(ripsForm.getNroFacturaAx().get("AXCONVENIO_"+x).toString().trim())+"");
					logger.info(".....CARGAR CONVENIO-->"+ripsForm.getConvenio()+"<---");
					
					ripsForm.setNumeroFactura(tmp1);
					ripsForm.setNitEntidad(tmp2.trim());
					
					rips.clean();
					this.llenarMundo(ripsForm,rips);
					
					rips.setRipsConFactura(UtilidadTexto.getBoolean(ripsConFactura));
					rips.setLoginUsuario(usu.getLoginUsuario());
					
					
					logger.info("Entro a estado generar "+ripsForm.isEsFactura());
					HashMap resultados = new HashMap();
					resultados = rips.generarArchivos(con,usu.getCodigoInstitucionInt());
					resultados.put("terconvenio", rips.consultarNombreTercero(con, tmp2.trim()));
					
					ripsForm.setZip(rips.isZip());
					ripsForm.setNumeroRemision(rips.getNumeroRemision());
					ripsForm.setBackupArchivo(rips.getNomZip());
					
					resultados.put("descarga", ripsForm.getBackupArchivo());
					
					Utilidades.imprimirMapa(resultados);
					
					ripsForm.getResultadoArray().add(resultados);
					
					
					// ---------------actualizar el campo de estado en la interfaz de ax_rips 
					
					
				/*	
					rips.actualizarEstadoAxRips( ConstantesBDInterfaz.codigoEstadoProcesadoAxioma+"",
					ripsForm.getNumeroEnvio(), 
					Utilidades.obtenerCodigoInterfazConvenioDeCodigo(Utilidades.convertirAEntero(ripsForm.getConvenio())), 
					Fecha, Hora,
					usu.getCodigoInstitucionInt());
					*/
					//----------------------------------------
					
					// reseteo el tmp1 de facturas para hacer nueva busqueda por otro convenio
					
					tmp1 = "";
					
				}
			}

			Utilidades.imprimirArrayList(ripsForm.getResultadoArray());
			//return mapping.findForward("resumen");
		}
		else
		{
			// Consulto el Nit para el Convenio
			ripsForm.setNitEntidad(rips.consultarNitConvenio(con, Utilidades.convertirAEntero((ripsForm.getConvenio().split("-"))[0])));
			
			//llenar parámetros de generación rips
			this.llenarMundo(ripsForm,rips);
			//asignar el parámetro general 'Rips con factura'
			rips.setRipsConFactura(UtilidadTexto.getBoolean(ripsConFactura));
			rips.setLoginUsuario(usu.getLoginUsuario());
			
			//llamado a método del mundo para generar los archivos RIPS
			logger.info("Entro a estado generar "+ripsForm.isEsFactura());
			ripsForm.setResultados(rips.generarArchivos(con,usu.getCodigoInstitucionInt()));
			
			ripsForm.getResultados().put("terconvenio", rips.getObjConvenio().getNombre());
			
			ripsForm.getResultadoArray().add(ripsForm.getResultados());
			ripsForm.setZip(rips.isZip());
			
			logger.info("===>Se genero el ZIP: "+ripsForm.isZip());
			
		}
		ripsForm.setNumeroRemision(rips.getNumeroRemision());
		UtilidadBD.cerrarConexion(con);
		
		//se verifica si se encontraron registros en la generación RIPS
		if(rips.isHuboRegistros())
		{
			if(ripsForm.getResultados("error")!=null)
			{
				//cambiar el estado
				if(rips.getTipoRips().equals("Consultorios"))
					ripsForm.setEstado("busquedaRangos");
				
				return mapping.findForward("principal");
			}
			else
			{
				ripsForm.setHuboInconsistencias(rips.isHuboInconsistencias());
				ripsForm.setPathGeneracion(rips.getPathGeneracion());
				
				// verificar la creacion de los forecat
				if(rips.isGeneroArchvsForecat())
				{
					// si se generaron archivos forecat cargar el path de estos.
					ripsForm.setPathGeneracionForecat(rips.getPathGeneracionForecat());
					
					//cargar los resultados del mapa de generacion de forecat
					ripsForm.setResultadosForecat(rips.getResultadosForecat());
					
					// cargar variable de bandera de generacion de archivos forecat, para obtener
					ripsForm.setGeneroArchvsForecat(rips.isGeneroArchvsForecat());
				}				
				
				//generación de LOG (únicamente para CARTERA)
				if(rips.getTipoRips().equals("Cartera"))
					this.generarLog(ripsForm,usu);
				ripsForm.setBackupArchivo(rips.getNomZip());
				logger.info("===>Se genero el ZIP 2: "+ripsForm.isZip());
				return mapping.findForward("resumen");
			}
		}
		else
		{
			if(rips.getErrores().isEmpty())
			{
				ActionErrors errores = new ActionErrors();
				errores.add("no se enonctró información",new ActionMessage("errors.notEspecific","No se encontró información para generar RIPS"));
				saveErrors(request,errores);
			}
			else
				saveErrors(request, rips.getErrores());
			return mapping.findForward("principal");
		}
	}

	/**
	 * @param ripsForm
	 * @param usu
	 */
	private void generarLog(RipsForm ripsForm, UsuarioBasico usu) {
		
		String log;
		log="\n            ====ARCHIVOS RIPS GENERADOS===== ";
		
			//archivos básicos
			if(ripsForm.getResultados(ConstantesBD.ripsCT)!=null)
				log+="\n "+ConstantesBD.ripsCT+ripsForm.getNumeroRemision()+".txt";
			if(ripsForm.getResultados(ConstantesBD.ripsAF)!=null)
				log+="\n "+ConstantesBD.ripsAF+ripsForm.getNumeroRemision()+".txt";
			if(ripsForm.getResultados(ConstantesBD.ripsAD)!=null)
				log+="\n "+ConstantesBD.ripsAD+ripsForm.getNumeroRemision()+".txt";
			if(ripsForm.getResultados(ConstantesBD.ripsUS)!=null)
				log+="\n "+ConstantesBD.ripsUS+ripsForm.getNumeroRemision()+".txt";
			
			//archivos de seleccion
			if(ripsForm.getResultados(ConstantesBD.ripsAC)!=null)
				log+="\n "+ConstantesBD.ripsAC+ripsForm.getNumeroRemision()+".txt";
			if(ripsForm.getResultados(ConstantesBD.ripsAH)!=null)
				log+="\n "+ConstantesBD.ripsAH+ripsForm.getNumeroRemision()+".txt";
			if(ripsForm.getResultados(ConstantesBD.ripsAM)!=null)
				log+="\n "+ConstantesBD.ripsAM+ripsForm.getNumeroRemision()+".txt";
			if(ripsForm.getResultados(ConstantesBD.ripsAN)!=null)
				log+="\n "+ConstantesBD.ripsAN+ripsForm.getNumeroRemision()+".txt";
			if(ripsForm.getResultados(ConstantesBD.ripsAP)!=null)
				log+="\n "+ConstantesBD.ripsAP+ripsForm.getNumeroRemision()+".txt";
			if(ripsForm.getResultados(ConstantesBD.ripsAT)!=null)
				log+="\n "+ConstantesBD.ripsAT+ripsForm.getNumeroRemision()+".txt";
			if(ripsForm.getResultados(ConstantesBD.ripsAU)!=null)
				log+="\n "+ConstantesBD.ripsAU+ripsForm.getNumeroRemision()+".txt";
			
			//archivo inconsistencias
			if(ripsForm.getResultados(ConstantesBD.ripsInconsistencias)!=null)
				log+="\n "+ConstantesBD.ripsInconsistencias+ripsForm.getNumeroRemision()+".txt";
			
		
		log+="\n========================================================\n\n\n " ;	
		
		LogsAxioma.enviarLog(ConstantesBD.logRipsCodigo, log, ConstantesBD.tipoRegistroLogInsercion,usu.getLoginUsuario());
	}

	/**
	 * @param formulario rips
	 * @param objeto del mundo rips
	 */
	private void llenarMundo(RipsForm ripsForm, Rips rips) 
	{
		
		if(ripsForm.getNumeroEnvio().equals(""))
		{
			rips.setConvenio(Utilidades.convertirAEntero((ripsForm.getConvenio().split("-"))[0]));
			rips.setTipoCodigo(ripsForm.getTipoCodigo());
			rips.setFechaInicial(ripsForm.getFechaInicial());
			rips.setFechaFinal(ripsForm.getFechaFinal());
			rips.setFechaRemision(ripsForm.getFechaRemision());
			rips.setNumeroRemision(ripsForm.getNumeroRemision());
			// cargar el campo de busqueda de ax_rips, en caso de este estar tramitado los demas campos estan vacios o no interesa su contenido.
			rips.setNumeroEnvio(ripsForm.getNumeroEnvio());
			rips.setNitEntidad(ripsForm.getNitEntidad());
		}
		else
		{
			rips.setConvenio(Utilidades.convertirAEntero(ripsForm.getConvenio()));
			rips.setTipoCodigo(ripsForm.getTipoCodigo());
			rips.setNumeroRemision(ripsForm.getNumeroRemision());
			rips.setNumeroEnvio(ripsForm.getNumeroEnvio());
			rips.setNroFacturaAx(ripsForm.getNumeroFactura());
			rips.setNitEntidad(ripsForm.getNitEntidad());
			rips.setFechaRemision(ripsForm.getFechaRemision());
		}
		
		
		//estado vinculado con RIPS CARTERA
		if(ripsForm.getEstado().equals("generar"))
		{
			
			rips.setNumeroCuentaCobro(ripsForm.getNumeroCuentaCobro().equals("")?0:Double.parseDouble(ripsForm.getNumeroCuentaCobro()));
			rips.setSeleccionArchivos(ripsForm.getSeleccion());
			//campo que me diferencia si la generación es por factura o por cuenta de cobro
			rips.setEsFactura(ripsForm.isEsFactura());
		}
		//estado vinculado con RIPS CONSULTORIOS
		else if(ripsForm.getEstado().equals("generarConsultorios"))
		{
			
			//los archivo AC y AP se seleccionan automáticamente
			ripsForm.setSeleccion("AC","true");
			ripsForm.setSeleccion("AP","true");
			rips.setSeleccionArchivos(ripsForm.getSeleccion());
			rips.setNumeroFactura(ripsForm.getNumeroFactura());
			rips.setFechaFactura(ripsForm.getFechaFactura());
			//se define el tipo de RIPS a usar
			rips.setTipoRips("Consultorios");
			
			//actualización del listado de registros
			rips.setRegistrosRangos(ripsForm.getRegistrosRangos());
			
		}
		else if(ripsForm.getEstado().equals("detalle"))
		{
			
			rips.setNumeroCuentaCobro(ripsForm.getNumeroCuentaCobro().equals("")?0:Double.parseDouble(ripsForm.getNumeroCuentaCobro()));
			rips.setSeleccionArchivos(ripsForm.getSeleccion());
			//campo que me diferencia si la generación es por factura o por cuenta de cobro
			rips.setEsFactura(ripsForm.isEsFactura());
			rips.setConvenio(Utilidades.convertirAEntero((ripsForm.getConvenio().split("-"))[0]));
			// identificar si es un archivo forecat, para cambiar la ruta de exposicion del archivo
			if (ripsForm.isGeneroArchvsForecat())
				rips.setGeneroArchvsForecat(true);
			 
		}
		
	}
}
