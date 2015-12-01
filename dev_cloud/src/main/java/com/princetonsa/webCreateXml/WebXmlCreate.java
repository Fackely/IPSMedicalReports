/*
 * @(#)WebXmlCreate.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.5.0_06
 *
 */
package com.princetonsa.webCreateXml;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.axioma.util.log.Log4JManager;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * Clase para la generacion del web.xml desde la BD
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios</a>
 */
public class WebXmlCreate 
{
	/**
	 * listado de roles del sistema
	 */
	private Vector roles;
	
	/**
	 * Metodo que obtiene toda la informacion de los roles funcionalidades 
	 */
	private HashMap rolesFuncionalidades;

	/**
	 * web xml parseado a Document w3c
	 */
	private Document documentWebXmlParseado;
	
	/**
	 * file que contiene el webXml
	 */
	private File fileWebXml;
	
	/**
	 * Constructor 
	 */
	public WebXmlCreate() 
	{
		reset();
	}

	/**
	 * Llena el vector de los roles del sistema
	 * @param con
	 * @return
	 */
	public boolean llenarRolesSistema(Connection con)
	{
		this.roles=XconnectionDB.listadoRolesSistema(con);
		if(this.roles.size()>0)
			return true;
		else
			return false;
	}
	
	
	/**
     * Metodo que carga toda la informacion de roles funcionalidades para poder hacer el 
     * <security-constraint> del web.xml, este mapa tiene el codigo - nombre -path funcionalidad
     * y el detalle_ que es otro mapa encapsulado con la información de los roles que tienen esa func 
     * @param con
     * @return
     */
	public boolean cargarInformacionRolesFuncionalidades(Connection con)
	{
		this.rolesFuncionalidades= XconnectionDB.cargarInformacionRolesFuncionalidades(con);
		if(Integer.parseInt(this.rolesFuncionalidades.get("numRegistros").toString())>0)
			return true;
		else
			return false;	
	}
	
	/**
	 * resetea los atributos 
	 * del objeto
	 */
	public void reset()
	{
		this.roles= new Vector();
		this.rolesFuncionalidades= new HashMap();
		this.fileWebXml= null;
		this.documentWebXmlParseado=null;
	}
	
	/**
	 * Metodo que parsea el web.xml a un objeto de tipo
	 * Document (W3c) validando que cumpla con el DTD
	 * @param pathWebXmlTemp
	 * @return
	 */
	public boolean cargarDocument(String pathWebXml)
	{
		fileWebXml = new File(pathWebXml);
		DocumentBuilderFactory myDocumentFactory= DocumentBuilderFactory.newInstance();
		myDocumentFactory.setValidating(true);
		try 
		{
			DocumentBuilder documentBuilder= myDocumentFactory.newDocumentBuilder();
			this.documentWebXmlParseado=documentBuilder.parse(fileWebXml);
		} 
		catch (ParserConfigurationException e) 
		{
			Log4JManager.error("Error ParserConfigurationException: "+e.toString());
			return false;
		} 
		catch (SAXException e) 
		{
			Log4JManager.error("Error SAXException: "+e.toString());
			return false;
		} 
		catch (IOException e) 
		{
			Log4JManager.error("Error IOException: "+e.toString());
			return false;
			
		}
		return true;
	}
	
	
	/**
	 * Crea el segmento de roles y funcionalidades
	 * @return
	 */
	public boolean crearSegmentoRolesYRolesFuncionalidades()
	{
		Transformer transformer=null;
		TransformerFactory myTransformerFactory= TransformerFactory.newInstance();
		try
		{
			Element webApp= (Element) this.documentWebXmlParseado.getLastChild();
			
			this.borrarSegmentoRolesOld();
			DocumentFragment rolesXml=this.crearSegmentoRoles();
			rolesXml.normalize();
			webApp.appendChild(rolesXml);
			
			this.borrarSegmentoRolesFuncionalidadesOld();
			DocumentFragment rolesFuncionalidadesXml= this.crearSegmentoRolesFuncionalidades();
			rolesFuncionalidadesXml.normalize();
			NodeList nodeLoginConfig= webApp.getElementsByTagName("login-config");
			webApp.insertBefore(rolesFuncionalidadesXml, nodeLoginConfig.item(0));
		}
		catch (Exception e) 
		{
			Log4JManager.error("Error crearSegmentoRolesYRolesFuncionalidades: "+e.toString());
			return false;
		}
		try 
		{
			transformer= myTransformerFactory.newTransformer();
		} 
		catch (TransformerConfigurationException e) 
		{
			Log4JManager.error("Error TransformerConfigurationException= "+e.toString());
			return false;
		}
		
		transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN");
		transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "web-app_2.3.dtd");
		
		synchronized(fileWebXml)
		{
			DOMSource source=null;
			StreamResult streamResult=null;
			try
			{
				source= new DOMSource(this.documentWebXmlParseado);
				File temporalFileWebXml= new File(fileWebXml.getParent()+File.separator+"webGenerado.xml");
				streamResult= new StreamResult(temporalFileWebXml);
			}
			catch (Exception e) 
			{
				Log4JManager.error("Error crearSegmentoRolesYRolesFuncionalidades: "+e.toString());
				return false;
			}
			try 
			{
				transformer.transform(source, streamResult);
			} 
			catch (TransformerException e) 
			{
				Log4JManager.error("Error TransformerException= "+e.toString());
				return false;
			}
			
		}
		return true;
	}
	
	/**
	 * Crea el segmento de roles <security-role>
	 * @return
	 */
	public DocumentFragment crearSegmentoRoles()
	{
		DocumentFragment rolesXml= this.documentWebXmlParseado.createDocumentFragment();
		for(int i=0; i<this.roles.size(); i++)
		{
			Text enter= this.documentWebXmlParseado.createTextNode("\n");
			Element rolName;
			Element securityRol;
			Text txt;
			
			securityRol= this.documentWebXmlParseado.createElement("security-role");
			securityRol.appendChild(enter);
			
			rolName= this.documentWebXmlParseado.createElement("role-name");
			txt= this.documentWebXmlParseado.createTextNode(this.getRoles(i));
			rolName.appendChild(txt);
			
			securityRol.appendChild(rolName);
			securityRol.appendChild(enter);
			rolesXml.appendChild(securityRol);
		}
		return rolesXml;
	}
	
	
	/**
	 * borra el segmento de roles <security-role>
	 *
	 */
	public void borrarSegmentoRolesOld()
	{
		Element webApp= (Element) this.documentWebXmlParseado.getLastChild();
		NodeList nodeListRoles= webApp.getElementsByTagName("security-role");
		int num=nodeListRoles.getLength();
		
		for(int w=0; w<num; w++)
		{
			Node nodeRolToDelete=nodeListRoles.item(0);
			if(nodeRolToDelete!=null)
			{
				webApp.removeChild(nodeRolToDelete);
			}
		}
	}
	
	/**
	 * existe un segmento con paginas quemadas en el web.xml y es REQUERIDO
	 * adicionarlas para que no existan problemas al cargar el contexto. 
	 *
	 */
	public void addPaginasQuemadasAlMapa()
	{
		/* ESTE SEGMENTO ESTABA QUEMADO DESDE EL COMIENZO DE LA APLICACION
		<security-constraint><web-resource-collection><web-resource-name>0-menuPrincipal</web-resource-name><url-pattern>/mainMenu.jsp</url-pattern><http-method>POST</http-method><http-method>GET</http-method></web-resource-collection><auth-constraint><role-name>*</role-name></auth-constraint><user-data-constraint><transport-guarantee>CONFIDENTIAL</transport-guarantee></user-data-constraint>
		</security-constraint>
		<security-constraint><web-resource-collection><web-resource-name>0-paginasComunes</web-resource-name><url-pattern>/common/*</url-pattern><http-method>POST</http-method><http-method>GET</http-method></web-resource-collection><auth-constraint><role-name>*</role-name></auth-constraint><user-data-constraint><transport-guarantee>NONE</transport-guarantee></user-data-constraint>
		</security-constraint>*/
		HashMap detalleTemp= new HashMap();
		detalleTemp.put("numRegistros", "1");
		detalleTemp.put("nombreRol_0", "*");
		
		this.setRolesFuncionalidades("codigoFuncionalidad_-2", "0");
		this.setRolesFuncionalidades("nombreFuncionalidad_-2", "menuPrincipal");
		this.setRolesFuncionalidades("pathFuncionalidad_-2", "/mainMenu.jsp");
		this.setRolesFuncionalidades("isSSL_-2", "t");
		this.setRolesFuncionalidades("detalle_-2", detalleTemp);
		
		this.setRolesFuncionalidades("codigoFuncionalidad_-1", "0");
		this.setRolesFuncionalidades("nombreFuncionalidad_-1", "paginasComunes");
		this.setRolesFuncionalidades("pathFuncionalidad_-1", "/common/*");
		this.setRolesFuncionalidades("isSSL_-1", "f");
		this.setRolesFuncionalidades("detalle_-1", detalleTemp);
	}
	
	
	/**
	 * Crea el segmento de roles <security-role>
	 * @return
	 */
	public DocumentFragment crearSegmentoRolesFuncionalidades()
	{
		DocumentFragment rolesFuncionalidadesXml= this.documentWebXmlParseado.createDocumentFragment();
		this.addPaginasQuemadasAlMapa();
		String codigoNombreFunc="";
		String urlFunc="";
		//toca inicializar en -2 porque hay dos paginas de acceso quemadas
		for(int i=-2; i<Integer.parseInt(this.getRolesFuncionalidades("numRegistros").toString()); i++)
		{
			if(i>=0)
			{	
				urlFunc="/"+this.getRolesFuncionalidades("pathFuncionalidad_"+i).toString().split("/")[0]+"/*";
				codigoNombreFunc= 	this.getRolesFuncionalidades("codigoFuncionalidad_"+i).toString().trim()
									+"-"+
									Xutilidades.flattenString(this.getRolesFuncionalidades("nombreFuncionalidad_"+i).toString());
			}	
			else
			{	
				urlFunc=this.getRolesFuncionalidades("pathFuncionalidad_"+i).toString();
				codigoNombreFunc= 	this.getRolesFuncionalidades("codigoFuncionalidad_"+i).toString().trim()
				+"-"+
				this.getRolesFuncionalidades("nombreFuncionalidad_"+i);
			}	
			
			Text enter = this.documentWebXmlParseado.createTextNode("\n");
	        Element securityConstraint = this.documentWebXmlParseado.createElement("security-constraint");
	        securityConstraint.appendChild(enter);
			Element webResourceCollection = this.documentWebXmlParseado.createElement("web-resource-collection");
	        Element authConstraint = this.documentWebXmlParseado.createElement("auth-constraint");
			Element userDataConstraint = this.documentWebXmlParseado.createElement("user-data-constraint");

			Element element = this.documentWebXmlParseado.createElement("web-resource-name");
			Text txt = this.documentWebXmlParseado.createTextNode(codigoNombreFunc);
			element.appendChild(txt);
			webResourceCollection.appendChild(element);

			element = this.documentWebXmlParseado.createElement("url-pattern");
			txt = documentWebXmlParseado.createTextNode(urlFunc);
			element.appendChild(txt);
			webResourceCollection.appendChild(element);

			element = this.documentWebXmlParseado.createElement("http-method");
			txt = this.documentWebXmlParseado.createTextNode("POST");
			element.appendChild(txt);
			webResourceCollection.appendChild(element);

			element = this.documentWebXmlParseado.createElement("http-method");
			txt = this.documentWebXmlParseado.createTextNode("GET");
			element.appendChild(txt);
			webResourceCollection.appendChild(element);

			String roleName = "";
			HashMap detalleRoles=(HashMap)this.getRolesFuncionalidades("detalle_"+i);
			//se accede al detalle de los roles que tienen acceso a esta funcionalidad
			for(int w=0; w<Integer.parseInt(detalleRoles.get("numRegistros").toString()); w++)
			{
				roleName= detalleRoles.get("nombreRol_"+w).toString();
				element = this.documentWebXmlParseado.createElement("role-name");
				txt = this.documentWebXmlParseado.createTextNode(roleName);
				element.appendChild(txt);
				authConstraint.appendChild(element);
			}
			
			element = this.documentWebXmlParseado.createElement("transport-guarantee");
			boolean isSSLTemp= false;
			if(this.getRolesFuncionalidades("isSSL_"+i).equals("t") || this.getRolesFuncionalidades("isSSL_"+i).equals("true") || this.getRolesFuncionalidades("isSSL_"+i).equals("1"))
				isSSLTemp=true;
			txt = this.documentWebXmlParseado.createTextNode( isSSLTemp ? "CONFIDENTIAL" : "NONE");
	        element.appendChild(txt);
			userDataConstraint.appendChild(element);

			securityConstraint.appendChild(webResourceCollection);
			securityConstraint.appendChild(authConstraint);
			securityConstraint.appendChild(userDataConstraint);
			securityConstraint.appendChild(enter);
	        rolesFuncionalidadesXml.appendChild(securityConstraint);
			
		}
		return rolesFuncionalidadesXml;
	}
	
	
	/**
	 * borra el segmento de roles funcionalidades <security-constraint>
	 *
	 */
	public void borrarSegmentoRolesFuncionalidadesOld()
	{
		Element webApp= (Element) this.documentWebXmlParseado.getLastChild();
		NodeList nodeListRolesFuncionalidades= webApp.getElementsByTagName("security-constraint");
		int num=nodeListRolesFuncionalidades.getLength();
		
		for(int w=0; w<num; w++)
		{
			Node nodeRolesFuncionalidadesToDelete=nodeListRolesFuncionalidades.item(0);
			if(nodeRolesFuncionalidadesToDelete!=null)
			{
				webApp.removeChild(nodeRolesFuncionalidadesToDelete);
			}
		}
	}
	
	/**
	 * @return Returns the roles.
	 */
	public Vector getRoles() {
		return roles;
	}

	/**
	 * @param roles The roles to set.
	 */
	public void setRoles(Vector roles) {
		this.roles = roles;
	}

	/**
	 * @return Returns the roles.
	 */
	public String getRoles(int key) {
		return roles.get(key).toString();
	}

	/**
	 * @param roles The roles to set.
	 */
	public void setRoles(int index, String elemento) {
		this.roles.set(index, elemento);
	}

	/**
	 * @return Returns the documentWebXmlParseado.
	 */
	public Document getDocumentWebXmlParseado() {
		return documentWebXmlParseado;
	}

	/**
	 * @param documentWebXmlParseado The documentWebXmlParseado to set.
	 */
	public void setDocumentWebXmlParseado(Document documentWebXmlParseado) {
		this.documentWebXmlParseado = documentWebXmlParseado;
	}

	/**
	 * @return Returns the fileWebXml.
	 */
	public File getFileWebXml() {
		return fileWebXml;
	}

	/**
	 * @param fileWebXmlTemp The fileWebXmlTemp to set.
	 */
	public void setFileWebXml(File fileWebXmlTemp) {
		this.fileWebXml = fileWebXmlTemp;
	}

	/**
	 * @return Returns the rolesFuncionalidades.
	 */
	public HashMap getRolesFuncionalidades() {
		return rolesFuncionalidades;
	}

	/**
	 * @param rolesFuncionalidades The rolesFuncionalidades to set.
	 */
	public void setRolesFuncionalidades(HashMap rolesFuncionalidades) {
		this.rolesFuncionalidades = rolesFuncionalidades;
	}

	/**
	 * @return Returns the rolesFuncionalidades.
	 */
	public Object getRolesFuncionalidades(String objKey) {
		return rolesFuncionalidades.get(objKey);
	}

	/**
	 * @param rolesFuncionalidades The rolesFuncionalidades to set.
	 */
	public void setRolesFuncionalidades(String key, Object value) {
		this.rolesFuncionalidades.put(key, value);
	}
}