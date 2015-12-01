/*
 * Creado en Apr 26, 2006
 * Andr�s Mauricio Ruiz V�lez
 * Princeton S.A (Parquesoft - Manizales)
 */
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

import util.UtilidadBD;

import com.princetonsa.actionform.interfaz.CampoInterfazForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.interfaz.CampoInterfaz;

public class CampoInterfazAction extends Action
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(CampoInterfazAction.class);
	
	/**
	 * M�todo encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
																ActionForm form,
																HttpServletRequest request,
																HttpServletResponse response ) throws Exception
																{

		Connection con=null;
		try{


			if (response==null); //Para evitar que salga el warning

			if (form instanceof CampoInterfazForm)
			{

				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexi�n"+e.toString());
				}

				UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				CampoInterfazForm campoInterfazForm = (CampoInterfazForm) form;
				String estado=campoInterfazForm.getEstado();

				logger.warn("Estado CampoInterfazAction [" + estado + "]");

				if(estado == null)
				{
					campoInterfazForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Campo Interfaz (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar (campoInterfazForm,mapping, con, usuario.getCodigoInstitucionInt());
				}
				else if(estado.equals("guardar"))
				{
					return this.accionGuardar(con, mapping, usuario, campoInterfazForm);
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
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
	 * Este m�todo especifica las acciones a realizar en el estado
	 * empezar de la parametrizaci�n de campos de interfaz
	 * @param campoInterfazForm CuentaInventarioForm 
	 * @param mapping Mapping para manejar la navegaci�n
	 * @param con Conexi�n con la fuente de datos
	 * @param institucion
	 * @return ActionForward a la p�gina principal "camposInterfaz.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar	(CampoInterfazForm forma, ActionMapping mapping, Connection con, int institucion) throws SQLException
	{
		CampoInterfaz mundoCampoInterfaz = new CampoInterfaz();
		System.out.print("\n\n Instituci�n  " + institucion);
		
		//----Limpiamos lo que venga del form
		forma.reset();
		
		//----------Se carga la informaci�n general de la parametrizaci�n del archivo plano-----------//
		if (mundoCampoInterfaz.cargarInformacionGral(con, institucion))
			forma.setHayInfoGral(true);
		
		//---------- Se carga en el mapa la informaci�n de los campos de interfaz ya parametrizados --------//
		mundoCampoInterfaz.setMapa(mundoCampoInterfaz.consultarCamposInterfaz (con, institucion));
			
		llenarForm (mundoCampoInterfaz, forma);
		
		forma.setEstado("empezar");		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");		
	}
	
	/**
	 * M�todo que guarda la informaci�n ingresada en las dos secciones de la parametrizaci�n
	 * de los campos de interfaz
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param campoInterfazForm
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionGuardar(Connection con, ActionMapping mapping, UsuarioBasico usuario, CampoInterfazForm campoInterfazForm) throws SQLException
	{
		CampoInterfaz mundoCampoInterfaz = new CampoInterfaz();
		this.llenarMundo(campoInterfazForm, mundoCampoInterfaz);
		
		
		//-------Se inserta/actualiza la informaci�n general de la parametrizaci�n de los campos de interfaz--------//
		mundoCampoInterfaz.insertarActualizarInfoGral (con, usuario, campoInterfazForm);
		
		//-----Se inserta/actuliza los campos de interfaz parametrizados --------------//
		mundoCampoInterfaz.insertarActualizarCamposInterfaz (con, usuario);
		campoInterfazForm.setEstado("empezar");
				
		return accionEmpezar(campoInterfazForm, mapping, con, usuario.getCodigoInstitucionInt());
	}

	/**
	 * M�todo que pasa la infomaci�n del mundo al form
	 * @param mundoCampoInterfaz
	 * @param forma
	 */
	private void llenarForm(CampoInterfaz mundoCampoInterfaz, CampoInterfazForm forma)
	{
		forma.setNombreArchivoSalida(mundoCampoInterfaz.getNombreArchivoSalida());
		forma.setNombreArchivoSalidaAnt(mundoCampoInterfaz.getNombreArchivoSalida());
		forma.setPathArchivoSalida(mundoCampoInterfaz.getPathArchivoSalida());
		forma.setPathArchivoSalidaAnt(mundoCampoInterfaz.getPathArchivoSalida());
		forma.setNombreArchivoInconsistencias(mundoCampoInterfaz.getNombreArchivoInconsistencias());
		forma.setNombreArchivoInconsistenciasAnt(mundoCampoInterfaz.getNombreArchivoInconsistencias());
		forma.setPathArchivoInconsistencias(mundoCampoInterfaz.getPathArchivoInconsistencias());
		forma.setPathArchivoInconsistenciasAnt(mundoCampoInterfaz.getPathArchivoInconsistencias());
		forma.setSeparadorCampos(mundoCampoInterfaz.getSeparadorCampos());
		forma.setSeparadorCamposAnt(mundoCampoInterfaz.getSeparadorCampos());
		forma.setSeparadorDecimales(mundoCampoInterfaz.getSeparadorDecimales());
		forma.setSeparadorDecimalesAnt(mundoCampoInterfaz.getSeparadorDecimales());
		forma.setNombreSeparadorDecimales(mundoCampoInterfaz.getNombreSeparadorDecimales());
		forma.setNombreSeparadorDecimalesAnt(mundoCampoInterfaz.getNombreSeparadorDecimales());
		forma.setIdentificadorFinArchivo(mundoCampoInterfaz.getIdentificadorFinArchivo());
		forma.setIdentificadorFinArchivoAnt(mundoCampoInterfaz.getIdentificadorFinArchivo());
		/*forma.setFacturasAnuladas(mundoCampoInterfaz.getFacturasAnuladas());
		forma.setFacturasAnuladasAnt(mundoCampoInterfaz.getFacturasAnuladas());*/
		forma.setPresentaDevolucionPaciente(mundoCampoInterfaz.getPresentaDevolucionPaciente());
		forma.setPresentaDevolucionPacienteAnt(mundoCampoInterfaz.getPresentaDevolucionPaciente());
		forma.setValorNegativoDevolPaciente(mundoCampoInterfaz.getValorNegativoDevolPaciente());
		forma.setValorNegativoDevolPacienteAnt(mundoCampoInterfaz.getValorNegativoDevolPaciente());
		forma.setDescripcionDebito(mundoCampoInterfaz.getDescripcionDebito());
		forma.setDescripcionDebitoAnt(mundoCampoInterfaz.getDescripcionDebito());
		forma.setDescripcionCredito(mundoCampoInterfaz.getDescripcionCredito());
		forma.setDescripcionCreditoAnt(mundoCampoInterfaz.getDescripcionCredito());
		/*forma.setAgruparFacturasValor(mundoCampoInterfaz.getAgruparFacturasValor());
		forma.setAgruparFacturasValorAnt(mundoCampoInterfaz.getAgruparFacturasValor());*/
		
		//------------- Informaci�n cargada en el mapa ------------------//
		forma.setMapa(mundoCampoInterfaz.getMapa());
	}
	
	/**
	 * M�todo para pasar los datos de la forma al mundo
	 * @param forma
	 * @param mundo
	 */
	private void llenarMundo (CampoInterfazForm forma, CampoInterfaz mundo)
	{
		mundo.setNombreArchivoSalida(forma.getNombreArchivoSalida());
		mundo.setPathArchivoSalida(forma.getPathArchivoInconsistencias());
		mundo.setNombreArchivoInconsistencias(forma.getNombreArchivoInconsistencias());
		mundo.setPathArchivoInconsistencias(forma.getPathArchivoInconsistencias());
		mundo.setSeparadorCampos(forma.getSeparadorCampos());
		mundo.setSeparadorDecimales(forma.getSeparadorDecimales());
		mundo.setIdentificadorFinArchivo(forma.getIdentificadorFinArchivo());
		mundo.setPresentaDevolucionPaciente(forma.getPresentaDevolucionPaciente());
		mundo.setValorNegativoDevolPaciente(forma.getValorNegativoDevolPaciente());
		mundo.setDescripcionDebito(forma.getDescripcionDebito());
		mundo.setDescripcionCredito(forma.getDescripcionCredito());
		
		mundo.setMapa(forma.getMapa());
		
	}
}
