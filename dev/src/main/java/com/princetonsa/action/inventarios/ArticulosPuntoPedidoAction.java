/*
 * Created on 2/12/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package com.princetonsa.action.inventarios;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.Listado;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.inventarios.ArticulosPuntoPedidoForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.ArticulosPuntoPedido;
import com.princetonsa.pdf.ArticulosPuntoPedidoPdf;

/**
 * @version 1.0, 2/12/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class ArticulosPuntoPedidoAction extends Action
{

    /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */ 
	private Logger logger = Logger.getLogger(ArticulosPuntoPedidoAction.class);
	
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

			if(form instanceof ArticulosPuntoPedidoForm)
			{


				//intentamos abrir una conexion con la fuente de datos 
				con = UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				ArticulosPuntoPedidoForm forma=(ArticulosPuntoPedidoForm)form;
				ArticulosPuntoPedido mundo=new ArticulosPuntoPedido();
				String estado = forma.getEstado();
				logger.warn("Estado-->"+estado);
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConceptoTesoreriaAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				if(estado.equals("empezar"))
				{
					forma.reset();
					ValoresPorDefecto.cargarValoresIniciales(con);
					forma.setPorcentajeAleta(ValoresPorDefecto.getPorcentajePuntoPedido(usuario.getCodigoInstitucionInt()));
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					mundo.consultarArticulosGeneral(con,forma.getPorcentajeAleta(),usuario.getCodigoInstitucionInt());
					forma.setArticulos((HashMap)mundo.getArticulos().clone());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");
				}
				if(estado.equals("buquedaAvanzada"))
				{
					forma.getArticulos().clear();
					mundo.consultarArticulosAvanzada(con,forma.getPorcentajeAleta(),forma.getCodBusqueda(),forma.getDesBusqueda(),usuario.getCodigoInstitucionInt());
					forma.setArticulos((HashMap)mundo.getArticulos().clone());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");

				}
				if(estado.equals("ordenar"))
				{
					this.accionOrdenar(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");
				}
				if(estado.equals("imprimir"))
				{
					String nombreArchivo;
					Random r= new Random();

					nombreArchivo= "/aBorrar" + r.nextInt() + ".pdf";
					ArticulosPuntoPedidoPdf.imprimirArticulosPuntoPedido(ValoresPorDefecto.getFilePath()+ nombreArchivo, forma.getArticulos(), usuario,forma.getPorcentajeAleta(),forma.getCodBusqueda(),forma.getDesBusqueda(), request);			

					UtilidadBD.closeConnection(con);
					request.setAttribute("nombreArchivo", nombreArchivo);
					request.setAttribute("nombreVentana", "Consulta Facturas");
					return mapping.findForward("abrirPdf");
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
				logger.error("El form no es compatible con el form de ConceptoTesoreriaForm");
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
     * @param forma
     */
    private void accionOrdenar(ArticulosPuntoPedidoForm forma)
    {
        String[] indices={
        		"codigo_",
        		"descripcion_",
        		"concentracion_",
        		"unidadmedida_",
        		"nomunidadmedida_",
        		"stockminimo_",
        		"stockmaximo_",
        		"puntopedido_",
        		"existencias_",
        		"menorpuntopedido_",
        		"detalle_"
        		};
        int numReg=Integer.parseInt(forma.getArticulos("numRegistros")+"");
		forma.setArticulos(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getArticulos(),numReg));
		forma.setArticulos("numRegistros", numReg+"");
		forma.setUltimoPatron(forma.getPatronOrdenar());
    }
}
