package com.princetonsa.action.tesoreria;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;

import com.princetonsa.actionform.tesoreria.ArqueoEntregaParcialForm;
import com.princetonsa.actionform.tesoreria.ArqueosCajaForm;
import com.princetonsa.actionform.tesoreria.CierreTurnoCajaForm;
import com.princetonsa.actionform.tesoreria.MovimientosCajaForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.MovimientosCaja;


/**
 * Action para manejar todo la informaci&oacute;n que es com&uacute;n a los Anexos:
 * 
 * 226 - Arqueos Caja
 * 227 - Arqueo Entrega parcial
 * 228 - Cierre Turno de Caja
 * 
 * @author Jorge Armando Agudelo Quintero 
 * 
 */

public class MovimientosCajaAction extends Action{
	
	/**
	 * M&eacute;todo que se encarga de procesar las peticiones realizadas que se relacionan
	 * con los movimientos de caja de tipo Arqueo
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 *
	 * @return ActionForward
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		try {
			if(form instanceof MovimientosCajaForm)
			{

				ActionErrors errores=new ActionErrors();

				MovimientosCajaForm forma=(MovimientosCajaForm)form;
				String estado=forma.getEstado();

				Log4JManager.info("Estado en Movimiento Action"+estado);

				UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				if(estado.equals("empezarMovimiento")){

					return mapping.findForward(ComunMovimientosCaja.accionEmpezar(forma, request,usuario.getCodigoInstitucionInt(),usuario.getCodigoCentroAtencion()));

				}else if(estado.equals("selectCajero")){

					String forward =  ComunMovimientosCaja.accionSeleccionaCajero(forma, usuario, errores, true);

					if(!errores.isEmpty())
					{
						saveErrors(request, errores);
					}

					return mapping.findForward(forward);

				}else if(estado.equals("selectMovimiento")){

					return mapping.findForward(ComunMovimientosCaja.accionSeleccionaMovimiento(forma, usuario));

				}else if(estado.equals("selectCaja")){

					String forward =  ComunMovimientosCaja.accionSeleccionaCaja(forma, usuario, errores);

					if(!errores.isEmpty())
					{
						saveErrors(request, errores);
					}

					return mapping.findForward(forward);

				}else if(estado.equals("redireccion")){

					ComunMovimientosCaja.asignarTipoFuncionalidad(forma);

					if(forma.getTipoFuncionalidad()==ConstantesBD.codigoFuncionalidadTipoArqueoCaja){

						request.getSession().setAttribute("ArqueosCajaForm", new ArqueosCajaForm(forma));

						return mapping.findForward("arqueo");

					}else if(forma.getTipoFuncionalidad()==ConstantesBD.codigoFuncionalidadTipoArqueoEntregaParcial){

						request.getSession().setAttribute("ArqueoEntregaParcialForm", new ArqueoEntregaParcialForm(forma));

						return mapping.findForward("arqueoEntregaParcial");

					}else if (forma.getTipoFuncionalidad()==ConstantesBD.codigoFuncionalidadTipoArqueoCierreTurno){

						request.getSession().setAttribute("CierreTurnoCajaForm", new CierreTurnoCajaForm(forma));

						return mapping.findForward("cierre");
					}

				}else if(estado.equals("generarArqueo")){

					MovimientosCaja movimiento=ComunMovimientosCaja.generarArqueo(forma, usuario, errores);
					ComunMovimientosCaja.asignarTipoFuncionalidad(forma);

					if(!errores.isEmpty())
					{
						saveErrors(request, errores);
					}

					if(movimiento!=null){

						return direccionarPorTipoArqueo(mapping, forma, request, response, forma, movimiento);

					}else{

						return mapping.findForward("principal");
					}

				}else if(estado.equals("volverPrincipalArqueo")){

					return mapping.findForward("volverPrincipalArqueo");

				}else{

					return direccionarPorTipoArqueo(mapping, forma, request, response, forma, forma.getConsolidadoMovimientoDTO().getMovimientosCaja());

				}
			}
		} catch (Exception e) {
			Log4JManager.error("Error Arqueos caja : " + e);
			HibernateUtil.abortTransaction();
			return mapping.findForward("paginaError");
		}
		return mapping.findForward("principal");
	}
	
	/**
	 * M&eacute;todo que determina si el rol del usuario que ha iniciado sesi&oacute;n en el sistema
	 * esta habilitado para ingresar a la funcionalidad requerida. Si tiene los permisos necesarios, 
	 * se le permite continuar con el proceso.
	 * 
	 * @param usuario
	 * @param tipoFuncionalidad
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param forma
	 * @param movimientosCaja
	 *
	 * @return ActionForward
	 */
	private ActionForward direccionarPorTipoArqueo(ActionMapping mapping, ActionForm form,	
			HttpServletRequest request, HttpServletResponse response, MovimientosCajaForm forma, MovimientosCaja movimientosCaja){
			
		if(forma.getTipoFuncionalidad()==ConstantesBD.codigoFuncionalidadTipoArqueoCaja){
			
			return new ArqueosCaja().execute(mapping, form, request, response, movimientosCaja);
		
		}else if(forma.getTipoFuncionalidad()==ConstantesBD.codigoFuncionalidadTipoArqueoEntregaParcial){
			
			return new ArqueoEntregaParcial().execute(mapping, form, request, response, movimientosCaja);
			
		}else if (forma.getTipoFuncionalidad()==ConstantesBD.codigoFuncionalidadTipoArqueoCierreTurno){
			
			return new CierreTurnoCaja().execute(mapping, form, request, response, movimientosCaja);
		}
		
		return mapping.findForward("busqueda");
			
	}
}