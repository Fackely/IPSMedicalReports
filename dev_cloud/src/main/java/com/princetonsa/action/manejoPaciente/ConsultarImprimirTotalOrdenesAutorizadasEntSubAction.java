package com.princetonsa.action.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.manejoPaciente.ConsultarImprimirTotalOrdenesAutorizadasEntSubForm;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.comun.DtoCheckBox;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.manejoPaciente.DtoConsultaTotalOrdenesAutorizadasEntSub;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.generadorReporte.manejoPaciente.consultaOrdenesAutorizadasAEntidadesSub.GeneradorReporteConsultaOrdenesTipoDetallado;
import com.servinte.axioma.generadorReporte.manejoPaciente.consultaOrdenesAutorizadasAEntidadesSub.GeneradorReporteConsultaOrdenesTipoGrupoClase;
import com.servinte.axioma.generadorReporte.manejoPaciente.consultaOrdenesAutorizadasAEntidadesSub.GeneradorReporteConsultaOrdenesTipoNivelAtencion;
import com.servinte.axioma.generadorReporte.manejoPaciente.consultaOrdenesAutorizadasAEntidadesSub.GeneradorReportePlanoConsultaOrdenesTipoDetallado;
import com.servinte.axioma.generadorReporte.manejoPaciente.consultaOrdenesAutorizadasAEntidadesSub.GeneradorReportePlanoConsultaOrdenesTipoGrupoClase;
import com.servinte.axioma.generadorReporte.manejoPaciente.consultaOrdenesAutorizadasAEntidadesSub.GeneradorReportePlanoConsultaOrdenesTipoNivelAtencion;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IConveniosMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IAutorizacionesEntidadesSubMundo;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.delegate.UsuariosDelegate;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.interfaz.facturacion.IEntidadesSubcontratadasServicio;

/**
 * Clase que se encarga de procesar calculos del anexo 925
 * CONSULTAR/IMPRIMIR TOTAL ORDENES AUTORIZADAS A ENT. SUBCONTRATADAS 
 * 
 * @author Camilo Gómez
 */
public class ConsultarImprimirTotalOrdenesAutorizadasEntSubAction extends Action{

	
	MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.manejoPaciente.ConsultarImprimirTotalOrdenesAutorizadasEntSubForm");
	
	/** 
	 * Este Método se encarga de ejecutar las acciones sobre las páginas
	 * de consultar las autorizaciones
	 * 
	 * @param  ActionMapping mapping, HttpServletRequest request,
	 *         HttpServletResponse response
	 * @return ActionForward     
	 * @author
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		
		ActionForward forward=null;
		if (form instanceof ConsultarImprimirTotalOrdenesAutorizadasEntSubForm) {
			
			ConsultarImprimirTotalOrdenesAutorizadasEntSubForm forma = (ConsultarImprimirTotalOrdenesAutorizadasEntSubForm)form;
			String estado = forma.getEstado();
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
			InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			Log4JManager.info("estado " + estado);			
			
			UtilidadTransaccion.getTransaccion().begin();
			
			
			try{
				if(estado.equals("empezar")){
					
					forward= empezar(request,forma,mapping,usuario );				
					
				}else if(estado.equals("consultar")){
					
					UsuariosDelegate usu= new UsuariosDelegate();
					Usuarios usuarioCompleto=usu.findById(usuario.getLoginUsuario());
					
					forma.getDtoFiltros().setNombreUsuario(usuarioCompleto.getPersonas().getPrimerNombre()
							+" "+usuarioCompleto.getPersonas().getPrimerApellido()
							+" ("+usuarioCompleto.getLogin()+")"
							);
					
					if (forma.getTipoSalida() != null && !forma.getTipoSalida().trim().equals("-1")) {
						//this.imprimir(forma,request,usuario,ins);
						consultarOrdenesAutorizadas(request, forma, mapping, usuario, ins);
						forma.setTipoSalida(null);
						forma.setEnumTipoSalida(null);
					}
					 
					forward= mapping.findForward("busqueda");
					
				}/*else if(estado.equals("imprimir")){
					
					String nombreUsuario = usuario.getNombreUsuario();
					forma.getDtoFiltros().setNombreUsuario(nombreUsuario);
					
					if (forma.getTipoSalida() != null && !forma.getTipoSalida().trim().equals("-1")) {
						this.imprimir(forma,request,usuario,ins);
						forma.setTipoSalida(null);
						forma.setEnumTipoSalida(null);
					}
					
					if(forma.getDtoFiltros().getTipoConsulta().equals(ConstantesIntegridadDominio.acronimoTipoConsultaNivelAtencion))
					{	
						return mapping.findForward("listaNivelAtencion");		
					}else 
						if(forma.getDtoFiltros().getTipoConsulta().equals(ConstantesIntegridadDominio.acronimoTipoConsultaGrupoServicioClaseInventario))
						{
							return mapping.findForward("listaGrupoClase");
						}else
							if(forma.getDtoFiltros().getTipoConsulta().equals(ConstantesIntegridadDominio.acronimoTipoConsultaDetallado))
							{
								return mapping.findForward("listaDetallado");
							}
				}*/
				UtilidadTransaccion.getTransaccion().commit();
			}catch (Exception e) {
				UtilidadTransaccion.getTransaccion().rollback();
				Log4JManager.error("Error en la consulta de total ordenes de las autorizaciones ", e);
			}		
		}
		return forward;
	}
	
	
	private void imprimir(ConsultarImprimirTotalOrdenesAutorizadasEntSubForm forma,  HttpServletRequest request, UsuarioBasico usuario, InstitucionBasica ins) {
        
        int tipoSalida         = Integer.parseInt(forma.getTipoSalida());
        String nombreArchivo="";
        int tamano=0;
        if(forma.getDtoFiltros().getEstadosAutorizacion().length==0)
        	tamano=forma.getListaOpcionesEstadosAutoriz().size();
        else
        	tamano=forma.getDtoFiltros().getEstadosAutorizacion().length;
        
        String[] listadosAutorizacion= new String[tamano];
        if(forma.getDtoFiltros().getEstadosAutorizacion().length==0)
        {
        	for(int i=0;i<forma.getListaOpcionesEstadosAutoriz().size();i++)
        	{
        		listadosAutorizacion[i] = forma.getListaOpcionesEstadosAutoriz().get(i).getCodigo();
        	}
        		
        }else
        {
        	listadosAutorizacion = forma.getDtoFiltros().getEstadosAutorizacion();
        }
        
        String[] tipoConsulta = new String[]{forma.getDtoFiltros().getTipoConsulta()};
        
		Connection con=UtilidadBD.abrirConexion();
		
		ArrayList<DtoIntegridadDominio> nombresEstadosAutorizaciones=Utilidades.generarListadoConstantesIntegridadDominio(
				con, listadosAutorizacion, false);
		ArrayList<DtoIntegridadDominio> nombreTipoConsulta=Utilidades.generarListadoConstantesIntegridadDominio(
				con, tipoConsulta, false);
		
		UtilidadBD.closeConnection(con);
		
		ArrayList<DtoIntegridadDominio> nombresEstadosAutorizacionesDef= new ArrayList<DtoIntegridadDominio>();
		for(DtoIntegridadDominio dtoIntegridad:nombresEstadosAutorizaciones)
		{
			if(dtoIntegridad.getAcronimo().equals(ConstantesIntegridadDominio.acronimoAutorizado))
			{
				nombresEstadosAutorizacionesDef.add(dtoIntegridad);
			}
		}
		for(DtoIntegridadDominio dtoIntegridad:nombresEstadosAutorizaciones)
		{
			if(!dtoIntegridad.getAcronimo().equals(ConstantesIntegridadDominio.acronimoAutorizado))
			{
				nombresEstadosAutorizacionesDef.add(dtoIntegridad);
			}
		}
	
		
		forma.getDtoFiltros().setNombresEstadosAutorizaciones(nombresEstadosAutorizacionesDef);
		forma.getDtoFiltros().setNombreTipoConsulta(nombreTipoConsulta.get(0).getDescripcion());
		//Razon social de la institucion
		String razonSocial = ins.getRazonSocial();
		forma.getDtoFiltros().setRazonSocial(razonSocial);
		forma.getDtoFiltros().setNit(ins.getNit());
		forma.getDtoFiltros().setTipoSalida(tipoSalida);
		
        if (tipoSalida > 0) {
        	
        	
        	/*ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto> consultaTiempoEspera=null;
    		if(tipoSalida==EnumTiposSalida.PDF.getCodigo()||tipoSalida==EnumTiposSalida.HOJA_CALCULO.getCodigo()){
    			ICitaOdontologicaServicio servicio = OdontologiaServicioFabrica.crearCitaOdontologicaServicio();
    			consultaTiempoEspera = servicio.consolidarInfoReporteTiemposEspera(forma.getFiltroTiempoEspera());
    		}else{
    			ICitaOdontologicaServicio servicio = OdontologiaServicioFabrica.crearCitaOdontologicaServicio();
    			consultaTiempoEspera = servicio.consolidarInfoReporteTiemposEsperaPlano(forma.getFiltroTiempoEspera());
    			
    		}*/
    		
	    	//if (consultaTiempoEspera != null && consultaTiempoEspera.size()>0) {	
        	
        	//tipoSalida=EnumTiposSalida.PDF.getCodigo();
	    		forma.getDtoFiltros().setUbicacionLogo(ins.getUbicacionLogo());
	    		String rutaLogo = ins.getLogoJsp();
	    		forma.getDtoFiltros().setRutaLogo(rutaLogo);
	    		
	    		JasperReportBuilder reporte=null;
				if(forma.getDtoFiltros().getTipoConsulta().equals(ConstantesIntegridadDominio.acronimoTipoConsultaNivelAtencion))
            	{
            		
            		if (tipoSalida == EnumTiposSalida.PDF.getCodigo()) {
            			GeneradorReporteConsultaOrdenesTipoNivelAtencion generadorReporte =
    	                    new GeneradorReporteConsultaOrdenesTipoNivelAtencion(forma.getDtoFinalTotalOrdenesAutorizadas().getConsolidadoDefinitivo(), forma.getDtoFiltros());
            			reporte = generadorReporte.generarReporte();
            			nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ReporteConsultaTotalOrdenesNivelAtencion");
            		}else
            		if(tipoSalida == EnumTiposSalida.HOJA_CALCULO.getCodigo())
            		{
            			GeneradorReporteConsultaOrdenesTipoNivelAtencion generadorReporte =
    	                    new GeneradorReporteConsultaOrdenesTipoNivelAtencion(forma.getDtoFinalTotalOrdenesAutorizadas().getConsolidadoDefinitivo(), forma.getDtoFiltros());
            			reporte = generadorReporte.generarReporte();
            			nombreArchivo = generadorReporte.exportarReporteExcel(reporte, "ReporteConsultaTotalOrdenesNivelAtencion");
            		}else
            		{
            			GeneradorReportePlanoConsultaOrdenesTipoNivelAtencion generadorReportePlano =
		                    new GeneradorReportePlanoConsultaOrdenesTipoNivelAtencion(forma.getDtoFinalTotalOrdenesAutorizadas().getConsolidadoDefinitivo(), forma.getDtoFiltros());
            			reporte = generadorReportePlano.generarReporte();
            			nombreArchivo = generadorReportePlano.exportarReporteArchivoPlano(reporte, "ReporteConsultaTotalOrdenesNivelAtencion");
            		}
            	}else
            		if(forma.getDtoFiltros().getTipoConsulta().equals(ConstantesIntegridadDominio.acronimoTipoConsultaGrupoServicioClaseInventario))
            		{
            			
            			if (tipoSalida == EnumTiposSalida.PDF.getCodigo()) {
            				GeneradorReporteConsultaOrdenesTipoGrupoClase generadorReporte =
    		                    new GeneradorReporteConsultaOrdenesTipoGrupoClase(forma.getDtoFinalTotalOrdenesAutorizadas().getConsolidadoDefinitivo(), forma.getDtoFiltros());
                			reporte = generadorReporte.generarReporte();
                			nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ReporteConsultaTotalOrdenesGrupoServicioClaseInventario");
                		}else
                		if(tipoSalida == EnumTiposSalida.HOJA_CALCULO.getCodigo())
                		{
                			GeneradorReporteConsultaOrdenesTipoGrupoClase generadorReporte =
    		                    new GeneradorReporteConsultaOrdenesTipoGrupoClase(forma.getDtoFinalTotalOrdenesAutorizadas().getConsolidadoDefinitivo(), forma.getDtoFiltros());
                			reporte = generadorReporte.generarReporte();
                			nombreArchivo = generadorReporte.exportarReporteExcel(reporte, "ReporteConsultaTotalOrdenesGrupoServicioClaseInventario");
                		}else
                		{
                			GeneradorReportePlanoConsultaOrdenesTipoGrupoClase generadorReportePlano =
    		                    new GeneradorReportePlanoConsultaOrdenesTipoGrupoClase(forma.getDtoFinalTotalOrdenesAutorizadas().getConsolidadoDefinitivo(), forma.getDtoFiltros());
                			reporte = generadorReportePlano.generarReporte();
                			nombreArchivo = generadorReportePlano.exportarReporteArchivoPlano(reporte, "ReporteConsultaTotalOrdenesGrupoServicioClaseInventario");
                		}
            		}
            		else
            			{
            			
	            			if (tipoSalida == EnumTiposSalida.PDF.getCodigo()) {
	            				GeneradorReporteConsultaOrdenesTipoDetallado generadorReporte =
	    		                    new GeneradorReporteConsultaOrdenesTipoDetallado(forma.getDtoFinalTotalOrdenesAutorizadas().getConsolidadoDefinitivo(), forma.getDtoFiltros());
	                			reporte = generadorReporte.generarReporte();
	                			nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ReporteConsultaTotalOrdenesDetallado");
	                		}else
	                		if(tipoSalida == EnumTiposSalida.HOJA_CALCULO.getCodigo())
	                		{
	                			GeneradorReporteConsultaOrdenesTipoDetallado generadorReporte =
	    		                    new GeneradorReporteConsultaOrdenesTipoDetallado(forma.getDtoFinalTotalOrdenesAutorizadas().getConsolidadoDefinitivo(), forma.getDtoFiltros());
	                			reporte = generadorReporte.generarReporte();
	                			nombreArchivo = generadorReporte.exportarReporteExcel(reporte, "ReporteConsultaTotalOrdenesDetallado");
	                		}else
	                		{
	                			GeneradorReportePlanoConsultaOrdenesTipoDetallado generadorReportePlano =
	    		                    new GeneradorReportePlanoConsultaOrdenesTipoDetallado(forma.getDtoFinalTotalOrdenesAutorizadas().getConsolidadoDefinitivo(), forma.getDtoFiltros());
	                			reporte = generadorReportePlano.generarReporte();
	                			nombreArchivo = generadorReportePlano.exportarReporteArchivoPlano(reporte, "ReporteConsultaTotalOrdenesDetallado");
	                		}
            			}
            		
	            
	            
	            
	            forma.setNombreArchivo(nombreArchivo);
	            
	       /* }
	        else{
	        	
	        	ActionErrors errores=new ActionErrors();
	            errores.add("No se encontraron resultados", new ActionMessage("errors.modOdontoReporteTiemposEspera"));
	            saveErrors(request, errores);
	            
	            
	        }*/
	     }
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar las autorizaciones de ordenes.
	 * 
	 * @param ConsultarImprimirTotalOrdenesAutorizadasEntSubForm forma, ActionMapping mapping
	 * @return ActionForward
	 * @author Camilo Gómez
	 */
	public ActionForward empezar(HttpServletRequest request,ConsultarImprimirTotalOrdenesAutorizadasEntSubForm forma,
			ActionMapping mapping,UsuarioBasico usuario )
	{
		IEntidadesSubcontratadasServicio entidadServicio = 
			FacturacionServicioFabrica.crearEntidadesSubcontratadasServicio();			
		ArrayList<DtoEntidadSubcontratada> listaEntidadesSub  =			
			entidadServicio.listarEntidadesSubActivasEnSistema();
		
		IConveniosMundo conveniosMundo=FacturacionFabricaMundo.crearcConveniosMundo();
		ArrayList<Convenios> listaConveniosActivos=new ArrayList<Convenios>();
			listaConveniosActivos=conveniosMundo.listarConveniosActivos();
				
		forma.reset();
		//listado de los estados de las autorizaciones
		ArrayList<DtoCheckBox> listaOpcionEstados=new ArrayList<DtoCheckBox>();
		DtoCheckBox opcionEstado  = new DtoCheckBox();
		
		opcionEstado  = new DtoCheckBox();
		opcionEstado.setCodigo(ConstantesIntegridadDominio.acronimoAutorizado);
		opcionEstado.setNombre(ValoresPorDefecto.getIntegridadDominio(opcionEstado.getCodigo())+"");
		listaOpcionEstados.add(opcionEstado);
		
		opcionEstado  = new DtoCheckBox();
		opcionEstado.setCodigo(ConstantesIntegridadDominio.acronimoEstadoAnulado);
		opcionEstado.setNombre(ValoresPorDefecto.getIntegridadDominio(opcionEstado.getCodigo())+"");
		listaOpcionEstados.add(opcionEstado);	
				
			forma.setListaOpcionesEstadosAutoriz(listaOpcionEstados);
		
		//listado de los tipos de consulta de las autorizaciones
		ArrayList<DtoCheckBox> listaOpcionConsulta=new ArrayList<DtoCheckBox>();
		DtoCheckBox opcionConsulta  = new DtoCheckBox();
		
		opcionConsulta  = new DtoCheckBox();
		opcionConsulta.setCodigo(ConstantesIntegridadDominio.acronimoTipoConsultaNivelAtencion);
		opcionConsulta.setNombre(ValoresPorDefecto.getIntegridadDominio(opcionConsulta.getCodigo())+"");
		listaOpcionConsulta.add(opcionConsulta);
		
		opcionConsulta  = new DtoCheckBox();
		opcionConsulta.setCodigo(ConstantesIntegridadDominio.acronimoTipoConsultaGrupoServicioClaseInventario);
		opcionConsulta.setNombre(ValoresPorDefecto.getIntegridadDominio(opcionConsulta.getCodigo())+"");
		listaOpcionConsulta.add(opcionConsulta);	
						
		opcionConsulta  = new DtoCheckBox();
		opcionConsulta.setCodigo(ConstantesIntegridadDominio.acronimoTipoConsultaDetallado);
		opcionConsulta.setNombre(ValoresPorDefecto.getIntegridadDominio(opcionConsulta.getCodigo())+"");
		listaOpcionConsulta.add(opcionConsulta);
		
			forma.setListaOpcionesTiposConsulta(listaOpcionConsulta);
		
		
		if((!Utilidades.isEmpty(listaEntidadesSub))){
			forma.setListaEntidadesSub(listaEntidadesSub);
		}	
		if(!Utilidades.isEmpty(listaConveniosActivos)){
			forma.setListaConvenios(listaConveniosActivos);
		}
		
		
		return mapping.findForward("busqueda");	
	}
	
	
	
	
	/**
	 * Metodo que se encarga de llenar los campos paa la busqueda de las ordenes
	 * @param forma
	 * @param mapping
	 * @param usuario
	 */
	public void inicializarCampos(ConsultarImprimirTotalOrdenesAutorizadasEntSubForm forma )
	{
		forma.setEstado("");	
	}
	/**
	 * Este Método se encarga de consultar y calcular los totales de las ordenes autorizadas de entidades subcontratadas
	 * 
	 * @param ConsultarImprimirTotalOrdenesAutorizadasEntSubForm forma, ActionMapping mapping
	 * @return ActionForward
	 * @author Camilo Gómez 
	 *
	 */
	public void consultarOrdenesAutorizadas(HttpServletRequest request,ConsultarImprimirTotalOrdenesAutorizadasEntSubForm forma,
			ActionMapping mapping,UsuarioBasico usuario, InstitucionBasica ins)
	{				
		ActionErrors errores=null;
		errores=new ActionErrors();		
		MessageResources mensajes=MessageResources.getMessageResources(
		"com.servinte.mensajes.manejoPaciente.ConsultarImprimirTotalOrdenesAutorizadasEntSubForm");
		
		DtoConsultaTotalOrdenesAutorizadasEntSub autorizaciones=null;
		
		//ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> listaAutorizaciones = null;
		IAutorizacionesEntidadesSubMundo autorizacionesEntidadesSubMundo=ManejoPacienteFabricaMundo.crearAutorizacionEntidadesSubMundo();
			
		String tamEstados=request.getParameter("numEstados");
		String estado="";
		ArrayList<String> tempEstados=new ArrayList<String>();
		for (int i=0;i<Utilidades.convertirAEntero(tamEstados);i++) 
		{	
			estado=request.getParameter("estadoAutoriz("+i+")");
			if(!UtilidadTexto.isEmpty(estado))
			{	
				tempEstados.add(estado);
			}
		}
		String []estadosAutorizacion=new String[tempEstados.size()];
		for (int i=0;i<tempEstados.size();i++) 
		{	
			estadosAutorizacion[i]=tempEstados.get(i);
		}		
		
		forma.getDtoFiltros().setEstadosAutorizacion(estadosAutorizacion);
		forma.getDtoFiltros().setInstitucion(usuario.getCodigoInstitucionInt());
		
		
		//consulta de las autorizaciones de entidades Sub	-->
		//-->Donde 'autorizaciones' -->Contiene la lista de entidad-convenio-nivel
		//							-->Y contiene el hashMap de los totales Finales
		autorizaciones=autorizacionesEntidadesSubMundo.ordenarOrdenesAutorizadasEntSub(forma.getDtoFiltros());
		
		if(autorizaciones!=null)
		{
			for(DtoConsultaTotalOrdenesAutorizadasEntSub dtoConsol:autorizaciones.getConsolidado())
			{
				Date fechaFin=new Date();
				try{					
					Calendar fechaCalendar=Calendar.getInstance();
					fechaCalendar.setTime(forma.getDtoFiltros().getFechaFinBusqueda());
					fechaCalendar.add(Calendar.DAY_OF_MONTH, 1);
					fechaFin=fechaCalendar.getTime();
					//dto.setFechaFinBusqueda(fechaCalendar.getTime());
					
				} catch (Exception e) {
					Log4JManager.error("Error cambiando el formato de la fecha", e);
					e.printStackTrace();
				}
				String fechaFinBusqueda=UtilidadFecha.conversionFormatoFechaAAp(fechaFin);
				String fechaAutorizacion=UtilidadFecha.conversionFormatoFechaAAp(dtoConsol.getFechaAutorizacion());
				if(!fechaFinBusqueda.equals(fechaAutorizacion))
					autorizaciones.getConsolidadoDefinitivo().add(dtoConsol);
			}
		}
		
		
		if(autorizaciones!=null)
		{	
			forma.setDtoFinalTotalOrdenesAutorizadas(autorizaciones);
			boolean sinNivel=false;
			
			for (DtoConsultaTotalOrdenesAutorizadasEntSub buscaInconsis : forma.getDtoFinalTotalOrdenesAutorizadas().getConsolidadoDefinitivo()) 
			{			
				if(buscaInconsis.getInconsistencia()!=null)
				{
					errores.add("nivel de atención requerido", new ActionMessage("errors.notEspecific", 
							mensajes.getMessage("ConsultarImprimirTotalOrdenesAutorizadasEntSubForm.errorNivelAtencion",
									buscaInconsis.getInconsistencia().getDescripcion())));
					
					sinNivel=true;
				}				
			}
			
			saveErrors(request, errores);
			if(!sinNivel)
				this.imprimir(forma, request, usuario, ins);
				//return mapping.findForward("busqueda");
			
		}else{
			forma.setSinOrdenes(true);
		}
		
		/*if(forma.getDtoFiltros().getTipoConsulta().equals(ConstantesIntegridadDominio.acronimoTipoConsultaNivelAtencion))
		{	
			return mapping.findForward("listaNivelAtencion");		
		}else 
			if(forma.getDtoFiltros().getTipoConsulta().equals(ConstantesIntegridadDominio.acronimoTipoConsultaGrupoServicioClaseInventario))
			{
				return mapping.findForward("listaGrupoClase");
			}else
				if(forma.getDtoFiltros().getTipoConsulta().equals(ConstantesIntegridadDominio.acronimoTipoConsultaDetallado))
				{
					return mapping.findForward("listaDetallado");
				}*/
		//this.imprimir(forma, request, usuario, ins);
		//return mapping.findForward("busqueda");
		
	}
	
}


