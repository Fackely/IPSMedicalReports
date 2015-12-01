/*
 * @(#)ArticulosAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.interfaces.UtilidadInsertarAxArt;

import com.princetonsa.actionform.ArticulosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.interfaz.DtoInterfazAxArt;
import com.princetonsa.mundo.Articulo;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.solicitudes.DocumentoAdjunto;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAtencionMundo;
import com.servinte.axioma.orm.NivelAtencion;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * @author rcancino
 * @version 1.0, Nov 28, 2003
 */
public class ArticulosAction extends Action {

	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 * de Articulos
	 */
	private Logger logger = Logger.getLogger(ArticulosAction.class);

	/**
	 * Objeto articulo con el que se trabaja dentro de
	 * esta clase (ahorro de memoria)
	 */
	private Articulo articulo=new Articulo();
	
	/**
	 * M茅todo encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response) throws SQLException
									{
		UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		Connection con = null;

		try {
			if (form instanceof ArticulosForm)
			{
				if( logger.isDebugEnabled() )
				{
					logger.debug("Entro al Action de Articulos");
				}

				ArticulosForm articulosForm=(ArticulosForm)form;
				String estado=articulosForm.getEstado();
				try
				{
					String tipoBD = System.getProperty("TIPOBD");
					DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
					con = myFactory.getConnection();
				}
				catch(Exception e)
				{
					e.printStackTrace();
					//No se cierra conexi贸n porque si llega aca ocurri贸 un
					//error al abrirla
					logger.error("Problemas abriendo la conexi贸n en ArticulosAction");
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				//Lo primero que vamos a hacer es validar que se
				//cumplan las condiciones.

				HttpSession session=request.getSession();		
				UsuarioBasico medico = (UsuarioBasico)session.getAttribute("usuarioBasico");



				//Primera Condici贸n: El usuario debe existir
				//la validaci贸n de si es m茅dico o no solo se hace en insertar
				if( medico == null )
				{
					if( logger.isDebugEnabled() )
					{
						logger.debug("No existe el usuario");
					}
					if (con!=null&&!con.isClosed())
					{
						UtilidadBD.closeConnection(con);
					}
					request.setAttribute("codigoDescripcionError", "errors.usuario.noCargado");
					return mapping.findForward("paginaError");				
				}
				else
					if (estado==null||estado.equals(""))
					{
						articulosForm.reset();
						if( logger.isDebugEnabled() )
						{
							logger.debug("La accion espec铆ficada no esta definida ");
						}
						if (con!=null&&!con.isClosed())
						{
							UtilidadBD.closeConnection(con);
						}
						request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
						return mapping.findForward("paginaError");
					}

				//logger.warn("Estado "+estado);
				logger.info("**************************************");
				logger.info("===> El Estado es: "+estado);
				logger.info("**************************************");

				if(estado.equals("empezar"))
				{
					articulosForm.reset();
					this.cargarNivelesAtencionArticulos(articulosForm);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}


				if(estado.equals("guardar"))
				{
					try
					{
						PropertyUtils.copyProperties(articulo, articulosForm);
						articulo.setUsuario(medico.getLoginUsuario());
						articulosForm.setViasAdminXFormaFarma("numRegistros", articulosForm.getNumViasAdmiXFormaFarma());
						articulosForm.setUnidosisXArticulo("numRegistros", articulosForm.getNumUnidosisXArticulo());
						articulosForm.setGrupoEspecialArticulo("numRegistros", articulosForm.getNumGrupoEspecialArticulo());
						articulosForm.setFechaCreacion(UtilidadFecha.conversionFormatoFechaAAp(articulosForm.getFechaCreacion()));
						articulosForm.setFechaPrecioBaseVenta(UtilidadFecha.conversionFormatoFechaAAp(articulosForm.getFechaPrecioBaseVenta()));

						logger.info("FECHA PRECIO VENTA action-->"+articulosForm.getFechaPrecioBaseVenta());

						int codigoArticulo=articulo.insertarArticulo(con, medico.getCodigoInstitucionInt(),articulosForm.getViasAdminXFormaFarma(),articulosForm.getUnidosisXArticulo(),articulosForm.getGrupoEspecialArticulo());
						articulosForm.setCodigo(codigoArticulo+"");
						Articulo mundo = new Articulo();
						llenarmundo(articulosForm, mundo);
						articulo.insertarInformMed(con, codigoArticulo, mundo);
						this.insertarDocumentosAdjuntos(con, articulosForm, mundo, codigoArticulo);

						logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
						logger.info("Naturaleza: " + articulosForm.getNaturaleza());	
						logger.info("Tipo Inventario: " + articulosForm.getTipoInventario());	


						if(codigoArticulo>0)
						{
							// Solo aplica cuando la interfaz se encuentra activa
							if(UtilidadTexto.getBoolean(ValoresPorDefecto.getArticuloInventario(usuario.getCodigoInstitucionInt()))){
								logger.info("8888888888888 Interfaz Activa.");

								//guardar en ax_art
								DtoInterfazAxArt dto=new DtoInterfazAxArt();
								dto.setCodaxi(codigoArticulo+"");
								dto.setDescri(articulosForm.getDescripcion());
								dto.setTipcrea("M");
								dto.setTipinv(articulosForm.getTipoInventario()); //81450
								dto.setEstreg("0");

								// Cambio Anexo 780 ---
								dto.setClase(Utilidades.consultarClaseInterfazArticulo(con, codigoArticulo+""));
								//---------------------

								UtilidadInsertarAxArt insertarAxArt=new UtilidadInsertarAxArt();
								ResultadoBoolean resultadoBoolean=insertarAxArt.insertar(dto,medico.getCodigoInstitucionInt(),false);
								ArrayList mensa = new ArrayList();
								mensa.add(resultadoBoolean.getDescripcion());
								articulosForm.setMensajes(mensa);

							}
						}
						else
						{
							UtilidadBD.closeConnection(con);
							request.setAttribute("codigoDescripcionError", "errors.problemasBd");
							return mapping.findForward("paginaError");
						}
						articulo.setCodigo(codigoArticulo+"");
						PropertyUtils.copyProperties(articulosForm, articulo);
						articulosForm.setInfoMedicaAdjMap(articulo.cargarInfoMAdjArticulo(con, codigoArticulo));

						try{
							UtilidadTransaccion.getTransaccion().begin();
							INivelAtencionMundo servicioNivelAtencion=CapitacionFabricaMundo.crearNivelAtencionMundo();
							NivelAtencion nivelAtenArt=servicioNivelAtencion.obtenerNivelAtencionPorId(articulo.getNivelAtencion());
							if(nivelAtenArt!=null){
								articulosForm.setDescripcionNivelAtencion(nivelAtenArt.getDescripcion());
							}else{
								articulosForm.setDescripcionNivelAtencion("");
							}
							UtilidadTransaccion.getTransaccion().commit();
						}catch (Exception e) {			
							UtilidadTransaccion.getTransaccion().rollback();
							Log4JManager.error("Error Cargando la descripcion del nivel de atencion " + e);
						}					
						UtilidadBD.closeConnection(con);
						return mapping.findForward("resumen");
					}
					catch (Exception e)
					{
						e.printStackTrace();
						logger.error("Error copiando las propiedades (Form --> Mundo) en estado guardar "+e);

						UtilidadBD.closeConnection(con);
						request.setAttribute("codigoDescripcionError", "errors.problemasBd");
						return mapping.findForward("paginaError");
					}
				}
				if(estado.equals("ubicacion"))
				{
					logger.info("Esta en el action");
					return this.accionCargarUbicacion(articulosForm, con, mapping, medico);
				}
				if(estado.equals("modificar"))
				{
					articulosForm.reset();
					articulosForm.setManejaLotes("");
					articulosForm.setManejaFechaVencimiento("");
					articulosForm.setMultidosis("");
					this.cargarNivelesAtencionArticulos(articulosForm);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("modificar");
				}
				if(estado.equals("buscar"))
				{
					articulosForm.setResultados(articulo.buscar(con, llenarMundoBusqueda(articulosForm, articulo), medico.getCodigoInstitucionInt()));
					articulosForm.reset();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("resultadoBusqueda");
				}
				else if(estado.equals("ordenar"))
				{
					try
					{
						articulosForm.setResultados(Listado.ordenarColumna(new ArrayList(articulosForm.getResultados()),articulosForm.getUltimaPropiedad(),articulosForm.getCampo()));
					}
					catch (IllegalAccessException e1)
					{
						e1.printStackTrace();
					}
					articulosForm.setUltimaPropiedad(articulosForm.getCampo());
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("resultadoBusqueda");
				}
				else if(estado.equals("prepararModificacion"))
				{
					logger.info("===> ");
					logger.info("===> Entr茅 a prepararModificacion");
					logger.info("InfoMedica - "+articulosForm.isInfoMedica());
					boolean esArticuloUsado = false;
					Articulo articulo = new Articulo();
					int codigoArticulo = articulosForm.getCodModificacion();
					HashMap minSalud = new HashMap();
					String codigosMinSalud = "", anato = "", pAct="", fFarma="", conc="";
					minSalud = articulo.cargarMinSalud(con, codigoArticulo);
					codigosMinSalud = minSalud.get("minsalud_0")+"";
					logger.info("===> Mapa minSalud = "+minSalud);
					logger.info("===> codigosMinSalud = "+minSalud);
					/*anato = codigosMinSalud.substring(0, 4);
			    pAct = codigosMinSalud.substring(4, 8);
			    fFarma = codigosMinSalud.substring(8, 10);
			    conc = codigosMinSalud.substring(10);*/

					logger.info("===> Traidos de la consulta");
					logger.info("===> Anato: "+anato);
					logger.info("===> P. Act: "+pAct);
					logger.info("===> F Farma: "+fFarma);
					logger.info("===> Conc: "+conc);
					logger.info("===> Fin Traidos de ls consulta");

					articulosForm.setM1(anato);
					articulosForm.setM2(pAct);
					articulosForm.setM3(fFarma);
					articulosForm.setM4(conc);

					esArticuloUsado = UtilidadValidacion.esArticuloUsado(con, codigoArticulo+"", usuario.getCodigoInstitucion());
					logger.info("===> 驴El Art铆culo est谩 siendo usado? "+esArticuloUsado);
					logger.info("===> Informaci贸n M茅dica: Codigos Min Salud:");
					logger.info("Minsalud ="+articulo.getMinsalud());
					logger.info("Anato: "+articulosForm.getM1());
					logger.info("P. Act: "+articulosForm.getM2());
					logger.info("F Farma: "+articulosForm.getM3());
					logger.info("Conc: "+articulosForm.getM4());
					articulosForm.setEsEsteArticuloUsado(esArticuloUsado);

					if(codigoArticulo>0)
					{




						articulo.cargarArticulo(con, codigoArticulo);
						articulosForm.setInfoMedicaMap(articulo.cargarInfoMArticulo(con, codigoArticulo));
						articulosForm.setInfoMedicaAdjMap(articulo.cargarInfoMAdjArticulo(con, codigoArticulo));



						if(articulo.getEsMedicamento().equals(ConstantesBD.acronimoSi))
							articulosForm.setMostrarInfoMedica(true);

						logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n INFO MEDICA "+articulosForm.isInfoMedica());

						if(Integer.parseInt(articulosForm.getInfoMedicaMap().get("numRegistros").toString())>0)
						{
							articulo.setTiempoEsp(articulosForm.getInfoMedicaMap().get("tiempoRespEsperado_0").toString());
							articulo.setEfectoDes(articulosForm.getInfoMedicaMap().get("efectoDeseado_0").toString());
							articulo.setEfectosSec(articulosForm.getInfoMedicaMap().get("efectoSecundario_0").toString());
							articulo.setObservaciones(articulosForm.getInfoMedicaMap().get("observaciones_0").toString());
							articulo.setBibliografia(articulosForm.getInfoMedicaMap().get("bibliografia_0").toString());
						}
					}
					else
					{
						UtilidadBD.closeConnection(con);
						request.setAttribute("codigoDescripcionError", "errors.problemasBd");
						return mapping.findForward("paginaError");
					}
					try
					{
						PropertyUtils.copyProperties(articulosForm, articulo);
						articulosForm.setFechaCreacion(UtilidadFecha.conversionFormatoFechaAAp(articulosForm.getFechaCreacion()));
						articulosForm.setFechaPrecioBaseVenta(UtilidadFecha.conversionFormatoFechaAAp(articulosForm.getFechaPrecioBaseVenta()));

					}
					catch (Exception e)
					{
						logger.error("Error copiando las propiedades (Form --> Mundo) en estado guardar "+e);
						e.printStackTrace();
						UtilidadBD.closeConnection(con);
						request.setAttribute("codigoDescripcionError", "errors.problemasBd");
						return mapping.findForward("paginaError");
					}
					articulosForm.setCodigo(codigoArticulo+"");
					this.cargarNivelesAtencionArticulos(articulosForm);
					articulosForm.setNivelAtencion(articulo.getNivelAtencion());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("prepararModificacion");
				}
				if(estado.equals("guardarModificacion")) ///////////////////////////////////////////////////////////////->
				{ 
					logger.info("===> Entr茅 a guardarModificacion");
					articulosForm.setViasAdminXFormaFarma("numRegistros", articulosForm.getNumViasAdmiXFormaFarma());
					articulosForm.setUnidosisXArticulo("numRegistros", articulosForm.getNumUnidosisXArticulo());
					articulosForm.setGrupoEspecialArticulo("numRegistros", articulosForm.getNumGrupoEspecialArticulo());

					generarLog(con,articulosForm,medico.getLoginUsuario(), medico.getCodigoInstitucionInt());
					try
					{
						PropertyUtils.copyProperties(articulo, articulosForm);
						double valor=0;
						String tarifa="";
						int codigo=Integer.parseInt(articulosForm.getCodigo());
						if(!articulosForm.getPrecioBaseVentaViejo().equals(String.valueOf(articulosForm.getPrecioBaseVenta())))
						{
							valor=articulosForm.getPrecioBaseVenta();
							tarifa=ConstantesIntegridadDominio.acronimoPrecioBaseVenta;
						}
						else if(!articulosForm.getPrecioUltimaCompraViejo().equals(String.valueOf(articulosForm.getPrecioUltimaCompra())))
						{
							valor=articulosForm.getPrecioUltimaCompra();
							tarifa=ConstantesIntegridadDominio.acronimoPrecioUltimaCompra;
						}
						else if(!articulosForm.getPrecioCompraMasAltaViejo().equals(String.valueOf(articulosForm.getPrecioCompraMasAlta())))
						{
							valor=articulosForm.getPrecioCompraMasAlta();
							tarifa=ConstantesIntegridadDominio.acronimoPrecioCompraMasAlta;
						}
						else if(!articulosForm.getCostoPromedioViejo().equals(String.valueOf(articulosForm.getCostoPromedio())))
						{
							valor=Utilidades.convertirADouble(articulosForm.getCostoPromedio());
							tarifa=ConstantesIntegridadDominio.acronimoCostoPromedio;
						}
						articulo.modificarTarifas(con, valor, codigo, tarifa);
					}
					catch (Exception e)
					{
						logger.error("Error copiando las propiedades (Form --> Mundo) en estado guardar "+e);
						e.printStackTrace();
						UtilidadBD.closeConnection(con);
						request.setAttribute("codigoDescripcionError", "errors.problemasBd");
						return mapping.findForward("paginaError");
					}
					articulo.setUsuario(medico.getLoginUsuario());
					///Si el manejo de Lote estaba en S y ahora se deja en N
					boolean manejoLoteDeSaN = false;
					if (articulosForm.getManejaLotesValorAnt().trim().equalsIgnoreCase("N") && articulosForm.getManejaLotes().trim().equalsIgnoreCase("S") )
					{
						manejoLoteDeSaN = true;
					}
					///

					boolean infoMedica = false;

					articulo.modificarArticulo(con,articulosForm.getCodModificacion()+"", medico.getCodigoInstitucionInt(), manejoLoteDeSaN, articulosForm.getViasAdminXFormaFarma(), articulosForm.getUnidosisXArticulo(), articulosForm.getGrupoEspecialArticulo(), medico.getLoginUsuario());
					articulo.modificarInfoMArticulo(con,articulosForm.getCodModificacion()+"");
					infoMedica = articulo.modificarInfoMedicaArticulo(con, articulosForm.getCodModificacion()+"");

					logger.info("驴Se insert贸 o actualiz贸 la info? "+infoMedica);

					Articulo mundo = new Articulo();
					this.insertarDocumentosAdjuntos(con, articulosForm, mundo, articulosForm.getCodModificacion());
					logger.info("MAPA DOCS ASUNTOS PA MIRAR LOS CHECK >>>>>>>>>>>>"+articulosForm.getInfoMedicaAdjMap());
					for(int z=0; z<Integer.parseInt(articulosForm.getInfoMedicaAdjMap().get("numRegistros").toString());z++)
					{
						if(articulosForm.getInfoMedicaAdjMap().get("checkac_"+z).toString().equals("0"))
							articulo.eliminarAdjIM(con, articulosForm.getInfoMedicaAdjMap().get("codigo_"+z).toString());
					}
					logger.info("MAPA DOCS ASUNTOS PA MIRAR LOS CHECK DESPUES DE ELIMINAR >>>>>>>>>>>>"+articulosForm.getInfoMedicaAdjMap());
					articulosForm.setViasAdminXFormaFarma(articulo.cargarViasAdminArticulo(con, articulosForm.getCodModificacion()));
					articulosForm.setUnidosisXArticulo(articulo.cargarUnidosisArticulo(con,articulosForm.getCodModificacion()));
					articulosForm.setGrupoEspecialArticulo(articulo.cargarGrupoEspecialesArticulo(con, articulosForm.getCodModificacion()));
					articulosForm.setDescripcionNivelAtencion(articulo.cargarDescripcionNivelAtencion(con, articulosForm.getCodModificacion()));

					articulosForm.setInfoMedicaAdjMap(articulo.cargarInfoMAdjArticulo(con, articulosForm.getCodModificacion()));

					UtilidadBD.closeConnection(con);
					return mapping.findForward("resumenModificacion");
				}
				if(estado.equals("consultar"))
				{
					articulosForm.reset();
					this.cargarNivelesAtencionArticulos(articulosForm);
					articulosForm.setMultidosis("");
					articulosForm.setManejaLotes("");
					articulosForm.setManejaFechaVencimiento("");

					UtilidadBD.closeConnection(con);
					return mapping.findForward("iniciarBusqueda");
				}
				if(estado.equals("detalleConsulta"))
				{
					int codigoArticulo=articulosForm.getCodModificacion();
					if(codigoArticulo>0)
					{
						articulo.cargarArticulo(con, codigoArticulo);
						articulosForm.setInfoMedicaMap(articulo.cargarInfoMArticulo(con, codigoArticulo));
						articulosForm.setInfoMedicaAdjMap(articulo.cargarInfoMAdjArticulo(con, codigoArticulo));
						articulosForm.setDescripcionNivelAtencion(articulo.cargarDescripcionNivelAtencion(con, articulosForm.getCodModificacion()));
					}
					else
					{
						UtilidadBD.closeConnection(con);
						request.setAttribute("codigoDescripcionError", "errors.problemasBd");
						return mapping.findForward("paginaError");
					}
					try
					{
						PropertyUtils.copyProperties(articulosForm, articulo);
						articulosForm.setFechaCreacion(UtilidadFecha.conversionFormatoFechaAAp(articulosForm.getFechaCreacion()));
						articulosForm.setFechaPrecioBaseVenta(UtilidadFecha.conversionFormatoFechaAAp(articulosForm.getFechaPrecioBaseVenta()));
					}
					catch (Exception e)
					{
						logger.error("Error copiando las propiedades (Form --> Mundo) en estado guardar "+e);
						e.printStackTrace();
						UtilidadBD.closeConnection(con);
						request.setAttribute("codigoDescripcionError", "errors.problemasBd");
						return mapping.findForward("paginaError");
					}
					articulosForm.setCodigo(codigoArticulo+"");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}

				if(estado.equals("eliminarViaAdministracion"))
				{
					this.accionEliminarViaAdministracion(articulosForm);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				
				//MT6602 * usuario: jesrioro * Fecha: 06/03/2013
				if(estado.equals("eliminarTodasViaAdministracion"))
				{
					this.accionEliminarTodasViaAdministracion(articulosForm);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				//MT6602 * usuario: jesrioro * Fecha: 06/03/2013
				if(estado.equals("eliminarTodasViaAdministracionMem"))
				{
					this.accionEliminarTodasViaAdministracionMem(articulosForm);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				
				
				if(estado.equals("eliminarUnidosis"))
				{
					this.accionEliminarUnidosis(articulosForm);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				if(estado.equals("eliminarGruposEspeciales"))
				{
					this.accionEliminarGrupoEspecial(articulosForm);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}


				if(estado.equals("paginaPrincipal"))
				{
					logger.info("InfoMedica - "+articulosForm.isInfoMedica());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
			else
			{
				//Todav铆a no existe conexi贸n, por eso no se cierra
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}
	
	
	/**
	 * Este m閠odo se encarga de obtener los niveles de Atenci髇 de articulos activos en el sistema
	 * @param forma ArticulosForm
	 */
	private void cargarNivelesAtencionArticulos(ArticulosForm articulosForm){
		
		try{
			UtilidadTransaccion.getTransaccion().begin();
			INivelAtencionMundo servicioNivelAtencion=CapitacionFabricaMundo.crearNivelAtencionMundo();
			articulosForm.setNivelesAtencion(servicioNivelAtencion.obtenerNivelesAtencionActivos());
			UtilidadTransaccion.getTransaccion().commit();
		}catch (Exception e) {			
			UtilidadTransaccion.getTransaccion().rollback();
			Log4JManager.error("Error Cargando los niveles de atencion " + e);
		}
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 */
	private boolean insertarDocumentosAdjuntos(Connection con, ArticulosForm articulosForm, Articulo articulo, int codigoArticulo)
	{
		logger.info("\n\n DOCUMENTROS ADJUNTOS--> "+articulosForm.getDocumentosAdjuntosGenerados()+" numDocsAdjuntos-->"+articulosForm.getNumDocumentosAdjuntos());
		
		//Documentos adjuntos
		int numAdjuntos = articulosForm.getNumDocumentosAdjuntos();
		
		for( int i=0; i<numAdjuntos; i++ )
		{
			String nombre = (String)articulosForm.getDocumentoAdjuntoGenerado(""+i);
			String checkbox = (String)articulosForm.getDocumentoAdjuntoGenerado("checkbox_"+i);
			
			if( nombre != null && checkbox != null && !checkbox.equals("off") )
			{
				String[] nombres = nombre.split("@");
				
				if( nombres.length == 2 )
				{
					String codigoStr = (String)articulosForm.getDocumentoAdjuntoGenerado("codigo_"+i);
					int codigo = 0;
					
					if( UtilidadCadena.noEsVacio(codigoStr) )
						codigo = Integer.parseInt(codigoStr);
						
					DocumentoAdjunto documento = new DocumentoAdjunto(nombres[1], nombres[0], false, codigo, articulosForm.getCodigoRespuesta());					
					articulo.getDocumentosAdjuntos().addDocumentoAdjunto(documento);
				}
			}		
		}
		return articulo.insertarDocumentosAdjuntos(con, codigoArticulo);
	}
	
	
	/**
	 * 
	 * @param articulosForm
	 * @param con
	 * @param mapping
	 * @param medico
	 * @return
	 */
	private ActionForward accionCargarUbicacion(ArticulosForm articulosForm, Connection con, ActionMapping mapping, UsuarioBasico medico) {
		
	
    	Articulo mundo= new Articulo();
    	
    	mundo.setCodigo(articulosForm.getCodigo());
    	logger.info("UBICANDO");
		   	
    	logger.info("articulosForm.getCodigo()---------->"+articulosForm.getCodigo());
		
		articulosForm.setUbicacionMap(Articulo.consultarUbicacion(con, mundo));
		
		
		logger.info("UbicacionMap----------->"+articulosForm.getUbicacionMap());
		
		UtilidadBD.closeConnection(con);
		
			
		
		return mapping.findForward("resultadoBusqueda");
    	
    	
		
	}
	
	
	/**
	 * 
	 * @param articulosForm
	 * @param mundo
	 */
	public void llenarmundo(ArticulosForm articulosForm, Articulo mundo)
	{
		mundo.setTiempoEsp(articulosForm.getTiempoEsp());
		mundo.setEfectoDes(articulosForm.getEfectoDes());
		mundo.setEfectosSec(articulosForm.getEfectosSec());
		mundo.setObservaciones(articulosForm.getObservaciones());
		mundo.setBibliografia(articulosForm.getBibliografia());
	}

	
	/**
	 * 
	 * @param forma
	 */
	private void accionEliminarUnidosis(ArticulosForm forma)
	{
		int ultimaPosAsignada=(forma.getNumUnidosisXArticulo()-1);
		for(int i=forma.getIndexUnidosisEliminar();i<ultimaPosAsignada;i++)
		{
			forma.setUnidosisXArticulo("unidad_medida_"+i, forma.getUnidosisXArticulo("unidad_medida_"+(i+1)));
			forma.setUnidosisXArticulo("nombre_"+i, forma.getUnidosisXArticulo("nombre_"+(i+1)));
			forma.setUnidosisXArticulo("cantidad_"+i, forma.getUnidosisXArticulo("cantidad_"+(i+1)));
			forma.setUnidosisXArticulo("tiporegistro_"+i, forma.getUnidosisXArticulo("tiporegistro_"+(i+1)));
			forma.setUnidosisXArticulo("activo_"+i, forma.getUnidosisXArticulo("activo_"+(i+1)));
		}
		forma.getUnidosisXArticulo().remove("unidad_medida_"+ultimaPosAsignada);
		forma.getUnidosisXArticulo().remove("nombre_"+ultimaPosAsignada);
		forma.getUnidosisXArticulo().remove("cantidad_"+ultimaPosAsignada);
		forma.getUnidosisXArticulo().remove("tiporegistro_"+ultimaPosAsignada);
		forma.getUnidosisXArticulo().remove("activo_"+ultimaPosAsignada);
		forma.setNumUnidosisXArticulo(ultimaPosAsignada);
	}


	/**
	 * 
	 * @param forma
	 */
    private void accionEliminarViaAdministracion(ArticulosForm forma)
	{
		int ultimaPosAsignada=(forma.getNumViasAdmiXFormaFarma()-1);
		for(int i=forma.getIndexViaAdminEliminar();i<ultimaPosAsignada;i++)
		{
			forma.setViasAdminXFormaFarma("via_admin_"+i, forma.getViasAdminXFormaFarma("via_admin_"+(i+1)));
			forma.setViasAdminXFormaFarma("nombre_"+i, forma.getViasAdminXFormaFarma("nombre_"+(i+1)));
			forma.setViasAdminXFormaFarma("tiporegistro_"+i, forma.getViasAdminXFormaFarma("tiporegistro_"+(i+1)));
			forma.setViasAdminXFormaFarma("activo_"+i, forma.getViasAdminXFormaFarma("activo_"+(i+1)));
		}
		forma.getViasAdminXFormaFarma().remove("via_admin_"+ultimaPosAsignada);
		forma.getViasAdminXFormaFarma().remove("nombre_"+ultimaPosAsignada);
		forma.getViasAdminXFormaFarma().remove("tiporegistro_"+ultimaPosAsignada);
		forma.getViasAdminXFormaFarma().remove("activo_"+ultimaPosAsignada);
		forma.setNumViasAdmiXFormaFarma(ultimaPosAsignada);
	}
    
    
    /*
	* Tipo Modificacion: Segun incidencia 6602
	* Autor: Jes鷖 Dar韔 R韔s
	* usuario: jesrioro
	* Fecha: 06/03/2013
	* Descripcion: 	elimina  todas las  vias de administracion cuando se cambia  la  forma farmac閡tica
	 */
    /**
     * @param forma
     */
    private void accionEliminarTodasViaAdministracion(ArticulosForm forma){
    	int ultimaPosAsignada=(forma.getNumViasAdmiXFormaFarma());
    	for(int i=0;i<ultimaPosAsignada;i++)
		{
    		forma.getViasAdminXFormaFarma().remove("via_admin_"+ultimaPosAsignada);
    		forma.getViasAdminXFormaFarma().remove("nombre_"+ultimaPosAsignada);
    		forma.getViasAdminXFormaFarma().remove("tiporegistro_"+ultimaPosAsignada);
    		forma.getViasAdminXFormaFarma().remove("activo_"+ultimaPosAsignada);
		}
    	forma.setNumViasAdmiXFormaFarma(0);
    }
    
    
    /**
     * @param forma
     */
    private void accionEliminarTodasViaAdministracionMem(ArticulosForm forma){
    	int ultimaPosAsignada=(forma.getNumViasAdmiXFormaFarma());
    	for(int i=0;i<ultimaPosAsignada;i++){
    		System.out.println("pos="+i+" de "+ultimaPosAsignada);
    		if(forma.getViasAdminXFormaFarma().get("tiporegistro_"+i).equals("MEM")){
    			
    			forma.getViasAdminXFormaFarma().remove("via_admin_"+i);
    			forma.getViasAdminXFormaFarma().remove("nombre_"+i);
    			forma.getViasAdminXFormaFarma().remove("tiporegistro_"+i);
    			forma.getViasAdminXFormaFarma().remove("activo_"+i);
    			
    			forma.setNumViasAdmiXFormaFarma(forma.getNumViasAdmiXFormaFarma()-1);
    		}
		}
    	
    	//
    }
    
    
    /**
     * @param forma
     */
    private void accionEliminarGrupoEspecial(ArticulosForm forma)
    {
    	int ultimaPosAsignada = (forma.getNumGrupoEspecialArticulo()-1);
    	for(int i=forma.getIndexGrupoEspecialEliminar();i<ultimaPosAsignada;i++)
    	{
    		forma.setGrupoEspecialArticulo("grupoespecial_"+i, forma.getGrupoEspecialArticulo("grupoespecial_"+(i+1)));
    		forma.setGrupoEspecialArticulo("nombre_"+i, forma.getGrupoEspecialArticulo("nombre_"+(i+1)));
    		forma.setGrupoEspecialArticulo("tiporegistro_"+i, forma.getGrupoEspecialArticulo("tiporegistro_"+(i+1)));
    		forma.setGrupoEspecialArticulo("activo_"+i, forma.getGrupoEspecialArticulo("activo_"+(i+1)));
    	}
    	forma.getGrupoEspecialArticulo().remove("grupoespecial_"+ultimaPosAsignada);
    	forma.getGrupoEspecialArticulo().remove("nombre_"+ultimaPosAsignada);
    	forma.getGrupoEspecialArticulo().remove("tiporegistro_"+ultimaPosAsignada);
    	forma.getGrupoEspecialArticulo().remove("activo_"+ultimaPosAsignada);
    	forma.setNumGrupoEspecialArticulo(ultimaPosAsignada);
    	Utilidades.imprimirMapa(forma.getGrupoEspecialArticulo());
    }


	/**
     * @param con
     * @param articulosForm
     * @param informacionGeneralPersonalSalud
     */
    private void generarLog(Connection con, ArticulosForm modificaciones, String usuario, int institucion)
    {
        Articulo articuloTempo=new Articulo();
		articuloTempo.cargarArticulo(con, modificaciones.getCodModificacion());
        String log="\n          =====INFORMACION ANTES DE LA MODIFICACION===== " +
		"\n*  Descripci贸n [" +articuloTempo.getDescripcion()+"] "+
		"\n*  Naturaleza ["+articuloTempo.getNaturaleza()+"] " +
		"\n*  C贸digo Minsalud ["+articuloTempo.getMinsalud()+"] "+
		"\n*  Forma Farmace煤tica ["+articuloTempo.getFormaFarmaceutica()+"] " +
		"\n*  Unidad de medida ["+articuloTempo.getUnidadMedida()+"] "+
		"\n*  Concentraci贸n ["+articuloTempo.getConcentracion()+"]" +
		"\n*  Tiempo de Respuesta Esperado ["+articuloTempo.getTiempoEsp()+"]" +
		"\n*  Efecto Deseado al Tratamiento ["+articuloTempo.getEfectoDes()+"]" +
		"\n*  Efectos secundarios y posibles riesgos al Tratamiento ["+articuloTempo.getEfectosSec()+"]" +
		"\n*  Observaciones ["+articuloTempo.getObservaciones()+"]" +
		"\n*  Bibliograf铆a ["+articuloTempo.getBibliografia()+"]" +
		"\n*  Stock M铆nimo ["+articuloTempo.getStockMinimo()+"]" +
		"\n*  Stock M谩ximo ["+articuloTempo.getStockMaximo()+"]" +
		
		"\n*  Manejo Lote ["+articuloTempo.getManejaLotes()+"]" +
		"\n*  Manejo Fecha Vencimiento ["+articuloTempo.getManejaFechaVencimiento()+"]" +
		"\n*  Porcentaje de Iva ["+articuloTempo.getPorcentajeIva()+"]" +
		"\n*  Precio 脷ltima Compra ["+articuloTempo.getPrecioUltimaCompra()+"]" +
		"\n*  Precio Base de Venta ["+articuloTempo.getPrecioBaseVenta()+"]" +
		"\n*  Precio Compra M谩s Alta ["+articuloTempo.getPrecioCompraMasAlta()+"]" +
		
		"\n*  Fecha Precio Base de Venta ["+articuloTempo.getFechaPrecioBaseVenta()+"]" +
		///
		"\n*  Multidosis ["+articuloTempo.getMultidosis()+"]" +
		"\n*  M谩xima Cantidad Despacho Mes ["+articuloTempo.getMaximaCantidadMes()+"]" +

		"\n*  Punto Pedido ["+articuloTempo.getPuntoPedido()+"]" +
		"\n*  Cantidad Compra ["+articuloTempo.getCantidadCompra()+"]" +
		"\n*  Costo promedio ["+articuloTempo.getCostoPromedio()+"]" +
		"\n*  Costo Donacion ["+articuloTempo.getCostoDonacion()+"]" +
		"\n*  C贸digo Interfaz ["+articuloTempo.getCodigoInterfaz()+"]" +
		"\n*  Indicativo Automatico ["+articuloTempo.getIndicativoAutomatico()+"]" +
		"\n*  Indicativo por completar["+articuloTempo.getIndicativoPorCompletar()+"]" +
		"\n*  Categor铆a ["+articuloTempo.getCategoria()+"]\n"+
		"\n*  Nivel Atencion ["+articuloTempo.getNivelAtencion()+"]\n"+
		
		"";
        modificaciones.setPrecioBaseVentaViejo(String.valueOf(articuloTempo.getPrecioBaseVenta()));
        modificaciones.setPrecioUltimaCompraViejo(String.valueOf(articuloTempo.getPrecioUltimaCompra()));
        modificaciones.setPrecioCompraMasAltaViejo(String.valueOf(articuloTempo.getPrecioCompraMasAlta()));
        String cadena="\n*  ----------------------VIAS ADMIN FORMA FARMACEUTICA------\n";
        for(int i=0;i<articuloTempo.getNumViasAdmiXFormaFarma();i++)
        {
        	HashMap mapa=articuloTempo.getViasAdminXFormaFarma();
        	if((mapa.get("forma_farmaceutica_"+i)+"").trim().equals(articuloTempo.getFormaFarmaceutica()))
        	{
        		cadena+="\n*  Via Administracion ["+mapa.get("nombre_"+i)+"]\t\t\tActivo:"+mapa.get("activo_"+i)+"";
        	}
        }
        
        String cadena2="\n*  ----------------------UNIDOSIS X ARTICULO------\n";
        for(int i=0;i<articuloTempo.getNumUnidosisXArticulo();i++)
        {
        	HashMap mapa2=articuloTempo.getUnidosisXArticulo();
        	cadena2+="\n*  Unidosis ["+mapa2.get("nombre_"+i)+"]\t\t\tCantidad:["+mapa2.get("cantidad_"+i)+"]\t\t\tActivo:["+mapa2.get("activounidosis_"+i)+"]";
        }

        log+=cadena+cadena2;


		if(articuloTempo.getEstadoArticulo())
			log += "\n*  Activa [ SI ]";
		else
			log += "\n*  Activa [ NO ]";
		log+="\n\n          =====INFORMACION DESPUES DE LA MODIFICACION===== " +
		"\n*  Descripcion [" +modificaciones.getDescripcion()+"] "+
		"\n*  Naturaleza ["+modificaciones.getNaturaleza()+"] " +
		"\n*  C贸digo Minsalud ["+modificaciones.getMinsalud()+"] "+
		"\n*  Forma Farmace煤tica ["+modificaciones.getFormaFarmaceutica()+"] " +
		"\n*  Unidad de medida ["+modificaciones.getUnidadMedida()+"] "+
		"\n*  Concentracion ["+modificaciones.getConcentracion()+"]" +
		"\n*  Tiempo de Respuesta Esperado ["+modificaciones.getTiempoEsp()+"]" +
		"\n*  Efecto Deseado al Tratamiento ["+modificaciones.getEfectoDes()+"]" +
		"\n*  Efectos secundarios y posibles riesgos al Tratamiento ["+modificaciones.getEfectosSec()+"]" +
		"\n*  Observaciones ["+modificaciones.getObservaciones()+"]" +
		"\n*  Bibliografia ["+modificaciones.getBibliografia()+"]" +
		"\n*  Stock Minimo ["+modificaciones.getStockMinimo()+"]" +
		"\n*  Stock Maximo ["+modificaciones.getStockMaximo()+"]" +
		
		"\n*  Manejo Lote ["+modificaciones.getManejaLotes()+"]" +
		"\n*  Manejo Fecha Vencimiento ["+modificaciones.getManejaFechaVencimiento()+"]" +
		"\n*  Porcentaje de Iva ["+modificaciones.getPorcentajeIva()+"]" +
		"\n*  Precio Ultima Compra ["+modificaciones.getPrecioUltimaCompra()+"]" +
		"\n*  Precio Base de Venta ["+modificaciones.getPrecioBaseVenta()+"]" +
		"\n*  Precio Compra Mas Alta ["+modificaciones.getPrecioCompraMasAlta()+"]" +
		"\n*  Fecha Precio Base de Venta ["+modificaciones.getFechaPrecioBaseVenta()+"]" +
		///
		"\n*  Multidosis ["+modificaciones.getMultidosis()+"]" +
		"\n*  Maxima Cantidad Despacho Mes ["+modificaciones.getMaximaCantidadMes()+"]" +
		
		
		"\n*  Punto Pedido ["+modificaciones.getPuntoPedido()+"]" +
		"\n*  Cantidad Compra ["+modificaciones.getCantidadCompra()+"]" +
		"\n*  Costo promedio ["+modificaciones.getCostoPromedio()+"]" +
		"\n*  Costo Donacion["+modificaciones.getCostoDonacion()+"]" +
		"\n*  Codigo Interfaz["+modificaciones.getCodigoInterfaz()+"]" +
		"\n*  Indicativo Automatico ["+modificaciones.getIndicativoAutomatico()+"]" +
		"\n*  Indicativo por completar ["+modificaciones.getIndicativoPorCompletar()+"]" +
		"\n*  Categoria ["+modificaciones.getCategoria()+"]" +
		"\n*  Nivel Atencion ["+modificaciones.getNivelAtencion()+"]\n";


		String cadena3="\n*  ----------------------VIAS ADMIN FORMA FARMACEUTICA------\n";
        for(int i=0;i<modificaciones.getNumViasAdmiXFormaFarma();i++)
        {
        	HashMap mapa=modificaciones.getViasAdminXFormaFarma();
        	if((mapa.get("forma_farmaceutica_"+i)+"").trim().equals(modificaciones.getFormaFarmaceutica()))
        	{
        		cadena3+="\n*  V铆a Administraci贸n ["+mapa.get("nombre_"+i)+"]\t\t\tActivo:"+mapa.get("activo_"+i)+"";
        	}
        }
        
        String cadena4="\n*  ----------------------UNIDOSIS X ARTICULO------\n";
        for(int i=0;i<modificaciones.getNumUnidosisXArticulo();i++)
        {
        	HashMap mapa2=modificaciones.getUnidosisXArticulo();
        	cadena4+="\n*  Unidosis ["+mapa2.get("nombre_"+i)+"]\t\t\tCantidad:["+mapa2.get("cantidad_"+i)+"]\t\t\tActivo:"+mapa2.get("activounidosis_"+i)+"";
        }

        log+=cadena3+cadena4;
		
		if(modificaciones.getEstadoArticulo())
			log += "\n*  Activa [ SI ]";
		else
			log += "\n*  Activa [ NO ]";
		
		log+="\n========================================================\n\n\n " ;
	
		LogsAxioma.enviarLog(ConstantesBD.logArticuloCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario);
        
    }

	/**
	 * @param articulosForm
	 * @param articulo
	 * @return retorna true si se seleccion贸 el check
	 * de busqueda por estado, false de lo contrario 
	 */
	private boolean llenarMundoBusqueda(ArticulosForm articulosForm, Articulo articulo)
	{
		boolean busActivo=false;
		articulo.setClase("");
		if(!articulosForm.getClase().equals("0"))
		{
			articulo.setClase(articulosForm.getClase());
		}
		articulo.setGrupo("");
		if(!articulosForm.getGrupo().equals("0"))
		{
		    articulo.setGrupo(articulosForm.getGrupo());
		}
		if(!articulosForm.getSubgrupo().equals("0"))
		{
			articulo.setGrupo(articulosForm.getGrupo());
		}
		articulo.setSubgrupo("");
		if(!articulosForm.getSubgrupo().equals("0"))
		{
			articulo.setSubgrupo(articulosForm.getSubgrupo());
		}
		articulo.setDescripcion("");
		if(!articulosForm.getDescripcion().trim().equals(""))
		{
		    articulo.setDescripcion(articulosForm.getDescripcion());
		}
		articulo.setCodigo("");
		if(!articulosForm.getCodigo().trim().equals(""))
		{
			articulo.setCodigo(articulosForm.getCodigo());
		}
		articulo.setNaturaleza("");
		if(!articulosForm.getNaturaleza().equals(""))
		{
			articulo.setNaturaleza(articulosForm.getNaturaleza());
		}
		articulo.setMinsalud("");
		if(!articulosForm.getMinsalud().trim().equals(""))
		{
			articulo.setMinsalud(articulosForm.getMinsalud());
		}
		articulo.setFormaFarmaceutica("");
		if(!articulosForm.getFormaFarmaceutica().trim().equals("0"))
		{
		    articulo.setFormaFarmaceutica(articulosForm.getFormaFarmaceutica());
		}
		articulo.setConcentracion("");
		if(!articulosForm.getConcentracion().trim().equals(""))
		{
			articulo.setConcentracion(articulosForm.getConcentracion());
		}
		articulo.setUnidadMedida("");
		if(!articulosForm.getUnidadMedida().trim().equals("0"))
		{
			articulo.setUnidadMedida(articulosForm.getUnidadMedida());
		}
		if(!articulosForm.getBuscarEstado().equals("-1"))
		{
		    articulo.setEstadoArticulo(UtilidadTexto.getBoolean(articulosForm.getBuscarEstado()));
		    busActivo=true;
		}
		articulo.setBusStockMinimo("");
		if(!articulosForm.getBusStockMinimo().trim().equals(""))
		{
		    articulo.setBusStockMinimo(articulosForm.getBusStockMinimo());
		}
		articulo.setBusStockMaximo("");
		if(!articulosForm.getBusStockMaximo().trim().equals(""))
		{
		    articulo.setBusStockMaximo(articulosForm.getBusStockMaximo());
		}
		articulo.setBusPuntoPedido("");
		if(!articulosForm.getBusPuntoPedido().trim().equals(""))
		{
		    articulo.setBusPuntoPedido(articulosForm.getBusPuntoPedido());
		}
		articulo.setBusCostoPromedio("");
		if(!articulosForm.getBusCostoPromedio().trim().equals(""))
		{
		    articulo.setBusCostoPromedio(articulosForm.getBusCostoPromedio());
		}
		articulo.setBusPrecioCompraMasAlta("");
		if(!articulosForm.getBusPrecioCompraMasAlta().trim().equals(""))
		{
		    articulo.setBusPrecioCompraMasAlta(articulosForm.getBusPrecioCompraMasAlta());
		}
		articulo.setBusCodigoInterfaz("");
		if(!articulosForm.getBusCodigoInterfaz().trim().equals(""))
		{
		    articulo.setBusCodigoInterfaz(articulosForm.getBusCodigoInterfaz());
		}
		articulo.setBusIndicativoAutomatico("");
		if(!articulosForm.getBusIndicativoAutomatico().trim().equals(""))
		{
		    articulo.setBusIndicativoAutomatico(articulosForm.getBusIndicativoAutomatico());
		}
		articulo.setBusIndicativoPorCompletar("");
		if(!articulosForm.getBusIndicativoPorCompletar().trim().equals(""))
		{
		    articulo.setBusIndicativoPorCompletar(articulosForm.getBusIndicativoPorCompletar());
		}
		articulo.setBusCantidadCompra("");
		if(!articulosForm.getBusCantidadCompra().trim().equals(""))
		{
		    articulo.setBusCantidadCompra(articulosForm.getBusCantidadCompra());		
		}
		articulo.setCategoria(ConstantesBD.codigoNuncaValido);
		if(articulosForm.getCategoria()!=ConstantesBD.codigoNuncaValido)
		{
		    articulo.setCategoria(articulosForm.getCategoria());
		}
		articulo.setFechaCreacion("");
		if(!articulosForm.getFechaCreacion().trim().equals(""))
		{
		    articulo.setFechaCreacion(articulosForm.getFechaCreacion());
		}
		articulo.setRegistroINVIMA("");
		if(!articulosForm.getRegistroINVIMA().trim().equals(""))
		{
		    articulo.setRegistroINVIMA(articulosForm.getRegistroINVIMA());
		}
		//
		articulo.setBusMaximaCantidadMes("");
		if(!articulosForm.getBusMaximaCantidadMes().trim().equals(""))
		{
		    articulo.setBusMaximaCantidadMes(articulosForm.getBusMaximaCantidadMes());
		}
		
		articulo.setMultidosis("");
		if(!articulosForm.getMultidosis().trim().equals(""))
		{
		    articulo.setMultidosis(articulosForm.getMultidosis());
		}		
		
		articulo.setManejaLotes("");
		if(!articulosForm.getManejaLotes().trim().equals(""))
		{
		    articulo.setManejaLotes(articulosForm.getManejaLotes());
		}			
		
		articulo.setManejaFechaVencimiento("");
		if(!articulosForm.getManejaFechaVencimiento().trim().equals(""))
		{
		    articulo.setManejaFechaVencimiento(articulosForm.getManejaFechaVencimiento());
		}
		
		articulo.setBusPorcentajeIva("");
		if(!articulosForm.getBusPorcentajeIva().trim().equals(""))
		{
		    articulo.setBusPorcentajeIva(articulosForm.getBusPorcentajeIva());
		}
		
		articulo.setBusPrecioUltimaCompra("");
		if(!articulosForm.getBusPrecioUltimaCompra().trim().equals(""))
		{
		    articulo.setBusPrecioUltimaCompra(articulosForm.getBusPrecioUltimaCompra());
		}
		
		articulo.setBusPrecioBaseVenta("");
		if(!articulosForm.getBusPrecioBaseVenta().trim().equals(""))
		{
		    articulo.setBusPrecioBaseVenta(articulosForm.getBusPrecioBaseVenta());
		}
		
		articulo.setFechaPrecioBaseVenta("");
		if(!articulosForm.getFechaPrecioBaseVenta().trim().equals(""))
		{
		    articulo.setFechaPrecioBaseVenta(articulosForm.getFechaPrecioBaseVenta());
		}
		if(!articulosForm.getPrecioBaseVentaViejo().trim().equals(""))
		{
		    articulosForm.setPrecioBaseVentaViejo(articulosForm.getBusPrecioBaseVenta());
		}
		if(!articulosForm.getPrecioUltimaCompraViejo().trim().equals(""))
		{
		    articulosForm.setPrecioUltimaCompraViejo(articulosForm.getBusPrecioUltimaCompra());
		}
		
		if(!articulosForm.getPrecioCompraMasAltaViejo().trim().equals(""))
		{
		    articulosForm.setPrecioCompraMasAltaViejo(articulosForm.getPrecioCompraMasAltaViejo());
		}
		//
		articulo.setDescripcionAlterna("");
		if(!articulosForm.getDescripcionAlterna().trim().equals(""))
		{
			articulo.setDescripcionAlterna(articulosForm.getDescripcionAlterna());
		}
		
		articulo.setAtencionOdon("");
		if(!articulosForm.getAtencionOdon().trim().equals(""))
		{
			articulo.setAtencionOdon(articulosForm.getAtencionOdon());
		}
		
		articulo.setNivelAtencion(ConstantesBD.codigoNuncaValidoLong);
		if(articulosForm.getNivelAtencion()!=ConstantesBD.codigoNuncaValido)
		{
			articulo.setNivelAtencion(articulosForm.getNivelAtencion());
		}
		
		return busActivo;
	}
	
}
