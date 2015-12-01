package util.Busqueda;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;


/**
 *   Action, controla todas las opciones dentro de la busqueda de Rublos 
 *   incluyendo los posibles casos de error y los casos de flujo.
 * @version 1.0, Dic 31, 2008
 * @author <a href="mailto:aesilva@PrincetonSA.com">Andres Silva</a>
 */
public class BusquedaRubloPresupuestalAction extends Action
{
    private Logger logger = Logger.getLogger(BusquedaRubloPresupuestalAction.class);
    
    public ActionForward execute(   ActionMapping mapping,
                                                    ActionForm form,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response ) throws Exception
                                                    {

    	Connection con=null;

    	try {
    		if (response==null); //Para evitar que salga el warning
    		if(form instanceof BusquedaRubloPresupuestalForm)
    		{

    			try
    			{
    				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();   
    			}
    			catch(SQLException e)
    			{
    				logger.warn("No se pudo abrir la conexión"+e.toString());
    			}

    			UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

    			BusquedaRubloPresupuestalForm forma =(BusquedaRubloPresupuestalForm)form;
    			String estado=forma.getEstado();
    			logger.warn("------------------------");
    			logger.warn("  ESTADO--->"+estado);
    			logger.warn("------------------------");

    			if(estado == null)
    			{
    				forma.reset(); 
    				UtilidadBD.cerrarConexion(con);
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				return mapping.findForward("paginaError");
    			}
    			else if(estado.equals("empezar"))
    			{
    				forma.reset();

    				request.setAttribute("valorPos", request.getParameter("valorPos"));

    				String fecha = UtilidadFecha.getFechaActual();
    				String split[]= fecha.split("/");
    				fecha=split[2];
    				forma.setAnioVigenciaRublo(fecha);
    				logger.info("FECHA ->"+fecha);
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("principal");
    			}

    			else if(estado.equals("busquedaAvanzada"))
    			{
    				forma.setResultados(new HashMap());
    				return this.accionBusquedaAvanzadaRublos(con, forma, mapping, usuario.getCodigoInstitucionInt());
    			}

    			else
    			{
    				forma.reset(); 
    				UtilidadBD.cerrarConexion(con);
    				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
    				return mapping.findForward("principal");
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
    
    /**
     * Metodo Busqueda avanzada de Rublos Presupuestales Generica
     * @param con
     * @param forma
     * @param mapping
     * @param institucion 
     * @return
     */
    private ActionForward accionBusquedaAvanzadaRublos(Connection con, BusquedaRubloPresupuestalForm forma, ActionMapping mapping, int institucion) 
    {
		forma.setResultados(Utilidades.obtenerRublosPresupuestales(con, forma.getAnioVigenciaRublo(), forma.getCodigoRublo(), forma.getDescripcionRublo(),institucion));
    	Utilidades.imprimirMapa(forma.getResultados());
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
		
	}
  
}