/**
 * 
 */
package com.princetonsa.action.inventarios;

import java.sql.Connection;
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
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.inventarios.ConstantesBDInventarios;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.actionform.inventarios.GenerarTransaccionesESSaldosInventariosForm;
import com.princetonsa.mundo.ConsecutivosDisponibles;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author axioma
 *
 */
public class GenerarTransaccionesESSaldosInventariosAction extends Action 
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */ 
	private Logger logger = Logger.getLogger(GenerarTransaccionesESSaldosInventariosAction.class);
	
	/**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
							        ActionForm form, 
							        HttpServletRequest request, 
							        HttpServletResponse response) throws Exception
							        {

		Connection con = null;
		try{

			if(form instanceof GenerarTransaccionesESSaldosInventariosForm)
			{

				con = UtilidadBD.abrirConexion();

				GenerarTransaccionesESSaldosInventariosForm forma=(GenerarTransaccionesESSaldosInventariosForm)form;

				String estado = forma.getEstado();
				logger.warn("Estado-->"+estado);
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());

				if(estado.equals("empezar"))
				{
					forma.reset();
					forma.setTransaccionCC(Utilidades.obtenerTransaccionesCentroCosto(con,usuario.getCodigoInstitucionInt()));
					forma.setClasesInventario(UtilidadInventarios.obtenerClasesInventario(con,usuario.getCodigoInstitucionInt()));
					forma.setGruposInventario(UtilidadInventarios.obtenerGrupoInventario(con,usuario.getCodigoInstitucionInt()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}			
				else if(estado.equals("generar"))
				{
					return this.accionGenerarTransaccionES(con,forma,usuario,request,mapping);
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de GenerarTransaccionesESSaldosInventariosForm");
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
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param mapping 
	 * @param request 
	 */
	private ActionForward accionGenerarTransaccionES(Connection con, GenerarTransaccionesESSaldosInventariosForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
	{
		int codTranEntrada=ConstantesBD.codigoNuncaValido;
		int codTranSalida=ConstantesBD.codigoNuncaValido;
		
		//////TRANSACCION ENTRADA
		String tipoConsecutivo=ValoresPorDefecto.getManejoConsecutivoTransInv(usuario.getCodigoInstitucionInt());
        ConsecutivosDisponibles consec=new ConsecutivosDisponibles();
		/////ABRO ESTA TRANSACCION SOLO PARA BLOQUEAR LOS CONSECUTIVOS DE INVENTARIOS
        UtilidadBD.iniciarTransaccion(con);
        int codigoAlmacen=0;
        if(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosUnicoSistema))
            codigoAlmacen=ConstantesBD.codigoNuncaValido;        
        else if(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosPorAlmacen))
            codigoAlmacen=Integer.parseInt(forma.getAlmacen()); 
        
        String consecutivo=consec.obtenerConsecutivoInventario(con,Integer.parseInt(forma.getTransaccionEntrada()),codigoAlmacen,usuario.getCodigoInstitucionInt())+"";
        if(Integer.parseInt(consecutivo+"")==ConstantesBD.codigoNuncaValido)
        {
            ActionErrors errores=new ActionErrors();
	        errores.add("falta consecutivo", new ActionMessage("error.inventarios.faltaDefinirConsecutivo"));
	        saveErrors(request, errores);
	        forma.setEstado("error");
	        UtilidadBD.closeConnection(con);
	        return mapping.findForward("principal");      
        }
        else     
        {           
        	codTranEntrada=accionGuardarInformacion(con,forma,consecutivo,ConstantesBD.codigoTipoConceptoEntradaInv,forma.getTransaccionEntrada(),usuario,mapping,request);
        	forma.setConsecutivoTranEnt(consecutivo);
            consec.actualizarValorConsecutivoInventarios(con,Integer.parseInt(forma.getTransaccionEntrada()),codigoAlmacen,usuario.getCodigoInstitucionInt());
            
        }
        
        
        ////// TRANSACCION Salida
		/////ABRO ESTA TRANSACCION SOLO PARA BLOQUEAR LOS CONSECUTIVOS DE INVENTARIOS
         
        consecutivo=consec.obtenerConsecutivoInventario(con,Integer.parseInt(forma.getTransaccionSalida()),codigoAlmacen,usuario.getCodigoInstitucionInt())+"";
        if(Integer.parseInt(consecutivo+"")==ConstantesBD.codigoNuncaValido)
        {
            ActionErrors errores=new ActionErrors();
	        errores.add("falta consecutivo", new ActionMessage("error.inventarios.faltaDefinirConsecutivo"));
	        saveErrors(request, errores);
	        forma.setEstado("error");
	        UtilidadBD.closeConnection(con);
	        return mapping.findForward("principal");      
        }
        else     
        {           
        	codTranSalida=accionGuardarInformacion(con,forma,consecutivo,ConstantesBD.codigoTipoConceptoSalidaInv,forma.getTransaccionSalida(),usuario,mapping,request);
        	forma.setConsecutivoTranSal(consecutivo);
            consec.actualizarValorConsecutivoInventarios(con,Integer.parseInt(forma.getTransaccionSalida()),codigoAlmacen,usuario.getCodigoInstitucionInt());
            
        }
        if(codTranEntrada>0 && codTranSalida>0)
        {
        	Utilidades.generarLogTransSaldosInventario(con,usuario.getLoginUsuario(),forma.getAlmacen(),forma.getTransaccionEntrada(),forma.getTransaccionSalida(),forma.getObservaciones());
        	UtilidadBD.finalizarTransaccion(con);
        }
        else
        {
        	UtilidadBD.abortarTransaccion(con);
        }
		
        UtilidadBD.closeConnection(con);
        return mapping.findForward("resumen");      
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param consecutivo
	 * @param codigoTipoConceptoEntradaInv
	 * @param usuario
	 * @param mapping
	 * @param request
	 */
	private int accionGuardarInformacion(Connection con, GenerarTransaccionesESSaldosInventariosForm forma, String consecutivo, int codigoTipoConcepto,String transaccion, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) 
	{
        int codTransaccion1=UtilidadInventarios.generarEncabezadoTransaccion(con,Integer.parseInt(consecutivo),Integer.parseInt(transaccion),UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),usuario.getLoginUsuario(),ConstantesBD.codigoNuncaValido,forma.getObservaciones(),ConstantesBDInventarios.codigoEstadoTransaccionInventarioPendiente,Integer.parseInt(forma.getAlmacen()),false);
        HashMap<String, Object> articulos=Utilidades.obtenerArticulosCantidadPosNeg(con,forma.getAlmacen(),forma.getClaseInventario(),forma.getGrupoInventario(),!(codigoTipoConcepto==ConstantesBD.codigoTipoConceptoEntradaInv));
        for(int i=0;i<Integer.parseInt(articulos.get("numRegistros")+"");i++)
        {
        	double valorUnitario=UtilidadInventarios.costoActualArticulo(con,Integer.parseInt(articulos.get("articulo_"+i)+""));
        	//NO MANEJA PROVEEDODR DE COMPRA NI CATALOGO, ENTONCES SE ENVIA EN BLANCO.
        	UtilidadInventarios.insertarDetalleTransaccion(con,codTransaccion1,Integer.parseInt(articulos.get("articulo_"+i)+""),Integer.parseInt(articulos.get("cantidad_"+i)+""),valorUnitario+"","","","","");
        }
        return codTransaccion1;
	}
}
