package com.princetonsa.action.inventarios;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.interfaces.UtilidadInsertarAxArt;

import com.princetonsa.actionform.inventarios.ArticuloCatalogoForm;
import com.princetonsa.dto.interfaz.DtoInterfazAxArt;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.ArticuloCatalogo;

public class ArticuloCatalogoAction extends Action {
	
	Logger logger=Logger.getLogger(ArticuloCatalogoAction.class);
	
	String[] indices={"tiporegistro_","proveedor_","descripcion_","ref_proveedor_","val_uni_compra_","val_uni_iva_","fecha_ini_vigencia_","fecha_fin_vigencia_","codigo_axioma_"};
	
	/**
	 * Objeto articuloCatalogo con el que se trabaja dentro de
	 * esta clase (ahorro de memoria)
	 */
	private ArticuloCatalogo articuloCatalogo=new ArticuloCatalogo();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{

		Connection con=null;
		try{
			if(form instanceof ArticuloCatalogoForm)
			{
				ArticuloCatalogoForm forma=(ArticuloCatalogoForm) form;
				String estado=forma.getEstado();
				logger.info("Estado-->> "+estado);
				con=UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
				ArticuloCatalogo mundo=new ArticuloCatalogo();
				forma.setMensaje(new ResultadoBoolean(false));
				forma.setAlerta(new ResultadoBoolean(false));
				forma.setMensajeVigencia("");
				if (estado==null)
				{
					logger.warn("Estado no valido dentro del flujo de Articulos de Catalogo (null)");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					forma.reset();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("busqueda");

				}
				else if(estado.equals("continuar"))
				{
					UtilidadBD.closeConnection(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(mundo.getArticuloCatalogoMap("numRegistros").toString()), response, request, "articuloCatalogo.jsp",true);
				}
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(con);
					if(UtilidadTexto.isEmpty(forma.getLinkSiguiente()))
						forma.setLinkSiguiente("../articuloCatalogo/articuloCatalogo.do?estado=continuar");
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("buscar"))
				{
					forma.setResultados(articuloCatalogo.buscar(con, llenarMundoBusqueda(forma,articuloCatalogo)));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("resultadosBusqueda");
				}
				else if(estado.equals("consultar"))
				{
					forma.reset();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("busqueda");
				}
				else if(estado.equals("crearArticulo"))
				{
					int acronimo=forma.getAcronimo();
					return this.accionCrearArticulo(con,mundo,forma,usuario,mapping,acronimo);
				}
				else if(estado.equals("guardarArticulo"))
				{
					return this.accionGuardarArticulo(con,forma,mundo,usuario,mapping);
				}
				else if(estado.equals("ordenar"))
				{
					this.accionOrdenarMapa(mundo,forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("resultadosBusqueda");
				}
				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de Articulos de Catalogo");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de Articulo de Catalogo");
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
	
	private boolean llenarMundoBusqueda(ArticuloCatalogoForm forma,ArticuloCatalogo mundo)
	{
		mundo.setAcronimo(ConstantesBD.codigoNuncaValido);
		if(forma.getAcronimo()!=-1)
		{
			mundo.setAcronimo(forma.getAcronimo());
		}
		mundo.setProveedor("");
		if(!forma.getProveedor().equals(""))
		{
			mundo.setProveedor(forma.getProveedor());
		}
		mundo.setDescripcion("");
		if(!forma.getDescripcion().equals(""))
		{
			mundo.setDescripcion(forma.getDescripcion());
		}
		mundo.setRef_proveedor("");
		if(!forma.getRef_proveedor().equals(""))
		{
			mundo.setRef_proveedor(forma.getRef_proveedor());
		}
		mundo.setCodigoAxioma("");
		if(!forma.getCodigo().trim().equals(""))
		{
			mundo.setCodigoAxioma(forma.getCodigo());
		}
		return false;
	}
	
	private ActionForward accionCrearArticulo(Connection con, ArticuloCatalogo mundo, ArticuloCatalogoForm forma,UsuarioBasico usuario,ActionMapping mapping,int acronimo)
	{
		
		mundo.consultarArticuloCatalogo(con,acronimo);
		int numDias = UtilidadFecha.numeroDiasEntreFechas(UtilidadFecha.getFechaActual(con), UtilidadFecha.conversionFormatoFechaAAp(mundo.getArticuloCatalogoMap().get("fecha_fin_vigencia").toString()));
		
		if(Utilidades.convertirAEntero(mundo.getArticuloCatalogoMap().get("numRegistros")+"")>0)
		{
			forma.setProveedor(mundo.getArticuloCatalogoMap().get("proveedor")+"");
			forma.setDescripcion(mundo.getArticuloCatalogoMap().get("descripcion")+"");
			forma.setRef_proveedor(mundo.getArticuloCatalogoMap().get("ref_proveedor")+"");
			forma.setVal_uni_compra(Double.parseDouble(mundo.getArticuloCatalogoMap().get("val_uni_compra")+""));
			forma.setVal_uni_iva(Double.parseDouble(mundo.getArticuloCatalogoMap().get("val_uni_iva")+""));
			forma.setFecha_ini_vigencia(mundo.getArticuloCatalogoMap().get("fecha_ini_vigencia")+"");
			forma.setFecha_fin_vigencia(mundo.getArticuloCatalogoMap().get("fecha_fin_vigencia")+"");
			forma.setUnidadMedida(mundo.getArticuloCatalogoMap().get("unidad_medida")+"");
			forma.setClase(mundo.getArticuloCatalogoMap().get("clase_inventario")+"");
			forma.setGrupo(mundo.getArticuloCatalogoMap().get("grupo_inventario")+"");
			forma.setSubgrupo(mundo.getArticuloCatalogoMap().get("subgrupo_inventario")+"");
			forma.setNaturaleza(mundo.getArticuloCatalogoMap().get("naturaleza_articulo")+"");
			forma.setCodigo(mundo.getArticuloCatalogoMap().get("codigo_axioma")+"");
			forma.setAcronimo(Integer.parseInt(mundo.getArticuloCatalogoMap().get("acronimo")+""));
			forma.setEstado(mundo.getArticuloCatalogoMap().get("estado")+"");
			forma.setTarifaInventarioMap(mundo.consultarTarifasArticulos(con,forma.getCodigo()));
		}
		
		if(!ValoresPorDefecto.getDiasAlertaVigencia(usuario.getCodigoInstitucionInt()).equals(""))
		{
			if(numDias <= Integer.parseInt(ValoresPorDefecto.getDiasAlertaVigencia(usuario.getCodigoInstitucionInt())))
			{
				forma.setMensajeVigencia("LA VIGENCIA DEL ARTICULO "+forma.getDescripcion()+" EN EL CATALOGO PROVEEDOR HA CADUCADO");
			}
			else
			{
				forma.setMensajeVigencia("");
			}
		}
		else
		{
			if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(mundo.getArticuloCatalogoMap().get("fecha_fin_vigencia").toString()), UtilidadFecha.getFechaActual(con)))
			{
				forma.setMensajeVigencia("LA VIGENCIA DEL ARTICULO "+forma.getDescripcion()+" EN EL CATALOGO PROVEEDOR HA CADUCADO");
			}
			else
			{
				forma.setMensajeVigencia("");
			}
		}
		
		return mapping.findForward("nuevoArticulo");
	}
	
	/**
	 * Accion Guardar Articulo de Catalogo en Articulo
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private ActionForward accionGuardarArticulo(Connection con, ArticuloCatalogoForm forma, ArticuloCatalogo mundo, UsuarioBasico usuario, ActionMapping mapping)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		HashMap vo= new HashMap();
		double precio_ultima_compra=((forma.getVal_uni_compra())+(forma.getVal_uni_iva()));
		double precio_compra_mas_alta=((forma.getVal_uni_compra())+(forma.getVal_uni_iva()));

		logger.info("<<<<<<<<<<<<<<<<<");
		logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		logger.info("Naturaleza: " + forma.getNaturaleza());
		logger.info("Tipo Inv: " + forma.getTipoInventario());
		
		vo.put("subgrupo", forma.getSubgrupo());
		vo.put("descripcion", forma.getDescripcion());
		vo.put("naturaleza", forma.getNaturaleza());
		vo.put("minsalud", forma.getMinsalud());
		vo.put("forma_farmaceutica", forma.getFormaFarmaceutica());
		vo.put("concentracion", forma.getConcentracion());
		vo.put("unidad_medida", forma.getUnidadMedida());
		vo.put("fecha_creacion", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		vo.put("hora_creacion", UtilidadFecha.getHoraActual());
		vo.put("usuario_creacion", usuario.getLoginUsuario());
		vo.put("categoria", 1);
		vo.put("stock_minimo", forma.getStockMinimo());
		vo.put("stock_maximo", forma.getStockMaximo());
		vo.put("punto_pedido", forma.getPuntoPedido());
		vo.put("cantidad_compra", forma.getCantidadCompra());
		vo.put("costo_promedio", forma.getCostoPromedio());
		vo.put("registro_invima", forma.getRegistroInvima());
		vo.put("maxima_cantidad_mes", forma.getMaximaCantidadMes());
		vo.put("multidosis", forma.getMultidosis());
		vo.put("maneja_lotes", forma.getManejaLotes());
		vo.put("maneja_fecha_vencimiento", forma.getManejaFechaVencimiento());
		vo.put("porcentaje_iva", forma.getPorcentajeIva());
		vo.put("precio_ultima_compra", precio_ultima_compra);
		vo.put("precio_base_venta", forma.getPrecioBaseVenta());
		vo.put("fecha_precio_base_venta", null);
		vo.put("institucion", usuario.getCodigoInstitucionInt());
		vo.put("codigo_interfaz", null);
		vo.put("costo_donacion", forma.getCostoDonacion());
		vo.put("indicativo_automatico", forma.getIndicativoAutomatico());
		vo.put("indicativo_por_completar", forma.getIndicativoPorCompletar());
		vo.put("precio_compra_mas_alta", precio_compra_mas_alta);
		
		String diasAlerta=ValoresPorDefecto.getDiasAlertaVigencia(usuario.getCodigoInstitucionInt());
		int numDias=UtilidadFecha.numeroDiasEntreFechas(UtilidadFecha.conversionFormatoFechaAAp(forma.getFecha_fin_vigencia()),UtilidadFecha.getFechaActual());
		int codigoArticulo=mundo.insertar(con, vo);
		if(codigoArticulo>0)
		{
			///guardar los esquemas tarifarios
			forma.setTarifaInventarioMap("articulo",codigoArticulo+"");
			forma.setTarifaInventarioMap("valor_tarifa",precio_ultima_compra+"");
			forma.setTarifaInventarioMap("tipo_tarifa","PULCOM");
			forma.setTarifaInventarioMap("porcentaje",0);
			forma.setTarifaInventarioMap("porcentaje_iva",0);
			forma.setTarifaInventarioMap("actualiz_automatic",ConstantesBD.acronimoSi);
			forma.setTarifaInventarioMap("usuario_modifica",usuario.getLoginUsuario());
			forma.setTarifaInventarioMap("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			forma.setTarifaInventarioMap("hora_modifica",UtilidadFecha.getHoraActual());
			mundo.guardarEsquemasInventario(con,forma.getTarifaInventarioMap());
			
			// Solo aplica cuando la interfaz se encuentra activa
			if(UtilidadTexto.getBoolean(ValoresPorDefecto.getArticuloInventario(usuario.getCodigoInstitucionInt()))){
			
				///guardar en ax_art
				DtoInterfazAxArt dto=new DtoInterfazAxArt();
				dto.setCodaxi(forma.getTarifaInventarioMap("articulo")+"");
				dto.setDescri(forma.getDescripcion());
				dto.setTipcrea("A");
				
				//81450
				if(forma.getTipoInventario().equals(ConstantesBD.acronimoSi))
					dto.setTipinv(ConstantesBD.acronimoTipoInventarioMedicamento);
				else
					dto.setTipinv(ConstantesBD.acronimoTipoInventarioElemento);
	
				// Cambio Anexo 780 ---
				dto.setClase(Utilidades.consultarClaseInterfazArticulo(con, codigoArticulo+""));
				//---------------------
				
				dto.setEstreg("0");
				UtilidadInsertarAxArt insertarAxArt=new UtilidadInsertarAxArt();
				
				//--------------------------------------------------------------------------------------------------------------------
				//modificado por anexo 779
				ResultadoBoolean resultadoBoolean=insertarAxArt.insertar(dto,usuario.getCodigoInstitucionInt(),false);
				forma.setMensaje(new ResultadoBoolean(true, resultadoBoolean.getDescripcion()));
				//--------------------------------------------------------------------------------------------------------------------
			}
			
			
		}
		
		if(codigoArticulo>0)
		{
			int acronimo=forma.getAcronimo();
			mundo.modificarArticuloCatalogo(con, codigoArticulo,acronimo);
			
		}
		else
		{
			transaccion=false;
		}
		
		if(!diasAlerta.equals("")&&numDias>=Integer.parseInt(diasAlerta)||(UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(forma.getFecha_fin_vigencia()), UtilidadFecha.getFechaActual())))
		{
			forma.setAlerta(new ResultadoBoolean(true, "LA VIGENCIA DEL ARTICULO "+forma.getDescripcion()+" EN EL CATALOGO PROVEEDOR HA CADUCADO"));
		}
		else
		{
			forma.setAlerta(new ResultadoBoolean(false,""));
		}
		if(transaccion)
		{
			forma.setMensaje(new ResultadoBoolean(true, forma.getMensaje().getDescripcion()));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		forma.reset();
		forma.setEstado("empezar");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("busqueda");
	}
	
	/**
	 * Accion ordenar mapa
	 * */
	private void accionOrdenarMapa(ArticuloCatalogo mundo,ArticuloCatalogoForm forma)
	{
		try {
			forma.setResultados(Listado.ordenarColumna(new ArrayList(forma.getResultados()), forma.getUltimoPatron(), forma.getPatronOrdenar()));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		forma.setUltimoPatron(forma.getPatronOrdenar());
	}

}
