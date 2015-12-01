/**
 * 
 */
package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosString;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.facturacion.ExcepcionesTarifasForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ExcepcionesTarifas;


/**
 * @author axioma
 *
 */
public class ExcepcionesTarifasAction extends Action 
{
	
	/**
	 * 
	 */
	public static Logger logger=Logger.getLogger(ExcepcionesTarifasAction.class);
	
	/**
	 * 
	 */
	private static String[] indicesExcepciones={"codigo_","viaingreso_","nomviaingreso_","tipopaciente_","nomtipopaciente_","tipocomplejidad_","nomtipocomplejidad_","tiporegistro_","tipospaciente_","fechavigencia_","observaciones_","numdetalles_","vigente_", "codcentroatencion_", "nomcentroatencion_"};
	
	

	/**
	 * 
	 */  
	private String[] indicesAgrupacionArticulos={"codigo_","codigoexcepcion_","clase_","nomclase_","grupo_","nomgrupo_","subgrupo_","nomsubgrupo_","naturaleza_","nomnaturaleza_","valorajuste_","baseexcepcion_","suma_","nuevatarifa_","tiporegistro_","fechavigencia_"};
	
	/**
	 * 
	 */
	private String[] indicesArticulos={"codigo_","codigoexcepcion_","codigoArticulo_","descripcionArticulo_","valorajuste_","baseexcepcion_","suma_","nuevatarifa_","tiporegistro_","fechavigencia_","valorbase_"};
	
	/**
	 * 
	 */
	private String[] indicesAgrupacionServicio={"codigo_","codigoexcepcion_","tipopos_","gruposervicio_","acronimogruposervicio_","descgruposervicio_","tiposervicio_","nomtiposervicio_","especialidad_","nomespecialidad_","valorajuste_","suma_","nuevatarifa_","tiporegistro_","fechavigencia_","valorbase_"};
	
	/**
	 * 
	 */
	private String[] indicesServicios={"codigo_","codigoexcepcion_","codigoServicio_","descripcionServicio_","valorajuste_","suma_","nuevatarifa_","tiporegistro_","fechavigencia_"};
	
	/**
	 * 
	 */
	private String[] indicesPorcentajes={"codigo_","porcentaje_","suma_","codigoexcepcion_","tiporegistro_"};
	
	
	/**
	 * 
	 */
	private static final int codigoSeccionAgrupacionArticulos=0;

	/**
	 * 
	 */
	private static final int codigoSeccionArticulos=1;

	/**
	 * 
	 */
	private static final int codigoSeccionAgrupacionServicios=2;

	/**
	 * 
	 */
	private static final int codigoSeccionServicios=3;

	/**
	 * Metodo que lleva el flujo de la fuincionalidad
	 */
	public ActionForward execute(	ActionMapping mapping, 	ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		Connection con=null;
		try{
			if(form instanceof ExcepcionesTarifasForm)
			{
				ExcepcionesTarifasForm forma=(ExcepcionesTarifasForm) form;
				ExcepcionesTarifas mundo=new ExcepcionesTarifas();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
				con=UtilidadBD.abrirConexion();
				String estado=forma.getEstado();

				logger.info("Estado -->"+estado);

				if(estado.equals("empezar"))
				{
					forma.reset();
					forma.setConvenios(Utilidades.obtenerConvenios(con, "","",false,"",false));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("cargarContratos"))
				{
					forma.setContrato(ConstantesBD.codigoNuncaValido);
					forma.setContratos(Utilidades.obtenerContratos(con,forma.getCodigoConvenio(),true,false));
					if(forma.getContratos().size()==1)
					{
						forma.setContrato(Integer.parseInt(((HashMap)(forma.getContratos().get(0))).get("codigo")+""));
						return this.accionCargarInfo(con,forma,mundo,usuario,mapping);
					}
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("cargarInfo"))
				{
					forma.resetOpTrue();
					return this.accionCargarInfo(con,forma,mundo,usuario,mapping);
				}

				else if(estado.equals("nuevaExcepcion"))
				{
					int pos = Integer.parseInt(forma.getExcepciones("numRegistros").toString());
					Utilidades.nuevoRegistroMapaGenerico(forma.getExcepciones(),indicesExcepciones,"numRegistros","tiporegistro_","MEM");
					forma.setExcepciones("tipospaciente_"+pos, new ArrayList<HashMap<String, Object>>());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("eliminarExcepcion"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getExcepciones(),forma.getExcepcionesEliminados(),forma.getPosEliminar(),indicesExcepciones,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("guardarGeneral"))
				{
					return this.accionGuardarGeneral(con, forma, mundo, usuario, mapping);
				}
				else if(estado.equals("detalleExcepcion"))
				{
					forma.resetOpTrue();
					forma.setDetalleExcepcionAnterior(false);
					this.accionDetalleExcepcion(con,forma,mundo,usuario,mapping);
					accionCargarPorcentaje(con, forma, mundo);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");			
				}
				else if(estado.equals("detalleExcepcionAnterior"))
				{
					forma.setDetalleExcepcionAnterior(true);
					this.accionDetalleExcepcionAnterior(con,forma,mundo,usuario,mapping);
					accionCargarPorcentaje(con, forma, mundo);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");			
				}
				else if(estado.equals("nuevaAgrupacion"))
				{
					Utilidades.nuevoRegistroMapaGenerico(forma.getAgrupacionArticulos(),indicesAgrupacionArticulos,"numRegistros","tiporegistro_","MEM");
					forma.getAgrupacionArticulos().put("fechavigencia_"+(Utilidades.convertirAEntero(forma.getAgrupacionArticulos().get("numRegistros")+"")-1), forma.getFechaVigencia()+"");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");

				}
				else if(estado.equals("eliminarAgrupacion"))
				{
					int posEliminar=forma.getPosEliminar();
					//eliminar los porcentajes
					if(forma.getAgrupacionArticulos("tiporegistro_"+forma.getPosEliminar()).equals("BD"))
					{
						forma.setCodigoSeccionPocentaje(codigoSeccionAgrupacionArticulos);
						forma.setIndexPorcentaje(forma.getPosEliminar());
						for(int i=0;i<Utilidades.convertirAEntero(forma.getPorcentajeAgrupacionArticulos("numRegistros_"+forma.getCodigoSeccionPocentaje()+"_"+forma.getIndexPorcentaje())+"");i++)
						{
							forma.setPosEliminar(i);
							this.accionEliminarPocentaje(forma.getPorcentajeAgrupacionArticulos(),forma.getPorcentajeAgrupacionArticulosEliminados(),forma,false);						
						}
					}
					forma.setPosEliminar(posEliminar);
					Utilidades.eliminarRegistroMapaGenerico(forma.getAgrupacionArticulos(),forma.getAgrupacionArticulosEliminados(),forma.getPosEliminar(),indicesAgrupacionArticulos,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");	
				}

				else if(estado.equals("nuevoArticulo"))
				{
					String[] indicesTemp={"codigo_","codigoexcepcion_","valorajuste_","nuevatarifa_","tiporegistro_","valorbase_"};
					Utilidades.nuevoRegistroMapaGenerico(forma.getArticulos(),indicesTemp,"numRegistros","tiporegistro_","MEM");
					forma.getArticulos().put("fechavigencia_"+(Utilidades.convertirAEntero(forma.getArticulos().get("numRegistros")+"")-1), forma.getFechaVigencia()+"");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}
				else if(estado.equals("eliminarArticulo"))
				{
					int posEliminar=forma.getPosEliminar();
					//eliminar los porcentajes
					if(forma.getArticulos("tiporegistro_"+forma.getPosEliminar()).equals("BD"))
					{
						forma.setCodigoSeccionPocentaje(codigoSeccionArticulos);
						forma.setIndexPorcentaje(forma.getPosEliminar());
						for(int i=0;i<Utilidades.convertirAEntero(forma.getArticulos("numRegistros_"+forma.getCodigoSeccionPocentaje()+"_"+forma.getIndexPorcentaje())+"");i++)
						{
							forma.setPosEliminar(i);
							this.accionEliminarPocentaje(forma.getArticulos(),forma.getPorcentajeArticulosEliminados(),forma,false);						
						}
					}
					forma.setPosEliminar(posEliminar);
					Utilidades.eliminarRegistroMapaGenerico(forma.getArticulos(),forma.getArticulosEliminados(),forma.getPosEliminar(),indicesArticulos,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");	
				}
				else if(estado.equals("eliminarPorcentaje"))
				{
					switch(forma.getCodigoSeccionPocentaje())
					{
					case codigoSeccionAgrupacionArticulos:
						this.accionEliminarPocentaje(forma.getPorcentajeAgrupacionArticulos(),forma.getPorcentajeAgrupacionArticulosEliminados(),forma,true);
						break;
					case codigoSeccionArticulos:
						this.accionEliminarPocentaje(forma.getPorcentajeArticulos(),forma.getPorcentajeArticulosEliminados(),forma,true);
						break;
					case codigoSeccionAgrupacionServicios:
						this.accionEliminarPocentaje(forma.getPorcentajeAgrupacionServicios(),forma.getPorcentajeAgrupacionServiciosEliminados(),forma,true);
						break;
					case codigoSeccionServicios:
						this.accionEliminarPocentaje(forma.getPorcentajeServicios(),forma.getPorcentajeServiciosEliminados(),forma,true);
						break;
					}

					UtilidadBD.closeConnection(con);
					return mapping.findForward("porcentajes");
				}
				else if(estado.equals("nuevaAgrupacionServicio"))
				{
					Utilidades.nuevoRegistroMapaGenerico(forma.getAgrupacionServicios(),indicesAgrupacionServicio,"numRegistros","tiporegistro_","MEM");
					forma.getAgrupacionServicios().put("fechavigencia_"+(Utilidades.convertirAEntero(forma.getAgrupacionServicios().get("numRegistros")+"")-1), forma.getFechaVigencia()+"");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");

				}
				else if(estado.equals("eliminarAgrupacionServicio"))
				{
					int posEliminar=forma.getPosEliminar();
					//eliminar los porcentajes
					if(forma.getAgrupacionServicios("tiporegistro_"+forma.getPosEliminar()).equals("BD"))
					{
						forma.setCodigoSeccionPocentaje(codigoSeccionAgrupacionServicios);
						forma.setIndexPorcentaje(forma.getPosEliminar());
						for(int i=0;i<Utilidades.convertirAEntero(forma.getAgrupacionServicios("numRegistros_"+forma.getCodigoSeccionPocentaje()+"_"+forma.getIndexPorcentaje())+"");i++)
						{
							forma.setPosEliminar(i);
							this.accionEliminarPocentaje(forma.getAgrupacionServicios(),forma.getAgrupacionServiciosEliminados(),forma,false);						
						}
					}				
					forma.setPosEliminar(posEliminar);
					Utilidades.eliminarRegistroMapaGenerico(forma.getAgrupacionServicios(),forma.getAgrupacionServiciosEliminados(),forma.getPosEliminar(),indicesAgrupacionServicio,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");	
				}
				else if(estado.equals("nuevoServicio"))
				{
					String[] indicesTemp={"codigo_","codigoexcepcion_","valorajuste_","nuevatarifa_","tiporegistro_"};
					Utilidades.nuevoRegistroMapaGenerico(forma.getServicios(),indicesTemp,"numRegistros","tiporegistro_","MEM");
					forma.getServicios().put("fechavigencia_"+(Utilidades.convertirAEntero(forma.getServicios().get("numRegistros")+"")-1), forma.getFechaVigencia()+"");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}
				else if(estado.equals("eliminarServicio"))
				{
					//eliminar los porcentajes
					int posEliminar=forma.getPosEliminar();
					if(forma.getServicios("tiporegistro_"+forma.getPosEliminar()).equals("BD"))
					{
						forma.setCodigoSeccionPocentaje(codigoSeccionServicios);
						forma.setIndexPorcentaje(forma.getPosEliminar());
						for(int i=0;i<Utilidades.convertirAEntero(forma.getServicios("numRegistros_"+forma.getCodigoSeccionPocentaje()+"_"+forma.getIndexPorcentaje())+"");i++)
						{
							forma.setPosEliminar(i);
							this.accionEliminarPocentaje(forma.getServicios(),forma.getServiciosEliminados(),forma,false);						
						}
					}				
					forma.setPosEliminar(posEliminar);
					Utilidades.eliminarRegistroMapaGenerico(forma.getServicios(),forma.getServiciosEliminados(),forma.getPosEliminar(),indicesServicios,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");	
				}
				else if(estado.equals("guardar"))
				{
					this.accionGuardar(con,forma,mundo,usuario);
					//consultar lo que se guardo.
					this.accionDetalleExcepcion(con,forma,mundo,usuario,mapping);

					accionCargarPorcentaje(con, forma, mundo);

					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}
				else if(estado.equals("consultarTipoPaciente"))
				{
					this.accionConsultarTipoPaciente(con,forma,mundo,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("ingresoPorcentajes"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("porcentajes");
				}
				else if(estado.equals("nuevoPorcentaje"))
				{
					switch(forma.getCodigoSeccionPocentaje())
					{
					case codigoSeccionAgrupacionArticulos:
						this.nuevoRegistroPorcentaje(forma.getPorcentajeAgrupacionArticulos(),forma);
						break;
					case codigoSeccionArticulos:
						this.nuevoRegistroPorcentaje(forma.getPorcentajeArticulos(),forma);
						break;
					case codigoSeccionAgrupacionServicios:
						this.nuevoRegistroPorcentaje(forma.getPorcentajeAgrupacionServicios(),forma);
						break;
					case codigoSeccionServicios:
						this.nuevoRegistroPorcentaje(forma.getPorcentajeServicios(),forma);
						break;
					}
					UtilidadBD.closeConnection(con);
					return mapping.findForward("porcentajes");

				}
				else if(estado.equals("cerrarPorcentaje"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("porcentajes");
				}
				else if(estado.equals("anteriores"))
				{
					organizarNoVigentes(con, forma, usuario, mundo);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");				
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("Error con la forma (Revisar StrutsConfig)");
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
	 * @param mapa,HashMap<String, Object> mapaEliminados 
	 * @param forma
	 */
	private void accionEliminarPocentaje(HashMap<String, Object> mapa,HashMap<String, Object> mapaEliminados, ExcepcionesTarifasForm forma,boolean eliminarBD) 
	{
		String indiceNumeroRegistros="numRegistros_"+forma.getCodigoSeccionPocentaje()+"_"+forma.getIndexPorcentaje();
		int numRegMapEliminados=Utilidades.convertirAEntero(mapaEliminados.get(indiceNumeroRegistros)+"",true);
		int ultimaPosMapa=(Integer.parseInt(mapa.get(indiceNumeroRegistros)+"")-1);
		int posEliminar=forma.getPosEliminar();
		//cargamos el mapa de eliminados solo para registro de la bd.
		if((mapa.get("tiporegistro_"+forma.getCodigoSeccionPocentaje()+"_"+forma.getIndexPorcentaje()+"_"+posEliminar)+"").trim().equalsIgnoreCase("BD")&&eliminarBD)
		{
			for(int i=0;i<indicesPorcentajes.length;i++)
			{
				mapaEliminados.put(indicesPorcentajes[i]+forma.getCodigoSeccionPocentaje()+"_"+forma.getIndexPorcentaje()+"_"+numRegMapEliminados, mapa.get(indicesPorcentajes[i]+forma.getCodigoSeccionPocentaje()+"_"+forma.getIndexPorcentaje()+"_"+posEliminar));
			}
			mapaEliminados.put(indiceNumeroRegistros, (numRegMapEliminados+1));
		}
		//acomodar los registros del mapa en su nueva posicion
		for(int i=posEliminar;i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indicesPorcentajes.length;j++)
			{
				mapa.put(indicesPorcentajes[j]+forma.getCodigoSeccionPocentaje()+"_"+forma.getIndexPorcentaje()+"_"+i,mapa.get(indicesPorcentajes[j]+forma.getCodigoSeccionPocentaje()+"_"+forma.getIndexPorcentaje()+"_"+(i+1)));
			}
		}

		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indicesPorcentajes.length;j++)
		{
			mapa.remove(indicesPorcentajes[j]+forma.getCodigoSeccionPocentaje()+"_"+forma.getIndexPorcentaje()+"_"+ultimaPosMapa);
		}
		
		//ahora actualizamo el numero de registros en el mapa.
		mapa.put(indiceNumeroRegistros,ultimaPosMapa);
	}



	/**
	 * 
	 * @param forma 
	 * @param porcentajeAgrupacionArticulos
	 */
	private void nuevoRegistroPorcentaje(HashMap<String, Object> mapa, ExcepcionesTarifasForm forma) 
	{
		int tamanio=Utilidades.convertirAEntero((mapa.get("numRegistros_"+forma.getCodigoSeccionPocentaje()+"_"+forma.getIndexPorcentaje())+""),true);
		for(int i=0;i<indicesPorcentajes.length;i++)
		{
			mapa.put(indicesPorcentajes[i]+forma.getCodigoSeccionPocentaje()+"_"+forma.getIndexPorcentaje()+"_"+tamanio, "");
		}
		mapa.put("tiporegistro_"+forma.getCodigoSeccionPocentaje()+"_"+forma.getIndexPorcentaje()+"_"+tamanio, "MEM");
		mapa.put("numRegistros_"+forma.getCodigoSeccionPocentaje()+"_"+forma.getIndexPorcentaje(), (tamanio+1));
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 */
	private void accionCargarPorcentaje(Connection con, ExcepcionesTarifasForm forma, ExcepcionesTarifas mundo) 
	{
		String codigosAgrupacionArticulos=ConstantesBD.codigoNuncaValido+"";
		String codigosArticulos=ConstantesBD.codigoNuncaValido+"";
		String codigosAgrupacionServicios=ConstantesBD.codigoNuncaValido+"";
		String codigosServicios=ConstantesBD.codigoNuncaValido+"";
		for(int i=0;i<Integer.parseInt(forma.getAgrupacionArticulos("numRegistros")+"");i++)
			codigosAgrupacionArticulos=codigosAgrupacionArticulos+ConstantesBD.separadorSplit+forma.getAgrupacionArticulos("codigo_"+i)+"";

		for(int i=0;i<Integer.parseInt(forma.getArticulos("numRegistros")+"");i++)
			codigosArticulos=codigosArticulos+ConstantesBD.separadorSplit+forma.getArticulos("codigo_"+i)+"";

		for(int i=0;i<Integer.parseInt(forma.getAgrupacionServicios("numRegistros")+"");i++)
			codigosAgrupacionServicios=codigosAgrupacionServicios+ConstantesBD.separadorSplit+forma.getAgrupacionServicios("codigo_"+i)+"";
		
		for(int i=0;i<Integer.parseInt(forma.getServicios("numRegistros")+"");i++)
			codigosServicios=codigosServicios+ConstantesBD.separadorSplit+forma.getServicios("codigo_"+i)+"";
		
		
		///los mapas se manejan de la siguiente forma.  indice_codigoseccion_posicionenseccion_contadorregistro.
		forma.setPorcentajeAgrupacionArticulos(mundo.consultarPorcentaje(con,codigosAgrupacionArticulos,codigoSeccionAgrupacionArticulos));
		forma.setPorcentajeArticulos(mundo.consultarPorcentaje(con,codigosArticulos,codigoSeccionArticulos));
		forma.setPorcentajeAgrupacionServicios(mundo.consultarPorcentaje(con,codigosAgrupacionServicios,codigoSeccionAgrupacionServicios));
		forma.setPorcentajeServicios(mundo.consultarPorcentaje(con,codigosServicios,codigoSeccionServicios));


	}


	/**
	 * Consultar tipos de pacientes según la vía de ingreso
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionConsultarTipoPaciente(Connection con, ExcepcionesTarifasForm forma, ExcepcionesTarifas mundo, UsuarioBasico usuario) {
		//	se carga el tipo de paciente
		forma.setExcepciones("tipospaciente_"+forma.getPosMapa(),UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con,forma.getExcepciones("viaingreso_"+forma.getPosMapa()).toString()));
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardar(Connection con, ExcepcionesTarifasForm forma, ExcepcionesTarifas mundo, UsuarioBasico usuario) 
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		if(transaccion)
			transaccion=this.accionGuardarAgrupacionArticulos(con,forma,mundo,usuario,transaccion);
		if(transaccion)
			transaccion=this.accionGuardarArticulos(con,forma,mundo,usuario,transaccion);
		if(transaccion)
			transaccion=this.accionGuardarAgrupacionServicios(con,forma,mundo,usuario,transaccion);
		if(transaccion)
			transaccion=this.accionGuardarServicios(con,forma,mundo,usuario,transaccion);
		forma.setOperacionTrue(transaccion);
		if(transaccion)
		{
			
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}	
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param transaccion
	 * @return
	 */
	private boolean accionGuardarServicios(Connection con, ExcepcionesTarifasForm forma, ExcepcionesTarifas mundo, UsuarioBasico usuario, boolean transaccion) 
	{
		int codigoServicio=ConstantesBD.codigoNuncaValido;
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getServiciosEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarServicios(con,forma.getServiciosEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getServiciosEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logExcepcionesTarifasCodigo,indicesServicios);
				transaccion=true;
			}
		}
		for(int i=0;i<Integer.parseInt(forma.getServicios("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getServicios("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getServicios(),mundo.consultarServiciosLLave(con, forma.getServicios("codigo_"+i)+""),i,usuario,indicesServicios))
			{
				HashMap vo=new HashMap();
				vo.put("codigo", forma.getServicios("codigo_"+i)+"");
				vo.put("servicio", forma.getServicios("codigoServicio_"+i));
				vo.put("valorAjuste", forma.getServicios("valorajuste_"+i));
				vo.put("suma", forma.getServicios("suma_"+i));
				vo.put("nuevaTarifa", forma.getServicios("nuevatarifa_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getServicios("fechavigencia_"+i)+""));
				transaccion=mundo.modificarServicios(con, vo);
				

			}
			
			if((forma.getServicios("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD"))
			{
				this.guardarPorcentajes(con,forma.getPorcentajeServicios(),forma.getPorcentajeServiciosEliminados(),i,codigoSeccionServicios,forma.getServicios("codigo_"+i)+"",usuario,mundo);
			}
			
			//insertar
			else if((forma.getServicios("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigoExcepcion", forma.getExcepciones("codigo_"+forma.getIndexSeleccionado())+"");
				vo.put("servicio", forma.getServicios("codigoServicio_"+i));
				vo.put("valorAjuste", forma.getServicios("valorajuste_"+i));
				vo.put("suma", forma.getServicios("suma_"+i));
				vo.put("nuevaTarifa", forma.getServicios("nuevatarifa_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getServicios("fechavigencia_"+i)+""));
				codigoServicio=mundo.insertarServicios(con, vo);
				if(codigoServicio>0)
				{
					this.guardarPorcentajes(con,forma.getPorcentajeServicios(),forma.getPorcentajeServiciosEliminados(),i,codigoSeccionServicios,codigoServicio+"",usuario,mundo);
				}
				else
				{
					transaccion=false;
				}
			}
			
		}
		return transaccion;
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param transaccion
	 * @return
	 */
	private boolean accionGuardarAgrupacionServicios(Connection con, ExcepcionesTarifasForm forma, ExcepcionesTarifas mundo, UsuarioBasico usuario, boolean transaccion) 
	{
		int codigoAgrupacionServicio=ConstantesBD.codigoNuncaValido;
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getAgrupacionServiciosEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarAgrupacionServicios(con,forma.getAgrupacionServiciosEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getAgrupacionServiciosEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logExcepcionesTarifasCodigo,indicesAgrupacionServicio);
				transaccion=true;
			}
		}
		for(int i=0;i<Integer.parseInt(forma.getAgrupacionServicios("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getAgrupacionServicios("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getAgrupacionServicios(),mundo.consultarAgrupacionServiciosLLave(con, forma.getAgrupacionServicios("codigo_"+i)+""),i,usuario,indicesAgrupacionServicio))
			{
				HashMap vo=new HashMap();
				vo.put("codigo", forma.getAgrupacionServicios("codigo_"+i)+"");
				vo.put("tipopos", forma.getAgrupacionServicios("tipopos_"+i));
				vo.put("gruposervicio", forma.getAgrupacionServicios("gruposervicio_"+i));
				vo.put("tiposervicio", forma.getAgrupacionServicios("tiposervicio_"+i));
				vo.put("especialidad", forma.getAgrupacionServicios("especialidad_"+i));
				vo.put("valorAjuste", forma.getAgrupacionServicios("valorajuste_"+i));
				vo.put("suma", forma.getAgrupacionServicios("suma_"+i));
				vo.put("nuevaTarifa", forma.getAgrupacionServicios("nuevatarifa_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getAgrupacionServicios("fechavigencia_"+i)+""));
				transaccion=mundo.modificarAgrupacionServicios(con, vo);
				

			}
			
			if((forma.getAgrupacionServicios("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD"))
			{
				this.guardarPorcentajes(con,forma.getPorcentajeAgrupacionServicios(),forma.getPorcentajeAgrupacionServiciosEliminados(),i,codigoSeccionAgrupacionServicios,forma.getAgrupacionServicios("codigo_"+i)+"",usuario,mundo);
			}

			//insertar
			else if((forma.getAgrupacionServicios("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigoExcepcion", forma.getExcepciones("codigo_"+forma.getIndexSeleccionado())+"");
				vo.put("tipopos", forma.getAgrupacionServicios("tipopos_"+i));
				vo.put("gruposervicio", forma.getAgrupacionServicios("gruposervicio_"+i));
				vo.put("tiposervicio", forma.getAgrupacionServicios("tiposervicio_"+i));
				vo.put("especialidad", forma.getAgrupacionServicios("especialidad_"+i));
				vo.put("valorAjuste", forma.getAgrupacionServicios("valorajuste_"+i));
				vo.put("suma", forma.getAgrupacionServicios("suma_"+i));
				vo.put("nuevaTarifa", forma.getAgrupacionServicios("nuevatarifa_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getAgrupacionServicios("fechavigencia_"+i)+""));
				codigoAgrupacionServicio=mundo.insertarAgrupacionServicios(con, vo);
				if(codigoAgrupacionServicio>0)
				{
					this.guardarPorcentajes(con,forma.getPorcentajeAgrupacionServicios(),forma.getPorcentajeAgrupacionServiciosEliminados(),i,codigoSeccionAgrupacionServicios,codigoAgrupacionServicio+"",usuario,mundo);
				}
				else
				{
					transaccion=false;
				}
			}
		}
		return transaccion;
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param transaccion
	 * @return
	 */
	private boolean accionGuardarArticulos(Connection con, ExcepcionesTarifasForm forma, ExcepcionesTarifas mundo, UsuarioBasico usuario, boolean transaccion) 
	{
		int codigoArticulo=ConstantesBD.codigoNuncaValido;
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getArticulosEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarArticulos(con,forma.getArticulosEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getArticulosEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logExcepcionesTarifasCodigo,indicesArticulos);
				transaccion=true;
			}
		}
		for(int i=0;i<Integer.parseInt(forma.getArticulos("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getArticulos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getArticulos(),mundo.consultarArticulosLLave(con, forma.getArticulos("codigo_"+i)+""),i,usuario,indicesArticulos))
			{
				HashMap vo=new HashMap();
				vo.put("codigo", forma.getArticulos("codigo_"+i)+"");
				vo.put("articulo", forma.getArticulos("codigoArticulo_"+i));
				vo.put("valorAjuste", forma.getArticulos("valorajuste_"+i));
				vo.put("baseExcepcion", forma.getArticulos("baseexcepcion_"+i));
				vo.put("suma", forma.getArticulos("suma_"+i));
				vo.put("nuevaTarifa", forma.getArticulos("nuevatarifa_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getArticulos("fechavigencia_"+i)+""));
				vo.put("valorBase", forma.getArticulos("valorbase_"+i));
				transaccion=mundo.modificarArticulos(con, vo);
				
			}
			
			if((forma.getArticulos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD"))
			{
				this.guardarPorcentajes(con,forma.getPorcentajeArticulos(),forma.getPorcentajeArticulosEliminados(),i,codigoSeccionArticulos,forma.getArticulos("codigo_"+i)+"",usuario,mundo);
			}
			//insertar
			else if((forma.getArticulos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigoExcepcion", forma.getExcepciones("codigo_"+forma.getIndexSeleccionado())+"");
				vo.put("articulo", forma.getArticulos("codigoArticulo_"+i));
				vo.put("valorAjuste", forma.getArticulos("valorajuste_"+i));
				vo.put("baseExcepcion", forma.getArticulos("baseexcepcion_"+i));
				vo.put("suma", forma.getArticulos("suma_"+i));
				vo.put("nuevaTarifa", forma.getArticulos("nuevatarifa_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getArticulos("fechavigencia_"+i)+""));
				vo.put("valorBase", forma.getArticulos("valorbase_"+i));
				codigoArticulo=mundo.insertarArticulos(con, vo);
				if(codigoArticulo>0)
				{
					this.guardarPorcentajes(con,forma.getPorcentajeArticulos(),forma.getPorcentajeArticulosEliminados(),i,codigoSeccionArticulos,codigoArticulo+"",usuario,mundo);
				}
				else
				{
					transaccion=false;
				}
			}
			
		}
		return transaccion;
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param transaccion
	 * @return
	 */
	private boolean accionGuardarAgrupacionArticulos(Connection con, ExcepcionesTarifasForm forma, ExcepcionesTarifas mundo, UsuarioBasico usuario, boolean transaccion) 
	{
		
		int codigoAgrupacionArticulo=ConstantesBD.codigoNuncaValido;
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getAgrupacionArticulosEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarAgrupacionArticulos(con,forma.getAgrupacionArticulosEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getAgrupacionArticulosEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logExcepcionesTarifasCodigo,indicesAgrupacionArticulos);
				transaccion=true;
			}
		}
		
		for(int i=0;i<Integer.parseInt(forma.getAgrupacionArticulos("numRegistros")+"");i++)
		{
			if((forma.getAgrupacionArticulos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getAgrupacionArticulos(),mundo.consultarAgrupacionArticulosLLave(con, forma.getAgrupacionArticulos("codigo_"+i)+""),i,usuario,indicesAgrupacionArticulos))
			{
				HashMap vo=new HashMap();
				vo.put("codigo", forma.getAgrupacionArticulos("codigo_"+i)+"");
				vo.put("clase", forma.getAgrupacionArticulos("clase_"+i));
				vo.put("grupo", forma.getAgrupacionArticulos("grupo_"+i));
				vo.put("subgrupo", forma.getAgrupacionArticulos("subgrupo_"+i));
				vo.put("naturaleza", forma.getAgrupacionArticulos("naturaleza_"+i));
				vo.put("valorAjuste", forma.getAgrupacionArticulos("valorajuste_"+i));
				vo.put("baseExcepcion", forma.getAgrupacionArticulos("baseexcepcion_"+i));
				vo.put("suma", forma.getAgrupacionArticulos("suma_"+i));
				vo.put("nuevaTarifa", forma.getAgrupacionArticulos("nuevatarifa_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getAgrupacionArticulos("fechavigencia_"+i)+""));
				vo.put("valorBase", forma.getAgrupacionArticulos("valorbase_"+i));
				transaccion=mundo.modificarAgrupacionArticulos(con, vo);
				
			}
			if((forma.getAgrupacionArticulos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD"))
			{
				this.guardarPorcentajes(con,forma.getPorcentajeAgrupacionArticulos(),forma.getPorcentajeAgrupacionArticulosEliminados(),i,codigoSeccionAgrupacionArticulos,forma.getAgrupacionArticulos("codigo_"+i)+"",usuario,mundo);
			}

			//insertar
			else if((forma.getAgrupacionArticulos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigoExcepcion", forma.getExcepciones("codigo_"+forma.getIndexSeleccionado())+"");
				vo.put("clase", forma.getAgrupacionArticulos("clase_"+i));
				vo.put("grupo", forma.getAgrupacionArticulos("grupo_"+i));
				vo.put("subgrupo", forma.getAgrupacionArticulos("subgrupo_"+i));
				vo.put("naturaleza", forma.getAgrupacionArticulos("naturaleza_"+i));
				vo.put("subgrupo", forma.getAgrupacionArticulos("subgrupo_"+i));
				vo.put("naturaleza", forma.getAgrupacionArticulos("naturaleza_"+i));
				vo.put("institucion",usuario.getCodigoInstitucion());
				vo.put("valorAjuste", forma.getAgrupacionArticulos("valorajuste_"+i));
				vo.put("baseExcepcion", forma.getAgrupacionArticulos("baseexcepcion_"+i));
				vo.put("suma", forma.getAgrupacionArticulos("suma_"+i));
				vo.put("nuevaTarifa", forma.getAgrupacionArticulos("nuevatarifa_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getAgrupacionArticulos("fechavigencia_"+i)+""));
				vo.put("valorBase", forma.getAgrupacionArticulos("valorbase_"+i));
				codigoAgrupacionArticulo=mundo.insertarAgrupacionArticulos(con, vo);
				if(codigoAgrupacionArticulo>0)
				{
					this.guardarPorcentajes(con,forma.getPorcentajeAgrupacionArticulos(),forma.getPorcentajeAgrupacionArticulosEliminados(),i,codigoSeccionAgrupacionArticulos,codigoAgrupacionArticulo+"",usuario,mundo);
				}
				else
				{
					transaccion=false;
				}
			}
		}
		
		return transaccion;
	}

	/**
	 * 
	 * @param con 
	 * @param mapaEliminados 
	 * @param codigoSeccion 
	 * @param porcentajeAgrupacionArticulos
	 * @param i
	 * @param usuario
	 * @param mundo 
	 */
	private boolean guardarPorcentajes(Connection con, HashMap<String, Object> mapa, HashMap<String, Object> mapaEliminados, int posRegistroExcepcion, int codigoSeccion,String codigoPKExcepcion, UsuarioBasico usuario, ExcepcionesTarifas mundo) 
	{
		boolean resultado=true;;
		String indiceNumeroRegistros="numRegistros_"+codigoSeccion+"_"+posRegistroExcepcion;
		
		logger.info("GUARDAR PORCENTAJES");
		Utilidades.imprimirMapa(mapa);

		String[] indicesPorcentajesLog={"codigo_"+codigoSeccion+"_"+posRegistroExcepcion+"_","porcentaje_"+codigoSeccion+"_"+posRegistroExcepcion+"_","suma_"+codigoSeccion+"_"+posRegistroExcepcion+"_","codigoexcepcion_"+codigoSeccion+"_"+posRegistroExcepcion+"_"};

		///
		for(int i=0;i<Utilidades.convertirAEntero(mapaEliminados.get(indiceNumeroRegistros)+"",true);i++)
		{
			logger.info("------->"+mapaEliminados.get("codigo_"+codigoSeccion+"_"+posRegistroExcepcion+"_"+i));
			mundo.eliminarPorcentajes(con,mapaEliminados.get("codigo_"+codigoSeccion+"_"+posRegistroExcepcion+"_"+i)+"",codigoSeccion);
			Utilidades.generarLogGenerico(mapaEliminados, new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logExcepcionesTarifasCodigo,indicesPorcentajesLog);
		}
		
		
		int numRegMap=Utilidades.convertirAEntero(mapa.get(indiceNumeroRegistros)+"",true);
		for(int i=0;i<numRegMap;i++)
		{
			

			logger.info("tipo registro: ("+codigoSeccion+"_"+posRegistroExcepcion+"_"+i+")-->"+(mapa.get("tiporegistro_"+codigoSeccion+"_"+posRegistroExcepcion+"_"+i)));

			if((mapa.get("tiporegistro_"+codigoSeccion+"_"+posRegistroExcepcion+"_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,mapa,mundo.consultarPorcentajeLLave(con,Utilidades.convertirAEntero(mapa.get("codigo_"+codigoSeccion+"_"+posRegistroExcepcion+"_"+i)+""),codigoSeccion,posRegistroExcepcion),i,usuario,indicesPorcentajesLog))
			{
				logger.info("MODIFICANDO EL PORCENTAJE");
				HashMap vo=new HashMap();
				vo.put("codigo", mapa.get("codigo_"+codigoSeccion+"_"+posRegistroExcepcion+"_"+i)+"");
				vo.put("codigoPKExcepcion", codigoPKExcepcion);
				vo.put("porcentaje", mapa.get("porcentaje_"+codigoSeccion+"_"+posRegistroExcepcion+"_"+i));
				vo.put("suma", mapa.get("suma_"+codigoSeccion+"_"+posRegistroExcepcion+"_"+i));
				vo.put("prioridad", mapa.get("prioridad_"+codigoSeccion+"_"+posRegistroExcepcion+"_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				resultado=mundo.modificarPorcentaje(con, vo,codigoSeccion);
			}
			else if(!(mapa.get("tiporegistro_"+codigoSeccion+"_"+posRegistroExcepcion+"_"+i)+"").trim().equalsIgnoreCase("BD"))
			{
				logger.info("INSERTANDO EL PORCENTAJE  ---> ");
				Utilidades.imprimirMapa(mapa);
				
				HashMap vo=new HashMap();
				vo.put("codigoPKExcepcion", codigoPKExcepcion);
				vo.put("porcentaje", mapa.get("porcentaje_"+codigoSeccion+"_"+posRegistroExcepcion+"_"+i));
				vo.put("suma", mapa.get("suma_"+codigoSeccion+"_"+posRegistroExcepcion+"_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("prioridad", mapa.get("prioridad_"+codigoSeccion+"_"+posRegistroExcepcion+"_"+i));
				resultado=mundo.insertarPorcentaje(con, vo,codigoSeccion);
				if(resultado)
					mapa.put("tiporegistro_"+codigoSeccion+"_"+posRegistroExcepcion+"_"+i,"BD");
			}
		}
		return resultado;
	}



	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private void accionDetalleExcepcion(Connection con, ExcepcionesTarifasForm forma, ExcepcionesTarifas mundo, UsuarioBasico usuario, ActionMapping mapping) 
	{
		forma.resetMapasEliminacion();
		forma.setAgrupacionArticulos(mundo.consultarAgrupacionArticulos(con,forma.getExcepciones("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setArticulos(mundo.consultarArticulos(con,forma.getExcepciones("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setAgrupacionServicios(mundo.consultarAgrupacionServicios(con,forma.getExcepciones("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setServicios(mundo.consultarServicios(con,forma.getExcepciones("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setFechaVigencia(""+forma.getExcepciones().get("fechavigencia_"+forma.getIndexSeleccionado()));
		forma.setNombreViaIngreso(""+forma.getExcepciones().get("nomviaingreso_"+forma.getIndexSeleccionado()));
		forma.setNombreCentroAtencion(""+forma.getExcepciones().get("nomcentroatencion_"+forma.getIndexSeleccionado()));
		forma.setNombreTipoComplejidad(""+forma.getExcepciones().get("nomtipocomplejidad_"+forma.getIndexSeleccionado()));
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private void accionDetalleExcepcionAnterior(Connection con, ExcepcionesTarifasForm forma, ExcepcionesTarifas mundo, UsuarioBasico usuario, ActionMapping mapping) 
	{
		forma.resetMapasEliminacion();
		forma.setAgrupacionArticulos(mundo.consultarAgrupacionArticulos(con,forma.getExcepcionesAnteriores().get("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setArticulos(mundo.consultarArticulos(con,forma.getExcepcionesAnteriores().get("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setAgrupacionServicios(mundo.consultarAgrupacionServicios(con,forma.getExcepcionesAnteriores().get("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setServicios(mundo.consultarServicios(con,forma.getExcepcionesAnteriores().get("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setFechaVigencia(""+forma.getExcepcionesAnteriores().get("fechavigencia_"+forma.getIndexSeleccionado()));
		forma.setNombreViaIngreso(""+forma.getExcepcionesAnteriores().get("nomviaingreso_"+forma.getIndexSeleccionado()));
		forma.setNombreCentroAtencion(""+forma.getExcepcionesAnteriores().get("nomcentroatencion_"+forma.getIndexSeleccionado()));
		forma.setNombreTipoComplejidad(""+forma.getExcepcionesAnteriores().get("nomtipocomplejidad_"+forma.getIndexSeleccionado()));
	}
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionGuardarGeneral(Connection con, ExcepcionesTarifasForm forma, ExcepcionesTarifas mundo, UsuarioBasico usuario, ActionMapping mapping) 
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getExcepcionesEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarExepciones(con,forma.getExcepcionesEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getExcepcionesEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logExcepcionesTarifasCodigo,indicesExcepciones);
				transaccion=true;
			}
		}
		
		for(int i=0;i<Integer.parseInt(forma.getExcepciones("numRegistros")+"");i++)
		{				

			//modificar
			String[] indicesExcepcionesLog={"codigo_","viaingreso_","nomviaingreso_","tipopaciente_","nomtipopaciente_","tipocomplejidad_","nomtipocomplejidad_","tiporegistro_","fechavigencia_","observaciones_","numdetalles_", "codcentroatencion_", "nomcentroatencion_"};
			if((forma.getExcepciones("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getExcepciones(),mundo.consultarExcepcionLLave(con, forma.getExcepciones("codigo_"+i)+""),i,usuario,indicesExcepcionesLog))
			{
				HashMap vo=new HashMap();
				vo.put("codigo", forma.getExcepciones("codigo_"+i)+"");
				vo.put("codigoContrato", forma.getContrato());
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("viaIngreso", forma.getExcepciones("viaingreso_"+i));
				vo.put("tipoPaciente", forma.getExcepciones("tipopaciente_"+i));
				vo.put("tipoComplejidad", forma.getExcepciones("tipocomplejidad_"+i));
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getExcepciones("fechavigencia_"+i)+""));
				vo.put("observaciones", forma.getExcepciones("observaciones_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("codcentroatencion", forma.getExcepciones("codcentroatencion_"+i));
				transaccion=mundo.modificarExcepcion(con, vo);
			}
			//insertar
			else if((forma.getExcepciones("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigoContrato", forma.getContrato());
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("viaIngreso", forma.getExcepciones("viaingreso_"+i));
				vo.put("tipoPaciente", forma.getExcepciones("tipopaciente_"+i));
				vo.put("tipoComplejidad", forma.getExcepciones("tipocomplejidad_"+i));
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getExcepciones("fechavigencia_"+i)+""));
				vo.put("observaciones", forma.getExcepciones("observaciones_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("codcentroatencion", forma.getExcepciones("codcentroatencion_"+i));
				transaccion=mundo.insertarExcepcion(con, vo);
			}
		}
		forma.setOperacionTrue(transaccion);
		if(transaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}	
		return this.accionCargarInfo(con,forma,mundo,usuario,mapping);
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionCargarInfo(Connection con, ExcepcionesTarifasForm forma, ExcepcionesTarifas mundo, UsuarioBasico usuario, ActionMapping mapping) 
	{
		HashMap clean = new HashMap ();
		clean.put("numRegistros", 0);
		forma.setExcepciones(clean);
		forma.setExcepcionesAnteriores(clean);
		//incializar la informacion de los select
		forma.setSelViasIngreso((Vector<InfoDatosString>)Utilidades.obtenerViasIngreso(con,true));
		forma.setSelCentrosAtencion(Utilidades.obtenerCentrosAtencion(usuario.getCodigoInstitucionInt()));
		forma.setSelTiposComplejidad((Vector<InfoDatosString>)Utilidades.obtenerTiposComplejidad(con));
						
		HashMap tmp= mundo.obtenerExcepciones(con,usuario.getCodigoInstitucionInt(),forma.getContrato(),true,true);
		
		///////////////////////////////////////////////////
			organizarVigentes(tmp, forma);
		///////////////////////////////////////////////////
			
			
	
		for(int i=0;i<Integer.parseInt(forma.getExcepciones("numRegistros").toString());i++)
			forma.setExcepciones("tipospaciente_"+i, UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con, forma.getExcepciones("viaingreso_"+i).toString()));
			
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	
	
	public static void  organizarVigentes (HashMap mapa , ExcepcionesTarifasForm forma)
	{
		logger.info("\n ente a organizarVigentes "+mapa);
		
		HashMap tmp = new HashMap ();
		tmp.put("numRegistros", 0);
		forma.setExcepcionesAnteriores(tmp);
		int numReg=Utilidades.convertirAEntero(mapa.get("numRegistros")+"");
		String mayor="";
		int index=0,indexAnt=ConstantesBD.codigoNuncaValido,index2=0;
		///////////////////////////////////////////
		//se inicializan los datos
		forma.setExcepciones("numRegistros", 0);
			///////////////////////////////////////////
		boolean igual=false, acabo = false;
		String viaIngresoAnt="",tipoPacienteAnt="";
		
		for (int i=0;i<numReg;i++)
		{				
			if (numReg==1)
			{
				logger.info("\n -- 1-- ");
				forma.setExcepciones(Listado.copyMapOnIndexMap(mapa, forma.getExcepciones(), indicesExcepciones, index,i));
				index++;
				igual=true;
				forma.setExcepciones("vigente_"+i, ConstantesBD.acronimoSi);
				
			}
			else
			{
				if(i == 0)
					indexAnt = i;				
				else				
					indexAnt=(i-1);
				
				if ((mapa.get("viaingreso_"+i)+"").equals(mapa.get("viaingreso_"+indexAnt)+"") &&
						(mapa.get("codcentroatencion_"+i)+"").equals(mapa.get("codcentroatencion_"+indexAnt)+"") &&
						(mapa.get("tipopaciente_"+i)+"").equals(mapa.get("tipopaciente_"+indexAnt)+""))
				{
					if(!acabo)
					{
						//se mira si la fecha de vigencia es mayor o igual a la fecha del sistema
						if (UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(), mapa.get("fechavigencia_"+i)+""))
						{
							if (UtilidadFecha.getFechaActual().equals(mapa.get("fechavigencia_"+i)+""))
							{
								igual = true;
								acabo = true;
							}
							
							logger.info("\n 1 mayor o igual mapa  "+mapa.get("tipopaciente_"+i)+" >> "+mapa.get("viaingreso_"+i)+" >> "+mapa.get("fechavigencia_"+i)+" >> igual "+igual+" >> acabo  "+acabo);
							forma.setExcepciones(Listado.copyMapOnIndexMap(mapa, forma.getExcepciones(), indicesExcepciones, index,i));
							index++;
							forma.setExcepciones("vigente_"+i, ConstantesBD.acronimoSi);
						}
						else
						{
							if(!igual)
							{
								forma.setExcepciones(Listado.copyMapOnIndexMap(mapa, forma.getExcepciones(), indicesExcepciones, index,i));
								index++;
								acabo = true;
								logger.info("\n 2 mayor o igual mapa  "+mapa.get("tipopaciente_"+i)+" >> "+mapa.get("viaingreso_"+i)+" >> "+mapa.get("fechavigencia_"+i)+" >> igual "+igual+" >> acabo  "+acabo);
								forma.setExcepciones("vigente_"+i, ConstantesBD.acronimoSi);
							}
						}
					}
					
				}
				else
				{
					igual = false;
					acabo = false;
					
					//se mira si la fecha de vigencia es mayor o igual a la fecha del sistema
					if (UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(), mapa.get("fechavigencia_"+i)+""))
					{
						if (UtilidadFecha.getFechaActual().equals(mapa.get("fechavigencia_"+i)+""))
						{
							igual = true;
							acabo = true;
						}
						
						logger.info("\n 3 mayor o igual mapa  "+mapa.get("tipopaciente_"+i)+" >> "+mapa.get("viaingreso_"+i)+" >> "+mapa.get("fechavigencia_"+i)+" >> igual "+igual+" >> acabo  "+acabo);
						forma.setExcepciones(Listado.copyMapOnIndexMap(mapa, forma.getExcepciones(), indicesExcepciones, index,i));
						index++;
						forma.setExcepciones("vigente_"+i, ConstantesBD.acronimoSi);
					}
					else
					{
						if(!igual)
						{
							forma.setExcepciones(Listado.copyMapOnIndexMap(mapa, forma.getExcepciones(), indicesExcepciones, index,i));
							index++;
							acabo = true;
							logger.info("\n 4 mayor o igual mapa  "+mapa.get("tipopaciente_"+i)+" >> "+mapa.get("viaingreso_"+i)+" >> "+mapa.get("fechavigencia_"+i)+" >> igual "+igual+" >> acabo  "+acabo);
							forma.setExcepciones("vigente_"+i, ConstantesBD.acronimoSi);
						}
					}
				}
			}
		}
		
	}
	
	
	public static void organizarNoVigentes ( Connection connection, ExcepcionesTarifasForm forma, UsuarioBasico usuario,ExcepcionesTarifas mundo)
	{
		
		logger.info("\n entre a organizarNoVigentes  -->"+forma.getExcepciones());
		HashMap tmp= mundo.obtenerExcepciones(connection,usuario.getCodigoInstitucionInt(),forma.getContrato(),true,true);
		int numReg= Utilidades.convertirAEntero(forma.getExcepciones("numRegistros")+"");
		int numReg2=Utilidades.convertirAEntero(tmp.get("numRegistros")+"");
		HashMap noVig= new HashMap ();
		noVig.put("numRegistros", 0); 
		forma.setExcepcionesAnteriores(noVig);
		boolean esta=false;
		int index=0;
		for (int j=0;j<numReg2;j++)
		{
			esta=false;
			for (int i=0;i<numReg;i++)
			{
				
				if ((forma.getExcepciones("codigo_"+i)+"").equals(tmp.get("codigo_"+j)+""))
				{
					//logger.info("\n \n ########################  esta @################# ");
					esta=true;
				}
			}
			
			if (!esta)
			{
				forma.setExcepcionesAnteriores(Listado.copyMapOnIndexMap(tmp, forma.getExcepcionesAnteriores(), indicesExcepciones, index,j));
				index++;
			}
		}
	}
	

	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @param mapaTemp
	 * @param pos
	 * @param usuario
	 * @param indices 
	 * @return
	 */
	private boolean existeModificacion(Connection con, HashMap mapa, HashMap mapaTemp, int pos, UsuarioBasico usuario, String[] indices)
	{
		Utilidades.imprimirMapa(mapa);
		Utilidades.imprimirMapa(mapaTemp);
		boolean resultado=false;
		for(int i=0;i<indices.length;i++)
		{
			if(mapaTemp.containsKey(indices[i]+"0")&&mapa.containsKey(indices[i]+""+pos))
			{
				if(!((mapaTemp.get(indices[i]+"0")+"").trim().equals((mapa.get(indices[i]+""+pos)+"").trim())))
				{
					Utilidades.generarLogGenerico(mapa, mapaTemp, usuario.getLoginUsuario(), false, pos,ConstantesBD.logExcepcionesTarifasCodigo,indices);
					resultado=true;
				}
			}
		}
		logger.info("diferentes -->"+resultado);
		return resultado;
	}

}
