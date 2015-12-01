package com.princetonsa.action.tesoreria;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.Utilidades;

import com.princetonsa.actionform.tesoreria.CambioResponsableFaltanteForm;
import com.princetonsa.dto.tesoreria.DTOCambioResponsableDetFaltanteSobrante;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.servicio.fabrica.TesoreriaFabricaServicio;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ICajasCajerosServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ICajasServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IDetFaltanteSobranteServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IFaltanteSobranteServicio;

/**
 * Esta clase se encarga de manejar las solicitudes generales de la
 * entidad Faltante/Sobrante Caja en el m&oacute;dulo de Tesoreria
 *
 * @author Yennifer Guerrero
 * @since
 *
 */
public class FaltanteSobranteAction extends Action {
	
	/**
	 * M&eacute;todo execute de la clase.
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {	
		return null;
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de inicializar los valores de los objetos de la
	 * p&aacute;gina para la b&uacute;squeda de faltante/sobrante
	 * 
	 * 
	 * @param CambioResponsableFaltanteForm
	 *            , UsuarioBasico, ActionMapping
	 * @return ActionForward
	 * @author Angela Maria Aguirre
	 * 
	 */
	public ActionForward empezar(CambioResponsableFaltanteForm forma,
			UsuarioBasico usuario, ActionMapping mapping)
			throws Exception {
		
		IFaltanteSobranteServicio faltanteSobranteServicio = TesoreriaFabricaServicio
				.crearFaltanteSobranteServicio();
		
		forma.reset();
		
		Connection con=HibernateUtil.obtenerConexion();
		
		forma.setPath(Utilidades.obtenerPathFuncionalidad(con, 
				ConstantesBD.codigoFuncionalidadMenuControlCaja));
		
		if(forma.getHabilitaConsultaHistorico().equals("false")){
			forma.getFiltrosFaltanteSobrante().setConsecutivoCA(
					usuario.getCodigoCentroAtencion());
		}	

		forma.setListaCentrosAtencion(AdministracionFabricaServicio
				.crearCentroAtencionServicio().listarCentrosAtencion(true));

		forma.setListadoTipoDiferencia(faltanteSobranteServicio
				.listarTipoDiferencia());
		
		listarCajasCajeros(forma, usuario, forma.getConsecutivoCA());	
		
		Integer[] filtro = {
				ConstantesBD.codigoTipoMovimientoArqueoCierreTurno,
				ConstantesBD.codigoTipoMovimientoAperturaTurnoCaja };

		forma.setListadoTurnoFaltanteSobrante(faltanteSobranteServicio
				.listarTurnosDeCaja(filtro));

		return mapping.findForward("principal");

	}

	/**
	 * 
	 * Este m&eacute;todo se encarga de consultar y retornar una lista de los registros
	 * de faltantes y sobrantes
	 * @param request 
	 * 
	 * @param CambioResponsableFaltanteForm
	 *            , ActionMapping
	 * @return ActionMapping
	 * @author, Angela Maria Aguirre
	 * 
	 */
	public ActionForward buscarDetalleFaltanteSobrante(
			CambioResponsableFaltanteForm forma, ActionMapping mapping, HttpServletRequest request) throws Exception{

		IDetFaltanteSobranteServicio detalleServicio = TesoreriaFabricaServicio.crearDetFaltanteSobranteServicio();

		forma.getFiltrosFaltanteSobrante().setFuncionalidadConsultaHistorico(
				forma.getHabilitaConsultaHistorico());
		ArrayList<DTOCambioResponsableDetFaltanteSobrante> listaDetFaltanteSobrante = detalleServicio
				.busquedaDetFaltanteSobrante(forma.getFiltrosFaltanteSobrante());
		forma.setReglaNavegacion("buscar");
		
		if (listaDetFaltanteSobrante != null
					&& listaDetFaltanteSobrante.size() > 0) {
				
			forma.setListaDetFaltanteSobrantes(listaDetFaltanteSobrante);
			
			if(listaDetFaltanteSobrante.size()==1){
					DTOCambioResponsableDetFaltanteSobrante dtoDetalle= listaDetFaltanteSobrante.get(
							listaDetFaltanteSobrante.size()-1);
					forma.setDtoDetalle(dtoDetalle);			
					forma.setReglaNavegacion("volver");
					forma.setResumen(false);
					forma.setMostrarDetalle(true);
					return mapping.findForward("mostrarDetalle");
			}
		}else {
			ActionErrors errores=new ActionErrors();
			errores.add("No se encontraron resultados", new ActionMessage("errores.modTesoreria.noResultados"));
			saveErrors(request, errores);
			
			return mapping.findForward("principal");
		}
		return mapping.findForward("buscar");				
	}

	/**
	 * 
	 * Este m&eacute;todo se encarga de consultar las cajas y cajeros de acuerdo al
	 * centro de atenci&oacute;n seleccionado.
	 * 
	 * @param CambioResponsableFaltanteForm, codigoCA
	 * @author, Angela Maria Aguirre
	 * 
	 */
	public void listarCajasCajeros(CambioResponsableFaltanteForm forma,
			UsuarioBasico usuario, int codigoCA) throws Exception{
		
		ICajasServicio cajaServicio = TesoreriaFabricaServicio.crearCajasServicio();		
		ICajasCajerosServicio cajerosServicio = TesoreriaFabricaServicio
				.crearCajasCajerosServicio();
		
		int codigoInstitucion = usuario.getCodigoInstitucionInt();
		
		if (codigoCA == -1|| codigoCA == 0) {
			forma.setListaCajas((ArrayList<Cajas>) cajaServicio
					.listarCajasPorInstitucionPorTipoCaja(codigoInstitucion,
							ConstantesBD.codigoTipoCajaRecaudado));
			
			forma.setListaCajeros(cajerosServicio
					.obtenerCajerosPorInstitucion(codigoInstitucion));
			
		}else {
			forma.setListaCajas(cajaServicio
					.listarCajasPorCentrosAtencionPorTipoCaja(codigoCA,
							ConstantesBD.codigoTipoCajaRecaudado));
			
			forma.setListaCajeros(cajerosServicio
					.obtenerCajerosCentrosAtencion(codigoCA));
		}
		
	}
	
	/**
	 * Este m&eacute;todo se encarga de ordenar las columnas de el resultado 
	 * de la b&uacute;squeda sig&aacute;n los par&aacute;metros ingresados
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return ActionForward
	 */
	public ActionForward accionOrdenar(CambioResponsableFaltanteForm forma, UsuarioBasico usuario, ActionMapping mapping){
		
		boolean ordenamiento = false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente"))
		{
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		Collections.sort(forma.getListaDetFaltanteSobrantes(),sortG);

		return mapping.findForward("buscar");
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de consultar el registro
	 * seleccionado por el usuario
	 * 
	 * @param CambioResponsableFaltanteForm, UsuarioBasico,
	 * ActionMapping
	 * @returnActionForward
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward mostrarDetalleRegistro(CambioResponsableFaltanteForm forma, UsuarioBasico usuario,
			ActionMapping mapping){
		
		DTOCambioResponsableDetFaltanteSobrante dtoDetalle = new DTOCambioResponsableDetFaltanteSobrante();
		dtoDetalle = (forma.getListaDetFaltanteSobrantes()).get(forma.getIndex());		
		forma.setDtoDetalle(dtoDetalle);
		forma.setMostrarDetalle(true);
		
		return mapping.findForward("mostrarDetalle");
	}
}
