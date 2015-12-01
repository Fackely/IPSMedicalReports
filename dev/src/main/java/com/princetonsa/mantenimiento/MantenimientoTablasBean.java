package com.princetonsa.mantenimiento;

import java.io.IOException;
import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.MantenimientoTablasDao;

	
/**
 * Clases que brinda la funcionalidad para manejar las tablas de 
 * mantenimiento
 * @author Diegon Andrés Ramírez <dramirez@princetonsa.com>
 */
public class MantenimientoTablasBean {
	
	private MantenimientoTablasDao mtDao;

	private static Logger logger = Logger.getLogger(MantenimientoTablasBean.class);
	/**
	 * Vector que contiene las tablas de mantenimiento activas en el sistema
	 */
	private Vector tablas;

	/**
	 * Conexin con la base de datos
	 */
	private Connection conexion;

	/**
	 * Contexto en el que esta instalada la aplicacin,
	 * con respecto al cuï¿½ se buscara el archivo de configuracin
	 */
	private String root;
	
	/**
	 * Nombre del archivo del que se obtendran la promiedades de las tablas de
	 * mantenimiento.<br>
	 * Por defecto tablasMantenimiento.xml
	 */
	private String archivo;
	
	/**
	 * Objeto que maneja en memoria el archivo xml
	 */
	private Document document;
	
	/**
	 * Login del usuario activo en el sistema.
	 */
	private String logginUsuario;
	
	/**
	 * Login del usuario activo en el sistema.
	 */
	private String institucionUsuario;

	/*/**
	 * Statement para la ejecucin de consultas
	 */
	//private PreparedStatementDecorator pstm;
	
	/**
	 * Variable para determinar si el archivo xml ya ha sido cargado
	 */
	private boolean loaded = false;
	
	/**
	 * Variable para almacenar el codigo del error SQL presentado
	 */
	private String estadoError = "";
	
	/**
	 * Constructora de la clase
	 * @see java.lang.Object#Object()
	 */
	public MantenimientoTablasBean() {
		this.root = "axioma";
		this.archivo = "tablasMantenimiento.xml";
		this.tablas = new Vector();
		
    }

	/**
	 * Constructora de la clase
	 * @param aArchivo Nombre del archivo del que es obtendrï¿½la configuracin
	 */
	public MantenimientoTablasBean(String aArchivo) {
		this.root = "axioma";
		this.archivo = aArchivo;
		this.tablas = new Vector();
	}

	/**
	 * Permite obtener los nombres de las tablas de mantenimiento que se puede
	 * manejar
	 * @return Un Arreglo de  Strings que contienen los nombres de mostrar de
	 * las tablas de mantenimiento disponibles a editar
	 */
	public String[] getTablasMantemiento() {
		String resp [] = new String[this.tablas.size()];
		for (int i = 0; i < resp.length; i++) {
			resp[i] = ((TablaMantenimiento )this.tablas.elementAt(i)).getNombreMostrar(); 	
		}  
		return resp;
	}

	/**
	 * Permite obtener los atributos de edición de una tabla dada
	 * @return Un String[] con los atributos editable de la tabla
	 * @param aTabla Nombre de la tabla de la cual se quieren obtener los
	 * encabezados
	 */
	public EncabezadoTupla[] getAtributosTabla(String aTabla) {
		TablaMantenimiento tabla = this.buscarTabla(aTabla);
		if(tabla!=null){
			Vector atributos = tabla.getAtributos();
			EncabezadoTupla ratributos [] = new EncabezadoTupla[atributos.size()];
			for (int i = 0; i < ratributos.length; i++) {
				ratributos[i] = (EncabezadoTupla) atributos.elementAt(i);
			}
			return ratributos;
		}
		return null;
	}

	/**
	 * Ejecuta una operación de update en la tabla especificada con los datos
	 * especificados
	 * @param aTabla Tabla en la que se quiere ejecutar el update
	 * @param aValores Valores que se quiere asignar<br>
	 * <b>Nota:</b> Para facilidad de uso este arreglo de valores debe  contener
	 * un valor para la totalidad de los atributos de la tabla especificada, cambiando los
	 * valores de los campos deseados
	 * @param aCondiciones Condiciones para buscar el objeto que se quiere
	 * editar
	 * @return true si se pudo realizar la operacion
	 */
	public boolean update(String aTabla,String[] aValores,Object[] aCondiciones) {
		TablaMantenimiento t = this.buscarTabla(aTabla);
		String dato1="", dato2="";

		//aqui formatear de app a BD fecha
		if(t==null)
		   return false;
		
		String validar = "SELECT count(*) as contador FROM " + t.getNombre() + " WHERE " ;
		String query = "UPDATE " + t.getNombre() + " SET ";

		EncabezadoTupla [] atributos = this.getAtributosTabla(aTabla);
		
		if(atributos.length!= aValores.length) // El numero de valores a asignar no es igual al numero de atributos de la tabla
		  return false;

		//logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

		for (int i = 0; i < atributos.length; i++) {
			if(i!=0&&aValores[i]!=null) {
				query += ",";
				validar += " AND ";
			}

			{
				if(aValores[i]!=null) {
					if(aValores[i].equals("true")) {
						logger.info("true<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"+atributos[i].getNombre());
						query+=atributos[i].getNombre()+"= "+ValoresPorDefecto.getValorTrueParaConsultas();
					}
					else if(aValores[i].equals("false")) {
						logger.info("false<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"+atributos[i].getNombre());
						query+=atributos[i].getNombre()+"='f' ";
					}
					else {
						logger.info("else<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"+atributos[i].getNombre()+"='"+aValores[i]+"' ");
						if(atributos[i].getNombre().equals("nombre"))
						{
							dato1 = atributos[i].getNombre()+"='"+aValores[i]+"'";
						}
						query += atributos[i].getNombre()+"='"+aValores[i]+"' ";
						validar += atributos[i].getNombre()+"='"+aValores[i]+"' ";
					}
				}
			}
		}

		if(aCondiciones.length>0){
			query+=" WHERE ";
		}
		//logger.info("<<Longit: " + aCondiciones.length);
		
		for (int i = 0; i < aCondiciones.length; i++) {
			if(i!=0 && !aCondiciones[i].toString().equals("")) 
				query+=" AND ";
			if(!aCondiciones.toString().equals(""))
			{
				logger.info("OTRO  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"+aCondiciones[i].toString());
			    dato2=aCondiciones[i].toString();
				query+=aCondiciones[i].toString();
			}
		}
		

		logger.info("Validar: " + validar);
		logger.info("Update: " + query);
		
		try {
			int cual;
			cual = mtDao.buscarDatosTabla(conexion, validar);
					
			logger.info("devolvio la validacion: " + cual);
			
			//se verifica ke los nuevos datos no esten almacenados
			if(cual == 0) {
				try {
					if(mtDao.updateDatosTabla(conexion,query) > 0){
						return true;
					}
				} 
				catch (SQLException e) {
					logger.warn("Error en el update "+ e);
				}
			}
			else
			{
				if(dato1.equals(dato2))
				{
					logger.info("tienen el mismo nombre: ");
					return true;
				}
				 
				return false;
			}	
		}
		catch (SQLException e) {
			logger.warn("Error en el select: "+ e);
		}
		return false;
	}

	
	/**
	 * 
	 * @param con
	 * @param nombreEspecialidad
	 * @return
	 */
	public static boolean existeEspecialidad(Connection con, String nombreEspecialidad)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMantenimientoTablasDao().existeEspecialidad(con, nombreEspecialidad);
	}
	
	/**
	 * 
	 * @param con
	 * @param nombreOcupacion
	 * @return
	 */
	public static boolean existeOcupacionMedica(Connection con, String nombreOcupacion)
	{
		logger.info("Entro a validqad Ocupacion Medica: Ocupacion " + nombreOcupacion);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMantenimientoTablasDao().existeOcupacionMedica(con, nombreOcupacion);
	}
	
	/**
	 * 
	 * @param con
	 * @param excepcionAgenda
	 * @return
	 */
	public static boolean existeExcepcionAgenda(Connection con, String fecha, String centroAtencion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMantenimientoTablasDao().existeExcepcionAgenda(con, fecha, centroAtencion);
	}
	
	/**
	 * 
	 * @param con
	 * @param consultaPermitida
	 * @return
	 */
	public static boolean existeInterconsultaPermitida(Connection con, String ocupacion, String especialidad )
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMantenimientoTablasDao().existeInterconsultaPermitida(con, ocupacion, especialidad);
	}
	
	
	 
	/**
	 * Inserta un nuevo elemento en la tabla especificada
	 * @param aTabla La tabla en la que se quiere realizar la operacion
	 * @param aEncabezado Información de las tuplas de esta tabla
	 * @param aValores Valores que se quieren insertar
	 * @return true si se pudo realizar el insert
	 */
	public boolean insert(String aTabla,String[] aValores) {
		TablaMantenimiento t = this.buscarTabla(aTabla);
		if(t==null)
			return false;
		
		Vector atributos = t.getAtributos();	
		//aqui formatear de app a bd fecha
		String query = "INSERT INTO "+t.getNombre()+" (";
		for(int i =0 ; i < atributos.size(); i++){
			if(i!=0) query+=" , ";
			query+=((EncabezadoTupla)atributos.elementAt(i)).getNombre();
		}
		query+=")  VALUES (";
		for (int i = 0; i < aValores.length; i++) {
			if(i!=0) query+=" , ";
			if(aValores[i]!=null)
			{
				if(aValores[i].equals("false"))
				{
					query+= "'f'";
				}
				else if(aValores[i].equals("true"))
				{
					query+= ""+ValoresPorDefecto.getValorTrueParaConsultas();
				}
				else
				{
					String tabla=t.getNombre();
					String campo=((EncabezadoTupla)atributos.elementAt(i)).getNombre();
					if((tabla.equals("clase_inventario"))&&(campo.equals("institucion")))
					{
						query+=this.institucionUsuario;
					}
					else
					{
						query+="'"+aValores[i]+"'";
					}
				}
			}
			else
				query+="null";
		} 
		query+=" )";
		try {
			if(mtDao.updateDatosTabla(conexion,query)>0){
				return true;
			}
			/*this.pstm = this.conexion.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	
			if(this.pstm.executeUpdate(query)>0)
			  return true;*/
		} 
		catch (SQLException e) {
			logger.warn("Error en la Insersion  "+ e.getSQLState());
			this.setEstadoError(e.getSQLState());
			
		
		}
		return false;
	}

	/**
	 * Elimina uno o varios elementos de la tabla especificada; todos aquellos
	 * que cumplan con las condiciones especificadas
	 * @param aTabla Tabla de la que se quiere eliminar los elementos
	 * @param aCondiciones Arreglo de String que contienen las condiciones de
	 * los elementos que quieren ser borrados, expresados asi:
	 * <ol>
	 * <li>columna=valor</li>
	 * </ol>
	 * <b>Nota:</b>Si la condicÃ³n involucra cadenas estas deben ser colocadas
	 * entre comillas.
	 * @return boolean
	 */
	public boolean delete(String aTabla, Object[] aCondiciones){
		TablaMantenimiento t = this.buscarTabla(aTabla);
		if(t==null){
			return false;
		}
		String query = "DELETE FROM "+t.getNombre();
		//aqui formatear condiciones si son fechas, revisar delete
		if(aCondiciones.length>0){
			query+=" WHERE ";
			for (int i = 0; i < aCondiciones.length; i++)
			{
				if(i<(aCondiciones.length) && i!=0){
					 query+=" AND "; 
				}
				query+=aCondiciones[i].toString();
			}
		}

		try {
			if(mtDao.updateDatosTabla(conexion,query)>0){
				return true;
			}

		} 
		catch (SQLException e) {

			e.printStackTrace();
		}
	    return false;
	}
	
	/**
	 * Inicializa los datos del bean con la información de las tablas de
	 * mantenimiento que se van a usar
	 */
	public void init() throws Exception {
		if(!this.loaded){ // Si no han sido cargados las tablas se cargan del archivo especificado
			this.tablas.removeAllElements();
			Properties p = System.getProperties();
			String path = this.archivo;
			String ps = p.getProperty("file.separator");
			/*
			if(p.getProperty("tmantenimiento.dir")!=null){
				path = p.getProperty("tmantenimiento.dir")+this.archivo;
			}
			else if(p.getProperty("catalina.home")!=null){
				path = p.getProperty("catalina.home")+ps+"webapps"+ps+this.root+ps+"WEB-INF"+ps+this.archivo;
			}
			*/
			path=ValoresPorDefecto.getDirectorioAxiomaBase()+ps+"WEB-INF"+ps+this.archivo;
			try{
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				dbf.setValidating(true);
				DocumentBuilder db = dbf.newDocumentBuilder();
				this.document = db.parse(path);
				this.loadTablas();
				this.loaded = true; 						
			}
			catch(IOException io){
				logger.warn("Error de entrada salida" + io);
				
				
			}
			catch(ParserConfigurationException pe){
				logger.warn("Error de validacion del dtd" + pe);
				
				
			}
			catch(SAXException se){
				logger.warn("Error de SAX " + se);
				
				
			}
			catch(Exception io){
				logger.warn("Error de exception " + io);
				
				
			}
		}
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		if (myFactory != null)
		{
			mtDao = myFactory.getMantenimientoTablasDao();
		}
	}

	/**
	 * Carga los objetos tablas de Mantenimiento de un documento xml
	 */
	private void loadTablas() throws Exception
	{
		//aqui incluir nuevas restricciones en la seleccion
		Element tablasm = (Element)this.document.getLastChild();
		NodeList nl = tablasm.getElementsByTagName("tabla");
		for (int i = 0; i < nl.getLength(); i++) {
			TablaMantenimiento tm = new TablaMantenimiento(); 
			Element tabla = (Element) nl.item(i);

			if(tabla.getAttribute("eliminable").equals("true")){
				tm.setEliminable(true);
			}else{
				tm.setEliminable(false);
			}
			if(tabla.getAttribute("editable").equals("true")){
				tm.setEditable(true);
			}else{
				tm.setEditable(false);
			}
			
			Text tnombre = (Text)(tabla.getElementsByTagName("nombre")).item(0).getFirstChild();
			tm.setNombre(tnombre.getData());
			
			Text tnombrem = (Text)(tabla.getElementsByTagName("nombre-mostrar")).item(0).getFirstChild();
			tm.setNombreMostrar(tnombrem.getData());
			
			Element atributos = (Element)(tabla.getElementsByTagName("atributos")).item(0);
			NodeList anl = atributos.getElementsByTagName("atributo");
			for (int j = 0; j < anl.getLength(); j++) 
			{
				EncabezadoTupla et = new EncabezadoTupla();
				Element atributo = (Element) anl.item(j);
				Text anombre = (Text)(atributo.getElementsByTagName("nombre-atributo")).item(0).getFirstChild();
				et.setNombre(anombre.getData());
				
				NamedNodeMap atAtributo = atributo.getAttributes(); // Se obtienen los atributos del tag atributo 
				boolean ed = (atAtributo.getNamedItem("editable").getNodeValue().equals("true"))?true:false;
				et.setEditable(ed);
				
				boolean req = (atAtributo.getNamedItem("requerido").getNodeValue().equals("true"))?true:false;
				et.setRequerido(req);
				
				Node n=atAtributo.getNamedItem("booleano");
				if(n!=null){
					boolean booleano = (n.getNodeValue().equals("true"))?true:false;
					et.setBooleano(booleano);
				}
				
				Element nombreAlias = (Element) (atributo.getElementsByTagName("alias") ).item(0);
				if (nombreAlias!=null)
				{
					et.setAlias(nombreAlias.getFirstChild().getNodeValue());
				}
				
				Element adepende = (Element)(atributo.getElementsByTagName("depende")).item(0);
				if( adepende!=null){ // Si hay depencias
					NamedNodeMap dAtributos = adepende.getAttributes();
					 et.setTablaDepende(dAtributos.getNamedItem("tabla").getNodeValue());
					 et.setColumnaDepende(dAtributos.getNamedItem("columna").getNodeValue());
					 if(dAtributos.getNamedItem("orden")!=null)
					 {
					 	et.setOrden(dAtributos.getNamedItem("orden").getNodeValue());
					 }
					 Node cMostrar = dAtributos.getNamedItem("columnaMostrar");
					 
					 if (cMostrar==null){
					 	et.setColumnaDependeMostrar(et.getColumnaDepende());
					 }
					 else{
						et.setColumnaDependeMostrar(cMostrar.getNodeValue());
					 }
					 
				}				

				tm.addAtributo(et);
			}
			this.tablas.addElement(tm);			
		}
	}


	/**
	 * Obtiene los datos de la tabla especificada
	 * @param aTabla Nombre de la tabla de mantenimiento de l que se quieren obtener
	 * los datos
	 * @return Una matriz con las tuplas de los datos de la tabla
	 */
	public String[][] getDatosTabla(String aTabla) {
		TablaMantenimiento t = buscarTabla(aTabla);
		if(t==null)
		  return null;
		
		int col = 0;
		Vector datos = new Vector();
		String [][] resp = null;
		String [] row;
		String nombreAtributo = "";
		String query = "SELECT ";
		Vector atributos = t.getAtributos();
		for(int i=0; i < atributos.size();i++)
		{
			if(i!=0) 
				query+=" , ";
			
			nombreAtributo = ((EncabezadoTupla)atributos.elementAt(i)).getNombre();
			if(t.getNombre().equals("excepciones_agenda")&&nombreAtributo.equals("fecha"))
			{
				nombreAtributo = "to_char("+nombreAtributo+", 'yyyy-mm-dd') as columna_"+i;
			}else
				nombreAtributo = ((EncabezadoTupla)atributos.elementAt(i)).getNombre()+" as columna_"+i;
			query+=nombreAtributo;
		}
		
		query+=" FROM  "+ t.getNombre();
		query+=" ORDER BY ";
		for(int i=0; i < atributos.size();i++){
			if(i!=0) query+=" , ";
			query+=((EncabezadoTupla)atributos.elementAt(i)).getNombre();
		}
		
		
		
		try{
			HashMap result = new HashMap(); 
			result=mtDao.getDatosTabla(this.conexion, query);
			col=atributos.size();
			for(int j=0; j<Utilidades.convertirAEntero(result.get("numRegistros").toString());j++)
			{
				row = new String[col];
				for(int i=0;i<col;i++)
				{
				    
				    try
				    {
				        row[i] = result.get("columna_"+i+"_"+(j)).toString();
				    }catch (NullPointerException e)
				    {
				        row[i]="";
				    }    
				        
					//aqui verificar formato fecha de BD  a aplicacion
				}
				
				
				datos.addElement(row);
			}
			
			resp = new String[datos.size()][col];
			for (int i = 0; i < datos.size(); i++) {
				row = (String[]) datos.elementAt(i);
				resp[i]=row;
			}
		}
		catch(SQLException se){
			logger.warn("Error en la seleccion "+ se);
			se.printStackTrace();
			
		}
		
		return resp;
	}

	public String[] getDatosTabla(String aTabla, String aColumna){

		Vector datos = new Vector();
		String [] resp = null;
		String query = "SELECT coalesce("+aColumna+"||'',' ') as columna" ;
		query+=" FROM  "+ aTabla;

		try{
			if(this.conexion==null || this.conexion.isClosed())
				this.conexion = UtilidadBD.abrirConexion();
			HashMap result = new HashMap(); 
			result=mtDao.getDatosTabla(this.conexion, query);
			for(int j=0;j<Utilidades.convertirAEntero(result.get("numRegistros").toString());j++){
				//aqui verificar lo de fecha de bd a app
				datos.addElement(result.get("columna_"+j).toString());
			}
			resp = new String[datos.size()];
			for (int i = 0; i < datos.size(); i++) {
				resp[i]=(String) datos.get(i);
			}
		}
		catch(SQLException se){
			logger.warn("Error en la seleccion "+ se);
			se.printStackTrace();			
		}
		
		return resp;
		
	}

	/**
	 * Listar los datos de la columna ordenados
	 * @param aTabla
	 * @param aColumna
	 * @param orden
	 * @return
	 */
	public String[] getDatosTabla(String aTabla, String aColumna, String orden){

		Vector datos = new Vector();
		String [] resp = null;
		String query = "SELECT coalesce("+aColumna+"||'',' ') as columna" ;
		query+=" FROM  "+ aTabla + " ORDER BY "+orden;
		
		try{
			HashMap result = new HashMap(); 
			result=mtDao.getDatosTabla(this.conexion, query);
			for(int j=0;j<Utilidades.convertirAEntero(result.get("numRegistros").toString());j++){
				//aqui verificar lo de fecha de bd a app
				datos.addElement(result.get("columna_"+j).toString());
			}
			resp = new String[datos.size()];
			for (int i = 0; i < datos.size(); i++) {
				resp[i]=(String) datos.elementAt(i);
			}
		}
		catch(SQLException se){
			logger.warn("Error en la seleccion "+ se);
			se.printStackTrace();			
		}
		
		return resp;
		
	}

	/**
	 * Retorna la conexin activa con la base de datos
	 * @return La conexin con la base de datos 
	 */
	public Connection getConexion() {
		return conexion;
	}

	/**
	 * Permite asignar una nueva  conexin con la base de datos
	 * @param Conexin activa con la base de datos
	 */
	public void setConexion(Connection aConexion) {
		conexion = aConexion;
	}

	/**
	 * Permite asignar el nombre del archivo desde el que se carga la
	 * configuracin de las tablas de mantenimiento
	 * @param archivo Nombre del archivo
	 */
	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}

	/**
	 * Permite obtener el nombre del archivo del que se carga la configuracin 
	 * @return String Nombre del archivo
	 */
	public String getArchivo() {
		return archivo;
	}
	
	/**
	 * Busca la tabla de mantenimiento correspondiente al nombre especificado
	 * @param aTabla Nombre de la tabla
	 * @return TablaMantenimiento El objeto que contiene los datos de la tabla
	 * especificada o null si no la encuentra.
	 */
	public TablaMantenimiento buscarTabla(String aTabla){
		for (int i = 0; i < this.tablas.size(); i++) {
			TablaMantenimiento t = (TablaMantenimiento) this.tablas.elementAt(i);
			if(t.getNombre().equalsIgnoreCase(aTabla) || t.getNombreMostrar().equalsIgnoreCase(aTabla)){
				return t;
			}
			
		}
		return null;
	}

	/**
	 * Permite inicializar el directorio base donde se encuentra el archivo de
	 * configuracin
	 * @param root Nombre del directorio base
	 */
	public void setRoot(String root) {
		this.root = root;
	}

	/**
	 * Permite obtener el nombre del directorio base donde se encuentra el
	 * archivo de configuracin.
	 * @return String Nombre del directorio base.
	 */
	public String getRoot() {
		return root;
	}
    
	/**
	 * @return Retorna el logginUsuario.
	 */
	public String getLogginUsuario()
	{
		return logginUsuario;
	}
	/**
	 * @param Asigna logginUsuario para logginUsuario. 
	 */
	public void setLogginUsuario(String logginUsuario)
	{
		this.logginUsuario = logginUsuario;
	}
	/**
	 * @return Retorna el institucionUsuario.
	 */
	public String getInstitucionUsuario()
	{
		return institucionUsuario;
	}
	/**
	 * @param Asigna institucionUsuario para institucionUsuario. 
	 */
	public void setInstitucionUsuario(String institucionUsuario)
	{
		this.institucionUsuario = institucionUsuario;
	}

	/**
	 * @return Returns the estadoError.
	 */
	public String getEstadoError() {
		return estadoError;
	}

	/**
	 * @param estadoError The estadoError to set.
	 */
	public void setEstadoError(String estadoError) {
		this.estadoError = estadoError;
	}
}
