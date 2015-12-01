package com.princetonsa.action.interfaz;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.Utilidades;

import com.princetonsa.actionform.interfaz.CuentasConveniosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.interfaz.CuentasConvenio;
import com.princetonsa.mundo.interfaz.CuentasRegimen;

public class CuentasConveniosAction extends Action 
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(CuentasConveniosAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception
			{
		UsuarioBasico usuario;
		Connection con=null;
		CuentasConveniosForm cuentasConveniosForm;

		try{


			if (form instanceof CuentasConveniosForm)
			{
				cuentasConveniosForm = (CuentasConveniosForm)form;
				String estado=cuentasConveniosForm.getEstado();
				logger.warn("Estado CuentasConveniosAction [" + estado + "]");

				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
					cuentasConveniosForm.clean();

					logger.warn("Problemas con la base de datos "+e);
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				if( usuario == null )
				{
					if( logger.isDebugEnabled() )
					{
						logger.debug("Usuario no válido (null)");
					}

					UtilidadBD.cerrarConexion(con);                
					cuentasConveniosForm.clean();                   

					request.setAttribute("codigoDescripcionError", "errors.usuario.noCargado");
					return mapping.findForward("paginaError");              
				}
				else if( estado == null )
				{
					if( logger.isDebugEnabled() )
					{
						logger.debug("estado no valido dentro del flujo de valoración (null) ");
					}

					UtilidadBD.cerrarConexion(con);                
					cuentasConveniosForm.clean();

					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					return mapping.findForward("descripcionError");             
				}
				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(cuentasConveniosForm, mapping, con);
				}
				else if(estado.equals("cargarCuentasRegimen"))
				{
					return this.accionCargarCuentasRegimen(cuentasConveniosForm, mapping, con, usuario);
				}
				else if(estado.equals("cargarCuentasConvenio"))
				{
					return this.accionCargarCuentasConvenio(cuentasConveniosForm, mapping, con, usuario);
				}
				else if(estado.equals("cargarDetalleConvenios"))
				{
					return this.accionCargarDetalleConvenios(cuentasConveniosForm, mapping, con);
				}
				else if(estado.equals("guardarCuentasRegimen"))
				{
					return this.accionGuardarCuentasRegimen(cuentasConveniosForm, mapping, con, usuario);
				}
				else if(estado.equals("guardarCuentasConvenio"))
				{
					return this.accionGuardarCuentasConvenio(cuentasConveniosForm, mapping, con, usuario);
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
	 * Este método especifica las acciones a realizar en el estado empezar de CuentasConvenios
	 * @param cuentasConveniosForm CuentasConveniosForm 
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @param usuario 
	 * @return ActionForward a la página
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar(
			CuentasConveniosForm cuentasConveniosForm, 
			ActionMapping mapping, 
			Connection con) throws SQLException
	{
		//System.out.print("\n\n El Codigo Institucion " + medico.getCodigoInstitucionInt());
		
		cuentasConveniosForm.clean();
		cuentasConveniosForm.setEstado("empezar");		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	private ActionForward accionCargarDetalleConvenios(
			CuentasConveniosForm cuentasConveniosForm, 
			ActionMapping mapping, 
			Connection con) throws SQLException
	{
		cuentasConveniosForm.setEstado("detalleConvenios");		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("detalleConvenios");
	}
	
	private ActionForward accionCargarCuentasRegimen(
			CuentasConveniosForm cuentasConveniosForm, 
			ActionMapping mapping, 
			Connection con, 
			UsuarioBasico usuario) throws Exception
	{
		if(UtilidadCadena.noEsVacio(cuentasConveniosForm.getAcronimoRegimen()))
		{
			CuentasRegimen cuentasRegimen = new CuentasRegimen();
			cuentasRegimen.consultarCuentasRegimen(con, cuentasConveniosForm.getAcronimoRegimen(), Integer.parseInt(usuario.getCodigoInstitucion()));
			cuentasConveniosForm.setCuentas(cuentasRegimen.getCuentas());
			cuentasConveniosForm.setCantidadCuentas(cuentasRegimen.getCantidadCuentas());
			Utilidades.imprimirMapa(cuentasConveniosForm.getCuentas());

			String log="\n          =====INFORMACION ANTES DE LA MODIFICACION===== " +
			"\n*  Acrónimo Tipo Régimen [" +cuentasConveniosForm.getAcronimoRegimen() +"] "+
			"\n*  Institución: ["+usuario.getCodigoInstitucion()+"] ";
			for(int i=0; i<cuentasRegimen.getCantidadCuentas(); i++)
			{
				Integer codigoTipoCuenta = (Integer)cuentasRegimen.getCuenta("codigoTipoCuenta_"+i);
				String nombreTipoCuenta = (String)cuentasRegimen.getCuenta("nombreTipoCuenta_"+codigoTipoCuenta);
				String valor = (String)cuentasRegimen.getCuenta("valor_"+codigoTipoCuenta);
				String capitacion = (String)cuentasRegimen.getCuenta("capitacion_"+codigoTipoCuenta);

				log+="\n*  "+nombreTipoCuenta+": ["+valor+"]";
			}

			log+="\n========================================================";
			cuentasConveniosForm.setLogModificacion(log);
			
			cuentasConveniosForm.setMensaje("");
			cuentasConveniosForm.setEstado("edicionCuentasRegimen");		
		}
		else
			cuentasConveniosForm.clean();
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	private ActionForward accionCargarCuentasConvenio(
			CuentasConveniosForm cuentasConveniosForm, 
			ActionMapping mapping, 
			Connection con, 
			UsuarioBasico usuario) throws Exception
	{
		if(UtilidadCadena.noEsVacio(cuentasConveniosForm.getCodigoConvenio()))
		{
			CuentasConvenio cuentasConvenio = new CuentasConvenio();
			cuentasConvenio.consultarCuentasConvenio(con, Utilidades.convertirAEntero(cuentasConveniosForm.getCodigoConvenio()), Utilidades.convertirAEntero(usuario.getCodigoInstitucion()));
			cuentasConveniosForm.setCuentas(cuentasConvenio.getCuentas());
			cuentasConveniosForm.setCantidadCuentas(cuentasConvenio.getCantidadCuentas());

			String log="\n          =====INFORMACION ANTES DE LA MODIFICACION===== " +
			"\n*  Convenio: [" +cuentasConveniosForm.getNombreConvenio() +"] "+
			"\n*  Institución: ["+usuario.getCodigoInstitucion()+"] ";
			for(int i=0; i<cuentasConvenio.getCantidadCuentas(); i++)
			{
				Integer codigoTipoCuenta = (Integer)cuentasConvenio.getCuenta("codigoTipoCuenta_"+i);
				String nombreTipoCuenta = (String)cuentasConvenio.getCuenta("nombreTipoCuenta_"+codigoTipoCuenta);
				String valor = (String)cuentasConvenio.getCuenta("valor_"+codigoTipoCuenta);

				log+="\n*  "+nombreTipoCuenta+": ["+valor+"]";
			}

			log+="\n========================================================";
			cuentasConveniosForm.setLogModificacion(log);
			
			cuentasConveniosForm.setMensaje("");
			cuentasConveniosForm.setEstado("edicionCuentasConvenio");		
		}
		else
			cuentasConveniosForm.clean();
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	private  ActionForward accionGuardarCuentasRegimen(
			CuentasConveniosForm cuentasConveniosForm, 
			ActionMapping mapping, 
			Connection con, 
			UsuarioBasico usuario) throws Exception
	{
		
		CuentasRegimen cuentasRegimen = new CuentasRegimen();
		Utilidades.imprimirMapa(cuentasRegimen.getCuentas());
		cuentasRegimen.setAcronimoTipoRegimen(cuentasConveniosForm.getAcronimoRegimen());
		cuentasRegimen.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
		cuentasRegimen.setCuentas(cuentasConveniosForm.getCuentas());
		cuentasRegimen.setCantidadCuentas(cuentasConveniosForm.getCantidadCuentas());
		cuentasRegimen.guardar(con);
		
		String log=cuentasConveniosForm.getLogModificacion()+
		"\n          =====INFORMACION DESPUES DE LA MODIFICACION===== " +
		"\n*  Acrónimo Tipo Régimen [" +cuentasRegimen.getAcronimoTipoRegimen() +"] "+
		"\n*  Institución: ["+cuentasRegimen.getCodigoInstitucion()+"] ";
		for(int i=0; i<cuentasRegimen.getCantidadCuentas(); i++)
		{
			String codigoTipoCuenta = (String)cuentasRegimen.getCuenta("codigoTipoCuenta_"+i);
			String nombreTipoCuenta = (String)cuentasRegimen.getCuenta("nombreTipoCuenta_"+codigoTipoCuenta);
			String valor = (String)cuentasRegimen.getCuenta("valor_"+codigoTipoCuenta);
			String rubro = (String)cuentasRegimen.getCuenta("rubro_"+codigoTipoCuenta);
			String capitacion = (String)cuentasRegimen.getCuenta("capitacion_"+codigoTipoCuenta);
			log+="\n*  "+nombreTipoCuenta+": ["+valor+"]";
		}

		log+="\n========================================================\n\n\n";		
		
		LogsAxioma.enviarLog(ConstantesBD.logInterfazFactCuentasConveniosModCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getInformacionGeneralPersonalSalud());
		
		cuentasConveniosForm.setMensaje("¡Las cuentas del régimen han sido modificadas correctamente!");
		cuentasConveniosForm.setEstado("edicionCuentasRegimen");
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	private  ActionForward accionGuardarCuentasConvenio(
			CuentasConveniosForm cuentasConveniosForm, 
			ActionMapping mapping, 
			Connection con, 
			UsuarioBasico usuario) throws Exception
	{
		CuentasConvenio cuentasConvenio = new CuentasConvenio();
		cuentasConvenio.setCodigoConvenio(Integer.parseInt(cuentasConveniosForm.getCodigoConvenio()));
		cuentasConvenio.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
		cuentasConvenio.setCuentas(cuentasConveniosForm.getCuentas());
		cuentasConvenio.setCantidadCuentas(cuentasConveniosForm.getCantidadCuentas());
		cuentasConvenio.guardar(con);
		
		String log=cuentasConveniosForm.getLogModificacion()+
		"\n          =====INFORMACION DESPUES DE LA MODIFICACION===== " +
		"\n*  Convenio: [" +cuentasConveniosForm.getNombreConvenio() +"] "+
		"\n*  Institución: ["+cuentasConvenio.getCodigoInstitucion()+"] ";
		for(int i=0; i<cuentasConvenio.getCantidadCuentas(); i++)
		{
			String codigoTipoCuenta = (String)cuentasConvenio.getCuenta("codigoTipoCuenta_"+i);
			String nombreTipoCuenta = (String)cuentasConvenio.getCuenta("nombreTipoCuenta_"+codigoTipoCuenta);
			String valor = (String)cuentasConvenio.getCuenta("valor_"+codigoTipoCuenta);
			String rubro = (String)cuentasConvenio.getCuenta("rubro_"+codigoTipoCuenta);	
			log+="\n*  "+nombreTipoCuenta+": ["+valor+"]";
		}

		log+="\n========================================================\n\n\n";		
		
		LogsAxioma.enviarLog(ConstantesBD.logInterfazFactCuentasConveniosModCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getInformacionGeneralPersonalSalud());
		
		cuentasConveniosForm.setMensaje("¡Las cuentas del convenio han sido modificadas correctamente!");
		cuentasConveniosForm.setEstado("edicionCuentasConvenio");
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}
}