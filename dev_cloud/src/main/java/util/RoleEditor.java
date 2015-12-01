/*
 * @(#)RoleEditor.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import com.princetonsa.mundo.FuncRoles;
import com.princetonsa.mundo.Funcionalidad;
import com.princetonsa.mundo.Funcionalidades;
import com.princetonsa.mundo.ListaFuncRoles;
import com.princetonsa.mundo.Rol;
import com.princetonsa.mundo.Roles;

/**
 * Esta clase permite editar el archivo web.xml para aï¿½adir/quitar roles, y
 * el acceso a pï¿½ginas de dichos roles.
 *
 * @version 1.0, Nov 21, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class RoleEditor {

	/**
	 * Archivo web.xml. 
	 */
	private File file;

	/**
	 * Documento xml creado al parsear web.xml
	 */
	private Document document;

	/**
	 * Lista de roles, incializada con los valores leï¿½dos de web.xml.
	 */
	private Roles roles;

	/**
	 * Lista de funcionalidades, incializada con los valores leï¿½dos de web.xml.
	 */
	private Funcionalidades funcs;

	/**
	 * Lista de funcionalidades y roles, en un formato que corresponde al usado
	 * en web.xml.
	 */
	private ListaFuncRoles listaFuncRoles = null;

	/**
	 * Crea una nueva instancia de RoleEditor, inicializando sus listas de
	 * roles, funcionalidades, cargando y parseando el archivo web.xml
	 * @param webappDeploymentDescriptor <i>path</i> del archivo web.xml
	 */
	public RoleEditor(String webappDeploymentDescriptor) {
		this.roles = new Roles();
		this.funcs = new Funcionalidades();
		load(webappDeploymentDescriptor);
	}

	/**
	 * Dado el nombre y la localizaciï¿½n de web.xml, carga dicho archivo, lo
	 * parsea y deja en 'document' una referencia al documento producto del
	 * parsing.
	 * @param fileName path que indica dï¿½nde estï¿½ el archivo web.xml
	 * @return <b>true</b> si se pudo cargar el archivo web.xml, <b>false</b> si
	 * no.
	 */
	public boolean load(String fileName) {

		boolean loaded = false;

		if (fileName == null || fileName.equals("")) {

		}

		else {

			this.file = new File(fileName);
			Document doc = null;

			try {
				// Obtiene de la factory el objeto que va a efectuar el parsing 
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				// Obligamos a que el documento generado cumpla con el DTD
				dbf.setValidating(true);
				// Parseamos el documento
				DocumentBuilder db = dbf.newDocumentBuilder();
				doc = db.parse(file);
			}
			catch (IOException ioe) {
				ioe.printStackTrace();
			}
			catch (ParserConfigurationException pce) {
				pce.printStackTrace();
			}
			catch (SAXException se) {
				Exception x = se;
				if (se.getException() != null) {
					se.getException().printStackTrace();
				}
				else {
					x.printStackTrace();
				}
			}

			finally {
				// En 'document' debe quedar una referencia al web.xml parseado
				this.document = doc;
				if (doc != null) {
					/* Si el parsing saliï¿½ bien, leemos del documento los roles y roles-funcionalidades
					   y los almacenamos en memoria en nuestras estructuras de datos */
					loaded = (loadRoles() && loadFuncs());
				}
			}

		}

		return loaded;
	}

	/**
	 * Este mï¿½todo guarda los cambios efectuados en web.xml, tomando como base
	 * el documento original y aï¿½adiï¿½ndole los fragmentos correspondientes a los
	 * nuevos roles y el control de acceso definido sobre estos roles.
	 * @return <b>true</b> si se pudo guardar nuevamente el archivo web.xml,
	 * <b>false</b> si no
	 */
	public boolean save() {

		boolean saved = false;

		try {

			/* Agregamos a document un fragmento con los roles nuevos */
			Element webApp = (Element) document.getLastChild();
			DocumentFragment rolesXml = document.createDocumentFragment();
			insertRoleXml(rolesXml, this.roles);
			rolesXml.normalize();
			webApp.appendChild(rolesXml);

			/* Agregamos a document un fragmento con los security-constraints nuevos */
			DocumentFragment funcsXml = document.createDocumentFragment();
			insertFuncXml(funcsXml, this.listaFuncRoles);
			funcsXml.normalize();
			NodeList nl = webApp.getElementsByTagName("login-config");
			webApp.insertBefore(funcsXml, nl.item(0));

			/* Obtenemos el transformer como producto de una Factory */
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();

			/* Por defecto, transformer remueve la informaciï¿½n del DOCTYPE. La ponemos a mano.
			   Si en un futuro cambia el dtd de web.xml (versiï¿½n 2.3 en este caso), la nueva
			   versiï¿½n del dtd debe incluï¿½rse a continuaciï¿½n. */
			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "web-app_2.3.dtd");

			synchronized (file)
			{
				/* Fuente y Destino del transformer */
				DOMSource source = new DOMSource(this.document);
				
                File tmp = new File(file.getParent() + File.separator + "tmp.xml");
                FileOutputStream archivoSalida = new FileOutputStream(tmp);                    
                StreamResult result = new StreamResult(archivoSalida);
				/* Transformaciï¿½n identidad : no modifica la fuente (excepto el DOCTYPE), sï¿½lo la redirecciona a un archivo */
                transformer.transform(source, result);
                archivoSalida.close(); // Cerramos el archivo para poder eliminarlo y copiarlo

                /* (saved == true) si y sï¿½lo si ya se escribiï¿½ el archivo */
                saved = file.delete() && tmp.renameTo(file);                
                

			}
		}
		catch (TransformerException te) 
        {
			Exception x = te;
			if (te.getException() != null) {
				te.getException().printStackTrace();
			}
			else {
				x.printStackTrace();
			}
            saved = false;
		}
        catch (FileNotFoundException fnfe)
        {
            fnfe.printStackTrace();
            saved = false;
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
            saved = false;
        }

		return saved;
	}

	/**
	 * Aï¿½ade un nuevo rol a la lista de roles.
	 * @param rol objeto <code>Rol</code> que se desea aï¿½adir
	 */
	public void insertRole(Rol rol) {
		this.roles.addRol(rol);
	}

	/**
	 * Quita uno de los roles de la lista de roles.
	 * @param roleName nombre del rol que se desea remover
	 */
	public void quitarRole(String roleName) {
		this.roles.delRol(roleName);
	}

	/**
	 * Retorna los roles.
	 * @return roles actualmente definidos
	 */
	public Set getRoles() {
		return roles.getRoles();
	}

	/**
	 * Aï¿½ade una nueva funcionalidad a la lista de roles-funcionalidades.
	 * @param func objeto <code>Funcionalidad</code> que se desea aï¿½adir
	 */
	public void insertFuncionalidad(Funcionalidad func) {
		this.funcs.addFuncionalidad(func);
	}

	/**
	 * Quita una de las <code>Funcionalidad</code>es (parejas rol-funcionalidad)
	 * de la lista de funcionalidades.
	 * @param roleName nombre del rol (dentro de la pareja rol-funcionalidad)
	 * que se desea remover
	 * @param funcName nombre de la funcionalidad (dentro de la pareja rol-
	 * funcionalidad) que se desea remover
	 */
	public void quitarFuncionalidad(String roleName, String funcName) {
		this.funcs.delFuncionalidad(roleName, funcName);
	}

	/**
	 * Retorna las funcionalidades.
	 * @return funcionalidades actualmente definidas
	 */
	public Set getFuncionalidades() {
		return funcs.getFuncionalidades();
	}

	/**
	 * Retornla la lista de Funcionalidades-Roles.
	 * @return la lista de Funcionalidades-Roles
	 */
	public ListaFuncRoles getListaFuncRoles() {
		return listaFuncRoles;
	}

	/**
	 * Establece la lista de Funcionalidades-Roles, y la de Funcionalidades a partir de
	 * los valores almacenados internamente en el objeto en este moemnto.
	 */
	public void setListaFuncRoles() 
	{
		this.listaFuncRoles = new ListaFuncRoles(this.funcs.getFuncionalidades());
		this.funcs.setFuncionalidades(this.listaFuncRoles.expandListaFuncRoles());
	}

	/**
	 * Este método lee del <i>document</i> generado a partir de web.xml, la
	 * información sobre los roles de la aplicación, y la almacena en una
	 * referencia local a una lista de objetos <code>Rol</code>.
	 * @return <b>true</b> si pudo leer los roles, <b>false</b> si no.
	 */
	private boolean loadRoles() {

		boolean wasLoaded = false;
		Element webApp = (Element) document.getLastChild();
		NodeList nl = webApp.getElementsByTagName("security-role");
		String tmp = "", desc = "", role = "";

		for (int i = 0, size = nl.getLength(); i < size; i++) {

			NodeList nl2 = (nl.item(0)).getChildNodes();

			desc = "";

			for (int j = 0, size2 = nl2.getLength(); j < size2; j++) {

				tmp = (nl2.item(j)).getNodeName();

				// "description" puede ser null si el usuario no entrï¿½ una descripciï¿½n para el rol
				if (tmp.equalsIgnoreCase("description")) {
					Node node = nl2.item(j);
					if (node != null) {
						Node child = node.getFirstChild();
						if (child != null) {
							desc = child.getNodeValue();
						}
					}
				}

				else if (tmp.equalsIgnoreCase("role-name")) {
					role = nl2.item(j).getFirstChild().getNodeValue();
				}

			}

			/* Removemos del document fragment en memoria la informaciï¿½n del rol,
               para escribirla toda de nuevo con los cambios que efectï¿½e el usuario en
               el momento de guardar nuevamente web.xml */
			roles.addRol(new Rol(desc, role));
			webApp.removeChild(nl.item(0));

		}

		wasLoaded = true;
		return wasLoaded;

	}

	/**
	 * Este mï¿½todo lee del <i>document</i> generado a partir de web.xml, la
	 * informaciï¿½n sobre el acceso de los roles sobre la funcionalidad de la
	 * aplicaciï¿½n, y la almacena en una referencia local a una lista de objetos
	 * <code>Funcionalidad</code>.
	 * @return <b>true</b> si pudo leer los roles, <b>false</b> si no.
	 */
	private boolean loadFuncs() {

		boolean isSSL = false;
		boolean wasLoaded = false;

		Vector roleNames = new Vector();
		ListaFuncRoles lfr = new ListaFuncRoles();
		Element webApp = (Element) document.getLastChild();
		NodeList nl = webApp.getElementsByTagName("security-constraint");
		String tmp = "", webResourceName = "", urlPattern = "";

		for (int i = 0, size = nl.getLength(); i < size; i++) {

			NodeList nl2 = (nl.item(0)).getChildNodes();

			for (int j = 0, size2 = nl2.getLength(); j < size2; j++) {

				NodeList nl3 = (nl2.item(j)).getChildNodes();

				for (int k = 0, size3 = nl3.getLength(); k < size3; k++) {

					tmp = (nl3.item(k)).getNodeName();
					if (tmp.equalsIgnoreCase("web-resource-name")) {
						webResourceName = nl3.item(k).getFirstChild().getNodeValue();
					}
					else if (tmp.equalsIgnoreCase("url-pattern")) {
						urlPattern = nl3.item(k).getFirstChild().getNodeValue();
					}
					else if (tmp.equalsIgnoreCase("role-name")) {
						roleNames.add(nl3.item(k).getFirstChild().getNodeValue());
					}
					else if (tmp.equalsIgnoreCase("transport-guarantee")) {
						tmp = nl3.item(k).getFirstChild().getNodeValue();
						if (tmp.equalsIgnoreCase("CONFIDENTIAL")) {
							isSSL = true;
						}
						else if (tmp.equalsIgnoreCase("NONE")) {
							isSSL = false;
						}
					}

				}

			}

			/* Removemos del document fragment en memoria la informaciï¿½n de la funcionalidad,
			   para escribirla toda de nuevo con los cambios que efectï¿½e el usuario en
			   el momento de guardar nuevamente web.xml */
			lfr.addFuncionalidad(new FuncRoles(webResourceName, urlPattern, roleNames, isSSL));
			webApp.removeChild(nl.item(0));
			roleNames = new Vector();

		}

		/* La forma como se almacena la relaciï¿½n funcionalidad-roles en el web.xml es diferente
		   a como esta informaciï¿½n se almacena y manipula en memoria, debo "expandirla" para
		   poder trabajar con ella. */
		this.funcs.setFuncionalidades(lfr.expandListaFuncRoles());
		wasLoaded = true;
		return wasLoaded;

	}

	/**
	 * Inserta un nuevo rol en el document fragment que representa los roles de
	 * la aplicaciï¿½n en el web.xml
	 */
	private void insertRoleXml(DocumentFragment rolesXml, Rol rol) {
	    
        Text enter = document.createTextNode("\n");
		Element element, securityRole;
		Text txt;

		securityRole = document.createElement("security-role");
		securityRole.appendChild(enter);
        
		element = document.createElement("description");
		txt = document.createTextNode(rol.getDescripcion());
		element.appendChild(txt);
		securityRole.appendChild(element);

		element = document.createElement("role-name");
		txt = document.createTextNode(rol.getNombre());
        element.appendChild(txt);
		securityRole.appendChild(element);
		securityRole.appendChild(enter);
		rolesXml.appendChild(securityRole);

	}

	/**
	 * Inserta un conjunto de roles en el document fragment que representa los
	 * roles de la aplicaciï¿½n en el web.xml
	 */
	private void insertRoleXml(DocumentFragment rolesXml, Roles roles) {

		Rol rol;
		Iterator i = roles.getRoles().iterator();

		while (i.hasNext()) {
			rol = (Rol) i.next();
			insertRoleXml(rolesXml, rol);
		}

	}

	/**
	 * Inserta la informaciï¿½n correspondiente a una funcionalidad, los roles que
	 * la pueden acceder y con quï¿½ tipo de comunicaciï¿½n en el document fragment
	 * que representa las funcionalidades con sus respectivos controles de
	 * acceso sobre la aplicaciï¿½n en el web.xml
	 */
	private void insertFuncXml(DocumentFragment funcsXml, FuncRoles fr) {

	    Text enter = document.createTextNode("\n");
        Element securityConstraint = document.createElement("security-constraint");
        securityConstraint.appendChild(enter);
		Element webResourceCollection = document.createElement("web-resource-collection");
        Element authConstraint = document.createElement("auth-constraint");
		Element userDataConstraint = document.createElement("user-data-constraint");

		Element element = document.createElement("web-resource-name");
		Text txt = document.createTextNode(fr.getFuncName());
		element.appendChild(txt);
		webResourceCollection.appendChild(element);

		element = document.createElement("url-pattern");
		txt = document.createTextNode(fr.getUrlPattern());
		element.appendChild(txt);
		webResourceCollection.appendChild(element);

		element = document.createElement("http-method");
		txt = document.createTextNode("POST");
		element.appendChild(txt);
		webResourceCollection.appendChild(element);

		element = document.createElement("http-method");
		txt = document.createTextNode("GET");
		element.appendChild(txt);
		webResourceCollection.appendChild(element);

		String roleName = "";
		Iterator i = fr.getRoleNames().iterator();

		while (i.hasNext()) {

			roleName = (String) i.next();
			element = document.createElement("role-name");
			txt = document.createTextNode(roleName);
			element.appendChild(txt);
			authConstraint.appendChild(element);

		}

		element = document.createElement("transport-guarantee");
		txt = document.createTextNode(fr.isSSL() ? "CONFIDENTIAL" : "NONE");
        element.appendChild(txt);
		userDataConstraint.appendChild(element);

		securityConstraint.appendChild(webResourceCollection);
		securityConstraint.appendChild(authConstraint);
		securityConstraint.appendChild(userDataConstraint);
		securityConstraint.appendChild(enter);
        funcsXml.appendChild(securityConstraint);
	}

	/**
	 * Inserta la informaciï¿½n correspondiente a un conjunto de funcionalidades,
	 * los roles que las pueden acceder y con quï¿½ tipo de comunicaciï¿½n en el
	 * document fragment que representa las funcionalidades con sus respectivos
	 * controles de acceso sobre la aplicaciï¿½n en el web.xml
	 */
	private void insertFuncXml(DocumentFragment funcsXml, ListaFuncRoles lfr) {

		FuncRoles fr;
		Iterator i = lfr.iterator();

		while (i.hasNext()) {
			fr = (FuncRoles) i.next();
			insertFuncXml(funcsXml, fr);
		}

	}

}