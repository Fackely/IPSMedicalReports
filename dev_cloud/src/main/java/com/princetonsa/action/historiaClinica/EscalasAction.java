package com.princetonsa.action.historiaClinica;

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
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.actionform.historiaClinica.EscalasForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.Escalas;

public class EscalasAction extends Action 
{

	
	/**
	 * 
	 */
	Logger logger =Logger.getLogger(EscalasAction.class);
	
	/**
	 * 
	 */
	private static String[] indicesSecciones={"nombreseccion_","tiporespuesta_","activo_","tiporegistro_"};
	
	/**
	 * 
	 */
	private static String[] indicesCampos={"nombrecampo_","nombrecampoantiguo_","indrequerido_","valorminimo_","tiporegistro_","valormaximo_","obsevreq_","activo_","codigopkcamposseccion_","codigopkcampo_"};
	
	/**
	 * 
	 */
	private static String[] indicesFactores={"codigofactor_","nombrefactor_","valorinicial_","tiporegistro_","valorfinal_","activofactor_"};
	
	
	
	/**
	 * Método excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{

		Connection con = null;

		try {
			if (form instanceof EscalasForm) 
			{
				EscalasForm forma=(EscalasForm) form;

				String estado=forma.getEstado();

				logger.info("Estado -->"+estado);

				con = UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());

				Escalas mundo=new Escalas();

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
					forma.setMapaConsultaEscalas(mundo.consultaEscalas(con));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("nuevaEscala"))
				{
					//forma.reset();
					forma.resetMensaje();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("ingreso");
				}
				else if(estado.equals("volverPrincipal"))
				{
					forma.reset();
					forma.setMapaConsultaEscalas(mundo.consultaEscalas(con));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("nuevaSeccion"))
				{
					//forma.reset();
					Utilidades.nuevoRegistroMapaGenerico(forma.getMapaSecciones(),indicesSecciones,"numRegistros","tiporegistro_","MEM");
					//Utilidades.generarLogGenerico(mapaModificado, posicionRegModificado, mapaOriginal, usuario, isEliminacion, posicionRegOriginal, ConstantesBD.logParametrizacionEscalas, indices)
					forma.setMapaCampos("numRegistros_"+forma.getMapaSecciones("numRegistros"), "0");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("ingreso");
				}
				else if(estado.equals("nuevoCampo"))
				{
					//forma.reset();
					this.accionAdicionarCampo(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("ingreso");
				}
				else if(estado.equals("nuevoFactor"))
				{
					//forma.reset();
					Utilidades.nuevoRegistroMapaGenerico(forma.getMapaFactores(),indicesFactores,"numRegistros","tiporegistro_","MEM");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("ingreso");
				}
				else if(estado.equals("guardar"))
				{
					//forma.reset();
					this.accionGuardar(con, forma, mundo, usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("ingreso");
				}
				else if(estado.equals("detalleEscala"))
				{
					//forma.reset();
					forma.resetMensaje();
					forma.setMapaDetalleEscala(mundo.consultaDetalleEscala(con, forma.getEscala(), usuario.getCodigoInstitucion()));
					forma.setMapaSecciones(mundo.detalleSeccion(con, forma.getEscala()));

					forma.setMapaCampos(mundo.detalleCampos(con, forma.getEscala()));
					armarKeysMapaCampos(forma);


					forma.setMapaFactores(mundo.detalleFactores(con, forma.getEscala(), usuario.getCodigoInstitucion()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}
				else if(estado.equals("modificar"))
				{
					//forma.reset();
					this.accionModificar(con, forma, mundo, usuario, request);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}
				else if(estado.equals("eliminarEscala"))
				{
					//forma.reset();
					this.accionElimarEscala(con, forma, mundo, usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("continuar"))
				{
					//forma.reset();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("ingreso");
				}
				else if(estado.equals("eliminarFactor"))
				{
					//eliminar Factor
					Utilidades.eliminarRegistroMapaGenerico(forma.getMapaFactores(),forma.getMapaFactores(),forma.getIndiceFactorEliminar(),indicesFactores,"numRegistros","tiporegistro_","BD",false);
					forma.setIndiceFactorEliminar(ConstantesBD.codigoNuncaValido);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("ingreso");
				}
				else if(estado.equals("eliminarCampos"))
				{
					//eliminar campos

					String[] indicesCamposTemporal={"nombrecampo_"+forma.getIndiceSeccionEliminar()+"_",
							"indrequerido_"+forma.getIndiceSeccionEliminar()+"_",
							"valorminimo_"+forma.getIndiceSeccionEliminar()+"_",
							"tiporegistro_"+forma.getIndiceSeccionEliminar()+"_",
							"valormaximo_"+forma.getIndiceSeccionEliminar()+"_",
							"obsevreq_"+forma.getIndiceSeccionEliminar()+"_",
							"activo_"+forma.getIndiceSeccionEliminar()+"_",
					};
					Utilidades.eliminarRegistroMapaGenerico(forma.getMapaCampos(),forma.getMapaCampos(),forma.getIndiceCampoEliminar(),indicesCamposTemporal,"numRegistros_"+forma.getIndiceSeccionEliminar(),"tiporegistro_"+forma.getIndiceSeccionEliminar()+"_","BD",false);
					forma.setIndiceCampoEliminar(ConstantesBD.codigoNuncaValido);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("ingreso");
				}
				else if(estado.equals("nuevaSeccionMod"))
				{
					//forma.reset();
					Utilidades.nuevoRegistroMapaGenerico(forma.getMapaSecciones(),indicesSecciones,"numRegistros","tiporegistro_","MEM");
					forma.setMapaCampos("numRegistros_"+(Utilidades.convertirAEntero(forma.getMapaSecciones("numRegistros")+"")-1), "0");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}
				else if(estado.equals("nuevoCampoMod"))
				{
					//forma.reset();
					this.accionAdicionarCampo(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}
				else if(estado.equals("nuevoFactorMod"))
				{
					//forma.reset();
					Utilidades.nuevoRegistroMapaGenerico(forma.getMapaFactores(),indicesFactores,"numRegistros","tiporegistro_","MEM");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}
				else if(estado.equals("eliminarFactorMod"))
				{
					//eliminar Factor
					//Utilidades.eliminarRegistroMapaGenerico(forma.getMapaFactores(),forma.getMapaFactores(),forma.getIndiceFactorEliminar(),indicesFactores,"numRegistros","tiporegistro_","BD",false);
					mundo.eliminarFactor(con, forma.getMapaFactores("codigopkfactor_"+forma.getIndiceFactorEliminar())+"");
					forma.setIndiceFactorEliminar(ConstantesBD.codigoNuncaValido);
					forma.setMapaDetalleEscala(mundo.consultaDetalleEscala(con, forma.getEscala(), usuario.getCodigoInstitucion()));
					forma.setMapaSecciones(mundo.detalleSeccion(con, forma.getEscala()));
					forma.setMapaCampos(mundo.detalleCampos(con, forma.getEscala()));
					armarKeysMapaCampos(forma);
					forma.setMapaFactores(mundo.detalleFactores(con, forma.getEscala(), usuario.getCodigoInstitucion()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}
				else if(estado.equals("eliminarCamposMod"))
				{
					//eliminar campos
					String[] indicesCamposTemporal={"nombrecampo_"+forma.getIndiceSeccionEliminar()+"_",
							"indrequerido_"+forma.getIndiceSeccionEliminar()+"_",
							"valorminimo_"+forma.getIndiceSeccionEliminar()+"_",
							"tiporegistro_"+forma.getIndiceSeccionEliminar()+"_",
							"valormaximo_"+forma.getIndiceSeccionEliminar()+"_",
							"obsevreq_"+forma.getIndiceSeccionEliminar()+"_",
							"activo_"+forma.getIndiceSeccionEliminar()+"_",
					};
					//Utilidades.eliminarRegistroMapaGenerico(forma.getMapaCampos(),forma.getMapaCampos(),forma.getIndiceCampoEliminar(),indicesCamposTemporal,"numRegistros_"+forma.getIndiceSeccionEliminar(),"tiporegistro_"+forma.getIndiceSeccionEliminar()+"_","BD",false);
					mundo.eliminarCampos(con, forma.getMapaCampos("codigopkcampo_"+forma.getMapaSecciones("codigopkseccion_"+forma.getIndiceSeccionEliminar())+"_"+forma.getIndiceCampoEliminar())+"", usuario.getLoginUsuario());
					forma.setIndiceCampoEliminar(ConstantesBD.codigoNuncaValido);
					forma.setMapaDetalleEscala(mundo.consultaDetalleEscala(con, forma.getEscala(), usuario.getCodigoInstitucion()));
					forma.setMapaSecciones(mundo.detalleSeccion(con, forma.getEscala()));
					forma.setMapaCampos(mundo.detalleCampos(con, forma.getEscala()));
					armarKeysMapaCampos(forma);
					forma.setMapaFactores(mundo.detalleFactores(con, forma.getEscala(), usuario.getCodigoInstitucion()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}
				else if(estado.equals("eliminarSeccionMod"))
				{
					//eliminar Factor
					//Utilidades.eliminarRegistroMapaGenerico(forma.getMapaFactores(),forma.getMapaFactores(),forma.getIndiceFactorEliminar(),indicesFactores,"numRegistros","tiporegistro_","BD",false);
					mundo.eliminarSecciones(con, forma.getMapaSecciones("codigopkseccion_"+forma.getIndiceSeccionEliminar())+"");
					forma.setIndiceFactorEliminar(ConstantesBD.codigoNuncaValido);
					forma.setMapaDetalleEscala(mundo.consultaDetalleEscala(con, forma.getEscala(), usuario.getCodigoInstitucion()));
					forma.setMapaSecciones(mundo.detalleSeccion(con, forma.getEscala()));
					forma.setMapaCampos(mundo.detalleCampos(con, forma.getEscala()));
					armarKeysMapaCampos(forma);
					forma.setMapaFactores(mundo.detalleFactores(con, forma.getEscala(), usuario.getCodigoInstitucion()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}
				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de CONSULTA FACTURAS VARIAS ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}		
			}
			else
			{
				logger.error("El form no es compatible con el form de ConsultaFacturasVariasForm");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
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
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionElimarEscala(Connection con, EscalasForm forma, Escalas mundo, UsuarioBasico usuario) 
	{
		
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		HashMap vo=new HashMap();
		vo.put("codigo_pk",forma.getEscala());
		vo.put("mostrar_modificacion",ConstantesBD.acronimoNo);
		vo.put("usuario_modifica",usuario.getLoginUsuario());
		transaccion=mundo.modificarMostrarEscala(con, vo);
		
		forma.setMapaConsultaEscalas(mundo.consultaEscalas(con));
		
		if(transaccion)
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		
	}




	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionModificar(Connection con, EscalasForm forma, Escalas mundo, UsuarioBasico usuario, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		int secuencia= ConstantesBD.codigoNuncaValido;
		HashMap contador= new HashMap();
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		if(!(forma.getMapaDetalleEscala("nombreescala_0")+"").equals(forma.getMapaDetalleEscala("nombreescalaantiguo_0")+""))
		{
			
			HashMap vo=new HashMap();
			vo.put("codigo_pk",forma.getMapaDetalleEscala("codigopkescala_0"));
			vo.put("mostrar_modificacion",ConstantesBD.acronimoNo);
			vo.put("usuario_modifica",usuario.getLoginUsuario());
			transaccion=mundo.modificarMostrarEscala(con, vo);
			
			transaccion=mundo.insertarEscala(con, forma.getMapaDetalleEscala("codigoescala_0")+"", forma.getMapaDetalleEscala("nombreescala_0")+"", forma.getMapaDetalleEscala("observrequeridasesc_0")+"", usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario());
			if(transaccion)
			{
				secuencia= UtilidadBD.obtenerUltimoValorSecuencia(con, "seq_escalas");
			}
			
			for(int i=0;i<Utilidades.convertirAEntero(forma.getMapaSecciones("numRegistros")+"");i++)
			{
				//insertar
				vo=new HashMap();
				vo.put("nombre",forma.getMapaSecciones("nombreseccion_"+i));
				vo.put("escala",secuencia);
				vo.put("tipo_respuesta",forma.getMapaSecciones("tiporespuesta_"+i));
				vo.put("activo",forma.getMapaSecciones("activo_"+i));
				int codigoSeccion=mundo.insertarSecciones(con, vo);
				if(codigoSeccion>0)
				{
					for(int j=0;j<Utilidades.convertirAEntero(forma.getMapaCampos("numRegistros_"+i)+"");j++)
					{
						//insertar
						vo=new HashMap();
						vo.put("nombre",forma.getMapaCampos("nombrecampo_"+forma.getMapaSecciones("codigopkseccion_"+i)+"_"+j));
						vo.put("requerido",forma.getMapaCampos("indrequerido_"+forma.getMapaSecciones("codigopkseccion_"+i)+"_"+j));
						vo.put("minimo",forma.getMapaCampos("valorminimo_"+forma.getMapaSecciones("codigopkseccion_"+i)+"_"+j));
						vo.put("maximo",forma.getMapaCampos("valormaximo_"+forma.getMapaSecciones("codigopkseccion_"+i)+"_"+j));
						vo.put("mostrar",forma.getMapaCampos("activo_"+forma.getMapaSecciones("codigopkseccion_"+i)+"_"+j));
						vo.put("mostrar_modificacion",ConstantesBD.acronimoSi);
						vo.put("usuario_modifica",usuario.getLoginUsuario());
						vo.put("institucion",usuario.getCodigoInstitucionInt());
						vo.put("seccion", codigoSeccion);
						
						int codigoCampo=mundo.insertarCampos(con, vo);
						if(codigoCampo>0)
						{
							vo=new HashMap();
							vo.put("campo_parametrizable",codigoCampo);
							vo.put("escala_seccion",codigoSeccion);
							vo.put("observaciones_requeridas",forma.getMapaCampos("obsevreq_"+i+"_"+j));
							transaccion=mundo.insertarCamposSeccion(con, vo);
						}
						else
						{
							transaccion=false;
							j=Utilidades.convertirAEntero(forma.getMapaCampos("numRegistros_"+i)+"");
						}
					}
				}
				else
				{
					transaccion=false;
					i=Utilidades.convertirAEntero(forma.getMapaCampos("numRegistros_"+i)+"");
				}
				
			}
			for(int i=0;i<Integer.parseInt(forma.getMapaFactores("numRegistros")+"");i++)
			{
				//insertar
				vo=new HashMap();
				vo.put("codigo",forma.getMapaFactores("codigofactor_"+i));
				vo.put("nombre",forma.getMapaFactores("nombrefactor_"+i));
				vo.put("escala",secuencia);
				vo.put("valor_inicial",forma.getMapaFactores("valorinicial_"+i));
				vo.put("valor_final",forma.getMapaFactores("valorfinal_"+i));
				vo.put("activo",forma.getMapaFactores("activofactor_"+i));
				vo.put("institucion",usuario.getCodigoInstitucionInt());
				transaccion=mundo.insertarFactores(con, vo);
			}
			
			
			
		}
		else
		{
				
			HashMap vo=new HashMap();
			vo.put("codigo_pk",forma.getMapaDetalleEscala("codigopkescala_0"));
			vo.put("codigo",forma.getMapaDetalleEscala("codigoescala_0"));
			vo.put("nombre",forma.getMapaDetalleEscala("nombreescala_0"));
			vo.put("observaciones_requeridas",forma.getMapaDetalleEscala("observrequeridasesc_0"));
			vo.put("usuario_modifica",usuario.getLoginUsuario());
			vo.put("institucion",usuario.getCodigoInstitucion());
			transaccion=mundo.modificarEscala(con, vo);
		
			for(int i=0;i<Utilidades.convertirAEntero(forma.getMapaSecciones("numRegistros")+"");i++)
			{
				
				if(!(forma.getMapaSecciones("nombreseccion_"+i)+"").equals(forma.getMapaSecciones("nombreseccionantiguo_"+i)+""))
				{
					
					if(!(forma.getMapaSecciones("tiporegistro_"+i)+"").equals("MEM"))
					{
						
						vo=new HashMap();
						vo.put("codigo_pk",forma.getMapaSecciones("codigopkseccion_"+i));
						vo.put("mostrar_modificacion",ConstantesBD.acronimoNo);
						transaccion=mundo.modificarMostrarSeccion(con, vo);
					
					}	
						
					//insertar
					vo=new HashMap();
					vo.put("nombre",forma.getMapaSecciones("nombreseccion_"+i));
					vo.put("escala",forma.getMapaDetalleEscala("codigopkescala_0"));
					vo.put("tipo_respuesta",forma.getMapaSecciones("tiporespuesta_"+i));
					vo.put("activo",forma.getMapaSecciones("activo_"+i));
					int codigoSeccion=mundo.insertarSecciones(con, vo);
					if(codigoSeccion>0)
					{
						for(int j=0;j<Utilidades.convertirAEntero(forma.getMapaCampos("numRegistros_"+i)+"");j++)
						{
							//insertar
							vo=new HashMap();
							vo.put("nombre",forma.getMapaCampos("nombrecampo_"+forma.getMapaSecciones("codigopkseccion_"+i)+"_"+j));
							vo.put("requerido",forma.getMapaCampos("indrequerido_"+forma.getMapaSecciones("codigopkseccion_"+i)+"_"+j));
							vo.put("minimo",forma.getMapaCampos("valorminimo_"+forma.getMapaSecciones("codigopkseccion_"+i)+"_"+j));
							vo.put("maximo",forma.getMapaCampos("valormaximo_"+forma.getMapaSecciones("codigopkseccion_"+i)+"_"+j));
							vo.put("mostrar",forma.getMapaCampos("activo_"+forma.getMapaSecciones("codigopkseccion_"+i)+"_"+j));
							vo.put("mostrar_modificacion",ConstantesBD.acronimoSi);
							vo.put("usuario_modifica",usuario.getLoginUsuario());
							vo.put("institucion",usuario.getCodigoInstitucionInt());
							vo.put("seccion", codigoSeccion);
							int codigoCampo=mundo.insertarCampos(con, vo);
							if(codigoCampo>0)
							{
								vo=new HashMap();
								vo.put("campo_parametrizable",codigoCampo);
								vo.put("escala_seccion",codigoSeccion);
								vo.put("observaciones_requeridas",forma.getMapaCampos("obsevreq_"+i+"_"+j));
								transaccion=mundo.insertarCamposSeccion(con, vo);
							}
							else
							{
								transaccion=false;
								j=Utilidades.convertirAEntero(forma.getMapaCampos("numRegistros_"+i)+"");
							}
						}
					}
					else
					{
						transaccion=false;
						i=Utilidades.convertirAEntero(forma.getMapaCampos("numRegistros_"+i)+"");
					}
					
				
					
				}
				else
				{
					
					//modificar
					vo=new HashMap();
					vo.put("codigo_pk",forma.getMapaSecciones("codigopkseccion_"+i));
					vo.put("nombre",forma.getMapaSecciones("nombreseccion_"+i));
					vo.put("tipo_respuesta",forma.getMapaSecciones("tiporespuesta_"+i));
					vo.put("activo",forma.getMapaSecciones("activo_"+i));
					transaccion=mundo.modificarSeccion(con, vo);
					if(transaccion)
					{	
						int l=0;
						int cont=0;
						for(int j=0;j<Utilidades.convertirAEntero(forma.getMapaCampos("numRegistros_"+i)+"");j++)
						{
							if((forma.getMapaSecciones("tiporespuesta_"+i)+"").equals(ConstantesIntegridadDominio.acronimoTipoDistribucionCostoUnica))
							{
								if((forma.getMapaCampos("indrequerido_"+forma.getMapaSecciones("codigopkseccion_"+i)+"_"+j)+"").equals(ConstantesBD.acronimoSi))
								{									
									cont++;
								}
							}
						
							if(cont > 1)
							{
								contador.put("error_"+l, ConstantesBD.acronimoSi);
								contador.put("seccion_"+l, (j+1));
								l++;
								contador.put("numRegistros", l);
							}
							
							if(!(forma.getMapaCampos("nombrecampo_"+forma.getMapaSecciones("codigopkseccion_"+i)+"_"+j)+"").equals(forma.getMapaCampos("nombrecampoantiguo_"+forma.getMapaSecciones("codigopkseccion_"+i)+"_"+j)+""))
							{
								
								if((forma.getMapaCampos("tiporegistro_"+forma.getMapaSecciones("codigopkseccion_"+i)+"_"+j)+"").equals("MEM"))
								{
									vo=new HashMap();
									vo.put("codigo_pk",forma.getMapaCampos("codigopkcampo_"+forma.getMapaSecciones("codigopkseccion_"+i)+"_"+j));
									vo.put("mostrar_modificacion",ConstantesBD.acronimoNo);
									vo.put("usuario_modifica",usuario.getLoginUsuario());
									transaccion=mundo.modificarMostrarCampos(con, vo);
								}
								
								//insertar
								vo=new HashMap();
								vo.put("nombre",forma.getMapaCampos("nombrecampo_"+forma.getMapaSecciones("codigopkseccion_"+i)+"_"+j));
								vo.put("requerido",forma.getMapaCampos("indrequerido_"+forma.getMapaSecciones("codigopkseccion_"+i)+"_"+j));
								vo.put("minimo",forma.getMapaCampos("valorminimo_"+forma.getMapaSecciones("codigopkseccion_"+i)+"_"+j));
								vo.put("maximo",forma.getMapaCampos("valormaximo_"+forma.getMapaSecciones("codigopkseccion_"+i)+"_"+j));
								vo.put("mostrar",forma.getMapaCampos("activo_"+forma.getMapaSecciones("codigopkseccion_"+i)+"_"+j));
								vo.put("mostrar_modificacion",ConstantesBD.acronimoSi);
								vo.put("usuario_modifica",usuario.getLoginUsuario());
								vo.put("institucion",usuario.getCodigoInstitucionInt());
								vo.put("seccion", forma.getMapaSecciones("codigopkseccion_"+i));
								int codigoCampo=mundo.insertarCampos(con, vo);
								if(codigoCampo>0)
								{
									vo=new HashMap();
									vo.put("campo_parametrizable",codigoCampo);
									vo.put("escala_seccion",forma.getMapaSecciones("codigopkseccion_"+i));
									vo.put("observaciones_requeridas",forma.getMapaCampos("obsevreq_"+i+"_"+j));
									transaccion=mundo.insertarCamposSeccion(con, vo);
								}
								else
								{
									transaccion=false;
									j=Utilidades.convertirAEntero(forma.getMapaCampos("numRegistros_"+i)+"");
								}
							}
							else
							{
								
								//modificar
								vo=new HashMap();
								vo.put("codigo_pk",forma.getMapaCampos("codigopkcampo_"+forma.getMapaSecciones("codigopkseccion_"+i)+"_"+j));
								vo.put("nombre",forma.getMapaCampos("nombrecampo_"+forma.getMapaSecciones("codigopkseccion_"+i)+"_"+j));
								vo.put("requerido",forma.getMapaCampos("indrequerido_"+forma.getMapaSecciones("codigopkseccion_"+i)+"_"+j));
								vo.put("minimo",forma.getMapaCampos("valorminimo_"+forma.getMapaSecciones("codigopkseccion_"+i)+"_"+j));
								vo.put("maximo",forma.getMapaCampos("valormaximo_"+forma.getMapaSecciones("codigopkseccion_"+i)+"_"+j));
								vo.put("mostrar",forma.getMapaCampos("activo_"+forma.getMapaSecciones("codigopkseccion_"+i)+"_"+j));
								vo.put("usuario_modifica",usuario.getLoginUsuario());
								vo.put("institucion",usuario.getCodigoInstitucion());
								transaccion=mundo.modificarCampos(con, vo);
								if(transaccion)
								{
									vo=new HashMap();
									vo.put("codigo_pk",forma.getMapaCampos("codigopkcamposseccion_"+forma.getMapaSecciones("codigopkseccion_"+i)+"_"+j));
									vo.put("observaciones_requeridas",forma.getMapaCampos("obsevreq_"+forma.getMapaSecciones("codigopkseccion_"+i)+"_"+j));
									transaccion=mundo.modificarCamposSeccion(con, vo);
								}
								else
								{
									transaccion=false;
									j=Utilidades.convertirAEntero(forma.getMapaCampos("numRegistros_"+i)+"");
								}
								
								
								
							}
						}
					}
					else
					{
						transaccion=false;
						i=Utilidades.convertirAEntero(forma.getMapaCampos("numRegistros_"+i)+"");
					}
					
				}
				
			}

			for(int l=0;l<Utilidades.convertirAEntero(contador.get("numRegistros")+"");l++)
			{
				if((contador.get("error_"+l)+"").equals(ConstantesBD.acronimoSi))
					errores.add("SeccionesMismoNombre", new ActionMessage("errors.notEspecific","Marcar solo un campo con indicativo requerido en seccion "+contador.get("seccion_"+l)));
			}
			
			if(!errores.isEmpty())
			{
				saveErrors(request,errores);	
				transaccion=false;
			}
			else
			{
				for(int i=0;i<Integer.parseInt(forma.getMapaFactores("numRegistros")+"");i++)
				{
					
					if(!(forma.getMapaFactores("nombrefactor_"+i)+"").equals(forma.getMapaFactores("nombrefactorantiguo_"+i)+""))
					{
						
						if(!(forma.getMapaFactores("tiporegistro_"+i)+"").equals("MEM"))
						{
							vo=new HashMap();
							vo.put("codigo_pk",forma.getMapaFactores("codigopkfactor_"+i));
							vo.put("mostrar_modificacion",ConstantesBD.acronimoNo);
							transaccion=mundo.modificarMostrarFactor(con, vo);
							
						}	
						
						//insertar
						vo=new HashMap();
						vo.put("codigo",forma.getMapaFactores("codigofactor_"+i));
						vo.put("nombre",forma.getMapaFactores("nombrefactor_"+i));
						vo.put("escala",forma.getMapaDetalleEscala("codigopkescala_0"));
						vo.put("valor_inicial",forma.getMapaFactores("valorinicial_"+i));
						vo.put("valor_final",forma.getMapaFactores("valorfinal_"+i));
						vo.put("activo",forma.getMapaFactores("activofactor_"+i));
						vo.put("institucion",usuario.getCodigoInstitucionInt());
						transaccion=mundo.insertarFactores(con, vo);
						
						
					}
					else
					{
						
						//modificar
						vo=new HashMap();
						vo.put("codigo_pk",forma.getMapaFactores("codigopkfactor_"+i));
						vo.put("codigo",forma.getMapaFactores("codigofactor_"+i));
						vo.put("nombre",forma.getMapaFactores("nombrefactor_"+i));
						vo.put("valor_inicial",forma.getMapaFactores("valorinicial_"+i));
						vo.put("valor_final",forma.getMapaFactores("valorfinal_"+i));
						vo.put("activo",forma.getMapaFactores("activofactor_"+i));
						vo.put("institucion",usuario.getCodigoInstitucion());
						transaccion=mundo.modificarFactores(con, vo);
						
					}
						
				}
			}
			
		}
		
		if(transaccion)
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}		
	}
	
	

	/**
	 * 
	 * @param forma
	 */
	private void armarKeysMapaCampos(EscalasForm forma) 
	{
		HashMap<String, Object> mapaIntermedio= new HashMap<String, Object>();
		for(int w=0; w<Utilidades.convertirAEntero(forma.getMapaSecciones("numRegistros")+""); w++)
		{
			int contador[]={0,0,0,0,0,0,0,0,0,0};
			for(int x=0; x<Utilidades.convertirAEntero(forma.getMapaCampos("numRegistros")+""); x++)
			{
				if(forma.getMapaCampos().get("seccion_"+x).toString().equals(forma.getMapaSecciones("codigopkseccion_"+w)+""))
				{
					for(int y=0; y<indicesCampos.length; y++)
					{
						mapaIntermedio.put(indicesCampos[y]+forma.getMapaCampos().get("seccion_"+x).toString()+"_"+contador[y], forma.getMapaCampos(indicesCampos[y]+x));
						contador[y]++;
					}	
				}
			}
			//Utilidades.imprimirMapa(mapaIntermedio);
			mapaIntermedio.put("numRegistros_"+w, contador[0]);
		}
		forma.setMapaCampos(mapaIntermedio);
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardar(Connection con, EscalasForm forma, Escalas mundo, UsuarioBasico usuario) 
	{
		
		int secuencia= ConstantesBD.codigoNuncaValido;
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		transaccion=mundo.insertarEscala(con, forma.getNombreEscala(), forma.getRequiereObservaciones(), usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario());
		if(transaccion)
		{
			secuencia= UtilidadBD.obtenerUltimoValorSecuencia(con, "seq_escalas");
		}
		
		for(int i=0;i<Utilidades.convertirAEntero(forma.getMapaSecciones("numRegistros")+"");i++)
		{
			
			//insertar
			HashMap vo=new HashMap();
			vo.put("nombre",forma.getMapaSecciones("nombreseccion_"+i));
			vo.put("escala",secuencia);
			vo.put("tipo_respuesta",forma.getMapaSecciones("tiporespuesta_"+i));
			vo.put("activo",forma.getMapaSecciones("activo_"+i));
			int codigoSeccion=mundo.insertarSecciones(con, vo);
			if(codigoSeccion>0)
			{
				for(int j=0;j<Utilidades.convertirAEntero(forma.getMapaCampos("numRegistros_"+i)+"");j++)
				{
					//insertar
					vo=new HashMap();
					vo.put("nombre",forma.getMapaCampos("nombrecampo_"+i+"_"+j));
					vo.put("requerido",forma.getMapaCampos("indrequerido_"+i+"_"+j));
					vo.put("minimo",forma.getMapaCampos("valorminimo_"+i+"_"+j));
					vo.put("maximo",forma.getMapaCampos("valormaximo_"+i+"_"+j));
					vo.put("mostrar",forma.getMapaCampos("activo_"+i+"_"+j));
					vo.put("mostrar_modificacion",ConstantesBD.acronimoSi);
					vo.put("usuario_modifica",usuario.getLoginUsuario());
					vo.put("institucion",usuario.getCodigoInstitucionInt());
					vo.put("seccion", codigoSeccion);
					int codigoCampo=mundo.insertarCampos(con, vo);
					if(codigoCampo>0)
					{
						vo=new HashMap();
						vo.put("campo_parametrizable",codigoCampo);
						vo.put("escala_seccion",codigoSeccion);
						vo.put("observaciones_requeridas",forma.getMapaCampos("obsevreq_"+i+"_"+j));
						transaccion=mundo.insertarCamposSeccion(con, vo);
					}
					else
					{
						transaccion=false;
						j=Utilidades.convertirAEntero(forma.getMapaCampos("numRegistros_"+i)+"");
					}
				}
			}
			else
			{
				transaccion=false;
				i=Utilidades.convertirAEntero(forma.getMapaCampos("numRegistros_"+i)+"");
			}
			
		}
		for(int i=0;i<Integer.parseInt(forma.getMapaFactores("numRegistros")+"");i++)
		{
			//insertar
			HashMap vo=new HashMap();
			vo.put("codigo",forma.getMapaFactores("codigofactor_"+i));
			vo.put("nombre",forma.getMapaFactores("nombrefactor_"+i));
			vo.put("escala",secuencia);
			vo.put("valor_inicial",forma.getMapaFactores("valorinicial_"+i));
			vo.put("valor_final",forma.getMapaFactores("valorfinal_"+i));
			vo.put("activo",forma.getMapaFactores("activofactor_"+i));
			vo.put("institucion",usuario.getCodigoInstitucionInt());
			transaccion=mundo.insertarFactores(con, vo);
		}
		
		if(transaccion)
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		
	}


	/**
	 * 
	 * @param forma
	 */
	private void accionAdicionarCampo(EscalasForm forma) 
	{
		int pos=Integer.parseInt(forma.getMapaCampos("numRegistros_"+forma.getIndexSeccion())+"");
		forma.setMapaCampos("nombrecampo_"+forma.getIndexSeccion()+"_"+pos,"");
		forma.setMapaCampos("indrequerido_"+forma.getIndexSeccion()+"_"+pos,"");
		forma.setMapaCampos("valorminimo_"+forma.getIndexSeccion()+"_"+pos,"");
		forma.setMapaCampos("valormaximo_"+forma.getIndexSeccion()+"_"+pos,"");
		forma.setMapaCampos("obsevreq_"+forma.getIndexSeccion()+"_"+pos,"");
		forma.setMapaCampos("activo_"+forma.getIndexSeccion()+"_"+pos,"");
		forma.setMapaCampos("tiporegistro_"+forma.getIndexSeccion()+"_"+pos,"MEM");
		forma.setMapaCampos("numRegistros_"+forma.getIndexSeccion(), (pos+1)+"");
	}
	
}
