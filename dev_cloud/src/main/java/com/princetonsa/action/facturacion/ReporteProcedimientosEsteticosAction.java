/*
 * @(#)ReporteProcedimientosEsteticosAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2007. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */
package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.facturacion.ReporteProcedimientosEsteticosForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.Paciente;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ReporteProcedimientosEsteticos;
import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;
import com.princetonsa.mundo.salasCirugia.LiquidacionServicios;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;


/**
 * Clase para el manejo del workflow de la funcionalidad
 * @author Julián Pacheco Jiménez
 * @author ANDRES E SILVA MONSALVE
 * jpacheco@princetonsa.com
 */

public class ReporteProcedimientosEsteticosAction extends Action
{
	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(ReporteProcedimientosEsteticosAction.class);

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
			if(form instanceof ReporteProcedimientosEsteticosForm)
			{

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico"); 
				ReporteProcedimientosEsteticosForm forma =(ReporteProcedimientosEsteticosForm)form;
				String estado=forma.getEstado();
				logger.warn("\n\n\nEl estado en Reporte Procedimientos Esteticos es------->"+estado+"\n");

				if(estado == null)
				{
					forma.reset(usuario.getCodigoCentroAtencion()+"",usuario.getCodigoInstitucion()); 
					logger.warn("Estado no valido dentro del flujo de Reporte Procedimientos Esteticos (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					forma.reset(usuario.getCodigoCentroAtencion()+"",usuario.getCodigoInstitucion());
					forma.setConsulta("numRegistros","0");
					return mapping.findForward("principal");

				}
				else if(estado.equals("consultar"))
				{
					return accionConsultar(con,forma,mapping);
				}
				else if (estado.equals("volverConsultar"))
				{
					return accionVolverConsultar(con,forma,mapping);
				}
				else if (estado.equals("detalle"))
				{
					return accionDetalle(con,forma,mapping);
				}
				else if (estado.equals("volverDetalle"))
				{
					return accionVolverDetalle(con,forma,mapping);
				}
				else if (estado.equals("detalleCargo"))
				{
					return accionDetalleCargo(con,forma,mapping);
				}
				else if (estado.equals("imprimir"))
				{
					return accionImprimir(con,forma,mapping,request);
				}
				//***********ESTADOS DETINADOS PARA LA PAGINACION Y ORDENACION DEL LISTADO**************
				else if (estado.equals("redireccion"))
				{
					return accionRedireccion(con,forma,response,mapping,request);
				}
				else if (estado.equals("ordenar"))
				{
					return accionOrdenar(con,forma,mapping);
				}
				//***************************************************************************************
				else
				{
					forma.reset(usuario.getCodigoCentroAtencion()+"",usuario.getCodigoInstitucion()); 
					logger.warn("Estado no valido dentro del flujo de Procedimientos Esteticos -> "+estado);
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
	 * Método implementado para imprimir el reporte
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionImprimir(Connection con, ReporteProcedimientosEsteticosForm forma, ActionMapping mapping, HttpServletRequest request) 
	{
		String nombreRptDesign = "ProcedimientosEsteticos.rptdesign";
		String fechaInicial = UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial());
		String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal());
		
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());     
        if(!ins.getActividadEconomica().equals(""))//si no se posee actividad economica no mostrar el campo
        	v.add("Actividad Económica: "+ins.getActividadEconomica());
        v.add(ins.getDireccion()+"          "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        comp.insertLabelInGridPpalOfHeader(1,0, "LISTADO PROCEDIMIENTOS ESTÉTICOS");
        comp.insertLabelInGridPpalOfHeader(2,0, "PERÍODO: "+forma.getFechaInicial()+" - "+forma.getFechaFinal());
        comp.insertLabelInGridPpalOfHeader(3,0, "Fecha y hora impresión: "+UtilidadFecha.getFechaActual(con)+" - "+UtilidadFecha.getHoraActual(con));
        
        //SEGUNDO SE MODIFICA LA CONSULTA 
        comp.obtenerComponentesDataSet("listado");            
        String oldQuery=comp.obtenerQueryDataSet();
        
        //Se agregan los filtros
        oldQuery += " WHERE se.institucion = "+forma.getInstitucion()+" " ;
        	
        oldQuery += " AND (solci.fecha_inicial_cx BETWEEN '"+fechaInicial+"' AND '"+fechaFinal+"' OR " +
        		"solci.fecha_final_cx BETWEEN '"+fechaInicial+"' AND '"+fechaFinal+"')";
        
        if(!forma.getGrupoEstetico().equals(""))
        {
        	oldQuery += " AND se.grupos_esteticos = '"+forma.getGrupoEstetico()+"' ";
        }
        
        if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteRealizado))
		{
        	//Se anexa a la consulta si el tipo de reporte es realizada
			oldQuery += "AND (sol.estado_historia_clinica = "+ConstantesBD.codigoEstadoHCInterpretada+" AND esSolCxCargada(sol.numero_solicitud) = '"+ConstantesBD.acronimoSi+"') ";
		}
		else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteFacturado))
		{
			//Se anexa a la consulta si el tipo de Reporte es Facturada
			oldQuery +=" AND esSolCxFacturada(sol.numero_solicitud) = '"+ConstantesBD.acronimoSi+"' ";
		}
		
		if(!forma.getCentroAtencion().equals(""))
		{
			//Se anexa a la consulta si esta solo se hace por un Centro de Atencion
			oldQuery += "AND getCentroAtencionSol( solcise.numero_solicitud ) = "+forma.getCentroAtencion()+" ";
		}
		
		oldQuery += " ORDER BY sol.fecha_interpretacion,sol.consecutivo_ordenes_medicas ";
        
        
		
		
		//se actualiza Query
        comp.modificarQueryDataSet(oldQuery);
        
        //SE ENVIAN LOS ATRIBUTOS AL JSP PARA IMPRIMIR
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        //SE ACTUALIZAN LOS PARAMETROS DE CONEXION CON LA BD
        comp.updateJDBCParameters(newPathReport);
        
        forma.setEstado("consultar");
        UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}




	/**
	 * Método que regresa a volver detalle
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionVolverDetalle(Connection con, ReporteProcedimientosEsteticosForm forma, ActionMapping mapping) 
	{
		forma.setEstado("detalle");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalle");
	}




	/**
	 * Método que consulta el detalle del cargo de la cirugía
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetalleCargo(Connection con, ReporteProcedimientosEsteticosForm forma, ActionMapping mapping) 
	{
		int pos = forma.getIndice();
		String numeroSolicitud = forma.getConsulta("numeroSolicitud_"+pos).toString();
		
		forma.setDetalleCargo(ReporteProcedimientosEsteticos.consultarDetalleCargoEstetico(con, numeroSolicitud));
		//Se cargan los materiales especiales
		forma.setMaterialesEspeciales(ReporteProcedimientosEsteticos.consultarMaterialesEspecialesEstetico(con, numeroSolicitud));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleCargo");
	}




	/**
	 * Método implementado para volver al listado de las cirugias estéticas encontradas
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionVolverConsultar(Connection con, ReporteProcedimientosEsteticosForm forma, ActionMapping mapping) 
	{
		forma.setEstado("consultar");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}




	/**
	 * Método que carga el detalle de una solicitud de cirugía
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetalle(Connection con, ReporteProcedimientosEsteticosForm forma, ActionMapping mapping) 
	{
		int pos = forma.getIndice();
		int codigoPaciente = Integer.parseInt(forma.getConsulta("codigoPaciente_"+pos).toString());
		int numeroSolicitud = Integer.parseInt(forma.getConsulta("numeroSolicitud_"+pos).toString());
		String valorOrden = forma.getConsulta("valorOrden_"+pos).toString();
		
		//*****************SE CONSULTA LA INFORMACION DEL PACIENTE******************************
		Paciente mundoPaciente = new Paciente();
		try 
		{
			mundoPaciente.cargarPaciente(con, codigoPaciente);
		} 
		catch (SQLException e) 
		{
			logger.error("Error al cargar la informacion del paciente: "+e);
		}
		forma.setDetalle("nombrePaciente", mundoPaciente.getPrimerNombrePersona(false)+" "+mundoPaciente.getSegundoNombrePersona(false)+" "+mundoPaciente.getPrimerApellidoPersona(false)+" "+mundoPaciente.getSegundoApellidoPersona(false));
		forma.setDetalle("idPaciente",mundoPaciente.getCodigoTipoIdentificacion()+" "+mundoPaciente.getNumeroIdentificacion());
		forma.setDetalle("sexoPaciente", mundoPaciente.getSexo());
		forma.setDetalle("fechaNacimiento", mundoPaciente.getDiaNacimiento()+"/"+mundoPaciente.getMesNacimiento()+"/"+mundoPaciente.getAnioNacimiento());
		//*****************************************************************
		
		//**************SE CONSULTA EL ENCABEZADO DE LA SOLICITUD**********************
		Solicitud mundoSolicitud = new Solicitud();
		try 
		{
			mundoSolicitud.cargar(con, numeroSolicitud);
		} 
		catch (SQLException e) 
		{
			logger.error("Error al tratar de cargar los datos de la solicitud");
		}
		forma.setDetalle("numeroSolicitud",numeroSolicitud);
		forma.setDetalle("consecutivoOrden",mundoSolicitud.getConsecutivoOrdenesMedicas());
		
		if(!mundoSolicitud.getFechaInterpretacion().toString().equals(""))
		{
			forma.setDetalle("fechaOrden",UtilidadFecha.conversionFormatoFechaAAp(mundoSolicitud.getFechaInterpretacion())+" - "+mundoSolicitud.getHoraInterpretacion());
			forma.setDetalle("fechaOrden",UtilidadFecha.conversionFormatoFechaAAp(mundoSolicitud.getFechaInterpretacion().toString())+" - "+mundoSolicitud.getHoraInterpretacion().toString());
		}
		else
		{
			mundoSolicitud.setFechaInterpretacion("");
			mundoSolicitud.setHoraInterpretacion("");			
			forma.setDetalle("fechaOrden","");			
		}		
		
		//forma.setDetalle("numeroAutorizacion",mundoSolicitud.getNumeroAutorizacion());
		forma.setDetalle("valorOrden",valorOrden);
		//*****************************************************************************
		
		//***********SE CONSULTAN LOS DATOS DEL ACTO QUIRURGICO***********************
		 LiquidacionServicios mundoLiquidacion = new LiquidacionServicios();
		mundoLiquidacion.setCon(con);
		mundoLiquidacion.setNumeroSolicitud(numeroSolicitud+"");
		mundoLiquidacion.cargarDetalleOrden();
		
		
		forma.setDetalle("fechaInicial",mundoLiquidacion.getDatosActoQx().get("fechaInicialCx"));
		forma.setDetalle("horaInicial",mundoLiquidacion.getDatosActoQx().get("horaInicialCx"));
		forma.setDetalle("fechaFinal",mundoLiquidacion.getDatosActoQx().get("fechaFinalCx"));
		forma.setDetalle("horaFinal",mundoLiquidacion.getDatosActoQx().get("horaFinalCx"));
		try
		{
			String[] duracion = mundoLiquidacion.getDatosActoQx().get("duracionCirugia").toString().split(":");
			forma.setDetalle("duracion",duracion[0]+" Horas "+duracion[1]+" Minutos");
		}
		catch(Exception e)
		{
			forma.setDetalle("duracion ","");
		}
		forma.setDetalle("nombreSala",mundoLiquidacion.getDatosActoQx().get("nombreSala"));
		forma.setDetalle("politrauma",mundoLiquidacion.getDatosActoQx().get("politraumatismo"));
		forma.setDetalle("tipoAnestesia",mundoLiquidacion.getDatosActoQx().get("nombreTipoAnestesia"));
		
		//*******************************************************************************
		
		//****************SE CONSULTA EL DETALLE DE LAS CIRUGÍAS***************************
		SolicitudesCx solicitudCx = new SolicitudesCx();
		HashMap servicioSolicitud = solicitudCx.cargarServiciosXSolicitudCx(con,numeroSolicitud+"",false);
		
		for(int i=0;i<Integer.parseInt(servicioSolicitud.get("numRegistros").toString());i++)
		{
			forma.setDetalle("codigoServicio_"+i,servicioSolicitud.get("codigoServicio_"+i));
			forma.setDetalle("consecutivoCirugia_"+i,servicioSolicitud.get("numeroServicio_"+i));
			forma.setDetalle("descripcionCirugia_"+i,servicioSolicitud.get("descripcionServicio_"+i));
			forma.setDetalle("valorCirugia_"+i,UtilidadTexto.formatearValores(servicioSolicitud.get("valor_"+i).toString()));
			forma.setDetalle("esquemaTarifario_"+i,servicioSolicitud.get("nombreEsquemaTarifario_"+i));
			forma.setDetalle("grupoUvr_"+i,servicioSolicitud.get("grupoUvr_"+i));
			forma.setDetalle("tipoCirugia_"+i,servicioSolicitud.get("nombreTipoCirugia_"+i));
			forma.setDetalle("noVia_"+i,servicioSolicitud.get("nombreViaCx_"+i));
			
			//Se cargan los asocios de la cirugía
			HashMap asocios = LiquidacionServicios.consultarAsociosLiquidados(con, servicioSolicitud.get("codigo_"+i).toString());
			
			for(int j=0;j<Integer.parseInt(asocios.get("numRegistros").toString());j++)
			{
				forma.setDetalle("nombreAsocio_"+i+"_"+j, asocios.get("nombreAsocio_"+j)+" ("+asocios.get("nombreTipoServicio_"+j)+")");
				forma.setDetalle("nombreMedico_"+i+"_"+j, asocios.get("nombreProfesional_"+j));
			}
			
			forma.setDetalle("numAsocios_"+i, asocios.get("numRegistros"));
			
			
		}
		forma.setDetalle("numCirugias", servicioSolicitud.get("numRegistros").toString());
		
		//***********************************************************************************
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalle");
	}




	/**
	 * Método que realiza la ordenacion del listado de servicios esteticos
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(Connection con, ReporteProcedimientosEsteticosForm forma, ActionMapping mapping) 
	{
		//columnas del listado
		String[] indices = {
				"fechaInterpretacion_",
				"nombreServicio_",
				"consecutivoOrden_",
				"codigoPaciente_",
				"nombrePaciente_",
				"idPaciente_",
				"valorOrden_",
				"numeroSolicitud_",
				"numServicios_"
			};

		
		int numRegistros = Integer.parseInt(forma.getConsulta("numRegistros").toString());
		
		forma.setConsulta(Listado.ordenarMapa(indices,
				forma.getColumna(),
				forma.getUltimaColumna(),
				forma.getConsulta(),
				numRegistros));
		
		
		forma.setConsulta("numRegistros",numRegistros+"");
		
		forma.setUltimaColumna(forma.getColumna());
		forma.setEstado("consultar");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}




	/**
	 * Método que se encarga de realizar la paginacion del pager
	 * @param con
	 * @param forma
	 * @param response
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, ReporteProcedimientosEsteticosForm forma, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) 
	{
		try
		{
			
		    UtilidadBD.closeConnection(con);
		    forma.setEstado("consultar");
			response.sendRedirect(forma.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion : "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en ResporteProcedimientosEsteticosAction", "errors.problemasDatos", true);
		}
	}


/**
 * METODO QUE SE ENCARGA DE HACER LA BUSQUEDA EN LA BD
 * @param con
 * @param forma
 * @param mapping
 * @return
 */
private ActionForward accionConsultar(Connection con, ReporteProcedimientosEsteticosForm forma, ActionMapping mapping) 
{
//	Se instancia objeto mundo
	
	ReporteProcedimientosEsteticos mundo = new ReporteProcedimientosEsteticos();
	
	forma.setConsulta(mundo.consultarProcedimientosEsteticos(con, forma.getFechaInicial(), forma.getFechaFinal(), forma.getGrupoEstetico(), forma.getCentroAtencion(), forma.getTipoReporte(), forma.getInstitucion()));
	
	return mapping.findForward("principal");
	
	
}


}