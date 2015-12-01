/*
 * Dic 04, 2007
 */
package util.Busqueda;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.Listado;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.salasCirugia.HojaGastos;

/**
 * Action implementado para realizar la busqueda genérica de paquetes materiales Qx.
 * @author Sebastián Gómez R.
 *
 */
public class BusquedaPaquetesQxGenericaAction extends Action 
{
	/**
	 * Objeto para manejar el log de la clase
	 * */
	Logger logger = Logger.getLogger(BusquedaPaquetesQxGenericaAction.class);
	
	/**
	 * 
	 * */
	public ActionForward execute(ActionMapping mapping,
			 					 ActionForm form,
			 					 HttpServletRequest request,
			 					 HttpServletResponse response) throws Exception 
			 					 {

		Connection con= null;
		try {
			if(response==null);

			if(form instanceof BusquedaPaquetesQxGenericaForm)
			{

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("CodigoDescripcionError","errors.probelmasBd");
					return mapping.findForward("paginaError");
				}

				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				BusquedaPaquetesQxGenericaForm forma = (BusquedaPaquetesQxGenericaForm)form;
				String estado = forma.getEstado();

				logger.info("Valor del Estado Busqueda Paquetes Qx Generica >> "+estado);

				if(estado == null)
				{
					logger.warn("\n\n Estado no Valido dentro del Flujo de Busqueda Paquetes Qx Generica (null)");
					request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}			

				else if(estado.equals("buscar"))
				{			
					return accionBuscar(con,forma,usuario,mapping,request);
				}
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(con);
					response.sendRedirect(forma.getLinkSiguiente());				
					return null;
				}
				else if (estado.equals("ordenarConsultar"))
				{
					return accionOrdenarConsultar(con,forma,mapping);
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
	 * Método que realiza la ordenación del listado de paquetes Qx.
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarConsultar(Connection con, BusquedaPaquetesQxGenericaForm forma, ActionMapping mapping) 
	{
		String[] indices = (String[])forma.getPaquetes("INDICES_MAPA");
		int numReg = forma.getNumRegistros();
		forma.setPaquetes(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getPaquetes(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setPaquetes("numRegistros",numReg+"");
		forma.setPaquetes("INDICES_MAPA",indices);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("buscarPaquetes");
	}

	/**
	 * Método implementado para realizar la busqueda de paquetes materiales Qx genérica
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param mapping 
	 * @param request 
	 * @return
	 */
	private ActionForward accionBuscar(Connection con, BusquedaPaquetesQxGenericaForm forma, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) 
	{
		forma.setPermitirEliminar(UtilidadTexto.getBoolean(request.getParameter("permitirEliminar")));
		forma.setPermitirRepetir(UtilidadTexto.getBoolean(request.getParameter("permitirRepetir")));
		
		forma.setPaquetes(HojaGastos.busquedaGenericaPaquetesMateriales(con, usuario.getCodigoInstitucionInt(), forma.getCodigoServicio(), forma.getParejasClaseGrupo(), forma.getConsecutivosPaquetesInsertados()));
		forma.setNumRegistros(Integer.parseInt(forma.getPaquetes("numRegistros").toString()));
		
		forma.setMaxPageItems(ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("buscarPaquetes");
	}
}
