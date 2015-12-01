package com.princetonsa.action.manejoPaciente;

import java.sql.Connection;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.CsvFile;
import util.ResultadoBoolean;
import util.TxtFile;
import util.UtilidadBD;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.lowagie.text.BadElementException;
import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.TotalOcupacionCamasForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.TotalOcupacionCamas;
import com.princetonsa.pdf.TotalOcupacionCamasPdf;

/**
 * Fecha Enero - 2008
 * @author Mauricio Jaramillo
 *
 */
public class TotalOcupacionCamasAction extends Action 
{

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger =Logger.getLogger(TotalOcupacionCamasAction.class);
	
	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
			if (form instanceof TotalOcupacionCamasForm) 
			{
				TotalOcupacionCamasForm forma=(TotalOcupacionCamasForm) form;
				String estado=forma.getEstado();
				logger.info("Estado -->"+estado);
				con=UtilidadBD.abrirConexion();
				UsuarioBasico usuarioActual=Utilidades.getUsuarioBasicoSesion(request.getSession());
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				TotalOcupacionCamas mundo= new TotalOcupacionCamas();
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				forma.setMensaje(new ResultadoBoolean(false));

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de TotalOcupacionCamasAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					return accionEmpezar(con, forma, mundo, mapping, request, usuarioActual);
				}
				else if(estado.equals("generar"))
				{
					return accionGenerar(con, forma, mundo, mapping, request, usuarioActual, paciente, institucion);
				}
				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo TOTAL OCUPACION CAMAS ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de TotalOcupacionCamasForm");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
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
	 * Metodo que valida segun el tipo de salida para llamar
	 * al metodo de impresión o de generación de archivo plano
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @param request
	 * @param usuarioActual
	 * @param paciente 
	 * @param institucion 
	 * @return
	 * @throws BadElementException 
	 */
	private ActionForward accionGenerar(Connection con, TotalOcupacionCamasForm forma, TotalOcupacionCamas mundo, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuarioActual, PersonaBasica paciente, InstitucionBasica institucion) throws BadElementException
	{
		//se cargan los datos
		forma.setDatosConsulta(mundo.consultarCamas(con, forma,usuarioActual));
		
		int numReg=Utilidades.convertirAEntero(forma.getDatosConsulta("numRegistros")+"");
		if (numReg>0)
		{
			forma.setNoDatos(false);
			if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
				return generarReporte(con, forma, mundo, usuarioActual, request, mapping);
			else if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
				return generarArchivoPlano(con, forma, mundo, usuarioActual, request, mapping, institucion);
		}
		else
		{
			UtilidadBD.closeConnection(con);
			forma.setNoDatos(true);
		}
		return mapping.findForward("principal");
	}

	/**
	 * Metodo que hace el llamado a la impresiónn en Archivo Plano (CSV)
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuarioActual
	 * @param request
	 * @param mapping
	 * @param institucion
	 */
	private ActionForward  generarArchivoPlano(Connection connection, TotalOcupacionCamasForm forma, TotalOcupacionCamas mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping, InstitucionBasica institucion)
	{
		
		//arma el nombre del reporte
		String nombreReport=CsvFile.armarNombreArchivo("Total-ocupacion-camas", usuario);
		//se genera el documento con la informacion
		StringBuffer texto= new StringBuffer();
		boolean OperacionTrue=false,existeTxt=false;
		int ban=ConstantesBD.codigoNuncaValido;
		texto=TotalOcupacionCamas.cargarMapa(connection,forma, institucion, usuario);
		
		
		//Cargamos en path la ruta definida en parametros generales para validar si esta ruta no esta vacia
		String path = ValoresPorDefecto.getArchivosPlanosReportes(usuario.getCodigoInstitucionInt());
		String pathFinal=ValoresPorDefecto.getReportPath()+path+"totalOcupacionCamas/";
		logger.info("====>Path: "+path);
		//Validamos si el path esta vacio o lleno
    	if(UtilidadTexto.isEmpty(path))
		{
    		forma.setOperacionTrue(false);
	    	forma.setExisteArchivo(false);
	    	UtilidadBD.closeConnection(connection);
	    	return ComunAction.accionSalirCasoError(mapping, request, connection, logger, "Manejo Paciente", "error.manejoPacientes.rutaNoDefinida", true);
		}
    	
    	if (Utilidades.convertirAEntero(forma.getDatosConsulta("numRegistros")+"")>0)
			OperacionTrue=TxtFile.generarTxt(texto, nombreReport, pathFinal, ".cvs");
    	
			if (OperacionTrue)
		{
			//se genera el archivo en formato Zip
			ban=BackUpBaseDatos.EjecutarComandoSO("zip  -j "+pathFinal+nombreReport+".zip"+" "+pathFinal+nombreReport+".cvs");
			//se ingresa la direccion donde se almaceno el archivo
			forma.setRuta(pathFinal+nombreReport+".cvs");
			//se ingresa la ruta para poder descargar el archivo
			forma.setUrlArchivo(ValoresPorDefecto.getReportUrl()+path+"totalOcupacionCamas/"+nombreReport+".zip");
			

			//se valida si existe el txt
			existeTxt=UtilidadFileUpload.existeArchivo(pathFinal, nombreReport+".cvs");
			//se valida si existe el zip
			forma.setExisteArchivo(UtilidadFileUpload.existeArchivo(pathFinal, nombreReport+".zip"));
									
			if (existeTxt)
				forma.setOperacionTrue(true);
		}
		
		
		
		UtilidadBD.closeConnection(connection);
		return mapping.findForward("principal");
	}

	/**
	 * Metodo que hace el llamado a la impresión del reporte en formato PDF
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuarioActual
	 * @param request
	 * @param mapping
	 * @throws BadElementException 
	 */
	private ActionForward generarReporte(Connection connection, TotalOcupacionCamasForm forma, TotalOcupacionCamas mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) throws BadElementException
	{
    	String nombreArchivo;
		Random r= new Random();
		nombreArchivo= "totalOcupacionCamas"+r.nextInt() + ".pdf";
		TotalOcupacionCamasPdf.imprimir(connection, ValoresPorDefecto.getReportPath()+nombreArchivo, forma, usuario, request);
		
		UtilidadBD.closeConnection(connection);
		request.setAttribute("nombreArchivo",nombreArchivo);
		request.setAttribute("nombreVentana", "Total Ocupacion Camas");
		logger.info("\n voy a salir");
		return mapping.findForward("principal");
	}

	/**
	 * Metodo que permite ejecutar los parametros iniciales de la funcionalidad 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @param request
	 * @param usuarioActual
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, TotalOcupacionCamasForm forma, TotalOcupacionCamas mundo, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuarioActual)
	{
		forma.reset();
		//Cargamos el select con todos los centros de atencion
		forma.setCentroAtencion(Utilidades.obtenerCentrosAtencion(usuarioActual.getCodigoInstitucionInt()));
		//Se selecciona el Centro de Atencion de Sesion
		forma.setCodigoCentroAtencion(usuarioActual.getCodigoCentroAtencion()+"");
		//Cargamos los Estados de Cama para ser visualizados
		forma.setInicialMap(mundo.consultarEstados(con));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
}