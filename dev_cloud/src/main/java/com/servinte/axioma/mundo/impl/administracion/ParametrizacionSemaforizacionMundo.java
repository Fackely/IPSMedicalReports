package com.servinte.axioma.mundo.impl.administracion;

import java.util.ArrayList;

import util.UtilidadFecha;

import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dao.fabrica.AdministracionFabricaDAO;
import com.servinte.axioma.dao.interfaz.administracion.IParametrizacionSemaforizacionDAO;
import com.servinte.axioma.mundo.interfaz.administracion.IParametrizacionSemaforizacionMundo;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.ParametrizacionSemaforizacion;
import com.servinte.axioma.orm.Usuarios;

public class ParametrizacionSemaforizacionMundo implements
IParametrizacionSemaforizacionMundo {

	/**
	 * Dao con las Operaciones Basicas
	 */
	private IParametrizacionSemaforizacionDAO parametrizacionSemaforizacionDAO;

	/**
	 *Contructor de componente 
	 */
	public ParametrizacionSemaforizacionMundo() {

		parametrizacionSemaforizacionDAO = AdministracionFabricaDAO
		.crearParametrizacionSemaforizacionDAO();

	}

	/**
	 * @see com.servinte.axioma.mundo.interfaz.administracion.IParametrizacionSemaforizacionMundo#consultarParametrizaciones(java.lang.String)
	 */
	public ArrayList<com.servinte.axioma.orm.ParametrizacionSemaforizacion> consultarParametrizaciones(
			String reporte) {

		//LLAMDO AL SERVICIO DEL DAO QUE ELIMINA REGISTROS
		return parametrizacionSemaforizacionDAO
		.consultarParametrizaciones(reporte);

	}


	/**
	 * @see com.servinte.axioma.mundo.interfaz.administracion.IParametrizacionSemaforizacionMundo#eliminarParametrizacion(java.lang.String, java.util.ArrayList)
	 */
	public void eliminarParametrizacion(String index, ArrayList<Long> listaParam) {

		// SE RECORRE LA LA LISTA Y SE LLAMA AL METODO ELIMINAR DEL DAO 
		for (int i = 0; i < listaParam.size(); i++) {

			//PRIMERO SE BUSCA EL ELEMENTO A ELIMINAR POR ID Y LUEGO ESE ELEMENTO SE ELIMINA
			parametrizacionSemaforizacionDAO
			.eliminar(parametrizacionSemaforizacionDAO
					.buscarxId(listaParam.get(i)));
		}

	}

	/**
	 * @see com.servinte.axioma.mundo.interfaz.administracion.IParametrizacionSemaforizacionMundo#adicionarModificar(com.princetonsa.mundo.UsuarioBasico, java.util.ArrayList, java.lang.String)
	 */
	public void adicionarModificar(
			UsuarioBasico usu,
			ArrayList<com.servinte.axioma.orm.ParametrizacionSemaforizacion> lista,
			String tipoReporte) {

		// SE RECORRE LA LISTA Y SE LLAMA AL DAO PARA QUE HAGA LA INSERCION O MODIFICACIION DEL REGISTRO EN LA BD
		for (ParametrizacionSemaforizacion parametrizacionSemaforizacion : lista) 
		{
			// SE OBTIENE EL ID DEL USUARIO PARA LA FK
			Usuarios usuarioFk = new Usuarios();
			usuarioFk.setLogin(usu.getLoginUsuario());
			parametrizacionSemaforizacion.setUsuarios(usuarioFk);

			// SE OBTIENE LA FECHA ACTUAL PARA INSERTAR 
			parametrizacionSemaforizacion.setFechaModifica(UtilidadFecha.getFechaActualTipoBD());

			//SE OBTIENE LA HORA DE MODIFICACIONES
			parametrizacionSemaforizacion.setHoraModifica(UtilidadFecha.getHoraActual());



			//SE OBTIENE LA INSTITUCION PARA LA FK 
			Instituciones institucion = new Instituciones();
			institucion.setCodigo(usu.getCodigoInstitucionInt());
			parametrizacionSemaforizacion.setInstituciones(institucion);

			// TIPO DEL REPORTE ASOCIADO
			parametrizacionSemaforizacion.setTipoReporte(tipoReporte);

			//SE LLAMA AL METODO DEL DAO QUE ACTUALIZA O REGISTRA 
			parametrizacionSemaforizacionDAO.insertar(parametrizacionSemaforizacion);


		}




	}

}
