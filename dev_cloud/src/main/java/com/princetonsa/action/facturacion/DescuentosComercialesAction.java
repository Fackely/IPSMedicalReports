/*
 * Creado May 22, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * DescuentosComercialesAction
 * com.princetonsa.action.facturacion
 * java version "1.5.0_07"
 */
package com.princetonsa.action.facturacion;

import java.math.BigDecimal;
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
import util.ConstantesIntegridadDominio;
import util.InfoDatosString;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.facturacion.DescuentosComercialesForm;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.facturacion.DtoProgDescComercialConvenioContrato;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.DescuentosComerciales;

/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 22, 2007
 */
public class DescuentosComercialesAction extends Action
{
	
	/**
	 * 
	 */
	private Logger logger=Logger.getLogger(DescuentosComercialesAction.class);
	
	/**
	 * 
	 */
	private String[] indicesDescuentos={"codigo_","viaingreso_","nomviaingreso_","tipopaciente_","nomtipopaciente_","fechavigencia_","tiporegistro_"};	

	/**
	 * 
	 */
	private String[] indicesAgrupacionArticulos={"codigo_","codigodescuento_","clase_","nomclase_","grupo_","nomgrupo_","subgrupo_","nomsubgrupo_","naturaleza_","nomnaturaleza_","porcentaje_","valor_","tiporegistro_","fechavigencia_"};
	
	/**
	 * 
	 */
	private String[] indicesArticulos={"codigo_","codigodescuento_","codigoArticulo_","descripcionArticulo_","porcentaje_","valor_","tiporegistro_","fechavigencia_"};
	
	/**
	 * 
	 */
	private String[] indicesAgrupacionServicio={"codigo_","codigodescuento_","tipopos_","gruposervicio_","acronimogruposervicio_","descgruposervicio_","tiposervicio_","nomtiposervicio_","especialidad_","nomespecialidad_","porcentaje_","valor_","tiporegistro_","fechavigencia_"};
	
	/**
	 * 
	 */
	private String[] indicesServicios={"codigo_","codigodescuento_","codigoServicio_","descripcionServicio_","porcentaje_","valor_","tiporegistro_","fechavigencia_"};
/**
 * 
 */
	private String[] indicesMapMap={"tipopaciente_"};
	/**
	 * Metodo que lleva el flujo de la fuincionalidad
	 */
	public ActionForward execute(	ActionMapping mapping, 	ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		Connection con=null;
		try{
			if(form instanceof DescuentosComercialesForm)
			{
				DescuentosComercialesForm forma=(DescuentosComercialesForm) form;
				DescuentosComerciales mundo=new DescuentosComerciales();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
				con=UtilidadBD.abrirConexion();
				String estado=forma.getEstado();
				// this.setUtilizaProgramas(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(institucion).equals(ConstantesBD.acronimoSi));

				forma.setUtilizaProgramas(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt()));

				logger.info("ESTADO -->"+estado);


				if(estado.equals("empezar"))
				{
					forma.reset();
					forma.setConvenios(Utilidades.obtenerConvenios(con, "","",false,"",false));

					UtilidadBD.closeConnection(con);
					logger.info(">>> Vamos realizar maping al principal");
					return mapping.findForward("principal");
				}
				else if(estado.equals("cargarContratos"))
				{
					logger.info("*************************************************************");
					logger.info("  CODIGO CONVENIO  "+forma.getCodigoConvenio());
					logger.info("*************************************************************");
					DtoConvenio dtoConvenio = new DtoConvenio();
					dtoConvenio.setCodigo(forma.getCodigoConvenio());
					dtoConvenio.setTipoAtencion(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico);

					logger.info("*************************************************************");


					logger.info("*************************************************************");
					logger.info("*************************************************************");
					if(DescuentosComerciales.validarTipoAtencionOdontologica( dtoConvenio)>0)
					{
						forma.setEsTipoAtencioOdontologicaEspecial(ConstantesBD.acronimoSi);
					}


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
					//forma.setTipoPacienteMapMap("tipopaciente_"+forma.getIndice(),forma.getTipoPacienteMap());
					return this.accionCargarInfo(con,forma,mundo,usuario,mapping);
				}
				else if(estado.equals("cargarTipoPaciente"))
				{
					forma.setViasDescuentos("tiposPaciente_"+forma.getIndice(), UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con, forma.getViasDescuentos("viaingreso_"+forma.getIndice())+""));

					return mapping.findForward("principal");
				}
				else if(estado.equals("nuevaVia"))
				{

					forma.setViasDescuentos("tiposPaciente_"+forma.getViasDescuentos("numRegistros"), new ArrayList<HashMap<String, Object>>());
					Utilidades.nuevoRegistroMapaGenerico(forma.getViasDescuentos(),indicesDescuentos,"numRegistros","tiporegistro_","MEM");
					forma.getViasDescuentos().put("fechavigencia_"+(Utilidades.convertirAEntero(forma.getViasDescuentos().get("numRegistros")+"")-1), UtilidadFecha.getFechaActual(con)+"");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("eliminarVia"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getViasDescuentos(),forma.getViasDescuentosEliminados(),forma.getPosEliminar(),indicesDescuentos,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("guardarViaDescuento"))
				{
					return this.accionGuardarViaDescuento(con, forma, mundo, usuario, mapping);
				}
				else if(estado.equals("detalleViaDescuento"))
				{
					forma.setDetalleDescuentoAnterior(false);
					return this.accionDetalleDescuento(con,forma,mundo,usuario,mapping);
				}
				else if(estado.equals("detalleViaDescuentoAnterior"))
				{
					forma.setDetalleDescuentoAnterior(true);
					return this.accionDetalleDescuentoAnterior(con,forma,mundo,usuario,mapping);
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
					Utilidades.eliminarRegistroMapaGenerico(forma.getAgrupacionArticulos(),forma.getAgrupacionArticulosEliminados(),forma.getPosEliminar(),indicesAgrupacionArticulos,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");	
				}

				else if(estado.equals("nuevoArticulo"))
				{
					String[] indicesTemp={"codigo_","codigodescuento_","porcentaje_","valor_","tiporegistro_"};
					Utilidades.nuevoRegistroMapaGenerico(forma.getArticulos(),indicesTemp,"numRegistros","tiporegistro_","MEM");
					forma.getArticulos().put("fechavigencia_"+(Utilidades.convertirAEntero(forma.getArticulos().get("numRegistros")+"")-1), forma.getFechaVigencia()+"");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}
				else if(estado.equals("eliminarArticulo"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getArticulos(),forma.getArticulosEliminados(),forma.getPosEliminar(),indicesArticulos,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");	
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
					Utilidades.eliminarRegistroMapaGenerico(forma.getAgrupacionServicios(),forma.getAgrupacionServiciosEliminados(),forma.getPosEliminar(),indicesAgrupacionServicio,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");	
				}
				else if(estado.equals("nuevoServicio"))
				{
					String[] indicesTemp={"codigo_","codigodescuento_","porcentaje_","valor_","tiporegistro_"};
					Utilidades.nuevoRegistroMapaGenerico(forma.getServicios(),indicesTemp,"numRegistros","tiporegistro_","MEM");
					forma.getServicios().put("fechavigencia_"+(Utilidades.convertirAEntero(forma.getServicios().get("numRegistros")+"")-1), forma.getFechaVigencia()+"");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}
				else if(estado.equals("eliminarServicio"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getServicios(),forma.getServiciosEliminados(),forma.getPosEliminar(),indicesServicios,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");	
				}
				else if(estado.equals("guardar"))
				{
					this.accionGuardar(con,forma,mundo,usuario);
					//consultar lo que se guardo.
					return this.accionDetalleDescuento(con,forma,mundo,usuario,mapping);
				}
				else if(estado.equals("adiccionarProgramas"))
				{	

					UtilidadBD.closeConnection(con);
					return this.adiccionarProgramas(forma, mapping);
				}
				else if(estado.equals("eliminarProgramas")){
					UtilidadBD.closeConnection(con);
					return this.eliminarListaProgramas(forma, mapping);
				}

				else if(estado.equals("anteriores"))
				{

					//forma.setViasDescuentosAnterior(mundo.obtenerListadoViasDescuento(con,usuario.getCodigoInstitucionInt(),forma.getContrato(),false,false));
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





	private void organizarNoVigentes(Connection con,DescuentosComercialesForm forma, UsuarioBasico usuario,DescuentosComerciales mundo) 
	{

		logger.info("\n entre a organizarNoVigentes  -->"+forma.getViasDescuentos());
		HashMap tmp= mundo.obtenerListadoViasDescuento(con,usuario.getCodigoInstitucionInt(),forma.getContrato(),true,true);
		int numReg= Utilidades.convertirAEntero(forma.getViasDescuentos("numRegistros")+"");
		int numReg2=Utilidades.convertirAEntero(tmp.get("numRegistros")+"");
		HashMap noVig= new HashMap ();
		noVig.put("numRegistros", 0); 
		forma.setViasDescuentosAnterior(noVig);
		boolean esta=false;
		int index=0;
		for (int j=0;j<numReg2;j++)
		{
			esta=false;
			for (int i=0;i<numReg;i++)
			{
				
				if ((forma.getViasDescuentos("codigo_"+i)+"").equals(tmp.get("codigo_"+j)+""))
				{
					//logger.info("\n \n ########################  esta @################# ");
					esta=true;
				}
			}
			
			if (!esta)
			{
				forma.setViasDescuentosAnterior(copiarAtributosMapa(tmp, forma.getViasDescuentosAnterior(), indicesDescuentos, index,j));
				index++;
			}
		}
	}





	private ActionForward adiccionarProgramas(DescuentosComercialesForm forma,
			ActionMapping mapping) {
		
		DtoProgDescComercialConvenioContrato dto= new  DtoProgDescComercialConvenioContrato();
		dto.setFechaVigencia(UtilidadFecha.getFechaActual());
		forma.getListProgDescComercial().add(dto);
		return mapping.findForward("detalle");	
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardar(Connection con, DescuentosComercialesForm forma, DescuentosComerciales mundo, UsuarioBasico usuario)
	{
		
		
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		
		if(transaccion)
			transaccion=this.accionGuardarAgrupacionArticulos(con,forma,mundo,usuario,transaccion);
		if(transaccion)
			transaccion=this.accionGuardarArticulos(con,forma,mundo,usuario,transaccion);
		
		/**
		 * VALIDAR SI UTILIZA PROGRAMAS O SERVICIOS 
		 */
		if(forma.getUtilizaProgramas().equals(ConstantesBD.acronimoSi))
		{
			if(transaccion)
			{
				transaccion=this.accionGuardarProgramas(con, forma, usuario);
			}
		}
		else
		{
			if(transaccion)
				transaccion=this.accionGuardarServicios(con,forma,mundo,usuario,transaccion);
		}
		
		if(transaccion)
			transaccion=this.accionGuardarAgrupacionServicios(con,forma,mundo,usuario,transaccion);
		
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
	private boolean accionGuardarServicios(Connection con, DescuentosComercialesForm forma, DescuentosComerciales mundo, UsuarioBasico usuario, boolean transaccion)
	{
		for(int i=0;i<Integer.parseInt(forma.getServiciosEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarServicios(con,forma.getServiciosEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getServiciosEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logDescuentosComercialesCodigo,indicesServicios);
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
				vo.put("porcentaje", forma.getServicios("porcentaje_"+i));
				vo.put("valor", forma.getServicios("valor_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getServicios("fechavigencia_"+i)+""));
				
				transaccion=mundo.modificarServicios(con, vo);
			}
			//insertar
			else if((forma.getServicios("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigoDescuento", forma.getViasDescuentos("codigo_"+forma.getIndexSeleccionado())+"");
				vo.put("contrato", forma.getContrato());
				vo.put("servicio", forma.getServicios("codigoServicio_"+i));
				vo.put("porcentaje", forma.getServicios("porcentaje_"+i));
				vo.put("valor", forma.getServicios("valor_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getServicios("fechavigencia_"+i)+""));
				transaccion=mundo.insertarServicios(con, vo);
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
	private boolean accionGuardarAgrupacionServicios(Connection con, DescuentosComercialesForm forma, DescuentosComerciales mundo, UsuarioBasico usuario, boolean transaccion)
	{
//		eliminar
		for(int i=0;i<Integer.parseInt(forma.getAgrupacionServiciosEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarAgrupacionServicios(con,forma.getAgrupacionServiciosEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getAgrupacionServiciosEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logDescuentosComercialesCodigo,indicesAgrupacionServicio);
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
				vo.put("porcentaje", forma.getAgrupacionServicios("porcentaje_"+i));
				vo.put("valor", forma.getAgrupacionServicios("valor_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getAgrupacionServicios("fechavigencia_"+i)+""));

				transaccion=mundo.modificarAgrupacionServicios(con, vo);
			}
			//insertar
			else if((forma.getAgrupacionServicios("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigoDescuento", forma.getViasDescuentos("codigo_"+forma.getIndexSeleccionado())+"");
				vo.put("contrato", forma.getContrato());
				vo.put("tipopos", forma.getAgrupacionServicios("tipopos_"+i));
				vo.put("gruposervicio", forma.getAgrupacionServicios("gruposervicio_"+i));
				vo.put("tiposervicio", forma.getAgrupacionServicios("tiposervicio_"+i));
				vo.put("especialidad", forma.getAgrupacionServicios("especialidad_"+i));
				vo.put("porcentaje", forma.getAgrupacionServicios("porcentaje_"+i));
				vo.put("valor", forma.getAgrupacionServicios("valor_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getAgrupacionServicios("fechavigencia_"+i)+""));

				transaccion=mundo.insertarAgrupacionServicios(con, vo);
			}
		}
		return transaccion;
	}


	
	/**
	 * GUARDA TODOS LOS PROGRAMAS 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private boolean accionGuardarProgramas(Connection con, DescuentosComercialesForm forma, UsuarioBasico usuario  ){
		
		String tmpCodigoDescuentoConvCont=forma.getViasDescuentos("codigo_"+forma.getIndexSeleccionado())+"";
		
		/**
		 * FALTA APLICAR LA TRANSACCION
		 */
		for(DtoProgDescComercialConvenioContrato newDto: forma.getListProgDescComercial())
		{
			newDto.setUsuarioModifica( new DtoInfoFechaUsuario(usuario.getCodigoInstitucion()) );
			
				
				/**
				 *		ELIMINAR DE LA BASE DE DATOS 
				 */
				
				if(newDto.getBorrar().equals(ConstantesBD.acronimoSi))
				{
					if(!DescuentosComerciales.eliminarProgDescComercialContrato(con, newDto))
					{
						return false;
					}
				}
			
				/**
				 * MODIFCIAR DE LAS LISTA
				 */
				else
				{
					if(newDto.getVieneBaseDatos().equals(ConstantesBD.acronimoSi))
					{
						if(!DescuentosComerciales.modificarProgDescComercialConvenioContrato (con, newDto))
						{
							return false;
						}
					}
					else
					{
						newDto.setCodigoDescuento(new BigDecimal(Utilidades.convertirAEntero(tmpCodigoDescuentoConvCont)));
						if(! DescuentosComerciales.insertProgDescComercialConvenioContrato(con, newDto) )
						{
							return false;
						}
						
					}
				}
		}
		return true;
	} 
	
	
	/**
	 * 	ELIMINA PROGRAMAS DE LA LISTA 
	 * @param forma
	 */
	private ActionForward eliminarListaProgramas(DescuentosComercialesForm forma, ActionMapping mapping){
		
		
		if( forma.getListProgDescComercial().get(forma.getPostArrayProgramas()).getVieneBaseDatos().equals(ConstantesBD.acronimoSi) )
		 {
			forma.getListProgDescComercial().get(forma.getPostArrayProgramas()).setBorrar(ConstantesBD.acronimoSi); 
		 }
		 else
		 {
			 forma.getListProgDescComercial().remove(forma.getPostArrayProgramas());
		 }
		return mapping.findForward("detalle");		
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
	private boolean accionGuardarArticulos(Connection con, DescuentosComercialesForm forma, DescuentosComerciales mundo, UsuarioBasico usuario, boolean transaccion)
	{
//		eliminar
		for(int i=0;i<Integer.parseInt(forma.getArticulosEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarArticulos(con,forma.getArticulosEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getArticulosEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logDescuentosComercialesCodigo,indicesArticulos);
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
				vo.put("porcentaje", forma.getArticulos("porcentaje_"+i));
				vo.put("valor", forma.getArticulos("valor_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getArticulos("fechavigencia_"+i)+""));

				transaccion=mundo.modificarArticulos(con, vo);
			}
			//insertar
			else if((forma.getArticulos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigoDescuento", forma.getViasDescuentos("codigo_"+forma.getIndexSeleccionado())+"");
				vo.put("articulo", forma.getArticulos("codigoArticulo_"+i));
				vo.put("porcentaje", forma.getArticulos("porcentaje_"+i));
				vo.put("valor", forma.getArticulos("valor_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getArticulos("fechavigencia_"+i)+""));

				transaccion=mundo.insertarArticulos(con, vo);
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
	private boolean accionGuardarAgrupacionArticulos(Connection con, DescuentosComercialesForm forma, DescuentosComerciales mundo, UsuarioBasico usuario, boolean transaccion)
	{
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getAgrupacionArticulosEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarAgrupacionArticulos(con,forma.getAgrupacionArticulosEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getAgrupacionArticulosEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logDescuentosComercialesCodigo,indicesAgrupacionArticulos);
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
				vo.put("porcentaje", forma.getAgrupacionArticulos("porcentaje_"+i));
				vo.put("valor", forma.getAgrupacionArticulos("valor_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getAgrupacionArticulos("fechavigencia_"+i)+""));

				transaccion=mundo.modificarAgrupacionArticulos(con, vo);
			}
			//insertar
			else if((forma.getAgrupacionArticulos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigoDescuento", forma.getViasDescuentos("codigo_"+forma.getIndexSeleccionado())+"");
				vo.put("contrato", forma.getContrato());
				vo.put("clase", forma.getAgrupacionArticulos("clase_"+i));
				vo.put("grupo", forma.getAgrupacionArticulos("grupo_"+i));
				vo.put("subgrupo", forma.getAgrupacionArticulos("subgrupo_"+i));
				vo.put("naturaleza", forma.getAgrupacionArticulos("naturaleza_"+i));
				vo.put("subgrupo", forma.getAgrupacionArticulos("subgrupo_"+i));
				vo.put("naturaleza", forma.getAgrupacionArticulos("naturaleza_"+i));
				vo.put("institucion",usuario.getCodigoInstitucion());
				vo.put("porcentaje", forma.getAgrupacionArticulos("porcentaje_"+i));
				vo.put("valor", forma.getAgrupacionArticulos("valor_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getAgrupacionArticulos("fechavigencia_"+i)+""));
				transaccion=mundo.insertarAgrupacionArticulos(con, vo);
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
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetalleDescuento(Connection con, DescuentosComercialesForm forma, DescuentosComerciales mundo, UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.resetMapasEliminacion();
		forma.setAgrupacionArticulos(mundo.consultarAgrupacionArticulos(con,forma.getViasDescuentos("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setArticulos(mundo.consultarArticulos(con,forma.getViasDescuentos("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setAgrupacionServicios(mundo.consultarAgrupacionServicios(con,forma.getViasDescuentos("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setServicios(mundo.consultarServicios(con,forma.getViasDescuentos("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setFechaVigencia(""+forma.getViasDescuentos("fechavigencia_"+forma.getIndexSeleccionado()));
		forma.setNombreViaIngreso(""+forma.getViasDescuentos("nomviaingreso_"+forma.getIndexSeleccionado()));
		forma.setNomTipoPaciente(""+forma.getViasDescuentos("nomtipopaciente_"+forma.getIndexSeleccionado()));
		UtilidadBD.closeConnection(con);
		
		
		logger.info("************************************************************************************************************************");
		logger.info("imprimir codigo mapa  "+ forma.getViasDescuentos("codigo_"+forma.getIndexSeleccionado())+"");
		
		/**
		 * MODIFICACION ANEXO 945 CAMBIOS FUNCIONALIDADES POR DESCUENTO ODONTOLOGICO
		 */
		DtoProgDescComercialConvenioContrato dto  = new DtoProgDescComercialConvenioContrato();
		
		if(! UtilidadTexto.isEmpty( String.valueOf(forma.getViasDescuentos("codigo_"+forma.getIndexSeleccionado())  )) )
		{
			dto.setCodigoDescuento(new BigDecimal( Integer.parseInt(String.valueOf(forma.getViasDescuentos("codigo_"+forma.getIndexSeleccionado()))  ))); //Pregunta a wilson
		
		}
	
		logger.info("CODIGO DESCUENTO DTO-->"+dto.getCodigoDescuento());
		
		forma.setListProgDescComercial(DescuentosComerciales.cargarProgDescComercialContrato(dto));
		
		//CARGAMOS LOS CODIGOS REPETIDOS
		
		int tmpTamanoLis= forma.getListProgDescComercial().size();
		String tmpLisCodigoProgramas="";
		for(int i=0; i<tmpTamanoLis; i++)
		{
			if(i>0)
			{
				tmpLisCodigoProgramas+=",";
			}
			tmpLisCodigoProgramas+=forma.getListProgDescComercial().get(i).getDtoPrograma().getCodigo()+"";
		}
		forma.setListaCodigosProgramas(tmpLisCodigoProgramas);
		
		return mapping.findForward("detalle");
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
	private ActionForward accionDetalleDescuentoAnterior(Connection con, DescuentosComercialesForm forma, DescuentosComerciales mundo, UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.resetMapasEliminacion();
		forma.setAgrupacionArticulos(mundo.consultarAgrupacionArticulos(con,forma.getViasDescuentosAnterior().get("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setArticulos(mundo.consultarArticulos(con,forma.getViasDescuentosAnterior().get("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setAgrupacionServicios(mundo.consultarAgrupacionServicios(con,forma.getViasDescuentosAnterior().get("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setServicios(mundo.consultarServicios(con,forma.getViasDescuentosAnterior().get("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setFechaVigencia(""+forma.getViasDescuentosAnterior().get("fechavigencia_"+forma.getIndexSeleccionado()));
		forma.setNombreViaIngreso(""+forma.getViasDescuentosAnterior().get("nomviaingreso_"+forma.getIndexSeleccionado()));
		forma.setNomTipoPaciente(""+forma.getViasDescuentosAnterior().get("nomtipopaciente_"+forma.getIndexSeleccionado()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalle");
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
	private ActionForward accionGuardarViaDescuento(Connection con, DescuentosComercialesForm forma, DescuentosComerciales mundo, UsuarioBasico usuario, ActionMapping mapping)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getViasDescuentosEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarViaDescuento(con,forma.getViasDescuentosEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getViasDescuentosEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logDescuentosComercialesCodigo,indicesDescuentos);
				transaccion=true;
			}
		}
		for(int i=0;i<Integer.parseInt(forma.getViasDescuentos("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getViasDescuentos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getViasDescuentos(),mundo.consultarViaDescuentoLLave(con, forma.getViasDescuentos("codigo_"+i)+""),i,usuario,indicesDescuentos))
			{
				HashMap vo=new HashMap();
				vo.put("codigo", forma.getViasDescuentos("codigo_"+i)+"");
				vo.put("viaIngreso", forma.getViasDescuentos("viaingreso_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("tipopaciente", forma.getViasDescuentos("tipopaciente_"+i));
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getViasDescuentos("fechavigencia_"+i)+""));
				transaccion=mundo.modificarViaDescuento(con, vo);
			}
			//insertar
			else if((forma.getViasDescuentos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigoContrato", forma.getContrato());
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("viaIngreso", forma.getViasDescuentos("viaingreso_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("tipopaciente",forma.getViasDescuentos("tipopaciente_"+i));
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getViasDescuentos("fechavigencia_"+i)+""));
				transaccion=mundo.insertarViaDescuento(con, vo);
			}
		}
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
	 * @param mapa
	 * @param mapaTemp
	 * @param pos
	 * @param usuario
	 * @param indices 
	 * @return
	 */
	private boolean existeModificacion(Connection con, HashMap mapa, HashMap mapaTemp, int pos, UsuarioBasico usuario, String[] indices)
	{
		for(int i=0;i<indices.length;i++)
		{
			if(mapaTemp.containsKey(indices[i]+"0")&&mapa.containsKey(indices[i]+""+pos))
			{
				if(!((mapaTemp.get(indices[i]+"0")+"").trim().equals((mapa.get(indices[i]+""+pos)+"").trim())))
				{
					Utilidades.generarLogGenerico(mapa, mapaTemp, usuario.getLoginUsuario(), false, pos,ConstantesBD.logDescuentosComercialesCodigo,indices);
					return true;
				}
			}
		}
		return false;
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
	private ActionForward accionCargarInfo(Connection con, DescuentosComercialesForm forma, DescuentosComerciales mundo, UsuarioBasico usuario, ActionMapping mapping)
	{
		//incializar la informacion de los select
		forma.setSelViasIngreso((Vector<InfoDatosString>)Utilidades.obtenerViasIngreso(con,true));
		forma.setViasDescuentos(new HashMap());
		forma.getViasDescuentos().put("numRegistros", "0");

		forma.setViasDescuentosAnterior(new HashMap());
		forma.getViasDescuentosAnterior().put("numRegistros", "0");

		HashMap tmp= mundo.obtenerListadoViasDescuento(con,usuario.getCodigoInstitucionInt(),forma.getContrato(),true,true);
		
		///////////////////////////////////////////////////
			organizarVigentes(con,tmp, forma);
		///////////////////////////////////////////////////
			
		for(int i=0;i<Integer.parseInt(forma.getViasDescuentos("numRegistros").toString());i++)
			forma.setViasDescuentos("tiposPaciente_"+i, UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con, forma.getViasDescuentos("viaingreso_"+i).toString()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

 



	private void organizarVigentes(Connection con, HashMap mapa, DescuentosComercialesForm forma) 
	{
		logger.info("\n ente a organizarVigentes "+mapa);
		
		HashMap tmp = new HashMap ();
		tmp.put("numRegistros", 0);
		forma.setViasDescuentosAnterior(tmp);
		int numReg=Utilidades.convertirAEntero(mapa.get("numRegistros")+"");
		String mayor="";
		int index=0,indexAnt=ConstantesBD.codigoNuncaValido,index2=0;
		///////////////////////////////////////////
		//se inicializan los datos
		forma.setViasDescuentos("numRegistros", 0);
			///////////////////////////////////////////
		boolean igual=false, acabo = false;
		String viaIngresoAnt="",tipoPacienteAnt="";
		String fechaActual=UtilidadFecha.getFechaActual(con);
		if (numReg==1)
		{
			logger.info("\n -- 1-- ");
			forma.setViasDescuentos(copiarAtributosMapa(mapa, forma.getViasDescuentos(), indicesDescuentos, index,0));
			index++;
			igual=true;
			forma.setViasDescuentos("vigente_"+(numReg-1), ConstantesBD.acronimoSi);
			forma.setViasDescuentos("numRegistros", index+"");
			
		}
		else
		{
			for (int i=0;i<numReg;i++)
			{	
				
				if (UtilidadFecha.esFechaMenorQueOtraReferencia(fechaActual, mapa.get("fechavigencia_"+i)+""))
				{
					forma.setViasDescuentos(copiarAtributosMapa(mapa, forma.getViasDescuentos(), indicesDescuentos, index,i));
					index++;
					forma.setViasDescuentos("vigente_"+i, ConstantesBD.acronimoSi);
					forma.setViasDescuentos("numRegistros", index+"");
				}
				else
				{
					boolean encontreResultado=false;
					HashMap mapaDesTemp=forma.getViasDescuentos();
					for(int j=0;j<Utilidades.convertirAEntero(mapaDesTemp.get("numRegistros")+"");j++)
					{
						if ((mapa.get("viaingreso_"+i)+"").equals(mapaDesTemp.get("viaingreso_"+j)+"") && (mapa.get("tipopaciente_"+i)+"").equals(mapaDesTemp.get("tipopaciente_"+j)+""))
						{
							encontreResultado=true;
							if (UtilidadFecha.esFechaMenorQueOtraReferencia(mapaDesTemp.get("fechavigencia_"+j)+"", mapa.get("fechavigencia_"+i)+""))
							{
								
								forma.setViasDescuentos(copiarAtributosMapa(mapa, forma.getViasDescuentos(), indicesDescuentos, j,i));
								forma.setViasDescuentos("vigente_"+i, ConstantesBD.acronimoSi);
								forma.setViasDescuentos("numRegistros", index+"");
							}
						}
					}
					if(!encontreResultado)
					{
						forma.setViasDescuentos(copiarAtributosMapa(mapa, forma.getViasDescuentos(), indicesDescuentos, index,i));
						index++;
						forma.setViasDescuentos("vigente_"+i, ConstantesBD.acronimoSi);
						forma.setViasDescuentos("numRegistros", index+"");
					}
				}
			}	
		}
	}
	
	public HashMap copiarAtributosMapa (HashMap mapaDatos, HashMap mapaResult, String [] indices,int index, int indexDatos)
	{
		try
		{
			for(int i=0;i<indices.length;i++)
			{		
				mapaResult.put(indices[i]+index, mapaDatos.get(indices[i]+indexDatos));
			
			}
			mapaResult.put("numRegistros", Integer.parseInt(mapaResult.get("numRegistros")+"")+1);
		}
		catch(Exception e)
		{
			logger.error("error:",e);
		}
		return mapaResult;
	}

}
