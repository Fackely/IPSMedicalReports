/*
 * @(#)BeanBusquedaServicio.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01 
 *
 */

package util.Beans;

/**
 * Clase para manejar la captura de los datos sugeridos por el usuario 
 * en la búsqueda de servicios
 *
 * @version 1.0 Dec 3, 2003
 */
public class BeanBusquedaServicio
{
	/**
	 * Código de la especialidad por la que se desea buscar
	 */
	private int especialidad;
	
	/**
	 * Código del sexo por el que se desea buscar
	 */
	private int sexo;
	
	/**
	 * Código de axioma por el que se desea buscar
	 */
	private int codigoAxioma=-1;
	
	/**
	 * Parte de la descripción del cups por la que se desea buscar
	 */
	private String descripcionCups;

	/**
	 * Código del tipo de servicio por el que se desea buscar
	 */
	private String tipoServicio;

	
	
	/**
	 * Código del tipo de servicio por el que se desea buscar
	 */
	private String naturalezaServicio;
	
	/**
	 * Código del grupo del servicio
	 */
	private int grupoServicio;

	/**
	 * Campo que define si este servicio en el POS o no (0)
	 */
	private int codigoPos;
	
	/**
	 * 
	 */
	private String esPosSubsidiado;

	/**
	 * Código de este servicio en el Cups
	 */
	private String codigoCups;

	/**
	 * Código de este servicio en el ISS
	 */
	private String codigoISS;
	
	/**
	 * Parte de la descripción del cups por la que se desea buscar
	 */
	private String descripcionISS;
	


	/**
	 * Código de este servicio en el Soat
	 */
	private String codigoSOAT;

	/**
	 * Parte de la descripción del soat por la que se desea buscar
	 */
	private String descripcionSOAT;

	/***
	 * Codigo de este servicio en SONRIA
	 */
	private String codigoSONRIA;
	
	/**
	 * Parte de la descripcion del SONRIA que desea Buscar
	 */
	private String descripcionSONRIA;
	
		/**
	 * Código del formulario por el que se desea buscar
	 */
	private int codigoFormulario;
	
	/**
	 * Consecutivo nivel
	 */
	private String nivel;
	
	/**
	 * Campo donde se restringe la búsqueda por activos o inactivos
	 */
	private int activo;
	
	/**
	 * Unidades Uvr
	 */
	private float unidadesUvr;
	
	/**
	 * Unidades Soat
	 */
	private float unidadesSoat;
	
	private String requiereInterpretacion;
	
	private float valor;
	
	private String realizaInstitucion;
	
	private String requiereDiagnostico;
	
	private int codigoServicioPortatil;
	
	private String desServicioPortatil;
	
	private String atencionOdontologica;
	
	private int convencion;
	
	private int minutosDuracion; 
	
	/**
	 * Campo donde se restringe por que criterios se va a buscar
	 */
	private String criteriosBusqueda[];
	
	/**
	 * Campo dinámico donde se guardan los codigos Tarifarios del Sistema
	 */
	private String codigosTarifarios[];
	
	

	/**
	 * @param string
	 */
	public void setCodigoAxioma(String string)
	{
		if (string==null)
		{
			this.codigoAxioma=-1;
		}
		else
		{
			try
			{
				this.codigoAxioma=Integer.parseInt(string);
			}
			catch (NumberFormatException e)
			{
				this.codigoAxioma=-1;
			}
		}
	}


	/**
	 * Método que dice si se selecciono activo o no
	 * @return
	 */
	public boolean getEsActivo ()
	{
		if (this.activo>0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Método que dice si se selecciono pos o no
	 * @return
	 */
	public boolean getEsPos ()
	{
		if (this.codigoPos == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	

	/**
	 * @return
	 */
	public int getActivo()
	{
		return activo;
	}

	/**
	 * @return
	 */
	public int getCodigoAxioma()
	{
		return codigoAxioma;
	}

	/**
	 * @return
	 */
	public String getCodigoCups()
	{
		return codigoCups;
	}

	/**
	 * @return
	 */
	public String getCodigoISS()
	{
		return codigoISS;
	}

	/**
	 * @return
	 */
	public int getCodigoPos()
	{
		return codigoPos;
	}

	/**
	 * @return
	 */
	public String getCodigoSOAT()
	{
		return codigoSOAT;
	}

	/**
	 * @return
	 */
	public String[] getCriteriosBusqueda()
	{
		return criteriosBusqueda;
	}

	/**
	 * @return
	 */
	public String getDescripcionCups()
	{
		return descripcionCups;
	}

	/**
	 * @return
	 */
	public String getDescripcionISS()
	{
		return descripcionISS;
	}

	/**
	 * @return
	 */
	public String getDescripcionSOAT()
	{
		return descripcionSOAT;
	}

	/**
	 * @return
	 */
	public int getEspecialidad()
	{
		return especialidad;
	}

	

	/**
	 * @return
	 */
	public int getCodigoFormulario()
	{
		return codigoFormulario;
	}

	/**
	 * @return
	 */
	public String getNaturalezaServicio()
	{
		return naturalezaServicio;
	}

	/**
	 * @return
	 */
	public String getTipoServicio()
	{
		return tipoServicio;
	}

	/**
	 * @param i
	 */
	public void setActivo(int i)
	{
		activo = i;
	}

	/**
	 * @param i
	 */
	public void setCodigoAxioma(int i)
	{
		codigoAxioma = i;
	}

	/**
	 * @param string
	 */
	public void setCodigoCups(String string)
	{
		codigoCups = string;
	}

	/**
	 * @param string
	 */
	public void setCodigoISS(String string)
	{
		codigoISS = string;
	}

	/**
	 * @param i
	 */
	public void setCodigoPos(int i)
	{
		codigoPos = i;
	}

	/**
	 * @param string
	 */
	public void setCodigoSOAT(String string)
	{
		codigoSOAT = string;
	}

	/**
	 * @param strings
	 */
	public void setCriteriosBusqueda(String[] strings)
	{
		criteriosBusqueda = strings;
	}

	/**
	 * @param string
	 */
	public void setDescripcionCups(String string)
	{
		descripcionCups = string;
	}

	/**
	 * @param string
	 */
	public void setDescripcionISS(String string)
	{
		descripcionISS = string;
	}

	/**
	 * @param string
	 */
	public void setDescripcionSOAT(String string)
	{
		descripcionSOAT = string;
	}

	/**
	 * @param i
	 */
	public void setEspecialidad(int i)
	{
		especialidad = i;
	}

	

	/**
	 * @param i
	 */
	public void setCodigoFormulario(int i)
	{
		codigoFormulario = i;
	}

	/**
	 * @param string
	 */
	public void setNaturalezaServicio(String string)
	{
		naturalezaServicio = string;
	}

	/**
	 * @param string
	 */
	public void setTipoServicio(String string)
	{
		tipoServicio = string;
	}

	/**
	 * @return
	 */
	public int getSexo() {
		return sexo;
	}

	/**
	 * @param i
	 */
	public void setSexo(int i) {
		sexo = i;
	}

	public float getUnidadesUvr() {
		return unidadesUvr;
	}
	public void setUnidadesUvr(float unidadesUvr) {
		this.unidadesUvr = unidadesUvr;
	}
	/**
	 * @return Returns the grupoServicio.
	 */
	public int getGrupoServicio() {
		return grupoServicio;
	}
	/**
	 * @param grupoServicio The grupoServicio to set.
	 */
	public void setGrupoServicio(int grupoServicio) {
		this.grupoServicio = grupoServicio;
	}


	public float getUnidadesSoat() {
		return unidadesSoat;
	}


	public void setUnidadesSoat(float unidadesSoat) {
		this.unidadesSoat = unidadesSoat;
	}


	/**
	 * @return Returns the nivel.
	 */
	public String getNivel() {
		return nivel;
	}


	/**
	 * @param nivel The nivel to set.
	 */
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}


	public String getRealizaInstitucion() {
		return realizaInstitucion;
	}


	public void setRealizaInstitucion(String realizaInstitucion) {
		this.realizaInstitucion = realizaInstitucion;
	}


	public String getRequiereInterpretacion() {
		return requiereInterpretacion;
	}


	public void setRequiereInterpretacion(String requiereInterpretacion) {
		this.requiereInterpretacion = requiereInterpretacion;
	}


	public float getValor() {
		return valor;
	}


	public void setValor(float valor) {
		this.valor = valor;
	}


	/**
	 * @return the requiereDiagnostico
	 */
	public String getRequiereDiagnostico() {
		return requiereDiagnostico;
	}


	/**
	 * @param requiereDiagnostico the requiereDiagnostico to set
	 */
	public void setRequiereDiagnostico(String requiereDiagnostico) {
		this.requiereDiagnostico = requiereDiagnostico;
	}


	public int getCodigoServicioPortatil() {
		return codigoServicioPortatil;
	}


	public void setCodigoServicioPortatil(int codigoServicioPortatil) {
		this.codigoServicioPortatil = codigoServicioPortatil;
	}


	public String getDesServicioPortatil() {
		return desServicioPortatil;
	}


	public void setDesServicioPortatil(String desServicioPortatil) {
		this.desServicioPortatil = desServicioPortatil;
	}


	public String getEsPosSubsidiado() {
		return esPosSubsidiado;
	}


	public void setEsPosSubsidiado(String esPosSubsidiado) {
		this.esPosSubsidiado = esPosSubsidiado;
	}


	public String getAtencionOdontologica() {
		return atencionOdontologica;
	}


	public void setAtencionOdontologica(String atencionOdontologica) {
		this.atencionOdontologica = atencionOdontologica;
	}


	public int getConvencion() {
		return convencion;
	}


	public void setConvencion(int convencion) {
		this.convencion = convencion;
	}


	public int getMinutosDuracion() {
		return minutosDuracion;
	}


	public void setMinutosDuracion(int minutosDuracion) {
		this.minutosDuracion = minutosDuracion;
	}


	public String getCodigoSONRIA() {
		return codigoSONRIA;
	}


	public void setCodigoSONRIA(String codigoSONRIA) {
		this.codigoSONRIA = codigoSONRIA;
	}


	public String getDescripcionSONRIA() {
		return descripcionSONRIA;
	}


	public void setDescripcionSONRIA(String descripcionSONRIA) {
		this.descripcionSONRIA = descripcionSONRIA;
	}


	public String[] getCodigosTarifarios() {
		return codigosTarifarios;
	}


	public void setCodigosTarifarios(String[] codigosTarifarios) {
		this.codigosTarifarios = codigosTarifarios;
	}
	
	
}
