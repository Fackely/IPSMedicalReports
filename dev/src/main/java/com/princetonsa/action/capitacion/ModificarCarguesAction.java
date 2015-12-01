package com.princetonsa.action.capitacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

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
import util.Errores;
import util.InfoDatosInt;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.capitacion.ModificarCarguesForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.capitacion.ContratoCargue;
import com.princetonsa.mundo.capitacion.ContratosCargue;
import com.princetonsa.mundo.capitacion.SubirPaciente;

public class ModificarCarguesAction extends Action
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(ModificarCarguesAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception
	{
        UsuarioBasico usuario;
        Connection con=null;
        try{
        ModificarCarguesForm modificarCarguesForm;
		if (form instanceof ModificarCarguesForm)
		{
			modificarCarguesForm = (ModificarCarguesForm)form;
			String estado=modificarCarguesForm.getEstado();
			logger.warn("Estado ModificarCarguesForm [" + estado + "]");

			try
			{
				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo abrir la conexión"+e.toString());
				modificarCarguesForm.cleanCompleto();
                
                logger.warn("Problemas con la base de datos "+e);
                request.setAttribute("codigoDescripcionError", "errors.problemasBd");
                return mapping.findForward("paginaError");
			}
			
			usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
            if( usuario == null )
            {
                if( logger.isDebugEnabled() )
                {
                    logger.debug("Usuario no válido (null)");
                }
                
                UtilidadBD.cerrarConexion(con);                
                modificarCarguesForm.cleanCompleto();                   
                
                request.setAttribute("codigoDescripcionError", "errors.usuario.noCargado");
                return mapping.findForward("paginaError");              
            }
            else if( estado == null )
            {
                if( logger.isDebugEnabled() )
                {
                    logger.debug("estado no valido dentro del flujo de valoración (null) ");
                }
                
                UtilidadBD.cerrarConexion(con);                
                modificarCarguesForm.cleanCompleto();

                request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
                return mapping.findForward("descripcionError");             
            }
            else if(estado.equals("empezar"))
			{
            	return this.accionEmpezar(modificarCarguesForm, mapping, con, usuario, request);
			}
            else if(estado.equals("buscar"))
			{
            	return this.accionBuscar(modificarCarguesForm, mapping, con, usuario, request);
			}
            else if(estado.equals("nuevo"))
			{
            	return this.accionNuevo(modificarCarguesForm, mapping, con, usuario, request);
			}            
            else if(estado.equals("guardar"))
			{
            	return this.accionGuardar(modificarCarguesForm, mapping, con, usuario, request);
			}
            else if(estado.equals("consultarUsuariosCargados"))
			{
            	modificarCarguesForm.resetMensaje();
            	return this.accionConsultarUsuariosCargados(modificarCarguesForm, mapping, con, request);
			}
            else if(estado.equals("cargarBusquedaUsuariosCargados"))
			{
            	return this.accionCargarBusquedaUsuariosCargados(modificarCarguesForm, mapping, con);
			}
            else if(estado.equals("buscarUsuariosCargados"))
			{
            	return this.accionBuscarUsuariosCargados(modificarCarguesForm, mapping, con, request);
			}
            else if(estado.equals("guardarCarguesGrupoEtareo"))
            {
        		UtilidadBD.cerrarConexion(con);
        		return mapping.findForward("principal");
            }
            else if(estado.equals("eliminar"))
            {
            	this.accionEliminarContratoCargue(con,modificarCarguesForm);
            	UtilidadBD.cerrarConexion(con);
    			return mapping.findForward("principal");	
            }
            else if(estado.equals("guardarUsuarios"))
            {
            	this.accionGuardarUsuarios(con, modificarCarguesForm, usuario);
            	UtilidadBD.cerrarConexion(con);
        		return mapping.findForward("usuariosCargados");
            }
    		else
			{
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
		}
	    else
	    {
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
	 * @param usuario 
	 * @param modificarCarguesForm
	 * @throws Errores 
	 */
	private void accionGuardarUsuarios(Connection con, ModificarCarguesForm forma, UsuarioBasico usuario) throws Errores 
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		for(int i=0;i<Utilidades.convertirAEntero(forma.getMapaUsuariosCargados().get("numRegistros")+"");i++)
		{
			if((forma.getMapaUsuariosCargados().get("capitado_"+i)+"").equals(ConstantesBD.acronimoSi)&&!forma.getMapaUsuariosCargados().get("eliminado_"+i).equals(ConstantesBD.acronimoSi))
			{	
				//forma.setMapaLogEliminados(ContratosCargue.consultarDatosEliminado(con, forma.getMapaUsuariosCargados().get("consecutivo_"+i)+""));
				transaccion=ContratosCargue.inactivarUsuarios(con, forma.getMapaUsuariosCargados().get("consecutivo_"+i)+"", forma.getMapaUsuariosCargados().get("activo_"+i)+"");
				/*if(transaccion)
				{
					HashMap vo = new HashMap();
					vo.put("convenio", forma.getMapaLogEliminados().get("convenio"));
					vo.put("contrato", forma.getMapaLogEliminados().get("contrato"));
					vo.put("fechainicial", forma.getMapaLogEliminados().get("fechainicial"));
					vo.put("fechafinal", forma.getMapaLogEliminados().get("fechafinal"));
					vo.put("tipoid", forma.getMapaLogEliminados().get("tipoidentificacion"));
					vo.put("numeroid", forma.getMapaLogEliminados().get("numeroidentificacion"));
					vo.put("primernombre", forma.getMapaLogEliminados().get("primernombre"));
					vo.put("segundonombre", forma.getMapaLogEliminados().get("segundonombre"));
					vo.put("primerapellido", forma.getMapaLogEliminados().get("primerapellido"));
					vo.put("segundoapellido", forma.getMapaLogEliminados().get("segundoapellido"));
					vo.put("usuarioproceso", usuario.getLoginUsuario());
					vo.put("fechaproceso", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
					vo.put("horaproceso", UtilidadFecha.getHoraActual());
					
					transaccion=ContratosCargue.insertarLogEliminacion(con, vo);
				}*/
			}
			else if((forma.getMapaUsuariosCargados().get("capitado_"+i)+"").equals(ConstantesBD.acronimoNo)&&!(forma.getMapaUsuariosCargados().get("eliminado_"+i)+"").equals(ConstantesBD.acronimoSi))
			{
				//forma.setMapaLogActivos(ContratosCargue.consultarDaotosInactivar(con, forma.getMapaUsuariosCargados().get("consecutivo_"+i)+""));
				transaccion=ContratosCargue.inactivarUsuariosActivos(con, forma.getMapaUsuariosCargados().get("consecutivo_"+i)+"", forma.getMapaUsuariosCargados().get("activo_"+i)+"");
				/*if(transaccion)
				{
					HashMap vo = new HashMap();
					vo.put("convenio", forma.getMapaLogActivos().get("convenio"));
					vo.put("contrato", forma.getMapaLogActivos().get("contrato"));
					vo.put("fechainicial", forma.getMapaLogActivos().get("fechainicial"));
					vo.put("fechafinal", forma.getMapaLogActivos().get("fechafinal"));
					vo.put("tipoid", forma.getMapaLogActivos().get("tipoidentificacion"));
					vo.put("numeroid", forma.getMapaLogActivos().get("numeroidentificacion"));
					vo.put("primernombre", forma.getMapaLogActivos().get("primernombre"));
					vo.put("segundonombre", forma.getMapaLogActivos().get("segundonombre"));
					vo.put("primerapellido", forma.getMapaLogActivos().get("primerapellido"));
					vo.put("segundoapellido", forma.getMapaLogActivos().get("segundoapellido"));
					vo.put("usuarioproceso", usuario.getLoginUsuario());
					vo.put("fechaproceso", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
					vo.put("horaproceso", UtilidadFecha.getHoraActual());
					
					transaccion=ContratosCargue.insertarLogEliminacion(con, vo);
					
				}*/
			}
			else if((forma.getMapaUsuariosCargados().get("capitado_"+i)+"").equals(ConstantesBD.acronimoSi)&&(forma.getMapaUsuariosCargados().get("eliminado_"+i)+"").equals(ConstantesBD.acronimoSi))
			{
				forma.setMapaLogEliminados(ContratosCargue.consultarDatosEliminado(con, forma.getMapaUsuariosCargados().get("consecutivo_"+i)+""));
				transaccion=ContratosCargue.eliminarUsuarios(con, forma.getMapaUsuariosCargados().get("consecutivo_"+i)+"");
				if(transaccion)
				{
					HashMap vo = new HashMap();
					vo.put("convenio", forma.getMapaLogEliminados().get("convenio"));
					vo.put("contrato", forma.getMapaLogEliminados().get("contrato"));
					vo.put("fechainicial", forma.getMapaLogEliminados().get("fechainicial"));
					vo.put("fechafinal", forma.getMapaLogEliminados().get("fechafinal"));
					vo.put("tipoid", forma.getMapaLogEliminados().get("tipoidentificacion"));
					vo.put("numeroid", forma.getMapaLogEliminados().get("numeroidentificacion"));
					vo.put("primernombre", forma.getMapaLogEliminados().get("primernombre"));
					vo.put("segundonombre", forma.getMapaLogEliminados().get("segundonombre"));
					vo.put("primerapellido", forma.getMapaLogEliminados().get("primerapellido"));
					vo.put("segundoapellido", forma.getMapaLogEliminados().get("segundoapellido"));
					vo.put("usuarioproceso", usuario.getLoginUsuario());
					vo.put("fechaproceso", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
					vo.put("horaproceso", UtilidadFecha.getHoraActual());
					
					transaccion=ContratosCargue.insertarLogEliminacion(con, vo);
					
				}
			}
		}
		if(transaccion)
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
			int numeroContratoCargue = forma.getNumeroContratoCargue();
			String fechaInicial = (String)forma.getContratoCargue("fechainicial_"+numeroContratoCargue);
			String fechaFinal = (String)forma.getContratoCargue("fechafinal_"+numeroContratoCargue);
			int codigoContrato =Integer.parseInt(""+forma.getContratoCargue("codigocontrato_"+numeroContratoCargue));
			 
			forma.setEstado("listandoUsuarios");
			//modificarCarguesForm.setUsuariosCargados(ContratosCargue.consultarUsuariosCargados(con, codigoContrato, fechaInicial, fechaFinal, "", "", "", "",""));
			
			forma.setMapaUsuariosCargados(ContratosCargue.consultarUsuariosCargados(con, codigoContrato, fechaInicial, fechaFinal, "", "", "", "",""));
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
	 * @param modificarCarguesForm
	 */
	private void accionEliminarContratoCargue(Connection con, ModificarCarguesForm modificarCarguesForm) 
	{
		String indices[]={"fechafinal_","fechainicial_","numerocontrato_","fechacargue_","codigo_","cuentacobro_","totalpacientes_","anulado_","tipopago_","valortotal_","upc_","anuladoenbd_","fechafinalmodificada_","codigocontrato_","numcarguesgrupoetareo_","enbd_","contrato_"};
		eliminarRegistroMapa(modificarCarguesForm.getContratosCargue(), modificarCarguesForm.getContratosCargueEliminados(), modificarCarguesForm.getRegEliminar(), indices,modificarCarguesForm.getNumContratosCargue());
		modificarCarguesForm.setNumContratosCargue(Utilidades.convertirAEntero(modificarCarguesForm.getContratoCargue("numRegistros")+""));
	}
	

	/**
	 * Metodo que elimina un objeto de una mapa, se debe enviar la referencia del mapa original y del mapa donde se almacenan los objetos eliminados.
	 * se almacenan los objetos almacenados, solo cuando son tipo base de datos.
	 * @param mapa
	 * @param mapaEliminados
	 * @param posEliminar
	 * @param indices
	 * @param valorTipoRegistrosBD ---> indice que contiene el numero de registro de los mapas 
	 * @param indiceTipoRegistro ----> indice que indica si el registro es de tipo memoria o bd
	 * @param indiceNumeroRegistros   ---> valor de atributo tipo registro cuando el registro es de la bd..
	 * @author Jorge Armando Osorio Velasquez.
	 * @param todosRegistros ----> parametro que indica si se debe mantener el historico de todos los registros, o solo los que son tipo BD.
	 */
	public void eliminarRegistroMapa(HashMap mapa, HashMap mapaEliminados, int posEliminar, String[] indices,int numReg)
	{
		int numRegMapEliminados=Integer.parseInt(mapaEliminados.get("numRegistros")+"");
		int ultimaPosMapa=numReg-1;

		//cargamos el mapa de eliminados solo para registro de la bd.
		String enbd=mapa.get("enbd_"+posEliminar)+"";
		if(UtilidadCadena.noEsVacio(enbd) && UtilidadTexto.getBoolean(enbd))//si esta en la BD
		{
			for(int i=0;i<indices.length;i++)
			{
			
				mapaEliminados.put(indices[i]+""+numRegMapEliminados, mapa.get(indices[i]+""+posEliminar));
			}
			mapaEliminados.put("numRegistros", (numRegMapEliminados+1));
		}
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=posEliminar;i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				mapa.put(indices[j]+""+i,mapa.get(indices[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			mapa.remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamo el numero de registros en el mapa.
		mapa.put("numRegistros",ultimaPosMapa);
	}

	

	/**
	 * Este método especifica las acciones a realizar en el estado empezar
	 * @param modificarCarguesForm
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar(
			ModificarCarguesForm modificarCarguesForm, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request) throws SQLException
	{
		modificarCarguesForm.cleanCompleto();
		modificarCarguesForm.setEstado("buscando");		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * 
	 * @param modificarCarguesForm
	 * @param mapping
	 * @param con
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionConsultarUsuariosCargados(ModificarCarguesForm modificarCarguesForm, ActionMapping mapping, Connection con, HttpServletRequest request) throws SQLException
	{
		try
		{
			int numeroContratoCargue = modificarCarguesForm.getNumeroContratoCargue();
			String fechaInicial = (String)modificarCarguesForm.getContratoCargue("fechainicial_"+numeroContratoCargue);
			String fechaFinal = (String)modificarCarguesForm.getContratoCargue("fechafinal_"+numeroContratoCargue);
			int codigoContrato =Integer.parseInt(""+modificarCarguesForm.getContratoCargue("codigocontrato_"+numeroContratoCargue));
			 
			modificarCarguesForm.setEstado("listandoUsuarios");
			//modificarCarguesForm.setUsuariosCargados(ContratosCargue.consultarUsuariosCargados(con, codigoContrato, fechaInicial, fechaFinal, "", "", "", "",""));
			
			modificarCarguesForm.setMapaUsuariosCargados(ContratosCargue.consultarUsuariosCargados(con, codigoContrato, fechaInicial, fechaFinal, "", "", "", "",""));
			
		}
		catch(Errores e)
		{
			ActionErrors errores = new ActionErrors();
			errores.add(e.getMessage(), e.getActionMessage());
			saveErrors(request, errores);
	        logger.warn(e);
		}
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("usuariosCargados");
	}
	
	/**
	 * 
	 * @param modificarCarguesForm
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionCargarBusquedaUsuariosCargados(ModificarCarguesForm modificarCarguesForm, ActionMapping mapping, Connection con) throws SQLException
	{
		modificarCarguesForm.setEstado("buscandoUsuarios");
		modificarCarguesForm.cleanBusqueda();
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("usuariosCargados");
	}
	
	/**
	 * 
	 * @param modificarCarguesForm
	 * @param mapping
	 * @param con
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionBuscarUsuariosCargados(ModificarCarguesForm modificarCarguesForm, ActionMapping mapping, Connection con, HttpServletRequest request) throws SQLException
	{
		try
		{
			int numeroContratoCargue = modificarCarguesForm.getNumeroContratoCargue();
			String fechaInicial = (String)modificarCarguesForm.getContratoCargue("fechainicial_"+numeroContratoCargue);
			String fechaFinal = (String)modificarCarguesForm.getContratoCargue("fechafinal_"+numeroContratoCargue);
			int codigoContrato =Integer.parseInt(""+modificarCarguesForm.getContratoCargue("codigocontrato_"+numeroContratoCargue));
			String tipoIdBusq = (modificarCarguesForm.isTipoIdBusquedaCheck())?modificarCarguesForm.getTipoIdBusqueda():"";
			String numeroIdBusq = (modificarCarguesForm.isNumeroIdBusquedaCheck())?modificarCarguesForm.getNumeroIdBusqueda():"";
			String nombreBusq = (modificarCarguesForm.isNombreBusquedaCheck())?modificarCarguesForm.getNombreBusqueda():"";
			String apellidoBusq = (modificarCarguesForm.getApellidoBusquedaCheck())?modificarCarguesForm.getApellidoBusqueda():"";
			String numeroFicha=(modificarCarguesForm.isNumeroFichaCheck())?modificarCarguesForm.getNumeroFicha():"";
			 
			//modificarCarguesForm.setUsuariosCargados(ContratosCargue.consultarUsuariosCargados(con, codigoContrato, fechaInicial, fechaFinal, tipoIdBusq, numeroIdBusq, nombreBusq, apellidoBusq,numeroFicha));
			modificarCarguesForm.setMapaUsuariosCargados(ContratosCargue.consultarUsuariosCargados(con, codigoContrato, fechaInicial, fechaFinal, tipoIdBusq, numeroIdBusq, nombreBusq, apellidoBusq,numeroFicha));
			modificarCarguesForm.setEstado("listandoUsuarios");
		}
		catch(Errores e)
		{
			modificarCarguesForm.setEstado("buscandoUsuarios");
			ActionErrors errores = new ActionErrors();
			errores.add(e.getMessage(), e.getActionMessage());
			saveErrors(request, errores);
	        logger.warn(e);
		}
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("usuariosCargados");
	}
	
	/**
	 * 
	 * @param modificarCarguesForm
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionBuscar(
			ModificarCarguesForm modificarCarguesForm, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request) throws SQLException
	{
		try
		{
			Collection contratos=SubirPaciente.consultarTipos(con, 2, usuario.getCodigoInstitucionInt(), modificarCarguesForm.getConvenio().getCodigo()); //  consulta los contratos pertenecientes al convenio indicado
			Iterator contratosIt = contratos.iterator();
			while(contratosIt.hasNext())
			{
				HashMap contratoDB=(HashMap)contratosIt.next();
				if(contratoDB.get("tipo_pago")==null)
				{ // si no esta definido el tipo de pago
					ActionErrors errores = new ActionErrors();
					errores.add("El contrato no tiene definido el tipo de pago", new ActionMessage("error.capitacion.contratoSinTipoPago", contratoDB.get("numero_contrato"), modificarCarguesForm.getConvenio().getNombre()));
					saveErrors(request, errores);
					UtilidadBD.cerrarConexion(con);
		            modificarCarguesForm.setEstado("buscando");
					return mapping.findForward("principal");
				}
			}
			
			modificarCarguesForm.setContratosCargue(ContratosCargue.buscarContratosCargue(
					con, modificarCarguesForm.getFechaInicial(), modificarCarguesForm.getFechaFinal(), modificarCarguesForm.getConvenio().getCodigo()));
			
			modificarCarguesForm.setCarguesGrupoEtareo(new HashMap());
			for(int i=0; i<modificarCarguesForm.getNumContratosCargue(); i++)
			{
				String tipoPago = (modificarCarguesForm.getContratoCargue("tipopago_"+i)).toString();
				int codigoContratoCargue = Integer.parseInt(modificarCarguesForm.getContratoCargue("codigo_"+i)+"");
				
				if(tipoPago.equalsIgnoreCase(ConstantesBD.codigoTipoPagoGrupoEtareo))
				{
					HashMap tempoCarguesGrupoEtareo = ContratosCargue.consultarCarguesGrupoEtareoContrato(con, codigoContratoCargue);
					
					int numCarguesGrupoEtareo = Integer.parseInt((String)tempoCarguesGrupoEtareo.get("numRegistros"));
					modificarCarguesForm.setContratoCargue("numcarguesgrupoetareo_"+i, (String)tempoCarguesGrupoEtareo.get("numRegistros"));
					
					for(int j=0; j<numCarguesGrupoEtareo; j++)
					{
						modificarCarguesForm.setCargueGrupoEtareo("cargue_"+i+"_consecutivo_"+j, tempoCarguesGrupoEtareo.get("consecutivo_"+j));
						modificarCarguesForm.setCargueGrupoEtareo("cargue_"+i+"_grupoetareo_"+j, tempoCarguesGrupoEtareo.get("grupoetareo_"+j));
						modificarCarguesForm.setCargueGrupoEtareo("cargue_"+i+"_totalusuarios_"+j, tempoCarguesGrupoEtareo.get("totalusuarios_"+j));
						modificarCarguesForm.setCargueGrupoEtareo("cargue_"+i+"_upc_"+j, tempoCarguesGrupoEtareo.get("upc_"+j));
						modificarCarguesForm.setCargueGrupoEtareo("cargue_"+i+"_enbd_"+j, ValoresPorDefecto.getValorTrueParaConsultas());
						modificarCarguesForm.setCargueGrupoEtareo("cargue_"+i+"_eliminado_"+j, ValoresPorDefecto.getValorFalseParaConsultas());
					}
				}
			}
			modificarCarguesForm.setEstado("modificando");		
		}
		catch(Errores e)
		{
			ActionErrors errores = new ActionErrors();
			errores.add(e.getMessage(), e.getActionMessage());
			saveErrors(request, errores);
            logger.warn(e);
            modificarCarguesForm.setEstado("modificando");
		}
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * 
	 * @param modificarCarguesForm
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionNuevo(
			ModificarCarguesForm modificarCarguesForm, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request) throws SQLException
	{
		int numContratosCargue = modificarCarguesForm.getNumContratosCargue();
		modificarCarguesForm.setContratoCargue("enbd_"+numContratosCargue, ValoresPorDefecto.getValorFalseParaConsultas()); // cuando se crea uno nuevo no está en la base de datos
		modificarCarguesForm.setContratoCargue("fechacargue_"+numContratosCargue, UtilidadFecha.getFechaActual()); // cuando se crea uno nuevo no está en la base de datos
		modificarCarguesForm.setContratoCargue("fechafinalmodificada_"+numContratosCargue, UtilidadFecha.getFechaActual()); // cuando se crea uno nuevo no está en la base de datos
		modificarCarguesForm.setContratoCargue("numcarguesgrupoetareo_"+numContratosCargue, "0");
		modificarCarguesForm.setNumContratosCargue(numContratosCargue+1); // se aumenta la cantidad de contratos cargue
        modificarCarguesForm.setEstado("modificando");
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	
	/**
	 * 
	 * @param modificarCarguesForm
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionGuardar(
			ModificarCarguesForm modificarCarguesForm, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request) throws SQLException
	{
		try
		{

			ContratosCargue contratosCargue = new ContratosCargue();
			ContratoCargue contratoCargue;
			modificarCarguesForm.getContratosCargueEliminados().put("usuario",usuario.getLoginUsuario());
			modificarCarguesForm.getContratosCargueEliminados().put("convenio", modificarCarguesForm.getConvenio().getCodigo());

			if(Utilidades.convertirAEntero(modificarCarguesForm.getContratosCargueEliminados().get("numRegistros")+"")>0)
			{
				contratosCargue.eliminarContratos(con,modificarCarguesForm.getContratosCargueEliminados());
			}
			
			//inicializamos el mapa despues de elimnar de la bd
			modificarCarguesForm.setContratosCargueEliminados(new HashMap());
			modificarCarguesForm.getContratosCargueEliminados().put("numRegistros", "0");
			
			for(int i=0; i<modificarCarguesForm.getNumContratosCargue(); i++)
			{
				contratoCargue = new ContratoCargue();
				
				String enbd = (String)modificarCarguesForm.getContratoCargue("enbd_"+i);

				contratoCargue.setAnulado(UtilidadTexto.getBoolean((String)modificarCarguesForm.getContratoCargue("anulado_"+i)));
				contratoCargue.setTotalPacientes(Integer.parseInt(modificarCarguesForm.getContratoCargue("totalpacientes_"+i)+""));
				contratoCargue.setUpc(Double.parseDouble(modificarCarguesForm.getContratoCargue("upc_"+i)+""));
				contratoCargue.setFechaFinalModificada(modificarCarguesForm.getContratoCargue("fechafinalmodificada_"+i)+"");
				contratoCargue.setCodigoTipoPago(modificarCarguesForm.getContratoCargue("tipopago_"+i)+"");
				if(contratoCargue.getCodigoTipoPago().equalsIgnoreCase(ConstantesBD.codigoTipoPagoGrupoEtareo))
				{
					contratoCargue.setNumCarguesGrupoEtareo(Integer.parseInt(modificarCarguesForm.getContratoCargue("numcarguesgrupoetareo_"+i)+""));
					for(int j=0; j<contratoCargue.getNumCarguesGrupoEtareo(); j++)
					{
						contratoCargue.setCargueGrupoEtareo("consecutivo_"+j, modificarCarguesForm.getCargueGrupoEtareo("cargue_"+i+"_consecutivo_"+j));
						contratoCargue.setCargueGrupoEtareo("grupoetareo_"+j, modificarCarguesForm.getCargueGrupoEtareo("cargue_"+i+"_grupoetareo_"+j));
						contratoCargue.setCargueGrupoEtareo("totalusuarios_"+j, modificarCarguesForm.getCargueGrupoEtareo("cargue_"+i+"_totalusuarios_"+j));
						logger.info("--->"+modificarCarguesForm.getCarguesGrupoEtareo());
						contratoCargue.setCargueGrupoEtareo("upc_"+j, modificarCarguesForm.getCargueGrupoEtareo("cargue_"+i+"_upc_"+j));
						contratoCargue.setCargueGrupoEtareo("eliminado_"+j, modificarCarguesForm.getCargueGrupoEtareo("cargue_"+i+"_eliminado_"+j));
						contratoCargue.setCargueGrupoEtareo("enbd_"+j, modificarCarguesForm.getCargueGrupoEtareo("cargue_"+i+"_enbd_"+j));
					}
				}

				if(UtilidadCadena.noEsVacio(enbd) && !UtilidadTexto.getBoolean(enbd))
				{ // si es un nuevo contrato_cargue
					contratoCargue.setEnBD(false);
					contratoCargue.setFechaCargue((String)modificarCarguesForm.getContratoCargue("fechacargue_"+i));
					contratoCargue.setFechaInicial((String)modificarCarguesForm.getContratoCargue("fechainicial_"+i));
					contratoCargue.setFechaFinal((String)modificarCarguesForm.getContratoCargue("fechafinal_"+i));
					contratoCargue.setTotalPacientes(Integer.parseInt((String)modificarCarguesForm.getContratoCargue("totalpacientes_"+i)));
					contratoCargue.setUpc(Double.parseDouble((String)modificarCarguesForm.getContratoCargue("upc_"+i)));
					
					InfoDatosInt contrato = new InfoDatosInt();
					String[] tempoContrato = ((String)modificarCarguesForm.getContratoCargue("contrato_"+i)).split("-");
					contrato.setCodigo(Integer.parseInt(tempoContrato[0]));
					contrato.setNombre(tempoContrato[1]);
					contratoCargue.setContrato(contrato);

					if(!contratoCargue.isAnulado()) // solo inserta uno nuevo si no lo han anulado
					{
						contratosCargue.addContratoCargue(contratoCargue);
						String log=
							"\n          =====REGISTRO INGRESADO===== " +
							"\n*  Contrato: [" +contratoCargue.getContrato().getNombre() +"] "+
							"\n*  Fecha Inicial: [" +contratoCargue.getFechaInicial() +"] "+
							"\n*  Fecha Final: [" +contratoCargue.getFechaFinal() +"] "+
							"\n*  Total Pacientes: [" +contratoCargue.getTotalPacientes()+"] "+
							"\n*  Upc: 	["+contratoCargue.getUpc()+"] "+
							"\n========================================================";

						LogsAxioma.enviarLog(ConstantesBD.logContratoCargueCodigo, log, ConstantesBD.tipoRegistroLogInsercion, usuario.getInformacionGeneralPersonalSalud());
					}
				}
				else
				{
					InfoDatosInt contrato = new InfoDatosInt();
					contrato.setCodigo(Utilidades.convertirAEntero(modificarCarguesForm.getContratoCargue("codigocontrato_"+i)+""));
					contrato.setNombre((String)modificarCarguesForm.getContratoCargue("numerocontrato_"+i));
					contratoCargue.setEnBD(true);
					contratoCargue.setCodigo(Integer.parseInt((String)modificarCarguesForm.getContratoCargue("codigo_"+i)));
					contratoCargue.setContrato(contrato);
					contratosCargue.addContratoCargue(contratoCargue);

					String log=
						"\n          =====REGISTRO MODIFICADO===== " +
						"\n*  Código: ["+contratoCargue.getCodigo()+"]" +
						"\n*  Total Pacientes: [" +contratoCargue.getTotalPacientes()+"] "+
						"\n*  Upc: 	["+contratoCargue.getUpc()+"] "+
						"\n========================================================";

					LogsAxioma.enviarLog(ConstantesBD.logContratoCargueCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getInformacionGeneralPersonalSalud());
					
					if(contratoCargue.isAnulado())
					{
						log=
							"\n          =====REGISTRO ANULADO===== " +
							"\n*  Código: ["+contratoCargue.getCodigo()+"]" +
							"\n========================================================";

						LogsAxioma.enviarLog(ConstantesBD.logContratoCargueCodigo, log, ConstantesBD.tipoRegistroLogEliminacion, usuario.getInformacionGeneralPersonalSalud());
					}
				}
				
			}
			
			contratosCargue.guardar(con, usuario.getCodigoInstitucionInt());
			modificarCarguesForm.setContratosCargue(ContratosCargue.buscarContratosCargue(
					con, modificarCarguesForm.getFechaInicial(), modificarCarguesForm.getFechaFinal(), modificarCarguesForm.getConvenio().getCodigo()));
			modificarCarguesForm.setCarguesGrupoEtareo(new HashMap());
			for(int i=0; i<modificarCarguesForm.getNumContratosCargue(); i++)
			{
				String tipoPago = (modificarCarguesForm.getContratoCargue("tipopago_"+i)).toString();
				int codigoContratoCargue = Integer.parseInt(modificarCarguesForm.getContratoCargue("codigo_"+i)+"");
								
				if(tipoPago.equalsIgnoreCase(ConstantesBD.codigoTipoPagoGrupoEtareo))
				{
					HashMap tempoCarguesGrupoEtareo = ContratosCargue.consultarCarguesGrupoEtareoContrato(con, codigoContratoCargue);
					
					int numCarguesGrupoEtareo = Integer.parseInt((String)tempoCarguesGrupoEtareo.get("numRegistros"));
					modificarCarguesForm.setContratoCargue("numcarguesgrupoetareo_"+i, (String)tempoCarguesGrupoEtareo.get("numRegistros"));
					
					for(int j=0; j<numCarguesGrupoEtareo; j++)
					{
						modificarCarguesForm.setCargueGrupoEtareo("cargue_"+i+"_consecutivo_"+j, tempoCarguesGrupoEtareo.get("consecutivo_"+j));
						modificarCarguesForm.setCargueGrupoEtareo("cargue_"+i+"_grupoetareo_"+j, tempoCarguesGrupoEtareo.get("grupoetareo_"+j));
						modificarCarguesForm.setCargueGrupoEtareo("cargue_"+i+"_totalusuarios_"+j, tempoCarguesGrupoEtareo.get("totalusuarios_"+j));
						modificarCarguesForm.setCargueGrupoEtareo("cargue_"+i+"_upc_"+j, tempoCarguesGrupoEtareo.get("upc_"+j));
						modificarCarguesForm.setCargueGrupoEtareo("cargue_"+i+"_enbd_"+j, ValoresPorDefecto.getValorTrueParaConsultas());
						modificarCarguesForm.setCargueGrupoEtareo("cargue_"+i+"_eliminado_"+j, ValoresPorDefecto.getValorFalseParaConsultas());
					}
				}
			}
			modificarCarguesForm.setEstado("modificando");
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("principal");
		}
		catch(Errores e)
		{
			ActionErrors errores = new ActionErrors();
			errores.add(e.getMessage(), e.getActionMessage());
			saveErrors(request, errores);
            logger.warn(e);
            modificarCarguesForm.setEstado("modificando");
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("principal");
		}
	}
	
}
