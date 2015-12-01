package com.princetonsa.action.tesoreria;

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

import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.actionform.tesoreria.ConsultaImpresionDevolucionForm;
import com.princetonsa.dao.sqlbase.tesoreria.SqlBaseConsultaImpresionDevolucionDao;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.tesoreria.ConsultaImpresionDevolucion;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

public class ConsultaImpresionDevolucionAction extends Action
{
	Logger logger = Logger.getLogger(ConsultaImpresionDevolucionAction.class);
	
	public ActionForward execute(   ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response ) throws Exception
			{

		Connection con = null;
		try{

			if(response==null);
			if(form instanceof ConsultaImpresionDevolucionForm)
			{

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{	
					request.setAttribute("CodigoDescripcionError","errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				ConsultaImpresionDevolucionForm forma = (ConsultaImpresionDevolucionForm)form;
				String estado = forma.getEstado();

				ActionErrors errores=new ActionErrors();

				logger.info("\n\n ESTADO CONSULTA DEVOLUCION IMPRESION RECIBOS CAJA---->"+estado+"\n\n");

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
				else if(estado.equals("buscarD"))
				{
					return this.accionBuscarD(forma, con, mapping, usuario);
				}
				else if(estado.equals("consultarDetalle"))
				{
					return this.accionConsultarDetalle(forma, con, mapping, usuario);
				}
				else if(estado.equals("imprimirD"))
				{    			
					return this.accionImprimirReporte(con, forma, mapping, request, usuario);    			
				}
				else
					/*------------------------------
					 * 		ESTADO ==> ORDENAR
				-------------------------------*/
					if (estado.equals("ordenar"))
					{
						accionOrdenarMapa(forma);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("ConsultaImpresionDevolucion");
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
    private void accionOrdenarMapa(ConsultaImpresionDevolucionForm forma) {
    	
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
	private ActionForward accionEmpezar(ConsultaImpresionDevolucionForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		forma.setCajasMap(ConsultaImpresionDevolucion.consultaCajas(con));
		forma.setMotivosMap(ConsultaImpresionDevolucion.consultaMotivos(con));
		//forma.setMotDevRCMap(ConsultaImpresionDevolucion.consultaMotivosD(con, usuario.getCodigoInstitucionInt()));
			
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ConsultaImpresionDevolucion");
		
	}
	
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionBuscarD(ConsultaImpresionDevolucionForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.setDevolucionesMap(ConsultaImpresionDevolucion.consultaDevoluciones(con, UtilidadFecha.conversionFormatoFechaABD(forma.getFechaini()), UtilidadFecha.conversionFormatoFechaABD(forma.getFechafin()), forma.getDevolucionI(), forma.getDevolucionF(), forma.getMotivo(), forma.getEstadoD(), forma.getTipoId(), forma.getNumeroId(), forma.getCentroAtencion(), forma.getCaja()));
		//Utilidades.imprimirMapa(forma.getDevolucionesMap());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ConsultaImpresionDevolucion");
	}
	
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionConsultarDetalle(ConsultaImpresionDevolucionForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.setDetalleDMap(ConsultaImpresionDevolucion.consultaDetalleD(con, forma.getDevolucionesMap().get("codigo_"+forma.getIndexMap()).toString()));
		//Utilidades.imprimirMapa(forma.getDetalleDMap());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ConsultaImpresionDevolucionC");
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
    private ActionForward accionImprimirReporte(Connection con, ConsultaImpresionDevolucionForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) {
    	String newQuery = SqlBaseConsultaImpresionDevolucionDao.consultaStrDD;
    	String nombreRptDesign = "ConsultaImpresionDevolucion.rptdesign";
    	ConsultaImpresionDevolucion cuc=new ConsultaImpresionDevolucion();

		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//Informacion del Cabezote
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"tesoreria/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,5);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());
        v.add(ins.getDireccion());
        v.add("Tels. "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        
        
        comp.obtenerComponentesDataSet("ConsultaImpresionDevolucion");
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
		return mapping.findForward("ConsultaImpresionDevolucionC");
    }
}