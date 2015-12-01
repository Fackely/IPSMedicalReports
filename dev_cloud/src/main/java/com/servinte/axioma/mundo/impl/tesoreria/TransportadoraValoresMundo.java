package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadFecha;

import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ITransportadoraValoresDAO;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.interfaz.tesoreria.ITransportadoraValoresMundo;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.CentrosAtenTransportadora;
import com.servinte.axioma.orm.TransportadoraValores;
import com.servinte.axioma.orm.delegate.UsuariosDelegate;
import com.servinte.axioma.orm.delegate.facturacion.TerceroDelegate;
import com.servinte.axioma.orm.delegate.tesoreria.CentrosAtenTransportadoraDelegate;
import com.servinte.axioma.orm.delegate.tesoreria.TransportadoraValoresDelegate;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * Contiene la l&oacute;gica de Negocio para todo lo relacionado con las
 * Transportadoras de Valores
 * 
 * @author Edgar Carvajal
 * @see ITransportadoraValoresMundo
 */

@SuppressWarnings("unchecked")
public class TransportadoraValoresMundo implements ITransportadoraValoresMundo {

	private ITransportadoraValoresDAO transportadoraValoresDAO;
	

	public TransportadoraValoresMundo() {

		inicializar();
	}

	private void inicializar() {
		transportadoraValoresDAO = TesoreriaFabricaDAO
				.crearTransportadoraValoresDAO();
	}

	/**
	 * SUB TITULO ARCHIVO PLANO MODIFICADO
	 */
	public static final String TITULO_MODIFICACION = "\n TRASNPORTADORA MODIFICADA \n";
	/**
	 * SUB TITULO ARCHIVO PLAN
	 */
	public static final String TITULO_ACTUAL = "\n TRANSPORTADORA ACTUAL \n";
	/**
	 * 
	 */
	public static final String TITULO_ELIMINAR = "\n TRANPORTADORA ELIMINAR \n";

	/**
	 * GUARDA LA TRANPORTADORA DE VALORES
	 * 
	 * @return
	 */
	public void guardar(TransportadoraValores dtoTransportadora,UsuarioBasico usuario, ArrayList<String> listaCodigo,ArrayList<CentroAtencion> listaCentrosAtencion) {
		
		HibernateUtil.beginTransaction();
		// GUARDAR TRANPORTADORA
		guardarTransportadora(dtoTransportadora, usuario);
		// GUARDAR RELACION CENTRO ATENCION
		guardarCentroTransportadora(dtoTransportadora, listaCodigo,
				listaCentrosAtencion);

		HibernateUtil.endTransaction();
	}

	/**
	 * GUARDAR RELACION CENTRO ATENCION CON TRANSPORTADORA
	 * 
	 * @param dtoTransportadora
	 * @param listaCodigo
	 * @param listaCentrosAtencion
	 */
	private void guardarCentroTransportadora(TransportadoraValores dtoTransportadora, ArrayList<String> listaCodigo, ArrayList<CentroAtencion> listaCentrosAtencion) {

		CentrosAtenTransportadoraDelegate centrosAtenTransportadoraDelegate = new CentrosAtenTransportadoraDelegate();
		ArrayList<CentroAtencion> lisDtoCentro = filtrarListaCentros(
				listaCentrosAtencion, listaCodigo);

		for (CentroAtencion dtoCentro : lisDtoCentro) {
			CentrosAtenTransportadora dtoCentroTrans = new CentrosAtenTransportadora();
			dtoCentroTrans.setTransportadoraValores(dtoTransportadora);
			dtoCentroTrans.setCentroAtencion(dtoCentro);
			centrosAtenTransportadoraDelegate.persist(dtoCentroTrans);
		}
	}

	/**
	 * ACCION GUARDAR TRANSPORTADORA
	 * 
	 * @param dtoTransportadora
	 */
	private void guardarTransportadora(TransportadoraValores dtoTransportadora,	UsuarioBasico usuario) {

		// HORA FECHA USUARIO
		accionsettearFechaHoraUsuario(dtoTransportadora, usuario);
		// CARGANDO DELEGATE
		TransportadoraValoresDelegate objTransportadora = new TransportadoraValoresDelegate();
		// GUARDANDO EL OBJETO
		dtoTransportadora.setTerceros(new TerceroDelegate().findById(dtoTransportadora.getTerceros().getCodigo()));
		objTransportadora.persist(dtoTransportadora);
	}

	/**
	 * FILTRAR LISTA CENTRO DE ATENCION
	 * 
	 * @param listaCentrosAtencion
	 */
	public ArrayList<CentroAtencion> filtrarListaCentros(ArrayList<CentroAtencion> listaCentrosAtencion,ArrayList<String> listaCodigoCentros) {
		
		ArrayList<CentroAtencion> listFinalCentros = new ArrayList<CentroAtencion>();

		for (String codigo : listaCodigoCentros) {
			for (CentroAtencion objCentro : listaCentrosAtencion) {
				if (codigo.equals(objCentro.getConsecutivo() + "")) {
					listFinalCentros.add(objCentro);
				}
			}
		}

		return listFinalCentros;

	}

	/**
	 * ACCION SETTERA FECHA HORA Y USUARIO MODIFICA
	 * 
	 * @param dtoTransportadora
	 * @param usuario
	 */
	private void accionsettearFechaHoraUsuario(TransportadoraValores dtoTransportadora, UsuarioBasico usuario) {
		
		dtoTransportadora.setFecha(UtilidadFecha.getFechaActualTipoBD());
		dtoTransportadora.setHora(UtilidadFecha.getHoraActual());
		dtoTransportadora.setUsuarios(new UsuariosDelegate().findById(usuario.getLoginUsuario()));
	}

	/**
	 * LISTA LAS TRANSPORTADORAS DE VALORES
	 * 
	 * @param dtoTransportadora
	 */
	public List<TransportadoraValores> consultar(TransportadoraValores dtoTransportadora, int institucion) {
	
		
		List<TransportadoraValores> listaTransportadora = new ArrayList<TransportadoraValores>();
		
		
		try{
			UtilidadTransaccion.getTransaccion().begin();
			listaTransportadora =  transportadoraValoresDAO.listarTodos(dtoTransportadora,institucion);
			cargarInformacionTerceros(listaTransportadora);
			UtilidadTransaccion.getTransaccion().commit();
		}
		catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
			Log4JManager.info(e);
			Log4JManager.error(e);
		}

		
		return listaTransportadora;
	}

	/**
	 * LISTA LAS TRANSPORTADORAS DE VALORES
	 * 
	 * @param dtoTransportadora
	 */
	public TransportadoraValores consultarTransportadoraValores(int codigoTransportadora) {
	
		HibernateUtil.beginTransaction();
		TransportadoraValores transportadoraValores = new TransportadoraValores();
		transportadoraValores =  transportadoraValoresDAO.consultarTransportadoraValores(codigoTransportadora);
		return transportadoraValores;
	}
	
	/**
	 * CARGAR LA INFORMACION DE TERCERO
	 * 
	 * @param listaTransportadora
	 */

	private void cargarInformacionTerceros(List<TransportadoraValores> listaTransportadora) {
		/**
		 * CARGAR TERCEROS
		 */
		for (TransportadoraValores list : listaTransportadora) {
			list.getTerceros().getCodigo();
			list.getTerceros().getDescripcion();
			list.getCentrosAtenTransportadoras();

			Iterator it = list.getCentrosAtenTransportadoras().iterator();

			while (it.hasNext()) {
				@SuppressWarnings("unused")
				CentrosAtenTransportadora cat = (CentrosAtenTransportadora) it
						.next();
			}
		}
	}

	/**
	 * MODIFICAR TRANSPORTADORA DE VALORES
	 * 
	 * @param dtoTransportadoraValores
	 * @param usuario
	 */
	public void modificar(TransportadoraValores dtoTransportadoraValores, UsuarioBasico usuario, ArrayList<Integer> listaCodigoEliminar, ArrayList<String> listaCodigoGuardar,
			ArrayList<CentroAtencion> listaCentroAtencion) {
		
		
		try
		{
			UtilidadTransaccion.getTransaccion().begin();
			HibernateUtil.beginTransaction();
	
			/*
			 * ACTUALIZAR TRANSPORTADORA
			 */
			accionsettearFechaHoraUsuario(dtoTransportadoraValores, usuario);
			TransportadoraValoresDelegate objTransportadora = new TransportadoraValoresDelegate();
			dtoTransportadoraValores.setTerceros(new TerceroDelegate()
					.findById(dtoTransportadoraValores.getTerceros().getCodigo()));
			objTransportadora.merge(dtoTransportadoraValores);
	
			/*
			 * Eliminar CENTRO ATENCION TRANSPORTADORA
			 */
			eliminiarCentroAtencionTransportadora(listaCodigoEliminar);
			/*
			 * Guardar CENTRO ATENCION TRANSPORTADORA
			 */
			guardarCentroTransportadora(dtoTransportadoraValores,
					listaCodigoGuardar, listaCentroAtencion);
	
			UtilidadTransaccion.getTransaccion().commit();
		}
		catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
			Log4JManager.info(e);
			Log4JManager.error(e);

		}
		

	}

	/**
	 * ELIMINAR TRANSPORTADORA
	 * 
	 * @param dtoTransportadora
	 */
	public void eliminar(TransportadoraValores dtoTransportadora) {
		
		try
		{
			UtilidadTransaccion.getTransaccion().begin();
			
			CentrosAtenTransportadoraDelegate objCentroTransportadoraDelegate = new CentrosAtenTransportadoraDelegate();
			/**
			 * Lista centro de atencion
			 */
	
			Iterator it = dtoTransportadora.getCentrosAtenTransportadoras().iterator();
			while (it.hasNext()) {
				CentrosAtenTransportadora cat = (CentrosAtenTransportadora)it.next();
				objCentroTransportadoraDelegate.delete(cat);
			}
	
			TransportadoraValoresDelegate objDelegate = new TransportadoraValoresDelegate();
			objDelegate.delete(dtoTransportadora);
			UtilidadTransaccion.getTransaccion().rollback();
		
		}
		catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
			Log4JManager.info(e);
		}

	}

	/**
	 * CARGA CODIGOS CENTROS DE ATENCION
	 * 
	 * @param dto
	 * @return
	 */
	public String[] cargarCodigoCentro(TransportadoraValores dto) {

		int indice = 0;
		Iterator lista = dto.getCentrosAtenTransportadoras().iterator();
		String[] codigoCentros = new String[dto.getCentrosAtenTransportadoras()
				.size()];

		while (lista.hasNext()) {
			CentrosAtenTransportadora cat = (CentrosAtenTransportadora) lista
					.next();
			codigoCentros[indice] = cat.getCentroAtencion().getConsecutivo()
					+ "";
			indice++;
		}
		return codigoCentros;
	}

	/**
	 * CARGA CODIGOS CENTROS DE ATENCION
	 * 
	 * @param dto
	 * @return
	 */
	public ArrayList<Integer> cargarCodigosPkCentroTransportadora(TransportadoraValores dto) {
		
		ArrayList<Integer> listaCodigoPk = new ArrayList<Integer>();
		
		Iterator lista = dto.getCentrosAtenTransportadoras().iterator();

		while (lista.hasNext()) {
			CentrosAtenTransportadora cat = (CentrosAtenTransportadora) lista
					.next();
			listaCodigoPk.add(cat.getCodigoPk());
		}

		return listaCodigoPk;
	}

	/**
	 * ELIMINAR LOS CENTROS DE ATENCION RECIBE UNA LISTA DE CODIGOS CENTRO
	 * ATENCION
	 * 
	 * @param listaCodigoCentroAtencion
	 */
	public void eliminiarCentroAtencionTransportadora(ArrayList<Integer> listaCodigoCentroAtencion) {
		
		CentrosAtenTransportadoraDelegate delegateTranportadora = new CentrosAtenTransportadoraDelegate();
		delegateTranportadora
				.eliminarTrasnportadoras(listaCodigoCentroAtencion);
	}


	/**
	 * RECIBE UN OBJTEO TRANPORTADORA Y RETORNA UN STRING BUILDER CON EL FORMATO
	 * DE ARCHIVO PLANO
	 * 
	 * @param dto
	 * @param titulo
	 * @param listaCentros
	 * @param listaCodigoCentro
	 * @return
	 */
	public StringBuilder armarArchivo(TransportadoraValores dto, String titulo,	ArrayList<CentroAtencion> listaCentros,	ArrayList<String> listaCodigoCentro) {

		StringBuilder archivoPlanoAnterior = new StringBuilder();
		archivoPlanoAnterior
				.append("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>><<>>>>>>>>>>>>>>>>>>>>>>");
		archivoPlanoAnterior.append("\n " + titulo);
		archivoPlanoAnterior.append("CODIGO_PK-> \t" + dto.getCodigoPk());
		archivoPlanoAnterior.append("\n");
		archivoPlanoAnterior.append("CODIGO-> \t" + dto.getCodigo());
		archivoPlanoAnterior.append("\n");
		archivoPlanoAnterior.append("HORA-> \t" + dto.getHora());
		archivoPlanoAnterior.append("\n");
		archivoPlanoAnterior.append("OBSERVACIONES-> \t"
				+ dto.getObservaciones());
		archivoPlanoAnterior.append("\n");
		archivoPlanoAnterior.append("PERSONA CONTACTO-> \t"
				+ dto.getPersonaContacto());
		archivoPlanoAnterior.append("\n");
		archivoPlanoAnterior.append("TELEFONO-> \t" + dto.getTelefono());
		archivoPlanoAnterior.append("\n");
		archivoPlanoAnterior.append("FECHA-> \t" + dto.getFecha());
		archivoPlanoAnterior.append("\n");
		archivoPlanoAnterior.append("\n");
		archivoPlanoAnterior
				.append("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<TERCEROS>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		archivoPlanoAnterior.append("\n");
		archivoPlanoAnterior.append("CODIGO TERCERO-> \t"
				+ dto.getTerceros().getCodigo());
		archivoPlanoAnterior.append("\n");
		archivoPlanoAnterior.append("NOMBRE TERCERO-> \t"
				+ dto.getTerceros().getDescripcion());
		archivoPlanoAnterior.append("\n");
		archivoPlanoAnterior.append("\n");
		archivoPlanoAnterior
				.append("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<CENTRO DE ATENCION>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		archivoPlanoAnterior.append("\n");

		if (listaCodigoCentro != null) {

			ArrayList<CentroAtencion> listCentro = filtrarListaCentros(
					listaCentros, listaCodigoCentro);
			for (CentroAtencion dtoCentro : listCentro) {
				archivoPlanoAnterior.append("CONSECUTIVO CENTRO ATENCION->\t"
						+ dtoCentro.getConsecutivo());
				archivoPlanoAnterior.append("\n");
				archivoPlanoAnterior.append("CODIGO CENTRO ATENCION \t"
						+ dtoCentro.getCodigo());
				archivoPlanoAnterior.append("\n");
				archivoPlanoAnterior.append("NOMBR CENTRO ATENCION \t"
						+ dtoCentro.getDescripcion());
				archivoPlanoAnterior.append("\n");
			}
		} else {
			Iterator it = dto.getCentrosAtenTransportadoras().iterator();
			while (it.hasNext()) {
				CentrosAtenTransportadora ca = (CentrosAtenTransportadora) it
						.next();
				archivoPlanoAnterior.append("CONSECUTIVO CENTRO ATENCION->\t"
						+ ca.getCentroAtencion().getConsecutivo());
				archivoPlanoAnterior.append("\n");
				archivoPlanoAnterior.append("CODIGO CENTRO ATENCION \t"
						+ ca.getCentroAtencion().getCodigo());
				archivoPlanoAnterior.append("\n");
				archivoPlanoAnterior.append("NOMBR CENTRO ATENCION \t"
						+ ca.getCentroAtencion().getDescripcion());
				archivoPlanoAnterior.append("\n");
			}

		}

		return archivoPlanoAnterior;
	}

	/**
	 * GUARDA EL LOG EN ARCHIVO PLANO
	 * 
	 * @param estructuraArchivo
	 */
	public void guardarLog(StringBuilder estructuraArchivo,	UsuarioBasico usuario, int tipoRegistro) {
		
		String archivoLog = "";

		try {
			archivoLog = estructuraArchivo.toString();
			String usuarioStr = "";
			usuarioStr += "\n";
			usuarioStr += " Usuario->" + usuario.getLoginUsuario();
			usuarioStr += "\n";
			usuarioStr += "Fecha Modifica->" + UtilidadFecha.getFechaActual();
			usuarioStr += "\n";
			usuarioStr += " Hora->" + UtilidadFecha.getHoraActual();
			archivoLog += usuarioStr;

		} catch (Exception e) {
			Log4JManager.info("\n\n\n Error Generar Archivo " + e + "\n\n\n");
		}

		LogsAxioma.enviarLog(ConstantesBD.logTransportadoraValores, archivoLog,
				tipoRegistro, usuario.getLoginUsuario());
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.ITransportadoraValoresMundo#listarTodos(com.servinte.axioma.orm.TransportadoraValores, int, int)
	 */
	@Override
	public List<TransportadoraValores> listarTodos(TransportadoraValores dtoTransportadora, int institucion, int consecutivoCentroAtencion) {
		
			return transportadoraValoresDAO.listarTodos(dtoTransportadora, institucion, consecutivoCentroAtencion);
	}

}
