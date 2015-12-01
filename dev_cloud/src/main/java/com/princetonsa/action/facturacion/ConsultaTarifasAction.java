	package com.princetonsa.action.facturacion;

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

import util.ConstantesBD;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.facturacion.ConsultaTarifasForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.facturacion.ConsultaTarifas;
import com.princetonsa.pdf.ConsultaTarifasPdf;
import com.princetonsa.pdf.ConsultaTarifasServiciosPdf;



public class ConsultaTarifasAction extends Action
{
	
	
	/**
	 * 
	 */
	Logger logger =Logger.getLogger(PaquetesConvenioAction.class);
	
	
	/**
	 * 
	 */
	private String[] indicesServicio={
										"fechaasignacion_",
										"tipoliquidacion_",
										"valortarifa_",
										"usuarioasigna_",
										"tipocambio_",
										"esquematarifario_",
										"tiporedondeo_",
										"servicio_",
										"liquidarasocios_",
										"descripcion_",
										"tarifabase_"
									 };
	
	
	/**
	 * 
	 */
	private String[] indicesArticulo={
										"fechaasignacion_",
										"tipotarifa_",
										"porcentaje_",
										"valortarifa_",
										"usuarioasigna_",
										"tipocambio_",
										"esquematarifario_",
										"tiporedondeo_",
										"articulo_",
										"tarifabase_"
									 };
	
	
	private String[] indicesBusqueda={
										"codigo_",
										"codigoservicio_",
										"descripcion_",
										"especialidad_",
										"tiposervicio_",
										"grupo_",
										"tarifabase_"
									 };
	
	
	private String[] indicesBusquedaArticulos={
												"codigoarticulo_",
												"descripcionarticulo_",
												"clase_",
												"grupoarticulo_",
												"subgrupo_",
												"naturaleza_",
												"tarifabase_"
											 };
	
	
	
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
			if (form instanceof ConsultaTarifasForm) 
			{
				ConsultaTarifasForm forma=(ConsultaTarifasForm) form;

				String estado=forma.getEstado();

				logger.info("Estado -->"+estado);

				con=UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());

				ConsultaTarifas mundo=new ConsultaTarifas();
				//se obtiene la institucion
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");


				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConceptosCarteraAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					forma.reset();
					//forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())+""));
					forma.setConvenios(Utilidades.obtenerConvenios(con, "","",false,"",false,true));

					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("nuevaBusqueda"))
				{
					//forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())+""));
					forma.setConvenios(Utilidades.obtenerConvenios(con, "","",false,"",false,true));

					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("cargarContratos"))
				{
					forma.setMostrarInfo(false);
					forma.setContrato(ConstantesBD.codigoNuncaValido);
					forma.setContratos(Utilidades.obtenerContratos(con,forma.getCodigoConvenio(),true,false));
					HashMap<String, Object> mapa=new HashMap<String, Object>();
					mapa.put("numRegistros", "0");
					forma.setEsquemasVigencias(mapa);
					forma.setEsquemasTarifarios(ConstantesBD.codigoNuncaValido+"");

					if(forma.getContratos().size()==1)
					{
						forma.setContrato(Integer.parseInt(((HashMap)(forma.getContratos().get(0))).get("codigo")+""));
						forma.setEsquemasVigencias(mundo.consultarEsquemasVigenciasContrato(con,forma.getContrato(),(Utilidades.convertirAEntero(forma.getTiposTarifarios())==1)));
					}

					forma.setConsultaPorServicio("");

					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("buscar"))
				{
					int esquemaTarifario=Utilidades.convertirAEntero(forma.getEsquemasVigencias().get("esquematarifario_"+forma.getEsquemasTarifarios())+"");
					forma.setResultados(mundo.buscar(con, llenarMundoBusqueda(forma,mundo),esquemaTarifario));
					forma.setNombreEsquemaTarifario("Convenio: "+Utilidades.obtenerNombreConvenioOriginal(con, forma.getCodigoConvenio())+" - Contrato: "+Contrato.obtenerNumeroContrato(con, forma.getContrato())+" - Esquema Tarifario/Vigencia: "+forma.getEsquemasVigencias().get("nomesquematarifario_"+forma.getEsquemasTarifarios())+" - "+forma.getEsquemasVigencias().get("fechavigencia_"+forma.getEsquemasTarifarios()));
					forma.setTarifarioOficial(Utilidades.getTarifarioOficial(con, esquemaTarifario));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("resultadosBusqueda");
				}
				else if(estado.equals("voverResultadoBusquedaServicios"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("resultadosBusqueda");
				}
				else if(estado.equals("buscarArticulo"))
				{
					Contrato.obtenerNumeroContrato(con, forma.getContrato());

					int esquemaTarifario=Utilidades.convertirAEntero(forma.getEsquemasVigencias().get("esquematarifario_"+forma.getEsquemasTarifarios())+"");
					forma.setArticulos(mundo.buscarArticulo(con, llenarMundoBusquedaArticulo(forma,mundo),esquemaTarifario));
					forma.setNombreEsquemaTarifario("Convenio: "+Utilidades.obtenerNombreConvenioOriginal(con, forma.getCodigoConvenio())+" - Contrato: "+Contrato.obtenerNumeroContrato(con, forma.getContrato())+" - Esquema Tarifario/Vigencia: "+forma.getEsquemasVigencias().get("nomesquematarifario_"+forma.getEsquemasTarifarios())+" - "+forma.getEsquemasVigencias().get("fechavigencia_"+forma.getEsquemasTarifarios()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("resultadosBusquedaArticulo");
				}
				else if(estado.equals("buscarPorConvenio"))
				{
					forma.setNombreEsquemaTarifario("Convenio: "+Utilidades.obtenerNombreConvenioOriginal(con, forma.getCodigoConvenio())+" - Contrato: "+Contrato.obtenerNumeroContrato(con, forma.getContrato())+"");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleConvenioContrato");
				}
				else if(estado.equals("consultaServicio"))
				{
					int esquemaTarifario=Utilidades.convertirAEntero(forma.getEsquemasVigencias().get("esquematarifario_"+forma.getEsquemasTarifarios())+"");
					forma.setConsultaServiciosMap(mundo.consultaServicio(con, forma.getCodigoServicio(),forma.getTarifarioOficial(),esquemaTarifario));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleServicio");
				}
				else if(estado.equals("consultaArticulo"))
				{
					forma.setConsultaArticulosMap(mundo.consultaArticulo(con, forma.getCodigoArti()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleArticulo");
				}
				else if(estado.equals("consultaServicioTarifaFinal"))
				{
					int esquemaTarifario=Utilidades.convertirAEntero(forma.getEsquemasVigencias().get("esquematarifario_"+forma.getEsquemasTarifarios())+"");
					forma.setFechaVigencia(forma.getEsquemasVigencias().get("fechavigencia_"+forma.getEsquemasTarifarios())+"");
					int grupoServicio=Utilidades.convertirAEntero(forma.getEsquemasVigencias().get("gruposervicio_"+forma.getEsquemasTarifarios())+"");
					forma.setConsultaServiciosMap(mundo.consultaServicioTarifaFinal(con, forma.getCodigoServicio(),forma.getTarifarioOficial(),esquemaTarifario,forma.getCodigoConvenio(),forma.getContrato(),usuario.getCodigoInstitucionInt(),forma.getFechaVigencia(),grupoServicio));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleServicioTarifaFinal");
				}
				else if(estado.equals("consultaArticuloTarifaFinal"))
				{
					Utilidades.imprimirMapa(forma.getEsquemasVigencias());
					int claseInventario=Utilidades.convertirAEntero(forma.getEsquemasVigencias().get("claseinventario_"+forma.getEsquemasTarifarios())+"");
					int esquemaTarifario=Utilidades.convertirAEntero(forma.getEsquemasVigencias().get("esquematarifario_"+forma.getEsquemasTarifarios())+"");
					forma.setFechaVigencia(forma.getEsquemasVigencias().get("fechavigencia_"+forma.getEsquemasTarifarios())+"");
					forma.setConsultaArticulosMap(mundo.consultaArticulosTarifaFinal(con, forma.getCodigoArti(),esquemaTarifario,forma.getCodigoConvenio(),forma.getContrato(),usuario.getCodigoInstitucionInt(),forma.getFechaVigencia(),claseInventario));


					if (!forma.getConsultaArticulosMap().containsKey("tiporedondeo"))
						forma.setConsultaArticulosMap("tiporedondeo", "");
					if (!forma.getConsultaArticulosMap().containsKey("articulo"))
						forma.setConsultaArticulosMap("articulo", Utilidades.obtenerNombreArticulo(con, forma.getCodigoArti()));


					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleArticuloTarifaFinal");
				}
				else if(estado.equals("consultaEsquema"))
				{
					forma.setEstado("empezar");
					HashMap<String, Object> mapa=new HashMap<String, Object>();
					mapa.put("numRegistros", "0");
					forma.setEsquemasVigencias(mapa);
					forma.setEsquemasTarifarios(ConstantesBD.codigoNuncaValido+"");
					forma.setCodigoConvenio(ConstantesBD.codigoNuncaValido);
					forma.setContrato(ConstantesBD.codigoNuncaValido);
					forma.setMostrarInfo(false);

					forma.setConsultaPorServicio("");

					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("cargarInfo"))
				{
					forma.setMostrarInfo(true);
					forma.setEstado("empezar");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("cargarEsquemasTarifarios"))
				{
					forma.setEsquemasVigencias(mundo.consultarEsquemasVigenciasContrato(con,forma.getContrato(),(Utilidades.convertirAEntero(forma.getTiposTarifarios())==1)));
					forma.setEstado("empezar");

					forma.setMostrarInfo(false);
					forma.setConsultaPorServicio("");

					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("ordernar"))
				{
					this.accionOrdenar(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleServicio");
				}
				else if(estado.equals("ordernarArticulo"))
				{
					this.accionOrdenarArticulo(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleArticulo");
				}
				else if(estado.equals("ordernarServicios"))
				{
					this.accionOrdenarServicios(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("resultadosBusqueda");
				}
				else if(estado.equals("ordenarBusqueda"))
				{
					this.accionOrdenarBusqueda(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("resultadosBusquedaArticulo");
				}
				else if(estado.equals("continuar"))
				{
					UtilidadBD.closeConnection(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getResultados("numRegistros").toString()), response, request, "resultadosBusqueda.jsp",true);
				}
				else if(estado.equals("continuarArticulo"))
				{
					UtilidadBD.closeConnection(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getResultados("numRegistros").toString()), response, request, "resultadosBusquedaArticulo.jsp",true);
				}
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(con);
					if(UtilidadTexto.isEmpty(forma.getLinkSiguiente()))
						forma.setLinkSiguiente("../consultaCambiosTarifas/consultaTarifas.do?estado=continuar");
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if (estado.equals("redireccionArticulo"))
				{
					UtilidadBD.closeConnection(con);
					if(UtilidadTexto.isEmpty(forma.getLinkSiguiente()))
						forma.setLinkSiguiente("../consultaCambiosTarifas/consultaTarifas.do?estado=continuarArticulo");
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				/*
				 * Estado para imprimir articulos el reporte paquetes inventario
				 */	
				else if(estado.equals("imprimirArticulos"))
				{

					UtilidadBD.closeConnection(con);
					///se edita nombre del archivo PDF
					String nombreArchivo;
					Random r=new Random();
					HashMap<String, Object> mapa = new HashMap<String, Object>();
					mapa = forma.getConsultaArticulosMap();
					logger.info("Aquí voy a imprimir el mapa");
					//Utilidades.imprimirMapa(mapa);
					int esquemaTarifario=Utilidades.convertirAEntero(forma.getEsquemasVigencias().get("esquematarifario_"+forma.getEsquemasTarifarios())+"");
					nombreArchivo="/consultaTarifas" + r.nextInt()  +".pdf";
					ConsultaTarifasPdf.pdfConsultaTarifas(ValoresPorDefecto.getFilePath()+nombreArchivo, forma, mapa, usuario, request);
					request.setAttribute("nombreArchivo", nombreArchivo);
					request.setAttribute("nombreVentana", "Consulta Tarifas");
					return mapping.findForward("abrirPdf");
				}
				else if(estado.equals("imprimirServicios"))
				{

					UtilidadBD.closeConnection(con);
					///se edita nombre del archivo PDF
					String nombreArchivo;
					Random r=new Random();
					HashMap<String, Object> mapa = new HashMap<String, Object>();
					mapa = forma.getConsultaServiciosMap();
					logger.info("Aqui voy a imprimir el mapa");
					//Utilidades.imprimirMapa(mapa);
					int esquemaTarifario=Utilidades.convertirAEntero(forma.getEsquemasVigencias().get("esquematarifario_"+forma.getEsquemasTarifarios())+"");
					nombreArchivo="/consultaTarifasServicios" + r.nextInt()  +".pdf";
					ConsultaTarifasServiciosPdf.pdfConsultaTarifasServicios(ValoresPorDefecto.getFilePath()+nombreArchivo, forma, mapa, usuario, request);
					request.setAttribute("nombreArchivo", nombreArchivo);
					request.setAttribute("nombreVentana", "Consulta Tarifas");
					return mapping.findForward("abrirPdf");
				}
				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de PAQUETES ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de PaquetesForm");
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
	 * @param forma
	 * @param mundo
	 * @return
	 */
	private boolean llenarMundoBusqueda(ConsultaTarifasForm forma,ConsultaTarifas mundo)
	{
		
		mundo.setCodigo("");
		if(!forma.getCodigo().equals(""))
		{
			mundo.setCodigo(forma.getCodigo());
		}
		mundo.setDescripcion("");
		if(!forma.getDescripcion().equals(""))
		{
			mundo.setDescripcion(forma.getDescripcion());
		}
		mundo.setEspecialidad("");
		if(!forma.getEspecialidad().equals(""))
		{
			mundo.setEspecialidad(forma.getEspecialidad());
		}
		mundo.setTipoServicio("");
		if(!forma.getTipoServicio().trim().equals(""))
		{
			mundo.setTipoServicio(forma.getTipoServicio());
		}
		mundo.setGrupo("");
		if(!forma.getGrupo().trim().equals(""))
		{
			mundo.setGrupo(forma.getGrupo());
		}
		return false;
	}
	
	
	/**
	 * 
	 */
	private boolean llenarMundoBusquedaArticulo(ConsultaTarifasForm forma,ConsultaTarifas mundo)
	{
		
		mundo.setCodigoArticulo("");
		if(!forma.getCodigoArticulo().equals(""))
		{
			mundo.setCodigoArticulo(forma.getCodigoArticulo());
		}
		mundo.setDescripcionArticulo("");
		if(!forma.getDescripcionArticulo().equals(""))
		{
			mundo.setDescripcionArticulo(forma.getDescripcionArticulo());
		}
		mundo.setClase("");
		if(!forma.getClase().equals(""))
		{
			mundo.setClase(forma.getClase());
		}
		mundo.setGrupoArticulo("");
		if(!forma.getGrupoArticulo().trim().equals(""))
		{
			mundo.setGrupoArticulo(forma.getGrupoArticulo());
		}
		mundo.setSubgrupo("");
		if(!forma.getSubgrupo().trim().equals(""))
		{
			mundo.setSubgrupo(forma.getSubgrupo());
		}
		mundo.setNaturaleza("");
		if(!forma.getNaturaleza().trim().equals(""))
		{
			mundo.setNaturaleza(forma.getNaturaleza());
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenar(ConsultaTarifasForm forma) 
	{
		int numReg=Utilidades.convertirAEntero(forma.getConsultaServiciosMap("numRegistros")+"");
		forma.setConsultaServiciosMap(Listado.ordenarMapa(indicesServicio,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getConsultaServiciosMap(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setConsultaServiciosMap("numRegistros",numReg+"");
	}
	
	
	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenarArticulo(ConsultaTarifasForm forma) 
	{
		int numReg=Utilidades.convertirAEntero(forma.getConsultaArticulosMap("numRegistros")+"");
		forma.setConsultaArticulosMap(Listado.ordenarMapa(indicesArticulo,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getConsultaArticulosMap(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setConsultaArticulosMap("numRegistros",numReg+"");
	}
	
	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenarServicios(ConsultaTarifasForm forma) 
	{
		int numReg=Utilidades.convertirAEntero(forma.getResultados("numRegistros")+"");
		forma.setResultados(Listado.ordenarMapa(indicesBusqueda,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getResultados(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setResultados("numRegistros",numReg+"");
	}
	
	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenarBusqueda(ConsultaTarifasForm forma) 
	{
		int numReg=Utilidades.convertirAEntero(forma.getArticulos("numRegistros")+"");
		forma.setArticulos(Listado.ordenarMapa(indicesBusquedaArticulos,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getArticulos(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setArticulos("numRegistros",numReg+"");
	}
	
	
	/*private void accionConsultarServicios(Connection con, ConsultaTarifasForm forma, ConsultaTarifas mundo)
	{
		HashMap vo=new HashMap();
		vo.put("codigo", forma.getResultados("codigo_"+forma.getIndexSeleccionado()));
		mundo.consultaServicio(con,vo);
		forma.setConsultaServiciosMap((HashMap)mundo.getConsultaServiciosMap().clone());
	}*/
	

}
