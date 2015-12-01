/*
* @(#)SolicitudConsultaExterna.java
*
* Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*/
package com.princetonsa.mundo.solicitudes;

import java.sql.Connection;
import java.sql.SQLException;

import java.lang.Integer;
import java.lang.String;

import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.SolicitudConsultaExternaDao;

import util.ConstantesBD;
import util.InfoDatosInt;

/**
* Esta clase encapsula los atributos y la funcionalidad de una solicitud de consulta externa
*
* @version 1.0, Abr 03, 2004
* @author <a href="mailto:edgar@PrincetonSA.com">Edgar Prieto</a>
*/
public class SolicitudConsultaExterna extends Solicitud
{
	/** Servicio asociado a la unidad de consulta para la cual se reserva / asigna la cita */
	InfoDatosInt iidi_servicioSolicitado;

	/**
	* El DAO usado por el objeto <code>SolicitudConsultaExterna</code> para acceder a la fuente de
	* datos
	*/
	private static transient SolicitudConsultaExternaDao isced_dao = null;

	/** Nueva solicitud de consulta externa */
	public SolicitudConsultaExterna()
	{
		/* Inicializar la solicitud general */
		super();

		/* Inicializar los datos particulares de la solicitud de consulta externa */
		clean();

		/* Inicializar el acceo a base de datos */
		init();
	}

	/** Carga un objeto de solicitud de consulta externa desde una fuente de datos */
	public boolean cargarSolicitudConsultaExterna(
		Connection ac_con,
		int ai_numeroSolicitud
	)throws SQLException
	{
		boolean lb_resp;

		/* Inicializar las propiedades de la soicitud de consulta externa */
		clean();

		/* Cargar la solicitud general */
		lb_resp = (super.cargar(ac_con, ai_numeroSolicitud) ) ;
		if(lb_resp )
		{
			/* Cargar los datos paticulares de la solicitud de consulta externa */
			HashMap ldb_solicitud;

			lb_resp = (ldb_solicitud = isced_dao.cargar(ac_con, ai_numeroSolicitud) ) != null;

			if(lb_resp)
			{
				Integer	li_aux;
				String	ls_aux;

				if( (li_aux = (Integer)ldb_solicitud.get("codigoserviciosolicitado") ) != null)
					setCodigoServicioSolicitado(li_aux.intValue() );

				if( (ls_aux = (String)ldb_solicitud.get("nombreserviciosolicitado") ) != null)
					setNombreServicioSolicitado(ls_aux);

				li_aux = (Integer)ldb_solicitud.get("codigoespecialidadsolicitada");
				ls_aux = (String)ldb_solicitud.get("nombreespecialidadsolicitada");

				if(li_aux != null && ls_aux != null)
					setEspecialidadSolicitada(new InfoDatosInt(li_aux.intValue(), ls_aux.trim() ) );
				else
					setEspecialidadSolicitada(new InfoDatosInt(-1, "") );
			}
		}

		return lb_resp;
	}

	/** Inicialización de las propiedades de la solicitud de consulta externa */
	public void clean()
	{
		super.clean();
		iidi_servicioSolicitado = new InfoDatosInt();
		setCodigoServicioSolicitado(-1);
	}

	/** Obtiene el código del servicio de la solicitud de consulta externa */
	public int getCodigoServicioSolicitado()
	{
		return iidi_servicioSolicitado.getCodigo();
	}

	/** Obtiene el nombre del servicio de la solicitud de consulta externa */
	public String getNombreServicioSolicitado()
	{
		return iidi_servicioSolicitado.getNombre();
	}

	/** Inicializa el acceso a bases de datos de este objeto. */
	public void init()
	{
		String ls_tipoBD;

		/* Inicializar el acceso a base de datos de la solicitud general */
		super.init(ls_tipoBD = System.getProperty("TIPOBD") );

		/* Obtener el DAO que encapsula las operaciones de BD de este objeto */
		if(isced_dao == null)
			isced_dao = DaoFactory.getDaoFactory(ls_tipoBD).getSolicitudConsultaExternaDao();
	}

	/** Inserta una solicitud de consulta externa en una fuente de datos */
	public int insertarSolicitudConsultaExternaTransaccional(Connection ac_con, String as_estado)throws SQLException
	{
		String	ls_estadoSolicitud;
		String ls_estadoSolicitudProcedimiento;

		/* Iniciar los estados de transacción */
		if(as_estado != null)
		{
			ls_estadoSolicitud = ls_estadoSolicitudProcedimiento = "";

			/* Si el estado es empezar la inserción de la solicitud debe iniciar la transacción */
			if(as_estado.equals(ConstantesBD.inicioTransaccion) )
				ls_estadoSolicitud = as_estado;
			/*
				Si el estado es finalizar la inserción de la solicitud de consulta externa debe
				terminar la transacción
			*/
			if(as_estado.equals(ConstantesBD.finTransaccion) )
				ls_estadoSolicitudProcedimiento = as_estado;
		}
		else
		{
			/* El estado es inválido. Permitir que cada objeto maneje esta situación */
			ls_estadoSolicitud = ls_estadoSolicitudProcedimiento = null;
		}

		/* Insertar los datos generales de la solicitud */
		insertarSolicitudTransaccional(ac_con, ls_estadoSolicitud);

		/* Insertar los datos particulares de la solicitud de consulta externa */
		if(
			isced_dao.insertarTransaccional(
				ac_con,
				ls_estadoSolicitudProcedimiento,
				getNumeroSolicitud(),
				getCodigoServicioSolicitado()
			) == 1 )
			return getNumeroSolicitud();
		else
			return -1;
	}

	/** Establece el código del servicio de la solicitud de consulta externa */
	public void setCodigoServicioSolicitado(int ai_servicioSolicitado)
	{
		iidi_servicioSolicitado.setCodigo(ai_servicioSolicitado < 0 ? -1 : ai_servicioSolicitado);
	}

	/** Establece el nombre del servicio de la solicitud de consulta externa */
	public void setNombreServicioSolicitado(String as_servicioSolicitado)
	{
		iidi_servicioSolicitado.setNombre(
			as_servicioSolicitado != null ? as_servicioSolicitado.trim() : ""
		);
	}
}