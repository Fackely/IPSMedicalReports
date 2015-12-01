 package com.princetonsa.action.facturacion;

import java.sql.Connection;
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
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.ActualizacionAutomaticaEsquemaTarifarioForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ActualizacionAutomaticaEsquemaTarifario;

public class ActualizacionAutomaticaEsquemaTarifarioAction extends Action 
{

	
	/**
	 * objeto para manejar los logs de esta clase
	 */
	Logger logger =Logger.getLogger(ActualizacionAutomaticaEsquemaTarifarioAction.class);
	
	/**
	 * 
	 */
	private static String[] indicesConvenio = {"convenio_","contrato_","tiporegistro_"};
	
	/**
	 * 
	 */
	private static String[] indicesInventario = {"claseinventario_","nombreclaseinventario_","esquemainventario_","fechainventario_","tiporegistro_"};
	
	/**
	 * 
	 */
	private static String[] indicesServicio = {"gruposervicio_","acronimogruposervicio_","nombregruposervicio_","esquemaservicio_","fechaservicio_","tiporegistro_"};
	
	
	/**
	 * Método excute del Action
	 */
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
			if (form instanceof ActualizacionAutomaticaEsquemaTarifarioForm) 
			{
				ActualizacionAutomaticaEsquemaTarifarioForm forma=(ActualizacionAutomaticaEsquemaTarifarioForm) form;

				String estado=forma.getEstado();

				logger.info("Estado -->"+estado);

				con=UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());

				ActualizacionAutomaticaEsquemaTarifario mundo=new ActualizacionAutomaticaEsquemaTarifario();

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
					forma.resetMensaje();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("consultaContrato"))
				{
					forma.setEstado("empezar");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("contratoBusqueda"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("busqueda");
				}
				else if(estado.equals("guardar"))
				{
					//forma.reset();
					this.accionGuardar(con, forma, mundo, usuario, request);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("adicionarConvenio"))
				{
					this.adicionarConvenio(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("adicionarInventario"))
				{
					//forma.reset();
					this.adicionarInventario(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("adicionarServicio"))
				{
					//forma.reset();
					this.adicionarServicio(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("eliminarConvenio"))
				{
					//eliminar convenio-contrato
					Utilidades.eliminarRegistroMapaGenerico(forma.getMapaConvenio(),forma.getMapaConvenio(),forma.getIndiceEliminar(),indicesConvenio,"numRegistros","tiporegistro_","BD",false);
					forma.setIndiceEliminar(ConstantesBD.codigoNuncaValido);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("eliminarInventario"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getMapaEsquemaInventario(), forma.getMapaEsquemaInventario(), forma.getIndiceInventario(), indicesInventario, "numRegistros", "tiporegistro", "BD", false);
					forma.setIndiceInventario(ConstantesBD.codigoNuncaValido);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("eliminarServicio"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getMapaEsquemaServicio(), forma.getMapaEsquemaServicio(), forma.getIndiceServicio(), indicesServicio, "numRegistros", "tipoRegistro", "BD", false);
					forma.setIndiceServicio(ConstantesBD.codigoNuncaValido);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("busquedaAvanzada"))
				{
					forma.resetBusqueda();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("busqueda");
				}
				else if(estado.equals("buscarConvenios"))
				{
					forma.setMapaConvenio(mundo.consultaConveniosVigentes(con, forma.getConvenio(), forma.getContrato(), forma.getEmpresa(), forma.getTipoConvenio(), forma.getEsquemaServicios(), forma.getEsquemaInventarios()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("busqueda");
				}
				else if(estado.equals("asignarConvenios"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
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
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardar(Connection con, ActualizacionAutomaticaEsquemaTarifarioForm forma, ActualizacionAutomaticaEsquemaTarifario mundo, UsuarioBasico usuario, HttpServletRequest request) 
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		
		for(int i=0;i<Integer.parseInt(forma.getMapaConvenio("numRegistros")+"");i++)
		{
			if((forma.getMapaConvenio("seleccionado_"+i)+"").equals(ConstantesBD.acronimoSi))
			{	
			
				for(int j=0;j<Integer.parseInt(forma.getMapaEsquemaInventario("numRegistros")+"");j++)
				{
							
					//insertar
					HashMap vo=new HashMap();
					vo.put("codigo",forma.getMapaEsquemaInventario("codigo_"+j));
					vo.put("contrato",forma.getMapaConvenio("contrato_"+i));
					vo.put("clase_inventario",forma.getMapaEsquemaInventario("claseinventario_"+j));
					vo.put("esquema_tarifario",forma.getMapaEsquemaInventario("esquemainventario_"+j));
					vo.put("fecha_vigencia",UtilidadFecha.conversionFormatoFechaABD(forma.getMapaEsquemaInventario("fechainventario_"+j)+""));
					vo.put("usuario_modifica",usuario.getLoginUsuario());
					vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
					vo.put("hora_modifica",UtilidadFecha.getHoraActual());
					transaccion=mundo.insertarInventario(con, vo);
						
				}
				for(int j=0;j<Integer.parseInt(forma.getMapaEsquemaServicio("numRegistros")+"");j++)
				{
					
					//insertar
					HashMap vo=new HashMap();
					vo.put("codigo",forma.getMapaEsquemaServicio("codigo_"+j));
					vo.put("contrato",forma.getMapaConvenio("contrato_"+i));
					vo.put("grupo_servicio",forma.getMapaEsquemaServicio("gruposervicio_"+j));
					vo.put("esquema_tarifario",forma.getMapaEsquemaServicio("esquemaservicio_"+j));
					vo.put("fecha_vigencia",UtilidadFecha.conversionFormatoFechaABD(forma.getMapaEsquemaServicio("fechaservicio_"+j)+""));
					vo.put("usuario_modifica",usuario.getLoginUsuario());
					vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
					vo.put("hora_modifica",UtilidadFecha.getHoraActual());
					transaccion=mundo.insertarServicio(con, vo);
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
	private void adicionarServicio(ActualizacionAutomaticaEsquemaTarifarioForm forma) 
	{
		int pos=Integer.parseInt(forma.getMapaEsquemaServicio("numRegistros")+"");
		forma.setMapaEsquemaServicio("gruposervicio_"+pos,"");
		forma.setMapaEsquemaServicio("acronimogruposervicio_"+pos,"");
		forma.setMapaEsquemaServicio("nombregruposervicio_"+pos,"");
		forma.setMapaEsquemaServicio("esquemaservicio_"+pos,"");
		forma.setMapaEsquemaServicio("fechaservicio_"+pos,"");
		forma.setMapaEsquemaServicio("tiporegistro_"+pos,"MEM");
		forma.setMapaEsquemaServicio("numRegistros", (pos+1)+"");
	}

	/**
	 * 
	 * @param forma
	 */
	private void adicionarInventario(ActualizacionAutomaticaEsquemaTarifarioForm forma) 
	{
		int pos=Integer.parseInt(forma.getMapaEsquemaInventario("numRegistros")+"");
		forma.setMapaEsquemaInventario("claseinventario_"+pos,"");
		forma.setMapaEsquemaInventario("nombreclaseinventario_"+pos,"");
		forma.setMapaEsquemaInventario("esquemainventario_"+pos,"");
		forma.setMapaEsquemaInventario("fechainventario_"+pos,"");
		forma.setMapaEsquemaInventario("tiporegistro_"+pos,"MEM");
		forma.setMapaEsquemaInventario("numRegistros", (pos+1)+"");
	}

	/**
	 * 
	 * @param forma
	 */
	private void adicionarConvenio(ActualizacionAutomaticaEsquemaTarifarioForm forma) 
	{
		int pos=Integer.parseInt(forma.getMapaConvenio("numRegistros")+"");
		forma.setMapaConvenio("convenio_"+pos,"");
		forma.setMapaConvenio("contrato_"+pos,"");
		forma.setMapaConvenio("tiporegistro_"+pos,"MEM");
		forma.setMapaConvenio("numRegistros", (pos+1)+"");
	}
	
	
}
