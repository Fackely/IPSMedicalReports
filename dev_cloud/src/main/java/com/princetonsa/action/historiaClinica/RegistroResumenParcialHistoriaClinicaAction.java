package com.princetonsa.action.historiaClinica;

import java.sql.Connection;
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

import util.ConstantesIntegridadDominio;
import util.RespuestaValidacion;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadValidacion;
import util.Utilidades;
import util.historiaClinica.UtilidadesHistoriaClinica;
import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.historiaClinica.RegistroResumenParcialHistoriaClinicaForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.RegistroResumenParcialHistoriaClinica;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * Clase para el manejo del workflow de GeneracionArchivoPlanoIndicadoresCalidadAction
 * Date: 2008-01-22
 * @author lgchavez@princetonsa.com
 */
public class RegistroResumenParcialHistoriaClinicaAction extends Action {
	
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(RegistroResumenParcialHistoriaClinicaAction.class);
	
	/**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
    {
    	Connection con = null;
    	try {
    	if(response==null);
    	if(form instanceof RegistroResumenParcialHistoriaClinicaForm)
    	{
    		
    		con = UtilidadBD.abrirConexion();
    		
    		if(con == null)
    		{	
    			request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    			return mapping.findForward("paginaError");
    		}
    		RegistroResumenParcialHistoriaClinicaForm forma = (RegistroResumenParcialHistoriaClinicaForm)form;
    		String estado = forma.getEstado();
    	
    		logger.info("\n\n ESTADO REGISTRO_RESUMEN_PARCIAL_HISTORIA_CLINICA ---->"+estado+"\n\n");
    		
    		ActionErrors errores = new ActionErrors();
    		UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    		PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
    		
    		if(estado == null)
    		{
    			forma.reset();
    			logger.warn("Estado no valido dentro del Flujo de Unidad de Procedimiento (null)");
    			request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
    			UtilidadBD.cerrarConexion(con);
    			return mapping.findForward("paginaError");
    		}
    		else if(estado.equals("empezar"))
    		{    			
				//Validaciones

   			 logger.info("********VALIDACION PACIENTE EN VALORACIONES*******************");
   				//Se realiza la validacion del paciente
   				RespuestaValidacion resp = UtilidadValidacion.esValidoPacienteCargado(con, paciente);
   				
   				logger.info("respuesta validacion : "+resp.puedoSeguir+", "+resp.textoRespuesta);
   				logger.info("codigo ingreso=> "+ paciente.getCodigoIngreso());
   				logger.info("********FIN VALIDACION PACIENTE EN VALORACIONES*******************");
   				//Se valida si el paciente tiene cuenta abierta
   				if(!resp.puedoSeguir)
   				{
   					//Se verifica si el paciente tiene cuentas abiertas en otros centros de atencion
   					//consultan el nombre del centro de atencion de alguna cuenta activa que tenga el paciente
   					String nomCentroAtencion = Utilidades.getNomCentroAtencionIngresoAbierto(con,paciente.getCodigoPersona()+"");
   					if(nomCentroAtencion.equals(""))
   						errores.add("error validacion paciente",new ActionMessage(resp.textoRespuesta));
   					else
   						errores.add("Paciente con ingreso abierto en otro centro de atencion",new ActionMessage("errores.paciente.ingresoAbiertoCentroAtencion",nomCentroAtencion));
   						
   					saveErrors(request,errores);
   					UtilidadBD.cerrarConexion(con);
   					return mapping.findForward("error");
   				}
   				else if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getAcronimo().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
   				{
   					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "no ingreso abierto", "errors.paciente.noIngreso", true);
   				}
   				//Validación de autoatencion
   				ResultadoBoolean respuesta = UtilidadesHistoriaClinica.esUsuarioAutoatendido(usuario, paciente);
   				if(respuesta.isTrue())
   					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El paciente no puede ser autoatendido", respuesta.getDescripcion(), true);
   				
   			
   			
   			if(!errores.isEmpty())
				{
					saveErrors(request,errores);	
					UtilidadBD.closeConnection(con);
					return mapping.findForward("error");	
				}
				else{
					return this.accionEmpezar(forma, con, mapping, usuario, paciente);
				} 
   			}
    		else if(estado.equals("empezar1"))
    		{    			
				//Validaciones

   			 logger.info("********VALIDACION PACIENTE EN VALORACIONES*******************");
   				//Se realiza la validacion del paciente
   				RespuestaValidacion resp = UtilidadValidacion.esValidoPacienteCargado(con, paciente);
   				
   				logger.info("respuesta validacion : "+resp.puedoSeguir+", "+resp.textoRespuesta);
   				logger.info("codigo ingreso=> "+ paciente.getCodigoIngreso());
   				logger.info("********FIN VALIDACION PACIENTE EN VALORACIONES*******************");
   				//Se valida si el paciente tiene cuenta abierta
   				if(!resp.puedoSeguir)
   				{
   					//Se verifica si el paciente tiene cuentas abiertas en otros centros de atencion
   					//consultan el nombre del centro de atencion de alguna cuenta activa que tenga el paciente
   					String nomCentroAtencion = Utilidades.getNomCentroAtencionIngresoAbierto(con,paciente.getCodigoPersona()+"");
   					if(nomCentroAtencion.equals(""))
   						errores.add("error validacion paciente",new ActionMessage(resp.textoRespuesta));
   					else
   						errores.add("Paciente con ingreso abierto en otro centro de atencion",new ActionMessage("errores.paciente.ingresoAbiertoCentroAtencion",nomCentroAtencion));
   						
   					saveErrors(request,errores);
   					UtilidadBD.cerrarConexion(con);
   					return mapping.findForward("error");
   				}
   				else if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getAcronimo().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
   				{
   					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "no ingreso abierto", "errors.paciente.noIngreso", true);
   				}
   			
   			
   			if(!errores.isEmpty())
				{
					saveErrors(request,errores);	
					UtilidadBD.closeConnection(con);
					return mapping.findForward("error");	
				}
				else{
					return this.accionEmpezar1(forma, con, mapping, usuario, paciente);
				} 
   			}
    		
    		else 
    			if (estado.equals("verDetalle")) {
    				return this.accionVerDetalle(forma, con, mapping, usuario, paciente);
			}
        	else 
        		if (estado.equals("guardarNuevo")) {
        			return this.accionGuardarNuevo(forma, con, mapping, usuario, paciente,request);
        		}
			else if(estado.equals("imprimir"))
			{
				this.generarReporte(con, forma, mapping, request, usuario, paciente);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("detalle");
			}

    	}
    	return null;
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
    }
    
    

    
    
    private ActionForward generarReporte(Connection con, RegistroResumenParcialHistoriaClinicaForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario, PersonaBasica paciente) 
	{
    	String nombreRptDesign = "RegistroResumenParcialHistoriaClinica.rptdesign";
    	
    	//Nombre: CC No:
    		String nombreCC=paciente.getNombrePersona()+" "+paciente.getTipoIdentificacionPersona()+" "+paciente.getNumeroIdentificacionPersona();
    	//Sexo: Masculino
    		String sexo=paciente.getSexo();
    	//F. ingreso:
    		String fechaIngreso=paciente.getFechaIngreso();
    	//N° ingreso:
    		String numeroIngreso=paciente.getConsecutivoIngreso();
    	//Vía ingreso:
    		String viaIngreso=paciente.getCodigoUltimaViaIngreso()+"";
    	//Area:
    
    	//No. Cta.:
    	//Cama:
    	//Tipo evento: 
    	
    	
    	InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		//Informacion del Cabezote
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"historiaClinica/",nombreRptDesign);

        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion() + "         " + ins.getNit());     
        v.add(ins.getDireccion());
        v.add("Tels. "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        comp.insertLabelInGridPpalOfHeader(4,0," Nombre:	 "+paciente.getNombrePersona());
        comp.insertLabelInGridPpalOfHeader(5,0," CC No:	 "+paciente.getTipoIdentificacionPersona()+""+paciente.getNumeroIdentificacionPersona());
        comp.insertLabelInGridPpalOfHeader(5,1," Edad:	 "+paciente.getEdadDetallada());
        comp.insertLabelInGridPpalOfHeader(6,0," Sexo: 	"+paciente.getSexo());
        comp.insertLabelInGridPpalOfHeader(6,1," F. Ingreso: 	"+paciente.getFechaIngreso());
        comp.insertLabelInGridPpalOfHeader(6,2," No. Ingreso: 	"+paciente.getConsecutivoIngreso());
        comp.insertLabelInGridPpalOfHeader(7,0," Via ingreso: 	"+paciente.getUltimaViaIngreso());
        comp.insertLabelInGridPpalOfHeader(8,0," Area: 	"+paciente.getArea());
        comp.insertLabelInGridPpalOfHeader(8,1," No. Cuenta: 	"+paciente.getCodigoCuenta());
        comp.insertLabelInGridPpalOfHeader(9,0," Cama: 	"+paciente.getCama());
        comp.insertLabelInGridPpalOfHeader(9,1," Tipo Evento: 	"+paciente.getTipoEvento());
        
        comp.insertLabelBodyPage(0, 0, ins.getPieHistoriaClinica(), "piehiscli");

        // modificar grid (8,3) para los sigtes datos 
        /*
         * comp.insertImageHeaderOfMasterPage1(0, 2, ins.getLogoReportes());
        //comp.insertGridHeaderOfMasterPage(0,1,1,4);
        //Vector v=new Vector();
        v.add(ins.getRazonSocial()+ "\n");
        v.add(ins.getTipoIdentificacion() + " :  " + ins.getNit() + " " + ins.getDigitoVerificacion() + "\n");     
        v.add(ins.getDireccion() + "\n");
        v.add("Tels. " + ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        comp.insertLabelInGridPpalOfHeader(2,0," Nombre:	 "+paciente.getNombrePersona());
        comp.insertLabelInGridPpalOfHeader(3,0," CC No:	 "+paciente.getTipoIdentificacionPersona()+" "+paciente.getNumeroIdentificacionPersona());
        comp.insertLabelInGridPpalOfHeader(3,1," Edad:	 "+paciente.getEdadDetallada());
        comp.insertLabelInGridPpalOfHeader(4,0," Sexo: 	"+paciente.getSexo());
        comp.insertLabelInGridPpalOfHeader(4,1," F. Ingreso: 	"+paciente.getFechaIngreso());
        comp.insertLabelInGridPpalOfHeader(4,2," No. Ingreso: 	"+paciente.getConsecutivoIngreso());
        comp.insertLabelInGridPpalOfHeader(5,0," Via ingreso: 	"+paciente.getUltimaViaIngreso());
        comp.insertLabelInGridPpalOfHeader(6,0," Area: 	"+paciente.getArea());
        comp.insertLabelInGridPpalOfHeader(6,1," No. Cuenta: 	"+paciente.getCodigoCuenta());
        comp.insertLabelInGridPpalOfHeader(7,0," Cama: 	"+paciente.getCama());
        comp.insertLabelInGridPpalOfHeader(7,1," Tipo Evento: 	"+paciente.getTipoEvento());
        */
        
        
        
       comp.obtenerComponentesDataSet("RegistroResumenParcialHistoriaClinica");
        
       String filtro=" rch.codigo="+forma.getFiltro().get("codigo_"+forma.getIndexmap())+" ";
        
       logger.info("consulta1 >>>"+comp.obtenerQueryDataSet());
       
       String newquery=comp.obtenerQueryDataSet();
      String [] splited= newquery.split("WHERE");
       newquery = " "+splited[0]+" WHERE "+filtro+" ORDER BY rch.fecha,rch.hora";
       
       logger.info("consulta modificada >><"+newquery);
       
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
       return mapping.findForward("detalle");
	
	}
    
    
    /**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @param paciente
     * @return
     */
    private ActionForward accionGuardarNuevo(
    		RegistroResumenParcialHistoriaClinicaForm forma, 
    		Connection con, 
    		ActionMapping mapping, 
    		UsuarioBasico usuario, 
    		PersonaBasica paciente,
    		HttpServletRequest request) {
		
		RegistroResumenParcialHistoriaClinica mundo=new RegistroResumenParcialHistoriaClinica();
		HashMap mapa=new HashMap();
		
		if(forma.getNota().length() > 3999)
		{
			ActionErrors errores = new ActionErrors();
			errores.add("descripcion",new ActionMessage("errors.notEspecific","El Numéro de Caracteres Maximo para la Nota es de 4000."));
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);			
			return mapping.findForward("empezar");
		}
		
		mapa.put("nota", forma.getNota());	
		mapa.put("ingreso", paciente.getCodigoIngreso());
		mapa.put("usuario", usuario.getLoginUsuario());
		mapa.put("infomedico", usuario.getEspecialidadesMedico());
		mundo.insertarNotas(con, mapa);
		
		mapa =new HashMap();
		mapa.put("ingreso", paciente.getCodigoIngreso());
		forma.setFiltro(mundo.consultarNotas(con, mapa));
		UtilidadBD.closeConnection(con);
		forma.setNota("");
		return mapping.findForward("empezar");		
	}


	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @param paciente
     * @return
     */
    private ActionForward accionVerDetalle(RegistroResumenParcialHistoriaClinicaForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente) {

    	RegistroResumenParcialHistoriaClinica mundo=new RegistroResumenParcialHistoriaClinica();
    	HashMap mapa=new HashMap();
    	HashMap mapaAsocio=new HashMap();

    	mapa.put("codigo", forma.getFiltro().get("codigo_"+forma.getIndexmap()));
    	mapa.put("ingreso", forma.getFiltro().get("ingreso_"+forma.getIndexmap()));


    	if(Integer.valueOf(String.valueOf(mundo.consultarNotas(con, mapa).get("numRegistros")))>0){
    		forma.setFiltrodet(mundo.consultarNotas(con, mapa));
    	}else if(Integer.valueOf(String.valueOf(mundo.consultarNotasAsocio(con, mapa).get("numRegistros")))>0){
    		forma.setFiltrodet(mundo.consultarNotasAsocio(con, mapa));
    	}


    	UtilidadBD.closeConnection(con);
    	return mapping.findForward("detalle");		

    }


	/**
     * @param forma
     * @param con
     * @param mapping
	 * @param paciente 
     * @return
     */
	private ActionForward accionEmpezar(RegistroResumenParcialHistoriaClinicaForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		forma.reset();
		RegistroResumenParcialHistoriaClinica mundo=new RegistroResumenParcialHistoriaClinica();
		HashMap mapa=new HashMap();
		HashMap mapaAsocio=new HashMap();

		mapa.put("ingreso", paciente.getCodigoIngreso());
		mapaAsocio.put("ingreso",paciente.getCodigoIngreso());

		if(Integer.valueOf(String.valueOf(mundo.consultarNotasAsocio(con, mapaAsocio).get("numRegistros")))>0){
			forma.setFiltro(mundo.consultarNotasAsocio(con, mapaAsocio));
		}
		if(Integer.valueOf(String.valueOf(mundo.consultarNotas(con, mapaAsocio).get("numRegistros")))>0){
			forma.setFiltro(mundo.consultarNotas(con, mapa));
		}

		UtilidadBD.closeConnection(con);
		return mapping.findForward("empezar");		
	}	
	
	/**
     * @param forma
     * @param con
     * @param mapping
	 * @param paciente 
     * @return
     */
	private ActionForward accionEmpezar1(RegistroResumenParcialHistoriaClinicaForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		
		
		RegistroResumenParcialHistoriaClinica mundo=new RegistroResumenParcialHistoriaClinica();
		HashMap mapa=new HashMap();
		HashMap mapaAsocio=new HashMap();

		mapa.put("ingreso", paciente.getCodigoIngreso());
		mapaAsocio.put("ingreso",paciente.getCodigoIngreso());

		if(Integer.valueOf(String.valueOf(mundo.consultarNotasAsocio(con, mapaAsocio).get("numRegistros")))>0){
			forma.setFiltro(mundo.consultarNotasAsocio(con, mapaAsocio));
		}
		if(Integer.valueOf(String.valueOf(mundo.consultarNotas(con, mapaAsocio).get("numRegistros")))>0){
			forma.setFiltro(mundo.consultarNotas(con, mapa));
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("empezar");		
	}
	
	
	
		
}