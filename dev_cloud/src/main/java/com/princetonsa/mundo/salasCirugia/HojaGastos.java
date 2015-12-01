/*
 * Marzo 27 del 2007
 */
package com.princetonsa.mundo.salasCirugia;


import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.salasCirugia.HojaGastosDao;
import com.princetonsa.mundo.UsuarioBasico;


/**
 * @author Sebastián Gómez 
 *
 *Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * Parametrización de hoja de gastos
 */
public class HojaGastos 
{
	
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(HojaGastos.class);
	/**
	 * DAO para el manejo de HojaGastosDao
	 */
	private HojaGastosDao hojaDao=null;
	
	/**
	 * Mapa de los servicios
	 */
	private HashMap mapaServicios = new HashMap();
	
	/**
	 * Número de los servicios
	 */
	private int numServicios;
	
	/**
	 * Mapa de los articulos
	 */
	private HashMap mapaArticulos = new HashMap();
	
	/**
	 * Número de los artículos
	 */
	private int numArticulos;
	
	//*****************************************************************
	//**********INICIALIZADORES & CONSTRUCTORES***********************
	/**
	 * Constructor
	 */
	public HojaGastos() 
	{
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.mapaServicios = new HashMap();
		this.numServicios = 0;
		this.mapaArticulos = new HashMap();
		this.numArticulos = 0;
	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (hojaDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			hojaDao = myFactory.getHojaGastosDao();
		}	
	}
	
	/**
	 * Método que retorna el DAO instanciado de Hoja Gastos
	 * @return
	 */
	public static HojaGastosDao hojaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHojaGastosDao();
	}
	
	//****************************************************************************
	//********************MÉTODOS**************************************************
	
	/**
	 * Método implementado para consultar el consecutivo x servicio
	 * de la parametrizacion de hoja de gastos
	 * @param con
	 * @param codigoServicio
	 * @param institucion
	 * @return
	 */
	public static int consultarConsecutivoXServicio(Connection con,String codigoServicio,String institucion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoServicio",codigoServicio);
		campos.put("institucion",institucion);
		return hojaDao().consultarConsecutivoXServicio(con, campos);
	}
	
	/**
	 * Método implementado para consultar la parametrizacion de una hoja de gastos por consecutivo
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public void consultarHojaGastosXConsecutivo(Connection con,int consecutivo)
	{
		HashMap campos = new HashMap();
		campos.put("consecutivo",consecutivo+"");
		
		HashMap respuesta = hojaDao.consultarHojaGastosXConsecutivo(con, campos);
		
		this.mapaServicios = (HashMap)respuesta.get("mapaServicios");
		this.mapaArticulos = (HashMap)respuesta.get("mapaArticulos");
		
		this.numServicios = Integer.parseInt(this.mapaServicios.get("numRegistros").toString());
		this.numArticulos = Integer.parseInt(this.mapaArticulos.get("numRegistros").toString());
		
	}
	
	
	/**
	 * Método implementado para guardar la hoja de gastos
	 * @param con
	 * @param campos
	 * @return
	 */
	public int guardar(Connection con,int consecutivo,String institucion)
	{
		HashMap campos = new HashMap();
		campos.put("consecutivo",consecutivo+"");
		campos.put("institucion", institucion);
		campos.put("mapaServicios",this.mapaServicios);
		campos.put("mapaArticulos",this.mapaArticulos);
		campos.put("numServicios",this.numServicios+"");
		campos.put("numArticulos",this.numArticulos+"");
		
		return hojaDao.guardar(con, campos);
	}
	
	/**
	 * Método que compara los datos de la base de datos y los de la aplicacion para verificar
	 * si una hoja de gastos fue modificada
	 * @param con
	 * @param consecutivo
	 *  @param mapaServiciosNuevo
	 * @param numServiciosNuevo
	 * @param mapaArticulosNuevo 
	 * @param numArticulosNuevo  
	 * @return
	 */
	public HashMap huboModificacion(Connection con, int consecutivo,HashMap mapaServiciosNuevo,int numServiciosNuevo, HashMap mapaArticulosNuevo, int numArticulosNuevo) 
	{
		HashMap mapa = new HashMap();
		boolean modificado = false;
		//se consulta la informacion de la hoja de gastos
		consultarHojaGastosXConsecutivo(con, consecutivo);
		
		//se verifica que el numero de servicios o el número de articulos no haya cambiado
		if(obtenerNumRegistrosAIngresar(mapaServiciosNuevo, numServiciosNuevo)!=this.numServicios||
			obtenerNumRegistrosAIngresar(mapaArticulosNuevo, numArticulosNuevo)!=this.numArticulos)
			modificado = true;
		else
		{
			//se verifica que tal vez se hayan modificado las cantidades de los articulos o hay articulos diferentes
			boolean encontrado = false; //avisa si el articulo se encuentra en el mapa nuevo
			for(int i=0;i<this.numArticulos;i++)
			{
				encontrado = false;
				for(int j=0;j<numArticulosNuevo;j++)
				{
					if(!UtilidadTexto.getBoolean(mapaArticulosNuevo.get("eliminar_"+j).toString())&&
						mapaArticulos.get("codigoArticulo_"+i).toString().equals(mapaArticulosNuevo.get("codigoArticulo_"+j).toString()))
					{
						if(Integer.parseInt(mapaArticulos.get("cantidad_"+i).toString())!=Integer.parseInt(mapaArticulosNuevo.get("cantidad_"+j).toString()))
						{
							modificado = true;
							i = this.numArticulos;
							j = numArticulosNuevo;
						}
						else
						{
							encontrado = true;
						}
						
					}	
				}
				
				//si el articulo no esta en el mapa nuevo quiere decir que hubo modificacion
				if(encontrado == false)
				{
					modificado = true;
					i = this.numArticulos;
				}
			}
		}
		
		if(modificado)
		{
			mapa.put("modificado", "true");
			mapa.put("mapaServicios",this.mapaServicios);
			mapa.put("numServicios",this.numServicios+"");
			mapa.put("mapaArticulos",this.mapaArticulos);
			mapa.put("numArticulos",this.numArticulos);
		}
		else
			mapa.put("modificado", "false");
		
		return mapa;
	}
	
	/**
	 * Método que obtiene el número de servicios que se van a eliminar
	 * @param mapa
	 * @param numRegistros
	 * @return
	 */
	private int obtenerNumRegistrosAIngresar(HashMap mapa, int numRegistros) 
	{
		int cantidad = 0;
		
		for(int i=0;i<numRegistros;i++)
		{
			if(!UtilidadTexto.getBoolean(mapa.get("eliminar_"+i).toString()))
				cantidad ++;
		}
		
		return cantidad;
	}
	
	
	/**
	 * Método que consulta los paquetes de materiales quirúrgicos
	 * @param con
	 * @param institucion
	 * @param conProcedimientos : se consultan solo los paquetes que tenga procedimientos asociados
	 * @param sinProcedimientos : se consultan solo los paquetes que no tenga procedimientos asociados
	 * @return
	 */
	public static HashMap<String, Object> consultarPaquetesMaterialesQx (Connection con,int institucion,boolean conProcedimientos,boolean sinProcedimientos)
	{
		return hojaDao().consultarPaquetesMaterialesQx(con, institucion, conProcedimientos, sinProcedimientos);
	}
	
	
	/**
	 * Método quie realiza la insercion/modificacion/eliminacion de los registros de paquetes de materiales quirurgicos
	 * @param con
	 * @param listado
	 * @param listadoEliminacion
	 * @param numRegistros
	 * @param numRegistroEliminacion
	 * @param usuario
	 * @return
	 */
	public static ActionErrors guardarPaquetes(Connection con,HashMap<String, Object> listado,HashMap<String, Object> listadoEliminacion,int numRegistros,int numRegistroEliminacion,UsuarioBasico usuario)
	{
		ActionErrors errores = new ActionErrors();
		HashMap campos = new HashMap();
		UtilidadBD.iniciarTransaccion(con);
		
		//***********INSERCION Y MODIFICACION ***********************************************
		//Iteración de cada paquete
		for(int i=0;i<numRegistros;i++)
		{
			campos.put("consecutivo",listado.get("consecutivo_"+i));
			campos.put("codigo",listado.get("codigo_"+i));
			campos.put("descripcion",listado.get("descripcion_"+i));
			campos.put("institucion",usuario.getCodigoInstitucion());
			campos.put("usuario",usuario.getLoginUsuario());
			
			if(listado.get("consecutivo_"+i).toString().equals(""))
			{
				//Se realiza la inserción del paquete
				if(hojaDao().insertarPaqueteMaterialesQx(con, campos)<=0)
					errores.add("", new ActionMessage("errors.noSeGraboInformacion","DEL REGISTRO Nro "+(i+1)));
			}
			else
			{
				//Se consulta la informacion actual del paquete
				HashMap paqueteOriginal = hojaDao().consultarPaqueteMaterialesQx(con, campos);
				
				//Se realiza la modificacion del paquete
				if(hojaDao().modificarPaqueteMaterialesQx(con, campos)>0)
				{
					//Se verifica si el paquete fue modificado
					if(!listado.get("descripcion_"+i).toString().equals(paqueteOriginal.get("descripcion_0").toString()))
					{
						//se genera log de modificacion
						String[] indices = {"consecutivo_","codigo_","descripcion_","relleno"};
						Utilidades.generarLogGenerico(listado, paqueteOriginal, usuario.getLoginUsuario(), false, i, ConstantesBD.logPaquetesMaterialesQxCodigo, indices);
					}
				}
				else
					errores.add("", new ActionMessage("errors.noSeGraboInformacion","DEL REGISTRO Nro "+(i+1)));
			}
		}
		//************************************************************************************
		//************ELIMINACION*************************************************************
		//Iteracion de los paquetes eliminados
		for(int i=0;i<numRegistroEliminacion;i++)
		{
			if(hojaDao().eliminarPaqueteMaterialesQx(con, listadoEliminacion.get("consecutivo_"+i).toString())>0)
			{
				//se genera log de eliminacion
				String[] indices = {"consecutivo_","codigo_","descripcion_","relleno"};
				Utilidades.generarLogGenerico(listadoEliminacion, null, usuario.getLoginUsuario(), true, i, ConstantesBD.logPaquetesMaterialesQxCodigo, indices);
			}
			else
				errores.add("", new ActionMessage("errors.noSeEliminoInformacion","DEL REGISTRO Nro "+(i+1)));
		}
		//*************************************************************************************
		
		if(errores.isEmpty())
			UtilidadBD.finalizarTransaccion(con);
		else
			UtilidadBD.abortarTransaccion(con);
		return errores;
	}
	
	/**
	 * Método que consulta los articulos de un paquete de materiales quirúrgicos
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static HashMap<String, Object> consultarArticulosXConsecutivo(Connection con,String consecutivo)
	{
		return hojaDao().consultarArticulosXConsecutivo(con, consecutivo);
	}
	
	/**
	 * Método que consulta los servicios del paquete material quirúrgico
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static HashMap<String, Object> consultarServiciosXConsecutivo(Connection con,String consecutivo)
	{
		return hojaDao().consultarServiciosXConsecutivo(con, consecutivo);
	}
	
	
	/**
	 * Método implementado para insertar/modificar/eliminar articulos de un paquete
	 * @param con
	 * @param articulos
	 * @param numArticulos
	 * @param usuario
	 * @param consecutivo
	 * @return
	 */
	public static ActionErrors guardarArticulos(Connection con,HashMap articulos,int numArticulos,UsuarioBasico usuario, String consecutivo)
	{
		ActionErrors errores = new ActionErrors();
		HashMap campos = new HashMap();
		
		UtilidadBD.iniciarTransaccion(con);
		
		//Iteración de los artículos
		for(int i=0;i<numArticulos;i++)
		{
			campos.put("consecutivo", consecutivo);
			campos.put("codigoArticulo",articulos.get("codigoArticulo_"+i));
			campos.put("cantidad",articulos.get("cantidad_"+i));
			
			//***********INSERCIÓN DE ARTÍCULO*******************************************
			if(articulos.get("consecutivo_"+i).toString().equals(""))
			{
				if(hojaDao().insertarArticulo(con, campos)<=0)
					errores.add("",new ActionMessage("errors.noSeGraboInformacion","DEL ARTICULO CON CÓDIGO "+campos.get("codigoArticulo")));
			}
			//*********************************************************************************
			//*********MODIFICACION DE ARTÍCULO**********************************************
			else if (!UtilidadTexto.getBoolean(articulos.get("eliminar_"+i).toString()))
			{
				//Se verifica si hubo modificacion
				if(!articulos.get("cantidad_"+i).toString().equals(articulos.get("cantidadOriginal_"+i).toString()))
				{
					if(hojaDao().modificarArticulo(con, campos)>0)
					{
						articulos.put("consecutivoPaquete_"+i,consecutivo);
						//generar log
						HashMap mapaOriginal = new HashMap();
						mapaOriginal.put("consecutivoPaquete_0",consecutivo);
						mapaOriginal.put("codigoArticulo_0",articulos.get("codigoArticulo_"+i));
						mapaOriginal.put("descripcionArticulo_0",articulos.get("descripcionArticulo_"+i));
						mapaOriginal.put("cantidad_0",articulos.get("cantidadOriginal_"+i));
						
						String[] indices = {"consecutivoPaquete_","codigoArticulo_","descripcionArticulo_","cantidad_","relleno"};
						Utilidades.generarLogGenerico(articulos, mapaOriginal, usuario.getLoginUsuario(), false, i, ConstantesBD.logPaquetesMaterialesQxCodigo, indices);
					}
					else
						errores.add("", new ActionMessage("errors.noSeGraboInformacion","DEL ARTICULO CON CÓDIGO "+campos.get("codigoArticulo")));
				}
			}
			//*********************************************************************************
			//*********ELIMINACIÓN DE ARTÍCULO************************************************
			else
			{
				if(hojaDao().eliminarArticulo(con, campos)>0)
				{
					articulos.put("consecutivoPaquete_"+i,consecutivo);
					//generar log
					String[] indices = {"consecutivoPaquete_","codigoArticulo_","descripcionArticulo_","cantidad_","relleno"};
					Utilidades.generarLogGenerico(articulos, null, usuario.getLoginUsuario(), true, i, ConstantesBD.logPaquetesMaterialesQxCodigo, indices);
				}
				else
					errores.add("", new ActionMessage("errors.noSeEliminoInformacion","DEL ARTICULO CON CÓDIGO "+campos.get("codigoArticulo")));
			}
			//********************************************************************************
		}
		
		if(errores.isEmpty())
			UtilidadBD.finalizarTransaccion(con);
		else
			UtilidadBD.abortarTransaccion(con);
		
		return errores;
	}
	
	/**
	 * Método implementado para insertar/modificar/eliminar servicios de un paquete
	 * @param con
	 * @param servicios
	 * @param numServicios
	 * @param usuario
	 * @param consecutivo
	 * @return
	 */
	public static ActionErrors guardarServicios(Connection con,HashMap servicios,int numServicios,UsuarioBasico usuario, String consecutivo)
	{
		ActionErrors errores = new ActionErrors();
		HashMap campos = new HashMap();
		
		UtilidadBD.iniciarTransaccion(con);
		
		//Iteración de los servicios
		for(int i=0;i<numServicios;i++)
		{
			campos.put("consecutivo", consecutivo);
			campos.put("codigoServicio",servicios.get("codigo_"+i));
			
			//***********INSERCIÓN DE SERVICIO*******************************************
			if(!UtilidadTexto.getBoolean(servicios.get("existeBd_"+i).toString()))
			{
				if(hojaDao().insertarServicio(con, campos)<=0)
					errores.add("",new ActionMessage("errors.noSeGraboInformacion","DEL SERVICIO "+servicios.get("descripcionServicio_"+i)));
			}
			//*********************************************************************************
			//*********ELIMINACION DE SERVICIO**********************************************
			else if (UtilidadTexto.getBoolean(servicios.get("eliminar_"+i).toString()))
			{
				if(hojaDao().eliminarServicio(con, campos)>0)
				{
					servicios.put("consecutivoPaquete_"+i,consecutivo);
					//generar log
					String[] indices = {"consecutivoPaquete_","descripcionServicio_","relleno"};
					Utilidades.generarLogGenerico(servicios, null, usuario.getLoginUsuario(), true, i, ConstantesBD.logProcedimientosPaquetesQuirugicosCodigo, indices);
				}
				else
					errores.add("", new ActionMessage("errors.noSeEliminoInformacion","DEL SERVICIO "+servicios.get("descripcionServicio_"+i)));
			}
			
		}
		
		if(errores.isEmpty())
			UtilidadBD.finalizarTransaccion(con);
		else
			UtilidadBD.abortarTransaccion(con);
		
		return errores;
	}
	
	
	/**
	 * Método que realiza la busqueda avanzada de los paquetes materiales Qx.
	 * @param con
	 * @param consecutivosPaquetesInsertados 
	 * @param campos
	 * @return
	 */
	public static HashMap busquedaGenericaPaquetesMateriales(Connection con,int institucion,String codigoServicio,String parejasClaseGrupo, String consecutivosPaquetesInsertados)
	{
		HashMap campos = new HashMap();
		campos.put("codigoInstitucion", institucion+"");
		campos.put("codigoServicio", codigoServicio);
		campos.put("parejasClaseGrupo",parejasClaseGrupo);
		
		HashMap paquetes = hojaDao().busquedaGenericaPaquetesMateriales(con, campos);
		
		//Se verifica si el paquete ya se insertó
		String[] paqueteInsertado = consecutivosPaquetesInsertados.split(",");
		boolean insertado = false;
		for(int i=0;i<Integer.parseInt(paquetes.get("numRegistros").toString());i++)
		{
			insertado = false;
			
			if(!consecutivosPaquetesInsertados.equals(""))
			{
				for(int j=0;j<paqueteInsertado.length;j++)
				{
					if(Integer.parseInt(paquetes.get("consecutivo_"+i).toString())==Integer.parseInt(paqueteInsertado[j]))
					{
						insertado = true;
						paquetes.put("insertado_"+i, ConstantesBD.acronimoSi);
					}
				}
			}
			
			if(!insertado)
				paquetes.put("insertado_"+i, ConstantesBD.acronimoNo);
		}
		
		return paquetes;
	}
	//****************************************************************************
	//********************GETTERS & SETTERS**************************************************
	
	/**
	 * @return the mapaArticulos
	 */
	public HashMap getMapaArticulos() {
		return mapaArticulos;
	}
	/**
	 * @param mapaArticulos the mapaArticulos to set
	 */
	public void setMapaArticulos(HashMap mapaArticulos) {
		this.mapaArticulos = mapaArticulos;
	}
	/**
	 * @return the mapaServicios
	 */
	public HashMap getMapaServicios() {
		return mapaServicios;
	}
	/**
	 * @param mapaServicios the mapaServicios to set
	 */
	public void setMapaServicios(HashMap mapaServicios) {
		this.mapaServicios = mapaServicios;
	}
	/**
	 * @return the numArticulos
	 */
	public int getNumArticulos() {
		return numArticulos;
	}
	/**
	 * @param numArticulos the numArticulos to set
	 */
	public void setNumArticulos(int numArticulos) {
		this.numArticulos = numArticulos;
	}
	/**
	 * @return the numServicios
	 */
	public int getNumServicios() {
		return numServicios;
	}
	/**
	 * @param numServicios the numServicios to set
	 */
	public void setNumServicios(int numServicios) {
		this.numServicios = numServicios;
	}
	
	
}
