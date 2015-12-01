package com.princetonsa.action.administracion;

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
import util.InfoDatosInt;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.administracion.FactorConversionMonedasForm;
import com.princetonsa.dto.administracion.DtoFactorConversionMonedas;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.administracion.FactorConversionMonedas;

/**
 * 
 * @author wilson
 *
 */
public class FactorConversionMonedasAction extends Action  
{
	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(FactorConversionMonedasAction.class);
    
    /**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
    {
    	Connection con=null;
    	try{
    	if (response==null); 
    	if(form instanceof FactorConversionMonedasForm)
    	{
    		
    		con = UtilidadBD.abrirConexion();
    		if(con == null)
			{
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}
    		
    		UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico"); 
    		FactorConversionMonedasForm forma =(FactorConversionMonedasForm)form;
    		String estado=forma.getEstado();
    		logger.warn("\n\n\nEl estado en FactorConversionMonedas es------->"+estado+"\n");

    		if(estado == null)
    		{
    			forma.reset(); 
    			logger.warn("Estado no valido dentro del flujo de Factor Conversion Monedas (null) ");
    			request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.cerrarConexion(con);
    			return mapping.findForward("paginaError");
    		}
    		/*----------------------------------------------
			 * ESTADO =================>>>>>  EMPEZAR
			 ---------------------------------------------*/
    		else if(estado.equals("empezar"))
    		{
    			return this.accionEmpezar(con,forma, mapping, con, usuario);
    		}
    		/*----------------------------------------------
			 * ESTADO =================>>>>>  NUEVO
			 ---------------------------------------------*/
				else if (estado.equals("nuevo"))
				{
				
					return this.accionNuevo(con, forma, request, response, usuario);
				}
    		/*----------------------------------------------
			 * ESTADO =================>>>>>  ELIMINARCAMPO
			 ---------------------------------------------*/
				else if (estado.equals("eliminarCampo"))
				{
					return this.accionEliminarCampo(forma, request, response, mapping, usuario.getCodigoInstitucionInt());
				}
    		/*----------------------------------------------
			 * ESTADO =================>>>>>  ORDENAR
			 ---------------------------------------------*/
			else if (estado.equals("ordenar"))//estado encargado de ordenar el HashMap del censo.
				{
					forma.setFactorMap(this.accionOrdenarMapa(forma.getFactorMap(),forma));				 
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");	
			
				}
				else /*----------------------------------------------
				 * ESTADO =================>>>>>  REDIRECCION
				 ---------------------------------------------*/
				if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
    		/*----------------------------------------------
			 * ESTADO =================>>>>>  GUARDAR
			 ---------------------------------------------*/
				else if (estado.equals("guardar"))
				{
					return this.accionGuardar(con, forma, mapping, usuario);
					
				}
    		
    		
    		else
			{
    			forma.reset(); 
    			logger.warn("Estado no valido dentro del flujo de Factor Conversion Monedas -> "+estado);
    			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				UtilidadBD.cerrarConexion(con);
    			return mapping.findForward("paginaError");
			}
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
     * @param mapping
     * @param con
     * @param usuario
     * @return
     */
	private ActionForward accionEmpezar(Connection connection,FactorConversionMonedasForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.reset();
		
		forma.init(connection, usuario.getCodigoInstitucionInt());
		InfoDatosInt infoDatosInt = new InfoDatosInt (-1);
		DtoFactorConversionMonedas factorConversionMonedas = new DtoFactorConversionMonedas (-1,infoDatosInt,"","","",usuario.getCodigoInstitucionInt(),"");
		forma.setFactorMap(FactorConversionMonedas.cargar(con, factorConversionMonedas));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * metodo encargado de agregar campos a la forma.
	 * @param forma
	 */
	public ActionForward accionNuevo(Connection connection, FactorConversionMonedasForm forma, HttpServletRequest request, HttpServletResponse response, UsuarioBasico usuario)
	{
		UtilidadBD.closeConnection(connection);
		
		int pos = Integer.parseInt(forma.getFactorMap("numRegistros").toString());		
			
		forma.setFactorMap("codigotipomoneda_"+pos, "");
		forma.setFactorMap("fechainicial_"+pos, UtilidadFecha.getFechaActual());
		forma.setFactorMap("fechafinal_"+pos, "");
		forma.setFactorMap("factor_"+pos, "");
		forma.setFactorMap("institucion_"+pos, usuario.getCodigoInstitucionInt());
		forma.setFactorMap("usuario_"+pos, usuario.getLoginUsuario());
		forma.setFactorMap("estabd_"+pos,ConstantesBD.acronimoNo);
		
		forma.setFactorMap("numRegistros",(pos+1)+"");
		
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()), Integer.parseInt(forma.getFactorMap("numRegistros").toString()), response, request, "factorConversionMonedas.jsp", true);
	}
	
	
	/**
	 * Elimina un Registro del HashMap, 
	 * Adicciona el Registro Eliminado en
	 * el HashMap Eliminados
	 * @param forma
	 * @param request
	 * @param response
	 * @param mapping
	 * @return ActionForward
	 */
	private ActionForward accionEliminarCampo(FactorConversionMonedasForm forma, HttpServletRequest request, HttpServletResponse response, ActionMapping mapping, int codigoInstitucion) 
	{
		logger.info("hasmap en accionEliminarCampo "+forma.getFactorMap());
		//se almacena el tamaño del HashMap eliminados
		int numRegMapEliminados=Integer.parseInt(forma.getFactorEliminadosMap("numRegistros")+"");
		
		//se almacena la ultima posicion del HashMap
		int ultimaPosMapa=(Integer.parseInt(forma.getFactorMap("numRegistros")+"")-1);
		
		//poner la informacion en el otro mapa.
		String[] indices= (String[])forma.getFactorMap("INDICES_MAPA");		
		
		for(int i=0;i<indices.length;i++)
		{ 
			//solo pasar al mapa los registros que son de BD
			if((forma.getFactorMap("estabd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
			{
				forma.setFactorEliminadosMap(indices[i]+""+numRegMapEliminados, forma.getFactorMap(indices[i]+""+forma.getIndexEliminado()));
			}
		}		
		
		if((forma.getFactorMap("estabd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
		{			
			forma.setFactorEliminadosMap("numRegistros", (numRegMapEliminados+1));
		}
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=Integer.parseInt(forma.getIndexEliminado().toString());i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				forma.setFactorMap(indices[j]+""+i,forma.getFactorMap(indices[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			forma.getFactorMap().remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamos el numero de registros en el mapa.
		forma.setFactorMap("numRegistros",ultimaPosMapa);		
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(codigoInstitucion),Integer.parseInt(forma.getFactorMap("numRegistros").toString()), response, request, "factorConversionMonedas.jsp",Integer.parseInt(forma.getIndexEliminado().toString())==ultimaPosMapa);
	}
	
	
	/**
	 * Metodo Que ordena el mapa.
	 * @param mapaOrdenar
	 * @param forma
	 * @return
	 */
	public HashMap accionOrdenarMapa (HashMap mapaOrdenar, FactorConversionMonedasForm forma)
	{
		HashMap tmp = new HashMap ();
		tmp=mapaOrdenar;
		
		
		String [] indices = ((String[])mapaOrdenar.get("INDICES_MAPA"));
		int numReg = Integer.parseInt(mapaOrdenar.get("numRegistros")+"");
		mapaOrdenar = (Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),mapaOrdenar,numReg));	
		forma.setUltimoPatron(forma.getPatronOrdenar());
		mapaOrdenar.put("numRegistros",numReg+"");
		mapaOrdenar.put("INDICES_MAPA",indices);	
		//logger.info("sale de ordenar ");
	
		return mapaOrdenar;
	}
	
	/**
	 * Guarda, Modifica o Elimina un Registro en Cobertura
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return ActionForward
	 * */
	private ActionForward accionGuardar(Connection connection, FactorConversionMonedasForm forma, ActionMapping mapping,UsuarioBasico usuario)
	{
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		String estadoEliminar="ninguno";
		FactorConversionMonedas mundo = new FactorConversionMonedas();
		String indices [] = {"codigo_","codigotipomoneda_","descripciontipomoneda_","factor_","fechainicial_","fechafinal_"};
		//logger.info("valor del hasmap FactorConversionMonedas al entar a accionGuardarRegistros "+forma.getFactorMap());
		//logger.info("valor del hasmap conceptosPagoPoolesEliminadosMap al entar a accionGuardarRegistros "+forma.getConceptosPagoPoolesEliminadosMap());
		//eliminar
		
		for(int i=0;i<Integer.parseInt(forma.getFactorEliminadosMap("numRegistros")+"");i++)
		{
			//logger.info("Entro a eliminar BD :::::::::::::contador I...>"+i);
			//logger.info("Hashmap:::::::::::"+forma.getConsentimientoInfEliminadoMap());
					
			if(eliminarBD(connection, forma, i));			
			{				
				//se genera el log tipo harchivo
				Utilidades.generarLogGenerico(forma.getFactorEliminadosMap(), null, usuario.getLoginUsuario(), true, i, ConstantesBD.logFactorConversionMonedasCodigo, indices);
				estadoEliminar="operacionTrue";
				 transacction=true;						 
			}
		}		
		
		for(int i=0;i<Integer.parseInt(forma.getFactorMap("numRegistros")+"");i++)
		{			
			
			//modificar			
		//	logger.info("pa ver como va el hashmap "+forma.getFactorMap());	
			if((forma.getFactorMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoSi))
			{
				if(this.existeModificacion(connection, forma, mundo, i, usuario))
				{	
												
					/*logger.info("HashMap:::::::::"+forma.getConsentimientoInfMap());
					logger.info("subindice:::::..."+i);*/
					//logger.info("ENTRO A MODIFICAR::::::::..");
					HashMap tmp = new HashMap();
						
					InfoDatosInt tipoMoneda = new InfoDatosInt ();
					tipoMoneda.setCodigo(-1);
					DtoFactorConversionMonedas factorConversionMonedas = new DtoFactorConversionMonedas (Integer.parseInt(forma.getFactorMap("codigo_"+i).toString()),tipoMoneda,"","","",usuario.getCodigoInstitucionInt(),"");
					tmp=FactorConversionMonedas.cargar(connection, factorConversionMonedas);
											
					//logger.info("EL VALOR DE TMP ES "+tmp);
					transacction = modificarBD(connection, forma, i,usuario);
					//se genera el log tipo harchivo
					Utilidades.generarLogGenerico(forma.getFactorMap(), tmp, usuario.getLoginUsuario(), false, i, ConstantesBD.logFactorConversionMonedasCodigo,indices);
					estadoEliminar="operacionTrue";
				}	
				
			}
			
			//insertar			
			else if(!forma.getFactorMap("numRegistros").toString().equals("0") && forma.getFactorMap("estabd_"+i).equals(ConstantesBD.acronimoNo))
			{
				//logger.info("Entro a insertar "+forma.getFactorMap()); 
				 transacction = insertarBD(connection, forma, i, usuario);
				 
				 estadoEliminar="operacionTrue";
			}
			
			// logger.info("\n\n valor i >> "+i);
		}
		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(connection);			
			logger.info("----->INSERTO 100% CONCEPTOS PAGO POOLES X CONVENIOS");
			
		}
		else
		{
			UtilidadBD.abortarTransaccion(connection);
		}
		if (estadoEliminar=="operacionTrue")
			forma.setEstado("operacionTrue");
		
		return this.accionEmpezar(connection, forma, mapping, connection, usuario);	
	}
	
	
	/**
	 * Carga los datos a eliminar en la BD; el parametro "i"
	 * es el encargado de manejar la posicion dentro del HashMap
	 * @param connection
	 * @param forma
	 * @return
	 */
	public boolean eliminarBD (Connection connection, FactorConversionMonedasForm forma,int i)
	{
		FactorConversionMonedas mundo = new FactorConversionMonedas();
       
		return mundo.eliminarRegistro(connection, Integer.parseInt(forma.getFactorEliminadosMap("codigo_"+i).toString()));
	}
	
	
	
	/**
	 * verifica si el registro se ha modificado
	 * @param connection
	 * @param forma
	 * @param mundo
	 * @param int pos
	 * */
	private boolean existeModificacion(Connection connection, FactorConversionMonedasForm forma,FactorConversionMonedas mundo, int pos , UsuarioBasico usuario)
	{
		HashMap temp = new HashMap();
		
		//iniciamos los datos para la consulta
		
		InfoDatosInt tipoMoneda = new InfoDatosInt ();
		tipoMoneda.setCodigo(-1);
		DtoFactorConversionMonedas factorConversionMonedas = new DtoFactorConversionMonedas (-1,tipoMoneda,"","","",usuario.getCodigoInstitucionInt(),"");
		temp=mundo.cargar(connection, factorConversionMonedas);
		
	
		 //logger.info("HashMap de tmp existeModificacion ::::::"+temp);
		String[] indices = (String[])forma.getFactorMap("INDICES_MAPA");		
			
		for(int i=0;i<indices.length;i++)
		{		
			
			if(temp.containsKey(indices[i]+"0")&&forma.getFactorMap().containsKey(indices[i]+pos))
			{				
				
				if(!((temp.get(indices[i]+"0").toString().equals((forma.getFactorMap(indices[i]+pos)).toString()))))
				{
					//this.generarlog(forma, temp, usuario, false, pos);
					
					return true;
					
				}				
			}
		}	
		
		return false;		
	}
	
	
	/**
	 * este metodo se encarga de guardar los cambios en la BD; el parametro "i"
	 * es el encargado de manejar la posicion dentro del HashMap
	 * @param connection
	 * @param forma
	 * @param i 
	 * @return
	 */
	public boolean modificarBD (Connection connection,FactorConversionMonedasForm forma,int i,UsuarioBasico usuario )
	{
		//logger.info("valor de haspmap al entrar"+forma.getConceptosPagoPoolesMap());
		//se instancia el mundo
		FactorConversionMonedas mundo = new FactorConversionMonedas();
		//logger.info("el valor de hashmap en modificarBD "+forma.getFactorMap());
		//se carga el valor del infodatos
		InfoDatosInt tipoMoneda = new InfoDatosInt ();
		tipoMoneda.setCodigo(Integer.parseInt(forma.getFactorMap("codigotipomoneda_"+i).toString()));
		//se carga el valor del Dto
		DtoFactorConversionMonedas factorConversionMonedas = new DtoFactorConversionMonedas (Integer.parseInt(forma.getFactorMap("codigo_"+i).toString()),
				tipoMoneda,forma.getFactorMap("fechainicial_"+i).toString(),forma.getFactorMap("fechafinal_"+i).toString(),forma.getFactorMap("factor_"+i).toString(),
				Integer.parseInt(forma.getFactorMap("institucion_"+i).toString()),forma.getFactorMap("usuario_"+i).toString());
		
		
		return mundo.modificar(connection,factorConversionMonedas);
	}
	
	
	/**
	 * Este metodo se encarga de insertar en la BD los nuevos datos; pordentro el crea un HashMap
	 * con los nuevos datos que se van a ingresar a la BD
	 * @param connection
	 * @param forma
	 * @param i
	 * @param usuario
	 * @return
	 */
	public boolean insertarBD (Connection connection, FactorConversionMonedasForm forma,int i,UsuarioBasico usuario)
	{
		FactorConversionMonedas mundo = new FactorConversionMonedas();
		//se carga el valor del infodatos
		//logger.info("esta en insertarBD "+forma.getFactorMap());
		InfoDatosInt tipoMoneda = new InfoDatosInt ();
				tipoMoneda.setCodigo(Integer.parseInt(forma.getFactorMap("codigotipomoneda_"+i).toString()));
		//se carga el valor del Dto
		DtoFactorConversionMonedas factorConversionMonedas = new DtoFactorConversionMonedas (-1,
				tipoMoneda,forma.getFactorMap("fechainicial_"+i).toString(),forma.getFactorMap("fechafinal_"+i).toString(),forma.getFactorMap("factor_"+i).toString(),
				Integer.parseInt(forma.getFactorMap("institucion_"+i).toString()),forma.getFactorMap("usuario_"+i).toString());
		
		
			//logger.info("el valor del factor "+factorConversionMonedas.obtenerDtoString());
			 //logger.info("\n\n saliendo de insertarDB el valor de temp"+temp);
		return mundo.insertar(connection, factorConversionMonedas);
	}
	
}
