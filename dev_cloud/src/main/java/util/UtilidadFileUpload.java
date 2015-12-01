/*
 * @(#)UtilidadFileUpload.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.axioma.util.log.Log4JManager;

import util.adjuntos.DTOArchivoAdjunto;

/**
 * Clase utilitaria para subir archivos
 *
 * @version 1.0, Oct 27, 2007
 */

public class UtilidadFileUpload 
{
	
	private static Logger logger=Logger.getLogger(UtilidadFileUpload.class);
	
	private static final String PARAMETRO_NOMBRE_ARCHIVO="Filename";

	private static final String PARAMETRO_ARCHIVO="file";
	
	private static final String SEPARADOR=System.getProperty("file.separator");

	/**
	 * M?todo que recibe un objeto request y guarda todos los archivos que vengan
	 * de la pagina anterior, devuelve un String con texto de guia y parejas 
	 * nombre-valor de todos los campos NO archivo en la forma 
	 * 
	 * @param req Objeto request
	 * @return
	 * @throws FileUploadException
	 * @throws Exception
	 * @deprecated
	 * 
	 * No se deben devolver String concatenados, Utilizar el m&eacute;todo guardarAdjuntoGenerico
	 * de esta misma clase, adem&aacute;s permite adjuntar varios documentos al mismo tiempo.
	 */
	@Deprecated
	public static String guardarArchivosEncontrados (HttpServletRequest req, String separador) throws FileUploadException, Exception
	{
		//Si viene en el encoding esperado
		ServletRequestContext servletRequestContext=new ServletRequestContext(req);
		if (ServletFileUpload.isMultipartContent(servletRequestContext))
		{	
			DiskFileItemFactory factory=new DiskFileItemFactory(); 
			//Tamanio del buffer en memoria
			factory.setSizeThreshold(10240000);
			// Sitio almacenamiento cuando el buffer esta lleno
			factory.setRepository(new File(System.getProperty("file.separator")+"tmp"));
			
			ServletFileUpload upload=new ServletFileUpload(factory); 
			// Tamanio maximo del archivo
			upload.setSizeMax(10240000);
			//esta lista contiene todos los fileItems que vienen en el request, por el momento solo trabajamos con el primero
			List fileItems = upload.parseRequest(req);

			Iterator i = fileItems.iterator();

			while (i.hasNext())
			{
				FileItem fi = (FileItem)i.next();

				//pueden ocurrir dos cosas, que sea un archivo o un elemento de la forma
			
				if (!fi.isFormField())
				{
					String fileName = fi.getName();
					
					//separar nombre y extension
					String elementos[]=fileName.split("[.]");


					String fechaActual=UtilidadFecha.getFechaActual();
					String horaActual=UtilidadFecha.getHoraActual();
					fechaActual=fechaActual.replace('/','-');
					horaActual=horaActual.replace(':','-');
					Random r=new Random();		
					//crear nombre aleatorio del sistema
					fileName=fechaActual+"-"+horaActual+r.nextInt();		
					
					if (elementos.length>1)
					{
						if (elementos[elementos.length-1]!=null)
						{
							//los archivos jsp se interpretan al invocarse, luego debemos reemplazar la extension
							//para evitar este problema de seguridad
							if (elementos[elementos.length-1].equals("jsp"))
							{
								fileName=fileName +".htm" ;
							}
							else
							{
								fileName=fileName +"." + elementos[elementos.length-1];
							}
						}
					}
					// Grabamos el archivo 
					String fpath=ValoresPorDefecto.getFilePath();
					fi.write(new File(fpath + fileName));
					//retornamos cadena de la forma nombreSistemaArchivo?nombreOriginalArchivo
					try
					{
						return fileName+separador+elementos[0]+"."+elementos[1];
					}
					catch(ArrayIndexOutOfBoundsException e){
						return fileName+separador+elementos[0];
					}
				}else{
					return null;
				}
				
			}
			return null;
			
		}
		else
		{
			return null;
		}		
	}

	/**
	 * Este m&eacute;todo se encarga de obtener los par&aacute;metros del request
	 * cuando es de tipo multipart/form-data, recibidos por el m6eacute;todo post 
	 * @param parametros HashMap<String, String> mapa de par&aacute;metros
	 * pasado por referencia para obtener los elementos del request
	 * @param request HttpServletRequest request el cual se va a recorrer para
	 * obtener los par&aacute;metros
	 * @return true en caso de no existir errores, false de lo contrario
	 * @author Yennifer Guerrero
	 */
	@SuppressWarnings("unchecked")
	public static boolean obtenerParametrosMultiPart(HashMap<String, String> parametros, HttpServletRequest request)
	{
		ServletRequestContext servletRequestContext=new ServletRequestContext(request);
		/* Se pregunta si es multipart */
		if (ServletFileUpload.isMultipartContent(servletRequestContext))
		{
			/* Se crea el fáctory para poderlo recorrer */
			DiskFileItemFactory factory=new DiskFileItemFactory();
			List<FileItem> fileItems;
			ServletFileUpload upload=new ServletFileUpload(factory);
			try {
				fileItems = upload.parseRequest(request);
			} catch (FileUploadException e) {
				Log4JManager.info("Error leyendo el request", e);
				return false;
			}

			for(FileItem item:fileItems)
			{
				/*
				 * Se pregunta si es un campo del formulario
				 */
				if(item.isFormField())
				{
					/*
					 * se adiciona al mapa el parámetro pasado
					 */
					parametros.put(item.getFieldName(), item.getString());
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * M&acute;todo que recibe un objeto request y guarda todos los archivos que vengan
	 * de la pagina anterior  
	 * @param req Objeto request
	 * @param parametros HashMap<String, String> mapa de par&aacute;metros
	 * pasado por referencia para obtener los elementos del request
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<DTOArchivoAdjunto> guardarAdjuntoGenerico(HttpServletRequest req, HashMap<String, String> parametros)
	{
		/* FIXME Hay que asegurarse de que primero se obtenga el pathAlmacenamiento (Juan David) */
		String pathAlmacenamiento="";
		
		//Si viene en el encoding esperado
		ServletRequestContext servletRequestContext=new ServletRequestContext(req);
		if (ServletFileUpload.isMultipartContent(servletRequestContext))
		{	
			DiskFileItemFactory factory=new DiskFileItemFactory(); 
			//Tamanio del buffer en memoria
			factory.setSizeThreshold(10240000);
			// Sitio almacenamiento cuando el buffer esta lleno
			factory.setRepository(new File(System.getProperty("file.separator")+"tmp"));
			
			ServletFileUpload upload=new ServletFileUpload(factory); 
			// Tamanio maximo del archivo
			//upload.setSizeMax(10240000);
			//esta lista contiene todos los fileItems que vienen en el request, por el momento solo trabajamos con el primero
			List<FileItem> fileItems;
			try {
				fileItems = upload.parseRequest(req);
			} catch (FileUploadException e) {
				Log4JManager.info("Error leyendo el request", e);
				return null;
			}

			Iterator<FileItem> i = fileItems.iterator();
			
			ArrayList<DTOArchivoAdjunto> listaArchivos=new ArrayList<DTOArchivoAdjunto>();

			while (i.hasNext())
			{
				FileItem fi = i.next();

				//pueden ocurrir dos cosas, que sea un archivo o un elemento de la forma
			
				if (!fi.isFormField())
				{
					String fileName = fi.getName();
					String nombreOriginal=fileName;
					
					//separar nombre y extension
					String elementos[]=fileName.split("[.]");


					String fechaActual=UtilidadFecha.getFechaActual();
					String horaActual=UtilidadFecha.getHoraActual();
					fechaActual=fechaActual.replace('/','-');
					horaActual=horaActual.replace(':','-');
					Random r=new Random();		
					//crear nombre aleatorio del sistema
					fileName=fechaActual+"-"+horaActual+r.nextInt();		
					
					if (elementos.length>1)
					{
						if (elementos[elementos.length-1]!=null)
						{
							//los archivos jsp se interpretan al invocarse, luego debemos reemplazar la extension
							//para evitar este problema de seguridad
							if (elementos[elementos.length-1].equals("jsp"))
							{
								fileName=fileName +".htm" ;
							}
							else
							{
								fileName=fileName +"." + elementos[elementos.length-1];
							}
						}
					}
					// Grabamos el archivo 
					try {
						
						String pathCompleto=ValoresPorDefecto.getDirectorioAxiomaBase()+System.getProperty("file.separator")+pathAlmacenamiento;
						File dir = new File(pathCompleto);
						
						if (!dir.exists()) {
							if (dir.mkdirs()) {
								Log4JManager.info("***********************************se crea el directorio "+dir);
							}
						}
						fi.write(new File(pathCompleto + fileName));
						
					} catch (Exception e) {
						Log4JManager.info("Error creando el archivo adjunto en el sistema", e);
						return null;
					}
					listaArchivos.add(new DTOArchivoAdjunto(nombreOriginal, fileName, pathAlmacenamiento));
				}else{
					parametros.put(fi.getFieldName(), fi.getString());
					if(fi.getFieldName().equals("pathAlmacenamiento"))
					{
						pathAlmacenamiento=fi.getString();
					}
				}
				
			}
			
			return listaArchivos;
		}
		else
		{
			return null;
		}		
	}

	/**
	 * Método que valida si la extención del archivo es jpg o png
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static boolean validarExtencionImagen (String fileName) throws Exception
	{
		String elementos[]=fileName.split("[.]");
		if (elementos[elementos.length-1].equalsIgnoreCase("jpg") || elementos[elementos.length-1].equalsIgnoreCase("png"))
			return true;
		return false;
	}
	
	/**
	 * * Método que recibe un objeto request y el directorio donde se va a guardar(despues del web) y guarda todos los archivos que vengan
	 * de la pagina anterior, devuelve un String con texto de guia y parejas 
	 * nombre-valor de todos los campos NO archivo en la forma 
	 * 
	 * @param req
	 * @param separador
	 * @param directorio
	 * @return
	 * @throws FileUploadException
	 * @throws Exception
	 */
	public static String guardarArchivosEncontrados (HttpServletRequest req, String separador, String directorio) throws FileUploadException, Exception
	{
		//Si viene en el encoding esperado
		ServletRequestContext servletRequestContext=new ServletRequestContext(req);
		if (ServletFileUpload.isMultipartContent(servletRequestContext))
		{	
			DiskFileItemFactory factory=new DiskFileItemFactory(); 
			//Tamanio del buffer en memoria
			factory.setSizeThreshold(10240000);
			// Sitio almacenamiento cuando el buffer esta lleno
			factory.setRepository(new File(System.getProperty("file.separator")+"tmp"));
			
			ServletFileUpload upload=new ServletFileUpload(factory); 
			// Tamanio maximo del archivo
			upload.setSizeMax(10240000);
			//esta lista contiene todos los fileItems que vienen en el request, por el momento solo trabajamos con el primero
			List fileItems = upload.parseRequest(req);

			Iterator i = fileItems.iterator();

			while (i.hasNext())
			{
				FileItem fi = (FileItem)i.next();

				//pueden ocurrir dos cosas, que sea un archivo o un elemento de la forma
			
				if (!fi.isFormField())
				{
					String fileName = fi.getName();
					
					//separar nombre y extension
					String elementos[]=fileName.split("[.]");


					String fechaActual=UtilidadFecha.getFechaActual();
					String horaActual=UtilidadFecha.getHoraActual();
					fechaActual=fechaActual.replace('/','-');
					horaActual=horaActual.replace(':','-');
					Random r=new Random();		
					//crear nombre aleatorio del sistema
					fileName=fechaActual+"-"+horaActual+r.nextInt();		
					
					if (elementos.length>1)
					{
						if (elementos[elementos.length-1]!=null)
						{
							//los archivos jsp se interpretan al invocarse, luego debemos reemplazar la extension
							//para evitar este problema de seguridad
							if (elementos[elementos.length-1].equals("jsp"))
							{
								fileName=fileName +".htm" ;
							}
							else
							{
								fileName=fileName +"." + elementos[elementos.length-1];
							}
						}
					}
					// Grabamos el archivo 
					String fpath=directorio;
					fi.write(new File(fpath + fileName));
					//retornamos cadena de la forma nombreSistemaArchivo?nombreOriginalArchivo
					try
					{
						return fileName+separador+elementos[0]+"."+elementos[1];
					}
					catch(ArrayIndexOutOfBoundsException e){
						return fileName+separador+elementos[0];
					}
				}else{
					return null;
				}
				
			}
			return null;
			
		}
		else
		{
			return null;
		}		
	}
	
	public static String guardarArchivosEncontrados (HttpServletRequest req) throws FileUploadException, Exception
	{
		return guardarArchivosEncontrados(req, "?");
	}

	@SuppressWarnings("unchecked")
	public static String subirFlash(HttpServletRequest request, String path, String nameImagen){
		ServletRequestContext servletRequestContext=new ServletRequestContext(request);
		if (!ServletFileUpload.isMultipartContent(servletRequestContext))
		{
			return null;
		}

		DiskFileItemFactory factory=new DiskFileItemFactory(); 
		// Sitio almacenamiento cuando el buffer esta lleno
		//factory.setRepository(new File(SEPARADOR+"tmp"));
		factory.setRepository(new File(ValoresPorDefecto.getFilePath()));
		ServletFileUpload upload=new ServletFileUpload(factory); 
		List<FileItem> archivosLlegan;
		String nombreArchivo="";
		try {
			archivosLlegan = upload.parseRequest(request);
			Iterator<FileItem> iterador=archivosLlegan.iterator();
			while (iterador.hasNext())
			{
				FileItem foto=(FileItem)iterador.next();
				
				//Para no intentar guardar campos de texto

				if(foto.getFieldName().equals(PARAMETRO_NOMBRE_ARCHIVO))
				{
					if(!nameImagen.contains(".jpg"))
					{
						nombreArchivo=path+nameImagen+".jpg";
					}
					else
					{
						nombreArchivo=path+nameImagen;
					}
				}
				else if(foto.getFieldName().equals(PARAMETRO_ARCHIVO))
				{
					try {
						logger.info("Genera una imagen "+nombreArchivo);
						foto.write(new File(nombreArchivo));
					} catch (Exception e) {
						logger.error("Error almacenando el archivo",e);
					}
				}
			}
		} catch (FileUploadException e) {
			logger.error("Error procesando el request",e);
		}
		//Para recorrela utilizamos un iterador
		logger.info("nombreArchivo: "+nombreArchivo);
		return nombreArchivo;
	}	
	/**
	 * M?todo que dado el request, la identificaci?n del paciente y un path
	 * guarda una foto (archivo) en el path especificado con el formato
	 * CodTipoId-NumTipoId.extOriginal, Ej. CC-45324.jpg . Devuelve la extensi?n
	 * original del archivo o null si no se pudo subir
	 * 
	 * @param request Objeto de tipo request
	 * @param codigoTipoIdentificacionPersona C?digo del tipo de identificaci?n
	 * de la persona a la cual se le va a guardar la foto
	 * @param numeroIdentificacionPersona N?mero de identificaci?n de la 
	 * persona a la cual se le va a guardar la foto
	 * @param path Path en el que se guardar? la foto
	 * @return
	 * @throws FileUploadException
	 * @throws Exception
	 */
	public static String subirFoto (HttpServletRequest request, String codigoTipoIdentificacionPersona, String numeroIdentificacionPersona, String path) throws FileUploadException, Exception
	{
		//Revisamos que llegue alg?n archivo
		ServletRequestContext servletRequestContext=new ServletRequestContext(request);
		if (!ServletFileUpload.isMultipartContent(servletRequestContext))
		{
			return null;
		}
		
		DiskFileItemFactory factory=new DiskFileItemFactory(); 
		//Tamanio del buffer en memoria
		factory.setSizeThreshold(4096);
		// Sitio almacenamiento cuando el buffer esta lleno
		/**
		 * Alberto Ovalle
		 * tm 6800
		 * se modifica para que pueda enviar fotos especificada en el path
		 */
		//factory.setRepository(new File(System.getProperty("file.separator")+"tmp"));
		factory.setRepository(new File(path));
		
		ServletFileUpload upload=new ServletFileUpload(factory); 
		// Tamanio maximo del archivo
		upload.setSizeMax(1000032);
		
		//Los archivos llegan en una lista
		List archivosLlegan=upload.parseRequest(request);
		//Para recorrela utilizamos un iterador 
		Iterator iterador=archivosLlegan.iterator();
		//No se utilizan 
		if (iterador.hasNext())
		{
			FileItem foto=(FileItem)iterador.next();
			
			//Para no intentar guardar campos de texto
			if (!foto.isFormField())
			{
				//foto.getName() me da el nombre inicial del archivo

				String elementos[]=foto.getName().split("[.]");

				//Solo si el archivo original tenia extension, lo guardamos
				if (elementos.length>1)
				{
					if (foto.getSize()>0)
					{
						foto.write(new File(path + codigoTipoIdentificacionPersona +"-"+ numeroIdentificacionPersona + "." + elementos[elementos.length-1]));
						return foto.getName();
					}
				}
			}
		}
		return null;
	}
	
	public static String guardarArchivos(HttpServletRequest req) throws FileUploadException, Exception
	{
		//Revisamos que llegue alg?n archivo
		ServletRequestContext servletRequestContext=new ServletRequestContext(req);
		if (ServletFileUpload.isMultipartContent(servletRequestContext))
		{
				String camposTexto="Los campos de texto encontrados son ";
				
				
				DiskFileItemFactory factory=new DiskFileItemFactory(); 
				//Tamanio del buffer en memoria
				factory.setSizeThreshold(4096);
				// Sitio almacenamiento cuando el buffer esta lleno
				factory.setRepository(new File(System.getProperty("file.separator")+"tmp"));
				
				ServletFileUpload upload=new ServletFileUpload(factory); 
				// Tamanio maximo del archivo
				upload.setSizeMax(536000);
				
				List fileItems = upload.parseRequest(req);

				Iterator i = fileItems.iterator();

				while (i.hasNext())
				{
					FileItem fi = (FileItem)i.next();

					//pueden ocurrir dos cosas, que sea un archivo o un elemento de la forma
			
					if (!fi.isFormField())
					{
						String fileName = fi.getName();
					
						// Grabamos el archivo con el nombre original
						fi.write(new File(System.getProperty("file.separator")+"tmp"+System.getProperty("file.separator")+"uploads"+System.getProperty("file.separator") + fileName));
					}
				}
				return camposTexto;
			}
			else
			{
				return "No venia en el encoding esperado";
			}
		}
	
	/**
	 * Guardar un archivo tomando un objeto FormFile
	 * @param archivo
	 * @author armando
	 * @return
	 */
	public static boolean guadarArchivo(FormFile archivo)
	{
		File arc=new File(ValoresPorDefecto.getFilePath()+archivo.getFileName());
		
		try
		{
			RandomAccessFile raf=new RandomAccessFile(arc, "rw");
			raf.write(archivo.getFileData());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		ValoresPorDefecto.getFilePath();
		return true;
	}
	

	/**
	 * Realizar una copia de un archivo en una ruta especifica al upload
	 * @author Andrés E. Silva Monsalve
	 * @param pathOriginal
	 * @param nombreOriginal
	 * @param pathCopia
	 * @param nombreCopia
	 * @return
	 */
	public static boolean generarCopiaArchivo(String pathOriginal,String nombreOriginal,String pathCopia, String nombreCopia)
	{
		
		File arc=new File(ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+pathCopia);
		
		if(!arc.exists())
			arc.mkdir();
		
		File archivoCopia = new File(arc,nombreCopia);
		
		try
		{
			RandomAccessFile raf=new RandomAccessFile(archivoCopia, "rw");
			
			File archivoOriginal = new File(pathOriginal,nombreOriginal);
			FileReader readerOriginal = new FileReader(archivoOriginal);
			BufferedReader bufferOriginal = new BufferedReader(readerOriginal);
			
			String cadena = bufferOriginal.readLine();
			
			while(cadena!=null)
			{
				raf.writeBytes(cadena+"\n");
				//raf.writeChars(cadena);
				cadena = bufferOriginal.readLine();
			}
			bufferOriginal.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		ValoresPorDefecto.getFilePath();
		return true;
	}
	
	/**
	 * Realizar una copia de un archivo en una ruta especifica al upload
	 * @author Andrés E. Silva Monsalve
	 * @param pathOriginal
	 * @param nombreOriginal
	 * @param pathCopia
	 * @param nombreCopia
	 * @return
	 */
	public static boolean generarCopiaArchivoLibre(String pathOriginal,String nombreOriginal,String pathCopia, String nombreCopia)
	{
		
		File arc=new File(pathCopia);
		
		if(!arc.exists())
			arc.mkdir();
		
		File archivoCopia = new File(arc,nombreCopia);
		
		try
		{
			// Se abre el fichero original para lectura
			File archivoOriginal = new File(pathOriginal,nombreOriginal);
			FileInputStream fileInput = new FileInputStream(archivoOriginal);
			BufferedInputStream bufferedInput = new BufferedInputStream(fileInput);
			
			// Se abre el fichero donde se hará la copia
			FileOutputStream fileOutput = new FileOutputStream (archivoCopia);
			BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);
			
			// Bucle para leer de un fichero y escribir en el otro.
			//Se lee de a 1000 bytes
			byte [] array = new byte[1000];
			int leidos = bufferedInput.read(array);
			while (leidos > 0)
			{
				bufferedOutput.write(array,0,leidos);
				leidos=bufferedInput.read(array);
			}

			// Cierre de los ficheros
			bufferedInput.close();
			bufferedOutput.close();

			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * Guardar un archivo tomando un objeto FormFile
	 * y retorna el archivo
	 * @param archivo
	 * @author Sebastián Gómez
	 * @return
	 */
	public static File guardarArchivo(FormFile archivo,String directorioTemporal)
	{
		File arcd = new File(directorioTemporal);
		if(!arcd.exists())
			arcd.mkdirs();
		File arc=new File(arcd.getAbsolutePath(),archivo.getFileName());
		if(arc.exists())
			arc.delete();
		try
		{
			RandomAccessFile raf=new RandomAccessFile(arc, "rw");
			raf.write(archivo.getFileData());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		ValoresPorDefecto.getFilePath();
		return arc;
	}
	
	/**
	 * Guardar un archivo tomando un objeto FormFile Con el nombre que viene en name
	 * @param archivo
	 * @param name
	 * @author Jhony Alexander Duque A.
	 * @return
	 */
	public static boolean guadarArchivo(FormFile archivo, String name)
	{
		//capturamos el nombre original del archivo
		String nametmp=archivo.getFileName();
		//Creamos un archivo con la ruta por defecto y el nombre del archivo original 
		File arc=new File("/"+ValoresPorDefecto.getFilePath()+archivo.getFileName());

		try
		{
			//lo guardamos en el dico duro con permisos de escritura y lectura
			RandomAccessFile raf=new RandomAccessFile(arc, "rw");
			raf.write(archivo.getFileData());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		//se crea otro archivo igual al primero, con la ruta y nombre iguales, 
		//esto a fin de poder compararlo y cambiarle el nombre
		File arc2=new File("/"+ValoresPorDefecto.getFilePath()+nametmp);
		//se crea el archivo con el nombre que le deseamos poner
		File arc3= new File("/"+ValoresPorDefecto.getFilePath()+name);
		//pregunta si el cambio de nombre tuvo exito
		boolean correcto = arc2.renameTo(arc3);
		
		if (correcto)
			  logger.info("El renombrado ha sido correcto");
		else
			  logger.info("El renombrado no se ha podido realizar");
		try
		{
			//almacena el archivo con el nuevo nombre
			RandomAccessFile raf=new RandomAccessFile(arc2, "rw");
			//se borra el archivo con el nombre original del DD
			arc.delete();
			raf.write(archivo.getFileData());
			arc.exists();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return true;
	}
	
	
	/**
	 * Guardar un archivo tomando un objeto FormFile Con el nombre que viene en name
	 * @param archivo
	 * @param name
	 * @author Jhony Alexander Duque A.
	 * @return
	 */
	public static boolean guadarArchivo(FormFile archivo, String name,String filePath)
	{
		
		//capturamos el nombre original del archivo
		String nametmp=archivo.getFileName();
		//Creamos un archivo con la ruta por defecto y el nombre del archivo original
		File arcd = new File(filePath);
		if(!arcd.exists())
			arcd.mkdirs();
		
		File arc=new File(arcd.getAbsolutePath()+System.getProperty("file.separator")+archivo.getFileName());
		
		try
		{
			//lo guardamos en el dico duro con permisos de escritura y lectura
			RandomAccessFile raf=new RandomAccessFile(arc, "rw");
			raf.write(archivo.getFileData());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		//se crea otro archivo igual al primero, con la ruta y nombre iguales, 
		//esto a fin de poder compararlo y cambiarle el nombre
		File arc2=new File(arcd.getAbsolutePath()+System.getProperty("file.separator")+nametmp);
		//se crea el archivo con el nombre que le deseamos poner
		File arc3= new File(arcd.getAbsolutePath()+System.getProperty("file.separator")+name);
		//pregunta si el cambio de nombre tuvo exito
		boolean correcto = arc2.renameTo(arc3);
		
		if (correcto)
			logger.info("El renombrado ha sido correcto");
		else
			logger.info("El renombrado no se ha podido realizar");
		try
		{
			//almacena el archivo con el nuevo nombre
			RandomAccessFile raf=new RandomAccessFile(arc2, "rw");
			//se borra el archivo con el nombre original del DD
			arc.delete();
			raf.write(archivo.getFileData());
			if(arc3.exists())
				return true;
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Metodo encargado de identificar que un archivo existe.
	 * devuelve true si existe y false si no existe.
	 * @author Jhony Alexander Duque A.
	 * @param nombre
	 * @return
	 */
	public static boolean existeArchivo(String nombre)
	{
		return existeArchivo(ValoresPorDefecto.getFilePath(),nombre);
	}

 	/**
	 * Metodo encargado de identificar que un archivo existe
	 * y borrarlo; devuelve true si existe y lo borro o si no existe
	 * y false si existe y no lo borro.
	 * @author Jhony Alexander Duque A.
	 * @param nombre
	 * @return
	 */public static boolean borrrarArchivo(String nombre,String filepath)
	{
		 System.out.print("\n entre borrar Archivo nombre=> "+nombre+"path -> "+filepath);
		 File arc=new File(filepath+nombre);
			if(arc.exists())
			{
				if( arc.delete())
					return true;
				else
					return false;
			}
			else
				return true;
	}
	 
	 /**
	  * Metodo encargado de Validar la existencia de un Path dado
	  * @param String ruta
	  * @author jearias
	  * */
	 public static boolean validarExistePath(String ruta)
	 {
		 File directorio = new File(ruta);			
			
		//se verifica que el directorio exista, sea directorio y sea absoluto
		if(!directorio.isDirectory()||!directorio.exists()||!directorio.isAbsolute())
			return false;
		
		return true;
	 }
	 
	 /**
	 * Metodo encargado de identificar que un archivo existe.
	 * devuelve true si existe y false si no existe.
	 * @author jearias
	 * @param path
	 * @param nombre archivo
	 * @return
	 */
	 public static boolean existeArchivo(String path,String nombre)
	 {
		File arc=new File(path,nombre);	
		return arc.exists();
	 }
	 
	 
	 /**
	 * Metodo encargado de identificar que un archivo existe.
	 * devuelve true si existe y false si no existe.
	 * @author jearias
	 * @param path
	 * @param nombre archivo
	 * @return
	 */
	 public static boolean existeArchivoRutaCompelta(String rutaCompleta)
	 {
		 if (rutaCompleta != null && !rutaCompleta.equals("null") && !rutaCompleta.equals(""))
		 {
			 File arc=new File(rutaCompleta);	
			 return arc.exists();
		 }
		 return false;
	 }
	 
	 /**
	  * 
	  * @param ruta
	  * @return
	  */
	 public static boolean esDirectorioValido(String ruta)
	 {
		 File directorio= new File(ruta);
		 //se verifica que el directorio exista, sea directorio y sea absoluto
		 if (!directorio.isDirectory() && !directorio.exists()) 
		 {
			 if (!directorio.mkdirs()) 
			 {
				 logger.warn("no creo el directorio");
				 return false;
			 }
		 }
		 
		 if(!directorio.canWrite())
		 {
			 logger.warn("no puedo escribir en ese directorio");
			 return false;
		 }
		 
		 return true;
	 }
	 
}