package com.princetonsa.dto.facturacion;

import java.util.ArrayList;
import java.util.Date;

import com.servinte.axioma.orm.EmpresasInstitucion;

/**
 * Esta clase se encarga de almacenar los datos del encabezado
 * del detalle de un monto de cobro
 * 
 * @author Angela Maria Aguirre
 * @since 30/08/2010
 */
public class DTOMontosCobro {
	
	private int viaIngresoID;
	private String viaIngresoNombre;
	private String tipoPacienteAcronimo;
	private String tipoPacienteNombre;
	private Date fechaVigenciaConvenio;			
	private DtoConvenio convenio;
	private int montoCobroID;
	private String tipoTransaccion;
	private ArrayList<DTOResultadoBusquedaDetalleMontos> listaDetalles;
	private EmpresasInstitucion empresasInstitucion;
	
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
	 * del atributo fechaVigenciaConvenio
	
	 * @return retorna la variable fechaVigenciaConvenio 
	 * @author Angela Maria Aguirre 
	 */
	public Date getFechaVigenciaConvenio() {
		return fechaVigenciaConvenio;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo fechaVigenciaConvenio
	
	 * @param valor para el atributo fechaVigenciaConvenio 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaVigenciaConvenio(Date fechaVigenciaConvenio) {
		this.fechaVigenciaConvenio = fechaVigenciaConvenio;
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaDetalles
	
	 * @return retorna la variable listaDetalles 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DTOResultadoBusquedaDetalleMontos> getListaDetalles() {
		return listaDetalles;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaDetalles
	
	 * @param valor para el atributo listaDetalles 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaDetalles(ArrayList<DTOResultadoBusquedaDetalleMontos> listaDetalles) {
		this.listaDetalles = listaDetalles;
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo montoCobroID
	
	 * @return retorna la variable montoCobroID 
	 * @author Angela Maria Aguirre 
	 */
	public int getMontoCobroID() {
		return montoCobroID;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo montoCobroID
	
	 * @param valor para el atributo montoCobroID 
	 * @author Angela Maria Aguirre 
	 */
	public void setMontoCobroID(int montoCobroID) {
		this.montoCobroID = montoCobroID;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo convenio
	
	 * @return retorna la variable convenio 
	 * @author Angela Maria Aguirre 
	 */
	public DtoConvenio getConvenio() {
		return convenio;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo convenio
	
	 * @param valor para el atributo convenio 
	 * @author Angela Maria Aguirre 
	 */
	public void setConvenio(DtoConvenio convenio) {
		this.convenio = convenio;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo empresasInstitucion
	
	 * @return retorna la variable empresasInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public EmpresasInstitucion getEmpresasInstitucion() {
		return empresasInstitucion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo empresasInstitucion
	
	 * @param valor para el atributo empresasInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public void setEmpresasInstitucion(EmpresasInstitucion empresasInstitucion) {
		this.empresasInstitucion = empresasInstitucion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo tipoTransaccion
	
	 * @return retorna la variable tipoTransaccion 
	 * @author Angela Maria Aguirre 
	 */
	public String getTipoTransaccion() {
		return tipoTransaccion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoTransaccion
	
	 * @param valor para el atributo tipoTransaccion 
	 * @author Angela Maria Aguirre 
	 */
	public void setTipoTransaccion(String tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}
	
}
