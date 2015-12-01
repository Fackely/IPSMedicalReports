package com.princetonsa.action.administracion;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.FacturasVarias.UtilidadesFacturasVarias;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.actionform.administracion.DetConceptosRetencionForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.administracion.ConceptosRetencion;
import com.princetonsa.mundo.administracion.DetConceptosRetencion;
import com.princetonsa.mundo.facturacion.IngresarModificarContratosEntidadesSubcontratadas;


public class DetConceptosRetencionAction extends Action
{
	Logger logger = Logger.getLogger(DetConceptosRetencionAction.class);
	
	public ActionForward execute (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Connection con = null;
		try{
		if(response == null);
		
		 if (form instanceof DetConceptosRetencionForm) 
		 {
			 
			 con = UtilidadBD.abrirConexion();
			 
			 if(con == null)
			 {
				 request.setAttribute("CodigoDescripcionError","erros.problemasBd");
				 return mapping.findForward("paginaError");
			 }
			 
			 UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");		 
			 
			 DetConceptosRetencionForm forma = (DetConceptosRetencionForm)form;
			 DetConceptosRetencionForm mundo=new DetConceptosRetencionForm();
			 String estado = forma.getEstado();

			 ActionErrors errores = new ActionErrors();
			 
			 logger.info("-------------------------------------");
			 logger.info("Valor del Estado  >> "+forma.getEstado());
			 logger.info("-------------------------------------");
			 //Estado null
			 if(estado == null)
			 {
				 forma.reset();
				 logger.warn("Estado no Valido dentro del Flujo  (null)");				 
				 request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
				 UtilidadBD.cerrarConexion(con);
				 return mapping.findForward("paginaError");
			 }
			 
			 //Estado ingresarModificar
			 if (estado.equals("ingresarModificar"))
			 {
				 forma.reset();
				 forma.setListaDetConceptosRetencion(DetConceptosRetencion.consultaDetConceptosRetencion());
				 forma.setNumRegistrosListaDetConRet(forma.getListaDetConceptosRetencion().size()); 
				 return mapping.findForward("ingresarModificar");
			 }
			 //Estado refrescar
			 if (estado.equals("refrescar"))
			 {
				 return mapping.findForward("ingresarModificar");
			 }
			 
			 //Estado guardarConceptoretencion
			 if (estado.equals("guardarConceptoRetencion"))
			 {
				 if(!DetConceptosRetencion.validarExisteDetConceptosRetencion(forma.getDtoDetConceptosRetencion()))
				 {
					 forma.getDtoDetConceptosRetencion().setFechaModifica(UtilidadFecha.getFechaActual());
					 forma.getDtoDetConceptosRetencion().setHoraModifica(UtilidadFecha.getHoraActual());
					 forma.getDtoDetConceptosRetencion().setUsuarioModifica(usuario.getLoginUsuario());
					 forma.getDtoDetConceptosRetencion().setInstitucion(usuario.getCodigoInstitucion());
					 DetConceptosRetencion.insertarDetConceptosRetencion(forma.getDtoDetConceptosRetencion()); 
				 }
				 else
				 {
					 errores.add("", new ActionMessage("errors.notEspecific","El Concepto de Retención con la Fecha de Vigencia Inicial y Tipo de Retención ya existe"));
					 saveErrors(request, errores);
				 }
				 forma.setListaDetConceptosRetencion(DetConceptosRetencion.consultaDetConceptosRetencion());
				 forma.setNumRegistrosListaDetConRet(forma.getListaDetConceptosRetencion().size());
				 forma.resetDetConceptosretencion();
				 return mapping.findForward("ingresarModificar");
			 }
			 
			 //Estado eliminarConceptoRetencion
			 if (estado.equals("eliminarConceptoRetencion"))
			 {
				 
				 if (!DetConceptosRetencion.validarDetalleXConcepto(forma.getListaDetConceptosRetencion().get(forma.getIndice()).getConsecutivoPk()))
				 {
					 forma.setListaDetConceptosRetencion(DetConceptosRetencion.consultaDetConceptosRetencion());
					 forma.setNumRegistrosListaDetConRet(forma.getListaDetConceptosRetencion().size());
				 }
				 else
				 {
					 errores.add("", new ActionMessage("errors.notEspecific","El Concepto de Retención Tiene un Detalle Asociado."));
					 saveErrors(request, errores);
				 }
				 return mapping.findForward("ingresarModificar");
			 }
			 
			//Estado ingresarModificar
			 if (estado.equals("consultar"))
			 {
				 forma.reset();
				 forma.setListaDetConceptosRetencion(DetConceptosRetencion.consultaDetConceptosRetencion());
				 forma.setNumRegistrosListaDetConRet(forma.getListaDetConceptosRetencion().size()); 
				 forma.empezarConsulta();
				 return mapping.findForward("ingresarModificar");
			 }
			 
			 //Estado consultarDetalle
			 if (estado.equals("consultarDetalle"))
			 {
				 int tipoRetencion=Utilidades.convertirAEntero(forma.getListaDetConceptosRetencion().get(forma.getIndice()).getTipoRetencion());
				 forma.setListaConceptosRetencion(ConceptosRetencion.consultarConceptosRetencion(usuario.getCodigoInstitucionInt(),tipoRetencion));
				 forma.setListaDetVigConceptosRetencion(DetConceptosRetencion.consultarDetVigenciaConceptosRetencion(forma.getListaDetConceptosRetencion().get(forma.getIndice()).getConsecutivoPk()));
				 forma.setNumRegistrosDetVig(forma.getListaDetVigConceptosRetencion().size());
				 return mapping.findForward("consultarDetalle");
			 }
			 
			 //Estado guardarConceptoRetencion
			 if (estado.equals("guardarDetalleConceptoRetencion"))
			 {
				boolean transaccion=false;
				 
				
			   	if (UtilidadTexto.isEmpty(forma.getDtoDetVigConRetencion().getConceptoRetencion()))
			   	{
					errores.add(forma.getDtoDetConceptosRetencion().getFechaVigenciaInicial(), new ActionMessage("errors.required","El Concepto de Retención "));
					saveErrors(request, errores);
					transaccion=true;
			   	}
		    	
		    	else if (forma.getDtoDetVigConRetencion().getIndicativoIntegral().equals(ConstantesBD.acronimoSi))
		    	{
		    		if (UtilidadTexto.isEmpty(forma.getDtoDetVigConRetencion().getBaseMinima()))
		    		{
						errores.add(forma.getDtoDetConceptosRetencion().getFechaVigenciaInicial(), new ActionMessage("errors.required","La Base Mínima "));
						saveErrors(request, errores);
						transaccion=true;
		    		}
		    		
		    		if (UtilidadTexto.isEmpty(forma.getDtoDetVigConRetencion().getPorcentajeRetInt()))
		    		{
						errores.add(forma.getDtoDetConceptosRetencion().getFechaVigenciaInicial(), new ActionMessage("errors.required","El Porcentaje de Retención Integral "));
						saveErrors(request, errores);
						transaccion=true;
		    		}	
		    	}
			   	
				boolean repiteConcepto=false;
				
			   	for(int i=0;i<forma.getListaDetVigConceptosRetencion().size();i++)
			   	{
			   		if (forma.getListaDetVigConceptosRetencion().get(i).getConceptoRetencion().equals(forma.getDtoDetVigConRetencion().getConceptoRetencion()))
			   			repiteConcepto=true;
			   	}
			   	
			   	if (repiteConcepto)
			   	{
			   		errores.add("", new ActionMessage("errors.notEspecific","Ya existe el detalle de concepto de retención."));
					saveErrors(request, errores);
			   		transaccion=true;
			   	}
			    	
				 if (!transaccion)
				 {
					 forma.getDtoDetVigConRetencion().setDetConceptoretencion(forma.getListaDetConceptosRetencion().get(forma.getIndice()).getConsecutivoPk());
					 forma.getDtoDetVigConRetencion().setFechaModifica(UtilidadFecha.getFechaActual());
					 forma.getDtoDetVigConRetencion().setHoraModifica(UtilidadFecha.getHoraActual());
					 forma.getDtoDetVigConRetencion().setUsuarioModifica(usuario.getLoginUsuario());
					 forma.getDtoDetVigConRetencion().setActivo(ConstantesBD.acronimoSi);
					 
					 if(DetConceptosRetencion.insertarDetVigConRetencion(forma.getDtoDetVigConRetencion()))
					 {
						 forma.setListaDetVigConceptosRetencion(DetConceptosRetencion.consultarDetVigenciaConceptosRetencion(forma.getListaDetConceptosRetencion().get(forma.getIndice()).getConsecutivoPk()));
						 forma.setNumRegistrosDetVig(forma.getListaDetVigConceptosRetencion().size());
						 forma.resetDetVigConRetencion();
					 }
				 }
				 return mapping.findForward("consultarDetalle");
			 }
			 
			 //Estado nuevaDetVigCon
			 if (estado.equals("nuevaDetVigCon"))
			 {
				 forma.resetDetVigConRetencion();
				 int tipoRetencion=Utilidades.convertirAEntero(forma.getListaDetConceptosRetencion().get(forma.getIndice()).getTipoRetencion());
				 forma.setListaConceptosRetencion(ConceptosRetencion.consultarConceptosRetencion(usuario.getCodigoInstitucionInt(),tipoRetencion));
				 forma.setListaDetVigConceptosRetencion(DetConceptosRetencion.consultarDetVigenciaConceptosRetencion(forma.getListaDetConceptosRetencion().get(forma.getIndice()).getConsecutivoPk()));
				 forma.setNumRegistrosDetVig(forma.getListaDetVigConceptosRetencion().size());
				 return mapping.findForward("consultarDetalle");
			 }
			 
			 //Estado para guardar/modificar el indicativo integral
			 if (estado.equals("guardarIndicativo"))
			 {
				 return mapping.findForward("consultarDetalle");
			 }
			 
			 //Estado eliminarDetConVig
			 if (estado.equals("eliminarDetConVig"))
			 {
				 //Se cargan los datos existentes en el dto, y adicionalmente se cargan el usuario, fecha y hora de inactivacion, para realizar la  misma
				 //Carga el Log
				 forma.getDtoLog().setConsecutivoPk(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConsecutivoPk());
				 forma.getDtoLog().setConceptoRetencion(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConceptoRetencion());
				 forma.getDtoLog().setIndicativoIntegral(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getIndicativoIntegral());
				 forma.getDtoLog().setBaseMinima(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getBaseMinima());
				 forma.getDtoLog().setPorcentajeRetInt(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getPorcentajeRetInt());
				 forma.getDtoLog().setFechaModifica(UtilidadFecha.getFechaActual());
				 forma.getDtoLog().setHoraModifica(UtilidadFecha.getHoraActual());
				 forma.getDtoLog().setUsuarioModifica(usuario.getLoginUsuario());
				 
				 //Carga Dto para inactivacion
				 forma.getDtoDetVigConRetencion().setConsecutivoPk(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConsecutivoPk());
				 forma.getDtoDetVigConRetencion().setFechaInactivacion(UtilidadFecha.getFechaActual());
				 forma.getDtoDetVigConRetencion().setHoraInactivacion(UtilidadFecha.getHoraActual());
				 forma.getDtoDetVigConRetencion().setUsuarioInactivacion(usuario.getLoginUsuario());
				 
				 //Se pregunta si el concepto tiene d etalles asociados, si no tiene se peude inactivar
				 int consecutivo=Utilidades.convertirAEntero(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConsecutivoPk());
				 if (!DetConceptosRetencion.poseeDetalles(consecutivo))
				 {
					 //Se actualiza para inactivar
					 if(DetConceptosRetencion.inactivarDetVigConRetencion(forma.getDtoDetVigConRetencion()))
					 {
						 //Si inactiva, se inserta en el log como estaban los datos antes de inactivar
						 DetConceptosRetencion.ingresarLog(forma.getDtoLog());
						 int tipoRetencion=Utilidades.convertirAEntero(forma.getListaDetConceptosRetencion().get(forma.getIndice()).getTipoRetencion());
						 forma.setListaConceptosRetencion(ConceptosRetencion.consultarConceptosRetencion(usuario.getCodigoInstitucionInt(),tipoRetencion));
						 forma.setListaDetVigConceptosRetencion(DetConceptosRetencion.consultarDetVigenciaConceptosRetencion(forma.getListaDetConceptosRetencion().get(forma.getIndice()).getConsecutivoPk()));
						 forma.setNumRegistrosDetVig(forma.getListaDetVigConceptosRetencion().size());
						 forma.resetDetVigConRetencion();
					 }
				 }
				 //Si tiene conceptos asociados, se presenta el mensaje de error
				 else
				 {
					 errores.add("", new ActionMessage("errors.notEspecific","No se puede eliminar el concepto de retención porque posee uno ó más detalles asociados."));
					 saveErrors(request, errores); 
				 }
				 return mapping.findForward("consultarDetalle");
			 }
			 
			 //estado modificarDetConVig
			 if (estado.equals("modificarDetConVig"))
			 {
				 //Se carga el Dto para la modificación con los datos actuales
				 forma.getDtoDetVigConRetencion().setConsecutivoPk(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConsecutivoPk());
				 forma.getDtoDetVigConRetencion().setConceptoRetencion(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConceptoRetencion());
				 forma.getDtoDetVigConRetencion().setDetConceptoretencion(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getDetConceptoretencion());
				 forma.getDtoDetVigConRetencion().setIndicativoIntegral(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getIndicativoIntegral());
				 forma.getDtoDetVigConRetencion().setBaseMinima(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getBaseMinima());
				 forma.getDtoDetVigConRetencion().setPorcentajeRetInt(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getPorcentajeRetInt());
				 forma.getDtoDetVigConRetencion().setActivo(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getActivo());
				 forma.getDtoDetVigConRetencion().setFechaModifica(UtilidadFecha.getFechaActual());
				 forma.getDtoDetVigConRetencion().setHoraModifica(UtilidadFecha.getHoraActual());
				 forma.getDtoDetVigConRetencion().setUsuarioModifica(usuario.getLoginUsuario());
				 //En éste Dto se cargan los datos de inactivación
				 forma.getDtoDetVigConRetencion().setConsecutivoPk(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConsecutivoPk());
				 forma.getDtoDetVigConRetencion().setFechaInactivacion(UtilidadFecha.getFechaActual());
				 forma.getDtoDetVigConRetencion().setHoraInactivacion(UtilidadFecha.getHoraActual());
				 forma.getDtoDetVigConRetencion().setUsuarioInactivacion(usuario.getLoginUsuario());
				 
				 //Se cargan los datos para el Log
				 forma.getDtoLog().setConsecutivoPk(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConsecutivoPk());
				 forma.getDtoLog().setConceptoRetencion(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConceptoRetencion());
				 forma.getDtoLog().setIndicativoIntegral(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getIndicativoIntegral());
				 forma.getDtoLog().setBaseMinima(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getBaseMinima());
				 forma.getDtoLog().setPorcentajeRetInt(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getPorcentajeRetInt());
				 forma.getDtoLog().setFechaModifica(UtilidadFecha.getFechaActual());
				 forma.getDtoLog().setHoraModifica(UtilidadFecha.getHoraActual());
				 forma.getDtoLog().setUsuarioModifica(usuario.getLoginUsuario());
				 
				 if (forma.getDtoDetVigConRetencion().getIndicativoIntegral().equals(ConstantesBD.acronimoSi))
					 forma.setMostrarDiv("visible");
				 else
					 forma.setMostrarDiv("hidden");
				 
				 return mapping.findForward("consultarDetalle");
			 }
			 
			 if (estado.equals("modificarDetConVigActualizar"))
			 {
				 //Se inactiva el registro seleccionado
				 if(DetConceptosRetencion.inactivarDetVigConRetencion(forma.getDtoDetVigConRetencion()))
				 {
					 //Se guarda el log
					 DetConceptosRetencion.ingresarLog(forma.getDtoLog());
					 logger.info("EL VALOR DEL DETALLE CONCEPTO RET-------->"+forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConsecutivoPk());
					 //Luego se inserta el neuvo registro que reemplazará al inactivado
					 if(DetConceptosRetencion.insertarDetVigConRetencion(forma.getDtoDetVigConRetencion()))
					 {
						 //Si el registro inactivado poseía Detalles, los detalles son actualizados con el consecutivo del neuvo registro 
						 int consecutivoPk=UtilidadBD.obtenerUltimoValorSecuencia(con,"administracion.seq_det_vig_con_ret");
						 DetConceptosRetencion.actualizarGrupo(consecutivoPk,Utilidades.convertirAEntero(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConsecutivoPk()));
						 DetConceptosRetencion.actualizarClase(consecutivoPk,Utilidades.convertirAEntero(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConsecutivoPk()));
						 DetConceptosRetencion.actualizarCfv(consecutivoPk,Utilidades.convertirAEntero( forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConsecutivoPk()));
						 
						 //Se lista de nuevo y se resetea e aray y el dto
						 int tipoRetencion=Utilidades.convertirAEntero(forma.getListaDetConceptosRetencion().get(forma.getIndice()).getTipoRetencion());
						 forma.setListaConceptosRetencion(ConceptosRetencion.consultarConceptosRetencion(usuario.getCodigoInstitucionInt(),tipoRetencion));
						 forma.setListaDetVigConceptosRetencion(DetConceptosRetencion.consultarDetVigenciaConceptosRetencion(forma.getListaDetConceptosRetencion().get(forma.getIndice()).getConsecutivoPk()));
						 forma.setNumRegistrosDetVig(forma.getListaDetVigConceptosRetencion().size());
						 forma.resetDetVigConRetencion(); 
					 }
				 }
				 return mapping.findForward("consultarDetalle");
			 }
			  
			 //Estado irRetencionGrupo
			 if (estado.equals("irRetencionGrupo"))
			 {
				 forma.setListaDetConRetGrupos(DetConceptosRetencion.consultarDetRetXGrupoServicio(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConsecutivoPk()));
				 forma.setNumRegistrosGrupos(forma.getListaDetConRetGrupos().size());
				 forma.setGruposServicio(IngresarModificarContratosEntidadesSubcontratadas.obtenerGruposServicio(con, ""));
				 return mapping.findForward("irRetencionGrupo");
			 }
			 
			 //Estado nuevoRetencionGrupo
			 if (estado.equals("nuevoRetencionGrupo"))
			 {
				 forma.resetGrupo();
				 return mapping.findForward("irRetencionGrupo");
			 }
			 //estado insertarRetencionGrupo
			 if (estado.equals("insertarRetencionGrupo"))
			 {
				boolean transaccion=false;
				//Se Valida que no estén vacios losc ampos requeridos
				if (UtilidadTexto.isEmpty(forma.getDtoDetVigConRetGrupo().getTipoElementoRetencion()))
			   	{
					errores.add(forma.getDtoDetConceptosRetencion().getFechaVigenciaInicial(), new ActionMessage("errors.required","El Grupo de Servicio "));
					saveErrors(request, errores);
					transaccion=true;
			   	}
				if (UtilidadTexto.isEmpty(forma.getDtoDetVigConRetGrupo().getBaseMinima()))
			   	{
					errores.add(forma.getDtoDetConceptosRetencion().getFechaVigenciaInicial(), new ActionMessage("errors.required","La Base Mínima "));
					saveErrors(request, errores);
					transaccion=true;
			   	}
				if (UtilidadTexto.isEmpty(forma.getDtoDetVigConRetGrupo().getPorcentaje()))
			   	{
					errores.add(forma.getDtoDetConceptosRetencion().getFechaVigenciaInicial(), new ActionMessage("errors.required","El porcentaje de Retención "));
					saveErrors(request, errores);
					transaccion=true;
			   	}
				
				boolean yaExiste=false;
				for (int i=0;i<forma.getListaDetConRetGrupos().size();i++)
				{
					if (forma.getListaDetConRetGrupos().get(i).getTipoElementoRetencion().equals(forma.getDtoDetVigConRetGrupo().getTipoElementoRetencion()))
						yaExiste=true;
				}
				
				if (yaExiste)
				{
					errores.add("", new ActionMessage("errors.notEspecific","Ya existe el detalle por grupo de servicio "));
					saveErrors(request, errores);
					transaccion=true;
				}
				
				if (!transaccion)
				{
					 forma.getDtoDetVigConRetGrupo().setDetVigconRetencion(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConsecutivoPk());
					 forma.getDtoDetVigConRetGrupo().setFechaModifica(UtilidadFecha.getFechaActual());
					 forma.getDtoDetVigConRetGrupo().setHoraModifica(UtilidadFecha.getHoraActual());
					 forma.getDtoDetVigConRetGrupo().setUsuarioModifica(usuario.getLoginUsuario());
					 forma.getDtoDetVigConRetGrupo().setActivo(ConstantesBD.acronimoSi);
					 if (DetConceptosRetencion.insertarDetRetXGrupoServicio(forma.getDtoDetVigConRetGrupo()))
					 {
						 forma.setListaDetConRetGrupos(DetConceptosRetencion.consultarDetRetXGrupoServicio(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConsecutivoPk()));
						 forma.setNumRegistrosGrupos(forma.getListaDetConRetGrupos().size());
						 forma.resetGrupo();
					 }
				}
				 
				 return mapping.findForward("irRetencionGrupo");
			 }
			 
			//estado insertarRetencionGrupo
			 if (estado.equals("eliminarGrupo"))
			 {
				 forma.getDtoDetVigConRetGrupo().setConsecutivoPk(forma.getListaDetConRetGrupos().get(forma.getIndiceGrupos()).getConsecutivoPk());
				 forma.getDtoDetVigConRetGrupo().setFechaInactivacion(UtilidadFecha.getFechaActual());
				 forma.getDtoDetVigConRetGrupo().setHoraInactivacion(UtilidadFecha.getHoraActual());
				 forma.getDtoDetVigConRetGrupo().setUsuarioInactivacion(usuario.getLoginUsuario());
				 //Actualizar listado y resetear el dto
				 if (DetConceptosRetencion.inactivarDetRetXGrupoServicio(forma.getDtoDetVigConRetGrupo()))
				 {
					 forma.setListaDetConRetGrupos(DetConceptosRetencion.consultarDetRetXGrupoServicio(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConsecutivoPk()));
					 forma.setNumRegistrosGrupos(forma.getListaDetConRetGrupos().size());
					 forma.resetGrupo();
				 }
				 return mapping.findForward("irRetencionGrupo");
			 }

			 //Estado modificarGrupo
			 if (estado.equals("modificarGrupo"))
			 {
				 forma.getDtoDetVigConRetGrupo().setConsecutivoPk(forma.getListaDetConRetGrupos().get(forma.getIndiceGrupos()).getConsecutivoPk());
				 forma.getDtoDetVigConRetGrupo().setDetVigconRetencion(forma.getListaDetConRetGrupos().get(forma.getIndiceGrupos()).getDetVigconRetencion());
				 forma.getDtoDetVigConRetGrupo().setTipoElementoRetencion(forma.getListaDetConRetGrupos().get(forma.getIndiceGrupos()).getTipoElementoRetencion());
				 forma.getDtoDetVigConRetGrupo().setBaseMinima(forma.getListaDetConRetGrupos().get(forma.getIndiceGrupos()).getBaseMinima());
				 forma.getDtoDetVigConRetGrupo().setPorcentaje(forma.getListaDetConRetGrupos().get(forma.getIndiceGrupos()).getPorcentaje());
				 forma.setDetalleModificable(true);
				 return mapping.findForward("irRetencionGrupo");
			 }
			 
			 //Estado modificarGrupoActualizar
			 if (estado.equals("modificarGrupoActualizar"))
			 {
				//Se Valida que no estén vacios losc ampos requeridos
				boolean transaccion=false;
				if (UtilidadTexto.isEmpty(forma.getDtoDetVigConRetGrupo().getTipoElementoRetencion()))
			   	{
					errores.add(forma.getDtoDetConceptosRetencion().getFechaVigenciaInicial(), new ActionMessage("errors.required","El Grupo de Servicio "));
					saveErrors(request, errores);
					transaccion=true;
			   	}
				if (UtilidadTexto.isEmpty(forma.getDtoDetVigConRetGrupo().getBaseMinima()))
			   	{
					errores.add(forma.getDtoDetConceptosRetencion().getFechaVigenciaInicial(), new ActionMessage("errors.required","La Base Mínima "));
					saveErrors(request, errores);
					transaccion=true;
			   	}
				if (UtilidadTexto.isEmpty(forma.getDtoDetVigConRetGrupo().getPorcentaje()))
			   	{
					errores.add(forma.getDtoDetConceptosRetencion().getFechaVigenciaInicial(), new ActionMessage("errors.required","El porcentaje de Retención "));
					saveErrors(request, errores);
					transaccion=true;
			   	}
				if (!transaccion)
				{
					//Se inactiva el elemento seleccionado 
					forma.getDtoDetVigConRetGrupo().setConsecutivoPk(forma.getListaDetConRetGrupos().get(forma.getIndiceGrupos()).getConsecutivoPk());
					 forma.getDtoDetVigConRetGrupo().setFechaInactivacion(UtilidadFecha.getFechaActual());
					 forma.getDtoDetVigConRetGrupo().setHoraInactivacion(UtilidadFecha.getHoraActual());
					 forma.getDtoDetVigConRetGrupo().setUsuarioInactivacion(usuario.getLoginUsuario());
					 if (DetConceptosRetencion.inactivarDetRetXGrupoServicio(forma.getDtoDetVigConRetGrupo()))
					 {
						 //Se inserta el neuvo registro
						 forma.getDtoDetVigConRetGrupo().setDetVigconRetencion(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConsecutivoPk());
						 forma.getDtoDetVigConRetGrupo().setFechaModifica(UtilidadFecha.getFechaActual());
						 forma.getDtoDetVigConRetGrupo().setHoraModifica(UtilidadFecha.getHoraActual());
						 forma.getDtoDetVigConRetGrupo().setUsuarioModifica(usuario.getLoginUsuario());
						 forma.getDtoDetVigConRetGrupo().setActivo(ConstantesBD.acronimoSi);
						 if (DetConceptosRetencion.insertarDetRetXGrupoServicio(forma.getDtoDetVigConRetGrupo()))
						 {
							 forma.setListaDetConRetGrupos(DetConceptosRetencion.consultarDetRetXGrupoServicio(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConsecutivoPk()));
							 forma.setNumRegistrosGrupos(forma.getListaDetConRetGrupos().size());
							 forma.resetGrupo();
						 }
					 }
			 	}
				 return mapping.findForward("irRetencionGrupo");
				 
			 }
			 
			 //Estado irRetencionClase
			 if (estado.equals("irRetencionClase"))
			 {
				 forma.setClasesInventario(UtilidadInventarios.obtenerClasesInventarioArray(con, usuario.getCodigoInstitucionInt()));
				 forma.setListaDetConRetClases(DetConceptosRetencion.consultarDetRetXClaseInv(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConsecutivoPk()));
				 forma.setNumRegistrosClases(forma.getListaDetConRetClases().size());
				 return mapping.findForward("irRetencionClase");
			 }
			 
			//Estado nuevoRetencionGrupo
			 if (estado.equals("nuevoRetencionClase"))
			 {
				 forma.resetClase();
				 return mapping.findForward("irRetencionClase");
			 }
			 
			 //estado insertarRetencionGrupo
			 if (estado.equals("insertarRetencionClase"))
			 {
				boolean transaccion=false;
				//Se Valida que no estén vacios losc ampos requeridos
				if (UtilidadTexto.isEmpty(forma.getDtoDetVigConRetClase().getTipoElementoRetencion()))
			   	{
					errores.add("", new ActionMessage("errors.required","La Clase de Inventario "));
					saveErrors(request, errores);
					transaccion=true;
			   	}
				if (UtilidadTexto.isEmpty(forma.getDtoDetVigConRetClase().getBaseMinima()))
			   	{
					errores.add("", new ActionMessage("errors.required","La Base Mínima "));
					saveErrors(request, errores);
					transaccion=true;
			   	}
				if (UtilidadTexto.isEmpty(forma.getDtoDetVigConRetClase().getPorcentaje()))
			   	{
					errores.add("", new ActionMessage("errors.required","El porcentaje de Retención "));
					saveErrors(request, errores);
					transaccion=true;
			   	}
				
				boolean yaExiste=false;
				for (int i=0;i<forma.getListaDetConRetClases().size();i++)
				{
					if (forma.getListaDetConRetClases().get(i).getTipoElementoRetencion().equals(forma.getDtoDetVigConRetClase().getTipoElementoRetencion()))
						yaExiste=true;
				}
				
				if (yaExiste)
				{
					errores.add("", new ActionMessage("errors.notEspecific","Ya existe el detalle por clase de inventario "));
					saveErrors(request, errores);
					transaccion=true;
				}
				
				if (!transaccion)
				{
					 forma.getDtoDetVigConRetClase().setDetVigconRetencion(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConsecutivoPk());
					 forma.getDtoDetVigConRetClase().setFechaModifica(UtilidadFecha.getFechaActual());
					 forma.getDtoDetVigConRetClase().setHoraModifica(UtilidadFecha.getHoraActual());
					 forma.getDtoDetVigConRetClase().setUsuarioModifica(usuario.getLoginUsuario());
					 forma.getDtoDetVigConRetClase().setActivo(ConstantesBD.acronimoSi);
					 if (DetConceptosRetencion.insertarDetRetXClaseInv(forma.getDtoDetVigConRetClase()))
					 {
						 forma.setListaDetConRetClases(DetConceptosRetencion.consultarDetRetXClaseInv(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConsecutivoPk()));
						 forma.setNumRegistrosClases(forma.getListaDetConRetClases().size());
						 forma.resetClase();
					 }
				}
				 
				 return mapping.findForward("irRetencionClase");
			 }
			 
			//estado eliminarClase
			 if (estado.equals("eliminarClase"))
			 {
				 forma.getDtoDetVigConRetClase().setConsecutivoPk(forma.getListaDetConRetClases().get(forma.getIndiceClase()).getConsecutivoPk());
				 forma.getDtoDetVigConRetClase().setFechaInactivacion(UtilidadFecha.getFechaActual());
				 forma.getDtoDetVigConRetClase().setHoraInactivacion(UtilidadFecha.getHoraActual());
				 forma.getDtoDetVigConRetClase().setUsuarioInactivacion(usuario.getLoginUsuario());
				 //Actualizar listado y resetear el dto
				 if (DetConceptosRetencion.inactivarDetRetXClaseInv(forma.getDtoDetVigConRetClase()))
				 {
					 forma.setListaDetConRetClases(DetConceptosRetencion.consultarDetRetXClaseInv(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConsecutivoPk()));
					 forma.setNumRegistrosGrupos(forma.getListaDetConRetGrupos().size());
					 forma.resetClase();
				 }
				 return mapping.findForward("irRetencionClase");
			 }
			 
			//Estado modificarClase
			 if (estado.equals("modificarClase"))
			 {
				 forma.getDtoDetVigConRetClase().setConsecutivoPk(forma.getListaDetConRetClases().get(forma.getIndiceClase()).getConsecutivoPk());
				 forma.getDtoDetVigConRetClase().setDetVigconRetencion(forma.getListaDetConRetClases().get(forma.getIndiceClase()).getDetVigconRetencion());
				 forma.getDtoDetVigConRetClase().setTipoElementoRetencion(forma.getListaDetConRetClases().get(forma.getIndiceClase()).getTipoElementoRetencion());
				 forma.getDtoDetVigConRetClase().setBaseMinima(forma.getListaDetConRetClases().get(forma.getIndiceClase()).getBaseMinima());
				 forma.getDtoDetVigConRetClase().setPorcentaje(forma.getListaDetConRetClases().get(forma.getIndiceClase()).getPorcentaje());
				 forma.setDetalleModificable(true);
				 return mapping.findForward("irRetencionClase");
			 }
			 
			 //Estado modificarClaseActualizar
			 if (estado.equals("modificarClaseActualizar"))
			 {
				//Se Valida que no estén vacios losc ampos requeridos
				boolean transaccion=false;
				if (UtilidadTexto.isEmpty(forma.getDtoDetVigConRetClase().getTipoElementoRetencion()))
			   	{
					errores.add(forma.getDtoDetConceptosRetencion().getFechaVigenciaInicial(), new ActionMessage("errors.required","La Clase de Inventario "));
					saveErrors(request, errores);
					transaccion=true;
			   	}
				if (UtilidadTexto.isEmpty(forma.getDtoDetVigConRetClase().getBaseMinima()))
			   	{
					errores.add(forma.getDtoDetConceptosRetencion().getFechaVigenciaInicial(), new ActionMessage("errors.required","La Base Mínima "));
					saveErrors(request, errores);
					transaccion=true;
			   	}
				if (UtilidadTexto.isEmpty(forma.getDtoDetVigConRetClase().getPorcentaje()))
			   	{
					errores.add(forma.getDtoDetConceptosRetencion().getFechaVigenciaInicial(), new ActionMessage("errors.required","El porcentaje de Retención "));
					saveErrors(request, errores);
					transaccion=true;
			   	}
				if (!transaccion)
				{
					//Se inactiva el elemento seleccionado 
					 forma.getDtoDetVigConRetClase().setConsecutivoPk(forma.getListaDetConRetClases().get(forma.getIndiceClase()).getConsecutivoPk());
					 forma.getDtoDetVigConRetClase().setFechaInactivacion(UtilidadFecha.getFechaActual());
					 forma.getDtoDetVigConRetClase().setHoraInactivacion(UtilidadFecha.getHoraActual());
					 forma.getDtoDetVigConRetClase().setUsuarioInactivacion(usuario.getLoginUsuario());
					 if (DetConceptosRetencion.inactivarDetRetXClaseInv(forma.getDtoDetVigConRetClase()))
					 {
						 forma.getDtoDetVigConRetClase().setDetVigconRetencion(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConsecutivoPk());
						 forma.getDtoDetVigConRetClase().setFechaModifica(UtilidadFecha.getFechaActual());
						 forma.getDtoDetVigConRetClase().setHoraModifica(UtilidadFecha.getHoraActual());
						 forma.getDtoDetVigConRetClase().setUsuarioModifica(usuario.getLoginUsuario());
						 forma.getDtoDetVigConRetClase().setActivo(ConstantesBD.acronimoSi);
						 if (DetConceptosRetencion.insertarDetRetXClaseInv(forma.getDtoDetVigConRetClase()))
						 {
							 forma.setListaDetConRetClases(DetConceptosRetencion.consultarDetRetXClaseInv(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConsecutivoPk()));
							 forma.setNumRegistrosClases(forma.getListaDetConRetClases().size());
							 forma.resetClase();
						 }
					 }
			 	}
				 return mapping.findForward("irRetencionClase"); 
			 }
			 
			//Estado irRetencionConcepto
			 if (estado.equals("irRetencionConcepto"))
			 {
				 forma.setCfv(UtilidadesFacturasVarias.obtenerConceptosFraVarias(con, usuario.getCodigoInstitucionInt(), true));
				 forma.setListaDetConRetConceptos(DetConceptosRetencion.consultarDetRetXConceptos(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConsecutivoPk()));
				 forma.setNumRegistrosConceptos(forma.getListaDetConRetConceptos().size());
				 return mapping.findForward("irRetencionCfv");
			 }
			 
			//Estado nuevoRetencionConcepto
			 if (estado.equals("nuevoRetencionConcepto"))
			 {
				 forma.resetConceptos();
				 return mapping.findForward("irRetencionCfv");
			 }
			 
			 //estado insertarRetencionConcepto
			 if (estado.equals("insertarRetencionConcepto"))
			 {
				boolean transaccion=false;
				//Se Valida que no estén vacios losc ampos requeridos
				if (UtilidadTexto.isEmpty(forma.getDtoDetVigConRetCfv().getTipoElementoRetencion()))
			   	{
					errores.add("", new ActionMessage("errors.required","El Concepto de Factura Varia "));
					saveErrors(request, errores);
					transaccion=true;
			   	}
				if (UtilidadTexto.isEmpty(forma.getDtoDetVigConRetCfv().getBaseMinima()))
			   	{
					errores.add("", new ActionMessage("errors.required","La Base Mínima "));
					saveErrors(request, errores);
					transaccion=true;
			   	}
				if (UtilidadTexto.isEmpty(forma.getDtoDetVigConRetCfv().getPorcentaje()))
			   	{
					errores.add("", new ActionMessage("errors.required","El porcentaje de Retención "));
					saveErrors(request, errores);
					transaccion=true;
			   	}
				
				boolean yaExiste=false;
				for (int i=0;i<forma.getListaDetConRetConceptos().size();i++)
				{
					if (forma.getListaDetConRetConceptos().get(i).getTipoElementoRetencion().equals(forma.getDtoDetVigConRetCfv().getTipoElementoRetencion()))
						yaExiste=true;
				}
				
				if (yaExiste)
				{
					errores.add("", new ActionMessage("errors.notEspecific","Ya existe el detalle por concepto de factura varia "));
					saveErrors(request, errores);
					transaccion=true;
				}
				
				if (!transaccion)
				{
					 forma.getDtoDetVigConRetCfv().setDetVigconRetencion(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConsecutivoPk());
					 forma.getDtoDetVigConRetCfv().setFechaModifica(UtilidadFecha.getFechaActual());
					 forma.getDtoDetVigConRetCfv().setHoraModifica(UtilidadFecha.getHoraActual());
					 forma.getDtoDetVigConRetCfv().setUsuarioModifica(usuario.getLoginUsuario());
					 forma.getDtoDetVigConRetCfv().setActivo(ConstantesBD.acronimoSi);
					 if (DetConceptosRetencion.insertarDetRetXConcepto(forma.getDtoDetVigConRetCfv()))
					 {
						 forma.setListaDetConRetConceptos(DetConceptosRetencion.consultarDetRetXConceptos(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConsecutivoPk()));
						 forma.setNumRegistrosConceptos(forma.getListaDetConRetConceptos().size());
						 forma.resetConceptos();
					 }
				}
				 return mapping.findForward("irRetencionCfv");
			 }
			 
			//estado eliminarConcepto
			 if (estado.equals("eliminarConcepto"))
			 {
				 forma.getDtoDetVigConRetCfv().setConsecutivoPk(forma.getListaDetConRetConceptos().get(forma.getIndiceConceptos()).getConsecutivoPk());
				 forma.getDtoDetVigConRetCfv().setFechaInactivacion(UtilidadFecha.getFechaActual());
				 forma.getDtoDetVigConRetCfv().setHoraInactivacion(UtilidadFecha.getHoraActual());
				 forma.getDtoDetVigConRetCfv().setUsuarioInactivacion(usuario.getLoginUsuario());
				 //Actualizar listado y resetear el dto
				 if (DetConceptosRetencion.inactivarDetRetXConcepto(forma.getDtoDetVigConRetCfv()))
				 {
					 forma.setListaDetConRetConceptos(DetConceptosRetencion.consultarDetRetXConceptos(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConsecutivoPk()));
					 forma.setNumRegistrosConceptos(forma.getListaDetConRetConceptos().size());
					 forma.resetConceptos();
				 }
				 return mapping.findForward("irRetencionCfv");
			 }
			 
			//Estado modificarConcepto
			 if (estado.equals("modificarConcepto"))
			 {
				 forma.getDtoDetVigConRetCfv().setConsecutivoPk(forma.getListaDetConRetConceptos().get(forma.getIndiceConceptos()).getConsecutivoPk());
				 forma.getDtoDetVigConRetCfv().setDetVigconRetencion(forma.getListaDetConRetConceptos().get(forma.getIndiceConceptos()).getDetVigconRetencion());
				 forma.getDtoDetVigConRetCfv().setTipoElementoRetencion(forma.getListaDetConRetConceptos().get(forma.getIndiceConceptos()).getTipoElementoRetencion());
				 forma.getDtoDetVigConRetCfv().setBaseMinima(forma.getListaDetConRetConceptos().get(forma.getIndiceConceptos()).getBaseMinima());
				 forma.getDtoDetVigConRetCfv().setPorcentaje(forma.getListaDetConRetConceptos().get(forma.getIndiceConceptos()).getPorcentaje());
				 forma.setDetalleModificable(true);
				 return mapping.findForward("irRetencionCfv");
			 }
			 
			 
			 //Estado modificarConceptoActualizar
			 if (estado.equals("modificarConceptoActualizar"))
			 {
				//Se Valida que no estén vacios losc ampos requeridos
				boolean transaccion=false;
				if (UtilidadTexto.isEmpty(forma.getDtoDetVigConRetCfv().getTipoElementoRetencion()))
			   	{
					errores.add("", new ActionMessage("errors.required","El Concepto de Factura Varia "));
					saveErrors(request, errores);
					transaccion=true;
			   	}
				if (UtilidadTexto.isEmpty(forma.getDtoDetVigConRetCfv().getBaseMinima()))
			   	{
					errores.add("", new ActionMessage("errors.required","La Base Mínima "));
					saveErrors(request, errores);
					transaccion=true;
			   	}
				if (UtilidadTexto.isEmpty(forma.getDtoDetVigConRetCfv().getPorcentaje()))
			   	{
					errores.add("", new ActionMessage("errors.required","El porcentaje de Retención "));
					saveErrors(request, errores);
					transaccion=true;
			   	}
				if (!transaccion)
				{
					//Se inactiva el elemento seleccionado 
					 forma.getDtoDetVigConRetCfv().setConsecutivoPk(forma.getListaDetConRetConceptos().get(forma.getIndiceConceptos()).getConsecutivoPk());
					 forma.getDtoDetVigConRetCfv().setFechaInactivacion(UtilidadFecha.getFechaActual());
					 forma.getDtoDetVigConRetCfv().setHoraInactivacion(UtilidadFecha.getHoraActual());
					 forma.getDtoDetVigConRetCfv().setUsuarioInactivacion(usuario.getLoginUsuario());
					 if (DetConceptosRetencion.inactivarDetRetXConcepto(forma.getDtoDetVigConRetCfv()))
					 {
						 forma.getDtoDetVigConRetCfv().setDetVigconRetencion(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConsecutivoPk());
						 forma.getDtoDetVigConRetCfv().setFechaModifica(UtilidadFecha.getFechaActual());
						 forma.getDtoDetVigConRetCfv().setHoraModifica(UtilidadFecha.getHoraActual());
						 forma.getDtoDetVigConRetCfv().setUsuarioModifica(usuario.getLoginUsuario());
						 forma.getDtoDetVigConRetCfv().setActivo(ConstantesBD.acronimoSi);
						 if (DetConceptosRetencion.insertarDetRetXConcepto(forma.getDtoDetVigConRetCfv()))
						 {
							 forma.setListaDetConRetConceptos(DetConceptosRetencion.consultarDetRetXConceptos(forma.getListaDetVigConceptosRetencion().get(forma.getIndiceDetConceptosVig()).getConsecutivoPk()));
							 forma.setNumRegistrosConceptos(forma.getListaDetConRetConceptos().size());
							 forma.resetConceptos();
						 }
					 }
			 	}
				 return mapping.findForward("irRetencionCfv"); 
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
}