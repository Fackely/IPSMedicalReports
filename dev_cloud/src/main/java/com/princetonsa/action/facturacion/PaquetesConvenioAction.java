package com.princetonsa.action.facturacion;

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
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.facturacion.PaquetesConvenioForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.PaquetesConvenio;

public class PaquetesConvenioAction extends Action 
{
	
	/**
	 * objeto para manejar los logs de esta clase
	 */
	
	Logger logger =Logger.getLogger(PaquetesConvenioAction.class);
	
	
	/**
	 *  Matriz que contiene los indices del mapa.
	 */
	
	String[] indices={"codigo_","institucion_","convenio_","contrato_","paquete_","descpaquete_","viaingreso_","nomviaingreso_","tipopaciente_","nomtipopaciente_","fechainicialvenc_","fechafinalvenc_","tiporegistro_","tipospaciente_"};
	
	
	
	/**
	 * Método excute del Action
	 */
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
			if (form instanceof PaquetesConvenioForm) 
			{
				PaquetesConvenioForm forma=(PaquetesConvenioForm) form;

				String estado=forma.getEstado();

				logger.info("Estado -->"+estado);

				con=UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());

				PaquetesConvenio mundo=new PaquetesConvenio();
				mundo.setInstitucion(usuario.getCodigoInstitucionInt());

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
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())+""));
					forma.setConvenios(Utilidades.obtenerConvenios(con, "","",false,"",false));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("cargarContratos"))
				{
					forma.setCodigoContrato(ConstantesBD.codigoNuncaValido);
					forma.setContratos(Utilidades.obtenerContratos(con,forma.getCodigoConvenio(),true,false));
					if(forma.getContratos().size()==1)
					{
						forma.setCodigoContrato(Integer.parseInt(((HashMap)(forma.getContratos().get(0))).get("codigo")+""));
						forma.setPaquetesConMap(mundo.consultarPaquetesConvenio(con, forma.getCodigoConvenio(),forma.getCodigoContrato()));

						for(int i=0;i<Integer.parseInt(forma.getPaquetesConMap("numRegistros").toString());i++)
							forma.setPaquetesConMap("tipospaciente_"+i, UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con, forma.getPaquetesConMap("viaingreso_"+i).toString()));

						UtilidadBD.closeConnection(con);
						return mapping.findForward("principal");
					}
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("cargarInfo"))
				{

					forma.setPaquetesConMap(mundo.consultarPaquetesConvenio(con, forma.getCodigoConvenio(),forma.getCodigoContrato()));

					for(int i=0;i<Integer.parseInt(forma.getPaquetesConMap("numRegistros").toString());i++)
						forma.setPaquetesConMap("tipospaciente_"+i, UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con, forma.getPaquetesConMap("viaingreso_"+i).toString()));

					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}

				else if(estado.equals("continuar"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(con);
					if(UtilidadTexto.isEmpty(forma.getLinkSiguiente()))
						forma.setLinkSiguiente("../paquetesConvenio/paquetesConvenio.do?estado=continuar");
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("nuevo"))
				{
					this.accionNuevoPaquete(forma);
					UtilidadBD.closeConnection(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getPaquetesConMap("numRegistros").toString()), response, request, "paquetesConvenio.jsp",true);
				}
				else if(estado.equals("eliminar"))
				{
					UtilidadBD.closeConnection(con);
					return this.accionEliminarRegistro(forma,request,response,mapping);
				}
				else if(estado.equals("guardar"))
				{

					//guardamos en BD.
					this.accionGuardarRegistros(con,forma,mundo,usuario);
					/*int codConvenio= forma.getCodigoConvenio();
				int codContrato= forma.getCodigoContrato();
				forma.reset();
				forma.setCodigoContrato(codContrato);
				forma.setCodigoConvenio(codConvenio);*/
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					forma.setContratos(Utilidades.obtenerContratos(con,forma.getCodigoConvenio(),true,false));
					forma.setPaquetesConMap(mundo.consultarPaquetesConvenio(con, forma.getCodigoConvenio(),forma.getCodigoContrato()));
					for(int i=0;i<Integer.parseInt(forma.getPaquetesConMap("numRegistros").toString());i++)
						forma.setPaquetesConMap("tipospaciente_"+i, UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con, forma.getPaquetesConMap("viaingreso_"+i).toString()));
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");

				}
				else if(estado.equals("ordenar"))
				{
					this.accionOrdenarMapa(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("consultarTipoPaciente"))
				{
					this.accionConsultarTipoPaciente(con,forma,mundo,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
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
	 * Consultar tipos de pacientes según la vía de ingreso
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionConsultarTipoPaciente(Connection con, PaquetesConvenioForm forma, PaquetesConvenio mundo, UsuarioBasico usuario) {
		//	se carga el tipo de paciente
		forma.setPaquetesConMap("tipospaciente_"+forma.getPosMapa(),UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con,forma.getPaquetesConMap("viaingreso_"+forma.getPosMapa()).toString()));
		logger.info("prueba..."+forma.getPaquetesConMap("tipospaciente_"+forma.getPosMapa()));
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	
	private void accionGuardarRegistros(Connection con, PaquetesConvenioForm forma, PaquetesConvenio mundo, UsuarioBasico usuario)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getPaquetesConEliminadosMap("numRegistros")+"");i++)
		{
			if(mundo.eliminarRegistro(con,(forma.getPaquetesConEliminadosMap("codigo_"+i)+"")))
			{
				this.generarLog(forma, mundo, usuario, true, i);
				transaccion=true;
			}
		}
		
		for(int i=0;i<Integer.parseInt(forma.getPaquetesConMap("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getPaquetesConMap("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma,mundo,i,usuario,forma.getPaquetesConMap("codigo_"+i)+""))
			{
				HashMap vo=new HashMap();
				vo.put("codigo",forma.getPaquetesConMap("codigo_"+i));
				vo.put("convenio",forma.getCodigoConvenio());
				vo.put("contrato",forma.getCodigoContrato());
				vo.put("paquete",forma.getPaquetesConMap("paquete_"+i));
				vo.put("via_ingreso",forma.getPaquetesConMap("viaingreso_"+i));
				vo.put("tipo_paciente",forma.getPaquetesConMap("tipopaciente_"+i));
				vo.put("fecha_inicial_venc",UtilidadFecha.conversionFormatoFechaABD(forma.getPaquetesConMap("fechainicialvenc_"+i)+""));
				vo.put("fecha_final_venc",UtilidadFecha.conversionFormatoFechaABD(forma.getPaquetesConMap("fechafinalvenc_"+i)+""));
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("usuario_modifica",usuario.getLoginUsuario());
				vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("hora_modifica",UtilidadFecha.getHoraActual());
				transaccion=mundo.modificar(con, vo);
			}
			
			//insertar
			
			else if((forma.getPaquetesConMap("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				/*vo.put("codigo",forma.getPaquetesConMap("codigo_"+i));*/
				vo.put("convenio",forma.getCodigoConvenio());
				vo.put("contrato",forma.getCodigoContrato());
				vo.put("paquete",forma.getPaquetesConMap("paquete_"+i));
				vo.put("via_ingreso",forma.getPaquetesConMap("viaingreso_"+i));
				vo.put("tipo_paciente",forma.getPaquetesConMap("tipopaciente_"+i));
				vo.put("fecha_inicial_venc",UtilidadFecha.conversionFormatoFechaABD(forma.getPaquetesConMap("fechainicialvenc_"+i)+""));
				vo.put("fecha_final_venc",UtilidadFecha.conversionFormatoFechaABD(forma.getPaquetesConMap("fechafinalvenc_"+i)+""));
				vo.put("institucion", usuario.getCodigoInstitucion());
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
	
	
	
	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param isEliminacion
	 * @param pos
	 */
	
	private void generarLog(PaquetesConvenioForm forma, PaquetesConvenio mundo, UsuarioBasico usuario, boolean isEliminacion, int pos) 
	{
		String log = "";
		int tipoLog=0;
		
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(forma.getPaquetesConEliminadosMap(indices[i]+""+pos)+""):forma.getPaquetesConEliminadosMap(indices[i]+""+pos))+" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(mundo.getPaquetesConMap().get(indices[i]+"0")+""):mundo.getPaquetesConMap().get(indices[i]+"0")) + " ]";
			}
			log += 		 "\n   ============INFORMACION MODIFICADA=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(forma.getPaquetesConMap(indices[i]+""+pos)+""):forma.getPaquetesConMap(indices[i]+""+pos)) +" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		
		LogsAxioma.enviarLog(ConstantesBD.logPaquetesConvenioCodigo,log,tipoLog,usuario.getLoginUsuario());
	}
	
	
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param institucion
	 * @param pos
	 * @param usuario
	 * @param codigoPaquete
	 * @return
	 */
	
	private boolean existeModificacion(Connection con, PaquetesConvenioForm forma, PaquetesConvenio mundo,int pos, UsuarioBasico usuario, String codigo)
	{
		HashMap temp=mundo.consultarPaqueteConvenioEspecifico(con, codigo);
		for(int i=0;i<indices.length;i++)
		{
			if(temp.containsKey(indices[i]+"0")&&forma.getPaquetesConMap().containsKey(indices[i]+""+pos))
			{
				if(!((temp.get(indices[i]+"0")+"").trim().equals(forma.getPaquetesConMap(indices[i]+""+pos)+"")))
				{
					mundo.setPaquetesConMap(temp);
					this.generarLog(forma, mundo, usuario, false, pos);
					return true;
				}
			}
		}
		return false;
	}
	
	
	
	
	/**
	 * 
	 * @param forma
	 * @param request
	 * @param response
	 * @param mapping
	 * @return
	 */
	
	private ActionForward accionEliminarRegistro(PaquetesConvenioForm forma, HttpServletRequest request, HttpServletResponse response, ActionMapping mapping)
	{
		int numRegMapEliminados=Integer.parseInt(forma.getPaquetesConEliminadosMap("numRegistros")+"");
		int ultimaPosMapa=(Integer.parseInt(forma.getPaquetesConMap("numRegistros")+"")-1);
		
		//poner la informacion en el otro mapa.
		
//		solo pasar al mapa los registros que son de BD
		if((forma.getPaquetesConMap("tiporegistro_"+forma.getPosEliminar())+"").trim().equalsIgnoreCase("BD"))
		{
			for(int i=0;i<indices.length;i++)
			{
			
				forma.setPaquetesConEliminadosMap(indices[i]+""+numRegMapEliminados, forma.getPaquetesConMap(indices[i]+""+forma.getPosEliminar()));
			}
			forma.setPaquetesConEliminadosMap("numRegistros", (numRegMapEliminados+1));
		}
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=forma.getPosEliminar();i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				forma.setPaquetesConMap(indices[j]+""+i,forma.getPaquetesConMap(indices[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			forma.getPaquetesConMap().remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamo el numero de registros en el mapa.
		forma.setPaquetesConMap("numRegistros",ultimaPosMapa);
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getPaquetesConMap("numRegistros").toString()), response, request, "paquetesConvenio.jsp",forma.getPosEliminar()==ultimaPosMapa);

		
	}
	
	
	
	
	/**
	 * 
	 * @param forma
	 */	
	
	private void accionNuevoPaquete(PaquetesConvenioForm forma)
	{
		int pos=Integer.parseInt(forma.getPaquetesConMap("numRegistros")+"");
		forma.setPaquetesConMap("institucion_"+pos,"");
		forma.setPaquetesConMap("convenio_"+pos,"");
		forma.setPaquetesConMap("contrato_"+pos,"");
		forma.setPaquetesConMap("paquete_"+pos,"");
		forma.setPaquetesConMap("viaingreso_"+pos,"");
		forma.setPaquetesConMap("fechainicialvenc_"+pos,"");
		forma.setPaquetesConMap("fechafinalvenc_"+pos,"");
		forma.setPaquetesConMap("tiporegistro_"+pos,"MEM");
		forma.setPaquetesConMap("numRegistros", (pos+1)+"");
		forma.setPaquetesConMap("tipospaciente_"+pos, new ArrayList<HashMap<String, Object>>());
	}
	
	
	/**
	 * 
	 * @param forma
	 */
	
	private void accionOrdenarMapa(PaquetesConvenioForm forma)
	{
		
		int numReg=Integer.parseInt(forma.getPaquetesConMap("numRegistros")+"");
		forma.setPaquetesConMap(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getPaquetesConMap(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setPaquetesConMap("numRegistros",numReg+"");
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	
	private void accionConsultarPaquetesConvenio(Connection con, PaquetesConvenioForm forma, PaquetesConvenio mundo, UsuarioBasico usuario)
	{
		mundo.consultarPaquetesConvenioExistentes(con,usuario.getCodigoInstitucion());
		forma.setPaquetesConMap((HashMap)mundo.getPaquetesConMap().clone());
	}

}
