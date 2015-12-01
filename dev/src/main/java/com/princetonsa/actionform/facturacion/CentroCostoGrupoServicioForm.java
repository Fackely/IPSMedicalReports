/*
 * Creado en May 10, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.actionform.facturacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;

public class CentroCostoGrupoServicioForm extends ValidatorForm
{
	/**
	 * Estado para el manejo del flujo de la funcionalidad
	 */
	private String estado;
	
//	---------------------------------------------------DECLARACIÓN DE LOS ATRIBUTOS-----------------------------------------------------------//
	/**
	 * Campo que guarda el centro de atención seleccionado
	 */
	private int centroAtencion;
	
	/**
	 * Campo que guarda el código del grupo de servicio
	 */
	private int codigoGrupoServicio;
	
	/**
	 * Campo que guarda el Nombre del grupo del servicio
	 */
	private String nombreGrupoServicio;
	
	/**
	 * Campo que guarda el código del centro de costo
	 */
	private int codigoCentroCosto;
	
	/**
	 * Campo que guarda el identificador del centro de costo
	 */
	private String identificador;
	
	/**
	 * Campo que guarda el nombre del centro de costo
	 */
	private String nombreCentroCosto;
	
	/**
	 * Campo que guarda el código del centro de atención asociado al centro de costo
	 */
	private String codigoCentroAtencion;
	
	/**
	 *Campo que guarda el nombre del centro de atención asociado al centro de costo 
	 */
	private String nombreCentroAtencion;

	/**
	 * Mapa que contiene la información de los centros de costo x grupos servicios
	 */
	private HashMap mapa;
	
	 /**
     * Almacena el indice por el cual 
     * se va ordenar el HashMap
     */
    private String patronOrdenar;
    
    /**
     * Almacena el ultimo indice por el 
     * cual se ordeno el HashMap
     */
    private String ultimoPatron;
    
    
    /**
     * Consecutivo para la tarea 61826
     */
    private int frmConsecutivo;
    private ResultadoBoolean mensaje = new ResultadoBoolean(false);
	
//	---------------------------------------------------FIN DE LA DECLARACIÓN DE LOS ATRIBUTOS-----------------------------------------------//
	
	/**
	 * Método para limpiar la clase
	 */
	public void reset()
	{
		String tmp = ""; 
		this.centroAtencion = ConstantesBD.codigoNuncaValido; //-1
		this.mapa=new HashMap();
		this.nombreGrupoServicio="";
		this.nombreCentroCosto="";
		this.nombreCentroAtencion="";
		this.codigoGrupoServicio=0;
		this.codigoCentroCosto=0;
		this.codigoCentroAtencion="";
		this.identificador = "";
		this.patronOrdenar ="";
		this.ultimoPatron = "";
		//this.frmConsecutivo = 0; //ConstantesBD.codigoNuncaValido;	//61826 lo habia puesto en -1 0
	}
	
	
	
	/**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found.  If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 * error messages.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		//------------Se verifica que el grupo de servicio y el centro de costo no se repita para el centro de atención -----//
		if(estado.equals("agregarRegistro"))
			{
				if(this.getMapa() != null)
					{
						int numRegistros=(Integer)this.getMapa("numRegistros");
						
						for (int i=0; i<numRegistros; i++)
							{
								int codGrupoServicio=Integer.parseInt(this.getMapa("codigo_gservicio_"+i)+"");
								int codCentroCosto=Integer.parseInt(this.getMapa("codigo_ccosto_"+i)+"");
								if (codGrupoServicio == this.codigoGrupoServicio && codCentroCosto == this.codigoCentroCosto)
									{
									errores.add("CCosto x GrupoServicio Repetido",  new ActionMessage("error.centrosCostoGrupoServicio.centroCostoGrupoServicioRepetido"));
									break;
									}
							}
					}
			}
		return errores;
	}
	
	/**
	 * Método que elimina un registro del mapa, de acuerdo
	 * al indice del registro a eliminar, realizando el respectivo desplazamiento
	 *
	 */
	public void eliminarRegistroMapa()
	{
		int numRegistros=(Integer)mapa.get("numRegistros");
		int indiceEliminado=Integer.parseInt(mapa.get("indiceRegEliminar")+"");
		
		for(int i=indiceEliminado; i<numRegistros-1; i++)
		{
			int codGrupoServicio=Integer.parseInt(mapa.get("codigo_gservicio_"+(i+1))+"");
			int codCentroCosto=Integer.parseInt(mapa.get("codigo_ccosto_"+(i+1))+"");
			String codCentroAtencion=(String)mapa.get("codigo_catencion_"+(i+1));
			String nomGrupoServicio=(String)mapa.get("nombre_gservicio_"+(i+1));
			String nomCentroCosto=(String)mapa.get("nombre_ccosto_"+(i+1));
			String nomCentroAtencion=(String)mapa.get("nombre_catencion_"+(i+1));
			String estaGrabado=(String)mapa.get("esta_grabado_"+(i+1));

			int frmConsecutivo=Integer.parseInt(mapa.get("consecutivo_"+(i+1))+"");	//61826
			
			mapa.put("codigo_gservicio_"+i, codGrupoServicio);
			mapa.put("codigo_ccosto_"+i, codCentroCosto);
			mapa.put("codigo_catencion_"+i, codCentroAtencion);
			mapa.put("nombre_gservicio_"+i, nomGrupoServicio);
			mapa.put("nombre_ccosto_"+i, nomCentroCosto);
			mapa.put("nombre_catencion_"+i, nomCentroAtencion);
			mapa.put("esta_grabado_"+i, estaGrabado);

			mapa.put("consecutivo_"+i, frmConsecutivo);	//61826
			
		}
		//---------El último registro se coloca en null----------------//
		mapa.put("codigo_gservicio_"+(numRegistros-1), null);
		mapa.put("codigo_ccosto_"+(numRegistros-1), null);
		mapa.put("codigo_catencion_"+(numRegistros-1), null);
		mapa.put("nombre_gservicio_"+(numRegistros-1), null);
		mapa.put("nombre_ccosto_"+(numRegistros-1), null);
		mapa.put("nombre_catencion_"+(numRegistros-1), null);
		mapa.put("esta_grabado_"+(numRegistros-1), null);

		mapa.put("consecutivo_"+(numRegistros-1), null);	//61826
		
		//---------Se decrementa en 1 el número de registros del mapa ---------//
		mapa.put("numRegistros", numRegistros-1);
	}

	//---------------------------------------- SETS Y GETS ---------------------------------------------------//
	/**
	 * @return Retorna the estado.
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * @return Retorna the centroAtencion.
	 */
	public int getCentroAtencion()
	{
		return centroAtencion;
	}

	/**
	 * @param centroAtencion The centroAtencion to set.
	 */
	public void setCentroAtencion(int centroAtencion)
	{
		this.centroAtencion = centroAtencion;
	}
	
	/**
	 * @return Retorna the mapa.
	 */
	public HashMap getMapa()
	{
		return mapa;
	}

	/**
	 * @param mapa The mapa to set.
	 */
	public void setMapa(HashMap mapa)
	{
		this.mapa = mapa;
	}
	
	/**
	 * @return Retorna mapa.
	 */
	public Object getMapa(Object key) {
		return mapa.get(key);
	}
	/**
	 * @param Asigna dato.
	 */
	public void setMapa(Object key, Object dato) {
		this.mapa.put(key, dato);
	}

	/**
	 * @return Retorna the codigoCentroAtencion.
	 */
	public String  getCodigoCentroAtencion()
	{
		return codigoCentroAtencion;
	}

	/**
	 * @param codigoCentroAtencion The codigoCentroAtencion to set.
	 */
	public void setCodigoCentroAtencion(String codigoCentroAtencion)
	{
		this.codigoCentroAtencion = codigoCentroAtencion;
	}

	/**
	 * @return Retorna the codigoCentroCosto.
	 */
	public int getCodigoCentroCosto()
	{
		return codigoCentroCosto;
	}

	/**
	 * @param codigoCentroCosto The codigoCentroCosto to set.
	 */
	public void setCodigoCentroCosto(int codigoCentroCosto)
	{
		this.codigoCentroCosto = codigoCentroCosto;
	}

	/**
	 * @return Retorna the codigoGrupoServicio.
	 */
	public int getCodigoGrupoServicio()
	{
		return codigoGrupoServicio;
	}

	/**
	 * @param codigoGrupoServicio The codigoGrupoServicio to set.
	 */
	public void setCodigoGrupoServicio(int codigoGrupoServicio)
	{
		this.codigoGrupoServicio = codigoGrupoServicio;
	}

	/**
	 * @return Retorna the nombreCentroAtencion.
	 */
	public String getNombreCentroAtencion()
	{
		return nombreCentroAtencion;
	}

	/**
	 * @param nombreCentroAtencion The nombreCentroAtencion to set.
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion)
	{
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	/**
	 * @return Retorna the nombreCentroCosto.
	 */
	public String getNombreCentroCosto()
	{
		return nombreCentroCosto;
	}

	/**
	 * @param nombreCentroCosto The nombreCentroCosto to set.
	 */
	public void setNombreCentroCosto(String nombreCentroCosto)
	{
		this.nombreCentroCosto = nombreCentroCosto;
	}

	/**
	 * @return Retorna the nombreGrupoServicio.
	 */
	public String getNombreGrupoServicio()
	{
		return nombreGrupoServicio;
	}

	/**
	 * @param nombreGrupoServicio The nombreGrupoServicio to set.
	 */
	public void setNombreGrupoServicio(String nombreGrupoServicio)
	{
		this.nombreGrupoServicio = nombreGrupoServicio;
	}

	/**
	 * @return Retorna the patronOrdenar.
	 */
	public String getPatronOrdenar()
	{
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar The patronOrdenar to set.
	 */
	public void setPatronOrdenar(String patronOrdenar)
	{
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return Retorna the ultimoPatron.
	 */
	public String getUltimoPatron()
	{
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron The ultimoPatron to set.
	 */
	public void setUltimoPatron(String ultimoPatron)
	{
		this.ultimoPatron = ultimoPatron;
	}

	/**
	 * @return the identificador
	 */
	public String getIdentificador() {
		return identificador;
	}

	/**
	 * @param identificador the identificador to set
	 */
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}


	
	/**	 * @return the frmConsecutivo	 */
	public int getFrmConsecutivo() {	return frmConsecutivo;	}
	public void setFrmConsecutivo(int frmConsecutivo) {	this.frmConsecutivo = frmConsecutivo;	}



	/**	 * @return the mensaje	 */
	public ResultadoBoolean getMensaje() {	return mensaje;	}
	public void setMensaje(ResultadoBoolean mensaje) {	this.mensaje = mensaje;	}

	
	
}