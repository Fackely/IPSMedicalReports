package com.princetonsa.mundo.solicitudes;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import java.util.HashMap;
import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.ResultadoCollectionDB;
import util.UtilidadCadena;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.DocumentosAdjuntosDao;

/**
 * Clase para el manejo de un conjunto de documentos adjuntos
 *
 * @version 1.0, Febrero 26 / 2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:juandavid@PrincetonSA.com">Juan David Ramirez</a>
 */

public class DocumentosAdjuntos
{
	
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(DocumentosAdjuntos.class);	

	
	/**
	 * Lista con los documentos adjuntados
	 */
	private ArrayList documentosAdjuntos;
	
	/**
	 * Interfaz para acceder a la fuente de datos
	 */
	private DocumentosAdjuntosDao documentosAdjuntosDao = null; 

	/**
	 * Creadora de la clase
	 * @see java.lang.Object#Object()
	 */
	public DocumentosAdjuntos()
	{
		this.documentosAdjuntos = new ArrayList();
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
		if( documentosAdjuntosDao == null)
		{
			if(tipoBD==null)
			{
				logger.error("No esta llegando el tipo de base de datos en clase DocumentosAdjuntos");
				System.exit(1);
			}
			else
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
				if (myFactory != null)
					documentosAdjuntosDao = myFactory.getDocumentosAdjuntosDao();
			}
		}		
	}	
	
	/**
	 * Adiciona un documento adjunto a la lista
	 * @param documentoAdjunto
	 */
	public void addDocumentoAdjunto(DocumentoAdjunto documentoAdjunto)
	{
		this.documentosAdjuntos.add(documentoAdjunto);
	}	
	
	/**
	 * Retorna el documento adjunto en el indice especificado
	 * @param indice
	 * @return DocumentoAdjunto
	 */
	public DocumentoAdjunto getDocumentoAdjunto(int indice)
	{
		if( indice >= this.documentosAdjuntos.size() )
			return null;
		else
			return (DocumentoAdjunto)this.documentosAdjuntos.get(indice);
	}

	/**
	 * Retorna el numero de documentos adjuntos existentes
	 * @return int
	 */
	public int getNumDocumentosAdjuntos()
	{
		return this.documentosAdjuntos.size();	
	}
	
	/**
	 * Inserta todos los documentos adjuntos que hay en el listado bajo el
	 * número de solicitud dado
	 * @param con
	 * @param numeroSolicitud
	 * @return ResultadoBoolean
	 */
	public ResultadoBoolean insertarEliminarDocumentosAdjuntos(Connection con, int numeroSolicitud)
	{
		int tam = this.getNumDocumentosAdjuntos();
		for( int i=0; i<tam; i++ )
		{
			DocumentoAdjunto tempDA = (DocumentoAdjunto)this.getDocumentoAdjunto(i);

			if( tempDA.isAdicionar() )
			{
				ResultadoBoolean existe;
				if( tempDA.getCodigoArchivo() > 0 )
					existe = tempDA.existeDocumentoAdjunto(con);
				else
					existe = new ResultadoBoolean(false);
	
				existe = tempDA.existeDocumentoAdjunto(con);
				if( !existe.isTrue() )
					tempDA.insertarDocumentoAdjunto(con, numeroSolicitud);
				else
				if( UtilidadCadena.noEsVacio(existe.getDescripcion()) )
					return existe;
			}
			else
			{
				ResultadoBoolean eliminar = tempDA.eliminarDocumentoAdjunto(con);
				
				if( !eliminar.isTrue() && UtilidadCadena.noEsVacio(eliminar.getDescripcion()) )
					return eliminar;
			}
		}		
		return new ResultadoBoolean(true);
	}
	
	/**
	 * Inserta todos los documentos adjuntos que hay en el listado bajo el
	 * número de solicitud dado dentro de una transacción dado el estado
	 * @param con
	 * @param numeroSolicitud
	 * @param estado
	 * @return ResultadoBoolean
	 */
	public ResultadoBoolean insertarEliminarDocumentosAdjuntosTransaccional(Connection con, int numeroSolicitud, String estado) throws Exception
	{		
		boolean inicioTransaccion = false;
		boolean finTransaccion = false;
		if( estado.equals(ConstantesBD.inicioTransaccion) )
			inicioTransaccion = true;

		else
		if( estado.equals(ConstantesBD.finTransaccion) )
			finTransaccion = true;
		else
		if( estado.equals(ConstantesBD.continuarTransaccion) )
		{
			inicioTransaccion = false;
			finTransaccion = false;						
		}
		else
			return new ResultadoBoolean(false, "El estado no es válido dentro de la transacción");
			
		String estadoActual = new String();
		
		logger.info("Mundo adjuntos numDocs->"+this.getNumDocumentosAdjuntos());
		
		int tam = this.getNumDocumentosAdjuntos();
		for( int i=0; i<tam; i++ )
		{
			DocumentoAdjunto tempDA = (DocumentoAdjunto)this.getDocumentoAdjunto(i);
			
			if( inicioTransaccion && i == 0 )
				estadoActual = ConstantesBD.inicioTransaccion;
			else
			if( finTransaccion && i == tam-1 )
				estadoActual = ConstantesBD.finTransaccion;
			else
				estadoActual = ConstantesBD.continuarTransaccion;												
			
			if( tempDA.isAdicionar() )
			{
				ResultadoBoolean existe;
				if( tempDA.getCodigoArchivo() > 0 )
					existe = tempDA.existeDocumentoAdjunto(con);
				else
					existe = new ResultadoBoolean(false);
					
				if( !existe.isTrue() )
				{
					logger.info("va ha insertar--> numSol= "+numeroSolicitud+" estadoActual->"+estadoActual);
					//tempDA.insertarDocumentoAdjuntoTransaccionalIM(con, numeroSolicitud, estadoActual);
					tempDA.insertarDocumentoAdjuntoTransaccional(con, numeroSolicitud, estadoActual);
				}
				else
				if( UtilidadCadena.noEsVacio(existe.getDescripcion()) )
					return existe;
			}
			else
			{
				ResultadoBoolean eliminar = tempDA.eliminarDocumentoAdjuntoTransaccional(con, estado);
				
				if( !eliminar.isTrue() && UtilidadCadena.noEsVacio(eliminar.getDescripcion()) )
					return eliminar;
			}
		}		
		return new ResultadoBoolean(true);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param estado
	 * @return
	 * @throws Exception
	 */
	public ResultadoBoolean insertarEliminarDocumentosAdjuntosTransaccionalIM(Connection con, int numeroSolicitud, String estado) throws Exception
	{		
		boolean inicioTransaccion = false;
		boolean finTransaccion = false;
		if( estado.equals(ConstantesBD.inicioTransaccion) )
			inicioTransaccion = true;

		else
		if( estado.equals(ConstantesBD.finTransaccion) )
			finTransaccion = true;
		else
		if( estado.equals(ConstantesBD.continuarTransaccion) )
		{
			inicioTransaccion = false;
			finTransaccion = false;						
		}
		else
			return new ResultadoBoolean(false, "El estado no es válido dentro de la transacción");
			
		String estadoActual = new String();
		
		logger.info("Mundo adjuntos numDocs->"+this.getNumDocumentosAdjuntos());
		
		int tam = this.getNumDocumentosAdjuntos();
		for( int i=0; i<tam; i++ )
		{
			DocumentoAdjunto tempDA = (DocumentoAdjunto)this.getDocumentoAdjunto(i);
			
			if( inicioTransaccion && i == 0 )
				estadoActual = ConstantesBD.inicioTransaccion;
			else
			if( finTransaccion && i == tam-1 )
				estadoActual = ConstantesBD.finTransaccion;
			else
				estadoActual = ConstantesBD.continuarTransaccion;												
			
			if( tempDA.isAdicionar() )
			{
				ResultadoBoolean existe;
				if( tempDA.getCodigoArchivo() > 0 )
					existe = tempDA.existeDocumentoAdjunto(con);
				else
					existe = new ResultadoBoolean(false);
					
				if( !existe.isTrue() )
				{
					logger.info("va ha insertar--> numSol= "+numeroSolicitud+" estadoActual->"+estadoActual);
					tempDA.insertarDocumentoAdjuntoTransaccionalIM(con, numeroSolicitud, estadoActual);
					//tempDA.insertarDocumentoAdjuntoTransaccional(con, numeroSolicitud, estadoActual);
				}
				else
				if( UtilidadCadena.noEsVacio(existe.getDescripcion()) )
					return existe;
			}
			else
			{
				ResultadoBoolean eliminar = tempDA.eliminarDocumentoAdjuntoTransaccional(con, estado);
				
				if( !eliminar.isTrue() && UtilidadCadena.noEsVacio(eliminar.getDescripcion()) )
					return eliminar;
			}
		}		
		return new ResultadoBoolean(true);
	}
	
	/**
	 * Carga todos los documentos adjuntos de la solicitud dada y del tipo
	 * (solicitud/respuesta) dado.
	 * @param con
	 * @param numeroSolicitud
	 * @param esSolicitud
	 * @return ResultadoBoolean
	 */
	public ResultadoBoolean cargarDocumentosAdjuntos(Connection con, int numeroSolicitud, boolean esSolicitud, String codigoRespuesta)
	{
		ResultadoCollectionDB documentos = this.documentosAdjuntosDao.cargarDocumentosAdjuntos(con, numeroSolicitud, esSolicitud, codigoRespuesta);
		this.resetDocumentosAdjuntos();
		
		if( !documentos.isTrue() )
			return new ResultadoBoolean(false, documentos.getDescripcion());
		else
		{
			Collection listadoDocs = (Collection)documentos.getFilasRespuesta();
			Iterator it=listadoDocs.iterator();
			int tam = 0;
			int i=0;
			while(it.hasNext())
			{
				HashMap documento = (HashMap)it.next();
				Utilidades.imprimirMapa(documento);
				DocumentoAdjunto tempDA = new DocumentoAdjunto();
				tempDA.setNombreGenerado((String)documento.get("nombregenerado"));
				tempDA.setCodigoArchivo(Utilidades.convertirAEntero(documento.get("codigoarchivo")+""));					
				tempDA.setNombreOriginal((String)documento.get("nombreoriginal"));
				if((documento.get("codigomedico"))!=null)
					tempDA.setCodigoMedico(Utilidades.convertirAEntero(documento.get("codigomedico")+""));
				tempDA.setEsSolicitud(esSolicitud);
				this.addDocumentoAdjunto(tempDA);					
			}
		}
		return new ResultadoBoolean(true);
	}
	
	private void resetDocumentosAdjuntos()
	{
		this.documentosAdjuntos=new ArrayList();
	}
	
	/**
	 * Método que retorna un iterador para el
	 * conjunto de documentos adjuntos existentes
	 * @return
	 */
	public Iterator getDocumentosAdjuntosIterator ()
	{
		return this.documentosAdjuntos.iterator();
	}
}
