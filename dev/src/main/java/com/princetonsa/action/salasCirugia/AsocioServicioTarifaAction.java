package com.princetonsa.action.salasCirugia;

import java.nio.charset.CodingErrorAction;
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
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.salasCirugia.AsocioServicioTarifaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.salasCirugia.AsocioServicioTarifa;

/**
 * @author Jose Eduardo Arias Doncel
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Parametrización de  Asocios Servicios de Tarifa
 */
public class AsocioServicioTarifaAction extends Action {	

	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(AsocioServicioTarifaAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response ) throws Exception
									{
		Connection con=null;
		try{
			if (response==null); //Para evitar que salga el warning
			if(form instanceof AsocioServicioTarifaForm)
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

				//OBJETOS A USAR
				AsocioServicioTarifaForm forma =(AsocioServicioTarifaForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				String estado=forma.getEstado(); 
				ActionErrors errores = new ActionErrors();
				logger.warn("estado Asocios Servicios -->"+estado);


				if(estado == null)
				{
					forma.reset();	
					logger.warn("Estado no valido dentro del flujo de Asocios Servicios de Tarifa (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}			

				//*******ESTADOS DEL ENCABEZADO ASOCIOS SERVICIO DE TARIFA*****
				else if(estado.equals("empezar"))
				{
					accionEmpezar(con,forma,mapping,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");					
				}
				//Estado para mostrar la informacion inicial dependiendo del tipo de ingreso (Esquema o Convenio) 
				else if(estado.equals("mostrarIngreso"))
				{
					if(forma.getIndicadorIngreso().equals("convenio"))
						return accionCargarInformacionVigencia(con, forma, usuario, mapping);
					if(forma.getIndicadorIngreso().equals("esquema"))
						return accionCargarInformacionDetalle(con, forma, usuario, mapping);
				}
				else if(estado.equals("mostrarDetalle"))
				{
					return accionCargarInformacionDetalle(con, forma, usuario, mapping);
				}
				else if(estado.equals("nuevaVigencia"))
				{
					return accionNuevoVigencia(con, forma, mapping, request, usuario, response);
				}
				else if (estado.equals("guardarVigencia"))
				{
					errores = accionValidarGuardarVigencia(forma, errores);

					if(!errores.isEmpty())
					{
						saveErrors(request,errores);
						return mapping.findForward("convenio");					
					}

					accionGuardarEncabezado(con,forma,mapping,usuario);				
					return mapping.findForward("convenio");		
				}
				else if(estado.equals("eliminarVigencia"))
				{
					return accionEliminarVigencia(con, forma, mapping, usuario, request);
				}		
				else if (estado.equals("redireccion"))
				{
					return accionRedireccion(con,forma,response,mapping,request);
				}
				else if(estado.equals("ordenarVigencia"))
				{
					return accionOrdenarVigencia(con,forma,mapping,request);
				}			

				//*************************************************************
				else if(estado.equals("nuevoDetalle"))
				{
					accionNuevoDetalle(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("buscarEspecialidad") || estado.equals("buscarGrupoServicio"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("busqueda");				
				}				
				else if(estado.equals("guardarDetalle"))
				{
					errores = accionValidarGuardarDetalle(forma,errores);

					if(!errores.isEmpty())
					{
						saveErrors(request,errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("principal");
					}

					return accionGuardarDetalle(con,forma, mapping, usuario, request);
				}	
				else if(estado.equals("eliminarDetalle"))
				{
					return accionEliminarDetalle(con, forma, mapping, usuario, request);
				}
				else if(estado.equals("ordenarDetalle"))
				{
					return accionOrdenarDetalle(con,forma,mapping,request);
				}
				//**********************************************************************			
			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}
	
	//*************************************************************************************
	//*************************************************************************************
	
	
	/**
	 * Método inicial que carga los datos del encabezado
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private void accionEmpezar(
			Connection con, 
			AsocioServicioTarifaForm forma, 
			ActionMapping mapping, 
			UsuarioBasico usuario) 
	{
		//se reinicia información
		String estado=forma.getEstado();
		HashMap parametros = new HashMap();
		forma.reset();
		
		//actualiza el estado 
		forma.setEstado(estado);
								
		//Toma la informacion de esquemas tarifarios
		forma.setEsquemasTarifariosArray(Utilidades.obtenerEsquemasTarifariosGenPartInArray(con,usuario.getCodigoInstitucionInt(),"asociosserv"));
		//Toma la informacion de los convenios
		forma.setConvenioArray(Utilidades.obtenerConvenios(con, "","2",false,"",false));
		//Toma la informacion de Grupo de Servicios
		parametros.put("institucion",usuario.getCodigoInstitucionInt());
		parametros.put("activo",ValoresPorDefecto.getValorTrueParaConsultas());
		forma.setGrupoServicioArray(Utilidades.obtenerGrupoServicios(con,parametros));
		//Toma la informacion de Especialidades
		forma.setEspecialidadMap(Utilidades.obtenerEspecialidades());
		
		//Consulta el tipo de servicio		
		forma.setTiposServicioArray(UtilidadesFacturacion.obtenerTiposServicio(con,"'"+ConstantesBD.codigoServicioQuirurgico+"','"+ConstantesBD.codigoServicioPartosCesarea+"','"+ConstantesBD.codigoServicioNoCruentos+"'",""));
		//Consulta los Asocios
		forma.setAsociosArray(Utilidades.obtenerAsocios(con, "", "", ""));
		
		//Inicializa los valores del encabezado
		forma.setConvenioMap("codigo",ConstantesBD.codigoNuncaValido+"");
		forma.setConvenioMap("nombre_convenio","");
						
		
		forma.setEsquemaTarifarioMap("codigo",ConstantesBD.codigoNuncaValido+"");
		forma.setEsquemaTarifarioMap("tipo_esquema",ConstantesBD.codigoNuncaValido+"");
		forma.setEsquemaTarifarioMap("nombre_esquema","");						
	}
	
	
	/**
	 * Carga la Informacion de las Vigencias por Convenio
	 * @param Connection con
	 * @param AsocioServicioTarifaForm forma
	 * @param UsuarioBasico usuario
	 * @param ActionMapping mapping
	 * */
	private ActionForward accionCargarInformacionVigencia(
			Connection con,
			AsocioServicioTarifaForm forma,
			UsuarioBasico usuario, 
			ActionMapping mapping)
	{
		//*********Carga la Informacion de las vigencias por convenio		
		HashMap parametros = new HashMap();
		
		//resetea los parametros del pager
		forma.setNumRegistros(0);
		forma.setOffsetHash(0);
		
		forma.setIndiceVigencias("");
				
		if(forma.getEsquemaTarifarioMap("codigo").toString().equals(ConstantesBD.codigoNuncaValido+"") 
			&& forma.getConvenioMap("codigo").toString().equals(ConstantesBD.codigoNuncaValido+""))
		{
			forma.setEstado("empezar");
			accionEmpezar(con,forma,mapping,usuario);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("convenio");	
		}		
		
		parametros.put("convenio",forma.getConvenioMap("codigo"));
		parametros.put("institucion",usuario.getCodigoInstitucionInt());
		forma.setVigenciasMap(AsocioServicioTarifa.cargarEncabAsociosServ(con, parametros));	
		forma.setNumRegistros(Integer.parseInt(forma.getVigenciasMap("numRegistros").toString()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("convenio");
	}
	
	
	/**
	 * Método usado para insertar un nuevo elemento en el listado de porcentajes Cx Multiples
	 * @param con
	 * @param AsociosServiciosTarifaForm
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @param response
	 * @return
	 */
	private ActionForward accionNuevoVigencia(Connection con, 
			AsocioServicioTarifaForm forma, 
			ActionMapping mapping, 
			HttpServletRequest request, 
			UsuarioBasico usuario, 
			HttpServletResponse response) 
	{
	
		try
		{
		 	int pos= Integer.parseInt(forma.getVigenciasMap("numRegistros").toString());
		    forma.setVigenciasMap("codigo_encab_"+pos,ConstantesBD.codigoNuncaValido+"");		    
		    forma.setVigenciasMap("esq_tar_particular_"+pos,"");
		    forma.setVigenciasMap("esq_tar_general_"+pos,"");
		    forma.setVigenciasMap("institucion_"+pos,usuario.getCodigoInstitucionInt());
		    forma.setVigenciasMap("convenio_"+pos,forma.getConvenioMap("codigo"));
		    forma.setVigenciasMap("fecha_inicial_"+pos,"");
		    forma.setVigenciasMap("fecha_final_"+pos,"");		    
		    forma.setVigenciasMap("cuanto_detalle_"+pos,0);
		    forma.setVigenciasMap("estabd_"+pos,ConstantesBD.acronimoNo);   
		    			    
		    //se aumenta tamaño del listado
		    pos++;
		    forma.setNumRegistros(pos);
		    forma.setVigenciasMap("numRegistros",pos+"");
		    
		    UtilidadBD.cerrarConexion(con);
		    int maxPageItems=Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt()));
		    if(request.getParameter("ultimaPagina")==null)
		    {
		        if(forma.getNumRegistros()>(forma.getOffsetHash()+maxPageItems))
			        forma.setOffsetHash(forma.getOffsetHash()+maxPageItems);
		        
		        response.sendRedirect("../asociosServiciosTarifa/ingresarAsocioSerTarifaConvenio.jsp?pager.offset="+forma.getOffsetHash());
		    }
		    else
		    {    
			    String ultimaPagina=request.getParameter("ultimaPagina");
			    int posOffSet=ultimaPagina.indexOf("offset=")+7;
			    forma.setOffsetHash((Integer.parseInt(ultimaPagina.substring(posOffSet, ultimaPagina.length() ))));
			    
			    if(forma.getNumRegistros()>(forma.getOffsetHash()+maxPageItems))
			        forma.setOffsetHash(forma.getOffsetHash()+maxPageItems);
			    
			    response.sendRedirect(ultimaPagina.substring(0, posOffSet)+forma.getOffsetHash());
		    }
		    return null;
		}
	    catch(Exception e)
		{
			logger.error("Error en accionNuevo de AsociosServiciosTarifaAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en AsociosServiciosTarifaAction", "errors.problemasDatos", true);
		}
	}

	/**
	 * Validaciones para Guardar las Vigencias
	 * @param  AsocioServicioTarifaForm forma
	 * @param ActionErrors errores
	 * */
	private ActionErrors accionValidarGuardarVigencia(AsocioServicioTarifaForm forma,ActionErrors errores)
	{
		int numRegistros = Integer.parseInt(forma.getVigenciasMap("numRegistros").toString());
		
		for(int i = 0; i< numRegistros; i++)
		{					
			if(forma.getVigenciasMap("fecha_inicial_"+i).equals(""))
				errores.add("fechainicial", new ActionMessage("errors.required","La Fecha inicial del registro "+(i+1)));    		
			
    		if(forma.getVigenciasMap("fecha_final_"+i).equals(""))
    			errores.add("fechafinal", new ActionMessage("errors.required","La Fecha final del registro "+(i+1)));    		

    		if(!forma.getVigenciasMap("fecha_inicial_"+i).equals("") && 
					!forma.getVigenciasMap("fecha_final_"+i).equals(""))
    		{
    			if(!UtilidadFecha.validarFecha(forma.getVigenciasMap("fecha_inicial_"+i)+"") ||
    					!UtilidadFecha.validarFecha(forma.getVigenciasMap("fecha_final_"+i)+""))
    						errores.add("fechas", new ActionMessage("errors.required","Verifique el Formato de Fechas para el campo "+(i+1)+". "));
    		}
    		
    		if(!forma.getVigenciasMap("fecha_inicial_"+i).equals("") && 
					!forma.getVigenciasMap("fecha_final_"+i).equals("") &&
			    		UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(forma.getVigenciasMap("fecha_final_"+i)+""), UtilidadFecha.conversionFormatoFechaAAp(forma.getVigenciasMap("fecha_inicial_"+i)+"")))
			    			errores.add("fechafinal", new ActionMessage("errors.debeSerNumeroMayor", "La Fecha Final debe ser Mayor o Igual a la Inicial ",UtilidadFecha.conversionFormatoFechaAAp(forma.getVigenciasMap("fecha_inicial_"+i)+". ")));

    		if(!forma.getVigenciasMap("fecha_inicial_"+i).equals("") && 
					!forma.getVigenciasMap("fecha_final_"+i).equals("")&&
						forma.getVigenciasMap("estabd_"+i).equals(ConstantesBD.acronimoNo)&&    			    		
							UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(forma.getVigenciasMap("fecha_final_"+i)+""), UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual())))	    	
	    						errores.add("fechafinal", new ActionMessage("errors.debeSerNumeroMayor", "La Fecha Final debe ser Mayor a la Fecha del Sistema ",UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual())));
	    				
			for(int j = i; j < forma.getNumRegistros(); j++)
			{
				if(!forma.getVigenciasMap("fecha_inicial_"+i).equals("") && 
						!forma.getVigenciasMap("fecha_final_"+i).equals(""))
				{
					if(j>i &&  UtilidadFecha.existeTraslapeEntreFechas(
								forma.getVigenciasMap("fecha_inicial_"+i).toString(),
								forma.getVigenciasMap("fecha_final_"+i).toString(),
								forma.getVigenciasMap("fecha_inicial_"+j).toString(),
								forma.getVigenciasMap("fecha_final_"+j).toString()))
								{
									errores.add("descripcion",new ActionMessage("errors.notEspecific","Existe Cruce de Fechas. la Fecha "+forma.getVigenciasMap("fecha_inicial_"+i).toString()+" - "+forma.getVigenciasMap("fecha_final_"+i).toString()+" del Registro Numero "+(i+1)+". Y la Fecha "+forma.getVigenciasMap("fecha_inicial_"+j).toString()+" - "+forma.getVigenciasMap("fecha_final_"+j).toString()+" del Registro Numero "+(j+1)+"."));
									j = forma.getNumRegistros();								
								}
				}
			}
		}		
		return errores;	
	}
	
	
	
	/**
	 * Método usado para actualizar registros o ingresar del encabezado de Porcentajes
	 * @param con
	 * @param AsocioServicioTarifaForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarEncabezado(
			Connection con, 
			AsocioServicioTarifaForm forma, 
			ActionMapping mapping, 
			UsuarioBasico usuario) 
	{
					
		//se inicia transacción
		UtilidadBD.iniciarTransaccion(con);			
		HashMap registroAntiguo=new HashMap(); //objeto usado para almacenar el registro antes de ser modificado
		HashMap parametros = new HashMap();												
		boolean resp=false;
		int numRegistros = Integer.parseInt(forma.getVigenciasMap("numRegistros").toString());
		forma.setEstado("otro");
		
		
		//--------------------------------------------------------------			
		//se itera arreglo de registros		
		for(int i=0;i<numRegistros;i++)
		{					
			//se verifica que el registro sea nuevo o de modificación
			if(forma.getVigenciasMap("estabd_"+i).toString().equals(ConstantesBD.acronimoSi))
			{					
				//obtener registro viejo
				parametros.put("codigo_encab",forma.getVigenciasMap("codigo_encab_"+i));					
				parametros.put("institucion",usuario.getCodigoInstitucionInt());
				registroAntiguo=AsocioServicioTarifa.cargarEncabAsociosServ(con, parametros);					
			
				//carga el objeto con los valores de Vigencias
				parametros = llenarHashMapVigencia(forma.getVigenciasMap(),i,usuario);
				
				if(fueModificadoVigencia(llenarHashMapVigencia(registroAntiguo, 0, usuario),parametros))
				{
					//se actualizan los datos
					logger.info("*****1**************");					
												
					resp=AsocioServicioTarifa.actualizarEncaAsociosSer(con, parametros);							
						
					//se revisa estado actualizacion						
					if(resp){				
						this.generarLog(
								con,
								llenarHashMapVigencia(registroAntiguo,0, usuario),										
								parametros,
								forma,
								"encabezado",
								ConstantesBD.tipoRegistroLogModificacion,
								usuario);
						
						forma.setEstado("guardarVigencia");
					}
				}
			}
			//se inserta nuevo registro
			else
			{
				//se insertan los datos	
				parametros = llenarHashMapVigencia(forma.getVigenciasMap(),i, usuario);					
				if(AsocioServicioTarifa.insertarEncaAsociosServ(con, parametros)>0)
				 {
					resp = true;
					forma.setEstado("guardarVigencia");
				 }
			}
		}			
		
		//verificación de la transacción			
		if(resp)
		{
			//éxito!!!!
			logger.info("******2**************");
			UtilidadBD.finalizarTransaccion(con);
			return accionCargarInformacionVigencia(con,forma, usuario, mapping);			
		}
		
		UtilidadBD.finalizarTransaccion(con);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("convenio");
	}
	
	
	/**
	 * Método usado para eliminar un registro del listado de porcentajes por Convenio
	 * @param con
	 * @param AsociosServicioTarifaForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionEliminarVigencia(
			Connection con, 
			AsocioServicioTarifaForm forma, 
			ActionMapping mapping, 
			UsuarioBasico usuario, 
			HttpServletRequest request) 
	{			
		boolean resp= false; //variable para almacenar el resultado de la eliminación
		int offset=0; //variable para manejar el offset del listado
		int numRegistros = Integer.parseInt(forma.getVigenciasMap("numRegistros").toString());
		forma.setEstado("otro");
				
		HashMap parametros = new HashMap();
		
		//se llena el mundo con los datos del formulario
		parametros = this.llenarHashMapVigencia(forma.getVigenciasMap(),Integer.parseInt(forma.getIndiceVigencias()), usuario);		
		
		logger.info("valor de parametros >> "+parametros);
		
		//se verifica que sea un registro existente 
		if(Integer.parseInt(parametros.get("codigo_encab").toString())>0)
		{
			//se realiza la eliminación del detalle del porcentaje
			resp = AsocioServicioTarifa.eliminarEncaAsociosServ(con,Integer.parseInt(parametros.get("codigo_encab").toString()));				
				//se verifica resultado de la transacción
				
				if(resp){
					this.generarLog(con,parametros,null,forma,"encabezado",ConstantesBD.tipoRegistroLogEliminacion,usuario);
					forma.setEstado("guardarVigencia");
				}
		}		
		
		//se corren posiciones dentro del mapa para borrar el registro
		for(int i=Integer.parseInt(forma.getIndiceVigencias());i<(numRegistros-1);i++)
		{
			forma.setVigenciasMap("codigo_encab_"+i,forma.getVigenciasMap("codigo_encab_"+(i+1)));
			forma.setVigenciasMap("institucion_"+i,forma.getVigenciasMap("institucion_"+(i+1)));				
			forma.setVigenciasMap("esq_tar_particular_"+i,forma.getVigenciasMap("esq_tar_particular_"+(i+1)));
			forma.setVigenciasMap("nombre_esq_tar_part_"+i,forma.getVigenciasMap("nombre_esq_tar_part_"+(i+1)));				
			forma.setVigenciasMap("esq_tar_general_"+i,forma.getVigenciasMap("esq_tar_general_"+(i+1)));
			forma.setVigenciasMap("nombre_esq_tar_gen_"+i,forma.getVigenciasMap("nombre_esq_tar_gen_"+(i+1)));				
			forma.setVigenciasMap("convenio_"+i,forma.getVigenciasMap("convenio_"+(i+1)));
			forma.setVigenciasMap("fecha_inicial_"+i,forma.getVigenciasMap("fecha_inicial_"+(i+1)));
			forma.setVigenciasMap("fecha_final_"+i,forma.getVigenciasMap("fecha_final_"+(i+1)));
			forma.setVigenciasMap("cuanto_detalle_"+i,forma.getVigenciasMap("cuanto_detalle_"+(i+1)));			
			forma.setVigenciasMap("estabd_"+i,forma.getVigenciasMap("estabd_"+(i+1)));				
		}
			
		//se disminuye el número de registros			
		numRegistros--;
		//se actualiza
		forma.setNumRegistros(numRegistros);
		forma.setVigenciasMap("numRegistros",numRegistros+"");
		
		//***SE valida que en la posición del pager hayan registros****
		if(forma.getOffsetHash()>=forma.getNumRegistros())
		{
			int maxPageItems=Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt()));
			offset=forma.getNumRegistros()-maxPageItems;
			if(offset<0)
				offset=0;
			forma.setOffsetHash(offset);
		}		
			
		try
		{		
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("convenio");
		}
		catch(SQLException e)
		{
			logger.error("Error en accionEliminar de AsociosServiciosTarifasAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en AsociosServiciosTarifasAction", "errors.problemasDatos", true);
		}		
	}
	
	
	
	/**
	 * Almacena informacion de vigencias sin indice
	 * @param HashMap datos
	 * @param int pos 
	 * */
	private HashMap llenarHashMapVigencia(HashMap datos, int pos,UsuarioBasico usuario)
	{
		HashMap respuesta = new HashMap();
		
		respuesta.put("codigo_encab",datos.get("codigo_encab_"+pos));
		respuesta.put("institucion",datos.get("institucion_"+pos));		
		respuesta.put("esq_tar_particular",datos.get("esq_tar_particular_"+pos));
		respuesta.put("esq_tar_general",datos.get("esq_tar_general_"+pos));		
		respuesta.put("convenio",datos.get("convenio_"+pos));
		respuesta.put("fecha_inicial",datos.get("fecha_inicial_"+pos));
		respuesta.put("fecha_final",datos.get("fecha_final_"+pos));
		respuesta.put("fecha_inicialBD",UtilidadFecha.conversionFormatoFechaABD(datos.get("fecha_inicial_"+pos).toString()));
		respuesta.put("fecha_finalBD",UtilidadFecha.conversionFormatoFechaABD(datos.get("fecha_final_"+pos).toString()));
		respuesta.put("usuario_modifica",usuario.getLoginUsuario());
		respuesta.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		respuesta.put("hora_modifica",UtilidadFecha.getHoraActual());
		
		return respuesta;
	}
	
	
	/**
	 * Verifica si se modifico las vigencias
	 * @param HashMap anterior
	 * @param HashMap actual
	 * */
	private boolean fueModificadoVigencia(HashMap anterior, HashMap actual)
	{	
		if(!anterior.get("esq_tar_particular").equals(actual.get("esq_tar_particular")) ||
				!anterior.get("esq_tar_general").equals(actual.equals("esq_tar_general")) || 
					!anterior.get("convenio").equals(actual.equals("convenio")) ||
						!anterior.get("fecha_inicial").equals(actual.equals("fecha_inicial")) ||
							!anterior.get("fecha_final").equals(actual.equals("fecha_final")))
			return true;					
		
		return false;	
	}	
		
	/**
	 * Método usado para realizar procesos entre cambio de página del pager
	 * @param con
	 * @param AsociosServiciosForm
	 * @param response
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, AsocioServicioTarifaForm forma, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) {
		try
		{
			
		    UtilidadBD.cerrarConexion(con);
			response.sendRedirect(forma.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion de AsociosServicioTarifaAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en AsociosServicioTarifaAction", "errors.problemasDatos", true);
		}
	}	
	
	
	/**
	 * Método usado para ornder el listado Vigencias
	 * @param con
	 * @param  AsocioServicioTarifaForm
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionOrdenarVigencia(Connection con, AsocioServicioTarifaForm forma, ActionMapping mapping, HttpServletRequest request) {
		try
		{
			String[] indices={
					"codigo_encab_",
					"institucion_",
					"esq_tar_particular_",
					"nombre_esq_tar_part_",
					"esq_tar_general_",
					"nombre_esq_tar_gen_",					
					"convenio_",				
					"nombre_convenio_",
					"fecha_inicial_",
					"fecha_final_",
					"cuanto_detalle_",
					"estabd_"					
				};			
			
			forma.setVigenciasMap(Listado.ordenarMapa(indices,
					forma.getIndice(),
					forma.getUltimoIndice(),
					forma.getVigenciasMap(),
					forma.getNumRegistros()));
			
			forma.setVigenciasMap("numRegistros",forma.getNumRegistros()+"");
			forma.setUltimoIndice(forma.getIndice());
			
			
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("convenio");
		}
		catch(SQLException e)
		{
			logger.error("Error en accionOrdenar de AsociosServiciosTarifaAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en AsociosServiciosTarifaAction", "errors.problemasDatos", true);
		}
	}
	
	//*****************************************************************************************
	//----------------------------------------Metodos del Detalle	
	/**
	 * Cargar informcion del Esquema o del Detalle del Convenio  
	 * @param Connection con
	 * @param AsociosServicioTarifaForm forma
	 * @param UsuarioBasico usuario
	 * @param ActionMapping mapping 
	 * */
	private ActionForward accionCargarInformacionDetalle(
			Connection con,
			AsocioServicioTarifaForm forma,
			UsuarioBasico usuario, 
			ActionMapping mapping)
	{
		
		//*****Carga la informacion general del detalle		
		HashMap parametros = new HashMap();
		
		//resetea los parametros del pager
		forma.setNumRegistros(0);
		forma.setOffsetHash(0);
		
		//logger.info("valores >> "+forma.getEsquemaTarifarioMap("codigo")+" >> "+forma.getConvenioMap("codigo").toString());
		
		if(forma.getEsquemaTarifarioMap("codigo").toString().equals(ConstantesBD.codigoNuncaValido+"") 
			&& forma.getConvenioMap("codigo").toString().equals(ConstantesBD.codigoNuncaValido+""))
		{
			forma.setEstado("empezar");
			accionEmpezar(con,forma,mapping,usuario);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");	
		}			
		
		//********Valida el ingreso a la funcionalidad				
		
		//Por el Convenio
		if(forma.getIndicadorIngreso().equals("convenio"))
		{		
			parametros.put("codigo_encab",forma.getVigenciasMap("codigo_encab_"+forma.getIndiceVigencias()));
			forma.setFechaInicial(forma.getVigenciasMap("fecha_inicial_"+forma.getIndiceVigencias()).toString());
			forma.setFechaFinal(forma.getVigenciasMap("fecha_final_"+forma.getIndiceVigencias()).toString());			
			
			forma.setEsquemaTarifarioMap("codigo_tari","");
			
		}//Por el esquema
		else if(forma.getIndicadorIngreso().equals("esquema"))
		{
			if(!forma.getEsquemaTarifarioMap("codigo").toString().equals(ConstantesBD.codigoNuncaValido+""))
			{
				String []cadena = forma.getEsquemaTarifarioMap("codigo").toString().split(ConstantesBD.separadorSplit);
				
				forma.setEsquemaTarifarioMap("codigo_tari",cadena[0]);
				forma.setEsquemaTarifarioMap("tipo_esquema",cadena[1]);
				forma.setEsquemaTarifarioMap("codigo_encab",cadena[2]);	
				forma.setEsquemaTarifarioMap("institucion",usuario.getCodigoInstitucionInt());			
				
				parametros.put("codigo_encab",forma.getEsquemaTarifarioMap("codigo_encab"));
				parametros.put("institucion",usuario.getCodigoInstitucionInt());
				
				forma.setFechaInicial("");
				forma.setFechaFinal("");			
			}
		}	
		
		llenarDatosBusqueda(forma.getBusquedaAvanzadaMap(), parametros);
		//carga la informacion del Detalle
		forma.setDetalleMap(AsocioServicioTarifa.consultarDetAsociosServ(con,parametros));
		forma.setNumRegistros(Integer.parseInt(forma.getDetalleMap("numRegistros").toString()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}	
	
	
	public static HashMap llenarDatosBusqueda (HashMap datos,HashMap result)
	{
		if (UtilidadCadena.noEsVacio(datos.get("especialidad_-1")+"")  && !(datos.get("especialidad_-1")+"").equals(ConstantesBD.codigoNuncaValido+""))
			result.put("especialidadB", datos.get("especialidad_-1"));
		if (UtilidadCadena.noEsVacio(datos.get("tipoServicio")+"") && !(datos.get("tipoServicio")+"").equals(ConstantesBD.codigoNuncaValido+"") )
			result.put("tipo_servicioB", datos.get("tipoServicio"));
		if (UtilidadCadena.noEsVacio(datos.get("grupoServicio")+"") && !(datos.get("grupoServicio")+"").equals(ConstantesBD.codigoNuncaValido+""))
			result.put("grupo_servicioB", datos.get("grupoServicio"));
		if (UtilidadCadena.noEsVacio(datos.get("servicio")+"") && !(datos.get("servicio")+"").equals(ConstantesBD.codigoNuncaValido+""))
			result.put("servicioB", datos.get("servicio") );
		if (UtilidadCadena.noEsVacio(datos.get("asicio")+"") && Utilidades.convertirAEntero(datos.get("asicio")+"")>0)
			result.put("asocioB", datos.get("asicio"));
		if (UtilidadCadena.noEsVacio(datos.get("liquidarPor")+""))
			result.put("liquidar_porB", datos.get("liquidarPor") );
		
		return result;
	}
	
	
	
	/**
	 * Ingresa un  nuevo registro al HashMap del detalle
	 * @param ExcepcionesForm forma
	 * */
	public void accionNuevoDetalle(AsocioServicioTarifaForm forma)
	{
		int numRegistros = Integer.parseInt(forma.getDetalleMap("numRegistros").toString());
		
		//Se verifica que no existan mas registros en blanco
		for(int i = 0; i< numRegistros; i++)
		 if((forma.getDetalleMap("tipo_servicio_"+i).toString().equals("") || forma.getDetalleMap("tipo_servicio_"+i).toString().equals(ConstantesBD.codigoNuncaValido+"") ) && 
				 (forma.getDetalleMap("especialidad_"+i).toString().equals("") || forma.getDetalleMap("especialidad_"+i).toString().equals(ConstantesBD.codigoNuncaValido+"") )&& 
				 	(forma.getDetalleMap("grupo_servicio_"+i).toString().equals("") || forma.getDetalleMap("grupo_servicio_"+i).toString().equals(ConstantesBD.codigoNuncaValido+"")) &&
				 		(forma.getDetalleMap("servicio_"+i).toString().equals("")  ||  forma.getDetalleMap("servicio_"+i).toString().equals(ConstantesBD.codigoNuncaValido+""))&&
				 			(forma.getDetalleMap("asocio_"+i).toString().equals("") || forma.getDetalleMap("asocio_"+i).toString().equals(ConstantesBD.codigoNuncaValido+"") ))
			 return;
		
		forma.setDetalleMap("codigo_"+numRegistros,ConstantesBD.codigoNuncaValido+"");
		forma.setDetalleMap("codigo_encab_"+numRegistros,ConstantesBD.codigoNuncaValido+"");
		forma.setDetalleMap("tipo_servicio_"+numRegistros,ConstantesBD.codigoNuncaValido+"");	
		forma.setDetalleMap("nombre_tipo_servicio_"+numRegistros,"");
		forma.setDetalleMap("especialidad_"+numRegistros,ConstantesBD.codigoNuncaValido+"");
		forma.setDetalleMap("nombre_especialidad_"+numRegistros,"");
		forma.setDetalleMap("grupo_servicio_"+numRegistros,ConstantesBD.codigoNuncaValido+"");
		forma.setDetalleMap("nombre_grupo_servicio_"+numRegistros,"");
		forma.setDetalleMap("servicio_"+numRegistros,ConstantesBD.codigoNuncaValido+"");
		forma.setDetalleMap("nombreservicio_"+numRegistros,"");
		forma.setDetalleMap("asocio_"+numRegistros,ConstantesBD.codigoNuncaValido+"");
		forma.setDetalleMap("nombre_asocio_"+numRegistros,"");		
		forma.setDetalleMap("estabd_"+numRegistros,ConstantesBD.acronimoNo);
		
		forma.setDetalleMap("numRegistros",numRegistros+1);
	}	
	
	
	/**
	 * Devueleve un HashMap con la informacion del detalle sin indice
	 * @param ExcepcionesForm forma
	 * */
	public HashMap accionLLenarHashMapDettalle(HashMap origen,int pos,UsuarioBasico usuario)
	{	
		HashMap datos = new HashMap();
		
		datos.put("codigo",origen.get("codigo_"+pos));
		datos.put("codigo_encab",origen.get("codigo_encab_"+pos));
		datos.put("tipo_servicio",origen.get("tipo_servicio_"+pos));	
		datos.put("nombre_tipo_servicio",origen.get("nombre_tipo_servicio_"+pos));
		datos.put("especialidad",origen.get("especialidad_"+pos));
		datos.put("nombre_especialidad",origen.get("nombre_especialidad_"+pos));
		datos.put("grupo_servicio",origen.get("grupo_servicio_"+pos));
		datos.put("nombre_grupo_servicio",origen.get("nombre_grupo_servicio_"+pos));
		datos.put("servicio",origen.get("servicio_"+pos));
		datos.put("nombreservicio",origen.get("nombreservicio_"+pos));
		datos.put("asocio",origen.get("asocio_"+pos));
		datos.put("nombre_asocio",origen.get("nombre_asocio_"+pos));
		datos.put("usuario_modifica",usuario.getLoginUsuario());
		datos.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		datos.put("hora_modifica",UtilidadFecha.getHoraActual());
		datos.put("liquidarpor",origen.get("liquidarpor_"+pos));
		
		return datos;
	}	
	
	
	/**
	 * Validaciones para Guardar las Excepciones del Detalle
	 * @param AsocioServicioTarifaForm forma
	 * @param ActionErrors errores
	 * */
	private ActionErrors accionValidarGuardarDetalle(AsocioServicioTarifaForm forma,ActionErrors errores)
	{
		int numRegistros = Integer.parseInt(forma.getDetalleMap("numRegistros").toString());
		forma.setEstado("otro");
		
		for(int i = 0; i< numRegistros; i++)
		{
			if(!UtilidadCadena.noEsVacio(forma.getDetalleMap("liquidarpor_"+i)+""))
			{
				errores.add("codigo",new ActionMessage("errors.required","El Item Liquidar por del Registro Nro. "+(i+1)));
			}
			
			if(forma.getDetalleMap("tipo_servicio_"+i).toString().equals(ConstantesBD.codigoNuncaValido+"") &&
					forma.getDetalleMap("especialidad_"+i).toString().equals(ConstantesBD.codigoNuncaValido+"") &&
						forma.getDetalleMap("grupo_servicio_"+i).toString().equals(ConstantesBD.codigoNuncaValido+"") &&
							forma.getDetalleMap("servicio_"+i).toString().equals(ConstantesBD.codigoNuncaValido+""))
				errores.add("descripcion",new ActionMessage("errors.notEspecific","Se debe Ingresar por lo menos uno de los siguientes items Tipo Servicio,Especialidad,Grupo Servicio,Servicio. en el Registro Nro. "+(i+1)));
			
			
			if(forma.getDetalleMap("asocio_"+i).toString().equals(ConstantesBD.codigoNuncaValido+"") || 
					forma.getDetalleMap("asocio_"+i).toString().equals(""))
				errores.add("descripcion",new ActionMessage("errors.notEspecific","El Item Asocio del Registro Nro. "+(i+1)+" es Requerido."));
			
			
			for(int j = 0; j< numRegistros; j++)
			{
				if(j!=i &&
						forma.getDetalleMap("tipo_servicio_"+i).toString().equals(forma.getDetalleMap("tipo_servicio_"+j).toString()) &&
							forma.getDetalleMap("especialidad_"+i).toString().equals(forma.getDetalleMap("especialidad_"+j).toString()) &&
								forma.getDetalleMap("grupo_servicio_"+i).toString().equals(forma.getDetalleMap("grupo_servicio_"+j).toString()) &&
									forma.getDetalleMap("servicio_"+i).toString().equals(forma.getDetalleMap("servicio_"+j).toString()) && 
										forma.getDetalleMap("asocio_"+i).toString().equals(forma.getDetalleMap("asocio_"+j).toString()))
				{
					errores.add("descripcion",new ActionMessage("errors.notEspecific","Los Registros Nro. "+(i+1)+" y Nro. "+(j+1)+" se Encuentran Repetidos."));						
					return errores;		
				}
			}				
		}
		
		return errores;
	}
	
	
	/**
	 * Método usado para actualizar registros o ingresar nuevos al Detalle de Asocios Servicios de Tarifa
	 * @param con
	 * @param AsocioServicioTarifaForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarDetalle(
			Connection con, 
			AsocioServicioTarifaForm forma, 
			ActionMapping mapping, 
			UsuarioBasico usuario, 
			HttpServletRequest request) 
	{
								
		UtilidadBD.iniciarTransaccion(con);
		
		HashMap registroAntiguo=new HashMap(); //objeto usado para almacenar el registro antes de ser modificado
		HashMap registroNuevo =new HashMap();  //objeto usado para almacenar el registro  modificado
	
		int codigoEnca = ConstantesBD.codigoNuncaValido;
		int numRegistros = Integer.parseInt(forma.getDetalleMap("numRegistros").toString());		
		boolean resp = false;
		forma.setEstado("otro");
		
		//--------------------------------------------------------------
		//se itera arreglo de registros
		
		codigoEnca = this.getCodigoEncabezado(con,forma, usuario);
		
		if(codigoEnca < 0)
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en AsocioServicioTarifaAction","errors.sinActualizar",true);
		
		for(int i=0;i< numRegistros;i++)
		{				
			//Almacena o actualiza la informacion del codigo del encabezado 
			forma.setDetalleMap("codigo_encab_"+i,codigoEnca);			 
			
			//se verifica que el registro sea nuevo o de modificación				
			if(Integer.parseInt(forma.getDetalleMap("codigo_"+i).toString()) > 0)
			{
				registroNuevo = accionLLenarHashMapDettalle(forma.getDetalleMap(),i,usuario);				
				registroAntiguo= accionLLenarHashMapDettalle(AsocioServicioTarifa.consultarDetAsociosServ(con,registroNuevo),0,usuario);
				
				if(fueModificadoDetalle(registroAntiguo, registroNuevo))
				{	
					resp = AsocioServicioTarifa.modificarDetAsociosServ(con, registroNuevo);							
					
					//se revisa estado actualizacion						
					if(resp){
						
						//se genera LOG
						this.generarLog(
								con,
								registroAntiguo,
								registroNuevo,								
								forma,
								"detalle",
								ConstantesBD.tipoRegistroLogModificacion,
								usuario);
						
						forma.setEstado("guardarDetalle");
					}
				}
			}
			//se inserta nuevo registro
			else
			{
				//se insertan los datos										
				registroNuevo = accionLLenarHashMapDettalle(forma.getDetalleMap(),i,usuario);			
				resp = AsocioServicioTarifa.insertarDetAsociosServicios(con,registroNuevo);
				
				if(resp)
					forma.setEstado("guardarDetalle");			
				else
					UtilidadBD.abortarTransaccion(con);			
			}
		}
		
		UtilidadBD.finalizarTransaccion(con);
		
		if(resp)
			return accionCargarInformacionDetalle(con, forma, usuario, mapping);
		
		return mapping.findForward("principal");
	}
	
	
	/**
	 * Consulta la informacion del encabezado 
	 * @param Connection con
	 * @param AsociosServicioTarifa forma
	 * @param UsuarioBasico usuario
	 * */
	private int getCodigoEncabezado(Connection con,AsocioServicioTarifaForm forma,UsuarioBasico usuario)
	{
		int respuesta=ConstantesBD.codigoNuncaValido;
		HashMap parametros = new HashMap();	
		
		//Ingreso por el esquema tarifario
		if(forma.getIndicadorIngreso().equals("esquema"))
		{
			
		
			//No existe el encabezado creado		
			if(forma.getEsquemaTarifarioMap("codigo_encab").toString().equals(ConstantesBD.codigoNuncaValido+""))
			{
				//esquema particular 
				if(forma.getEsquemaTarifarioMap("tipo_esquema").toString().equals("1"))
				{
					parametros.put("esq_tar_particular",forma.getEsquemaTarifarioMap("codigo_tari"));
					parametros.put("esq_tar_general",null);
				}
				else
				{
					parametros.put("esq_tar_particular",null);
					parametros.put("esq_tar_general",forma.getEsquemaTarifarioMap("codigo_tari"));					
				}
					
				parametros.put("institucion",usuario.getCodigoInstitucionInt());
				parametros = AsocioServicioTarifa.cargarEncabAsociosServ(con, parametros);
								
								
				if(parametros.get("numRegistros").equals("0"))
				{
					respuesta = accionGuardarEncabezado(con,forma,usuario);			
					
					if(respuesta > 0)
					{
						//Toma la informacion de esquemas tarifarios
						forma.setEsquemasTarifariosArray(Utilidades.obtenerEsquemasTarifariosGenPartInArray(con,usuario.getCodigoInstitucionInt(),"asociosserv"));
						forma.setEsquemaTarifarioMap("codigo",forma.getEsquemaTarifarioMap("codigo_tari").toString()+ConstantesBD.separadorSplit+forma.getEsquemaTarifarioMap("tipo_esquema").toString()+ConstantesBD.separadorSplit+respuesta);
					}
				}
				else
				{
					respuesta = Integer.parseInt(parametros.get("codigo_encab_0").toString());					
				}
			}
			else
			{
				respuesta = Integer.parseInt(forma.getEsquemaTarifarioMap("codigo_encab").toString());
			}
		}
		else if(forma.getIndicadorIngreso().equals("convenio"))
		{
			respuesta= Integer.parseInt(forma.getVigenciasMap("codigo_encab_"+forma.getIndiceVigencias()).toString());						
		}			
				
		return respuesta;				
	}
	
	
	
	/**
	 * @param con
	 * @param AsociosServiciosTarifaForm
	 * @param usuario
	 * @param request
	 * @return el Codigo del encabezado
	 * */
	public int accionGuardarEncabezado(
			Connection con, 
			AsocioServicioTarifaForm forma,		 
			UsuarioBasico usuario)
	{
		HashMap parametros = new HashMap();		
		
		//Via de Ingreso Esquema
		if(forma.getIndicadorIngreso().equals("esquema"))
		{
			parametros.put("institucion",usuario.getCodigoInstitucionInt());
			
			//esquema particular 
			if(forma.getEsquemaTarifarioMap("tipo_esquema").toString().equals("1"))
			{
				parametros.put("esq_tar_particular",forma.getEsquemaTarifarioMap("codigo_tari"));
				parametros.put("esq_tar_general","");
			}
			else
			{
				parametros.put("esq_tar_particular","");
				parametros.put("esq_tar_general",forma.getEsquemaTarifarioMap("codigo_tari"));					
			}
			
			parametros.put("convenio","");
			parametros.put("fecha_inicial","");
			parametros.put("fecha_final","");
			
			parametros.put("fecha_inicialBD","");
			parametros.put("fecha_finalBD","");
			
			parametros.put("usuario_modifica",usuario.getLoginUsuario());
			parametros.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			parametros.put("hora_modifica",UtilidadFecha.getHoraActual());
			
			return AsocioServicioTarifa.insertarEncaAsociosServ(con, parametros);		
		}		
		//--------------------------------------
		
		return ConstantesBD.codigoNuncaValido;
	}
	
	
	
	/**
	 * Método implementado para eliminar un registro del mapa Detalle
	 * @param con
	 * @param AsocioServicioTarifaForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */	
	private ActionForward accionEliminarDetalle(Connection con, 
											  AsocioServicioTarifaForm forma, 
											  ActionMapping mapping, 
											  UsuarioBasico usuario, 
											  HttpServletRequest request) 
	{
		int pos = Integer.parseInt(forma.getIndicadorDetalle().toString());		
		int numRegistros = Integer.parseInt(forma.getDetalleMap("numRegistros").toString());
		boolean resp0 = true;
		
		//se toma el código del registro
		int auxI0 = Integer.parseInt(forma.getDetalleMap("codigo_"+pos).toString());
		int codigo_encab = Integer.parseInt(forma.getDetalleMap("codigo_encab_"+pos).toString());
				
		//se instancian errores
		ActionErrors errores = new ActionErrors();
		
		HashMap parametros = new HashMap();
		
		//se verifica si el registro es nuevo o ya existe en la base de datos
		if(auxI0>0)
		{
			//*********ELIMINACION DEL REGISTRO DESDE LA BASE DE DATOS**************			
			parametros.put("codigo",auxI0);
			parametros.put("codigo_encab",codigo_encab);			
			resp0 =	AsocioServicioTarifa.EliminarDetAsociosServ(con, parametros);
			
			if(!resp0)
				errores.add("error al eliminar",new ActionMessage("errors.sinEliminar"));
			else
			{
				//se genera LOG
				this.generarLog(
						con,
						accionLLenarHashMapDettalle(forma.getDetalleMap(),pos, usuario),										
						null,
						forma,
						"detalle",
						ConstantesBD.tipoRegistroLogEliminacion,
						usuario);
				forma.setEstado("guardarDetalle");
			}
			//************************************************************************
		}
		
		
		saveErrors(request,errores);
		//si la eliminación es exitosa se elimina del mapa
		if(resp0)
		{
			//*******ELIMINACIÓN DEL REGISTRO EN EL MAPA***********************
			for(int i=pos;i<(numRegistros-1);i++)
			{
				forma.setDetalleMap("codigo_"+i,forma.getDetalleMap("codigo_"+(i+1)));
				forma.setDetalleMap("codigo_encab_"+i,forma.getDetalleMap("codigo_encab_"+(i+1)));
				forma.setDetalleMap("tipo_servicio_"+i,forma.getDetalleMap("tipo_servicio_"+(i+1)));
				forma.setDetalleMap("nombre_tipo_servicio_"+i,forma.getDetalleMap("nombre_tipo_servicio_"+(i+1)));
				forma.setDetalleMap("especialidad_"+i,forma.getDetalleMap("especialidad_"+(i+1)));
				forma.setDetalleMap("nombre_especialidad_"+i,forma.getDetalleMap("nombre_especialidad_"+(i+1)));
				forma.setDetalleMap("grupo_servicio_"+i,forma.getDetalleMap("grupo_servicio_"+(i+1)));
				forma.setDetalleMap("nombre_grupo_servicio_"+i,forma.getDetalleMap("nombre_grupo_servicio_"+(i+1)));				
				forma.setDetalleMap("servicio_"+i,forma.getDetalleMap("servicio_"+(i+1)));
				forma.setDetalleMap("nombreservicio_"+i,forma.getDetalleMap("nombreservicio_"+(i+1)));				
				forma.setDetalleMap("asocio_"+i,forma.getDetalleMap("asocio_"+(i+1)));
				forma.setDetalleMap("nombre_asocio_"+i,forma.getDetalleMap("nombre_asocio_"+(i+1)));							
				forma.setDetalleMap("estabd_"+i,forma.getDetalleMap("estabd_"+(i+1)));
				
			}
			//se elimina último registro
			pos = numRegistros - 1;
			forma.getDetalleMap().remove("codigo_"+pos);
			forma.getDetalleMap().remove("codigo_encab_"+pos);
			forma.getDetalleMap().remove("tipo_servicio_"+pos);
			forma.getDetalleMap().remove("nombre_tipo_servicio_"+pos);
			forma.getDetalleMap().remove("especialidad_"+pos);
			forma.getDetalleMap().remove("nombre_especialidad_"+pos);
			forma.getDetalleMap().remove("grupo_servicio_"+pos);
			forma.getDetalleMap().remove("nombre_grupo_servicio_"+pos);			
			forma.getDetalleMap().remove("servicio_"+pos);
			forma.getDetalleMap().remove("nombreservicio_"+pos);			
			forma.getDetalleMap().remove("asocio_"+pos);
			forma.getDetalleMap().remove("nombre_asocio_"+pos);			
			forma.getDetalleMap().remove("estabd_"+pos);			
			
			//******************************************************************
			
			//se actualiza tamaño del mapa			
			forma.setDetalleMap("numRegistros",pos+"");
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
		
	
	/**
	 * Verifiica si se modifico la informacion del Detalle
	 * @param HashMap anteriorMap
	 * @param HashMap actualMap
	 * */
	public boolean fueModificadoDetalle(HashMap anteriorMap, HashMap actualMap)	
	{	
		if(!anteriorMap.get("tipo_servicio").toString().equals(actualMap.get("tipo_servicio").toString()) ||
				!anteriorMap.get("especialidad").toString().equals(actualMap.get("especialidad").toString()) ||
					!anteriorMap.get("grupo_servicio").toString().equals(actualMap.get("grupo_servicio").toString()) ||
						!anteriorMap.get("servicio").toString().equals(actualMap.get("servicio").toString()) ||
							!anteriorMap.get("asocio").toString().equals(actualMap.get("asocio").toString()) ||
								!anteriorMap.get("liquidarpor").toString().equals(actualMap.get("liquidarpor").toString()))
		{
			return true;			
		}
		
		return false;	
	}
	
	
	/**
	 * Método usado para ornder el listado de Detalle
	 * @param con
	 * @param  AsocioServicioTarifaForm
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionOrdenarDetalle(Connection con, AsocioServicioTarifaForm forma, ActionMapping mapping, HttpServletRequest request) 
	{
		logger.info("\n entro a accionOrdenarDetalle mapa detalle -->"+forma.getDetalleMap());
		
		try
		{			
			String[] indices={
					"codigo_",
					"codigo_encab_",
					"tipo_servicio_",
					"nombre_tipo_servicio_",
					"especialidad_",
					"nombre_especialidad_",
					"grupo_servicio_",
					"nombre_grupo_servicio_",
					"servicio_",
					"nombreservicio_",
					"asocio_",
					"nombre_asocio_",
					"liquidarpor_",					
					"estabd_"					
				};
			
			String numRegistros = forma.getDetalleMap("numRegistros").toString();
			
			forma.setDetalleMap(Listado.ordenarMapa(indices,
					forma.getIndice(),
					forma.getUltimoIndice(),
					forma.getDetalleMap(),
					Integer.parseInt(numRegistros)));
			
			forma.setDetalleMap("numRegistros",numRegistros);
			logger.info("\n al salir el mapa detalle es -->"+forma.getDetalleMap());
			forma.setUltimoIndice(forma.getIndice());
			
			
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("principal");
		}
		catch(SQLException e)
		{
			logger.error("Error en accionOrdenar de AsociosServiciosTarifaAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en AsociosServiciosTarifaAction", "errors.problemasDatos", true);
		}
	}
	
	
	/**
	 * @param con
	 * @param registro (antiguo)	 
	 * @param registroNuevo
	 * @param Forma 
	 * @param parteTabla
	 * @param tipo (tipo de Log)
	 * @param usuario
	 */
	private void generarLog(
			Connection con, 
			HashMap registro, 
			HashMap registroNuevo,
			AsocioServicioTarifaForm forma,
			String parteTabla,
			int tipo, 
			UsuarioBasico usuario) 
	{
		String log="";
	    //***********************************************************
		
		if(parteTabla.equals("detalle"))
		{
			if(tipo==ConstantesBD.tipoRegistroLogModificacion)
			{				
			    log="\n            ====INFORMACION ORIGINAL DEL DETALLE ASOCIOS SERVICIOS DE TARIFA===== " +
			    
			    "\n*  ENCABEZADO " +
			    "\n*  Codigo Encabezado ["+registro.get("codigo_encab")+"] " +
			    "\n*  Codigo Esquema Tarifario ["+forma.getEsquemaTarifarioMap("codigo_tari")+"] " +
			    "\n*  Nombre Esquema Tarifario ["+forma.getEsquemaTarifarioMap("nombre_esquema")+"] ";
			    
			    if(forma.getConvenioMap("codigo").toString().equals(ConstantesBD.codigoNuncaValido+""))
			    	log+="Codigo Convenio []";
			    else
			    	log+="Codigo Convenio ["+forma.getConvenioMap("codigo")+"]";			    
			    
			    log+=
			    "\n*  Nombre Convenio ["+forma.getConvenioMap("nombre_convenio")+"] "+			    
			    "\n*  Fecha Inicial ["+forma.getFechaInicial()+"] " +
				"\n*  Fecha Final ["+forma.getFechaFinal()+"] " +
				"\n*  " +
				"\n*  DETALLE "+
				"\n*  Codigo ["+registro.get("codigo")+"] ";
								
				if(registro.get("tipo_servicio").toString().equals(ConstantesBD.codigoNuncaValido+""))
					log+="\n*  Tipo Servicio [] " ;
				else	
					log+="\n*  Tipo Servicio ["+registro.get("tipo_servicio")+"] " ;
				
			    log+= "\n*  Nombre Tipo Servicio ["+registro.get("nombre_tipo_servicio")+"] " ;
			    
			    if(registro.get("especialidad").toString().equals(ConstantesBD.codigoNuncaValido+""))
					log+="\n*  Especialidad [] " ;
				else	
					log+="\n*  Especialidad ["+registro.get("especialidad")+"] " ;
			    
			    log+="\n*  Nombre de la Especialidad ["+registro.get("nombre_especialidad")+"] " ;
				
				if(registro.get("grupo_servicio").toString().equals(ConstantesBD.codigoNuncaValido+""))
					log+="\n*  Grupo Servicio [] " ;
				else	
					log+="\n*  Grupo Servicio ["+registro.get("grupo_servicio")+"] " ;
				
				log+= "\n*  Nombre Grupo Servicio ["+registro.get("nombre_grupo_servicio")+"] " ;
				
				if(registro.get("servicio").toString().equals(ConstantesBD.codigoNuncaValido+""))
					log+= "\n*  Servicio [] " ;
				else
					log+= "\n*  Servicio ["+registro.get("servicio")+"] " ;
														
				log+="\n*  Nombre Servicio ["+registro.get("nombreservicio")+"] " ;
				
				log+="\n*  Asocio ["+registro.get("asocio")+"] " +				
				"\n*  Nombre Asocio ["+registro.get("nombre_asocio")+"] " +
				"";
	
				//***************************************************
			    log+="\n\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓN DEL DETALLE ASOCIOS SERVICIOS DE TARIFA===== " +
				
		    	"\n*  DETALLE "+
				"\n*  Codigo ["+registroNuevo.get("codigo")+"] ";
								
				if(registroNuevo.get("tipo_servicio").toString().equals(ConstantesBD.codigoNuncaValido+""))
					log+="\n*  Tipo Servicio [] " ;
				else	
					log+="\n*  Tipo Servicio ["+registroNuevo.get("tipo_servicio")+"] " ;
				
			    log+= "\n*  Nombre Tipo Servicio ["+registroNuevo.get("nombre_tipo_servicio")+"] " ;
			    
			    if(registroNuevo.get("especialidad").toString().equals(ConstantesBD.codigoNuncaValido+""))
					log+="\n*  Especialidad [] " ;
				else	
					log+="\n*  Especialidad ["+registroNuevo.get("especialidad")+"] " ;
			    
			    log+="\n*  Nombre de la Especialidad ["+registroNuevo.get("nombre_especialidad")+"] " ;
				
				if(registroNuevo.get("grupo_servicio").toString().equals(ConstantesBD.codigoNuncaValido+""))
					log+="\n*  Grupo Servicio [] " ;
				else	
					log+="\n*  Grupo Servicio ["+registroNuevo.get("grupo_servicio")+"] " ;
				
				log+= "\n*  Nombre Grupo Servicio ["+registroNuevo.get("nombre_grupo_servicio")+"] " ;
				
				if(registroNuevo.get("servicio").toString().equals(ConstantesBD.codigoNuncaValido+""))
					log+= "\n*  Servicio [] " ;
				else
					log+= "\n*  Servicio ["+registroNuevo.get("servicio")+"] " ;
														
				log+="\n*  Nombre Servicio ["+registroNuevo.get("nombreservicio")+"] " ;
				
				log+="\n*  Asocio ["+registroNuevo.get("asocio")+"] " +				
				"\n*  Nombre Asocio ["+registroNuevo.get("nombre_asocio")+"] " +
				"";

			}
			else if(tipo==ConstantesBD.tipoRegistroLogEliminacion)
			{
				
				//***************************************************
				 log="\n            ====INFORMACION ELIMINADA DEL DETALLE ASOCIOS SERVICIOS DE TARIFA===== " +
				    
			    "\n*  ENCABEZADO " +
			    "\n*  Codigo Encabezado ["+registro.get("codigo_encab")+"] " +
			    "\n*  Codigo Esquema Tarifario ["+forma.getEsquemaTarifarioMap("codigo_tari")+"] " +
			    "\n*  Nombre Esquema Tarifario ["+forma.getEsquemaTarifarioMap("nombre_esquema")+"] " ;
			    
			    if(forma.getConvenioMap("codigo").toString().equals(ConstantesBD.codigoNuncaValido+""))
			    	log+="Codigo Convenio []";
			    else
			    	log+="Codigo Convenio ["+forma.getConvenioMap("codigo")+"]";			    
			    
			    log+=			    
			    "\n*  Nombre Convenio ["+forma.getConvenioMap("nombre_convenio")+"] " +
			    "\n*  Fecha Inicial ["+forma.getFechaInicial()+"] " +
				"\n*  Fecha Final ["+forma.getFechaFinal()+"] " +
				"\n* " +
				"\n*  DETALLE "+
				"\n*  Codigo ["+registro.get("codigo")+"] ";
								
				if(registro.get("tipo_servicio").toString().equals(ConstantesBD.codigoNuncaValido+""))
					log+="\n*  Tipo Servicio [] " ;
				else	
					log+="\n*  Tipo Servicio ["+registro.get("tipo_servicio")+"] " ;
				
			    log+= "\n*  Nombre Tipo Servicio ["+registro.get("nombre_tipo_servicio")+"] " ;
			    
			    if(registro.get("especialidad").toString().equals(ConstantesBD.codigoNuncaValido+""))
					log+="\n*  Especialidad [] " ;
				else	
					log+="\n*  Especialidad ["+registro.get("especialidad")+"] " ;
			    
			    log+="\n*  Nombre de la Especialidad ["+registro.get("nombre_especialidad")+"] " ;
				
				if(registro.get("grupo_servicio").toString().equals(ConstantesBD.codigoNuncaValido+""))
					log+="\n*  Grupo Servicio [] " ;
				else	
					log+="\n*  Grupo Servicio ["+registro.get("grupo_servicio")+"] " ;
				
				log+= "\n*  Nombre Grupo Servicio ["+registro.get("nombre_grupo_servicio")+"] " ;
				
				if(registro.get("servicio").toString().equals(ConstantesBD.codigoNuncaValido+""))
					log+= "\n*  Servicio [] " ;
				else
					log+= "\n*  Servicio ["+registro.get("servicio")+"] " ;
														
				log+="\n*  Nombre Servicio ["+registro.get("nombreservicio")+"] " ;
				
				log+="\n*  Asocio ["+registro.get("asocio")+"] " +				
				"\n*  Nombre Asocio ["+registro.get("nombre_asocio")+"] " +
				"";
			}
			
		} 
		else if(parteTabla.equals("encabezado"))
		{
			if(tipo==ConstantesBD.tipoRegistroLogModificacion)
			{				
			    log="\n            ====INFORMACION ORIGINAL DEL ENCABEZADO CONVENIO/VIGENCIA===== " +
			    
			    "\n*  ENCABEZADO " +
			    "\n*  Codigo Encabezado ["+registro.get("codigo_encab")+"] " +
			    "\n*  Codigo Esquema Tarifario [] " +
			    "\n*  Nombre Esquema Tarifario [] " +
			    "\n*  Codigo Convenio ["+forma.getConvenioMap("codigo")+"] " +
			    "\n*  Nombre Convenio ["+forma.getConvenioMap("nombre_convenio")+"] " +
				"\n*  Fecha Inicial ["+registro.get("fecha_inicial")+"] " +
				"\n*  Fecha Final ["+registro.get("fecha_final")+"] " +			
				"";
	
				//***************************************************
			    log+="\n\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓN DEL ENCABEZADO CONVENIO/VIGENCIA===== " +
			    "\n*  ENCABEZADO " +
			    "\n*  Codigo Encabezado ["+registroNuevo.get("codigo_encab")+"] " +
			    "\n*  Codigo Esquema Tarifario [] " +
			    "\n*  Nombre Esquema Tarifario [] " +
			    "\n*  Codigo Convenio ["+forma.getConvenioMap("codigo")+"] " +
			    "\n*  Nombre Convenio ["+forma.getConvenioMap("nombre_convenio")+"] " +
				"\n*  Fecha Inicial ["+registroNuevo.get("fecha_inicial")+"] " +
				"\n*  Fecha Final ["+registroNuevo.get("fecha_final")+"] " +			
				"";
			}
			else if(tipo==ConstantesBD.tipoRegistroLogEliminacion)
			{
				
				//***************************************************
				 log="\n            ====INFORMACION ELIMINADA DEL ENCABEZADO DE PORCENTAJE CIRUGÍA===== " +				    
			    "\n*  ENCABEZADO " +
			    "\n*  Codigo Encabezado ["+registro.get("codigo_encab")+"] " +
			    "\n*  Codigo Esquema Tarifario [] " +
			    "\n*  Nombre Esquema Tarifario [] " +
			    "\n*  Codigo Convenio ["+forma.getConvenioMap("codigo")+"] " +
			    "\n*  Nombre Convenio ["+forma.getConvenioMap("nombre_convenio")+"] " +
				"\n*  Fecha Inicial ["+registro.get("fecha_inicial")+"] " +
				"\n*  Fecha Final ["+registro.get("fecha_final")+"] " +			
				"";
			}			
		}		
		
		log+="\n========================================================\n\n\n " ;
		LogsAxioma.enviarLog(ConstantesBD.logAsociosServiciosTarifasCodigo, log, tipo,usuario.getLoginUsuario());		
	}
}		