package com.princetonsa.actionform.tesoreria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.UtilidadTexto;

import com.princetonsa.action.tesoreria.MovimientosCajaAction;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.tesoreria.DtoCajaCajeros;
import com.princetonsa.dto.tesoreria.DtoConsolidadoMovimiento;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.InstitucionBasica;
import com.servinte.axioma.dto.tesoreria.DtoFiltroReportesArqueosCierres;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.TiposMovimientoCaja;
import com.servinte.axioma.orm.TurnoDeCaja;

/**
 * Form que contiene los datos espec&iacute;ficos para generar los movimientos de 
 * Arqueo Caja, Arqueo Entrega Parcial y Cierre Turno de Caja (Anexos: 226 - 227 - 228)
 * 
 * Adem&aacute;s maneja el proceso de validaci&oacute;n de errores de datos de entrada.
 *
 * @author Jorge Armando Agudelo Quintero
 * @see MovimientosCajaAction
 *
 */
@SuppressWarnings("serial")
public class MovimientosCajaForm extends ActionForm {

	/**
	 * Atributo utilizado para el control del flujo de la aplicaci&oacute;n
	 */
	private String estado;

	/**
	 * Listado con los tipos de movimientos de caja que corresponden a Arqueos
	 */
	private ArrayList<TiposMovimientoCaja> listadoTiposArqueo;

	/**
	 * Listado con los Cajeros disponibles para realizar un movimiento de caja
	 */
	private List<DtoUsuarioPersona> listadoCajeros;

	/**
	 * Listado de Caja disponibles para realizar un movimiento de caja
	 */
	private ArrayList<Cajas> listadoCajas;
	
	/**
	 * Listado de Cajas de tipo Recaudo disponibles para realizar un movimiento de caja
	 */
	private ArrayList<Cajas> listadoCajasRecaudo;

	/**
	 * Tipo de movimiento seleccionado en el proceso
	 */
	private TiposMovimientoCaja tipoArqueo;

	/**
	 * Cajero seleccionado en el proceso
	 */
	private DtoUsuarioPersona cajero;

	/**
	 * Caja seleccionada en el movimiento
	 */
	private Cajas caja;

	/**
	 * Fecha del movimiento de arqueo seleccionada
	 */
	private Date fechaArqueo;

	/**
	 * Dto con la informaci&oacute;n consolidada que se relaciona con los movimientos de Arqueo de Caja,
	 * Arqueo Entrega Parcial y Cierre de Turno de Caja. 
	 */
	private DtoConsolidadoMovimiento consolidadoMovimientoDTO;

	/**
	 * Listado de Cajas de tipo Principal o Mayor disponibles para realizar un movimiento de caja
	 */
	private ArrayList<Cajas> listadoCajasPrincipalMayor;

	/**
	 * Atributo que indica el nombre de la secci&oacute;n que se quiere mostrar
	 */
	private String mostrarParaSeccionEspecial;
	
	/**
	 * Atributo que indica si se muestra o no la informaci&oacute;n relacionada en el Cuadre de Caja
	 */
	private String estadoMostrarCuadreCaja;
    
	/**
	 * Atributo que indica si se muestra o no desplegada la secci&oacute;n del Cuadre de Caja
	 */
	private boolean mostrarCuadreCaja;
	
	/**
	 * Atributo que contiene la informaci&oacute;n de la Instituci&oacute;n B&aacute;sica
	 */
	private InstitucionBasica institucionBasica;
	
	/**
	 * Caja Princiapl o Mayor seleccionada en el proceso
	 */
	private Cajas cajaMayorPrincipal;
	
	/**
	 * Caja de recaudo seleccionada en el proceso (Solicitud traslado a caja)
	 */
	private Cajas cajaRecaudo;

	/**
	 * Atributo utilizado para el control de flujo
	 */
	private String propiedad;
	
	
	/** * Lista de testigos para la aceptacion */
	private ArrayList<DtoUsuarioPersona> listadoTestigos;
	
	
	/** * Parametro que indica si se debe mostrar el testigo en el proceso activo del movimiento o no */
	private boolean parametroRequiereTestigo;
	
	/**
	 * Mensaje del proceso de registro del movimiento realizado
	 */
	private String mensajeProceso;
	
	/**
	 * Estado en el que se encuentra el proceso de registro del movimiento
	 */
	private String estadoGuardarMovimiento;
	
	/**
	 * Testigo seleccionado en el proceso de Solicitud de Traslado a Caja
	 */
	private DtoUsuarioPersona testigo;
	
	/**
	 * Utilizado para conocer si el proceso que se esta realizando es una consulta de un movimiento
	 * ya registrado o un nuevo movimiento. Sirve para ocultar y mostrar componenentes de presentaci&oacute;n.
	 * 
	 */
	private boolean consulta;
	
	
	/**
	 * Utilizado para conocer si el consecutivo de Faltante / Sobrante esta parametrizado
	 */
	private boolean existeConsecutivoFaltante;
	
	
	/**
	 * Utilizado para conocer si se debe tomar la hora y la fecha que trae el movimiento como las de generacion
	 */
	private boolean esConsultaConsolidadoCierre;
	
	
	/**
	 * Campo Observación utilizado en los registros de Arqueo Entrega Parcial (Entrega a Caja Mayor / Principal o Entrega
	 * a Transportadora de Valores) y en la Solicitud de Traslado a Caja de Recaudo
	 */
	private String observacion;
	
	
	/**
	 * Atributo que contiene la &uacute;ltima fecha registrada en el sistema
	 * para un movimiento de caja
	 */
	private long fechaUltimoMovimiento;
	
	/**
	 * Atributo con el Turno de Caja activo para la caja y el cajero 
	 * seleccionado por el usuario
	 */
	private TurnoDeCaja turnoDeCaja;
	

	/**
	 * Atributo con el que se indica si se deben inhabilitar o no
	 * los listados de caja y cajero respectivamente
	 */
	private boolean inhabilitaListados;
	
	/**
	 * Atributo con el que se indica si se deben inhabilitar o no
	 * la fecha de arqueo
	 */
	private boolean inhabilitaFechaArqueo;
	
	/**
	 * Atributo para almacenar la lista de tipos de reporte 
	 */
	private ArrayList<DtoIntegridadDominio> listadoTiposReporte;
	
	/**
	 * Atributo que almacena el tipo de reporte seleccionado
	 */
	private String tipoReporte;
	
	/**
	 * Atributo que contiene la información del titulo, encabezado del reporte
	 */
	private DtoFiltroReportesArqueosCierres dtoFiltroReporte;
	
	/**
	 * Atributo que almacena el nombre del reporte generado
	 */
	private String nombreArchivo;
	
	/**
	 * Lista de dto con datos de cajas cajeros parametrizados
	 */
	private List<DtoCajaCajeros> listadoCajaCajeroParametrizado;
	
	/**
	 *Datos de cajas  
	 */
	private List<DtoCajaCajeros> listadoDatosCaja; 
	
	
	/**
	 * 
	 */
	private String esDescendente;
	
	/**
	 * 
	 */
	private String patronOrdenar;
	
	/**
	 * Atributo que almacena la lista de nombres de reportes
	 * a imprimir
	 */
	private ArrayList<String> listaNombresReportes;
	
	/**
	 * Indica si el arqueo caja fue generado o no
	 */
	private boolean exito;
	
	
	/**
	 *Código del cajero que viene del jsp  
	 */
	private Integer codigoCajeroHelper;
	
	/**
	 * Atributo que valida si documentos generados en el arqueo
	 */
	private boolean existenDocumentos;
	
	/**
	 * Constructor de la clase
	 */
	public MovimientosCajaForm() {
		this.mostrarParaSeccionEspecial = "";
		this.setConsulta(false);
		//this.estadoGuardarMovimiento = "";
		this.estadoGuardarMovimiento = "";
		this.setMensajeProceso("");
		this.setExisteConsecutivoFaltante(true);
		this.observacion = "";
		this.fechaUltimoMovimiento = ConstantesBD.codigoNuncaValidoLong;
		this.setTurnoDeCaja(null);
		this.setFechaArqueo(null);
		this.setConsolidadoMovimientoDTO(new DtoConsolidadoMovimiento());
		this.setInhabilitaListados(true);
		this.setInhabilitaFechaArqueo(true);
		this.listadoTiposArqueo = new ArrayList<TiposMovimientoCaja>();
		this.setListadoTiposReporte(new ArrayList<DtoIntegridadDominio>());
		this.tipoReporte="";
		this.setDtoFiltroReporte(new DtoFiltroReportesArqueosCierres());
		this.nombreArchivo="";
		this.listadoCajaCajeroParametrizado = new ArrayList<DtoCajaCajeros>();
		this.listadoDatosCaja= new ArrayList<DtoCajaCajeros>();
		this.esDescendente="";
		this.patronOrdenar="";
		this.setListaNombresReportes(new ArrayList<String>());
		this.exito=false;
		this.codigoCajeroHelper= new Integer(0);
		this.setExistenDocumentos(false);
	}
	
	/**
	 * Atributo de control para manejo de funcionalidad seleccionada
	 */
	private int tipoFuncionalidad;

	
	/**
	 * M&eacute;todo donde se centralizan las validaciones relacionadas con los 
	 * movimientos de caja.
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping,	HttpServletRequest request) {
		/*
		 * Almacenamiento de los errores encontrados
		 */
		ActionErrors errores = new ActionErrors();

		if (UtilidadTexto.isEmpty(estado)) {
			errores.add("estado invalido", new ActionMessage(
					"errors.estadoInvalido"));
			return errores;

		} else if (estado.equals("generarArqueo")
				|| estado.equals("redireccion")) {

			if (getTipoArqueoHelper() == ConstantesBD.codigoNuncaValido
					|| getTipoArqueo() == null) {
				errores.add("valor requerido", new ActionMessage(
						"errors.required", "El Tipo de Arqueo"));
			}
			
			if(getTipoArqueo() != null && getTipoArqueo().getCodigo() == ConstantesBD.codigoTipoMovimientoArqueoCierreTurno){
				
				if(getCajaMayorPrincipal()==null){
							
					errores.add("valor requerido", new ActionMessage("errors.required", "La Caja Mayor / Principal"));
				}
			}

			if (getCajero() == null) {
				errores.add("valor requerido", new ActionMessage(
						"errors.required", "El Cajero"));
			}

			if (getCaja() == null) {
				errores.add("valor requerido", new ActionMessage(
						"errors.required", "La Caja"));
			}

			if(getTipoArqueo().getCodigo()==ConstantesBD.codigoTipoMovimientoArqueoCierreTurno || 
					getTipoArqueo().getCodigo()==ConstantesBD.codigoTipoMovimientoArqueoEntregaParcial){
				
				if (getFechaArqueo() == null) {
					errores.add("valor requerido", new ActionMessage(
							"errors.required", "La Fecha"));
				}
			}
		}
		
		return errores;
	}

	/**
	 * M&eacute;todo para limpiar los atributos del formulario
	 */
	public void reset() {
		this.setListadoCajasPrincipalMayor(null);
		this.setConsolidadoMovimientoDTO(null);
		this.listadoCajasRecaudo = null;
		this.listadoTiposArqueo = null;
		this.listadoTestigos = null;
		this.listadoCajeros = null;
		this.listadoCajas = null;
		this.tipoArqueo = null;
		this.cajero = null;
		this.caja = null;
		this.parametroRequiereTestigo = false;
		this.existeConsecutivoFaltante = true;
		this.esConsultaConsolidadoCierre = false;
		this.setFechaUltimoMovimiento(ConstantesBD.codigoNuncaValidoLong);
		this.setTurnoDeCaja(null);
		this.setFechaArqueo(null);
		this.codigoCajeroHelper= new Integer(0);
		this.setExistenDocumentos(false);
	}

	/**
	 * M&eacute;todo que limpia el tipo de arqueo que fue seleccionado previamente
	 */
	public void resetTipoArqueo() {

		this.tipoArqueo = new TiposMovimientoCaja();
		this.tipoArqueo.setCodigo(ConstantesBD.codigoNuncaValido);
	}


	/**
	 * M&eacute;todo que limpia los listados de la p&aaute;gina inicial
	 */
	public void resetListados() {

		this.setCaja(null);
		this.setCajaHelper(ConstantesBD.codigoNuncaValido);
		this.setListadoCajas(null);
		this.setCajero(null);
		this.setCajaMayorPrincipal(null);
	}
	
	/**
	 * 
	 * Helper que retorna el c&oacute;digo del Tipo de Arqueo seleccionado
	 * 
	 * @return int con el c&oacute;digo del Tipo de Arqueo seleccionado
	 */
	public int getTipoArqueoHelper() {

		if (tipoArqueo == null) {
			return ConstantesBD.codigoNuncaValido;
		}
		return tipoArqueo.getCodigo();
	}


	/**
     *
	 * Helper utilizado para asignar el Tipo de Arqueo que fue seleccionado por el usuario
	 *
	 * @param codigoTipoArqueo
	 */
	public void setTipoArqueoHelper(int codigoTipoArqueo) {

		boolean asigno = false;

		for (TiposMovimientoCaja tiposMovimientoCaja : listadoTiposArqueo) {
			if (tiposMovimientoCaja.getCodigo() == codigoTipoArqueo) {
				asigno = true;
				this.tipoArqueo = tiposMovimientoCaja;
			}
		}

		if (!asigno) {

			this.setTipoArqueo(null);
		}
	}

	/**
	 * Helper utilizado para asignar el Cajero que fue seleccionado por el usuario
	 * 
	 * @param codigoCajero
	 */
	public void setCajeroHelper(int codigoCajero) {

		boolean asigno = false;
		setCodigoCajeroHelper(codigoCajero);
		for (DtoUsuarioPersona cajero : listadoCajeros) {

			if (cajero.getCodigo() == codigoCajero) {
				asigno = true;
				this.setCajero(cajero);
			}
		}

		if (!asigno) {

			this.setCajero(null);
		}
	}

	/**
	 * 
	 * Helper que retorna el c&oacute;digo del Cajero seleccionado
	 * 
	 * @return int con el c&oacute;digo del Cajero seleccionado
	 */
	public int getCajeroHelper() {

		if (cajero == null) {
			return ConstantesBD.codigoNuncaValido;
		}

		return cajero.getCodigo();
	}


	/**
	 * Helper utilizado para asignar la Caja que fue seleccionada por el usuario
	 * 
	 * @param consecutivoCaja
	 */
	public void setCajaHelper(int consecutivoCaja) {

		boolean asigno = false;

		if(consecutivoCaja != ConstantesBD.codigoNuncaValido){
			
			if (listadoCajas != null) {
				for (Cajas caja : listadoCajas) {
					if (caja.getConsecutivo() == consecutivoCaja) {
						asigno = true;
						this.setCaja(caja);
					}
				}
			}
		}
		
		if (!asigno) {

			this.setCaja(null);
		}
	}

	/**
	 * 
	 * Helper que retorna el consecutivo de la Caja seleccionada
	 * 
	 * @return int con el consecutivo de la Caja
	 */
	public int getCajaHelper() {

		if (caja == null) {
			return ConstantesBD.codigoNuncaValido;
		}

		return caja.getConsecutivo();
	}
	
	/**
	 * M&eacute;todo que retorna el nombre completo del cajero seleccionado
	 * @return
	 */
	public String getNombreCompletoCajero() {

//		cajero = consolidadoMovimientoDTO.getMovimientosCaja().getTurnoDeCaja()
//				.getUsuarios();
//
//		return cajero.getPersonas().getPrimerApellido() + " "
//				+ cajero.getPersonas().getSegundoApellido()
//				+ cajero.getPersonas().getPrimerNombre() + " "
//				+ cajero.getPersonas().getSegundoNombre() + " ("
//				+ cajero.getLogin() + ")";
		
		
		cajero = getCajero();

		String nombreCompletoCajero = "";
		
		if(cajero!=null){
			
			nombreCompletoCajero = cajero.getApellido() + " "
			+ (UtilidadTexto.isEmpty(cajero.getSegundoApellido())?"":cajero.getSegundoApellido())+ " "
			+ cajero.getNombre() + " "
			+ (UtilidadTexto.isEmpty(cajero.getSegundoNombre())?"":cajero.getSegundoNombre()) + " ("
			+ cajero.getLogin() + ")";
		}
		
		return nombreCompletoCajero;

	}


	/**
	 * Helper utilizado para asignar la Caja Caja Mayor / Principal que fue seleccionada por el usuario
	 * 
	 * @param consecutivoCaja
	 */
	public void setCajaPrincipalMayorHelper(int consecutivoCaja) {

		boolean asigno = false;

		if (listadoCajasPrincipalMayor != null) {
			for (Cajas cajaMayorPrincipal : listadoCajasPrincipalMayor) {
				if (cajaMayorPrincipal.getConsecutivo() == consecutivoCaja) {
					asigno = true;
					this.setCajaMayorPrincipal(cajaMayorPrincipal);
				}
			}
		}

		if (!asigno) {

			this.setCajaMayorPrincipal(null);
		}
	}

	/**
	 * 
	 * Helper que retorna el consecutivo de la Caja Mayor / Principal seleccionada
	 * 
	 * @return int con el consecutivo de la Caja Mayor / Principal
	 */
	public int getCajaPrincipalMayorHelper() {

		if (getCajaMayorPrincipal() == null) {
			return ConstantesBD.codigoNuncaValido;
		}

		return this.getCajaMayorPrincipal().getConsecutivo();
	}


	/**
	 * Helper utilizado para asignar la Caja de tipo Recaudo que fue seleccionada por el usuario
	 * 
	 * @param consecutivoCaja
	 */
	public void setCajaRecaudoHelper(int consecutivoCaja) {

		boolean asigno = false;

		if (listadoCajasRecaudo != null) {
			for (Cajas caja : listadoCajasRecaudo) {
				if (caja.getConsecutivo() == consecutivoCaja) {
					asigno = true;
					this.setCajaRecaudo(caja);
				}
			}
		}

		if (!asigno) {

			this.setCajaRecaudo(null);
		}
	}

	/**
	 * 
	 * Helper que retorna el consecutivo de la Caja de tipo Recaudo seleccionada
	 * 
	 * @return int con el consecutivo de la caja de tipo Recaudo
	 */
	public int getCajaRecaudoHelper() {

		if (cajaRecaudo == null) {
			return ConstantesBD.codigoNuncaValido;
		}

		return cajaRecaudo.getConsecutivo();
	}

	
	/**
	 * Helper utilizado para asignar el testigo seleccionado por el usuario
	 * 
	 * @param loginTestigo
	 */
	public void setTestigoHelper(String loginTestigo) {

		boolean asigno = false;
		
		if (listadoTestigos != null) {
			for (DtoUsuarioPersona testigo : listadoTestigos) {
				if (testigo.getLogin().equals(loginTestigo)) {
					asigno = true;
					this.setTestigo(testigo); 
				}
			}
		}

		if (!asigno) {

			this.setTestigo(null);
		}
	}

	/**
	 * Helper que retorna el login del testigo seleccionado
	 * 
	 * @return String con el login del testigo seleccionado
	 */
	public String getTestigoHelper() {

		if (getTestigo() == null) {
			return "";
		}

		return this.getTestigo().getLogin();
	}


	/**
	 * @param mensajeProceso the mensajeProceso to set
	 */
	public void setMensajeProceso(String mensajeProceso) {
		this.mensajeProceso = mensajeProceso;
	}

	/**
	 * @return the mensajeProceso
	 */
	public String getMensajeProceso() {
		return mensajeProceso;
	}

	/**
	 * @param existeConsecutivoFaltante the existeConsecutivoFaltante to set 
	 */
	public void setExisteConsecutivoFaltante(boolean existeConsecutivoFaltante) {
		this.existeConsecutivoFaltante = existeConsecutivoFaltante;
	}
	
	/**
	 * @return the existeConsecutivoFaltante
	 */
	public boolean isExisteConsecutivoFaltante() {
		return existeConsecutivoFaltante;
	}

	/**
	 * @param testigo the testigo to set
	 */
	public void setTestigo(DtoUsuarioPersona testigo) {
		this.testigo = testigo;
	}

	/**
	 * @return the testigo
	 */
	public DtoUsuarioPersona getTestigo() {
		return testigo;
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the listadoTiposArqueo
	 */
	public ArrayList<TiposMovimientoCaja> getListadoTiposArqueo() {
		return listadoTiposArqueo;
	}

	/**
	 * @param listadoTiposArqueo the listadoTiposArqueo to set
	 */
	public void setListadoTiposArqueo(
			ArrayList<TiposMovimientoCaja> listadoTiposArqueo) {
		this.listadoTiposArqueo = listadoTiposArqueo;
	}

	/**
	 * @return the listadoCajeros
	 */
	public List<DtoUsuarioPersona> getListadoCajeros() {
		return listadoCajeros;
	}

	/**
	 * @param listadoCajeros the listadoCajeros to set
	 */
	public void setListadoCajeros(List<DtoUsuarioPersona> listadoCajeros) {
		this.listadoCajeros = listadoCajeros;
	}

	/**
	 * @return the listadoCajas
	 */
	public ArrayList<Cajas> getListadoCajas() {
		return listadoCajas;
	}

	/**
	 * @param listadoCajas the listadoCajas to set
	 */
	public void setListadoCajas(ArrayList<Cajas> listadoCajas) {
		this.listadoCajas = listadoCajas;
	}

	/**
	 * @return the listadoCajasRecaudo
	 */
	public ArrayList<Cajas> getListadoCajasRecaudo() {
		return listadoCajasRecaudo;
	}

	/**
	 * @param listadoCajasRecaudo the listadoCajasRecaudo to set
	 */
	public void setListadoCajasRecaudo(ArrayList<Cajas> listadoCajasRecaudo) {
		this.listadoCajasRecaudo = listadoCajasRecaudo;
	}

	/**
	 * @return the tipoArqueo
	 */
	public TiposMovimientoCaja getTipoArqueo() {
		return tipoArqueo;
	}

	/**
	 * @param tipoArqueo the tipoArqueo to set
	 */
	public void setTipoArqueo(TiposMovimientoCaja tipoArqueo) {
		this.tipoArqueo = tipoArqueo;
	}

	/**
	 * @return the cajero
	 */
	public DtoUsuarioPersona getCajero() {
		return cajero;
	}

	/**
	 * @param cajero the cajero to set
	 */
	public void setCajero(DtoUsuarioPersona cajero) {
		this.cajero = cajero;
	}

	/**
	 * @return the caja
	 */
	public Cajas getCaja() {
		return caja;
	}

	/**
	 * @param caja the caja to set
	 */
	public void setCaja(Cajas caja) {
		this.caja = caja;
	}

	/**
	 * @return the fechaArqueo
	 */
	public Date getFechaArqueo() {
		return fechaArqueo;
	}

	/**
	 * @param fechaArqueo the fechaArqueo to set
	 */
	public void setFechaArqueo(Date fechaArqueo) {

		this.fechaArqueo = fechaArqueo;
	}

	/**
	 * @return the consolidadoMovimientoDTO
	 */
	public DtoConsolidadoMovimiento getConsolidadoMovimientoDTO() {
		return consolidadoMovimientoDTO;
	}

	/**
	 * @param consolidadoMovimientoDTO the consolidadoMovimientoDTO to set
	 */
	public void setConsolidadoMovimientoDTO(
			DtoConsolidadoMovimiento consolidadoMovimientoDTO) {
		this.consolidadoMovimientoDTO = consolidadoMovimientoDTO;
	}

	/**
	 * @return the listadoCajasPrincipalMayor
	 */
	public ArrayList<Cajas> getListadoCajasPrincipalMayor() {
		return listadoCajasPrincipalMayor;
	}

	/**
	 * @param listadoCajasPrincipalMayor the listadoCajasPrincipalMayor to set
	 */
	public void setListadoCajasPrincipalMayor(
			ArrayList<Cajas> listadoCajasPrincipalMayor) {
		this.listadoCajasPrincipalMayor = listadoCajasPrincipalMayor;
	}

	/**
	 * @return the mostrarParaSeccionEspecial
	 */
	public String getMostrarParaSeccionEspecial() {
		return mostrarParaSeccionEspecial;
	}

	/**
	 * @param mostrarParaSeccionEspecial the mostrarParaSeccionEspecial to set
	 */
	public void setMostrarParaSeccionEspecial(String mostrarParaSeccionEspecial) {
		this.mostrarParaSeccionEspecial = mostrarParaSeccionEspecial;
	}

	/**
	 * @return the estadoMostrarCuadreCaja
	 */
	public String getEstadoMostrarCuadreCaja() {
		return estadoMostrarCuadreCaja;
	}

	/**
	 * @param estadoMostrarCuadreCaja the estadoMostrarCuadreCaja to set
	 */
	public void setEstadoMostrarCuadreCaja(String estadoMostrarCuadreCaja) {
		this.estadoMostrarCuadreCaja = estadoMostrarCuadreCaja;
	}

	/**
	 * @return the mostrarCuadreCaja
	 */
	public boolean isMostrarCuadreCaja() {
		return mostrarCuadreCaja;
	}

	/**
	 * @param mostrarCuadreCaja the mostrarCuadreCaja to set
	 */
	public void setMostrarCuadreCaja(boolean mostrarCuadreCaja) {
		this.mostrarCuadreCaja = mostrarCuadreCaja;
	}

	/**
	 * @return the institucionBasica
	 */
	public InstitucionBasica getInstitucionBasica() {
		return institucionBasica;
	}

	/**
	 * @param institucionBasica the institucionBasica to set
	 */
	public void setInstitucionBasica(InstitucionBasica institucionBasica) {
		this.institucionBasica = institucionBasica;
	}

	/**
	 * @return the cajaMayorPrincipal
	 */
	public Cajas getCajaMayorPrincipal() {
		return cajaMayorPrincipal;
	}

	/**
	 * @param cajaMayorPrincipal the cajaMayorPrincipal to set
	 */
	public void setCajaMayorPrincipal(Cajas cajaMayorPrincipal) {
		this.cajaMayorPrincipal = cajaMayorPrincipal;
	}

	/**
	 * @return the cajaRecaudo
	 */
	public Cajas getCajaRecaudo() {
		return cajaRecaudo;
	}

	/**
	 * @param cajaRecaudo the cajaRecaudo to set
	 */
	public void setCajaRecaudo(Cajas cajaRecaudo) {
		this.cajaRecaudo = cajaRecaudo;
	}

	/**
	 * @return the propiedad
	 */
	public String getPropiedad() {
		return propiedad;
	}

	/**
	 * @param propiedad the propiedad to set
	 */
	public void setPropiedad(String propiedad) {
		this.propiedad = propiedad;
	}

	/**
	 * @return the listadoTestigos
	 */
	public ArrayList<DtoUsuarioPersona> getListadoTestigos() {
		return listadoTestigos;
	}

	/**
	 * @param listadoTestigos the listadoTestigos to set
	 */
	public void setListadoTestigos(ArrayList<DtoUsuarioPersona> listadoTestigos) {
		this.listadoTestigos = listadoTestigos;
	}

	/**
	 * @return the parametroRequiereTestigo
	 */
	public boolean isParametroRequiereTestigo() {
		return parametroRequiereTestigo;
	}

	/**
	 * @param parametroRequiereTestigo the parametroRequiereTestigo to set
	 */
	public void setParametroRequiereTestigo(boolean parametroRequiereTestigo) {
		this.parametroRequiereTestigo = parametroRequiereTestigo;
	}

	/**
	 * @return the estadoGuardarMovimiento
	 */
	public String getEstadoGuardarMovimiento() {
		return estadoGuardarMovimiento;
	}

	/**
	 * @param estadoGuardarMovimiento the estadoGuardarMovimiento to set
	 */
	public void setEstadoGuardarMovimiento(String estadoGuardarMovimiento) {
		this.estadoGuardarMovimiento = estadoGuardarMovimiento;
	}

	/**
	 * @return the consulta
	 */
	public boolean isConsulta() {
		return consulta;
	}

	/**
	 * @param consulta the consulta to set
	 */
	public void setConsulta(boolean consulta) {
		this.consulta = consulta;
	}

	/**
	 * @return the tipoFuncionalidad
	 */
	public int getTipoFuncionalidad() {
		return tipoFuncionalidad;
	}

	/**
	 * @param tipoFuncionalidad the tipoFuncionalidad to set
	 */
	public void setTipoFuncionalidad(int tipoFuncionalidad) {
		this.tipoFuncionalidad = tipoFuncionalidad;
	}

	public boolean isEsConsultaConsolidadoCierre() {
		return esConsultaConsolidadoCierre;
	}

	public void setEsConsultaConsolidadoCierre(boolean esConsultaConsolidadoCierre) {
		this.esConsultaConsolidadoCierre = esConsultaConsolidadoCierre;
	}

	/**
	 * @return the observacion
	 */
	public String getObservacion() {
		return observacion;
	}

	/**
	 * @param observacion the observacion to set
	 */
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	/**
	 * @param fechaUltimoMovimiento the fechaUltimoMovimiento to set
	 */
	public void setFechaUltimoMovimiento(long fechaUltimoMovimiento) {
		this.fechaUltimoMovimiento = fechaUltimoMovimiento;
	}

	/**
	 * @return the fechaUltimoMovimiento
	 */
	public long getFechaUltimoMovimiento() {
		return fechaUltimoMovimiento;
	}

	/**
	 * @param turnoDeCaja the turnoDeCaja to set
	 */
	public void setTurnoDeCaja(TurnoDeCaja turnoDeCaja) {
		this.turnoDeCaja = turnoDeCaja;
	}

	/**
	 * @return the turnoDeCaja
	 */
	public TurnoDeCaja getTurnoDeCaja() {
		return turnoDeCaja;
	}

	/**
	 * @param inhabilitaListados the inhabilitaListados to set
	 */
	public void setInhabilitaListados(boolean inhabilitaListados) {
		this.inhabilitaListados = inhabilitaListados;
	}

	/**
	 * @return the inhabilitaListados
	 */
	public boolean isInhabilitaListados() {
		return inhabilitaListados;
	}

	public void setListadoTiposReporte(ArrayList<DtoIntegridadDominio> listadoTiposReporte) {
		this.listadoTiposReporte = listadoTiposReporte;
	}

	public ArrayList<DtoIntegridadDominio> getListadoTiposReporte() {
		return listadoTiposReporte;
	}

	public void setTipoReporte(String tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	public String getTipoReporte() {
		return tipoReporte;
	}

	public void setDtoFiltroReporte(DtoFiltroReportesArqueosCierres dtoFiltroReporte) {
		this.dtoFiltroReporte = dtoFiltroReporte;
	}

	public DtoFiltroReportesArqueosCierres getDtoFiltroReporte() {
		return dtoFiltroReporte;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	/**
	 * @return the listadoCajaCajeroParametrizado
	 */
	public List<DtoCajaCajeros> getListadoCajaCajeroParametrizado() {
		return listadoCajaCajeroParametrizado;
	}

	/**
	 * @param listadoCajaCajeroParametrizado the listadoCajaCajeroParametrizado to set
	 */
	public void setListadoCajaCajeroParametrizado(
			List<DtoCajaCajeros> listadoCajaCajeroParametrizado) {
		this.listadoCajaCajeroParametrizado = listadoCajaCajeroParametrizado;
	}

	/**
	 * @return the listadoDatosCaja
	 */
	public List<DtoCajaCajeros> getListadoDatosCaja() {
		return listadoDatosCaja;
	}

	/**
	 * @param listadoDatosCaja the listadoDatosCaja to set
	 */
	public void setListadoDatosCaja(List<DtoCajaCajeros> listadoDatosCaja) {
		this.listadoDatosCaja = listadoDatosCaja;
	}

	/**
	 * @return the esDescendente
	 */
	public String getEsDescendente() {
		return esDescendente;
	}

	/**
	 * @param esDescendente the esDescendente to set
	 */
	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}

	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}
	
	public void setListaNombresReportes(ArrayList<String> listaNombresReportes) {
		this.listaNombresReportes = listaNombresReportes;
	}

	public ArrayList<String> getListaNombresReportes() {
		return listaNombresReportes;
	}

	public void setExito(boolean exito) {
		this.exito = exito;
	}

	public boolean isExito() {
		return exito;
	}

	/**
	 * @return the codigoCajeroHelper
	 */
	public Integer getCodigoCajeroHelper() {
		return codigoCajeroHelper;
	}

	/**
	 * @param codigoCajeroHelper the codigoCajeroHelper to set
	 */
	public void setCodigoCajeroHelper(Integer codigoCajeroHelper) {
		this.codigoCajeroHelper = codigoCajeroHelper;
	}

	/**
	 * @param inhabilitaFechaArqueo the inhabilitaFechaArqueo to set
	 */
	public void setInhabilitaFechaArqueo(boolean inhabilitaFechaArqueo) {
		this.inhabilitaFechaArqueo = inhabilitaFechaArqueo;
	}

	/**
	 * @return the inhabilitaFechaArqueo
	 */
	public boolean isInhabilitaFechaArqueo() {
		return inhabilitaFechaArqueo;
	}

	/**
	 * @param existenDocumentos the existenDocumentos to set
	 */
	public void setExistenDocumentos(boolean existenDocumentos) {
		this.existenDocumentos = existenDocumentos;
	}

	/**
	 * @return the existenDocumentos
	 */
	public boolean isExistenDocumentos() {
		return existenDocumentos;
	}
	
	
	
}
