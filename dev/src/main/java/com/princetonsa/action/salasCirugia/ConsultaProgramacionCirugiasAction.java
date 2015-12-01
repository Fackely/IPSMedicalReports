package com.princetonsa.action.salasCirugia;

import java.sql.Connection;
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
import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.Administracion.UtilidadesAdministracion;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.salas.UtilidadesSalas;

import com.princetonsa.actionform.salasCirugia.ConsultaProgramacionCirugiasForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.salasCirugia.ConsultaProgramacionCirugias;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * Clase para el manejo de la consulta de la programacion de cirugias
 * Date: 2008-06-03
 * @author garias@princetonsa.com
 */
public class ConsultaProgramacionCirugiasAction extends Action 
{
	
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(ConsultaProgramacionCirugiasAction.class);
	
	/**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{
    	Connection con = null;
    	try{
    		if(response==null);
    		if(form instanceof ConsultaProgramacionCirugiasForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}
    			ConsultaProgramacionCirugiasForm forma = (ConsultaProgramacionCirugiasForm)form;
    			String estado = forma.getEstado();

    			ActionErrors errores = new ActionErrors();

    			logger.info("\n\n\n ESTADO (CONSULTA PROGRAMACIÓN CIRUGIAS) ---> "+estado+"\n\n");

    			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

    			//if(paciente==null)
    			//	errores.add("Paciente", new ActionMessage("errors.required","Paciente"));

    			if(estado == null)
    			{
    				forma.reset();
    				logger.warn("Estado No Valido Dentro Del Flujo de la Consulta de Programación de Cirugías (null)");
    				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else 
    				/*------------------------------
    				 * 		ESTADO > consultaXPaciente
    				 *-------------------------------*/
    				if(estado.equals("consultaXPaciente"))
    				{ 
    					return accionConsultaXPaciente(con, mapping, request, errores, forma, usuario, paciente);
    				}
    				else 
    					/*------------------------------
    					 * 		ESTADO > ordenarListadoXPaciente
    					 *-------------------------------*/
    					if(estado.equals("ordenarListadoXPaciente"))
    					{
    						return accionOrdenarListadoXPaciente(con, mapping, request, errores, forma, usuario, paciente);
    					}
    			/*------------------------------
    			 * 		ESTADO > consultarServiciosXPeticion
    			 *-------------------------------*/
    			if(estado.equals("consultarServiciosXPeticion"))
    			{
    				return accionConsultarServiciosXPeticion(con, mapping, request, errores, forma, usuario, paciente);
    			}
    			/*------------------------------
    			 * 		ESTADO > consultarDetallePeticion
    			 *-------------------------------*/
    			if(estado.equals("consultarDetallePeticion"))
    			{
    				return accionConsultarDetallePeticion(con, mapping, request, errores, forma, usuario, paciente);
    			}
    			/*------------------------------
    			 * 		ESTADO > consultaXRango
    			 *-------------------------------*/
    			if(estado.equals("busquedaXRango"))
    			{
    				return accionBusquedaXRango(con, mapping, request, errores, forma, usuario, paciente);
    			}
    			/*------------------------------
    			 * 		ESTADO > cambiarCentroAtencion
    			 *-------------------------------*/
    			if(estado.equals("cambiarCentroAtencion"))
    			{
    				return accionCambiarCentroAtencion(con, mapping, request, errores, forma, usuario, paciente);
    			}
    			/*------------------------------
    			 * 		ESTADO > consultaXRango
    			 *-------------------------------*/
    			if(estado.equals("consultaXRango"))
    			{
    				return accionConsultaXRango(con, mapping, request, errores, forma, usuario, paciente);
    			}
    			/*------------------------------
    			 * 		ESTADO > imprimirBoletaProgramacion
    			 *-------------------------------*/
    			if(estado.equals("imprimirBoletaProgramacion"))
    			{
    				return accionImprimirBoletaProgramacion(con, mapping, request, errores, forma, usuario, paciente);
    			}
    			/*------------------------------
    			 * 		ESTADO > imprimirListadoProgramacion
    			 *-------------------------------*/
    			if(estado.equals("imprimirListadoProgramacion"))
    			{
    				return accionImprimirListadoProgramacion(con, mapping, request, errores, forma, usuario, paciente);
    			}
    			/*------------------------------
    			 * 		ESTADO > consultarDetallePedidos
    			 *-------------------------------*/
    			if(estado.equals("consultarDetallePedidos"))
    			{
    				return accionConsultarDetallePedidos(con, mapping, request, errores, forma, usuario, paciente);
    			}
    			/*------------------------------
    			 * 		ESTADO > consultarDetalleArticulosPedido
    			 *-------------------------------*/
    			if(estado.equals("consultarDetalleArticulosPedido"))
    			{
    				return accionConsultarDetalleArticulosPedido(con, mapping, request, errores, forma, usuario, paciente);
    			}
    			UtilidadBD.closeConnection(con);
    		}
    	}catch (Exception e) {
    		Log4JManager.error(e);
    	}
    	finally{
    		UtilidadBD.closeConnection(con);
    	}
    	return null;
     }
    
    private ActionForward accionConsultarDetalleArticulosPedido(Connection con, ActionMapping mapping, HttpServletRequest request, ActionErrors errores, ConsultaProgramacionCirugiasForm forma, UsuarioBasico usuario, PersonaBasica paciente) {
    	ConsultaProgramacionCirugias mundo = new ConsultaProgramacionCirugias();
    	logger.info("pedido -- "+forma.getPedido());
    	mundo.setPedido(Integer.parseInt(forma.getPedido()));
    	forma.setArticulosPedidoMap(mundo.consultarArticulosPedido(con, mundo));
    	UtilidadBD.closeConnection(con);
    	return mapping.findForward("popupDetalleArticulosPedido");
	}

	private ActionForward accionConsultarDetallePedidos(Connection con, ActionMapping mapping, HttpServletRequest request, ActionErrors errores, ConsultaProgramacionCirugiasForm forma, UsuarioBasico usuario, PersonaBasica paciente) {
		ConsultaProgramacionCirugias mundo = new ConsultaProgramacionCirugias();
		mundo.setPeticion(Integer.parseInt(forma.getPeticionqx()));
		forma.setPedidosMap(mundo.consultarPedidos(con, mundo));
    	UtilidadBD.closeConnection(con);
    	return mapping.findForward("popupDetallePedidos");
	}

	private ActionForward accionImprimirListadoProgramacion(Connection con, ActionMapping mapping, HttpServletRequest request, ActionErrors errores, ConsultaProgramacionCirugiasForm forma, UsuarioBasico usuario, PersonaBasica paciente) {
    	String nombreRptDesign = "listadoPeticionesProgramacion.rptdesign";
    	
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//Flujo solo para imprimir desde el detalle ingresando desde el listado X paciente
		if (forma.getPeticionesCirugiasMap().containsKey("paciente")){
			forma.setPeticionesCirugiasMap("paciente_"+forma.getPosMap(), forma.getPeticionesCirugiasMap("paciente"));
			forma.setPeticionesCirugiasMap("id_paciente_"+forma.getPosMap(), forma.getPeticionesCirugiasMap("id_paciente"));
		}
		
		//Informacion del Cabezote
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"salasCirugia/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
        
        Vector v = new Vector();
        v.add(institucion.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
        v.add(institucion.getDireccion());
        v.add("Tels. "+institucion.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        comp.insertLabelInGridPpalOfHeader(2,1, "BOLETA DE PROGRAMACIÓN CIRUGIA");
        
		comp.insertLabelInGridPpalOfHeader(0,2, "Usuario Responsable: "+usuario.getLoginUsuario()+"\nDel "+forma.getFiltrosMap("fechaInicial")+" al "+forma.getFiltrosMap("fechaFinal"));
		
		String where="pq.institucion="+usuario.getCodigoInstitucionInt()+" "+
			"AND pq.programable='"+ConstantesBD.acronimoSi+"' " +
			"AND pq.estado_peticion NOT IN ("+ConstantesBD.codigoEstadoPeticionPendiente+", "+ConstantesBD.codigoEstadoPeticionAnulada+") " +
			"AND ps.numero_servicio=1 ";
		
		
		
		//Filtro por centro de atención
		if (!forma.getFiltrosMap().get("centroAtencion").equals("")){
			where += " AND pq.centro_atencion="+forma.getFiltrosMap().get("centroAtencion");
		}
		 
		// Filtro por sala
		if (!forma.getFiltrosMap().get("sala").equals("")){
			where += " AND psq.sala="+forma.getFiltrosMap().get("sala");
		}
		
		// Filtro por cirujano
		if (!forma.getFiltrosMap().get("cirujano").equals("")){
			where += " AND pq.solicitante="+forma.getFiltrosMap().get("cirujano");
		}
		
		// Filtro por anestesiologo
		if (!forma.getFiltrosMap().get("anestesiologo").equals("")){
			where += " AND getProfesionalPeticionQx(pq.codigo, "+ConstantesBD.codigoTipoParticipanteAnestesiologo+")="+forma.getFiltrosMap().get("anestesiologo");
		}
		
		// Filtro por tipo de anestesia
		if (!forma.getFiltrosMap().get("tipoAnestesia").equals("")){
			where += " AND pq.tipo_anestesia="+forma.getFiltrosMap().get("tipoAnestesia");
		}
		
		// Filtro Cancelacion
		
		// Filtro por el estado de la peticion
		if (!forma.getFiltrosMap().get("estadoPeticion").equals("")){
			where += " AND pq.estado_peticion="+forma.getFiltrosMap().get("estadoPeticion");
		}
		
		// Filtro por el usuario que programa
		if (!forma.getFiltrosMap().get("usuario").equals("")){
			where += " AND psq.usuario='"+forma.getFiltrosMap().get("usuario")+"' ";
		}
		
		// Filtro por la fecha
		if(!forma.getFiltrosMap().get("horaInicial").equals("")&&!forma.getFiltrosMap().get("horaFinal").equals(""))
			where += " AND (" +
					"psq.fecha_cirugia || '-' || psq.hora_inicio >= '"+UtilidadFecha.conversionFormatoFechaABD(forma.getFiltrosMap().get("fechaInicial").toString())+"-"+forma.getFiltrosMap().get("horaInicial")+"' AND " +
					"psq.fecha_cirugia || '-' || psq.hora_inicio <= '"+UtilidadFecha.conversionFormatoFechaABD(forma.getFiltrosMap().get("fechaFinal").toString())+"-"+forma.getFiltrosMap().get("horaFinal")+"' ) ";
		else
			where += " AND (psq.fecha_cirugia BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(forma.getFiltrosMap().get("fechaInicial").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(forma.getFiltrosMap().get("fechaFinal").toString())+"') ";
		
		
		
		
       comp.obtenerComponentesDataSet("dataset");
       String newquery=comp.obtenerQueryDataSet().replaceAll("1=1", where);
       

       logger.info("Query >>>"+newquery);
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
       	return mapping.findForward(forma.getForward());

	}

	private ActionForward accionImprimirBoletaProgramacion(Connection con, ActionMapping mapping, HttpServletRequest request, ActionErrors errores, ConsultaProgramacionCirugiasForm forma, UsuarioBasico usuario, PersonaBasica paciente) {
    	
    	String nombreRptDesign = "boletaProgramacionCirugia.rptdesign";
    	
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//Flujo solo para imprimir desde el detalle ingresando desde el listado X paciente
		if (forma.getPeticionesCirugiasMap().containsKey("paciente")){
			forma.setPeticionesCirugiasMap("paciente_"+forma.getPosMap(), forma.getPeticionesCirugiasMap("paciente"));
			forma.setPeticionesCirugiasMap("id_paciente_"+forma.getPosMap(), forma.getPeticionesCirugiasMap("id_paciente"));
		}
		
		//Informacion del Cabezote
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"salasCirugia/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());     
        v.add(ins.getDireccion());
        v.add("Tels. "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
		comp.insertLabelInGridPpalOfHeader(2,0, "BOLETA DE PROGRAMACIÓN CIRUGIA");
		comp.insertLabelInGridPpalOfHeader(4,0, "Usuario Responsable: "+usuario.getLoginUsuario());
		comp.insertLabelInGridPpalOfHeader(5,0, "Paciente: "+forma.getPeticionesCirugiasMap("paciente_"+forma.getPosMap()));
		comp.insertLabelInGridPpalOfHeader(5,1, "Id: "+forma.getPeticionesCirugiasMap("id_paciente_"+forma.getPosMap()));
		
		logger.info("Ingreso -----> "+forma.getIngresoMap("ingreso_0"));
		logger.info("Cuenta  -----> "+forma.getIngresoMap("cuenta_0"));
		
		if (forma.getIngresoMap().containsKey("ingreso_0"))
			comp.insertLabelInGridPpalOfHeader(5,2, "Ingreso: "+forma.getIngresoMap("ingreso_0"));
		else
			comp.insertLabelInGridPpalOfHeader(5,2, "Ingreso: ");
		if (forma.getIngresoMap().containsKey("cuenta_0"))
			comp.insertLabelInGridPpalOfHeader(5,3, "Cuenta: "+forma.getIngresoMap("cuenta_0"));
		else
			comp.insertLabelInGridPpalOfHeader(5,3, "Cuenta: ");
		
		/*if (!forma.getIngresoMap("ingreso_0").equals(""))
			comp.insertLabelInGridPpalOfHeader(5,2, "Ingreso: "+forma.getPeticionesCirugiasMap("ingreso_0"));
		else
			comp.insertLabelInGridPpalOfHeader(5,2, "Ingreso: ");
		if (!forma.getIngresoMap("cuenta_0").equals(""))
			comp.insertLabelInGridPpalOfHeader(5,3, "Cuenta: "+forma.getPeticionesCirugiasMap("cuenta_0"));
		else
			comp.insertLabelInGridPpalOfHeader(5,3, "Cuenta: ");*/
		
		comp.insertLabelInGridPpalOfHeader(6,0, "Centro Atención: "+forma.getPeticionesCirugiasMap("centro_atencion_"+forma.getPosMap()));
		comp.insertLabelInGridPpalOfHeader(6,1, "Sala: "+forma.getPeticionesCirugiasMap("sala_"+forma.getPosMap()));
		comp.insertLabelInGridPpalOfHeader(6,2, "Fecha y Hora Programada: "+forma.getPeticionesCirugiasMap("fecha_programacion_"+forma.getPosMap())+" "+forma.getPeticionesCirugiasMap("hora_inicio_"+forma.getPosMap()) );
  
       comp.obtenerComponentesDataSet("boletaProgramacionCirugia");
       
       String newquery=comp.obtenerQueryDataSet().replaceAll("1=1", "pq.institucion="+usuario.getCodigoInstitucion()+" AND pq.codigo="+forma.getPeticionesCirugiasMap("peticion_"+forma.getPosMap()));
       comp.modificarQueryDataSet(newquery);
       
       String newquery1=comp.obtenerQueryDataSet().replaceAll("666", ValoresPorDefecto.getAsocioCirujano(usuario.getCodigoInstitucionInt()));
       comp.modificarQueryDataSet(newquery1);
       
       logger.info("Query >>>"+newquery1);
      
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
       	return mapping.findForward("popupDetallePeticion");
	}

	private ActionForward accionConsultaXRango(Connection con, ActionMapping mapping, HttpServletRequest request, ActionErrors errores, ConsultaProgramacionCirugiasForm forma, UsuarioBasico usuario, PersonaBasica paciente) {
    	Utilidades.imprimirMapa(forma.getFiltrosMap());
    	errores = validarParametrosBusqueda(con, mapping, request, errores,forma);
    	if (!errores.isEmpty()){
    		saveErrors(request,errores);
        	UtilidadBD.closeConnection(con);
    		return mapping.findForward("busquedaXRango");
    	}
    	ConsultaProgramacionCirugias mundo = new ConsultaProgramacionCirugias();
		mundo.setInstitucion(usuario.getCodigoInstitucion());
		mundo.setFiltrosMap(forma.getFiltrosMap());
    	forma.setPeticionesCirugiasMap(mundo.consultarXRango(con, mundo));
    	forma.setForward("listadoXRango");
    	
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoXRango");
	}
    
    private ActionErrors validarParametrosBusqueda(Connection con, ActionMapping mapping, HttpServletRequest request, ActionErrors errores, ConsultaProgramacionCirugiasForm forma){
    	
    	
    	//  Validacion rango fechas
		if(!forma.getFiltrosMap("fechaInicial").equals("")&&!forma.getFiltrosMap("fechaFinal").equals(""))
		{
			if(UtilidadFecha.validarFecha(forma.getFiltrosMap("fechaInicial").toString())&&UtilidadFecha.validarFecha(forma.getFiltrosMap("fechaFinal").toString()))
			{
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFiltrosMap("fechaInicial").toString(),UtilidadFecha.getFechaActual()))
					errores.add("Fecha inicial mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","inicial","actual"));
				/*if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFiltrosMap("fechaFinal").toString(),UtilidadFecha.getFechaActual()))
					errores.add("Fecha final mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","final","actual"));*/
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getFiltrosMap("fechaFinal").toString(),forma.getFiltrosMap("fechaInicial").toString()))
					errores.add("Fecha inicial mayor a la fecha final",new ActionMessage("errors.fechaAnteriorIgualActual","final","inicial"));
				else if(UtilidadFecha.numeroMesesEntreFechas(forma.getFiltrosMap("fechaInicial").toString(),forma.getFiltrosMap("fechaFinal").toString(),true)>3)
					errores.add("Rango de fechas > a 3 meses",new ActionMessage("errors.rangoMayorTresMeses","PARA CONSULTA DE CUENTAS"));
			}
			else
			{
				if(!UtilidadFecha.validarFecha(forma.getFiltrosMap("fechaInicial").toString()))
					errores.add("Fecha Inicial inválida",new ActionMessage("errors.formatoFechaInvalido","inicial"));
				if(!UtilidadFecha.validarFecha(forma.getFiltrosMap("fechaFinal").toString()))
					errores.add("Fecha Final inválida",new ActionMessage("errors.formatoFechaInvalido","final"));
			}
		}
		else
		{
			if(forma.getFiltrosMap("fechaInicial").equals(""))
				errores.add("fecha Inicial requerida",new ActionMessage("errors.required","La Fecha Inicial"));
			else if(!UtilidadFecha.validarFecha(forma.getFiltrosMap("fechaInicial").toString()))
				errores.add("Fecha Inicial inválida",new ActionMessage("errors.formatoFechaInvalido","inicial"));
			else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFiltrosMap("fechaInicial").toString(),UtilidadFecha.getFechaActual()))
				errores.add("Fecha inicial mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","inicial","actual"));
			
			if(forma.getFiltrosMap("fechaFinal").equals(""))
				errores.add("fecha Final requerida",new ActionMessage("errors.required","La Fecha Final"));
			else if(!UtilidadFecha.validarFecha(forma.getFiltrosMap("fechaFinal").toString()))
				errores.add("Fecha Final inválida",new ActionMessage("errors.formatoFechaInvalido","final"));
			else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFiltrosMap("fechaFinal").toString(),UtilidadFecha.getFechaActual()))
				errores.add("Fecha final mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","final","actual"));
		}
		
		
		// validacion hora inicial
		if(!forma.getFiltrosMap("horaInicial").equals(""))
			if (!UtilidadFecha.validacionHora(forma.getFiltrosMap("horaInicial").toString()).puedoSeguir)
				errores.add("Hora invalida",new ActionMessage("errors.formatoHoraInvalido",forma.getFiltrosMap("horaInicial")));
		
		// validacion hora final
		if(!forma.getFiltrosMap("horaFinal").equals(""))
			if (!UtilidadFecha.validacionHora(forma.getFiltrosMap("horaFinal").toString()).puedoSeguir)
				errores.add("Hora invalida",new ActionMessage("errors.formatoHoraInvalido",forma.getFiltrosMap("horaFinal")));
			
		if (forma.getFiltrosMap("horaInicial").equals("") && !forma.getFiltrosMap("horaFinal").equals(""))
			errores.add("Hora final requerida",new ActionMessage("errors.required","Hora Final"));
		
		if (!forma.getFiltrosMap("horaInicial").equals("") && forma.getFiltrosMap("horaFinal").equals(""))
			errores.add("Hora inicial requerida",new ActionMessage("errors.required","Hora Inicial"));

		return errores;
    }

	/**
     * 
     * @param con
     * @param mapping
     * @param request
     * @param errores
     * @param forma
     * @param usuario
     * @param paciente
     * @return
     */
    private ActionForward accionCambiarCentroAtencion(Connection con, ActionMapping mapping, HttpServletRequest request, ActionErrors errores, ConsultaProgramacionCirugiasForm forma, UsuarioBasico usuario, PersonaBasica paciente) {
    	if (!forma.getFiltrosMap("centroAtencion").equals(""))	
    		forma.setSalas(UtilidadesSalas.obtenerSalas(con, usuario.getCodigoInstitucionInt(), Integer.parseInt(forma.getFiltrosMap("centroAtencion").toString()), "t", ""));
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("busquedaXRango");
	}

    /**
     * 
     * @param con
     * @param mapping
     * @param request
     * @param errores
     * @param forma
     * @param usuario
     * @param paciente
     * @return
     */
	private ActionForward accionBusquedaXRango(Connection con, ActionMapping mapping, HttpServletRequest request, ActionErrors errores, ConsultaProgramacionCirugiasForm forma, UsuarioBasico usuario, PersonaBasica paciente) {
    	forma.reset();
    	
    	forma.setFiltrosMap("centroAtencion",usuario.getCodigoCentroAtencion());
    	forma.setFiltrosMap("cancelacion", "");
		forma.setCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(con, usuario.getCodigoInstitucionInt(),""));
		forma.setSalas(UtilidadesSalas.obtenerSalas(con, usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(), "t", ""));
		forma.setProfesionales(UtilidadesAdministracion.obtenerProfesionales(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoNuncaValido, false, true, ConstantesBD.codigoNuncaValido));
		forma.setTiposAnestesia(Utilidades.obtenerTiposAnestesia(con, ""));
		forma.setUsuariosMap(Utilidades.obtenerUsuarios(con, usuario.getCodigoInstitucionInt(),false));
		
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("busquedaXRango");
	}

	/**
	 * 
	 * @param con
	 * @param mapping
	 * @param request
	 * @param errores
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	private ActionForward accionConsultarDetallePeticion(Connection con, ActionMapping mapping, HttpServletRequest request, ActionErrors errores, ConsultaProgramacionCirugiasForm forma, UsuarioBasico usuario, PersonaBasica paciente) {
    	ConsultaProgramacionCirugias mundo = new ConsultaProgramacionCirugias();
    	mundo.setPeticion(Integer.parseInt(forma.getPeticionesCirugiasMap("peticion_"+forma.getPosMap()).toString()));
    	
    	//Consultar servicios asociados a la petición
		forma.setServiciosMap(mundo.consultarServiciosXPeticion(con, mundo));
		
		//ConsultarProfesionales asociados a la petición
		forma.setProfesionalesMap(mundo.consultarProfesionalesXPeticion(con, mundo));
		
		//Si la solicitud es valida se consulta la información de ingreso y materiales especiales
		if (Integer.parseInt(forma.getPeticionesCirugiasMap("solicitud_"+forma.getPosMap()).toString()) != ConstantesBD.codigoNuncaValido){
			mundo.setSolicitud(Integer.parseInt(forma.getPeticionesCirugiasMap("solicitud_"+forma.getPosMap()).toString()));
			forma.setIngresoMap(mundo.consultarIngreso(con, mundo));
			forma.setMaterialesEspecialesMap(mundo.consultarMaterialesEspeciales(con, mundo));
		}
		
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("popupDetallePeticion");
	}

	/**
	 * 
	 * @param con
	 * @param mapping
	 * @param request
	 * @param errores
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	private ActionForward accionConsultarServiciosXPeticion(Connection con, ActionMapping mapping, HttpServletRequest request, ActionErrors errores, ConsultaProgramacionCirugiasForm forma, UsuarioBasico usuario, PersonaBasica paciente) {
    	ConsultaProgramacionCirugias mundo = new ConsultaProgramacionCirugias();
		mundo.setPeticion(Integer.parseInt(forma.getPeticionesCirugiasMap("peticion_"+forma.getPosMap()).toString()));
		forma.setServiciosMap(mundo.consultarServiciosXPeticion(con, mundo));
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("popupDetalleServicios");
	}


	/**
	 * 
	 * @param con
	 * @param mapping
	 * @param request
	 * @param errores
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	private ActionForward accionOrdenarListadoXPaciente(Connection con, ActionMapping mapping, HttpServletRequest request, ActionErrors errores, ConsultaProgramacionCirugiasForm forma, UsuarioBasico usuario, PersonaBasica paciente) {
    	String[] indices = {"estado_peticion_","centro_atencion_","peticion_","fecha_programacion_","hora_inicio_","hora_final_","sala_","servicio_","cirujano_","num_servicios_",""};
		int numReg = Integer.parseInt(forma.getPeticionesCirugiasMap("numRegistros")+"");
		forma.setPeticionesCirugiasMap(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getPeticionesCirugiasMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setPeticionesCirugiasMap("numRegistros",numReg+"");
		forma.setPeticionesCirugiasMap("INDICES_MAPA",indices);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoXPaciente");
	}

	/**
	 * 
	 * @param con
	 * @param mapping
	 * @param request
	 * @param errores
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	private ActionForward accionConsultaXPaciente(Connection con, ActionMapping mapping, HttpServletRequest request, ActionErrors errores, ConsultaProgramacionCirugiasForm forma, UsuarioBasico usuario, PersonaBasica paciente) {
		forma.reset();
		//Validamos que el paciente se encuentre cargado
		if (paciente.getCodigoPersona()==ConstantesBD.codigoNuncaValido){
			errores.add("Validación Paciente Cargado", new ActionMessage("errors.usuario.noCargado"));
			saveErrors(request,errores);
			forma.setEstado("error");
		}
		else 
		{
			ConsultaProgramacionCirugias mundo = new ConsultaProgramacionCirugias();
			mundo.setInstitucion(usuario.getCodigoInstitucion());
			mundo.setPaciente(paciente.getCodigoPersona());
			forma.setPeticionesCirugiasMap(mundo.consultarXPaciente(con, mundo));
			forma.setPeticionesCirugiasMap("paciente", paciente.getNombrePersona());
			forma.setPeticionesCirugiasMap("id_paciente", paciente .getTipoIdentificacionPersona(false)+" "+paciente.getNumeroIdentificacionPersona());
		}
		forma.setForward("listadoXPaciente");
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoXPaciente");
	}
}