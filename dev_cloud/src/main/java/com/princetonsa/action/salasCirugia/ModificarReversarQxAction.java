/*
 * Nov 30, 2005
 *
 */
package com.princetonsa.action.salasCirugia;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.RespuestaValidacion;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.Administracion.UtilidadesAdministracion;
import util.facturacion.UtilidadesFacturacion;
import util.salas.UtilidadesSalas;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.salasCirugia.ModificarReversarQxForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;
import com.princetonsa.mundo.salasCirugia.LiquidacionServicios;
import com.princetonsa.mundo.salasCirugia.MaterialesQx;
import com.princetonsa.mundo.salasCirugia.ModificarReversarQx;
import com.princetonsa.mundo.solicitudes.Solicitud;

/**
 * @author Sebasti�n G�mez R
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Modificar Reversar Liquidacion Qx.
 */
public class ModificarReversarQxAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ModificarReversarQxAction.class);
	
	/**
	 * M�todo encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
													ActionForm form,
													HttpServletRequest request,
													HttpServletResponse response ) throws Exception
													{
		Connection con=null;
		try{
			if (response==null); //Para evitar que salga el warning
			if(form instanceof ModificarReversarQxForm)
			{

				//SE ABRE CONEXION
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexi�n"+e.toString());
				}

				//OBJETOS A USAR ****************************************************************
				ModificarReversarQxForm modificarForm =(ModificarReversarQxForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");

				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				String estado=modificarForm.getEstado(); 
				logger.warn("estado Modificar Reversar Qx-->"+estado);
				//*************************************************************************************

				//SE REALIZAN LAS VALIDACIONES************************************************************
				if(estado.equals("empezar"))
				{
					ActionForward validaciones=this.validacionesUsuarios(con,paciente,modificarForm,mapping,request,logger,usuario);
					if(validaciones!=null)
					{
						UtilidadBD.closeConnection(con);	
						return validaciones;
					}
				}
				//*****************************************************************************************


				if(estado == null)
				{
					modificarForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Modificar Reversar Liquidacion Qx. (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				//******************************************************************************
				//*************ESTADOS PARA EL LISTADO DE ORDENES (MODIFICAR/REVERSAR)*********************
				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con,modificarForm,mapping,usuario,request);
				}
				else if (estado.equals("ordenarOrdenes"))
				{
					return accionOrdenarOrdenes(con,modificarForm,mapping);
				}
				else if (estado.equals("redireccion"))
				{
					return accionRedireccion(con,modificarForm,response,mapping,request);
				}
				else if (estado.equals("iniciarModificar"))
				{
					return accionIniciarModificar(con,modificarForm,mapping,usuario,request);
				}
				else if (estado.equals("modificar"))
				{
					return accionModificar(con,modificarForm,mapping,usuario,request);
				}
				else if (estado.equals("iniciarReversar"))
				{
					return accionIniciarReversar(con,modificarForm,mapping);
				}
				else if (estado.equals("reversar"))
				{
					return accionReversar(con,modificarForm,mapping,usuario,request);
				}
				//*************ESTADOS PARA LA CONSULTA DE LOG MODIFICACION/REVERSION QX****************
				else if(estado.equals("iniciarConsulta"))
				{
					return accionIniciarConsulta(con,modificarForm,mapping,usuario);
				}
				else if(estado.equals("consultar"))
				{
					return accionConsultar(con,modificarForm,mapping,usuario);
				}
				else if(estado.equals("ordenarConsulta"))
				{
					return accionOrdenarConsulta(con,modificarForm,mapping);
				}
				else if(estado.equals("detalleConsulta"))
				{
					return accionDetalleConsulta(con,modificarForm,mapping);
				}
				//*****************************************************************************
				else if (estado.equals("refrescar")) //usado como conexion de Reversar con Modificar
				{
					modificarForm.setEstado("reversar");
					this.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				//**************ESTADOS PARA FILTROS AJAX*********************************************
				else if (estado.equals("filtrarEspecialidades"))
				{
					return accionFiltrarEspecialidades(con,modificarForm,response);
				}
				//*************************************************************************************
				else
				{
					modificarForm.reset();
					logger.warn("Estado no valido dentro del flujo de Modificar Reversar Qx. (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
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
	
	
	/**
	 * M�todo implementado para filtrar las especialidades de un profesional de la salud
	 * @param con
	 * @param modificarForm
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltrarEspecialidades(Connection con, ModificarReversarQxForm modificarForm, HttpServletResponse response) 
	{
		String resultado = "<respuesta>" +
		"<infoid>" +
			"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
			"<id-select>codigoEspecialidad_"+modificarForm.getIndexProf()+"</id-select>" +
			"<id-arreglo>especialidad</id-arreglo>" +
		"</infoid>" ;
	
	
		//***********************ADICION DE LAS ESPECIALIDADES**********************************************************************
		//Se verifica si se ingres� profesional
		if(!modificarForm.getCodigoProfesional().equals(""))
			modificarForm.setAsocios("comboEspecialidad_"+modificarForm.getIndexProf(), Utilidades.obtenerEspecialidadesEnArray(con, Integer.parseInt(modificarForm.getCodigoProfesional()), ConstantesBD.codigoNuncaValido));
		//Si no se seleccion� profesional se quitan las especialidades
		else
			modificarForm.setAsocios("comboEspecialidades_"+modificarForm.getIndexProf(), new ArrayList<HashMap<String, Object>>());
		
		for(HashMap<String,Object> elemento:(ArrayList<HashMap<String, Object>>)modificarForm.getAsocios("comboEspecialidad_"+modificarForm.getIndexProf()))
		{
			resultado += "<especialidad>";
				resultado += "<codigo>"+elemento.get("codigoespecialidad")+"</codigo>";
				resultado += "<descripcion>"+elemento.get("nombreespecialidad")+"</descripcion>";
			resultado += "</especialidad>";
		}
		
		///****************************************************************************************************************
		
		resultado += "</respuesta>";
		
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
			logger.error("Error al enviar respuesta AJAX en accionFiltrarEspecialidades: "+e);
		}
		return null;
	}


	/**
	 * M�todo que consulta el detalle del LOG
	 * @param con
	 * @param modificarForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetalleConsulta(Connection con, ModificarReversarQxForm modificarForm, ActionMapping mapping) 
	{
		//variables auxiliares
		int pos = modificarForm.getCodigoRegistro();
		int codigo = Integer.parseInt(modificarForm.getOrdenes("codigo_"+pos)+"");
		
		//se toman datos generales
		modificarForm.setMotivo(modificarForm.getOrdenes("motivo_"+pos)+"");
		modificarForm.setCodigoTipoCambio(Integer.parseInt(modificarForm.getOrdenes("codigo_tipo_cambio_"+pos)+""));
		
		//se instancia objeto ModificarReversarQx
		ModificarReversarQx modificar = new ModificarReversarQx();
		
		modificarForm.setAsocios(modificar.getDetalleLog(con,codigo));
		
		//se asigna tama�o al mapa
		modificarForm.setNumRegistros(Integer.parseInt(modificarForm.getAsocios("numRegistros")+""));
		
		this.formatearValoresLOG(modificarForm);
		
		//Se consultan los materiales eseciales registrados desde la finalizaci�n del consumo
		modificarForm.setMaterialesEspeciales(UtilidadesSalas.obtenerListadoMaterialesEspeciales(con, modificarForm.getOrdenes("numero_solicitud_"+pos).toString(), "", true, false));
		
		this.cerrarConexion(con);
		return mapping.findForward("detalle");
	}


	/**
	 * M�todo que orden el listado de resultados en la Consulta LOG
	 * @param con
	 * @param modificarForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarConsulta(Connection con, ModificarReversarQxForm modificarForm, ActionMapping mapping) 
	{
		//columnas del listado
		String[] indices={
				"codigo_",
				"fecha_cambio_",
				"codigo_tipo_cambio_",
				"nombre_tipo_cambio_",
				"usuario_",
				"fecha_cirugia_",
				"orden_",
				"numero_solicitud_",
				"motivo_"
			};
		
		int numeroElementos=modificarForm.getNumOrdenes();
		
		//se pasan las fechas a Formato BD
		for(int i=0;i<numeroElementos;i++)
		{
			String[] fecha = (modificarForm.getOrdenes("fecha_cambio_"+i)+"").split(" ");
			modificarForm.setOrdenes("fecha_cambio_"+i,UtilidadFecha.conversionFormatoFechaABD(fecha[0])+" "+fecha[1]);
			modificarForm.setOrdenes("fecha_cirugia_"+i,UtilidadFecha.conversionFormatoFechaABD(modificarForm.getOrdenes("fecha_cirugia_"+i)+""));
		}
		
		
		modificarForm.setOrdenes(Listado.ordenarMapa(indices,
				modificarForm.getIndice(),
				modificarForm.getUltimoIndice(),
				modificarForm.getOrdenes(),
				numeroElementos));
		
		modificarForm.setOrdenes("numRegistros",numeroElementos+"");
		
		//se pasan las fechas a formato aplicacion
		for(int i=0;i<numeroElementos;i++)
		{
			String[] fecha = (modificarForm.getOrdenes("fecha_cambio_"+i)+"").split(" ");
			modificarForm.setOrdenes("fecha_cambio_"+i,UtilidadFecha.conversionFormatoFechaAAp(fecha[0])+" "+fecha[1]);
			modificarForm.setOrdenes("fecha_cirugia_"+i,UtilidadFecha.conversionFormatoFechaAAp(modificarForm.getOrdenes("fecha_cirugia_"+i)+""));
		}
		modificarForm.setUltimoIndice(modificarForm.getIndice());
		modificarForm.setEstado("consultar");
		
		this.cerrarConexion(con);
		return mapping.findForward("consulta");
	}


	/**
	 * M�todo que consulta los LOG de la modificacion/reversion de la
	 * liquidacion Qx.
	 * @param con
	 * @param modificarForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionConsultar(Connection con, ModificarReversarQxForm modificarForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//se asigna tama�o del pager
		modificarForm.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
		
		
		//Se instancia objeto de Modificacion/Reversion Qx.
		ModificarReversarQx modificar = new ModificarReversarQx();
		
		modificarForm.setOrdenes(modificar.busquedaGeneralLOG(
				con,
				modificarForm.getTipoCambio(),
				modificarForm.getUsuario(),
				modificarForm.getFechaInicial(),
				modificarForm.getFechaFinal(),
				modificarForm.getOrdenInicial(),
				modificarForm.getOrdenFinal(),
				modificarForm.getTipoIdentificacion(),
				modificarForm.getNumeroIdentificacion(),
				usuario.getCodigoCentroAtencion()));
		
		//se asigna el tama�o
		modificarForm.setNumOrdenes(Integer.parseInt(modificarForm.getOrdenes("numRegistros")+""));
		
		
		this.cerrarConexion(con);
		return mapping.findForward("consulta");
	}


	/**
	 * M�todo que postula la parametrizacion de la busqueda
	 * de LOGS
	 * @param con
	 * @param modificarForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionIniciarConsulta(Connection con, ModificarReversarQxForm modificarForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		// Se limpia el formulario
		modificarForm.reset();
		modificarForm.setInstitucion(usuario.getCodigoInstitucion());
		modificarForm.setCentroAtencion(usuario.getCodigoCentroAtencion()+"");
		
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}


	/**
	 * M�todo que realiza la reversi�n de la liquidacion Qx.
	 * @param con
	 * @param modificarForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionReversar(Connection con, ModificarReversarQxForm modificarForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{
		//variables auxiliares
		boolean exito = false;
		
		
		//se instancia objeto ModificarReversar
		ModificarReversarQx modificar = new ModificarReversarQx();
		
		//se cargan los datos al mundo
		this.llenarMundo(modificarForm,modificar,usuario);
		
		
		///******REVERSI�N DE LIQUIDACION*******************
		exito = modificar.reversar(con,modificarForm.getCirugiasSolicitud(),modificarForm.getAsocios());
		//**************************************************
		
		if(!exito)
		{
			ActionErrors errores = new ActionErrors(); 
	        errores.add("no ingreso/modifico/elimino", new ActionMessage("prompt.noSeGraboInformacion"));
	        saveErrors(request, errores);
		}
		else
			//se guarda LOG
			modificar.generarLogReversion(
				modificarForm.getCirugiasSolicitud(),
				modificarForm.getAsocios(),
				modificarForm.getEncabezadoSolicitud());
		
		
		this.cerrarConexion(con);
		return mapping.findForward("resumen");
	}


	/**
	 * M�todo que carga los datos del formulario al mundo
	 * @param modificarForm
	 * @param modificar
	 * @param usuario
	 */
	private void llenarMundo(ModificarReversarQxForm modificarForm, ModificarReversarQx modificar, UsuarioBasico usuario) 
	{
		modificar.setNumeroSolicitud(modificarForm.getNumeroSolicitud());
		modificar.setFechaCirugia(modificarForm.getHojaQx("fechaInicial")+"");
		modificar.setMotivo(modificarForm.getMotivo());
		modificar.setUsuario(usuario.getLoginUsuario());
		
	}


	/**
	 * M�todo que postula el inicio de la reversi�n, para ingresar
	 * el motivo de la reversi�n
	 * @param con
	 * @param modificarForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionIniciarReversar(Connection con, ModificarReversarQxForm modificarForm, ActionMapping mapping) 
	{
		
		//se inicializa el motivo***********
		modificarForm.setMotivo("");
		
		this.cerrarConexion(con);
		return mapping.findForward("principal");
		
	}


	


	/**
	 * M�todo implementado para realizar la modificaci�n de la liquidacion
	 * @param con
	 * @param modificarForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionModificar(Connection con, ModificarReversarQxForm modificarForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//variables auxiliares
		boolean exito = false;
		
		//Se instancia objeto ModificarReversarQx
		ModificarReversarQx modificarQx = new ModificarReversarQx();
		
		///se cargan los datos al mundo
		this.llenarMundo(modificarForm,modificarQx,usuario);
		
		//********MODIFICACION DE LIQUIDACION******************
		exito = modificarQx.modificar(con,
			modificarForm.getEncabezadoSolicitud(),
			modificarForm.getCirugiasSolicitud(),
			modificarForm.getAsocios());
		//*****************************************************
		
		if(!exito)
		{
			ActionErrors errores = new ActionErrors(); 
	        errores.add("no ingreso/modifico/elimino", new ActionMessage("prompt.noSeGraboInformacion"));
	        saveErrors(request, errores);
	        this.cerrarConexion(con);
		}
		else
		{
			//SE carga de nuevo el resumen
			accionIniciarModificar(con, modificarForm, mapping, usuario, request);
			//Se cambia el estado a "modificar"
			modificarForm.setEstado("modificar");
		}
		
		
		return mapping.findForward("principal");
		
		
	}


	/**
	 * M�todo implementado para postular los datos de liquidacion
	 * para su modificaci�n
	 * @param con
	 * @param modificarForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionIniciarModificar(Connection con, ModificarReversarQxForm modificarForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		///se limpia formulario
		int numeroSolicitud = modificarForm.getNumeroSolicitud();
		int estadoCuenta = modificarForm.getEstadoCuenta();
		boolean esPaquetizada = false;
		
		modificarForm.reset();
		modificarForm.setNumeroSolicitud(numeroSolicitud);
		modificarForm.setEstadoCuenta(estadoCuenta);
		modificarForm.setEstado("iniciarModificar");
		modificarForm.setUsuario(usuario.getLoginUsuario());
		//se asigna institucion en la cual se trabaja
		//liquidacionForm.setInstitucion(usuario.getCodigoInstitucion());
		
		//****CARGA DE ENCABEZADO SOLICITUD***********
		Solicitud solicitud = new Solicitud();
		try
		{
			solicitud.cargar(con,numeroSolicitud);
		}
		catch(SQLException e)
		{
			logger.error("Hubo error aqui=> "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.problemasBd", "errors.problemasBd", true);
		}
		this.llenarFormSeccionEncabezadoSol(modificarForm,solicitud);
		
		//SE VERIFICA SI SOLICITUD ES PAQUETIZADA***********
		if(UtilidadesFacturacion.esSolicitudPaquetizada(con, modificarForm.getNumeroSolicitud()+""))
		{
			//No se puede ni modificar ni reversar
			modificarForm.setPuedoModificar(false);
			modificarForm.setPuedoReversar(false);
			esPaquetizada = true;
		}
		
		//****CARGAR DETALLE CIRUG�AS DE LA SOLICITUD***********
		SolicitudesCx solicitudCx = new SolicitudesCx();
		HashMap servicioSolicitud = solicitudCx.cargarServiciosXSolicitudCx(con,numeroSolicitud+"",true);
		modificarForm.setCirugiasSolicitud(servicioSolicitud);
		modificarForm.setNumCirugiasSolicitud(Integer.parseInt(servicioSolicitud.get("numRegistros")+""));
		this.cargarAsociosXCirugia(con,modificarForm,usuario.getCodigoInstitucionInt(),esPaquetizada);
		
		//****CARGA DE HOJA QUIR�RGICA ***********************
		LiquidacionServicios mundoLiquidacionServicios = new LiquidacionServicios();
		mundoLiquidacionServicios.setCon(con);
		mundoLiquidacionServicios.setNumeroSolicitud(numeroSolicitud+"");
		mundoLiquidacionServicios.cargarDetalleOrden();
		this.llenarFormSeccionHojaQx(modificarForm,mundoLiquidacionServicios);
		
		//***CARGA DE HOJA DE ANESTESIA*********************
		this.llenarFormSeccionHojaAnes(modificarForm,mundoLiquidacionServicios);
		
		//*****CARGA DE LOS MATERIALES ESPECIALES*****************
		modificarForm.setMaterialesEspeciales(UtilidadesSalas.obtenerListadoMaterialesEspeciales(con, numeroSolicitud+"","",false,true));
		//******************************************************
		
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}


	/**
	 * M�todo implementado para consultar los asocios de cada cirugias
	 * @param con
	 * @param modificarForm
	 * @param codigoInstitucion 
	 * @param esPaquetizada 
	 */
	private void cargarAsociosXCirugia(Connection con, ModificarReversarQxForm modificarForm, int codigoInstitucion, boolean esPaquetizada) 
	{
		//Se carga el arreglo de los profesionales
		modificarForm.setProfesionales(UtilidadesAdministracion.obtenerProfesionales(con, codigoInstitucion, ConstantesBD.codigoNuncaValido, true, true, ConstantesBD.codigoNuncaValido));
		
		//Se instancia objeto de LiquidacionQx
		
		int numAsocios = 0;
		HashMap asocios = new HashMap();
		logger.info("�PUEDO REVERSAR ANTES DE VERIFICAR ASOCIO? "+modificarForm.isPuedoReversar());
		for(int i=0;i<modificarForm.getNumCirugiasSolicitud();i++)
		{
			
			//se consultan sus asocios
			asocios = LiquidacionServicios.consultarAsociosLiquidados(con, modificarForm.getCirugiasSolicitud("codigo_"+i).toString());
			
			//se toma el numero de asocios de cada cirugia
			numAsocios = Integer.parseInt(asocios.get("numRegistros")+"");
			modificarForm.setAsocios("numAsocios_"+i,numAsocios+"");
			
			
			///se adhieren los asocios al mapa de cirugiasdSolicitud
			for(int j=0;j<numAsocios;j++)
			{
				
				modificarForm.setAsocios("codigoAxiomaAsocio_"+i+"_"+j,asocios.get("consecutivo_"+j));
				modificarForm.setAsocios("nombreMedicoAsocio_"+i+"_"+j,asocios.get("nombreProfesional_"+j));
				modificarForm.setAsocios("codigoMedicoAsocio_"+i+"_"+j,asocios.get("codigoProfesional_"+j));
				modificarForm.setAsocios("codigoServicioAsocio_"+i+"_"+j,asocios.get("codigoServicio_"+j));
				modificarForm.setAsocios("nombreServicioAsocio_"+i+"_"+j,asocios.get("nombreServicio_"+j));
				modificarForm.setAsocios("nombreAsocio_"+i+"_"+j,asocios.get("nombreAsocio_"+j));
				modificarForm.setAsocios("codigoAsocio_"+i+"_"+j,asocios.get("codigoAsocio_"+j));
				modificarForm.setAsocios("acronimoAsocio_"+i+"_"+j,asocios.get("acronimoAsocio_"+j));
				modificarForm.setAsocios("tipoServicioAsocio_"+i+"_"+j,asocios.get("codigoTipoServicio_"+j));
				modificarForm.setAsocios("nombreTipoServicio_"+i+"_"+j,asocios.get("nombreTipoServicio_"+j));
				modificarForm.setAsocios("valorAsocio_"+i+"_"+j,asocios.get("valor_"+j));
				modificarForm.setAsocios("valorInicialAsocio_"+i+"_"+j,asocios.get("valor_"+j));
				modificarForm.setAsocios("codigoEspecialidad_"+i+"_"+j,asocios.get("codigoEspecialidad_"+j));
				modificarForm.setAsocios("nombreEspecialidad_"+i+"_"+j,asocios.get("nombreEspecialidad_"+j));
				//El combo de las especialidades solo aplica para asocios Honorarios 
				if(!asocios.get("codigoTipoServicio_"+j).toString().equals(ConstantesBD.codigoServicioSalaCirugia+"")&&
					!asocios.get("codigoTipoServicio_"+j).toString().equals(ConstantesBD.codigoServicioMaterialesCirugia+"")&&
					Utilidades.convertirAEntero(asocios.get("codigoEspecialidad_"+j).toString())<=0&&
					Utilidades.convertirAEntero(asocios.get("codigoProfesional_"+j).toString())>0)
					modificarForm.setAsocios("comboEspecialidad_"+i+"_"+j,Utilidades.obtenerEspecialidadesEnArray(con, Utilidades.convertirAEntero(asocios.get("codigoProfesional_"+j).toString()), ConstantesBD.codigoNuncaValido));
				else
					modificarForm.setAsocios("comboEspecialidad_"+i+"_"+j,new ArrayList<HashMap<String, Object>>());
				modificarForm.setAsocios("nombrePool_"+i+"_"+j,asocios.get("nombrePool_"+j));
				modificarForm.setAsocios("activoAsocio_"+i+"_"+j,asocios.get("activo_"+j));
				//Se consultan los convenois del asocio
				modificarForm.setAsocios("conveniosAsocio_"+i+"_"+j,
					ModificarReversarQx.cargarConveniosAsocio(con, 
						modificarForm.getNumeroSolicitud()+"", 
						modificarForm.getCirugiasSolicitud("codigoServicio_"+i).toString(), 
						asocios.get("consecutivo_"+j).toString(),
						asocios.get("codigoTipoServicio_"+j).toString()
					)
				);
				
				
				//Se verifica si se puede modificar si faltan asocios de honorarios por profesional o por especialidad
				if(
					!asocios.get("codigoTipoServicio_"+j).toString().equals(ConstantesBD.codigoServicioSalaCirugia+"")&&
					!asocios.get("codigoTipoServicio_"+j).toString().equals(ConstantesBD.codigoServicioMaterialesCirugia+"")&&
					(Utilidades.convertirAEntero(asocios.get("codigoEspecialidad_"+j).toString())<=0||Utilidades.convertirAEntero(asocios.get("codigoProfesional_"+j).toString())<=0)
					)
					modificarForm.setPuedoModificar(true);
				
				
				logger.info("******validaciones para asocio: "+asocios.get("consecutivo_"+j)+", de tipo servicio: "+asocios.get("codigoTipoServicio_"+j)+" ********************************");
				logger.info("esSolicitudServicioAsocioDistribuido? "+UtilidadesFacturacion.esSolicitudServicioAsocioDistribuido(con,asocios.get("consecutivo_"+j).toString(),asocios.get("codigoTipoServicio_"+j).toString()));
				//logger.info("esSolicitudServicioAsocioFacturado? "+UtilidadesFacturacion.esSolicitudServicioAsocioFacturado(con,asocios.get("consecutivo_"+j).toString(),asocios.get("codigoTipoServicio_"+j).toString()));
				//logger.info("puedo modificar? "+modificarForm.isPuedoModificar());
				//logger.info("esSolicitudServicioAsocioCargado? "+UtilidadesFacturacion.esSolicitudServicioAsocioCargado(con,asocios.get("consecutivo_"+j).toString(),asocios.get("codigoTipoServicio_"+j).toString()));
				//logger.info("esPaquetizada? "+esPaquetizada);
				logger.info("***********************************************************************************************************************************************");
				
				//Se verifica si el asocio se puede modificar
				if(
					//Si ya fue distribuido
					UtilidadesFacturacion.esSolicitudServicioAsocioDistribuido(
						con,  
						asocios.get("consecutivo_"+j).toString(),
						asocios.get("codigoTipoServicio_"+j).toString())
					||
					//Si ya fue facturado
					UtilidadesFacturacion.esSolicitudServicioAsocioFacturado(
						con, 
						asocios.get("consecutivo_"+j).toString(),
						asocios.get("codigoTipoServicio_"+j).toString())
					||
					//No se pod�a modificar desde antes
					!modificarForm.isPuedoModificar()
					||
					//Si no est� cargado no se puede modificat
					!UtilidadesFacturacion.esSolicitudServicioAsocioCargado(
							con, 
							asocios.get("consecutivo_"+j).toString(),
							asocios.get("codigoTipoServicio_"+j).toString())
					||
					//Si la solicitud est� paquetizada no se puede modificar asocio
					esPaquetizada
				)
				{
					modificarForm.setAsocios("puedoModificarAsocio_"+i+"_"+j,ConstantesBD.acronimoNo);
					//tampoco se puede reversar
					modificarForm.setPuedoReversar(false);
				}
				else
					modificarForm.setAsocios("puedoModificarAsocio_"+i+"_"+j,ConstantesBD.acronimoSi);
				
				
			}
			
			
		}
		
	}


	/**
	 * M�todo para llenar el formulario con los datos de la 
	 * hoja de anestesia
	 * @param modificarForm
	 * @param hojaAnes
	 */
	private void llenarFormSeccionHojaAnes(ModificarReversarQxForm modificarForm, LiquidacionServicios mundoLiquidacion) 
	{
		modificarForm.setHojaAnestesia("nombreTipoAnestesia",mundoLiquidacion.getDatosActoQx().get("nombreTipoAnestesia"));
		modificarForm.setHojaAnestesia("codigoTipoAnestesia",mundoLiquidacion.getDatosActoQx().get("codigoTipoAnestesia"));
		modificarForm.setHojaAnestesia("codigoAnestesiologo",mundoLiquidacion.getDatosActoQx().get("codigoAnestesiologo"));
		modificarForm.setHojaAnestesia("nombreAnestesiologo",mundoLiquidacion.getDatosActoQx().get("nombreAnestesiologo"));
		
	}

	/**
	 * M�todo para llenar el formulario con los datos de la 
	 * hoja quirurgica
	 * @param modificarForm
	 * @param hojaQx
	 */
	private void llenarFormSeccionHojaQx(ModificarReversarQxForm modificarForm, LiquidacionServicios mundoLiquidacion) 
	{
		modificarForm.setHojaQx("fechaInicial",mundoLiquidacion.getDatosActoQx().get("fechaInicialCx"));
		modificarForm.setHojaQx("horaInicial",mundoLiquidacion.getDatosActoQx().get("horaInicialCx"));
		modificarForm.setHojaQx("fechaFinal",mundoLiquidacion.getDatosActoQx().get("fechaFinalCx"));
		modificarForm.setHojaQx("horaFinal",mundoLiquidacion.getDatosActoQx().get("horaFinalCx"));
		if (UtilidadCadena.noEsVacio(mundoLiquidacion.getDatosActoQx().get("duracionCirugia")+""))
		{
			String[] duracion = mundoLiquidacion.getDatosActoQx().get("duracionCirugia").toString().split(":");
			modificarForm.setHojaQx("duracion",duracion[0]+" Horas "+duracion[1]+" Minutos");
		}
		else
			modificarForm.setHojaQx("duracion"," 0 Horas 0 Minutos");
		modificarForm.setHojaQx("sala",mundoLiquidacion.getDatosActoQx().get("codigoSala"));
		modificarForm.setHojaQx("nombreSala",mundoLiquidacion.getDatosActoQx().get("nombreSala"));
		modificarForm.setHojaQx("politrauma",mundoLiquidacion.getDatosActoQx().get("politraumatismo"));
	}

	/**
	 * M�todo para llenar el formulario con los datos del encabezado
	 * de la solicitud
	 * @param modificarForm
	 * @param solicitud
	 */
	private void llenarFormSeccionEncabezadoSol(ModificarReversarQxForm modificarForm, Solicitud solicitud) 
	{
		modificarForm.setEncabezadoSolicitud("orden",solicitud.getConsecutivoOrdenesMedicas()+"");
		modificarForm.setEncabezadoSolicitud("fechaOrden",solicitud.getFechaSolicitud());
		//modificarForm.setEncabezadoSolicitud("autorizacion",solicitud.getNumeroAutorizacion());
		modificarForm.setEncabezadoSolicitud("horaOrden",solicitud.getHoraSolicitud());
		
	}
	
	/**
	 * M�todo implementado para paginar el listado de ordenes
	 * @param con
	 * @param modificarForm
	 * @param response
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, ModificarReversarQxForm modificarForm, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) 
	{
		try
		{
			
		    this.cerrarConexion(con);
			response.sendRedirect(modificarForm.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion de ModificarReversarQxAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en ModificarReversarQxAction", "errors.problemasDatos", true);
		}
	}


	/**
	 * M�todo implementado para la ordenaci�n por columnas del listado
	 * de ordenes de cirugia que se van a liquidar
	 * @param con
	 * @param modificarForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarOrdenes(Connection con, ModificarReversarQxForm modificarForm, ActionMapping mapping) 
	{
		//columnas del listado
		String[] indices={
				"numero_solicitud_",
				"fecha_orden_",
				"orden_",
				"medico_solicita_",
				"fecha_cirugia_",
				"medico_responde_",
				"estado_pedido_"
			};
		
		int numeroElementos=modificarForm.getNumOrdenes();
		
		//se pasan las fechas a Formato BD
		for(int i=0;i<numeroElementos;i++)
		{
			String[] fecha = (modificarForm.getOrdenes("fecha_orden_"+i)+"").split(" ");
			modificarForm.setOrdenes("fecha_orden_"+i,UtilidadFecha.conversionFormatoFechaABD(fecha[0])+" "+fecha[1]);
			modificarForm.setOrdenes("fecha_cirugia_"+i,UtilidadFecha.conversionFormatoFechaABD(modificarForm.getOrdenes("fecha_cirugia_"+i)+""));
		}
		
		
		modificarForm.setOrdenes(Listado.ordenarMapa(indices,
				modificarForm.getIndice(),
				modificarForm.getUltimoIndice(),
				modificarForm.getOrdenes(),
				numeroElementos));
		
		modificarForm.setOrdenes("numRegistros",numeroElementos+"");
		
		//se pasan las fechas a formato aplicacion
		for(int i=0;i<numeroElementos;i++)
		{
			String[] fecha = (modificarForm.getOrdenes("fecha_orden_"+i)+"").split(" ");
			modificarForm.setOrdenes("fecha_orden_"+i,UtilidadFecha.conversionFormatoFechaAAp(fecha[0])+" "+fecha[1]);
			modificarForm.setOrdenes("fecha_cirugia_"+i,UtilidadFecha.conversionFormatoFechaAAp(modificarForm.getOrdenes("fecha_cirugia_"+i)+""));
		}
		modificarForm.setUltimoIndice(modificarForm.getIndice());
		modificarForm.setEstado("empezar");
		
		this.cerrarConexion(con);
		return mapping.findForward("listadoOrdenes");
	}
	
	/**
	 * M�todo implementado para preparar los datos al inicio
	 * de la opcion Modificar/Reversar de la funcionalidad Modificar-Reversar Liquidacion Qx.
	 * @param con
	 * @param modificarForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, ModificarReversarQxForm modificarForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//se limpia formulario
		int id = modificarForm.getIdCuenta();
		int estadoCuenta = modificarForm.getEstadoCuenta();
		modificarForm.reset();
		modificarForm.setEstado("empezar");
		modificarForm.setIdCuenta(id);
		modificarForm.setEstadoCuenta(estadoCuenta);
		modificarForm.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
		
		
		//se instancia objeto MaterialesQx (REUTILIZACI�N)
		MaterialesQx liquidacion=new MaterialesQx();
		//se consultan las ordenes de cirugia de la cuenta 
		modificarForm.setOrdenes(liquidacion.cargarOrdenesCirugia(con,modificarForm.getIdCuenta(),"modificar/reversar"));
		
		//se asigna el tama�o del mapa
		modificarForm.setNumOrdenes(Integer.parseInt(modificarForm.getOrdenes("numRegistros")+""));
		
		
		//segun numero de ordenes se lanza el forward indicado
		if(modificarForm.getNumOrdenes()==1)
		{
			
			//se toma la �nica solicitud encontrada
			modificarForm.setNumeroSolicitud(Integer.parseInt(modificarForm.getOrdenes("numero_solicitud_0")+""));
			//se cambia el estado
			modificarForm.setEstado("empezarModificar");
			//cuando solo hay una solicitud se env�a directo a inicio de liquidaci�n
			return accionIniciarModificar(con,modificarForm,mapping,usuario,request);
		}
		else
		{
			this.cerrarConexion(con);
			//cuando no hay o hay varias solicitudes se env�a al listado de ordenes
			return mapping.findForward("listadoOrdenes");
		}
	}

	

	
	/**
	 * M�todo para formatear los valores del listado de ordenes
	 * @param modificarForm
	 */
	private void formatearValoresLOG(ModificarReversarQxForm modificarForm) 
	{
		int numRegistros = Integer.parseInt(modificarForm.getAsocios("numRegistros")+"");
		
		for(int i=0;i<numRegistros;i++)
		{
			//VALOR INICIAL *** se formatea el valor 
			modificarForm.setAsocios("valor_inicial_"+i,"$ "+
					UtilidadTexto.formatearValores(modificarForm.getAsocios("valor_inicial_"+i)+"",2,true,true));
			//VALOR MODIFICADO *** se formatea el valor 
			modificarForm.setAsocios("valor_modificado_"+i,"$ "+
					UtilidadTexto.formatearValores(modificarForm.getAsocios("valor_modificado_"+i)+"",2,true,true));
		}
		
	}
	
	
	/**
	 * M�todo usado para realizar las validaciones generales de la funcionalidad
	 * @param con
	 * @param paciente
	 * @param modificarForm
	 * @param mapping
	 * @param request
	 * @param logger
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward validacionesUsuarios(
			Connection con, PersonaBasica paciente, 
			ModificarReversarQxForm modificarForm, 
			ActionMapping mapping, 
			HttpServletRequest request, 
			Logger logger, UsuarioBasico usuario)  
	{
		//***************VALIDACIONES************************************************
		RespuestaValidacion resp = UtilidadValidacion.esValidoPacienteCargado(con, paciente);
		
		if(!resp.puedoSeguir)
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, resp.textoRespuesta, resp.textoRespuesta, true);
		
		int idCuenta = paciente.getCodigoCuenta();
		//Se carga la cuenta del paciente 
		Cuenta cuenta=new Cuenta();
		try 
		{
			cuenta.cargarCuenta(con,idCuenta+"");
		} 
		catch (Exception e) 
		{
			logger.error("Error al cargar cuenta en validacionesUsuarios de ModificarReversarQxAction: "+e);
			e.printStackTrace();
		}
		
		/**
		 * Validar concurrencia
		 * Si ya est� en proceso de facturaci�n, no debe dejar entrar
		 **/
		if(UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), "") )
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoFact", "error.facturacion.cuentaEnProcesoFact", true);
		}
		/**
		 * Validar concurrencia
		 * Si ya est� en proceso de distribucion, no debe dejar entrar
		 **/
		else if(UtilidadValidacion.estaEnProcesoDistribucion(con, paciente.getCodigoPersona(), usuario.getLoginUsuario()) )
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoDistribucion", "error.facturacion.cuentaEnProcesoDistribucion", true);
		}
		
		
		//se asigna el Id de la cuenta al formulario
		modificarForm.setIdCuenta(Integer.parseInt(cuenta.getIdCuenta()));
		//se asigna el estado de la cuenta al formulario
		modificarForm.setEstadoCuenta(Integer.parseInt(cuenta.getCodigoEstadoCuenta()));
		
		//--------------------------------------------------------------------------------------------------
		//modificado por tarea 73190 solicitada por oscar cely
		//verificar que la cuenta es Hospitalizacion o Ambulatorios con Tipo Paciente Cirugia Ambulatoria
		/*
		if(Integer.parseInt(cuenta.getCodigoViaIngreso())==ConstantesBD.codigoViaIngresoConsultaExterna)
		{
			request.setAttribute("arrayErrores","no");
			request.setAttribute("codigoDescripcionError","error.salasCirugia.viaNoHospitalizacionNoAmbulatorio");
			
			 this.cerrarConexion(con);
			return mapping.findForward("paginaError");
		}*/
		//--------------------------------------------------------------------------------------------------
		
		//***************************************************************************
		return null;
	}
	
	/**
	 * M�todo en que se cierra la conexi�n (Buen manejo
	 * recursos), usado ante todo al momento de hacer un forward
	 * @param con Conexi�n con la fuente de datos
	 */
	public void cerrarConexion (Connection con)
	{
	    try
		{
	        UtilidadBD.cerrarConexion(con);
	    }
	    catch(Exception e)
		{
	        logger.error(e+"Error al tratar de cerrar la conexion con la fuente de datos. \n Excepcion: " +e.toString());
	    }
	}
}
