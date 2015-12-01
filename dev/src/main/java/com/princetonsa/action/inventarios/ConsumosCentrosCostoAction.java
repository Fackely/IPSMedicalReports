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

import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.actionform.inventarios.ConsumosCentrosCostoForm;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseConsumosCentrosCostoDao;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.ConsumosCentrosCosto;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

public class ConsumosCentrosCostoAction extends Action
{
	Logger logger = Logger.getLogger(ConsumosCentrosCostoAction.class);
	
	public ActionForward execute(   ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response ) throws Exception
			{

		Connection con = null;
		try
		{

			if(response==null);
			if(form instanceof ConsumosCentrosCostoForm){

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{	
					request.setAttribute("CodigoDescripcionError","errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				ConsumosCentrosCostoForm forma = (ConsumosCentrosCostoForm)form;
				String estado = forma.getEstado();

				ActionErrors errores=new ActionErrors();

				logger.info("\n\n ESTADO CONSUMOS CENTROS DE COSTO---->"+estado+"\n\n");

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
				else if(estado.equals("cargarCentroCosto"))
				{    			
					return this.accionCargarCentroCosto(forma, con, mapping, usuario);    			
				}
				else if(estado.equals("resetMap"))
				{    			
					return this.accionResetMap(forma, con, mapping, usuario);    			
				}
				else if(estado.equals("generarReporte"))
				{    			
					return this.accionCargarReporte(forma, con, mapping, usuario);    			
				}
				else if(estado.equals("imprimirReporte"))
				{    			
					return this.accionImprimirReporte(con, forma, mapping, request, usuario);    			
				}
				else if(estado.equals("cambiarCentroCosto"))
				{    			
					return this.accionCambiarCentroCosto(con, forma, mapping, request, usuario);    			
				}
				else if(estado.equals("cambiarClase"))
				{    			
					return this.accionCambiarClase(con, forma, mapping, request, usuario);    			
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
	
	private ActionForward accionCambiarClase(Connection con,
			ConsumosCentrosCostoForm forma, ActionMapping mapping,
			HttpServletRequest request, UsuarioBasico usuario) {
		forma.resetCambiarClase();
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ConsumosCentrosCosto");
	}

	private ActionForward accionCambiarCentroCosto(Connection con,
			ConsumosCentrosCostoForm forma, ActionMapping mapping,
			HttpServletRequest request, UsuarioBasico usuario) {
		forma.resetCambiarCentroCosto();
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ConsumosCentrosCosto");	
	}

	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionEmpezar(ConsumosCentrosCostoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ConsumosCentrosCosto");
	}
	
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
	private ActionForward accionCargarCentroCosto(ConsumosCentrosCostoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.setCentroCosto("-1");
		forma.setCentroCostoMap(ConsumosCentrosCosto.consultaCentroCosto(con, Integer.parseInt(forma.getCentroAtencion())));
		
		forma.setClase("-1");
		forma.setClasesMap(ConsumosCentrosCosto.consultaClases(con));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ConsumosCentrosCosto");
	}
	
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
	private ActionForward accionResetMap(ConsumosCentrosCostoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.resetMap();
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ConsumosCentrosCosto");	
	}
	
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
	private ActionForward accionCargarReporte(ConsumosCentrosCostoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
//		se llama el garbage collector
		System.gc();
		logger.info("11 centroCosto - "+forma.getCentroCosto());
		forma.setPedidosMap(ConsumosCentrosCosto.consultaPedidos(con, UtilidadFecha.conversionFormatoFechaABD(forma.getFechafin()), UtilidadFecha.conversionFormatoFechaABD(forma.getFechaini()), Integer.parseInt(forma.getCentroAtencion()), forma.getAlmacen(), forma.getCentroCosto(), forma.getClase()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ConsumosCentrosCosto");
	}
	
	/**
     * @param con
     * @param forma
     * @param mapping
     * @param request
     * @param usuario
     * @return
     */
    private ActionForward accionImprimirReporte(Connection con, ConsumosCentrosCostoForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) {
    	String newQuery = SqlBaseConsumosCentrosCostoDao.cadena;
    	String nombreRptDesign = "ConsumosCentrosCosto.rptdesign";
		
    	ConsumosCentrosCosto cuc=new ConsumosCentrosCosto();

		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//Informacion del Cabezote
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,5);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());
        v.add("SALIDAS POR CONSUMOS DE CENTROS DE COSTO");
        v.add(ins.getDireccion());
        v.add("Tels. "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        
        
        comp.obtenerComponentesDataSet("ConsumosCentrosCosto");
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
		return mapping.findForward("ConsumosCentrosCosto");
    }
}