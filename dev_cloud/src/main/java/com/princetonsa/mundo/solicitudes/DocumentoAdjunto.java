package com.princetonsa.mundo.solicitudes;

import java.sql.Connection;

import util.ResultadoBoolean;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.DocumentoAdjuntoDao;

/**
 * Clase para el manejo de un documento adjunto
 *
 * @version 1.0, Febrero 26 / 2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:juandavid@PrincetonSA.com">Juan David Ramirez</a>
 */
public class DocumentoAdjunto
{
	
	/**
	 * Código del archivo en la fuente de datos
	 */
	private int codigoArchivo;
	
	/**
	 * Nombre original del documento adjuntado
	 */
	private String nombreOriginal;
	
	/**
	 * Nombre generado del documento adjuntado
	 */
	private String nombreGenerado;
	
	/**
	 * Dice si fue adjuntado en la solicitud (true) o en la respuesta (false)
	 */
	private boolean esSolicitud;		
	
	/**
	 * Código del médico que adicionó el documento 
	 */
	private int codigoMedico;
	
	/**
	 * Dice si se va a adicionar o a eliminar
	 */
	private boolean adicionar;
	
	/**
	 * Interfaz para acceder a la fuente de datos
	 */
	private DocumentoAdjuntoDao documentoAdjuntoDao = null; 
	
	/**
	 * 
	 */
	private String codigoRespuestaSolicitud;
	
	/**
	 * Creadora de la clase
	 * @see java.lang.Object#Object()
	 */
	public DocumentoAdjunto()
	{
		this.codigoArchivo=0;
		this.nombreOriginal = new String();
		this.nombreGenerado = new String();		
		this.init(System.getProperty("TIPOBD"));
		this.adicionar = true;
		this.codigoMedico=0;
		this.codigoRespuestaSolicitud="";
	}
	
	/**
	 * Creadora de la clase
	 * @param nombreOriginal
	 * @param nombreGenerado
	 * @param esSolicitud
	 * @param codigoArchivo
	 * @param codigoMedico
	 */
	public DocumentoAdjunto(String nombreOriginal, String nombreGenerado, boolean esSolicitud, int codigoArchivo, int codigoMedico, String codigoRespuestaSolicitud)
	{
		this.codigoArchivo = codigoArchivo;
		this.nombreOriginal = nombreOriginal;
		this.nombreGenerado = nombreGenerado;	
		this.esSolicitud = esSolicitud;
		this.adicionar = true;
		this.codigoMedico=codigoMedico;
		this.codigoRespuestaSolicitud=codigoRespuestaSolicitud;
		this.init(System.getProperty("TIPOBD"));
	}

	/**
	 * Creadora de la clase
	 * @param nombreOriginal
	 * @param nombreGenerado
	 * @param esSolicitud
	 * @param codigoArchivo
	 */
	public DocumentoAdjunto(String nombreOriginal, String nombreGenerado, boolean esSolicitud, int codigoArchivo, String codigoRespuestaSolicitud)
	{
		this.codigoArchivo = codigoArchivo;
		this.nombreOriginal = nombreOriginal;
		this.nombreGenerado = nombreGenerado;	
		this.esSolicitud = esSolicitud;
		this.adicionar = true;
		this.codigoMedico=0;
		this.codigoRespuestaSolicitud=codigoRespuestaSolicitud;
		this.init(System.getProperty("TIPOBD"));
	}

	
	/**
	 * Creadora de la clase
	 * @param nombreOriginal
	 * @param nombreGenerado
	 * @param esSolicitud
	 */
	public DocumentoAdjunto(String nombreOriginal, String nombreGenerado, boolean esSolicitud)
	{
		this.codigoArchivo = 0;
		this.nombreOriginal = nombreOriginal;
		this.nombreGenerado = nombreGenerado;	
		this.esSolicitud = esSolicitud;
		this.adicionar = true;
		this.init(System.getProperty("TIPOBD"));
	}
	
	public DocumentoAdjunto(String nombreOriginal, String nombreGenerado, boolean esSolicitud, int codigoArchivo, boolean adicionar)
	{
		this.codigoArchivo = codigoArchivo;
		this.nombreOriginal = nombreOriginal;
		this.nombreGenerado = nombreGenerado;	
		this.esSolicitud = esSolicitud;
		this.adicionar = adicionar;
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public void init(String tipoBD)
	{
		if( documentoAdjuntoDao == null )
		{
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

			if (myFactory != null)
				documentoAdjuntoDao = myFactory.getDocumentoAdjuntoDao();
		}		
	}

	
	
	/**
	 * Retorna el nombre original del documento adjuntado
	 * @return String
	 */
	public String getNombreOriginal()
	{
		return nombreOriginal;
	}

	/**
	 * Asigna el nombre original del documento adjuntado
	 * @param nombreOriginal The nombreOriginal to set
	 */
	public void setNombreOriginal(String nombreOriginal)
	{
		this.nombreOriginal = nombreOriginal;
	}

	/**
	 * Retorna el nombre generado del documento adjuntado
	 * @return String
	 */
	public String getNombreGenerado()
	{
		return nombreGenerado;
	}

	/**
	 * Asgina el nombre generado del documento adjuntado
	 * @param nombreGenerado The nombreGenerado to set
	 */
	public void setNombreGenerado(String nombreGenerado)
	{
		this.nombreGenerado = nombreGenerado;
	}

	/**
	 * Dice si fue adjuntado en la solicitud (true) o en la respuesta
	 * (false)
	 * @return boolean
	 */
	public boolean isEsSolicitud()
	{
		return esSolicitud;
	}

	/**
	 * Asigna si fue adjuntado en la solicitud (true) o en la respuesta (false)
	 * @param esSolicitud The esSolicitud to set
	 */
	public void setEsSolicitud(boolean esSolicitud)
	{
		this.esSolicitud = esSolicitud;
	}
	
	/**
	 * Retorna el código del archivo en la fuente de datos
	 * @return int
	 */
	public int getCodigoArchivo()
	{
		return codigoArchivo;
	}

	/**
	 * Asigna el código del archivo en la fuente de datos
	 * @param codigoArchivo The codigoArchivo to set
	 */
	public void setCodigoArchivo(int codigoArchivo)
	{
		this.codigoArchivo = codigoArchivo;
	}
	
	/**
	 * Inserta el documento adjunto actual
	 * @param con
	 * @param numeroSolicitud
	 * @return ResultadoBoolean
	 */
	public ResultadoBoolean insertarDocumentoAdjunto(Connection con, int numeroSolicitud)
	{
		return this.documentoAdjuntoDao.insertarDocumentoAdjunto(con, numeroSolicitud, this.nombreGenerado, this.nombreOriginal, this.esSolicitud, this.codigoMedico, this.codigoRespuestaSolicitud); 
	}	
	
	/**
	 * @return Returns the codigoRespuestaSolicitud.
	 */
	public String getCodigoRespuestaSolicitud() {
		return codigoRespuestaSolicitud;
	}

	/**
	 * @param codigoRespuestaSolicitud The codigoRespuestaSolicitud to set.
	 */
	public void setCodigoRespuestaSolicitud(String codigoRespuestaSolicitud) {
		this.codigoRespuestaSolicitud = codigoRespuestaSolicitud;
	}

	/**
	 * Inserta el documento adjunto actual dentro de una transacción
	 * @param con
	 * @param numeroSolicitud
	 * @return ResultadoBoolean
	 */
	public ResultadoBoolean insertarDocumentoAdjuntoTransaccional(Connection con, int numeroSolicitud, String estado) throws Exception
	{
		return this.documentoAdjuntoDao.insertarDocumentoAdjuntoTransaccional(con, numeroSolicitud, this.nombreGenerado, this.nombreOriginal, this.esSolicitud, this.codigoMedico, estado, this.codigoRespuestaSolicitud); 
	}
	
	/**
	 * Inserta el documento adjunto actual dentro de una transacción
	 * @param con
	 * @param numeroSolicitud
	 * @return ResultadoBoolean
	 */
	public ResultadoBoolean insertarDocumentoAdjuntoTransaccionalIM(Connection con, int numeroSolicitud, String estado) throws Exception
	{
		return this.documentoAdjuntoDao.insertarDocumentoAdjuntoTransaccionalIM(con, numeroSolicitud, this.nombreGenerado, this.nombreOriginal, estado); 
	}
	
	/**
	 * Revisa si existe o no el documento adjunto actual
	 * @param con
	 * @return ResultadoBoolean
	 */
	public ResultadoBoolean existeDocumentoAdjunto(Connection con)
	{
		return this.documentoAdjuntoDao.existeDocumentoadjunto(con, this.codigoArchivo);
	}
	
	/**
	 * Elimina el archivo adjunto si existe
	 * @param con
	 * @return ResultadoBoolean
	 */
	public ResultadoBoolean eliminarDocumentoAdjunto(Connection con)
	{
		return this.documentoAdjuntoDao.eliminarDocumentoAdjunto(con, this.codigoArchivo);
	}

	/**
	 * Elimina el archivo adjunto si existe
	 * @param con
	 * @return ResultadoBoolean
	 */
	public ResultadoBoolean eliminarDocumentoAdjuntoTransaccional(Connection con, String estado)
	{
		return this.documentoAdjuntoDao.eliminarDocumentoAdjuntoTransaccional(con, this.codigoArchivo, estado);
	}

	/**
	 * Dice si se va a adicionar o a eliminar
	 * @return boolean
	 */
	public boolean isAdicionar()
	{
		return adicionar;
	}

	/**
	 * Asigna si se va a adicionar o a eliminar
	 * @param adicionar The adicionar to set
	 */
	public void setAdicionar(boolean adicionar)
	{
		this.adicionar = adicionar;
	}

	/**Retorna el código del médico que adicionó
	 * el documento 
	 * @return
	 */
	public int getCodigoMedico() 
	{
		return codigoMedico;
	}

	/**
	 * Asigna el código del médico que adicionó
	 * el documento
	 * @param i
	 */
	public void setCodigoMedico(int codigoMedico)
	{
		this.codigoMedico = codigoMedico;
	}

}
