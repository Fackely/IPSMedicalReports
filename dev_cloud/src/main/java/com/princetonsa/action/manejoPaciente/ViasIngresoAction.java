package com.princetonsa.action.manejoPaciente;

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
import util.Listado;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.manejoPaciente.ViasIngresoForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.ViasIngreso;


public class ViasIngresoAction extends Action {
	
Logger logger =Logger.getLogger(ViasIngresoAction.class);
	

	String[] indices={"codigo_","nombre_","identificador_","paciente_","verificacion_","recibo_","convenio_","tiporegistro_","corte_facturacion_","validarcierrenotaenfer_","validarepicrisisfinali_"};
	String[] indicesGarantiaPaciente={"codigo_via_ingreso_","aconimo_tipo_paciente_","garantia_","tiporegistro_"};
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
			if (form instanceof ViasIngresoForm) 
			{
				ViasIngresoForm forma=(ViasIngresoForm) form;

				String estado=forma.getEstado();

				logger.info("Estado -->"+estado);

				con=UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());

				ViasIngreso mundo=new ViasIngreso();

				forma.setMensaje(new ResultadoBoolean(false));
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
					forma.setConvenios(Utilidades.obtenerConvenios(con, "","2",false,"",false));
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())+""));
					this.accionConsultarViasIngreso(con, forma, mundo);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("continuar"))
				{
					UtilidadBD.closeConnection(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getViasIngresoMap("numRegistros").toString()), response, request, "viasIngreso.jsp",true);

				}
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(con);
					if(UtilidadTexto.isEmpty(forma.getLinkSiguiente()))
						forma.setLinkSiguiente("../viasIngreso/viasIngreso.do?estado=continuar");
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("nuevo"))
				{
					this.accionNuevoViasIngreso(forma);
					UtilidadBD.closeConnection(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getViasIngresoMap("numRegistros").toString()), response, request, "viasIngreso.jsp",true);
				}

				else if(estado.equals("nuevoGarantiaPaciente") || estado.equals("continuarGarantiaPaciente"))
				{
					if(estado.equals("nuevoGarantiaPaciente"))
						this.accionNuevoGarantiaPaciente(forma);
					UtilidadBD.closeConnection(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getGarantiaPaciente("numRegistros").toString()), response, request, "detalleViasIngreso.jsp",true);
				}
				else if(estado.equals("eliminar"))
				{
					UtilidadBD.closeConnection(con);
					Utilidades.eliminarRegistroMapaGenerico(forma.getViasIngresoMap(),forma.getViasIngresoEliminadosMap(),forma.getPosEliminar(), indices, "numRegistros", "tiporegistro_", ConstantesBD.acronimoSi, false);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getViasIngresoMap("numRegistros").toString()), response, request, "viasIngreso.jsp",forma.getPosEliminar()==(Integer.parseInt(forma.getViasIngresoMap("numRegistros")+"")));
				}
				else if(estado.equals("guardar"))
				{

					//guardamos en BD.
					this.accionGuardarRegistros(con,forma,mundo,usuario);

					//limipiamos el form
					forma.reset();
					forma.setConvenios(Utilidades.obtenerConvenios(con, "","2",false,"",false));
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					this.accionConsultarViasIngreso(con,forma,mundo);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("ordenar"))
				{
					this.accionOrdenarMapa(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("detalleViasIngreso"))
				{
					return this.accionDetalleViasIngreso(con,forma,mundo,usuario,mapping);				
				}
				else if(estado.equals("guardarDetalle"))
				{
					this.accionGuardarDetalle(con,forma,mundo, usuario);
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					this.accionDetalleViasIngreso(con, forma, mundo, usuario, mapping);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("detalle");
				}
				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de Vias de Ingreso ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de ViasIngresoForm");
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
	 * Guardar detalle vias de ingreso
	 * */
	private void accionGuardarDetalle(Connection con, ViasIngresoForm forma, ViasIngreso mundo, UsuarioBasico usuario)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		for(int i=0;i<Integer.parseInt(forma.getGarantiaPaciente("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getGarantiaPaciente("tiporegistro_"+i)+"").trim().equalsIgnoreCase(ConstantesBD.acronimoSi))
			{
				HashMap vo=new HashMap();
				vo.put("codigo_via_ingreso", forma.getViasIngresoMap("codigo_"+forma.getIndexSeleccionado()));
				vo.put("acronimo_tipo_paciente", forma.getGarantiaPaciente("acronimo_tipo_paciente_"+i)+"");
				vo.put("garantia", forma.getGarantiaPaciente("garantia_"+i)+"");
				vo.put("codinterfaz", forma.getGarantiaPaciente("codinterfaz_"+i)+"");
				vo.put("tamfactura", forma.getGarantiaPaciente("tamfactura_"+i)+"");
				
				vo.put("bloqueaingresodeudor", forma.getGarantiaPaciente("bloqueaingresodeudor_"+i)+"");
				vo.put("bloqueaingresopaciente", forma.getGarantiaPaciente("bloqueaingresopaciente_"+i)+"");
				transaccion=mundo.modificarGarantiaPaciente(con, vo);
				logger.info("mapa al salir de la accion modificar-->> "+vo);
			}
			//insertar
			else if((forma.getGarantiaPaciente("tiporegistro_"+i)+"").trim().equalsIgnoreCase(ConstantesBD.acronimoNo))
			{
				HashMap vo=new HashMap();
				vo.put("codigo_via_ingreso", forma.getViasIngresoMap("codigo_"+forma.getIndexSeleccionado()));
				vo.put("acronimo_tipo_paciente", forma.getGarantiaPaciente("acronimo_tipo_paciente_"+i)+"");
				vo.put("garantia", forma.getGarantiaPaciente("garantia_"+i)+"");
				vo.put("codinterfaz", forma.getGarantiaPaciente("codinterfaz_"+i)+"");
				vo.put("tamfactura", forma.getGarantiaPaciente("tamfactura_"+i)+"");
				vo.put("bloqueaingresodeudor", forma.getGarantiaPaciente("bloqueaingresodeudor_"+i)+"");
				vo.put("bloqueaingresopaciente", forma.getGarantiaPaciente("bloqueaingresopaciente_"+i)+"");
				transaccion=mundo.insertarGarantiaPaciente(con, vo);
				logger.info("mapa al salir de la accion insertar-->> "+vo);
			}
		}
		//return transaccion;
		
		if(transaccion)
		{
			forma.setMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		
		/*if(transaccion)
			transaccion=this.accionGuardarGarantiaPaciente(con,forma,mundo,usuario,transaccion);
		if(transaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}*/
	}
	
	private void accionGuardarRegistros(Connection con, ViasIngresoForm forma, ViasIngreso mundo, UsuarioBasico usuario)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		for(int i=0;i<Integer.parseInt(forma.getViasIngresoMap("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getViasIngresoMap("tiporegistro_"+i)+"").trim().equals(ConstantesBD.acronimoSi)&&this.existeModificacion(con,forma,mundo,Integer.parseInt(forma.getViasIngresoMap("codigo_"+i)+""),i,usuario))
			{
				HashMap vo=new HashMap();
				vo.put("codigo",forma.getViasIngresoMap("codigo_"+i));
				vo.put("nombre",forma.getViasIngresoMap("nombre_"+i));
				vo.put("identificador",forma.getViasIngresoMap("identificador_"+i));
				vo.put("responsable_paciente",forma.getViasIngresoMap("paciente_"+i));
				vo.put("verificacion_derechos",forma.getViasIngresoMap("verificacion_"+i));
				vo.put("recibo_automatico",forma.getViasIngresoMap("recibo_"+i));
				vo.put("convenio",forma.getViasIngresoMap("convenio_"+i));
				vo.put("corte_facturacion",forma.getViasIngresoMap("corte_facturacion_"+i));
				
				vo.put("validarcierrenotaenfer",forma.getViasIngresoMap("validarcierrenotaenfer_"+i));
				vo.put("validarepicrisisfinali",forma.getViasIngresoMap("validarepicrisisfinali_"+i));
				
				vo.put("usuario_modifica",usuario.getLoginUsuario());
				vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("hora_modifica",UtilidadFecha.getHoraActual());
				
				transaccion=mundo.modificar(con, vo);
			}
			
			//insertar
			else if((forma.getViasIngresoMap("tiporegistro_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
			{
				HashMap vo=new HashMap();
				vo.put("codigo",forma.getViasIngresoMap("codigo_"+i));
				vo.put("nombre",forma.getViasIngresoMap("nombre_"+i));
				vo.put("identificador",forma.getViasIngresoMap("identificador_"+i));
				vo.put("responsable_paciente",forma.getViasIngresoMap("paciente_"+i));
				vo.put("verificacion_derechos",forma.getViasIngresoMap("verificacion_"+i));
				vo.put("recibo_automatico",forma.getViasIngresoMap("recibo_"+i));
				vo.put("convenio",forma.getViasIngresoMap("convenio_"+i));
				vo.put("corte_facturacion",forma.getViasIngresoMap("corte_facturacion_"+i));
				vo.put("validarcierrenotaenfer",forma.getViasIngresoMap("validarcierrenotaenfer_"+i));
				vo.put("validarepicrisisfinali",forma.getViasIngresoMap("validarepicrisisfinali_"+i));
				vo.put("usuario_modifica",usuario.getLoginUsuario());
				vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("hora_modifica",UtilidadFecha.getHoraActual());
				transaccion=mundo.insertar(con, vo);
			}
			
		}
		
		if(transaccion)
		{
			forma.setMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
	}
	
	/*private boolean accionGuardarGarantiaPaciente(Connection con, ViasIngresoForm forma, ViasIngreso mundo, UsuarioBasico usuario, boolean transaccion)
	{
		for(int i=0;i<Integer.parseInt(forma.getGarantiaPaciente("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getGarantiaPaciente("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacionGarantia(con, forma.getGarantiaPaciente(), mundo.consultarGarantiaPaciente(con, Integer.parseInt(forma.getGarantiaPaciente("codigo_"+i)+"")),i,usuario,indicesGarantiaPaciente))
			{
				HashMap vo=new HashMap();
				vo.put("garantia", forma.getGarantiaPaciente("garantia_"+i)+"");
				transaccion=mundo.modificarGarantiaPaciente(con, vo);
			}
			//insertar
			else if((forma.getGarantiaPaciente("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigo_via_ingreso", forma.getGarantiaPaciente("codigo_"+i)+"");
				vo.put("acronimo_tipo_paciente", forma.getGarantiaPaciente("acronimo_"+i)+"");
				vo.put("garantia", forma.getGarantiaPaciente("garantia_"+i)+"");
				transaccion=mundo.insertarGarantiaPaciente(con, vo);
			}
		}
		return transaccion;
	}*/
	
	/**
	 * detalle vias de ingreso
	 * */
	private ActionForward accionDetalleViasIngreso(Connection con, ViasIngresoForm forma, ViasIngreso mundo,UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.setGarantiaPaciente(mundo.consultarGarantiaPaciente(con, Integer.parseInt(forma.getViasIngresoMap("codigo_"+forma.getIndexSeleccionado())+"")));
		forma.setNoTipPacViaIngreso(mundo.consultarNoTipoPacViaIng(con, Integer.parseInt(forma.getViasIngresoMap("codigo_"+forma.getIndexSeleccionado())+"")));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalle");
	}
	
	/**
	 * Generar Log
	 * */
	private void generarLog(ViasIngresoForm forma, ViasIngreso mundo, UsuarioBasico usuario, boolean isEliminacion, int pos) 
	{
		String log = "";
		int tipoLog=0;
		
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(forma.getViasIngresoEliminadosMap(indices[i]+""+pos)+""):forma.getViasIngresoEliminadosMap(indices[i]+""+pos))+" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(mundo.getViasIngresoMap().get(indices[i]+"0")+""):mundo.getViasIngresoMap().get(indices[i]+"0")) + " ]";
			}
			log += 		 "\n   ============INFORMACION MODIFICADA=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(forma.getViasIngresoMap(indices[i]+""+pos)+""):forma.getViasIngresoMap(indices[i]+""+pos)) +" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		
		LogsAxioma.enviarLog(ConstantesBD.logViasIngresoCodigo,log,tipoLog,usuario.getLoginUsuario());
	}
	
	/**
	 * Existe modificacion
	 * */
	private boolean existeModificacion(Connection con, ViasIngresoForm forma, ViasIngreso mundo, int codigo,int pos, UsuarioBasico usuario)
	{
		HashMap temp=mundo.consultarViasIngresoEspecifico(con, codigo);
		for(int i=0;i<indices.length;i++)
		{
			if(temp.containsKey(indices[i]+"0")&&forma.getViasIngresoMap().containsKey(indices[i]+""+pos))
			{
				if(!((temp.get(indices[i]+"0")+"").trim().equals(forma.getViasIngresoMap(indices[i]+""+pos)+"")))
				{
					mundo.setViasIngresoMap(temp);
					this.generarLog(forma, mundo, usuario, false, pos);
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean existeModificacionGarantia(Connection con, HashMap mapa, HashMap mapaTemp, int pos, UsuarioBasico usuario, String[] indices)
	{
		for(int i=0;i<indices.length;i++)
		{
			if(mapaTemp.containsKey(indices[i]+"0")&&mapa.containsKey(indices[i]+""+pos))
			{
				if(!((mapaTemp.get(indices[i]+"0")+"").trim().equals((mapa.get(indices[i]+""+pos)+"").trim())))
				{
					Utilidades.generarLogGenerico(mapa, mapaTemp, usuario.getLoginUsuario(), false, pos,ConstantesBD.logCoberturaConvenioCodigo,indices);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Accion Nueva Via de ingreso
	 * */
	private void accionNuevoViasIngreso(ViasIngresoForm forma)
	{
		int pos=Integer.parseInt(forma.getViasIngresoMap("numRegistros")+"");
		forma.setViasIngresoMap("codigo_"+pos,"");
		forma.setViasIngresoMap("nombre_"+pos,"");
		forma.setViasIngresoMap("identificador_"+pos,"");
		forma.setViasIngresoMap("paciente_"+pos,ConstantesBD.acronimoNo);
		forma.setViasIngresoMap("verificacion_"+pos,ConstantesBD.acronimoNo);	
		forma.setViasIngresoMap("recibo_"+pos,ConstantesBD.acronimoNo);
		forma.setViasIngresoMap("convenio_"+pos,"");
		forma.setViasIngresoMap("tiporegistro_"+pos,ConstantesBD.acronimoNo);
		forma.setViasIngresoMap("numRegistros", (pos+1)+"");
		forma.setViasIngresoMap("validarcierrenotaenfer_"+pos,ConstantesBD.acronimoNo);
		forma.setViasIngresoMap("validarepicrisisfinali_"+pos,ConstantesBD.acronimoNo);	
		forma.setViasIngresoMap("corte_facturacion_"+pos,ConstantesBD.acronimoNo);					
	}
	
	/**
	 * Accion ordenar mapa
	 * */
	private void accionOrdenarMapa(ViasIngresoForm forma)
	{
		
		int numReg=Integer.parseInt(forma.getViasIngresoMap("numRegistros")+"");
		forma.setViasIngresoMap(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getViasIngresoMap(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setViasIngresoMap("numRegistros",numReg+"");
	}
	
	
	/**
	 * Accion consultar Vias de ingreso
	 */
	private void accionConsultarViasIngreso(Connection con, ViasIngresoForm forma, ViasIngreso mundo)
	{
		mundo.consultarViasIngresoExistentes(con);
		forma.setViasIngresoMap((HashMap)mundo.getViasIngresoMap().clone());
		logger.info("\n\n");
		logger.info("Valor de Mapa Consultado >> "+forma.getViasIngresoMap());
		logger.info("\n\n");
	}
	
	/**
	 * Accion Nueva detalle Via de ingreso
	 * */
	private void accionNuevoGarantiaPaciente(ViasIngresoForm forma)
	{
		int pos=Integer.parseInt(forma.getGarantiaPaciente("numRegistros")+"");
		forma.setGarantiaPaciente("codigo_via_ingreso_"+pos,forma.getViasIngresoMap("codigo_"+forma.getIndexSeleccionado())+"");
		forma.setGarantiaPaciente("acronimo_tipo_paciente_"+pos,"");
		forma.setGarantiaPaciente("garantia_"+pos,"");
		forma.setGarantiaPaciente("tiporegistro_"+pos,ConstantesBD.acronimoNo+"");
		forma.setGarantiaPaciente("numRegistros", (pos+1)+"");
		
	}

}
