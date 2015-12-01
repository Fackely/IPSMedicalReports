/**
 * 
 */
package com.princetonsa.action.cartera;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ValoresPorDefecto;

import com.princetonsa.actionform.cartera.ReporteCuentaCobroForm;
import com.princetonsa.dto.cartera.DtoDetalleReporteCuentaCobro;
import com.princetonsa.dto.cartera.DtoResultadoReporteCuentaCobro;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cartera.CuentasCobro;

/**
 * @author armando
 *
 */
public class ReporteCuentaCobroAction extends Action 
{
	
	/**
	 * 
	 */
	private Logger logger = Logger.getLogger(ReporteCuentaCobroAction.class);
	
	/**
	 * 
	 */
	public ActionForward execute(	ActionMapping mapping, 	
	        ActionForm form, 
	        HttpServletRequest request, 
	        HttpServletResponse response) throws Exception
	{
		if(form instanceof ReporteCuentaCobroForm)
		{
			/**
			 * 
			 */
			ReporteCuentaCobroForm forma=(ReporteCuentaCobroForm)form;
			
			UsuarioBasico usuario =(UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
			
			if(forma.getEstado() == null)
			{
				logger.warn("Estado no valido dentro del flujo de CuentasCobroAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
			else if(forma.getEstado().equals("empezar"))
			{
				forma.reset();
				forma.getDtoFiltro().setCentroAtencion(usuario.getCodigoCentroAtencion());
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("imprimirVisual"))
			{
				forma.setDtoResultado(CuentasCobro.generarReporteCuentaCobro(forma.getDtoFiltro()));
				forma.setReporteGenerado(forma.getDtoResultado().size()>0);
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("imprimirArchivoPlano"))
			{
				forma.setDtoResultado(CuentasCobro.generarReporteCuentaCobro(forma.getDtoFiltro()));
				forma.setReporteGenerado(forma.getDtoResultado().size()>0);
				if(forma.isReporteGenerado())
					generarReportePlano(forma,"\n\r",request);
				return mapping.findForward("principal");
			}
			else
			{
				return null;
			}
		}
		else
		{
			logger.error("El form no es compatible con el form de CuentasCobro");
			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			return mapping.findForward("paginaError");
		}	
	}
	
	/**
	 * 
	 * @param forma
	 * @param request 
	 * @param finLinea
	 */
	private void generarReportePlano(ReporteCuentaCobroForm forma, String finDeLinea, HttpServletRequest request) 
	{
		StringBuffer resultado=new StringBuffer();
		resultado.append("Reporte:|Cuentas de Cobro"+finDeLinea);
		resultado.append("Convenio|Cuenta de Cobro| Estado| Centro Atención Elaboración| Fecha Elaboración| Usuario Elaboración| Fecha Radicación| Usuario Radicación| Valor "+finDeLinea);
		for(DtoResultadoReporteCuentaCobro dto:forma.getDtoResultado())
		{
			for(DtoDetalleReporteCuentaCobro dtoDet:dto.getDtoDetalle())
			{
				resultado.append(dto.getNombreConvenio()+"|"+dtoDet.getCuentaCobro()+"|"+dtoDet.getDescripcionEstado()+"|"+dtoDet.getDescripcionCentroAtencion()+"|"+dtoDet.getFechaElaboracion()+"|"+dtoDet.getNombreUsuarioElaboraccion()+"|"+dtoDet.getFechaRadicacion()+"|"+dtoDet.getNombreUsuarioRadicacion()+"|"+dtoDet.getValor()+finDeLinea);
			}
		}
		generarArchivo(ValoresPorDefecto.getReportPath()+"/reporteCuentasCobro/", "ReporteCuentasCobro.csv", resultado);
	}

	/**
	 * 
	 * @param string
	 * @param string2
	 * @param resultado
	 */
	private void generarArchivo(String rutaArchivo, String nombreArchivo,
			StringBuffer resultado) {
		File directorio = new File(rutaArchivo);

		if (!directorio.isDirectory() && !directorio.exists()) 
		{
			if (!directorio.mkdirs()) 
			{
				Log4JManager.error("Error creando el directorio "+ rutaArchivo);
			}
		}
		try 
		{
			FileWriter escritorArchivo = new FileWriter(rutaArchivo+nombreArchivo, false);
			escritorArchivo.write(resultado.toString());
			escritorArchivo.close();
		} 
		catch (IOException e) 
		{
			Log4JManager.error("error generando reporte",e);
		}
		
	}

}
