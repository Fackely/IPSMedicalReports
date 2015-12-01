package com.princetonsa.action.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.actionform.inventarios.ConsultaDevolucionInventarioPacienteForm;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseConsultaDevolucionInventarioPacienteDao;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.ConsultaDevolucionInventarioPaciente;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

public class ConsultaDevolucionInventarioPacienteAction extends Action
{
	Logger logger = Logger.getLogger(ConsultaDevolucionInventarioPacienteAction.class);
	
	public ActionForward execute(   ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response ) throws Exception
			{
		Connection con = null;
		try{

			if(response==null);
			if(form instanceof ConsultaDevolucionInventarioPacienteForm){

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{	
					request.setAttribute("CodigoDescripcionError","errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				ConsultaDevolucionInventarioPacienteForm forma = (ConsultaDevolucionInventarioPacienteForm)form;
				String estado = forma.getEstado();

				ActionErrors errores=new ActionErrors();

				logger.info("\n\n ESTADO CONSULTA DEVOLUCION INVENTARIO PACIENTE---->"+estado+"\n\n");

				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

				if(estado == null)
				{
					forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
					request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					if(paciente == null|| paciente.getCodigoTipoIdentificacionPersona().equals(""))
					{
						logger.warn("Paciente no válido (null)");			
						request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("paginaError");
					}
					return this.accionEmpezar(forma, con, mapping, usuario, request);
				}
				else if(estado.equals("empezarP"))
				{
					return this.accionEmpezarP(forma, con, mapping, usuario);
				}
				else if(estado.equals("empezarR"))
				{
					return this.accionEmpezarR(forma, con, mapping, usuario);
				}
				else if(estado.equals("consultarDetalleIC"))
				{
					return this.accionConsultarDetalleIC(forma, con, mapping, usuario);
				}
				else if(estado.equals("consultarDetalleAIC"))
				{
					return this.accionConsultarDetalleAIC(forma, con, mapping, usuario, request);
				}
				else if(estado.equals("imprimirReporte"))
				{    			
					return this.accionImprimirReporte(con, forma, mapping, request, usuario);    			
				}
				else if(estado.equals("consultarDevR"))
				{    			
					//MT 6772 se envia el almacen y el centro de costos a la forma para que realice la consulta
					String almacenSelect= request.getParameter("almacenSelect");
					String centroCostosSelect= request.getParameter("costoSelect");
					if(almacenSelect!=null && !almacenSelect.equals("") && !centroCostosSelect.equals("") && centroCostosSelect!=null)
						{						
							forma.setAlmacen(almacenSelect);					
							forma.setCentroCosto(centroCostosSelect);
						}
					return this.accionConsultarDevR(forma, con, mapping, usuario);    			
				}
				else if(estado.equals("consultarDetalleDR"))
				{
					return this.accionConsultarDetalleAIC(forma, con, mapping, usuario, request);
				}
				else if(estado.equals("imprimirReporteD"))
				{    			
					return this.accionImprimirReporteD(con, forma, mapping, request, usuario);    			
				}
				else if(estado.equals("imprimirReporteDR"))
				{    			
					return this.accionImprimirReporteDR(con, forma, mapping, request, usuario);    			
				}
				else if(estado.equals("imprimirReporteDD"))
				{    			
					return this.accionImprimirReporteDD(con, forma, mapping, request, usuario);    			
				}
				else if(estado.equals("imprimirReporteDDR"))
				{    			
					return this.accionImprimirReporteDDR(con, forma, mapping, request, usuario);    			
				}
				else
					/*------------------------------
					 * 		ESTADO ==> ORDENAR
				-------------------------------*/
					if (estado.equals("ordenarR"))
					{
						accionOrdenarMapa(forma);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("ConsultaDevolucionInventarioPacienteRangos");
					}
					else
						if (estado.equals("ordenar"))
						{
							accionOrdenarMapa2(forma);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("ConsultaDevolucionInventarioPaciente");
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
     * 
     * @param forma
     */    
    private void accionOrdenarMapa(ConsultaDevolucionInventarioPacienteForm forma) {
    	
    	String[] indices = (String[])forma.getDevolucionesMap("INDICES");
		int numReg = Integer.parseInt(forma.getDevolucionesMap("numRegistros")+"");
		forma.setDevolucionesMap(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getDevolucionesMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setDevolucionesMap("numRegistros",numReg+"");
		forma.setDevolucionesMap("INDICES",indices);
    }
    
    /**
     * 
     * @param forma
     */    
    private void accionOrdenarMapa2(ConsultaDevolucionInventarioPacienteForm forma) {
    	
    	String[] indices = (String[])forma.getDetalleICMap("INDICES");
		int numReg = Integer.parseInt(forma.getDetalleICMap("numRegistros")+"");
		forma.setDevolucionesMap(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getDetalleICMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setDetalleICMap("numRegistros",numReg+"");
		forma.setDetalleICMap("INDICES",indices);
    }
	
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionEmpezar(ConsultaDevolucionInventarioPacienteForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
		forma.setCodigoPaciente(paciente.getCodigoPersona());
		forma.setListadoIngresosMap(ConsultaDevolucionInventarioPaciente.consultaListadoIngresos(con, forma.getCodigoPaciente()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ConsultaDevolucionInventarioPaciente");
	}
	
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionEmpezarR(ConsultaDevolucionInventarioPacienteForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		forma.setCentroCostoMap(ConsultaDevolucionInventarioPaciente.consultaCentroCosto(con));
		forma.setEstadosDevolucionMap(ConsultaDevolucionInventarioPaciente.consultaEstadosDevolucion(con));
		forma.setMotivosDevolucionMap(ConsultaDevolucionInventarioPaciente.consultaMotivosDevolucion(con));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ConsultaDevolucionInventarioPacienteRangos");
	}
	
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionEmpezarP(ConsultaDevolucionInventarioPacienteForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ConsultaDevolucionInventarioPacientePrincipal");
	}
	
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionConsultarDetalleIC(ConsultaDevolucionInventarioPacienteForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario)
	{
		logger.info("NUMERO DE LA CUENTA>>>>>>>>>>>"+forma.getListadoIngresosMap().get("idcue_"+forma.getIndexMap()));
		logger.info("INDX MAAAPPPPPPPPPP>>>>>>>>>>>"+forma.getIndexMap());
		forma.setDetalleICMap(ConsultaDevolucionInventarioPaciente.consultaDetalleIC(con, forma.getListadoIngresosMap().get("idcue_"+forma.getIndexMap()).toString()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ConsultaDevolucionInventarioPaciente");
	}
	
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionConsultarDetalleAIC(ConsultaDevolucionInventarioPacienteForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException
	{
		if(forma.getEstado().equals("consultarDetalleAIC")){
			forma.setDetalleAICMap(ConsultaDevolucionInventarioPaciente.consultaDetalleAIC(con, forma.getDetalleICMap().get("numdev_"+forma.getIndexMap()).toString(), forma.getDetalleICMap().get("campod_"+forma.getIndexMap()).toString()));
			UtilidadBD.closeConnection(con);
			return mapping.findForward("ConsultaDevolucionInventarioPaciente");
		}
		else{
			if(forma.getEstado().equals("consultarDetalleDR")){
				forma.setCodigoPaciente(Integer.parseInt(forma.getDevolucionesMap().get("cpdv_"+forma.getIndexMap()).toString()));
				PersonaBasica persona= (PersonaBasica) request.getSession().getAttribute("pacienteActivo");
		        persona.setCodigoPersona(forma.getCodigoPaciente());
		        persona.cargar(con, forma.getCodigoPaciente());
		        persona.cargarPaciente(con, forma.getCodigoPaciente(), usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
				forma.setDetalleAICMap(ConsultaDevolucionInventarioPaciente.consultaDetalleAIC(con, forma.getDevolucionesMap().get("numdev_"+forma.getIndexMap()).toString(), forma.getDevolucionesMap().get("campod_"+forma.getIndexMap()).toString()));
			}
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ConsultaDevolucionInventarioPacienteRangos");
	}
	
	/**
     * 
     * @param con
     * @param forma
     * @param mapping
     * @param request
     * @param usuario
     * @return
     */
    private ActionForward accionImprimirReporte(Connection con, ConsultaDevolucionInventarioPacienteForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) {
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("ConsultaDevolucionInventarioPacientes");
    }
    
    /**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionConsultarDevR(ConsultaDevolucionInventarioPacienteForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//MT 6772 
		forma.setDevolucionesMap(ConsultaDevolucionInventarioPaciente.consultaDevR(con, forma.getNumeroDevolucion(), forma.getCentroAtencion(), forma.getAlmacen(), forma.getCentroCosto(), UtilidadFecha.conversionFormatoFechaABD(forma.getFechaini()), UtilidadFecha.conversionFormatoFechaABD(forma.getFechafin()), forma.getEstadoDevolucion(), forma.getTipoDevolucion(), forma.getMotivoDevolucion(), forma.getUsuarioDevuelve(), forma.getUsuarioRecibe().trim()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ConsultaDevolucionInventarioPacienteRangos");
	}
	
	/**
     * 
     * @param con
     * @param forma
     * @param mapping
     * @param request
     * @param usuario
     * @return
     */
    private ActionForward accionImprimirReporteD(Connection con, ConsultaDevolucionInventarioPacienteForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) {
    	String newQuery = SqlBaseConsultaDevolucionInventarioPacienteDao.consultaStrIC;
    	newQuery+="ORDER BY \"numdev\"";
    	String nombreRptDesign = "ConsultaDevolucionInventarioPaciente.rptdesign";
    	ConsultaDevolucionInventarioPaciente cuc=new ConsultaDevolucionInventarioPaciente();

		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//Informacion del Cabezote
        DesignEngineApi comp;
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,5);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());
        v.add("DEVOLUCIONES INVENTARIOS PACIENTE");
        v.add(ins.getDireccion());
        v.add("Tels. "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        
        
        comp.obtenerComponentesDataSet("ConsultaDevolucionInventarioPaciente");
        comp.modificarQueryDataSet(newQuery);
         
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
		return mapping.findForward("ConsultaDevolucionInventarioPaciente");
    }
    
    /**
     * 
     * @param con
     * @param forma
     * @param mapping
     * @param request
     * @param usuario
     * @return
     */
    private ActionForward accionImprimirReporteDD(Connection con, ConsultaDevolucionInventarioPacienteForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) {
    	String newQuery = SqlBaseConsultaDevolucionInventarioPacienteDao.consultaStrAsIC;
    	String nombreRptDesign = "ConsultaDevolucionInventarioPaciente2.rptdesign";
    	ConsultaDevolucionInventarioPaciente cuc=new ConsultaDevolucionInventarioPaciente();

		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//Informacion del Cabezote
        DesignEngineApi comp;
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,5);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());
        v.add("DEVOLUCIONES INVENTARIOS PACIENTE");
        String fechaHoraDevol=UtilidadFecha.conversionFormatoFechaAAp(forma.getDetalleAICMap().get("fecha_0")+"")+"  "+forma.getDetalleAICMap().get("hora_0")+"";
        v.add("FECHA DEVOLUCIÓN: "+fechaHoraDevol);
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        
        
        comp.obtenerComponentesDataSet("ConsultaDevolucionInventarioPaciente");
        comp.modificarQueryDataSet(newQuery);
         
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
		return mapping.findForward("ConsultaDevolucionInventarioPaciente");
    }
    
    /**
     * 
     * @param con
     * @param forma
     * @param mapping
     * @param request
     * @param usuario
     * @return
     */
    private ActionForward accionImprimirReporteDR(Connection con, ConsultaDevolucionInventarioPacienteForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) {
    	String newQuery = SqlBaseConsultaDevolucionInventarioPacienteDao.cadena;
    	String nombreRptDesign = "ConsultaDevolucionInventarioPaciente.rptdesign";
    	ConsultaDevolucionInventarioPaciente cuc=new ConsultaDevolucionInventarioPaciente();

		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//Informacion del Cabezote
        DesignEngineApi comp;
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,5);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());
        v.add("DEVOLUCIONES INVENTARIOS PACIENTE");
        v.add(ins.getDireccion());
        v.add("Tels. "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        logger.info("consulta -> "+newQuery);
        
        comp.obtenerComponentesDataSet("ConsultaDevolucionInventarioPaciente");
        comp.modificarQueryDataSet(newQuery);
         
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
		return mapping.findForward("ConsultaDevolucionInventarioPacienteRangos");
    }
    
    /**
     * 
     * @param con
     * @param forma
     * @param mapping
     * @param request
     * @param usuario
     * @return
     */
    private ActionForward accionImprimirReporteDDR(Connection con, ConsultaDevolucionInventarioPacienteForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) {
    	String newQuery = SqlBaseConsultaDevolucionInventarioPacienteDao.consultaStrAsIC;
    	String nombreRptDesign = "ConsultaDevolucionInventarioPaciente2.rptdesign";
    	ConsultaDevolucionInventarioPaciente cuc=new ConsultaDevolucionInventarioPaciente();

		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//Informacion del Cabezote
        DesignEngineApi comp;
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,5);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());
        v.add("DEVOLUCIONES INVENTARIOS PACIENTE");
        String fechaHoraDevol=UtilidadFecha.conversionFormatoFechaAAp(forma.getDetalleAICMap().get("fecha_0")+"")+"  "+forma.getDetalleAICMap().get("hora_0")+"";
        v.add("FECHA DEVOLUCIÓN: "+fechaHoraDevol);
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        
        
        comp.obtenerComponentesDataSet("ConsultaDevolucionInventarioPaciente");
        comp.modificarQueryDataSet(newQuery);
         
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
		return mapping.findForward("ConsultaDevolucionInventarioPacienteRangos");
    }
}