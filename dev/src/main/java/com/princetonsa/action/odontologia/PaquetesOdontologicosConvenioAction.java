/**
 * 
 */
package com.princetonsa.action.odontologia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.odontologia.PaquetesOdontologicosConvenioForm;
import com.princetonsa.dto.odontologia.DtoDetallePaquetesOdontologicosConvenios;
import com.princetonsa.dto.odontologia.DtoPaquetesOdontologicos;
import com.princetonsa.dto.odontologia.DtoPaquetesOdontologicosConvenio;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.EsquemaTarifario;
import com.princetonsa.mundo.odontologia.PaquetesOdontologicosConveniosMundo;
import com.princetonsa.mundo.odontologia.PaquetesOdontologicosMundo;
import com.princetonsa.sort.odontologia.SortGenerico;

/**
 * @author armando
 *
 */
public class PaquetesOdontologicosConvenioAction extends Action 
{
	/**
	 * 
	 */
	private static Logger logger=Logger.getLogger(PaquetesOdontologicosConvenioAction.class);
	
	/**
	 * 
	 */
	public ActionForward execute(	ActionMapping mapping,
		 	ActionForm form,
		 	HttpServletRequest request,
		 	HttpServletResponse response) throws Exception
	{
		
	
		if(form instanceof  PaquetesOdontologicosConvenioForm)
		{
			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			PaquetesOdontologicosConvenioForm forma=(PaquetesOdontologicosConvenioForm)form;
			logger.info("ESTADO: "+forma.getEstado());
			forma.setMensaje(new ResultadoBoolean(false,""));
			if(forma.getEstado().equals("empezar"))
			{
				forma.reset();
				forma.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
				forma.setConvenios(Utilidades.obtenerConvenios("", "", false, "", true, "", "", ""));
				forma.setEsPorProgramas(UtilidadTexto.getBoolean(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt())));
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("cargarContratos"))
			{
				forma.setContrato(ConstantesBD.codigoNuncaValido);
				if(forma.isEsConsulta())
					forma.setContratos(Utilidades.obtenerContratos(forma.getCodigoConvenio(),true,false,false));
				else
					forma.setContratos(Utilidades.obtenerContratos(forma.getCodigoConvenio(),true,false,true));
				forma.setPaqueteConvenio(new DtoPaquetesOdontologicosConvenio());
				if(forma.getContratos().size()==1)
				{
					forma.setContrato(Integer.parseInt(((HashMap)(forma.getContratos().get(0))).get("codigo")+""));
					this.accionCargarInfo(forma,request);
				}
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("cargarInfo"))
			{
				forma.setPaqueteConvenio(new DtoPaquetesOdontologicosConvenio());
				this.accionCargarInfo(forma,request);
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("registroNuevo"))
			{
				forma.setModificacion(false);
				forma.resetNuevoRegistro();
				forma.setPuedoModificar(true);
				//forma.setPaquetesOdontologicos(PaquetesOdontologicosMundo.cargarPaquetesOdontologicoDiferentesArray(cargarArrayList(forma.getPaqueteConvenio().getDetallePaquete())));
				//SE MODIFICA EL DOCUMENTO, YA NO DEBE VALIDAR QUE EL PAQUETE SE REPITA.
				forma.setPaquetesOdontologicos(PaquetesOdontologicosMundo.cargarPaquetesOdontologicoDiferentesArray(new ArrayList<Integer>()));
				forma.setEsquemasTarifario(EsquemaTarifario.cargarEsquemasTarifariosServicios());
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("cargarModificar"))
			{
				DtoDetallePaquetesOdontologicosConvenios dto=forma.getPaqueteConvenio().getDetallePaquete().get(forma.getRegistroSeleccionado());
				forma.setModificacion(true);
				forma.resetNuevoRegistro();
				forma.setCodigoPaquete(dto.getPaquete().getCodigoPk());
				forma.setCodigoEsquema(dto.getEsquemaTarifario());
				forma.setFechaFinal(UtilidadFecha.conversionFormatoFechaAAp(dto.getFechaFinal()));
				forma.setFechaFinalAnterior(UtilidadFecha.conversionFormatoFechaAAp(dto.getFechaFinal()));
				forma.setFechaInicial(UtilidadFecha.conversionFormatoFechaAAp(dto.getFechaIncial()));
				forma.setActivo(dto.getActivo());
				forma.setCodigoPkDetalle(dto.getCodigoPk());
				forma.setPuedoModificar(!dto.isEsUsado());
				//forma.setPaquetesOdontologicos(PaquetesOdontologicosMundo.cargarPaquetesOdontologicoDiferentesArray(cargarArrayList(forma.getPaqueteConvenio().getDetallePaquete())));
				//SE MODIFICA EL DOCUMENTO, YA NO DEBE VALIDAR QUE EL PAQUETE SE REPITA.
				forma.setPaquetesOdontologicos(PaquetesOdontologicosMundo.cargarPaquetesOdontologicoDiferentesArray(new ArrayList<Integer>()));
				forma.setEsquemasTarifario(EsquemaTarifario.cargarEsquemasTarifariosServicios());
				return mapping.findForward("principal");
				
			}
			else if(forma.getEstado().equals("eliminarPaquete"))
			{
				if(PaquetesOdontologicosConveniosMundo.eliminarDetallePaquete(forma.getPaqueteConvenio().getDetallePaquete().get(forma.getRegistroSeleccionado()).getCodigoPk()))
				{
					accionCargarInfo(forma,request);
					forma.setMensaje(new ResultadoBoolean(true,"REGISTRO ELIMINADO EXITOSAMENTE."));
				}
				else
				{
					forma.setMensaje(new ResultadoBoolean(true,"EL REGISTRO NO SE PUDO ELIMINAR."));
				}
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("detallePaquete"))
			{
				if(forma.getCodigoEsquema()>0)
				{
					forma.setTipoTarifario(EsquemaTarifario.obtenerTarifarioOficialXCodigoEsquemaTar(forma.getCodigoEsquema()));
					forma.setDescripcionEsquema(EsquemaTarifario.obtenerNombreEsquema(forma.getCodigoEsquema()));
				}
				
				forma.setPaqueteOdontologico(PaquetesOdontologicosMundo.cargarPaqueteOdontologico(forma.getCodigoPaquete()));
				return mapping.findForward("resumen");
			}
			else if(forma.getEstado().equals("guardar"))
			{
				forma.getPaqueteConvenio().setContrato(forma.getContrato());
				forma.getPaqueteConvenio().setConvenio(forma.getCodigoConvenio());
				forma.getPaqueteConvenio().setFechaModifica(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				forma.getPaqueteConvenio().setHoraModifica(UtilidadFecha.getHoraActual());
				forma.getPaqueteConvenio().setUsuarioModifica(usuario.getLoginUsuario());
				forma.getPaqueteConvenio().setInstitucion(usuario.getCodigoInstitucionInt());
				forma.getPaqueteConvenio().setActivo(forma.getActivo());
				DtoDetallePaquetesOdontologicosConvenios dto=new DtoDetallePaquetesOdontologicosConvenios();
				dto.setCodigoPk(forma.getCodigoPkDetalle());
				dto.setFechaIncial(UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial()));
				dto.setFechaFinal(UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal()));
				
				dto.setActivo(forma.getActivo());
				DtoPaquetesOdontologicos paq=new DtoPaquetesOdontologicos();
				//si no es modificacion, se asigna el paquete seleccionado.
				if(forma.isPuedoModificar())
				{
					paq.setCodigoPk(forma.getCodigoPaquete());
					dto.setEsquemaTarifario(forma.getCodigoEsquema());
				}
				else
				{
				
					paq.setCodigoPk(forma.getPaqueteConvenio().getDetallePaquete().get(forma.getRegistroSeleccionado()).getPaquete().getCodigoPk());
					dto.setEsquemaTarifario(forma.getPaqueteConvenio().getDetallePaquete().get(forma.getRegistroSeleccionado()).getEsquemaTarifario());
				}
				dto.setPaquete(paq);
				if(PaquetesOdontologicosConveniosMundo.insetarModificar(forma.getPaqueteConvenio(),dto,forma.isModificacion()))
				{
					accionCargarInfo(forma,request);
					forma.setMensaje(new ResultadoBoolean(true,"El registro de los paquetes odontol&oacute;gicos por convenio fue exitoso."));
				}
				else
				{
					forma.setMensaje(new ResultadoBoolean(true,"El registro de los paquetes odontol&oacute;gicos por convenio no fue exitoso.  Por favor verifique"));
				}
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("ordenar"))
			{
			
				boolean ordenamiento= false;
				if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente"))
				{
					ordenamiento = true;
				}
				SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
				Collections.sort(forma.getPaqueteConvenio().getDetallePaquete() ,sortG);
				return mapping.findForward("principal");
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	private ArrayList<Integer> cargarArrayList(ArrayList<DtoDetallePaquetesOdontologicosConvenios> detallePaquete) 
	{
		ArrayList<Integer> retorno=new ArrayList<Integer>();
		for(DtoDetallePaquetesOdontologicosConvenios det:detallePaquete)
		{
			retorno.add(det.getPaquete().getCodigoPk());
		}
		return retorno;
	}

	/**
	 * 
	 * @param forma
	 * @param request
	 */
	private void accionCargarInfo(PaquetesOdontologicosConvenioForm forma, HttpServletRequest request) 
	{
		DtoPaquetesOdontologicosConvenio dtoPaquetesOdontologicosConvenio = new DtoPaquetesOdontologicosConvenio();
		dtoPaquetesOdontologicosConvenio = PaquetesOdontologicosConveniosMundo.consultarPaquetesOdontologicosConvenioContrato(forma.getCodigoConvenio(),forma.getContrato());

		if ((dtoPaquetesOdontologicosConvenio != null)
				&& (dtoPaquetesOdontologicosConvenio.getCodigoPk() > 0)) {
			forma.setPaqueteConvenio(dtoPaquetesOdontologicosConvenio);
		} else {
			ActionErrors errores = new ActionErrors();
			errores.add("error no_se_encontraron_resultados",
					new ActionMessage("error.noExisteInformacionBusqueda"));
			saveErrors(request, errores);
		}
		//forma.setPaqueteConvenio(PaquetesOdontologicosConveniosMundo.consultarPaquetesOdontologicosConvenioContrato(forma.getCodigoConvenio(),forma.getContrato()));
		//SortGenerico sortG=new SortGenerico("NombrePaqueteAyudante",true);
		//Collections.sort(forma.getPaqueteConvenio().getDetallePaquete() ,sortG);
	}
}
