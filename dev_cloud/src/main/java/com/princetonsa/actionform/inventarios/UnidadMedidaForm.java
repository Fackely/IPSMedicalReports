/*
 * Creado en May 31, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.actionform.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.Utilidades;

public class UnidadMedidaForm extends ValidatorForm
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(UnidadMedidaForm.class);
	
	/**
	 * Estado para el manejo del flujo de la funcionalidad
	 */
	private String estado;
	
//	---------------------------------------------------DECLARACIÓN DE LOS ATRIBUTOS-----------------------------------------------------------//
	/**
	 * Campo que guarda el código o acrónimo de la unidad de medida
	 */
	private String acronimo;
	
	/**
	 * Campo que guarda el nombre de la unidad de medida
	 */
	private String nombreUnidad;
	
	/**
	 * Campo que guarda la unidosis de la unidad de medida, es de chequeo
	 */
	private String unidosis;
	
	/**
	 * Campo que guarda si la unidad de medida está activa o no, es de chequeo
	 */
	private String activo;
	
	/**
	 * Mapa que contiene la información de las unidades de medida para la institución
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
	 * Mensaje de exito o fracaso en de la operacion
	 */
	private ResultadoBoolean mensaje = new ResultadoBoolean(false);
    
//  --------------------Pager del listado de articulos ------------------------------//
	/**
     * Número de registros por pager
     */
    private int maxPageItems;
    
    /**
     * Variables para manejar el paginador de la consulta de pacientes del centro de costo
     */
    private int index;
	private int pager;
    private int offset;
    private String linkSiguiente;
//	---------------------------------------------------FIN DECLARACIÓN DE LOS ATRIBUTOS-----------------------------------------------------------//
    
    /**
     * Método que limpia los atributos de la clase
     */
    public void reset(boolean resetearMapa)
    {
    	this.acronimo ="";
    	this.nombreUnidad="";
    	this.unidosis="";
    	this.activo="";
    	this.patronOrdenar="";
    	this.ultimoPatron="";
    	    	
    	if (resetearMapa)
	    	{
	    		this.mapa =new HashMap();
	    		this.mapa.put("numRegistros", "0");
	    		this.maxPageItems = 0;
	    	}
    }
    
    /**
     * Método encargado de resetear el mensaje
     * Ésto se utiliza para que cuando entre a la funcionalidad no se quede en pantalla el mensaje de "PROCESO REALIZADO CON ÉXITO !!! "
     * y de esta manera sólo se muestre cuando se haga una Inserción, Modificación o Eliminación
     */
    public void resetMensaje()
	{
		this.mensaje = new ResultadoBoolean(false);
	}
    
    /**
	 * Método que elimina un registro del mapa, de acuerdo
	 * al indice del registro a eliminar, realizando el respectivo desplazamiento
	 *
	 */
	public void eliminarRegistroMapa()
	{
		int numRegistros=Integer.parseInt(mapa.get("numRegistros")+"");
		int indiceEliminado=Integer.parseInt(mapa.get("indiceRegEliminar")+"");
		
		for(int i=indiceEliminado; i<numRegistros-1; i++)
		{
			String acronimo=mapa.get("acronimo_"+(i+1))+"";
			String acronimoAnt=mapa.get("acronimoant_"+(i+1))+"";
			String unidad=mapa.get("unidad_"+(i+1))+"";
			String unidadAnt=mapa.get("unidadant_"+(i+1))+"";
			String unidosis=mapa.get("unidosis_"+(i+1))+"";
			String unidosisAnt=mapa.get("unidosisant_"+(i+1))+"";
			String activo=mapa.get("activo_"+(i+1))+"";
			String activoAnt=mapa.get("activoant_"+(i+1))+"";
			String estaGrabado=mapa.get("esta_grabado_"+(i+1))+"";
			
			mapa.put("acronimo_"+i, acronimo);
			mapa.put("acronimoant_"+i, acronimoAnt);
			mapa.put("unidad_"+i, unidad);
			mapa.put("unidadant_"+i, unidadAnt);
			mapa.put("unidosis_"+i, unidosis);
			mapa.put("unidosisant_"+i, unidosisAnt);
			mapa.put("activo_"+i, activo);
			mapa.put("activoant_"+i, activoAnt);
			mapa.put("esta_grabado_"+i, estaGrabado);
		}
		//---------El último registro se coloca en null----------------//
		mapa.put("acronimo_"+(numRegistros-1), null);
		mapa.put("acronimoant_"+(numRegistros-1), null);
		mapa.put("unidad_"+(numRegistros-1), null);
		mapa.put("unidadant_"+(numRegistros-1), null);
		mapa.put("unidosis_"+(numRegistros-1), null);
		mapa.put("unidosisant_"+(numRegistros-1), null);
		mapa.put("activo_"+(numRegistros-1), null);
		mapa.put("activoant_"+(numRegistros-1), null);
		mapa.put("esta_grabado_"+(numRegistros-1), null);
		
		//---------Se decrementa en 1 el número de registros del mapa ---------//
		mapa.put("numRegistros", numRegistros-1+"");
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
		//------------Se verifica que el código o acrónimo de la unidad de medidad no se repita -----//
		if(estado.equals("agregarRegistro"))
			{
			if(this.getMapa() != null)
				{
					boolean error=false;
					//-----Se verifica que no esté vació el acrónimo ---------//
					if (!UtilidadCadena.noEsVacio(this.acronimo))
						{
						error=true;
						errores.add("Acronimo Requerido",  new ActionMessage("errors.required","El Código o acrónimo"));
						}
					
					//-----Se verifica que no esté vació el acrónimo ---------//
					if (!UtilidadCadena.noEsVacio(this.nombreUnidad))
						{
						error=true;
						errores.add("Unidad Requerida",  new ActionMessage("errors.required","La Unidad"));
						}
				
					//--------Si no hay errores se verifica que no esté repetido ----------//
					if (!error)
						{
						int numRegistros=Integer.parseInt(this.getMapa("numRegistros")+"");
						
						//--------Se verifica que el la nueva unidad de medida no se repita ----------//
						for (int i=0; i<numRegistros; i++)
							{
								String codigoAcronimo=this.getMapa("acronimo_"+i)+"";
								
								if (codigoAcronimo.trim().equals(acronimo.trim()))
									{
									errores.add("Unidad de Medida Repetida",  new ActionMessage("error.inventarios.unidadMedidaRepetida", codigoAcronimo));
									break;
									}
							}//for
						}//if no hay error
				}//if mapa!=null
			}//if estado agregarRegistro
		
		//------------Se verifica que el código o acrónimo de la unidad de medidad no se repita -----//
		if(estado.equals("guardar"))
			{
			if(this.getMapa() != null)
				{
				int numRegistros=Integer.parseInt(this.getMapa("numRegistros")+"");
				
				//--------Se verifica que la unidad de medida no se repita ----------//
				String acronimosMostrados="";
				for (int i=0; i<numRegistros; i++)
					{
					String codigoAcronimo=this.getMapa("acronimo_"+i)+"";
					int cont=0;
					for (int j=0; j<numRegistros; j++)
						{
						String codigoAcronimo2=this.getMapa("acronimo_"+j)+"";
						
						if (codigoAcronimo.trim().equals(codigoAcronimo2.trim()))
							{
							cont++;
							
								if (cont > 1 && acronimosMostrados.indexOf(codigoAcronimo)==-1)
									{
									errores.add("Unidad de Medida Repetida",  new ActionMessage("error.inventarios.unidadMedidaRepetida", codigoAcronimo));
																		
									//-----Se agrega al vector de codigos acrónimos mostrados para indicar que ya se mostró el error-------//
									if (acronimosMostrados=="")
									{
										acronimosMostrados=codigoAcronimo;
									}
									else
									{
										acronimosMostrados+=ConstantesBD.separadorTags+codigoAcronimo;
									}
									break;
									}
							}
						
						}
					}//for
				}
			}//if estado es guardar
		
		if (estado.equals("eliminarUnidadMedida"))
			{
			Connection con=null;
			try
			{
				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo abrir la conexión"+e.toString());
			}
			
			int indiceEliminado=Integer.parseInt(this.getMapa("indiceRegEliminar")+"");
			String acronimoEliminar=this.getMapa("acronimo_"+indiceEliminado)+"";
			
			//--------Se verifica que la unidad de medida no se encuentre relacionada en la tabla artículos --------//
			int nroReg=Utilidades.nroRegistrosConsulta(con, "SELECT unidad_medida FROM articulo WHERE unidad_medida=\'"+acronimoEliminar+"\'");
			
			
			//------Si nroReg es mayor que 1 entonces ya está referenciado en artículos ---------//
			if (nroReg > 0)
				{
				errores.add("Unidad de Medida Imposible Eliminar",  new ActionMessage("error.inventarios.noSePuedeEliminarUnidadMedida", acronimoEliminar));
				}
			
			try
			{
				UtilidadBD.cerrarConexion(con);
			}
			catch (SQLException e)
			{
				logger.warn("Error al cerrar la conexión"+e.toString());
			}
			
			}//if estado eliminarUnidadMedida
		return errores;
	}
    
//  ---------------------------------------- SETS Y GETS ---------------------------------------------------//
    
	/**
	 * @return Retorna the acronimo.
	 */
	public String getAcronimo()
	{
		return acronimo;
	}
	/**
	 * @param acronimo The acronimo to set.
	 */
	public void setAcronimo(String acronimo)
	{
		this.acronimo = acronimo;
	}
	/**
	 * @return Retorna the activo.
	 */
	public String getActivo()
	{
		return activo;
	}
	/**
	 * @param activo The activo to set.
	 */
	public void setActivo(String activo)
	{
		this.activo = activo;
	}
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
	 * @return Retorna the nombreUnidad.
	 */
	public String getNombreUnidad()
	{
		return nombreUnidad;
	}
	/**
	 * @param nombreUnidad The nombreUnidad to set.
	 */
	public void setNombreUnidad(String nombreUnidad)
	{
		this.nombreUnidad = nombreUnidad;
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
	 * @return Retorna the unidosis.
	 */
	public String getUnidosis()
	{
		return unidosis;
	}
	/**
	 * @param unidosis The unidosis to set.
	 */
	public void setUnidosis(String unidosis)
	{
		this.unidosis = unidosis;
	}

	/**
	 * @return Retorna the index.
	 */
	public int getIndex()
	{
		return index;
	}

	/**
	 * @param index The index to set.
	 */
	public void setIndex(int index)
	{
		this.index = index;
	}

	/**
	 * @return Retorna the linkSiguiente.
	 */
	public String getLinkSiguiente()
	{
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente)
	{
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return Retorna the maxPageItems.
	 */
	public int getMaxPageItems()
	{
		return maxPageItems;
	}

	/**
	 * @param maxPageItems The maxPageItems to set.
	 */
	public void setMaxPageItems(int maxPageItems)
	{
		this.maxPageItems = maxPageItems;
	}

	/**
	 * @return Retorna the offset.
	 */
	public int getOffset()
	{
		return offset;
	}

	/**
	 * @param offset The offset to set.
	 */
	public void setOffset(int offset)
	{
		this.offset = offset;
	}

	/**
	 * @return Retorna the pager.
	 */
	public int getPager()
	{
		return pager;
	}

	/**
	 * @param pager The pager to set.
	 */
	public void setPager(int pager)
	{
		this.pager = pager;
	}

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}
        
}
