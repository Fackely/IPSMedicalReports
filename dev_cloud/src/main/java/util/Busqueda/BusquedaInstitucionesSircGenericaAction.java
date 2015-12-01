package util.Busqueda;


import java.util.HashMap;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.Busqueda.BusquedaInstitucionesSircGenericaForm;
import com.princetonsa.mundo.BusquedaInstitucionesSircGenerica;


import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.UtilidadBD;
import com.princetonsa.mundo.UsuarioBasico;

public class BusquedaInstitucionesSircGenericaAction extends Action
{
	
	/**
	 * Objeto para manejar el log de la clase
	 * */
	Logger logger = Logger.getLogger(BusquedaInstitucionesSircGenericaAction.class);
	
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

			if(form instanceof BusquedaInstitucionesSircGenericaForm)
			{

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("CodigoDescripcionError","errors.probelmasBd");
					return mapping.findForward("paginaError");
				}

				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				BusquedaInstitucionesSircGenericaForm forma = (BusquedaInstitucionesSircGenericaForm)form;
				String estado = forma.getEstado();

				if(estado == null)
				{
					logger.warn("\n\n Estado no Valido dentro del Flujo de Busqueda Condicion Toma de Examen (null)");
					request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

				else if(estado.equals("buscar"))
				{
					forma.reset();
					this.accionConsultarInstitucionesSirc(con,forma,usuario.getCodigoInstitucionInt());
					forma.setEstado("listado");
					return mapping.findForward("listarInstituciones");
				}			

				else if(estado.equals("ordenarConsultar"))
				{
					this.accionOrdenarMapa(forma);
					UtilidadBD.closeConnection(con);
					forma.setEstado("listado");
					return mapping.findForward("listarInstituciones");					
				}

				else if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(con);
					forma.setEstado("listado");
					response.sendRedirect(forma.getLinkSiguiente());				
					return null;
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
	 * Llamado a la consulta de Instituciones SIRC
	 * @param Connecition con
	 * @param BusquedaInstitucionesSircGenericaForm 
	 * @param int institucion
	 * */ 
	public void accionConsultarInstitucionesSirc(Connection con,BusquedaInstitucionesSircGenericaForm forma,int institucion)
	{		
		HashMap parametros = new HashMap();
		forma.setConsultaMap("numRegistros","0");
				
		parametros.put("institucion",institucion);
		parametros.put("codigoInstitucionesSirInsert",forma.getCodigosInstitucionesInsertados());
		
		if(!forma.getTipoinstrefC().toString().equals("") && (!forma.getTipoinstrefC().toString().toString().equals(ConstantesBD.codigoNuncaValido+"")))
			parametros.put("tipoinstreferencia",forma.getTipoinstrefC());
		
		if((!forma.getTipoinstambC().toString().equals("")) && (!forma.getTipoinstambC().toString().toString().equals(ConstantesBD.codigoNuncaValido+"")))
			parametros.put("tipoinstambulancia",forma.getTipoinstambC());	
		  
		if((!forma.getOpcionNotIn().toString().equals("")) && (!forma.getOpcionNotIn().toString().toString().equals(ConstantesBD.codigoNuncaValido+"")))
			parametros.put("opcionNotInt",forma.getOpcionNotIn());		
		
		forma.setConsultaMap(BusquedaInstitucionesSircGenerica.consultarInstitucioneSirc(con, parametros));		
		forma.setEstado("listado");
		UtilidadBD.closeConnection(con);		
	}
	
	/**
	 * Ordena el Mapa de acuerdo a un patron
	 * @param forma
	 * */
	private void accionOrdenarMapa(BusquedaInstitucionesSircGenericaForm forma)
	{
		String[] indices = (String[])forma.getConsultaMap("INDICES_MAPA");
		int numReg = Integer.parseInt(forma.getConsultaMap("numRegistros")+"");
		forma.setConsultaMap(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getConsultaMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setConsultaMap("numRegistros",numReg+"");
		forma.setConsultaMap("INDICES_MAPA",indices);		
	}
} 