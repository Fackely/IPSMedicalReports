/*
 * Dic 06, -2005
 *
 */
package com.princetonsa.action.salasCirugia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.salas.UtilidadesSalas;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.facturacion.CargosDirectosCxDytForm;
import com.princetonsa.actionform.salasCirugia.ConsultaLiquidacionQxForm;
import com.princetonsa.actionform.salasCirugia.LiquidacionServiciosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;
import com.princetonsa.mundo.resumenAtenciones.EstadoCuenta;
import com.princetonsa.mundo.salasCirugia.LiquidacionServicios;
import com.princetonsa.mundo.salasCirugia.MaterialesQx;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.pdf.LiquidacionServiciosPdf;

/**
 *   @author Sebastián Gómez R
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Consulta Liquidacion Qx.
 */
public class ConsultaLiquidacionQxAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ConsultaLiquidacionQxAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
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
		if(form instanceof ConsultaLiquidacionQxForm)
		{
			
			//SE ABRE CONEXION
			try
			{
				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo abrir la conexión"+e.toString());
			}
						
			//OBJETOS A USAR ****************************************************************
			ConsultaLiquidacionQxForm liquidacionForm =(ConsultaLiquidacionQxForm)form;
			HttpSession session=request.getSession();		
			UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
			
			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			String estado=liquidacionForm.getEstado(); 
			logger.warn("estado Consulta Liquidación Qx-->"+estado);
			//*************************************************************************************
			
			//SE REALIZAN LAS VALIDACIONES************************************************************
			if(estado.equals("empezarPaciente"))
			{
				ActionForward validaciones=this.validacionesUsuarios(con,paciente,mapping,request,logger);
				if(validaciones!=null)
					return validaciones;
			}
			//*****************************************************************************************
			
			
			if(estado == null)
			{
				liquidacionForm.reset();	
				logger.warn("Estado no valido dentro del flujo de Liquidacion Qx. (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				 UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
			//******************************************************************************
			//******************ESTADOS OPCION POR PACIENTE*******************************
			else if (estado.equals("empezarPaciente"))
			{
				return accionEmpezarPaciente(con,liquidacionForm,mapping,paciente,usuario);
			}
			else if (estado.equals("redireccion"))
			{
				return accionRedireccion(con,liquidacionForm,response,mapping,request);
			}
			else if (estado.equals("ordenar"))
			{
				return accionOrdenarOrdenes(con,liquidacionForm,mapping);
			}
			else if (estado.equals("detalle")) //Detalle cuanod apenas se selecciona la solicitud
			{
				return accionDetalle(con,liquidacionForm,mapping,request,usuario,paciente);
			}
			else if (estado.equals("detalleConvenio")) //Detalle cuando se selecciona el convenio de una solicitud
			{
				return cargarDetalleSolicitud(con,liquidacionForm,mapping,request,liquidacionForm.getPosConvenio(),usuario,paciente);
			}
			else if (estado.equals("imprimir"))
			{
				return accionImprimir(con, liquidacionForm, usuario, mapping, request, paciente);
			}
			//*****************ESTADOS OPCION POR TODOS************************************
			else if (estado.equals("empezarTodos"))
			{
				return accionEmpezarTodos(con,liquidacionForm,mapping,usuario);
			}
			else if (estado.equals("consultar"))
			{
				return accionConsultar(con,liquidacionForm,mapping,usuario);
			}
			else if (estado.equals("volverListado"))
			{
				UtilidadBD.closeConnection(con);
				return mapping.findForward("listadoOrdenes");
			}
			//*****************************************************************************
			else
			{
				liquidacionForm.reset();
				logger.warn("Estado no valido dentro del flujo de Liquidacion Qx. (null) ");
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
	 * Método para realizar la impresion de la liquidación
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionImprimir(Connection con, ConsultaLiquidacionQxForm forma, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request, PersonaBasica paciente) 
	{
		
		UtilidadBD.closeConnection(con);
		///se edita nombre del archivo PDF
		String nombreArchivo;
    	Random r=new Random();
    	nombreArchivo="/liquidacionServicios" + r.nextInt()  +".pdf";
    	
    	
    	LiquidacionServiciosPdf.pdfConsultaLiquidacionServicios(ValoresPorDefecto.getFilePath()+nombreArchivo, forma, usuario, request, paciente);
    	
    	request.setAttribute("nombreArchivo", nombreArchivo);
    	request.setAttribute("nombreVentana", "Liquidación Servicios");
    	return mapping.findForward("abrirPdf");
	}
	
	/**
	 * Método que realiza la búsqueda avanzada de las ordenes liquidadas
	 * @param con
	 * @param liquidacionForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionConsultar(Connection con, ConsultaLiquidacionQxForm liquidacionForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		
		//se asigna tamaño del pager
		liquidacionForm.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
		
		
		//Se instancia objeto MaterialesQX (REUTILIZACION)
		MaterialesQx materiales = new MaterialesQx();
		
		//consulta de las ordenes del paciente
		liquidacionForm.setOrdenes(materiales.cargarOrdenesCirugia(
				con,
				liquidacionForm.getOrdenInicial(),
				liquidacionForm.getOrdenFinal(),
				liquidacionForm.getFechaOrdenInicial(),
				liquidacionForm.getFechaOrdenFinal(),
				liquidacionForm.getFechaCxInicial(),
				liquidacionForm.getFechaCxFinal(),
				liquidacionForm.getMedico(),
				usuario.getCodigoCentroAtencion()));
		
		
		//se asigna el tamaño del mapa
		liquidacionForm.setNumOrdenes(Integer.parseInt(liquidacionForm.getOrdenes("numRegistros")+""));
		
		this.cerrarConexion(con);
		return mapping.findForward("listadoOrdenes");
	}

	/**
	 * Método que incializa los datos de la forma para postular el formulario de captura
	 * de busqueda
	 * @param con
	 * @param liquidacionForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarTodos(Connection con, ConsultaLiquidacionQxForm liquidacionForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//Se limpia formulario
		liquidacionForm.reset();
		//se almacena la institucion del usuario
		liquidacionForm.setInstitucion(usuario.getCodigoInstitucion());
		
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método que carga el detalle de una orden liquidada para postular
	 * su información al usuario
	 * @param con
	 * @param liquidacionForm
	 * @param mapping
	 * @param request
	 * @param usuario 
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionDetalle(Connection con, ConsultaLiquidacionQxForm liquidacionForm, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		
		
		//Se consulta el número de convenios de la solicitud
		liquidacionForm.setConveniosSol(UtilidadesFacturacion.cargarConveniosSolicitud(con, liquidacionForm.getNumeroSolicitud()+""));
		
		
		//Si solo es un convenio entonces se lleva directo al detalle de la orden
		if(Integer.parseInt(liquidacionForm.getConveniosSol("numRegistros").toString())==1)
		{
			liquidacionForm.setPosConvenio(0);
			return cargarDetalleSolicitud(con,liquidacionForm,mapping,request,0,usuario,paciente);
		}
		//Si hay mas convenios se lleva al forrward
		else
		{
			UtilidadBD.closeConnection(con);
			return mapping.findForward("conveniosSolicitud");
		}
	}

	
	/**
	 * Método que carga el detalle de la solicitud despues de saber a cual convenio se ha elegido
	 * @param con
	 * @param liquidacionForm
	 * @param mapping
	 * @param request
	 * @param pos
	 * @param usuario 
	 * @param paciente 
	 * @return
	 */
	private ActionForward cargarDetalleSolicitud(Connection con, ConsultaLiquidacionQxForm liquidacionForm, ActionMapping mapping, HttpServletRequest request, int pos, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		int numeroSolicitud = liquidacionForm.getNumeroSolicitud();
		HashMap conveniosSol = liquidacionForm.getConveniosSol();
		HashMap ordenes = liquidacionForm.getOrdenes();
		int numOrdenes = liquidacionForm.getNumOrdenes();
		int maxPageItems = liquidacionForm.getMaxPageItems();
		
		liquidacionForm.reset();
		liquidacionForm.setNumeroSolicitud(numeroSolicitud);
		liquidacionForm.setConveniosSol(conveniosSol);
		liquidacionForm.setOrdenes(ordenes);
		liquidacionForm.setNumOrdenes(numOrdenes);
		liquidacionForm.setMaxPageItems(maxPageItems);
		liquidacionForm.setEstado("detalle");
		
		//**CARGA DE ID CUENTA Y NOMBRE CONVENIO************
		liquidacionForm.setIdCuenta(Utilidades.getCuentaSolicitud(con,numeroSolicitud));
		liquidacionForm.setConvenio(liquidacionForm.getConveniosSol("nombreConvenio_"+pos).toString());
		
		//***CARGA DE NOMBRE DEL PACIENTE A TRAVÉS DE LA CUENTA*************
		try
		{
			//se instancia objeto Cuenta
			Cuenta cuenta = new Cuenta();
			cuenta.cargarCuenta(con,liquidacionForm.getIdCuenta()+"");
			liquidacionForm.setNombrePaciente(cuenta.getPaciente().getNombrePersona());
			
			paciente.setCodigoPersona(cuenta.getPaciente().getCodigoPersona());
			UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
			
		}
		catch(Exception e)
		{
			logger.error("Error cargando cuenta en accionDetalle de ConsultaLiquidacionQxAction : "+e);
			liquidacionForm.setNombrePaciente("");
		}
		
		//*******************************************************************
		
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
		this.llenarFormSeccionEncabezadoSol(liquidacionForm,solicitud);
		
		//****CARGAR DETALLE CIRUGÍAS DE LA SOLICITUD***********
		SolicitudesCx solicitudCx = new SolicitudesCx();
		HashMap servicioSolicitud = solicitudCx.cargarServiciosXSolicitudCx(con,numeroSolicitud+"",false);
		liquidacionForm.setCirugiasSolicitud(servicioSolicitud);
		liquidacionForm.setNumCirugiasSolicitud(Integer.parseInt(servicioSolicitud.get("numRegistros")+""));
		this.cargarAsociosXCirugia(con,liquidacionForm,pos);
		
		//****CARGA DE HOJA QUIRÚRGICA ***********************
		LiquidacionServicios mundoLiquidacionServicios = new LiquidacionServicios();
		mundoLiquidacionServicios.setCon(con);
		mundoLiquidacionServicios.setNumeroSolicitud(numeroSolicitud+"");
		mundoLiquidacionServicios.cargarDetalleOrden();
		this.llenarFormSeccionHojaQx(liquidacionForm,mundoLiquidacionServicios);
		
		//***CARGA DE HOJA DE ANESTESIA**************
		this.llenarFormSeccionHojaAnes(liquidacionForm,mundoLiquidacionServicios);
		
		
		this.cerrarConexion(con);
		return mapping.findForward("detalle");
	}

	/**
	 * Método implementado para ordenar el pager
	 * por la columna seleccionada
	 * @param con
	 * @param liquidacionForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarOrdenes(Connection con, ConsultaLiquidacionQxForm liquidacionForm, ActionMapping mapping) 
	{
		//columnas del listado
		String[] indices={
				"numero_solicitud_",
				"fecha_orden_",
				"orden_",
				"medico_solicita_",
				"fecha_cirugia_",
				"medico_responde_",
				"nombre_paciente_",
				"estado_pedido_"
			};
		
		int numeroElementos=liquidacionForm.getNumOrdenes();
		
		//se pasan las fechas a Formato BD
		for(int i=0;i<numeroElementos;i++)
		{
			String[] fecha = (liquidacionForm.getOrdenes("fecha_orden_"+i)+"").split(" ");
			liquidacionForm.setOrdenes("fecha_orden_"+i,UtilidadFecha.conversionFormatoFechaABD(fecha[0])+" "+fecha[1]);
			liquidacionForm.setOrdenes("fecha_cirugia_"+i,UtilidadFecha.conversionFormatoFechaABD(liquidacionForm.getOrdenes("fecha_cirugia_"+i)+""));
		}
		
		
		liquidacionForm.setOrdenes(Listado.ordenarMapa(indices,
				liquidacionForm.getIndice(),
				liquidacionForm.getUltimoIndice(),
				liquidacionForm.getOrdenes(),
				numeroElementos));
		
		liquidacionForm.setOrdenes("numRegistros",numeroElementos+"");
		
		//se pasan las fechas a formato aplicacion
		for(int i=0;i<numeroElementos;i++)
		{
			String[] fecha = (liquidacionForm.getOrdenes("fecha_orden_"+i)+"").split(" ");
			liquidacionForm.setOrdenes("fecha_orden_"+i,UtilidadFecha.conversionFormatoFechaAAp(fecha[0])+" "+fecha[1]);
			liquidacionForm.setOrdenes("fecha_cirugia_"+i,UtilidadFecha.conversionFormatoFechaAAp(liquidacionForm.getOrdenes("fecha_cirugia_"+i)+""));
		}
		liquidacionForm.setUltimoIndice(liquidacionForm.getIndice());
		//liquidacionForm.setEstado("empezar");
		
		this.cerrarConexion(con);
		return mapping.findForward("listadoOrdenes");
	}

	/**
	 * Método implementado para ir a la siguiente página del pager
	 * @param con
	 * @param liquidacionForm
	 * @param response
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, ConsultaLiquidacionQxForm liquidacionForm, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) 
	{
		try
		{
			
		    this.cerrarConexion(con);
			response.sendRedirect(liquidacionForm.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion de ConsultaLiquidacionQxAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en ConsultaLiquidacionQxAction", "errors.problemasDatos", true);
		}
	}

	/**
	 * Método que consulta las ordenes liquidadas del paciente cargado en sesión
	 * @param con
	 * @param liquidacionForm
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarPaciente(Connection con, ConsultaLiquidacionQxForm liquidacionForm, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario) 
	{
		//Se limpia formulario
		liquidacionForm.reset();
		
		int idCuenta = paciente.getCodigoCuenta();
		// davgommo: incidencia 6487, se pedia quitar la valoracion de cuenta activa, pero no existia codigo para el manejo de pacientes en ese estado
				if (idCuenta==0 && paciente.getCodigoIngreso()==0)
				{
				String cuenta="SELECT cu.id FROM manejopaciente.cuentas cu WHERE cu.codigo_paciente="+ paciente.getCodigoPersona();	
				try{
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cuenta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
				if(rs.next())
					{idCuenta = rs.getInt("id");}
				pst.close();
				rs.close();
				
				}
				
				catch (Exception e){
					
				}
				}
		
		//se asigna tamaño del pager
		liquidacionForm.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
	
		
		//Se instancia objeto MaterialesQX (REUTILIZACION)
		MaterialesQx materiales = new MaterialesQx();
		
		//consulta de las ordenes del paciente
		liquidacionForm.setOrdenes(materiales.cargarOrdenesCirugia(con,idCuenta,"consultaLiquidacion"));
		
		
		//se asigna el tamaño del mapa
		liquidacionForm.setNumOrdenes(Integer.parseInt(liquidacionForm.getOrdenes("numRegistros")+""));
		
		this.cerrarConexion(con);
		return mapping.findForward("listadoOrdenes");
	}
	
	
	
	
	/**
	 * Método implementado para consultar los asocios de cada cirugias
	 * @param con
	 * @param liquidacionForm
	 * @param pos 
	 */
	private void cargarAsociosXCirugia(Connection con, ConsultaLiquidacionQxForm liquidacionForm, int pos) 
	{
		//Se instancia objeto de EstadoCuenta
		EstadoCuenta mundoEstadoCuenta = new EstadoCuenta();
		
		int numAsocios = 0;
		HashMap asocios = new HashMap();
		double totalOrden = 0;
		double totalCirugia = 0;
		
		
		for(int i=0;i<liquidacionForm.getNumCirugiasSolicitud();i++)
		{
			
			//se consultan sus asocios
			asocios = mundoEstadoCuenta.cargarDetalleCargoCirugia(
				con, 
				liquidacionForm.getNumeroSolicitud()+"", 
				liquidacionForm.getConveniosSol("subCuenta_"+pos).toString(), 
				liquidacionForm.getCirugiasSolicitud("codigoServicio_"+i).toString(),
				true); //se muestra la paqeuitzacion
			
			//se toma el numero de asocios de cada cirugia
			numAsocios = Integer.parseInt(asocios.get("numRegistros")+"");
			liquidacionForm.setAsocios("numAsocios_"+i,numAsocios+"");
			
			totalCirugia = 0;
			
			///se adhieren los asocios al mapa de cirugiasdSolicitud
			for(int j=0;j<numAsocios;j++)
			{
				
				/**
				 * estadoCuentaForm.setDetalleSolicitud("totalCargo_"+i+"_"+j, asociosSolicitud.get("totalCargo_"+j));
				estadoCuentaForm.setDetalleSolicitud("fechaCargo_"+i+"_"+j, asociosSolicitud.get("fechaCargo_"+j));
				estadoCuentaForm.setDetalleSolicitud("estadoCargo_"+i+"_"+j, asociosSolicitud.get("estado_"+j));
				estadoCuentaForm.setDetalleSolicitud("nombreAsocio_"+i+"_"+j, asociosSolicitud.get("nombreAsocio_"+j));
				estadoCuentaForm.setDetalleSolicitud("nombreProfesional_"+i+"_"+j, asociosSolicitud.get("nombreProfesional_"+j));
				if(Integer.parseInt(asociosSolicitud.get("codigoEstado_"+j).toString())==ConstantesBD.codigoEstadoFCargada)
					totalCirugia += Double.parseDouble(asociosSolicitud.get("totalCargo_"+j).toString());
				 */
				
				
				liquidacionForm.setAsocios("nombreMedicoAsocio_"+i+"_"+j,asocios.get("nombreProfesional_"+j));
				liquidacionForm.setAsocios("nombreAsocio_"+i+"_"+j,asocios.get("nombreAsocio_"+j));
				liquidacionForm.setAsocios("paquetizado_"+i+"_"+j,asocios.get("paquetizado_"+j));
				liquidacionForm.setAsocios("codigoAsocio_"+i+"_"+j,asocios.get("codigoAsocio_"+j));
				liquidacionForm.setAsocios("valorAsocio_"+i+"_"+j,UtilidadTexto.formatearValores(asocios.get("totalCargo_"+j).toString()));
				liquidacionForm.setAsocios("estadoAsocio_"+i+"_"+j, asocios.get("estado_"+j));
				liquidacionForm.setAsocios("cobrable_"+i+"_"+j, asocios.get("cobrable_"+j));
				
				if(Integer.parseInt(asocios.get("codigoEstado_"+j).toString())==ConstantesBD.codigoEstadoFCargada)
					totalCirugia += Double.parseDouble(asocios.get("totalCargo_"+j).toString());
			}
			
			liquidacionForm.setCirugiasSolicitud("valorCirugia_"+i, UtilidadTexto.formatearValores(totalCirugia));
			totalOrden += totalCirugia;
			
		}
		
		
		//*****SE CARGAN LOS MATERIALES ESPECIALES**********************
		liquidacionForm.setMaterialesEspeciales(UtilidadesSalas.obtenerListadoMaterialesEspeciales(con, liquidacionForm.getNumeroSolicitud()+"",liquidacionForm.getConveniosSol("subCuenta_"+pos).toString(),false, true /*mostrar paquetizado*/));
		for(HashMap<String, Object> elemento:liquidacionForm.getMaterialesEspeciales())
			totalOrden += Double.parseDouble(elemento.get("valorTotal").toString());
		//***************************************************************
		
		liquidacionForm.setEncabezadoSolicitud("valorOrden", UtilidadTexto.formatearValores(totalOrden));
		
	}
	
	/**
	 * Método para llenar el formulario con los datos de la 
	 * hoja de anestesia
	 * @param liquidacionForm
	 * @param hojaAnes
	 */
	private void llenarFormSeccionHojaAnes(ConsultaLiquidacionQxForm liquidacionForm, LiquidacionServicios mundoLiquidacion) 
	{
		
		liquidacionForm.setHojaAnestesia("nombreTipoAnestesia",mundoLiquidacion.getDatosActoQx().get("nombreTipoAnestesia"));
		liquidacionForm.setHojaAnestesia("codigoTipoAnestesia",mundoLiquidacion.getDatosActoQx().get("codigoTipoAnestesia"));
		liquidacionForm.setHojaAnestesia("codigoAnestesiologo",mundoLiquidacion.getDatosActoQx().get("codigoAnestesiologo"));
		liquidacionForm.setHojaAnestesia("nombreAnestesiologo",mundoLiquidacion.getDatosActoQx().get("nombreAnestesiologo"));
	}
	
	/**
	 * Método para llenar el formulario con los datos de la 
	 * hoja quirurgica
	 * @param liquidacionForm
	 * @param hojaQx
	 */
	private void llenarFormSeccionHojaQx(ConsultaLiquidacionQxForm liquidacionForm, LiquidacionServicios mundoLiquidacion) 
	{
		
		liquidacionForm.setHojaQx("fechaInicial",mundoLiquidacion.getDatosActoQx().get("fechaInicialCx"));
		liquidacionForm.setHojaQx("horaInicial",mundoLiquidacion.getDatosActoQx().get("horaInicialCx"));
		liquidacionForm.setHojaQx("fechaFinal",mundoLiquidacion.getDatosActoQx().get("fechaFinalCx"));
		liquidacionForm.setHojaQx("horaFinal",mundoLiquidacion.getDatosActoQx().get("horaFinalCx"));
		String[] duracion = mundoLiquidacion.getDatosActoQx().get("duracionCirugia").toString().split(":");
		if(duracion.length>1)
			liquidacionForm.setHojaQx("duracion",duracion[0]+" Horas "+duracion[1]+" Minutos");
		else
			liquidacionForm.setHojaQx("duracion","0 Horas 0 Minutos");
		liquidacionForm.setHojaQx("sala",mundoLiquidacion.getDatosActoQx().get("codigoSala"));
		liquidacionForm.setHojaQx("nombreSala",mundoLiquidacion.getDatosActoQx().get("nombreSala"));
		liquidacionForm.setHojaQx("politrauma",mundoLiquidacion.getDatosActoQx().get("politraumatismo"));
	}
	
	
	/**
	 * Método para llenar el formulario con los datos del encabezado
	 * de la solicitud
	 * @param liquidacionForm
	 * @param solicitud
	 */
	private void llenarFormSeccionEncabezadoSol(ConsultaLiquidacionQxForm liquidacionForm, Solicitud solicitud) 
	{
		liquidacionForm.setEncabezadoSolicitud("orden",solicitud.getConsecutivoOrdenesMedicas()+"");
		liquidacionForm.setEncabezadoSolicitud("fechaOrden",solicitud.getFechaSolicitud());
		//liquidacionForm.setEncabezadoSolicitud("autorizacion",solicitud.getNumeroAutorizacion());
		liquidacionForm.setEncabezadoSolicitud("horaOrden",solicitud.getHoraSolicitud());
		
	}
	
	
	
	/**
	 * Método usado para realizar las validaciones generales de la funcionalidad
	 * @param con
	 * @param paciente
	 * @param mapping
	 * @param request
	 * @param logger
	 * @return
	 * @throws SQLException
	 */
	private ActionForward validacionesUsuarios(
			Connection con, PersonaBasica paciente, 
			ActionMapping mapping, 
			HttpServletRequest request, 
			Logger logger)  
	{
		//***************VALIDACIONES************************************************
		//verificar si es null (Paciente está cargado)
		if((paciente==null || paciente.getTipoIdentificacionPersona().equals("")))
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente no cargado", "errors.paciente.noCargado", true);
		
		/* davgommo.
		 * Cambio segun Incidencia 6487
		 * Se quita la validacion de cuenta activa segun DCU 182
		 *  */
		//		if(paciente.getCodigoIngreso()==0)
//			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El ingreso no está cargado", "errors.paciente.noIngresoSesion", true);
//		//***************************************************************************
		return null;
	}
	
	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer un forward
	 * @param con Conexión con la fuente de datos
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
