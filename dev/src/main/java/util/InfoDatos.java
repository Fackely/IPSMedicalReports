package util;


/**
 *	Clase	 genérica para el almacenamiento de datos que vienen dados en
 * parejas o trios. Y que son dependientes o necesarios.
 *  
 * @version 1.0, Abril 3, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 */
public class InfoDatos
{
	/**
	 * Identificador (código) del conjunto de datos. Puede ser un acrónimo o un
	 * código.
	 */
  	private String id = "";
  	private int codigo = -2; 
  	/**
  	 * Valor del dato.
  	 */
	private String value = "";
  
  	/**
  	 *  Descripcion del conjunto de datos.
  	 */
  	private String descripcion = "";
  	
  	/**
  	 *  
  	 */
  	private String descripcionInd = "";

	/**
	 * valor del dato tipo int
	 */
  	private int valueInt;
	/**
	 * valor del dato tipo double
	 */
  	private double valueDouble;
  	
  	/**
	 * Atributo que define si este elemento está
	 * activo en el sistema o no
	 */
	private boolean activo=true;  
	
  	public InfoDatos()
  	{
  	}
  
	/**
	 * Constructor para una pareja de datos
	 */
	public InfoDatos(String id) 
	{
		this.id = id;
	}
	
	/**
	 * Constructor para una pareja de datos
	 */
	public InfoDatos(String id, String value) 
	{
	    this.valueInt = ConstantesBD.codigoNuncaValido;
		this.valueDouble=ConstantesBD.codigoNuncaValido;
	    if (id!=null)
	    {
			this.id = id;
	        
	    }
		if (value!=null)
		{
			this.value = value;
		}
	}
	
	public InfoDatos(int codigo, String nombre) 
	{
		this.codigo = codigo;
		if (nombre!=null)
		{
			this.value = nombre;
		}
	}
	
	/**
	 * Constructor de un trio de datos
	 */
	public InfoDatos(String id, String value, String descripcion)
	{
		this.id = id;
		if (value!=null)
		{
			this.value = value;
		}
		if (descripcion!=null)
		{
			this.descripcion = descripcion;
		}
	}

	/**
	 * Constructor de un trio de datos, con su definición 
	 * de activo o inactivo
	 */
	public InfoDatos(String id, String value, String descripcion, boolean activo)
	{
		this.id = id;
		if (value!=null)
		{
			this.value = value;
		}
		if (descripcion!=null)
		{
			this.descripcion = descripcion;
		}
		this.activo=activo;
	}
	
	/**
	 * Constructor de un trio de datos
	 */
	public InfoDatos(int codigo, String nombre, String descripcion)
	{
		this.codigo = codigo;
		
		if (nombre!=null)
		{
			this.value = nombre;
		}
		if (descripcion!=null)
		{
			this.descripcion = descripcion;
		}
	}

	/**
	 * Constructor de un trio de datos, con su definición 
	 * de activo o inactivo
	 */
	public InfoDatos(int codigo, String nombre, String descripcion, boolean activo)
	{
		this.codigo = codigo;
		if (nombre!=null)
		{
			this.value = nombre;
		}
		if (descripcion!=null)
		{
			this.descripcion = descripcion;
		}
		this.activo=activo;
	}

	/**
	 * Constructor para dos tipos de datos,
	 * id de tipo String y un valor de tipo int
	 * @param id String
	 * @param valueInt int
	 */
	public InfoDatos(String id, int valueInt,String descripcion) 
	{
		this.valueInt = valueInt;
		this.valueDouble=ConstantesBD.codigoNuncaValido;
		this.value=ConstantesBD.codigoNuncaValido+"";
		if (id!=null)
		{
			this.id = id;
		}
		if (descripcion!=null)
		{
			this.descripcion = descripcion;
		}
	}
	
	/**
	 * Constructor para dos tipos de datos,
	 * id de tipo String y un valor de tipo double
	 * @param id String
	 * @param valueDouble double
	 */
	public InfoDatos(String id, double valueDouble,String descripcion) 
	{
		this.valueDouble = valueDouble;
		this.valueInt=ConstantesBD.codigoNuncaValido;
		this.value=ConstantesBD.codigoNuncaValido+"";
		if (id!=null)
		{
			this.id = id;
		}
		if (descripcion!=null)
		{
			this.descripcion = descripcion;
		}
	}
	
	/**
	 * Retorna la descripción del conjunto de datos.
	 * @return 		String, cadena con la descripción del dato.
	 */
	public String getDescripcion() 
	{
		return descripcion;
	}

	/**
	 * Retorna el acrónimo (id cadena) del dato.
	 * @return 		String, con el identificador del dato.
	 */
	public String getAcronimo() 
	{
		return id;
	}

	/**
	 * Retorna el acrónimo (id cadena) del dato.
	 * @return 		String, con el identificador del dato.
	 */
	@Deprecated
	public int getCodigo() 
	{
		if(this.id==null||this.id.equals("")) return this.codigo;
		return Integer.parseInt(id);
	}

	/**
	 * Returns el valor actual del dato.
	 * @return String
	 */
	public String getValue() 
	{
		return value;
	}
	public String getNombre() 
	{
		return value;
	}

	/**
	 * Asigna la descripcion del dato
	 * @param 	String, con la descripcion a ser asignada.
	 */
	public void setDescripcion(String descripcion) 
	{
	    if (descripcion!=null)
	    {
			this.descripcion = descripcion;
	    }
	}

	/**
	 * Asigna el identificador del dato, en este caso el acrónimo
	 * @param 	String, acrónimo identificador del conjunto de datos
	 */
	public void setAcronimo(String id) 
	{
	    if (id!=null)
	    {
			this.id = id;
	    }
	}
	
	/**
	 * Asigna el identificador del dato, en este caso el código
	 * @param  	int, código identificador del conjunto de datos
	 */
	 public void setCodigo(int id) 
	 {		
	 	 if(codigo == -2) 
	 	 	this.id = (new Integer(id)).toString();
	 	 else this.codigo = id;
	 }

	/**
	 * Asigna el valor del dato
	 * @param 	String, valor del dato.
	 */
	public void setValue(String value) 
	{
	    if (value!=null)
	    {
			this.value = value;
	    }
	}
	public void setNombre(String nombre) 
	{
	    if (nombre!=null)
	    {
			this.value = nombre;
	    }
	}
	
	/**
	 * @return
	 */
	public boolean isActivo()
	{
		return activo;
	}

	/**
	 * @return
	 */
	public boolean getActivo()
	{
		return activo;
	}

	/**
	 * @param b
	 */
	public void setActivo(boolean b)
	{
		activo = b;
	}

    /**
     * @return Retorna id.
     */
    public String getId() {
        return id;
    }
    /**
     * @param id Asigna id.
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return Retorna valueInt.
     */
    public int getValueInt() {
        return valueInt;
    }
    /**
     * @param valueInt Asigna valueInt.
     */
    public void setValueInt(int valueInt) {
        this.valueInt = valueInt;
    }
    /**
     * @return Retorna valueDouble.
     */
    public double getValueDouble() {
        return valueDouble;
    }
    /**
     * @param valueDouble Asigna valueDouble.
     */
    public void setValueDouble(double valueDouble) {
        this.valueDouble = valueDouble;
    }

	public String getDescripcionInd() {
		return descripcionInd;
	}

	public void setDescripcionInd(String descripcionInd) {
		this.descripcionInd = descripcionInd;
	}
}