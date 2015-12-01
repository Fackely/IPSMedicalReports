/*
 * @(#)SolicitudesIterator.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.mundo.atencion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.solicitudes.SolicitudInterconsulta;
import com.princetonsa.mundo.solicitudes.SolicitudProcedimiento;

/**
 * Este <code>Iterator</code> me permite recorrer las solicitudes de un paciente.
 * Cada invocación de next(), carga la siguiente y la retorna como un <code>Object</code>.
 * 
 *	@version 1.0, Mar 9, 2004
 */
public class SolicitudesIterator implements Iterator
{
	/**
	 * Número de solicitudes que recorrerá este Iterator.
	 */
	private int length;

	/**
	 * Índice de la solicitud en la que estamos parados.
	 */
	private int index;
	
	/**
	 * Código del tipo de solicitud que va a manejar este
	 * iterador
	 */
	private int codigoTipoSolicitud;

	/**
	 * Arreglo con los números de solicitud sobre las que iteraremos.
	 */
	private ArrayList solicitudes;

	/**
	 * Crea un nuevo <code>Iterator</code> de solicitudes.
	 * @param solicitudes arreglo con los números de solicitud
	 */
	public SolicitudesIterator (ArrayList solicitudes, int codigoTipoSolicitud) throws Exception 
	{
		this.codigoTipoSolicitud=codigoTipoSolicitud;
		this.index = 0;
		this.length = solicitudes.size();
		this.solicitudes= solicitudes;
	}

	/**
	 * Indica si todavía quedan solicitudes por iterar.
	 * @return <b>true</b> si aún quedan solicitudes, <b>false</b> si no
	 */
	public boolean hasNext() {
		return (index < length);
	}

	/**
	 * Retorna la siguiente Solicitud, como un <code>Object</code>.,
	 * la carga con el tipo que corresponda
	 * @return la siguiente solicitud
	 */
	public Object next() 
	{
		Connection con;
		boolean wasLoaded = false;
		if (ConstantesBD.codigoTipoSolicitudInterconsulta==this.codigoTipoSolicitud)
		{
			SolicitudInterconsulta sol= new SolicitudInterconsulta();
			try
			{
				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
				wasLoaded=sol.cargar(con, ((Integer)solicitudes.get(index++)).intValue());
				if (wasLoaded)
				{
					if (sol.getNombreOtros()==null)
					{
						sol.cargarCodigoServicioSolicitadoInterconsulta(con, sol.getNumeroSolicitud());
					}
					sol.cargarDocumentosAdjuntos(con);
					sol.setComentario(UtilidadTexto.observacionAHTML(sol.getComentario()));
					sol.setMotivoSolicitud(UtilidadTexto.observacionAHTML(sol.getMotivoSolicitud()));
					sol.setMotivoAnulacion(UtilidadTexto.observacionAHTML(sol.getMotivoAnulacion()));
					sol.setResumenHistoriaClinica(UtilidadTexto.observacionAHTML(sol.getResumenHistoriaClinica()));
					sol.setInterpretacion(UtilidadTexto.observacionAHTML(sol.getInterpretacion()));
					sol.setRespuestaOtros(UtilidadTexto.observacionAHTML(sol.getRespuestaOtros()));
				}
				
				UtilidadBD.closeConnection(con);
			}
			catch (SQLException sqle) 
			{
					sqle.printStackTrace();
			}	
			finally 
			{
				if (wasLoaded) 
				{
					return sol;
				}
				else 
				{
					return null;
				}
			}
		}
		else if (ConstantesBD.codigoTipoSolicitudProcedimiento==this.codigoTipoSolicitud)
		{
			SolicitudProcedimiento sol=new SolicitudProcedimiento();
			try
			{
				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
				int numACargar=((Integer)solicitudes.get(index++)).intValue();
				wasLoaded=sol.cargarSolicitudProcedimiento(con, numACargar);
				sol.setComentario(UtilidadTexto.observacionAHTML(sol.getComentario()));
				sol.setMotivoAnulacion(UtilidadTexto.observacionAHTML(sol.getMotivoAnulacion()));
				sol.setInterpretacion(UtilidadTexto.observacionAHTML(sol.getInterpretacion()));
				UtilidadBD.closeConnection(con);
			}
			
			catch (SQLException sqle) 
			{
				sqle.printStackTrace();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
				
			finally 
			{
					if (wasLoaded) 
					{
						return sol;
					}
					else 
					{
						return null;
					}
			}
		}
		else
		{
			return null;
		}
	}

	/**
	 * Método vacío, esta implementación de Iterator no lo utiliza.
	 */
	public void remove() {
	}
	
	


}
