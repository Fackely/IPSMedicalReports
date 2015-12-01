/*
 * Creado en 16/07/2004
 *
 */
package com.princetonsa.action.cargos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadValidacion;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.cargos.PagosPacienteForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.PagosPaciente;

/**
 * @author Juan David Ramírez López
 *
 */
public class PagosPacienteAction extends Action
{
	/**
	 * Manejador de log de la clase
	 */
	Logger logger=Logger.getLogger(PagosPacienteAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws SQLException
	{
		
		Connection con=null;
		try{
		
		PagosPacienteForm pagosPacienteForm;
		PagosPaciente pagosPacienteMundo = new PagosPaciente();
		
		UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		PersonaBasica paciente=(PersonaBasica)request.getSession().getAttribute("pacienteActivo");
		
		
		if(paciente.getCodigoPersona()<=0)
		{
			request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
			return mapping.findForward("paginaError");
		}
		if(form instanceof PagosPacienteForm)
		{
			try
			{
				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo abrir la conexión"+e.toString());
			}

			pagosPacienteForm=(PagosPacienteForm)form;
			
			String estado=pagosPacienteForm.getEstado();
			
			logger.warn("Estado "+estado);
			
			if(estado.equals("empezar"))
			{
				/**
				 * Validar concurrencia
				 * Si ya está en proceso de facturación, no debe dejar entrar
				 **/
				if(UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), "") )
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoFact", "error.facturacion.cuentaEnProcesoFact", true);
				}
				else
				{
					pagosPacienteForm.reset();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("empezar");
				}
			}

			if(estado.equals("consultar"))
			{
				pagosPacienteForm.reset();
				UtilidadBD.closeConnection(con);
				return mapping.findForward("consultarModificar");
			}
			
			if(estado.equals("consultarOtro"))
			{
				pagosPacienteForm.resetChecks();
				UtilidadBD.closeConnection(con);
				return mapping.findForward("consultarModificar");
			}
			
			if(estado.equals("modificar"))
			{
				pagosPacienteMundo.setCodigo(pagosPacienteForm.getCodigo());
				pagosPacienteMundo.cargar(con);
				UtilidadBD.closeConnection(con);
				try
				{
					PropertyUtils.copyProperties(pagosPacienteForm, pagosPacienteMundo);
				}
				catch (Exception e)
				{
					logger.error("Error copiando las propiedades del mundo al form "+e);
				}
				return mapping.findForward("modificar");
			}

			if(estado.equals("eliminar"))
			{
				pagosPacienteMundo.setCodigo(pagosPacienteForm.getCodigo());
				pagosPacienteMundo.cargar(con);
				String texto="\n            ====INFORMACION ELIMINADA===== " +
				"\n*  Código Pago del Paciente ["+pagosPacienteMundo.getCodigo()+"]" +
				"\n*  Tipo Monto ["+pagosPacienteMundo.getTipoMonto()+"]" +
				"\n*  Documento ["+pagosPacienteMundo.getDocumento()+"]" +
				"\n*  Fecha ["+pagosPacienteMundo.getFecha()+"]" +
				"\n*  Diagnostico ["+pagosPacienteMundo.getDiagnostico()+"]" +
				"\n*  Tipo Cie ["+pagosPacienteMundo.getTipoCie()+"]" +
				"\n*  Descripción ["+pagosPacienteMundo.getDescripcion()+"]" +
				"\n*  Valor Pago ["+pagosPacienteMundo.getValor()+"]" +
				"\n*  Origen Pago ["+pagosPacienteMundo.getOrigen()+"]" +
				"\n*  Institucion ["+pagosPacienteMundo.getInstitucion()+"]" +
				"\n*  Tipo Regimen ["+pagosPacienteMundo.getTipoRegimen()+"]";
				LogsAxioma.enviarLog(ConstantesBD.logPagosPacienteCodigo,texto,ConstantesBD.tipoRegistroLogEliminacion,usuario.getNombreUsuario());
				pagosPacienteMundo.borrar(con);
				pagosPacienteForm.setEstado("consultarOtro");

				UtilidadBD.closeConnection(con);
				return mapping.findForward("consultarModificar");
			}


			if(estado.equals("resultado"))
			{
				HashMap tempo=pagosPacienteForm.getChecks();
				if(tempo.get("entidad")!=null)
				{
					pagosPacienteMundo.setEntidad(pagosPacienteForm.getEntidad());
				}
				if(tempo.get("tipoMonto")!=null)
				{
					pagosPacienteMundo.setTipoMonto(pagosPacienteForm.getTipoMonto());
				}
				if(tempo.get("tipoRegimen")!=null)
				{
					pagosPacienteMundo.setTipoRegimen(pagosPacienteForm.getTipoRegimen());
				}
				if(tempo.get("documento")!=null)
				{
					pagosPacienteMundo.setDocumento(pagosPacienteForm.getDocumento());
				}
				if(tempo.get("fecha")!=null)
				{
					pagosPacienteMundo.setFecha(UtilidadFecha.conversionFormatoFechaABD(pagosPacienteForm.getFecha()));
				}
				if(tempo.get("diagnostico")!=null)
				{
					pagosPacienteMundo.setDiagnostico(pagosPacienteForm.getDiagnostico());
					pagosPacienteMundo.setTipoCie(pagosPacienteForm.getTipoCie());
				}
				if(tempo.get("descripcion")!=null)
				{
					pagosPacienteMundo.setDescripcion(pagosPacienteForm.getDescripcion());
				}
				if(tempo.get("valor")!=null)
				{
					pagosPacienteMundo.setValor(pagosPacienteForm.getValor());
				}
				if(tempo.get("origen")!=null)
				{
					pagosPacienteMundo.setOrigen(pagosPacienteForm.getOrigen());
				}
				String loginUsuario="";
				if(tempo.get("usuario")!=null)
				{
					loginUsuario=pagosPacienteForm.getUsuario();
				}
				pagosPacienteMundo.setInstitucion(Integer.parseInt(usuario.getCodigoInstitucion()));
				pagosPacienteMundo.setUsuario(usuario.getLoginUsuario());
				pagosPacienteForm.setConsulta(pagosPacienteMundo.consultar(con, loginUsuario, paciente.getCodigoPersona()));
				UtilidadBD.closeConnection(con);
				return mapping.findForward("consultarModificar");
			}

			if(estado.equals("guardar"))
			{
				pagosPacienteMundo.setEntidad(pagosPacienteForm.getEntidad());
				pagosPacienteMundo.setTipoMonto(pagosPacienteForm.getTipoMonto());
				pagosPacienteMundo.setDocumento(pagosPacienteForm.getDocumento());
				pagosPacienteMundo.setFecha(UtilidadFecha.conversionFormatoFechaABD(pagosPacienteForm.getFecha()));
				pagosPacienteMundo.setDiagnostico(pagosPacienteForm.getDiagnostico());
				pagosPacienteMundo.setTipoCie(pagosPacienteForm.getTipoCie());
				pagosPacienteMundo.setDescripcion(pagosPacienteForm.getDescripcion());
				pagosPacienteMundo.setValor(pagosPacienteForm.getValor());
				pagosPacienteMundo.setOrigen(pagosPacienteForm.getOrigen());
				pagosPacienteMundo.setInstitucion(Integer.parseInt(usuario.getCodigoInstitucion()));
				pagosPacienteMundo.setUsuario(usuario.getLoginUsuario());
				pagosPacienteMundo.setTipoRegimen(pagosPacienteForm.getTipoRegimen());
				if(!pagosPacienteMundo.insertar(con, paciente.getCodigoPersona()))
				{
					UtilidadBD.closeConnection(con);
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				pagosPacienteMundo.cargar(con);
				try
				{
					pagosPacienteForm.reset();
					PropertyUtils.copyProperties(pagosPacienteForm, pagosPacienteMundo);
				}
				catch (Exception e)
				{
					logger.error("Error copiando las propiedades del mundo al form "+e);
					e.printStackTrace();
					UtilidadBD.closeConnection(con);
					return null;
				}
				System.out.print("Diagnostico "+pagosPacienteForm.getDiagnostico());
				pagosPacienteForm.setEstado("resumen");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("empezar");
			}

			if(estado.equals("guardarModificacion"))
			{
				pagosPacienteMundo.setCodigo(pagosPacienteForm.getCodigo());
				pagosPacienteMundo.cargar(con);
				
				String texto="\n            ====INFORMACION ORIGINAL===== " +
				"\n*  Código Pago del Paciente ["+pagosPacienteMundo.getCodigo()+"]" +
				"\n*  Tipo Monto ["+pagosPacienteMundo.getTipoMonto()+"]" +
				"\n*  Documento ["+pagosPacienteMundo.getDocumento()+"]" +
				"\n*  Fecha ["+pagosPacienteMundo.getFecha()+"]" +
				"\n*  Diagnostico ["+pagosPacienteMundo.getDiagnostico()+"]" +
				"\n*  Tipo Cie ["+pagosPacienteMundo.getTipoCie()+"]" +
				"\n*  Descripción ["+pagosPacienteMundo.getDescripcion()+"]" +
				"\n*  Valor Pago ["+pagosPacienteMundo.getValor()+"]" +
				"\n*  Origen Pago ["+pagosPacienteMundo.getOrigen()+"]" +
				"\n*  Institucion ["+pagosPacienteMundo.getInstitucion()+"]";

				pagosPacienteMundo.setCodigo(pagosPacienteForm.getCodigo());
				pagosPacienteMundo.setEntidad(pagosPacienteForm.getEntidad());
				pagosPacienteMundo.setTipoMonto(pagosPacienteForm.getTipoMonto());
				pagosPacienteMundo.setDocumento(pagosPacienteForm.getDocumento());
				pagosPacienteMundo.setFecha(UtilidadFecha.conversionFormatoFechaABD(pagosPacienteForm.getFecha()));
				pagosPacienteMundo.setDiagnostico(pagosPacienteForm.getDiagnostico());
				pagosPacienteMundo.setTipoCie(pagosPacienteForm.getTipoCie());
				pagosPacienteMundo.setDescripcion(pagosPacienteForm.getDescripcion());
				pagosPacienteMundo.setValor(pagosPacienteForm.getValor());
				pagosPacienteMundo.setOrigen(pagosPacienteForm.getOrigen());
				pagosPacienteMundo.setInstitucion(Integer.parseInt(usuario.getCodigoInstitucion()));
				pagosPacienteMundo.setUsuario(usuario.getLoginUsuario());
				pagosPacienteMundo.setTipoRegimen(pagosPacienteForm.getTipoRegimen());
				pagosPacienteMundo.modificar(con);
				
				try
				{
					pagosPacienteMundo.cargar(con);
					pagosPacienteForm.reset();
					PropertyUtils.copyProperties(pagosPacienteForm, pagosPacienteMundo);
				}
				catch (Exception e)
				{
					logger.error("Error copiando las propiedades del mundo al form "+e);
					UtilidadBD.closeConnection(con);
					return null;
				}
				
				texto+="\n            ====INFORMACION DESPUES DE LA MODIFICACIÓN===== " +
				"\n*  Código Pago del Paciente ["+pagosPacienteMundo.getCodigo()+"]" +
				"\n*  Tipo Monto ["+pagosPacienteMundo.getTipoMonto()+"]" +
				"\n*  Documento ["+pagosPacienteMundo.getDocumento()+"]" +
				"\n*  Fecha ["+pagosPacienteMundo.getFecha()+"]" +
				"\n*  Diagnostico ["+pagosPacienteMundo.getDiagnostico()+"]" +
				"\n*  Tipo Cie ["+pagosPacienteMundo.getTipoCie()+"]" +
				"\n*  Descripción ["+pagosPacienteMundo.getDescripcion()+"]" +
				"\n*  Valor Pago ["+pagosPacienteMundo.getValor()+"]" +
				"\n*  Origen Pago ["+pagosPacienteMundo.getOrigen()+"]" +
				"\n*  Institucion ["+pagosPacienteMundo.getInstitucion()+"]"+
				"\n*  Tipo Regimen ["+pagosPacienteMundo.getTipoRegimen()+"]";
				
				LogsAxioma.enviarLog(ConstantesBD.logPagosPacienteCodigo,texto,ConstantesBD.tipoRegistroLogModificacion,usuario.getNombreUsuario());
				UtilidadBD.closeConnection(con);
				return mapping.findForward("modificar");
			}
			
			if(estado.equals("detalle"))
			{
				pagosPacienteMundo.setDiagnostico(pagosPacienteForm.getDiagnostico());
				pagosPacienteMundo.setTipoCie(pagosPacienteForm.getTipoCie());
				pagosPacienteMundo.setTipoMonto(pagosPacienteForm.getTipoMonto());
				pagosPacienteMundo.setTipoRegimen(pagosPacienteForm.getTipoRegimen());
				pagosPacienteForm.setConsulta(pagosPacienteMundo.detalle(con, pagosPacienteForm.getAnio(), paciente.getCodigoPersona()));
				UtilidadBD.closeConnection(con);
				return mapping.findForward("consulta");
			}
			
			if(estado.equals("consulta"))
			{
				pagosPacienteForm.reset();
				pagosPacienteForm.setConsulta(pagosPacienteMundo.consultarAcumulado(con, paciente.getCodigoPersona()));
				UtilidadBD.closeConnection(con);
				return mapping.findForward("consulta");
			}
			
			if(estado.equals("ordenar"))
			{
				try
				{
					pagosPacienteForm.setConsulta(Listado.ordenarColumna(new ArrayList(pagosPacienteForm.getConsulta()),pagosPacienteForm.getOrdenxColumnaAnterior(),pagosPacienteForm.getOrdenxColumna()));
					pagosPacienteForm.setOrdenxColumnaAnterior(pagosPacienteForm.getOrdenxColumna());
					pagosPacienteForm.setEstado("detalle");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("consulta");
				}
				catch (IllegalAccessException e)
				{
					logger.error("Error al ordenar "+e);
					UtilidadBD.closeConnection(con);
					return null;
				}
			}
			UtilidadBD.closeConnection(con);
			return null;
		}
		else
		{
			return null;
		}
		
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		
		return null;
	}
}
