package com.princetonsa.action.inventarios;

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
import org.axioma.util.log.Log4JManager;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.actionform.inventarios.ConsultaDevolucionPedidosForm;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseConsultaDevolucionPedidosDao;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.ConsultaDevolucionPedidos;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.ICentroAtencionMundo;
import com.servinte.axioma.orm.CentroAtencion;

public class ConsultaDevolucionPedidosAction extends Action
{
	Logger logger = Logger.getLogger(ConsultaDevolucionPedidosAction.class);
	
	public ActionForward execute(   ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response ) throws Exception
			{

		Connection con = null;
		try{
			if(response==null);
			if(form instanceof ConsultaDevolucionPedidosForm){

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{	
					request.setAttribute("CodigoDescripcionError","errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				ConsultaDevolucionPedidosForm forma = (ConsultaDevolucionPedidosForm)form;
				String estado = forma.getEstado();

				ActionErrors errores=new ActionErrors();

				logger.info("\n\n ESTADO CONSULTA DEVOLUCION PEDIDOS---->"+estado+"\n\n");

				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				if(estado == null)
				{
					forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
					request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(forma, con, mapping, usuario);
				}
				else if(estado.equals("consultar"))
				{
					return this.accionConsultar(forma, con, mapping, usuario);
				}
				else if(estado.equals("consultarDetalle"))
				{
					return this.accionConsultarDetalle(forma, con, mapping, usuario);
				}
				else if(estado.equals("imprimirReporte"))
				{    			
					return this.accionImprimirReporte(con, forma, mapping, request, usuario);    			
				}
				else if(estado.equals("imprimirListado"))
				{    			
					return this.accionImprimirListado(con, forma, mapping, request, usuario);    			
				}
				else
					/*------------------------------
					 * 		ESTADO ==> ORDENAR
				-------------------------------*/
					if (estado.equals("ordenar"))
					{
						accionOrdenarMapa(forma);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("ConsultaDevolucionPedidos");
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
    private void accionOrdenarMapa(ConsultaDevolucionPedidosForm forma) {
    	
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
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionEmpezar(ConsultaDevolucionPedidosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		forma.setCentroCostoMap(ConsultaDevolucionPedidos.consultaCentroCosto(con, Integer.parseInt(forma.getCentroAtencion())));
		forma.setEstadosDevolucionMap(ConsultaDevolucionPedidos.consultaEstadosDevolucion(con));
		forma.setMotivosDevolucionMap(ConsultaDevolucionPedidos.consultaMotivosDevolucion(con));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ConsultaDevolucionPedidos");
	}
	
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionConsultar(ConsultaDevolucionPedidosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario)
	{
		forma.setDevolucionesMap(ConsultaDevolucionPedidos.consultaDevoluciones(con, forma.getNumeroDevolucion(), forma.getCentroAtencion(), forma.getAlmacen(), forma.getCentroCosto(), UtilidadFecha.conversionFormatoFechaABD(forma.getFechaini()), UtilidadFecha.conversionFormatoFechaABD(forma.getFechafin()), forma.getEstadoDevolucion(), forma.getCheck(), forma.getMotivoDevolucion(), forma.getUsuarioDevuelve(), forma.getUsuarioRecibe()));

		UtilidadBD.closeConnection(con);
		return mapping.findForward("ConsultaDevolucionPedidos");
	}
	
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionConsultarDetalle(ConsultaDevolucionPedidosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario)
	{
		forma.setDetalleDevolucionesMap(ConsultaDevolucionPedidos.consultaDetalleDevoluciones(con, forma.getDevolucionesMap().get("codigo_"+forma.getIndexMap()).toString()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ConsultaDevolucionPedidos");
	}
	
	/**
     * Metdo para Imprimir el Detalle de una Devolcuion Seleccionada
     * @param con
     * @param forma
     * @param mapping
     * @param request
     * @param usuario
     * @return
     */
    private ActionForward accionImprimirReporte(Connection con, ConsultaDevolucionPedidosForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) {
    	String newQuery = SqlBaseConsultaDevolucionPedidosDao.cadena2;
    	String nombreRptDesign = "ConsultaDevolucionPedidos.rptdesign";
    	ConsultaDevolucionPedidos cuc=new ConsultaDevolucionPedidos();

		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//Informacion del Cabezote
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,5);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());
        v.add("DETALLE DEVOLUCION DE PEDIDOS");
        v.add(ins.getDireccion());
        v.add("Tels. "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        
        
        comp.obtenerComponentesDataSet("ConsultaDevolucionPedidos");
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
		return mapping.findForward("ConsultaDevolucionPedidos");
    }
    
    /**
     * Metodo para Imprimir el Listado de Devoluciones
     * @param con
     * @param forma
     * @param mapping
     * @param request
     * @param usuario
     * @return
     */
    private ActionForward accionImprimirListado(Connection con, ConsultaDevolucionPedidosForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) {
    	String newQuery = SqlBaseConsultaDevolucionPedidosDao.cadena;
    	String nombreRptDesign = "ConsultaDevolucionPedidosListado.rptdesign";
    	ConsultaDevolucionPedidos cuc=new ConsultaDevolucionPedidos();

		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");		
		//Informacion del Cabezote
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,5);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());
        v.add("LISTADO DEVOLUCION DE PEDIDOS");
        v.add(ins.getDireccion());
        v.add("Tels. "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
   
        if(forma.getMapaAuxiliar("centroAtencion").equals(""))
        {
		    CentroAtencion centroAtencion= new CentroAtencion();
			ICentroAtencionMundo centroAtencionMundo=AdministracionFabricaMundo.crearCentroAtencionMundo();
			centroAtencion=centroAtencionMundo.findById(Utilidades.convertirAEntero(forma.getCentroAtencion()));
			forma.getMapaAuxiliar().put("centroAtencion",centroAtencion.getDescripcion());
        }
        
        v=new Vector();
        v.add("CENTRO DE ATENCION: "+forma.getMapaAuxiliar("centroAtencion"));
        if(!forma.getNumeroDevolucion().equals(""))
        {
        	v.add("NUMERO DE DEVOLUCION: ");
        }
        else
        {
        	if(!forma.getAlmacen().equals("-1"))
        		v.add("ALMACEN: "+forma.getMapaAuxiliar("almacen"));
        	if(!forma.getCentroCosto().equals("-1"))
        		v.add("CENTRO DE COSTO: "+forma.getMapaAuxiliar("centroCosto"));
        	if(!forma.getFechaini().equals(""))
        		v.add("FECHA INICIAL: "+forma.getFechaini());
        	if(!forma.getFechafin().equals(""))
        		v.add("FECHA FINAL: "+forma.getFechafin());
        	if(!forma.getEstadoDevolucion().equals("-1"))
        		v.add("ESTADO DE LA DEVOLUCION: "+forma.getMapaAuxiliar("estado"));
        	if(forma.getCheck().equals("1"))
        		v.add("INDICATIVO DEVOL PED QX: "+forma.getCheck());
        	if(!forma.getMotivoDevolucion().equals("-1"))
        		v.add("MOTIVO DE LA DEVOLUCION: "+forma.getMapaAuxiliar("motivo"));
        	if(!forma.getUsuarioDevuelve().equals(""))
        		v.add("USUARIO QUE DEVUELVE: "+forma.getUsuarioDevuelve());
        	if(!forma.getUsuarioRecibe().equals(""))
        		v.add("USUARIO QUE RECIBE: "+forma.getUsuarioRecibe());
        }
        comp.insertLabelInGridOfMasterPageWithProperties(2,0,v,DesignChoiceConstants.TEXT_ALIGN_LEFT);
        
        comp.obtenerComponentesDataSet("ConsultaDevolucionPedidosListado");
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
		return mapping.findForward("ConsultaDevolucionPedidos");
    }
	
}