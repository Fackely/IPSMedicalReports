package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Errores;
import util.UtilidadTexto;
import util.Errores.Tipo;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.consultaExterna.DtoConceptoFacturaVaria;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.facturasVarias.DtoFacturaVaria;
import com.princetonsa.dto.odontologia.DtoBeneficiarioCliente;
import com.princetonsa.dto.odontologia.DtoEmisionTarjetaCliente;
import com.princetonsa.dto.odontologia.DtoParentesco;
import com.princetonsa.dto.odontologia.DtoTarjetaCliente;
import com.princetonsa.dto.odontologia.DtoVentaTarjetasCliente;
import com.princetonsa.dto.tesoreria.DtoBonoSerialValor;
import com.princetonsa.dto.tesoreria.DtoDetallePagosBonos;
import com.princetonsa.dto.tesoreria.DtoEntidadesFinancieras;
import com.princetonsa.dto.tesoreria.DtoFormaPago;
import com.princetonsa.dto.tesoreria.DtoInformacionFormaPago;
import com.princetonsa.dto.tesoreria.DtoTarjetasFinancieras;
import com.princetonsa.dto.tesoreria.DtoTipoDetalleFormaPago;
import com.princetonsa.mundo.Usuario;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.orm.TiposIdentificacion;
import com.servinte.axioma.servicio.fabrica.TesoreriaFabricaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IFormasPagoServicio;

/**
 * Intercambio datos control y vista
 * @author Edgar Carvajal, Juan David Ramírez
 * @since 27 Ago 2010
 *
 */
@SuppressWarnings("serial")
public class VentaTarjetaClienteForm  extends ActionForm  {

	/**
	 * Estados para controlar el flujo de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Atributo de Presentación
	 * Sirve para validar si carga la sección Comprador después de hacer un submit
	 */
	private boolean cargarTipoVentaPersonalFamilia;
	
	/**
	 * Dto deudor
	 * Atributo de Presentación
	 * se asigna como deudor porque tiene que construirse como deudor
	 */
	private DtoPersonas dtoCompradorTarjeta;
	
	/**
	 * Atributo para apilar las lista de convenios tarifa Venta
	 */
	private List<DtoConvenio> listaConvenioTarifaVenta;

	/**
	 * Dto Convenio
	 * Atributo para almacenar el convenio seleccionado por el usuario
	 */
	private DtoConvenio convenioTarjeta;
	
	/* Seccion mensajes */
	/**
	 * Mensaje informativo de ingreso a la funcionalidad
	 * para indicar si se puede o no generar recibo de caja automático.
	 */
	private String mensajeInformativoIngreso="";

	/**
	 * Manejo de mensajes informativos sección información comprador
	 */
	private Errores msgInformacionComprador;

	/**
	 * Manejo de mensajes informativos sección información tipo tarjeta
	 */
	private Errores msgInformacionTipoTarjeta;

	/**
	 * Manejo de mensajes informativos sección información tipo tarjeta
	 */
	private Errores msgInformacionTarifa;

	/* Fin sección mensajes */
	
	/**
	 * Listado de las clases de venta (Empresarial, Personal, Familiar)
	 */
	private ArrayList<DtoIntegridadDominio> listaClaseVenta;
	
	/**
	 * Tipos de Identificación 
	 */
	private ArrayList<TiposIdentificacion> tiposIdentificacion = new ArrayList<TiposIdentificacion>();

	/**
	 * Mantener en memoria el colspan para poder mostrar correctamente las páginas
	 */
	private int colspan;

	/**
	 * Mantener en memoria el colspan para poder mostrar correctamente la
	 * sub sección de seriales y números de tarjeta de la sección
	 * venta tipo tarjeta
	 */
	private int colspanSeccionSerialesTarjeta;

	/**
	 * Permitir o bloquear la información del paciente
	 */
	private boolean permitirModificacionInformacionPaciente;

	/**  
	 * Tipos de tarjeta Cliente 
	 */
	private ArrayList<DtoTarjetaCliente> listaTarjetaCliente= new ArrayList<DtoTarjetaCliente>();

	/**
	 * Dto para el control de las ventas de la tarjeta
	 */
	private DtoVentaTarjetasCliente dtoVentas;

	/**
	 * Lista de usuarios del sistema
	 */
	private ArrayList<Usuario> listaUsuarios = new ArrayList<Usuario>();

	/**
	 * Listado de los parentescos para los beneficiarios de la tarjeta
	 */
	private ArrayList<DtoParentesco> listaParentesco;

	/**
	 * Mostrar o no el campo Serial
	 */
	private boolean mostrarCampoSerial;

	/**
	 * Mostrar o no el campo Número de Tarjeta
	 */
	private boolean mostrarCampoNroTarjeta;
	
	/**
	 * Listado de las emisiones de tarjeta cliente, utilizado
	 * para validar los seriales ingresados, se filtran
	 * por tipo de tarjeta y centro de atención
	 */
	private ArrayList<DtoEmisionTarjetaCliente> listaEmisionTarjeta;
	
	// Focus por seccion
	/**
	 * Atributo para definir el focus de la sección tipo tarjeta
	 */
	private String focusSeccionTipoTarjeta;
	
	/**
	 * Factura varia
	 */
	private DtoFacturaVaria dtoFacturaVaria;
	
	/**
	 * Lista de conceptos facturas varias;
	 */
	private ArrayList<DtoConceptoFacturaVaria> listaConceptos;
	
	/**
	 * Atributo de control que permite cancelar el proceso en cualquier momento
	 * del flujo
	 */
	private boolean cancelarProceso;
	
	/**
	 * Índice del beneficiario seleccionado
	 */
	private int indexBeneficiario;
		
	/**
	 * Lista de las formas de pago exustentes en el sistema
	 */
	private ArrayList<DtoFormaPago> listaFormasPago;
		
	/**
	 * Lista de cuidades
	 */
	private ArrayList<Ciudades> listaCiudades;
	
	/**
	 * Lista de las entidades financieras
	 */
	private ArrayList<DtoTarjetasFinancieras> listaTarjetasFinancieras;
	
	/**
	 * Lista de las entidades financieras existentes
	 */
	private ArrayList<DtoEntidadesFinancieras> listaEntidadesFinancieras;
	
	/**
	 * Indica si se va a mostrar la página de venta como resumen
	 */
	private boolean esResumen;
		
	
	/**
	 * Atributo que indica si se debe mostrar o no la
	 * sección de Formas de Pago.
	 * 
	 */
	private boolean turnoAbierto;
	
	public ArrayList<DtoFormaPago> getListaFormasPago() {
		return listaFormasPago;
	}


	public void setListaFormasPago(ArrayList<DtoFormaPago> listaFormasPago) {
		this.listaFormasPago = listaFormasPago;
	}


	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errores=new ActionErrors();
		// Validaciones estado guardar
		if(estado.equals("guardar"))
		{
			if(dtoVentas.getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioPersona))
			{
				this.validarVentaPersonal(errores);
			}
			if(dtoVentas.getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioFamiliar))
			{
				this.validarVentaFamiliar(errores);
			}
			int indice=0;
			for(DtoBeneficiarioCliente beneficiario:dtoVentas.getListaBeneficiarios())
			{
				indice++;
				if(!UtilidadTexto.isEmpty(beneficiario.getDtoPersonas().getTipoIdentificacion()))
				{
					MessageResources mensajes=MessageResources.getMessageResources("com.servinte.mensajes.odontologia.VentaTarjetaClienteForm");
					if(UtilidadTexto.isEmpty(beneficiario.getDtoPersonas().getNumeroIdentificacion()))
					{
						errores.add("num_id_beneficiario", new ActionMessage("errors.notEspecific", mensajes.getMessage("VentaTarjetaClienteForm.error.numeroIdBeneficiario", mensajes.getMessage("VentaTarjetaClienteForm.numeroIdentificacionBeneficiario"), indice)));
					}
				}
			}
		}
		return errores;
	}
	
	
//	/**
//	 *
//	 * @return
//	 */
//	private boolean existeConceptoIngresoFactura() {
//		
//		
//		
//		if(UtilidadTexto.isEmpty(ValoresPorDefecto.getConceptoIngresoFacturasVarias(2)))
//		{
//			return false;
//		}
//		
//		return true;
//	}

	/**
	 * Validación campos venta personal
	 * @param errores Errores de las validaciones
	 */
	private void validarVentaFamiliar(ActionErrors errores) {
		MessageResources mensajes=MessageResources.getMessageResources("com.servinte.mensajes.odontologia.VentaTarjetaClienteForm");
		this.validacionesGenerales(errores, mensajes);

		// Valor unitario
		
		// Cantidad de tarjetas
		if(dtoVentas.getCantidad()<1)
		{
			errores.add("cantidad", new ActionMessage("errors.required", mensajes.getMessage("VentaTarjetaClienteForm.cantidad")));
		}
		
		// Valor total
		
		// Existencia de un (1) beneficiario
		if(dtoVentas.getListaBeneficiarios().size()==0)
		{
			errores.add("beneficiarios", new ActionMessage("errors.notEspecific", mensajes.getMessage("VentaTarjetaClienteForm.error.beneficiariosRequeridos")));
		}
		else
		{
			for(DtoBeneficiarioCliente beneficiario:dtoVentas.getListaBeneficiarios())
			{
				// Se verifica si se tiene serial o número de tarjeta
				boolean ingresoSerialOTarjeta=false;
				boolean esSerial=false;
				if(!UtilidadTexto.isEmpty(beneficiario.getSerial()))
				{
					esSerial=true;
					ingresoSerialOTarjeta=true;
				}
				if(!ingresoSerialOTarjeta)
				{
					if(!UtilidadTexto.isEmpty(beneficiario.getNumTarjeta()))
					{
						ingresoSerialOTarjeta=true;
					}
				}
				if(!ingresoSerialOTarjeta)
				{
					if(mostrarCampoSerial && mostrarCampoNroTarjeta)
					{
						errores.add("serialOTarjeta", new ActionMessage("errors.notEspecific",mensajes.getMessage("VentaTarjetaClienteForm.error.requerido.serialOTarjeta")));
						break;
					}
					else
					{
						if(mostrarCampoSerial)
						{
							errores.add("serial", new ActionMessage("errors.notEspecific",mensajes.getMessage("VentaTarjetaClienteForm.error.requerido.serial")));
							break;
						}
						if(mostrarCampoNroTarjeta)
						{
							errores.add("tarjeta", new ActionMessage("errors.notEspecific",mensajes.getMessage("VentaTarjetaClienteForm.error.requerido.tarjeta")));
							break;
						}
					}
				}
				ActionMessage error=this.validarSerialesTarjetasRepetidos(esSerial, mensajes);
				if(error!=null)
				{
					errores.add("seriales repetidos", error);
				}
			}
		}
	}

	/**
	 * Valida si hay seriales repetidos
	 * @param esSerial indica si el campo es serial o no
	 * @param mensajes 
	 */
	private ActionMessage validarSerialesTarjetasRepetidos(boolean esSerial, MessageResources mensajes) {
		HashMap<String, String> seriales=new HashMap<String, String>();
		 
		for(DtoBeneficiarioCliente beneficiario:dtoVentas.getListaBeneficiarios())
		{
			if(esSerial)
			{
				if(seriales.containsKey(beneficiario.getSerial()))
				{
					return new ActionMessage("errors.notEspecific", mensajes.getMessage("VentaTarjetaClienteForm.error.serialRepetido", beneficiario.getSerial()));
				}
				seriales.put(beneficiario.getSerial(), beneficiario.getSerial());
			}
			else
			{
				if(seriales.containsKey(beneficiario.getNumTarjeta()))
				{
					return new ActionMessage("errors.notEspecific", mensajes.getMessage("VentaTarjetaClienteForm.error.numeroTarjetaRepetido", beneficiario.getNumTarjeta()));
				}
				seriales.put(beneficiario.getNumTarjeta(), beneficiario.getNumTarjeta());
			}
		}
		return null;
	}

	/**
	 * Validación campos venta personal
	 * @param errores Errores de las validaciones
	 */
	private void validarVentaPersonal(ActionErrors errores) {
		MessageResources mensajes=MessageResources.getMessageResources("com.servinte.mensajes.odontologia.VentaTarjetaClienteForm");
		this.validacionesGenerales(errores, mensajes);

		// Valor unitario
		
		// Cantidad de tarjetas
		if(dtoVentas.getCantidad()<1)
		{
			errores.add("cantidad", new ActionMessage("errors.required", mensajes.getMessage("VentaTarjetaClienteForm.cantidad")));
		}
		
		// Valor total
		
		// Existencia de un (1) beneficiario
		if(dtoVentas.getListaBeneficiarios().size()==0)
		{
			errores.add("beneficiarios", new ActionMessage("errors.notEspecific", mensajes.getMessage("VentaTarjetaClienteForm.error.beneficiariosRequeridos")));
		}
		else
		{
			// Se verifica si se tiene serial o número de tarjeta
			DtoBeneficiarioCliente beneficiario=dtoVentas.getListaBeneficiarios().get(0);
			boolean ingresoSerialOTarjeta=false;
			if(!UtilidadTexto.isEmpty(beneficiario.getSerial()))
			{
				ingresoSerialOTarjeta=true;
			}
			if(!ingresoSerialOTarjeta)
			{
				if(!UtilidadTexto.isEmpty(beneficiario.getNumTarjeta()))
				{
					ingresoSerialOTarjeta=true;
				}
			}
			if(!ingresoSerialOTarjeta)
			{
				if(mostrarCampoSerial && mostrarCampoNroTarjeta)
				{
					errores.add("serialOTarjeta", new ActionMessage("errors.notEspecific",mensajes.getMessage("VentaTarjetaClienteForm.error.requerido.serialOTarjeta")));
				}
				else
				{
					if(mostrarCampoSerial)
					{
						errores.add("serial", new ActionMessage("errors.notEspecific",mensajes.getMessage("VentaTarjetaClienteForm.error.requerido.serial")));
					}
					if(mostrarCampoNroTarjeta)
					{
						errores.add("tarjeta", new ActionMessage("errors.notEspecific",mensajes.getMessage("VentaTarjetaClienteForm.error.requerido.tarjeta")));
					}
				}
			}
		}
	}

	/**
	 * Valida los campos generales para los 3 tipos de venta
	 * @param errores Objeto para almacenar los errores encontrados
	 * @param mensajes Fuente de mensajes para internacionalización
	 */
	private void validacionesGenerales(ActionErrors errores, MessageResources mensajes) {
		// tipo identificación
		if(UtilidadTexto.isEmpty(dtoCompradorTarjeta.getTipoIdentificacion())){
			errores.add("tipoIdentificacion", new ActionMessage("errors.required", mensajes.getMessage("VentaTarjetaClienteForm.tipoIdentificacion")));
		}
		
		// Número identificación
		if(UtilidadTexto.isEmpty(dtoCompradorTarjeta.getNumeroIdentificacion())){
			errores.add("numeroIdentificacion", new ActionMessage("errors.required", mensajes.getMessage("VentaTarjetaClienteForm.numeroIdentificacion")));
		}

		// Primer apellido
		if(UtilidadTexto.isEmpty(dtoCompradorTarjeta.getPrimerApellido())){
			errores.add("primerApellido", new ActionMessage("errors.required", mensajes.getMessage("VentaTarjetaClienteForm.primerApellido")));
		}

		// Primer nombre
		if(UtilidadTexto.isEmpty(dtoCompradorTarjeta.getPrimerNombre())){
			errores.add("primerNombre", new ActionMessage("errors.required", mensajes.getMessage("VentaTarjetaClienteForm.primerNombre")));
		}
		
		// Tipo de tarjeta
		if(dtoVentas.getTipoTarjeta()<1)
		{
			errores.add("tipoTarjeta", new ActionMessage("errors.required", mensajes.getMessage("VentaTarjetaClienteForm.tipoTarjeta")));
		}
		
		// Convenio de la tarjeta
		if(convenioTarjeta.getCodigo()<1)
		{
			errores.add("convenioTarjeta", new ActionMessage("errors.required", mensajes.getMessage("VentaTarjetaClienteForm.convenioTarifaVenta")));
		}

		// Contrato de la tarjeta
		if(dtoVentas.getContratoTarjeta().getCodigo()<1)
		{
			errores.add("contatoTarjeta", new ActionMessage("errors.required", mensajes.getMessage("VentaTarjetaClienteForm.contrato")));
		}
		
		// Usuario Vendedor
		if(UtilidadTexto.isEmpty(dtoVentas.getUsuarioVendedor()))
		{
			errores.add("tarjeta", new ActionMessage("errors.required",mensajes.getMessage("VentaTarjetaClienteForm.usuarioVendedor")));
		}
		
		// Concepto factura varia
		if(dtoFacturaVaria.getConcepto()<=0)
		{
			errores.add("conceptoFactura", new ActionMessage("errors.required",mensajes.getMessage("VentaTarjetaClienteForm.conceptoFactura")));
		}

	}

	public String getFocusSeccionTipoTarjeta() {
		return focusSeccionTipoTarjeta;
	}

	public void setFocusSeccionTipoTarjeta(String focusSeccionTipoTarjeta) {
		this.focusSeccionTipoTarjeta = focusSeccionTipoTarjeta;
	}

	/**
	 * Limpiar la información de la venta
	 */
	public void reset(){
		this.cargarTipoVentaPersonalFamilia=false;
		this.dtoCompradorTarjeta=new DtoPersonas();

		this.dtoVentas=new DtoVentaTarjetasCliente();
		this.dtoFacturaVaria=new DtoFacturaVaria();
		this.tiposIdentificacion = new ArrayList<TiposIdentificacion>();
		
		this.convenioTarjeta=new DtoConvenio();

		// Sección mensajes
		this.mensajeInformativoIngreso=null;
		this.msgInformacionComprador=null;
		this.msgInformacionTipoTarjeta=null;
		// fin sección mensajes
		
		// Listas
		this.listaUsuarios = new ArrayList<Usuario>();
		this.listaClaseVenta=null;
		this.listaParentesco=new ArrayList<DtoParentesco>();
		this.listaEmisionTarjeta=null;
		this.listaConceptos=null;
		this.listaFormasPago=null;
		this.listaCiudades=null;
		
		this.colspan=0;
		this.colspanSeccionSerialesTarjeta=0;
		
		this.mostrarCampoSerial=false;
		this.mostrarCampoNroTarjeta=false;
		
		this.limpiarInformacionTipoTarjeta();
		this.listaConvenioTarifaVenta=null;
		
		this.indexBeneficiario=0;

		this.limpiarInformacionFormaPago();
		
		this.setTurnoAbierto(false);
	 }

	/**
	 * Limpiar la información de la venta
	 */
	public void resetSinLimpiarTipoVenta(){
		this.cargarTipoVentaPersonalFamilia=false;
		this.dtoCompradorTarjeta=new DtoPersonas();

		String tipoVenta=this.dtoVentas.getTipoVenta();
		this.dtoVentas=new DtoVentaTarjetasCliente();
		this.dtoVentas.setTipoVenta(tipoVenta);
		this.dtoFacturaVaria=new DtoFacturaVaria();
		this.tiposIdentificacion = new ArrayList<TiposIdentificacion>();
		
		this.convenioTarjeta=new DtoConvenio();

		// Sección mensajes
		this.mensajeInformativoIngreso=null;
		this.msgInformacionComprador=null;
		this.msgInformacionTipoTarjeta=null;
		// fin sección mensajes
		
		// Listas
		this.listaUsuarios = new ArrayList<Usuario>();
		this.listaClaseVenta=null;
		this.listaParentesco=new ArrayList<DtoParentesco>();
		this.listaEmisionTarjeta=null;
		this.listaConceptos=null;
		
		this.colspan=0;
		this.colspanSeccionSerialesTarjeta=0;
		
		this.mostrarCampoSerial=false;
		this.mostrarCampoNroTarjeta=false;
		
		this.limpiarInformacionTipoTarjeta();
		this.listaConvenioTarifaVenta=null;
		
		this.indexBeneficiario=0;

	 }

	/**
	 * Obtiene el valor del atributo estado
	 *
	 * @return Retorna atributo estado
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * Establece el valor del atributo estado
	 *
	 * @param valor para el atributo estado
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * Obtiene el valor del atributo cargarTipoVentaPersonalFamilia
	 *
	 * @return Retorna atributo cargarTipoVentaPersonalFamilia
	 */
	public boolean isCargarTipoVentaPersonalFamilia()
	{
		return cargarTipoVentaPersonalFamilia;
	}

	/**
	 * Establece el valor del atributo cargarTipoVentaPersonalFamilia
	 *
	 * @param valor para el atributo cargarTipoVentaPersonalFamilia
	 */
	public void setCargarTipoVentaPersonalFamilia(
			boolean cargarTipoVentaPersonalFamilia)
	{
		this.cargarTipoVentaPersonalFamilia = cargarTipoVentaPersonalFamilia;
	}

	/**
	 * Obtiene el valor del atributo dtoCompradorTarjeta
	 *
	 * @return Retorna atributo dtoCompradorTarjeta
	 */
	public DtoPersonas getDtoCompradorTarjeta()
	{
		return dtoCompradorTarjeta;
	}

	/**
	 * Establece el valor del atributo dtoCompradorTarjeta
	 *
	 * @param valor para el atributo dtoCompradorTarjeta
	 */
	public void setDtoCompradorTarjeta(DtoPersonas dtoCompradorTarjeta)
	{
		this.dtoCompradorTarjeta = dtoCompradorTarjeta;
	}

	/**
	 * Obtiene el valor del atributo listaConvenioTarifaVenta
	 *
	 * @return Retorna atributo listaConvenioTarifaVenta
	 */
	public List<DtoConvenio> getListaConvenioTarifaVenta()
	{
		return listaConvenioTarifaVenta;
	}

	/**
	 * Establece el valor del atributo listaConvenioTarifaVenta
	 *
	 * @param valor para el atributo listaConvenioTarifaVenta
	 */
	public void setListaConvenioTarifaVenta(
			List<DtoConvenio> listaConvenioTarifaVenta)
	{
		this.listaConvenioTarifaVenta = listaConvenioTarifaVenta;
	}

	/**
	 * Obtiene el valor del atributo convenioTarjeta
	 *
	 * @return Retorna atributo convenioTarjeta
	 */
	public DtoConvenio getConvenioTarjeta()
	{
		return convenioTarjeta;
	}

	/**
	 * Establece el valor del atributo convenioTarjeta
	 *
	 * @param valor para el atributo convenioTarjeta
	 */
	public void setConvenioTarjeta(DtoConvenio convenioTarjeta)
	{
		this.convenioTarjeta = convenioTarjeta;
	}

	/**
	 * Obtiene el valor del atributo mensajeInformativoIngreso
	 *
	 * @return Retorna atributo mensajeInformativoIngreso
	 */
	public String getMensajeInformativoIngreso()
	{
		return mensajeInformativoIngreso;
	}

	/**
	 * Establece el valor del atributo mensajeInformativoIngreso
	 *
	 * @param valor para el atributo mensajeInformativoIngreso
	 */
	public void setMensajeInformativoIngreso(String mensajeInformativoIngreso)
	{
		this.mensajeInformativoIngreso = mensajeInformativoIngreso;
	}

	/**
	 * Obtiene el valor del atributo msgInformacionComprador
	 *
	 * @return Retorna atributo msgInformacionComprador
	 */
	public Errores getMsgInformacionComprador()
	{
		return msgInformacionComprador;
	}

	/**
	 * Establece el valor del atributo msgInformacionComprador
	 *
	 * @param valor para el atributo msgInformacionComprador
	 */
	public void setMsgInformacionComprador(Errores msgInformacionComprador)
	{
		this.msgInformacionComprador = msgInformacionComprador;
	}

	/**
	 * Obtiene el valor del atributo msgInformacionTipoTarjeta
	 *
	 * @return Retorna atributo msgInformacionTipoTarjeta
	 */
	public Errores getMsgInformacionTipoTarjeta()
	{
		return msgInformacionTipoTarjeta;
	}

	/**
	 * Establece el valor del atributo msgInformacionTipoTarjeta
	 *
	 * @param valor para el atributo msgInformacionTipoTarjeta
	 */
	public void setMsgInformacionTipoTarjeta(Errores msgInformacionTipoTarjeta)
	{
		this.msgInformacionTipoTarjeta = msgInformacionTipoTarjeta;
	}

	/**
	 * Obtiene el valor del atributo listaClaseVenta
	 *
	 * @return Retorna atributo listaClaseVenta
	 */
	public ArrayList<DtoIntegridadDominio> getListaClaseVenta()
	{
		return listaClaseVenta;
	}

	/**
	 * Establece el valor del atributo listaClaseVenta
	 *
	 * @param valor para el atributo listaClaseVenta
	 */
	public void setListaClaseVenta(ArrayList<DtoIntegridadDominio> listaClaseVenta)
	{
		this.listaClaseVenta = listaClaseVenta;
	}

	/**
	 * Obtiene el valor del atributo tiposIdentificacion
	 *
	 * @return Retorna atributo tiposIdentificacion
	 */
	public ArrayList<TiposIdentificacion> getTiposIdentificacion()
	{
		return tiposIdentificacion;
	}

	/**
	 * Establece el valor del atributo tiposIdentificacion
	 *
	 * @param valor para el atributo tiposIdentificacion
	 */
	public void setTiposIdentificacion(
			ArrayList<TiposIdentificacion> tiposIdentificacion)
	{
		this.tiposIdentificacion = tiposIdentificacion;
	}

	/**
	 * Obtiene el valor del atributo colspan
	 *
	 * @return Retorna atributo colspan
	 */
	public int getColspan()
	{
		return colspan;
	}

	/**
	 * Establece el valor del atributo colspan
	 *
	 * @param valor para el atributo colspan
	 */
	public void setColspan(int colspan)
	{
		this.colspan = colspan;
	}

	/**
	 * Obtiene el valor del atributo permitirModificacionInformacionPaciente
	 *
	 * @return Retorna atributo permitirModificacionInformacionPaciente
	 */
	public boolean isPermitirModificacionInformacionPaciente()
	{
		return permitirModificacionInformacionPaciente;
	}

	/**
	 * Establece el valor del atributo permitirModificacionInformacionPaciente
	 *
	 * @param valor para el atributo permitirModificacionInformacionPaciente
	 */
	public void setPermitirModificacionInformacionPaciente(
			boolean permitirModificacionInformacionPaciente)
	{
		this.permitirModificacionInformacionPaciente = permitirModificacionInformacionPaciente;
	}

	/**
	 * Obtiene el valor del atributo listaTarjetaCliente
	 *
	 * @return Retorna atributo listaTarjetaCliente
	 */
	public ArrayList<DtoTarjetaCliente> getListaTarjetaCliente()
	{
		return listaTarjetaCliente;
	}

	/**
	 * Establece el valor del atributo listaTarjetaCliente
	 *
	 * @param valor para el atributo listaTarjetaCliente
	 */
	public void setListaTarjetaCliente(
			ArrayList<DtoTarjetaCliente> listaTarjetaCliente)
	{
		this.listaTarjetaCliente = listaTarjetaCliente;
	}

	/**
	 * Obtiene el valor del atributo dtoVentas
	 *
	 * @return Retorna atributo dtoVentas
	 */
	public DtoVentaTarjetasCliente getDtoVentas()
	{
		return dtoVentas;
	}

	/**
	 * Establece el valor del atributo dtoVentas
	 *
	 * @param valor para el atributo dtoVentas
	 */
	public void setDtoVentas(DtoVentaTarjetasCliente dtoVentas)
	{
		this.dtoVentas = dtoVentas;
	}

	/**
	 * Obtiene el valor del atributo listaUsuarios
	 *
	 * @return Retorna atributo listaUsuarios
	 */
	public ArrayList<Usuario> getListaUsuarios()
	{
		return listaUsuarios;
	}

	/**
	 * Establece el valor del atributo listaUsuarios
	 *
	 * @param valor para el atributo listaUsuarios
	 */
	public void setListaUsuarios(ArrayList<Usuario> listaUsuarios)
	{
		this.listaUsuarios = listaUsuarios;
	}

	/**
	 * Obtiene el valor del atributo listaParentesco
	 *
	 * @return Retorna atributo listaParentesco
	 */
	public ArrayList<DtoParentesco> getListaParentesco()
	{
		return listaParentesco;
	}

	/**
	 * Establece el valor del atributo listaParentesco
	 *
	 * @param valor para el atributo listaParentesco
	 */
	public void setListaParentesco(ArrayList<DtoParentesco> listaParentesco)
	{
		this.listaParentesco = listaParentesco;
	}

	/**
	 * Obtiene el valor del atributo mostrarCampoSerial
	 *
	 * @return Retorna atributo mostrarCampoSerial
	 */
	public boolean isMostrarCampoSerial()
	{
		return mostrarCampoSerial;
	}

	/**
	 * Establece el valor del atributo mostrarCampoSerial
	 *
	 * @param valor para el atributo mostrarCampoSerial
	 */
	public void setMostrarCampoSerial(boolean mostrarCampoSerial)
	{
		this.mostrarCampoSerial = mostrarCampoSerial;
	}

	/**
	 * Obtiene el valor del atributo mostrarCampoNroTarjeta
	 *
	 * @return Retorna atributo mostrarCampoNroTarjeta
	 */
	public boolean isMostrarCampoNroTarjeta()
	{
		return mostrarCampoNroTarjeta;
	}

	/**
	 * Establece el valor del atributo mostrarCampoNroTarjeta
	 *
	 * @param valor para el atributo mostrarCampoNroTarjeta
	 */
	public void setMostrarCampoNroTarjeta(boolean mostrarCampoNroTarjeta)
	{
		this.mostrarCampoNroTarjeta = mostrarCampoNroTarjeta;
	}

	/**
	 * Obtiene el valor del atributo listaEmisionTarjeta
	 *
	 * @return Retorna atributo listaEmisionTarjeta
	 */
	public ArrayList<DtoEmisionTarjetaCliente> getListaEmisionTarjeta()
	{
		return listaEmisionTarjeta;
	}

	/**
	 * Establece el valor del atributo listaEmisionTarjeta
	 *
	 * @param valor para el atributo listaEmisionTarjeta
	 */
	public void setListaEmisionTarjeta(
			ArrayList<DtoEmisionTarjetaCliente> listaEmisionTarjeta)
	{
		this.listaEmisionTarjeta = listaEmisionTarjeta;
	}

	/**
	 * Obtiene el valor del atributo colspanSeccionSerialesTarjeta
	 *
	 * @return Retorna atributo colspanSeccionSerialesTarjeta
	 */
	public int getColspanSeccionSerialesTarjeta()
	{
		return colspanSeccionSerialesTarjeta;
	}

	/**
	 * Establece el valor del atributo colspanSeccionSerialesTarjeta
	 *
	 * @param valor para el atributo colspanSeccionSerialesTarjeta
	 */
	public void setColspanSeccionSerialesTarjeta(int colspanSeccionSerialesTarjeta)
	{
		this.colspanSeccionSerialesTarjeta = colspanSeccionSerialesTarjeta;
	}
	
	/**
	 * Obtener el número de convenios disponibles para seleccionar
	 * en la sección Tipo Tarjeta
	 * @return cantidad de convenios
	 */
	public int getNumeroConvenios()
	{
		if(listaConvenioTarifaVenta==null)
		{
			return 0;
		}
		return listaConvenioTarifaVenta.size();
	}

	/**
	 * Limpia los mensajes de la sección tipo tarjeta
	 */
	public void limpiarMensajesInformacionTipoTarjeta()
	{
		this.msgInformacionTipoTarjeta=null;
		this.msgInformacionTarifa=null;
	}
	
	/**
	 * Limpia los campos de la sección tipo venta
	 */
	public void limpiarInformacionTipoTarjeta()
	{
		this.limpiarMensajesInformacionTipoTarjeta();
		this.listaConvenioTarifaVenta=null;
		this.dtoVentas.setValorUnitarioTarjeta(0);
		this.dtoVentas.setValorTotalTarjetas(0);
		this.dtoVentas.setCantidad(0);
		this.setConvenioTarjeta(new DtoConvenio());
	}
	
	/**
	 * Limpia los campos de la sección forma de pago
	 */
	public void limpiarInformacionFormaPago()
	{
		this.dtoVentas.setListaInformacionFormasPago(new ArrayList<DtoInformacionFormaPago>());
	}

	public DtoFacturaVaria getDtoFacturaVaria() {
		return dtoFacturaVaria;
	}

	public void setDtoFacturaVaria(DtoFacturaVaria dtoFacturaVaria) {
		this.dtoFacturaVaria = dtoFacturaVaria;
	}

	public ArrayList<DtoConceptoFacturaVaria> getListaConceptos() {
		return listaConceptos;
	}

	public void setListaConceptos(ArrayList<DtoConceptoFacturaVaria> listaConceptos) {
		this.listaConceptos = listaConceptos;
	}

	/**
	 * Obtiene el valor del atributo cancelarProceso
	 *
	 * @return Retorna atributo cancelarProceso
	 */
	public boolean isCancelarProceso()
	{
		return cancelarProceso;
	}

	/**
	 * Establece el valor del atributo cancelarProceso
	 *
	 * @param valor para el atributo cancelarProceso
	 */
	public void setCancelarProceso(boolean cancelarProceso)
	{
		this.cancelarProceso = cancelarProceso;
	}

	/**
	 * Obtiene el valor del atributo msgInformacionTarifa
	 *
	 * @return Retorna atributo msgInformacionTarifa
	 */
	public Errores getMsgInformacionTarifa()
	{
		return msgInformacionTarifa;
	}

	/**
	 * Establece el valor del atributo msgInformacionTarifa
	 *
	 * @param valor para el atributo msgInformacionTarifa
	 */
	public void setMsgInformacionTarifa(Errores msgInformacionTarifa)
	{
		this.msgInformacionTarifa = msgInformacionTarifa;
	}

	public int getIndexBeneficiario() {
		return indexBeneficiario;
	}

	public void setIndexBeneficiario(int indexBeneficiario) {
		this.indexBeneficiario = indexBeneficiario;
	}

	public ArrayList<Ciudades> getListaCiudades() {
		return listaCiudades;
	}

	public void setListaCiudades(ArrayList<Ciudades> listaCiudades) {
		this.listaCiudades = listaCiudades;
	}

	/**
	 * Acción para validar los cheques
	 * @return Errores
	 */
	public ActionErrors validarCheques() {
		ActionErrors errores=new ActionErrors();
		DtoInformacionFormaPago cheques=dtoVentas.getListaInformacionFormasPago().get(dtoVentas.getIndiceFormaPago());
		if(UtilidadTexto.isEmpty(cheques.getNumeroCheque())){
			errores.add("numeroCheque", new ActionMessage("errors.required", "Número de cheque"));
		}
		if(cheques.getCodigoBanco()<=0){
			errores.add("codigoBanco", new ActionMessage("errors.required", "Banco"));
		}
		if(UtilidadTexto.isEmpty(cheques.getFecha())){
			errores.add("fechaGiro", new ActionMessage("errors.required", "Fecha Giro"));
		}
		if(cheques.getValor()<=0){
			errores.add("Valor", new ActionMessage("errors.required", "Valor"));
		}
		if(UtilidadTexto.isEmpty(cheques.getNombre())){
			errores.add("nombreGirador", new ActionMessage("errors.required", "Nombre del Girador"));
		}
		if(errores.isEmpty())
		{
			cheques.setValido(true);
		}
		return errores;
	}


	public ArrayList<DtoEntidadesFinancieras> getListaEntidadesFinancieras() {
		return listaEntidadesFinancieras;
	}


	public void setListaEntidadesFinancieras(
			ArrayList<DtoEntidadesFinancieras> listaEntidadesFinancieras) {
		this.listaEntidadesFinancieras = listaEntidadesFinancieras;
	}

	
	public ArrayList<DtoTarjetasFinancieras> getListaTarjetasFinancieras() {
		return listaTarjetasFinancieras;
	}


	public void setListaTarjetasFinancieras(
			ArrayList<DtoTarjetasFinancieras> listaTarjetasFinancieras) {
		this.listaTarjetasFinancieras = listaTarjetasFinancieras;
	}

	/**
	 * Acción para validar los cheques
	 * @return Errores
	 */
	public ActionErrors validarTarjetas() {
		ActionErrors errores=new ActionErrors();
		DtoInformacionFormaPago tarjeta=dtoVentas.getListaInformacionFormasPago().get(dtoVentas.getIndiceFormaPago());
		MessageResources mensajes=MessageResources.getMessageResources("com.servinte.mensajes.odontologia.VentaTarjetaClienteForm");
		if(tarjeta.getCodigoTarjetaFinanciera()<=0)
		{
			errores.add("tarjetaFinanciera", new ActionMessage("errors.required", mensajes.getMessage("VentaTarjetaClienteForm.formasPago.tarjetas.codigoTarjeta")));
		}
		if(UtilidadTexto.isEmpty(tarjeta.getNumeroTarjeta()))
		{
			errores.add("numeroTarjeta", new ActionMessage("errors.required", mensajes.getMessage("VentaTarjetaClienteForm.formasPago.tarjetas.numeroTarjeta")));
		}
		if(UtilidadTexto.isEmpty(tarjeta.getNumeroAutorizacion()))
		{
			errores.add("numeroAutorizacion", new ActionMessage("errors.required", mensajes.getMessage("VentaTarjetaClienteForm.formasPago.tarjetas.numeroAutorizacion")));
		}
		if(UtilidadTexto.isEmpty(tarjeta.getNumeroComprobante()))
		{
			errores.add("numeroComprobante", new ActionMessage("errors.required", mensajes.getMessage("VentaTarjetaClienteForm.formasPago.tarjetas.numeroComprobante")));
		}
		if(UtilidadTexto.isEmpty(tarjeta.getFecha()))
		{
			errores.add("fecha", new ActionMessage("errors.required", mensajes.getMessage("VentaTarjetaClienteForm.formasPago.tarjetas.fecha")));
		}
		if(tarjeta.getValor()<=0)
		{
			errores.add("valor", new ActionMessage("errors.required", mensajes.getMessage("VentaTarjetaClienteForm.formasPago.tarjetas.valor")));
		}
		if(UtilidadTexto.isEmpty(tarjeta.getNombre()))
		{
			errores.add("girador", new ActionMessage("errors.required", mensajes.getMessage("VentaTarjetaClienteForm.formasPago.tarjetas.girador")));
		}
		if(errores.isEmpty())
		{
			tarjeta.setValido(true);
		}
		return errores;
	}

	/**
	 * Acción para validar los registros ingresados para las formas de pago con tipo detalle Bono.
	 * @return Errores
	 */
	public ActionErrors validarBonos() {
		ActionErrors errores=new ActionErrors();
		DtoInformacionFormaPago formaPago=dtoVentas.getListaInformacionFormasPago().get(dtoVentas.getIndiceFormaPago());
		DtoDetallePagosBonos bonos=formaPago.getDetalleBonos();
		MessageResources mensajes=MessageResources.getMessageResources("com.servinte.mensajes.odontologia.VentaTarjetaClienteForm");
		int i=0;
		double valor=0;
		
		ArrayList<String> serialesRepetidos=new ArrayList<String>();
		
		for(DtoBonoSerialValor bono:bonos.getSerialesBonos())
		{
			i++;
			
			if(!UtilidadTexto.isEmpty(bono.getSerial()))
			{
				if(serialesRepetidos.contains(bono.getSerial()))
				{
					errores.add("serial repetido", new ActionMessage("errors.notEspecific", mensajes.getMessage("VentaTarjetaClienteForm.formasPago.bonos.serialRepetido")));
					break;
				}
				else
				{
					serialesRepetidos.add(bono.getSerial());
				}
				
			}else{
				
				errores.add("serial", new ActionMessage("errors.required", mensajes.getMessage("VentaTarjetaClienteForm.formasPago.bonos.serial")+" "+i));
			}
			
			if(bono.getValor()==0)
			{
				errores.add("valor", new ActionMessage("errors.required", mensajes.getMessage("VentaTarjetaClienteForm.formasPago.bonos.valor")+" "+i));
			}
			else
			{
				valor+=bono.getValor();
			}
		}
		if(errores.isEmpty())
		{
			formaPago.setValido(true);
		}
		formaPago.setValor(valor);
		return errores;
	}

	/**
	 * Método que se encarga de validar que no se repitan seriales ingresados para las formas de pago
	 * con tipo detalle Bono.
	 * @return
	 */
	public ActionErrors validarTodasFormasPagoBono ()
	{
		ActionErrors errores=new ActionErrors();
	
		IFormasPagoServicio formasPagoServicio = TesoreriaFabricaServicio.crearFormasPagoServicio();
		
		DtoFormaPago formaPagoFiltro= new DtoFormaPago();
		
		DtoTipoDetalleFormaPago tipoDetalleFormaPago = new DtoTipoDetalleFormaPago();
		tipoDetalleFormaPago.setCodigo(ConstantesBD.codigoTipoDetalleFormasPagoBono);
		formaPagoFiltro.setTipoDetalle(tipoDetalleFormaPago);
		
		List<DtoFormaPago> listaFormasPagoBono = formasPagoServicio.obtenerFormasPagos(formaPagoFiltro);
		
		MessageResources mensajes=MessageResources.getMessageResources("com.servinte.mensajes.odontologia.VentaTarjetaClienteForm");
		
		for (DtoFormaPago dtoFormaPago : listaFormasPagoBono) {
			
			ArrayList<String> serialesRepetidos=new ArrayList<String>();
			
			int i=0;
			
			for(DtoInformacionFormaPago formaPago:dtoVentas.getListaInformacionFormasPago())
			{
				DtoDetallePagosBonos detallePagoBono= formaPago.getDetalleBonos();
				
				if(detallePagoBono!=null && formaPago.getFormaPago().getConsecutivo() == dtoFormaPago.getConsecutivo()){
					
					for(DtoBonoSerialValor bono:detallePagoBono.getSerialesBonos())
					{
						if(!UtilidadTexto.isEmpty(bono.getSerial()))
						{
							if(serialesRepetidos.contains(bono.getSerial()))
							{
								errores.add("serial repetido", new ActionMessage("errors.notEspecific", mensajes.getMessage("VentaTarjetaClienteForm.formasPago.bonos.serialRepetido")));
								break;
							}
							else
							{
								serialesRepetidos.add(bono.getSerial());
							}
						}
						else
						{
							errores.add("serial", new ActionMessage("errors.required", mensajes.getMessage("VentaTarjetaClienteForm.formasPago.bonos.serial")+" "+i));
						
						}if(bono.getValor()<=0){
							
							errores.add("valor", new ActionMessage("errors.required", mensajes.getMessage("VentaTarjetaClienteForm.formasPago.bonos.valor")+" "+i));
						}
						i++;
					}
					
					if(!errores.isEmpty()){
						
						break;
					}
				}
			}
		}
		
		return errores;
	}
	

	public boolean isEsResumen() {
		return esResumen;
	}


	public void setEsResumen(boolean esResumen) {
		this.esResumen = esResumen;
	}


	/**
	 * Verifica si existe error o no en la información ingresada
	 * Utilizado para bloquear el flujo de guardado de datos
	 * @return true en caso de existir un error, false de lo contrario
	 */
	public boolean existeError()
	{
		if(msgInformacionComprador!=null && msgInformacionComprador.getTipo()==Tipo.ERROR)
		{
			return true;
		}
		if(msgInformacionTarifa!=null && msgInformacionTarifa.getTipo()==Tipo.ERROR)
		{
			return true;
		}
		if(msgInformacionTipoTarjeta!=null && msgInformacionTipoTarjeta.getTipo()==Tipo.ERROR)
		{
			return true;
		}
		return false;
	}


	/**
	 * @param turnoAbierto the turnoAbierto to set
	 */
	public void setTurnoAbierto(boolean turnoAbierto) {
		this.turnoAbierto = turnoAbierto;
	}


	/**
	 * @return the turnoAbierto
	 */
	public boolean isTurnoAbierto() {
		return turnoAbierto;
	}
}
