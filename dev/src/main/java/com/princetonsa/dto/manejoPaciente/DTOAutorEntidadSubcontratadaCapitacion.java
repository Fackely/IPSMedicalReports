package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.capitacion.DTOPacienteCapitado;
import com.princetonsa.dto.facturacion.DtoContrato;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionEntregaDto;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.TiposContrato;

/**
 * Esta clase se encarga de contener los datos de la autorización
 * de entidad subcontratada para población capitada
 * 
 * @author Angela Maria Aguirre
 * @since 17/12/2010
 */
public class DTOAutorEntidadSubcontratadaCapitacion implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//Atributos de la autorización de Entidad Subcontratada	
	private long autorizacion;
	private long numeroSolicitudEntSub;
	private String estado;
	private String tipoAutorizacion;
	private Date fechaAutorizacion;
	private String horaAutorizacion;
	private Date fechaVencimiento;
	private String observacionesGenerales;
	private String razonSocialEntidadAutoriza;
	private String consecutivoAutorizacion;
	private long codigoAutorizacionEntSub;
	private Convenios convenioResponsable;
	private String observaciones;
	private DTOPacienteCapitado dtoPaciente;
	private Integer codCentroCostoEjecutaSol;
	private Integer estadoSolicitud;
	private DtoContrato dtoContrato;
	private String estadoOrdenAmb;
	private Integer estadoPeticion;
	private boolean tieneSolicitud;
	private boolean tieneOrdenAmb;
	private boolean tienePeticion;
	private String nombreEstadoOrdenAmb;
	private String consecutivoAutorizEntidadSub;
	
	// Serivcios, Medicamentos e Insumos Autorizados
	private ArrayList<DtoArticulosAutorizaciones> listaArticulos;
	private ArrayList<DtoServiciosAutorizaciones> listaServicios;
		
	private DtoUsuarioPersona usuarioAutoriza;
	private DtoEntidadSubcontratada dtoEntidadSubcontratada;
	private DTOAutorizacionCapitacionSubcontratada autorCapitacion;	
	private DtoTrazabilidadAutorizacion trazabilidadAutorizacion;
	
	private String consecutivoOrdenAmb;
	private Date fechaOrdenAmb;
	private long consecutivoPeticion;
	private Date fechaPeticion;
	private boolean mostrarOrdenAmb;
	private boolean mostrarPeticion;
	 
	
	private boolean esOrden;
	
	private boolean esSolicitud;
	
	private Integer cantidadMonto;
	
	private Integer codigoDetalleMonto;
	
	/**
	 * Atributo que almacena los datos de la entrega de la autorizacion
	 */
	private AutorizacionEntregaDto autorizacionEntrega;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public DTOAutorEntidadSubcontratadaCapitacion(){		
		this.usuarioAutoriza                    = new DtoUsuarioPersona();
		this.dtoEntidadSubcontratada            = new DtoEntidadSubcontratada();
		this.autorCapitacion                    = new DTOAutorizacionCapitacionSubcontratada();   
		this.autorizacion						= ConstantesBD.codigoNuncaValidoLong;
		this.numeroSolicitudEntSub              = ConstantesBD.codigoNuncaValidoLong;
		this.estado								= "";
		this.tipoAutorizacion					= "";
		this.fechaAutorizacion					= null;
		this.horaAutorizacion					= "";
		this.fechaVencimiento					= null;
		this.observacionesGenerales				= "";
		this.listaArticulos						= new ArrayList<DtoArticulosAutorizaciones>();
		this.listaServicios						= new ArrayList<DtoServiciosAutorizaciones>();
		this.razonSocialEntidadAutoriza         = "";
		this.consecutivoAutorizacion            = "";
		this.convenioResponsable				= new Convenios();
		this.dtoPaciente                        = new DTOPacienteCapitado();
		this.codCentroCostoEjecutaSol           = ConstantesBD.codigoNuncaValido;
		this.estadoSolicitud                    = ConstantesBD.codigoNuncaValido;
		this.trazabilidadAutorizacion			= new DtoTrazabilidadAutorizacion();
		this.dtoContrato						= new DtoContrato();
		this.estadoOrdenAmb						= "";
		this.estadoPeticion						= ConstantesBD.codigoNuncaValido;
		this.tieneSolicitud						= false;
		this.tieneOrdenAmb						= false;
		this.tienePeticion						= false;
		this.nombreEstadoOrdenAmb				= "";
		this.consecutivoOrdenAmb				= null;
		this.fechaOrdenAmb						= null;
		this.consecutivoPeticion				= ConstantesBD.codigoNuncaValidoLong;
		this.fechaPeticion						= null;
		this.mostrarOrdenAmb					= false;
		this.mostrarPeticion					= false;
		this.consecutivoAutorizEntidadSub		= "";
		this.autorizacionEntrega = new AutorizacionEntregaDto();
	}
	
	/**
	 * Método constructor, se usa para mapear los datos del select de la consulta
	 * de autorizaciones de entidad subcontratada y su respectiva autorización de 
	 * capitación
	 * 
	 * @param primerNombrePersona
	 * @param segundoNombrePersona
	 * @param primerApellidoPersona
	 * @param segundoApellidoPersona
	 * @param codigoPersona
	 * @param tipoID
	 * @param numeroIdentificacion
	 * @param nombreConvenio
	 * @param tipoContrato
	 * @param estratoSocial
	 * @param tipoAfiliado
	 * @param valorMonto
	 * @param porcentajeMonto
	 * @param codigoViaIngreso
	 * @param nombreViaIngreso
	 * @param codigoCentroCosto
	 * @param nombreCentroCosto
	 * @param semanasCotizacion
	 * @param convenioRecobro
	 * @param descripcionentidadRecobro
	 * @param entidadSubcontratada
	 * @param direccion
	 * @param telefono
	 * @param consecutivo
	 * @param fechaAutorizacion
	 * @param fechaVencimiento
	 * @param indicadorPrioridad
	 * @param estado
	 * @param entidadAutoriza
	 * @param nombreUsuarioAutoriza
	 * @param apellidoUsuarioAutoriza
	 * @param observaciones
	 * @param fechaNacimiento
	 * @param acronimoTipoPaciente
	 * @param codigoNaturalezaPaciente
	 * @param codAutorEntSub
	 * @param temporal
	 * @param direccionotra
	 * @param telefonootra
	 * @param descripcionEntidad
	 * @param codigoContrato
	 * @param idIngresoPaciente
	 * @param cuentaPaciente
	 * @param estadoCuenta
	 * @param codigoSolicitud
	 * @param codigoOrdenAmb
	 * @param codigoPeticion
	 * @param estadoSolicitud
	 * @param estadoOrdenAmb
	 * @param estadoPeticion
	 */
	public  DTOAutorEntidadSubcontratadaCapitacion(String primerNombrePersona,String segundoNombrePersona,
			String primerApellidoPersona,String segundoApellidoPersona,int codigoPersona,
			String tipoID,String numeroIdentificacion,String nombreConvenio,int codigoConvenio,String tipoContrato,
			String estratoSocial,String tipoAfiliado,Double valorMonto, Double porcentajeMonto,
			Integer codigoViaIngreso,String nombreViaIngreso,
			String convenioRecobro, String descripcionentidadRecobro,
			String entidadSubcontratada, String direccion, String telefono,long consecutivo,			
			Date fechaAutorizacion,Date fechaVencimiento,Integer indicadorPrioridad,String estado,			
			String entidadAutoriza, String nombreUsuarioAutoriza,String apellidoUsuarioAutoriza,			
			String observaciones, Date fechaNacimiento,	
			long codAutorEntSub,char temporal, String direccionotra,String telefonootra,
			String descripcionEntidad, Integer codigoSolicitud, Long codigoOrdenAmb, String consecutivoOrdenAmb, Date fechaOrdenAmb,
			Integer codigoPeticion, Date fechaPeticion, Integer estadoSolicitud, 
			Byte estadoOrdenAmb, Integer estadoPeticion, String nombreEstadoOrdenAmb,
			long codigoAutorizacionEntSub,
			String consecutivoAutorEntSub,
			String tipoAutorizacionCapitacion)
	{
		
		this.dtoPaciente = new DTOPacienteCapitado();
		
		this.dtoPaciente.setPrimerNombre(primerNombrePersona);
		this.dtoPaciente.setPrimerApellido(primerApellidoPersona);
		this.dtoPaciente.setSegundoNombre(segundoNombrePersona);
		this.dtoPaciente.setSegundoApellido(segundoApellidoPersona);
		this.dtoPaciente.setCodigo(codigoPersona);
		this.dtoPaciente.setTipoIdentificacion(tipoID);
		this.dtoPaciente.setNumeroIdentificacion(numeroIdentificacion);
		
		if(fechaNacimiento!=null){
			this.dtoPaciente.setFechaNacimiento(UtilidadFecha.conversionFormatoFechaAAp(fechaNacimiento));
		}
		
		this.dtoPaciente.setConvenio(new Convenios());
		this.dtoPaciente.getConvenio().setNombre(nombreConvenio);
		this.dtoPaciente.getConvenio().setTiposContrato(new TiposContrato());
		this.dtoPaciente.getConvenio().getTiposContrato().setNombre(tipoContrato);
		this.dtoPaciente.getConvenio().setCodigo(codigoConvenio);
		this.dtoPaciente.setClasificacionSocioEconomica(estratoSocial);
		this.dtoPaciente.setTipoAfiliado(tipoAfiliado);
		this.dtoPaciente.setTipoMontoCobro(valorMonto==null?"%":"");
		this.autorCapitacion = new DTOAutorizacionCapitacionSubcontratada();
		
		if(!UtilidadTexto.isEmpty(descripcionentidadRecobro)){
			this.autorCapitacion.setOtroConvenioRecobro(descripcionentidadRecobro);
		}else if(!UtilidadTexto.isEmpty(convenioRecobro)){
			this.autorCapitacion.setOtroConvenioRecobro(convenioRecobro);
		}else{
			this.autorCapitacion.setOtroConvenioRecobro("");		
		}
		this.dtoEntidadSubcontratada = new DtoEntidadSubcontratada();
		this.dtoEntidadSubcontratada.setRazonSocial(entidadSubcontratada);
		this.dtoEntidadSubcontratada.setDireccion(direccion);
		this.dtoEntidadSubcontratada.setTelefono(telefono);
	    this.autorCapitacion.setConsecutivo(consecutivo);	    
		this.fechaAutorizacion = fechaAutorizacion;
		this.fechaVencimiento = fechaVencimiento;
		this.autorCapitacion.setIndicadorPrioridad(indicadorPrioridad);
		this.autorCapitacion.setIndicativoTemporal(temporal);
		this.autorCapitacion.setTipoAutorizacion(tipoAutorizacionCapitacion);
		this.estado = estado;
		this.razonSocialEntidadAutoriza=entidadAutoriza;		
		this.usuarioAutoriza = new DtoUsuarioPersona();		
		this.usuarioAutoriza.setNombre(nombreUsuarioAutoriza);
		this.usuarioAutoriza.setApellido(apellidoUsuarioAutoriza);
		this.observaciones = observaciones;
		this.autorizacion = codAutorEntSub;
		this.consecutivoAutorizacion = consecutivoAutorEntSub;
		this.codigoAutorizacionEntSub=codigoAutorizacionEntSub;
		this.dtoEntidadSubcontratada.setDireccionotra(direccionotra);
		this.dtoEntidadSubcontratada.setTelefonootra(telefonootra);
		this.dtoEntidadSubcontratada.setDescripcionEntidad(descripcionEntidad);
		this.dtoContrato = new DtoContrato();
		if(codigoViaIngreso!=null){
			this.dtoPaciente.setCodigoViaIngreso(codigoViaIngreso);
			this.dtoPaciente.setNombreViaIngreso(nombreViaIngreso);
		}
		else{
			this.dtoPaciente.setCodigoViaIngreso(ConstantesBD.codigoNuncaValido);
		}
		this.dtoPaciente.setCodigoCentroCosto(ConstantesBD.codigoNuncaValido);
		
		if(valorMonto!=null){
			this.dtoPaciente.setValorPorcentajeMontoCobro(new BigDecimal(valorMonto));
		}
		else if(porcentajeMonto!=null){
			this.dtoPaciente.setValorPorcentajeMontoCobro(new BigDecimal(porcentajeMonto));
		}
		
		if(codigoSolicitud != null){
			this.numeroSolicitudEntSub=codigoSolicitud.longValue();
			this.estadoSolicitud=estadoSolicitud;
			this.tieneSolicitud=true;
			this.tieneOrdenAmb=false;
			this.tienePeticion=false;
		}
		else if(codigoOrdenAmb != null){
			this.numeroSolicitudEntSub=codigoOrdenAmb.longValue();
			this.estadoOrdenAmb=estadoOrdenAmb.toString();
			this.nombreEstadoOrdenAmb=nombreEstadoOrdenAmb;
			this.tieneSolicitud=false;
			this.tieneOrdenAmb=true;
			this.tienePeticion=false;
		}
		else if(codigoPeticion != null){
			this.numeroSolicitudEntSub=codigoPeticion.longValue();
			this.estadoPeticion=estadoPeticion;
			this.tieneSolicitud=false;
			this.tieneOrdenAmb=false;
			this.tienePeticion=true;
		}
		//Se valida nuevamente porque puede tener solicitud y orden ambulatoria
		if(codigoOrdenAmb != null){
			this.consecutivoOrdenAmb=consecutivoOrdenAmb;
			this.fechaOrdenAmb=fechaOrdenAmb;
			this.mostrarOrdenAmb=true;
			this.mostrarPeticion=false;
		}
		else if(codigoPeticion != null){
			this.consecutivoPeticion=codigoPeticion.longValue();
			this.fechaPeticion=fechaPeticion;
			this.mostrarOrdenAmb=false;
			this.mostrarPeticion=true;
		}
		else{
			this.mostrarOrdenAmb=false;
			this.mostrarPeticion=false;
		}
	}
	
	/**
	 * Método constructor, se usa para mapear los datos del select de la consulta
	 * de autorizaciones de entidad subcontratada y su respectiva autorización de 
	 * capitación
	 * 
	 * @param primerNombrePersona
	 * @param segundoNombrePersona
	 * @param primerApellidoPersona
	 * @param segundoApellidoPersona
	 * @param codigoPersona
	 * @param tipoID
	 * @param numeroIdentificacion
	 * @param nombreConvenio
	 * @param tipoContrato
	 * @param estratoSocial
	 * @param tipoAfiliado
	 * @param valorMonto
	 * @param porcentajeMonto
	 * @param codigoViaIngreso
	 * @param nombreViaIngreso
	 * @param codigoCentroCosto
	 * @param nombreCentroCosto
	 * @param semanasCotizacion
	 * @param convenioRecobro
	 * @param descripcionentidadRecobro
	 * @param entidadSubcontratada
	 * @param direccion
	 * @param telefono
	 * @param consecutivo
	 * @param fechaAutorizacion
	 * @param fechaVencimiento
	 * @param indicadorPrioridad
	 * @param estado
	 * @param entidadAutoriza
	 * @param nombreUsuarioAutoriza
	 * @param apellidoUsuarioAutoriza
	 * @param observaciones
	 * @param fechaNacimiento
	 * @param acronimoTipoPaciente
	 * @param codigoNaturalezaPaciente
	 * @param codAutorEntSub
	 * @param temporal
	 * @param direccionotra
	 * @param telefonootra
	 * @param descripcionEntidad
	 * @param codigoContrato
	 * @param idIngresoPaciente
	 * @param cuentaPaciente
	 * @param estadoCuenta
	 * @param codigoSolicitud
	 * @param codigoOrdenAmb
	 * @param codigoPeticion
	 * @param estadoSolicitud
	 * @param estadoOrdenAmb
	 * @param estadoPeticion
	 * @param codigoClasificacionSE 
	 * @param tipoAfiliadoChar 
	 */
	public  DTOAutorEntidadSubcontratadaCapitacion(String primerNombrePersona,String segundoNombrePersona,
			String primerApellidoPersona,String segundoApellidoPersona,int codigoPersona,
			String tipoID,String numeroIdentificacion,String nombreConvenio,int codigoConvenio,String tipoContrato,
			String estratoSocial,String tipoAfiliado,Double valorMonto, Double porcentajeMonto,
			Integer codigoViaIngreso,String nombreViaIngreso,
			String convenioRecobro, String descripcionentidadRecobro,
			String entidadSubcontratada, String direccion, String telefono,long consecutivo,			
			Date fechaAutorizacion,Date fechaVencimiento,Integer indicadorPrioridad,String estado,			
			String entidadAutoriza, String nombreUsuarioAutoriza,String apellidoUsuarioAutoriza,			
			String observaciones, Date fechaNacimiento,	
			long codAutorEntSub,char temporal, String direccionotra,String telefonootra,
			String descripcionEntidad, Integer codigoSolicitud, Long codigoOrdenAmb, String consecutivoOrdenAmb, Date fechaOrdenAmb,
			Integer codigoPeticion, Date fechaPeticion, Integer estadoSolicitud, 
			Byte estadoOrdenAmb, Integer estadoPeticion, String nombreEstadoOrdenAmb,
			long codigoAutorizacionEntSub,
			String consecutivoAutorEntSub,
			String tipoAutorizacionCapitacion, Integer codigoClasificacionSE, Character tipoAfiliadoChar,Integer tipoMonto, Integer cantidadMonto, Integer codigoDetalleMonto)
	{
		
		this.dtoPaciente = new DTOPacienteCapitado();
		
		this.dtoPaciente.setPrimerNombre(primerNombrePersona);
		this.dtoPaciente.setPrimerApellido(primerApellidoPersona);
		this.dtoPaciente.setSegundoNombre(segundoNombrePersona);
		this.dtoPaciente.setSegundoApellido(segundoApellidoPersona);
		this.dtoPaciente.setCodigo(codigoPersona);
		this.dtoPaciente.setTipoIdentificacion(tipoID);
		this.dtoPaciente.setNumeroIdentificacion(numeroIdentificacion);
		
		if(fechaNacimiento!=null){
			this.dtoPaciente.setFechaNacimiento(UtilidadFecha.conversionFormatoFechaAAp(fechaNacimiento));
		}
		
		this.dtoPaciente.setConvenio(new Convenios());
		this.dtoPaciente.getConvenio().setNombre(nombreConvenio);
		this.dtoPaciente.getConvenio().setTiposContrato(new TiposContrato());
		this.dtoPaciente.getConvenio().getTiposContrato().setNombre(tipoContrato);
		this.dtoPaciente.getConvenio().setCodigo(codigoConvenio);
		this.dtoPaciente.setCodigoClasificacionSE(codigoClasificacionSE);
		this.dtoPaciente.setClasificacionSocioEconomica(estratoSocial);
		this.dtoPaciente.setTipoAfiliadoChar(tipoAfiliadoChar);
		this.dtoPaciente.setTipoAfiliado(tipoAfiliado);
		
		if(tipoMonto!=null){
			if(tipoMonto.intValue()==ConstantesBD.codigoTipoMontoCopago){
				this.dtoPaciente.setTipoMontoCobro("CO");
			}else{
				if(tipoMonto.intValue()==ConstantesBD.codigoTipoMontoCuotaModeradora){
					this.dtoPaciente.setTipoMontoCobro("CM");
				}
			}
		}
		
		this.cantidadMonto=cantidadMonto;
		this.codigoDetalleMonto=codigoDetalleMonto;
				
		this.autorCapitacion = new DTOAutorizacionCapitacionSubcontratada();
		
		if(!UtilidadTexto.isEmpty(descripcionentidadRecobro)){
			this.autorCapitacion.setOtroConvenioRecobro(descripcionentidadRecobro);
		}else if(!UtilidadTexto.isEmpty(convenioRecobro)){
			this.autorCapitacion.setOtroConvenioRecobro(convenioRecobro);
		}else{
			this.autorCapitacion.setOtroConvenioRecobro("");		
		}
		this.dtoEntidadSubcontratada = new DtoEntidadSubcontratada();
		this.dtoEntidadSubcontratada.setRazonSocial(entidadSubcontratada);
		this.dtoEntidadSubcontratada.setDireccion(direccion);
		this.dtoEntidadSubcontratada.setTelefono(telefono);
	    this.autorCapitacion.setConsecutivo(consecutivo);	    
		this.fechaAutorizacion = fechaAutorizacion;
		this.fechaVencimiento = fechaVencimiento;
		this.autorCapitacion.setIndicadorPrioridad(indicadorPrioridad);
		this.autorCapitacion.setIndicativoTemporal(temporal);
		this.autorCapitacion.setTipoAutorizacion(tipoAutorizacionCapitacion);
		this.estado = estado;
		this.razonSocialEntidadAutoriza=entidadAutoriza;		
		this.usuarioAutoriza = new DtoUsuarioPersona();		
		this.usuarioAutoriza.setNombre(nombreUsuarioAutoriza);
		this.usuarioAutoriza.setApellido(apellidoUsuarioAutoriza);
		this.observaciones = observaciones;
		this.autorizacion = codAutorEntSub;
		this.consecutivoAutorizacion = consecutivoAutorEntSub;
		this.codigoAutorizacionEntSub=codigoAutorizacionEntSub;
		this.dtoEntidadSubcontratada.setDireccionotra(direccionotra);
		this.dtoEntidadSubcontratada.setTelefonootra(telefonootra);
		this.dtoEntidadSubcontratada.setDescripcionEntidad(descripcionEntidad);
		this.dtoContrato = new DtoContrato();
		if(codigoViaIngreso!=null){
			this.dtoPaciente.setCodigoViaIngreso(codigoViaIngreso);
			this.dtoPaciente.setNombreViaIngreso(nombreViaIngreso);
		}
		else{
			this.dtoPaciente.setCodigoViaIngreso(ConstantesBD.codigoNuncaValido);
		}
		this.dtoPaciente.setCodigoCentroCosto(ConstantesBD.codigoNuncaValido);
		
		if(valorMonto!=null){
			this.dtoPaciente.setValorMontoCobro(new BigDecimal(valorMonto));
		}
		else if(porcentajeMonto!=null){
			this.dtoPaciente.setValorPorcentajeMontoCobro(new BigDecimal(porcentajeMonto));
		}
		
		if(codigoSolicitud != null){
			this.numeroSolicitudEntSub=codigoSolicitud.longValue();
			this.estadoSolicitud=estadoSolicitud;
			this.tieneSolicitud=true;
			this.tieneOrdenAmb=false;
			this.tienePeticion=false;
		}
		else if(codigoOrdenAmb != null){
			this.numeroSolicitudEntSub=codigoOrdenAmb.longValue();
			this.estadoOrdenAmb=estadoOrdenAmb.toString();
			this.nombreEstadoOrdenAmb=nombreEstadoOrdenAmb;
			this.tieneSolicitud=false;
			this.tieneOrdenAmb=true;
			this.tienePeticion=false;
		}
		else if(codigoPeticion != null){
			this.numeroSolicitudEntSub=codigoPeticion.longValue();
			this.estadoPeticion=estadoPeticion;
			this.tieneSolicitud=false;
			this.tieneOrdenAmb=false;
			this.tienePeticion=true;
		}
		//Se valida nuevamente porque puede tener solicitud y orden ambulatoria
		if(codigoOrdenAmb != null){
			this.consecutivoOrdenAmb=consecutivoOrdenAmb;
			this.fechaOrdenAmb=fechaOrdenAmb;
			this.mostrarOrdenAmb=true;
			this.mostrarPeticion=false;
		}
		else if(codigoPeticion != null){
			this.consecutivoPeticion=codigoPeticion.longValue();
			this.fechaPeticion=fechaPeticion;
			this.mostrarOrdenAmb=false;
			this.mostrarPeticion=true;
		}
		else{
			this.mostrarOrdenAmb=false;
			this.mostrarPeticion=false;
		}
	}
	
	/**
	 * Método constructor, se usa para mapear los datos del select de la consulta
	 * del historico de las autorizaciones de capitacion Subcontratada 
	 * 
	 * @param codigoPk
	 * @param usuario
	 * @param indTemporal
	 * @param descripcionEntidad
	 * @param dirEntidad
	 * @param teleEntidad
	 * @param fechaModifica
	 * @param horaModifica
	 * @param accion
	 * @param obser
	 * @param fechaVenci
	 */
	public  DTOAutorEntidadSubcontratadaCapitacion(
			long codigoPk,String usuario, char indTemporal, String descripcionEntidad,String dirEntidad,
			String teleEntidad,Date fechaModifica, String horaModifica, String accion, String obser, Date fechaVenci			
			){
				
		trazabilidadAutorizacion =new DtoTrazabilidadAutorizacion();
		this.trazabilidadAutorizacion.setCodigoPk(codigoPk);
		this.trazabilidadAutorizacion.setUsuarioModifica(usuario);
		this.trazabilidadAutorizacion.setIndicativoTemporal(indTemporal);
		this.trazabilidadAutorizacion.setDescripcionEntidad(descripcionEntidad);
		this.trazabilidadAutorizacion.setDireccionEntidad(dirEntidad);
		this.trazabilidadAutorizacion.setTelefonoEntidad(teleEntidad);
		this.trazabilidadAutorizacion.setFechaModifica(fechaModifica);
		this.trazabilidadAutorizacion.setFechaModifica(fechaModifica);
		this.trazabilidadAutorizacion.setHoraModifica(horaModifica);
		this.trazabilidadAutorizacion.setAccionRealizada(accion);
		this.trazabilidadAutorizacion.setObservaciones(obser);
		this.trazabilidadAutorizacion.setFechaVencimiento(fechaVenci);		
	}
	
	
	
	/**
	 * Método constructor, se usa para mapear los datos del select de la consulta
	 * de autorizaciones de entidad subcontratada y su respectiva autorización de 
	 * capitación, estas autorizaciones son generadas en un ingreso estancia
	 * 
	 * @param consecutivo
	 * @param fechaAutorizacion
	 * @param estado
	 * @param nombreUsuarioModifica
	 * @param apellidoUsuarioModifica
	 * @param observaciones
	 * @param institucion
	 * @param primerNombrePersona
	 * @param segundoNombrePersona
	 * @param primerApellidoPersona
	 * @param segundoApellidoPersona
	 * @param codigoPersona
	 * @param tipoID
	 * @param numeroIdentificacion
	 * @param nombreConvenio
	 * @param tipoContrato
	 * @param tipoAfiliado
	 * @param estratoSocial
	 * @param tipoMonto 
	 * @param cantidadMonto 
	 * @param codigoDetalleMonto 
	 * @param entidadRecobrar
	 * @param semanasCotizacion
	 */
	public  DTOAutorEntidadSubcontratadaCapitacion(String primerNombrePersona,String segundoNombrePersona,
			String primerApellidoPersona,String segundoApellidoPersona,int codigoPersona,
			String tipoID,String numeroIdentificacion,String nombreConvenio,String tipoContrato,
			String estratoSocial,String tipoAfiliado,
			String convenioRecobro, String descripcionentidadRecobro,
			String entidadSubcontratada, String direccion, String telefono,long consecutivo,			
			Date fechaAutorizacion,Date fechaVencimiento,Integer indicadorPrioridad,String estado,			
			String entidadAutoriza, String nombreUsuarioAutoriza,String apellidoUsuarioAutoriza,			
			String observaciones, Date fechaNacimiento, Integer codigoViaIngreso,
			String nombreViaIngreso,  
			Double valorMonto, Double porcentajeMonto, long codAutorEntSub, char temporal, Integer tipoMonto, Integer cantidadMonto, Integer codigoDetalleMonto){
		
		
		this.dtoPaciente = new DTOPacienteCapitado();
		
		this.dtoPaciente.setPrimerNombre(primerNombrePersona);
		this.dtoPaciente.setPrimerApellido(primerApellidoPersona);
		this.dtoPaciente.setSegundoNombre(segundoNombrePersona);
		this.dtoPaciente.setSegundoApellido(segundoApellidoPersona);
		this.dtoPaciente.setCodigo(codigoPersona);
		this.dtoPaciente.setTipoIdentificacion(tipoID);
		this.dtoPaciente.setNumeroIdentificacion(numeroIdentificacion);
		
		if(fechaNacimiento!=null){
			this.dtoPaciente.setFechaNacimiento(UtilidadFecha.conversionFormatoFechaAAp(fechaNacimiento));
		}
		
		this.dtoPaciente.setConvenio(new Convenios());
		this.dtoPaciente.getConvenio().setNombre(nombreConvenio);
		this.dtoPaciente.getConvenio().setTiposContrato(new TiposContrato());
		this.dtoPaciente.getConvenio().getTiposContrato().setNombre(tipoContrato);
		this.dtoPaciente.setClasificacionSocioEconomica(estratoSocial);
		this.dtoPaciente.setTipoAfiliado(tipoAfiliado);
		
		
		if(codigoViaIngreso!=null){
			this.dtoPaciente.setCodigoViaIngreso(codigoViaIngreso);
			this.dtoPaciente.setNombreViaIngreso(nombreViaIngreso);
		}
		
		if(tipoMonto!=null){
			if(tipoMonto.intValue()==ConstantesBD.codigoTipoMontoCopago){
				this.dtoPaciente.setTipoMontoCobro("CO");
			}else{
				if(tipoMonto.intValue()==ConstantesBD.codigoTipoMontoCuotaModeradora){
					this.dtoPaciente.setTipoMontoCobro("CM");
				}
			}
		}
		
		if(valorMonto!=null){
			this.dtoPaciente.setValorMontoCobro(new BigDecimal(valorMonto));
		}
		else if(porcentajeMonto!=null){
			this.dtoPaciente.setValorPorcentajeMontoCobro(new BigDecimal(porcentajeMonto));
		}
		
		this.cantidadMonto=cantidadMonto;
		this.codigoDetalleMonto=codigoDetalleMonto;
		
		this.autorCapitacion = new DTOAutorizacionCapitacionSubcontratada();
		
		if(!UtilidadTexto.isEmpty(descripcionentidadRecobro)){
			this.autorCapitacion.setOtroConvenioRecobro(descripcionentidadRecobro);
		}else if(!UtilidadTexto.isEmpty(convenioRecobro)){
			this.autorCapitacion.setOtroConvenioRecobro(convenioRecobro);
		}else{
			this.autorCapitacion.setOtroConvenioRecobro("");		
		}
		this.dtoEntidadSubcontratada = new DtoEntidadSubcontratada();
		this.dtoEntidadSubcontratada.setRazonSocial(entidadSubcontratada);
		this.dtoEntidadSubcontratada.setDireccion(direccion);
		this.dtoEntidadSubcontratada.setTelefono(telefono);
	    this.autorCapitacion.setConsecutivo(consecutivo);	    
		this.fechaAutorizacion = fechaAutorizacion;
		this.fechaVencimiento = fechaVencimiento;
		this.autorCapitacion.setIndicadorPrioridad(indicadorPrioridad);
		this.autorCapitacion.setIndicativoTemporal(temporal);
		this.estado = estado;
		this.razonSocialEntidadAutoriza=entidadAutoriza;		
		this.usuarioAutoriza = new DtoUsuarioPersona();		
		this.usuarioAutoriza.setNombre(nombreUsuarioAutoriza);
		this.usuarioAutoriza.setApellido(apellidoUsuarioAutoriza);
		this.observaciones = observaciones;
		this.autorizacion = codAutorEntSub;
		this.numeroSolicitudEntSub = ConstantesBD.codigoNuncaValidoLong;
		
	}
	
	/**
	 * Método constructor, se usa para mapear los datos del select de la consulta
	 * de autorizaciones de entidad subcontratada y su respectiva autorización de 
	 * capitación, estas autorizaciones son generadas en un ingreso estancia (nuevo parametro
	 * consecutivoAutorEntSub)
	 * 
	 * @param primerNombrePersona
	 * @param segundoNombrePersona
	 * @param primerApellidoPersona
	 * @param segundoApellidoPersona
	 * @param codigoPersona
	 * @param tipoID
	 * @param numeroIdentificacion
	 * @param nombreConvenio
	 * @param tipoContrato
	 * @param estratoSocial
	 * @param tipoAfiliado
	 * @param convenioRecobro
	 * @param descripcionentidadRecobro
	 * @param entidadSubcontratada
	 * @param direccion
	 * @param telefono
	 * @param consecutivo
	 * @param fechaAutorizacion
	 * @param fechaVencimiento
	 * @param indicadorPrioridad
	 * @param estado
	 * @param entidadAutoriza
	 * @param nombreUsuarioAutoriza
	 * @param apellidoUsuarioAutoriza
	 * @param observaciones
	 * @param fechaNacimiento
	 * @param codigoViaIngreso
	 * @param nombreViaIngreso
	 * @param valorMonto
	 * @param porcentajeMonto
	 * @param codAutorEntSub
	 * @param temporal
	 * @param tipoMonto
	 * @param cantidadMonto
	 * @param codigoDetalleMonto
	 * @param consecutivoAutorEntSub
	 * @author hermorhu
	 * @created 21-feb-2013
	 */
	public  DTOAutorEntidadSubcontratadaCapitacion(String primerNombrePersona,String segundoNombrePersona,
			String primerApellidoPersona,String segundoApellidoPersona,int codigoPersona,
			String tipoID,String numeroIdentificacion,String nombreConvenio,String tipoContrato,
			String estratoSocial,String tipoAfiliado,
			String convenioRecobro, String descripcionentidadRecobro,
			String entidadSubcontratada, String direccion, String telefono,long consecutivo,			
			Date fechaAutorizacion,Date fechaVencimiento,Integer indicadorPrioridad,String estado,			
			String entidadAutoriza, String nombreUsuarioAutoriza,String apellidoUsuarioAutoriza,			
			String observaciones, Date fechaNacimiento, Integer codigoViaIngreso,
			String nombreViaIngreso,  
			Double valorMonto, Double porcentajeMonto, long codAutorEntSub, char temporal, Integer tipoMonto, 
			Integer cantidadMonto, Integer codigoDetalleMonto, String consecutivoAutorEntSub){
		
		
		this.dtoPaciente = new DTOPacienteCapitado();
		
		this.dtoPaciente.setPrimerNombre(primerNombrePersona);
		this.dtoPaciente.setPrimerApellido(primerApellidoPersona);
		this.dtoPaciente.setSegundoNombre(segundoNombrePersona);
		this.dtoPaciente.setSegundoApellido(segundoApellidoPersona);
		this.dtoPaciente.setCodigo(codigoPersona);
		this.dtoPaciente.setTipoIdentificacion(tipoID);
		this.dtoPaciente.setNumeroIdentificacion(numeroIdentificacion);
		
		if(fechaNacimiento!=null){
			this.dtoPaciente.setFechaNacimiento(UtilidadFecha.conversionFormatoFechaAAp(fechaNacimiento));
		}
		
		this.dtoPaciente.setConvenio(new Convenios());
		this.dtoPaciente.getConvenio().setNombre(nombreConvenio);
		this.dtoPaciente.getConvenio().setTiposContrato(new TiposContrato());
		this.dtoPaciente.getConvenio().getTiposContrato().setNombre(tipoContrato);
		this.dtoPaciente.setClasificacionSocioEconomica(estratoSocial);
		this.dtoPaciente.setTipoAfiliado(tipoAfiliado);
		
		
		if(codigoViaIngreso!=null){
			this.dtoPaciente.setCodigoViaIngreso(codigoViaIngreso);
			this.dtoPaciente.setNombreViaIngreso(nombreViaIngreso);
		}
		
		if(tipoMonto!=null){
			if(tipoMonto.intValue()==ConstantesBD.codigoTipoMontoCopago){
				this.dtoPaciente.setTipoMontoCobro("CO");
			}else{
				if(tipoMonto.intValue()==ConstantesBD.codigoTipoMontoCuotaModeradora){
					this.dtoPaciente.setTipoMontoCobro("CM");
				}
			}
		}
		
		if(valorMonto!=null){
			this.dtoPaciente.setValorMontoCobro(new BigDecimal(valorMonto));
		}
		else if(porcentajeMonto!=null){
			this.dtoPaciente.setValorPorcentajeMontoCobro(new BigDecimal(porcentajeMonto));
		}
		
		this.cantidadMonto=cantidadMonto;
		this.codigoDetalleMonto=codigoDetalleMonto;
		
		this.autorCapitacion = new DTOAutorizacionCapitacionSubcontratada();
		
		if(!UtilidadTexto.isEmpty(descripcionentidadRecobro)){
			this.autorCapitacion.setOtroConvenioRecobro(descripcionentidadRecobro);
		}else if(!UtilidadTexto.isEmpty(convenioRecobro)){
			this.autorCapitacion.setOtroConvenioRecobro(convenioRecobro);
		}else{
			this.autorCapitacion.setOtroConvenioRecobro("");		
		}
		this.dtoEntidadSubcontratada = new DtoEntidadSubcontratada();
		this.dtoEntidadSubcontratada.setRazonSocial(entidadSubcontratada);
		this.dtoEntidadSubcontratada.setDireccion(direccion);
		this.dtoEntidadSubcontratada.setTelefono(telefono);
	    this.autorCapitacion.setConsecutivo(consecutivo);	    
		this.fechaAutorizacion = fechaAutorizacion;
		this.fechaVencimiento = fechaVencimiento;
		this.autorCapitacion.setIndicadorPrioridad(indicadorPrioridad);
		this.autorCapitacion.setIndicativoTemporal(temporal);
		this.estado = estado;
		this.razonSocialEntidadAutoriza=entidadAutoriza;		
		this.usuarioAutoriza = new DtoUsuarioPersona();		
		this.usuarioAutoriza.setNombre(nombreUsuarioAutoriza);
		this.usuarioAutoriza.setApellido(apellidoUsuarioAutoriza);
		this.observaciones = observaciones;
		this.autorizacion = codAutorEntSub;
		this.numeroSolicitudEntSub = ConstantesBD.codigoNuncaValidoLong;
	
		this.consecutivoAutorizacion = consecutivoAutorEntSub;
	}
	
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo autorizacion
	
	 * @return retorna la variable autorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public long getAutorizacion() {
		return autorizacion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo autorizacion
	
	 * @param valor para el atributo autorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setAutorizacion(long autorizacion) {
		this.autorizacion = autorizacion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo numeroSolicitudEntSub
	
	 * @return retorna la variable numeroSolicitudEntSub 
	 * @author Angela Maria Aguirre 
	 */
	public long getNumeroSolicitudEntSub() {
		return numeroSolicitudEntSub;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo numeroSolicitudEntSub
	
	 * @param valor para el atributo numeroSolicitudEntSub 
	 * @author Angela Maria Aguirre 
	 */
	public void setNumeroSolicitudEntSub(long numeroSolicitudEntSub) {
		this.numeroSolicitudEntSub = numeroSolicitudEntSub;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo estado
	
	 * @return retorna la variable estado 
	 * @author Angela Maria Aguirre 
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo estado
	
	 * @param valor para el atributo estado 
	 * @author Angela Maria Aguirre 
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo tipoAutorizacion
	
	 * @return retorna la variable tipoAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public String getTipoAutorizacion() {
		return tipoAutorizacion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoAutorizacion
	
	 * @param valor para el atributo tipoAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setTipoAutorizacion(String tipoAutorizacion) {
		this.tipoAutorizacion = tipoAutorizacion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo fechaAutorizacion
	
	 * @return retorna la variable fechaAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public Date getFechaAutorizacion() {
		return fechaAutorizacion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo fechaAutorizacion
	
	 * @param valor para el atributo fechaAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaAutorizacion(Date fechaAutorizacion) {
		this.fechaAutorizacion = fechaAutorizacion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo horaAutorizacion
	
	 * @return retorna la variable horaAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public String getHoraAutorizacion() {
		return horaAutorizacion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo horaAutorizacion
	
	 * @param valor para el atributo horaAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setHoraAutorizacion(String horaAutorizacion) {
		this.horaAutorizacion = horaAutorizacion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo fechaVencimiento
	
	 * @return retorna la variable fechaVencimiento 
	 * @author Angela Maria Aguirre 
	 */
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo fechaVencimiento
	
	 * @param valor para el atributo fechaVencimiento 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo observacionesGenerales
	
	 * @return retorna la variable observacionesGenerales 
	 * @author Angela Maria Aguirre 
	 */
	public String getObservacionesGenerales() {
		return observacionesGenerales;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo observacionesGenerales
	
	 * @param valor para el atributo observacionesGenerales 
	 * @author Angela Maria Aguirre 
	 */
	public void setObservacionesGenerales(String observacionesGenerales) {
		this.observacionesGenerales = observacionesGenerales;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaArticulos
	
	 * @return retorna la variable listaArticulos 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DtoArticulosAutorizaciones> getListaArticulos() {
		return listaArticulos;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaArticulos
	
	 * @param valor para el atributo listaArticulos 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaArticulos(
			ArrayList<DtoArticulosAutorizaciones> listaArticulos) {
		this.listaArticulos = listaArticulos;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaServicios
	
	 * @return retorna la variable listaServicios 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DtoServiciosAutorizaciones> getListaServicios() {
		return listaServicios;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaServicios
	
	 * @param valor para el atributo listaServicios 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaServicios(
			ArrayList<DtoServiciosAutorizaciones> listaServicios) {
		this.listaServicios = listaServicios;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo usuarioAutoriza
	
	 * @return retorna la variable usuarioAutoriza 
	 * @author Angela Maria Aguirre 
	 */
	public DtoUsuarioPersona getUsuarioAutoriza() {
		return usuarioAutoriza;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo usuarioAutoriza
	
	 * @param valor para el atributo usuarioAutoriza 
	 * @author Angela Maria Aguirre 
	 */
	public void setUsuarioAutoriza(DtoUsuarioPersona usuarioAutoriza) {
		this.usuarioAutoriza = usuarioAutoriza;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dtoEntidadSubcontratada
	
	 * @return retorna la variable dtoEntidadSubcontratada 
	 * @author Angela Maria Aguirre 
	 */
	public DtoEntidadSubcontratada getDtoEntidadSubcontratada() {
		return dtoEntidadSubcontratada;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dtoEntidadSubcontratada
	
	 * @param valor para el atributo dtoEntidadSubcontratada 
	 * @author Angela Maria Aguirre 
	 */
	public void setDtoEntidadSubcontratada(
			DtoEntidadSubcontratada dtoEntidadSubcontratada) {
		this.dtoEntidadSubcontratada = dtoEntidadSubcontratada;
	}



	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo autorCapitacion
	
	 * @return retorna la variable autorCapitacion 
	 * @author Angela Maria Aguirre 
	 */
	public DTOAutorizacionCapitacionSubcontratada getAutorCapitacion() {
		return autorCapitacion;
	}



	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo autorCapitacion
	
	 * @param valor para el atributo autorCapitacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setAutorCapitacion(
			DTOAutorizacionCapitacionSubcontratada autorCapitacion) {
		this.autorCapitacion = autorCapitacion;
	}



	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo razonSocialEntidadAutoriza
	
	 * @return retorna la variable razonSocialEntidadAutoriza 
	 * @author Angela Maria Aguirre 
	 */
	public String getRazonSocialEntidadAutoriza() {
		return razonSocialEntidadAutoriza;
	}



	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo razonSocialEntidadAutoriza
	
	 * @param valor para el atributo razonSocialEntidadAutoriza 
	 * @author Angela Maria Aguirre 
	 */
	public void setRazonSocialEntidadAutoriza(String razonSocialEntidadAutoriza) {
		this.razonSocialEntidadAutoriza = razonSocialEntidadAutoriza;
	}



	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo consecutivoAutorizacion
	
	 * @return retorna la variable consecutivoAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public String getConsecutivoAutorizacion() {
		return consecutivoAutorizacion;
	}



	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo consecutivoAutorizacion
	
	 * @param valor para el atributo consecutivoAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setConsecutivoAutorizacion(String consecutivoAutorizacion) {
		this.consecutivoAutorizacion = consecutivoAutorizacion;
	}



	public int getCodigoPaciente() {
		
		return this.dtoPaciente.getCodigo();
	}



	public void setCodigoPaciente(int codigoPaciente) {
		
		if(this.dtoPaciente==null){
			this.dtoPaciente = new DTOPacienteCapitado();
		}
		this.dtoPaciente.setCodigo(codigoPaciente);
	}



	public Convenios getConvenioResponsable() {
		return convenioResponsable;
	}



	public void setConvenioResponsable(Convenios convenioResponsable) {
		this.convenioResponsable = convenioResponsable;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public DTOPacienteCapitado getDtoPaciente() {
		return dtoPaciente;
	}

	public void setDtoPaciente(DTOPacienteCapitado dtoPaciente) {
		this.dtoPaciente = dtoPaciente;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codCentroCostoEjecutaSol
	
	 * @return retorna la variable codCentroCostoEjecutaSol 
	 * @author Angela Maria Aguirre 
	 */
	public Integer getCodCentroCostoEjecutaSol() {
		return codCentroCostoEjecutaSol;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codCentroCostoEjecutaSol
	
	 * @param valor para el atributo codCentroCostoEjecutaSol 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodCentroCostoEjecutaSol(Integer codCentroCostoEjecutaSol) {
		this.codCentroCostoEjecutaSol = codCentroCostoEjecutaSol;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo estadoSolicitud
	
	 * @return retorna la variable estadoSolicitud 
	 * @author Angela Maria Aguirre 
	 */
	public Integer getEstadoSolicitud() {
		return estadoSolicitud;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo estadoSolicitud
	
	 * @param valor para el atributo estadoSolicitud 
	 * @author Angela Maria Aguirre 
	 */
	public void setEstadoSolicitud(Integer estadoSolicitud) {
		this.estadoSolicitud = estadoSolicitud;
	}

	/**
	 * 
	 * @return 
	 * @author Camilo Gomez
	 */
	public DtoTrazabilidadAutorizacion getTrazabilidadAutorizacion() {
		return trazabilidadAutorizacion;
	}

	public void setTrazabilidadAutorizacion(
			DtoTrazabilidadAutorizacion trazabilidadAutorizacion) {
		this.trazabilidadAutorizacion = trazabilidadAutorizacion;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dtoContrato
	
	 * @return retorna la variable dtoContrato 
	 * @author Angela Maria Aguirre 
	 */
	public DtoContrato getDtoContrato() {
		return dtoContrato;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dtoContrato
	
	 * @param valor para el atributo dtoContrato 
	 * @author Angela Maria Aguirre 
	 */
	public void setDtoContrato(DtoContrato dtoContrato) {
		this.dtoContrato = dtoContrato;
	}

	/**
	 * @return the estadoOrdenAmb
	 */
	public String getEstadoOrdenAmb() {
		return estadoOrdenAmb;
	}

	/**
	 * @param estadoOrdenAmb the estadoOrdenAmb to set
	 */
	public void setEstadoOrdenAmb(String estadoOrdenAmb) {
		this.estadoOrdenAmb = estadoOrdenAmb;
	}

	/**
	 * @return the estadoPeticion
	 */
	public Integer getEstadoPeticion() {
		return estadoPeticion;
	}

	/**
	 * @param estadoPeticion the estadoPeticion to set
	 */
	public void setEstadoPeticion(Integer estadoPeticion) {
		this.estadoPeticion = estadoPeticion;
	}

	/**
	 * @return the tieneSolicitud
	 */
	public boolean isTieneSolicitud() {
		return tieneSolicitud;
	}

	/**
	 * @param tieneSolicitud the tieneSolicitud to set
	 */
	public void setTieneSolicitud(boolean tieneSolicitud) {
		this.tieneSolicitud = tieneSolicitud;
	}

	/**
	 * @return the tieneOrdenAmb
	 */
	public boolean isTieneOrdenAmb() {
		return tieneOrdenAmb;
	}

	/**
	 * @param tieneOrdenAmb the tieneOrdenAmb to set
	 */
	public void setTieneOrdenAmb(boolean tieneOrdenAmb) {
		this.tieneOrdenAmb = tieneOrdenAmb;
	}

	/**
	 * @return the tienePeticion
	 */
	public boolean isTienePeticion() {
		return tienePeticion;
	}

	/**
	 * @param tienePeticion the tienePeticion to set
	 */
	public void setTienePeticion(boolean tienePeticion) {
		this.tienePeticion = tienePeticion;
	}

	/**
	 * @return the nombreEstadoOrdenAmb
	 */
	public String getNombreEstadoOrdenAmb() {
		return nombreEstadoOrdenAmb;
	}

	/**
	 * @param nombreEstadoOrdenAmb the nombreEstadoOrdenAmb to set
	 */
	public void setNombreEstadoOrdenAmb(String nombreEstadoOrdenAmb) {
		this.nombreEstadoOrdenAmb = nombreEstadoOrdenAmb;
	}

	
	

	/**
	 * Obtiene el estado de la orden, sin importar sie s una solicitud, peticion o ambulatoria
	 * @param dtoAutorEntSub
	 * @return estado de la orden
	 *
	 * @autor Cristhian Murillo
	*/
	public int obtenerEstadoOrden()
	{
		int estaroOrden = ConstantesBD.codigoNuncaValido;
		
		if(this.estadoSolicitud != null){
			estaroOrden = this.estadoSolicitud;
		}
		else if(!UtilidadTexto.isEmpty(this.estadoOrdenAmb)){
			estaroOrden = Integer.parseInt(this.estadoOrdenAmb);
		}
		else if(this.estadoPeticion!=null){
			estaroOrden = this.estadoPeticion;
		}
		
		return estaroOrden;
	}

	/**
	 * @return the consecutivoOrdenAmb
	 */
	public String getConsecutivoOrdenAmb() {
		return consecutivoOrdenAmb;
	}

	/**
	 * @param consecutivoOrdenAmb the consecutivoOrdenAmb to set
	 */
	public void setConsecutivoOrdenAmb(String consecutivoOrdenAmb) {
		this.consecutivoOrdenAmb = consecutivoOrdenAmb;
	}

	/**
	 * @return the fechaOrdenAmb
	 */
	public Date getFechaOrdenAmb() {
		return fechaOrdenAmb;
	}

	/**
	 * @param fechaOrdenAmb the fechaOrdenAmb to set
	 */
	public void setFechaOrdenAmb(Date fechaOrdenAmb) {
		this.fechaOrdenAmb = fechaOrdenAmb;
	}

	/**
	 * @return the consecutivoPeticion
	 */
	public long getConsecutivoPeticion() {
		return consecutivoPeticion;
	}

	/**
	 * @param consecutivoPeticion the consecutivoPeticion to set
	 */
	public void setConsecutivoPeticion(long consecutivoPeticion) {
		this.consecutivoPeticion = consecutivoPeticion;
	}

	/**
	 * @return the fechaPeticion
	 */
	public Date getFechaPeticion() {
		return fechaPeticion;
	}

	/**
	 * @param fechaPeticion the fechaPeticion to set
	 */
	public void setFechaPeticion(Date fechaPeticion) {
		this.fechaPeticion = fechaPeticion;
	}

	/**
	 * @return the mostrarOrdenAmb
	 */
	public boolean isMostrarOrdenAmb() {
		return mostrarOrdenAmb;
	}

	/**
	 * @param mostrarOrdenAmb the mostrarOrdenAmb to set
	 */
	public void setMostrarOrdenAmb(boolean mostrarOrdenAmb) {
		this.mostrarOrdenAmb = mostrarOrdenAmb;
	}

	/**
	 * @return the mostrarPeticion
	 */
	public boolean isMostrarPeticion() {
		return mostrarPeticion;
	}

	/**
	 * @param mostrarPeticion the mostrarPeticion to set
	 */
	public void setMostrarPeticion(boolean mostrarPeticion) {
		this.mostrarPeticion = mostrarPeticion;
	}

	public void setConsecutivoAutorizEntidadSub(
			String consecutivoAutorizEntidadSub) {
		this.consecutivoAutorizEntidadSub = consecutivoAutorizEntidadSub;
	}

	public String getConsecutivoAutorizEntidadSub() {
		return consecutivoAutorizEntidadSub;
	}

	/**
	 * @return the esOrden
	 */
	public boolean isEsOrden() {
		return esOrden;
	}

	/**
	 * @param esOrden the esOrden to set
	 */
	public void setEsOrden(boolean esOrden) {
		this.esOrden = esOrden;
	}

	/**
	 * @return the esSolicitud
	 */
	public boolean isEsSolicitud() {
		return esSolicitud;
	}

	/**
	 * @param esSolicitud the esSolicitud to set
	 */
	public void setEsSolicitud(boolean esSolicitud) {
		this.esSolicitud = esSolicitud;
	}

	/**
	 * @return the codigoAutorizacionEntSub
	 */
	public long getCodigoAutorizacionEntSub() {
		return codigoAutorizacionEntSub;
	}

	/**
	 * @param codigoAutorizacionEntSub the codigoAutorizacionEntSub to set
	 */
	public void setCodigoAutorizacionEntSub(long codigoAutorizacionEntSub) {
		this.codigoAutorizacionEntSub = codigoAutorizacionEntSub;
	}

	/**
	 * @return the cantidadMonto
	 */
	public Integer getCantidadMonto() {
		return cantidadMonto;
	}

	/**
	 * @param cantidadMonto the cantidadMonto to set
	 */
	public void setCantidadMonto(Integer cantidadMonto) {
		this.cantidadMonto = cantidadMonto;
	}

	/**
	 * @return the codigoDetalleMonto
	 */
	public Integer getCodigoDetalleMonto() {
		return codigoDetalleMonto;
	}

	/**
	 * @param codigoDetalleMonto the codigoDetalleMonto to set
	 */
	public void setCodigoDetalleMonto(Integer codigoDetalleMonto) {
		this.codigoDetalleMonto = codigoDetalleMonto;
	}

	/**
	 * @return the autorizacionEntrega
	 */
	public AutorizacionEntregaDto getAutorizacionEntrega() {
		return autorizacionEntrega;
	}

	/**
	 * @param autorizacionEntrega the autorizacionEntrega to set
	 */
	public void setAutorizacionEntrega(AutorizacionEntregaDto autorizacionEntrega) {
		this.autorizacionEntrega = autorizacionEntrega;
	}

}
