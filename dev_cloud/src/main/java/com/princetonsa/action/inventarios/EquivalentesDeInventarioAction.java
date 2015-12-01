package com.princetonsa.action.inventarios;
/**
 * autor: Juan Sebastian Castaño
 * Clase action para el form utilizado en la funcionalidad de equivalentes de inventario
 */

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.inventarios.EquivalentesDeInventarioForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.EquivalentesDeInventario;


public class EquivalentesDeInventarioAction extends Action {
	
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 */
	private Logger logger= Logger.getLogger( EquivalentesDeInventarioAction.class);

	
	/**
	 * Método excute del Action
	 */
	public ActionForward execute(	ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response ) throws Exception
									{

		Connection con = null;
		try{ 

			if(form instanceof EquivalentesDeInventarioForm)
			{



				//SE ABRE CONEXION
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}


				EquivalentesDeInventarioForm equiDeInventariosForm = (EquivalentesDeInventarioForm)form;
				HttpSession session = request.getSession();
				PersonaBasica paciente= (PersonaBasica)session.getAttribute("pacienteActivo");
				UsuarioBasico usuario= (UsuarioBasico)session.getAttribute("usuarioBasico");
				boolean soloConsulta = true;

				// crear obj mundo

				String estado = equiDeInventariosForm.getEstado();

				logger.info("\n\nESTADO EQUIVALENTES DE INVENTARIO >>>>>>>>>"+estado+"\n\n");

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del Flujo de Unidad de Procedimiento (null)");
					request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}

				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(mapping, equiDeInventariosForm,con,usuario);
				}
				else if (estado.equals("cargarArtEqui"))
				{
					return this.accionConsultarArtEquivalente(con,mapping, equiDeInventariosForm);
				}
				else if (estado.equals("cargarDatosAd"))
				{
					return this.accionConsultarDatosAd(con,mapping, equiDeInventariosForm);
				}
				else if (estado.equals("nuevo"))
				{
					return accionNuevo(con,equiDeInventariosForm,usuario,request,response);
				}
				else if (estado.equals("eliminar"))
				{
					return accionEliminar(con, equiDeInventariosForm , mapping);
				}
				else if (estado.equals("guardar"))
				{
					return accionGuardar(con, equiDeInventariosForm ,usuario,request,mapping);
				}
				else if (estado.equals("empezarConsulta"))
				{
					return this.accionEmpezar(mapping, equiDeInventariosForm,con,usuario, soloConsulta);
				}
				else if (estado.equals("consultar"))
				{
					return this.accionConsultarArtEquivalente(con,mapping, equiDeInventariosForm,soloConsulta);
				}
				else if (estado.equals("consultar2"))
				{
					return this.accionConsultarArtEquivalente(con,mapping, equiDeInventariosForm,soloConsulta);
				}

				//**************************************************************

			}
			else
			{	
				logger.error("El form no es compatible con el form de Equivalentes de Inventario");
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
	 * Metodo utilizado para guardar los cambios en la base de datos.
	 * @param con
	 * @param equiDeInventariosForm
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionGuardar(Connection con, EquivalentesDeInventarioForm equiDeInventariosForm, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		ActionErrors errores = new ActionErrors();
		logger.info("MAPA PA VERIFICAAAAAAAR>>>>>>>>>> "+equiDeInventariosForm.getMapaEquiInventarios());
		logger.info("NUM MAPA EQUI INVENTARIOS>>>>>>>>>>"+equiDeInventariosForm.getNumMapaEquiInventarios());
		EquivalentesDeInventario mundoEquiInvent = new EquivalentesDeInventario();
		int pos=0;
		UtilidadBD.iniciarTransaccion(con);
		pos = equiDeInventariosForm.getNumMapaEquiInventarios();
		for (int i=0; i< equiDeInventariosForm.getNumMapaEquiInventarios(); i++)
		{
			mundoEquiInvent.clean();
			// Si el codigo ppal es vacio quiere decir qque es un nuevo registro
			if( equiDeInventariosForm.getMapaEquiInventarios().containsKey("codPpal_"+i) && equiDeInventariosForm.getMapaEquiInventarios("codPpal_"+i).toString().equals(""))
			{
				logger.info("ENTRO AL GUARDAR EN LA PARTE DE INSERTAR>>>>>>>>");
				//*************** Insercion de un nuevo articulo equivalente para un articulo seleccionado.
				this.llenarMundo(equiDeInventariosForm,mundoEquiInvent,i,usuario);
				
								
				//** Verificar si es un articulo que ya existe**/
				if(verificarArticuloEquivalente(equiDeInventariosForm,mundoEquiInvent,i))
				{
					if(!mundoEquiInvent.insertarArticuloEquivalente(con))
						errores.add("",new ActionMessage("errors.ingresoDatos","el registro N° "+(i+1)+". Proceso Cancelado"));
					/*else
					{
						int posA=Integer.parseInt(equiDeInventariosForm.getMapaEquiInventarios("numRegistros").toString());
						posA++;
						
					}*/
				}
				else
					errores.add("",new ActionMessage("errors.warnings","El registro N° "+(i+1)+". Es un articulo equivalente ya asociado al articulo principal, por favor cambielo o eliminelo, dado que este no podra ser asociado mas de una vez"));
					//errores.add("",new ActionMessage("errors.ingresoDatos","el registro N° "+(i+1)+". Es un articulo equivalente ya asociado al articulo principal, por favor cambielo o eliminelo, dado que este no podra ser asociado mas de una vez"));
			}
					
			//** Eliminar un registro de articulo equivalente */ 
			else if (equiDeInventariosForm.getMapaEquiInventarios().containsKey("eliminar_"+i) && UtilidadTexto.getBoolean(equiDeInventariosForm.getMapaEquiInventarios("eliminar_"+i).toString()))
			{
				logger.info("ENTRO AL GUARDAR EN LA PARTE DE ELIMINAR>>>>>>>>");
				// enviar a eliminar el articulo equivalente con el codigo codEqui_
				if(equiDeInventariosForm.getMapaEquiInventarios().containsKey("codEqui_"+i) && !equiDeInventariosForm.getMapaEquiInventarios("codEqui_"+i).toString().equals(""))
				{
					this.llenarMundo(equiDeInventariosForm,mundoEquiInvent,i,usuario);
					if (!mundoEquiInvent.eliminarArticuloEquivalente(con))
						errores.add("",new ActionMessage("errors.problemasGenericos","al eliminar el registro N° "+(i+1)));
					else
					{
						String temp3[] = {"artEquiDescripcion_",
								"artEquiNaturaleza_",
								"artEquiFormaFarm_",
								"artEquiConcentracion_",
								"artEquiUnidMedida_", 
								"artEquiCantidad_", 
								"codEqui_", 
								"codPpal_"};
						Utilidades.generarLogGenerico(equiDeInventariosForm.getMapaEquiInventarios(), null, usuario.getLoginUsuario(), true, i, ConstantesBD.logEquivalentesDeInventarioCodigo,(String[])temp3);
						//Utilidades.generarLogGenerico(equiDeInventariosForm.getMapaEquiInventarios(), null, usuario.getLoginUsuario(), true, i, ConstantesBD.logEquivalentesDeInventarioCodigo,(String[])equiDeInventariosForm.getMapaEquiInventarios("INDICES"));
						
						
						
						//pos = equiDeInventariosForm.getNumMapaEquiInventarios();
						pos--;
						equiDeInventariosForm.getMapaEquiInventarios().remove("codEqui_"+i);
						equiDeInventariosForm.getMapaEquiInventarios().remove("artEquiDescripcion_"+i);
						equiDeInventariosForm.getMapaEquiInventarios().remove("artEquiNaturaleza_"+i);
						equiDeInventariosForm.getMapaEquiInventarios().remove("artEquiFormaFarm_"+i);
						equiDeInventariosForm.getMapaEquiInventarios().remove("artEquiConcentracion_"+i);
						equiDeInventariosForm.getMapaEquiInventarios().remove("artEquiUnidMedida_"+i);
						equiDeInventariosForm.getMapaEquiInventarios().remove("artEquiCantidad_"+i);
						equiDeInventariosForm.getMapaEquiInventarios().remove("codPpal_"+i);
						equiDeInventariosForm.getMapaEquiInventarios().remove("DescripcionArtEquivalente_"+i);
						equiDeInventariosForm.getMapaEquiInventarios().remove("eliminar_"+i);
						
						//equiDeInventariosForm.setMapaEquiInventarios("numRegistros", pos+"");
						
						
					}
				}
				else
				{
					pos--;
					equiDeInventariosForm.getMapaEquiInventarios().remove("codEqui_"+i);
					equiDeInventariosForm.getMapaEquiInventarios().remove("artEquiCantidad_"+i);
					equiDeInventariosForm.getMapaEquiInventarios().remove("codPpal_"+i);
					equiDeInventariosForm.getMapaEquiInventarios().remove("DescripcionArtEquivalente_"+i);
					equiDeInventariosForm.getMapaEquiInventarios().remove("eliminar_"+i);					
				}
			}
			else
			{
				logger.info("ENTRO AL GUARDAR EN LA PARTE DE MODIFICAR>>>>>>>>");				
				// si la cantidad fue modificada realizar la modificacion
				if (equiDeInventariosForm.getMapaEquiInventarios().containsKey("artEquiCantidad_"+i) && !equiDeInventariosForm.getMapaEquiInventarios("artEquiCantidad_"+i).equals(equiDeInventariosForm.getMapaEquiInventarios("artEquiCantidadOriginal_"+i)))
				{
					//Accion de modificacion de una cantidad de articulo equivalente
					this.llenarMundo(equiDeInventariosForm,mundoEquiInvent,i,usuario);					
					
					if(!mundoEquiInvent.modificarArticuloEquivalente(con))
							errores.add("",new ActionMessage("errors.sinActualizar"));
					else
					{
						String temp3[] = {"artEquiDescripcion_",
								"artEquiNaturaleza_",
								"artEquiFormaFarm_",
								"artEquiConcentracion_",
								"artEquiUnidMedida_", 
								"artEquiCantidad_", 
								"codEqui_", 
								"codPpal_",
								"artEquiCantidadOriginal_"};
						
						HashMap<String, Object> temp2 = new HashMap<String, Object> ();
						temp2.put("artEquiDescripcion_0", equiDeInventariosForm.getMapaEquiInventarios("artEquiDescripcion_"+i));
						temp2.put("artEquiNaturaleza_0", equiDeInventariosForm.getMapaEquiInventarios("artEquiNaturaleza_"+i));
						temp2.put("artEquiFormaFarm_0", equiDeInventariosForm.getMapaEquiInventarios("artEquiFormaFarm_"+i));						
						temp2.put("artEquiConcentracion_0", equiDeInventariosForm.getMapaEquiInventarios("artEquiConcentracion_"+i));						
						temp2.put("artEquiUnidMedida_0", equiDeInventariosForm.getMapaEquiInventarios("artEquiUnidMedida_"+i));
						
						//temp2.put("artEquiCantidad_0", equiDeInventariosForm.getMapaEquiInventarios("artEquiCantidad_"+i));
						temp2.put("artEquiCantidad_0", equiDeInventariosForm.getMapaEquiInventarios("artEquiCantidadOriginal_"+i));
						//temp2.put("artEquiCantidadOriginal_0", equiDeInventariosForm.getMapaEquiInventarios("artEquiCantidadOriginal_"+i));
						
						temp2.put("codEqui_0", equiDeInventariosForm.getMapaEquiInventarios("codEqui_"+i));
						temp2.put("codPpal_0", equiDeInventariosForm.getMapaEquiInventarios("codPpal_"+i));
						
						temp2.put("INDICES",temp3);
						temp2.remove("eliminar_"+i);
						
						//Utilidades.generarLogGenerico(equiDeInventariosForm.getMapaEquiInventarios(),temp2, usuario.getLoginUsuario(), false, i, ConstantesBD.logEquivalentesDeInventarioCodigo,(String[])equiDeInventariosForm.getMapaEquiInventarios("INDICES"));
						Utilidades.generarLogGenerico(equiDeInventariosForm.getMapaEquiInventarios(),temp2, usuario.getLoginUsuario(), false, i, ConstantesBD.logEquivalentesDeInventarioCodigo,(String[])temp3);
						
						 				
					}
				
				}	
			}
		
		}
		//equiDeInventariosForm.setMapaEquiInventarios("numRegistros", pos+"");
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			equiDeInventariosForm.setEstado("");
			UtilidadBD.abortarTransaccion(con);
		}
		else
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		logger.info("\n\nNUMMMMM REGISTROOOOOOOOSSSSSSSSSSSSSJEEEEE>>>>>>>>>"+equiDeInventariosForm.getMapaEquiInventarios("numRegistros")+"\n\n");
		logger.info("\n\nNUMMMMM REGISTROS VERIABLEEEEEEEEEEEJEEEEE>>>>>>>>>"+equiDeInventariosForm.getNumMapaEquiInventarios()+"\n\n");
		logger.info("\n\nMAPAAAAAAAAAAAAAAJEEEEE>>>>>>>>>"+equiDeInventariosForm.getMapaEquiInventarios()+"\n\n");
		UtilidadBD.closeConnection(con);
		
		return mapping.findForward("principal");
	}

	/**
	 * Verifica que el articulo equivalente seleccionado no esta ya registrado
	 * @param equiDeInventariosForm
	 * @param mundoEquiInvent
	 * @param pos
	 * @return
	 */
	private boolean verificarArticuloEquivalente(EquivalentesDeInventarioForm equiDeInventariosForm, EquivalentesDeInventario mundoEquiInvent, int pos) {
		
		// verificar que el codigo del articulo equivalente no sea existente
		for(int i= 0; i < equiDeInventariosForm.getNumMapaEquiInventarios();i++)
		{		
			if (i != pos)
			{
				// si algun valor de codigo equivalente en el mapa coinside quiere decir que este articulo rquivalente ya existe para este articulo principal
				if(equiDeInventariosForm.getMapaEquiInventarios().containsKey("codEqui_"+pos) && equiDeInventariosForm.getMapaEquiInventarios().containsKey("codEqui_"+i) && equiDeInventariosForm.getMapaEquiInventarios("codEqui_"+i).toString().equals(equiDeInventariosForm.getMapaEquiInventarios("codEqui_"+pos).toString()) && equiDeInventariosForm.getMapaEquiInventarios("eliminar_"+i).toString().equals("N"))
				{
					return false;
				}
				// verificar que el articulo ppal no sea, a su vez, articulo equivalente
				if(equiDeInventariosForm.getMapaEquiInventarios().containsKey("codEqui_"+pos) && equiDeInventariosForm.getMapaEquiInventarios().containsKey("codEqui_"+i) && equiDeInventariosForm.getArticuloPpal() == Integer.parseInt(equiDeInventariosForm.getMapaEquiInventarios("codEqui_"+pos).toString()))
				{
					return false;
				}
			}
		}
		
		return true;
	}

	/**
	 * Prparar los atributos del mundo de la clase para la insercion
	 * @param equiDeInventariosForm
	 * @param mundoEquiInvent
	 * @param i
	 * @param usuario
	 */
	private void llenarMundo(EquivalentesDeInventarioForm equiDeInventariosForm, EquivalentesDeInventario mundoEquiInvent, int i, UsuarioBasico usuario) {
		/*if(!equiDeInventariosForm.getMapaEquiInventarios("codPpal_0").toString().equals(""))
			//el codigo del articulo ppal siempres estara en la pos cero del mapa de consulta.
			mundoEquiInvent.setCodigoPpal(Integer.parseInt(equiDeInventariosForm.getMapaEquiInventarios("codPpal_0").toString()));*/
		
		if (equiDeInventariosForm.getArticuloPpal() > 0)
		{
			mundoEquiInvent.setCodigoPpal(equiDeInventariosForm.getArticuloPpal());
		}
		mundoEquiInvent.setCodigoEquivalente(Integer.parseInt((String)equiDeInventariosForm.getMapaEquiInventarios("codEqui_"+i).toString()));
		mundoEquiInvent.setCantidadEquivalente(Integer.parseInt((String)equiDeInventariosForm.getMapaEquiInventarios("artEquiCantidad_"+i).toString()));
		
		mundoEquiInvent.setUsuarioModifica(usuario.getLoginUsuario());
	}

	/**
	 * Accion de eliminacion de un articulo equivalente
	 * @param con
	 * @param equiDeInventariosForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminar(Connection con, EquivalentesDeInventarioForm equiDeInventariosForm, ActionMapping mapping) {
		logger.info("\n\nMAPA DE EQUIVALENTES DESDE ELIMINAR ANTESSSSS>>>>>>>>>>>>>>>>>>"+equiDeInventariosForm.getMapaEquiInventarios());
		logger.info("\n\nPOSICIONNNNNNNNN DESDE ELIMINAR ANTESSSSS>>>>>>>>>>>>>>>>>>"+Integer.parseInt(equiDeInventariosForm.getCodigoEliminarPos()));
		logger.info("\n\nNUM MAPA EQUI>>>>>>>>>>>>>>>>"+equiDeInventariosForm.getNumMapaEquiInventarios());
		int pos = Integer.parseInt(equiDeInventariosForm.getCodigoEliminarPos());
		
		if (equiDeInventariosForm.getMapaEquiInventarios("codPpal_"+pos).toString().equals(""))
		{
			logger.info("ENTROOOOOOO 1111111111");
			for(int i=pos;i<(equiDeInventariosForm .getNumMapaEquiInventarios()-1);i++)
			{
				logger.info("ENTROOOOOOO 222222222");
				equiDeInventariosForm.setMapaEquiInventarios("codEqui_"+i, equiDeInventariosForm.getMapaEquiInventarios("codEqui_"+(i+1)));
				equiDeInventariosForm.setMapaEquiInventarios("artEquiDescripcion_"+i, equiDeInventariosForm.getMapaEquiInventarios("artEquiDescripcion_"+(i+1)));
				equiDeInventariosForm.setMapaEquiInventarios("artEquiNaturaleza_"+i, equiDeInventariosForm.getMapaEquiInventarios("artEquiNaturaleza_"+(i+1)));
				equiDeInventariosForm.setMapaEquiInventarios("artEquiFormaFarm_"+i, equiDeInventariosForm.getMapaEquiInventarios("artEquiFormaFarm_"+(i+1)));
				equiDeInventariosForm.setMapaEquiInventarios("artEquiConcentracion_"+i, equiDeInventariosForm.getMapaEquiInventarios("artEquiConcentracion_"+(i+1)));
				equiDeInventariosForm.setMapaEquiInventarios("artEquiUnidMedida_"+i, equiDeInventariosForm.getMapaEquiInventarios("artEquiUnidMedida_"+(i+1)));
				equiDeInventariosForm.setMapaEquiInventarios("artEquiCantidad_"+i, equiDeInventariosForm.getMapaEquiInventarios("artEquiCantidad_"+(i+1)));
				equiDeInventariosForm.setMapaEquiInventarios("codPpal_"+i, equiDeInventariosForm.getMapaEquiInventarios("codPpal_"+(i+1)));
				equiDeInventariosForm.setMapaEquiInventarios("DescripcionArtEquivalente_"+i, equiDeInventariosForm.getMapaEquiInventarios("DescripcionArtEquivalente_"+(i+1)));
				equiDeInventariosForm.setMapaEquiInventarios("eliminar_"+i, equiDeInventariosForm.getMapaEquiInventarios("eliminar_"+(i+1)));
				
				
				
			}
			/*pos = equiDeInventariosForm.getNumMapaEquiInventarios();
			pos--;
			equiDeInventariosForm.getMapaEquiInventarios().remove("codEqui_"+pos);
			equiDeInventariosForm.getMapaEquiInventarios().remove("artEquiDescripcion_"+pos);
			equiDeInventariosForm.getMapaEquiInventarios().remove("artEquiNaturaleza_"+pos);
			equiDeInventariosForm.getMapaEquiInventarios().remove("artEquiFormaFarm_"+pos);
			equiDeInventariosForm.getMapaEquiInventarios().remove("artEquiConcentracion_"+pos);
			equiDeInventariosForm.getMapaEquiInventarios().remove("artEquiUnidMedida_"+pos);
			equiDeInventariosForm.getMapaEquiInventarios().remove("artEquiCantidad_"+pos);
			equiDeInventariosForm.getMapaEquiInventarios().remove("codPpal_"+pos);
			equiDeInventariosForm.getMapaEquiInventarios().remove("DescripcionArtEquivalente_"+pos);
			equiDeInventariosForm.getMapaEquiInventarios().remove("eliminar_"+pos);
			
			equiDeInventariosForm.setMapaEquiInventarios("numRegistros", pos+"");*/
			equiDeInventariosForm.setMapaEquiInventarios("codPpal_"+pos, equiDeInventariosForm.getArticuloPpal());
			equiDeInventariosForm.setMapaEquiInventarios("eliminar_"+pos, ConstantesBD.acronimoSi);
			
		}
		else
		{
			logger.info("ENTROOOOOOO 333333333333");
			//Si existe simplemente se marca el campo eliminar en S
			equiDeInventariosForm.setMapaEquiInventarios("eliminar_"+pos, ConstantesBD.acronimoSi);
			//if (!equiDeInventariosForm.getMapaEquiInventarios("codEqui_"+(pos-1)).toString().equals(""))			
				//equiDeInventariosForm.setArticuloEquivalente(Integer.parseInt((String)equiDeInventariosForm.getMapaEquiInventarios("codEqui_"+(pos-1))));
		}
		logger.info("\n\nMAPA DE EQUIVALENTES DESDE ELIMINAR>>>>>>>>>>>>>>>>>>"+equiDeInventariosForm.getMapaEquiInventarios());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}


	/**
	 * Crear un nuevo campo de registro de inventario equivalente
	 * @param con
	 * @param equiDeInventariosForm
	 * @param usuario
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward accionNuevo(Connection con, EquivalentesDeInventarioForm equiDeInventariosForm, UsuarioBasico usuario, HttpServletRequest request, HttpServletResponse response) {
		int pos = equiDeInventariosForm.getNumMapaEquiInventarios();
		
		
		equiDeInventariosForm.setMapaEquiInventarios("codPpal_"+pos, "");
		equiDeInventariosForm.setMapaEquiInventarios("codEqui_"+pos, "");
		equiDeInventariosForm.setMapaEquiInventarios("artEquiCantidad_"+pos,"");
		equiDeInventariosForm.setMapaEquiInventarios("eliminar_"+pos, ConstantesBD.acronimoNo);
		
		pos++;
		equiDeInventariosForm.setMapaEquiInventarios("numRegistros", pos+"");
		
		// Reinicializar el articulo equivalente seleccionado anteriormente
		equiDeInventariosForm.setArticuloEquivalente(0);
		
		UtilidadBD.closeConnection(con);	
		
		return UtilidadSesion.redireccionar("",ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),equiDeInventariosForm.getNumMapaEquiInventarios(), response, request, "equivalentesDeInventario.jsp",true);
	}



	/**
	 * Metodo utilizado para la consulta de articulos equivalentes
	 * @param con
	 * @param mapping
	 * @param equiDeInventariosForm
	 * @return
	 */
	private ActionForward accionConsultarArtEquivalente(Connection con, ActionMapping mapping, EquivalentesDeInventarioForm equiDeInventariosForm) {
		
		EquivalentesDeInventario mundoEquiInvent = new EquivalentesDeInventario();		
		mundoEquiInvent.setCodigoPpal(equiDeInventariosForm.getArticuloPpal());
		
		equiDeInventariosForm.setCamposBusquedaMap(mundoEquiInvent.consultaCamposBusqueda(con, equiDeInventariosForm.getArticuloPpal()));
		
		equiDeInventariosForm.setMapaEquiInventarios(mundoEquiInvent.consultaEquivalentes(con));
		
		UtilidadBD.closeConnection(con);
		
		return mapping.findForward("principal");
	}
	
	/**
	 * Metodo que Consulta Datos Adicionales del Articulo Equivalente
	 * @param con
	 * @param mapping
	 * @param equiDeInventariosForm
	 * @return
	 */
	private ActionForward accionConsultarDatosAd(Connection con, ActionMapping mapping, EquivalentesDeInventarioForm equiDeInventariosForm) {
		
		EquivalentesDeInventario mundoEquiInvent = new EquivalentesDeInventario();
		
		equiDeInventariosForm.setDatosAdMap(mundoEquiInvent.consultaDatosAd(con, equiDeInventariosForm.getArticuloEquivalente()));
		
		UtilidadBD.closeConnection(con);
		
		return mapping.findForward("principal");
	}

	
	private ActionForward accionConsultarArtEquivalente(Connection con, ActionMapping mapping, EquivalentesDeInventarioForm equiDeInventariosForm, boolean soloConsulta) 
	{
		if(equiDeInventariosForm.getEstado().equals("consultar"))
		{
			equiDeInventariosForm.setArticuloPpal(0);
			equiDeInventariosForm.setDescripcionArticuloPpal("");
		}
		ActionForward temp;		
		if ( soloConsulta == true)
			temp = accionConsultarArtEquivalente(con, mapping,equiDeInventariosForm);
		temp = null;
		UtilidadBD.closeConnection(con);
		return mapping.findForward("soloConsultaArtEquivalentes");
	}

	
	/**
	 * Accion de inicializacion
	 * @param mapping
	 * @param equiDeInventariosForm
	 * @param con
	 * @param usuario
	 * @return
	 */

	private ActionForward accionEmpezar(ActionMapping mapping, EquivalentesDeInventarioForm equiDeInventariosForm, Connection con, UsuarioBasico usuario) {
		equiDeInventariosForm.reset();
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Metodo sobrecargado de inicio de pagina para la consulta de equivalentes de inventario
	 * @param mapping
	 * @param equiDeInventariosForm
	 * @param con
	 * @param usuario
	 * @param soloConsulta
	 * @return
	 */
	private ActionForward accionEmpezar(ActionMapping mapping, EquivalentesDeInventarioForm equiDeInventariosForm, Connection con, UsuarioBasico usuario, boolean soloConsulta) {
		if ( soloConsulta == true)
			equiDeInventariosForm.reset();
		UtilidadBD.closeConnection(con);
		return mapping.findForward("soloConsultaArtEquivalentes");
	}
	
	

}
