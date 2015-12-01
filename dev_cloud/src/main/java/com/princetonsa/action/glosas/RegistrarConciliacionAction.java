package com.princetonsa.action.glosas;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

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
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.reportes.ConsultasBirt;

import com.princetonsa.actionform.glosas.RegistrarConciliacionForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.glosas.DtoFacturaGlosa;
import com.princetonsa.dto.glosas.DtoRespuestaFacturaGlosa;
import com.princetonsa.dto.glosas.DtoRespuestaSolicitudGlosa;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.glosas.RegistrarConciliacion;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.persistencia.UtilidadPersistencia;


public class RegistrarConciliacionAction extends Action
{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private Logger logger = Logger.getLogger(RegistrarConciliacionAction.class);
	
	
	/**
	 * Mï¿½todo encargado de el flujo y control de la funcionalidad
	 * @author Diego Bedoya
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response ) throws Exception
		{
		Connection con=null;
		try{

			if (response==null); //Para evitar que salga el warning
			if(form instanceof RegistrarConciliacionForm)
			{

				//SE ABRE CONEXION
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexiï¿½n"+e.toString());
				}

				//se obtiene el usuario cargado en sesion.
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				//se obtiene el paciente cargado en sesion.
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

				//se obtiene la institucion
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

				//se instancia la forma
				RegistrarConciliacionForm forma = (RegistrarConciliacionForm)form;		

				//se instancia el mundo
				RegistrarConciliacion mundo = new RegistrarConciliacion();

				//optenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				//se instancia la variable para manejar los errores.
				ActionErrors errores=new ActionErrors();

				forma.setMensaje(new ResultadoBoolean(false));

				logger.info("\n\n***************************************************************************");
				logger.info(" 	  EL ESTADO DE REGISTRAR CONCILIACION (GLOSAS) ES ====>> "+estado);
				logger.info("\n***************************************************************************");


				// ESTADO --> NULL
				if(estado == null)
				{
					forma.reset(usuario.getCodigoInstitucionInt());
					request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);

					return mapping.findForward("paginaError");
				}			
				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(forma, mundo, mapping, usuario, request);					   			
				}
				else if(estado.equals("busquedaConciliaciones"))
				{
					return this.accionBusquedaConciliaciones(forma, mundo, mapping, usuario, request);				
				}
				else if(estado.equals("guardarConciliacion"))
				{
					return this.accionGuardarConciliacion(forma, mundo, mapping, usuario, request);
				}
				else if(estado.equals("listarFacturasGlosa"))
				{
					forma.setArregloFacturasGlosa(new ArrayList<DtoFacturaGlosa>());

					return mapping.findForward("buscarFacturasGlosa");
				}
				else if(estado.equals("buscarFacturasGlosa"))
				{
					return this.accionBuscarFacturasGlosa(forma, mundo, mapping, usuario, request);
				}
				else if(estado.equals("seleccionFacturas"))
				{				
					return this.accionSeleccionFacturas(forma, mundo, mapping, usuario, request);
				}
				else if(estado.equals("volver"))
				{
					for(int i=0;i<(forma.getArregloFacturasGlosa().size());i++)
						logger.info("\n\nseleccionado: "+forma.getArregloFacturasGlosa().get(i).getSeleccionado()+" glosa: "+forma.getArregloFacturasGlosa().get(i).getGlosa().getGlosaSistema());
					return mapping.findForward("buscarFacturasGlosa");
				}
				else if(estado.equals("volverP"))
				{
					return mapping.findForward("principal");
				}
				else if(estado.equals("guardarFacturasConciliacion"))
				{
					return this.accionGuardarFactConciliacion(forma, mundo, mapping, usuario, request);
				}
				else if(estado.equals("imprimirConciliacion"))
				{
					return this.accionImprimirConciliacion(forma, mundo, mapping, usuario, request);
				}
				else if(estado.equals("consultarDetFactura"))
				{
					return this.accionConsultarDetFactura(forma, mundo, mapping, usuario, request);
				}
				else if(estado.equals("guardarDetFactConc"))
				{
					return this.accionGuardarDetFactConc(forma, mundo, mapping, usuario, request);
				}
				else if(estado.equals("guardarDetFactExtConc"))
				{
					return this.accionGuardarDetFactExtConc(forma, mundo, mapping, usuario, request);
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
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionGuardarDetFactExtConc(RegistrarConciliacionForm forma, RegistrarConciliacion mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		ActionErrors errors = new ActionErrors();
		boolean guardo=false;
		String parametroGenerarAjustesAuto="";
		
		for(int i=0;i<forma.getConceptosGlosaFac().size();i++)
		{
			if(forma.getConceptosGlosaFac().get(i).getConceptoRespuesta().equals("-1"))
				errors.add("descripcion",new ActionMessage("errors.required","El Concepto de Conciliacion para la Solicitud "+forma.getArregloSolResp().get(i).getSolicitud()));
		}
		
		if(!errors.isEmpty())
		{
			forma.setEstado("listarFacturasGlosa");
			saveErrors(request,errors);
		}
		else
		{
			parametroGenerarAjustesAuto=ValoresPorDefecto.getGenerarAjusteAutoRegRespuesConciliacion(usuario.getCodigoInstitucionInt());
						
			if(mundo.guardarDetalleFactExtConciliacion(forma.getArregloFactResp().get(forma.getPosDetFactura()), parametroGenerarAjustesAuto, usuario))
				guardo=true;
			
	
			if(guardo)
			{
				forma.setPermiteImprimir(true);
				forma.setModificable2(true);
				forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
			}
			else
				forma.setMensaje(new ResultadoBoolean(true,"La operaciï¿½n no finalizï¿½ satisfactoriamente"));
		}
		
		return mapping.findForward("detalleFacturasExtConc");
	}
	
	/**
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionGuardarDetFactConc(RegistrarConciliacionForm forma, RegistrarConciliacion mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		ActionErrors errors = new ActionErrors();
		boolean guardo=false;
		String parametroGenerarAjustesAuto="";
		
		for(int i=0;i<forma.getArregloSolResp().size();i++)
		{
			if(forma.getArregloSolResp().get(i).getConceptoRespuesta().equals("-1"))
				errors.add("descripcion",new ActionMessage("errors.required","El Concepto de Conciliacion para la Solicitud "+forma.getArregloSolResp().get(i).getSolicitud()));
		}
		
		if(!errors.isEmpty())
		{
			forma.setEstado("listarFacturasGlosa");
			saveErrors(request,errors);
		}
		else
		{	
			parametroGenerarAjustesAuto=ValoresPorDefecto.getGenerarAjusteAutoRegRespuesConciliacion(usuario.getCodigoInstitucionInt());
			
			for(int i=0;i<forma.getArregloDetFactGlosa().size();i++)
			{
				logger.info("\n\nmotivo en resp:: "+forma.getArregloSolResp().get(i).getMotivo());
				forma.getArregloDetFactGlosa().get(i).setMotivo(forma.getArregloSolResp().get(i).getMotivo());
			}
			
			if(mundo.guardarDetalleFactConciliacion(forma.getArregloAsociosGlosa(),forma.getArregloDetFactGlosa(), forma.getArregloFactResp().get(forma.getPosDetFactura()), parametroGenerarAjustesAuto, usuario))
				guardo=true;
						
			if(guardo)
			{
				forma.setPermiteImprimir(true);
				forma.setModificable2(true);
				forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
			}
			else
				forma.setMensaje(new ResultadoBoolean(true,"La operaciï¿½n no finalizï¿½ satisfactoriamente"));
		}
		
		return mapping.findForward("detalleFacturasConc");
	}
	
	/**
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionConsultarDetFactura(RegistrarConciliacionForm forma, RegistrarConciliacion mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{	
		forma.setCodFacturaDet(forma.getArregloFactResp().get(forma.getPosDetFactura()).getConsecutivoFactura());
		forma.setValorGlosaDet(forma.getArregloFactResp().get(forma.getPosDetFactura()).getValorGlosaFactura());
		
		forma.setArregloSolResp(new ArrayList<DtoRespuestaSolicitudGlosa>());
		//cambiar por interna
		if(forma.getArregloFactResp().get(forma.getPosDetFactura()).getFactura().getTipoFacturaSistema())
		{
			String codigoTarifario= ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt());
			forma.setArregloDetFactGlosa(mundo.consultaDetFactGlosa(forma.getArregloFactResp().get(forma.getPosDetFactura()).getAuditoria(),codigoTarifario));
			for(int k=0;k<(forma.getArregloDetFactGlosa().size());k++)
			{
				forma.setArregloAsociosGlosa(mundo.consultarAsociosDetFacturaResp(Utilidades.convertirAEntero(forma.getArregloDetFactGlosa().get(k).getConcepto().getCodigoDetalleFacturaGlosa())));
				DtoRespuestaSolicitudGlosa dto= new DtoRespuestaSolicitudGlosa();
				
				dto.setCentroCosto(forma.getArregloDetFactGlosa().get(k).getCentroCosto());
				dto.setSolicitud(forma.getArregloDetFactGlosa().get(k).getNumeroSolicitud());
				dto.setDescripcionServicioArticulo(forma.getArregloDetFactGlosa().get(k).getDescripcionServicioArticulo());
				dto.setCantidadGlosa(forma.getArregloDetFactGlosa().get(k).getCantidadGlosa()+"");
				dto.setValorGlosa(forma.getArregloDetFactGlosa().get(k).getValorGlosa()+"");
				dto.getConcepto().setCodigo(forma.getArregloDetFactGlosa().get(k).getConcepto().getCodigo());
				dto.getConcepto().setDescripcion(forma.getArregloDetFactGlosa().get(k).getConcepto().getDescripcion());
				dto.setConceptoRespuesta(forma.getArregloFactResp().get(forma.getPosDetFactura()).getConcepto());
				dto.setValorAceptado((Utilidades.convertirADouble(forma.getArregloDetFactGlosa().get(forma.getPosDetFactura()).getValorStr())/100) * Utilidades.convertirADouble(forma.getAceptado())+"");
				dto.setValorSoportado(Utilidades.convertirADouble(forma.getArregloDetFactGlosa().get(forma.getPosDetFactura()).getValorStr()) - Utilidades.convertirADouble(dto.getValorAceptado())+"");

				for(int j=0;j<forma.getArregloAsociosGlosa().size();j++)
				{					
					forma.getArregloAsociosGlosa().get(j).getDetalleFacturaGlosa().setCodigo(forma.getArregloDetFactGlosa().get(k).getConcepto().getCodigoDetalleFacturaGlosa());
					forma.getArregloAsociosGlosa().get(j).getConceptoResp().setCodigo(forma.getArregloFactResp().get(forma.getPosDetFactura()).getConcepto());
					logger.info("\n\nconcepto:: "+forma.getArregloAsociosGlosa().get(j).getConceptoResp().getCodigo());
				}
				forma.getArregloSolResp().add(dto);
			}				
			return mapping.findForward("detalleFacturasConc");
		}
		else
		{
			forma.setConceptosGlosaFac(mundo.consultarConceptosGlosaFact(forma.getArregloFactResp().get(forma.getPosDetFactura()).getAuditoria()));
			
			for(int i=0;i<forma.getConceptosGlosaFac().size();i++)
			{
				logger.info("\n\nposicion:: "+forma.getPosDetFactura());
				logger.info("\n\nvalor:: "+forma.getConceptosGlosaFac().get(forma.getPosDetFactura()).getValorGlosaFactura());
				forma.getConceptosGlosaFac().get(i).setValorAceptado((forma.getConceptosGlosaFac().get(forma.getPosDetFactura()).getValorGlosaFactura()/100) * Utilidades.convertirADouble(forma.getAceptado()));
				forma.getConceptosGlosaFac().get(i).setValorSoportado(forma.getConceptosGlosaFac().get(forma.getPosDetFactura()).getValorGlosaFactura() - forma.getConceptosGlosaFac().get(i).getValorAceptado());
			}
			
			return mapping.findForward("detalleFacturasExtConc");
		}
	}
	
	/**
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBusquedaConciliaciones(RegistrarConciliacionForm forma, RegistrarConciliacion mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{				
		String codConvenio="";
		
		if(forma.getCodConvenioBusq().contains("@"))
		{
			forma.setNomConvenioC(forma.getCodConvenioBusq().split(ConstantesBD.separadorSplit)[1]);
			codConvenio=forma.getCodConvenioBusq().split(ConstantesBD.separadorSplit)[0];
			forma.setCodConvenioC(codConvenio);		
		}		
		
		if(!forma.getCodconciliacionC().equals(""))
		{
			forma.setArregloFactResp(mundo.consultaFacturasConciliacion(forma.getCodconciliacionC()));
		}
		
		if(forma.getArregloFactResp().size() > 0)
		{
			forma.setModificable1(true);
			forma.setModificable2(true);
			forma.setModificable(true);
			forma.setPermiteImprimir(true);
		}
		
		if(!forma.getCodconciliacionC().equals(""))
			forma.setModificable(true);
		return mapping.findForward("principal");
	}
		
	/**
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionImprimirConciliacion(RegistrarConciliacionForm forma, RegistrarConciliacion mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{	
		String formatoImpresion="";
		
		formatoImpresion=ValoresPorDefecto.getFormatoImpresionConciliacion(usuario.getCodigoInstitucionInt());
		
		logger.info("\n\nformato impresion: "+formatoImpresion);
		
		if(formatoImpresion.equals(ConstantesIntegridadDominio.acronimoSuba))
			imprimirConciliacionFormatoSuba(forma, mundo, mapping, usuario, request);
		
		if(forma.getArregloFactResp().get(forma.getPosDetFactura()).getFactura().getTipoFacturaSistema())
			return mapping.findForward("detalleFacturasConc");
		else
			return mapping.findForward("detalleFacturasExtConc");
	}
	
	public void imprimirConciliacionFormatoSuba(RegistrarConciliacionForm forma, RegistrarConciliacion mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		String codigoTarifario= ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt());
		Connection con = null;
		con = UtilidadBD.abrirConexion();
		String nombreRptDesign = "FormatoSubaConciliacionGlosas.rptdesign";
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		Vector v;
		
		//***************** INFORMACIï¿½N DEL CABEZOTE
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"glosas/",nombreRptDesign);
         
        // Logo
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        
        // Nombre Instituciï¿½n, titulo y rango de fechas
        comp.insertGridHeaderOfMasterPage(0,1,1,3);
        v=new Vector();
        v.add("\n\nEMPRESA SOCIAL DEL ESTADO\n"+ins.getRazonSocial());
        v.add("\n\nOFICINA DE FACTURACIï¿½N\n");
        v.add("\n\nCONCILIACIï¿½N GLOSA NO. "+forma.getConciliacionC()+"- "+forma.getNomConvenioC());
        comp.insertLabelInGridOfMasterPage(0,1,v);
               
        
        // Fecha hora de proceso y usuario
        comp.insertLabelInGridPpalOfFooter(0,0,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
        comp.insertLabelInGridPpalOfFooter(0,1,"Usuario: "+usuario.getLoginUsuario());
        	                
        //***************** NUEVO WHERE DEL REPORTE
      
      
	   String newquery = "";
	   
	  comp.obtenerComponentesDataSet("encabezado");
	  newquery= ConsultasBirt.formatoSubaConciliacion(
				con, forma.getCodconciliacionC(), codigoTarifario, usuario.getCodigoInstitucionInt(), "e");
      comp.modificarQueryDataSet(newquery);
      
      comp.obtenerComponentesDataSet("facturas");
	  newquery= ConsultasBirt.formatoSubaConciliacion(
				con, forma.getCodconciliacionC(), codigoTarifario, usuario.getCodigoInstitucionInt(), "f");
      comp.modificarQueryDataSet(newquery);
      
      comp.obtenerComponentesDataSet("solicitudes");
	  newquery= ConsultasBirt.formatoSubaConciliacion(
				con, forma.getCodconciliacionC(), codigoTarifario, usuario.getCodigoInstitucionInt(), "s");
      comp.modificarQueryDataSet(newquery);
      
       //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
       comp.lowerAliasDataSet();
       String newPathReport = comp.saveReport1(false);
       comp.updateJDBCParameters(newPathReport);
       
       if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
             
       UtilidadBD.closeConnection(con);
	}
	
	/**
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionSeleccionFacturas(RegistrarConciliacionForm forma, RegistrarConciliacion mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{	
		ActionErrors errors = new ActionErrors();
		for(int k=0;k<forma.getArregloFacturasGlosa().size();k++)
		{
			if(forma.getArregloFacturasGlosa().get(k).getSeleccionado().equals(ConstantesBD.acronimoSi))
			{
				for(int j=0;j<forma.getArregloFactResp().size();j++)
				{
					if(forma.getArregloFacturasGlosa().get(k).getCodigoGlosa().equals(forma.getArregloFactResp().get(j).getGlosa().getCodigo()))
					{
						errors.add("descripcion",new ActionMessage("prompt.generico","La Factura ya Fue Seleccionada."));
					}
				}
			}
		}
		
		if(!errors.isEmpty())
		{
			//forma.setEstado("listarFacturasGlosa");
			saveErrors(request,errors);
		}
		else
		{	
			for(int k=0;k<forma.getArregloFacturasGlosa().size();k++)
			{				
				logger.info("seleccionado: "+forma.getArregloFacturasGlosa().get(k).getSeleccionado());
				if(forma.getArregloFacturasGlosa().get(k).getSeleccionado().equals(ConstantesBD.acronimoSi))
				{					
					DtoRespuestaFacturaGlosa dto= new DtoRespuestaFacturaGlosa();
					dto.setCodigoFactura(Utilidades.convertirAEntero(forma.getArregloFacturasGlosa().get(k).getCodigoFactura()));
					dto.setConsecutivoFactura(forma.getArregloFacturasGlosa().get(k).getConsecutivoFactura());
					dto.setFechaFactura(forma.getArregloFacturasGlosa().get(k).getFechaElaboracionFactura());
					dto.setFechaRadicacion(forma.getArregloFacturasGlosa().get(k).getFechaRadicacion());
					dto.getGlosa().setCodigo(forma.getArregloFacturasGlosa().get(k).getCodigoGlosa());
					dto.getGlosa().setGlosaSistema(forma.getArregloFacturasGlosa().get(k).getGlosa().getGlosaSistema());
					dto.setSaldoFactura(forma.getArregloFacturasGlosa().get(k).getSaldoFacturaStr());
					dto.setValorGlosaFactura(forma.getArregloFacturasGlosa().get(k).getValorGlosaFacturaStr());
					dto.getFactura().setNombrePaciente(forma.getArregloFacturasGlosa().get(k).getFactura().getNombrePaciente());
					dto.getFactura().setTipoFacturaSistema(forma.getArregloFacturasGlosa().get(k).getFactura().getTipoFacturaSistema());
					dto.getGlosa().setGlosaEntidad(forma.getArregloFacturasGlosa().get(k).getGlosa().getGlosaEntidad());
					dto.setPorcentajeAceptado(forma.getArregloFacturasGlosa().get(k).getPorcentajeAceptado());
					dto.setPorcentajeSoportado(forma.getArregloFacturasGlosa().get(k).getPorcentajeSoportado());
					dto.setValorAceptado(forma.getArregloFacturasGlosa().get(k).getValorAceptado());
					dto.setValorSoportado(forma.getArregloFacturasGlosa().get(k).getValorSoportado());
					dto.setAuditoria(forma.getArregloFacturasGlosa().get(k).getCodigo());
					dto.setRespuesta(Utilidades.convertirAEntero(forma.getCodconciliacionC()));
					dto.setEliminar(forma.getArregloFacturasGlosa().get(k).getEliminar());
					dto.setConcepto(forma.getConceptoC());
					forma.getArregloFactResp().add(dto);
				}
			}
		}
		return mapping.findForward("principal");
	}
	
	/**
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionGuardarFactConciliacion(RegistrarConciliacionForm forma, RegistrarConciliacion mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{		
		String ajuste="";
		ActionErrors errores= new ActionErrors();
		boolean guardo=false;
		
		if(forma.getArregloFactResp().size() <= 0)
			errores.add("descripcion",new ActionMessage("prompt.generico","Debe estar selecicoanda al menos una Factura para guardar el detalle de la Conciliacion "));
		else
		{		
			for(int i=0;i<forma.getArregloFactResp().size();i++)
			{
				if(forma.getArregloFactResp().get(i).getConcepto().equals("-1"))
					errores.add("descripcion",new ActionMessage("errors.required","El concepto de Conciliacion para la Factura de Conciliacion de la posicion "+i+" "));
			}
		}

		if(!errores.isEmpty())
		{
			forma.setEstado("seleccionFacturas");
			saveErrors(request, errores);
		}
		else
		{			
			for(int i=0;i<forma.getArregloFactResp().size();i++)
			{						
				if(forma.getArregloFactResp().get(i).getEliminar().equals(ConstantesBD.acronimoNo))
				{	
					logger.info("\n\ncod conciliacion c:: "+forma.getCodconciliacionC());
					forma.getArregloFactResp().get(i).setRespuesta(Utilidades.convertirAEntero(forma.getCodconciliacionC()));
					guardo=false;
					forma.getArregloFactResp().get(i).setCodigoFactRespuestaGlosa(mundo.insertarFacturasRespGlosa(forma.getArregloFactResp().get(i), usuario.getCodigoInstitucionInt()));
					if(!forma.getArregloFactResp().get(i).getCodigoFactRespuestaGlosa().equals(""))
						guardo=true;
				}
			}
			
			if(guardo)
			{
				forma.setModificable1(true);
				forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
			}
			else
				forma.setMensaje(new ResultadoBoolean(true,"La operaciï¿½n no finalizï¿½ satisfactoriamente"));
		}	
		
		return mapping.findForward("principal");
	}
		
	/**
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBuscarFacturasGlosa(RegistrarConciliacionForm forma, RegistrarConciliacion mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		HashMap<String, Object> mapa = new HashMap<String, Object>();
		ActionErrors errores = new ActionErrors();

		logger.info("\n\nglosa:"+forma.getGlosa()+" glosa ent: "+forma.getGlosaEnt()+" fact ini: "+forma.getFacturaIni()+" fact fin "+forma.getFacturaFin()+" fecha ini: "+forma.getFechaElabIni()+" fecha fin: "+forma.getFechaElabFin());
		
		if(forma.getGlosa().equals("") && forma.getGlosaEnt().equals("") && forma.getFacturaIni().equals("") 
				&& forma.getFacturaFin().equals("") && forma.getFechaElabIni().equals("") && forma.getFechaElabFin().equals(""))
			errores.add("descripcion", new ActionMessage("prompt.generico","Es requerido al menos el ingreso de un parametro"));
		else
		{	
			if((!forma.getFechaElabIni().equals("") && forma.getFechaElabFin().equals("")) || (forma.getFechaElabIni().equals("") && !forma.getFechaElabFin().equals("")))
				errores.add("descripcion", new ActionMessage("errors.required","La Factura Inicial y Final "));
			if((!forma.getFechaElabIni().equals("") && !forma.getFechaElabFin().equals("")))
			{
				if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),"00:00",forma.getFechaElabIni().toString(), "00:00").isTrue())
					errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",forma.getFechaElabIni().toString(),UtilidadFecha.getFechaActual()));
				if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),"00:00",forma.getFechaElabFin().toString(), "00:00").isTrue())
					errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",forma.getFechaElabFin().toString(),UtilidadFecha.getFechaActual()));
				if(!UtilidadFecha.compararFechas(forma.getFechaElabFin().toString(), "00:00", forma.getFechaElabIni().toString(), "00:00").isTrue())
				errores.add("descripcion",new ActionMessage("errors.invalid"," Fecha Elaboracion Inicial "+forma.getFechaElabIni().toString()+" mayor a la Fecha Elaboracion Final "+forma.getFechaElabFin().toString()+" "));
			}
		}
		
		if(!errores.isEmpty())
			saveErrors(request, errores);
		else
		{
			mapa.put("glosa", forma.getGlosa());
			mapa.put("glosaEnt", forma.getGlosaEnt());
			mapa.put("facturaIni", forma.getFacturaIni());
			mapa.put("facturaFin", forma.getFacturaFin());
			mapa.put("fechaElabIni", forma.getFechaElabIni());
			mapa.put("fechaElabFin", forma.getFechaElabFin());
			logger.info("\n\ncod covenio busq:: "+forma.getCodConvenioBusq()+"\n\ncod convenio envio:: "+forma.getCodConvenioC());
			mapa.put("convenio", forma.getCodConvenioBusq().split(ConstantesBD.separadorSplit)[0]);
			
			ArrayList<DtoFacturaGlosa> aux = new ArrayList<DtoFacturaGlosa>();
			
			aux= mundo.consultarFacturasGlosa(mapa);
				
			for(int i=0;i<aux.size();i++)
			{
				aux.get(i).setValorAceptado(Utilidades.convertirADouble(forma.getAceptado())*(aux.get(i).getValorGlosaFactura()/100));
				aux.get(i).setValorSoportado(aux.get(i).getValorGlosaFactura() - aux.get(i).getValorAceptado());			
				aux.get(i).setConcepto(forma.getConceptoC());
			}
			
			for(int i=0;i<aux.size();i++)
			{
				logger.info("\n\nestado de respuesta:: "+aux.get(i).getGlosa().getEstadoRespuesta());
				if(aux.get(i).getGlosa().getEstadoRespuesta().equals(ConstantesIntegridadDominio.acronimoEstadoGlosaAnulada) || aux.get(i).getGlosa().getEstadoRespuesta().isEmpty())
				{
					DtoFacturaGlosa dto= new DtoFacturaGlosa();
					dto= aux.get(i);					
					forma.getArregloFacturasGlosa().add(dto);
				}
			}
			logger.info("\n\nposiciones del arreglo:: "+forma.getArregloFacturasGlosa().size());
		}
		
		return mapping.findForward("buscarFacturasGlosa");
	}	
		
	/**
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarConciliacion(RegistrarConciliacionForm forma, RegistrarConciliacion mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		// FIXME Cambiar este método a transaccional
		ActionErrors errores=new ActionErrors();
		int numConciliacion=0;
		HashMap<String, Object> mapa = new HashMap<String, Object>();
		
		String codConvenio="";
		
		if(forma.getCodConvenioC().contains("@"))
		{
			forma.setNomConvenioC(forma.getCodConvenioC().split(ConstantesBD.separadorSplit)[1]);
			codConvenio=forma.getCodConvenioC().split(ConstantesBD.separadorSplit)[0];
			forma.setCodConvenioC(codConvenio);		
		}		
		
		if(!forma.getCodConvenioBusq().equals("-1"))
			mapa.put("convenio", forma.getCodConvenioBusq().split(ConstantesBD.separadorSplit)[0]);
		else
			mapa.put("convenio", forma.getCodConvenioBusq());
		mapa.put("fecha", forma.getFechaC());
		mapa.put("hora", forma.getHoraC());
		mapa.put("nroActa", forma.getNroActa());
		mapa.put("repConvenio", forma.getRepresentanteConvenio());
		mapa.put("cargoRConvenio", forma.getCargoRepConv());
		mapa.put("repInstitucion", forma.getRepresentanteInst());
		mapa.put("cargoRInst", forma.getCargoRepInst());
		mapa.put("pSoportado", forma.getSoportado());
		mapa.put("pAceptado", forma.getAceptado());
		mapa.put("concConciliacion", forma.getConceptoC().split(ConstantesBD.separadorSplit)[0]);
		mapa.put("observ", forma.getObservaciones());
		mapa.put("usuario", usuario.getLoginUsuario());
		mapa.put("institucion", usuario.getCodigoInstitucionInt());
		mapa.put("codConciliacion", forma.getCodconciliacionC());
		
		if(forma.getConciliacionC().equals(""))
		{
			String valorConsecutivoRespuestaGlosa=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoRespuestaGlosas, usuario.getCodigoInstitucionInt());
			
			if(!UtilidadCadena.noEsVacio(valorConsecutivoRespuestaGlosa) || valorConsecutivoRespuestaGlosa.equals("-1"))
				errores.add("Falta consecutivo disponible",new ActionMessage("error.glosas.faltaDefinirConsecutivo","el registro de Respuesta"));
			else
			{
				try
				{
					Integer.parseInt(valorConsecutivoRespuestaGlosa);
				}
				catch(Exception e)
				{
					logger.error("Error en validacionConsecutivoDisponibleIngreso:  "+e);
					errores.add("Consecutivo no es entero", new ActionMessage("errors.integer","el consecutivo de la Respuesta Glosa"));
				}
			}
			
			mapa.put("conciliacion", valorConsecutivoRespuestaGlosa);
			mapa.put("nuevo", ConstantesBD.acronimoSi);
			Connection con=UtilidadPersistencia.getPersistencia().obtenerConexion();
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoRespuestaGlosas, usuario.getCodigoInstitucionInt(), valorConsecutivoRespuestaGlosa, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
		}
		else
		{
			mapa.put("nuevo", ConstantesBD.acronimoNo);
			mapa.put("conciliacion", forma.getConciliacionC());
		}				
		
		numConciliacion=mundo.guardarConciliacion(mapa);
				
		if(numConciliacion > 0)
		{
			forma.setCodconciliacionC(numConciliacion+"");
			forma.setModificable(true);
			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
		}
		else
			forma.setMensaje(new ResultadoBoolean(true,"La operaciï¿½n no finalizï¿½ satisfactoriamente"));
		
		return mapping.findForward("principal");
	}	
	
	/**
	 * Metodo para empezar la funcionalidad
	 * y cargar formulario de busqueda
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(RegistrarConciliacionForm forma, RegistrarConciliacion mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		Connection con;
				
		con= UtilidadBD.abrirConexion();
		
		forma.reset(usuario.getCodigoInstitucionInt());
		
		forma.setArregloConvenios(Utilidades.obtenerConvenios(con, "", "", false, "", false));
		
		forma.setArregloConceptosResp(mundo.consultarTiposConcepto());
					
		forma.setFechaC(UtilidadFecha.getFechaActual());
		forma.setHoraC(UtilidadFecha.getHoraActual());
		
		UtilidadBD.closeConnection(con);
		
		return mapping.findForward("principal");
	}
}
