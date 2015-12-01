/*
 * Mayo 6, 2008
 */
package com.princetonsa.dto.historiaClinica.parametrizacion;

import java.io.Serializable;
import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoTratamientoExterno;
import com.princetonsa.dto.odontologia.DtoTratamientoInterno;
import com.servinte.axioma.generadorReporte.odontologia.dtoReportes.DtoFormatoPlantillaOrdenarColumnasPDF;
import com.servinte.axioma.generadorReporte.odontologia.dtoReportes.DtoFormatoPlantillaDetalleHallazgosPlanTratamientoInicial;
import com.servinte.axioma.generadorReporte.odontologia.dtoReportes.DtoFormatoPlantillaHallazgosPiezasDentales;
import com.servinte.axioma.generadorReporte.odontologia.dtoReportes.DtoFormatoPlantillaProgServCitaAsignada;

import net.sf.jasperreports.engine.JRDataSource;

import util.ConstantesBD;
import util.InfoDatosInt;

/**
 * Data Transfer Object: Sección Fija
 * @author Sebastián Gómez R.
 *
 */
public class DtoSeccionFija implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String codigoPK;
	
	/**
	 * Codigo de fun_param_secciones_fijas 
	 * */
	private String codigoPkFunParamSecFij;
	
	/**
	 * Codigo de Secciones fijas
	 * */
	private String codigoSeccionParam;
	
	/**
	 * Codigo Nombre que identifica la sección
	 */
	private InfoDatosInt seccion;
	private int orden;
	private boolean visible;
	private boolean mostrarModificacion;
	private boolean esFija;
	private ArrayList<DtoElementoParam> elementos;
	private boolean enviarEpicrisis;
	private String nombreS;
	private String detalle;
	private String fechas;
	private String imagen;
	
	
	//Dto de informacion para la generacion de tablas en ireport en diferentes componentes
	private ArrayList<DtoFormatoPlantillaDetalleHallazgosPlanTratamientoInicial> dtohallazgos;
	private ArrayList<DtoFormatoPlantillaHallazgosPiezasDentales> dtohallazgosPiezasDentales;
	private ArrayList<DtoFormatoPlantillaHallazgosPiezasDentales> dtohallazgosBoca;
	private ArrayList<DtoTratamientoInterno> dtoTratamientosInternos;
	private ArrayList<DtoTratamientoExterno> dtoTratamientosExternos;
	private ArrayList<DtoFormatoPlantillaProgServCitaAsignada> dtoProgServCitaAsignada;
	private ArrayList<DtoFormatoPlantillaOrdenarColumnasPDF> dtoOrdenarColumnas;
	
	
    /** Objeto jasper para el subreporte elementos */
    private JRDataSource dsHallazgos;
    
    /** Objeto jasper para el subreporte hallazgos piezas dentales para el componente Odontograma Diagnostico */
    private JRDataSource dsHallazgosPiezasDentales;
    
    /** Objeto jasper para el subreporte hallazgos boca para el componente Odontograma Diagnostico */
    private JRDataSource dsHallazgosBoca;
    
    /** Objeto jasper para el subreporte tratamientos internos para el componente Antecedentes Odontologicos */
    private JRDataSource dsTratamientosInternos;
    
    /** Objeto jasper para el subreporte tratamientos externos para el componente Antecedentes Odontologicos */
    private JRDataSource dsTratamientosExternos;
    
    /** Objeto jasper para el subreporte programas servicios para el componente odontograma de evolucion */
    private JRDataSource dsProgServCitaAsign;
    
    /** Objeto jasper para el subreporte de ordenamiento por columnas de los detalles */
    private JRDataSource dsOrdenar;
	
	/**
	 * Resetea los datos del DTO
	 *
	 */
	public void clean()
	{
		this.codigoPK = "";
		this.codigoPkFunParamSecFij = ""; 
		this.codigoSeccionParam = "";
		this.seccion = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");		
		this.orden = ConstantesBD.codigoNuncaValido;
		this.visible = false;
		this.mostrarModificacion = false;
		this.esFija = true;
		this.elementos = new ArrayList<DtoElementoParam>();
		this.enviarEpicrisis = false;
		this.detalle=new String("");
		this.dtohallazgos=new ArrayList<DtoFormatoPlantillaDetalleHallazgosPlanTratamientoInicial>();
		this.dtohallazgosPiezasDentales=new ArrayList<DtoFormatoPlantillaHallazgosPiezasDentales>();
		this.dtohallazgosBoca=new ArrayList<DtoFormatoPlantillaHallazgosPiezasDentales>();
		this.setDtoTratamientosExternos(new ArrayList<DtoTratamientoExterno>());
		this.setDtoTratamientosInternos(new ArrayList<DtoTratamientoInterno>());
		this.setDtoProgServCitaAsignada(new ArrayList<DtoFormatoPlantillaProgServCitaAsignada>());
		this.setDtoOrdenarColumnas(new ArrayList<DtoFormatoPlantillaOrdenarColumnasPDF>());
	}
	
	/**
	 * Constructor del DTO
	 *
	 */
	public DtoSeccionFija()
	{
		this.clean();
	}

	/**
	 * @return the orden
	 */
	public int getOrden() {
		return orden;
	}

	/**
	 * @param orden the orden to set
	 */
	public void setOrden(int orden) {
		this.orden = orden;
	}

	/**
	 * @return the seccion
	 */
	public int getCodigoSeccion() {
		return seccion.getCodigo();
	}

	/**
	 * @param seccion the seccion to set
	 */
	public void setCodigoSeccion(int seccion) {
		this.seccion.setCodigo(seccion);
	}
	
	/**
	 * @return the seccion
	 */
	public String getNombreSeccion() {
		return seccion.getNombre();
	}

	/**
	 * @param seccion the seccion to set
	 */
	public void setNombreSeccion(String seccion) {
		this.seccion.setNombre(seccion);
	}

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * @return the elementos
	 */
	public ArrayList<DtoElementoParam> getElementos() {
		return elementos;
	}
	
	/**
	 * @return the elementos
	 */
	public DtoElementoParam getElementosPos(int index) {
		return elementos.get(index);
	}
	
	/**
	 * @return the elementos
	 */
	public DtoSeccionParametrizable getElementoSeccionPos(int index) 
	{		
		return (DtoSeccionParametrizable)elementos.get(index);		
	}
	
	/**
	 * @return the elementos
	 */
	public DtoEscala getElementoEscalaPos(int index) 
	{		
		return (DtoEscala)elementos.get(index);		
	}
		
	/**
	 * @return the elementos
	 */
	public DtoComponente getElementoComponentePos(int index) 
	{		
		return (DtoComponente)elementos.get(index);		
	}

	/**
	 * @param elementos the elementos to set
	 */
	public void setElementos(ArrayList<DtoElementoParam> elementos) {
		this.elementos = elementos;
	}

	/**
	 * @return the codigoPK
	 */
	public String getCodigoPK() {
		return codigoPK;
	}

	/**
	 * @param codigoPK the codigoPK to set
	 */
	public void setCodigoPK(String codigoPK) {
		this.codigoPK = codigoPK;
	}

	/**
	 * @return the mostrarModificacion
	 */
	public boolean isMostrarModificacion() {
		return mostrarModificacion;
	}

	/**
	 * @param mostrarModificacion the mostrarModificacion to set
	 */
	public void setMostrarModificacion(boolean mostrarModificacion) {
		this.mostrarModificacion = mostrarModificacion;
	}

	/**
	 * @return the codigoPkFunParamSecFij
	 */
	public String getCodigoPkFunParamSecFij() {
		return codigoPkFunParamSecFij;
	}

	/**
	 * @param codigoPkFunParamSecFij the codigoPkFunParamSecFij to set
	 */
	public void setCodigoPkFunParamSecFij(String codigoPkFunParamSecFij) {
		this.codigoPkFunParamSecFij = codigoPkFunParamSecFij;
	}

	/**
	 * @return the codigoSeccionParam
	 */
	public String getCodigoSeccionParam() {
		return codigoSeccionParam;
	}

	/**
	 * @param codigoSeccionParam the codigoSeccionParam to set
	 */
	public void setCodigoSeccionParam(String codigoSeccionParam) {
		this.codigoSeccionParam = codigoSeccionParam;
	}

	/**
	 * @return the seccion
	 */
	public InfoDatosInt getSeccion() {
		return seccion;
	}

	/**
	 * @param seccion the seccion to set
	 */
	public void setSeccion(InfoDatosInt seccion) {
		this.seccion = seccion;
	}

	/**
	 * @return the esFija
	 */
	public boolean isEsFija() {
		return esFija;
	}

	/**
	 * @param esFija the esFija to set
	 */
	public void setEsFija(boolean esFija) {
		this.esFija = esFija;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNumElementosVisibles()
	{
		int numero = 0;
		
		for(DtoElementoParam elemento:this.elementos)
			if(elemento.isVisible())
				numero++;
		
		return numero;
	}

	public boolean isEnviarEpicrisis() {
		return enviarEpicrisis;
	}

	public void setEnviarEpicrisis(boolean enviarEpicrisis) {
		this.enviarEpicrisis = enviarEpicrisis;
	}

	/**
	 * Método que almacena el nombre de la seccion de la plantilla
	 * @param nombreS
	 */
	public void setNombreS(String nombreS) {
		this.nombreS = nombreS;
	}

	/**
	 * Método que devuelve el nombre de la seccion
	 * @return nombreS
	 */
	public String getNombreS() {
		return nombreS;
	}

	/**
	 * Método que devuelve el detalle de cada sección almacenado en un String
	 * @param detalle
	 */
	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	/**
	 * Método que devuelve el valor del atributo detalle
	 * @return detalle
	 */
	public String getDetalle() {
		return detalle;
	}

	/**
	 * Método que almacena el nombre de la imagen
	 * @param imagen
	 */
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	/**
	 * Metodo que el nombre de la imagen
	 * @return imagen
	 */
	public String getImagen() {
		return imagen;
	}

	/**
	 * Método que almacena el dto con la información de los hallazgos en el
	 * plan de tratamiento para crear un subreporte, utilizado en el tipo de
	 * componente odontograma
	 * @param ArrayList<DtoFormatoPlantillaDetalleHallazgosPlanTratamientoInicial>
	 */
	public void setDtohallazgos(ArrayList<DtoFormatoPlantillaDetalleHallazgosPlanTratamientoInicial> dtohallazgos) {
		this.dtohallazgos = dtohallazgos;
	}

	/**
	 * Método que devuelve e valor del atributo dtohallazgos
	 * @return ArrayList<DtoFormatoPlantillaDetalleHallazgosPlanTratamientoInicial>
	 */
	public ArrayList<DtoFormatoPlantillaDetalleHallazgosPlanTratamientoInicial> getDtohallazgos() {
		return dtohallazgos;
	}

	/**
	 * Método que almacena el datasource de de los hallazgos en el
	 * plan de tratamiento, utilizado en el tipo de
	 * componente odontograma
	 * @param dsHallazgos
	 */
	public void setDsHallazgos(JRDataSource dsHallazgos) {
		this.dsHallazgos = dsHallazgos;
	}

	/**
 	 * Método que devuelve el valor del atributo dsHallazgos
	 * @return dsHallazgos
	 */
	public JRDataSource getDsHallazgos() {
		return dsHallazgos;
	}

	/**
	 * @param 
	 */
	public void setFechas(String fechas) {
		this.fechas = fechas;
	}

	/**
	 * 
	 * @return
	 */
	public String getFechas() {
		return fechas;
	}

	/**
	 * Método que almacena el dto con la información de los hallazgos 
	 * de piezas dentales para crear un subreporte, utilizado en el tipo de
	 * componente odontograma
	 * @param ArrayList<DtoFormatoPlantillaHallazgosPiezasDentales>
	 */
	public void setDtohallazgosPiezasDentales(
			ArrayList<DtoFormatoPlantillaHallazgosPiezasDentales> dtohallazgosPiezasDentales) {
		this.dtohallazgosPiezasDentales = dtohallazgosPiezasDentales;
	}

	/**
	 * Método que devuelve e valor del atributo dtohallazgosPiezasDentales
	 * @return dtohallazgosPiezasDentales
	 */
	public ArrayList<DtoFormatoPlantillaHallazgosPiezasDentales> getDtohallazgosPiezasDentales() {
		return dtohallazgosPiezasDentales;
	}

	/**
	 * Método que almacena el datasource de de los hallazgos en 
	 * piezas dentales, utilizado en el tipo de
	 * componente odontograma
	 * @param dsHallazgosPiezasDentales
	 */
	public void setDsHallazgosPiezasDentales(JRDataSource dsHallazgosPiezasDentales) {
		this.dsHallazgosPiezasDentales = dsHallazgosPiezasDentales;
	}

	/**
	 * Método que devuelve e valor del atributo dsHallazgosPiezasDentales
	 * @return dsHallazgosPiezasDentales
	 */
	public JRDataSource getDsHallazgosPiezasDentales() {
		return dsHallazgosPiezasDentales;
	}

	/**
	 * Método que almacena el dto con la información de los hallazgos 
	 * en boca para crear un subreporte, utilizado en el tipo de
	 * componente odontograma, utiliza el mismo dto de piezas dentales
	 * por tener los mismos atributos
	 * @param ArrayList<DtoFormatoPlantillaHallazgosPiezasDentales>
	 */
	public void setDtohallazgosBoca(ArrayList<DtoFormatoPlantillaHallazgosPiezasDentales> dtohallazgosBoca) {
		this.dtohallazgosBoca = dtohallazgosBoca;
	}

	/**
	 * Método que devuelve e valor del atributo dtohallazgosBoca
	 * @return dtohallazgosBoca
	 */
	public ArrayList<DtoFormatoPlantillaHallazgosPiezasDentales> getDtohallazgosBoca() {
		return dtohallazgosBoca;
	}

	/**
	 * Método que almacena el datasource de de los hallazgos en 
	 * boca, utilizado en el tipo de
	 * componente odontograma
	 * @param dsHallazgosBoca
	 */
	public void setDsHallazgosBoca(JRDataSource dsHallazgosBoca) {
		this.dsHallazgosBoca = dsHallazgosBoca;
	}

	/**
	 * Método que devuelve el valor del atributo dsHallazgosBoca
	 * @return dsHallazgosBoca
	 */
	public JRDataSource getDsHallazgosBoca() {
		return dsHallazgosBoca;
	}

	/**
	 * Método que almacena el datasource de los tratamientos internos
	 * utilizado en el tipo de
	 * componente antecedentes odontologicos
	 * @param dsTratamientosInternos
	 */
	public void setDsTratamientosInternos(JRDataSource dsTratamientosInternos) {
		this.dsTratamientosInternos = dsTratamientosInternos;
	}

	/**
	 * Método que devuelve el valor del atributo dsTratamientosInternos
	 * @return dsTratamientosInternos
	 */
	public JRDataSource getDsTratamientosInternos() {
		return dsTratamientosInternos;
	}

	/**
	 * Método que almacena el dto con la información de los tratamientos 
	 * internos para crear un subreporte, utilizado en el tipo de
	 * componente antecedentes odontologicos
	 * @param ArrayList<DtoTratamientoInterno>
	 */
	public void setDtoTratamientosInternos(ArrayList<DtoTratamientoInterno> dtoTratamientosInternos) {
		this.dtoTratamientosInternos = dtoTratamientosInternos;
	}

	/**
	 * Método que devuelve el valor del atributo dtoTratamientosInternos
	 * @return dtoTratamientosInternos
	 */
	public ArrayList<DtoTratamientoInterno> getDtoTratamientosInternos() {
		return dtoTratamientosInternos;
	}

	/**
	 * Método que almacena el dto con la información de los tratamientos 
	 * externos para crear un subreporte, utilizado en el tipo de
	 * componente antecedentes odontologicos
	 * @param ArrayList<DtoTratamientoExterno>
	 */ 
	public void setDtoTratamientosExternos(ArrayList<DtoTratamientoExterno> dtoTratamientosExternos) {
		this.dtoTratamientosExternos = dtoTratamientosExternos;
	}

	/**
	 * Método que devuelve el valor del atributo dtoTratamientosExternos
	 * @return dtoTratamientosExternos
	 */
	public ArrayList<DtoTratamientoExterno> getDtoTratamientosExternos() {
		return dtoTratamientosExternos;
	}

	/**
	 * Método que almacena el datasource de los tratamientos externos
	 * utilizado en el tipo de
	 * componente antecedentes odontologicos
	 * @param dsTratamientosExternos
	 */
	public void setDsTratamientosExternos(JRDataSource dsTratamientosExternos) {
		this.dsTratamientosExternos = dsTratamientosExternos;
	}

	/**
	 * Método que devuelve el valor del atributo dsTratamientosExternos
	 * @return dsTratamientosExternos
	 */
	public JRDataSource getDsTratamientosExternos() {
		return dsTratamientosExternos;
	}


	/**
	 * Método que almacena el datasource de los programas y servicios
	 * de la cita asignada utilizado en el tipo de
	 * componente odontograma de evolución
	 * @param dsProgServCitaAsign
	 */
	public void setDsProgServCitaAsign(JRDataSource dsProgServCitaAsign) {
		this.dsProgServCitaAsign = dsProgServCitaAsign;
	}

	/**
	 * Método que devuelve el valor del atributo dsProgServCitaAsign
	 * @return dsProgServCitaAsign
	 */
	public JRDataSource getDsProgServCitaAsign() {
		return dsProgServCitaAsign;
	}

	/**
	 * Método que almacena el dto con la información de los programas 
	 * y servicios de la cita asignada para crear un subreporte, 
	 * utilizado en el tipo de componente odontograma evolución
	 * @param ArrayList<DtoFormatoPlantillaProgServCitaAsignada>
	 */
	public void setDtoProgServCitaAsignada(ArrayList<DtoFormatoPlantillaProgServCitaAsignada> dtoProgServCitaAsignada) {
		this.dtoProgServCitaAsignada = dtoProgServCitaAsignada;
	}

	/**
	 * Método que devuelve el valor del atributo dtoProgServCitaAsignada
	 * @return dtoProgServCitaAsignada
	 */
	public ArrayList<DtoFormatoPlantillaProgServCitaAsignada> getDtoProgServCitaAsignada() {
		return dtoProgServCitaAsignada;
	}

	public void setDtoOrdenarColumnas(ArrayList<DtoFormatoPlantillaOrdenarColumnasPDF> dtoOrdenarColumnas) {
		this.dtoOrdenarColumnas = dtoOrdenarColumnas;
	}

	public ArrayList<DtoFormatoPlantillaOrdenarColumnasPDF> getDtoOrdenarColumnas() {
		return dtoOrdenarColumnas;
	}

	public void setDsOrdenar(JRDataSource dsOrdenar) {
		this.dsOrdenar = dsOrdenar;
	}

	public JRDataSource getDsOrdenar() {
		return dsOrdenar;
	}

		
	
}
