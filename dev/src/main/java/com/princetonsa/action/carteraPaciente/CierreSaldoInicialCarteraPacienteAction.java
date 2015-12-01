/**
 * 
 */
package com.princetonsa.action.carteraPaciente;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.ResultadoBoolean;
import util.UtilidadFecha;

import com.princetonsa.actionform.carteraPaciente.CierreSaldoInicialCarteraPacienteForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.carteraPaciente.CierreSaldoInicialCarteraPaciente;

/**
 * @author armando
 *
 */
public class CierreSaldoInicialCarteraPacienteAction extends Action 
{
	private static Logger logger = Logger.getLogger(CierreSaldoInicialCarteraPacienteAction.class);
	
	/**
	 * Metodo execute del action
	 * */	
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form, 
								 HttpServletRequest request, 
								 HttpServletResponse response) throws Exception
	{
		
		if (form instanceof CierreSaldoInicialCarteraPacienteForm)
			
		{
			CierreSaldoInicialCarteraPacienteForm forma=(CierreSaldoInicialCarteraPacienteForm)form;
		
			String estado = forma.getEstado();
			
			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			logger.info("estado -->"+estado);
			
			forma.setMensaje(new ResultadoBoolean(false,""));
			if(estado == null)
			{
				logger.warn("Estado no Valido dentro del Saldo Iniciales Cartera Paciente(null)");				 
				request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
			else if(estado.equals("empezar"))
			{
				forma.reset();
				forma.setCierreCarteraPaciente(CierreSaldoInicialCarteraPaciente.consultarCierreInicial(usuario.getCodigoInstitucionInt()));
				if(forma.getCierreCarteraPaciente().getCodigo().intValue()>0)
				{
					return mapping.findForward("resumen");	
				}
				else
				{
					forma.reset();
					forma.getCierreCarteraPaciente().setAnioCierre(UtilidadFecha.getMesAnioDiaActual("anio")+"");
		            int mesAnterior = UtilidadFecha.getMesAnioDiaActual("mes");
		            String mesAnteriorStr=(mesAnterior<10)?"0"+mesAnterior:""+mesAnterior;
		            forma.getCierreCarteraPaciente().setMesCierre(mesAnteriorStr);		
					forma.getCierreCarteraPaciente().setUsuarioGenracion(usuario.getLoginUsuario());
					forma.getCierreCarteraPaciente().setInstitucion(usuario.getCodigoInstitucionInt());
					return mapping.findForward("empezar");
				}
			}
			else if(estado.equals("generar"))
			{
				forma.getCierreCarteraPaciente().setDetalleCierreSaldoIncial(CierreSaldoInicialCarteraPaciente.consultarPosibleListadoDocumentosCierre(forma.getCierreCarteraPaciente().getAnioCierre(),forma.getCierreCarteraPaciente().getMesCierre()));
				if(forma.getCierreCarteraPaciente().getDetalleCierreSaldoIncial().size()>0)
				{
					forma.getCierreCarteraPaciente().setFechaGeneracion(UtilidadFecha.getFechaActual());
					forma.getCierreCarteraPaciente().setHoraGeneracion(UtilidadFecha.getHoraActual());
					forma.getCierreCarteraPaciente().setCodigo(new BigDecimal(CierreSaldoInicialCarteraPaciente.insertarCierreSaldoInicial(forma.getCierreCarteraPaciente())));
					if(forma.getCierreCarteraPaciente().getCodigo().intValue()>0)
						return mapping.findForward("resumen");	
				}
				else
				{
					forma.setMensaje(new ResultadoBoolean(true,"No existe informacion para saldo inicial. Se generara informacion en cero, Continua S/N?"));
					return mapping.findForward("empezar");
				}
				
			}
			else if(estado.equals("generarConfirmado"))
			{
				forma.getCierreCarteraPaciente().setFechaGeneracion(UtilidadFecha.getFechaActual());
				forma.getCierreCarteraPaciente().setHoraGeneracion(UtilidadFecha.getHoraActual());
				forma.getCierreCarteraPaciente().setCodigo(new BigDecimal(CierreSaldoInicialCarteraPaciente.insertarCierreSaldoInicial(forma.getCierreCarteraPaciente())));
				if(forma.getCierreCarteraPaciente().getCodigo().intValue()>0)
					return mapping.findForward("resumen");	
			}
		}				
		return null;
	}

}
