package com.princetonsa.action.carteraPaciente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
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
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.carteraPaciente.ConsultarRefinanciarCuotaPacienteForm;
import com.princetonsa.dto.carteraPaciente.DtoCuotasDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoIngresosFacturasAtenMedica;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.carteraPaciente.ConsultarRefinanciarCuotaPaciente;
import com.princetonsa.pdf.FormatoGarantiaLetraPdf;
import com.princetonsa.sort.odontologia.SortGenerico;



public class ConsultarRefinanciarCuotaPacienteAction extends Action
{
	Logger logger = Logger.getLogger(ConsultarRefinanciarCuotaPacienteAction.class);
	ConsultarRefinanciarCuotaPaciente mundo;

	public ActionForward execute(ActionMapping mapping,
			 ActionForm form, 
			 HttpServletRequest request, 
			 HttpServletResponse response) throws Exception
		{
		Connection con = null;
		try{
		if(response == null);
		
		if (form instanceof ConsultarRefinanciarCuotaPacienteForm) 
		{			 
			
			con = UtilidadBD.abrirConexion();
			
			if(con == null)
			{
			request.setAttribute("CodigoDescripcionError","erros.problemasBd");
			return mapping.findForward("paginaError");
			}
			
			//Usuario cargado en session
			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			//paciente cargado en sesion 
			PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			//Institucion
			InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			mundo = new ConsultarRefinanciarCuotaPaciente();
			
			ConsultarRefinanciarCuotaPacienteForm forma = (ConsultarRefinanciarCuotaPacienteForm)form;		
			String estado = forma.getEstado();		 
			
			logger.info("-------------------------------------");
			logger.info("Valor del Estado    >> "+forma.getEstado());			 
			logger.info("-------------------------------------");
			logger.info("-------------------------------------");
			
			if(estado.equals("empezar"))
			{ 
			    forma.reset();	  
				UtilidadBD.closeConnection(con); 
				return mapping.findForward("principal");
			}
			else if(estado.equals("consultaPaciente")){
				
				forma.reset();
				ActionForward forward = new ActionForward();
				  forward=accionValidarPaciente(con, forma, paciente, usuario, request, mapping);
						if(forward != null)
							return forward;	
				UtilidadBD.closeConnection(con);
				forma.setOpcionListado("paciente");
				return listadoIngresos(forma, paciente, usuario, request, mapping);
				
			}else if(estado.equals("consultarRango")){
			   
				forma.reset();
			    UtilidadBD.closeConnection(con);
				forma.setOpcionListado("rango");
				return accionBuscarRango(forma,usuario,request,mapping);
				
			}else if(estado.equals("buscarDocumentosCartera")){
				
				UtilidadBD.closeConnection(con);
				return listadoDocumentosGarantiaXRango(forma, usuario, request, mapping);
			
			}else if(estado.equals("detalleRefinanciacion")){
				
				if(!Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(),252))
				{
					forma.setPermisosUsuario(false);
					logger.info("Permisos Usuario "+forma.isPermisosUsuario());
				}
				UtilidadBD.closeConnection(con);
				return mapping.findForward("menuRefinanciacion");				
			
			}else if(estado.equals("verDetalleAplicacionPagos"))
			{
				UtilidadBD.closeConnection(con);
				return mapping.findForward("detalleAplicacionPagos");
				
			}else if (estado.equals("refinanciar"))
			{	 
				  forma.setParametrosFiltros("operacionExitosa","");
				  return accionValidarRequisitos(con, forma, paciente, usuario, request, mapping);						
				
			}else if(estado.equals("editarCuotasRefinanciacion"))
			{
				UtilidadBD.closeConnection(con);
				return accionEditarCuotasRefinanciacion(forma, paciente, usuario, request, mapping);				
				
			}else if(estado.equals("guardarCuotasRefinanciacion"))
			{    
				  UtilidadBD.closeConnection(con);
			     return accionGuardarCuotasRefinanciacion(forma, paciente, usuario, request, mapping);
				
			}else if(estado.equals("guardarRefinanciacion"))
			{
				return accionGuardarRefinanciacion(con, forma, paciente, usuario, request, mapping);
				
			}else if (estado.equals("consultarHistorial"))
			{		
				UtilidadBD.closeConnection(con);
			    return accionConsultarHistorialRefinanciacion(forma,request, mapping);	
				
			}else if(estado.equals("formatoImprimir"))
			{  
			   UtilidadBD.closeConnection(con);
			   forma.setImprimePagare(false);
			   forma.setFormatoDefinido(true);
			   forma.setParametrosFiltros("operacionExitosa","");
			   
			   if(forma.getListaIngresos().get(forma.getPosIngreso()).getTipoDocumentoCartera().equals(ConstantesIntegridadDominio.acronimoTipoDocumentoPagare))
			   {
				forma.setImprimePagare(true);   
				if(!ValoresPorDefecto.getFormatoDocumentosGarantia_Pagare(usuario.getCodigoInstitucionInt()).equals(ConstantesIntegridadDominio.acronimoFormatoDocGarantiaShaio))
				    {					  
					   forma.setFormatoDefinido(false);
					   forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoNo);
			           forma.setParametrosFiltros("mensaje","Sin formato definido no se puede realizar impresión");
				    }
				
			   }
			   
			   return mapping.findForward("impresionGarantiaLetra");
				
			}else if(estado.equals("imprimir"))
			{
			   return accionImprimir(con, forma, usuario,request,mapping);
				
			}else if(estado.equals("imprimirpagare")||estado.equals("imprimirCartaInst")||estado.equals("imprimirActaCompromisoConsulta"))
			{
				return this.accionImprimirVoucherChequeLetraActa(con, forma, mapping, request, usuario, estado,0,"");
			}			
			else if (estado.equals("ordenar"))
			{	
				UtilidadBD.closeConnection(con);    		
				return accionOrdenar(forma,request,mapping);
			
			}else if (estado.equals("redireccion"))
			{
				UtilidadBD.closeConnection(con);
				response.sendRedirect(forma.getLinkSiguiente());
				return null;
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



	private ActionForward accionImprimirVoucherChequeLetraActa(Connection con,ConsultarRefinanciarCuotaPacienteForm forma, ActionMapping mapping,HttpServletRequest request, UsuarioBasico usuario, String estado,int i, String string) {
		
        String nombreRptDesign="",forward="",tipo="";
		
        if (estado.equals("imprimirpagare"))
		{
			nombreRptDesign = "docgarantiaPagare1.rptdesign";
			tipo="impresion";
		}
		else{
			if (estado.equals("imprimirActaCompromiso"))
			{
				nombreRptDesign = "docgarantiaActaCompromiso.rptdesign";
				tipo="impresion";
			}
			else
			{
				if (estado.equals("imprimirCartaInst"))
				{
					nombreRptDesign = "docgarantiaCartaInst.rptdesign";
					tipo="impresion";
				}
			}
		}
        UtilidadBD.closeConnection(con);
		return null;
	}



	/**
	 * Metodo que realiza el proceso de Refinanciacion
	 * @param con
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionGuardarRefinanciacion(Connection con,ConsultarRefinanciarCuotaPacienteForm forma,PersonaBasica paciente, UsuarioBasico usuario,HttpServletRequest request, ActionMapping mapping) {
		
		 ActionErrors errores =new ActionErrors();
		 errores = mundo.ValidarGuardarRefinanciacion(forma, usuario);

		 if(!errores.isEmpty())
		  {	 
			 saveErrors(request, errores);
			
		  } else
		  {
		     HashMap resultado = new HashMap();
			 HashMap datos=new HashMap();
			 datos.put("codigoFin", ConstantesBD.codigoNuncaValido);
			 datos.put("numCuotas",forma.getNroCuotasRefinanciacion());
			 datos.put("numCuotasAnt", forma.getNroCuotasRefinanciacionActual());
			 datos.put("diasporCuota",forma.getDiasporCuotaRefinanciacion());
			 datos.put("fechaInicio",forma.getFechaInicioRefinanciacion());
			 datos.put("observaciones",forma.getObservacionesRefinanciacion());
			 datos.put("valorCuota", forma.getValorCuota());
			 datos.put("usuario", usuario.getLoginUsuario());
			 datos.put("listaCuotasRefinanciacion",(ArrayList<DtoCuotasDatosFinanciacion>)forma.getCuotasRefinanciacion());
			 datos.put("dtoIngresoPaciente",(DtoIngresosFacturasAtenMedica)forma.getListaIngresos().get(forma.getPosIngreso()));
			  
			 UtilidadBD.iniciarTransaccion(con);
			 resultado = mundo.guardarRefinanciacion(con,datos);
			 
			 if(Utilidades.convertirAEntero(resultado.get("codigoFin").toString())>=1)
			 {
				    forma.setNroCuotasRefinanciacionActual(forma.getNroCuotasRefinanciacion());
				    
				    forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoSi);
			   	    forma.setParametrosFiltros("mensaje","Proceso Exitoso ");
			   	    UtilidadBD.finalizarTransaccion(con);
		     }	 
			 else
			  {
		        UtilidadBD.abortarTransaccion(con);

		        forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoNo);
		        forma.setParametrosFiltros("mensaje","El Proceso No fue Exitoso");
		        errores = (ActionErrors)resultado.get("error");
		        saveErrors(request, errores);	
					
			  }
			 
		    UtilidadBD.closeConnection(con);
		    return mapping.findForward("formatoRefinanciacion");
				 
			 
			  
		  }
		 UtilidadBD.closeConnection(con);
		 return  mapping.findForward("formatoRefinanciacion");
		 
	}

	
	/**
    * Metodo que consulta los ingresos de un pacinte, asociados a facturas da atencion medica 
    * @param forma
    * @param paciente
    * @param usuario
    * @param request
    * @param mapping
    * @return
    */
	private ActionForward listadoIngresos(ConsultarRefinanciarCuotaPacienteForm forma,PersonaBasica paciente, UsuarioBasico usuario,HttpServletRequest request, ActionMapping mapping) {
		
		forma.setListaIngresos(mundo.listarIngresosFacAtenMedica(paciente.getCodigoPersona()));
		return mapping.findForward("listaIngresosPaciente");
	}
	
	  
	/**
	 * 
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward listadoDocumentosGarantiaXRango(ConsultarRefinanciarCuotaPacienteForm forma, UsuarioBasico usuario,HttpServletRequest request, ActionMapping mapping) {
		
		
		 ActionErrors errores = new ActionErrors();
		  errores = mundo.validacionBusquedaporRango(forma.getParametrosBusqueda());		
		 
			if(errores.isEmpty())
			{
	 	      forma.setListaIngresos(mundo.listarDocsCarteraPacienteXRango(forma.getParametrosBusqueda()));
	 	      return mapping.findForward("listaIngresosPaciente");
			}
			else{
				saveErrors(request, errores);	
				return mapping.findForward("busquedaXRango");
			}
	 	      
	}

	/**
     * Metodo que captura los datos de Busqueda por rango
     * @param forma
     * @param usuario
     * @param request
     * @param mapping
     * @return
     */
	private ActionForward accionBuscarRango(ConsultarRefinanciarCuotaPacienteForm forma, UsuarioBasico usuario,HttpServletRequest request, ActionMapping mapping) {
		
		forma.setParametrosBusqueda(mundo.inicializarParametrosBusquedaRango(usuario.getCodigoCentroAtencion()));
		forma.setListadoCentroAtencion(Utilidades.obtenerCentrosAtencion(usuario.getCodigoInstitucionInt()));
		
		return mapping.findForward("busquedaXRango");
	}
	
	
	/**
	 * Metodo que realiza la consulta del Historico de las refinanciaciones 
	 * @param forma
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionConsultarHistorialRefinanciacion(ConsultarRefinanciarCuotaPacienteForm forma,HttpServletRequest request, ActionMapping mapping) {
		
		forma.setHistoricoRefinanciacion(mundo.consultarHistoricoRefinanciacion(forma.getListaIngresos().get(forma.getPosIngreso()).getCodDatosFinanciacion()));
		return mapping.findForward("historicoFinanciacion");

	}

    
	/**
	 * Metodo que realiza el ordenamiento por columnas
	 * @param forma
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(ConsultarRefinanciarCuotaPacienteForm forma,HttpServletRequest request, ActionMapping mapping) {
		
		
		boolean ordenamiento= false;
		
		if(forma.getEsDescendente().equals(forma.getPropiedadOrdenar()))
		{
			forma.setEsDescendente(forma.getPropiedadOrdenar()+"descendente") ;
		}else{
			forma.setEsDescendente(forma.getPropiedadOrdenar());
		}	
		
		logger.info("patron ORDENAR-> " + forma.getPropiedadOrdenar());
		logger.info("DESCENDENTE --> " + forma.getEsDescendente() );
		
		if(forma.getEsDescendente().equals(forma.getPropiedadOrdenar()+"descendente")){
			
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPropiedadOrdenar(),ordenamiento);
		Collections.sort(forma.getListaIngresos(),sortG);
		return mapping.findForward("listaIngresosPaciente");
		
	}
	
	
	
	/**
	 * Metodo que realiza la validacion del paciente cargado en sesion
	 * @param con
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	 private ActionForward accionValidarPaciente(Connection con,ConsultarRefinanciarCuotaPacienteForm forma, PersonaBasica paciente,UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
		{
			if(paciente==null || paciente.getCodigoPersona()<=0)
			{
				UtilidadBD.closeConnection(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.noCargado", "errors.paciente.noCargado", true);
			}
			UtilidadBD.closeConnection(con);
			return null;
			
		}
	 
	 /**
	  * Metodo para validar los requisitos necesarios para realizar la refinanciacion
	  * @param con
	  * @param forma
	  * @param paciente
	  * @param usuario
	  * @param request
	  * @param mapping
	  * @return
	  */
	 private ActionForward accionValidarRequisitos(Connection con,ConsultarRefinanciarCuotaPacienteForm forma, PersonaBasica paciente,UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping)
	 {
		 ActionErrors errores =new ActionErrors();
		
		 
		if(!forma.getListaIngresos().get(forma.getPosIngreso()).getEstadoDocCartera().equals(ConstantesIntegridadDominio.acronimoPolizaVigente))
		{
			     
			   errores.add("descripcion",new ActionMessage("errors.notEspecific","EL docuemento debe estar Vigente"));
			   saveErrors(request, errores);
		}else
		{
			if(Utilidades.convertirAEntero(forma.getListaIngresos().get(forma.getPosIngreso()).getSaldo())<=0)
			{
				     
				   errores.add("descripcion",new ActionMessage("errors.notEspecific","EL Paciente no tiene Saldos Pendientes"));
				   saveErrors(request, errores);
			}
		}
		 
		UtilidadBD.closeConnection(con);
		if(!errores.isEmpty())
		{ 			
			return mapping.findForward("menuRefinanciacion"); 
		}
		if(forma.getNroCuotasRefinanciacion()<=0)
		{
			forma.setNroCuotasRefinanciacion(forma.getListaIngresos().get(forma.getPosIngreso()).getNumCuotasFinanciacion());
			forma.setDiasporCuotaRefinanciacion(forma.getListaIngresos().get(forma.getPosIngreso()).getDiaporcuotaFinanciacion());
			forma.setFechaInicioRefinanciacion(UtilidadFecha.conversionFormatoFechaAAp(forma.getListaIngresos().get(forma.getPosIngreso()).getFechaInicioFinanciacion()));
			
			if(forma.getNroCuotasRefinanciacionActual()<=0)
				forma.setNroCuotasRefinanciacionActual(forma.getNroCuotasRefinanciacion());
			   
		}		
		
		forma.setNroMaximoCuotasRefinanciacion(Utilidades.convertirAEntero(ValoresPorDefecto.getMaximoNumeroCuotasFinanciacion(usuario.getCodigoInstitucionInt())));
		
		
		logger.info("Numero Maximo >> "+ forma.getNroMaximoCuotasRefinanciacion());
		logger.info("Dias por cuota >>"+forma.getDiasporCuotaRefinanciacion());
		logger.info("MAX dias por CUOTA >>" + ValoresPorDefecto.getMaximoNumeroDiasFinanciacionPorCuota(usuario.getCodigoInstitucionInt()));
		logger.info("Numero Cuotas Refinanciacion >>" + forma.getNroCuotasRefinanciacion());
		logger.info("Numero Cuotas Refinanciacion ACTUAL >>" + forma.getNroCuotasRefinanciacionActual());
		return mapping.findForward("formatoRefinanciacion");
		 
	 }
	 
	 
	 /**
	  * Metodo para editar las cuotas de refinanciacion
	  * @param con
	  * @param forma
	  * @param paciente
	  * @param usuario
	  * @param request
	  * @param mapping
	  * @return
	  */
	 private ActionForward accionEditarCuotasRefinanciacion(ConsultarRefinanciarCuotaPacienteForm forma,PersonaBasica paciente, UsuarioBasico usuario,HttpServletRequest request, ActionMapping mapping) {
			
		   forma.resetCuotasRefinanciacion();
		   BigDecimal calculoCuota= new BigDecimal(0);
		   BigDecimal sumParcial=new BigDecimal(0);
		   BigDecimal saldo = new BigDecimal(forma.getListaIngresos().get(forma.getPosIngreso()).getSaldo());
		   calculoCuota = saldo.divide(new BigDecimal(forma.getNroCuotasRefinanciacion()),2, BigDecimal.ROUND_HALF_EVEN);
		   logger.info("valor cuota: "+calculoCuota);
		   logger.info("saldo: "+saldo);
		   for(int i=1; i< forma.getNroCuotasRefinanciacion();i++)
		   {   
			   DtoCuotasDatosFinanciacion dto = new DtoCuotasDatosFinanciacion();			   
			   sumParcial = sumParcial.add(calculoCuota);
			   dto.setValorCuota(calculoCuota);
			   logger.info("Valor cuota >>"+ dto.getValorCuota());
			   forma.getCuotasRefinanciacion().add(dto);
			   forma.setValorCuota(dto.getValorCuota()+"");
		   }
		   
		   logger.info("suma parcial: "+sumParcial);
		   logger.info("saldo: "+saldo);
		   logger.info("valor cuota: "+calculoCuota);
		   calculoCuota = saldo.subtract(sumParcial).abs();
		   DtoCuotasDatosFinanciacion dtoUltimo = new DtoCuotasDatosFinanciacion();			   
	   	   dtoUltimo.setValorCuota(calculoCuota);
	   	   forma.getCuotasRefinanciacion().add(dtoUltimo);
	   	   logger.info("Ultima Cuota >>"+ dtoUltimo.getValorCuota());
	   	   
	   	   if(forma.getNroCuotasRefinanciacion()==1)
	   	   {
	   	    forma.setValorCuota(calculoCuota+"");
	   	   }
		   
		   
		   return mapping.findForward("cuotasRefinanciacion");
		}
	

/**
 * Metodo para validar datos Cuotas Financiacion
 * @param forma
 * @param paciente
 * @param usuario
 * @param request
 * @param mapping
 * @return
 */
	 private ActionForward accionGuardarCuotasRefinanciacion(ConsultarRefinanciarCuotaPacienteForm forma,PersonaBasica paciente, UsuarioBasico usuario,	HttpServletRequest request, ActionMapping mapping) 
	 {
	 		
		  ActionErrors errores =new ActionErrors();
		  int numCuota=1;
		  for(int i=0; i< forma.getNroCuotasRefinanciacion();i++)
		  {
			  if(forma.getCuotasRefinanciacion().get(i).getNumeroDocumento().equals(""))
			  {
				  
				   errores.add("descripcion",new ActionMessage("errors.notEspecific","EL Numero de docuemento del la cuota "+numCuota+" es Requerido"));
				   saveErrors(request, errores);
			  }
			  numCuota++;
		  }
		  if(errores.isEmpty())
		  {
			forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoSi);  
		  }
		  return mapping.findForward("cuotasRefinanciacion");
	 	}
	 
	 
	 /**
		 * Metodo que realiza la impresion de la Garantia
		 * @param con
		 * @param forma
		 * @param request
		 * @param mapping
		 * @return
		 */
		private ActionForward accionImprimir(Connection con,ConsultarRefinanciarCuotaPacienteForm forma,UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
			
			request.setAttribute("nombreArchivo",FormatoGarantiaLetraPdf.pdfFormatoImpresionGarantiaLetra(usuario, request, forma));
	    	request.setAttribute("nombreVentana", "Impresión Garantía de Letra ");
	    	UtilidadBD.closeConnection(con);
	    	return mapping.findForward("abrirPdf");
		}

	 
	 
	 
	 
}
