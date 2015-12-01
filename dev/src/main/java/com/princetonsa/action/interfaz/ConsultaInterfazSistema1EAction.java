package com.princetonsa.action.interfaz;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesIntegridadDominio;
import util.UtilidadBD;

import com.princetonsa.actionform.interfaz.ConsultaInterfazSistema1EForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ArchivoPlano;
import com.princetonsa.mundo.interfaz.ConsultaInterfazSistema1E;
import com.princetonsa.mundo.interfaz.DesmarcarDocProcesados;
import com.princetonsa.mundo.interfaz.GeneracionInterfaz1E;

public class ConsultaInterfazSistema1EAction extends Action
{
	Logger logger = Logger.getLogger(ConsultaInterfazSistema1EAction.class);
	
	public ActionForward execute (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Connection con = null;
		try{

			if(response == null);

			if (form instanceof ConsultaInterfazSistema1EForm) 
			{

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");		 

				ConsultaInterfazSistema1EForm forma = (ConsultaInterfazSistema1EForm)form;
				ConsultaInterfazSistema1EForm mundo=new ConsultaInterfazSistema1EForm();
				String estado = forma.getEstado();

				ActionErrors errores = new ActionErrors();

				logger.info("-------------------------------------");
				logger.info("Valor del Estado  >> "+forma.getEstado());
				logger.info("-------------------------------------");

				if(estado == null)
				{
					forma.reset();
					logger.warn("Estado no Valido dentro del Flujo  (null)");				 
					request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

				//Estado Volver al menu
				if (estado.equals("volverMenu"))
					return mapping.findForward("menu");


				//Estado empezar
				if (estado.equals("filtrosConsulta"))
				{
					forma.reset();
					return mapping.findForward("filtrosConsulta");
				}

				//Estado para consultar los tipos de documentos X tipo de mVto buscarTipoDocXMov
				if (estado.equals("buscarTipoDocXMov"))
				{
					if (!forma.isSelectActivo())
						forma.setArrayDocs(DesmarcarDocProcesados.consultarDocumentosXtipoMvto(forma.getFiltros("tipoMovimiento")+""));

					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("filtrosConsulta");
				}

				//Estado para consultar el log
				if (estado.equals("buscar"))
				{
					forma.setArrayLog(ConsultaInterfazSistema1E.consultarLog(con, forma.getFiltros()));
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("resultadoBusqueda");
				}

				//Estado para cargar la info del archivo
				if (estado.equals("cargarArchivo"))
				{
					accionCargarArchivo(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("cargarArchivo");
				}

				//Estado para generar el .zip del archivo
				if (estado.equals("generarArchivo"))
				{
					String ruta=ArchivoPlano.generarArchivoZipString(forma.getRutaInd(), forma.getNombreInd());
					request.setAttribute("nombreArchivo", ruta);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("descargarFile");
				}

				if (estado.equals("activarInactivarTipoDoc"))
				{
					if (forma.getFiltros("tipoProceso").toString().equals(ConstantesIntegridadDominio.acronimoTipoProcesoGeneracionInterfaz))
					{
						forma.setSelectActivo(true);
						forma.resetTipoDoc();
					}
					else
					{
						forma.setSelectActivo(false);
						forma.setArrayDocs(DesmarcarDocProcesados.consultarDocumentosXtipoMvto(forma.getFiltros("tipoMovimiento")+""));
					}


					return mapping.findForward("filtrosConsulta");
				}



				//Error
				if (estado.equals(""))
				{
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
	 * carga un archivo 
	 * @param GeneracionInterfaz1EForm forma
	 * */
	public void accionCargarArchivo(ConsultaInterfazSistema1EForm forma)
	{		
		forma.setContenidoArchivo(GeneracionInterfaz1E.cargarArchivo(forma.getRutaInd(),forma.getNombreInd()));
	}
}
