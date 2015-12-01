package com.princetonsa.dto.facturacion;

import java.util.ArrayList;

import util.UtilidadTexto;
import util.Utilidades;


/**
 * Esta clase se encarga de almacenar los datos del detalle
 * de un monto de cobro
 * 
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public class DTOResultadoBusquedaDetalleMontos {
	
	private static final long serialVersionUID = 1L;
	
	private int viaIngresoID;
	private String viaIngresoNombre;
	private String tipoPacienteAcronimo;
	private String tipoPacienteNombre;
	private int estratoID;
	private String estratoDescripcion;	
	private Integer naturalezaID;
	private String naturalezaNombre;
	private int tipoMontoID;
	private String tipoMontoNombre;
	private String tipoDetalleAcronimo;
	private String tipoDetalle;
	private char tipoAfiliadoAcronimo;
	private String tipoAfiliadoNombre;
	private int detalleCodigo;
	private boolean manejaDetalle;	
	private Double valor;
	private String valorHelper;
	private Double porcentaje;
	private String porcentajeHelper;
	private Integer cantidadMonto;
	private boolean permiteEliminar;
	private Long idSubcuenta;
	private int idMontoCobro;
	private String usuarioRegistra; 
	private boolean activo;	
	private ArrayList<DTOBusquedaMontoAgrupacionServicio> listaAgrupacionServicios;
	private ArrayList<DTOBusquedaMontoServicioEspecifico> listaServicioEspecifico;
	private ArrayList<DTOBusquedaMontoArticuloEspecifico> listaArticuloEspecifico;
	private ArrayList<DTOBusquedaMontoAgrupacionArticulo> listaAgrupacionArticulo;
	
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public DTOResultadoBusquedaDetalleMontos(){
		
	}
	
	/**
	 * 
	 * Método constructor de la clase
	 * 
	 * @author, Angela Maria Aguirre
	 */
	public DTOResultadoBusquedaDetalleMontos(int estratoID, int tipoMontoID,
			int detalleCodigo,int viaIngresoID,Integer naturalezaID,
			ArrayList<DTOBusquedaMontoServicioEspecifico> listaServicioEspecifico){
		
		this.estratoID=estratoID;
		this.tipoMontoID=tipoMontoID;
		this.viaIngresoID=viaIngresoID;
		this.naturalezaID=naturalezaID;
		this.listaServicioEspecifico=listaServicioEspecifico;
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo viaIngresoID
	
	 * @return retorna la variable viaIngresoID 
	 * @author Angela Maria Aguirre 
	 */
	public int getViaIngresoID() {
		return viaIngresoID;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo viaIngresoID
	
	 * @param valor para el atributo viaIngresoID 
	 * @author Angela Maria Aguirre 
	 */
	public void setViaIngresoID(int viaIngresoID) {
		this.viaIngresoID = viaIngresoID;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo viaIngresoNombre
	
	 * @return retorna la variable viaIngresoNombre 
	 * @author Angela Maria Aguirre 
	 */
	public String getViaIngresoNombre() {
		return viaIngresoNombre;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo viaIngresoNombre
	
	 * @param valor para el atributo viaIngresoNombre 
	 * @author Angela Maria Aguirre 
	 */
	public void setViaIngresoNombre(String viaIngresoNombre) {
		this.viaIngresoNombre = viaIngresoNombre;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo tipoPacienteAcronimo
	
	 * @return retorna la variable tipoPacienteAcronimo 
	 * @author Angela Maria Aguirre 
	 */
	public String getTipoPacienteAcronimo() {
		return tipoPacienteAcronimo;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoPacienteAcronimo
	
	 * @param valor para el atributo tipoPacienteAcronimo 
	 * @author Angela Maria Aguirre 
	 */
	public void setTipoPacienteAcronimo(String tipoPacienteAcronimo) {
		this.tipoPacienteAcronimo = tipoPacienteAcronimo;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo tipoPacienteNombre
	
	 * @return retorna la variable tipoPacienteNombre 
	 * @author Angela Maria Aguirre 
	 */
	public String getTipoPacienteNombre() {
		return tipoPacienteNombre;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoPacienteNombre
	
	 * @param valor para el atributo tipoPacienteNombre 
	 * @author Angela Maria Aguirre 
	 */
	public void setTipoPacienteNombre(String tipoPacienteNombre) {
		this.tipoPacienteNombre = tipoPacienteNombre;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo estratoID
	
	 * @return retorna la variable estratoID 
	 * @author Angela Maria Aguirre 
	 */
	public int getEstratoID() {
		return estratoID;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo estratoID
	
	 * @param valor para el atributo estratoID 
	 * @author Angela Maria Aguirre 
	 */
	public void setEstratoID(int estratoID) {
		this.estratoID = estratoID;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo estratoDescripcion
	
	 * @return retorna la variable estratoDescripcion 
	 * @author Angela Maria Aguirre 
	 */
	public String getEstratoDescripcion() {
		return estratoDescripcion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo estratoDescripcion
	
	 * @param valor para el atributo estratoDescripcion 
	 * @author Angela Maria Aguirre 
	 */
	public void setEstratoDescripcion(String estratoDescripcion) {
		this.estratoDescripcion = estratoDescripcion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo naturalezaID
	
	 * @return retorna la variable naturalezaID 
	 * @author Angela Maria Aguirre 
	 */
	public Integer getNaturalezaID() {
		return naturalezaID;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo naturalezaID
	
	 * @param valor para el atributo naturalezaID 
	 * @author Angela Maria Aguirre 
	 */
	public void setNaturalezaID(Integer naturalezaID) {
		this.naturalezaID = naturalezaID;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo naturalezaNombre
	
	 * @return retorna la variable naturalezaNombre 
	 * @author Angela Maria Aguirre 
	 */
	public String getNaturalezaNombre() {		
		return naturalezaNombre;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo naturalezaNombre
	
	 * @param valor para el atributo naturalezaNombre 
	 * @author Angela Maria Aguirre 
	 */
	public void setNaturalezaNombre(String naturalezaNombre) {
		if(naturalezaNombre==null){
			naturalezaNombre="";
		}
		this.naturalezaNombre = naturalezaNombre;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo tipoMontoID
	
	 * @return retorna la variable tipoMontoID 
	 * @author Angela Maria Aguirre 
	 */
	public int getTipoMontoID() {
		return tipoMontoID;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoMontoID
	
	 * @param valor para el atributo tipoMontoID 
	 * @author Angela Maria Aguirre 
	 */
	public void setTipoMontoID(int tipoMontoID) {
		this.tipoMontoID = tipoMontoID;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo tipoMontoNombre
	
	 * @return retorna la variable tipoMontoNombre 
	 * @author Angela Maria Aguirre 
	 */
	public String getTipoMontoNombre() {
		return tipoMontoNombre;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoMontoNombre
	
	 * @param valor para el atributo tipoMontoNombre 
	 * @author Angela Maria Aguirre 
	 */
	public void setTipoMontoNombre(String tipoMontoNombre) {
		this.tipoMontoNombre = tipoMontoNombre;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo tipoDetalle
	
	 * @return retorna la variable tipoDetalle 
	 * @author Angela Maria Aguirre 
	 */
	public String getTipoDetalle() {
		return tipoDetalle;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoDetalle
	
	 * @param valor para el atributo tipoDetalle 
	 * @author Angela Maria Aguirre 
	 */
	public void setTipoDetalle(String tipoDetalle) {
		this.tipoDetalle = tipoDetalle;
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo tipoAfiliadoAcronimo
	
	 * @return retorna la variable tipoAfiliadoAcronimo 
	 * @author Angela Maria Aguirre 
	 */
	public char getTipoAfiliadoAcronimo() {
		return tipoAfiliadoAcronimo;
	}	
	
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoAfiliadoAcronimo
	 * 	
	 * @param Char, valor para el atributo tipoAfiliadoAcronimo 
	 * @author Angela Maria Aguirre 
	 */
	public void setTipoAfiliadoAcronimo(char tipoAfiliadoAcronimo) {
		this.tipoAfiliadoAcronimo = tipoAfiliadoAcronimo;
		
	}
		
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo tipoAfiliadoNombre
	 * 	
	 * @return retorna la variable tipoAfiliadoNombre 
	 * @author Angela Maria Aguirre 
	 */
	public String getTipoAfiliadoNombre() {
		return tipoAfiliadoNombre;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoAfiliadoNombre
	
	 * @param valor para el atributo tipoAfiliadoNombre 
	 * @author Angela Maria Aguirre 
	 */
	public void setTipoAfiliadoNombre(String tipoAfiliadoNombre) {
		this.tipoAfiliadoNombre = tipoAfiliadoNombre;
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo detalleCodigo
	 * 	
	 * @return retorna la variable detalleCodigo 
	 * @author Angela Maria Aguirre 
	 */
	public int getDetalleCodigo() {
		return detalleCodigo;
	}
	
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo detalleCodigo
	
	 * @param valor para el atributo detalleCodigo 
	 * @author Angela Maria Aguirre 
	 */
	public void setDetalleCodigo(int detalleCodigo) {
		this.detalleCodigo = detalleCodigo;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo tipoDetalleAcronimo
	
	 * @return retorna la variable tipoDetalleAcronimo 
	 * @author Angela Maria Aguirre 
	 */
	public String getTipoDetalleAcronimo() {
		return tipoDetalleAcronimo;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoDetalleAcronimo
	
	 * @param valor para el atributo tipoDetalleAcronimo 
	 * @author Angela Maria Aguirre 
	 */
	public void setTipoDetalleAcronimo(String tipoDetalleAcronimo) {
		this.tipoDetalleAcronimo = tipoDetalleAcronimo;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo manejaDetalle
	
	 * @return retorna la variable manejaDetalle 
	 * @author Angela Maria Aguirre 
	 */
	public boolean isManejaDetalle() {
		return manejaDetalle;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo manejaDetalle
	
	 * @param valor para el atributo manejaDetalle 
	 * @author Angela Maria Aguirre 
	 */
	public void setManejaDetalle(boolean manejaDetalle) {
		this.manejaDetalle = manejaDetalle;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo valor
	
	 * @return retorna la variable valor 
	 * @author Angela Maria Aguirre 
	 */
	public Double getValor() {
		return valor;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo valor
	
	 * @param valor para el atributo valor 
	 * @author Angela Maria Aguirre 
	 */
	public void setValor(Double valor) {		
		if(valor!=null && valor.doubleValue()>=0){
			this.valor = valor;
			this.valorHelper = valor.toString();
		}else{
			this.valor = null;
		}			
	}
	
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo valorHelper
	
	 * @return retorna la variable valorHelper 
	 * @author Angela Maria Aguirre 
	 */
	public String getValorHelper() {
		return valorHelper;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo valorHelper
	
	 * @param valor para el atributo valorHelper 
	 * @author Angela Maria Aguirre 
	 */
	public void setValorHelper(String valorHelper) {
		this.valorHelper=valorHelper;
		if(!UtilidadTexto.isEmpty(valorHelper)){
			Double valorAux = Utilidades.convertirADouble(valorHelper, false);
			this.valor = valorAux;			
		}else{
			this.valor = null;
		}
	}	
		
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo porcentaje
	
	 * @return retorna la variable porcentaje 
	 * @author Angela Maria Aguirre 
	 */
	public Double getPorcentaje() {
		return porcentaje;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo porcentaje
	
	 * @param valor para el atributo porcentaje 
	 * @author Angela Maria Aguirre 
	 */
	public void setPorcentaje(Double porcentaje) {
		if(porcentaje!=null && porcentaje.doubleValue()>=0){
			this.porcentaje = porcentaje;
			this.porcentajeHelper = porcentaje.toString();
		}else{
			this.porcentaje = null;
		}
	}
	
	

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo cantidadMonto
	
	 * @return retorna la variable cantidadMonto 
	 * @author Angela Maria Aguirre 
	 */
	public Integer getCantidadMonto() {
		return cantidadMonto;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo cantidadMonto
	
	 * @param valor para el atributo cantidadMonto 
	 * @author Angela Maria Aguirre 
	 */
	public void setCantidadMonto(Integer cantidadMonto) {
		this.cantidadMonto = cantidadMonto;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo permiteEliminar
	
	 * @return retorna la variable permiteEliminar 
	 * @author Angela Maria Aguirre 
	 */
	public boolean isPermiteEliminar() {
		return permiteEliminar;
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo permiteEliminar
	
	 * @return retorna la variable permiteEliminar 
	 * @author Angela Maria Aguirre 
	 */
	public boolean getPermiteEliminar() {
		return permiteEliminar;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo permiteEliminar
	
	 * @param valor para el atributo permiteEliminar 
	 * @author Angela Maria Aguirre 
	 */
	public void setPermiteEliminar(boolean permiteEliminar) {
		this.permiteEliminar = permiteEliminar;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo idSubcuenta
	
	 * @return retorna la variable idSubcuenta 
	 * @author Angela Maria Aguirre 
	 */
	public Long getIdSubcuenta() {
		return idSubcuenta;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo idSubcuenta
	
	 * @param valor para el atributo idSubcuenta 
	 * @author Angela Maria Aguirre 
	 */
	public void setIdSubcuenta(Long idSubcuenta) {
		this.idSubcuenta = idSubcuenta;
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo idMontoCobro
	
	 * @return retorna la variable idMontoCobro 
	 * @author Angela Maria Aguirre 
	 */
	public int getIdMontoCobro() {
		return idMontoCobro;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo idMontoCobro
	
	 * @param valor para el atributo idMontoCobro 
	 * @author Angela Maria Aguirre 
	 */
	public void setIdMontoCobro(int idMontoCobro) {
		this.idMontoCobro = idMontoCobro;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo usuarioRegistra
	
	 * @return retorna la variable usuarioRegistra 
	 * @author Angela Maria Aguirre 
	 */
	public String getUsuarioRegistra() {
		return usuarioRegistra;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo usuarioRegistra
	
	 * @param valor para el atributo usuarioRegistra 
	 * @author Angela Maria Aguirre 
	 */
	public void setUsuarioRegistra(String usuarioRegistra) {
		this.usuarioRegistra = usuarioRegistra;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo activo
	
	 * @return retorna la variable activo 
	 * @author Angela Maria Aguirre 
	 */
	public boolean isActivo() {
		return activo;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo activo
	
	 * @param valor para el atributo activo 
	 * @author Angela Maria Aguirre 
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaAgrupacionServicios
	
	 * @return retorna la variable listaAgrupacionServicios 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DTOBusquedaMontoAgrupacionServicio> getListaAgrupacionServicios() {
		return listaAgrupacionServicios;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaAgrupacionServicios
	
	 * @param valor para el atributo listaAgrupacionServicios 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaAgrupacionServicios(
			ArrayList<DTOBusquedaMontoAgrupacionServicio> listaAgrupacionServicios) {
		this.listaAgrupacionServicios = listaAgrupacionServicios;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaServicioEspecifico
	
	 * @return retorna la variable listaServicioEspecifico 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DTOBusquedaMontoServicioEspecifico> getListaServicioEspecifico() {
		return listaServicioEspecifico;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaServicioEspecifico
	
	 * @param valor para el atributo listaServicioEspecifico 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaServicioEspecifico(
			ArrayList<DTOBusquedaMontoServicioEspecifico> listaServicioEspecifico) {
		this.listaServicioEspecifico = listaServicioEspecifico;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaArticuloEspecifico
	
	 * @return retorna la variable listaArticuloEspecifico 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DTOBusquedaMontoArticuloEspecifico> getListaArticuloEspecifico() {
		return listaArticuloEspecifico;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaArticuloEspecifico
	
	 * @param valor para el atributo listaArticuloEspecifico 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaArticuloEspecifico(
			ArrayList<DTOBusquedaMontoArticuloEspecifico> listaArticuloEspecifico) {
		this.listaArticuloEspecifico = listaArticuloEspecifico;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaAgrupacionArticulo
	
	 * @return retorna la variable listaAgrupacionArticulo 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DTOBusquedaMontoAgrupacionArticulo> getListaAgrupacionArticulo() {
		return listaAgrupacionArticulo;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaAgrupacionArticulo
	
	 * @param valor para el atributo listaAgrupacionArticulo 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaAgrupacionArticulo(
			ArrayList<DTOBusquedaMontoAgrupacionArticulo> listaAgrupacionArticulo) {
		this.listaAgrupacionArticulo = listaAgrupacionArticulo;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo porcentajeHelper
	
	 * @return retorna la variable porcentajeHelper 
	 * @author Angela Maria Aguirre 
	 */
	public String getPorcentajeHelper() {
		return porcentajeHelper;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo porcentajeHelper
	
	 * @param valor para el atributo porcentajeHelper 
	 * @author Angela Maria Aguirre 
	 */
	public void setPorcentajeHelper(String porcentajeHelper) {
		this.porcentajeHelper = porcentajeHelper;
		if(!UtilidadTexto.isEmpty(porcentajeHelper)){
			Double porcentajeAux = Utilidades.convertirADouble(porcentajeHelper, false);
			this.porcentaje = porcentajeAux;			
		}else{
			this.porcentaje = null;
		}
		
	}	
	
}
