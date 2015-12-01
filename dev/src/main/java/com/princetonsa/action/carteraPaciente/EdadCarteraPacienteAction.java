package com.princetonsa.action.carteraPaciente;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

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
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.carteraPaciente.EdadCarteraPacienteForm;
import com.princetonsa.dto.carteraPaciente.DtoDatosFinanciacion;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.carteraPaciente.EdadCarteraPaciente;
import com.princetonsa.pdf.ReporteEdadCarteraPacientePdf;

/** * 
 * ** Nota: Se solicita para proximas modificaciones, guardar la estructura del codigo y comentar debidamente los cambios y nuevas lineas.
 */
public class EdadCarteraPacienteAction extends Action
{
	Logger logger = Logger.getLogger(EdadCarteraPacienteAction.class);
	
	/**
	 * Metodo execute del action
	 * */	
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form, 
								 HttpServletRequest request, 
								 HttpServletResponse response) throws Exception
								 {
		Connection con = null;
		try{
			if(response == null);

			if (form instanceof EdadCarteraPacienteForm) 
			{

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				//Usuario cargado en session
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				//ActionErrors
				ActionErrors errores = new ActionErrors();

				EdadCarteraPacienteForm forma = (EdadCarteraPacienteForm)form;		
				String estado = forma.getEstado();		 

				logger.info("-------------------------------------");
				logger.info("Valor del Estado    >> "+forma.getEstado());
				logger.info("-------------------------------------");
				logger.info("-------------------------------------");

				if(estado.equals("iniciar"))
				{
					forma.reset();
					forma.setMapaCentrosAtencion(Utilidades.obtenerCentrosAtencion(usuario.getCodigoInstitucionInt()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("busqueda");
				}
				else if(estado.equals("buscar"))
				{
					accionBuscar(con,forma,usuario,request,errores);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("busqueda");
				}
				else if(estado.equals("imprimir"))
				{
					accionImprimir(con,forma,usuario,request,errores);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("abrirPdf");
				}
				else if(estado.equals("csv"))
				{
					accionArchivCsv(con,forma,usuario,request,false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("descargarFile");
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
	 * Accion Buscar
	 * @param Connection con
	 * @param EdadCarteraPacienteForm forma
	 * @param HttpServletRequest request
	 * @param ActionErrors errores
	 * */
	public void accionBuscar(Connection con,EdadCarteraPacienteForm forma,UsuarioBasico usuario,HttpServletRequest request,ActionErrors errores)
	{	
		errores = EdadCarteraPaciente.validarDatosBusqueda(forma.getDtoEdad().getFechaCorte(),forma.getDtoEdad().getTipoDocumento());
		
		if(errores.isEmpty())
		{
			ArrayList<DtoDatosFinanciacion> array = EdadCarteraPaciente.getListadoEdadGlosa(con,forma.getDtoEdad().getFechaCorte(),forma.getDtoEdad().getTipoDocumento(),forma.getDtoEdad().getCodigoCentroAtencion());
			
			if(array.size() > 0)
			{
				forma.setDtoEdad(EdadCarteraPaciente.construirReporte(forma.getDtoEdad(),array));
				
				if(forma.getDtoEdad().getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
				{
					accionArchivCsv(con, forma, usuario, request, true);
				}
			}
			else
			{
				forma.getDtoEdad().setArrayDatos(new ArrayList<DtoDatosFinanciacion>());
			}
		}
		else
			saveErrors(request, errores);
	}
	
	/**
	 * 
	 * @param Connection con
	 * @param EdadCarteraPacienteForm forma
	 * @param HttpServletRequest request
	 * @param ActionErrors errores
	 * */
	public void accionImprimir(Connection con,EdadCarteraPacienteForm forma,UsuarioBasico usuario,HttpServletRequest request,ActionErrors errores)
	{
		HashMap respuesta = ReporteEdadCarteraPacientePdf.pdfCancelacionCita(
				ValoresPorDefecto.getFilePath(),
				forma.getDtoEdad(), 
				usuario, 
				request);
		
		request.setAttribute("nombreArchivo", System.getProperty("file.separator")+respuesta.get("nombreArchivo").toString());
    	request.setAttribute("nombreVentana", "Edad Cartera Paciente");
	}
	
	/**
	 * 
	 * @param Connection con
	 * @param EdadCarteraPacienteForm forma
	 * @param HttpServletRequest request
	 * @param ActionErrors errores
	 * */
	public void accionArchivCsv(
			Connection con,
			EdadCarteraPacienteForm forma,
			UsuarioBasico usuario,
			HttpServletRequest request,
			boolean generarFile)
	{	
		if(generarFile)
		{
			HashMap respuesta = new HashMap();
			
			respuesta =  ReporteEdadCarteraPacientePdf.cvsCancelacionCita(
					ValoresPorDefecto.getFilePath(),
					forma.getDtoEdad(), 
					usuario, 
					request);
			
			forma.dtoEdad.setRutaNombreArchivo(respuesta.get("nombreArchivo2").toString());
			forma.dtoEdad.setRutaNombreArchivoPrivado(respuesta.get("nombreArchivo").toString());
		}
		
		request.setAttribute("nombreArchivo",forma.dtoEdad.getRutaNombreArchivoPrivado());
		request.setAttribute("nombreVentana", "Reporte Cartera Paciente");
	}
}