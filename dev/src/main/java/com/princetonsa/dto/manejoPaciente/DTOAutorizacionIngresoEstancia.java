package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.TiposContrato;

/**
 * Esta clase se encarga de contener los datos de 
 * una autorización de ingreso estancia 
 * 
 * @author Angela Maria Aguirre
 * @since 9/12/2010
 */
public class DTOAutorizacionIngresoEstancia implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long codigoPk;
	private DtoCentroCosto dtoCentroCosto;		
	private Convenios convenioResponsable;
	private Date fechaAutorizacion;
	private Date fechaVencimiento;
	private Date nuevaFechaVencimiento;
	private long consecutivoAutorizacion;
	private DtoIngresoEstancia dtoIngresoEstancia;
	private int diasEstanciaAutorizados;
	private String tipoAutorizacion;
	private char indicativoTemporal;
	private DtoUsuarioPersona usuarioAutoriza;	
	private String observaciones;
	private String usuarioContacta;
	private String cargoUsuarioContacta;
	private String estado;	
	private DtoTrazabilidadAutorizacionIngEstancia trazabilidadAutorizacionIngEstancia;
	private String tipoAfiliado;
	private int clasificacionSocioeconomica;
	/**
	 * Este atributo indica el valor de Ingreso/Estancia, a través
	 * del cual se generó la autorización
	 */
	private String tipoGeneracionAutorizacion;

	/**
	 * Servicios solicitados, si hay mas de un servicio,
	 * se genera una autorización por cada uno
	 */
	private ArrayList<DtoServiciosAutorizaciones> listaServicios;
	
	
	/**
	 * lista de artículos a autorizar
	 */
	private ArrayList<DtoArticulosAutorizaciones> listaArticulos;
	
	/**
	 * Método Constructor de la clase
	 */
	public  DTOAutorizacionIngresoEstancia(){
		
	}
			
	
	/**
	 * Método constructor, usado para mapear los datos
	 * del select de la consulta del detalle de una autorización
	 * de ingreso estancia
	 * 
	 * @param consecutivo
	 * @param fechaAdmision
	 * @param diasEstancia
	 * @param indicativoTemporal
	 * @param estado
	 * @param usuarioAutoriza
	 * @param observaciones
	 * @param institucion
	 * @param fechaAdminsion
	 * @param horaAdmision
	 * @param descripcionDxPrincipal
	 * @param descripcionDxComplicacion
	 * @param medicoSolicitante
	 * @param observacionAdmision
	 * @param viaIngreso
	 * @param entidadSubcontratada
	 * @param direccion
	 * @param telefono
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
	 * @param entidadRecobrar
	 * @param usuarioContacta
	 */
	public  DTOAutorizacionIngresoEstancia(long consecutivo,Date fechaAutorizacion,
			int diasEstancia,char indicativoTemporal,String estado,String nombreUsuarioModifica,
			String apellidoUsuarioModifica,	
			String observaciones,String institucion,Date fechaAdmision,String horaAdmision,
			String acronimoDxPrincipal,String acronimoDxComplicacion,String medicoSolicitante,
			String observacionAdmision,String viaIngreso,String entidadSubcontratada,String direccion,
			String telefono,String primerNombrePersona,String segundoNombrePersona,String primerApellidoPersona,
			String segundoApellidoPersona,int codigoPersona,String tipoID,String numeroIdentificacion,
			String nombreConvenio,String tipoContrato,String tipoAfiliado,String estratoSocial,
			String entidadRecobrar,String nombreUsuarioContacta, String cargoUsuarioContacta,
			String nombreConvenioRecobro, Date fechaNacimiento,Integer tipoCIEComplicacion,
			Integer tipoCIEPrincipal, String nombreDxPrincipal, String nombreDxComplicacion, Integer codigoViaIngreso,long codigoIngresoEstancia){
		
		this.consecutivoAutorizacion = consecutivo;
		this.fechaAutorizacion = fechaAutorizacion;
		this.usuarioAutoriza = new DtoUsuarioPersona();
		this.usuarioAutoriza.setNombre(nombreUsuarioModifica);
		this.usuarioAutoriza.setApellido(apellidoUsuarioModifica);
		this.observaciones = observaciones;
		this.dtoIngresoEstancia = new DtoIngresoEstancia();		
		this.dtoIngresoEstancia.setFechaAdmision(fechaAdmision);
		this.diasEstanciaAutorizados = diasEstancia;
		this.dtoIngresoEstancia.setInsitucionAutorizada(institucion);
		this.dtoIngresoEstancia.setFechaAdmision(fechaAdmision);
		this.dtoIngresoEstancia.setHoraAdmision(horaAdmision);
		this.dtoIngresoEstancia.getDxPrincipal().setAcronimoDiagnostico(acronimoDxPrincipal);
		this.dtoIngresoEstancia.getDxPrincipal().setTipoCieDiagnostico(tipoCIEPrincipal.toString());
		this.dtoIngresoEstancia.getDxPrincipal().setNombreDiagnostico(nombreDxPrincipal);
		this.dtoIngresoEstancia.getDxPrincipal().setDescripcionDiagnostico(acronimoDxPrincipal+" - " + tipoCIEPrincipal);
		
		if(tipoCIEComplicacion!=null){
			this.dtoIngresoEstancia.getDxComplicacion().setAcronimoDiagnostico(acronimoDxComplicacion);
			this.dtoIngresoEstancia.getDxComplicacion().setTipoCieDiagnostico(tipoCIEComplicacion.toString());
			this.dtoIngresoEstancia.getDxComplicacion().setNombreDiagnostico(nombreDxComplicacion);
			this.dtoIngresoEstancia.getDxComplicacion().setDescripcionDiagnostico(acronimoDxComplicacion +" - " + tipoCIEComplicacion);
		}
		
		this.dtoIngresoEstancia.setObservaciones(observacionAdmision);
		this.dtoIngresoEstancia.setViaIngreso(viaIngreso);
		this.dtoIngresoEstancia.getEntidadSubcontratada().setRazonSocial(entidadSubcontratada);
		this.dtoIngresoEstancia.getEntidadSubcontratada().setDireccion(direccion);
		this.dtoIngresoEstancia.getEntidadSubcontratada().setTelefono(telefono);
		this.dtoIngresoEstancia.getDtoPaciente().setPrimerNombre(primerNombrePersona);
		this.dtoIngresoEstancia.getDtoPaciente().setSegundoNombre(segundoNombrePersona);
		this.dtoIngresoEstancia.getDtoPaciente().setPrimerApellido(primerApellidoPersona);
		this.dtoIngresoEstancia.getDtoPaciente().setSegundoApellido(segundoApellidoPersona);
		this.dtoIngresoEstancia.getDtoPaciente().setCodigo(codigoPersona);
		this.dtoIngresoEstancia.getDtoPaciente().setTipoIdentificacion(tipoID);
		this.dtoIngresoEstancia.getDtoPaciente().setNumeroIdentificacion(numeroIdentificacion);
		this.dtoIngresoEstancia.getDtoPaciente().setConvenio(new Convenios());
		this.dtoIngresoEstancia.getDtoPaciente().getConvenio().setTiposContrato(new TiposContrato());
		this.dtoIngresoEstancia.getDtoPaciente().getConvenio().getTiposContrato().setNombre(tipoContrato);
		this.dtoIngresoEstancia.getDtoPaciente().getConvenio().setNombre(nombreConvenio);
		this.dtoIngresoEstancia.getDtoPaciente().setTipoAfiliado(tipoAfiliado);
		this.dtoIngresoEstancia.getDtoPaciente().setClasificacionSocioEconomica(estratoSocial);
		this.dtoIngresoEstancia.setMedicoSolicitante(medicoSolicitante);
		
		if(fechaNacimiento!=null){
			this.dtoIngresoEstancia.getDtoPaciente().setFechaNacimiento(UtilidadFecha.conversionFormatoFechaAAp(fechaNacimiento));
		}
		
		if(!UtilidadTexto.isEmpty(entidadRecobrar)){
			this.dtoIngresoEstancia.setOtroConvenioRecobro(entidadRecobrar);
		}else if(!UtilidadTexto.isEmpty(nombreConvenioRecobro)){
			this.dtoIngresoEstancia.setOtroConvenioRecobro(nombreConvenioRecobro);
		}else{
			this.dtoIngresoEstancia.setOtroConvenioRecobro("");
		}
		if(codigoViaIngreso!=null){
			this.dtoIngresoEstancia.setCodigoViaIngreso(codigoViaIngreso);
		}
		
		this.dtoIngresoEstancia.setCodigoPk(codigoIngresoEstancia);
		
		this.usuarioContacta = nombreUsuarioContacta;
		this.cargoUsuarioContacta = cargoUsuarioContacta;		
		this.indicativoTemporal =indicativoTemporal;
		this.estado = estado; 
		this.observaciones = observaciones;		
		
	}	
	
	/**
	 * Método constructor, usado para mapear los datos
	 * del select de la consulta del detalle de una autorización
	 * de ingreso estancia
	 * 
	 * @param consecutivo
	 * @param fechaAdmision
	 * @param diasEstancia
	 * @param indicativoTemporal
	 * @param estado
	 * @param usuarioAutoriza
	 * @param observaciones
	 * @param institucion
	 * @param fechaAdminsion
	 * @param horaAdmision
	 * @param descripcionDxPrincipal
	 * @param descripcionDxComplicacion
	 * @param medicoSolicitante
	 * @param observacionAdmision
	 * @param viaIngreso
	 * @param entidadSubcontratada
	 * @param direccion
	 * @param telefono
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
	 * @param entidadRecobrar
	 * @param usuarioContacta
	 */
	public  DTOAutorizacionIngresoEstancia(long consecutivo,Date fechaAutorizacion,
			int diasEstancia,char indicativoTemporal,String estado,String nombreUsuarioModifica,
			String apellidoUsuarioModifica,	
			String observaciones,String institucion,Date fechaAdmision,String horaAdmision,
			String acronimoDxPrincipal,String acronimoDxComplicacion,String medicoSolicitante,
			String observacionAdmision,String viaIngreso,String entidadSubcontratada,String descripcionEntSub,String direccion,
			String telefono,String primerNombrePersona,String segundoNombrePersona,String primerApellidoPersona,
			String segundoApellidoPersona,int codigoPersona,String tipoID,String numeroIdentificacion,
			String nombreConvenio,String tipoContrato,String tipoAfiliado,String estratoSocial,
			String entidadRecobrar,String nombreUsuarioContacta, String cargoUsuarioContacta,
			String nombreConvenioRecobro, Date fechaNacimiento,Integer tipoCIEComplicacion,
			Integer tipoCIEPrincipal, String nombreDxPrincipal, String nombreDxComplicacion, Integer codigoViaIngreso,long codigoIngresoEstancia,
			String direccionEntidadOtra,String telefonoEntidadOtra){
		
		this.consecutivoAutorizacion = consecutivo;
		this.fechaAutorizacion = fechaAutorizacion;
		this.usuarioAutoriza = new DtoUsuarioPersona();
		this.usuarioAutoriza.setNombre(nombreUsuarioModifica);
		this.usuarioAutoriza.setApellido(apellidoUsuarioModifica);
		this.observaciones = observaciones;
		this.dtoIngresoEstancia = new DtoIngresoEstancia();		
		this.dtoIngresoEstancia.setFechaAdmision(fechaAdmision);
		this.diasEstanciaAutorizados = diasEstancia;
		this.dtoIngresoEstancia.setInsitucionAutorizada(institucion);
		this.dtoIngresoEstancia.setFechaAdmision(fechaAdmision);
		this.dtoIngresoEstancia.setHoraAdmision(horaAdmision);
		this.dtoIngresoEstancia.getDxPrincipal().setAcronimoDiagnostico(acronimoDxPrincipal);
		this.dtoIngresoEstancia.getDxPrincipal().setTipoCieDiagnostico(tipoCIEPrincipal.toString());
		this.dtoIngresoEstancia.getDxPrincipal().setNombreDiagnostico(nombreDxPrincipal);
		this.dtoIngresoEstancia.getDxPrincipal().setDescripcionDiagnostico(acronimoDxPrincipal+" - " + tipoCIEPrincipal);
		
		if(tipoCIEComplicacion!=null){
			this.dtoIngresoEstancia.getDxComplicacion().setAcronimoDiagnostico(acronimoDxComplicacion);
			this.dtoIngresoEstancia.getDxComplicacion().setTipoCieDiagnostico(tipoCIEComplicacion.toString());
			this.dtoIngresoEstancia.getDxComplicacion().setNombreDiagnostico(nombreDxComplicacion);
			this.dtoIngresoEstancia.getDxComplicacion().setDescripcionDiagnostico(acronimoDxComplicacion +" - " + tipoCIEComplicacion);
		}
		
		this.dtoIngresoEstancia.setObservaciones(observacionAdmision);
		this.dtoIngresoEstancia.setViaIngreso(viaIngreso);
		this.dtoIngresoEstancia.getEntidadSubcontratada().setRazonSocial(entidadSubcontratada);
		this.dtoIngresoEstancia.getEntidadSubcontratada().setDescripcionEntidad(descripcionEntSub);
		this.dtoIngresoEstancia.getEntidadSubcontratada().setDireccion(direccion);
		this.dtoIngresoEstancia.getEntidadSubcontratada().setTelefono(telefono);
		this.dtoIngresoEstancia.getEntidadSubcontratada().setDireccionotra(direccionEntidadOtra);
		this.dtoIngresoEstancia.getEntidadSubcontratada().setTelefonootra(telefonoEntidadOtra);		
		this.dtoIngresoEstancia.getDtoPaciente().setPrimerNombre(primerNombrePersona);
		this.dtoIngresoEstancia.getDtoPaciente().setSegundoNombre(segundoNombrePersona);
		this.dtoIngresoEstancia.getDtoPaciente().setPrimerApellido(primerApellidoPersona);
		this.dtoIngresoEstancia.getDtoPaciente().setSegundoApellido(segundoApellidoPersona);
		this.dtoIngresoEstancia.getDtoPaciente().setCodigo(codigoPersona);
		this.dtoIngresoEstancia.getDtoPaciente().setTipoIdentificacion(tipoID);
		this.dtoIngresoEstancia.getDtoPaciente().setNumeroIdentificacion(numeroIdentificacion);
		this.dtoIngresoEstancia.getDtoPaciente().setConvenio(new Convenios());
		this.dtoIngresoEstancia.getDtoPaciente().getConvenio().setTiposContrato(new TiposContrato());
		this.dtoIngresoEstancia.getDtoPaciente().getConvenio().getTiposContrato().setNombre(tipoContrato);
		this.dtoIngresoEstancia.getDtoPaciente().getConvenio().setNombre(nombreConvenio);
		this.dtoIngresoEstancia.getDtoPaciente().setTipoAfiliado(tipoAfiliado);
		this.dtoIngresoEstancia.getDtoPaciente().setClasificacionSocioEconomica(estratoSocial);
		this.dtoIngresoEstancia.setMedicoSolicitante(medicoSolicitante);
		
		if(fechaNacimiento!=null){
			this.dtoIngresoEstancia.getDtoPaciente().setFechaNacimiento(UtilidadFecha.conversionFormatoFechaAAp(fechaNacimiento));
		}
		
		if(!UtilidadTexto.isEmpty(entidadRecobrar)){
			this.dtoIngresoEstancia.setOtroConvenioRecobro(entidadRecobrar);
		}else if(!UtilidadTexto.isEmpty(nombreConvenioRecobro)){
			this.dtoIngresoEstancia.setOtroConvenioRecobro(nombreConvenioRecobro);
		}else{
			this.dtoIngresoEstancia.setOtroConvenioRecobro("");
		}
		if(codigoViaIngreso!=null){
			this.dtoIngresoEstancia.setCodigoViaIngreso(codigoViaIngreso);
		}
		
		this.dtoIngresoEstancia.setCodigoPk(codigoIngresoEstancia);
		
		this.usuarioContacta = nombreUsuarioContacta;
		this.cargoUsuarioContacta = cargoUsuarioContacta;		
		this.indicativoTemporal =indicativoTemporal;
		this.estado = estado; 
		this.observaciones = observaciones;		
		
	}	
	
	/**
	 * Método constructor, se usa para mapear los datos del select de la consulta
	 *  del historico de las autorizaciones de Ingreso Estancia 
	 * 
	 * @param codigoPk
	 * @param usuario
	 * @param fechaInicioAutoriz
	 * @param observaciones
	 * @param indicativoTemp
	 * @param estado
	 * @param fechaModifica
	 * @param horaModifica
	 * @param accion
	 */
	public  DTOAutorizacionIngresoEstancia(
			long codigoPk,String usuario,Date fechaInicioAutoriz,String observaciones,char indicativoTemp,
			String estado,Date fechaModifica,String horaModifica,String accion, String descripcionEntidad,
			String direccionEntidad, String telefonoEntidad, int numDiasAutorizados){
				
		trazabilidadAutorizacionIngEstancia =new DtoTrazabilidadAutorizacionIngEstancia();
		this.trazabilidadAutorizacionIngEstancia.setUsuarioModifica(usuario);
		this.trazabilidadAutorizacionIngEstancia.setIndicativoTemporal(indicativoTemp);
		this.trazabilidadAutorizacionIngEstancia.setFechaInicioAutorizacion(fechaInicioAutoriz);		
		this.trazabilidadAutorizacionIngEstancia.setFechaModifica(fechaModifica);
		this.trazabilidadAutorizacionIngEstancia.setHoraModifica(horaModifica);
		this.trazabilidadAutorizacionIngEstancia.setAccionRealizada(accion);
		this.trazabilidadAutorizacionIngEstancia.setObservaciones(observaciones);
		this.trazabilidadAutorizacionIngEstancia.setDescripcionEntidad(descripcionEntidad);
		this.trazabilidadAutorizacionIngEstancia.setDireccionEntidad(direccionEntidad);
		this.trazabilidadAutorizacionIngEstancia.setTelefonoEntidad(telefonoEntidad);
		this.trazabilidadAutorizacionIngEstancia.setFechaVencimiento(UtilidadFecha.conversionFormatoFechaStringDate(UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaAAp(fechaInicioAutoriz), numDiasAutorizados, false)));
			
	}
	
	
	
	
	public String getObservaciones() {
		return observaciones;
	}


	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}


		
	
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoPk
	
	 * @return retorna la variable codigoPk 
	 * @author Angela Maria Aguirre 
	 */
	public long getCodigoPk() {
		return codigoPk;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoPk
	
	 * @param valor para el atributo codigoPk 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dtoCentroCosto
	
	 * @return retorna la variable dtoCentroCosto 
	 * @author Angela Maria Aguirre 
	 */
	public DtoCentroCosto getDtoCentroCosto() {
		return dtoCentroCosto;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dtoCentroCosto
	
	 * @param valor para el atributo dtoCentroCosto 
	 * @author Angela Maria Aguirre 
	 */
	public void setDtoCentroCosto(DtoCentroCosto dtoCentroCosto) {
		this.dtoCentroCosto = dtoCentroCosto;
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
	 * del atributo convenioResponsable
	
	 * @return retorna la variable convenioResponsable 
	 * @author Angela Maria Aguirre 
	 */
	public Convenios getConvenioResponsable() {
		return convenioResponsable;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo convenioResponsable
	
	 * @param valor para el atributo convenioResponsable 
	 * @author Angela Maria Aguirre 
	 */
	public void setConvenioResponsable(Convenios convenioResponsable) {
		this.convenioResponsable = convenioResponsable;
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
	 * del atributo consecutivoAutorizacion
	
	 * @return retorna la variable consecutivoAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public long getConsecutivoAutorizacion() {
		return consecutivoAutorizacion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo consecutivoAutorizacion
	
	 * @param valor para el atributo consecutivoAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setConsecutivoAutorizacion(long consecutivoAutorizacion) {
		this.consecutivoAutorizacion = consecutivoAutorizacion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dtoIngresoEstancia
	
	 * @return retorna la variable dtoIngresoEstancia 
	 * @author Angela Maria Aguirre 
	 */
	public DtoIngresoEstancia getDtoIngresoEstancia() {
		return dtoIngresoEstancia;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dtoIngresoEstancia
	
	 * @param valor para el atributo dtoIngresoEstancia 
	 * @author Angela Maria Aguirre 
	 */
	public void setDtoIngresoEstancia(DtoIngresoEstancia dtoIngresoEstancia) {
		this.dtoIngresoEstancia = dtoIngresoEstancia;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo diasEstanciaAutorizados
	
	 * @return retorna la variable diasEstanciaAutorizados 
	 * @author Angela Maria Aguirre 
	 */
	public int getDiasEstanciaAutorizados() {
		return diasEstanciaAutorizados;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo diasEstanciaAutorizados
	
	 * @param valor para el atributo diasEstanciaAutorizados 
	 * @author Angela Maria Aguirre 
	 */
	public void setDiasEstanciaAutorizados(int diasEstanciaAutorizados) {
		this.diasEstanciaAutorizados = diasEstanciaAutorizados;
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
	 * del atributo indicativoTemporal
	
	 * @return retorna la variable indicativoTemporal 
	 * @author Angela Maria Aguirre 
	 */
	public char getIndicativoTemporal() {
		return indicativoTemporal;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo indicativoTemporal
	
	 * @param valor para el atributo indicativoTemporal 
	 * @author Angela Maria Aguirre 
	 */
	public void setIndicativoTemporal(char indicativoTemporal) {
		this.indicativoTemporal = indicativoTemporal;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo tipoGeneracionAutorizacion
	
	 * @return retorna la variable tipoGeneracionAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public String getTipoGeneracionAutorizacion() {
		return tipoGeneracionAutorizacion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoGeneracionAutorizacion
	
	 * @param valor para el atributo tipoGeneracionAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setTipoGeneracionAutorizacion(String tipoGeneracionAutorizacion) {
		this.tipoGeneracionAutorizacion = tipoGeneracionAutorizacion;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public DtoUsuarioPersona getUsuarioAutoriza() {
		return usuarioAutoriza;
	}


	public void setUsuarioAutoriza(DtoUsuarioPersona usuarioAutoriza) {
		this.usuarioAutoriza = usuarioAutoriza;
	}


	/**
	 * @return the usuarioContacta
	 */
	public String getUsuarioContacta() {
		return usuarioContacta;
	}


	/**
	 * @param usuarioContacta the usuarioContacta to set
	 */
	public void setUsuarioContacta(String usuarioContacta) {
		this.usuarioContacta = usuarioContacta;
	}


	/**
	 * @return the cargoUsuarioContacta
	 */
	public String getCargoUsuarioContacta() {
		return cargoUsuarioContacta;
	}


	/**
	 * @param cargoUsuarioContacta the cargoUsuarioContacta to set
	 */
	public void setCargoUsuarioContacta(String cargoUsuarioContacta) {
		this.cargoUsuarioContacta = cargoUsuarioContacta;
	}


	public void setTrazabilidadAutorizacionIngEstancia(
			DtoTrazabilidadAutorizacionIngEstancia trazabilidadAutorizacionIngEstancia) {
		this.trazabilidadAutorizacionIngEstancia = trazabilidadAutorizacionIngEstancia;
	}


	public DtoTrazabilidadAutorizacionIngEstancia getTrazabilidadAutorizacionIngEstancia() {
		return trazabilidadAutorizacionIngEstancia;
	}


	public void setTipoAfiliado(String tipoAfiliado) {
		this.tipoAfiliado = tipoAfiliado;
	}


	public String getTipoAfiliado() {
		return tipoAfiliado;
	}


	public void setClasificacionSocioeconomica(int clasificacionSocioeconomica) {
		this.clasificacionSocioeconomica = clasificacionSocioeconomica;
	}


	public int getClasificacionSocioeconomica() {
		return clasificacionSocioeconomica;
	}


	/**
	 * @return the nuevaFechaVencimiento
	 */
	public Date getNuevaFechaVencimiento() {
		return nuevaFechaVencimiento;
	}


	/**
	 * @param nuevaFechaVencimiento the nuevaFechaVencimiento to set
	 */
	public void setNuevaFechaVencimiento(Date nuevaFechaVencimiento) {
		this.nuevaFechaVencimiento = nuevaFechaVencimiento;
	}
}
